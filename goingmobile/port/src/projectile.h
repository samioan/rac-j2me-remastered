// projectile.h -- port of goingmobile/src/Projectile.java (class g).
// Milestone 3.1: field surface + constructor; movement/collision -> 3.2.
#pragma once
#include "midp.h"

class Game;

class Projectile {
 public:
  jbyte type = -1;
  jint posX = 0, posY = 0;
  jint prevX = 0, prevY = 0;
  jint prev2X = 0, prev2Y = 0;
  jint vx = 0, vy = 0;
  jbyte age = 0;
  jint halfWidth = 0, halfHeight = 0;
  jbyte sourceAnim = 0;
  bool facingRight = false;
  jint spawnX = 0;

  explicit Projectile(Game* game);

  bool a_();                       // still alive? (3.2 stub)
  void a_(bool playerSide);        // tick (3.2 stub)
  jbyte b_();
  void b_(bool playerSide);        // move step (3.2 stub)
  void a_(Graphics* g, bool playerSide);  // render (3.2 stub)

  Game* game = nullptr;
};
