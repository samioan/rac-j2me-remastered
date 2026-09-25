// main.cpp -- Win32 entry point for the a1 port (milestone 3.1a). Boots the
// MIDlet exactly as startApp() does, then drives CanvasShell's tick/paint
// loop at the same ~33ms cadence as its own Thread.sleep(33) (see
// src_a1/CanvasShell.java's run()) -- the platform namespace's window
// replaces MIDP's Display/callSerially machinery, same simplification the
// legacy build's main.cpp makes (see ../src/main.cpp).
#include "canvasshell.h"
#include "midlet.h"
#include "game.h"
#include "intromanager.h"
#include "input.h"
#include "screen.h"
#include "midp.h"

#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#include <mmsystem.h>
#include <string>
#include <cstdio>
#include <cstdlib>
#pragma comment(lib, "winmm.lib")
#ifdef _DEBUG
#include <crtdbg.h>
#include <dbghelp.h>
#pragma comment(lib, "dbghelp.lib")

// Debug-build aid: a failed CRT assertion (vector range checks etc.) writes a symbolised
// stack to crash.txt next to the exe, then terminates, instead of only showing the dialog.
static int crtReportHook(int reportType, char* message, int* returnValue) {
  if (reportType != _CRT_ASSERT && reportType != _CRT_ERROR) return FALSE;
  FILE* f = nullptr;
  if (fopen_s(&f, "crash.txt", "w") == 0 && f) {
    fprintf(f, "%s\n", message ? message : "");
    HANDLE proc = GetCurrentProcess();
    SymSetOptions(SYMOPT_LOAD_LINES | SYMOPT_UNDNAME);
    SymInitialize(proc, nullptr, TRUE);
    void* frames[48];
    USHORT n = CaptureStackBackTrace(0, 48, frames, nullptr);
    for (USHORT i = 0; i < n; i++) {
      char buf[sizeof(SYMBOL_INFO) + 256] = {};
      SYMBOL_INFO* sym = (SYMBOL_INFO*)buf;
      sym->SizeOfStruct = sizeof(SYMBOL_INFO);
      sym->MaxNameLen = 255;
      DWORD64 addr = (DWORD64)frames[i];
      DWORD64 disp = 0;
      IMAGEHLP_LINE64 line = {sizeof(line)};
      DWORD ld = 0;
      const char* name = SymFromAddr(proc, addr, &disp, sym) ? sym->Name : "?";
      if (SymGetLineFromAddr64(proc, addr, &ld, &line))
        fprintf(f, "#%d %s (%s:%lu)\n", i, name, line.FileName, line.LineNumber);
      else
        fprintf(f, "#%d %s\n", i, name);
    }
    fclose(f);
  }
  if (returnValue) *returnValue = 0;
  TerminateProcess(GetCurrentProcess(), 3);
  return TRUE;
}
#endif

static ratchetandclank* g_midlet = nullptr;

static void onKeyDown(int code) {
  if (g_midlet && g_midlet->canvas) g_midlet->canvas->keyPressed(code);
}
// Which mapping the bindings use right now: live gameplay, menu-style screens (menus,
// dialogue, cutscenes, weapon wheel), or typing a name.
static input::Context currentContext() {
  if (!g_midlet) return input::Context::Menu;
  if (g_midlet->gameStarted) {
    Game* game = g_midlet->game;
    return (game && game->b == 0 && !game->cD) ? input::Context::Gameplay : input::Context::Menu;
  }
  IntroManager* im = g_midlet->introManager;
  return (im && im->e == 15) ? input::Context::Text : input::Context::Menu;
}

static void onChar(int ch) {
  if (g_midlet && !g_midlet->gameStarted && g_midlet->introManager) g_midlet->introManager->textInput(ch);
}
static void onKeyUp(int code) {
  if (g_midlet && g_midlet->canvas) g_midlet->canvas->keyReleased(code);
}

static std::string exeDir() {
  char buf[MAX_PATH];
  GetModuleFileNameA(nullptr, buf, MAX_PATH);
  std::string p(buf);
  size_t slash = p.find_last_of("\\/");
  return slash == std::string::npos ? "." : p.substr(0, slash);
}

// a1's unpacked jar contents (tools/extract_jar.py's output on
// roms/RAC-GoingMobile-a1.jar, i.e. goingmobile/extracted_a1/, gitignored).
static void findDataDir() {
  static const char* kCandidates[] = {
      "extracted_a1",          "../extracted_a1",          "../../extracted_a1",
      "../../../extracted_a1", "../../../../goingmobile/extracted_a1",
  };
  std::string base = exeDir();
  for (const char* rel : kCandidates) {
    if (setDataDir(base + "/" + rel)) return;
  }
}

int WINAPI wWinMain(HINSTANCE, HINSTANCE, PWSTR cmdLine, int) {
#ifdef _DEBUG
  _CrtSetReportHook2(_CRT_RPTHOOK_INSTALL, crtReportHook);
#endif
  findDataDir();

  screen::load();
  bool windowed = cmdLine && wcsstr(cmdLine, L"--windowed") != nullptr;
  if (!platform::initWindow(!windowed)) return 0;
  input::init(onKeyDown, onKeyUp, currentContext);
  platform::setCharCallback(onChar);

  g_midlet = new ratchetandclank();
  g_midlet->startApp();

  Graphics g(&platform::canvas);
  timeBeginPeriod(1);
  LARGE_INTEGER freq;
  QueryPerformanceFrequency(&freq);
  long long lastCounter = 0;
  double acc = 0;
  const double kStep = 0.033;
  const char* startLevel = getenv("RAC_LEVEL");  // debug aid: start straight in a level
  int frame = 0;
  while (!platform::quitRequested()) {
    if (startLevel && ++frame == 120) {
      g_midlet->startNewGame(0);
    }
    if (startLevel && frame >= 200 && getenv("RAC_SWEEP") && (frame - 200) % 45 == 0) {
      int room = (frame - 200) / 45;
      if (room < 12) g_midlet->game->loadLevel(atoi(startLevel), (short)room);
      else if (room == 12) { FILE* d = nullptr; if (fopen_s(&d, "sweep_done.txt", "w") == 0 && d) fclose(d); }
    } else if (startLevel && frame == 200) {
      const char* room = getenv("RAC_ROOM");
      g_midlet->game->loadLevel(atoi(startLevel), (short)(room ? atoi(room) : 0));
    }
    platform::pumpEvents();
    input::poll();
    if (screen::update()) {  // widescreen mode / window shape changed: repaint everything
      if (g_midlet->game) g_midlet->game->onScreenResize();
      if (g_midlet->introManager) g_midlet->introManager->f = (jbyte)(g_midlet->introManager->f | 3);
    }
    if (platform::quitRequested()) break;

    const int fps = screen::fpsTarget();
    if (fps == 0) {
      // Original cadence: one tick + one paint per ~33 ms, no interpolation.
      if (g_midlet->game) { g_midlet->game->interpAlpha = 1.0f; g_midlet->game->advanceAnim = true; }
      if (g_midlet->canvas) {
        g_midlet->canvas->tick();
        g_midlet->canvas->paint(&g);
        platform::present();
      }
      if (g_midlet->soundPlayer) g_midlet->soundPlayer->update();
      Sleep(33);
      lastCounter = 0;
    } else {
      // Logic stays on its fixed ~30 Hz step; frames in between are drawn interpolated.
      LARGE_INTEGER now;
      QueryPerformanceCounter(&now);
      if (lastCounter == 0) { lastCounter = now.QuadPart; acc = 0; }
      acc += (double)(now.QuadPart - lastCounter) / (double)freq.QuadPart;
      lastCounter = now.QuadPart;
      if (acc > 0.25) acc = 0.25;
      bool ticked = false;
      Game* game = g_midlet->game;
      bool live = g_midlet->gameStarted && game;
      for (int steps = 0; acc >= kStep && steps < 4; steps++) {
        if (live) game->snapshotForInterp();
        if (g_midlet->canvas) g_midlet->canvas->tick();
        acc -= kStep;
        ticked = true;
      }
      if (acc >= kStep) acc = 0;
      live = g_midlet->gameStarted && game;
      if (live) { game->interpAlpha = (float)(acc / kStep); game->advanceAnim = ticked; }
      else if (game) { game->interpAlpha = 1.0f; game->advanceAnim = true; }
      // Menus and screens are drawn only when the logic advanced; the world every frame.
      if (g_midlet->canvas && (ticked || (live && game->b == 0))) {
        g_midlet->canvas->paint(&g);
        platform::present();
        if (getenv("RAC_FPSLOG")) {  // debug aid: presented frames per second -> fps.txt
          static int n = 0; static DWORD t0 = GetTickCount();
          n++;
          if (GetTickCount() - t0 >= 1000) { FILE* d = nullptr; if (fopen_s(&d, "fps.txt", "a") == 0 && d) { fprintf(d, "%d\n", n); fclose(d); } n = 0; t0 = GetTickCount(); }
        }
      }
      if (g_midlet->soundPlayer) g_midlet->soundPlayer->update();
      // Pace to the target rate: sleep most of the wait, spin the last millisecond.
      if (fps > 0) {
        double period = 1.0 / fps;
        for (;;) {
          LARGE_INTEGER t;
          QueryPerformanceCounter(&t);
          double elapsed = (double)(t.QuadPart - lastCounter) / (double)freq.QuadPart;
          if (elapsed >= period) break;
          if (period - elapsed > 0.002) Sleep(1);
          else if (platform::quitRequested()) break;
        }
      } else {
        Sleep(0);
      }
    }
  }

  return 0;
}
