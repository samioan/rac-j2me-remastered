// enemy.h -- port of goingmobile/src/Enemy.java (class d).
// Milestone 3.1: field surface + constructor; AI/animation stubs -> 3.2.
#pragma once
#include "entity.h"

class Game;

class Enemy : public Entity {
 public:
  bool s = false;
  jbyte animKind = 0;
  jbyte bounceTimer = 0;
  bool v = false;
  bool w = false;
  bool x = false;
  jshort y = 0;
  jshort z = 0;
  jbyte A = 0;
  jshort B = 0;
  jint C = 0;

  explicit Enemy(Game* game);

  void a_();                       // load /p + /q tables (3.2 stub)
  jshort b_();
  jshort c_();
  void a_(Graphics* g, int arg);
  void d_();                       // AI step (3.2 stub)
  void e_();                       // physics step (3.2 stub)

  Game* game = nullptr;
};
