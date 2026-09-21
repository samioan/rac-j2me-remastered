import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;

public final class Enemy extends Entity {
   public static byte[] a;
   public static byte[] b;
   public static byte[] c;
   public static byte[] d;
   public static byte[] e;
   public static byte[] f;
   public static byte[] g;
   public static byte[] h;
   public static final byte[] HP_BY_ANIM = new byte[]{6, 10, 16, 5, 8, 10, 5, 5, 5, 15, 15, 15, 22, 22, 22, 5, 5, 5, 5, 5, 5, 5, 5, 5, 3, 0, 0};
   public static final byte[] DAMAGE_BY_ANIM = new byte[]{1, 3, 4, 4, 5, 6, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 4, 2, 3, 4, 2, 3, 4, 0, 2, 2};
   public static final byte[] BOLTS_DROPPED_BY_ANIM = new byte[]{1, 2, 3, 1, 2, 3, 1, 1, 1, 2, 2, 2, 3, 3, 3, 1, 1, 1, 2, 2, 2, 3, 3, 3, 0};
   public static final byte[] l = new byte[]{0, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1};
   public static final byte[] m = new byte[]{10, 10, 10, 10, 10};
   public static final short[] n = new short[]{1024, 512, 768, 0, 512};
   public static final byte[] o = new byte[]{0, 20, 5, 5, 0};
   public static byte[][][] ANIM_FRAMES;
   public static byte[][] ANIM_FRAME_COUNTS;
   public static byte[][] ANIM_FRAME_EXTRA;
   public boolean s;
   public byte animKind;
   private Game game;
   public byte bounceTimer;
   public boolean v;
   public boolean w;
   public boolean x;
   public short y;
   public short z;
   public byte A;
   public short B;
   public int C;

   public Enemy(Game var1) {
      game = var1;
      super.kind = -1;
      super.subState = 0;
      this.B = 0;
      super.facingRight = true;
      this.s = false;
      this.v = false;
      this.w = false;
      this.x = false;
      this.z = 0;
   }

   public final void a() {
      if (ANIM_FRAMES == null) {
         ANIM_FRAMES = new byte[5][6][5];
      }

      if (ANIM_FRAME_COUNTS == null) {
         ANIM_FRAME_COUNTS = new byte[5][6];
      }

      if (ANIM_FRAME_EXTRA == null) {
         ANIM_FRAME_EXTRA = new byte[5][6];
      }

      if (a == null) {
         a = new byte[5];
      }

      if (b == null) {
         b = new byte[5];
      }

      if (c == null) {
         c = new byte[5];
      }

      if (d == null) {
         d = new byte[5];
      }

      if (e == null) {
         e = new byte[5];
      }

      if (f == null) {
         f = new byte[5];
      }

      if (g == null) {
         g = new byte[5];
      }

      if (h == null) {
         h = new byte[5];
      }

      this.b("/ANIM_FRAMES");
      this.a("/ANIM_FRAME_COUNTS");
   }

   public final short b() {
      return (short)(super.row * 14 + (super.posInRow >> 8) + 14 - 22);
   }

   public final short c() {
      return (short)(super.posX >> 8);
   }

   private byte f() {
      return (byte)(((super.posX >> 8) + 10) / 22);
   }

   private byte g() {
      return (byte)(((super.posX >> 8) - 10) / 22);
   }

   private int a(boolean var1) {
      int var2 = (super.posX >> 8) / 22;
      boolean var6 = false;
      if (var1) {
         this.y = game.N.a(var2, super.row);
      }

      int var3;
      for (var3 = super.row + 1; var3 < 18; var3++) {
         if ((LevelMap.columnSolidMasks[var2] & 1 << var3) > 0) {
            var3 *= 14;
            var6 = true;
            break;
         }
      }

      if (!var6) {
         var3 = 252;
      }

      var2 = super.posX >> 8;

      for (int var7 = 0; var7 < 20; var7++) {
         if (game.titaniumBoltType[var7] != -1
            && game.titaniumBoltX[var7] <= var2 + 4
            && game.titaniumBoltX[var7] + 16 >= var2 - 4
            && game.titaniumBoltY[var7] < var3
            && game.titaniumBoltY[var7] > super.row * 14) {
            var3 = game.titaniumBoltY[var7];
         }
      }

      return var3;
   }

   private boolean h() {
      int var6 = super.posX >> 8;
      int var7 = this.b() + 22 - 14;

      for (int var10 = 19; var10 >= 0; var10--) {
         if (game.titaniumBoltType[var10] >= 0
            && Math.abs(game.titaniumBoltX[var10] - var6) <= 22
            && game.titaniumBoltY[var10] == var7
            && game.a(game.titaniumBoltX[var10], game.titaniumBoltY[var10], 16, 16, var6 - 4, var7, 8, 13)) {
            return true;
         }
      }

      return false;
   }

   private void HP_BY_ANIM() {
      int var1 = this.a(true) / 14;
      this.x = false;
      this.v = false;
      this.w = false;
      if (super.row + 1 < var1 && super.kind != 3 || super.animState == 2) {
         this.setAnimState((byte)2);
         super.velY = (short)(super.velY + 256);
         super.posInRow = super.posInRow + super.velY;
         this.wrapRow();
         if (super.row + 1 == var1) {
            this.setAnimState((byte)0);
            super.velY = 0;
            super.posInRow = 0;
         }

         if (game.a(this.c() - a[super.kind], this.b() + b[super.kind], c[super.kind], d[super.kind], game.player.b() + 0 - 6, game.player.c() + -6 + 3, 12, 20)) {
            super.velX = 0;
            if (super.velY > 0) {
               super.velY = (short)(-super.velY / 2);
               if (game.player.b() < this.c()) {
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
         super.fieldW = super.facingRight ? this.f() : this.g();
         super.spawnRow = super.row;
         int var2 = super.posX;
         super.posX = super.posX + (super.facingRight ? 2816 : -2816);
         int var3 = this.a(false) / 14;
         this.w = false;
         if (var3 > var1) {
            this.w = true;
         }

         if (bounceTimer != 0 && this.w) {
            super.velX = 0;
         }

         super.posX = var2;
         if (super.fieldW > 0 && super.fieldW < 27 && game.N.b(super.fieldW, super.spawnRow)) {
            super.posX = super.posX + super.velX;
            if (this.h()) {
               super.posX = super.posX - super.velX;
               if (super.animState != 2) {
                  this.v = true;
                  return;
               }
            }
         } else {
            this.x = true;
         }
      }
   }

   public final void d() {
      if (super.animState == 5) {
         if (super.animRestart == 2) {
            super.kind = -1;
         }
      } else if (super.animState == 4 && bounceTimer == 0) {
         if (++super.animHold > m[super.kind]) {
            super.velX = 0;
            super.animRestart = 2;
            this.setAnimState((byte)0);
         }
      } else if (Math.abs(game.player.b() - this.c()) <= 110 && Math.abs(game.player.c() - this.b()) <= 112) {
         if (super.kind != 3 || Math.abs(game.player.b() - this.c()) <= 77 && Math.abs(game.player.c() - this.b()) <= 70) {
            HP_BY_ANIM();
            if (bounceTimer != 0 && super.kind != 3) {
               super.velX = (short)(bounceTimer << 8);
               super.velY = 0;
               this.setAnimState((byte)4);
               Enemy var7;
               boolean var10001;
               if (bounceTimer > 0) {
                  bounceTimer--;
                  var7 = this;
                  var10001 = true;
               } else {
                  bounceTimer++;
                  var7 = this;
                  var10001 = false;
               }

               var7.facingRight = var10001;
               if (this.x) {
                  super.velX = 0;
               }
            } else {
               int var1 = game.player.b() - this.c();
               int var2 = game.player.c() + 14 - 22 - this.b();
               if (super.kind == 0) {
                  label317: {
                     if (this.v) {
                        super.facingRight = !super.facingRight;
                     } else if (this.x) {
                        if (this.y == -96) {
                           super.velY = -510;
                           this.setAnimState((byte)2);
                           return;
                        }

                        super.facingRight = !super.facingRight;
                     } else {
                        if (!this.w) {
                           break label317;
                        }

                        if (this.y == -97) {
                           super.velY = -768;
                           super.velX = (short)(n[super.kind] << 2);
                           if (!super.facingRight) {
                              super.velX = (short)(super.velX * -1);
                           }

                           this.setAnimState((byte)2);
                           return;
                        }

                        if (this.y == -96) {
                           break label317;
                        }

                        super.facingRight = !super.facingRight;
                     }

                     this.B = 10;
                  }

                  if (super.animState == 2) {
                     return;
                  }

                  if (var1 < 66 && var1 > 22 && Math.abs(var2) < 14 && this.B == 0) {
                     if (super.animState != 1) {
                        this.setAnimState((byte)1);
                     }

                     super.velX = n[super.kind];
                     super.facingRight = true;
                  } else if (var1 < -22 && var1 > -66 && Math.abs(var2) < 14 && this.B == 0) {
                     if (super.animState != 1) {
                        this.setAnimState((byte)1);
                     }

                     super.velX = (short)(-n[super.kind]);
                     super.facingRight = false;
                  } else if (Math.abs(var1) <= 22 && var2 == 0) {
                     super.velX = 0;
                     game.a(super.posX, this.b() + 7 << 8, 21, super.facingRight, false, 0, animKind);
                     super.kind = -1;
                  } else {
                     DAMAGE_BY_ANIM();
                  }

                  if (this.B != 0) {
                     this.B--;
                     return;
                  }
               } else {
                  Enemy var10000;
                  if (super.kind == 1 && super.animState != 2) {
                     if (this.y == -95) {
                        if (var2 < 0 && Math.abs(var1) > 11) {
                           super.velY = -3584;
                           super.velX = 0;
                           this.setAnimState((byte)2);
                        }
                     } else if (this.v) {
                        super.facingRight = !super.facingRight;
                     } else if (this.x) {
                        if (this.y == -96) {
                           super.velY = -1700;
                           this.setAnimState((byte)2);
                           return;
                        }

                        super.facingRight = !super.facingRight;
                     } else if (this.w) {
                        if (this.y == -97) {
                           super.velY = -3584;
                           super.velX = (short)(n[super.kind] << 2);
                           if (!super.facingRight) {
                              super.velX = (short)(super.velX * -1);
                           }

                           this.setAnimState((byte)2);
                           return;
                        }

                        if (this.y != -96) {
                           super.facingRight = !super.facingRight;
                        }
                     }

                     if (super.animState == 2) {
                        return;
                     }

                     if (Math.abs(var1) <= 22 && Math.abs(var2) <= 22 && Math.abs(var2) < 14) {
                        super.velX = 0;
                        BOLTS_DROPPED_BY_ANIM();
                        return;
                     }

                     var10000 = this;
                  } else if (super.kind == 2) {
                     if (this.v) {
                        super.velX = 0;
                        super.animRestart = 1;
                        this.setAnimState((byte)0);
                        super.facingRight = !super.facingRight;
                     } else if (this.x) {
                        super.velX = 0;
                        super.animRestart = 1;
                        this.setAnimState((byte)0);
                        super.facingRight = !super.facingRight;
                     } else if (this.w) {
                        super.velX = 0;
                        super.animRestart = 1;
                        this.setAnimState((byte)0);
                        super.facingRight = !super.facingRight;
                     }

                     int var5 = Math.abs(this.C - super.posX) >> 8;
                     if (this.A != 0) {
                        if (this.A == 1) {
                           if (this.B < l[animKind]) {
                              this.l();
                              return;
                           }

                           if (super.animRestart == 2 && super.animState == 0) {
                              this.A = 0;
                              return;
                           }

                           if (super.animState != 0) {
                              super.animRestart = 1;
                              this.setAnimState((byte)0);
                              return;
                           }
                        }

                        return;
                     }

                     if ((var1 > 0 && super.facingRight || var1 < 0 && !super.facingRight) && Math.abs(var2) < 14 && Math.abs(var1) < 66) {
                        super.velX = 0;
                        this.A = 1;
                        this.B = 0;
                        this.z = 0;
                        return;
                     }

                     if ((this.C > super.posX && !super.facingRight || this.C < super.posX && super.facingRight) && var5 >= 66) {
                        super.velX = 0;
                        super.animRestart = 1;
                        this.setAnimState((byte)0);
                        super.facingRight = !super.facingRight;
                        return;
                     }

                     if (super.animState == 0 && (super.animRestart != 2 || super.animState != 0)) {
                        return;
                     }

                     var10000 = this;
                  } else {
                     if (super.kind == 3) {
                        if (this.A == 0) {
                           if (this.B < l[animKind]) {
                              this.l();
                           } else {
                              this.B = 0;
                              this.A = 1;
                           }
                        }

                        if (this.A == 1 && this.B++ > 10) {
                           if (super.facingRight && !this.s) {
                              this.s = true;
                           } else if (super.facingRight) {
                              this.s = super.facingRight = false;
                           } else {
                              super.facingRight = true;
                           }

                           this.A = 0;
                           this.B = 0;
                           return;
                        }

                        return;
                     }

                     if (super.kind != 4) {
                        return;
                     }

                     if (this.v) {
                        super.facingRight = !super.facingRight;
                     } else if (this.x) {
                        super.facingRight = !super.facingRight;
                     } else if (this.w) {
                        super.facingRight = !super.facingRight;
                     }

                     if (Math.abs(game.O.nextInt()) % 20 == 0) {
                        super.facingRight = !super.facingRight;
                     }

                     var10000 = this;
                  }

                  DAMAGE_BY_ANIM();
               }
            }
         }
      } else {
         super.velX = super.velY = 0;
      }
   }

   public final void e() {
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

                  if (super.animState == 3) {
                     this.setAnimState((byte)0);
                     if (super.kind == 1) {
                        super.facingRight = !super.facingRight;
                        this.B = 20;
                     }
                  }
               }
            }
         }
      }
   }

   public final void a(Graphics var1, int var2) {
      int var5 = 0;
      int var6 = super.activeFlag;
      int var3 = this.c() + Game.r - 11;
      int var4 = this.b() + Game.s;
      if (var3 >= -22 && var3 <= 128 && var4 >= -22 && var4 <= 128) {
         if (var4 + 22 > 20 && var4 < 128) {
            int var7 = var4;
            var5 = 0;
            if (var7 < 20) {
               var5 = 20 - var7;
               var7 = 20;
            }

            var1.setClip(var3, var7, 22, 22 - var5);
            if (super.subState == 0 && super.facingRight) {
               if ((var6 & 1) > 0) {
                  byte var8 = 0;
                  if (this.s) {
                     var8 = 44;
                  }

                  var1.drawImage(Game.enemySegmentImage, var3, var4 - ANIM_FRAMES[super.kind][super.animState][super.animFrame] * 22 - var8, 20);
                  return;
               }
            } else if ((var6 & 1) > 0) {
               game.a(var1, var2, var3, var4);
            }
         }
      }
   }

   private void DAMAGE_BY_ANIM() {
      if (super.animState != 1) {
         this.setAnimState((byte)1);
      }

      super.animRestart = 0;
      super.velX = (short)(super.facingRight ? n[super.kind] : -n[super.kind]);
   }

   private void BOLTS_DROPPED_BY_ANIM() {
      if (super.animState != 3) {
         if (game.player.animState != 10) {
            super.facingRight = false;
            if (game.player.b() - this.c() > 0) {
               super.facingRight = true;
            }

            if (--this.z < 0) {
               this.z = o[super.kind];
               if (game.Q != 0) {
                  this.setAnimState((byte)3);
               }

               if (game.player.invulnTimer > 0) {
                  return;
               }

               if (game
                     .a(
                        game.player.b() - 6 + 0,
                        game.player.c() + 3 + -6,
                        12,
                        20,
                        this.c() + (super.facingRight ? e[super.kind] : -e[super.kind] - g[super.kind]),
                        this.b() + f[super.kind],
                        g[super.kind],
                        h[super.kind]
                     )
                  && game.Q != 0) {
                  if (!Game.i) {
                     game.player.health = (byte)(game.player.health - DAMAGE_BY_ANIM[animKind]);
                  }

                  game.player.setAnimState((byte)9);
                  game.player.animHold = 0;
                  if (game.player.jumpPhase == 2) {
                     game.player.jumpPhase = 1;
                  }

                  game.e = true;
               }
            }
         }
      }
   }

   private void l() {
      if (--this.z < 0) {
         label60: {
            this.setAnimState((byte)3);
            super.animRestart = 1;
            Game var10000;
            int var10001;
            int var10002;
            byte var10003;
            if (animKind == 6 || animKind == 9 || animKind == 12 || animKind == 15 || animKind == 18 || animKind == 21) {
               var10000 = game;
               var10001 = super.posX;
               var10002 = this.b() + 11 << 8;
               var10003 = 0;
            } else if (animKind == 7 || animKind == 10 || animKind == 13 || animKind == 16 || animKind == 19 || animKind == 22) {
               var10000 = game;
               var10001 = super.posX;
               var10002 = this.b() + 11 << 8;
               var10003 = 6;
            } else if (animKind != 7 && animKind != 10 && animKind != 13) {
               if (animKind != 17 && animKind != 20 && animKind != 23) {
                  break label60;
               }

               var10000 = game;
               var10001 = super.posX;
               var10002 = this.b() + 11 << 8;
               var10003 = 3;
            } else {
               var10000 = game;
               var10001 = super.posX;
               var10002 = this.b() + 11 << 8;
               var10003 = 18;
            }

            var10000.a(var10001, var10002, var10003, super.facingRight, this.s, super.subState, animKind);
         }

         this.z = o[super.kind];
         this.B++;
      }
   }

   private static void a(InputStream var0, byte[] var1, int var2, boolean var3) {
      try {
         for (int var5 = 0; var5 < var2; var5++) {
            var1[var5] = (byte)var0.read();
            var1[var5] = (byte)(var1[var5] * 22 / 44);
         }
      } catch (IOException var6) {
      }
   }

   private void a(String var1) {
      byte[] var2 = new byte[5];

      try {
         InputStream var3;
         a(var3 = var2.getClass().getResourceAsStream(var1), var2, 5, true);
         System.arraycopy(var2, 0, a, 0, var2.length);
         a(var3, var2, 5, false);
         System.arraycopy(var2, 0, b, 0, var2.length);
         a(var3, var2, 5, true);
         System.arraycopy(var2, 0, c, 0, var2.length);
         a(var3, var2, 5, false);
         System.arraycopy(var2, 0, d, 0, var2.length);
         a(var3, var2, 5, true);
         System.arraycopy(var2, 0, e, 0, var2.length);
         a(var3, var2, 5, false);
         System.arraycopy(var2, 0, f, 0, var2.length);
         a(var3, var2, 5, true);
         System.arraycopy(var2, 0, g, 0, var2.length);
         a(var3, var2, 5, false);
         System.arraycopy(var2, 0, h, 0, var2.length);
         var3.close();
      } catch (IOException var4) {
      }
   }

   private void b(String var1) {
      byte[] var4 = new byte[5];

      try {
         InputStream var5 = var4.getClass().getResourceAsStream(var1);

         for (int var6 = 0; var6 < 5; var6++) {
            for (int var7 = 0; var7 < 6; var7++) {
               byte var2 = (byte)var5.read();
               byte var3 = (byte)var5.read();
               ANIM_FRAME_COUNTS[var6][var7] = var2;
               ANIM_FRAME_EXTRA[var6][var7] = var3;
               a(var5, var4, var2);
               System.arraycopy(var4, 0, ANIM_FRAMES[var6][var7], 0, var2);
            }
         }

         var5.close();
      } catch (IOException var8) {
      }
   }

   private static void a(InputStream var0, byte[] var1, int var2) {
      try {
         for (int var3 = 0; var3 < var2; var3++) {
            var1[var3] = (byte)var0.read();
         }
      } catch (IOException var4) {
      }
   }
}
