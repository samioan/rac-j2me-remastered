import com.nokia.mid.ui.DirectUtils;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;

public final class Player extends Entity {
   public static final byte[] a = new byte[]{-1, 0, -1, 0, -1, 44, 44, 44, 44, -2, 0, 0, 44, 44, 44};
   public static final byte[] b = new byte[]{9, 8, 8, 6, 7, 44, 44, 44, 44, 9, 8, 8, 44, 44, 44};
   public static final byte[] c = new byte[]{
      42,
      51,
      18,
      32,
      51,
      0,
      45,
      0,
      0,
      12,
      15,
      30,
      24,
      36,
      0,
      22,
      22,
      22,
      22,
      42,
      0,
      0,
      42,
      22,
      42,
      0,
      0,
      42,
      42,
      60,
      9,
      13,
      14,
      10,
      11,
      15,
      22,
      12,
      18,
      12,
      15,
      15,
      12,
      15,
      20,
      18,
      18,
      19,
      19,
      15,
      22,
      21,
      18,
      20,
      17,
      22,
      22,
      17,
      17,
      5,
      7,
      4,
      3,
      6,
      7,
      0,
      0,
      3,
      3,
      5,
      1,
      1,
      5,
      4,
      1,
      4,
      4,
      3,
      2,
      3,
      0,
      1,
      2,
      0,
      5,
      0,
      0,
      5,
      5,
      17
   };
   public static final short[] d = new short[]{0, 0, 0, 100, 150, 200, 35, 45, 60, 25, 30, 40, 40, 55, 70, 200, 250, 300, 1, 1, 1, 70, 90, 110};
   public static final short[] e = new short[]{0, 50, 20, 15, 20, 100, 1, 30};
   public static final short[] f = new short[]{0, 15, 7, 5, 8, 30, 1, 10};
   public static final byte[][] g = new byte[][]{{0, 4, 16, 20, 16, 5, 4, 5}, {0, 4, 16, 20, 16, 5, 4, 5}, {0, 4, 16, 20, 16, 5, 4, 5}};
   public static final short[] h = new short[]{0, 0, 250, 400, 700, 950, 1650, 0};
   public static final byte[] i = new byte[]{3, 6, 6};
   private Game game;
   public static short j;
   public static short k;
   public static final byte[] l = new byte[]{4, -7};
   public static final byte[] m = new byte[]{3, 10};
   public static final byte[] n = new byte[]{5, 8};
   public static final byte[] o = new byte[]{16, 13};
   public byte jumpPhase;
   public byte q;
   public byte r;
   public byte s;
   public int swingTargetX;
   public int swingTargetY;
   public int swingCurX;
   public int swingCurY;
   public int swingStepX;
   public int swingStepY;
   public byte attackTimer;
   public byte weaponPose;
   public byte invulnTimer;
   public byte swingTargetType;
   public boolean onLadder;
   public short platformUnder;
   public boolean meleeActive;
   public static byte[][][] ANIM_FRAMES;
   public static byte[][] ANIM_FRAME_COUNTS;
   public static byte[][] ANIM_FRAME_EXTRA;
   public byte currentWeapon;
   public short[] ammo;
   public short[] weaponXp;
   public byte ownedWeapons;
   public byte[] weaponLevel;
   public int swingVelX;
   public int swingVelY;
   public int swingEndX;

   public Player(Game var1) {
      this.game = var1;
      k = 1280;
      jumpPhase = -1;
      this.r = -2;
      attackTimer = -1;
      weaponPose = 0;
      onLadder = false;
      this.q = 0;
      platformUnder = 0;
      ammo = new short[8];
      weaponXp = new short[8];
      weaponLevel = new byte[8];
      super.velY = 0;
      super.velX = 0;
      super.subState = 0;
      super.facingRight = true;
      super.health = 20;
      j = 1792;
   }

   public final void a() {
      if (ANIM_FRAMES == null) {
         ANIM_FRAMES = new byte[1][15][4];
      }

      if (ANIM_FRAME_COUNTS == null) {
         ANIM_FRAME_COUNTS = new byte[1][15];
      }

      if (ANIM_FRAME_EXTRA == null) {
         ANIM_FRAME_EXTRA = new byte[1][15];
      }

      a("/r");
   }

   public final short b() {
      return (short)(super.posX >> 8);
   }

   public final short c() {
      return (short)(super.row * 14 + (super.posInRow >> 8));
   }

   public final byte d() {
      return (byte)(((super.posX >> 8) + 4) / 22);
   }

   public final byte e() {
      return (byte)(((super.posX >> 8) - 4) / 22);
   }

   public final short a(boolean var1) {
      int var2 = 238;
      byte var3 = 1;
      if (var1) {
         var3 = 5;
      }

      label28: {
         int var10000;
         if (super.posInRow > var3 << 8) {
            if ((LevelMap.columnSolidMasks[this.d()] & 1 << super.row + 2) <= 0 && (LevelMap.columnSolidMasks[this.e()] & 1 << super.row + 2) <= 0) {
               break label28;
            }

            var10000 = super.row + 1;
         } else {
            if ((LevelMap.columnSolidMasks[this.d()] & 1 << super.row + 1) <= 0 && (LevelMap.columnSolidMasks[this.e()] & 1 << super.row + 1) <= 0) {
               break label28;
            }

            var10000 = super.row;
         }

         var2 = var10000 * 14;
      }

      short var4 = this.game.f();
      short var5 = (short)Game.d(var2, var4);
      short var6 = this.game.e();
      onLadder = false;
      if (var6 < var5) {
         onLadder = true;
         return var6;
      } else {
         return var5;
      }
   }

   public final boolean f() {
      int var1;
      int var2 = (var1 = super.posX >> 8) / 22;
      int var3;
      if ((var3 = var1 % 22) < 5) {
         if (this.game.N.tiles[var2 - 1][super.row + 1] != 4) {
            return false;
         }
      } else if (var3 > 17 && this.game.N.tiles[var2 + 1][super.row + 1] != 4) {
         return false;
      }

      return this.game.N.tiles[var2][super.row + 1] == 4;
   }

   public final boolean g() {
      return !this.game.N.b(this.column() - 1, super.row + 1) && ((super.posX >> 8) - 4) % 22 < 4;
   }

   public final boolean h() {
      return !this.game.N.b(this.column() + 1, super.row + 1) && ((super.posX >> 8) + 4) % 22 > 18;
   }

   private void r() {
      if (this.game.bh == -1) {
         this.setAnimState((byte)2);
         this.game.bh = super.facingRight ? -4 : -3;
         super.velY = 2560;
         super.velX = 0;
         jumpPhase = 0;
         this.game.i();
      } else {
         super.posX = super.posX + swingVelX;
         super.posInRow = super.posInRow + swingVelY;
         this.wrapRow();
         if (this.c() + 22 > swingEndX) {
            this.setAnimState((byte)3);
            super.velX = 0;
            this.game.bi = this.game.bh = 0;
         }
      }
   }

   private void s() {
      if (this.r == 0) {
         short var8 = this.a(true);
         super.velY = (short)(super.velY - 128);
         super.posInRow = super.posInRow - super.velY;
         this.wrapRow();
         if (this.c() >= var8) {
            super.row = (byte)(var8 / 14);
            super.posInRow = (short)(var8 % 14);
         }

         swingCurX = swingCurX + swingStepX;
         swingCurY = swingCurY + swingStepY;
         if (swingCurY < swingTargetY || Math.abs(swingCurX - swingTargetX) < 1280 && Math.abs(swingCurY - swingTargetY) < 1280) {
            label80: {
               Player var19;
               byte var21;
               if (swingTargetType == 97) {
                  var19 = this;
                  var21 = 1;
               } else {
                  this.r = 3;
                  if (jumpPhase != -1) {
                     break label80;
                  }

                  super.velY = 420;
                  var19 = this;
                  var21 = 2;
               }

               var19.r = var21;
            }

            byte var11 = 4;
            if (!super.facingRight) {
               var11 = -4;
            }

            boolean var14 = false;
            int var15 = (super.posX >> 8) + var11 << 8;
            int var16 = super.row * 14 + (super.posInRow >> 8) + 1 << 8;
            swingStepX = (swingTargetX - var15) / 10;
            swingStepY = (swingTargetY - var16) / 10;
            return;
         }
      } else {
         if (this.r == 1) {
            super.animFrame = 1;
            super.posX = super.posX + swingStepX;
            super.posInRow = super.posInRow + swingStepY;
            this.wrapRow();
            super.velY = 0;
            if (this.c() << 8 >= swingTargetY && (Math.abs((this.b() << 8) - swingTargetX) >= 1280 || Math.abs((this.c() << 8) - swingTargetY) >= 1280)) {
               return;
            }

            this.setAnimState((byte)3);
            this.r = -2;
            this.s = 0;
            Game.u = false;
         } else {
            if (this.r == 2) {
               super.animFrame = 1;
               super.velY = (short)(super.velY - 128);
               super.posInRow = super.posInRow - super.velY;
               this.wrapRow();
               if (super.velY < 0) {
                  this.r = 3;
                  return;
               }

               return;
            }

            if (this.r == 3) {
               int var7 = (swingTargetY >> 8) - this.c();
               int var10 = (swingTargetX >> 8) - this.b();
               boolean var13 = false;
               this.r = 5;
               super.velX = 0;
               super.velY = 0;
               if (var10 * var10 + var7 * var7 > 1530) {
                  super.velX = (short)(var10 << 4);
                  super.velY = (short)(var7 << 4);
                  this.r = 4;
                  return;
               }

               return;
            }

            if (this.r == 4) {
               int var6 = (swingTargetY >> 8) - this.c();
               int var9 = (swingTargetX >> 8) - this.b();
               boolean var12 = false;
               if (var9 * var9 + var6 * var6 > 1530) {
                  super.posX = super.posX + super.velX;
                  super.posInRow = super.posInRow + super.velY;
                  this.wrapRow();
                  return;
               }

               this.r = 5;
               super.velY = 0;
               super.velX = 0;
               return;
            }

            if (this.r != 5) {
               return;
            }

            super.animFrame = 1;
            short var1 = 0;
            short var2 = 0;
            int var3 = this.c() - (swingTargetY >> 8) + 1;
            int var10000;
            int var10001;
            if (super.facingRight) {
               var10000 = swingTargetX >> 8;
               var10001 = this.b() + 9;
            } else {
               var10000 = this.b() - 9;
               var10001 = swingTargetX >> 8;
            }

            byte var5;
            label132: {
               int var4;
               short var17;
               if ((var5 = (byte)((var4 = var10000 - var10001) == 0 ? 2000 : (var3 << 8) / var4)) < -1945 || var5 > 1945) {
                  var1 = 2880;
                  var17 = 0;
               } else if (var5 > 616) {
                  var1 = 2781;
                  var17 = 447;
               } else if (var5 > 333) {
                  var1 = 2494;
                  var17 = 864;
               } else if (var5 > -10) {
                  var1 = 2036;
                  var17 = 1222;
               } else if (var5 > -334) {
                  var1 = 2036;
                  var17 = -1222;
               } else if (var5 > -617) {
                  var1 = 2494;
                  var17 = -864;
               } else {
                  if (var5 <= -1946) {
                     break label132;
                  }

                  var1 = 2781;
                  var17 = -447;
               }

               var2 = var17;
            }

            Player var18;
            if (super.facingRight) {
               var18 = this;
               var10001 = super.posX + var1;
            } else {
               var18 = this;
               var10001 = super.posX - var1;
            }

            var18.posX = var10001;
            super.posInRow += var2;
            super.velY = 447;
            this.wrapRow();
            if (var5 >= 0 || var5 <= -150) {
               return;
            }

            this.setAnimState((byte)2);
            super.animRestart = 0;
            this.r = -1;
            this.s = 15;
         }

         this.game.bi = this.game.bh = 0;
      }
   }

   public final void i() {
      this.setAnimState((byte)2);
      jumpPhase = 0;
      super.subState = 0;
   }

   public final void j() {
      if (super.velY <= -j) {
         super.velY = (short)(-j);
      }

      if (super.animState == 10 && super.animRestart == 2) {
         this.game.d();
         if (this.game.by) {
            this.game.by = false;
         }

         Game.bA++;
      } else if (super.animState == 10) {
         this.game.bi = this.game.bh = 0;
         this.game.bj &= -129;
      } else if (super.health <= 0 && super.animState != 10) {
         if (Game.a.soundEnabled && Game.a.soundPlayer != null) {
            Game.a.soundPlayer.queue(3, 1);
         }

         super.health = 0;
         super.animRestart = 1;
         this.setAnimState((byte)10);
         Game.bA++;
      } else if (this.r >= 0) {
         this.s();
         this.v();
      } else if (super.animState == 6) {
         this.r();
         this.t();
         this.v();
      } else {
         if (super.animState == 11 && !meleeActive) {
            this.game.bi = this.game.bh = 0;
            super.velX = 0;
            weaponPose = 1;
            this.n();
         } else if (super.animState == 9) {
            if (++super.animHold > 6) {
               if (jumpPhase == -1) {
                  this.setAnimState((byte)0);
               } else {
                  super.velX = 0;
                  super.velY = 0;
                  this.setAnimState((byte)3);
                  jumpPhase = 0;
               }
            } else {
               this.game.bh = 0;
            }
         }

         if (this.game.bh == -4 || this.game.bi == -4 || super.animState == 8 && super.facingRight) {
            if (super.animState == 4) {
               this.game.bh = 0;
            }

            if (!super.facingRight || super.animState != 1) {
               if (jumpPhase == 2) {
                  k = 512;
               } else if (super.animState == 8) {
                  k = 256;
               } else if (jumpPhase > -1) {
                  k = 1280;
               } else {
                  this.setAnimState((byte)1);
                  k = 1280;
                  super.animRestart = 0;
               }

               super.velX = k;
               super.facingRight = true;
            }
         } else if (this.game.bh == -3 || this.game.bi == -3 || super.animState == 8 && !super.facingRight) {
            if (super.animState == 4) {
               this.game.bh = 0;
            }

            if (super.facingRight || super.animState != 1) {
               if (jumpPhase == 2) {
                  k = 512;
               } else if (super.animState == 8) {
                  k = 256;
               } else if (jumpPhase > -1) {
                  k = 1280;
               } else {
                  this.setAnimState((byte)1);
                  k = 1280;
                  super.animRestart = 0;
               }

               super.velX = (short)(-k);
               super.facingRight = false;
            }
         } else if (this.game.bh == 0) {
            if (jumpPhase == 2) {
               super.velX = (short)(super.facingRight ? 512 : -512);
            } else {
               label520: {
                  if (jumpPhase >= 0) {
                     label447: {
                        Player var10000;
                        int var10001;
                        if (super.velX > 0) {
                           var10000 = this;
                           var10001 = super.velX - 98;
                        } else {
                           if (super.velX >= 0) {
                              break label447;
                           }

                           var10000 = this;
                           var10001 = super.velX + 98;
                        }

                        var10000.velX = (short)var10001;
                     }

                     if (super.velX > 98 || super.velX < -98) {
                        break label520;
                     }
                  }

                  super.velX = 0;
               }
            }

            if (super.animState == 1) {
               this.setAnimState((byte)0);
            }
         }

         if (this.game.bh == -1) {
            label434: {
               if (jumpPhase == -1) {
                  super.velY = 2560;
               } else {
                  if (jumpPhase != 0) {
                     break label434;
                  }

                  if (Game.t) {
                     super.velY = 2560;
                  } else {
                     super.velY = 1920;
                     if (super.animState != 8 && super.animState != 11) {
                        super.animRestart = 1;
                        this.setAnimState((byte)13);
                     }
                  }
               }

               jumpPhase++;
            }

            this.game.i();
         }

         if (super.animState == 4 && jumpPhase >= 0) {
            if (super.animCounter > 2) {
               this.setAnimState((byte)2);
            }

            super.animRestart = 0;
         } else {
            label427:
            if (super.velX != 0) {
               super.fieldW = super.facingRight ? this.d() : this.e();
               Player var10;
               int var15;
               if (super.posInRow < k) {
                  var10 = this;
                  var15 = super.row;
               } else {
                  var10 = this;
                  var15 = (byte)(super.row + 1);
               }

               var10.spawnRow = (byte)var15;
               if (super.fieldW >= 0 && super.fieldW <= 27 && this.game.N.b(super.fieldW, super.spawnRow) && (super.row < 17 || super.row == 17 && super.posInRow == 0)) {
                  super.posX = super.posX + super.velX;
                  super.fieldW = super.facingRight ? this.d() : this.e();
                  if (this.game.h() == -1 && !this.game.g() && this.game.N.b(super.fieldW, super.spawnRow)) {
                     break label427;
                  }

                  var10 = this;
                  var15 = super.posX - super.velX;
               } else {
                  super.velX = 0;
                  if (super.facingRight) {
                     if (this.d() != super.fieldW) {
                        break label427;
                     }

                     var10 = this;
                     var15 = super.fieldW * 22 - 4 - 1;
                  } else {
                     if (this.e() != super.fieldW) {
                        break label427;
                     }

                     var10 = this;
                     var15 = super.fieldW * 22 + 22 + 4 + 1;
                  }

                  var15 <<= 8;
               }

               var10.posX = var15;
            }

            if (attackTimer >= 0 && super.animState != 11) {
               this.u();
            }

            short var1;
            if ((var1 = this.a(false)) - this.c() > 7) {
               onLadder = false;
            }

            if (onLadder && jumpPhase == -1) {
               if (super.animState == 3) {
                  super.animState = 0;
               }

               super.row = (byte)((var1 + 1) / 14);
               super.posInRow = (short)((var1 + 1) % 14 << 8);
               super.posX = super.posX + (platformUnder << 8);
               super.velY = 0;
               if (!this.game.N.b(this.e(), super.row)) {
                  super.posX += 256;
               } else if (!this.game.N.b(this.d(), super.row)) {
                  super.posX -= 256;
               }
            } else {
               if ((jumpPhase >= 0 || this.c() < var1) && super.subState == 0) {
                  Player var12;
                  short var18;
                  if (jumpPhase == 2) {
                     var12 = this;
                     var18 = -768;
                  } else {
                     var12 = this;
                     var18 = (short)(super.velY - 384);
                  }

                  var12.velY = var18;
                  super.posInRow = super.posInRow - super.velY;
                  if (super.velY > 0 && (!this.game.N.b(this.d(), super.row) || !this.game.N.b(this.e(), super.row))) {
                     super.posInRow = super.posInRow + super.velY;
                     super.velY = 0;
                  }

                  if (super.posInRow < 0) {
                     super.posInRow += 3584;
                     super.row--;
                     if (super.row < 0) {
                        super.row = 0;
                        super.posInRow = 0;
                     }

                     if (super.row == 0) {
                     }
                  } else if (super.posInRow > 3584) {
                     super.posInRow -= 3584;
                     super.row++;
                  }

                  if (jumpPhase == -1) {
                     jumpPhase = 0;
                  }

                  if (super.animState != 11 && super.animState != 14) {
                     if (jumpPhase == 0 && super.animState != 8) {
                        this.setAnimState((byte)2);
                     }

                     label543: {
                        boolean var19;
                        if (this.game.bh != -3 && super.facingRight) {
                           if (this.game.bh != -4 && !super.facingRight) {
                              break label543;
                           }

                           var12 = this;
                           var19 = true;
                        } else {
                           var12 = this;
                           var19 = false;
                        }

                        var12.facingRight = var19;
                     }

                     if (super.velY <= 0 && jumpPhase == 1 && !Game.t && super.animState == 12) {
                        jumpPhase = 2;
                     } else if (super.velY <= -1024 && jumpPhase != 2) {
                        this.setAnimState((byte)3);
                     }
                  }
               }

               if (super.subState == 0) {
                  if (super.animState != 11 && super.velY < 0 && super.animState != 10) {
                     for (int var2 = 0; var2 < Game.MAX_ENEMIES; var2++) {
                        if (this.game.enemies[var2].animState != 2 && this.game.d(var2) && this.game.enemies[var2].b() > this.c()) {
                           super.velY = 0;
                           super.velX = 0;
                           this.setAnimState((byte)3);
                           jumpPhase = 0;
                           Player var14;
                           int var20;
                           if (this.b() > this.game.enemies[var2].c()) {
                              this.game.enemies[var2].bounceTimer = -10;
                              if (this.game.N.b(this.d(), super.row)) {
                                 super.posX += 510;
                              }

                              if (this.game.N.b(this.d(), super.row)) {
                                 continue;
                              }

                              var14 = this;
                              var20 = super.posX - 510;
                           } else {
                              this.game.enemies[var2].bounceTimer = 10;
                              if (this.game.N.b(this.e(), super.row)) {
                                 super.posX -= 510;
                              }

                              if (this.game.N.b(this.e(), super.row)) {
                                 continue;
                              }

                              var14 = this;
                              var20 = super.posX + 510;
                           }

                           var14.posX = var20;
                        }
                     }
                  }

                  if (this.c() >= var1 && super.velY < 0) {
                     super.row = (byte)(var1 / 14);
                     super.posInRow = (short)(var1 % 14 << 8);
                     super.velY = 0;
                     jumpPhase = -1;
                     if (super.animState == 3 || super.animState == 2 || super.animState >= 11 && super.animState <= 14) {
                        if (jumpPhase == 2) {
                           jumpPhase = 1;
                        }

                        this.setAnimState((byte)4);
                        super.animRestart = 1;
                     }
                  }
               }
            }

            if (super.animState != 10) {
               this.v();
               if (this.q == 0) {
                  int var8 = super.posX >> 8;
                  if (jumpPhase >= 0 && super.velY < 0) {
                     for (int var3 = 3; var3 >= 0; var3--) {
                        boolean var4;
                        int var5;
                        if (this.game.zipX1[var3] != -1
                           && (
                              (var4 = this.game.zipX2[var3] - this.game.zipX1[var3] > 0) && var8 > this.game.zipX1[var3] && var8 < this.game.zipX2[var3]
                                 || !var4 && var8 < this.game.zipX1[var3] && var8 > this.game.zipX2[var3]
                           )
                           && Math.abs((var5 = this.game.zipDy[var3] * var8 / this.game.zipDx[var3] + this.game.zipBaseY[var3]) - (this.c() + 11 << 8)) < -super.velY) {
                           var5 -= 5632;
                           super.row = (byte)((var5 >> 8) / 14);
                           super.posInRow = (short)((var5 >> 8) % 14 << 8);
                           jumpPhase = -1;
                           this.setAnimState((byte)6);
                           super.velX = super.velY = 0;
                           super.facingRight = var4;
                           int var7 = (Math.abs(this.game.zipX2[var3] - this.game.zipX1[var3]) << 8) / 1920;
                           swingVelX = (this.game.zipDx[var3] << 8) / var7;
                           swingVelY = this.game.zipDy[var3] / var7;
                           swingEndX = this.game.zipY2[var3];
                           break;
                        }
                     }
                  }
               } else {
                  this.q--;
               }

               this.t();
            }
         }
      }
   }

   private void t() {
      byte var1 = this.column();
      byte var2 = (byte)((this.c() + 22) / 14);
      if (this.s > 0) {
         this.game.e = true;
         Game.u = true;
         if (--this.s <= 0) {
            this.r = -2;
            this.s = 0;
            Game.u = false;
         }
      }

      short var3 = this.game.N.a(var1, var2);
      Game.v = false;
      if (var3 >= 56 && var3 <= 61) {
         if (this.game.Q != 10 || var1 != 17 || var2 != 12 || var3 != 58) {
            this.r = -1;
            this.s = 10;
         }
      } else if (var3 == 8) {
         super.velY = 2560;
         jumpPhase = 0;
         super.animRestart = 1;
         this.setAnimState((byte)2);
         this.game.d(super.posX, this.c() << 8, 30);
         if (!Game.i) {
            super.health--;
         }

         this.game.e = true;
      } else if (var3 != -125 && var3 != -122) {
         int var10000 = --var2 + 2;

         byte var4;
         while ((var4 = (byte)var10000) >= var2) {
            if ((var3 = this.game.N.a(var1, var4)) == 99 || var3 == 100) {
               this.game.V = this.game.P;
               this.game.R = var1;
               this.game.S = var2;
               Game var7;
               short var10001;
               if (var1 < 1) {
                  var7 = this.game;
                  var10001 = 0;
               } else {
                  var7 = this.game;
                  var10001 = (short)(-22 * (var1 - 1));
               }

               var7.T = var10001;
               if (var2 < 6) {
                  this.game.U = 0;
               } else {
                  this.game.U = (short)(-14 * (var2 - 5));
               }
               break;
            }

            if (var3 >= 77 && var3 <= 96 && this.game.c(var3 - 77)) {
               this.game.messageTimer = Game.bk[var3 - 77];
               this.game.e(Game.bl[var3 - 77], this.game.Q == 3 ? 11 : (this.game.Q == 10 ? 9 : -1));
               this.game.b(var3 - 77);
               return;
            }

            var10000 = var4 - 1;
         }
      } else {
         Game.v = true;
         Game.x = false;
         Game.w = -1;
         if (this.game.scoringActive) {
            this.game.j();
         }
      }

      if (super.row >= 17 && this.game.bz) {
         this.game.by = true;
         this.setAnimState((byte)10);
         super.animRestart = 1;
      }
   }

   public final void k() {
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
                  return;
               }

               super.animFrame--;
               super.animRestart = 2;
               this.o();
            }
         }
      }
   }

   public final void a(Graphics var1) {
      int var2 = (super.posX >> 8) - 11 + Game.r + 0;
      int var3 = super.row * 14 + (super.posInRow >> 8) + Game.s + -6;
      if (super.animState == 6) {
         var3 += 7;
      }

      if (this.r >= 0) {
         byte var9 = 13;
         if (!super.facingRight) {
            var9 = 9;
         }

         boolean var10 = false;
         int var11 = var2 + var9 - 4 << 8;
         int var12 = var3 + 3 - 4 << 8;
         int var13 = swingCurX + (Game.r << 8);
         int var14 = swingCurY + (Game.s << 8);
         if (this.r == 0) {
            var12 += 2304;
         }

         int var10000;
         short var10001;
         if (super.facingRight) {
            var10000 = var13;
            var10001 = 512;
         } else {
            var10000 = var13;
            var10001 = 1024;
         }

         int var15 = (var10000 - var10001 - var11) / 9;
         int var16 = (var14 - var12) / 9;

         for (int var17 = 0; var17 < 10; var17++) {
            int var4 = 0;
            int var7 = var12;
            if (var12 >> 8 < 20) {
               var4 = 20 - (var7 >> 8);
               var7 = 5120;
            }

            if (var4 < 8 && var4 >= 0) {
               var1.setClip(var11 >> 8, var7 >> 8, 8, 8 - var4);
               var1.drawImage(Game.smallSpriteImage, var11 >> 8, (var7 >> 8) - 48 - var4, 0);
            }

            var11 += var15;
            var12 += var16;
         }

         int var18 = 0;
         int var31 = var11 - var15 >> 8;
         var18 = var12 - var16 >> 8;
         int var22 = 0;
         int var25;
         if ((var25 = var18 - 4) < 20) {
            var22 = 20 - var25;
            var25 = 20;
         }

         if (var22 < 8 && var22 >= 0) {
            var1.setClip(var31, var25, 8, 8 - var22);
            if (super.facingRight) {
               var1.drawImage(Game.smallSpriteImage, var31, var25 - 40 - var22, 0);
            } else {
               DirectUtils.getDirectGraphics(var1).drawImage(Game.smallSpriteImage, var31, var25 - 40 - var22, 0, 8192);
            }
         }
      }

      if (var3 + 22 > 20 && var3 < 128 && invulnTimer % 2 == 0) {
         int var26 = var3;
         int var23 = 0;
         if (var26 < 20) {
            var23 = 20 - var26;
            var26 = 20;
         }

         byte var27 = ANIM_FRAMES[super.kind][super.animState][super.animFrame];
         var1.setClip(var2 + (c[60 + var27] & 255), var26 + (c[75 + var27] & 255), c[30 + var27] & 255, (c[45 + var27] & 255) - var23);
         if (super.subState == 0 && super.facingRight) {
            var1.drawImage(Game.playerImage, var2 + (c[60 + var27] & 255) - (c[0 + var27] & 255), var3 + (c[75 + var27] & 255) - (c[15 + var27] & 255), 20);
         } else {
            this.game
               .a(
                  var1,
                  var2 + (c[60 + var27] & 255) - (Game.playerImage.getWidth() - (c[0 + var27] & 255) - (c[30 + var27] & 255)),
                  var3 + (c[75 + var27] & 255) - (c[15 + var27] & 255)
               );
         }
      }

      if (currentWeapon != 0) {
         byte var28 = a[ANIM_FRAMES[super.kind][super.animState][super.animFrame]];
         byte var29 = b[ANIM_FRAMES[super.kind][super.animState][super.animFrame]];
         if (super.animState == 6) {
            var29 += 7;
         }

         if (var28 != 44 && var29 != 44) {
            var2 = (super.posX >> 8) + Game.r + 0 - var28 - 12;
            if (super.facingRight) {
               var2 = (super.posX >> 8) + Game.r + 0 + var28;
            }

            var3 = super.row * 14 + (super.posInRow >> 8) + Game.s + -6 + var29;
            int var24 = 0;
            if (var3 < 20) {
               var24 = 20 - var3;
               var3 = 20;
            }

            if (var24 < 12 && var24 >= 0) {
               int var30 = currentWeapon - 1;
               var1.setClip(var2, var3, 12, 12 - var24);
               if (super.facingRight) {
                  var1.drawImage(Game.weaponImage, var2, var3 - var30 * 12 - var24, 0);
                  return;
               }

               DirectUtils.getDirectGraphics(var1).drawImage(Game.weaponImage, var2, var3 - var30 * 12 - var24, 0, 8192);
            }
         }
      }
   }

   public final void l() {
      boolean var1 = true;
      if (super.facingRight) {
         int var2 = this.d();
         if ((super.posX >> 8) % 22 > 11) {
            var2++;
         }

         for (int var3 = var2; var3 < var2 + 3 && var1; var3++) {
            for (int var4 = super.row; var4 >= super.row - 3 && var1; var4--) {
               short var5;
               if (var3 >= 0 && var4 >= 0 && ((var5 = this.game.N.a(var3, var4)) == 97 || var5 == 98)) {
                  this.game.a(var3, var4, var5);
                  var1 = false;
               }
            }
         }
      } else {
         int var6 = this.e();
         if ((super.posX >> 8) % 22 < 11) {
            var6--;
         }

         for (int var7 = var6; var7 > var6 - 3 && var1; var7--) {
            for (int var8 = super.row; var8 >= super.row - 3 && var1; var8--) {
               short var9;
               if (var7 >= 0 && var8 >= 0 && ((var9 = this.game.N.a(var7, var8)) == 97 || var9 == 98)) {
                  this.game.a(var7, var8, var9);
                  var1 = false;
               }
            }
         }
      }
   }

   public final boolean m() {
      byte var1 = weaponLevel[currentWeapon];
      if (ammo[currentWeapon] <= 0) {
         return false;
      }

      if (super.animState != 6 && super.animState != 7 && super.animState != 12 && super.animState != 13) {
         this.setAnimState((byte)7);
         super.animRestart = 0;
      }

      Game var10000;
      int var10001;
      int var10002;
      byte var10003;
      switch (currentWeapon) {
         case 0:
         default:
            return true;
         case 1:
            var10000 = this.game;
            var10001 = super.posX + (super.facingRight ? 2816 : -2816);
            var10002 = (this.c() << 8) + 1024;
            var10003 = 0;
            break;
         case 2:
            var10000 = this.game;
            var10001 = super.posX + (super.facingRight ? 256 : -256);
            var10002 = (this.c() << 8) + 1024;
            var10003 = 3;
            break;
         case 3:
            var10000 = this.game;
            var10001 = super.posX + (super.facingRight ? 256 : -256);
            var10002 = (this.c() << 8) + 1024;
            var10003 = 6;
            break;
         case 4:
            this.game.d(super.posX + (super.facingRight ? 2816 : -2816), (this.c() << 8) + 1024, 9 + var1);
            if (ammo[currentWeapon] <= 0 || var1 != 2) {
               return true;
            }

            var10000 = this.game;
            var10001 = super.posX + (super.facingRight ? 2816 : -2816);
            var10002 = (this.c() << 8) + 1024;
            var10003 = 9;
            break;
         case 5:
            var10000 = this.game;
            var10001 = super.posX + (super.facingRight ? 256 : -256);
            var10002 = (this.c() << 8) + 1024 + ammo[currentWeapon] % 2 * 1024;
            var10003 = 12;
            break;
         case 6:
            var10000 = this.game;
            var10001 = super.posX + (super.facingRight ? 256 : -256);
            var10002 = (this.c() << 8) + 1024;
            var10003 = 15;
            break;
         case 7:
            var10000 = this.game;
            var10001 = super.posX + (super.facingRight ? 256 : -256);
            var10002 = (this.c() << 8) + 1024;
            var10003 = 18;
      }

      var10000.d(var10001, var10002, var10003 + var1);
      return true;
   }

   public final void n() {
      if (super.animState < 4 || super.animState == 11) {
         this.game.e(-1);
         if (super.animState != 11) {
            super.animRestart = 1;
            this.setAnimState((byte)8);
         }

         byte var3 = 0;
         byte var4 = 0;

         for (int var6 = Game.MAX_ENEMIES - 1; var6 >= 0; var6--) {
            byte var5 = this.game.enemies[var6].kind;
            if (this.game.enemies[var6].kind != -1 && this.game.enemies[var6].animState != 5) {
               int var1 = this.game.enemies[var6].c() - Enemy.a[var5];
               int var2 = this.game.enemies[var6].b() + Enemy.b[var5];
               var3 = Enemy.c[var5];
               var4 = Enemy.d[var5];
               if (Math.abs(var1 - this.b()) <= 33
                  && Math.abs(var2 - this.c()) <= 14
                  && this.game
                     .a(var1, var2, var3, var4, (super.posX >> 8) + (super.facingRight ? l[weaponPose] : -l[weaponPose] - n[weaponPose]), this.c() + m[weaponPose], n[weaponPose], o[weaponPose])) {
                  meleeActive = true;
                  Enemy var10000;
                  byte var10001;
                  byte[] var10002;
                  byte[] var10003;
                  byte var10004;
                  if (super.animState == 11) {
                     var10000 = this.game.enemies[var6];
                     var10001 = this.game.enemies[var6].health;
                     var10002 = i;
                     var10003 = weaponLevel;
                     var10004 = 1;
                  } else {
                     var10000 = this.game.enemies[var6];
                     var10001 = this.game.enemies[var6].health;
                     var10002 = i;
                     var10003 = weaponLevel;
                     var10004 = 0;
                  }

                  var10000.health = (byte)(var10001 - var10002[var10003[var10004]]);
                  if (this.game.enemies[var6].posX > super.posX) {
                     var10000 = this.game.enemies[var6];
                     var10001 = 10;
                  } else {
                     var10000 = this.game.enemies[var6];
                     var10001 = -10;
                  }

                  swingTargetY = var10001;
                  if (this.game.enemies[var6].health <= 0) {
                     if (Game.a.soundEnabled && Game.a.soundPlayer != null) {
                        Game.a.soundPlayer.queue(3, 1);
                     }

                     this.game.enemies[var6].setAnimState((byte)5);
                     this.game.enemies[var6].animRestart = 1;
                     if (var5 != 4) {
                        Game.enemyKills++;
                        Game.totalKills++;
                        if (this.game.Q != 0) {
                           for (int var7 = 0; var7 < Enemy.BOLTS_DROPPED_BY_ANIM[this.game.enemies[var6].animKind]; var7++) {
                              this.game.b(this.game.enemies[var6].c(), this.game.enemies[var6].b(), 0);
                           }
                        }
                     }
                  } else {
                     this.game.enemies[var6].setAnimState((byte)4);
                     this.game.enemies[var6].animHold = 0;
                  }
               }
            }
         }

         if (this.game.Q == 12) {
            this.game.a((super.posX >> 8) + (super.facingRight ? l[weaponPose] : -l[weaponPose] - n[weaponPose]), this.c() + m[weaponPose], n[weaponPose], o[weaponPose]);
         }

         if (jumpPhase == -1) {
            this.game.bh = 0;
         }
      }
   }

   public final void o() {
      weaponPose = 0;
      Player var10000;
      byte var10001;
      if (super.animState == 7 || super.animState == 8 || super.animState == 9 || super.animState == 0 || super.animState == 4) {
         super.animRestart = 0;
         var10000 = this;
         var10001 = 0;
      } else if (super.animState == 13) {
         super.animRestart = 0;
         var10000 = this;
         var10001 = 12;
      } else {
         if (super.animState != 14) {
            return;
         }

         super.animRestart = 0;
         var10000 = this;
         var10001 = 11;
      }

      var10000.a(var10001);
   }

   private void u() {
      if (++attackTimer > 15) {
         attackTimer = -1;
         weaponPose = 0;
      }
   }

   private void v() {
      int var1 = this.c() + 3 + 20;

      for (int var2 = 5; var2 >= 0; var2--) {
         byte var3 = this.game.pickupType[var2];
         int var4 = this.game.pickupX[var2];
         short var5 = this.game.pickupVx[var2];
         if (var3 != -1 && (super.health != 20 || var3 != 2) && (var5 != 0 || Math.abs(var4 - super.posX) <= 14080 && Math.abs((this.game.pickupY[var2] >> 8) - var1) <= 22)) {
            label61: {
               int var10000;
               if (var4 > super.posX) {
                  if (var5 <= -1536) {
                     break label61;
                  }

                  var10000 = var5 - 128;
               } else {
                  if (var5 >= 1536) {
                     break label61;
                  }

                  var10000 = var5 + 128;
               }

               var5 = (short)var10000;
            }

            if ((var4 = var4 + var5) >= 154880) {
               var4 = 154880;
            }

            int var7;
            if ((var7 = Math.abs(var4 - super.posX)) <= 1408) {
               this.game.e = true;
               if (var3 <= 1) {
                  this.game.boltCount += 10;
                  Game.boltsCollected += 10;
                  if (this.game.boltCount > 99999) {
                     this.game.boltCount = 99999;
                  }
               } else if (var3 == 2) {
                  super.health = (byte)(super.health + 4);
                  if (super.health > 20) {
                     super.health = 20;
                  }
               } else {
                  this.a(1);
               }

               this.game.pickupType[var2] = -1;
               return;
            }

            int var8 = this.game.pickupY[var2] - (this.c() + 7 << 8);
            if (var7 != 0) {
               this.game.pickupY[var2] = this.game.pickupY[var2] + var8 * var5 * (var5 >= 0 ? -1 : 1) / var7;
            }

            this.game.pickupX[var2] = var4;
            this.game.pickupVx[var2] = var5;
         }
      }
   }

   private void a(int var1) {
      int var2 = 1;
      short var3 = 9999;

      for (int var4 = 1; var4 < 8; var4++) {
         if (var4 != 6 && (ownedWeapons & 1 << var4) > 0 && ammo[var4] < var3 && ammo[var4] != d[var4 * 3 + weaponLevel[var4]]) {
            var3 = ammo[var4];
            var2 = var4;
         }
      }

      ammo[var2] = (short)(ammo[var2] + var1 * f[var2]);
      if (ammo[var2] > d[var2 * 3 + weaponLevel[var2]]) {
         ammo[var2] = d[var2 * 3 + weaponLevel[var2]];
      }
   }

   private static void a(String var0) {
      byte[] var3 = new byte[4];

      try {
         InputStream var4 = var3.getClass().getResourceAsStream(var0);

         for (int var5 = 0; var5 < 15; var5++) {
            byte var1 = (byte)var4.read();
            byte var2 = (byte)var4.read();
            ANIM_FRAME_COUNTS[0][var5] = var1;
            ANIM_FRAME_EXTRA[0][var5] = var2;

            try {
               for (int var6 = 0; var6 < var1; var6++) {
                  var3[var6] = (byte)var4.read();
               }
            } catch (IOException var7) {
            }

            System.arraycopy(var3, 0, ANIM_FRAMES[0][var5], 0, var1);
         }

         var4.close();
      } catch (IOException var8) {
      }
   }
}
