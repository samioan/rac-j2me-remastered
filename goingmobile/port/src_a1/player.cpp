// player.cpp -- implementation of Player (see player.h), transcribed from
// src_a1/Player.java's constructor and asset loading.
#include "player.h"
#include "game.h"
#include "canvasshell.h"
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

void Player::resetAttackAnim() {
  hyperCastTimer = 0;
  if (animState == 7 || animState == 8 || animState == 9 || animState == 0 || animState == 4) {
    animRestart = 0;
    setAnimState(0);
  } else if (animState == 13) {
    animRestart = 0;
    setAnimState(12);
  } else if (animState == 14) {
    animRestart = 0;
    setAnimState(11);
  }
}

void Player::updateAnimation() {
  if (animRestart == 2) return;
  if (animState == 14) velY = 0;
  animCounter++;
  if (animCounter > ANIM_FRAME_EXTRA[kind][animState]) {
    animCounter = 0;
    animFrame++;
    if (animFrame >= ANIM_FRAME_COUNTS[kind][animState]) {
      if (animRestart == 0) {
        animFrame = 0;
      } else {
        animFrame--;
        animRestart = 2;
        resetAttackAnim();
      }
    }
    game_->d = true;
  }
}

void Player::render(Graphics* g, int, int cameraX, int cameraY) {
  Game::x = (jshort)cameraX;
  Game::y = (jshort)cameraY;
  int var5 = (posX >> 8) - (Game::J >> 1) + Game::x;
  int var6 = row * tileHeight + (posInRow >> 8) + Game::y + Game::L;
  if (animState == 6) var6 += tileHeight >> 1;

  if (swingPhase >= 0) {
    int var11 = 26 * tileWidth / 44;
    if (!facingRight) var11 = tileWidth - var11;
    int var12 = 10 * tileHeight / 44;
    int var13 = (var5 + var11 - 9) << 8;
    int var14 = (var6 + var12 - 9) << 8;
    int var15 = swingCurX + (Game::x << 8);
    int var16 = swingCurY + (Game::y << 8);
    if (swingPhase == 0) var14 += 2304;
    if (facingRight) var15 -= 1216; else var15 -= 2432;
    int var17 = (var15 - var13) / 19;
    int var18 = (var16 - var14) / 19;
    for (int i = 0; i < 20; i++) {
      int var20 = var13 >> 8;
      int var21 = var14 >> 8;
      if (var20 < 176 && var21 < 220 && var20 + 19 >= 0 && var21 + 19 >= 0) {
        game_->b_(g, var20, var21, 19, 19);
        g->drawImage(Game::aL, var20, var21 - 114, 0);
      }
      var13 += var17;
      var14 += var18;
    }
    int var33 = (var13 - var17) >> 8;
    int var25 = ((var14 - var18) >> 8) - 9;
    if (var33 < 176 && var25 < 220 && var33 + 19 >= 0 && var25 + 19 >= 0) {
      game_->b_(g, var33, var25, 19, 19);
      if (facingRight) g->drawImage(Game::aL, var33, var25 - 95, 20);
      else CanvasShell::directGraphics->drawImageManip(Game::aL, var33, var25 - 95, 20, 8192);
    }
  }

  if (var6 + Game::K > hudHeight && var6 < 220 && invulnTimer % 2 == 0) {
    jbyte fr = ANIM_FRAMES[kind][animState][animFrame];
    const jbyte* f = FRAME_GEOMETRY[fr];
    int f0 = f[0] & 255, f1 = f[1] & 255, f2 = f[2] & 255, f3 = f[3] & 255, f4 = f[4] & 255, f5 = f[5] & 255;
    if (var5 + f4 < 176 && var6 + f5 < 220 && var5 + f4 + f2 >= 0 && var6 + f5 + f3 >= 0) {
      game_->b_(g, var5 + f4, var6 + f5, f2, f3);
      if (ap == 0 && facingRight) {
        g->drawImage(Game::aH, var5 + f4 - f0, var6 + f5 - f1, 20);
      } else {
        game_->a_(g, var5 + f4 - (Game::aH->getWidth() - f0 - f2), var6 + f5 - f1, 0);
      }
    }
  }

  if (currentWeapon != 0) {
    jbyte fr = ANIM_FRAMES[kind][animState][animFrame];
    int mx = MUZZLE_X_BY_FRAME[fr];
    int my = MUZZLE_Y_BY_FRAME[fr];
    if (animState == 6) my += tileHeight >> 1;
    if (mx != 44 && my != 44) {
      int w = Game::aI->getWidth();
      if (facingRight) var5 = (posX >> 8) + Game::x + mx;
      else var5 = (posX >> 8) + Game::x - mx - w;
      var6 = row * tileHeight + (posInRow >> 8) + Game::y + Game::L + my;
      int wi = currentWeapon - 1;
      game_->b_(g, var5, var6, w, 25);
      if (facingRight) g->drawImage(Game::aI, var5, var6 - wi * 25, 20);
      else CanvasShell::directGraphics->drawImageManip(Game::aI, var5, var6 - wi * 25, 20, 8192);
    }
  }
}
