// intromanager.h -- port of goingmobile/src_a1/IntroManager.java (class g).
// a1 moved the legacy build's entire in-Game menu system here: splash
// sequence, every menu page, name entry, high scores, the store, and the
// unlock-code registration UI (not exercised by the shipped a1.jar -- no
// Unlock-Code manifest attribute, see ROADMAP.md's "The target").
//
// Scoped to milestone 3.1a's boot-chain/splash slice: the constructor,
// the boot dispatcher (h_(), which drives Game::runBootStep), the splash
// sequence (i_()/j_() ticking, b_(Graphics) rendering three logos with a
// progress bar), the paint/tick entry points CanvasShell calls, and the
// hide/show resource load-on-resume pair (d_()/e_()/f_()/g_(), which null
// out and reload Game's cached images around a background/foreground
// cycle -- a real, confirmed-by-reading memory-conservation quirk of the
// original, not a bug in this port). a_(jbyte) (the ~20-screen state
// setter) is transcribed in full except the unlock-code Form/TextField
// branch (var1==15, unreachable on a fresh/normal playthrough -- see
// intromanager.cpp). The individual per-screen render/input handlers for
// the interactive menu (main menu, language select, sound toggle, save
// slots, credits/help/about, ...) are NOT transcribed yet -- that's the
// next slice of milestone 3.1a.
#pragma once
#include "midp.h"

class ratchetandclank;

class IntroManager {
 public:
  static const jint CHEAT_CODE_KEYS[4];
  static jint b;          // boot/splash progress, 0-100
  bool c = false;
  static jbyte d;
  jbyte e = 0;
  jbyte f = 0;
  jint* g = nullptr;      // [8]
  bool h = false;         // data field -- coexists with method h_() (obfuscated
                          // methods get the trailing '_', data fields never do,
                          // see PORT_ROADMAP.md's naming convention)
  String i;
  bool j = true;
  String m;
  jlong n = 0;
  bool o = false;
  bool p = false;         // "still busy loading" guard (ratchetandclank::playMenuLoopSound)
  bool q = false;         // set true in ratchetandclank::startApp()
  bool r = false;
  jint s = 0;
  jlong t = 0;
  jint u = 0;
  jint v[2] = {652482873, 766492548};
  jint w[2][8] = {};
  static jint x;
  static jint y;

  explicit IntroManager(ratchetandclank* midlet);

  void a_(Graphics* g);                        // paint dispatch
  void b_(int key);                            // keyPressed (stub -- next slice)
  void h_();                                    // tick (CanvasShell's per-frame call)
  void d_();                                    // hideNotify path
  void e_();                                    // showNotify/activate path
  void a_(Command* cmd, Displayable* screen);   // commandAction (paused state, stub)
  void b_(Command* cmd, Displayable* screen);   // commandAction (active state, stub)
  void a_(bool newGame);                        // ratchetandclank startNewGame/continueGame
  void c_();                                    // ratchetandclank returnToIntro
  void a_(jbyte state);                         // screen-state setter
  void d_(int weight);                          // boot-progress tick (weight is unused
                                                 // in the original too -- see .cpp)

 private:
  void f_();  // hides: releases Game's cached images
  void g_();  // shows: reloads Game's cached images
  void i_();  // idle-tick dispatch (splash timer while e==0)
  void j_();  // splash sequencing (3 logos, ~2s each)
  void b_(Graphics* g);  // splash render (screen e==0)

  Image* splashImages_[3] = {};
  static jbyte A;
  static jbyte B;
  jbyte D = 0;
  jlong E = 0;
  jlong F = 0;
  jbyte G = 0;
  jbyte H = 0;
  jbyte I = 0;
  jbyte J = 0;
  jint recentKeyHistory_[4] = {52, 54, 52, 54};
  jint M = 4;

  ratchetandclank* midlet_;
};
