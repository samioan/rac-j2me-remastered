// player.cpp -- Player static tables + constructor (behavior -> 3.2).
#include "player.h"
#include "game.h"

const jbyte Player::a[15] = {-1, 0, -1, 0, -1, 44, 44, 44, 44, -2, 0, 0, 44, 44, 44};
const jbyte Player::b[15] = {9, 8, 8, 6, 7, 44, 44, 44, 44, 9, 8, 8, 44, 44, 44};
const jbyte Player::c[90] = {
    42, 51, 18, 32, 51, 0, 45, 0, 0,
    12, 15, 30, 24, 36, 0, 22, 22, 22, 22, 42, 0, 0, 42, 22, 42, 0, 0, 42, 42, 60,
    9, 13, 14, 10, 11, 15, 22, 12, 18, 12, 15, 15, 12, 15, 20, 18, 18, 19, 19, 15,
    22, 21, 18, 20, 17, 22, 22, 17, 17,
    5, 7, 4, 3, 6, 7, 0, 0, 3, 3, 5, 1, 1, 5, 4, 1, 4, 4, 3, 2, 3, 0, 1, 2, 0, 5,
    0, 0, 5, 5, 17};
const jshort Player::d[24] = {0, 0, 0, 100, 150, 200, 35, 45, 60,
                              25, 30, 40, 40, 55, 70, 200, 250, 300,
                              1, 1, 1, 70, 90, 110};
const jshort Player::e[8] = {0, 50, 20, 15, 20, 100, 1, 30};
const jshort Player::f[8] = {0, 15, 7, 5, 8, 30, 1, 10};
const jbyte Player::g[3][8] = {{0, 4, 16, 20, 16, 5, 4, 5},
                               {0, 4, 16, 20, 16, 5, 4, 5},
                               {0, 4, 16, 20, 16, 5, 4, 5}};
const jshort Player::h[8] = {0, 0, 250, 400, 700, 950, 1650, 0};
const jbyte Player::i[3] = {3, 6, 6};
const jbyte Player::l[2] = {4, -7};
const jbyte Player::m[2] = {3, 10};
const jbyte Player::n[2] = {5, 8};
const jbyte Player::o[2] = {16, 13};
jshort Player::j = 0;
jshort Player::k = 0;

Player::Player(Game* g) : game(g) {
  k = 1280;
  jumpPhase = -1;
  actionState = -2;
  attackTimer = -1;
  weaponPose = 0;
  onLadder = false;
  ladderExitTimer = 0;
  platformUnder = 0;
  for (int idx = 0; idx < 8; idx++) {
    ammo[idx] = 0;
    weaponXp[idx] = 0;
    weaponLevel[idx] = 0;
  }
  velY = 0;
  velX = 0;
  subState = 0;
  facingRight = true;
  health = 20;
  j = 1792;
}

// ---- behavior stubs (milestone 3.2) --------------------------------------

void Player::a_() {}
jshort Player::b_() { return 0; }
jshort Player::c_() { return 0; }
jbyte Player::d_() { return 0; }
jbyte Player::e_() { return 0; }
jshort Player::a_(bool) { return 0; }
bool Player::f_() { return false; }
bool Player::g_() { return false; }
bool Player::h_() { return false; }
void Player::i_() {}
void Player::j_() {}
void Player::k_() {}
void Player::a_(Graphics*) {}
void Player::l_() {}
bool Player::m_() { return false; }
void Player::n_() {}
void Player::o_() {}
