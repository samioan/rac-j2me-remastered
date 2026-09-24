// enemy.cpp -- implementation of Enemy (see enemy.h), transcribed from
// src_a1/Enemy.java's constructor and asset loading.
#include "enemy.h"
#include "game.h"
#include <cstddef>

const jbyte Enemy::HP_BY_ANIM[27] = {6,  10, 16, 5,  8,  10, 5, 5, 5,  15, 15, 15, 22, 22,
                                      22, 5,  5,  5,  5,  5,  5, 5, 5,  5,  3,  0,  0};
const jbyte Enemy::DAMAGE_BY_ANIM[27] = {1, 3, 4, 4, 5, 6, 1, 3, 1, 1, 3, 1, 1, 3,
                                          1, 1, 3, 4, 2, 3, 4, 2, 3, 4, 0, 2, 4};
const jbyte Enemy::BOLTS_DROPPED_BY_ANIM[25] = {1, 2, 3, 1, 2, 3, 1, 1, 1, 2, 2, 2, 3,
                                                 3, 3, 1, 1, 1, 2, 2, 2, 3, 3, 3, 0};
const jbyte Enemy::ATTACK_WINDUP_TICKS[24] = {0, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 1,
                                               3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1};
const jshort Enemy::SPEED_BY_TYPE[5] = {1024, 512, 768, 0, 512};
const jbyte Enemy::ATTACK_COOLDOWN_BY_TYPE[5] = {0, 20, 5, 5, 0};

jbyte Enemy::HITBOX_X_OFFSETS[5] = {};
jbyte Enemy::HITBOX_Y_OFFSETS[5] = {};
jbyte Enemy::HITBOX_WIDTHS[5] = {};
jbyte Enemy::HITBOX_HEIGHTS[5] = {};
jbyte Enemy::ATTACK_X_OFFSETS[5] = {};
jbyte Enemy::ATTACK_Y_OFFSETS[5] = {};
jbyte Enemy::ATTACK_WIDTHS[5] = {};
jbyte Enemy::ATTACK_HEIGHTS[5] = {};
jbyte Enemy::ANIM_FRAMES[5][8][5] = {};
jbyte Enemy::ANIM_FRAME_COUNTS[5][8] = {};
jbyte Enemy::ANIM_FRAME_EXTRA[5][8] = {};
jbyte Enemy::tileWidth = 0;
jbyte Enemy::tileHeight = 0;
jshort Enemy::hudHeight = 0;

Enemy::Enemy(Game* game) : game_(game) {
  tileWidth = Game::tileWidth;
  tileHeight = Game::tileHeight;
  hudHeight = Game::hudHeight;
  kind = -1;
  ap = 0;
  stateTimer = 0;
  facingRight = true;
  wallCrawlFlipped = false;
  wallAhead = false;
  edgeAhead = false;
  invalidTileAhead = false;
  bounceTimer = 0;
}

void Enemy::loadAssets() {
  loadAnimTables("/enemy.bin");
  loadHitboxTables("/enemy_spr_box.bin");
}

// 5 bytes each, alternating scaled-by-tileWidth (Game::J) and
// scaled-by-tileHeight (Game::K) reads -- x/w pairs scale by tile width,
// y/h pairs by tile height, matching the x,y,w,h,x,y,w,h field order
// (HITBOX_* then ATTACK_*). See src_a1/Enemy.java's loadAnimFile(InputStream,...).
void Enemy::loadHitboxTables(const String& path) {
  ByteArray data = loadResource(path);
  size_t pos = 0;
  auto readGroup = [&](jbyte* out, bool scaleByWidth) {
    jbyte scale = scaleByWidth ? Game::J : Game::K;
    for (int i = 0; i < 5; i++) {
      jbyte v = pos < data.size() ? (jbyte)std::to_integer<uint8_t>(data[pos++]) : 0;
      out[i] = (jbyte)(v * scale / 44);
    }
  };
  readGroup(HITBOX_X_OFFSETS, true);
  readGroup(HITBOX_Y_OFFSETS, false);
  readGroup(HITBOX_WIDTHS, true);
  readGroup(HITBOX_HEIGHTS, false);
  readGroup(ATTACK_X_OFFSETS, true);
  readGroup(ATTACK_Y_OFFSETS, false);
  readGroup(ATTACK_WIDTHS, true);
  readGroup(ATTACK_HEIGHTS, false);
}

// For each of 5 types x 8 anim slots, a (frameCount, extraTicks) byte pair
// into ANIM_FRAME_COUNTS/ANIM_FRAME_EXTRA, then frameCount raw frame-index
// bytes into ANIM_FRAMES[type][anim].
void Enemy::loadAnimTables(const String& path) {
  ByteArray data = loadResource(path);
  size_t pos = 0;
  auto readByte = [&]() -> jbyte {
    return pos < data.size() ? (jbyte)std::to_integer<uint8_t>(data[pos++]) : 0;
  };
  for (int type = 0; type < 5; type++) {
    for (int anim = 0; anim < 8; anim++) {
      jbyte count = readByte();
      jbyte extra = readByte();
      ANIM_FRAME_COUNTS[type][anim] = count;
      ANIM_FRAME_EXTRA[type][anim] = extra;
      for (int f = 0; f < count && f < 5; f++) ANIM_FRAMES[type][anim][f] = readByte();
    }
  }
}
