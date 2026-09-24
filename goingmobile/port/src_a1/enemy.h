// enemy.h -- port of goingmobile/src_a1/Enemy.java (class f), scoped to
// milestone 3.1a's boot-chain needs (see player.h's header note for the
// precedent/rationale): static data tables, constructor, and asset
// loading (loadAssets/loadAnimTables/loadHitboxTables). Behavior
// (tick/render/AI/melee -- see src_a1/Enemy.java) is not transcribed yet;
// that's milestone 3.2a.
#pragma once
#include "entity.h"

class Game;

class Enemy : public Entity {
 public:
  static jbyte HITBOX_X_OFFSETS[5];
  static jbyte HITBOX_Y_OFFSETS[5];
  static jbyte HITBOX_WIDTHS[5];
  static jbyte HITBOX_HEIGHTS[5];
  static jbyte ATTACK_X_OFFSETS[5];
  static jbyte ATTACK_Y_OFFSETS[5];
  static jbyte ATTACK_WIDTHS[5];
  static jbyte ATTACK_HEIGHTS[5];
  static const jbyte HP_BY_ANIM[27];
  static const jbyte DAMAGE_BY_ANIM[27];
  static const jbyte BOLTS_DROPPED_BY_ANIM[25];
  static const jbyte ATTACK_WINDUP_TICKS[24];
  static const jshort SPEED_BY_TYPE[5];
  static const jbyte ATTACK_COOLDOWN_BY_TYPE[5];
  // Java allocates these as new byte[5][8][5] / [5][8] -- fixed
  // dimensions, not ragged.
  static jbyte ANIM_FRAMES[5][8][5];
  static jbyte ANIM_FRAME_COUNTS[5][8];
  static jbyte ANIM_FRAME_EXTRA[5][8];
  static jbyte tileWidth;
  static jbyte tileHeight;
  static jshort hudHeight;

  bool wallCrawlFlipped = false;
  jbyte animKind = 0;
  jbyte bounceTimer = 0;
  bool wallAhead = false;
  bool edgeAhead = false;
  bool invalidTileAhead = false;
  jshort tileAhead = 0;
  jshort attackCooldown = 0;
  jbyte attackPhase = 0;
  jbyte spawnBitIndex = 0;
  jshort stateTimer = 0;
  jint patrolTargetX = 0;
  bool flattenedRenderFlag = false;

  explicit Enemy(Game* game);

  void loadAssets();
  void updateAnimation();
  void render(Graphics* g, int enemyIndex, int visibilityFlags, int cameraX, int cameraY);
  short x() const { return (short)(posX >> 8); }
  short y() const { return (short)(row * tileHeight + (posInRow >> 8) + tileHeight - hudHeight); }

 private:
  void loadAnimTables(const String& path);
  void loadHitboxTables(const String& path);

  Game* game_;
};
