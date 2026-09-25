// input.cpp -- see input.h. Bindings live in the two tables below (keyboard, gamepad) so a
// future remapping screen only has to edit them.
#include "input.h"
#include "midp.h"

#define WIN32_LEAN_AND_MEAN
#define NOMINMAX
#include <windows.h>
#include <xinput.h>
#include <map>

#pragma comment(lib, "xinput.lib")

namespace input {
namespace {

void (*g_down)(int) = nullptr;
void (*g_up)(int) = nullptr;
Context (*g_context)() = nullptr;

// Source ids: virtual-key codes as-is; gamepad inputs offset so they never collide.
constexpr unsigned kPad = 0x10000;
constexpr unsigned kStickLeft = kPad + 0x100, kStickRight = kPad + 0x101, kStickUp = kPad + 0x102,
                   kStickDown = kPad + 0x103, kTriggerR = kPad + 0x110, kTriggerL = kPad + 0x111;

struct Binding { unsigned source; Action action; };

const Binding kKeyboard[] = {
    {VK_LEFT, Action::Left},   {'A', Action::Left},
    {VK_RIGHT, Action::Right}, {'D', Action::Right},
    {VK_UP, Action::Up},       {'W', Action::Up},
    {VK_DOWN, Action::Down},   {'S', Action::Down},
    {VK_SPACE, Action::Jump},  {VK_LSHIFT, Action::Jump},
    {'J', Action::Fire},       {'X', Action::Fire},       {VK_LCONTROL, Action::Fire},
    {'K', Action::Melee},      {'C', Action::Melee},      {VK_OEM_2, Action::Melee},  // '/'
    {'Q', Action::Wheel},      {VK_TAB, Action::Wheel},   {'E', Action::Wheel},
    {VK_ESCAPE, Action::Pause}, {'P', Action::Pause},
    {VK_RETURN, Action::Confirm},
    {VK_BACK, Action::Back},
    {VK_LBUTTON, Action::Fire},       // fire also melees when the game decides to (adjacent enemy, no ammo)
    {VK_RBUTTON, Action::Secondary},
};

const Binding kGamepad[] = {
    {kPad + XINPUT_GAMEPAD_DPAD_LEFT, Action::Left},   {kStickLeft, Action::Left},
    {kPad + XINPUT_GAMEPAD_DPAD_RIGHT, Action::Right}, {kStickRight, Action::Right},
    {kPad + XINPUT_GAMEPAD_DPAD_UP, Action::Up},       {kStickUp, Action::Up},
    {kPad + XINPUT_GAMEPAD_DPAD_DOWN, Action::Down},   {kStickDown, Action::Down},
    {kPad + XINPUT_GAMEPAD_A, Action::Jump},
    {kPad + XINPUT_GAMEPAD_X, Action::Fire},           {kTriggerR, Action::Fire},
    {kPad + XINPUT_GAMEPAD_B, Action::Melee},          {kTriggerL, Action::Melee},
    {kPad + XINPUT_GAMEPAD_Y, Action::Wheel},          {kPad + XINPUT_GAMEPAD_RIGHT_SHOULDER, Action::Wheel},
    {kPad + XINPUT_GAMEPAD_LEFT_SHOULDER, Action::Wheel},
    {kPad + XINPUT_GAMEPAD_START, Action::Pause},
    {kPad + XINPUT_GAMEPAD_BACK, Action::Back},
};

bool lookup(const Binding* table, size_t n, unsigned source, Action& out) {
  for (size_t i = 0; i < n; i++)
    if (table[i].source == source) { out = table[i].action; return true; }
  return false;
}

// Game key code for an action in a context; 0 = the action does nothing here.
int translate(Action a, Context c) {
  switch (c) {
    case Context::Gameplay:
      switch (a) {
        case Action::Left: return KEY_LEFT;
        case Action::Right: return KEY_RIGHT;
        case Action::Up: case Action::Jump: return KEY_UP;
        case Action::Down: return KEY_DOWN;
        case Action::Fire: case Action::Confirm: return KEY_FIRE;
        case Action::Melee: return KEY_STAR;
        case Action::Wheel: return 35;  // '#'
        case Action::Pause: return KEY_SOFT_LEFT;
        case Action::Back: return KEY_SOFT_RIGHT;
        case Action::Secondary: return 35;  // weapon wheel, same as Wheel
        default: return 0;
      }
    case Context::Menu:
      switch (a) {
        case Action::Left: return KEY_LEFT;
        case Action::Right: return KEY_RIGHT;
        case Action::Up: return KEY_UP;
        case Action::Down: return KEY_DOWN;
        case Action::Jump: case Action::Fire: case Action::Confirm: return KEY_FIRE;
        case Action::Melee: case Action::Pause: case Action::Back: case Action::Secondary: return KEY_SOFT_RIGHT;
        default: return 0;
      }
    case Context::Text:
      switch (a) {
        case Action::Confirm: return KEY_FIRE;
        case Action::Back: return KEY_SOFT_RIGHT;
        default: return 0;  // typed characters arrive separately through WM_CHAR
      }
  }
  return 0;
}

bool isDirection(Action a) {
  return a == Action::Left || a == Action::Right || a == Action::Up || a == Action::Down;
}

std::map<unsigned, int> g_emitted;  // held source -> game code it pressed
int g_refs[512] = {};                // presses per game code (several sources can share one)

int refIndex(int code) {  // codes span -10..57; shift into the array
  int i = code + 16;
  return (i >= 0 && i < 512) ? i : 0;
}

void press(unsigned source, Action action, bool isRepeat) {
  Context ctx = g_context ? g_context() : Context::Menu;
  int code = translate(action, ctx);
  if (isRepeat) {
    // Held key: only menu navigation repeats (so scrolling a list works); gameplay must not.
    auto it = g_emitted.find(source);
    if (it != g_emitted.end() && ctx == Context::Menu && isDirection(action) && g_down) g_down(it->second);
    return;
  }
  if (g_emitted.count(source)) return;
  if (code == 0) return;
  g_emitted[source] = code;
  if (++g_refs[refIndex(code)] == 1 && g_down) g_down(code);
}

void release(unsigned source) {
  auto it = g_emitted.find(source);
  if (it == g_emitted.end()) return;
  int code = it->second;
  g_emitted.erase(it);
  if (--g_refs[refIndex(code)] == 0 && g_up) g_up(code);
}

}  // namespace

void init(void (*keyDown)(int), void (*keyUp)(int), Context (*context)()) {
  g_down = keyDown;
  g_up = keyUp;
  g_context = context;
}

void keyEvent(unsigned vk, bool down, bool isRepeat) {
  Action action;
  if (!lookup(kKeyboard, sizeof(kKeyboard) / sizeof(kKeyboard[0]), vk, action)) {
    // Legacy handset keys stay live: digits (the game reads 2/4/6/8/5/1/3 directly).
    if (vk >= '0' && vk <= '9') {
      if (down && !isRepeat && g_down) g_down((int)vk);
      else if (!down && g_up) g_up((int)vk);
    }
    return;
  }
  Context ctx = g_context ? g_context() : Context::Menu;
  if (down && ctx == Context::Text && action != Action::Confirm && action != Action::Back &&
      !isDirection(action))
    return;  // a typed letter/space, not a command
  if (down) press(vk, action, isRepeat);
  else release(vk);
}

void releaseAll() {
  std::map<unsigned, int> held;
  held.swap(g_emitted);
  for (auto& kv : held)
    if (--g_refs[refIndex(kv.second)] == 0 && g_up) g_up(kv.second);
}

void poll() {
  static bool wasConnected = false;
  static bool padHeld[64] = {};  // by table index: was this pad source down last frame
  XINPUT_STATE st = {};
  bool connected = XInputGetState(0, &st) == ERROR_SUCCESS;
  if (!connected) {
    if (wasConnected) {
      for (size_t i = 0; i < sizeof(kGamepad) / sizeof(kGamepad[0]); i++) { release(kGamepad[i].source); padHeld[i] = false; }
    }
    wasConnected = false;
    return;
  }
  wasConnected = true;
  const XINPUT_GAMEPAD& p = st.Gamepad;

  // Sticks are digital with hysteresis: engage past ~50%, release below ~35%.
  auto stick = [&](unsigned id, int value, bool positive) {
    int threshold = 16384, releaseAt = 11500;
    bool currently = g_emitted.count(id) != 0;
    int v = positive ? value : -value;
    return currently ? v > releaseAt : v > threshold;
  };

  for (size_t i = 0; i < sizeof(kGamepad) / sizeof(kGamepad[0]); i++) {
    unsigned src = kGamepad[i].source;
    bool down;
    if (src == kStickLeft) down = stick(src, p.sThumbLX, false);
    else if (src == kStickRight) down = stick(src, p.sThumbLX, true);
    else if (src == kStickUp) down = stick(src, p.sThumbLY, true);
    else if (src == kStickDown) down = stick(src, p.sThumbLY, false);
    else if (src == kTriggerR) down = p.bRightTrigger > 100;
    else if (src == kTriggerL) down = p.bLeftTrigger > 100;
    else down = (p.wButtons & (src - kPad)) != 0;

    if (down && !padHeld[i]) press(src, kGamepad[i].action, false);
    else if (!down && padHeld[i]) release(src);
    padHeld[i] = down;
  }
}

}  // namespace input
