import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class g {
   public static final byte[] a = new byte[]{1, 2, 3, 0, 0, 0, 0, 0, 0, 12, 12, 12, 1, 2, 3, 1, 2, 2, 0, 0, 0, 5, 8, 10, 6, 11, 17, 4, 6, 8, 0, 0, 0};
   public static final byte[] b = new byte[]{6, 6, 6, 3, 3, 3, 3, 3, 3, 0, 0, 0, 3, 3, 3, 0, 0, 0, 3, 3, 3, 20, 30, 30, 10, 10, 20, 10, 10, 10, 0, 8, 6};
   public static final byte[] c = new byte[]{3, 3, 3, 2, 2, 2, 2, 2, 2, 0, 0, 0, 2, 2, 2, 0, 0, 0, 2, 2, 2, 20, 30, 30, 10, 10, 20, 10, 10, 10, 0, 8, 6};
   public static final byte[] d = new byte[]{12, 12, 12, 3, 3, 3, 8, 8, 8, 1, 1, 1, 4, 4, 4, 1, 1, 1, 8, 8, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 8};
   public static final byte[] e = new byte[]{
      8, 8, 8, 11, 11, 11, 10, 10, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, 9, 9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, -1
   };
   public static final byte[] f = new byte[]{3, 4, 6};
   public byte g;
   public int h;
   public int i;
   public int j;
   public int k;
   public int l;
   public int m;
   public int n;
   public int o;
   public byte p;
   public int q;
   public int r;
   private boolean v;
   private short w;
   public byte s;
   public boolean t;
   public int u;
   private f x;
   private boolean y;

   public g(f var1) {
      this.x = var1;
      this.g = -1;
      this.h = this.i = 0;
      this.n = this.o = 0;
      this.p = 0;
      this.q = 0;
      this.r = 0;
      this.v = false;
      this.w = -1;
      this.l = this.j = 0;
      this.m = this.k = 0;
      this.s = 0;
   }

   private void c() {
      this.g = -1;
      this.h = this.i = 0;
      this.n = this.o = 0;
      this.p = 0;
      this.q = 0;
      this.r = 0;
      this.v = false;
      this.w = -1;
      this.l = this.j = 0;
      this.m = this.k = 0;
      this.s = 0;
   }

   public final boolean a() {
      return !this.v;
   }

   public final void a(boolean var1) {
      if (!this.v && this.g != 32) {
         this.v = true;
         switch (this.g) {
            case 3:
            case 4:
            case 5:
               int var4 = this.g - 3;
               if (var1) {
                  this.x.a(this.h, this.i, 21 + var4, true, false, 0, this.s);
                  return;
               }

               this.x.d(this.h, this.i, 21 + var4);
               return;
            case 6:
            case 7:
            case 8:
               int var3 = this.g - 6;
               if (var1) {
                  this.x.a(this.h, this.i, 24 + var3, true, false, 0, this.s);
                  return;
               }

               this.x.d(this.h, this.i, 24 + var3);
               return;
            case 18:
            case 19:
            case 20:
               int var2 = this.g - 18;
               if (var1) {
                  this.x.a(this.h, this.i, 27 + var2, true, false, 0, this.s);
                  return;
               }

               this.x.d(this.h, this.i, 27 + var2);
               return;
            case 31:
               this.x.c(this.h, this.i);
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
      if (this.g >= 0 && this.g <= 2) {
         return 1;
      } else if (this.g >= 21 && this.g <= 23) {
         return 2;
      } else if (this.g >= 24 && this.g <= 26) {
         return 3;
      } else if (this.g >= 9 && this.g <= 11) {
         return 4;
      } else if (this.g >= 12 && this.g <= 14) {
         return 5;
      } else if (this.g >= 15 && this.g <= 17) {
         return 6;
      } else {
         return (byte)(this.g >= 27 && this.g <= 29 ? 7 : 0);
      }
   }

   public final void b(boolean var1) {
      int var4 = f.r;
      int var5 = f.s;
      if (!this.v || this.g >= 21 && this.g <= 30) {
         if (this.g != -1) {
            if (this.g == 32) {
               if (this.n != 0) {
                  this.n--;
               }

               if (this.n == 0) {
                  this.i = this.i + this.o;
               }

               label225: {
                  g var10000;
                  byte var10001;
                  if (this.i < this.m && this.n == 0) {
                     this.o *= -1;
                     var10000 = this;
                     var10001 = 15;
                  } else {
                     if (this.i <= this.k || this.n != 0) {
                        break label225;
                     }

                     this.o *= -1;
                     var10000 = this;
                     var10001 = 1;
                  }

                  var10000.n = var10001;
               }

               this.p++;
               if (this.p >= 3) {
                  this.p = 0;
               }
            } else if (this.g >= 6 && this.g <= 8) {
               this.h = this.h + this.n;
               int var9 = this.h >> 8;
               this.i = this.i + this.o;
               int var14 = this.i >> 8;
               if (var9 < -var4 - 22 || var9 > -var4 + 128 + 22 || var14 < -var5 - 14 || var14 > -var5 + 160 + 14) {
                  this.a(var1);
               }
            } else if (this.g >= 0 && this.g <= 2) {
               this.h = this.h + this.n;
               int var8 = this.h >> 8;
               this.i = this.i + this.o;
               int var13 = this.i >> 8;
               if (var8 < -var4 - 22 || var8 > -var4 + 128 + 22 || var13 < -var5 - 14 || var13 > -var5 + 160 + 14) {
                  this.c();
               }
            } else if (this.g >= 3 && this.g <= 5) {
               label226: {
                  this.h = this.h + this.n;
                  int var7 = this.h >> 8;
                  int var12 = this.i >> 8;
                  g var18;
                  if (this.h >= 0 && this.i >= 0 && var12 <= 238 && var7 <= 594) {
                     if (var12 + c[this.g] < f.b(var7, var12)) {
                        break label226;
                     }

                     var18 = this;
                  } else {
                     var18 = this;
                  }

                  var18.a(var1);
               }

               this.i = this.i - this.o;
               this.o -= 256;
            } else if (this.g >= 9 && this.g <= 11) {
               if (this.p++ == 2) {
                  this.c();
               }
            } else if (this.g >= 12 && this.g <= 14) {
               this.d();
               int var6 = this.h >> 8;
               int var11 = this.i >> 8;
               if (var6 < -var4 - 22 || var6 > -var4 + 128 + 22 || var11 < -var5 - 14 || var11 > -var5 + 160 + 14) {
                  this.c();
               }
            } else if (this.g >= 15 && this.g <= 17) {
               if (this.p++ == 3) {
                  this.c();
               }
            } else if (this.g >= 18 && this.g <= 20) {
               if (var1) {
                  return;
               }

               this.d();
               int var2 = this.h >> 8;
               int var3 = this.i >> 8;
               if (var2 < -var4 - 22 || var2 > -var4 + 128 + 22 || var3 < -var5 - 14 || var3 > -var5 + 160 + 14) {
                  this.a(var1);
               }
            } else if (this.g >= 21 && this.g <= 30) {
               if (++this.p == 3) {
                  this.c();
               }
            } else if (this.g == 31) {
               this.h = this.h + this.n;
               this.i = this.i + this.o;
            }

            if ((this.g != 15 || this.g != 16 || this.g != 17 || this.g != 9 || this.g != 10 || this.g != 11) && this.g < 21 || this.g == 31) {
               int var10 = (this.h >> 8) / 22;
               int var15 = (this.i >> 8) / 14;
               if (!this.x.N.b(var10, var15)) {
                  this.a(var1);
                  if (this.g >= 12 && this.g <= 14 && this.x.N.a(var10, var15) == 13) {
                     this.x.N.f[var10][var15] = 0;
                     c.e[var10] = c.e[var10] & ~(1 << var15);
                     this.x.N.c(var10, var15);
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
         int var4 = this.h >> 8;
         int var5 = this.i >> 8;

         for (int var1 = f.au - 1; var1 >= 0; var1--) {
            if (this.x.W[var1].R != -1 && this.x.W[var1].ad != 5) {
               short var2 = this.x.W[var1].c();
               short var3 = this.x.W[var1].b();
               if ((var2 - var4) * (var2 - var4) + (var3 - var5) * (var3 - var5) <= 4356 && (this.n > 0 ? var2 > var4 : var2 < var4)) {
                  this.w = (short)var1;
                  this.y = true;
                  break;
               }
            }
         }
      } else if (this.x.W[this.w].R != -1 && this.x.W[this.w].ad != 5) {
         short var8 = this.x.W[this.w].c();
         int var9 = this.x.W[this.w].b() + d.b[this.x.W[this.w].R] + (d.d[this.x.W[this.w].R] >> 1);
         int var10 = this.h >> 8;
         int var11 = this.i >> 8;
         int var12 = var8 - var10;
         int var6 = var9 - var11;
         int var7 = d[this.g] << 8;
         if (this.g < 18 || this.g > 20) {
            if (Math.abs(var12) <= 10 && this.y) {
               this.y = false;
               this.l = this.j;
               this.m = this.k;
               this.j = this.h;
               this.k = this.i;
               if (Math.abs(var12) > Math.abs(var6)) {
                  g var10000;
                  int var10001;
                  if (var12 > 0) {
                     var10000 = this;
                     var10001 = var7;
                  } else {
                     var10000 = this;
                     var10001 = -var7;
                  }

                  var10000.n = var10001;
                  this.o = 0;
               } else {
                  g var13;
                  int var14;
                  if (var6 > 0) {
                     var13 = this;
                     var14 = var7;
                  } else {
                     var13 = this;
                     var14 = -var7;
                  }

                  var13.o = var14;
                  this.n = 0;
               }

               this.p = 0;
            }
         } else if (var6 > 0) {
            this.o = var7;
            if (var6 << 8 < this.o) {
               this.o = 0;
            }
         } else {
            this.o = -var7;
            if (var6 << 8 > this.o) {
               this.o = 0;
            }
         }
      } else {
         this.w = -1;
      }

      this.p++;
      this.h = this.h + this.n;
      this.i = this.i + this.o;
   }

   public final void a(Graphics var1, boolean var2) {
      int var3 = f.r;
      int var4 = f.s;
      if (this.g >= 21 && this.g <= 30) {
         int var18 = (this.h >> 8) + var3 - 11;
         int var19 = (this.i >> 8) + var4 - (14 * (this.p == 1 ? 2 : 1) >> 1);
         int var20 = 14 * (this.p == 1 ? 2 : 1);
         int var26 = f[this.p] * 14;
         int var17 = 0;
         if (var19 < 20) {
            var17 = 20 - var19;
            var19 = 20;
         }

         if (var17 < var20 && var17 >= 0) {
            var1.setClip(var18, var19, 22, var20 - var17);
            var1.drawImage(f.am, var18, var19 - var26 - var17, 0);
            return;
         }
      } else {
         boolean var6 = false;
         boolean var7 = false;
         int var8;
         if ((var8 = e[this.g] * 8) < 0) {
            int var21 = (this.h >> 8) + var3;
            int var25 = (this.i >> 8) + var4;
            if (this.g >= 9 && this.g <= 11) {
               var21 -= this.n > 0 ? this.q : -this.q;
               var1.setClip(0, 20, 128, 140);
               Graphics var32;
               short var33;
               short var34;
               if (this.p == 0) {
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

               var1.drawLine(var21, var25, this.n > 0 ? var21 + 110 : var21 - 110, var25 + 7);
               var1.drawLine(var21, var25, this.n > 0 ? var21 + 110 : var21 - 110, var25 + 3);
               var1.drawLine(var21, var25, this.n > 0 ? var21 + 110 : var21 - 110, var25 - 3);
               var1.drawLine(var21, var25, this.n > 0 ? var21 + 110 : var21 - 110, var25 - 7);
               return;
            }

            if (this.g >= 12 && this.g <= 14) {
               int var27 = (this.j >> 8) + var3;
               int var29 = (this.k >> 8) + var4;
               int var13 = (this.l >> 8) + var3;
               int var14 = (this.m >> 8) + var4;
               if (!var2) {
                  var25 += 4;
                  var29 += 4;
                  var14 += 4;
               }

               var1.setClip(0, 20, 128, 140);
               var1.setColor(255, 255, 0);
               int var16 = (this.u >> 8) + var3;
               int var31;
               if (this.t) {
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

            if (this.g >= 15 && this.g <= 17) {
               if (var2) {
                  if (this.n > 0) {
                     var21 -= this.q;
                  } else {
                     var21 += this.q;
                  }
               } else {
                  var21 = this.x.X.b() + var3;
                  var25 += 4;
               }

               var1.setClip(0, 20, 128, 140);
               var1.setColor(0, 0, 255);
               if (this.n > 0) {
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

            var1.setClip(0, 20, 128, 140);
            var1.setColor(255, 255, 255);
            var1.fillRect(var21 - 3, var25 - 3, 6, 6);
            return;
         }

         int var9 = (this.h >> 8) + var3 - 4;
         int var10 = (this.i >> 8) + var4 - 4;
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
            if (this.n <= 0 && e[this.g] > 7 && e[this.g] < 12) {
               DirectGraphics var10000;
               Image var10001;
               int var10002;
               int var10003;
               byte var10004;
               short var10005;
               if (this.n == 0) {
                  if (this.o < 0) {
                     var10000 = DirectUtils.getDirectGraphics(var1);
                     var10001 = f.ai;
                     var10002 = var9 - var8;
                     var10003 = var10 - var5;
                     var10004 = 0;
                     var10005 = 90;
                  } else {
                     var10000 = DirectUtils.getDirectGraphics(var1);
                     var10001 = f.ai;
                     var10002 = var9 - 136 + var8;
                     var10003 = var10 - var5;
                     var10004 = 0;
                     var10005 = 270;
                  }
               } else {
                  var10000 = DirectUtils.getDirectGraphics(var1);
                  var10001 = f.ai;
                  var10002 = var9;
                  var10003 = var10 - var8 - var5;
                  var10004 = 0;
                  var10005 = 8192;
               }

               var10000.drawImage(var10001, var10002, var10003, var10004, var10005);
            } else {
               if (this.g != 31) {
                  var1.drawImage(f.ai, var9, var10 - var8 - var5, 0);
                  return;
               }

               int var11 = 304 - ((this.h >> 8) - 4);
               int var12 = 122 - ((this.i >> 8) - 4);
               if (var11 * var11 + var12 * var12 >= 625 || Math.abs(var11) >= 22 || Math.abs(var12) >= 22) {
                  var1.drawImage(f.ai, var9, var10 - var8 - var5, 0);
                  return;
               }
            }
         }
      }
   }
}
