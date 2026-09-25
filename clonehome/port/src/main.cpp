#define _CRT_SECURE_NO_WARNINGS
#include <windows.h>

#include <cstdio>
#include <cstring>
#include <string>

#include "assets.h"
#include "gfx.h"

using namespace ch;

// Milestone-2 test scene: the player atlas laid out on a grid, then the font.
static void drawTestScene(Assets& a, Surface& s) {
  s.fillRect(0, 0, kScreenW, kScreenH, 0xFF203040);
  Image sheet;
  SpriteSet set = a.loadSprites(1049);
  if (!a.loadImage(set.imageId, nullptr, sheet)) return;
  int x = 4, y = 4, rowH = 0;
  for (auto& r : set.sprites) {
    int cw = r.w + 4, chh = r.h + 4;
    if (x + cw > kScreenW) { x = 4; y += rowH; rowH = 0; }
    s.drawSprite(sheet, r, x - r.offX, y - r.offY, 0);
    x += cw;
    if (chh > rowH) rowH = chh;
  }
  y += rowH + 4;
  Image font;
  SpriteSet fs = a.loadSprites(1040);
  if (!a.loadImage(fs.imageId, nullptr, font)) return;
  x = 4;
  for (auto& r : fs.sprites) {
    if (x + r.w > kScreenW) { x = 4; y += 12; }
    s.drawSprite(font, r, x - r.offX, y - r.offY, 0);
    x += r.w + 1;
  }
}

static Assets g_assets;
static Surface g_screen;
static int g_scale = 2;

static LRESULT CALLBACK WindowProc(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam) {
  switch (msg) {
    case WM_PAINT: {
      PAINTSTRUCT ps;
      HDC dc = BeginPaint(hwnd, &ps);
      BITMAPINFO bi = {};
      bi.bmiHeader.biSize = sizeof bi.bmiHeader;
      bi.bmiHeader.biWidth = kScreenW;
      bi.bmiHeader.biHeight = -kScreenH;
      bi.bmiHeader.biPlanes = 1;
      bi.bmiHeader.biBitCount = 32;
      StretchDIBits(dc, 0, 0, kScreenW * g_scale, kScreenH * g_scale, 0, 0, kScreenW, kScreenH,
                    g_screen.px.data(), &bi, DIB_RGB_COLORS, SRCCOPY);
      EndPaint(hwnd, &ps);
      return 0;
    }
    case WM_DESTROY:
      PostQuitMessage(0);
      return 0;
  }
  return DefWindowProcW(hwnd, msg, wParam, lParam);
}

// Data directory: --data <dir>, else the exe's own directory.
int WINAPI wWinMain(HINSTANCE hInstance, HINSTANCE, PWSTR, int nCmdShow) {
  std::string dir = ".", dump;
  int argc;
  wchar_t** argv = CommandLineToArgvW(GetCommandLineW(), &argc);
  auto narrow = [](const wchar_t* w) {
    char b[1024];
    WideCharToMultiByte(CP_UTF8, 0, w, -1, b, sizeof b, nullptr, nullptr);
    return std::string(b);
  };
  for (int i = 1; i + 1 < argc; i++) {
    if (!wcscmp(argv[i], L"--data")) dir = narrow(argv[++i]);
    else if (!wcscmp(argv[i], L"--dump")) dump = narrow(argv[++i]);
  }
  if (!g_assets.load(dir)) {
    MessageBoxW(nullptr, L"Could not read RP1, RP2, RP3. Use --data <folder containing them>.",
                L"Clone Home", MB_ICONERROR);
    return 1;
  }
  drawTestScene(g_assets, g_screen);
  if (!dump.empty()) return writeBmp(g_screen, dump.c_str()) ? 0 : 1;

  const wchar_t* kClassName = L"CloneHomePortWindow";
  WNDCLASSW wc = {};
  wc.lpfnWndProc = WindowProc;
  wc.hInstance = hInstance;
  wc.hCursor = LoadCursor(nullptr, IDC_ARROW);
  wc.lpszClassName = kClassName;
  RegisterClassW(&wc);

  RECT rc = {0, 0, kScreenW * g_scale, kScreenH * g_scale};
  DWORD style = WS_OVERLAPPED | WS_CAPTION | WS_SYSMENU | WS_MINIMIZEBOX;
  AdjustWindowRect(&rc, style, FALSE);
  HWND hwnd = CreateWindowExW(0, kClassName, L"Ratchet and Clank: Clone Home Port", style, CW_USEDEFAULT,
                              CW_USEDEFAULT, rc.right - rc.left, rc.bottom - rc.top, nullptr, nullptr, hInstance,
                              nullptr);
  if (!hwnd) return 0;
  ShowWindow(hwnd, nCmdShow);
  MSG msg = {};
  while (GetMessageW(&msg, nullptr, 0, 0)) {
    TranslateMessage(&msg);
    DispatchMessageW(&msg);
  }
  return 0;
}
