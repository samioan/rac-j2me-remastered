// Keyboard, mouse and gamepad bindings (modelled on the Going Mobile port).
//
// Physical inputs map to logical Actions; each Action is then translated to the phone-keypad
// code the game expects (MIDP key codes: -1 up, -2 down, -3 left, -4 right, -5 fire, -6/-7 soft
// keys, 42 '*', 35 '#', digits) according to the current Context. The game code is untouched.
#pragma once

namespace input {

enum class Context {
  Gameplay,  // state 0 (and the transient level-start states): the player is controllable
  Menu,      // everything else: menus, shops, map, weapon wheel, dialogue screens
};

// Fire is the circle button (fire, also confirms in menus); Melee is the wrench (square) and
// does nothing else in gameplay.
enum class Action { Left, Right, Up, Down, Jump, Fire, Melee, Wheel, Pause, Confirm, Back, Secondary, Count };

// keyDown/keyUp receive MIDP key codes; context() is polled on every press so the mapping
// follows the game state.
void init(void (*keyDown)(int), void (*keyUp)(int), Context (*context)());

void keyEvent(unsigned virtualKey, bool down, bool isRepeat);  // mouse buttons use VK_LBUTTON / VK_RBUTTON
void poll();                                                    // XInput gamepad, once per frame
void releaseAll();                                              // focus lost: no stuck keys

}  // namespace input
