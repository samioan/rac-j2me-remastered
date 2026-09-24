// entity.h -- port of goingmobile/src_a1/Entity.java (class i). a1's base
// class for Player and Enemy: fixed-point position (px << 8), grid
// row/column bookkeeping, health and animation state. Same convention as
// the legacy build's Entity (../src/entity.h): posX is 1/256-pixel fixed
// point, row/posInRow the tile-row + in-row offset (wrapping against
// Game::tileHeight).
#pragma once
#include "midp.h"

class Game;  // column()/wrapRow() read Game::tileWidth/tileHeight

class Entity {
 public:
  jbyte kind = 0;
  jbyte row = 0;
  jint posX = 0;
  jshort velY = 0;
  jshort velX = 0;
  jbyte ae = 0;             // unconfirmed -- see src_a1/Entity.java
  jbyte af = 0;             // unconfirmed -- see src_a1/Entity.java
  jint posInRow = 0;
  jint activeFlag = 0;      // int here, byte in the legacy build
  jbyte health = 0;
  jshort animFrame = 0;     // short here, byte in the legacy build
  jshort animCounter = 0;   // short here, byte in the legacy build
  jbyte animState = 0;
  jbyte animRestart = 0;    // tentative
  jbyte animHold = 0;       // tentative
  bool facingRight = false;
  jbyte ap = 0;              // unconfirmed

  jbyte column() const;
  void setAnimState(jbyte state) {
    animState = state;
    animFrame = animCounter = 0;
  }
  void wrapRow();
};
