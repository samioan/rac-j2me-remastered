// Hand-written, faithfully renamed from decompiled_a1/j.java (class j).
// a1's equivalent of the legacy build's Projectile (decompiled/g.java,
// see ../src/Projectile.java) -- same two-10-slot-pool design (confirmed
// at decompiled_a1/h.java:5500 `e(int,int,int)` for enemy shots -> Game's
// `ak[]` pool, and :5533 `a(int,int,int,boolean,boolean,int,byte)` for
// player shots -> Game's `al[]` pool), same 33 types, same per-type
// table shape as the legacy build's (DAMAGE_BY_TYPE/HALF_WIDTHS/
// HALF_HEIGHTS/SPEEDS/RENDER_MODES) -- "evolved tuning" per
// BUILD_COMPARISON.md, confirmed here: RENDER_MODES uses 13-24 with -1
// sentinels (vs. the legacy build's 8-11).
//
// Only Projectile's own members are renamed. Game/Enemy/Player/LevelMap
// members reached through `this.game.xxx` stay obfuscated except where
// already confirmed by an earlier class's phase-1 (Entity's kind/
// animState/posX/facingRight; LevelMap's tiles/columnSolidMasks;
// CanvasShell's directGraphics) -- see each of those files for the
// evidence. `Enemy.b`/`Enemy.d` (this file's `weaponIndex...` no --
// see below) are referenced here as class-renamed-only, roles
// unconfirmed pending Enemy's own phase-1, though the call site
// (enemy center-y = enemy.y() + Enemy.b[kind] + Enemy.d[kind]/2) is
// a strong hint they're the legacy build's per-type y-offset/height
// hitbox pair.
import javax.microedition.lcdui.Graphics;

public final class Projectile {
   public static final byte[] DAMAGE_BY_TYPE = new byte[]{1, 2, 4, 0, 0, 0, 0, 0, 0, 12, 12, 12, 1, 2, 3, 1, 2, 2, 0, 0, 0, 3, 5, 6, 6, 11, 17, 4, 7, 10, 0, 0, 0};
   public static final byte[] HALF_WIDTHS = new byte[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 3, 3, 3, 0, 0, 0, 3, 3, 3, 45, 65, 65, 15, 15, 30, 15, 15, 15, 0, 12, 10};
   public static final byte[] HALF_HEIGHTS = new byte[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 3, 3, 3, 0, 0, 0, 3, 3, 3, 45, 65, 65, 15, 15, 30, 15, 15, 15, 0, 12, 10};
   public static final byte[] SPEEDS = new byte[]{10, 16, 16, 5, 5, 5, 8, 8, 8, 1, 1, 1, 4, 4, 4, 1, 1, 1, 8, 8, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 10};
   public static final short[] RENDER_MODES = new short[]{
      13, 14, 15, 22, 23, 24, 19, 20, 21, -1, -1, -1, -1, -1, -1, -1, -1, -1, 16, 17, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1
   };
   public byte type;
   public int posX;
   public int posY;
   public int prevX;
   public int prevY;
   public int prev2X;
   public int prev2Y;
   public int vx;
   public int vy;
   public byte age;
   public int halfWidth;   // copy of HALF_WIDTHS[type], set at spawn (confirmed decompiled_a1/h.java:5513,5541)
   public int halfHeight;  // copy of HALF_HEIGHTS[type], set at spawn
   private boolean detonated;
   private short homingTargetEnemy; // -1 = none yet; index into Game.enemies[] (the enemy pool)
   public byte sourceAnim;          // damage/weapon-hit class, set only for player shots (h.java:5540, `this.al[var8].r = var7`)
   public int playerOffsetX;        // horizontal offset from the player's posX at spawn, facing-adjusted (h.java:5523)
   public static byte tileWidth;
   public static byte tileHeight;
   public static short hudHeight;   // Game.hudHeight -- clip-below-HUD y offset, used by setClip(0, hudHeight, 176, 220-hudHeight)
   private Game game;

   public Projectile(Game var1) {
      tileWidth = Game.tileWidth;
      tileHeight = Game.tileHeight;
      hudHeight = Game.hudHeight;
      this.game = var1;
      this.type = -1;
      this.posX = this.posY = 0;
      this.vx = this.vy = 0;
      this.age = 0;
      this.halfWidth = 0;
      this.halfHeight = 0;
      this.detonated = false;
      this.homingTargetEnemy = -1;
      this.prevX = this.prev2X = 0;
      this.prevY = this.prev2Y = 0;
      this.sourceAnim = 0;
   }

   public final void reset() {
      this.type = -1;
      this.posX = this.posY = 0;
      this.vx = this.vy = 0;
      this.age = 0;
      this.halfWidth = 0;
      this.halfHeight = 0;
      this.detonated = false;
      this.homingTargetEnemy = -1;
      this.prevX = this.prev2X = 0;
      this.prevY = this.prev2Y = 0;
      this.sourceAnim = 0;
   }

   public final boolean isActive() {
      return !this.detonated;
   }

   // On impact: spawns a per-type hit effect (hit spark / hyper-shot
   // continuation, matching type ranges 3-5/6-8/18-20) then marks this
   // slot detonated, unless already detonated or it's the decoy drone
   // (type 32, which never detonates via this path). Type 31 (the boss
   // aim marker) has its own one-shot "just aimed" flag (Game.dI).
   public final void detonate(boolean var1) {
      if (!this.detonated && this.type != 32) {
         this.detonated = true;
         switch (this.type) {
            case 3:
            case 4:
            case 5:
               int var4 = this.type - 3;
               if (var1) {
                  this.game.a(this.posX, this.posY, 21 + var4, true, false, 0, this.sourceAnim);
                  return;
               }

               this.game.e(this.posX, this.posY, 21 + var4);
               return;
            case 6:
            case 7:
            case 8:
               int var3 = this.type - 6;
               if (var1) {
                  this.game.a(this.posX, this.posY, 24 + var3, true, false, 0, this.sourceAnim);
                  return;
               }

               this.game.e(this.posX, this.posY, 24 + var3);
               return;
            case 18:
            case 19:
            case 20:
               int var2 = this.type - 18;
               if (var1) {
                  this.game.a(this.posX, this.posY, 27 + var2, true, false, 0, this.sourceAnim);
                  return;
               }

               this.game.e(this.posX, this.posY, 27 + var2);
               return;
            case 31:
               if (!Game.dI) {
                  this.game.C(this.posX, this.posY);
               }

               Game.dI = false;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
         }
      }
   }

   // Groups projectile types into weapon-family ids 1-7 (types 0-2,
   // 9-11, 12-14, 15-17, 21-23, 24-26, 27-29 respectively; everything
   // else 0) -- matches the 7 non-melee weapons in Player's weapon
   // system (legacy build convention: weapon index 1-7, 0 = wrench),
   // likely used for ammo/weapon-XP bookkeeping when a hit registers.
   // TENTATIVE name pending Player's own phase-1.
   public final byte weaponIndex() {
      if (this.type >= 0 && this.type <= 2) {
         return 1;
      } else if (this.type >= 21 && this.type <= 23) {
         return 2;
      } else if (this.type >= 24 && this.type <= 26) {
         return 3;
      } else if (this.type >= 9 && this.type <= 11) {
         return 4;
      } else if (this.type >= 12 && this.type <= 14) {
         return 5;
      } else if (this.type >= 15 && this.type <= 17) {
         return 6;
      } else {
         return (byte)(this.type >= 27 && this.type <= 29 ? 7 : 0);
      }
   }

   // Main per-tick update: movement per type-range (straight shot,
   // gravity-arc melee-ish types 3-5, trail-laying homing missiles
   // 12-14, one-shot "flash" types 9-11/15-17/21-30, the decoy drone's
   // bounce, the boss crosshair 31), then off-screen and tile-solidity
   // culling (detonate on leaving the visible area with margin, or on
   // hitting a solid/half-solid tile -- shattering a breakable tile 34
   // back to 0 for the homing-missile types).
   public final void update(boolean var1) {
      int var4 = Game.x;
      int var5 = Game.y;
      if (this.detonated) {
         this.reset();
      } else if (this.type != -1) {
         if (this.type == 32) {
            // Decoy drone (type 32) reuses vx/prevY/prev2Y for a purpose
            // unrelated to their name here: vx is a pause countdown
            // between bounce steps, prevY/prev2Y are the min/max Y
            // bounce bounds (set at spawn, not updated as position
            // history the way they are for the homing-missile types).
            if (this.vx != 0) {
               this.vx--;
            }

            if (this.vx == 0) {
               this.posY = this.posY + this.vy;
            }

            if (this.posY < this.prev2Y && this.vx == 0) {
               this.vy *= -1;
               this.vx = 15;
            } else if (this.posY > this.prevY && this.vx == 0) {
               this.vy *= -1;
               this.vx = 1;
            }

            this.age++;
            if (this.age >= 3) {
               this.age = 0;
            }
         } else if (this.type >= 6 && this.type <= 8) {
            this.posX = this.posX + this.vx;
            int var9 = this.posX >> 8;
            this.posY = this.posY + this.vy;
            int var14 = this.posY >> 8;
            if (var9 < -var4 - tileWidth || var9 > -var4 + 176 + tileWidth || var14 < -var5 - tileHeight || var14 > -var5 + 220 + tileHeight) {
               this.detonate(var1);
            }
         } else if (this.type >= 0 && this.type <= 2) {
            this.posX = this.posX + this.vx;
            int var8 = this.posX >> 8;
            this.posY = this.posY + this.vy;
            int var13 = this.posY >> 8;
            if (var8 < -var4 - tileWidth || var8 > -var4 + 176 + tileWidth || var13 < -var5 - tileHeight || var13 > -var5 + 220 + tileHeight) {
               this.reset();
            }
         } else if (this.type >= 3 && this.type <= 5) {
            this.posX = this.posX + this.vx;
            int var7 = this.posX >> 8;
            int var12 = this.posY >> 8;
            if (this.posX >= 0 && this.posY >= 0 && var12 <= 17 * tileHeight && var7 <= 27 * tileWidth) {
               if (var12 + HALF_HEIGHTS[this.type] >= this.game.b(var7, var12, tileHeight - HALF_HEIGHTS[this.type])) {
                  this.detonate(var1);
               }
            } else {
               this.detonate(var1);
            }

            this.posY = this.posY - this.vy;
            this.vy -= 256;
         } else if (this.type >= 9 && this.type <= 11) {
            if (this.age++ == 2) {
               this.reset();
            }
         } else if (this.type >= 12 && this.type <= 14) {
            this.homingSeek(1);
            int var6 = this.posX >> 8;
            int var11 = this.posY >> 8;
            if (var6 < -var4 - tileWidth || var6 > -var4 + 176 + tileWidth || var11 < -var5 - tileHeight || var11 > -var5 + 220 + tileHeight) {
               this.reset();
            }
         } else if (this.type >= 15 && this.type <= 17) {
            if (this.age++ == 3) {
               this.reset();
            }
         } else if (this.type >= 18 && this.type <= 20) {
            if (var1) {
               return;
            }

            this.homingSeek(4);
            int var2 = this.posX >> 8;
            int var3 = this.posY >> 8;
            if (var2 < -var4 - tileWidth || var2 > -var4 + 176 + tileWidth || var3 < -var5 - tileHeight || var3 > -var5 + 220 + tileHeight) {
               this.detonate(var1);
            }
         } else if (this.type >= 21 && this.type <= 30) {
            if (++this.age == 3) {
               this.reset();
            }
         } else if (this.type == 31) {
            this.posX = this.posX + this.vx;
            this.posY = this.posY + this.vy;
         }

         if ((this.type != 15 || this.type != 16 || this.type != 17 || this.type != 9 || this.type != 10 || this.type != 11) && this.type < 21 || this.type == 31) {
            int var10 = (this.posX >> 8) / tileWidth;
            int var15 = (this.posY >> 8) / tileHeight;
            if (!this.game.levelMap.isWalkable(var10, var15)) {
               this.detonate(var1);
               if (this.type >= 12 && this.type <= 14 && this.game.levelMap.getTile(var10, var15) == 34) {
                  this.game.levelMap.tiles[var10][var15] = 0;
                  LevelMap.columnSolidMasks[var10] = LevelMap.columnSolidMasks[var10] & ~(1 << var15);
               }
            }
         }
      }
   }

   // Homing steer step for the trail-laying missile types (12-14) and
   // the turret-lock types (18-20): finds the nearest alive, non-dying
   // (animState != 5) enemy roughly ahead of travel direction within a
   // ~126px radius (15876 = 126^2), then steers vx/vy toward it every
   // 10 ticks (18-20 instead locks a fixed vy toward/away immediately).
   // `var1` is read nowhere in the original method body -- a genuinely
   // unused/vestigial parameter, kept for signature fidelity.
   public final void homingSeek(int var1) {
      if (this.homingTargetEnemy == -1) {
         int var5 = this.posX >> 8;
         int var6 = this.posY >> 8;

         for (int var2 = Game.enemyPoolSize - 1; var2 >= 0; var2--) {
            if (this.game.enemies[var2].kind != -1 && this.game.enemies[var2].animState != 5) {
               short var3 = this.game.enemies[var2].x();
               short var4 = this.game.enemies[var2].y();
               if ((var3 - var5) * (var3 - var5) + (var4 - var6) * (var4 - var6) <= 15876 && (this.vx > 0 ? var3 > var5 : var3 < var5)) {
                  this.homingTargetEnemy = (short)var2;
                  break;
               }
            }
         }
      } else if (this.game.enemies[this.homingTargetEnemy].kind != -1 && this.game.enemies[this.homingTargetEnemy].animState != 5) {
         short var9 = this.game.enemies[this.homingTargetEnemy].x();
         int var10 = this.game.enemies[this.homingTargetEnemy].y() + Enemy.HITBOX_Y_OFFSETS[this.game.enemies[this.homingTargetEnemy].kind] + (Enemy.HITBOX_HEIGHTS[this.game.enemies[this.homingTargetEnemy].kind] >> 1);
         int var11 = this.posX >> 8;
         int var12 = this.posY >> 8;
         int var13 = var9 - var11;
         int var7 = var10 - var12;
         int var8 = SPEEDS[this.type] << 8;
         if (this.type >= 18 && this.type <= 20) {
            if (var7 > 0) {
               this.vy = var8;
               if (var7 << 8 < this.vy) {
                  this.vy = 0;
               }
            } else {
               this.vy = -var8;
               if (var7 << 8 > this.vy) {
                  this.vy = 0;
               }
            }
         } else if (this.age >= 10) {
            this.prev2X = this.prevX;
            this.prev2Y = this.prevY;
            this.prevX = this.posX;
            this.prevY = this.posY;
            if (this.game.abs(var13) > this.game.abs(var7)) {
               if (var13 > 0) {
                  this.vx = var8;
               } else {
                  this.vx = -var8;
               }

               this.vy = 0;
            } else {
               if (var7 > 0) {
                  this.vy = var8;
               } else {
                  this.vy = -var8;
               }

               this.vx = 0;
            }

            this.age = 0;
         }
      } else {
         this.homingTargetEnemy = -1;
      }

      this.age++;
      this.posX = this.posX + this.vx;
      this.posY = this.posY + this.vy;
   }

   public final void render(Graphics var1) {
      int var2 = Game.x;
      int var3 = Game.y;
      if (this.type >= 0) {
         if (this.type >= 21 && this.type <= 30) {
            int var24 = this.age * 44;
            int var31 = (this.posX >> 8) + var2 - 22;
            int var35 = (this.posY >> 8) + var3 - 22;
            if (var31 < 176 && var31 + 44 >= 0) {
               this.game.b(var1, var31, var35, 44, 44);
               var1.drawImage(Game.aM, var31, var35 - var24 - 0, 0);
               return;
            }
         } else if (this.type == 32) {
            int var5 = Game.ay.getHeight() / 3;
            int var6 = Game.ay.getWidth();
            int var7 = this.age * var5;
            int var8 = (this.posX >> 8) + var2 - (var6 >> 1);
            int var9 = (this.posY >> 8) + var3 - (var5 >> 1);
            if (0 < var5 && var8 < 176 && var8 + var6 >= 0) {
               this.game.b(var1, var8, var9, var6, var5 - 0);
               var1.drawImage(Game.ay, var8, var9 - var7 - 0, 0);
               return;
            }
         } else if (this.type == 31) {
            int var16 = (this.posX >> 8) + var2 - (Game.aO.getWidth() >> 1);
            int var19 = (this.posY >> 8) + var3 - (Game.aO.getHeight() >> 1);
            int var22 = tileWidth * 14 - ((this.posX >> 8) - (Game.aO.getWidth() >> 1));
            int var25 = tileHeight * 9 - ((this.posY >> 8) - (Game.aO.getHeight() >> 1));
            if (var22 * var22 + var25 * var25 > 1764 && var16 < 176 && var16 + 12 >= 0 && var19 < 220 && var19 + 12 >= 0) {
               var1.setClip(0, hudHeight, 176, 220 - hudHeight);
               var1.drawImage(Game.aO, var16, var19, 0);
               return;
            }
         } else {
            int var23;
            if ((var23 = RENDER_MODES[this.type] * 19) < 0) {
               int var27 = (this.posX >> 8) + var2;
               int var33 = (this.posY >> 8) + var3;
               if (this.type >= 9 && this.type <= 11) {
                  var27 -= this.vx > 0 ? this.halfWidth : -this.halfWidth;
                  var1.setClip(0, hudHeight, 176, 220 - hudHeight);
                  if (this.age == 0) {
                     var1.setColor(255, 255, 255);
                  } else {
                     var1.setColor(0, 0, 255);
                  }

                  var1.drawLine(var27, var33, this.vx > 0 ? var27 + 168 : var27 - 168, var33 + 11);
                  var1.drawLine(var27, var33, this.vx > 0 ? var27 + 168 : var27 - 168, var33 + 5);
                  var1.drawLine(var27, var33, this.vx > 0 ? var27 + 168 : var27 - 168, var33 - 5);
                  var1.drawLine(var27, var33, this.vx > 0 ? var27 + 168 : var27 - 168, var33 - 11);
                  return;
               }

               if (this.type >= 12 && this.type <= 14) {
                  int var10 = (this.prevX >> 8) + var2;
                  int var11 = (this.prevY >> 8) + var3;
                  int var12 = (this.prev2X >> 8) + var2;
                  int var13 = (this.prev2Y >> 8) + var3;
                  var1.setClip(0, hudHeight, 176, 220 - hudHeight);
                  var1.setColor(255, 255, 0);
                  var1.drawLine(var27, var33, var10, var11);
                  var1.drawLine(var10, var11, var12, var13);
                  var1.fillRect(var27 - 1, var33 - 1, 3, 3);
                  return;
               }

               if (this.type >= 15 && this.type <= 17) {
                  if (this.game.player.facingRight) {
                     if (this.posX < this.game.player.posX + this.playerOffsetX) {
                        this.posX = this.game.player.posX + this.playerOffsetX;
                     }
                  } else if (this.posX > this.game.player.posX - this.playerOffsetX) {
                     this.posX = this.game.player.posX - this.playerOffsetX;
                  }

                  var27 = (this.posX >> 8) + var2;
                  var33 = (this.posY >> 8) + var3;
                  var27 -= this.vx > 0 ? this.halfWidth : -this.halfWidth;
                  var1.setClip(0, hudHeight, 176, 220 - hudHeight);
                  var1.setColor(0, 0, 255);
                  if (this.vx > 0) {
                     var1.fillRoundRect(var27, var33 - 11, 168, 22, 12, 12);
                     var1.setColor(16250871);
                     var1.fillRect(var27 + 4, var33 - 11 + 10, 164, 2);
                     return;
                  }

                  var1.fillRoundRect(var27 - 168, var33 - 11, 168, 22, 12, 12);
                  var1.setColor(16250871);
                  var1.fillRect(var27 - 168, var33 - 11 + 10, 164, 2);
                  return;
               }

               var1.setClip(0, hudHeight, 176, 220 - hudHeight);
               var1.setColor(255, 255, 255);
               var1.fillRect(var27 - 3, var33 - 3, 6, 6);
               return;
            }

            int var26 = (this.posX >> 8) + var2 - 9;
            int var32 = (this.posY >> 8) + var3 - 9;
            if (var26 >= 176 || var32 >= 220 || var26 + 19 < 0 || var32 + 19 < 0) {
               return;
            }

            this.game.b(var1, var26, var32, 19, 19);
            if (this.vx > 0) {
               var1.drawImage(Game.aL, var26, var32 - var23 - 0, 20);
               return;
            }

            if (this.vx == 0) {
               if (this.vy < 0) {
                  CanvasShell.directGraphics.drawImage(Game.aL, var26 - var23, var32 - 0, 20, 90);
                  return;
               }

               CanvasShell.directGraphics.drawImage(Game.aL, var26 - 475 + var23, var32 - 0, 20, 270);
               return;
            }

            CanvasShell.directGraphics.drawImage(Game.aL, var26, var32 - var23 - 0, 20, 8192);
         }
      }
   }
}
