#define _CRT_SECURE_NO_WARNINGS
#include "engine.h"
#include "gen/classes.h"

#include <algorithm>
#include <climits>
#include <cstdio>

#include <windows.h>

namespace ch {

namespace {
// Engine.keyCodeTable: bit index -> MIDP key code.
int8_t g_keyCodeTable[32];
struct KeyTableInit {
  KeyTableInit() {
    g_keyCodeTable[1] = -1;
    g_keyCodeTable[2] = -3;
    g_keyCodeTable[3] = 35;
    g_keyCodeTable[4] = -8;
    g_keyCodeTable[5] = -4;
    g_keyCodeTable[6] = -2;
    g_keyCodeTable[8] = -5;
    g_keyCodeTable[10] = 42;
    g_keyCodeTable[13] = -36;
    g_keyCodeTable[14] = -37;
    g_keyCodeTable[15] = -11;
    for (int i = 0; i < 10; i++) g_keyCodeTable[16 + i] = (int8_t)(48 + i);
    g_keyCodeTable[27] = -6;
    g_keyCodeTable[29] = -7;
  }
} g_keyTableInit;
}  // namespace

void MIDlet::notifyDestroyed() {}

// Sprite.draw (Sprite.java)
void Sprite::draw(Graphics* g, int x, int y, int trans) {
  trans &= 3;
  x += (trans & 2) == 0 ? offsetX : -(offsetX + width);
  y += (trans & 1) == 0 ? offsetY : -(offsetY + height);
  int rw = width, rh = height, sx = srcX, sy = srcY;
  if (sx < 0) { rw += sx; x += sx; sx = 0; }
  if (sy < 0) { rh += sy; y += sy; sy = 0; }
  if (sx + rw > sheet->w) rw = sheet->w - sx;
  if (sy + rh > sheet->h) rh = sheet->h - sy;
  if (rw > 0 && rh > 0) g->drawRegion(sheet, sx, sy, rw, rh, trans, x, y, 20);
}

Engine::Engine(MIDlet* m) : midlet(m) {
  ok_ = assets.load(Platform::dataDir);
  CreateDirectoryA(Platform::saveDir.c_str(), nullptr);
  resourceTypes = Arr<int8_t>(1024);
  setViewport(kScreenW, kScreenH);
}

// ---------------------------------------------------------------- lifecycle
void Engine::onLifecycle(int state) {
  if (state == 3) quit = true;
  if (state == 0 || state == 1 || state == 2) {
    clearBorders_ = true;
    resetFrameTiming_ = true;
    clearKeys();
  }
}

void Engine::pause() {
  if (paused_) return;
  onLifecycle(1);
  paused_ = true;
}

void Engine::resume() {
  if (!paused_) return;
  paused_ = false;
  onLifecycle(2);
  onLifecycle(5);
}

void Engine::resetFrameTimingNow() {
  frameTime = minFrameTime;
  resetFrameTiming_ = true;
}

// The Java game threw uncaught exceptions (killing its thread) in a few corner cases;
// the port logs them and carries on with the next frame instead.
static void logJavaException(const char* where, const char* what) {
  static int count = 0;
  if (++count > 200) return;
  FILE* f = std::fopen("exceptions.log", "a");
  if (f) {
    std::fprintf(f, "%s: %s\n", where, what);
    std::fclose(f);
  }
}

int Engine::runFrame(long nowMs, Surface& screen) {
  if (!started_) {
    started_ = true;
    onLifecycle(0);
  }
  if (paused_) {
    resetFrameTiming_ = true;
    return 100;
  }
  int sleepMs = 0;
  if (realTime) {
    // Fixed-rate pacing. The game runs one logic step per frame, and only when the frame time is
    // over 40 ms (tickAccum > 40), so it is always told at least 41 ms whatever the real rate.
    long period = 1000 / (targetFps < 1 ? 1 : targetFps);
    if (resetFrameTiming_) {
      resetFrameTiming_ = false;
      lastFrameStart_ = nowMs - period;
    }
    long due = lastFrameStart_ + period;
    if (nowMs < due) {
      Sleep((DWORD)(due - nowMs));
    } else if (nowMs - due > period * 3) {
      due = nowMs;  // fell far behind (window drag, breakpoint): resync instead of catching up
    }
    lastFrameStart_ = due;
    frameTime = (int)std::max(period, 41L);
  } else {
    if (resetFrameTiming_) {
      resetFrameTiming_ = false;
      frameTime = minFrameTime;
    } else {
      int dt = (int)(nowMs - lastFrameStart_);
      if (dt > maxFrameTime || dt < 0) dt = maxFrameTime;
      frameTime = dt;
      if (frameTime < minFrameTime) {
        sleepMs = std::max(minFrameTime - frameTime, 10);
        frameTime = minFrameTime;
      }
    }
    lastFrameStart_ = nowMs;
  }
  try {
    update();
  } catch (const JavaException& e) {
    logJavaException("update", e.what);
  }
  if (soundPlayer) soundPlayer->run();
  Player::pumpLoops();
  if (quit) return 0;

  screen.resetTransform();
  bordersCleared = clearBorders_;
  clearBorders_ = false;
  if (canvasW != kScreenW || canvasH != kScreenH) {
    if (bordersCleared) {
      screen.resetClip();
      int v = (kScreenH - canvasH + 1) >> 1;
      if (v > 0) {
        if (offY > 0) screen.fillRectArgb(0, 0, kScreenW, offY, 0xFF000000);
        screen.fillRectArgb(0, kScreenH - v, kScreenW, v, 0xFF000000);
      }
      v = (kScreenW - canvasW + 1) >> 1;
      if (v > 0) {
        if (offX > 0) screen.fillRectArgb(0, offY, offX, canvasH, 0xFF000000);
        screen.fillRectArgb(kScreenW - v, offY, v, canvasH, 0xFF000000);
      }
    }
    screen.translate(offX, offY);
  }
  screen.setClip(0, 0, canvasW, canvasH);
  try {
    render(&screen);
  } catch (const JavaException& e) {
    logJavaException("render", e.what);
  }
  return sleepMs;
}

void Engine::setViewport(int w, int h) {
  clearBorders_ = true;
  if (w <= 0) w = kScreenW;
  if (h <= 0) h = kScreenH;
  canvasW = w;
  canvasH = h;
  offX = kScreenW - w;
  offY = kScreenH - h;
  if (offX < 0) offX++;
  if (offY < 0) offY++;
  offX >>= 1;
  offY >>= 1;
}

// -------------------------------------------------------------------- keys
void Engine::platformKey(int code, bool down) {
  if (!code) return;
  for (int i = 1; i < 32; i++) {
    if (g_keyCodeTable[i] != code) continue;
    int bit = 1 << i;
    if (down) {
      pendingPressed_ |= bit;
      pendingHeld_ |= bit;
      pendingReleased_ &= ~bit;
    } else {
      pendingReleased_ |= bit;
    }
  }
}

void Engine::clearKeys() {
  pendingPressed_ = 0;
  pendingHeld_ = 0;
  pendingReleased_ |= keysHeld_;
  keysPressed = 0;
  keysHeld_ = 0;
  keysReleased = 0;
}

void Engine::pollKeys() {
  keysPressed = pendingPressed_;
  pendingPressed_ = 0;
  pendingReleased_ &= pendingHeld_;
  pendingHeld_ &= ~pendingReleased_;
  keysHeld_ = keysPressed | pendingHeld_;
  keysReleased = pendingReleased_;
  pendingReleased_ = 0;
}

// ----------------------------------------------------------------- records
static std::string recPath(int id) { return Platform::saveDir + "/rec" + std::to_string(id) + ".bin"; }

Arr<int8_t> Engine::readRecord(int id) {
  FILE* f = std::fopen(recPath(id).c_str(), "rb");
  if (!f) return Arr<int8_t>();
  std::fseek(f, 0, SEEK_END);
  long n = std::ftell(f);
  std::fseek(f, 0, SEEK_SET);
  Arr<int8_t> out((int)n);
  size_t got = n ? std::fread(out.data(), 1, (size_t)n, f) : 0;
  std::fclose(f);
  return got == (size_t)n ? out : Arr<int8_t>();
}

bool Engine::writeRecord(int id, const Arr<int8_t>& data) {
  std::string p = recPath(id);
  std::remove(p.c_str());
  if (data == nullptr) return true;
  FILE* f = std::fopen(p.c_str(), "wb");
  if (!f) return false;
  bool ok = data.length() == 0 || std::fwrite(data.data(), 1, (size_t)data.length(), f) == (size_t)data.length();
  std::fclose(f);
  return ok;
}

void Engine::syncSettings(bool write) {
  if (!write && settingsLoaded_) return;
  if (write) {
    writeRecord(-9, Arr<int8_t>::make((uint8_t)soundEnabled, (uint8_t)optM_, (uint8_t)optN_));
    return;
  }
  settingsLoaded_ = true;
  Arr<int8_t> b = readRecord(-9);
  if (b == nullptr || b.length() != 3) {
    b = Arr<int8_t>::make(-1, -1, -1);
    writeRecord(-9, b);
  }
  soundEnabled = b[0] != 0;
  optM_ = b[1] != 0;
  optN_ = b[2] != 0;
}

// --------------------------------------------------------------- resources
static Bytes toBytes(const Arr<int8_t>& a) {
  return Bytes{(const uint8_t*)a.data(), a.length()};
}

Image* Engine::loadImage(int id, const Arr<int8_t>& swap) {
  std::string key = std::to_string(id);
  if (swap != nullptr) {
    uint32_t h = 2166136261u;
    for (int i = 0; i < swap.length(); i++) h = (h ^ (uint8_t)swap[i]) * 16777619u;
    key += ":" + std::to_string(h);
  }
  // Images are immutable in MIDP, so one decoded copy can be shared.
  Bytes res = assets.resource(id);  // also selects the bank for a bank-0 id
  key = std::to_string(assets.currentBank()) + "/" + key;
  auto it = imageCache_.find(key);
  if (it != imageCache_.end()) return it->second;
  Image* im = new Image();
  Bytes sw = toBytes(swap);
  if (!assets.loadImage(id, swap != nullptr ? &sw : nullptr, *im)) {
    delete im;
    im = nullptr;
  }
  (void)res;
  imageCache_[key] = im;
  return im;
}

Arr<Sprite*> Engine::loadSprites(int id, const Arr<int8_t>& swap) {
  SpriteSet set = assets.loadSprites(id);
  if (set.sprites.empty()) return Arr<Sprite*>();
  Image* sheet = loadImage(set.imageId, swap);
  if (!sheet) return Arr<Sprite*>();
  Arr<Sprite*> out((int)set.sprites.size());
  for (size_t i = 0; i < set.sprites.size(); i++) {
    const SpriteRect& r = set.sprites[i];
    Sprite* s = new Sprite(sheet);
    Arr<int16_t> v = Arr<int16_t>::make(r.offX, r.offY, r.srcX, r.srcY, r.w, r.h, r.f6, r.f7, r.advance, r.metric);
    s->load(v);
    out[(int)i] = s;
  }
  return out;
}

static void syncTypes(Engine& e, Assets& a, Arr<int8_t>& types) {
  const std::vector<uint8_t>& t = a.bankTypes(a.currentBank());
  types = Arr<int8_t>((int)t.size());
  for (size_t i = 0; i < t.size(); i++) types[(int)i] = (int8_t)t[i];
  (void)e;
}

Arr<int8_t> Engine::getResourceBytes(int id) {
  Bytes b = assets.resource(id);
  syncTypes(*this, assets, resourceTypes);
  Arr<int8_t> out(b.n);
  for (int i = 0; i < b.n; i++) out[i] = (int8_t)b.p[i];
  return out;
}

DataInputStream* Engine::openResource(int id) {
  Bytes b = assets.resource(id);
  syncTypes(*this, assets, resourceTypes);
  return new DataInputStream(b.p, b.n);
}

void Engine::loadAnimSet(int id) {
  Bytes res = assets.resource(id);
  struct R {
    Bytes b;
    int p = 0;
    int s8() { return (int8_t)b.p[p++]; }
    int u16() { int v = b.p[p] << 8 | b.p[p + 1]; p += 2; return v; }
    int s16() { return (int16_t)u16(); }
  } r{res};
  setId_.push_back((int16_t)id);
  setSeq_.push_back((int16_t)seqOff_.size());
  setList_.push_back((int16_t)listOff_.size());
  int nSeq = r.u16() & 0x7FFF;
  int nLists = r.u16();
  r.u16(); r.u16(); r.u16();
  int steps = 0, parts = 0;
  std::vector<int8_t> cols[11];
  for (int c = 0; c < 11; c++) {
    bool delta = c != 8;
    int n = nSeq;
    if (c > 1) n = steps;
    if (c > 6) n = nLists;
    if (c > 7) n = parts;
    cols[c].resize((size_t)n);
    int acc = 0;
    for (int i = 0; i < n; i++) {
      int v = r.s8();
      if (v == -128 && delta) v = r.s16();
      int val = acc + v;
      if (delta) acc = val;
      if (c == 1) steps += val;
      if (c == 7) parts += val;
      if (c == 2) {
        size_t k = 0;
        while (k < durations_.size() && durations_[k] != (int16_t)val) k++;
        if (k == durations_.size()) durations_.push_back((int16_t)val);
        val = (int)k;
      }
      cols[c][(size_t)i] = (int8_t)val;
    }
  }
  int sp = 0, pp = 0;
  for (int s = 0; s < nSeq; s++) {
    seqOff_.push_back((int16_t)steps_.size());
    int n = (uint8_t)cols[1][(size_t)s];
    steps_.push_back(cols[0][(size_t)s]);
    steps_.push_back((int8_t)n);
    for (; n-- > 0; sp++)
      for (int c = 2; c < 7; c++) steps_.push_back(cols[c][(size_t)sp]);
  }
  for (int l = 0; l < nLists; l++) {
    listOff_.push_back((int16_t)parts_.size());
    int n = (uint8_t)cols[7][(size_t)l];
    parts_.push_back((int8_t)n);
    for (; n-- > 0; pp++)
      for (int c = 8; c < 11; c++) parts_.push_back(cols[c][(size_t)pp]);
  }
}

String Engine::getString(int i) { return String::fromUtf8(assets.str(i)); }

void Engine::loadStringTable(int id) {
  if (id == -1) return;
  assets.loadStringTable(id);
}

// -------------------------------------------------------------------- font
void Engine::setFont(const Arr<Sprite*>& font) {
  font_ = font;
  if (font_ != nullptr && font[3]->metric == 255) {
    font[3]->metric = 0;
    int16_t acc = 0;
    for (int i = 0; i < font.length(); i++) {
      Sprite* s = font[i];
      acc = (int16_t)(acc + (s->offsetX << 8) + (s->offsetY & 255));
      s->offsetX = s->charCode;
      s->offsetY = s->f;
      s->charCode = acc;
    }
  }
}

Sprite* Engine::findGlyph(char16_t c) {
  int lo = 0, hi = font_.length() - 1;
  while (lo <= hi) {
    int mid = (lo + hi) >> 1;
    char16_t v = (char16_t)font_[mid]->charCode;
    if (v < c) lo = mid + 1;
    else if (v > c) hi = mid - 1;
    else return font_[mid];
  }
  return nullptr;
}

int Engine::stringWidth(const String& s, int off, int len) {
  Arr<char16_t> a(len);
  for (int i = 0; i < len; i++) a[i] = s.charAt(off + i);
  return charsWidth(a, 0, len);
}

int Engine::charWidth(char16_t c) {
  Arr<char16_t> a = Arr<char16_t>::make(c);
  return charsWidth(a, 0, 1);
}

int Engine::charsWidth(const Arr<char16_t>& s, int off, int len) {
  if (font_ == nullptr) return len * 6;
  len += off;
  int16_t total = 0;
  for (int i = off; i < len; i++) {
    Sprite* g = findGlyph(s[i]);
    total = (int16_t)(total + (g ? g->advance : font_[2]->metric));
  }
  return total;
}

int Engine::fontBaseline() { return font_ != nullptr ? font_[1]->metric : 10; }
int Engine::fontHeight() { return font_ != nullptr ? font_[0]->metric : 12; }

void Engine::drawString(Graphics* g, const String& s, int off, int len, int x, int y, int anchor) {
  Arr<char16_t> a(len);
  for (int i = 0; i < len; i++) a[i] = s.charAt(off + i);
  drawString(g, a, 0, len, x, y, anchor);
}

void Engine::drawString(Graphics* g, const Arr<char16_t>& s, int off, int len, int x, int y, int anchor) {
  if (anchor & 64) y -= fontBaseline();
  else if (anchor & 32) y -= fontHeight();
  if (anchor & 9) x -= charsWidth(s, off, len) >> (anchor & 1);
  if (font_ == nullptr) return;
  for (int end = len + off; off < end; off++) {
    Sprite* gl = findGlyph(s[off]);
    if (gl) {
      gl->draw(g, x, y, 0);
      x += gl->advance;
    } else {
      x += font_[2]->metric;
    }
  }
}

// --------------------------------------------------------------- tile layer
void Engine::initTileMap(int cols, int rows, Image* sheet, int tw, int th) {
  scrollX_ = scrollY_ = 0;
  gridCols_ = cols;
  gridRows_ = rows;
  tileGrid_.assign((size_t)cols * rows, 0);
  animatedTiles_.clear();
  tileSrcX_.clear();
  setTileSheet(sheet, tw, th);
}

void Engine::setTileSheet(Image* sheet, int tw, int th) {
  tileW_ = tw;
  tileH_ = th;
  int w = sheet->w, h = sheet->h;
  int oldCount = (int)tileSrcX_.size();
  int newCount = w / tw * (h / th);
  if (oldCount > newCount) {
    fillTiles(0, 0, gridCols_, gridRows_, 0);
    animatedTiles_.clear();
  }
  tileSheet_ = sheet;
  tileSrcX_.assign((size_t)newCount, 0);
  tileSrcY_.assign((size_t)newCount, 0);
  int n = 0;
  for (int y = 0; y <= h - th; y += th)
    for (int x = 0; x <= w - tw; x += tw) {
      tileSrcX_[(size_t)n] = x;
      tileSrcY_[(size_t)n] = y;
      n++;
    }
}

void Engine::fillTiles(int x, int y, int w, int h, int tile) {
  for (int yy = y; yy < y + h; yy++)
    for (int xx = x; xx < x + w; xx++) tileGrid_[(size_t)yy * gridCols_ + xx] = (int16_t)tile;
}

void Engine::setViewSize(int w, int h) {
  viewW_ = std::max(0, w);
  viewH_ = std::max(0, h);
  if (w < 0 || h < 0) {
    tileSheet_ = nullptr;
    tileSrcX_.clear();
    tileSrcY_.clear();
    tileGrid_.clear();
    animatedTiles_.clear();
  }
}

int Engine::addAnimatedTile(int tile) {
  animatedTiles_.push_back(tile);
  return -(int)animatedTiles_.size();
}

void Engine::drawTiles(Graphics* g, int mapX, int mapY, int dx, int dy, int w, int h) {
  if (!tileSheet_ || tileGrid_.empty()) return;
  auto floorDiv = [](int a, int b) { return a >= 0 ? a / b : -((-a + b - 1) / b); };
  int c0 = floorDiv(mapX, tileW_), c1 = floorDiv(mapX + w - 1, tileW_);
  int r0 = floorDiv(mapY, tileH_), r1 = floorDiv(mapY + h - 1, tileH_);
  for (int r = r0; r <= r1; r++) {
    for (int c = c0; c <= c1; c++) {
      if (c < 0 || r < 0 || c >= gridCols_ || r >= gridRows_) continue;
      int t = tileGrid_[(size_t)r * gridCols_ + c];
      if (t < 0) t = animatedTiles_[(size_t)~t];
      if (t <= 0) continue;
      t--;
      int px0 = std::max(mapX, c * tileW_), px1 = std::min(mapX + w, (c + 1) * tileW_);
      int py0 = std::max(mapY, r * tileH_), py1 = std::min(mapY + h, (r + 1) * tileH_);
      g->drawRegion(tileSheet_, tileSrcX_[(size_t)t] + (px0 - c * tileW_), tileSrcY_[(size_t)t] + (py0 - r * tileH_),
                    px1 - px0, py1 - py0, 0, dx + (px0 - mapX), dy + (py0 - mapY), 20);
    }
  }
}

void Engine::drawTileMap(Graphics* g) {
  int tx;
  if (scrollX_ < 0) tx = scrollX_;
  else if ((tx = scrollX_ + viewW_ - gridCols_ * tileW_) < 0) tx = 0;
  int ty;
  if (scrollY_ < 0) ty = scrollY_;
  else if ((ty = scrollY_ + viewH_ - gridRows_ * tileH_) < 0) ty = 0;
  if (tx != 0 || ty != 0) {
    scrollX_ -= tx;
    scrollY_ -= ty;
    g->translate(-tx, -ty);
  }
  int mx = scrollX_, my = scrollY_, dx = 0, dy = 0, w = viewW_, h = viewH_;
  int v = g->getClipX();
  if (v > 0) { mx += v; w -= v; dx = v; }
  v = dx + w - (v + g->getClipWidth());
  if (v > 0) w -= v;
  v = g->getClipY();
  if (v > 0) { my += v; h -= v; dy = v; }
  v = dy + h - (v + g->getClipHeight());
  if (v > 0) h -= v;
  if (w > 0 && h > 0) drawTiles(g, mx, my, dx, dy, w, h);
  if (tx != 0 || ty != 0) {
    scrollX_ += tx;
    scrollY_ += ty;
    g->translate(tx, ty);
  }
}

// -------------------------------------------------------------- animations
void Engine::startAnim(int idx, int setId, int seq) {
  int s = 0;
  while (s < (int)setId_.size() && setId != setId_[(size_t)s]) s++;
  int seqIndex = setSeq_[(size_t)s] + seq;
  int b = idx * 8;
  if ((int)inst_.size() < b + 8) inst_.resize((size_t)b + 8, 0);
  int16_t off = seqOff_[(size_t)seqIndex];
  inst_[(size_t)b + 0] = 0;
  inst_[(size_t)b + 1] = 0;
  inst_[(size_t)b + 2] = steps_[(size_t)off + 0];
  inst_[(size_t)b + 3] = 0;
  inst_[(size_t)b + 4] = 0;
  inst_[(size_t)b + 5] = (int16_t)((uint8_t)steps_[(size_t)off + 2 + 4]);
  inst_[(size_t)b + 6] = off;
  inst_[(size_t)b + 7] = (int16_t)s;
}

bool Engine::stepAnim(int idx, int dt) {
  size_t b = (size_t)idx * 8;
  int loops = inst_[b + 2];
  if (loops == 0) return false;
  int t = (uint16_t)inst_[b + 0] + dt;
  int step = inst_[b + 1];
  int off = inst_[b + 6];
  int nSteps = (uint8_t)steps_[(size_t)off + 1];
  int base = off + 2;
  int best = step, bestPri = 0;
  int dur;
  while (t > (dur = (uint16_t)durations_[(uint8_t)steps_[(size_t)(base + step * 5 + 0)]])) {
    t -= dur;
    if (++step == nSteps) {
      if (loops > 0) {
        if (--loops == 0) {
          step--;
          t = dur;
          break;
        }
      }
      step = 0;
    }
    int pri = steps_[(size_t)(base + step * 5 + 3)] & 63;
    if (pri >= bestPri) {
      bestPri = pri;
      best = step;
    }
  }
  int frameList = (uint8_t)steps_[(size_t)(base + best * 5 + 4)];
  base += step * 5;
  int x = steps_[(size_t)base + 1];
  int y = steps_[(size_t)base + 2];
  if ((steps_[(size_t)base + 3] >> 6) > 0) {
    base -= 5;
    int px = 0, py = 0;
    if (step != 0) {
      px = steps_[(size_t)base + 1];
      py = steps_[(size_t)base + 2];
    }
    int k = (t << 12) / std::max(1, dur);
    x = px + (((x - px) * k) >> 12);
    y = py + (((y - py) * k) >> 12);
  }
  inst_[b + 0] = (int16_t)t;
  inst_[b + 1] = (int16_t)step;
  inst_[b + 2] = (int16_t)loops;
  inst_[b + 3] = (int16_t)x;
  inst_[b + 4] = (int16_t)y;
  inst_[b + 5] = (int16_t)frameList;
  return loops != 0;
}

void Engine::getAnimBounds(const Arr<Sprite*>& sp, int idx, int x, int y, int trans, const Arr<int>& out) {
  size_t b = (size_t)idx * 8;
  int minX = INT_MAX, minY = INT_MAX, maxX = INT_MIN, maxY = INT_MIN;
  int p = listOff_[(size_t)(setList_[(size_t)inst_[b + 7]] + inst_[b + 5])];
  int n = (uint8_t)parts_[(size_t)p++];
  while (n-- > 0) {
    int si = (uint8_t)parts_[(size_t)p++];
    int dx = parts_[(size_t)p++];
    int dy = parts_[(size_t)p++];
    Sprite* s = sp[si];
    int px = s->offsetX + dx;
    if (trans & 2) px = -px - s->width;
    int py = s->offsetY + dy;
    if (trans & 1) py = -py - s->height;
    minX = std::min(minX, px);
    minY = std::min(minY, py);
    maxX = std::max(maxX, px + s->width);
    maxY = std::max(maxY, py + s->height);
  }
  out[0] = x + minX;
  out[1] = y + minY;
  out[2] = maxX - minX;
  out[3] = maxY - minY;
}

void Engine::drawAnim(Graphics* g, const Arr<Sprite*>& sp, int idx, int x, int y, int trans) {
  size_t b = (size_t)idx * 8;
  int p = listOff_[(size_t)(setList_[(size_t)inst_[b + 7]] + inst_[b + 5])];
  int n = (uint8_t)parts_[(size_t)p++];
  while (n-- > 0) {
    int si = (uint8_t)parts_[(size_t)p++];
    int dx = parts_[(size_t)p++];
    int dy = parts_[(size_t)p++];
    Sprite* s = sp[si];
    s->offsetX = (int16_t)(s->offsetX + dx);
    s->offsetY = (int16_t)(s->offsetY + dy);
    s->draw(g, x, y, trans);
    s->offsetX = (int16_t)(s->offsetX - dx);
    s->offsetY = (int16_t)(s->offsetY - dy);
  }
}

}  // namespace ch
