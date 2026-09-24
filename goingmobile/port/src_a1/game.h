// game.h -- port of goingmobile/src_a1/Game.java (class h, the 195KB
// engine). a1 moved the legacy build's in-Game menu system entirely into
// IntroManager (see intromanager.h), so Game itself is gameplay-only:
// tick/render/collision, the player/enemy/projectile pools, and save-data
// serialization -- none of which is transcribed yet.
//
// Stubbed for now to just the call surface CanvasShell.java and
// ratchetandclank.java actually exercise (pause/resume/render/tick/
// keyPressed/keyReleased, writeSaveData/readSaveData, and the tentative
// level-start/return-to-intro entry points ratchetandclank.java calls --
// cu/c_/m_, obfuscated pending Game's own phase-1 pass, see
// src_a1/README.md). Re-transcribing the real tick loop, pools, and save
// layout from Game.java is the bulk of milestone 3.1a/3.2a still to come.
#pragma once
#include "midp.h"

class ratchetandclank;

class Game {
 public:
  explicit Game(ratchetandclank* midlet);

  void pause();
  void resume();
  void render(Graphics* g);
  void tick();
  void keyPressed(int key);
  void keyReleased(int key);

  void writeSaveData(jbyte* buf);
  void readSaveData(const jbyte* buf);

  // Tentative -- see ratchetandclank.java's startNewGame/continueGame/
  // returnToIntro (cu/c(int,int)/m()), obfuscated pending Game's own
  // phase-1 pass.
  jint cu = 0;
  void c_(int a, int b);
  void m_();

 private:
  ratchetandclank* midlet_;
};
