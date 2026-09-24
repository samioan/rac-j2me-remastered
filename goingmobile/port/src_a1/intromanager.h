// intromanager.h -- port of goingmobile/src_a1/IntroManager.java (class g).
// a1 moved the legacy build's entire in-Game menu system here: splash
// sequence, every menu page, name entry, high scores, the store, and the
// unlock-code registration UI (not exercised by the shipped a1.jar -- no
// Unlock-Code manifest attribute, see ROADMAP.md's "The target").
//
// Stubbed for now to just the call surface CanvasShell.java and
// ratchetandclank.java actually exercise. Re-transcribing the real splash/
// menu logic from IntroManager.java is the bulk of milestone 3.1a still
// remaining (comparable in scope to the legacy build's Game.cpp menu code,
// milestone 3.1) -- see PORT_ROADMAP.md.
#pragma once
#include "midp.h"

class ratchetandclank;

class IntroManager {
 public:
  explicit IntroManager(ratchetandclank* midlet);

  void a_(Graphics* g);                        // paint
  void b_(int key);                            // keyPressed
  void h_();                                    // tick (CanvasShell's per-frame call)
  void d_();                                    // hideNotify path
  void e_();                                    // showNotify/activate path
  void a_(Command* cmd, Displayable* screen);   // commandAction (paused state)
  void b_(Command* cmd, Displayable* screen);   // commandAction (active state)
  void a_(bool newGame);                        // ratchetandclank startNewGame/continueGame
  void c_();                                    // ratchetandclank returnToIntro

  // Tentative fields ratchetandclank.java reads/writes directly (package-
  // private in the Java).
  bool p = false;  // "still busy loading" guard (playMenuLoopSound)
  bool q = false;  // set true in startApp(); role unconfirmed

 private:
  ratchetandclank* midlet_;
};
