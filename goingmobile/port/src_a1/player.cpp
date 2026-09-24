// player.cpp -- implementation of Player (see player.h), transcribed from
// src_a1/Player.java's constructor and asset loading.
#include "player.h"
#include "game.h"
#include <cstddef>

const jbyte Player::MUZZLE_X_BY_FRAME[28] = {
    -2, -2, -2, 2, 0, -1, 1, -1, -2, 0, 0, -1, 0, 44,
    44, 44, 44, 44, 44, -2, -3, 0, 0, 0, 44, 44, 44, 44};
const jbyte Player::MUZZLE_Y_BY_FRAME[28] = {
    15, 13, 13, 12, 13, 14, 15, 7, 7, 12, 17, 20, 12, 44,
    44, 44, 44, 44, 44, 14, 15, 12, 13, 13, 44, 44, 44, 44};
const jbyte Player::FRAME_GEOMETRY[29][6] = {
    {0, 0, 20, 35, 11, 9},      {0, 119, 20, 36, 11, 8},
    {20, 119, 20, 36, 11, 8},   {20, 0, 25, 35, 8, 9},
    {0, -101, 30, 31, 5, 8},    {108, 0, 27, 37, 7, 7},
    {57, 120, 23, 34, 9, 10},   {0, 79, 20, 40, 10, 1},
    {30, -101, 25, 31, 9, 4},   {-121, 0, 23, 39, 7, 5},
    {68, -102, 23, 29, 10, 15}, {40, 119, 17, 36, 13, 8},
    {91, 35, 20, 43, 10, 1},    {0, 35, 30, 44, 0, 0},
    {111, 39, 44, 42, 0, 2},    {108, 121, 43, 28, 1, 16},
    {125, 81, 24, 36, 8, 2},    {20, 79, 44, 40, 0, 0},
    {64, 79, 36, 41, 7, 3},     {45, 0, 26, 35, 9, 9},
    {80, 120, 28, 34, 7, 10},   {100, 81, 25, 40, 6, 3},
    {30, 35, 31, 44, 1, 0},     {61, 35, 31, 44, 1, 0},
    {91, -102, 24, 33, 11, 9},  {71, 0, 37, 35, 7, 9},
    {115, -107, 41, 27, 2, 16}, {115, -80, 40, 11, 1, 33},
};
const jshort Player::AMMO_CAPACITY[24] = {
    0, 0, 0, 100, 150, 200, 35, 45, 60, 25, 30, 40,
    40, 55, 70, 200, 250, 300, 1, 1, 1, 70, 90, 110};
const jshort Player::INITIAL_AMMO[8] = {0, 50, 20, 15, 20, 100, 1, 30};
const jshort Player::AMMO_PER_PICKUP[8] = {0, 15, 7, 5, 8, 30, 1, 10};
const jbyte Player::CHARGE_TICKS[3][8] = {
    {0, 4, 16, 20, 16, 5, 4, 5},
    {0, 4, 16, 20, 16, 5, 4, 5},
    {0, 4, 16, 20, 16, 5, 4, 5},
};
const jint Player::h[5] = {50, 800, 2500, 18000, 0};
const jbyte Player::MELEE_DAMAGE_BY_LEVEL[3] = {3, 8, 8};
const jbyte Player::SWING_X_OFFSET[2] = {14, -11};
const jbyte Player::SWING_Y_OFFSET[2] = {4, 17};
const jbyte Player::SWING_WIDTH[2] = {10, 20};
const jbyte Player::SWING_HEIGHT[2] = {10, 50};

jbyte Player::tileWidth = 0;
jbyte Player::tileHeight = 0;
jshort Player::hudHeight = 0;
jshort Player::terminalVelocityY = 0;
jshort Player::currentWalkSpeed = 0;
jbyte Player::ANIM_FRAMES[1][15][4] = {};
jbyte Player::ANIM_FRAME_COUNTS[1][15] = {};
jbyte Player::ANIM_FRAME_EXTRA[1][15] = {};

Player::Player(Game* game) : game_(game) {
  tileWidth = Game::tileWidth;
  tileHeight = Game::tileHeight;
  hudHeight = Game::hudHeight;
  currentWalkSpeed = 1536;
  jumpPhase = -1;
  swingPhase = -2;
  hyperCastTimer = -1;
  swingTargetType = 0;
  ledgeAhead = false;
  zipGrabRetryDelay = 0;
  invulnTimer = 0;
  roomEdgeReached = false;
  ammo = new jshort[8]();
  N = new jshort[8]();
  weaponLevel = new jbyte[8]();
  T = new jshort[8]();
  S = new jshort[8]();
  U = new jbyte[8]();
  velY = 0;
  velX = 0;
  ap = 0;
  facingRight = true;
  health = 20;
  terminalVelocityY = (jshort)((tileHeight >> 1) << 8);
}

void Player::loadAssets() { loadAnimFile("/player.bin"); }

void Player::loadAnimFile(const String& path) {
  ByteArray data = loadResource(path);
  size_t pos = 0;
  auto readByte = [&]() -> jbyte {
    return pos < data.size() ? (jbyte)std::to_integer<uint8_t>(data[pos++]) : 0;
  };
  for (int i = 0; i < 15; i++) {
    jbyte count = readByte();
    jbyte extra = readByte();
    ANIM_FRAME_COUNTS[0][i] = count;
    ANIM_FRAME_EXTRA[0][i] = extra;
    for (int j = 0; j < count && j < 4; j++) ANIM_FRAMES[0][i][j] = readByte();
  }
}
