// entity.h -- port of goingmobile/src/Entity.java (hand-written in phase 1,
// fully renamed). Base of Player and Enemy: fixed-point position (px << 8),
// grid row/column bookkeeping, health and animation state.
//
// Position convention: posX is the horizontal position in 1/256-pixel units
// (the game's fixed point), row is the 14-pixel-tall tile row and posInRow
// the 1/256-px offset within it (0..3583, wrapping via wrapRow()).
// column() derives the 22-pixel-wide tile column from posX.
#pragma once
#include "midp.h"

class Entity {
 public:
  jbyte kind = 0;       // enemy type 0..4 (Player: always 0)
  jbyte row = 0;
  jint posX = 0;
  jshort velY = 0;      // 1/256 px per tick (up positive; jump = 2560)
  jshort velX = 0;
  jbyte fieldW = 0;     // never read anywhere -- role unconfirmed
  jbyte spawnRow = 0;   // copy of row kept at spawn
  jint posInRow = 0;    // 1/256 px within the row (wraps at 14*256)
  jbyte activeFlag = 0; // set to 1 at spawn; role otherwise unconfirmed
  jbyte health = 0;
  jbyte animFrame = 0;
  jbyte animCounter = 0;
  jbyte animState = 0;
  jbyte animRestart = 0;
  jbyte animHold = 0;
  bool facingRight = false;
  jbyte subState = 0;

  jbyte column() const { return (jbyte)((posX >> 8) / 22); }
  void setAnimState(jbyte state) {
    animState = state;
    animFrame = 0;
    animCounter = 0;
  }
  void wrapRow() {
    if (posInRow < 0) {
      posInRow += 3584;
      row--;
      if (row < 0) {
        row = 0;
        posInRow = 0;
        return;
      }
    } else if (posInRow > 3584) {
      posInRow -= 3584;
      row++;
    }
  }
};
