// midp.cpp -- implementation of the MIDP/Nokia shim (see midp.h).
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
  if (!fileExists(dir + "/m.txt")) return false;
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
  // MIDP: setClip replaces (bounded by the target image), in translated space.
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
  // MIDP: the stroke covers an area (w+1) x (h+1).
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
  // Only two call sites in the whole game (HUD/message boxes). MIDP fills a
  // rect with rounded corners; a plain rect is visually close enough here,
  // revisit when porting those call sites (milestone 3.2).
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
  int w = img->w, h = img->h;
  // Transformed dimensions (rotation swaps w/h).
  bool rot = (manipulation >= 1 && manipulation <= 7 && manipulation != 4 && manipulation != 6);
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
      switch (manipulation) {
        case MANIP_ROT_90:        sx = dyy; sy = h - 1 - dxx; break;
        case MANIP_ROT_180:      sx = w - 1 - dxx; sy = h - 1 - dyy; break;
        case MANIP_ROT_270:      sx = w - 1 - dyy; sy = dxx; break;
        case MANIP_MIRROR:       sx = w - 1 - dxx; break;
        case MANIP_MIRROR_ROT_90:  sx = dyy; sy = h - 1 - dxx; break;
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

void Graphics::fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int argb) {
  if (!target_) return;
  // ARGB color (Nokia DirectGraphics): alpha in the top byte, blends.
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
    return String(path) + "\\rac-gm-port";
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

static const char* kSoundFileNames[7] = {
  "port", "killen", "bubble", "death", "box", "shoot", "menu"
};

void SoundPlayer::loadAll() {
  for (int i = 0; i < 7; i++) {
    String p = resourcePath(String("/") + kSoundFileNames[i] + ".mid");
    soundFiles_[i] = fileExists(p) ? p : "";
  }
}

void SoundPlayer::queue(int id, int loop) {
  if (pendingSound_ == -1) {
    pendingSound_ = id;
    pendingLoop_ = loop;
  }
}

void SoundPlayer::stopPlayer() {
  if (playing_) {
    mciSendStringA("close racsnd", nullptr, 0, nullptr);
    playing_ = false;
  }
}

void SoundPlayer::stop() {
  pendingSound_ = -1;
  stopPlayer();
}

void SoundPlayer::update() {
  if (!threadAlive) return;
  // Poll MCI completion = MMAPI endOfMedia -> playerUpdate().
  if (playing_ && playingLoop_ != -1) {
    char mode[32] = {};
    mciSendStringA("status racsnd mode", mode, sizeof(mode), nullptr);
    if (strcmp(mode, "stopped") == 0) endOfMedia_ = true;
  }
  if (endOfMedia_ && pendingLoop_ != -1) pendingSound_ = -2;
  endOfMedia_ = false;

  if (pendingSound_ <= -1) {
    if (pendingSound_ == -2) {
      pendingSound_ = -1;
      stopPlayer();
    }
  } else if (playing_) {
    stopPlayer();
  } else {
    if (pendingSound_ < 0 || pendingSound_ > 6) { pendingSound_ = -1; return; }
    if (soundFiles_[pendingSound_].empty()) {
      if (pendingLoop_ != -1) pendingSound_ = -1;
      return;
    }
    char cmd[512];
    sprintf_s(cmd, "open \"%s\" alias racsnd", soundFiles_[pendingSound_].c_str());
    if (mciSendStringA(cmd, nullptr, 0, nullptr) == 0) {
      playingLoop_ = pendingLoop_;
      sprintf_s(cmd, "play racsnd%s", pendingLoop_ == -1 ? " repeat" : "");
      mciSendStringA(cmd, nullptr, 0, nullptr);
      playing_ = true;
      pendingSound_ = -1;
    } else {
      if (pendingLoop_ != -1) pendingSound_ = -1;
    }
  }
}

// ---- platform (Win32 window, input, presentation) -------------------------

namespace platform {
Image canvas;

static HWND g_hwnd = nullptr;
static bool g_quit = false;
static void (*g_onKeyDown)(int) = nullptr;
static void (*g_onKeyUp)(int) = nullptr;

// Maps a Win32 virtual-key to the Nokia FullCanvas key code the decompiled
// game expects (see KeyCode in midp.h): arrows + Enter/Space (fire) +
// Escape/Backspace (soft keys) for gameplay, plus '0'-'9' and '/' (stands in
// for the phone keypad's '*') for the multitap name-entry screen. Provisional
// -- revisit once milestone 3.2 exercises real input.
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
    case VK_OEM_2: return 42;  // '/' -- stand-in for the keypad '*'
    default:
      if (vk >= '0' && vk <= '9') return (int)vk;
      return 0;
  }
}

static LRESULT CALLBACK WndProc(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam) {
  switch (msg) {
    case WM_DESTROY:
      g_quit = true;
      PostQuitMessage(0);
      return 0;
    case WM_KEYDOWN: {
      int code = mapVirtualKey(wParam);
      if (code != 0 && g_onKeyDown) g_onKeyDown(code);
      return 0;
    }
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

bool initWindow() {
  canvas.w = 128;
  canvas.h = 128;
  canvas.px.assign(128u * 128u, 0xFF000000u);

  const wchar_t* kClassName = L"GoingMobilePortWindow";
  WNDCLASSW wc = {};
  wc.lpfnWndProc = WndProc;
  wc.hInstance = GetModuleHandleW(nullptr);
  wc.lpszClassName = kClassName;
  wc.hCursor = LoadCursorW(nullptr, MAKEINTRESOURCEW(32512));  // IDC_ARROW
  RegisterClassW(&wc);

  RECT r = {0, 0, 128, 128};
  AdjustWindowRect(&r, WS_OVERLAPPEDWINDOW, FALSE);
  g_hwnd = CreateWindowExW(0, kClassName, L"Ratchet and Clank: Going Mobile Port",
                            WS_OVERLAPPEDWINDOW, CW_USEDEFAULT, CW_USEDEFAULT,
                            r.right - r.left, r.bottom - r.top,
                            nullptr, nullptr, wc.hInstance, nullptr);
  if (!g_hwnd) return false;
  ShowWindow(g_hwnd, SW_SHOWDEFAULT);
  return true;
}

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
  StretchDIBits(hdc, 0, 0, rc.right - rc.left, rc.bottom - rc.top, 0, 0,
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
  sprintf_s(name, "screenshot_%s_%02d.bmp", tag, counter++);
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


