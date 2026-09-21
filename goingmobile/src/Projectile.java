import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class Projectile {
   public static final byte[] DAMAGE_BY_TYPE = new byte[]{1, 2, 3, 0, 0, 0, 0, 0, 0, 12, 12, 12, 1, 2, 3, 1, 2, 2, 0, 0, 0, 5, 8, 10, 6, 11, 17, 4, 6, 8, 0, 0, 0};
   public static final byte[] HALF_WIDTHS = new byte[]{6, 6, 6, 3, 3, 3, 3, 3, 3, 0, 0, 0, 3, 3, 3, 0, 0, 0, 3, 3, 3, 20, 30, 30, 10, 10, 20, 10, 10, 10, 0, 8, 6};
   public static final byte[] HALF_HEIGHTS = new byte[]{3, 3, 3, 2, 2, 2, 2, 2, 2, 0, 0, 0, 2, 2, 2, 0, 0, 0, 2, 2, 2, 20, 30, 30, 10, 10, 20, 10, 10, 10, 0, 8, 6};
   public static final byte[] SPEEDS = new byte[]{12, 12, 12, 3, 3, 3, 8, 8, 8, 1, 1, 1, 4, 4, 4, 1, 1, 1, 8, 8, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 8};
   public static final byte[] RENDER_MODES = new byte[]{
      8, 8, 8, 11, 11, 11, 10, 10, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, 9, 9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, -1
   };
   public static final byte[] f = new byte[]{3, 4, 6};
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
   public int halfWidth;
   public int halfHeight;
   private boolean detonated;
   private short w;
   public byte sourceAnim;
   public boolean facingRight;
   public int spawnX;
   private Game game;
   private boolean y;

   public Projectile(Game var1) {
      game = var1;
      type = -1;
      posX = posY = 0;
      vx = vy = 0;
      age = 0;
      halfWidth = 0;
      halfHeight = 0;
      detonated = false;
      this.w = -1;
      prev2X = prevX = 0;
      prev2Y = prevY = 0;
      sourceAnim = 0;
   }

   private void c() {
      type = -1;
      posX = posY = 0;
      vx = vy = 0;
      age = 0;
      halfWidth = 0;
      halfHeight = 0;
      detonated = false;
      this.w = -1;
      prev2X = prevX = 0;
      prev2Y = prevY = 0;
      sourceAnim = 0;
   }

   public final boolean a() {
      return !detonated;
   }

   public final void a(boolean var1) {
      if (!detonated && type != 32) {
         detonated = true;
         switch (type) {
            case 3:
            case 4:
            case 5:
               int var4 = type - 3;
               if (var1) {
                  game.a(posX, posY, 21 + var4, true, false, 0, sourceAnim);
                  return;
               }

               game.d(posX, posY, 21 + var4);
               return;
            case 6:
            case 7:
            case 8:
               int var3 = type - 6;
               if (var1) {
                  game.a(posX, posY, 24 + var3, true, false, 0, sourceAnim);
                  return;
               }

               game.d(posX, posY, 24 + var3);
               return;
            case 18:
            case 19:
            case 20:
               int var2 = type - 18;
               if (var1) {
                  game.a(posX, posY, 27 + var2, true, false, 0, sourceAnim);
                  return;
               }

               game.d(posX, posY, 27 + var2);
               return;
            case 31:
               game.c(posX, posY);
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

   public final byte b() {
      if (type >= 0 && type <= 2) {
         return 1;
      } else if (type >= 21 && type <= 23) {
         return 2;
      } else if (type >= 24 && type <= 26) {
         return 3;
      } else if (type >= 9 && type <= 11) {
         return 4;
      } else if (type >= 12 && type <= 14) {
         return 5;
      } else if (type >= 15 && type <= 17) {
         return 6;
      } else {
         return (byte)(type >= 27 && type <= 29 ? 7 : 0);
      }
   }

   public final void b(boolean var1) {
      int var4 = Game.r;
      int var5 = Game.s;
      if (!detonated || type >= 21 && type <= 30) {
         if (type != -1) {
            if (type == 32) {
               if (vx != 0) {
                  vx--;
               }

               if (vx == 0) {
                  posY = posY + vy;
               }

               label225: {
                  Projectile var10000;
                  byte var10001;
                  if (posY < prev2Y && vx == 0) {
                     vy *= -1;
                     var10000 = this;
                     var10001 = 15;
                  } else {
                     if (posY <= prevY || vx != 0) {
                        break label225;
                     }

                     vy *= -1;
                     var10000 = this;
                     var10001 = 1;
                  }

                  vx = var10001;
               }

               age++;
               if (age >= 3) {
                  age = 0;
               }
            } else if (type >= 6 && type <= 8) {
               posX = posX + vx;
               int var9 = posX >> 8;
               posY = posY + vy;
               int var14 = posY >> 8;
               if (var9 < -var4 - 22 || var9 > -var4 + 128 + 22 || var14 < -var5 - 14 || var14 > -var5 + 128 + 14) {
                  this.a(var1);
               }
            } else if (type >= 0 && type <= 2) {
               posX = posX + vx;
               int var8 = posX >> 8;
               posY = posY + vy;
               int var13 = posY >> 8;
               if (var8 < -var4 - 22 || var8 > -var4 + 128 + 22 || var13 < -var5 - 14 || var13 > -var5 + 128 + 14) {
                  this.c();
               }
            } else if (type >= 3 && type <= 5) {
               label226: {
                  posX = posX + vx;
                  int var7 = posX >> 8;
                  int var12 = posY >> 8;
                  Projectile var18;
                  if (posX >= 0 && posY >= 0 && var12 <= 238 && var7 <= 594) {
                     if (var12 + HALF_HEIGHTS[type] < Game.b(var7, var12)) {
                        break label226;
                     }

                     var18 = this;
                  } else {
                     var18 = this;
                  }

                  var18.a(var1);
               }

               posY = posY - vy;
               vy -= 256;
            } else if (type >= 9 && type <= 11) {
               if (age++ == 2) {
                  this.c();
               }
            } else if (type >= 12 && type <= 14) {
               this.d();
               int var6 = posX >> 8;
               int var11 = posY >> 8;
               if (var6 < -var4 - 22 || var6 > -var4 + 128 + 22 || var11 < -var5 - 14 || var11 > -var5 + 128 + 14) {
                  this.c();
               }
            } else if (type >= 15 && type <= 17) {
               if (age++ == 3) {
                  this.c();
               }
            } else if (type >= 18 && type <= 20) {
               if (var1) {
                  return;
               }

               this.d();
               int var2 = posX >> 8;
               int var3 = posY >> 8;
               if (var2 < -var4 - 22 || var2 > -var4 + 128 + 22 || var3 < -var5 - 14 || var3 > -var5 + 128 + 14) {
                  this.a(var1);
               }
            } else if (type >= 21 && type <= 30) {
               if (++age == 3) {
                  this.c();
               }
            } else if (type == 31) {
               posX = posX + vx;
               posY = posY + vy;
            }

            if ((type != 15 || type != 16 || type != 17 || type != 9 || type != 10 || type != 11) && type < 21 || type == 31) {
               int var10 = (posX >> 8) / 22;
               int var15 = (posY >> 8) / 14;
               if (!game.N.b(var10, var15)) {
                  this.a(var1);
                  if (type >= 12 && type <= 14 && game.N.a(var10, var15) == 13) {
                     game.N.tiles[var10][var15] = 0;
                     LevelMap.columnSolidMasks[var10] = LevelMap.columnSolidMasks[var10] & ~(1 << var15);
                  }
               }
            }
         }
      } else {
         this.c();
      }
   }

   private void d() {
      if (this.w == -1) {
         int var4 = posX >> 8;
         int var5 = posY >> 8;

         for (int var1 = Game.MAX_ENEMIES - 1; var1 >= 0; var1--) {
            if (game.enemies[var1].kind != -1 && game.enemies[var1].animState != 5) {
               short var2 = game.enemies[var1].c();
               short var3 = game.enemies[var1].b();
               if ((var2 - var4) * (var2 - var4) + (var3 - var5) * (var3 - var5) <= 4356 && (vx > 0 ? var2 > var4 : var2 < var4)) {
                  this.w = (short)var1;
                  this.y = true;
                  break;
               }
            }
         }
      } else if (game.enemies[this.w].kind != -1 && game.enemies[this.w].animState != 5) {
         short var8 = game.enemies[this.w].c();
         int var9 = game.enemies[this.w].b() + Enemy.b[game.enemies[this.w].kind] + (Enemy.d[game.enemies[this.w].kind] >> 1);
         int var10 = posX >> 8;
         int var11 = posY >> 8;
         int var12 = var8 - var10;
         int var6 = var9 - var11;
         int var7 = SPEEDS[type] << 8;
         if (type < 18 || type > 20) {
            if (Math.abs(var12) <= 10 && this.y) {
               this.y = false;
               prev2X = prevX;
               prev2Y = prevY;
               prevX = posX;
               prevY = posY;
               if (Math.abs(var12) > Math.abs(var6)) {
                  Projectile var10000;
                  int var10001;
                  if (var12 > 0) {
                     var10000 = this;
                     var10001 = var7;
                  } else {
                     var10000 = this;
                     var10001 = -var7;
                  }

                  vx = var10001;
                  vy = 0;
               } else {
                  Projectile var13;
                  int var14;
                  if (var6 > 0) {
                     var13 = this;
                     var14 = var7;
                  } else {
                     var13 = this;
                     var14 = -var7;
                  }

                  vy = var14;
                  vx = 0;
               }

               age = 0;
            }
         } else if (var6 > 0) {
            vy = var7;
            if (var6 << 8 < vy) {
               vy = 0;
            }
         } else {
            vy = -var7;
            if (var6 << 8 > vy) {
               vy = 0;
            }
         }
      } else {
         this.w = -1;
      }

      age++;
      posX = posX + vx;
      posY = posY + vy;
   }

   public final void a(Graphics var1, boolean var2) {
      int var3 = Game.r;
      int var4 = Game.s;
      if (type >= 21 && type <= 30) {
         int var18 = (posX >> 8) + var3 - 11;
         int var19 = (posY >> 8) + var4 - (14 * (age == 1 ? 2 : 1) >> 1);
         int var20 = 14 * (age == 1 ? 2 : 1);
         int var26 = f[age] * 14;
         int var17 = 0;
         if (var19 < 20) {
            var17 = 20 - var19;
            var19 = 20;
         }

         if (var17 < var20 && var17 >= 0) {
            var1.setClip(var18, var19, 22, var20 - var17);
            var1.drawImage(Game.actorImage, var18, var19 - var26 - var17, 0);
            return;
         }
      } else {
         boolean var6 = false;
         boolean var7 = false;
         int var8;
         if ((var8 = RENDER_MODES[type] * 8) < 0) {
            int var21 = (posX >> 8) + var3;
            int var25 = (posY >> 8) + var4;
            if (type >= 9 && type <= 11) {
               var21 -= vx > 0 ? halfWidth : -halfWidth;
               var1.setClip(0, 20, 128, 108);
               Graphics var32;
               short var33;
               short var34;
               if (age == 0) {
                  var32 = var1;
                  var33 = 255;
                  var34 = 255;
               } else {
                  var32 = var1;
                  var33 = 0;
                  var34 = 0;
               }

               var32.setColor(var33, var34, 255);
               if (!var2) {
                  var25 += 4;
               }

               var1.drawLine(var21, var25, vx > 0 ? var21 + 110 : var21 - 110, var25 + 7);
               var1.drawLine(var21, var25, vx > 0 ? var21 + 110 : var21 - 110, var25 + 3);
               var1.drawLine(var21, var25, vx > 0 ? var21 + 110 : var21 - 110, var25 - 3);
               var1.drawLine(var21, var25, vx > 0 ? var21 + 110 : var21 - 110, var25 - 7);
               return;
            }

            if (type >= 12 && type <= 14) {
               int var27 = (prevX >> 8) + var3;
               int var29 = (prevY >> 8) + var4;
               int var13 = (prev2X >> 8) + var3;
               int var14 = (prev2Y >> 8) + var4;
               if (!var2) {
                  var25 += 4;
                  var29 += 4;
                  var14 += 4;
               }

               var1.setClip(0, 20, 128, 108);
               var1.setColor(255, 255, 0);
               int var16 = (spawnX >> 8) + var3;
               int var31;
               if (facingRight) {
                  var27 = Math.max(var27, var16 + 12);
                  var13 = Math.max(var13, var16 + 12);
                  var31 = Math.max(var21, var16 + 12);
               } else {
                  var27 = Math.min(var27, var16 - 12);
                  var13 = Math.min(var13, var16 - 12);
                  var31 = Math.min(var21, var16 - 12);
               }

               var21 = var31;
               var1.drawLine(var21, var25, var27, var29);
               var1.drawLine(var27, var29, var13, var14);
               var1.fillRect(var21 - 1, var25 - 1, 3, 3);
               return;
            }

            if (type >= 15 && type <= 17) {
               if (var2) {
                  if (vx > 0) {
                     var21 -= halfWidth;
                  } else {
                     var21 += halfWidth;
                  }
               } else {
                  var21 = game.player.b() + var3;
                  var25 += 4;
               }

               var1.setClip(0, 20, 128, 108);
               var1.setColor(0, 0, 255);
               if (vx > 0) {
                  var1.fillRoundRect(var21 + 8, var25 - 7, 110, 14, 12, 12);
                  var1.setColor(255, 255, 255);
                  var1.fillRect(var21 + 12, var25 - 7 + 6, 110, 2);
                  return;
               }

               var1.fillRoundRect(var21 - 8 - 110, var25 - 7, 110, 14, 12, 12);
               var1.setColor(255, 255, 255);
               var1.fillRect(var21 - 12 - 110, var25 - 7 + 6, 110, 2);
               return;
            }

            if (!var2) {
               var25 += 4;
            }

            var1.setClip(0, 20, 128, 108);
            var1.setColor(255, 255, 255);
            var1.fillRect(var21 - 3, var25 - 3, 6, 6);
            return;
         }

         int var9 = (posX >> 8) + var3 - 4;
         int var10 = (posY >> 8) + var4 - 4;
         if (!var2) {
            var10 += 4;
         }

         int var5 = 0;
         if (var10 < 20) {
            var5 = 20 - var10;
            var10 = 20;
         }

         if (var5 < 8 && var5 >= 0) {
            var1.setClip(var9, var10, 8, 8 - var5);
            if (vx <= 0 && RENDER_MODES[type] > 7 && RENDER_MODES[type] < 12) {
               DirectGraphics var10000;
               Image var10001;
               int var10002;
               int var10003;
               byte var10004;
               short var10005;
               if (vx == 0) {
                  if (vy < 0) {
                     var10000 = DirectUtils.getDirectGraphics(var1);
                     var10001 = Game.smallSpriteImage;
                     var10002 = var9 - var8;
                     var10003 = var10 - var5;
                     var10004 = 0;
                     var10005 = 90;
                  } else {
                     var10000 = DirectUtils.getDirectGraphics(var1);
                     var10001 = Game.smallSpriteImage;
                     var10002 = var9 - 128 + var8;
                     var10003 = var10 - var5;
                     var10004 = 0;
                     var10005 = 270;
                  }
               } else {
                  var10000 = DirectUtils.getDirectGraphics(var1);
                  var10001 = Game.smallSpriteImage;
                  var10002 = var9;
                  var10003 = var10 - var8 - var5;
                  var10004 = 0;
                  var10005 = 8192;
               }

               var10000.drawImage(var10001, var10002, var10003, var10004, var10005);
            } else {
               if (type != 31) {
                  var1.drawImage(Game.smallSpriteImage, var9, var10 - var8 - var5, 0);
                  return;
               }

               int var11 = 304 - ((posX >> 8) - 4);
               int var12 = 122 - ((posY >> 8) - 4);
               if (var11 * var11 + var12 * var12 >= 625 || Math.abs(var11) >= 22 || Math.abs(var12) >= 22) {
                  var1.drawImage(Game.smallSpriteImage, var9, var10 - var8 - var5, 0);
                  return;
               }
            }
         }
      }
   }
}
