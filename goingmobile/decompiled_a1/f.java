import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;

public final class f extends i {
   public static byte[] a;
   public static byte[] b;
   public static byte[] c;
   public static byte[] d;
   public static byte[] e;
   public static byte[] f;
   public static byte[] g;
   public static byte[] h;
   public static final byte[] i = new byte[]{6, 10, 16, 5, 8, 10, 5, 5, 5, 15, 15, 15, 22, 22, 22, 5, 5, 5, 5, 5, 5, 5, 5, 5, 3, 0, 0};
   public static final byte[] j = new byte[]{1, 3, 4, 4, 5, 6, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 4, 2, 3, 4, 2, 3, 4, 0, 2, 4};
   public static final byte[] k = new byte[]{1, 2, 3, 1, 2, 3, 1, 1, 1, 2, 2, 2, 3, 3, 3, 1, 1, 1, 2, 2, 2, 3, 3, 3, 0};
   public static final byte[] l = new byte[]{0, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1};
   public static final short[] m = new short[]{1024, 512, 768, 0, 512};
   public static final byte[] n = new byte[]{0, 20, 5, 5, 0};
   public static byte[][][] o;
   public static byte[][] p;
   public static byte[][] q;
   public static byte r;
   public static byte s;
   public static short t;
   public boolean u;
   public byte v;
   private h H;
   public byte w;
   public boolean x;
   public boolean y;
   public boolean z;
   public short A;
   public short B;
   public byte C;
   public byte D;
   public short E;
   public int F;
   public boolean G;

   public f(h var1) {
      this.H = var1;
      r = h.F;
      s = h.G;
      t = h.H;
      super.Z = -1;
      super.ap = 0;
      this.E = 0;
      super.ao = true;
      this.u = false;
      this.x = false;
      this.y = false;
      this.z = false;
      this.B = 0;
   }

   public final void a() {
      if (o == null) {
         o = new byte[5][8][5];
      }

      if (p == null) {
         p = new byte[5][8];
      }

      if (q == null) {
         q = new byte[5][8];
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

      this.b("/enemy.bin");
      this.a("/enemy_spr_box.bin");
   }

   public final short b() {
      return (short)(super.aa * s + (super.ag >> 8) + s - h.K);
   }

   public final short c() {
      return (short)(super.ab >> 8);
   }

   public final byte d() {
      return (byte)(((super.ab >> 8) + 12) / r);
   }

   public final byte e() {
      return (byte)(((super.ab >> 8) - 12) / r);
   }

   public final int a(boolean var1) {
      int var2 = (super.ab >> 8) / r;
      boolean var4 = false;
      int var5 = h.aJ.getWidth();
      boolean var6 = false;
      if (var1) {
         this.A = this.H.V.a(var2, super.aa);
      }

      int var3;
      for (var3 = super.aa + 1; var3 < 18; var3++) {
         if ((d.e[var2] & 1 << var3) > 0) {
            var3 *= s;
            var6 = true;
            break;
         }
      }

      if (!var6) {
         var3 = 18 * s;
      }

      var2 = super.ab >> 8;

      for (int var7 = 0; var7 < 50; var7++) {
         if (this.H.bn[var7] != -1
            && this.H.bl[var7] <= var2 + 8
            && this.H.bl[var7] + var5 >= var2 - 8
            && this.H.bm[var7] < var3
            && this.H.bm[var7] > super.aa * s) {
            var3 = this.H.bm[var7];
         }
      }

      return var3;
   }

   public final boolean f() {
      int var1 = h.aJ.getWidth();
      int var2 = h.aJ.getHeight() >> 3;
      byte var3 = r;
      byte var4 = s;
      boolean var5 = false;
      int var6 = super.ab >> 8;
      int var7 = this.b() + h.K - s;
      boolean var8 = false;
      int var9 = var4 - 1;

      for (int var10 = 49; var10 >= 0; var10--) {
         if (this.H.bn[var10] >= 0
            && this.a(this.H.bl[var10] - var6) <= var3
            && this.H.bm[var10] == var7
            && this.H.a(this.H.bl[var10], this.H.bm[var10], var1, var2, var6 - 8, var7, 16, var9)) {
            return true;
         }
      }

      return false;
   }

   public final void g() {
      int var1 = this.a(true) / s;
      this.z = false;
      this.x = false;
      this.y = false;
      if (super.aa + 1 < var1 && super.Z != 3 || super.al == 2) {
         this.a((byte)2);
         super.ac = (short)(super.ac + 256);
         super.ag = super.ag + super.ac;
         this.v();
         if (super.aa + 1 == var1 && super.ac > 0) {
            this.a((byte)0);
            super.ac = 0;
            super.ag = 0;
         }

         if (this.H.a(this.c() - a[super.Z], this.b() + b[super.Z], c[super.Z], d[super.Z], this.H.aj.b() - 11, this.H.aj.c() + h.L + 7, 18, 37)) {
            super.ad = 0;
            if (super.ac > 0) {
               super.ac = (short)(-super.ac / 2);
               if (this.H.aj.b() < this.c()) {
                  super.ad = 510;
                  super.ao = false;
               } else {
                  super.ad = -510;
                  super.ao = true;
               }
            } else {
               super.ac = (short)(-super.ac);
            }
         }
      }

      if (super.ad != 0) {
         super.ae = super.ao ? this.d() : this.e();
         super.af = super.aa;
         int var2 = super.ab;
         super.ab = super.ab + (super.ao ? r << 7 : -(r << 7));
         int var3 = this.a(false) / s;
         this.y = false;
         if (var3 > var1) {
            this.y = true;
         }

         if (this.w != 0 && this.y) {
            super.ad = 0;
         }

         super.ab = var2;
         if (super.ae > 0 && super.ae < 27 && this.H.V.b(super.ae, super.af)) {
            super.ab = super.ab + super.ad;
            if (this.f()) {
               super.ab = super.ab - super.ad;
               if (super.al != 2) {
                  this.x = true;
                  return;
               }
            }
         } else {
            this.z = true;
         }
      }
   }

   public final void h() {
      if (super.al == 5) {
         if (super.am == 2) {
            super.Z = -1;
         }
      } else if (super.al != 7 && super.al != 6) {
         if (this.a(this.H.aj.b() - this.c()) <= 176 && this.a(this.H.aj.c() - this.b()) <= 220) {
            if (super.Z != 3 || this.a(this.H.aj.b() - this.c()) <= 3 * r + (r >> 1) && this.a(this.H.aj.c() - this.b()) <= 5 * s) {
               this.g();
               if (this.w != 0 && super.Z != 3) {
                  super.ad = (short)(this.w << 8);
                  super.ac = 0;
                  this.a((byte)4);
                  if (this.w > 0) {
                     this.w--;
                     super.ao = true;
                  } else {
                     this.w++;
                     super.ao = false;
                  }

                  if (this.z) {
                     super.ad = 0;
                  }
               } else {
                  this.G = false;
                  int var1 = this.H.aj.b() - this.c();
                  int var2 = this.H.aj.c() + s - h.K - this.b();
                  byte var3 = r;
                  int var4 = r * 3;
                  if (super.Z == 0) {
                     if (this.x) {
                        super.ao = !super.ao;
                        this.E = 10;
                     } else if (this.z) {
                        if (this.A == -96) {
                           super.ac = -510;
                           this.a((byte)2);
                           return;
                        }

                        super.ao = !super.ao;
                        this.E = 10;
                     } else if (this.y) {
                        if (this.A == -97) {
                           super.ac = -768;
                           super.ad = (short)(m[super.Z] << 2);
                           if (!super.ao) {
                              super.ad = (short)(super.ad * -1);
                           }

                           this.a((byte)2);
                           return;
                        }

                        if (this.A != -96) {
                           super.ao = !super.ao;
                           this.E = 10;
                        }
                     }

                     if (super.al == 2) {
                        return;
                     }

                     if (var1 < var4 && var1 > var3 && this.a(var2) < s && this.E == 0) {
                        if (super.al != 1) {
                           this.a((byte)1);
                        }

                        super.ad = m[super.Z];
                        super.ao = true;
                     } else if (var1 < -var3 && var1 > -var4 && this.a(var2) < s && this.E == 0) {
                        if (super.al != 1) {
                           this.a((byte)1);
                        }

                        super.ad = (short)(-m[super.Z]);
                        super.ao = false;
                     } else if (this.a(var1) <= var3 && var2 == 0) {
                        super.ad = 0;
                        this.H.a(super.ab, this.b() + (s >> 1) << 8, 21, super.ao, false, 0, this.v);
                        super.Z = -1;
                        short var7 = this.H.X;
                        if (this.H.cV) {
                           var7 = 0;
                        }

                        int var6 = this.C + var7 * 10 >> 5;
                        this.H.bv[var6] = this.H.bv[var6] & ~(1 << this.C + var7 * 10 - (var6 << 5));
                     } else {
                        this.j();
                     }

                     if (this.E != 0) {
                        this.E--;
                        return;
                     }
                  } else {
                     if (super.Z == 1 && super.al != 2) {
                        if (this.A == -95) {
                           if (var2 < 0 && this.a(var1) > r / 2) {
                              super.ac = -3584;
                              super.ad = 0;
                              this.a((byte)2);
                           }
                        } else if (this.x) {
                           super.ao = !super.ao;
                        } else if (this.z) {
                           if (this.A == -96 && this.a(var1) > r / 2) {
                              super.ac = -1700;
                              this.a((byte)2);
                              return;
                           }

                           super.ao = !super.ao;
                        } else if (this.y) {
                           if (this.A == -97 && this.a(var1) > r / 2) {
                              super.ac = -3584;
                              super.ad = (short)(m[super.Z] << 2);
                              if (!super.ao) {
                                 super.ad = (short)(super.ad * -1);
                              }

                              this.a((byte)2);
                              return;
                           }

                           if (this.A != -96) {
                              super.ao = !super.ao;
                           }
                        }

                        if (super.al == 2) {
                           return;
                        }

                        if (this.a(var1) <= var3 && this.a(var2) <= var3 && this.a(var2) < s) {
                           super.ad = 0;
                           this.k();
                           return;
                        }

                        this.j();
                        return;
                     }

                     if (super.Z == 2) {
                        if (this.x) {
                           super.ad = 0;
                           super.am = 1;
                           this.a((byte)0);
                           super.ao = !super.ao;
                        } else if (this.z) {
                           super.ad = 0;
                           super.am = 1;
                           this.a((byte)0);
                           super.ao = !super.ao;
                        } else if (this.y) {
                           super.ad = 0;
                           super.am = 1;
                           this.a((byte)0);
                           super.ao = !super.ao;
                        }

                        int var5 = this.a(this.F - super.ab) >> 8;
                        if (this.D != 0) {
                           if (this.D == 1) {
                              if (this.E < l[this.v]) {
                                 this.l();
                                 return;
                              }

                              if (super.am == 2 && super.al == 0) {
                                 this.D = 0;
                                 return;
                              }

                              if (super.al != 0) {
                                 super.am = 1;
                                 this.a((byte)0);
                                 return;
                              }
                           }
                        } else {
                           if ((var1 > 0 && super.ao || var1 < 0 && !super.ao) && this.a(var2) < s && this.a(var1) < r * 3) {
                              super.ad = 0;
                              this.D = 1;
                              this.E = 0;
                              this.B = 0;
                              return;
                           }

                           if ((this.F > super.ab && !super.ao || this.F < super.ab && super.ao) && var5 >= r * 3) {
                              super.ad = 0;
                              super.am = 1;
                              this.a((byte)0);
                              super.ao = !super.ao;
                              return;
                           }

                           if (super.al != 0 || super.am == 2 && super.al == 0) {
                              this.j();
                              return;
                           }
                        }
                     } else if (super.Z == 3) {
                        if (this.D == 0) {
                           if (this.E < l[this.v]) {
                              this.l();
                           } else {
                              this.E = 0;
                              this.D = 1;
                           }
                        }

                        if (this.D == 1 && this.E++ > 10) {
                           if (super.ao && !this.u) {
                              this.u = true;
                           } else if (super.ao) {
                              this.u = super.ao = false;
                           } else {
                              super.ao = true;
                           }

                           this.D = 0;
                           this.E = 0;
                           return;
                        }
                     } else if (super.Z == 4) {
                        if (this.x) {
                           super.ao = !super.ao;
                        } else if (this.z) {
                           super.ao = !super.ao;
                        } else if (this.y) {
                           super.ao = !super.ao;
                        }

                        this.j();
                     }
                  }
               }
            }
         } else {
            super.ad = super.ac = 0;
         }
      }
   }

   public final void i() {
      if (super.am != 2) {
         if (super.Z == -1) {
            super.aj = super.ak = 0;
         } else {
            super.ak++;
            if (super.ak > q[super.Z][super.al]) {
               super.ak = 0;
               super.aj++;
               if (super.aj >= p[super.Z][super.al]) {
                  if (super.am == 0) {
                     super.aj = 0;
                  } else {
                     super.aj--;
                     super.am = 2;
                  }

                  if (super.al == 6 || super.al == 7) {
                     this.a((byte)0);
                  }

                  if (super.al == 3) {
                     this.a((byte)0);
                     if (super.Z == 1) {
                        super.ao = !super.ao;
                        this.E = 20;
                     }
                  }
               }

               this.H.d = true;
            }
         }
      }
   }

   public final void a(Graphics var1, int var2, int var3, int var4, int var5) {
      boolean var8 = false;
      h.x = (short)var4;
      h.y = (short)var5;
      int var6 = this.c() + h.x - (h.J >> 1);
      int var7 = this.b() + h.y;
      if (var6 >= -h.J && var6 < 176 && var7 >= -h.K && var7 < 220) {
         if ((var3 & 1) > 0 && var7 + h.K > t && var7 < 220) {
            byte var9 = h.J;
            this.H.b(var1, var6, var7, var9, h.K - 0);
            if (super.ap == 0 && super.ao) {
               int var10 = 0;
               if (this.u) {
                  var10 = h.K << 1;
               }

               var1.drawImage(h.aG[super.Z], var6, var7 - o[super.Z][super.al][super.aj] * h.K - 0 - var10, 20);
               return;
            }

            this.H.c(var1, var2, var6, var7, 0);
         }
      }
   }

   public final void j() {
      if (super.al != 1) {
         this.a((byte)1);
      }

      super.am = 0;
      super.ad = (short)(super.ao ? m[super.Z] : -m[super.Z]);
   }

   public final void k() {
      if (super.al != 3) {
         if (this.H.aj.al != 10) {
            super.ao = false;
            if (this.H.aj.b() - this.c() > 0) {
               super.ao = true;
            }

            if (--this.B < 0) {
               this.B = n[super.Z];
               if (this.H.Z != 0) {
                  this.a((byte)3);
               }

               if (this.H.aj.E > 0) {
                  return;
               }

               if (this.H
                     .a(
                        this.H.aj.b() - 11,
                        this.H.aj.c() + 7 + h.L,
                        18,
                        37,
                        this.c() + (super.ao ? e[super.Z] : -e[super.Z] - g[super.Z]),
                        this.b() + f[super.Z],
                        g[super.Z],
                        h[super.Z]
                     )
                  && this.H.Z != 0) {
                  if (!h.f) {
                     this.H.aj.ai = (byte)(this.H.aj.ai - j[this.v]);
                  }

                  this.H.ab = 1;
                  this.H.aj.a((byte)9);
                  this.H.aj.an = 0;
                  if (this.H.aj.s == 2) {
                     this.H.aj.s = 1;
                  }

                  this.H.e = true;
               }
            }
         }
      }
   }

   public final void l() {
      if (--this.B < 0) {
         this.a((byte)3);
         super.am = 1;
         if (this.v == 6 || this.v == 9 || this.v == 12 || this.v == 15 || this.v == 18 || this.v == 21) {
            this.H.a(super.ab, this.b() + (h.K >> 1) << 8, 0, super.ao, this.u, super.ap, this.v);
         } else if (this.v == 7 || this.v == 10 || this.v == 13 || this.v == 16 || this.v == 19 || this.v == 22) {
            this.H.a(super.ab, this.b() + (h.K >> 1) << 8, 6, super.ao, this.u, super.ap, this.v);
         } else if (this.v == 7 || this.v == 10 || this.v == 13) {
            this.H.a(super.ab, this.b() + (h.K >> 1) << 8, 18, super.ao, this.u, super.ap, this.v);
         } else if (this.v == 17 || this.v == 20 || this.v == 23) {
            this.H.a(super.ab, this.b() + (h.K >> 1) << 8, 3, super.ao, this.u, super.ap, this.v);
         }

         this.B = n[super.Z];
         this.E++;
      }
   }

   private void a(InputStream var1, byte[] var2, int var3, boolean var4) {
      byte var5;
      if (var4) {
         var5 = h.J;
      } else {
         var5 = h.K;
      }

      try {
         for (int var6 = 0; var6 < var3; var6++) {
            var2[var6] = (byte)var1.read();
            var2[var6] = (byte)(var2[var6] * var5 / 44);
         }
      } catch (IOException var7) {
      }
   }

   public final void a(String var1) {
      byte[] var3 = new byte[5];

      try {
         InputStream var4 = var3.getClass().getResourceAsStream(var1);
         this.a(var4, var3, 5, true);
         System.arraycopy(var3, 0, a, 0, var3.length);
         this.a(var4, var3, 5, false);
         System.arraycopy(var3, 0, b, 0, var3.length);
         this.a(var4, var3, 5, true);
         System.arraycopy(var3, 0, c, 0, var3.length);
         this.a(var4, var3, 5, false);
         System.arraycopy(var3, 0, d, 0, var3.length);
         this.a(var4, var3, 5, true);
         System.arraycopy(var3, 0, e, 0, var3.length);
         this.a(var4, var3, 5, false);
         System.arraycopy(var3, 0, f, 0, var3.length);
         this.a(var4, var3, 5, true);
         System.arraycopy(var3, 0, g, 0, var3.length);
         this.a(var4, var3, 5, false);
         System.arraycopy(var3, 0, h, 0, var3.length);
         var4.close();
      } catch (IOException var5) {
      }
   }

   private void b(String var1) {
      byte[] var4 = new byte[5];

      try {
         InputStream var5 = var4.getClass().getResourceAsStream(var1);

         for (int var6 = 0; var6 < 5; var6++) {
            for (int var7 = 0; var7 < 8; var7++) {
               byte var2 = (byte)var5.read();
               byte var3 = (byte)var5.read();
               p[var6][var7] = var2;
               q[var6][var7] = var3;
               this.a(var5, var4, var2);
               System.arraycopy(var4, 0, o[var6][var7], 0, var2);
            }
         }

         var5.close();
      } catch (IOException var8) {
      }
   }

   private void a(InputStream var1, byte[] var2, int var3) {
      try {
         for (int var4 = 0; var4 < var3; var4++) {
            var2[var4] = (byte)var1.read();
         }
      } catch (IOException var5) {
      }
   }

   public final int a(int var1) {
      return var1 < 0 ? var1 * -1 : var1;
   }

   static {
      byte[] var10000 = new byte[]{10, 10, 10, 10, 10};
   }
}
