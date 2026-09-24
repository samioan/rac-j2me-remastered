// projectile.h -- port of goingmobile/src_a1/Projectile.java (class j),
// scoped to milestone 3.1a's boot-chain needs: static data tables, the
// constructor, and reset() (real). Physics/rendering (see
// src_a1/Projectile.java) is not transcribed yet; that's milestone 3.2a.
#pragma once
#include "midp.h"

class Game;

class Projectile {
 public:
  static const jbyte DAMAGE_BY_TYPE[33];
  static const jbyte HALF_WIDTHS[33];
  static const jbyte HALF_HEIGHTS[33];
  static const jbyte SPEEDS[33];
  static const jshort RENDER_MODES[33];
  static jbyte tileWidth;
  static jbyte tileHeight;
  static jshort hudHeight;

  jbyte type = -1;
  jint posX = 0;
  jint posY = 0;
  jint prevX = 0;
  jint prevY = 0;
  jint prev2X = 0;
  jint prev2Y = 0;
  jint vx = 0;
  jint vy = 0;
  jbyte age = 0;
  jint halfWidth = 0;
  jint halfHeight = 0;
  jbyte sourceAnim = 0;
  jint playerOffsetX = 0;

  explicit Projectile(Game* game);

  void reset();
  bool isActive() const { return !detonated_; }
  void detonate(bool enemyShot);
  jbyte weaponIndex() const;
  void update(bool enemyShot);
  void homingSeek(int unused);
  void render(Graphics* g);

 private:
  bool detonated_ = false;
  jshort homingTargetEnemy_ = -1;
  Game* game_;
};
