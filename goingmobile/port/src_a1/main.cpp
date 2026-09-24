// main.cpp -- Win32 entry point for the a1 port (milestone 3.1a). Boots the
// MIDlet exactly as startApp() does, then drives CanvasShell's tick/paint
// loop at the same ~33ms cadence as its own Thread.sleep(33) (see
// src_a1/CanvasShell.java's run()) -- the platform namespace's window
// replaces MIDP's Display/callSerially machinery, same simplification the
// legacy build's main.cpp makes (see ../src/main.cpp).
#include "canvasshell.h"
#include "midlet.h"
#include "midp.h"

#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#include <string>

static ratchetandclank* g_midlet = nullptr;

static void onKeyDown(int code) {
  if (g_midlet && g_midlet->canvas) g_midlet->canvas->keyPressed(code);
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

int WINAPI wWinMain(HINSTANCE, HINSTANCE, PWSTR, int) {
  findDataDir();

  if (!platform::initWindow()) return 0;
  platform::setKeyCallback(onKeyDown, onKeyUp);

  g_midlet = new ratchetandclank();
  g_midlet->startApp();

  Graphics g(&platform::canvas);
  while (!platform::quitRequested()) {
    platform::pumpEvents();
    if (platform::quitRequested()) break;

    if (g_midlet->canvas) {
      g_midlet->canvas->tick();
      g_midlet->canvas->paint(&g);
      platform::present();
    }
    if (g_midlet->soundPlayer) g_midlet->soundPlayer->update();
    Sleep(33);
  }

  return 0;
}
