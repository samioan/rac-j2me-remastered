// canvasshell.h -- port of goingmobile/src_a1/CanvasShell.java (class e).
// a1's canvas shell: a plain MIDP Canvas + CommandListener that delegates
// every real callback to whichever of the MIDlet's controller objects is
// active (IntroManager before gameStarted, Game after). The port folds the
// original's separate paint-thread/tick-thread Runnable into a single
// tick() the platform loop drives once per frame (same simplification the
// legacy build's main.cpp already makes for Game::run_(), see
// ../src/main.cpp).
//
// canvasState: -1 = never shown; 0 = active; 1 = paused ("Press * Key...");
// 2 = transitional (set by '*' while paused, tick() sees it and reactivates
// via activate()).
#pragma once
#include "midp.h"

class ratchetandclank;

class CanvasShell {
 public:
  jbyte canvasState = -1;
  static Graphics* directGraphics;  // stands in for Java's DirectGraphics --
                                     // the port's Graphics already has the
                                     // Nokia DirectGraphics methods folded in

  explicit CanvasShell(ratchetandclank* midlet);

  void hideNotify();
  void showNotify();
  void activate();
  void paint(Graphics* g);
  void tick();  // one frame of CanvasShell.run()'s loop body
  void keyPressed(int key);
  void keyReleased(int key);
  void commandAction(Command* cmd, Displayable* screen);

 private:
  ratchetandclank* midlet_;
};
