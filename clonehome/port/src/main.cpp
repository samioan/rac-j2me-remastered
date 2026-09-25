#define _CRT_SECURE_NO_WARNINGS
#include <windows.h>
#include <shellapi.h>
#include <dbghelp.h>

#include <cstdio>
#include <string>

#include "gen/classes.h"

using namespace ch;

// Writes crash.txt with the faulting stack (needs the .pdb next to the exe for names).
static LONG WINAPI crashHandler(EXCEPTION_POINTERS* ep) {
  FILE* f = std::fopen("crash.txt", "w");
  if (!f) return EXCEPTION_EXECUTE_HANDLER;
  std::fprintf(f, "exception %08lx at %p\n", ep->ExceptionRecord->ExceptionCode, ep->ExceptionRecord->ExceptionAddress);
  HANDLE proc = GetCurrentProcess(), thread = GetCurrentThread();
  SymSetOptions(SYMOPT_LOAD_LINES | SYMOPT_UNDNAME);
  SymInitialize(proc, nullptr, TRUE);
  CONTEXT ctx = *ep->ContextRecord;
  STACKFRAME64 sf = {};
  sf.AddrPC.Offset = ctx.Rip;
  sf.AddrFrame.Offset = ctx.Rbp;
  sf.AddrStack.Offset = ctx.Rsp;
  sf.AddrPC.Mode = sf.AddrFrame.Mode = sf.AddrStack.Mode = AddrModeFlat;
  alignas(SYMBOL_INFO) char buf[sizeof(SYMBOL_INFO) + 256];
  SYMBOL_INFO* sym = (SYMBOL_INFO*)buf;
  for (int i = 0; i < 40; i++) {
    if (!StackWalk64(IMAGE_FILE_MACHINE_AMD64, proc, thread, &sf, &ctx, nullptr, SymFunctionTableAccess64,
                     SymGetModuleBase64, nullptr) || !sf.AddrPC.Offset)
      break;
    sym->SizeOfStruct = sizeof(SYMBOL_INFO);
    sym->MaxNameLen = 255;
    DWORD64 disp = 0;
    DWORD lineDisp = 0;
    IMAGEHLP_LINE64 line = {sizeof line};
    if (SymFromAddr(proc, sf.AddrPC.Offset, &disp, sym)) {
      bool hasLine = SymGetLineFromAddr64(proc, sf.AddrPC.Offset, &lineDisp, &line) != 0;
      std::fprintf(f, "%2d %s +0x%llx  %s:%lu\n", i, sym->Name, (unsigned long long)disp,
                   hasLine ? line.FileName : "?", hasLine ? line.LineNumber : 0);
    }
  }
  std::fclose(f);
  return EXCEPTION_EXECUTE_HANDLER;
}

static Game* g_game;
static Surface g_screen;
static int g_scale = 2;

static int midpKey(WPARAM vk) {
  switch (vk) {
    case VK_UP: return -1;
    case VK_DOWN: return -2;
    case VK_LEFT: return -3;
    case VK_RIGHT: return -4;
    case VK_RETURN: case VK_SPACE: return -5;
    case VK_F1: return -6;
    case VK_F2: case VK_ESCAPE: return -7;
    case VK_MULTIPLY: return 42;
  }
  if (vk >= '0' && vk <= '9') return (int)vk;
  if (vk >= VK_NUMPAD0 && vk <= VK_NUMPAD9) return (int)(48 + vk - VK_NUMPAD0);
  return 0;
}

static void present(HDC dc) {
  BITMAPINFO bi = {};
  bi.bmiHeader.biSize = sizeof bi.bmiHeader;
  bi.bmiHeader.biWidth = kScreenW;
  bi.bmiHeader.biHeight = -kScreenH;
  bi.bmiHeader.biPlanes = 1;
  bi.bmiHeader.biBitCount = 32;
  StretchDIBits(dc, 0, 0, kScreenW * g_scale, kScreenH * g_scale, 0, 0, kScreenW, kScreenH, g_screen.px.data(), &bi,
                DIB_RGB_COLORS, SRCCOPY);
}

static LRESULT CALLBACK WindowProc(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam) {
  switch (msg) {
    case WM_PAINT: {
      PAINTSTRUCT ps;
      present(BeginPaint(hwnd, &ps));
      EndPaint(hwnd, &ps);
      return 0;
    }
    case WM_KEYDOWN:
      if (!(lParam & (1 << 30)) && g_game) g_game->platformKey(midpKey(wParam), true);
      return 0;
    case WM_KEYUP:
      if (g_game) g_game->platformKey(midpKey(wParam), false);
      return 0;
    case WM_KILLFOCUS:
      if (g_game) g_game->hideNotify();
      return 0;
    case WM_SETFOCUS:
      if (g_game) g_game->showNotify();
      return 0;
    case WM_DESTROY:
      PostQuitMessage(0);
      return 0;
  }
  return DefWindowProcW(hwnd, msg, wParam, lParam);
}

static std::string narrow(const wchar_t* w) {
  char b[1024];
  WideCharToMultiByte(CP_UTF8, 0, w, -1, b, sizeof b, nullptr, nullptr);
  return b;
}

// --data <dir holding RP1..RP3> (default: exe folder), --saves <dir>,
// --dump <file.bmp> [--frames N] [--press K@F[:HOLD] ...] [--level W:S] [--fuzz SEED]: run N ticks headless and
// write a screenshot (K is a MIDP key code, F the frame it is pressed on).
int WINAPI wWinMain(HINSTANCE hInstance, HINSTANCE, PWSTR, int nCmdShow) {
  std::string dump;
  int frames = 30, argc;
  struct Press { int key, frame, hold; };
  std::vector<Press> presses;
  unsigned fuzzSeed = 0;
  int jumpWorld = -1, jumpSection = 0;
  wchar_t** argv = CommandLineToArgvW(GetCommandLineW(), &argc);
  for (int i = 1; i + 1 < argc; i++) {
    if (!wcscmp(argv[i], L"--data")) Platform::dataDir = narrow(argv[++i]);
    else if (!wcscmp(argv[i], L"--saves")) Platform::saveDir = narrow(argv[++i]);
    else if (!wcscmp(argv[i], L"--dump")) dump = narrow(argv[++i]);
    else if (!wcscmp(argv[i], L"--frames")) frames = _wtoi(argv[++i]);
    else if (!wcscmp(argv[i], L"--level")) swscanf(argv[++i], L"%d:%d", &jumpWorld, &jumpSection);
    else if (!wcscmp(argv[i], L"--fuzz")) fuzzSeed = (unsigned)_wtoi(argv[++i]);
    else if (!wcscmp(argv[i], L"--press")) {
      int k = 0, f = 0, h = 3;
      swscanf(argv[++i], L"%d@%d:%d", &k, &f, &h);
      presses.push_back({k, f, h});
    }
  }
  // Default data folder: next to the exe, in ./data, or in the repo's clonehome/extracted.
  {
    wchar_t exe[MAX_PATH];
    GetModuleFileNameW(nullptr, exe, MAX_PATH);
    std::string dir = narrow(exe);
    dir = dir.substr(0, dir.find_last_of("\\/"));
    if (Platform::dataDir == ".") {
      for (const char* rel : {"", "/data", "/../extracted", "/../../extracted", "/../../../clonehome/extracted"}) {
        std::string cand = dir + rel;
        if (GetFileAttributesA((cand + "/RP1").c_str()) != INVALID_FILE_ATTRIBUTES) {
          Platform::dataDir = cand;
          break;
        }
      }
    }
    if (Platform::saveDir == "saves") Platform::saveDir = dir + "/saves";
  }
  SetUnhandledExceptionFilter(crashHandler);
  RatchetMIDlet* midlet = new RatchetMIDlet();
  g_game = RatchetMIDlet::game;
  if (!g_game->ok()) {
    MessageBoxW(nullptr, L"Could not find the game data (files RP1, RP2, RP3 from the original jar).\nPut them next to this exe, or run with --data <folder>.", L"Clone Home",
                MB_ICONERROR);
    return 1;
  }
  midlet->startApp();

  if (!dump.empty()) {
    for (int i = 0; i < frames; i++) {
      for (auto& p : presses) {
        if (p.frame == i) g_game->platformKey(p.key, true);
        if (p.frame + p.hold == i) g_game->platformKey(p.key, false);
      }
      if (jumpWorld >= 0 && i == 350) {  // debug: skip the menus and start in the given world/section
        Game::worldId = jumpWorld;
        g_game->inArena = jumpWorld >= 15;
        g_game->enterLevel(jumpWorld, jumpSection);
      }
      if (fuzzSeed) {  // random key toggles: finds crashes in the translated game logic
        static const int keys[] = {-1, -2, -3, -4, -4, -3, -5, -5, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 42, 35};
        fuzzSeed = fuzzSeed * 1664525u + 1013904223u;
        if (i > 500 && (fuzzSeed >> 24) % 3 == 0) {
          fuzzSeed = fuzzSeed * 1664525u + 1013904223u;
          g_game->platformKey(keys[(fuzzSeed >> 16) % (sizeof keys / sizeof *keys)], ((fuzzSeed >> 8) & 1) != 0);
        }
      }
      g_game->runFrame(i * 40L, g_screen);
    }
    return writeBmp(g_screen, dump.c_str()) ? 0 : 1;
  }

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
  while (!g_game->quit) {
    while (PeekMessageW(&msg, nullptr, 0, 0, PM_REMOVE)) {
      if (msg.message == WM_QUIT) return 0;
      TranslateMessage(&msg);
      DispatchMessageW(&msg);
    }
    int sleepMs = g_game->runFrame((long)GetTickCount64(), g_screen);
    HDC dc = GetDC(hwnd);
    present(dc);
    ReleaseDC(hwnd, dc);
    Sleep(sleepMs > 0 ? sleepMs : 1);
  }
  return 0;
}
