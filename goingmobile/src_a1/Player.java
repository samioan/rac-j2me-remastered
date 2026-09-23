// Hand-written, faithfully renamed from decompiled_a1/b.java (class b).
// a1's equivalent of the legacy build's Player (decompiled/a.java, see
// ../src/Player.java). Single instance (Game.player, confirmed via
// Entity.java/Projectile.java/Enemy.java cross-checks).
//
// IMPORTANT lesson from Enemy.java's self-review, doubly confirmed here:
// Player's own x()/y() pixel-position methods (`b()`/`c()`) are the
// OPPOSITE of Enemy's (Enemy: b()=y, c()=x; Player: b()=x, c()=y) --
// same obfuscated letters, unrelated meaning, because each class was
// obfuscated independently. Nothing renamed in this file assumes any
// role from Enemy.java, Projectile.java, or the legacy build's Player
// without re-deriving it from this file's own bodies (or, where noted,
// an exact-value or exact-dimension match to the legacy build's
// already-documented tables, which is separate from assuming behavior).
//
// Confirmed static tables (exact-value or exact-formula matches to the
// legacy build's Player statics, described in CLASS_MAP.md's legacy
// section): AMMO_CAPACITY (24 = 8 weapons x 3 levels, same `weapon*3+
// level` indexing formula, used in refillAmmo()); INITIAL_AMMO (8
// values byte-for-byte identical to the legacy build's `e`); AMMO_PER_
// PICKUP (8 values byte-for-byte identical to the legacy build's `f`,
// and used identically in refillAmmo()); CHARGE_TICKS ([3][8], same
// shape as the legacy build's `g`, not used in this file so unconfirmed
// role beyond the shape match). ANIM_FRAMES/ANIM_FRAME_COUNTS/
// ANIM_FRAME_EXTRA loaded from `/player.bin`, same [1][15][*] shape as
// the legacy build's G/H/I loaded from `/r`.
//
// Everything else is TENTATIVE unless marked "confirmed" inline -- named
// from this file's own call shape (which fields move together, what
// they're compared against, what they gate), not from the legacy build.
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;

public final class Player extends Entity {
   public static final byte[] MUZZLE_X_BY_FRAME = new byte[]{-2, -2, -2, 2, 0, -1, 1, -1, -2, 0, 0, -1, 0, 44, 44, 44, 44, 44, 44, -2, -3, 0, 0, 0, 44, 44, 44, 44};
   public static final byte[] MUZZLE_Y_BY_FRAME = new byte[]{15, 13, 13, 12, 13, 14, 15, 7, 7, 12, 17, 20, 12, 44, 44, 44, 44, 44, 44, 14, 15, 12, 13, 13, 44, 44, 44, 44};
   // Per-frame [hotspotX, hotspotY, width, height, drawOffsetX, drawOffsetY]
   // (byte values masked with & 255 at every use site) -- names are a
   // best-effort read of render()'s usage, not independently confirmed.
   public static final byte[][] FRAME_GEOMETRY = new byte[][]{
      {0, 0, 20, 35, 11, 9},
      {0, 119, 20, 36, 11, 8},
      {20, 119, 20, 36, 11, 8},
      {20, 0, 25, 35, 8, 9},
      {0, -101, 30, 31, 5, 8},
      {108, 0, 27, 37, 7, 7},
      {57, 120, 23, 34, 9, 10},
      {0, 79, 20, 40, 10, 1},
      {30, -101, 25, 31, 9, 4},
      {-121, 0, 23, 39, 7, 5},
      {68, -102, 23, 29, 10, 15},
      {40, 119, 17, 36, 13, 8},
      {91, 35, 20, 43, 10, 1},
      {0, 35, 30, 44, 0, 0},
      {111, 39, 44, 42, 0, 2},
      {108, 121, 43, 28, 1, 16},
      {125, 81, 24, 36, 8, 2},
      {20, 79, 44, 40, 0, 0},
      {64, 79, 36, 41, 7, 3},
      {45, 0, 26, 35, 9, 9},
      {80, 120, 28, 34, 7, 10},
      {100, 81, 25, 40, 6, 3},
      {30, 35, 31, 44, 1, 0},
      {61, 35, 31, 44, 1, 0},
      {91, -102, 24, 33, 11, 9},
      {71, 0, 37, 35, 7, 9},
      {115, -107, 41, 27, 2, 16},
      {115, -80, 40, 11, 1, 33}
   };
   public static final short[] AMMO_CAPACITY = new short[]{0, 0, 0, 100, 150, 200, 35, 45, 60, 25, 30, 40, 40, 55, 70, 200, 250, 300, 1, 1, 1, 70, 90, 110};
   public static final short[] INITIAL_AMMO = new short[]{0, 50, 20, 15, 20, 100, 1, 30};
   public static final short[] AMMO_PER_PICKUP = new short[]{0, 15, 7, 5, 8, 30, 1, 10};
   public static final byte[][] CHARGE_TICKS = new byte[][]{{0, 4, 16, 20, 16, 5, 4, 5}, {0, 4, 16, 20, 16, 5, 4, 5}, {0, 4, 16, 20, 16, 5, 4, 5}};
   // Unconfirmed -- not referenced anywhere in this file; presumably
   // read by Game. Values kept verbatim, no legacy-build equivalent
   // (the legacy build's weapon-price table is 8 entries, this is 5).
   public static final int[] h = new int[]{50, 800, 2500, 18000, 0};
   // Melee/swing damage by weapon level (3 entries = 3 levels), applied
   // in meleeHit() indexed by weaponLevel[0] (normal) or weaponLevel[1]
   // (during a swing/hyper-shot, animState 11) -- confirmed by
   // meleeHit()'s own indexing, `i[this.weaponLevel[1]]` /
   // `i[this.weaponLevel[0]]`.
   public static final byte[] MELEE_DAMAGE_BY_LEVEL = new byte[]{3, 8, 8};
   private Game game;
   public static byte tileWidth;
   public static byte tileHeight;
   public static short hudHeight;
   public static short terminalVelocityY;
   // Reused as a small preset-speed selector in tick() (1536 normal,
   // 768 crouch/hyper-state, 384 ledge-hang) -- TENTATIVE name.
   public static short currentWalkSpeed;
   // Hyper-shot/swing attack hitbox per swingTargetType (2 entries) --
   // x-offset/y-offset/width/height, confirmed by meleeHit()'s own
   // indexing (`o[this.swingTargetType]` etc.).
   public static final byte[] SWING_X_OFFSET = new byte[]{14, -11};
   public static final byte[] SWING_Y_OFFSET = new byte[]{4, 17};
   public static final byte[] SWING_WIDTH = new byte[]{10, 20};
   public static final byte[] SWING_HEIGHT = new byte[]{10, 50};
   // jumpPhase: -1 grounded/idle, incremented through 0/1/2 during a
   // jump/hyper-shot-swing sequence; 2 gates a "hyper" walk-speed preset
   // (tick()'s `this.jumpPhase == 2` -> currentWalkSpeed = 768).
   // TENTATIVE, named by analogy to the *shape* of legacy Player.p, but
   // re-derived here from this file's own -1/0/1/2 usage, not assumed.
   public byte jumpPhase;
   // TENTATIVE: a distinct field from swingPhase/swingEndTimer (original
   // letter `t`, vs. their `u`/`v` -- confirmed distinct by re-reading
   // every bare `this.t`/`this.u`/`this.v` site in the original after an
   // earlier draft of this file wrongly merged all three; also distinct
   // from the method `this.t()` = updatePickupMagnet(), a field/method
   // letter collision like every other class in this codebase). Only
   // read/written in tick()'s
   // zip-line-grab-detection gate: 0 = re-scan for a grab this tick,
   // nonzero = skip scanning and count down. Never set anywhere else in
   // this file, so its nonzero value must come from Game (obfuscated,
   // not confirmed).
   public byte zipGrabRetryDelay;
   // Swing/zip-ride phase state machine, -2 = inactive. See zipRide()/
   // swingUpdate() for the full -2..5 sequence. TENTATIVE names.
   public byte swingPhase;
   public byte swingEndTimer;
   public int swingTargetX;
   public int swingTargetY;
   public int swingCurX;
   public int swingCurY;
   public int swingStepX;
   public int swingStepY;
   public byte hyperCastTimer;  // counts 0..15 (updateHyperCastTimer()), resets swingTargetType on wrap
   // swingTargetType: 0/1, selects which of the two hyper-shot target
   // tile ids (97/98, scanned by hyperShotSearch()) is being tracked --
   // confirmed by hyperShotSearch()'s own tile-97/98 branches matching
   // this field's 2-valued range and SWING_*'s 2-entry tables.
   public byte swingTargetType;
   public short invulnTimer;    // TENTATIVE: gated a render blink (`% 2 == 0`); not itself incremented anywhere in this file
   public byte grabTileType;    // TENTATIVE: tile id cached when starting a zip/swing grab, compared against 97 in swingUpdate()
   public boolean ledgeAhead;   // TENTATIVE: "ledge/step to snap onto" detected by groundYAhead()
   public short ledgeSnapOffsetX; // TENTATIVE: small x-nudge applied when snapping onto a ledge
   private boolean roomEdgeReached; // TENTATIVE: set when posX/row hits a room boundary; drives the Game.j(1/2/5/6) room-transition calls in handleTileInteractions()
   public static byte[][][] ANIM_FRAMES;
   public static byte[][] ANIM_FRAME_COUNTS;
   public static byte[][] ANIM_FRAME_EXTRA;
   public byte currentWeapon;   // confirmed: indexes ammo[]/weaponLevel[] throughout fire()/render()
   public short[] ammo;         // confirmed: fire()'s ammo check, refillAmmo()'s pickup logic
   public short[] N;            // unconfirmed -- declared/sized like ammo (short[8]) but never referenced in this file
   public byte ownedWeapons;    // confirmed: bitmask tested before refilling a weapon's ammo, and before firing weapon 5 (`& 32`)
   public byte[] weaponLevel;   // confirmed: AMMO_CAPACITY/MELEE_DAMAGE_BY_LEVEL indexing, fire()'s projectile-type offset
   public int Q;                // unconfirmed -- not referenced in this file
   public byte R;               // unconfirmed -- not referenced in this file
   public short[] S;            // unconfirmed -- not referenced in this file
   public short[] T;            // unconfirmed -- not referenced in this file
   public byte[] U;             // unconfirmed -- not referenced in this file
   public int zipVelX;          // TENTATIVE: per-tick step while riding a zip line (zipRide())
   public int zipVelY;
   public int zipEndY;          // TENTATIVE: the y (pixel<<8, offset by hudHeight) at which zipRide() ends the ride
   public boolean ledgeSnapPending; // TENTATIVE: one-tick-delayed copy of ledgeAhead, used to detect the falling edge

   public Player(Game var1) {
      this.game = var1;
      tileWidth = Game.tileWidth;
      tileHeight = Game.tileHeight;
      hudHeight = Game.hudHeight;
      currentWalkSpeed = 1536;
      this.jumpPhase = -1;
      this.swingPhase = -2;
      this.hyperCastTimer = -1;
      this.swingTargetType = 0;
      this.ledgeAhead = false;
      this.zipGrabRetryDelay = 0;
      this.invulnTimer = 0;
      this.roomEdgeReached = false;
      this.ammo = new short[8];
      this.N = new short[8];
      this.weaponLevel = new byte[8];
      this.T = new short[8];
      this.S = new short[8];
      this.U = new byte[8];
      super.velY = 0;
      super.velX = 0;
      super.ap = 0;
      super.facingRight = true;
      super.health = 20;
      terminalVelocityY = (short)(tileHeight >> 1 << 8);
   }

   public final void loadAssets() {
      if (ANIM_FRAMES == null) {
         ANIM_FRAMES = new byte[1][15][4];
      }

      if (ANIM_FRAME_COUNTS == null) {
         ANIM_FRAME_COUNTS = new byte[1][15];
      }

      if (ANIM_FRAME_EXTRA == null) {
         ANIM_FRAME_EXTRA = new byte[1][15];
      }

      this.loadAnimFile("/player.bin");
   }

   public final short x() {
      return (short)(super.posX >> 8);
   }

   public final short y() {
      return (short)(super.row * tileHeight + (super.posInRow >> 8));
   }

   public final byte columnRight() {
      return (byte)(((super.posX >> 8) + 8) / tileWidth);
   }

   public final byte columnLeft() {
      return (byte)(((super.posX >> 8) - 8) / tileWidth);
   }

   // Ground-ahead scan, delegated to Game's own (still-obfuscated)
   // helpers C()/D(int,int)/B() rather than an inline tile scan like
   // Enemy's equivalent -- sets ledgeAhead when the platform-scan result
   // (B()) beats the tile-scan result (D()).
   public final short groundYAhead(boolean var1) {
      int var2 = 17 * tileHeight;
      int var3 = 4 * tileHeight / 44;
      if (var1) {
         var3 = 18 * tileHeight / 44;
      }

      if (super.posInRow > var3 << 8) {
         if ((LevelMap.columnSolidMasks[this.columnRight()] & 1 << super.row + 2) > 0 || (LevelMap.columnSolidMasks[this.columnLeft()] & 1 << super.row + 2) > 0) {
            var2 = (super.row + 1) * tileHeight;
         }
      } else if ((LevelMap.columnSolidMasks[this.columnRight()] & 1 << super.row + 1) > 0 || (LevelMap.columnSolidMasks[this.columnLeft()] & 1 << super.row + 1) > 0) {
         var2 = super.row * tileHeight;
      }

      short var4 = this.game.C();
      short var5 = (short)this.game.D(var2, var4);
      short var6 = this.game.B();
      this.ledgeAhead = false;
      if (var6 < var5) {
         this.ledgeAhead = true;
         return var6;
      } else {
         return var5;
      }
   }

   // TENTATIVE name -- checks the tile directly below (and, near a
   // column edge, the adjacent column's tile too) for tile id 19.
   public final boolean onLadderTop() {
      int var1;
      int var2 = (var1 = super.posX >> 8) / tileWidth;
      int var3;
      if ((var3 = var1 % tileWidth) < 10) {
         if (this.game.levelMap.tiles[var2 - 1][super.row + 1] != 19) {
            return false;
         }
      } else if (var3 > tileWidth - 10 && this.game.levelMap.tiles[var2 + 1][super.row + 1] != 19) {
         return false;
      }

      return this.game.levelMap.tiles[var2][super.row + 1] == 19;
   }

   // TENTATIVE names -- wall/ledge detection at the near edge of the
   // current column, using the inherited Entity.column().
   public final boolean wallOnLeft() {
      return !this.game.levelMap.isWalkable(this.column() - 1, super.row + 1) && ((super.posX >> 8) - 8) % tileWidth < 8;
   }

   public final boolean wallOnRight() {
      return !this.game.levelMap.isWalkable(this.column() + 1, super.row + 1) && ((super.posX >> 8) + 8) % tileWidth > tileWidth - 8;
   }

   // Zip-line ride tick: on entry (swingPhase == -2, Game.cv sentinel
   // -1) starts the ride and requests a hint (Game.B(0,-1)); otherwise
   // advances posX/posInRow by the per-tick step (zipVelX/zipVelY) and
   // ends the ride once past zipEndY.
   public final void zipRide() {
      if (this.game.cv == -1) {
         this.setAnimState((byte)2);
         this.game.cv = super.facingRight ? -4 : -3;
         super.velY = 3584;
         super.velX = 0;
         this.jumpPhase = 0;
         this.game.B(0, -1);
      } else {
         super.posX = super.posX + this.zipVelX;
         super.posInRow = super.posInRow + this.zipVelY;
         this.wrapRow();
         if (this.y() + Game.K > this.zipEndY) {
            this.setAnimState((byte)3);
            super.velX = 0;
            this.game.cw = this.game.cv = 0;
         }
      }
   }

   // The swing/hyper-shot-launch state machine (swingPhase -2..5):
   // 0 = riding toward the swing anchor along a computed arc, snapping
   // to it (swingTargetX/Y) once close; 1 = airborne launch away from
   // the anchor; 2/3 = a brief pause-then-release; 4 = flying toward
   // the target under a fixed step (swingStepX/Y) until in range;
   // 5 = arcing into a landing animation with a facing-dependent curve
   // lookup table (var6/var9), ending in a fall (animState 2) with a
   // short post-swing grace window (swingEndTimer = 15).
   public final void swingUpdate() {
      if (this.swingPhase == 0) {
         short var8 = this.groundYAhead(true);
         super.velY = (short)(super.velY - 128);
         super.posInRow = super.posInRow - super.velY;
         this.wrapRow();
         if (this.y() >= var8) {
            super.row = (byte)(var8 / tileHeight);
            super.posInRow = (short)(var8 % tileHeight);
         }

         this.swingCurX = this.swingCurX + this.swingStepX;
         this.swingCurY = this.swingCurY + this.swingStepY;
         if (this.swingCurY < this.swingTargetY || this.abs(this.swingCurX - this.swingTargetX) < 1280 && this.abs(this.swingCurY - this.swingTargetY) < 1280) {
            if (this.grabTileType == 97) {
               this.swingPhase = 1;
            } else {
               this.swingPhase = 3;
               if (this.jumpPhase == -1) {
                  super.velY = (short)(1320 * tileHeight / 44);
                  this.swingPhase = 2;
               }
            }

            int var11 = 9 * tileWidth / 44;
            if (!super.facingRight) {
               var11 = -var11;
            }

            int var14 = 6 * tileHeight / 44;
            int var15 = (super.posX >> 8) + var11 << 8;
            int var16 = super.row * tileHeight + (super.posInRow >> 8) + var14 << 8;
            this.swingStepX = (this.swingTargetX - var15) / 10;
            this.swingStepY = (this.swingTargetY - var16) / 10;
            return;
         }
      } else if (this.swingPhase == 1) {
         super.animFrame = 1;
         super.posX = super.posX + this.swingStepX;
         super.posInRow = super.posInRow + this.swingStepY;
         this.wrapRow();
         super.velY = 0;
         if (this.y() << 8 < this.swingTargetY || this.abs((this.x() << 8) - this.swingTargetX) < 1280 && this.abs((this.y() << 8) - this.swingTargetY) < 1280) {
            this.setAnimState((byte)3);
            this.swingPhase = -2;
            this.swingEndTimer = 0;
            Game.A = false;
            this.game.cw = this.game.cv = 0;
            return;
         }
      } else if (this.swingPhase == 2) {
         super.animFrame = 1;
         super.velY = (short)(super.velY - 128);
         super.posInRow = super.posInRow - super.velY;
         this.wrapRow();
         if (super.velY < 0) {
            this.swingPhase = 3;
            return;
         }
      } else if (this.swingPhase == 3) {
         int var1 = (this.swingTargetY >> 8) - this.y();
         int var2 = (this.swingTargetX >> 8) - this.x();
         int var3 = (tileWidth + (tileWidth >> 1)) * (tileWidth + (tileWidth >> 1)) + (tileHeight + (tileHeight >> 1)) * (tileHeight + (tileHeight >> 1));
         this.swingPhase = 5;
         super.velX = 0;
         super.velY = 0;
         if (var2 * var2 + var1 * var1 > var3) {
            super.velX = (short)(var2 << 4);
            super.velY = (short)(var1 << 4);
            this.swingPhase = 4;
            return;
         }
      } else {
         if (this.swingPhase == 4) {
            int var7 = (this.swingTargetY >> 8) - this.y();
            int var10 = (this.swingTargetX >> 8) - this.x();
            int var13 = (tileWidth + (tileWidth >> 1)) * (tileWidth + (tileWidth >> 1)) + (tileHeight + (tileHeight >> 1)) * (tileHeight + (tileHeight >> 1));
            if (var10 * var10 + var7 * var7 > var13) {
               super.posX = super.posX + super.velX;
               super.posInRow = super.posInRow + super.velY;
               this.wrapRow();
               return;
            }

            this.swingPhase = 5;
            super.velY = 0;
            super.velX = 0;
            return;
         }

         if (this.swingPhase == 5) {
            super.animFrame = 1;
            short var6 = 0;
            short var9 = 0;
            int var12 = this.y() - (this.swingTargetX >> 8) + 6 * tileHeight / 44;
            int var4;
            int var5;
            if ((var4 = super.facingRight ? (this.swingTargetY >> 8) - (this.x() + 9) : this.x() - 9 - (this.swingTargetY >> 8)) == 0) {
               var5 = 2000;
            } else {
               var5 = (var12 << 8) / var4;
            }

            if (var5 < -1945 || var5 > 1945) {
               var6 = 2880;
               var9 = 0;
            } else if (var5 > 616) {
               var6 = 2781;
               var9 = 447;
            } else if (var5 > 333) {
               var6 = 2494;
               var9 = 864;
            } else if (var5 > -10) {
               var6 = 2036;
               var9 = 1222;
            } else if (var5 > -334) {
               var6 = 2036;
               var9 = -1222;
            } else if (var5 > -617) {
               var6 = 2494;
               var9 = -864;
            } else if (var5 > -1946) {
               var6 = 2781;
               var9 = -447;
            }

            if (super.facingRight) {
               super.posX += var6;
            } else {
               super.posX -= var6;
            }

            super.posInRow += var9;
            super.velY = 447;
            this.wrapRow();
            if (var5 < 0 && var5 > -150) {
               this.setAnimState((byte)2);
               super.animRestart = 0;
               this.swingPhase = -1;
               this.swingEndTimer = 15;
               this.game.cw = this.game.cv = 0;
            }
         }
      }
   }

   public final void startFall() {
      this.setAnimState((byte)2);
      this.jumpPhase = 0;
      super.ap = 0;
   }

   // Master per-tick dispatcher (mirrors Enemy.tick()'s role): death,
   // hurt-invuln handling, the zip/swing state machines, then walk
   // physics, wall/enemy-bounce collision, ladder handling, room-edge
   // triggers and zip-line-grab detection. See inline notes -- this is
   // the single largest, least-independently-verified method in this
   // file; every `this.game.xxx` is Game's own obfuscated member.
   public final void tick() {
      this.game.d = true;
      if (super.velY <= -terminalVelocityY) {
         super.velY = (short)(-terminalVelocityY);
      }

      if (super.animState == 10 && super.animRestart == 2) {
         this.game.w();
         if (this.game.cS) {
            this.game.cS = false;
         }

         Game.dm++;
      } else if (super.animState == 10) {
         this.game.cw = this.game.cv = 0;
         this.game.cx &= -129;
      } else if (super.health <= 0 && super.animState != 10) {
         this.game.midlet.playSoundIfEnabled(0);
         super.health = 0;
         super.animRestart = 1;
         this.setAnimState((byte)10);
         Game.dm++;
      } else if (this.swingPhase >= 0) {
         this.swingUpdate();
         this.updatePickupMagnet();
      } else if (super.animState == 6) {
         this.zipRide();
         this.updatePickupMagnet();
         this.handleTileInteractions();
      } else {
         if (super.animState == 11) {
            this.game.cw = this.game.cv = 0;
            super.velX = 0;
            this.swingTargetType = 1;
            this.meleeHit();
         } else if (super.animState == 9) {
            if (++super.animHold > 6) {
               if (this.jumpPhase == -1) {
                  this.setAnimState((byte)0);
               } else {
                  super.velX = 0;
                  super.velY = 0;
                  this.setAnimState((byte)3);
                  this.jumpPhase = 0;
               }
            } else {
               this.game.cv = 0;
            }
         }

         if (this.game.cv == -4 || this.game.cw == -4 || super.animState == 8 && super.facingRight) {
            if (super.animState == 4) {
               this.game.cv = 0;
            }

            if (!super.facingRight || super.animState != 1) {
               if (this.jumpPhase == 2) {
                  currentWalkSpeed = 768;
               } else if (super.animState == 8) {
                  currentWalkSpeed = 384;
               } else if (this.jumpPhase > -1) {
                  currentWalkSpeed = 1536;
               } else {
                  this.setAnimState((byte)1);
                  currentWalkSpeed = 1536;
                  super.animRestart = 0;
               }

               super.velX = currentWalkSpeed;
               super.facingRight = true;
            }
         } else if (this.game.cv == -3 || this.game.cw == -3 || super.animState == 8 && !super.facingRight) {
            if (super.animState == 4) {
               this.game.cv = 0;
            }

            if (super.facingRight || super.animState != 1) {
               if (this.jumpPhase == 2) {
                  currentWalkSpeed = 768;
               } else if (super.animState == 8) {
                  currentWalkSpeed = 384;
               } else if (this.jumpPhase > -1) {
                  currentWalkSpeed = 1536;
               } else {
                  this.setAnimState((byte)1);
                  currentWalkSpeed = 1536;
                  super.animRestart = 0;
               }

               super.velX = (short)(-currentWalkSpeed);
               super.facingRight = false;
            }
         } else if (this.game.cv == 0) {
            if (this.jumpPhase == 2) {
               super.velX = (short)(super.facingRight ? 768 : -768);
            } else if (this.jumpPhase >= 0) {
               if (super.velX > 0) {
                  super.velX = (short)(super.velX - 196);
               } else if (super.velX < 0) {
                  super.velX = (short)(super.velX + 196);
               }

               if (super.velX <= 196 && super.velX >= -196) {
                  super.velX = 0;
               }
            } else {
               super.velX = 0;
            }

            if (super.animState == 1) {
               this.setAnimState((byte)0);
            }
         }

         if (this.game.cv == -1) {
            if (this.jumpPhase == -1) {
               super.velY = 3584;
               this.jumpPhase++;
            } else if (this.jumpPhase == 0) {
               if (Game.z) {
                  super.velY = 3584;
               } else {
                  super.velY = 3072;
                  super.animRestart = 1;
                  this.setAnimState((byte)13);
               }

               this.jumpPhase++;
            }

            this.game.B(0, -1);
         }

         if (super.animState == 4 && this.jumpPhase >= 0) {
            if (super.animCounter > 2) {
               this.setAnimState((byte)2);
            }

            super.animRestart = 0;
         } else {
            if (super.velX != 0) {
               super.ae = super.facingRight ? this.columnRight() : this.columnLeft();
               if (super.posInRow < currentWalkSpeed) {
                  super.af = super.row;
               } else {
                  super.af = (byte)(super.row + 1);
               }

               if (super.ae >= 0 && super.ae <= 27 && this.game.levelMap.isWalkable(super.ae, super.af) && (super.row < 17 || super.row == 17 && super.posInRow == 0)) {
                  int var1 = super.posX;
                  super.posX = super.posX + super.velX;
                  super.ae = super.facingRight ? this.columnRight() : this.columnLeft();
                  if (this.game.G() != -1) {
                     super.posX = super.posX - super.velX * 3 / 2;
                     if (!this.game.levelMap.isWalkable((super.posX >> 8) / tileWidth, super.af)) {
                        super.posX = var1;
                     }
                  } else if (this.game.D()) {
                     super.posX = super.posX - super.velX;
                  } else if (!this.game.levelMap.isWalkable(super.ae, super.af)) {
                     super.posX = super.posX - super.velX;
                  }
               } else {
                  super.velX = 0;
                  if (super.facingRight) {
                     if (this.columnRight() == super.ae) {
                        super.posX = super.ae * tileWidth - 8 - 1 << 8;
                     }
                  } else if (this.columnLeft() == super.ae) {
                     super.posX = super.ae * tileWidth + tileWidth + 8 + 1 << 8;
                  }
               }

               if (super.ae >= 27) {
                  this.roomEdgeReached = true;
               }

               if (super.ae == 0) {
                  this.roomEdgeReached = true;
               }
            }

            if (this.hyperCastTimer >= 0 && super.animState != 11) {
               this.updateHyperCastTimer();
            }

            short var8;
            if ((var8 = this.groundYAhead(false)) - this.y() > tileHeight >> 1) {
               this.ledgeAhead = false;
            }

            if (this.ledgeSnapPending && !this.ledgeAhead) {
               this.ledgeSnapPending = false;
               if (this.jumpPhase == -1) {
                  super.posInRow = 0;
               }
            }

            if (this.ledgeAhead && this.jumpPhase == -1) {
               this.ledgeSnapPending = this.ledgeAhead;
               super.row = (byte)(var8 / tileHeight);
               super.posInRow = (short)(var8 % tileHeight << 8);
               super.posX = super.posX + (this.ledgeSnapOffsetX << 8);
               super.velY = 0;
               if (!this.game.levelMap.isWalkable(this.columnLeft(), super.row)) {
                  super.posX = this.y() + 1 << 8;
               } else if (!this.game.levelMap.isWalkable(this.columnRight(), super.row)) {
                  super.posX = this.y() - 1 << 8;
               }
            } else {
               if ((this.jumpPhase >= 0 || this.x() < var8) && super.ap == 0) {
                  if (this.jumpPhase == 2) {
                     super.velY = -768;
                  } else {
                     super.velY = (short)(super.velY - 384);
                  }

                  super.posInRow = super.posInRow - super.velY;
                  if (super.velY > 0 && (!this.game.levelMap.isWalkable(this.columnRight(), super.row) || !this.game.levelMap.isWalkable(this.columnLeft(), super.row))) {
                     super.posInRow = super.posInRow + super.velY;
                     super.velY = 0;
                  }

                  if (super.posInRow < 0) {
                     super.posInRow = super.posInRow + (tileHeight << 8);
                     super.row--;
                     if (super.row < 0) {
                        super.row = 0;
                        super.posInRow = 0;
                     }

                     if (super.row == 0) {
                        this.roomEdgeReached = true;
                     }
                  } else if (super.posInRow > tileHeight << 8) {
                     super.posInRow = super.posInRow - (tileHeight << 8);
                     super.row++;
                  }

                  if (this.jumpPhase == -1) {
                     this.jumpPhase = 0;
                  }

                  if (super.animState != 11 && super.animState != 14) {
                     if (this.jumpPhase == 0) {
                        this.setAnimState((byte)2);
                     }

                     if (this.game.cv == -3 || !super.facingRight) {
                        super.facingRight = false;
                     } else if (this.game.cv == -4 || super.facingRight) {
                        super.facingRight = true;
                     }

                     if (super.velY <= 0 && this.jumpPhase == 1 && !Game.z && super.animState == 12) {
                        this.jumpPhase = 2;
                     } else if (super.velY <= -1024 && this.jumpPhase != 2) {
                        this.setAnimState((byte)3);
                     }
                  }
               }

               if (super.ap == 0) {
                  if (super.animState != 11 && super.velY < 0 && super.animState != 10) {
                     for (int var2 = 0; var2 < Game.enemyPoolSize; var2++) {
                        if (this.game.enemies[var2].animState != 2 && this.game.k(var2) && this.game.enemies[var2].y() > this.x()) {
                           this.setAnimState((byte)3);
                           if (this.y() > this.game.enemies[var2].x()) {
                              this.game.enemies[var2].bounceTimer = -10;
                              if (this.game.levelMap.isWalkable((super.posX >> 8) / tileWidth + 1, super.row + 1) && this.game.levelMap.isWalkable((super.posX >> 8) / tileWidth, super.row + 1)) {
                                 super.velY = 0;
                                 super.velX = 0;
                                 this.jumpPhase = 0;
                                 if (this.game.levelMap.isWalkable((super.posX >> 8) / tileWidth + 1, super.row)) {
                                    super.posX += 510;
                                 }

                                 if (!this.game.levelMap.isWalkable((super.posX >> 8) / tileWidth + 1, super.row)) {
                                    super.posX -= 510;
                                 }
                              } else {
                                 this.jumpPhase = -1;
                              }
                           } else {
                              this.game.enemies[var2].bounceTimer = 10;
                              if (this.game.levelMap.isWalkable((super.posX >> 8) / tileWidth - 1, super.row + 1) && this.game.levelMap.isWalkable((super.posX >> 8) / tileWidth, super.row + 1)) {
                                 super.velY = 0;
                                 super.velX = 0;
                                 this.jumpPhase = 0;
                                 if (this.game.levelMap.isWalkable((super.posX >> 8) / tileWidth - 1, super.row)) {
                                    super.posX -= 510;
                                 }

                                 if (!this.game.levelMap.isWalkable((super.posX >> 8) / tileWidth - 1, super.row)) {
                                    super.posX += 510;
                                 }
                              } else {
                                 this.jumpPhase = -1;
                              }
                           }
                        }
                     }
                  }

                  if (this.x() >= var8 && super.velY < 0) {
                     super.row = (byte)(var8 / tileHeight);
                     super.posInRow = (short)(var8 % tileHeight << 8);
                     this.roomEdgeReached = true;
                     super.velY = 0;
                     this.jumpPhase = -1;
                     if (super.animState == 3 || super.animState == 2 || super.animState >= 11 && super.animState <= 14) {
                        if (this.jumpPhase == 2) {
                           this.jumpPhase = 1;
                        }

                        this.setAnimState((byte)4);
                        super.animRestart = 1;
                     }
                  }
               }
            }

            if (super.animState != 10) {
               this.updatePickupMagnet();
               if (this.zipGrabRetryDelay == 0) {
                  int var9 = super.posX >> 8;
                  if (this.jumpPhase >= 0 && super.velY < 0) {
                     for (int var3 = 3; var3 >= 0; var3--) {
                        boolean var4;
                        if (this.game.bc[var3] != -1
                           && (
                              (var4 = this.game.be[var3] - this.game.bc[var3] > 0) && var9 > this.game.bc[var3] && var9 < this.game.be[var3]
                                 || !var4 && var9 < this.game.bc[var3] && var9 > this.game.be[var3]
                           )) {
                           int var5;
                           int var6 = (var5 = this.game.bh[var3] * var9 / this.game.bg[var3] + this.game.bi[var3]) - (this.y() + (Game.K >> 1) << 8);
                           if (this.abs(var6) < -super.velY) {
                              var5 -= Game.K << 8;
                              super.row = (byte)((var5 >> 8) / tileHeight);
                              super.posInRow = (short)((var5 >> 8) % tileHeight << 8);
                              this.jumpPhase = -1;
                              this.setAnimState((byte)6);
                              super.velX = super.velY = 0;
                              super.facingRight = var4;
                              int var7 = (this.abs(this.game.be[var3] - this.game.bc[var3]) << 8) / 2304;
                              this.zipVelX = (this.game.bg[var3] << 8) / var7;
                              this.zipVelY = this.game.bh[var3] / var7;
                              this.zipEndY = this.game.bf[var3];
                              break;
                           }
                        }
                     }
                  }
               } else {
                  this.zipGrabRetryDelay--;
               }

               this.handleTileInteractions();
            }
         }
      }
   }

   // Tile-under-player interaction (matches Enemy.tick()'s dispatch
   // role but for player-specific hazards/pickups/triggers): hazard
   // tiles 56-61 briefly lock movement, tile 26 is a hazard that costs
   // 4 health and resets the zip state, tile -125 is a checkpoint-like
   // marker (Game.B/D/C flags), and the row above is scanned for
   // infolink (77-96), name-registration (-118..-99), and a special
   // -94/tile-30 trigger -- see Game's own obfuscated fields for the
   // exact meaning of each (not renamed, Game not done yet). Falls
   // through to roomEdgeReached handling (Game.j(1/2/5/6), the room-
   // transition calls -- direction encoding TENTATIVE, matches
   // LevelMap's 4-entry subGridNeighbors shape).
   public final void handleTileInteractions() {
      byte var1 = this.column();
      byte var2 = (byte)((this.y() + Game.K) / tileHeight);
      if (this.swingEndTimer > 0) {
         this.game.e = true;
         Game.A = true;
         if (--this.swingEndTimer <= 0) {
            this.swingPhase = -2;
            this.swingEndTimer = 0;
            Game.A = false;
         }
      }

      short var3 = this.game.levelMap.getTile(var1, var2);
      Game.B = false;
      if (var3 >= 56 && var3 <= 61) {
         this.swingPhase = -1;
         this.swingEndTimer = 10;
      } else if (var3 == 26) {
         super.velY = 3584;
         this.jumpPhase = 0;
         this.game.ab = 1;
         super.animRestart = 1;
         this.setAnimState((byte)2);
         this.game.e(super.posX, this.y() << 8, 30);
         if (!Game.f) {
            super.health = (byte)(super.health - 4);
         }

         this.swingPhase = -2;
         this.swingEndTimer = 0;
         Game.A = false;
         this.game.e = true;
      } else if (var3 == -125) {
         Game.B = true;
         Game.D = false;
         Game.C = -1;
      } else {
         for (byte var4 = (byte)(--var2 + 2); var4 >= var2; var4--) {
            if ((var3 = this.game.levelMap.getTile(var1, var4)) == 99 || var3 == 100) {
               this.game.ah = this.game.X;
               this.game.ad = var1;
               this.game.ae = var2;
               if (var1 < 1) {
                  this.game.af = 0;
               } else {
                  this.game.af = (short)(-tileWidth * (var1 - 1));
               }

               if (var2 < 6) {
                  this.game.ag = 0;
               } else {
                  this.game.ag = (short)(-tileHeight * (var2 - 5));
               }
               break;
            }

            if (var3 >= 77 && var3 <= 96 && this.game.h(var3 - 77)) {
               this.game.cC = true;
               this.game.cI = Game.cz[var3 - 77];
               this.game.r(Game.cA[var3 - 77]);
               this.game.g(var3 - 77);
               if (Game.cA[var3 - 77] == 112 && (this.game.bw & 2) == 0) {
                  this.game.dC = 248;
                  Game.dy = 16;
                  return;
               }

               this.game.dC = 233 + (var3 - 77);
               Game.dy = (byte)(var3 - 77);
               return;
            }

            if (var3 >= -118 && var3 <= -99 && this.game.h(var3 - -118 + 20)) {
               if (var3 - -118 == 18 && (this.ownedWeapons & 32) == 0) {
                  return;
               }

               if (var3 - -118 != 19 || !this.game.h(4) && !this.game.h(5) && !this.game.h(6) && !this.game.h(7) && !this.game.h(9) && !this.game.h(10)) {
                  this.game.cC = true;
                  this.game.cI = Game.cz[var3 - -118 + 20];
                  this.game.r(Game.cA[var3 - -118 + 20]);
                  this.game.g(var3 - -118 + 20);
                  return;
               }

               return;
            }

            if (var3 == -94 && !this.game.h(11) && this.game.h(30)) {
               this.game.cC = true;
               this.game.cI = Game.cz[30];
               this.game.r(Game.cA[30]);
               this.game.g(30);
               return;
            }
         }
      }

      if (super.row >= 17 && this.game.cT) {
         this.game.cS = true;
         this.setAnimState((byte)10);
         super.animRestart = 1;
      } else {
         if (this.roomEdgeReached) {
            this.roomEdgeReached = false;
            if (this.columnRight() == 0 && !super.facingRight) {
               this.game.j(2);
               return;
            }

            if (this.columnLeft() >= 27 && super.facingRight) {
               this.game.j(5);
               return;
            }

            if (super.row >= 17 && super.velY <= 0) {
               this.game.j(6);
               return;
            }

            if (super.row == 0 && super.posInRow < 1280) {
               this.game.j(1);
            }
         }
      }
   }

   public final void updateAnimation() {
      if (super.animRestart != 2) {
         if (super.animState == 14) {
            super.velY = 0;
         }

         super.animCounter++;
         if (super.animCounter > ANIM_FRAME_EXTRA[super.kind][super.animState]) {
            super.animCounter = 0;
            super.animFrame++;
            if (super.animFrame >= ANIM_FRAME_COUNTS[super.kind][super.animState]) {
               if (super.animRestart == 0) {
                  super.animFrame = 0;
               } else {
                  super.animFrame--;
                  super.animRestart = 2;
                  this.resetAttackAnim();
               }
            }

            this.game.d = true;
         }
      }
   }

   // var2/var3/var4: TENTATIVE (renderVariant/cameraX/cameraY -- var3/
   // var4 written straight into Game.x/Game.y, same source-faithful
   // camera-reapply pattern as Enemy.render()). Draws the zip-line rope
   // segments (when swingPhase >= 0), then the body sprite
   // (FRAME_GEOMETRY-indexed), then a held-weapon overlay sprite when
   // currentWeapon != 0.
   public final void render(Graphics var1, int var2, int var3, int var4) {
      Game.x = (short)var3;
      Game.y = (short)var4;
      int var5 = (super.posX >> 8) - (Game.J >> 1) + Game.x;
      int var6 = super.row * tileHeight + (super.posInRow >> 8) + Game.y + Game.L;
      if (super.animState == 6) {
         var6 += tileHeight >> 1;
      }

      if (this.swingPhase >= 0) {
         int var11 = 26 * tileWidth / 44;
         if (!super.facingRight) {
            var11 = tileWidth - var11;
         }

         int var12 = 10 * tileHeight / 44;
         int var13 = var5 + var11 - 9 << 8;
         int var14 = var6 + var12 - 9 << 8;
         int var15 = this.swingCurX + (Game.x << 8);
         int var16 = this.swingCurY + (Game.y << 8);
         if (this.swingPhase == 0) {
            var14 += 2304;
         }

         if (super.facingRight) {
            var15 -= 1216;
         } else {
            var15 -= 2432;
         }

         int var17 = (var15 - var13) / 19;
         int var18 = (var16 - var14) / 19;

         for (int var32 = 0; var32 < 20; var32++) {
            int var20 = var13 >> 8;
            int var21 = var14 >> 8;
            if (var20 < 176 && var21 < 220 && var20 + 19 >= 0 && var21 + 19 >= 0) {
               this.game.b(var1, var20, var21, 19, 19);
               var1.drawImage(Game.aL, var20, var21 - 114 - 0, 0);
            }

            var13 += var17;
            var14 += var18;
         }

         int var33 = var13 - var17 >> 8;
         int var25 = (var14 - var18 >> 8) - 9;
         if (var33 < 176 && var25 < 220 && var33 + 19 >= 0 && var25 + 19 >= 0) {
            this.game.b(var1, var33, var25, 19, 19);
            if (super.facingRight) {
               var1.drawImage(Game.aL, var33, var25 - 95 - 0, 20);
            } else {
               CanvasShell.directGraphics.drawImage(Game.aL, var33, var25 - 95 - 0, 20, 8192);
            }
         }
      }

      if (var6 + Game.K > hudHeight && var6 < 220 && this.invulnTimer % 2 == 0) {
         byte var26 = ANIM_FRAMES[super.kind][super.animState][super.animFrame];
         byte[] var28 = FRAME_GEOMETRY[var26];
         if (var5 + (var28[4] & 255) < 176
            && var6 - 0 + (var28[5] & 255) < 220
            && var5 + (var28[4] & 255) + (var28[2] & 255) >= 0
            && var6 - 0 + (var28[5] & 255) + (var28[3] & 255) >= 0) {
            this.game.b(var1, var5 + (var28[4] & 255), var6 + (var28[5] & 255), var28[2] & 255, (var28[3] & 255) - 0);
            if (super.ap == 0 && super.facingRight) {
               var1.drawImage(Game.aH, var5 + (var28[4] & 255) - (var28[0] & 255), var6 - 0 + (var28[5] & 255) - (var28[1] & 255), 20);
            } else {
               this.game
                  .a(var1, var5 + (var28[4] & 255) - (Game.aH.getWidth() - (var28[0] & 255) - (var28[2] & 255)), var6 + (var28[5] & 255) - (var28[1] & 255), 0);
            }
         }
      }

      if (this.currentWeapon != 0) {
         byte var27 = MUZZLE_X_BY_FRAME[ANIM_FRAMES[super.kind][super.animState][super.animFrame]];
         int var29 = MUZZLE_Y_BY_FRAME[ANIM_FRAMES[super.kind][super.animState][super.animFrame]];
         if (super.animState == 6) {
            var29 += tileHeight >> 1;
         }

         if (var27 != 44 && var29 != 44) {
            int var24 = Game.aI.getWidth();
            if (super.facingRight) {
               var5 = (super.posX >> 8) + Game.x + var27;
            } else {
               var5 = (super.posX >> 8) + Game.x - var27 - var24;
            }

            var6 = super.row * tileHeight + (super.posInRow >> 8) + Game.y + Game.L + var29;
            int var30 = this.currentWeapon - 1;
            this.game.b(var1, var5, var6, var24, 25);
            if (super.facingRight) {
               var1.drawImage(Game.aI, var5, var6 - var30 * 25 - 0, 20);
            } else {
               CanvasShell.directGraphics.drawImage(Game.aI, var5, var6 - var30 * 25 - 0, 20, 8192);
            }
         }
      }
   }

   // Hyper-shot/swing target search: scans 3 columns ahead (facing-
   // dependent) x 4 rows for tiles 97/98, triggering Game's own
   // (obfuscated) handler on a hit. Confirms swingTargetType's role
   // (its two SWING_* table entries correspond to these two tile ids).
   public final void hyperShotSearch() {
      if (super.facingRight) {
         int var1 = this.columnRight();
         if ((super.posX >> 8) % tileWidth > tileWidth >> 1) {
            var1++;
         }

         for (int var2 = var1; var2 < var1 + 3; var2++) {
            for (int var3 = super.row; var3 >= super.row - 3; var3--) {
               short var4;
               if (var2 >= 0 && var3 >= 0 && ((var4 = this.game.levelMap.getTile(var2, var3)) == 97 || var4 == 98)) {
                  this.game.a(var2, var3, var4);
               }
            }
         }
      } else {
         int var5 = this.columnLeft();
         if ((super.posX >> 8) % tileWidth < tileWidth >> 1) {
            var5--;
         }

         for (int var6 = var5; var6 > var5 - 3; var6--) {
            for (int var7 = super.row; var7 >= super.row - 3; var7--) {
               short var8;
               if (var6 >= 0 && var7 >= 0 && ((var8 = this.game.levelMap.getTile(var6, var7)) == 97 || var8 == 98)) {
                  this.game.a(var6, var7, var8);
               }
            }
         }
      }
   }

   // Fire: checks ammo[currentWeapon] > 0, plays a fire sound only
   // while actually in-game (ratchetandclank.gameStarted, confirmed via
   // `this.game.midlet.i` = Game.midlet (the midlet instance) .gameStarted, and
   // `.c(2)` = ratchetandclank.playSoundIfEnabled(2)), then spawns a
   // projectile per weapon slot via Game.e(x,y,type) -- the same method
   // Enemy.rangedAttack() uses. NOTE: this means Game.e(int,int,int) is
   // NOT enemy-shot-specific as Projectile.java's header speculated from
   // its h.java:5500 call site alone -- it's evidently a shared spawn
   // helper called by both Player and Enemy. Projectile.java's header
   // comment should be corrected once Game's own phase-1 clarifies which
   // pool(s) it actually writes to.
   public final void fire() {
      byte var1 = this.weaponLevel[this.currentWeapon];
      if (super.animState != 6 && super.animState != 7 && super.animState != 12 && super.animState != 13) {
         this.setAnimState((byte)7);
         super.animRestart = 0;
      }

      if (this.ammo[this.currentWeapon] > 0) {
         if (this.game.midlet.gameStarted) {
            this.game.midlet.playSoundIfEnabled(2);
         }

         switch (this.currentWeapon) {
            case 0:
               return;
            case 1:
               this.game.e(super.posX + (super.facingRight ? 4608 : -4608), (this.y() << 8) + 2048, 0 + var1);
               return;
            case 2:
               this.game.e(super.posX + (super.facingRight ? 4608 : -4608), (this.y() << 8) + 2048, 3 + var1);
               return;
            case 3:
               this.game.e(super.posX + (super.facingRight ? 4608 : -4608), (this.y() << 8) + 2048, 6 + var1);
               return;
            case 4:
               this.game.e(super.posX + (super.facingRight ? 4608 : -4608), (this.y() << 8) + 2048, 9 + var1);
               if (this.ammo[this.currentWeapon] > 0 && var1 == 2) {
                  this.game.e(super.posX + (super.facingRight ? 4608 : -4608), (this.y() << 8) + 2048, 9 + var1);
                  return;
               }
               break;
            case 5:
               this.game.e(super.posX + (super.facingRight ? 4608 : -4608), (this.y() << 8) + 2048 + this.ammo[this.currentWeapon] % 2 * 1024, 12 + var1);
               return;
            case 6:
               this.game.e(super.posX + (super.facingRight ? 4608 : -4608), (this.y() << 8) + 2048, 15 + var1);
               return;
            case 7:
               this.game.e(super.posX + (super.facingRight ? 4608 : -4608), (this.y() << 8) + 2048, 18 + var1);
         }
      }
   }

   // Melee/swing hit: tests the swing hitbox (SWING_*[swingTargetType])
   // against moving platforms (triggering an infolink-style event on a
   // specific platform kind) and every enemy in range, applying
   // MELEE_DAMAGE_BY_LEVEL[weaponLevel[0 or 1]] and either killing
   // (health <= 0 -> clear the enemy's spawn bit, drop
   // Enemy.BOLTS_DROPPED_BY_ANIM[animKind] bolts) or just flinching it.
   public final void meleeHit() {
      if (super.animState < 4 || super.animState == 11) {
         this.game.m(-1);
         if (super.animState != 11) {
            super.animRestart = 1;
            this.setAnimState((byte)8);
         }

         byte var3 = 0;
         byte var4 = 0;

         for (int var6 = 2; var6 >= 0; var6--) {
            if (this.game.bF[var6] != -1) {
               int var1 = this.game.bF[var6] * tileWidth + (tileWidth - 19 >> 1);
               int var2 = this.game.bG[var6] * tileHeight;
               if (this.abs(var1 - this.x()) <= 3 * tileWidth >> 1
                  && this.abs(var2 - this.y()) <= tileHeight
                  && this.game
                     .a(var1, var2, 19, 19, (super.posX >> 8) + (super.facingRight ? SWING_X_OFFSET[this.swingTargetType] : -SWING_X_OFFSET[this.swingTargetType] - SWING_WIDTH[this.swingTargetType]), this.y() + SWING_Y_OFFSET[this.swingTargetType], SWING_WIDTH[this.swingTargetType], SWING_HEIGHT[this.swingTargetType])) {
                  int var7 = this.game.levelMap.getTile(this.game.bF[var6], this.game.bG[var6]);
                  var7 -= 36;
                  if (var7 == 2 && (this.game.bw & 1 << var7) > 0) {
                     this.game.cC = true;
                     this.game.di = 32;
                     this.game.cL |= 32;
                     this.game.cI = Game.cz[33];
                     this.game.r(Game.cA[33]);
                     this.game.dC = 256;
                     Game.dy = 15;
                  }

                  this.game.bw = (byte)(this.game.bw & ~(1 << var7));
                  if (var7 == 0) {
                     this.game.e(this.game.Z, this.game.X);
                  }
               }
            }
         }

         for (int var15 = Game.enemyPoolSize - 1; var15 >= 0; var15--) {
            byte var5 = this.game.enemies[var15].kind;
            if (this.game.enemies[var15].kind != -1 && this.game.enemies[var15].animState != 5) {
               int var11 = this.game.enemies[var15].x() - Enemy.HITBOX_X_OFFSETS[var5];
               int var12 = this.game.enemies[var15].y() + Enemy.HITBOX_Y_OFFSETS[var5];
               var3 = Enemy.HITBOX_WIDTHS[var5];
               var4 = Enemy.HITBOX_HEIGHTS[var5];
               if (this.abs(var11 - this.x()) <= 3 * tileWidth >> 1
                  && this.abs(var12 - this.y()) <= tileHeight
                  && this.game
                     .a(var11, var12, var3, var4, (super.posX >> 8) + (super.facingRight ? SWING_X_OFFSET[this.swingTargetType] : -SWING_X_OFFSET[this.swingTargetType] - SWING_WIDTH[this.swingTargetType]), this.y() + SWING_Y_OFFSET[this.swingTargetType], SWING_WIDTH[this.swingTargetType], SWING_HEIGHT[this.swingTargetType])
                  && (!this.game.enemies[var15].flattenedRenderFlag || super.animState != 11)) {
                  if (super.animState == 11) {
                     this.game.enemies[var15].flattenedRenderFlag = true;
                  }

                  if (super.animState == 11) {
                     this.game.enemies[var15].health = (byte)(this.game.enemies[var15].health - MELEE_DAMAGE_BY_LEVEL[this.weaponLevel[1]]);
                  } else {
                     this.game.enemies[var15].health = (byte)(this.game.enemies[var15].health - MELEE_DAMAGE_BY_LEVEL[this.weaponLevel[0]]);
                  }

                  if (this.game.enemies[var15].animState != 2) {
                     if (this.game.enemies[var15].posX > super.posX) {
                        this.game.enemies[var15].bounceTimer = 10;
                     } else {
                        this.game.enemies[var15].bounceTimer = -10;
                     }
                  }

                  if (this.game.enemies[var15].health <= 0) {
                     short var8 = this.game.X;
                     if (this.game.cV) {
                        var8 = 0;
                     }

                     int var9 = var15 + var8 * 10 >> 5;
                     this.game.bv[var9] = this.game.bv[var9] & ~(1 << var15 + var8 * 10 - (var9 << 5));
                     this.game.enemies[var15].setAnimState((byte)5);
                     this.game.enemies[var15].animRestart = 1;
                     if (var5 != 4) {
                        Game.do_++;
                        if (this.game.aa && ++this.game.ab > 10) {
                           this.game.ab = 10;
                        }

                        if (this.game.Z != 0) {
                           for (int var10 = 0; var10 < Enemy.BOLTS_DROPPED_BY_ANIM[this.game.enemies[var15].animKind]; var10++) {
                              this.game.c(this.game.enemies[var15].x(), this.game.enemies[var15].y(), 0);
                           }
                        }
                     }
                  } else if (this.game.enemies[var15].animState != 2) {
                     this.game.enemies[var15].setAnimState((byte)4);
                     this.game.enemies[var15].animHold = 0;
                  }
               }
            }
         }

         if (this.jumpPhase == -1) {
            this.game.cv = 0;
         }
      }
   }

   public final void resetAttackAnim() {
      this.hyperCastTimer = 0;
      if (super.animState == 7 || super.animState == 8 || super.animState == 9 || super.animState == 0 || super.animState == 4) {
         super.animRestart = 0;
         this.setAnimState((byte)0);
      } else if (super.animState == 13) {
         super.animRestart = 0;
         this.setAnimState((byte)12);
      } else {
         if (super.animState == 14) {
            super.animRestart = 0;
            this.setAnimState((byte)11);
         }
      }
   }

   public final void updateHyperCastTimer() {
      if (++this.hyperCastTimer > 15) {
         this.hyperCastTimer = -1;
         this.resetAttackAnim();
      }
   }

   // Pickup-magnet attraction: scans Game's 12-entry pickup arrays
   // (ck/cg/ci -- type/x/velocity, TENTATIVE reading) for ones in range
   // and steers them toward the player, applying the pickup's effect on
   // arrival: type <= 1 adds to Game.aX/Game.dq (score/bolts, TENTATIVE
   // -- matches the legacy build's boltCount-style field), type == 2
   // heals 4 health (capped at 20), anything else refills ammo via
   // refillAmmo(1).
   public final void updatePickupMagnet() {
      int var1 = this.x() + 7 + 37;

      for (int var2 = 11; var2 >= 0; var2--) {
         byte var3 = this.game.ck[var2];
         int var4 = this.game.cg[var2];
         short var5 = this.game.ci[var2];
         int var6 = 0;
         if (var3 != -1 && (super.health != 20 || var3 != 2)) {
            var6 = tileWidth * 5 << 7;
            if (var5 != 0 || this.abs(var4 - super.posX) <= var6 && this.abs((this.game.ch[var2] >> 8) - var1) <= Game.K) {
               if (var4 > super.posX) {
                  var5 = (short)(var5 - 256);
               } else {
                  var5 = (short)(var5 + 256);
               }

               if ((var4 = var4 + var5) >= tileWidth * 27 + (tileWidth >> 1) << 8) {
                  var4 = tileWidth * 27 + (tileWidth >> 1) << 8;
               }

               int var7;
               if ((var7 = this.abs(var4 - super.posX)) <= tileWidth << 6) {
                  this.game.e = true;
                  if (var3 <= 1) {
                     this.game.aX = this.game.aX + 10 * this.game.ab;
                     Game.dq = Game.dq + 10 * this.game.ab;
                     if (this.game.aX > 999999) {
                        this.game.aX = 999999;
                     }
                  } else if (var3 == 2) {
                     super.health = (byte)(super.health + 4);
                     if (super.health > 20) {
                        super.health = 20;
                     }
                  } else {
                     this.refillAmmo(1);
                  }

                  this.game.ck[var2] = -1;
                  return;
               }

               int var8 = this.game.ch[var2] - (this.y() + (tileHeight >> 1) << 8);
               if (var7 != 0) {
                  this.game.ch[var2] = this.game.ch[var2] + var8 * var5 * (var5 >= 0 ? -1 : 1) / var7;
               }

               this.game.cg[var2] = var4;
               this.game.ci[var2] = var5;
            }
         }
      }
   }

   // Distributes `var1` units of ammo to the lowest-ammo owned weapon
   // (excluding weapon 6) that isn't already at capacity, scaled by
   // AMMO_PER_PICKUP[weapon].
   private void refillAmmo(int var1) {
      int var2 = 1;
      short var3 = 9999;

      for (int var4 = 1; var4 < 8; var4++) {
         if (var4 != 6 && (this.ownedWeapons & 1 << var4) > 0 && this.ammo[var4] < var3 && this.ammo[var4] != AMMO_CAPACITY[var4 * 3 + this.weaponLevel[var4]]) {
            var3 = this.ammo[var4];
            var2 = var4;
         }
      }

      this.ammo[var2] = (short)(this.ammo[var2] + var1 * AMMO_PER_PICKUP[var2]);
      if (this.ammo[var2] > AMMO_CAPACITY[var2 * 3 + this.weaponLevel[var2]]) {
         this.ammo[var2] = AMMO_CAPACITY[var2 * 3 + this.weaponLevel[var2]];
      }
   }

   // Loads `/player.bin`: 15 anim slots, each a (frameCount, extraTicks)
   // byte pair into ANIM_FRAME_COUNTS[0]/ANIM_FRAME_EXTRA[0], then
   // `frameCount` raw frame-index bytes into ANIM_FRAMES[0][slot].
   private void loadAnimFile(String var1) {
      byte[] var4 = new byte[4];

      try {
         InputStream var5 = var4.getClass().getResourceAsStream(var1);

         for (int var6 = 0; var6 < 15; var6++) {
            byte var2 = (byte)var5.read();
            byte var3 = (byte)var5.read();
            ANIM_FRAME_COUNTS[0][var6] = var2;
            ANIM_FRAME_EXTRA[0][var6] = var3;
            this.readBytes(var5, var4, var2);
            System.arraycopy(var4, 0, ANIM_FRAMES[0][var6], 0, var2);
         }

         var5.close();
      } catch (IOException var7) {
      }
   }

   private void readBytes(InputStream var1, byte[] var2, int var3) {
      try {
         for (int var4 = 0; var4 < var3; var4++) {
            var2[var4] = (byte)var1.read();
         }
      } catch (IOException var5) {
      }
   }

   public final int abs(int var1) {
      return var1 < 0 ? var1 * -1 : var1;
   }
}
