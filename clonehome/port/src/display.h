// Window presentation: borderless fullscreen, scaling policy and saved display settings
// (modelled on the Going Mobile port).
#pragma once
#include <windows.h>

#include "gfx.h"

namespace display {

enum class Scaling { Fit, Integer };  // Fit: largest size keeping the aspect ratio; Integer: whole multiples only

// Resolution: the canvas keeps the original 320 px height and widens to the chosen aspect ratio (the level
// simply shows more to the sides). Auto follows the window's shape.
enum class Aspect { Original, Auto, R4_3, R16_10, R16_9, R21_9, Count };

struct Settings {
  bool fullscreen = false;
  Scaling scaling = Scaling::Fit;
  int hz = 25;
  Aspect aspect = Aspect::Auto;
};

Settings& settings();
void load();  // %LOCALAPPDATA%\rac-ch-port\display.cfg
void save();

// Creates the window (resizable, first sized to fit the desktop) and enters fullscreen if saved.
HWND createWindow(HINSTANCE inst, WNDPROC proc, bool fullscreen);
void toggleFullscreen(HWND hwnd);
bool isFullscreen();
void cycleScaling(HWND hwnd);
const wchar_t* scalingName();
void cycleAspect(HWND hwnd, int dir);
const wchar_t* aspectName();
// Logical canvas width for the current mode and window client size.
int logicalWidth(int clientW, int clientH);

// Draws the surface into the window: scaled per the policy, centred, black bars around it.
void present(HWND hwnd, const ch::Surface& s);

}  // namespace display
