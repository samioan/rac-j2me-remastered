import javax.microedition.lcdui.Graphics;

public final class d {
   public static f[] a;
   public int b = -1;
   public static byte[] c;
   public static byte[] d;
   public static byte[] e;
   public static byte[] f;
   public static final byte[] g = new byte[]{2, 3, 4, 11, 12, 13, 8, 9, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, 5, 6, 7};
   public int h;
   public int i;
   public int j;
   public int k;
   public int l;
   public int m;
   public int n;
   public int o;
   public int p;
   public int q;
   public int r;
   public int s;
   public boolean t;
   public int u;
   public int v;
   public static b w;

   public d(b var1) {
      w = var1;
      this.a();
   }

   public final void a() {
      this.h = -1;
      this.i = this.j = 0;
      this.o = this.p = 0;
      this.q = 0;
      this.r = 0;
      this.s = 0;
      this.t = false;
      this.u = -1;
      this.m = this.k = 0;
      this.n = this.l = 0;
      this.v = 0;
   }

   public final void a(boolean var1) {
      if (!this.t) {
         this.t = true;
         switch (this.h) {
            case 3:
            case 4:
            case 5:
               int var4 = this.h - 3;
               if (var1) {
                  w.a(this.i, this.j, 21 + var4, true, false, 0, this.v);
                  return;
               }

               w.a(this.i, this.j, 21 + var4, true);
               return;
            case 6:
            case 7:
            case 8:
               int var3 = this.h - 6;
               if (var1) {
                  w.a(this.i, this.j, 24 + var3, true, false, 0, this.v);
                  return;
               }

               w.a(this.i, this.j, 24 + var3, true);
               return;
            case 18:
            case 19:
            case 20:
               int var2 = this.h - 18;
               if (var1) {
                  w.a(this.i, this.j, 27 + var2, true, false, 0, this.v);
                  return;
               } else {
                  w.a(this.i, this.j, 27 + var2, true);
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

   public final int b() {
      if (this.h >= 0 && this.h <= 2) {
         return 1;
      } else if (this.h >= 21 && this.h <= 23) {
         return 2;
      } else if (this.h >= 24 && this.h <= 26) {
         return 3;
      } else if (this.h >= 9 && this.h <= 11) {
         return 4;
      } else if (this.h >= 12 && this.h <= 14) {
         return 5;
      } else if (this.h >= 15 && this.h <= 17) {
         return 6;
      } else {
         return this.h >= 27 && this.h <= 29 ? 7 : 0;
      }
   }

   public final void b(boolean var1) {
      int var4 = b.Z;
      int var5 = b.aa;
      if (this.t) {
         this.a();
      } else if (this.h != -1) {
         if (this.h >= 6 && this.h <= 8) {
            this.i = this.i + this.o;
            int var9 = this.i >> 8;
            this.j = this.j + this.p;
            int var14 = this.j >> 8;
            if (var9 < -var4 - 57 || var9 > -var4 + 240 + 57 || var14 < -var5 - 38 || var14 > -var5 + 320 + 38) {
               this.a(var1);
            }
         } else if (this.h >= 0 && this.h <= 2) {
            this.i = this.i + this.o;
            int var8 = this.i >> 8;
            this.j = this.j + this.p;
            int var13 = this.j >> 8;
            if (var8 < -var4 - 57 || var8 > -var4 + 240 + 57 || var13 < -var5 - 38 || var13 > -var5 + 320 + 38) {
               this.a();
            }
         } else if (this.h >= 3 && this.h <= 5) {
            this.i = this.i + this.o;
            int var7 = this.i >> 8;
            int var12 = this.j >> 8;
            if (this.i >= 0 && this.j >= 0 && var12 <= 646 && var7 <= 1539) {
               if (var12 + e[this.h] >= b.f(var7, var12)) {
                  this.a(var1);
               }
            } else {
               this.a(var1);
            }

            this.j = this.j - this.p;
            this.p -= 256;
         } else if (this.h >= 9 && this.h <= 11) {
            if (this.q++ == 2) {
               this.a();
            }
         } else if (this.h >= 12 && this.h <= 14) {
            this.c();
            int var6 = this.i >> 8;
            int var11 = this.j >> 8;
            if (var6 < -var4 - 57 || var6 > -var4 + 240 + 57 || var11 < -var5 - 38 || var11 > -var5 + 320 + 38) {
               this.a();
            }
         } else if (this.h >= 15 && this.h <= 17) {
            if (this.q++ == 3) {
               this.a();
            }
         } else if (this.h >= 18 && this.h <= 20) {
            if (var1) {
               return;
            }

            this.c();
            int var2 = this.i >> 8;
            int var3 = this.j >> 8;
            if (var2 < -var4 - 57 || var2 > -var4 + 240 + 57 || var3 < -var5 - 38 || var3 > -var5 + 320 + 38) {
               this.a(var1);
            }
         }

         if (this.h != 15 && this.h != 16 && this.h != 17 && this.h != 9 && this.h != 10 && this.h != 11 && this.h < 21) {
            int var10 = (this.i >> 8) / 57;
            int var15 = this.j / 38 >> 8;
            if (!w.h(var10, var15)) {
               this.a(var1);
               if (this.h >= 12 && this.h <= 14 && b.g(var10, var15) == -1 && var10 > 0 && var10 < 28 && var15 > 0 && var15 < 18) {
                  w.eN[var10][var15] = 0;
                  b.eM[var10] = b.eM[var10] & ~(1 << var15);
                  w.a(var10, var15, 1);
               }
            }
         }
      }
   }

   public final void c() {
      if (this.u == -1) {
         int var4 = this.i >> 8;
         int var5 = this.j >> 8;

         for (int var1 = b.bs - 1; var1 >= 0; var1--) {
            if (w.aF[var1].a != -1 && w.aF[var1].n != 5) {
               int var2 = w.aF[var1].c();
               int var3 = w.aF[var1].b();
               if ((var2 - var4) * (var2 - var4) + (var3 - var5) * (var3 - var5) <= 15876 && (this.o > 0 ? var2 > var4 : var2 < var4)) {
                  this.u = var1;
                  break;
               }
            }
         }
      } else if (w.aF[this.u].a != -1 && w.aF[this.u].n != 5) {
         int var8 = w.aF[this.u].c();
         int var9 = w.aF[this.u].b() + c.u[w.aF[this.u].a] + (c.w[w.aF[this.u].a] >> 1);
         int var10 = this.i >> 8;
         int var11 = this.j >> 8;
         int var12 = var8 - var10;
         int var6 = var9 - var11;
         int var7 = f[this.h] << 8;
         if (this.h >= 18 && this.h <= 20) {
            if (var6 > 0) {
               this.p = var7;
               if (var6 << 8 < this.p) {
                  this.p = 0;
               }
            } else {
               this.p = -var7;
               if (var6 << 8 > this.p) {
                  this.p = 0;
               }
            }
         } else if (this.q >= 10) {
            this.m = this.k;
            this.n = this.l;
            this.k = this.i;
            this.l = this.j;
            if (Math.abs(var12) > Math.abs(var6)) {
               if (var12 > 0) {
                  this.o = var7;
               } else {
                  this.o = -var7;
               }

               this.p = 0;
            } else {
               if (var6 > 0) {
                  this.p = var7;
               } else {
                  this.p = -var7;
               }

               this.o = 0;
            }

            this.q = 0;
         }
      } else {
         this.u = -1;
      }

      this.q++;
      this.i = this.i + this.o;
      this.j = this.j + this.p;
   }

   public final void a(Graphics var1, b var2) {
      int var3 = b.Z;
      int var4 = b.aa;
      if (this.h >= 21 && this.h <= 30) {
         short var13 = a[0].d;
         short var14 = a[0].c;
         int var19 = (this.i >> 8) + var3 - (var14 << 1);
         int var22 = (this.j >> 8) + var4 - (var13 << 1);
         if (this.b > 0) {
            var2.a(var1, a, b.bs + this.b, var19, var22, 0);
            if (!var2.d(b.bs + this.b, b.v)) {
               b.en[this.b] = -1;
               this.b = -1;
               this.a();
               return;
            }
         }
      } else {
         int var5 = var2.ak;
         byte var6;
         if ((var6 = g[this.h]) < 0) {
            int var15 = (this.i >> 8) + var3;
            int var20 = (this.j >> 8) + var4;
            if (this.h >= 9 && this.h <= 11) {
               var15 -= this.o > 0 ? this.r : -this.r;
               if (this.q == 0) {
                  var1.setColor(255, 255, 255);
               } else if (this.h == 9) {
                  var1.setColor(0, 0, 255);
               } else if (this.h == 10) {
                  var1.setColor(0, 255, 255);
               } else {
                  var1.setColor(255, 0, 0);
               }

               var15 += this.o > 0 ? 4 : -5;
               var1.drawLine(var15, var20, this.o > 0 ? var15 + 168 : var15 - 168, var20 + 11);
               var1.drawLine(var15, var20, this.o > 0 ? var15 + 168 : var15 - 168, var20 + 5);
               var1.drawLine(var15, var20, this.o > 0 ? var15 + 168 : var15 - 168, var20 - 5);
               var1.drawLine(var15, var20, this.o > 0 ? var15 + 168 : var15 - 168, var20 - 11);
               return;
            }

            if (this.h >= 12 && this.h <= 14) {
               int var23 = (this.k >> 8) + var3;
               int var10 = (this.l >> 8) + var4;
               int var11 = (this.m >> 8) + var3;
               int var12 = (this.n >> 8) + var4;
               if (this.h == 12) {
                  var1.setColor(255, 255, 0);
               }

               if (this.h == 13) {
                  var1.setColor(255, 255, 255);
               }

               if (this.h == 14) {
                  var1.setColor(0, 100, 255);
               }

               var1.drawLine(var15, var20, var23, var10);
               var1.drawLine(var23, var10, var11, var12);
               var1.fillRect(var15 - 1, var20 - 1, 3, 3);
               return;
            }

            if (this.h >= 15 && this.h <= 17) {
               var15 = var15 - (this.o > 0 ? this.r : -this.r) + (this.o > 0 ? -5 : 3);
               var20 += 3;
               if (this.h == 15) {
                  var1.setColor(0, 0, 255);
               } else if (this.h == 16) {
                  var1.setColor(0, 255, 255);
               } else {
                  var1.setColor(255, 100, 255);
               }

               if (this.o > 0) {
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

         int var7 = (this.i >> 8) + var3;
         int var8 = (this.j >> 8) + var4 - (var5 >> 1);
         byte var9 = 0;
         if (this.o < 0) {
            var9 = 2;
         } else if (this.o == 0) {
            if (var6 < 11) {
               var6 += 12;
            }

            if (this.p > 0) {
               var9 = 1;
            }
         }

         b.bj[var6].a(var1, var7, var8, var9);
      }
   }
}
