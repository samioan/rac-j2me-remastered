import javax.microedition.lcdui.Graphics;

// $VF: renamed from: d
public final class Projectile {
   // $VF: renamed from: a f[]
   public static Sprite[] sprites;
   // $VF: renamed from: b int
   public int animSlot = -1;
   // $VF: renamed from: c byte[]
   public static byte[] damage;
   // $VF: renamed from: d byte[]
   public static byte[] halfWidths;
   // $VF: renamed from: e byte[]
   public static byte[] halfHeights;
   // $VF: renamed from: f byte[]
   public static byte[] speeds;
   // $VF: renamed from: g byte[]
   public static final byte[] spriteMap = new byte[]{2, 3, 4, 11, 12, 13, 8, 9, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, 5, 6, 7};
   // $VF: renamed from: h int
   public int type;
   // $VF: renamed from: i int
   public int worldX;
   // $VF: renamed from: j int
   public int worldY;
   // $VF: renamed from: k int
   public int trailX1;
   // $VF: renamed from: l int
   public int trailY1;
   // $VF: renamed from: m int
   public int trailX2;
   // $VF: renamed from: n int
   public int trailY2;
   // $VF: renamed from: o int
   public int velX;
   // $VF: renamed from: p int
   public int velY;
   // $VF: renamed from: q int
   public int age;
   // $VF: renamed from: r int
   public int halfW;
   // $VF: renamed from: s int
   public int halfH;
   // $VF: renamed from: t boolean
   public boolean detonated;
   // $VF: renamed from: u int
   public int homingTarget;
   // $VF: renamed from: v int
   public int sourceEnemy;
   // $VF: renamed from: w b
   public static Game game;

   public Projectile(Game var1) {
      game = var1;
      this.reset();
   }

   // $VF: renamed from: a () void
   public final void reset() {
      this.type = -1;
      this.worldX = this.worldY = 0;
      this.velX = this.velY = 0;
      this.age = 0;
      this.halfW = 0;
      this.halfH = 0;
      this.detonated = false;
      this.homingTarget = -1;
      this.trailX2 = this.trailX1 = 0;
      this.trailY2 = this.trailY1 = 0;
      this.sourceEnemy = 0;
   }

   // $VF: renamed from: a (boolean) void
   public final void detonate(boolean var1) {
      if (!this.detonated) {
         this.detonated = true;
         switch (this.type) {
            case 3:
            case 4:
            case 5:
               int var4 = this.type - 3;
               if (var1) {
                  game.spawnEnemyProjectile(this.worldX, this.worldY, 21 + var4, true, false, 0, this.sourceEnemy);
                  return;
               }

               game.spawnPlayerProjectile(this.worldX, this.worldY, 21 + var4, true);
               return;
            case 6:
            case 7:
            case 8:
               int var3 = this.type - 6;
               if (var1) {
                  game.spawnEnemyProjectile(this.worldX, this.worldY, 24 + var3, true, false, 0, this.sourceEnemy);
                  return;
               }

               game.spawnPlayerProjectile(this.worldX, this.worldY, 24 + var3, true);
               return;
            case 18:
            case 19:
            case 20:
               int var2 = this.type - 18;
               if (var1) {
                  game.spawnEnemyProjectile(this.worldX, this.worldY, 27 + var2, true, false, 0, this.sourceEnemy);
                  return;
               } else {
                  game.spawnPlayerProjectile(this.worldX, this.worldY, 27 + var2, true);
               }
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
         }
      }
   }

   // $VF: renamed from: b () int
   public final int weaponClass() {
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
         return this.type >= 27 && this.type <= 29 ? 7 : 0;
      }
   }

   // $VF: renamed from: b (boolean) void
   public final void update(boolean var1) {
      int var4 = Game.cameraX;
      int var5 = Game.cameraY;
      if (this.detonated) {
         this.reset();
      } else if (this.type != -1) {
         if (this.type >= 6 && this.type <= 8) {
            this.worldX = this.worldX + this.velX;
            int var9 = this.worldX >> 8;
            this.worldY = this.worldY + this.velY;
            int var14 = this.worldY >> 8;
            if (var9 < -var4 - 57 || var9 > -var4 + 240 + 57 || var14 < -var5 - 38 || var14 > -var5 + 320 + 38) {
               this.detonate(var1);
            }
         } else if (this.type >= 0 && this.type <= 2) {
            this.worldX = this.worldX + this.velX;
            int var8 = this.worldX >> 8;
            this.worldY = this.worldY + this.velY;
            int var13 = this.worldY >> 8;
            if (var8 < -var4 - 57 || var8 > -var4 + 240 + 57 || var13 < -var5 - 38 || var13 > -var5 + 320 + 38) {
               this.reset();
            }
         } else if (this.type >= 3 && this.type <= 5) {
            this.worldX = this.worldX + this.velX;
            int var7 = this.worldX >> 8;
            int var12 = this.worldY >> 8;
            if (this.worldX >= 0 && this.worldY >= 0 && var12 <= 646 && var7 <= 1539) {
               if (var12 + halfHeights[this.type] >= Game.getFloorY(var7, var12)) {
                  this.detonate(var1);
               }
            } else {
               this.detonate(var1);
            }

            this.worldY = this.worldY - this.velY;
            this.velY -= 256;
         } else if (this.type >= 9 && this.type <= 11) {
            if (this.age++ == 2) {
               this.reset();
            }
         } else if (this.type >= 12 && this.type <= 14) {
            this.homeInOnTarget();
            int var6 = this.worldX >> 8;
            int var11 = this.worldY >> 8;
            if (var6 < -var4 - 57 || var6 > -var4 + 240 + 57 || var11 < -var5 - 38 || var11 > -var5 + 320 + 38) {
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

            this.homeInOnTarget();
            int var2 = this.worldX >> 8;
            int var3 = this.worldY >> 8;
            if (var2 < -var4 - 57 || var2 > -var4 + 240 + 57 || var3 < -var5 - 38 || var3 > -var5 + 320 + 38) {
               this.detonate(var1);
            }
         }

         if (this.type != 15 && this.type != 16 && this.type != 17 && this.type != 9 && this.type != 10 && this.type != 11 && this.type < 21) {
            int var10 = (this.worldX >> 8) / 57;
            int var15 = this.worldY / 38 >> 8;
            if (!game.isPassable(var10, var15)) {
               this.detonate(var1);
               if (this.type >= 12 && this.type <= 14 && Game.getRawTile(var10, var15) == -1 && var10 > 0 && var10 < 28 && var15 > 0 && var15 < 18) {
                  game.tiles[var10][var15] = 0;
                  Game.solidColumnMask[var10] = Game.solidColumnMask[var10] & ~(1 << var15);
                  game.setTile(var10, var15, 1);
               }
            }
         }
      }
   }

   // $VF: renamed from: c () void
   public final void homeInOnTarget() {
      if (this.homingTarget == -1) {
         int var4 = this.worldX >> 8;
         int var5 = this.worldY >> 8;

         for (int var1 = Game.maxEnemies - 1; var1 >= 0; var1--) {
            if (game.enemies[var1].type != -1 && game.enemies[var1].state != 5) {
               int var2 = game.enemies[var1].pixelX();
               int var3 = game.enemies[var1].feetY();
               if ((var2 - var4) * (var2 - var4) + (var3 - var5) * (var3 - var5) <= 15876 && (this.velX > 0 ? var2 > var4 : var2 < var4)) {
                  this.homingTarget = var1;
                  break;
               }
            }
         }
      } else if (game.enemies[this.homingTarget].type != -1 && game.enemies[this.homingTarget].state != 5) {
         int var8 = game.enemies[this.homingTarget].pixelX();
         int var9 = game.enemies[this.homingTarget].feetY()
            + Enemy.bodyOffsetY[game.enemies[this.homingTarget].type]
            + (Enemy.bodyHeight[game.enemies[this.homingTarget].type] >> 1);
         int var10 = this.worldX >> 8;
         int var11 = this.worldY >> 8;
         int var12 = var8 - var10;
         int var6 = var9 - var11;
         int var7 = speeds[this.type] << 8;
         if (this.type >= 18 && this.type <= 20) {
            if (var6 > 0) {
               this.velY = var7;
               if (var6 << 8 < this.velY) {
                  this.velY = 0;
               }
            } else {
               this.velY = -var7;
               if (var6 << 8 > this.velY) {
                  this.velY = 0;
               }
            }
         } else if (this.age >= 10) {
            this.trailX2 = this.trailX1;
            this.trailY2 = this.trailY1;
            this.trailX1 = this.worldX;
            this.trailY1 = this.worldY;
            if (Math.abs(var12) > Math.abs(var6)) {
               if (var12 > 0) {
                  this.velX = var7;
               } else {
                  this.velX = -var7;
               }

               this.velY = 0;
            } else {
               if (var6 > 0) {
                  this.velY = var7;
               } else {
                  this.velY = -var7;
               }

               this.velX = 0;
            }

            this.age = 0;
         }
      } else {
         this.homingTarget = -1;
      }

      this.age++;
      this.worldX = this.worldX + this.velX;
      this.worldY = this.worldY + this.velY;
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics, b) void
   public final void draw(Graphics var1, Game var2) {
      int var3 = Game.cameraX;
      int var4 = Game.cameraY;
      if (this.type >= 21 && this.type <= 30) {
         short var13 = sprites[0].height;
         short var14 = sprites[0].width;
         int var19 = (this.worldX >> 8) + var3 - (var14 << 1);
         int var22 = (this.worldY >> 8) + var4 - (var13 << 1);
         if (this.animSlot > 0) {
            var2.drawAnim(var1, sprites, Game.maxEnemies + this.animSlot, var19, var22, 0);
            if (!var2.stepAnim(Game.maxEnemies + this.animSlot, Game.deltaTime)) {
               Game.en[this.animSlot] = -1;
               this.animSlot = -1;
               this.reset();
               return;
            }
         }
      } else {
         int var5 = var2.ak;
         byte var6;
         if ((var6 = spriteMap[this.type]) < 0) {
            int var15 = (this.worldX >> 8) + var3;
            int var20 = (this.worldY >> 8) + var4;
            if (this.type >= 9 && this.type <= 11) {
               var15 -= this.velX > 0 ? this.halfW : -this.halfW;
               if (this.age == 0) {
                  var1.setColor(255, 255, 255);
               } else if (this.type == 9) {
                  var1.setColor(0, 0, 255);
               } else if (this.type == 10) {
                  var1.setColor(0, 255, 255);
               } else {
                  var1.setColor(255, 0, 0);
               }

               var15 += this.velX > 0 ? 4 : -5;
               var1.drawLine(var15, var20, this.velX > 0 ? var15 + 168 : var15 - 168, var20 + 11);
               var1.drawLine(var15, var20, this.velX > 0 ? var15 + 168 : var15 - 168, var20 + 5);
               var1.drawLine(var15, var20, this.velX > 0 ? var15 + 168 : var15 - 168, var20 - 5);
               var1.drawLine(var15, var20, this.velX > 0 ? var15 + 168 : var15 - 168, var20 - 11);
               return;
            }

            if (this.type >= 12 && this.type <= 14) {
               int var23 = (this.trailX1 >> 8) + var3;
               int var10 = (this.trailY1 >> 8) + var4;
               int var11 = (this.trailX2 >> 8) + var3;
               int var12 = (this.trailY2 >> 8) + var4;
               if (this.type == 12) {
                  var1.setColor(255, 255, 0);
               }

               if (this.type == 13) {
                  var1.setColor(255, 255, 255);
               }

               if (this.type == 14) {
                  var1.setColor(0, 100, 255);
               }

               var1.drawLine(var15, var20, var23, var10);
               var1.drawLine(var23, var10, var11, var12);
               var1.fillRect(var15 - 1, var20 - 1, 3, 3);
               return;
            }

            if (this.type >= 15 && this.type <= 17) {
               var15 = var15 - (this.velX > 0 ? this.halfW : -this.halfW) + (this.velX > 0 ? -5 : 3);
               var20 += 3;
               if (this.type == 15) {
                  var1.setColor(0, 0, 255);
               } else if (this.type == 16) {
                  var1.setColor(0, 255, 255);
               } else {
                  var1.setColor(255, 100, 255);
               }

               if (this.velX > 0) {
                  var1.fillRoundRect(var15 + 8, var20 - 11, 168, 22, 12, 12);
                  var1.setColor(255, 255, 255);
                  var1.fillRect(var15 + 12, var20 - 11 + 6, 168, 10);
                  return;
               }

               var1.fillRoundRect(var15 - 8 - 168, var20 - 11, 168, 22, 12, 12);
               var1.setColor(255, 255, 255);
               var1.fillRect(var15 - 12 - 168, var20 - 11 + 6, 168, 10);
               return;
            }

            var1.setColor(255, 255, 255);
            var1.fillRect(var15 - 3, var20 - 3, 6, 6);
            return;
         }

         int var7 = (this.worldX >> 8) + var3;
         int var8 = (this.worldY >> 8) + var4 - (var5 >> 1);
         byte var9 = 0;
         if (this.velX < 0) {
            var9 = 2;
         } else if (this.velX == 0) {
            if (var6 < 11) {
               var6 += 12;
            }

            if (this.velY > 0) {
               var9 = 1;
            }
         }

         Game.swingshotSprites[var6].draw(var1, var7, var8, var9);
      }
   }
}
