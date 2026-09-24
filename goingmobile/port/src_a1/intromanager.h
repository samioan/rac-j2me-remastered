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
// intromanager.cpp).
//
// This slice adds the interactive menu itself: main menu, options
// (sound/delete-save/language), save-write picker, weapon store, delete-
// save picker, a shared Yes/No confirm screen, the credits/help/about
// pager, exit-game confirm, new-game slot picker, the "get Ratchet skin"
// message screen, and language select -- states 1/2/4/5/6/7/8/9/11/16/20
// of the ~20-screen state machine (see IntroManager.java's own e field).
// States 15/17 (the unlock-code Form/TextField entry and its post-submit
// hash screen) stay unreachable/unimplemented, same reasoning as before --
// the shipped a1.jar carries no Unlock-Code manifest attribute, so `j`
// stays true and isGameWon() gates the only path to state 15. States 3/
// 10/12/13/14/18/19 are unused by IntroManager itself (case 10 just
// blanks the screen; the rest have no case at all in the original) and
// stay as the existing default no-op.
//
// Two things are deliberately NOT here, both because they need
// Player::render()/Enemy::render()/updateAnimation() -- which don't
// exist yet, see player.h/enemy.h's own "behavior stubbed" boundary,
// same milestone-3.2a deferral as Game's tick/render:
//   - the decorative main-menu background animation (Player + 4 Enemy
//     instances walking back and forth, state 1's i()-tick case and the
//     matching render calls in d(Graphics)) and the weapon store's
//     decorative player pose (a(Graphics,int)'s var2>2&&var2<11 branch);
//   - Game's own menu background/text-layout helpers (Game.a(Graphics,
//     int,int,int,int)/e(Graphics)/a(Graphics,String,int,int,int,int)/
//     k()), since Game's own phase-1 read-through is still deferred to
//     3.2a (see game.h). Given real bodies here instead: an exact
//     word-wrap paragraph draw (generic MIDP text layout, no Game
//     internals needed) and an exact %N string formatter, but a
//     SIMPLIFIED backdrop/content-metrics approximation for the rest --
//     see game.h/game.cpp's own note on exactly which of these is which.
#pragma once
#include "midp.h"

class ratchetandclank;
class Game;

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
  void a_(Graphics* g, int weaponIndex);        // state 5 (weapon store) render
  void b_(int key);                            // keyPressed
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
  void i_();  // idle-tick dispatch (splash timer while e==0; states 1/5's
              // decorative-animation cases stay no-ops, see header note)
  void j_();  // splash sequencing (3 logos, ~2s each)
  void b_(Graphics* g);  // splash render (screen e==0)

  // --- per-screen render (screen e selects one; see a_(Graphics*)) -------
  void d_(Graphics* g);           // state 1: main menu
  void h_(Graphics* g);           // state 2: options (sound/delete/language)
  void e_(Graphics* g);           // state 4: save-write picker
  void k_(Graphics* g);           // state 6: delete-save picker
  void l_(Graphics* g);           // state 7: shared Yes/No confirm
  void b_(Graphics* g, int page); // state 8: credits/help/about pager
  void j_(Graphics* g);           // state 9: exit-game confirm
  void c_(Graphics* g);           // state 11: new-game slot picker
  void g_(Graphics* g);           // state 16: "get Ratchet skin" message
  void i_(Graphics* g);           // state 20: language select

  // --- per-screen input (screen e selects one; see b_(int)) ---------------
  void c_(int key, int action);   // state 1
  void e_(int key, int action);   // state 2
  void d_(int key, int action);   // state 4
  void h_(int key, int action);   // state 5
  void k_(int key, int action);   // state 6
  void j_(int key, int action);   // state 7
  void g_(int key, int action);   // state 8 (also records the cheat sequence)
  void i_(int key, int action);   // state 9
  void l_(int key, int action);   // state 11
  void b_(int key, int action);   // state 16
  void f_(int key, int action);   // state 20

  // --- shared widgets/helpers ---------------------------------------------
  jbyte a_(jbyte cur, jbyte lowerBound, jbyte resetTo);  // decrement-clamp
  jbyte b_(jbyte cur, jbyte upperBound, jbyte resetTo);  // increment-clamp
  int b_();                                              // side margin
  String c_(int ms);                                     // ms -> "M:SS:SS"
  int a_(Graphics* g, const String& text, int x, int y, int anchor, bool selected);
  int b_(Graphics* g, const String& text, int x, int y, int anchor, bool selected);
  int a_(Graphics* g, const String& text, int x, int y, int anchor, bool selected, int color);
  int a_(Graphics* g, jbyte slotIndex, int x, int y, int anchor, bool selected);
  int a_(Graphics* g, const String& title);              // large-font title
  void a_(Graphics* g, int leftStringIdx, int rightStringIdx, void* caller);  // softkey hint row (caller unused, matches the original's dead Object param)
  void a_(Graphics* g, int x, int y);                    // selection bracket around g[G]
  void a_(Graphics* g, jbyte unused);                    // bordered wallpaper backdrop
  void b_(Graphics* g, int page, int total);              // "Page X/Y" footer
  void recordKeyForCheat(int key);
  bool checkCheatCode();

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

  friend class Game;  // Game::render calls the softkey-hint row directly
};
