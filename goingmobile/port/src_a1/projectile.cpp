// projectile.cpp -- implementation of Projectile (see projectile.h),
// transcribed from src_a1/Projectile.java's static tables, constructor,
// and reset().
#include "projectile.h"
#include "game.h"

const jbyte Projectile::DAMAGE_BY_TYPE[33] = {1, 2, 4,  0,  0,  0,  0, 0, 0, 12, 12, 12, 1,  2,  3, 1, 2,
                                               2, 0, 0,  0,  3,  5,  6, 6, 11, 17, 4,  7,  10, 0,  0, 0};
const jbyte Projectile::HALF_WIDTHS[33] = {3,  3,  3, 3, 3, 3, 3, 3, 3, 0,  0,  0,  3, 3, 3, 0, 0,
                                            0,  3,  3, 3, 45, 65, 65, 15, 15, 30, 15, 15, 15, 0, 12, 10};
const jbyte Projectile::HALF_HEIGHTS[33] = {3,  3,  3, 3, 3, 3, 3, 3, 3, 0,  0,  0,  3, 3, 3, 0, 0,
                                             0,  3,  3, 3, 45, 65, 65, 15, 15, 30, 15, 15, 15, 0, 12, 10};
const jbyte Projectile::SPEEDS[33] = {10, 16, 16, 5, 5, 5, 8, 8, 8, 1, 1, 1,  4,  4,  4, 1, 1,
                                       1,  8,  8,  8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 10};
const jshort Projectile::RENDER_MODES[33] = {13, 14, 15, 22, 23, 24, 19, 20, 21, -1, -1, -1, -1, -1, -1, -1, -1,
                                              -1, 16, 17, 18, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -1, -1};

jbyte Projectile::tileWidth = 0;
jbyte Projectile::tileHeight = 0;
jshort Projectile::hudHeight = 0;

Projectile::Projectile(Game* game) : game_(game) {
  tileWidth = Game::tileWidth;
  tileHeight = Game::tileHeight;
  hudHeight = Game::hudHeight;
  reset();
}

void Projectile::reset() {
  type = -1;
  posX = posY = 0;
  vx = vy = 0;
  age = 0;
  halfWidth = 0;
  halfHeight = 0;
  detonated_ = false;
  homingTargetEnemy_ = -1;
  prevX = prev2X = 0;
  prevY = prev2Y = 0;
  sourceAnim = 0;
}
