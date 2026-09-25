// midp.cpp -- implementation of the a1 MIDP/Nokia shim (see midp.h).
// Graphics/Image/RecordStore/resource-loading are byte-for-byte the same
// logic as ../src/midp.cpp (generic MIDP, not build-specific); only
// SoundPlayer (6 cues, wav/mid mix) and platform:: (176x220 canvas, RMS
// path) differ for a1.
#include <cmath>
#include "midp.h"

#define WIN32_LEAN_AND_MEAN
#define NOMINMAX
#include <windows.h>
#include <objidl.h>  // IStream/PROPID -- WIN32_LEAN_AND_MEAN drops these from
                     // windows.h, but Gdiplus.h needs them.
#include <gdiplus.h>
#include <mmsystem.h>
#include <shlobj.h>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <algorithm>
#include <atomic>
#include <chrono>
#include <condition_variable>
#include <deque>
#include <mutex>
#include <thread>

#pragma comment(lib, "gdiplus.lib")
#pragma comment(lib, "winmm.lib")
#pragma comment(lib, "user32.lib")
#pragma comment(lib, "gdi32.lib")
#pragma comment(lib, "shell32.lib")

// ---- java.lang helpers ---------------------------------------------------

int length(const String& s) { return (int)s.size(); }
char charAt(const String& s, int i) { return (char)(uint8_t)s[(size_t)i]; }
String substring(const String& s, int b, int e) {
  return s.substr((size_t)b, (size_t)(e - b));
}
String trim(const String& s) {
  size_t b = s.find_first_not_of(" \t\r\n");
  if (b == String::npos) return "";
  size_t e = s.find_last_not_of(" \t\r\n");
  return s.substr(b, e - b + 1);
}
String toUpperCase(const String& s) {
  String r = s;
  for (auto& c : r)
    if (c >= 'a' && c <= 'z') c -= 32;
  return r;
}
bool equals(const String& a, const String& b) { return a == b; }
String valueOf(int v) { char b[16]; sprintf_s(b, "%d", v); return b; }
int indexOf(const String& s, char c) {
  size_t p = s.find(c);
  return p == String::npos ? -1 : (int)p;
}

jlong currentTimeMillis() { return (jlong)GetTickCount64(); }
void arraycopy(const void* src, int srcPos, void* dst, int dstPos, int len) {
  memmove((uint8_t*)dst + dstPos, (const uint8_t*)src + srcPos, (size_t)len);
}
void sleepMs(int ms) { if (ms > 0) Sleep((DWORD)ms); }

// ---- resources -----------------------------------------------------------

static String g_dataDir;

static bool fileExists(const String& p) {
  return GetFileAttributesA(p.c_str()) != INVALID_FILE_ATTRIBUTES;
}

static String moduleDir() {
  char buf[MAX_PATH];
  GetModuleFileNameA(nullptr, buf, MAX_PATH);
  String p(buf);
  size_t s = p.find_last_of("\\/");
  return s == String::npos ? "." : p.substr(0, s);
}

bool setDataDir(const String& dir) {
  // mapData.txt is a1-only (unobfuscated asset name, see ROADMAP.md's
  // "The target") -- a reliable marker that this is the a1 extraction, not
  // the legacy build's extracted/.
  if (!fileExists(dir + "/mapData.txt")) return false;
  g_dataDir = dir;
  return true;
}

String getDataDir() { return g_dataDir; }

String resourcePath(const String& path) {
  if (g_dataDir.empty()) return "";
  // "/name" -> "<dataDir>/name" (Java resource paths are absolute)
  String rel = (path.size() > 0 && path[0] == '/') ? path.substr(1) : path;
  return g_dataDir + "/" + rel;
}

bool resourceExists(const String& path) {
  return fileExists(resourcePath(path));
}

ByteArray loadResource(const String& path) {
  ByteArray out;
  HANDLE f = CreateFileA(resourcePath(path).c_str(), GENERIC_READ,
                         FILE_SHARE_READ, nullptr, OPEN_EXISTING,
                         FILE_ATTRIBUTE_NORMAL, nullptr);
  if (f == INVALID_HANDLE_VALUE) return out;
  LARGE_INTEGER sz;
  if (GetFileSizeEx(f, &sz) && sz.QuadPart > 0 && sz.QuadPart < 64 * 1024 * 1024) {
    out.resize((size_t)sz.QuadPart);
    DWORD got = 0;
    if (!ReadFile(f, out.data(), (DWORD)out.size(), &got, nullptr) || got != out.size())
      out.clear();
  }
  CloseHandle(f);
  return out;
}

// ---- Image ---------------------------------------------------------------

Image* Image::createImage(const String& path) {
  String p = resourcePath(path);
  if (p.empty()) return nullptr;
  Gdiplus::GdiplusStartupInput si;
  static ULONG_PTR token = 0;
  if (token == 0) Gdiplus::GdiplusStartup(&token, &si, nullptr);
  std::wstring wp(p.begin(), p.end());
  Gdiplus::Bitmap* bmp = Gdiplus::Bitmap::FromFile(wp.c_str());
  if (!bmp || bmp->GetLastStatus() != Gdiplus::Ok) { delete bmp; return nullptr; }
  Image* img = new Image();
  img->w = (int)bmp->GetWidth();
  img->h = (int)bmp->GetHeight();
  Gdiplus::Rect rect(0, 0, img->w, img->h);
  Gdiplus::BitmapData data;
  if (bmp->LockBits(&rect, Gdiplus::ImageLockModeRead, PixelFormat32bppARGB,
                    &data) == Gdiplus::Ok) {
    img->px.resize((size_t)img->w * img->h);
    for (int y = 0; y < img->h; y++) {
      const uint32_t* src = (const uint32_t*)((uint8_t*)data.Scan0 + (size_t)y * data.Stride);
      uint32_t* dst = &img->px[(size_t)y * img->w];
      for (int x = 0; x < img->w; x++) {
        uint32_t c = src[x];
        dst[x] = ((c & 0xFF000000u) << 0) |  // already 0xAARRGGBB
                  (c & 0x00FFFFFFu);
      }
    }
    bmp->UnlockBits(&data);
  }
  delete bmp;
  return img;
}

Image* Image::createImage(int width, int height) {
  Image* img = new Image();
  img->w = width;
  img->h = height;
  img->px.assign((size_t)width * height, 0xFF000000u);
  return img;
}

// ---- Graphics ------------------------------------------------------------

static inline uint32_t rgbToNative(int rgb) {
  uint32_t r = (rgb >> 16) & 0xFF, g = (rgb >> 8) & 0xFF, b = rgb & 0xFF;
  return 0xFF000000u | (r << 16) | (g << 8) | b;
}

void Graphics::setClip(int x, int y, int w, int h) {
  x += transX_; y += transY_;
  if (!target_) { clipW_ = -1; return; }
  if (x < 0) { w += x; x = 0; }
  if (y < 0) { h += y; y = 0; }
  if (x + w > target_->w) w = target_->w - x;
  if (y + h > target_->h) h = target_->h - y;
  if (w < 0) w = 0;
  if (h < 0) h = 0;
  clipX_ = x; clipY_ = y; clipW_ = w; clipH_ = h;
}

void Graphics::clipRect(int x, int y, int w, int h) {
  x += transX_; y += transY_;
  if (clipW_ == -1) { setClip(x - transX_, y - transY_, w, h); return; }
  int x0 = std::max(x, clipX_), y0 = std::max(y, clipY_);
  int x1 = std::min(x + w, clipX_ + clipW_), y1 = std::min(y + h, clipY_ + clipH_);
  clipX_ = x0; clipY_ = y0;
  clipW_ = x1 > x0 ? x1 - x0 : 0;
  clipH_ = y1 > y0 ? y1 - y0 : 0;
}

void Graphics::translate(int dx, int dy) { transX_ += dx; transY_ += dy; }

void Graphics::fillArc(int x, int y, int w, int h, int, int) {
  if (w <= 0 || h <= 0) return;
  double a = w / 2.0, b = h / 2.0;
  for (int yy = 0; yy < h; yy++) {
    double dy = (yy + 0.5 - b) / b;
    double t = 1.0 - dy * dy;
    if (t <= 0) continue;
    int half = (int)(a * std::sqrt(t) + 0.5);
    int cx = x + (int)a;
    fillRect(cx - half, y + yy, 2 * half + (w & 1 ? 1 : 0), 1);
  }
}

void Graphics::fillRect(int x, int y, int w, int h) {
  if (!target_ || w <= 0 || h <= 0) return;
  x += transX_; y += transY_;
  uint32_t c = rgbToNative(color_);
  int x0 = std::max(x, clipX_), x1 = std::min(x + w, clipX_ + clipW_);
  int y0 = std::max(y, clipY_), y1 = std::min(y + h, clipY_ + clipH_);
  if (clipW_ == -1) { x0 = std::max(x, 0); x1 = std::min(x + w, target_->w);
                      y0 = std::max(y, 0); y1 = std::min(y + h, target_->h); }
  for (int yy = y0; yy < y1; yy++) {
    uint32_t* row = &target_->px[(size_t)yy * target_->w];
    for (int xx = x0; xx < x1; xx++) row[xx] = c;
  }
}

void Graphics::drawRect(int x, int y, int w, int h) {
  fillRect(x, y, w + 1, 1);
  if (h > 0) fillRect(x, y + h, w + 1, 1);
  if (h > 1) {
    fillRect(x, y + 1, 1, h - 1);
    if (w > 0) fillRect(x + w, y + 1, 1, h - 1);
  }
}

void Graphics::drawLine(int x1, int y1, int x2, int y2) {
  int dx = abs(x2 - x1), dy = abs(y2 - y1);
  int sx = x1 < x2 ? 1 : -1, sy = y1 < y2 ? 1 : -1;
  int err = dx - dy;
  int x = x1, y = y1;
  while (true) {
    fillRect(x, y, 1, 1);
    if (x == x2 && y == y2) break;
    int e2 = 2 * err;
    if (e2 > -dy) { err -= dy; x += sx; }
    if (e2 < dx) { err += dx; y += sy; }
  }
}

void Graphics::fillRoundRect(int x, int y, int w, int h, int arcWidth, int arcHeight) {
  (void)arcWidth; (void)arcHeight;
  fillRect(x, y, w, h);
}

void Graphics::drawImage(Image* img, int x, int y, int anchor) {
  if (!img || img->isNull()) return;
  int w = img->w, h = img->h;
  if ((anchor & RIGHT) != 0) x -= w;
  else if ((anchor & HCENTER) != 0) x -= w / 2;
  if ((anchor & BOTTOM) != 0) y -= h;
  else if ((anchor & VCENTER) != 0) y -= h / 2;
  x += transX_; y += transY_;
  int x0 = std::max(x, clipX_), y0 = std::max(y, clipY_);
  int x1 = std::min(x + w, clipX_ + clipW_), y1 = std::min(y + h, clipY_ + clipH_);
  if (clipW_ == -1) { x0 = std::max(x, 0); y0 = std::max(y, 0);
                      x1 = std::min(x + w, target_->w); y1 = std::min(y + h, target_->h); }
  if (x0 >= x1 || y0 >= y1) return;
  for (int yy = y0; yy < y1; yy++) {
    uint32_t* drow = &target_->px[(size_t)yy * target_->w];
    const uint32_t* srow = &img->px[(size_t)(yy - y) * w + (size_t)(x0 - x)];
    for (int xx = x0; xx < x1; xx++, srow++) {
      uint32_t c = *srow, a = (c >> 24) & 0xFF;
      if (a == 0xFF) drow[xx] = c;
      else if (a != 0) {
        uint32_t d = drow[xx], da = 255 - a;
        uint32_t r = (((c >> 16) & 0xFF) * a + ((d >> 16) & 0xFF) * da) / 255;
        uint32_t g = (((c >> 8) & 0xFF) * a + ((d >> 8) & 0xFF) * da) / 255;
        uint32_t b = ((c & 0xFF) * a + (d & 0xFF) * da) / 255;
        drow[xx] = 0xFF000000u | (r << 16) | (g << 8) | b;
      }
    }
  }
}

// ---- Nokia DirectGraphics -----------------------------------------------

void Graphics::drawImageManip(Image* img, int x, int y, int anchor, int manipulation) {
  if (!img || img->isNull()) return;
  // Nokia DirectGraphics codes: rotation is counterclockwise for 90 and clockwise for 270
  // (matches the offsets the game uses), then FLIP_H(8192)/FLIP_V(16384) apply to the result.
  bool nokia = manipulation >= 90;
  int nRot = 0;
  bool nFh = false, nFv = false;
  if (nokia) {
    nRot = manipulation & 0x1FFF;
    nFh = (manipulation & 8192) != 0;
    nFv = (manipulation & 16384) != 0;
    manipulation = 0;
  }
  int w = img->w, h = img->h;
  bool rot = nokia ? (nRot == 90 || nRot == 270)
                   : (manipulation >= 1 && manipulation <= 7 && manipulation != 4 && manipulation != 6);
  int dw = w, dh = h;
  if (rot) { dw = h; dh = w; }
  if ((anchor & RIGHT) != 0) x -= dw;
  else if ((anchor & HCENTER) != 0) x -= dw / 2;
  if ((anchor & BOTTOM) != 0) y -= dh;
  else if ((anchor & VCENTER) != 0) y -= dh / 2;
  x += transX_; y += transY_;
  int X0 = std::max(x, clipX_), Y0 = std::max(y, clipY_);
  int X1 = std::min(x + dw, clipX_ + clipW_), Y1 = std::min(y + dh, clipY_ + clipH_);
  if (clipW_ == -1) { X0 = std::max(x, 0); Y0 = std::max(y, 0);
                      X1 = std::min(x + dw, target_->w); Y1 = std::min(y + dh, target_->h); }
  for (int dy = Y0; dy < Y1; dy++) {
    int dyy = dy - y;
    for (int dx = X0; dx < X1; dx++) {
      int dxx = dx - x;
      int sx = dxx, sy = dyy;
      if (nokia) {
        int rx = nFh ? dw - 1 - dxx : dxx, ry = nFv ? dh - 1 - dyy : dyy;
        switch (nRot) {
          case 90:  sx = w - 1 - ry; sy = rx; break;
          case 180: sx = w - 1 - rx; sy = h - 1 - ry; break;
          case 270: sx = ry; sy = h - 1 - rx; break;
          default:  sx = rx; sy = ry; break;
        }
      } else switch (manipulation) {
        case MANIP_ROT_90:        sx = dyy; sy = h - 1 - dxx; break;
        case MANIP_ROT_180:      sx = w - 1 - dxx; sy = h - 1 - dyy; break;
        case MANIP_ROT_270:      sx = w - 1 - dyy; sy = dxx; break;
        case MANIP_MIRROR:       sx = w - 1 - dxx; break;
        case MANIP_MIRROR_ROT_90:  sx = w - 1 - dyy; sy = h - 1 - dxx; break;
        case MANIP_MIRROR_ROT_180: sx = dxx; sy = h - 1 - dyy; break;
        case MANIP_MIRROR_ROT_270: sx = dyy; sy = dxx; break;
        default: break;
      }
      if (sx < 0 || sy < 0 || sx >= w || sy >= h) continue;
      uint32_t c = img->px[(size_t)sy * w + sx], a = (c >> 24) & 0xFF;
      uint32_t* d = &target_->px[(size_t)dy * target_->w + dx];
      if (a == 0xFF) *d = c;
      else if (a != 0) {
        uint32_t base = *d, da = 255 - a;
        uint32_t r = (((c >> 16) & 0xFF) * a + ((base >> 16) & 0xFF) * da) / 255;
        uint32_t g = (((c >> 8) & 0xFF) * a + ((base >> 8) & 0xFF) * da) / 255;
        uint32_t b = ((c & 0xFF) * a + (base & 0xFF) * da) / 255;
        *d = 0xFF000000u | (r << 16) | (g << 8) | b;
      }
    }
  }
}

void Graphics::fillPolygon(const int* xs, const int* ys, int n, int argb) {
  for (int k2 = 1; k2 + 1 < n; k2++) fillTriangle(xs[0], ys[0], xs[k2], ys[k2], xs[k2 + 1], ys[k2 + 1], argb);
}

void Graphics::drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int argb) {
  int saved = color_;
  color_ = argb & 0xFFFFFF;
  drawLine(x1, y1, x2, y2);
  drawLine(x2, y2, x3, y3);
  drawLine(x3, y3, x1, y1);
  color_ = saved;
}

void Graphics::fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int argb) {
  if (!target_) return;
  int a = (argb >> 24) & 0xFF;
  int rgb = argb & 0xFFFFFF;
  x1 += transX_; y1 += transY_; x2 += transX_; y2 += transY_; x3 += transX_; y3 += transY_;
  int minX = std::min(x1, std::min(x2, x3)), maxX = std::max(x1, std::max(x2, x3));
  int minY = std::min(y1, std::min(y2, y3)), maxY = std::max(y1, std::max(y2, y3));
  int area2 = (x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1);
  if (area2 == 0) return;
  int X0 = std::max(minX, clipX_), Y0 = std::max(minY, clipY_);
  int X1 = std::max(std::min(maxX + 1, clipX_ + clipW_), X0);
  int Y1 = std::max(std::min(maxY + 1, clipY_ + clipH_), Y0);
  if (clipW_ == -1) { X0 = std::max(minX, 0); Y0 = std::max(minY, 0);
                      X1 = std::min(maxX + 1, target_->w); Y1 = std::min(maxY + 1, target_->h); }
  uint32_t r = (rgb >> 16) & 0xFF, g = (rgb >> 8) & 0xFF, b = rgb & 0xFF;
  for (int py = Y0; py < Y1; py++) {
    for (int px = X0; px < X1; px++) {
      int w0 = (x2 - px) * (y3 - py) - (x3 - px) * (y2 - py);
      int w1 = (x3 - px) * (y1 - py) - (x1 - px) * (y3 - py);
      int w2 = (x1 - px) * (y2 - py) - (x2 - px) * (y1 - py);
      if (!((w0 >= 0 && w1 >= 0 && w2 >= 0) || (w0 <= 0 && w1 <= 0 && w2 <= 0))) continue;
      uint32_t* d = &target_->px[(size_t)py * target_->w + px];
      if (a == 0xFF) *d = 0xFF000000u | (r << 16) | (g << 8) | b;
      else if (a != 0) {
        uint32_t base = *d, da = 255 - a;
        uint32_t rr = (r * a + ((base >> 16) & 0xFF) * da) / 255;
        uint32_t gg = (g * a + ((base >> 8) & 0xFF) * da) / 255;
        uint32_t bb = (b * a + (base & 0xFF) * da) / 255;
        *d = 0xFF000000u | (rr << 16) | (gg << 8) | bb;
      }
    }
  }
}

// ---- RecordStore ---------------------------------------------------------

static String rmsDir() {
  char path[MAX_PATH];
  if (SUCCEEDED(SHGetFolderPathA(nullptr, CSIDL_LOCAL_APPDATA, nullptr, 0, path)))
    return String(path) + "\\rac-gm-port-a1";
  return ".";
}

static String rmsPath(const char* name) {
  return rmsDir() + "\\" + name + ".rms";
}

RecordStore* RecordStore::openRecordStore(const char* name, bool createIfNecessary) {
  CreateDirectoryA(rmsDir().c_str(), nullptr);
  String p = rmsPath(name);
  if (!fileExists(p)) {
    if (!createIfNecessary) return nullptr;
    HANDLE f = CreateFileA(p.c_str(), GENERIC_WRITE, 0, nullptr,
                           CREATE_ALWAYS, FILE_ATTRIBUTE_NORMAL, nullptr);
    if (f != INVALID_HANDLE_VALUE) CloseHandle(f);
  }
  RecordStore* rs = new RecordStore();
  rs->path_ = p;
  rs->load();
  return rs;
}

void RecordStore::deleteRecordStore(const char* name) {
  DeleteFileA(rmsPath(name).c_str());
}

RecordStore::~RecordStore() { if (dirty_) save(); }

void RecordStore::load() {
  records_.clear();
  HANDLE f = CreateFileA(path_.c_str(), GENERIC_READ, FILE_SHARE_READ,
                         nullptr, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, nullptr);
  if (f == INVALID_HANDLE_VALUE) return;
  for (int id = 1; id <= 4; id++) {
    jint len = 0; DWORD got = 0;
    if (!ReadFile(f, &len, 4, &got, nullptr) || got != 4 || len < 0 || len > (1 << 20)) break;
    std::vector<jbyte> rec((size_t)len);
    if (len > 0 && (!ReadFile(f, rec.data(), (DWORD)len, &got, nullptr) ||
                    got != (DWORD)len))
      rec.clear();
    records_.push_back(std::move(rec));
  }
  CloseHandle(f);
}

void RecordStore::save() {
  HANDLE f = CreateFileA(path_.c_str(), GENERIC_WRITE, 0, nullptr,
                         CREATE_ALWAYS, FILE_ATTRIBUTE_NORMAL, nullptr);
  if (f == INVALID_HANDLE_VALUE) return;
  for (auto& rec : records_) {
    jint len = (jint)rec.size();
    DWORD got;
    WriteFile(f, &len, 4, &got, nullptr);
    if (len > 0) WriteFile(f, rec.data(), (DWORD)len, &got, nullptr);
  }
  CloseHandle(f);
  dirty_ = false;
}

int RecordStore::getNumRecords() { return (int)records_.size(); }

void RecordStore::addRecord(const jbyte* data, int offset, int length) {
  records_.push_back(std::vector<jbyte>(data + offset, data + offset + length));
  dirty_ = true; save();
}

void RecordStore::setRecord(int id, const jbyte* data, int offset, int length) {
  if (id < 1 || id > (int)records_.size()) return;
  records_[(size_t)id - 1].assign(data + offset, data + offset + length);
  dirty_ = true; save();
}

int RecordStore::getRecord(int id, jbyte* buffer, int offset) {
  if (id < 1 || id > (int)records_.size()) return 0;
  auto& rec = records_[(size_t)id - 1];
  if (!rec.empty()) memcpy(buffer + offset, rec.data(), rec.size());
  return (int)rec.size();
}

int RecordStore::getRecordSize(int id) {
  if (id < 1 || id > (int)records_.size()) return 0;
  return (int)records_[(size_t)id - 1].size();
}

void RecordStore::closeRecordStore() { if (dirty_) save(); }

// ---- SoundPlayer (MMAPI subset via MCI) ----------------------------------

// src_a1/SoundPlayer.java's soundFiles/soundFormats tables: only MENU (5)
// is MIDI here, the rest are .wav.
static const char* kSoundFileNames[6] = {
  "death", "bubble", "shoot", "msound", "box", "menu"
};
static const bool kSoundIsWav[6] = { true, true, true, true, true, false };

void SoundPlayer::loadAll() {
  for (int i = 0; i < 6; i++) {
    String ext = kSoundIsWav[i] ? ".wav" : ".mid";
    String p = resourcePath(String("/") + kSoundFileNames[i] + ext);
    isWav_[i] = kSoundIsWav[i];
    soundFiles_[i] = fileExists(p) ? p : "";
    if (kSoundIsWav[i] && !soundFiles_[i].empty()) {
      FILE* f = nullptr;
      if (fopen_s(&f, soundFiles_[i].c_str(), "rb") == 0 && f) {
        fseek(f, 0, SEEK_END);
        long n = ftell(f);
        fseek(f, 0, SEEK_SET);
        if (n > 0) {
          wavData_[i].resize((size_t)n);
          if (fread(wavData_[i].data(), 1, (size_t)n, f) != (size_t)n) wavData_[i].clear();
        }
        fclose(f);
      }
    }
  }
}

void SoundPlayer::queue(int id) { queue(id, 1); }

void SoundPlayer::queue(int id, int loop) {
  if (pendingSound_ == -1) {
    pendingSound_ = id;
    pendingLoop_ = loop;
  }
}

bool SoundPlayer::playMenuLoop(bool) {
  queue(MENU, -1);
  return true;
}

// MCI open/play/close block for tens of ms (MIDI sequencer setup especially), so every
// MCI call runs on one worker thread instead of the game thread.
static std::mutex g_mciMutex;
static std::condition_variable g_mciCv;
static std::deque<std::string> g_mciQueue;
static std::atomic<bool> g_mciLoop{false};
static std::once_flag g_mciOnce;

static void mciWorker() {
  for (;;) {
    std::deque<std::string> batch;
    {
      std::unique_lock<std::mutex> lk(g_mciMutex);
      g_mciCv.wait_for(lk, std::chrono::milliseconds(250), [] { return !g_mciQueue.empty(); });
      batch.swap(g_mciQueue);
    }
    for (auto& c : batch) mciSendStringA(c.c_str(), nullptr, 0, nullptr);
    if (g_mciLoop) {
      char mode[32] = {};
      mciSendStringA("status racsnd_a1 mode", mode, sizeof(mode), nullptr);
      if (strcmp(mode, "stopped") == 0)
        mciSendStringA("play racsnd_a1 from 0", nullptr, 0, nullptr);  // sequencer rejects "repeat"
    }
  }
}

static void mciPost(const std::string& cmd) {
  std::call_once(g_mciOnce, [] { std::thread(mciWorker).detach(); });
  {
    std::lock_guard<std::mutex> lk(g_mciMutex);
    g_mciQueue.push_back(cmd);
  }
  g_mciCv.notify_one();
}

void SoundPlayer::haltPlayer() {
  if (playing_) {
    g_mciLoop = false;
    mciPost("close racsnd_a1");
    playing_ = false;
    playingId_ = -1;
  }
}

void SoundPlayer::stop() {
  pendingSound_ = -1;
  PlaySoundA(nullptr, nullptr, 0);
  haltPlayer();
}

void SoundPlayer::update() {
  if (pendingSound_ < 0) return;
  if (pendingSound_ > 5) { pendingSound_ = -1; return; }
  if (!wavData_[pendingSound_].empty()) {
    if (playing_) haltPlayer();
    PlaySoundA(wavData_[pendingSound_].data(), nullptr, SND_MEMORY | SND_ASYNC | SND_NODEFAULT);
    pendingSound_ = -1;
    return;
  }
  if (soundFiles_[pendingSound_].empty()) { pendingSound_ = -1; return; }
  if (playing_ && playingId_ == pendingSound_ && pendingLoop_ == -1 && playingLoop_ == -1) { pendingSound_ = -1; return; }
  if (playing_) haltPlayer();
  const char* type = isWav_[pendingSound_] ? "waveaudio" : "sequencer";
  mciPost("open \"" + soundFiles_[pendingSound_] + "\" type " + type + " alias racsnd_a1");
  mciPost("play racsnd_a1");
  g_mciLoop = (pendingLoop_ == -1);
  playingLoop_ = pendingLoop_;
  playingId_ = pendingSound_;
  playing_ = true;
  pendingSound_ = -1;
}

// ---- platform (Win32 window, input, presentation) -------------------------

namespace platform {
Image canvas;

static HWND g_hwnd = nullptr;
static bool g_quit = false;
static void (*g_onKeyDown)(int) = nullptr;
static void (*g_onKeyUp)(int) = nullptr;
static void (*g_onChar)(int) = nullptr;

// Fullscreen state: a borderless WS_POPUP window covering the current monitor.
static bool g_fullscreen = false;
static WINDOWPLACEMENT g_windowedPlacement = {sizeof(WINDOWPLACEMENT)};
static DWORD g_windowedStyle = 0;

// Where the canvas is drawn inside the client area. All scaling policy lives here: today it
// is "fit, keep aspect ratio, centre". A widescreen mode would change canvas.w (the logical
// width) and/or this function; present() and the window code don't assume 176x220.
struct Viewport { int x, y, w, h; };
static Viewport computeViewport(int clientW, int clientH, int canvasW, int canvasH) {
  if (clientW <= 0 || clientH <= 0 || canvasW <= 0 || canvasH <= 0) return {0, 0, clientW, clientH};
  // Compare clientW/clientH against canvasW/canvasH without floating point.
  if ((long long)clientW * canvasH >= (long long)clientH * canvasW) {
    int w = (int)((long long)clientH * canvasW / canvasH);  // pillarbox
    return {(clientW - w) / 2, 0, w, clientH};
  }
  int h = (int)((long long)clientW * canvasH / canvasW);    // letterbox
  return {0, (clientH - h) / 2, clientW, h};
}

// See midp.h's KeyCode note: arrows/fire/soft-keys map to the same Nokia
// values the legacy build's shim used, '/' still stands in for the keypad
// '*' (CanvasShell.java checks literal 42, MIDP's KEY_STAR). Provisional --
// revisit once a real keyPressed handler (IntroManager's) is transcribed.
static int mapVirtualKey(WPARAM vk) {
  switch (vk) {
    case VK_UP: return KEY_UP;
    case VK_DOWN: return KEY_DOWN;
    case VK_LEFT: return KEY_LEFT;
    case VK_RIGHT: return KEY_RIGHT;
    case VK_RETURN:
    case VK_SPACE: return KEY_FIRE;
    case VK_ESCAPE: return KEY_SOFT_LEFT;
    case VK_BACK: return KEY_SOFT_RIGHT;
    case VK_TAB: return 35;  // '#' (weapon wheel)
    case VK_OEM_2: return KEY_STAR;  // '/' -- stand-in for the keypad '*'
    default:
      if (vk >= '0' && vk <= '9') return (int)vk;
      return 0;
  }
}

void toggleFullscreen() {
  if (!g_hwnd) return;
  if (!g_fullscreen) {
    g_windowedStyle = (DWORD)GetWindowLongW(g_hwnd, GWL_STYLE);
    GetWindowPlacement(g_hwnd, &g_windowedPlacement);
    MONITORINFO mi = {sizeof(mi)};
    if (!GetMonitorInfoW(MonitorFromWindow(g_hwnd, MONITOR_DEFAULTTONEAREST), &mi)) return;
    SetWindowLongW(g_hwnd, GWL_STYLE, (LONG)(g_windowedStyle & ~WS_OVERLAPPEDWINDOW) | (LONG)WS_POPUP);
    SetWindowPos(g_hwnd, HWND_TOP, mi.rcMonitor.left, mi.rcMonitor.top,
                 mi.rcMonitor.right - mi.rcMonitor.left, mi.rcMonitor.bottom - mi.rcMonitor.top,
                 SWP_FRAMECHANGED | SWP_NOOWNERZORDER | SWP_SHOWWINDOW);
    g_fullscreen = true;
  } else {
    SetWindowLongW(g_hwnd, GWL_STYLE, (LONG)g_windowedStyle);
    SetWindowPlacement(g_hwnd, &g_windowedPlacement);
    SetWindowPos(g_hwnd, nullptr, 0, 0, 0, 0,
                 SWP_NOMOVE | SWP_NOSIZE | SWP_NOZORDER | SWP_NOOWNERZORDER | SWP_FRAMECHANGED);
    g_fullscreen = false;
  }
  present();
}

bool isFullscreen() { return g_fullscreen; }

static LRESULT CALLBACK WndProc(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam) {
  switch (msg) {
    case WM_DESTROY:
      g_quit = true;
      PostQuitMessage(0);
      return 0;
    case WM_ERASEBKGND:
      return 1;  // present() paints the bars itself; avoids flicker on resize
    case WM_PAINT: {
      PAINTSTRUCT ps;
      BeginPaint(hwnd, &ps);
      EndPaint(hwnd, &ps);
      present();
      return 0;
    }
    case WM_SETCURSOR:
      if (g_fullscreen && LOWORD(lParam) == HTCLIENT) { SetCursor(nullptr); return TRUE; }
      return DefWindowProcW(hwnd, msg, wParam, lParam);
    case WM_SYSKEYDOWN:
      if (wParam == VK_RETURN && (lParam & (1 << 29))) { toggleFullscreen(); return 0; }  // Alt+Enter
      return DefWindowProcW(hwnd, msg, wParam, lParam);
    case WM_KEYDOWN: {
      if (wParam == VK_F11) { if (!(lParam & (1 << 30))) toggleFullscreen(); return 0; }
      int code = mapVirtualKey(wParam);
      if (code != 0 && g_onKeyDown) g_onKeyDown(code);
      return 0;
    }
    case WM_CHAR:
      if (wParam >= 32 && wParam < 127 && g_onChar) g_onChar((int)wParam);
      return 0;
    case WM_KEYUP: {
      int code = mapVirtualKey(wParam);
      if (code != 0 && g_onKeyUp) g_onKeyUp(code);
      return 0;
    }
    case WM_CLOSE:
      g_quit = true;
      DestroyWindow(hwnd);
      return 0;
    default:
      return DefWindowProcW(hwnd, msg, wParam, lParam);
  }
}

bool initWindow(bool fullscreen) {
  canvas.w = 176;
  canvas.h = 220;
  canvas.px.assign(176u * 220u, 0xFF000000u);

  const wchar_t* kClassName = L"GoingMobilePortA1Window";
  WNDCLASSW wc = {};
  wc.lpfnWndProc = WndProc;
  wc.hInstance = GetModuleHandleW(nullptr);
  wc.lpszClassName = kClassName;
  wc.hCursor = LoadCursorW(nullptr, MAKEINTRESOURCEW(32512));  // IDC_ARROW
  RegisterClassW(&wc);

  RECT r = {0, 0, 176, 220};
  AdjustWindowRect(&r, WS_OVERLAPPEDWINDOW, FALSE);
  g_hwnd = CreateWindowExW(0, kClassName, L"Ratchet and Clank: Going Mobile Port (a1)",
                            WS_OVERLAPPEDWINDOW, CW_USEDEFAULT, CW_USEDEFAULT,
                            r.right - r.left, r.bottom - r.top,
                            nullptr, nullptr, wc.hInstance, nullptr);
  if (!g_hwnd) return false;
  ShowWindow(g_hwnd, SW_SHOWDEFAULT);
  if (fullscreen) toggleFullscreen();  // windowed placement is captured first, so F11 returns to it
  return true;
}

void setCharCallback(void (*onChar)(int)) { g_onChar = onChar; }

void setKeyCallback(void (*onKeyDown)(int), void (*onKeyUp)(int)) {
  g_onKeyDown = onKeyDown;
  g_onKeyUp = onKeyUp;
}

void pumpEvents() {
  MSG msg;
  while (PeekMessageW(&msg, nullptr, 0, 0, PM_REMOVE)) {
    if (msg.message == WM_QUIT) { g_quit = true; continue; }
    TranslateMessage(&msg);
    DispatchMessageW(&msg);
  }
}

void present() {
  if (!g_hwnd) return;
  HDC hdc = GetDC(g_hwnd);
  BITMAPINFO bmi = {};
  bmi.bmiHeader.biSize = sizeof(bmi.bmiHeader);
  bmi.bmiHeader.biWidth = canvas.w;
  bmi.bmiHeader.biHeight = -canvas.h;  // negative = top-down source
  bmi.bmiHeader.biPlanes = 1;
  bmi.bmiHeader.biBitCount = 32;
  bmi.bmiHeader.biCompression = BI_RGB;
  RECT rc;
  GetClientRect(g_hwnd, &rc);
  int cw = rc.right - rc.left, ch = rc.bottom - rc.top;
  Viewport vp = computeViewport(cw, ch, canvas.w, canvas.h);
  HBRUSH black = (HBRUSH)GetStockObject(BLACK_BRUSH);
  if (vp.x > 0) {
    RECT l = {0, 0, vp.x, ch}, r = {vp.x + vp.w, 0, cw, ch};
    FillRect(hdc, &l, black); FillRect(hdc, &r, black);
  }
  if (vp.y > 0) {
    RECT t = {0, 0, cw, vp.y}, b = {0, vp.y + vp.h, cw, ch};
    FillRect(hdc, &t, black); FillRect(hdc, &b, black);
  }
  SetStretchBltMode(hdc, COLORONCOLOR);  // nearest-neighbour: keeps pixel art crisp
  StretchDIBits(hdc, vp.x, vp.y, vp.w, vp.h, 0, 0,
                canvas.w, canvas.h, canvas.px.data(), &bmi, DIB_RGB_COLORS, SRCCOPY);
  ReleaseDC(g_hwnd, hdc);
}

void requestQuit() {
  g_quit = true;
  if (g_hwnd) PostMessageW(g_hwnd, WM_CLOSE, 0, 0);
}

bool quitRequested() { return g_quit; }
}  // namespace platform

// ---- screenshot ---------------------------------------------------------

void platform::screenshot(const char* tag) {
  static int counter = 0;
  char name[64];
  sprintf_s(name, "screenshot_a1_%s_%02d.bmp", tag, counter++);
  HANDLE f = CreateFileA(name, GENERIC_WRITE, 0, nullptr, CREATE_ALWAYS,
                         FILE_ATTRIBUTE_NORMAL, nullptr);
  if (f == INVALID_HANDLE_VALUE) return;
  int w = canvas.w, h = canvas.h;
  int rowBytes = ((w * 3 + 3) / 4) * 4;
  BITMAPFILEHEADER fh = {};
  BITMAPINFOHEADER ih = {};
  fh.bfType = 'B' + ('M' << 8);
  fh.bfOffBits = sizeof(fh) + sizeof(ih);
  fh.bfSize = fh.bfOffBits + (DWORD)(rowBytes * h);
  ih.biSize = sizeof(ih); ih.biWidth = w; ih.biHeight = h;
  ih.biPlanes = 1; ih.biBitCount = 24;
  DWORD got;
  WriteFile(f, &fh, sizeof(fh), &got, nullptr);
  WriteFile(f, &ih, sizeof(ih), &got, nullptr);
  std::vector<uint8_t> row((size_t)rowBytes);
  for (int y = h - 1; y >= 0; y--) {
    memset(row.data(), 0, row.size());
    for (int x = 0; x < w; x++) {
      uint32_t c = canvas.px[(size_t)y * w + x];
      row[(size_t)x * 3 + 0] = (uint8_t)(c & 0xFF);
      row[(size_t)x * 3 + 1] = (uint8_t)((c >> 8) & 0xFF);
      row[(size_t)x * 3 + 2] = (uint8_t)((c >> 16) & 0xFF);
    }
    WriteFile(f, row.data(), (DWORD)row.size(), &got, nullptr);
  }
  CloseHandle(f);
}
