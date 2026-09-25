// screen.cpp -- see screen.h.
#include "screen.h"
#include "midp.h"

#include <algorithm>
#include <cstdio>
#include <cstdlib>

namespace screen {

int width = kBaseW;

namespace {
Mode g_mode = Mode::Original;

std::string configPath() {
  const char* base = getenv("LOCALAPPDATA");
  if (!base) return "";
  return std::string(base) + "\\rac-gm-port-a1\\display.cfg";
}

// Logical width for a mode; Auto follows the window's aspect ratio.
int widthFor(Mode m, int clientW, int clientH) {
  auto ratio = [](int num, int den) { return (kHeight * num + den / 2) / den; };
  switch (m) {
    case Mode::R4_3: return ratio(4, 3);
    case Mode::R16_10: return ratio(16, 10);
    case Mode::R16_9: return ratio(16, 9);
    case Mode::R21_9: return ratio(21, 9);
    case Mode::Auto:
      if (clientW <= 0 || clientH <= 0) return kBaseW;
      return std::clamp((int)((long long)kHeight * clientW / clientH), kBaseW, ratio(24, 9));
    default: return kBaseW;
  }
}

const char* modeName(Mode m) {
  switch (m) {
    case Mode::Original: return "Original";
    case Mode::Auto: return "Auto";
    case Mode::R4_3: return "4:3";
    case Mode::R16_10: return "16:10";
    case Mode::R16_9: return "16:9";
    case Mode::R21_9: return "21:9";
    default: return "";
  }
}

const int kFpsChoices[] = {0, 60, 90, 120, 144, 165, 240, -1};
constexpr int kFpsCount = sizeof(kFpsChoices) / sizeof(kFpsChoices[0]);
int g_fpsIdx = 0;

void save() {
  std::string path = configPath();
  if (path.empty()) return;
  FILE* f = nullptr;
  if (fopen_s(&f, path.c_str(), "w") == 0 && f) {
    fprintf(f, "%d %d\n", (int)g_mode, g_fpsIdx);
    fclose(f);
  }
}
}  // namespace

int fpsTarget() { return kFpsChoices[g_fpsIdx]; }

void cycleFps(int dir) {
  g_fpsIdx = (g_fpsIdx + dir + kFpsCount) % kFpsCount;
  save();
}

std::string fpsLabel() {
  int v = fpsTarget();
  if (v == 0) return "FPS: Original";
  if (v < 0) return "FPS: Unlimited";
  return "FPS: " + std::to_string(v);
}

Mode mode() { return g_mode; }

void setMode(Mode m) {
  g_mode = m;
  save();
}

void cycleMode(int dir) {
  int n = (int)Mode::Count;
  setMode((Mode)(((int)g_mode + dir + n) % n));
}

std::string modeLabel() { return std::string("Resolution: ") + modeName(g_mode); }

void load() {
  std::string path = configPath();
  FILE* f = nullptr;
  if (path.empty() || fopen_s(&f, path.c_str(), "r") != 0 || !f) return;
  int v = -1;
  if (fscanf_s(f, "%d", &v) == 1 && v >= 0 && v < (int)Mode::Count) g_mode = (Mode)v;
  if (fscanf_s(f, "%d", &v) == 1 && v >= 0 && v < kFpsCount) g_fpsIdx = v;
  fclose(f);
}

namespace {
uint32_t g_edgeL[kHeight], g_edgeR[kHeight];
bool g_edgesValid = false;
}

void beginFrame() { g_edgesValid = false; }

void captureEdges() {
  const Image& c = platform::canvas;
  int off = offsetX();
  if (off <= 0 || c.w < off + kBaseW || c.h < kHeight) return;
  for (int y = 0; y < kHeight; y++) {
    g_edgeL[y] = c.px[(size_t)y * c.w + off];
    g_edgeR[y] = c.px[(size_t)y * c.w + off + kBaseW - 1];
  }
  g_edgesValid = true;
}

void paintSideBars() {
  Image& c = platform::canvas;
  int off = offsetX();
  if (off <= 0 || c.h < kHeight) return;
  for (int y = 0; y < kHeight; y++) {
    uint32_t* row = &c.px[(size_t)y * c.w];
    uint32_t l = g_edgesValid ? g_edgeL[y] : 0xFF000000u, r = g_edgesValid ? g_edgeR[y] : 0xFF000000u;
    std::fill(row, row + off, l);
    std::fill(row + off + kBaseW, row + c.w, r);
  }
}

bool update() {
  int cw = 0, ch = 0;
  platform::clientSize(cw, ch);
  int want = widthFor(g_mode, cw, ch);
  if (want == width) return false;
  width = want;
  platform::resizeCanvas(width, kHeight);
  return true;
}

}  // namespace screen
