// screen.h -- logical screen size for the a1 port.
//
// The game was authored for 176x220. Widescreen keeps the height and widens the canvas:
// world rendering (tiles, sprites, camera, HUD bar) uses the full width, while menus, dialogue
// text and other screens laid out in 176x220 design coordinates are drawn centred at
// offsetX() with no scaling, so no sprite or HUD element is ever stretched.
#pragma once
#include <string>

namespace screen {

constexpr int kBaseW = 176;   // design width
constexpr int kHeight = 220;

enum class Mode { Original, Auto, R4_3, R16_10, R16_9, R21_9, Count };

extern int width;                                   // current logical canvas width (>= kBaseW)
inline int offsetX() { return (width - kBaseW) / 2; }  // left edge of the design-space area

Mode mode();
void setMode(Mode m);          // saves the choice
void cycleMode(int dir);       // +1 / -1, wraps
std::string modeLabel();       // e.g. "Resolution: 16:9"
void load();                   // read the saved choice (call once at start-up)

// Side areas beside a centred 176-wide screen. Menu wallpaper calls captureEdges() right after
// it is drawn (before any text or sprites); paintSideBars() then continues those edge columns
// outward, or fills black when no wallpaper was drawn this frame.
void beginFrame();
void captureEdges();
void paintSideBars();

// Recomputes the width for the current mode and window; resizes the canvas when it changed.
// Returns true when the width changed (callers then force a full redraw).
bool update();

}  // namespace screen
