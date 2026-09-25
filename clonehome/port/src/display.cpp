#define _CRT_SECURE_NO_WARNINGS
#include "display.h"

#include <cstdio>
#include <cstdlib>
#include <string>

namespace display {

namespace {
Settings g_settings;
bool g_fullscreen = false;
WINDOWPLACEMENT g_windowedPlacement = {sizeof(WINDOWPLACEMENT)};
DWORD g_windowedStyle = 0;

std::string configPath() {
  const char* base = std::getenv("LOCALAPPDATA");
  if (!base) return "";
  return std::string(base) + "\\rac-ch-port\\display.cfg";
}

struct Viewport {
  int x, y, w, h;
};

Viewport computeViewport(int clientW, int clientH, int srcW, int srcH) {
  if (clientW <= 0 || clientH <= 0) return {0, 0, clientW, clientH};
  int w, h;
  if (g_settings.scaling == Scaling::Integer && clientW >= srcW && clientH >= srcH) {
    int k = clientW / srcW < clientH / srcH ? clientW / srcW : clientH / srcH;
    w = srcW * k;
    h = srcH * k;
  } else if ((long long)clientW * srcH >= (long long)clientH * srcW) {
    h = clientH;  // pillarbox
    w = (int)((long long)clientH * srcW / srcH);
  } else {
    w = clientW;  // letterbox
    h = (int)((long long)clientW * srcH / srcW);
  }
  return {(clientW - w) / 2, (clientH - h) / 2, w, h};
}
}  // namespace

Settings& settings() { return g_settings; }

void load() {
  std::string path = configPath();
  if (path.empty()) return;
  FILE* f = std::fopen(path.c_str(), "r");
  if (!f) return;
  int fs = 0, sc = 0, hz = 25, asp = (int)Aspect::Auto;
  if (std::fscanf(f, "%d %d %d %d", &fs, &sc, &hz, &asp) >= 3) {
    if (asp >= 0 && asp < (int)Aspect::Count) g_settings.aspect = (Aspect)asp;
    g_settings.fullscreen = fs != 0;
    g_settings.scaling = sc == 1 ? Scaling::Integer : Scaling::Fit;
    g_settings.hz = hz >= 5 && hz <= 120 ? hz : 25;
  }
  std::fclose(f);
}

void save() {
  std::string path = configPath();
  if (path.empty()) return;
  std::string dir = path.substr(0, path.find_last_of('\\'));
  CreateDirectoryA(dir.c_str(), nullptr);
  FILE* f = std::fopen(path.c_str(), "w");
  if (!f) return;
  std::fprintf(f, "%d %d %d\n", g_fullscreen ? 1 : 0, g_settings.scaling == Scaling::Integer ? 1 : 0, g_settings.hz);
  std::fclose(f);
}

bool isFullscreen() { return g_fullscreen; }

void toggleFullscreen(HWND hwnd) {
  if (!hwnd) return;
  if (!g_fullscreen) {
    g_windowedStyle = (DWORD)GetWindowLongW(hwnd, GWL_STYLE);
    GetWindowPlacement(hwnd, &g_windowedPlacement);
    MONITORINFO mi = {sizeof(mi)};
    if (!GetMonitorInfoW(MonitorFromWindow(hwnd, MONITOR_DEFAULTTONEAREST), &mi)) return;
    SetWindowLongW(hwnd, GWL_STYLE, (LONG)(g_windowedStyle & ~WS_OVERLAPPEDWINDOW) | (LONG)WS_POPUP);
    SetWindowPos(hwnd, HWND_TOP, mi.rcMonitor.left, mi.rcMonitor.top, mi.rcMonitor.right - mi.rcMonitor.left,
                 mi.rcMonitor.bottom - mi.rcMonitor.top, SWP_FRAMECHANGED | SWP_NOOWNERZORDER | SWP_SHOWWINDOW);
    g_fullscreen = true;
  } else {
    SetWindowLongW(hwnd, GWL_STYLE, (LONG)g_windowedStyle);
    SetWindowPlacement(hwnd, &g_windowedPlacement);
    SetWindowPos(hwnd, nullptr, 0, 0, 0, 0,
                 SWP_NOMOVE | SWP_NOSIZE | SWP_NOZORDER | SWP_NOOWNERZORDER | SWP_FRAMECHANGED);
    g_fullscreen = false;
  }
  g_settings.fullscreen = g_fullscreen;
  save();
  InvalidateRect(hwnd, nullptr, TRUE);
}

void cycleScaling(HWND hwnd) {
  g_settings.scaling = g_settings.scaling == Scaling::Fit ? Scaling::Integer : Scaling::Fit;
  save();
  InvalidateRect(hwnd, nullptr, TRUE);
}

void cycleAspect(HWND hwnd, int dir) {
  int n = (int)Aspect::Count;
  g_settings.aspect = (Aspect)(((int)g_settings.aspect + dir + n) % n);
  save();
  InvalidateRect(hwnd, nullptr, TRUE);
}

const wchar_t* aspectName() {
  switch (g_settings.aspect) {
    case Aspect::Original: return L"Original";
    case Aspect::Auto: return L"Auto";
    case Aspect::R4_3: return L"4:3";
    case Aspect::R16_10: return L"16:10";
    case Aspect::R16_9: return L"16:9";
    case Aspect::R21_9: return L"21:9";
    default: return L"";
  }
}

int logicalWidth(int clientW, int clientH) {
  const int H = ch::kScreenH, base = ch::kScreenW, maxW = H * 24 / 9;
  auto ratio = [&](int num, int den) { return (H * num + den / 2) / den; };
  int w = base;
  switch (g_settings.aspect) {
    case Aspect::R4_3: w = ratio(4, 3); break;
    case Aspect::R16_10: w = ratio(16, 10); break;
    case Aspect::R16_9: w = ratio(16, 9); break;
    case Aspect::R21_9: w = ratio(21, 9); break;
    case Aspect::Auto:
      if (clientW > 0 && clientH > 0) w = (int)((long long)H * clientW / clientH);
      break;
    default: break;
  }
  return w < base ? base : w > maxW ? maxW : w;
}

const wchar_t* scalingName() { return g_settings.scaling == Scaling::Fit ? L"Fit" : L"Integer"; }

HWND createWindow(HINSTANCE inst, WNDPROC proc, bool fullscreen) {
  const wchar_t* kClassName = L"CloneHomePortWindow";
  WNDCLASSW wc = {};
  wc.lpfnWndProc = proc;
  wc.hInstance = inst;
  wc.hCursor = LoadCursor(nullptr, IDC_ARROW);
  wc.lpszClassName = kClassName;
  RegisterClassW(&wc);

  // Largest whole multiple of 240x320 that fits the desktop (at most 3x).
  RECT work = {};
  SystemParametersInfoW(SPI_GETWORKAREA, 0, &work, 0);
  int k = (work.bottom - work.top - 60) / ch::kScreenH;
  k = k < 1 ? 1 : k > 3 ? 3 : k;
  RECT rc = {0, 0, ch::kScreenW * k, ch::kScreenH * k};
  AdjustWindowRect(&rc, WS_OVERLAPPEDWINDOW, FALSE);
  HWND hwnd = CreateWindowExW(0, kClassName, L"Ratchet and Clank: Clone Home Port", WS_OVERLAPPEDWINDOW,
                              CW_USEDEFAULT, CW_USEDEFAULT, rc.right - rc.left, rc.bottom - rc.top, nullptr, nullptr,
                              inst, nullptr);
  if (!hwnd) return nullptr;
  ShowWindow(hwnd, SW_SHOWDEFAULT);
  if (fullscreen) toggleFullscreen(hwnd);  // windowed placement is captured first, so F11 returns to it
  return hwnd;
}

void present(HWND hwnd, const ch::Surface& s) {
  HDC dc = GetDC(hwnd);
  RECT rc;
  GetClientRect(hwnd, &rc);
  int cw = rc.right - rc.left, chh = rc.bottom - rc.top;
  Viewport v = computeViewport(cw, chh, s.w, s.h);

  HBRUSH black = (HBRUSH)GetStockObject(BLACK_BRUSH);
  RECT bars[4] = {{0, 0, cw, v.y}, {0, v.y + v.h, cw, chh}, {0, v.y, v.x, v.y + v.h}, {v.x + v.w, v.y, cw, v.y + v.h}};
  for (const RECT& b : bars)
    if (b.right > b.left && b.bottom > b.top) FillRect(dc, &b, black);

  BITMAPINFO bi = {};
  bi.bmiHeader.biSize = sizeof bi.bmiHeader;
  bi.bmiHeader.biWidth = s.w;
  bi.bmiHeader.biHeight = -s.h;
  bi.bmiHeader.biPlanes = 1;
  bi.bmiHeader.biBitCount = 32;
  SetStretchBltMode(dc, COLORONCOLOR);  // nearest neighbour: keeps the pixel art crisp
  StretchDIBits(dc, v.x, v.y, v.w, v.h, 0, 0, s.w, s.h, s.px.data(), &bi, DIB_RGB_COLORS, SRCCOPY);
  ReleaseDC(hwnd, dc);
}

}  // namespace display
