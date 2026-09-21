import javax.microedition.lcdui.Graphics;

public final class j {
   public static final byte[] a = new byte[]{1, 2, 4, 0, 0, 0, 0, 0, 0, 12, 12, 12, 1, 2, 3, 1, 2, 2, 0, 0, 0, 3, 5, 6, 6, 11, 17, 4, 7, 10, 0, 0, 0};
   public static final byte[] b = new byte[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 3, 3, 3, 0, 0, 0, 3, 3, 3, 45, 65, 65, 15, 15, 30, 15, 15, 15, 0, 12, 10};
   public static final byte[] c = new byte[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 3, 3, 3, 0, 0, 0, 3, 3, 3, 45, 65, 65, 15, 15, 30, 15, 15, 15, 0, 12, 10};
   public static final byte[] d = new byte[]{10, 16, 16, 5, 5, 5, 8, 8, 8, 1, 1, 1, 4, 4, 4, 1, 1, 1, 8, 8, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 10};
   public static final short[] e = new short[]{
      13, 14, 15, 22, 23, 24, 19, 20, 21, -1, -1, -1, -1, -1, -1, -1, -1, -1, 16, 17, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1
   };
   public byte f;
   public int g;
   public int h;
   public int i;
   public int j;
   public int k;
   public int l;
   public int m;
   public int n;
   public byte o;
   public int p;
   public int q;
   private boolean w;
   private short x;
   public byte r;
   public int s;
   public static byte t;
   public static byte u;
   public static short v;
   private h y;

   public j(h var1) {
      t = h.F;
      u = h.G;
      v = h.H;
      this.y = var1;
      this.f = -1;
      this.g = this.h = 0;
      this.m = this.n = 0;
      this.o = 0;
      this.p = 0;
      this.q = 0;
      this.w = false;
      this.x = -1;
      this.k = this.i = 0;
      this.l = this.j = 0;
      this.r = 0;
   }

   public final void a() {
      this.f = -1;
      this.g = this.h = 0;
      this.m = this.n = 0;
      this.o = 0;
      this.p = 0;
      this.q = 0;
      this.w = false;
      this.x = -1;
      this.k = this.i = 0;
      this.l = this.j = 0;
      this.r = 0;
   }

   public final boolean b() {
      return !this.w;
   }

   public final void a(boolean var1) {
      if (!this.w && this.f != 32) {
         this.w = true;
         switch (this.f) {
            case 3:
            case 4:
            case 5:
               int var4 = this.f - 3;
               if (var1) {
                  this.y.a(this.g, this.h, 21 + var4, true, false, 0, this.r);
                  return;
               }

               this.y.e(this.g, this.h, 21 + var4);
               return;
            case 6:
            case 7:
            case 8:
               int var3 = this.f - 6;
               if (var1) {
                  this.y.a(this.g, this.h, 24 + var3, true, false, 0, this.r);
                  return;
               }

               this.y.e(this.g, this.h, 24 + var3);
               return;
            case 18:
            case 19:
            case 20:
               int var2 = this.f - 18;
               if (var1) {
                  this.y.a(this.g, this.h, 27 + var2, true, false, 0, this.r);
                  return;
               }

               this.y.e(this.g, this.h, 27 + var2);
               return;
            case 31:
               if (!h.dI) {
                  this.y.C(this.g, this.h);
               }

               h.dI = false;
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

   public final byte c() {
      if (this.f >= 0 && this.f <= 2) {
         return 1;
      } else if (this.f >= 21 && this.f <= 23) {
         return 2;
      } else if (this.f >= 24 && this.f <= 26) {
         return 3;
      } else if (this.f >= 9 && this.f <= 11) {
         return 4;
      } else if (this.f >= 12 && this.f <= 14) {
         return 5;
      } else if (this.f >= 15 && this.f <= 17) {
         return 6;
      } else {
         return (byte)(this.f >= 27 && this.f <= 29 ? 7 : 0);
      }
   }

   public final void b(boolean var1) {
      int var4 = h.x;
      int var5 = h.y;
      if (this.w) {
         this.a();
      } else if (this.f != -1) {
         if (this.f == 32) {
            if (this.m != 0) {
               this.m--;
            }

            if (this.m == 0) {
               this.h = this.h + this.n;
            }

            if (this.h < this.l && this.m == 0) {
               this.n *= -1;
               this.m = 15;
            } else if (this.h > this.j && this.m == 0) {
               this.n *= -1;
               this.m = 1;
            }

            this.o++;
            if (this.o >= 3) {
               this.o = 0;
            }
         } else if (this.f >= 6 && this.f <= 8) {
            this.g = this.g + this.m;
            int var9 = this.g >> 8;
            this.h = this.h + this.n;
            int var14 = this.h >> 8;
            if (var9 < -var4 - t || var9 > -var4 + 176 + t || var14 < -var5 - u || var14 > -var5 + 220 + u) {
               this.a(var1);
            }
         } else if (this.f >= 0 && this.f <= 2) {
            this.g = this.g + this.m;
            int var8 = this.g >> 8;
            this.h = this.h + this.n;
            int var13 = this.h >> 8;
            if (var8 < -var4 - t || var8 > -var4 + 176 + t || var13 < -var5 - u || var13 > -var5 + 220 + u) {
               this.a();
            }
         } else if (this.f >= 3 && this.f <= 5) {
            this.g = this.g + this.m;
            int var7 = this.g >> 8;
            int var12 = this.h >> 8;
            if (this.g >= 0 && this.h >= 0 && var12 <= 17 * u && var7 <= 27 * t) {
               if (var12 + c[this.f] >= this.y.b(var7, var12, u - c[this.f])) {
                  this.a(var1);
               }
            } else {
               this.a(var1);
            }

            this.h = this.h - this.n;
            this.n -= 256;
         } else if (this.f >= 9 && this.f <= 11) {
            if (this.o++ == 2) {
               this.a();
            }
         } else if (this.f >= 12 && this.f <= 14) {
            this.a(1);
            int var6 = this.g >> 8;
            int var11 = this.h >> 8;
            if (var6 < -var4 - t || var6 > -var4 + 176 + t || var11 < -var5 - u || var11 > -var5 + 220 + u) {
               this.a();
            }
         } else if (this.f >= 15 && this.f <= 17) {
            if (this.o++ == 3) {
               this.a();
            }
         } else if (this.f >= 18 && this.f <= 20) {
            if (var1) {
               return;
            }

            this.a(4);
            int var2 = this.g >> 8;
            int var3 = this.h >> 8;
            if (var2 < -var4 - t || var2 > -var4 + 176 + t || var3 < -var5 - u || var3 > -var5 + 220 + u) {
               this.a(var1);
            }
         } else if (this.f >= 21 && this.f <= 30) {
            if (++this.o == 3) {
               this.a();
            }
         } else if (this.f == 31) {
            this.g = this.g + this.m;
            this.h = this.h + this.n;
         }

         if ((this.f != 15 || this.f != 16 || this.f != 17 || this.f != 9 || this.f != 10 || this.f != 11) && this.f < 21 || this.f == 31) {
            int var10 = (this.g >> 8) / t;
            int var15 = (this.h >> 8) / u;
            if (!this.y.V.b(var10, var15)) {
               this.a(var1);
               if (this.f >= 12 && this.f <= 14 && this.y.V.a(var10, var15) == 34) {
                  this.y.V.f[var10][var15] = 0;
                  d.e[var10] = d.e[var10] & ~(1 << var15);
               }
            }
         }
      }
   }

   public final void a(int var1) {
      if (this.x == -1) {
         int var5 = this.g >> 8;
         int var6 = this.h >> 8;

         for (int var2 = h.ba - 1; var2 >= 0; var2--) {
            if (this.y.ai[var2].Z != -1 && this.y.ai[var2].al != 5) {
               short var3 = this.y.ai[var2].c();
               short var4 = this.y.ai[var2].b();
               if ((var3 - var5) * (var3 - var5) + (var4 - var6) * (var4 - var6) <= 15876 && (this.m > 0 ? var3 > var5 : var3 < var5)) {
                  this.x = (short)var2;
                  break;
               }
            }
         }
      } else if (this.y.ai[this.x].Z != -1 && this.y.ai[this.x].al != 5) {
         short var9 = this.y.ai[this.x].c();
         int var10 = this.y.ai[this.x].b() + f.b[this.y.ai[this.x].Z] + (f.d[this.y.ai[this.x].Z] >> 1);
         int var11 = this.g >> 8;
         int var12 = this.h >> 8;
         int var13 = var9 - var11;
         int var7 = var10 - var12;
         int var8 = d[this.f] << 8;
         if (this.f >= 18 && this.f <= 20) {
            if (var7 > 0) {
               this.n = var8;
               if (var7 << 8 < this.n) {
                  this.n = 0;
               }
            } else {
               this.n = -var8;
               if (var7 << 8 > this.n) {
                  this.n = 0;
               }
            }
         } else if (this.o >= 10) {
            this.k = this.i;
            this.l = this.j;
            this.i = this.g;
            this.j = this.h;
            if (this.y.d(var13) > this.y.d(var7)) {
               if (var13 > 0) {
                  this.m = var8;
               } else {
                  this.m = -var8;
               }

               this.n = 0;
            } else {
               if (var7 > 0) {
                  this.n = var8;
               } else {
                  this.n = -var8;
               }

               this.m = 0;
            }

            this.o = 0;
         }
      } else {
         this.x = -1;
      }

      this.o++;
      this.g = this.g + this.m;
      this.h = this.h + this.n;
   }

   public final void a(Graphics var1) {
      int var2 = h.x;
      int var3 = h.y;
      if (this.f >= 0) {
         if (this.f >= 21 && this.f <= 30) {
            boolean var18 = false;
            boolean var21 = false;
            int var24 = this.o * 44;
            int var31 = (this.g >> 8) + var2 - 22;
            int var35 = (this.h >> 8) + var3 - 22;
            boolean var15 = false;
            if (var31 < 176 && var31 + 44 >= 0) {
               this.y.b(var1, var31, var35, 44, 44);
               var1.drawImage(h.aM, var31, var35 - var24 - 0, 0);
               return;
            }
         } else if (this.f == 32) {
            int var5 = h.ay.getHeight() / 3;
            int var6 = h.ay.getWidth();
            int var7 = this.o * var5;
            int var8 = (this.g >> 8) + var2 - (var6 >> 1);
            int var9 = (this.h >> 8) + var3 - (var5 >> 1);
            boolean var4 = false;
            if (0 < var5 && var8 < 176 && var8 + var6 >= 0) {
               this.y.b(var1, var8, var9, var6, var5 - 0);
               var1.drawImage(h.ay, var8, var9 - var7 - 0, 0);
               return;
            }
         } else if (this.f == 31) {
            int var16 = (this.g >> 8) + var2 - (h.aO.getWidth() >> 1);
            int var19 = (this.h >> 8) + var3 - (h.aO.getHeight() >> 1);
            int var22 = t * 14 - ((this.g >> 8) - (h.aO.getWidth() >> 1));
            int var25 = u * 9 - ((this.h >> 8) - (h.aO.getHeight() >> 1));
            if (var22 * var22 + var25 * var25 > 1764 && var16 < 176 && var16 + 12 >= 0 && var19 < 220 && var19 + 12 >= 0) {
               var1.setClip(0, v, 176, 220 - v);
               var1.drawImage(h.aO, var16, var19, 0);
               return;
            }
         } else {
            boolean var17 = false;
            boolean var20 = false;
            int var23;
            if ((var23 = e[this.f] * 19) < 0) {
               int var27 = (this.g >> 8) + var2;
               int var33 = (this.h >> 8) + var3;
               if (this.f >= 9 && this.f <= 11) {
                  var27 -= this.m > 0 ? this.p : -this.p;
                  var1.setClip(0, v, 176, 220 - v);
                  if (this.o == 0) {
                     var1.setColor(255, 255, 255);
                  } else {
                     var1.setColor(0, 0, 255);
                  }

                  var1.drawLine(var27, var33, this.m > 0 ? var27 + 168 : var27 - 168, var33 + 11);
                  var1.drawLine(var27, var33, this.m > 0 ? var27 + 168 : var27 - 168, var33 + 5);
                  var1.drawLine(var27, var33, this.m > 0 ? var27 + 168 : var27 - 168, var33 - 5);
                  var1.drawLine(var27, var33, this.m > 0 ? var27 + 168 : var27 - 168, var33 - 11);
                  return;
               }

               if (this.f >= 12 && this.f <= 14) {
                  int var10 = (this.i >> 8) + var2;
                  int var11 = (this.j >> 8) + var3;
                  int var12 = (this.k >> 8) + var2;
                  int var13 = (this.l >> 8) + var3;
                  var1.setClip(0, v, 176, 220 - v);
                  var1.setColor(255, 255, 0);
                  var1.drawLine(var27, var33, var10, var11);
                  var1.drawLine(var10, var11, var12, var13);
                  var1.fillRect(var27 - 1, var33 - 1, 3, 3);
                  return;
               }

               if (this.f >= 15 && this.f <= 17) {
                  if (this.y.aj.ao) {
                     if (this.g < this.y.aj.ab + this.s) {
                        this.g = this.y.aj.ab + this.s;
                     }
                  } else if (this.g > this.y.aj.ab - this.s) {
                     this.g = this.y.aj.ab - this.s;
                  }

                  var27 = (this.g >> 8) + var2;
                  var33 = (this.h >> 8) + var3;
                  var27 -= this.m > 0 ? this.p : -this.p;
                  var1.setClip(0, v, 176, 220 - v);
                  var1.setColor(0, 0, 255);
                  if (this.m > 0) {
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

               var1.setClip(0, v, 176, 220 - v);
               var1.setColor(255, 255, 255);
               var1.fillRect(var27 - 3, var33 - 3, 6, 6);
               return;
            }

            int var26 = (this.g >> 8) + var2 - 9;
            int var32 = (this.h >> 8) + var3 - 9;
            if (var26 >= 176 || var32 >= 220 || var26 + 19 < 0 || var32 + 19 < 0) {
               return;
            }

            boolean var14 = false;
            this.y.b(var1, var26, var32, 19, 19);
            if (this.m > 0) {
               var1.drawImage(h.aL, var26, var32 - var23 - 0, 20);
               return;
            }

            if (this.m == 0) {
               if (this.n < 0) {
                  e.b.drawImage(h.aL, var26 - var23, var32 - 0, 20, 90);
                  return;
               }

               e.b.drawImage(h.aL, var26 - 475 + var23, var32 - 0, 20, 270);
               return;
            }

            e.b.drawImage(h.aL, var26, var32 - var23 - 0, 20, 8192);
         }
      }
   }
}
