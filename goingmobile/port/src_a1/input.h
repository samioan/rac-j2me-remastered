// input.h -- modern keyboard + gamepad bindings for the a1 port.
//
// Physical inputs (keys, XInput buttons/stick) map to logical Actions; each Action is then
// translated to the original game's phone-keypad code (midp.h's KeyCode) according to the
// current Context. The engine (Game::keyPressed etc.) is untouched -- it still sees the same
// codes the original 176x220 handset build produced.
#pragma once

namespace input {

enum class Context {
  Gameplay,  // live platforming: b == 0 and no dialogue
  Menu,      // menus, dialogue, cutscenes, wheel: move / confirm / back
  Text,      // typing a name: letters and space are text, only Enter/Backspace act
};

// Secondary = right mouse button: weapon wheel in gameplay, Back in menus.
enum class Action { Left, Right, Up, Down, Jump, Fire, Melee, Wheel, Pause, Confirm, Back, Secondary, Count };

// keyDown/keyUp receive game key codes; context() is polled on every press so the mapping
// follows the game state. Any of them may be null before the game exists.
void init(void (*keyDown)(int), void (*keyUp)(int), Context (*context)());

void keyEvent(unsigned virtualKey, bool down, bool isRepeat);  // WM_KEYDOWN / WM_KEYUP; mouse buttons use VK_LBUTTON / VK_RBUTTON
void poll();                                                    // gamepad, once per frame
void releaseAll();                                              // focus lost: no stuck keys

}  // namespace input
