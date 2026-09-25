#define _CRT_SECURE_NO_WARNINGS
#include <windows.h>
#include <shellapi.h>
#include <dbghelp.h>
#include <mmsystem.h>

#include <cstdio>
#include <string>

#include "gen/classes.h"
#include "display.h"
#include "input.h"

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

static void gameKeyDown(int code) { if (g_game) g_game->platformKey(code, true); }
static void gameKeyUp(int code) { if (g_game) g_game->platformKey(code, false); }
// State 0 is live play (16/17/24 are the level-start transitions); every other state is a menu.
static input::Context gameContext() {
  int st = Game::state;
  return (st == 0 || st == 16 || st == 17 || st == 24) ? input::Context::Gameplay : input::Context::Menu;
}

// WM_KEYDOWN reports VK_SHIFT/VK_CONTROL without a side; the bindings use the left ones.
static unsigned resolveKey(WPARAM vk, LPARAM lParam) {
  if (vk == VK_SHIFT || vk == VK_CONTROL || vk == VK_MENU)
    return MapVirtualKeyW((UINT)((lParam >> 16) & 0xFF), MAPVK_VSC_TO_VK_EX);
  return (unsigned)vk;
}

static void updateTitle(HWND hwnd) {
  wchar_t title[128];
  swprintf(title, 128, L"Ratchet and Clank: Clone Home Port  [%d Hz | %s | %s scaling]", g_game ? g_game->targetFps : 0,
           display::aspectName(), display::scalingName());
  SetWindowTextW(hwnd, title);
}

static LRESULT CALLBACK WindowProc(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam) {
  switch (msg) {
    case WM_ERASEBKGND:
      return 1;  // present() paints the bars itself
    case WM_PAINT: {
      PAINTSTRUCT ps;
      BeginPaint(hwnd, &ps);
      EndPaint(hwnd, &ps);
      display::present(hwnd, g_screen);
      return 0;
    }
    case WM_SETCURSOR:
      if (display::isFullscreen() && LOWORD(lParam) == HTCLIENT) {
        SetCursor(nullptr);
        return TRUE;
      }
      return DefWindowProcW(hwnd, msg, wParam, lParam);
    case WM_SYSKEYDOWN:
      if (wParam == VK_RETURN && (lParam & (1 << 29))) {  // Alt+Enter
        display::toggleFullscreen(hwnd);
        return 0;
      }
      return DefWindowProcW(hwnd, msg, wParam, lParam);
    case WM_KEYDOWN:
      if (wParam == VK_F11) {
        if (!(lParam & (1 << 30))) display::toggleFullscreen(hwnd);
        return 0;
      }
      if (wParam == VK_F8) {  // resolution: cycle the canvas aspect (Shift+F8 goes back)
        if (!(lParam & (1 << 30))) {
          display::cycleAspect(hwnd, GetKeyState(VK_SHIFT) < 0 ? -1 : 1);
          updateTitle(hwnd);
        }
        return 0;
      }
      if (wParam == VK_F7) {  // scaling: fit <-> integer multiples
        if (!(lParam & (1 << 30))) {
          display::cycleScaling(hwnd);
          updateTitle(hwnd);
        }
        return 0;
      }
      if (wParam == VK_F5 || wParam == VK_F6) {  // game speed: F5 slower, F6 faster
        if (g_game) {
          int hz = g_game->targetFps + (wParam == VK_F6 ? 5 : -5);
          g_game->targetFps = hz < 5 ? 5 : hz > 120 ? 120 : hz;
          display::settings().hz = g_game->targetFps;
          display::save();
          updateTitle(hwnd);
        }
        return 0;
      }
      input::keyEvent(resolveKey(wParam, lParam), true, (lParam & (1 << 30)) != 0);
      return 0;
    case WM_KEYUP:
      input::keyEvent(resolveKey(wParam, lParam), false, false);
      return 0;
    case WM_LBUTTONDOWN: input::keyEvent(VK_LBUTTON, true, false); return 0;
    case WM_LBUTTONUP: input::keyEvent(VK_LBUTTON, false, false); return 0;
    case WM_RBUTTONDOWN: input::keyEvent(VK_RBUTTON, true, false); return 0;
    case WM_RBUTTONUP: input::keyEvent(VK_RBUTTON, false, false); return 0;
    case WM_KILLFOCUS:
      input::releaseAll();
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
int WINAPI wWinMain(HINSTANCE hInstance, HINSTANCE, PWSTR, int) {
  std::string dump;
  int frames = 30, argc;
  struct Press { int key, frame, hold; };
  std::vector<Press> presses;
  unsigned fuzzSeed = 0;
  int startHz = 0;  // 0 = saved setting
  int fullscreenArg = -1;
  int headlessWidth = kScreenW;
  bool stats = false;
  int jumpWorld = -1, jumpSection = 0;
  wchar_t** argvRaw = CommandLineToArgvW(GetCommandLineW(), &argc);
  std::vector<const wchar_t*> argv(argvRaw, argvRaw + argc);
  argv.push_back(L"");  // value options at the very end read an empty string instead of running off the array
  for (int i = 1; i < argc; i++) {
    if (!wcscmp(argv[i], L"--data")) Platform::dataDir = narrow(argv[++i]);
    else if (!wcscmp(argv[i], L"--saves")) Platform::saveDir = narrow(argv[++i]);
    else if (!wcscmp(argv[i], L"--dump")) dump = narrow(argv[++i]);
    else if (!wcscmp(argv[i], L"--frames")) frames = _wtoi(argv[++i]);
    else if (!wcscmp(argv[i], L"--stats")) stats = true;
    else if (!wcscmp(argv[i], L"--width")) headlessWidth = _wtoi(argv[++i]);
    else if (!wcscmp(argv[i], L"--fullscreen")) fullscreenArg = 1;
    else if (!wcscmp(argv[i], L"--windowed")) fullscreenArg = 0;
    else if (!wcscmp(argv[i], L"--hz")) startHz = _wtoi(argv[++i]);
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
  input::init(gameKeyDown, gameKeyUp, gameContext);

  if (!dump.empty()) {
    if (headlessWidth != kScreenW) g_screen.resize(headlessWidth, kScreenH);
    Game::viewW = g_screen.w;
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
      g_game->runFrame(i * 50L, g_screen);
    }
    return writeBmp(g_screen, dump.c_str()) ? 0 : 1;
  }

  display::load();
  bool wantFullscreen = fullscreenArg >= 0 ? fullscreenArg == 1 : display::settings().fullscreen;
  HWND hwnd = display::createWindow(hInstance, WindowProc, wantFullscreen);
  if (!hwnd) return 0;
  if (startHz == 0) startHz = display::settings().hz;

  timeBeginPeriod(1);  // 1 ms timer/Sleep resolution for steady frame pacing
  g_game->realTime = true;
  g_game->targetFps = startHz < 5 ? 5 : startHz > 120 ? 120 : startHz;
  MSG msg = {};
  long statFrames = 0;
  bool jumped = false;
  DWORD statStart = timeGetTime();
  while (!g_game->quit) {
    while (PeekMessageW(&msg, nullptr, 0, 0, PM_REMOVE)) {
      if (msg.message == WM_QUIT) {
        if (stats) {
          FILE* f = std::fopen("perf.log", "w");
          if (f) {
            DWORD ms = timeGetTime() - statStart;
            std::fprintf(f, "%ld frames in %lu ms = %.2f fps (target %d)\n", statFrames, ms, statFrames * 1000.0 / ms, g_game->targetFps);
            std::fclose(f);
          }
        }
        return 0;
      }
      TranslateMessage(&msg);
      DispatchMessageW(&msg);
    }
    if (jumpWorld >= 0 && !jumped && Game::state == 105) {  // debug: --level skips the menus in the live window too
      jumped = true;
      Game::worldId = jumpWorld;
      g_game->inArena = jumpWorld >= 15;
      g_game->enterLevel(jumpWorld, jumpSection);
    }
    {
      RECT crc;
      GetClientRect(hwnd, &crc);
      int lw = display::logicalWidth(crc.right - crc.left, crc.bottom - crc.top);
      if (lw != g_screen.w) g_screen.resize(lw, kScreenH);
      Game::viewW = g_screen.w;
    }
    input::poll();
    int sleepMs = g_game->runFrame((long)timeGetTime(), g_screen);
    statFrames++;
    display::present(hwnd, g_screen);
    Sleep(sleepMs > 0 ? sleepMs : 1);
  }
  return 0;
}
