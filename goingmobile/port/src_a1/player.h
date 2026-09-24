// player.h -- port of goingmobile/src_a1/Player.java (class b), scoped to
// milestone 3.1a's boot-chain needs: static data tables, the constructor,
// and asset loading (loadAssets/loadAnimFile/readBytes) -- real, matching
// the precedent milestone 3.1 set for the legacy build ("Player/Enemy/
// LevelMap/Projectile ship their complete static data tables and
// constructor-faithful initialization; their behavior methods... are
// declared and stubbed"). Physics/AI/rendering (tick/render/fire/
// meleeHit/swingUpdate/zipRide/... -- see src_a1/Player.java) are not
// transcribed yet; that's milestone 3.2a.
#pragma once
#include "entity.h"

class Game;

class Player : public Entity {
 public:
  static const jbyte MUZZLE_X_BY_FRAME[28];
  static const jbyte MUZZLE_Y_BY_FRAME[28];
  // Per-frame [hotspotX, hotspotY, width, height, drawOffsetX, drawOffsetY].
  static const jbyte FRAME_GEOMETRY[29][6];
  static const jshort AMMO_CAPACITY[24];
  static const jshort INITIAL_AMMO[8];
  static const jshort AMMO_PER_PICKUP[8];
  static const jbyte CHARGE_TICKS[3][8];
  static const jint h[5];  // unconfirmed -- see src_a1/Player.java
  static const jbyte MELEE_DAMAGE_BY_LEVEL[3];
  static jbyte tileWidth;
  static jbyte tileHeight;
  static jshort hudHeight;
  static jshort terminalVelocityY;
  static jshort currentWalkSpeed;
  static const jbyte SWING_X_OFFSET[2];
  static const jbyte SWING_Y_OFFSET[2];
  static const jbyte SWING_WIDTH[2];
  static const jbyte SWING_HEIGHT[2];

  jbyte jumpPhase = 0;
  jbyte zipGrabRetryDelay = 0;
  jbyte swingPhase = 0;
  jbyte swingEndTimer = 0;
  jint swingTargetX = 0;
  jint swingTargetY = 0;
  jint swingCurX = 0;
  jint swingCurY = 0;
  jint swingStepX = 0;
  jint swingStepY = 0;
  jbyte hyperCastTimer = 0;
  jbyte swingTargetType = 0;
  jshort invulnTimer = 0;
  jbyte grabTileType = 0;
  bool ledgeAhead = false;
  jshort ledgeSnapOffsetX = 0;
  bool roomEdgeReached = false;

  // Java allocates these as new byte[1][15][4] / [1][15] -- fixed
  // dimensions, not ragged, so plain static arrays suffice.
  static jbyte ANIM_FRAMES[1][15][4];
  static jbyte ANIM_FRAME_COUNTS[1][15];
  static jbyte ANIM_FRAME_EXTRA[1][15];

  jbyte currentWeapon = 0;
  jshort* ammo = nullptr;       // [8]
  jshort* N = nullptr;          // [8], unconfirmed
  jbyte ownedWeapons = 0;
  jbyte* weaponLevel = nullptr; // [8]
  jint Q = 0;
  jbyte R = 0;
  jshort* S = nullptr;  // [8], unconfirmed
  jshort* T = nullptr;  // [8], unconfirmed
  jbyte* U = nullptr;   // [8], unconfirmed
  jint zipVelX = 0;
  jint zipVelY = 0;
  jint zipEndY = 0;
  bool ledgeSnapPending = false;

  explicit Player(Game* game);

  void loadAssets();
  short x() const { return (short)(posX >> 8); }
  short y() const { return (short)(row * tileHeight + (posInRow >> 8)); }
  void updateAnimation();
  void resetAttackAnim();
  void render(Graphics* g, int renderVariant, int cameraX, int cameraY);

 private:
  void loadAnimFile(const String& path);

  Game* game_;
};
