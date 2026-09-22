// player.h -- port of goingmobile/src/Player.java (class a: Ratchet).
//
// Milestone 3.1 ships the complete field/static data surface (the save
// serializer, store screens, weapon tables and M()'s charge logic all touch
// it) plus constructor-faithful initialization; the behavior methods
// (physics j()/k(), firing m(), melee, animation from /r, rendering) are
// stubs to be filled in milestone 3.2 -- see docs/ROADMAP.md.
#pragma once
#include "entity.h"

class Game;

class Player : public Entity {
 public:
  // Per-weapon tuning tables (indices 0..7 = weapon ids, x3 levels where
  // applicable; exactly as in the decompile). Unlike the behavior methods
  // below, these obfuscated statics keep their bare letter with NO trailing
  // underscore -- C++ (unlike Java) can't have a data member and a member
  // function share one name, so the trailing '_' is what keeps e.g. this `g`
  // table distinct from the `g_()` method (matches Game's own a/b/c/... vs
  // a_()/b_()/c_()... convention in game.h).
  static const jbyte  a[15];
  static const jbyte  b[15];
  static const jbyte  c[90];
  static const jshort d[24];   // ammo per weapon x level
  static const jshort e[8];    // full-ammo amounts
  static const jshort f[8];
  static const jbyte  g[3][8]; // charge ticks per level
  static const jshort h[8];   // store prices
  static const jbyte  i[3];
  static const jbyte  l[2];
  static const jbyte  m[2];
  static const jbyte  n[2];
  static const jbyte  o[2];
  static jshort j;
  static jshort k;

  jbyte jumpPhase = 0;
  jbyte ladderExitTimer = 0;
  jbyte actionState = 0;
  jbyte specialTimer = 0;
  jint swingTargetX = 0, swingTargetY = 0;
  jint swingCurX = 0, swingCurY = 0;
  jint swingStepX = 0, swingStepY = 0;
  jbyte attackTimer = 0;
  jbyte weaponPose = 0;
  jbyte invulnTimer = 0;
  jbyte swingTargetType = 0;
  bool onLadder = false;
  jshort platformUnder = 0;
  bool meleeActive = false;

  jbyte currentWeapon = 0;
  jshort ammo[8] = {};
  jshort weaponXp[8] = {};
  jbyte ownedWeapons = 0;
  jbyte weaponLevel[8] = {};

  explicit Player(Game* game);

  // --- behavior (milestone 3.2 stubs) ------------------------------------
  void a_();                       // load /r anim tables
  jshort b_();                     // screen x (game.r-relative)
  jshort c_();                     // screen y
  jbyte d_();
  jbyte e_();
  jshort a_(bool arg);
  bool f_();
  bool g_();
  bool h_();
  void i_();
  void j_();                       // physics step 1
  void k_();                       // physics step 2
  void a_(Graphics* g);            // render
  void l_();
  bool m_();                       // fire weapon
  void n_();
  void o_();
  Game* game = nullptr;
};
