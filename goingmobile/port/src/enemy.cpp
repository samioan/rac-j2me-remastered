// enemy.cpp -- Enemy constructor + stubs (milestone 3.2 fills AI/animation).
#include "enemy.h"
#include "game.h"

Enemy::Enemy(Game* g) : game(g) {
  kind = -1;    // pool slot starts inactive (Entity default is 0 == alive)
  subState = 0;
  B = 0;
  facingRight = true;
  s = false;
  v = false;
  w = false;
  x = false;
  z = 0;
}

void Enemy::a_() {}
jshort Enemy::b_() { return 0; }
jshort Enemy::c_() { return 0; }
void Enemy::a_(Graphics*, int) {}
void Enemy::d_() {}
void Enemy::e_() {}
