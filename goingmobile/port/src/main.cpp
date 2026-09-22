// main.cpp -- Win32 entry point. Boots the MIDlet (ratchetandclank -> Game)
// exactly as startApp() does, then drives the tick/paint loop the platform
// namespace's window replaces MIDP's Display/callSerially machinery with
// (see midp.h). See ../docs/ROADMAP.md for the port's milestone plan.
#include "game.h"
#include "midlet.h"
#include "midp.h"

#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#include <string>

static ratchetandclank* g_midlet = nullptr;

static void onKeyDown(int code) {
  if (g_midlet && g_midlet->b) g_midlet->b->keyPressed(code);
}
static void onKeyUp(int code) {
  if (g_midlet && g_midlet->b) g_midlet->b->keyReleased(code);
}

static std::string exeDir() {
  char buf[MAX_PATH];
  GetModuleFileNameA(nullptr, buf, MAX_PATH);
  std::string p(buf);
  size_t slash = p.find_last_of("\\/");
  return slash == std::string::npos ? "." : p.substr(0, slash);
}

// The canonical build's unpacked jar contents (tools/extract_jar.py ->
// goingmobile/extracted/, gitignored). Tried relative to the exe since the
// build output directory varies (build/, build/Debug/, ...).
static void findDataDir() {
  static const char* kCandidates[] = {
      "extracted",       "../extracted",       "../../extracted",
      "../../../extracted", "../../../../goingmobile/extracted",
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

    g_midlet->b->run_();
    if (g_midlet->b->repaintRequested) {
      g_midlet->b->repaintRequested = false;
      g_midlet->b->paint(&g);
      platform::present();
    }
    if (g_midlet->soundPlayer) g_midlet->soundPlayer->update();
    Sleep(1);
  }

  g_midlet->a_();  // quit: stop the sound thread
  return 0;
}
