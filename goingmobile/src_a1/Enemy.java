// Hand-written, faithfully renamed from decompiled_a1/f.java (class f).
// a1's equivalent of the legacy build's Enemy (decompiled/d.java, see
// ../src/Enemy.java) -- same 5-slot pool / 5-type shape (Game.ai[],
// confirmed via Entity.java/Projectile.java cross-checks), same
// hitbox-table split confirmed against BUILD_COMPARISON.md's note that
// canonical `/q` is byte-identical to a1's `enemy_spr_box.bin`: the
// x/y/w/h hitbox offsets (HITBOX_*) and a second w/h "attack" geometry
// group (ATTACK_*) both load from `/enemy_spr_box.bin` (loadAssets()'s
// `a(String)`, renamed loadHitboxTables()); animation frame tables
// (ANIM_FRAMES/ANIM_FRAME_COUNTS/ANIM_FRAME_EXTRA) load from
// `/enemy.bin` (loadAssets()'s `b(String)`, renamed loadAnimTables()) --
// same three-table shape as the legacy build's `p`/`q`/`r` from `/p`,
// just `[5][8][5]` here vs. the legacy build's `[5][6][5]` (8 anim slots
// per type instead of 6).
//
// HP_BY_ANIM/DAMAGE_BY_ANIM/BOLTS_DROPPED_BY_ANIM/ATTACK_WINDUP_TICKS
// are all confirmed by cross-file grep, not just this file: `Game`
// (`h.java:5615,5804`) sets a freshly-spawned enemy's health straight
// from HP_BY_ANIM; `Enemy.meleeAttack()` (`k()`, below) subtracts
// DAMAGE_BY_ANIM straight from the player's health; `Player`
// (`b.java:1154`) and `Game` (`h.java:5798,5832`) both loop
// `BOLTS_DROPPED_BY_ANIM[kind]` times spawning bolt pickups on death.
// All four are indexed by `animKind` (this file's `v`), not by `kind`
// (Entity's `Z`) -- same "by anim, not by type" convention as the
// legacy build's identically-named tables.
//
// Everything under "TENTATIVE" is named from call shape, not confirmed
// against Game's own phase-1 (not done yet) or independently verified.
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;

public final class Enemy extends Entity {
   public static byte[] HITBOX_X_OFFSETS;
   public static byte[] HITBOX_Y_OFFSETS;
   public static byte[] HITBOX_WIDTHS;
   public static byte[] HITBOX_HEIGHTS;
   public static byte[] ATTACK_X_OFFSETS;
   public static byte[] ATTACK_Y_OFFSETS;
   public static byte[] ATTACK_WIDTHS;
   public static byte[] ATTACK_HEIGHTS;
   public static final byte[] HP_BY_ANIM = new byte[]{6, 10, 16, 5, 8, 10, 5, 5, 5, 15, 15, 15, 22, 22, 22, 5, 5, 5, 5, 5, 5, 5, 5, 5, 3, 0, 0};
   public static final byte[] DAMAGE_BY_ANIM = new byte[]{1, 3, 4, 4, 5, 6, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 4, 2, 3, 4, 2, 3, 4, 0, 2, 4};
   public static final byte[] BOLTS_DROPPED_BY_ANIM = new byte[]{1, 2, 3, 1, 2, 3, 1, 1, 1, 2, 2, 2, 3, 3, 3, 1, 1, 1, 2, 2, 2, 3, 3, 3, 0};
   public static final byte[] ATTACK_WINDUP_TICKS = new byte[]{0, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1};
   public static final short[] SPEED_BY_TYPE = new short[]{1024, 512, 768, 0, 512};
   public static final byte[] ATTACK_COOLDOWN_BY_TYPE = new byte[]{0, 20, 5, 5, 0};
   public static byte[][][] ANIM_FRAMES;
   public static byte[][] ANIM_FRAME_COUNTS;
   public static byte[][] ANIM_FRAME_EXTRA;
   public static byte tileWidth;
   public static byte tileHeight;
   public static short hudHeight;
   // TENTATIVE -- toggled in the wall-crawler (kind 3) direction-reversal
   // logic and read in render() to pick an alternate sprite row offset;
   // likely "is this wall-crawler upside down / on the ceiling".
   public boolean wallCrawlFlipped;
   // The anim-set index (0-26ish) indexing HP_BY_ANIM/DAMAGE_BY_ANIM/
   // BOLTS_DROPPED_BY_ANIM/ATTACK_WINDUP_TICKS -- matches the legacy
   // build's Enemy.animKind (there: `t`) exactly in role.
   public byte animKind;
   private Game game;
   // Knockback/bounce state: nonzero while the enemy is being pushed
   // back (e.g. bounced off by the player), decrementing toward 0 each
   // tick and driving both velX and facing -- matches the legacy
   // build's Enemy.bounceTimer (there: `u`) in role.
   public byte bounceTimer;
   public boolean wallAhead;       // TENTATIVE: set in updateWalk() when blocked by a wall/invalid tile ahead
   public boolean edgeAhead;       // TENTATIVE: set in updateWalk() when the ground ahead drops away
   public boolean invalidTileAhead; // TENTATIVE: set in updateWalk() when the tile ahead is off-level/unwalkable
   public short tileAhead;         // the tile id sampled ahead of the enemy (groundYAhead()'s optional sample)
   public short attackCooldown;
   public byte attackPhase;        // TENTATIVE: 0 = approaching/idle, 1 = winding up an attack (counts stateTimer up to ATTACK_WINDUP_TICKS[animKind])
   public byte spawnBitIndex;      // TENTATIVE: indexes a packed per-level "cleared" bitset (Game.bv[]) cleared on melee kill
   public short stateTimer;        // multi-purpose: post-turn cooldown, and (with attackPhase) attack windup counter
   public int patrolTargetX;       // TENTATIVE: only used by kind 2 (flying?) -- last-seen player x or a patrol target
   // TENTATIVE -- mirrors the legacy build's Enemy note ("s kept: flag
   // that switches the renderer to the flattened variant"); set false
   // at the top of tick()'s main per-tick branch, not otherwise read in
   // this file (must be read by Game's own render loop).
   public boolean flattenedRenderFlag;

   public Enemy(Game var1) {
      this.game = var1;
      tileWidth = Game.F;
      tileHeight = Game.G;
      hudHeight = Game.H;
      super.kind = -1;
      super.ap = 0;
      this.stateTimer = 0;
      super.facingRight = true;
      this.wallCrawlFlipped = false;
      this.wallAhead = false;
      this.edgeAhead = false;
      this.invalidTileAhead = false;
      this.bounceTimer = 0;
   }

   public final void loadAssets() {
      if (ANIM_FRAMES == null) {
         ANIM_FRAMES = new byte[5][8][5];
      }

      if (ANIM_FRAME_COUNTS == null) {
         ANIM_FRAME_COUNTS = new byte[5][8];
      }

      if (ANIM_FRAME_EXTRA == null) {
         ANIM_FRAME_EXTRA = new byte[5][8];
      }

      if (HITBOX_X_OFFSETS == null) {
         HITBOX_X_OFFSETS = new byte[5];
      }

      if (HITBOX_Y_OFFSETS == null) {
         HITBOX_Y_OFFSETS = new byte[5];
      }

      if (HITBOX_WIDTHS == null) {
         HITBOX_WIDTHS = new byte[5];
      }

      if (HITBOX_HEIGHTS == null) {
         HITBOX_HEIGHTS = new byte[5];
      }

      if (ATTACK_X_OFFSETS == null) {
         ATTACK_X_OFFSETS = new byte[5];
      }

      if (ATTACK_Y_OFFSETS == null) {
         ATTACK_Y_OFFSETS = new byte[5];
      }

      if (ATTACK_WIDTHS == null) {
         ATTACK_WIDTHS = new byte[5];
      }

      if (ATTACK_HEIGHTS == null) {
         ATTACK_HEIGHTS = new byte[5];
      }

      this.loadAnimTables("/enemy.bin");
      this.loadHitboxTables("/enemy_spr_box.bin");
   }

   // Pixel y (bottom-ish, per the legacy build's Entity.row/posInRow
   // convention: row*tileHeight + posInRow_px, offset by +tileHeight
   // and -hudHeight).
   public final short y() {
      return (short)(super.row * tileHeight + (super.posInRow >> 8) + tileHeight - hudHeight);
   }

   public final short x() {
      return (short)(super.posX >> 8);
   }

   public final byte columnRight() {
      return (byte)(((super.posX >> 8) + 12) / tileWidth);
   }

   public final byte columnLeft() {
      return (byte)(((super.posX >> 8) - 12) / tileWidth);
   }

   // Scans downward from the current row for the next solid column
   // (LevelMap.columnSolidMasks, via Game's still-obfuscated LevelMap
   // field) to find the ground y-pixel ahead of the enemy, then checks
   // every moving-platform slot (Game.bn/.bl/.bm, obfuscated) for one
   // that would put a closer platform top under that same x range.
   // `var1` also opportunistically samples the tile ahead into
   // tileAhead when true.
   public final int groundYAhead(boolean var1) {
      int var2 = (super.posX >> 8) / tileWidth;
      int var5 = Game.aJ.getWidth();
      if (var1) {
         this.tileAhead = this.game.V.getTile(var2, super.row);
      }

      int var3;
      for (var3 = super.row + 1; var3 < 18; var3++) {
         if ((LevelMap.columnSolidMasks[var2] & 1 << var3) > 0) {
            var3 *= tileHeight;
            break;
         }
      }

      if (var3 >= 18) {
         var3 = 18 * tileHeight;
      }

      var2 = super.posX >> 8;

      for (int var7 = 0; var7 < 50; var7++) {
         if (this.game.bn[var7] != -1
            && this.game.bl[var7] <= var2 + 8
            && this.game.bl[var7] + var5 >= var2 - 8
            && this.game.bm[var7] < var3
            && this.game.bm[var7] > super.row * tileHeight) {
            var3 = this.game.bm[var7];
         }
      }

      return var3;
   }

   // Whether the melee-attack hitbox in front of the enemy overlaps any
   // active moving platform (used to suppress an attack that would hit
   // through a platform).
   public final boolean attackBlockedByPlatform() {
      int var1 = Game.aJ.getWidth();
      int var2 = Game.aJ.getHeight() >> 3;
      byte var3 = tileWidth;
      byte var4 = tileHeight;
      int var6 = super.posX >> 8;
      int var7 = this.y() + hudHeight - tileHeight;
      int var9 = var4 - 1;

      for (int var10 = 49; var10 >= 0; var10--) {
         if (this.game.bn[var10] >= 0
            && this.abs(this.game.bl[var10] - var6) <= var3
            && this.game.bm[var10] == var7
            && this.game.a(this.game.bl[var10], this.game.bm[var10], var1, var2, var6 - 8, var7, 16, var9)) {
            return true;
         }
      }

      return false;
   }

   // Per-tick walk physics for the ground-walking types: advance
   // posInRow (via velY-as-fall-speed, reused here as a downward
   // step toward groundYAhead()), snap to the ground when reached,
   // then check for a bounce-back collision with the player's hitbox
   // (HITBOX_*[kind]) -- on a solid hit, either stop dead (facing the
   // player) or reverse into a bounce-back arc, matching the legacy
   // build's ground-enemy knockback-on-contact behavior. Also computes
   // wallAhead/edgeAhead/invalidTileAhead for the next tick's facing
   // decision in tick().
   public final void updateWalk() {
      int var1 = this.groundYAhead(true) / tileHeight;
      this.invalidTileAhead = false;
      this.wallAhead = false;
      this.edgeAhead = false;
      if (super.row + 1 < var1 && super.kind != 3 || super.animState == 2) {
         this.setAnimState((byte)2);
         super.velY = (short)(super.velY + 256);
         super.posInRow = super.posInRow + super.velY;
         this.wrapRow();
         if (super.row + 1 == var1 && super.velY > 0) {
            this.setAnimState((byte)0);
            super.velY = 0;
            super.posInRow = 0;
         }

         if (this.game.a(this.x() - HITBOX_X_OFFSETS[super.kind], this.y() + HITBOX_Y_OFFSETS[super.kind], HITBOX_WIDTHS[super.kind], HITBOX_HEIGHTS[super.kind], this.game.aj.b() - 11, this.game.aj.c() + Game.L + 7, 18, 37)) {
            super.velX = 0;
            if (super.velY > 0) {
               super.velY = (short)(-super.velY / 2);
               if (this.game.aj.b() < this.x()) {
                  super.velX = 510;
                  super.facingRight = false;
               } else {
                  super.velX = -510;
                  super.facingRight = true;
               }
            } else {
               super.velY = (short)(-super.velY);
            }
         }
      }

      if (super.velX != 0) {
         super.ae = super.facingRight ? this.columnRight() : this.columnLeft();
         super.af = super.row;
         int var2 = super.posX;
         super.posX = super.posX + (super.facingRight ? tileWidth << 7 : -(tileWidth << 7));
         int var3 = this.groundYAhead(false) / tileHeight;
         this.edgeAhead = false;
         if (var3 > var1) {
            this.edgeAhead = true;
         }

         if (this.bounceTimer != 0 && this.edgeAhead) {
            super.velX = 0;
         }

         super.posX = var2;
         if (super.ae > 0 && super.ae < 27 && this.game.V.isWalkable(super.ae, super.af)) {
            super.posX = super.posX + super.velX;
            if (this.attackBlockedByPlatform()) {
               super.posX = super.posX - super.velX;
               if (super.animState != 2) {
                  this.wallAhead = true;
                  return;
               }
            }
         } else {
            this.invalidTileAhead = true;
         }
      }
   }

   // Master per-tick dispatcher: handles death-anim cleanup, culls AI
   // for enemies far off-screen (with a tighter box for wall-crawlers,
   // kind 3), then branches by bounceTimer (knockback override) or by
   // kind for the real walk/fly/wall-crawl/boss AI. See inline notes at
   // each kind branch -- kind 0 ground-walker, kind 1 (TENTATIVE, some
   // ranged/turning variant), kind 2 (TENTATIVE, patrol/flier using
   // patrolTargetX), kind 3 wall-crawler, kind 4 (TENTATIVE, boss-part-like).
   public final void tick() {
      if (super.animState == 5) {
         if (super.animRestart == 2) {
            super.kind = -1;
         }
      } else if (super.animState != 7 && super.animState != 6) {
         if (this.abs(this.game.aj.b() - this.x()) <= 176 && this.abs(this.game.aj.c() - this.y()) <= 220) {
            if (super.kind != 3 || this.abs(this.game.aj.b() - this.x()) <= 3 * tileWidth + (tileWidth >> 1) && this.abs(this.game.aj.c() - this.y()) <= 5 * tileHeight) {
               this.updateWalk();
               if (this.bounceTimer != 0 && super.kind != 3) {
                  super.velX = (short)(this.bounceTimer << 8);
                  super.velY = 0;
                  this.setAnimState((byte)4);
                  if (this.bounceTimer > 0) {
                     this.bounceTimer--;
                     super.facingRight = true;
                  } else {
                     this.bounceTimer++;
                     super.facingRight = false;
                  }

                  if (this.invalidTileAhead) {
                     super.velX = 0;
                  }
               } else {
                  this.flattenedRenderFlag = false;
                  int var1 = this.game.aj.b() - this.x();
                  int var2 = this.game.aj.c() + tileHeight - hudHeight - this.y();
                  byte var3 = tileWidth;
                  int var4 = tileWidth * 3;
                  if (super.kind == 0) {
                     if (this.wallAhead) {
                        super.facingRight = !super.facingRight;
                        this.stateTimer = 10;
                     } else if (this.invalidTileAhead) {
                        if (this.tileAhead == -96) {
                           super.velY = -510;
                           this.setAnimState((byte)2);
                           return;
                        }

                        super.facingRight = !super.facingRight;
                        this.stateTimer = 10;
                     } else if (this.edgeAhead) {
                        if (this.tileAhead == -97) {
                           super.velY = -768;
                           super.velX = (short)(SPEED_BY_TYPE[super.kind] << 2);
                           if (!super.facingRight) {
                              super.velX = (short)(super.velX * -1);
                           }

                           this.setAnimState((byte)2);
                           return;
                        }

                        if (this.tileAhead != -96) {
                           super.facingRight = !super.facingRight;
                           this.stateTimer = 10;
                        }
                     }

                     if (super.animState == 2) {
                        return;
                     }

                     if (var1 < var4 && var1 > var3 && this.abs(var2) < tileHeight && this.stateTimer == 0) {
                        if (super.animState != 1) {
                           this.setAnimState((byte)1);
                        }

                        super.velX = SPEED_BY_TYPE[super.kind];
                        super.facingRight = true;
                     } else if (var1 < -var3 && var1 > -var4 && this.abs(var2) < tileHeight && this.stateTimer == 0) {
                        if (super.animState != 1) {
                           this.setAnimState((byte)1);
                        }

                        super.velX = (short)(-SPEED_BY_TYPE[super.kind]);
                        super.facingRight = false;
                     } else if (this.abs(var1) <= var3 && var2 == 0) {
                        super.velX = 0;
                        this.game.a(super.posX, this.y() + (tileHeight >> 1) << 8, 21, super.facingRight, false, 0, this.animKind);
                        super.kind = -1;
                        short var7 = this.game.X;
                        if (this.game.cV) {
                           var7 = 0;
                        }

                        int var6 = this.spawnBitIndex + var7 * 10 >> 5;
                        this.game.bv[var6] = this.game.bv[var6] & ~(1 << this.spawnBitIndex + var7 * 10 - (var6 << 5));
                     } else {
                        this.startWalk();
                     }

                     if (this.stateTimer != 0) {
                        this.stateTimer--;
                        return;
                     }
                  } else {
                     // TENTATIVE -- kind 1 branch, role/behavior not
                     // independently confirmed beyond what this code
                     // shows (turn-around on wall/edge/invalid-tile,
                     // else melee-range check via meleeAttack()).
                     if (super.kind == 1 && super.animState != 2) {
                        if (this.tileAhead == -95) {
                           if (var2 < 0 && this.abs(var1) > tileWidth / 2) {
                              super.velY = -3584;
                              super.velX = 0;
                              this.setAnimState((byte)2);
                           }
                        } else if (this.wallAhead) {
                           super.facingRight = !super.facingRight;
                        } else if (this.invalidTileAhead) {
                           if (this.tileAhead == -96 && this.abs(var1) > tileWidth / 2) {
                              super.velY = -1700;
                              this.setAnimState((byte)2);
                              return;
                           }

                           super.facingRight = !super.facingRight;
                        } else if (this.edgeAhead) {
                           if (this.tileAhead == -97 && this.abs(var1) > tileWidth / 2) {
                              super.velY = -3584;
                              super.velX = (short)(SPEED_BY_TYPE[super.kind] << 2);
                              if (!super.facingRight) {
                                 super.velX = (short)(super.velX * -1);
                              }

                              this.setAnimState((byte)2);
                              return;
                           }

                           if (this.tileAhead != -96) {
                              super.facingRight = !super.facingRight;
                           }
                        }

                        if (super.animState == 2) {
                           return;
                        }

                        if (this.abs(var1) <= var3 && this.abs(var2) <= var3 && this.abs(var2) < tileHeight) {
                           super.velX = 0;
                           this.meleeAttack();
                           return;
                        }

                        this.startWalk();
                        return;
                     }

                     // TENTATIVE -- kind 2, uses patrolTargetX
                     // (a stored x position, role/setter not traced
                     // here -- likely set elsewhere in Game).
                     if (super.kind == 2) {
                        if (this.wallAhead) {
                           super.velX = 0;
                           super.animRestart = 1;
                           this.setAnimState((byte)0);
                           super.facingRight = !super.facingRight;
                        } else if (this.invalidTileAhead) {
                           super.velX = 0;
                           super.animRestart = 1;
                           this.setAnimState((byte)0);
                           super.facingRight = !super.facingRight;
                        } else if (this.edgeAhead) {
                           super.velX = 0;
                           super.animRestart = 1;
                           this.setAnimState((byte)0);
                           super.facingRight = !super.facingRight;
                        }

                        int var5 = this.abs(this.patrolTargetX - super.posX) >> 8;
                        if (this.attackPhase != 0) {
                           if (this.attackPhase == 1) {
                              if (this.stateTimer < ATTACK_WINDUP_TICKS[this.animKind]) {
                                 this.rangedAttack();
                                 return;
                              }

                              if (super.animRestart == 2 && super.animState == 0) {
                                 this.attackPhase = 0;
                                 return;
                              }

                              if (super.animState != 0) {
                                 super.animRestart = 1;
                                 this.setAnimState((byte)0);
                                 return;
                              }
                           }
                        } else {
                           if ((var1 > 0 && super.facingRight || var1 < 0 && !super.facingRight) && this.abs(var2) < tileHeight && this.abs(var1) < tileWidth * 3) {
                              super.velX = 0;
                              this.attackPhase = 1;
                              this.stateTimer = 0;
                              this.attackCooldown = 0;
                              return;
                           }

                           if ((this.patrolTargetX > super.posX && !super.facingRight || this.patrolTargetX < super.posX && super.facingRight) && var5 >= tileWidth * 3) {
                              super.velX = 0;
                              super.animRestart = 1;
                              this.setAnimState((byte)0);
                              super.facingRight = !super.facingRight;
                              return;
                           }

                           if (super.animState != 0 || super.animRestart == 2 && super.animState == 0) {
                              this.startWalk();
                              return;
                           }
                        }
                     } else if (super.kind == 3) {
                        if (this.attackPhase == 0) {
                           if (this.stateTimer < ATTACK_WINDUP_TICKS[this.animKind]) {
                              this.rangedAttack();
                           } else {
                              this.stateTimer = 0;
                              this.attackPhase = 1;
                           }
                        }

                        if (this.attackPhase == 1 && this.stateTimer++ > 10) {
                           if (super.facingRight && !this.wallCrawlFlipped) {
                              this.wallCrawlFlipped = true;
                           } else if (super.facingRight) {
                              this.wallCrawlFlipped = super.facingRight = false;
                           } else {
                              super.facingRight = true;
                           }

                           this.attackPhase = 0;
                           this.stateTimer = 0;
                           return;
                        }
                     } else if (super.kind == 4) {
                        if (this.wallAhead) {
                           super.facingRight = !super.facingRight;
                        } else if (this.invalidTileAhead) {
                           super.facingRight = !super.facingRight;
                        } else if (this.edgeAhead) {
                           super.facingRight = !super.facingRight;
                        }

                        this.startWalk();
                     }
                  }
               }
            }
         } else {
            super.velX = super.velY = 0;
         }
      }
   }

   // Advances animFrame/animCounter against ANIM_FRAME_COUNTS/
   // ANIM_FRAMES[kind][animState]; on reaching the frame count, either
   // loops (animRestart == 0) or holds the last frame and marks
   // animRestart == 2 (matching Entity's setAnimState/legacy
   // convention); anim states 6/7 (hurt?) and 3 (attack, kind 1 only --
   // re-triggers a facing flip + 20-tick stateTimer) reset back to
   // idle (0) on completing their cycle.
   public final void updateAnimation() {
      if (super.animRestart != 2) {
         if (super.kind == -1) {
            super.animFrame = super.animCounter = 0;
         } else {
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
                  }

                  if (super.animState == 6 || super.animState == 7) {
                     this.setAnimState((byte)0);
                  }

                  if (super.animState == 3) {
                     this.setAnimState((byte)0);
                     if (super.kind == 1) {
                        super.facingRight = !super.facingRight;
                        this.stateTimer = 20;
                     }
                  }
               }

               this.game.d = true;
            }
         }
      }
   }

   // var2/var3: TENTATIVE (renderVariant/visibilityFlags -- var3's bit
   // 0 gates drawing entirely; var2 selects Game's alternate/flattened
   // draw path, `game.c(...)`, when facingRight is false or ap != 0).
   // var4/var5 are the camera position, written straight into
   // Game.x/Game.y as a side effect of this call (source-faithful, not
   // a rename artifact -- every enemy render call re-applies the same
   // camera position).
   public final void render(Graphics var1, int var2, int var3, int var4, int var5) {
      Game.x = (short)var4;
      Game.y = (short)var5;
      int var6 = this.x() + Game.x - (Game.J >> 1);
      int var7 = this.y() + Game.y;
      if (var6 >= -Game.J && var6 < 176 && var7 >= -Game.K && var7 < 220) {
         if ((var3 & 1) > 0 && var7 + Game.K > hudHeight && var7 < 220) {
            byte var9 = Game.J;
            this.game.b(var1, var6, var7, var9, Game.K - 0);
            if (super.ap == 0 && super.facingRight) {
               int var10 = 0;
               if (this.wallCrawlFlipped) {
                  var10 = Game.K << 1;
               }

               var1.drawImage(Game.aG[super.kind], var6, var7 - ANIM_FRAMES[super.kind][super.animState][super.animFrame] * Game.K - 0 - var10, 20);
               return;
            }

            this.game.c(var1, var2, var6, var7, 0);
         }
      }
   }

   public final void startWalk() {
      if (super.animState != 1) {
         this.setAnimState((byte)1);
      }

      super.animRestart = 0;
      super.velX = (short)(super.facingRight ? SPEED_BY_TYPE[super.kind] : -SPEED_BY_TYPE[super.kind]);
   }

   // Direct-contact melee attack: faces the player, and every
   // ATTACK_COOLDOWN_BY_TYPE[kind] ticks (via attackCooldown counting
   // down) tests the attack hitbox (ATTACK_*[kind]) against the
   // player's fixed hurtbox; on a hit, applies DAMAGE_BY_ANIM[animKind]
   // to the player's health, sets the player's hurt anim (9) and clears
   // its animHold state. `.E`/`.s` are Player's own not-yet-renamed
   // fields (Player hasn't had its phase-1 pass) -- NOT Entity's
   // confirmed fields, left obfuscated on purpose (this file initially
   // guessed `invulnTimer`/`specialTimer` by analogy to the legacy
   // build without confirming against a1's actual Player code; fixed
   // before commit, see CLASS_MAP.md).
   public final void meleeAttack() {
      if (super.animState != 3) {
         if (this.game.aj.animState != 10) {
            super.facingRight = false;
            if (this.game.aj.b() - this.x() > 0) {
               super.facingRight = true;
            }

            if (--this.attackCooldown < 0) {
               this.attackCooldown = ATTACK_COOLDOWN_BY_TYPE[super.kind];
               if (this.game.Z != 0) {
                  this.setAnimState((byte)3);
               }

               if (this.game.aj.E > 0) {
                  return;
               }

               if (this.game
                     .a(
                        this.game.aj.b() - 11,
                        this.game.aj.c() + 7 + Game.L,
                        18,
                        37,
                        this.x() + (super.facingRight ? ATTACK_X_OFFSETS[super.kind] : -ATTACK_X_OFFSETS[super.kind] - ATTACK_WIDTHS[super.kind]),
                        this.y() + ATTACK_Y_OFFSETS[super.kind],
                        ATTACK_WIDTHS[super.kind],
                        ATTACK_HEIGHTS[super.kind]
                     )
                  && this.game.Z != 0) {
                  if (!Game.f) {
                     this.game.aj.health = (byte)(this.game.aj.health - DAMAGE_BY_ANIM[this.animKind]);
                  }

                  this.game.ab = 1;
                  this.game.aj.setAnimState((byte)9);
                  this.game.aj.animHold = 0;
                  if (this.game.aj.s == 2) {
                     this.game.aj.s = 1;
                  }

                  this.game.e = true;
               }
            }
         }
      }
   }

   // Ranged attack: every ATTACK_COOLDOWN_BY_TYPE[kind] ticks (via
   // attackCooldown), fires a player-damaging projectile via Game's
   // 7-arg spawn method (`a(int,int,int,boolean,boolean,int,byte)`,
   // the same one Projectile.java's header documents as the player-shot
   // pool spawner) -- projectile type selected from animKind ranges
   // (6/9/12/15/18/21 -> 0, 7/10/13/16/19/22 -> 6, 17/20/23 -> 3).
   // NOTE: the `this.v == 7 || this.v == 10 || this.v == 13` branch
   // (type 18) is unreachable -- those three animKind values are
   // already caught by the type-6 branch above it. Source-faithful,
   // not a rename artifact; likely a copy-paste bug in the original.
   public final void rangedAttack() {
      if (--this.attackCooldown < 0) {
         this.setAnimState((byte)3);
         super.animRestart = 1;
         if (this.animKind == 6 || this.animKind == 9 || this.animKind == 12 || this.animKind == 15 || this.animKind == 18 || this.animKind == 21) {
            this.game.a(super.posX, this.y() + (Game.K >> 1) << 8, 0, super.facingRight, this.wallCrawlFlipped, super.ap, this.animKind);
         } else if (this.animKind == 7 || this.animKind == 10 || this.animKind == 13 || this.animKind == 16 || this.animKind == 19 || this.animKind == 22) {
            this.game.a(super.posX, this.y() + (Game.K >> 1) << 8, 6, super.facingRight, this.wallCrawlFlipped, super.ap, this.animKind);
         } else if (this.animKind == 7 || this.animKind == 10 || this.animKind == 13) {
            this.game.a(super.posX, this.y() + (Game.K >> 1) << 8, 18, super.facingRight, this.wallCrawlFlipped, super.ap, this.animKind);
         } else if (this.animKind == 17 || this.animKind == 20 || this.animKind == 23) {
            this.game.a(super.posX, this.y() + (Game.K >> 1) << 8, 3, super.facingRight, this.wallCrawlFlipped, super.ap, this.animKind);
         }

         this.attackCooldown = ATTACK_COOLDOWN_BY_TYPE[super.kind];
         this.stateTimer++;
      }
   }

   private void loadAnimFile(InputStream var1, byte[] var2, int var3, boolean var4) {
      byte var5;
      if (var4) {
         var5 = Game.J;
      } else {
         var5 = Game.K;
      }

      try {
         for (int var6 = 0; var6 < var3; var6++) {
            var2[var6] = (byte)var1.read();
            var2[var6] = (byte)(var2[var6] * var5 / 44);
         }
      } catch (IOException var7) {
      }
   }

   // Loads the hitbox/attack-geometry tables (`/enemy_spr_box.bin`):
   // 5 bytes each, alternating scaled-by-tileWidth (loadAnimFile(...,
   // true)) and scaled-by-tileHeight (..., false) reads -- x/w pairs
   // scale by tile width, y/h pairs by tile height, matching the
   // x,y,w,h,x,y,w,h field order (HITBOX_* then ATTACK_*).
   public final void loadHitboxTables(String var1) {
      byte[] var3 = new byte[5];

      try {
         InputStream var4 = var3.getClass().getResourceAsStream(var1);
         this.loadAnimFile(var4, var3, 5, true);
         System.arraycopy(var3, 0, HITBOX_X_OFFSETS, 0, var3.length);
         this.loadAnimFile(var4, var3, 5, false);
         System.arraycopy(var3, 0, HITBOX_Y_OFFSETS, 0, var3.length);
         this.loadAnimFile(var4, var3, 5, true);
         System.arraycopy(var3, 0, HITBOX_WIDTHS, 0, var3.length);
         this.loadAnimFile(var4, var3, 5, false);
         System.arraycopy(var3, 0, HITBOX_HEIGHTS, 0, var3.length);
         this.loadAnimFile(var4, var3, 5, true);
         System.arraycopy(var3, 0, ATTACK_X_OFFSETS, 0, var3.length);
         this.loadAnimFile(var4, var3, 5, false);
         System.arraycopy(var3, 0, ATTACK_Y_OFFSETS, 0, var3.length);
         this.loadAnimFile(var4, var3, 5, true);
         System.arraycopy(var3, 0, ATTACK_WIDTHS, 0, var3.length);
         this.loadAnimFile(var4, var3, 5, false);
         System.arraycopy(var3, 0, ATTACK_HEIGHTS, 0, var3.length);
         var4.close();
      } catch (IOException var5) {
      }
   }

   // Loads `/enemy.bin`: for each of 5 types x 8 anim slots, a
   // (frameCount, extraTicks) byte pair into ANIM_FRAME_COUNTS/
   // ANIM_FRAME_EXTRA, then `frameCount` raw frame-index bytes into
   // ANIM_FRAMES[type][anim].
   private void loadAnimTables(String var1) {
      byte[] var4 = new byte[5];

      try {
         InputStream var5 = var4.getClass().getResourceAsStream(var1);

         for (int var6 = 0; var6 < 5; var6++) {
            for (int var7 = 0; var7 < 8; var7++) {
               byte var2 = (byte)var5.read();
               byte var3 = (byte)var5.read();
               ANIM_FRAME_COUNTS[var6][var7] = var2;
               ANIM_FRAME_EXTRA[var6][var7] = var3;
               this.readBytes(var5, var4, var2);
               System.arraycopy(var4, 0, ANIM_FRAMES[var6][var7], 0, var2);
            }
         }

         var5.close();
      } catch (IOException var8) {
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

   // Dead code (decompiler-visible, not a rename artifact): an
   // unused local array in a static initializer, never stored to any
   // field. Same flavor of finding as the legacy build's Vineflower
   // artifacts (CLASS_MAP.md) -- harmless, kept for source fidelity.
   static {
      byte[] var10000 = new byte[]{10, 10, 10, 10, 10};
   }
}
