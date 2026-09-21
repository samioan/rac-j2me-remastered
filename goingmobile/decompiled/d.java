import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;

public final class d extends h {
   public static byte[] a;
   public static byte[] b;
   public static byte[] c;
   public static byte[] d;
   public static byte[] e;
   public static byte[] f;
   public static byte[] g;
   public static byte[] h;
   public static final byte[] i = new byte[]{6, 10, 16, 5, 8, 10, 5, 5, 5, 15, 15, 15, 22, 22, 22, 5, 5, 5, 5, 5, 5, 5, 5, 5, 3, 0, 0};
   public static final byte[] j = new byte[]{1, 3, 4, 4, 5, 6, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 4, 2, 3, 4, 2, 3, 4, 0, 2, 2};
   public static final byte[] k = new byte[]{1, 2, 3, 1, 2, 3, 1, 1, 1, 2, 2, 2, 3, 3, 3, 1, 1, 1, 2, 2, 2, 3, 3, 3, 0};
   public static final byte[] l = new byte[]{0, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1};
   public static final byte[] m = new byte[]{10, 10, 10, 10, 10};
   public static final short[] n = new short[]{1024, 512, 768, 0, 512};
   public static final byte[] o = new byte[]{0, 20, 5, 5, 0};
   public static byte[][][] p;
   public static byte[][] q;
   public static byte[][] r;
   public boolean s;
   public byte t;
   private f D;
   public byte u;
   public boolean v;
   public boolean w;
   public boolean x;
   public short y;
   public short z;
   public byte A;
   public short B;
   public int C;

   public d(f var1) {
      this.D = var1;
      super.R = -1;
      super.ah = 0;
      this.B = 0;
      super.ag = true;
      this.s = false;
      this.v = false;
      this.w = false;
      this.x = false;
      this.z = 0;
   }

   public final void a() {
      if (p == null) {
         p = new byte[5][6][5];
      }

      if (q == null) {
         q = new byte[5][6];
      }

      if (r == null) {
         r = new byte[5][6];
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

      this.b("/p");
      this.a("/q");
   }

   public final short b() {
      return (short)(super.S * 14 + (super.Y >> 8) + 14 - 22);
   }

   public final short c() {
      return (short)(super.T >> 8);
   }

   private byte f() {
      return (byte)(((super.T >> 8) + 10) / 22);
   }

   private byte g() {
      return (byte)(((super.T >> 8) - 10) / 22);
   }

   private int a(boolean var1) {
      int var2 = (super.T >> 8) / 22;
      boolean var6 = false;
      if (var1) {
         this.y = this.D.N.a(var2, super.S);
      }

      int var3;
      for (var3 = super.S + 1; var3 < 18; var3++) {
         if ((c.e[var2] & 1 << var3) > 0) {
            var3 *= 14;
            var6 = true;
            break;
         }
      }

      if (!var6) {
         var3 = 252;
      }

      var2 = super.T >> 8;

      for (int var7 = 0; var7 < 20; var7++) {
         if (this.D.aF[var7] != -1
            && this.D.aD[var7] <= var2 + 4
            && this.D.aD[var7] + 16 >= var2 - 4
            && this.D.aE[var7] < var3
            && this.D.aE[var7] > super.S * 14) {
            var3 = this.D.aE[var7];
         }
      }

      return var3;
   }

   private boolean h() {
      int var6 = super.T >> 8;
      int var7 = this.b() + 22 - 14;

      for (int var10 = 19; var10 >= 0; var10--) {
         if (this.D.aF[var10] >= 0
            && Math.abs(this.D.aD[var10] - var6) <= 22
            && this.D.aE[var10] == var7
            && this.D.a(this.D.aD[var10], this.D.aE[var10], 16, 16, var6 - 4, var7, 8, 13)) {
            return true;
         }
      }

      return false;
   }

   private void i() {
      int var1 = this.a(true) / 14;
      this.x = false;
      this.v = false;
      this.w = false;
      if (super.S + 1 < var1 && super.R != 3 || super.ad == 2) {
         this.a((byte)2);
         super.U = (short)(super.U + 256);
         super.Y = super.Y + super.U;
         this.q();
         if (super.S + 1 == var1) {
            this.a((byte)0);
            super.U = 0;
            super.Y = 0;
         }

         if (this.D.a(this.c() - a[super.R], this.b() + b[super.R], c[super.R], d[super.R], this.D.X.b() + 0 - 6, this.D.X.c() + -6 + 3, 12, 20)) {
            super.V = 0;
            if (super.U > 0) {
               super.U = (short)(-super.U / 2);
               if (this.D.X.b() < this.c()) {
                  super.V = 510;
                  super.ag = false;
               } else {
                  super.V = -510;
                  super.ag = true;
               }
            } else {
               super.U = (short)(-super.U);
            }
         }
      }

      if (super.V != 0) {
         super.W = super.ag ? this.f() : this.g();
         super.X = super.S;
         int var2 = super.T;
         super.T = super.T + (super.ag ? 2816 : -2816);
         int var3 = this.a(false) / 14;
         this.w = false;
         if (var3 > var1) {
            this.w = true;
         }

         if (this.u != 0 && this.w) {
            super.V = 0;
         }

         super.T = var2;
         if (super.W > 0 && super.W < 27 && this.D.N.b(super.W, super.X)) {
            super.T = super.T + super.V;
            if (this.h()) {
               super.T = super.T - super.V;
               if (super.ad != 2) {
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
      if (super.ad == 5) {
         if (super.ae == 2) {
            super.R = -1;
         }
      } else if (super.ad == 4 && this.u == 0) {
         if (++super.af > m[super.R]) {
            super.V = 0;
            super.ae = 2;
            this.a((byte)0);
         }
      } else if (Math.abs(this.D.X.b() - this.c()) <= 110 && Math.abs(this.D.X.c() - this.b()) <= 112) {
         if (super.R != 3 || Math.abs(this.D.X.b() - this.c()) <= 77 && Math.abs(this.D.X.c() - this.b()) <= 70) {
            this.i();
            if (this.u != 0 && super.R != 3) {
               super.V = (short)(this.u << 8);
               super.U = 0;
               this.a((byte)4);
               d var7;
               boolean var10001;
               if (this.u > 0) {
                  this.u--;
                  var7 = this;
                  var10001 = true;
               } else {
                  this.u++;
                  var7 = this;
                  var10001 = false;
               }

               var7.ag = var10001;
               if (this.x) {
                  super.V = 0;
               }
            } else {
               int var1 = this.D.X.b() - this.c();
               int var2 = this.D.X.c() + 14 - 22 - this.b();
               if (super.R == 0) {
                  label317: {
                     if (this.v) {
                        super.ag = !super.ag;
                     } else if (this.x) {
                        if (this.y == -96) {
                           super.U = -510;
                           this.a((byte)2);
                           return;
                        }

                        super.ag = !super.ag;
                     } else {
                        if (!this.w) {
                           break label317;
                        }

                        if (this.y == -97) {
                           super.U = -768;
                           super.V = (short)(n[super.R] << 2);
                           if (!super.ag) {
                              super.V = (short)(super.V * -1);
                           }

                           this.a((byte)2);
                           return;
                        }

                        if (this.y == -96) {
                           break label317;
                        }

                        super.ag = !super.ag;
                     }

                     this.B = 10;
                  }

                  if (super.ad == 2) {
                     return;
                  }

                  if (var1 < 66 && var1 > 22 && Math.abs(var2) < 14 && this.B == 0) {
                     if (super.ad != 1) {
                        this.a((byte)1);
                     }

                     super.V = n[super.R];
                     super.ag = true;
                  } else if (var1 < -22 && var1 > -66 && Math.abs(var2) < 14 && this.B == 0) {
                     if (super.ad != 1) {
                        this.a((byte)1);
                     }

                     super.V = (short)(-n[super.R]);
                     super.ag = false;
                  } else if (Math.abs(var1) <= 22 && var2 == 0) {
                     super.V = 0;
                     this.D.a(super.T, this.b() + 7 << 8, 21, super.ag, false, 0, this.t);
                     super.R = -1;
                  } else {
                     this.j();
                  }

                  if (this.B != 0) {
                     this.B--;
                     return;
                  }
               } else {
                  d var10000;
                  if (super.R == 1 && super.ad != 2) {
                     if (this.y == -95) {
                        if (var2 < 0 && Math.abs(var1) > 11) {
                           super.U = -3584;
                           super.V = 0;
                           this.a((byte)2);
                        }
                     } else if (this.v) {
                        super.ag = !super.ag;
                     } else if (this.x) {
                        if (this.y == -96) {
                           super.U = -1700;
                           this.a((byte)2);
                           return;
                        }

                        super.ag = !super.ag;
                     } else if (this.w) {
                        if (this.y == -97) {
                           super.U = -3584;
                           super.V = (short)(n[super.R] << 2);
                           if (!super.ag) {
                              super.V = (short)(super.V * -1);
                           }

                           this.a((byte)2);
                           return;
                        }

                        if (this.y != -96) {
                           super.ag = !super.ag;
                        }
                     }

                     if (super.ad == 2) {
                        return;
                     }

                     if (Math.abs(var1) <= 22 && Math.abs(var2) <= 22 && Math.abs(var2) < 14) {
                        super.V = 0;
                        this.k();
                        return;
                     }

                     var10000 = this;
                  } else if (super.R == 2) {
                     if (this.v) {
                        super.V = 0;
                        super.ae = 1;
                        this.a((byte)0);
                        super.ag = !super.ag;
                     } else if (this.x) {
                        super.V = 0;
                        super.ae = 1;
                        this.a((byte)0);
                        super.ag = !super.ag;
                     } else if (this.w) {
                        super.V = 0;
                        super.ae = 1;
                        this.a((byte)0);
                        super.ag = !super.ag;
                     }

                     int var5 = Math.abs(this.C - super.T) >> 8;
                     if (this.A != 0) {
                        if (this.A == 1) {
                           if (this.B < l[this.t]) {
                              this.l();
                              return;
                           }

                           if (super.ae == 2 && super.ad == 0) {
                              this.A = 0;
                              return;
                           }

                           if (super.ad != 0) {
                              super.ae = 1;
                              this.a((byte)0);
                              return;
                           }
                        }

                        return;
                     }

                     if ((var1 > 0 && super.ag || var1 < 0 && !super.ag) && Math.abs(var2) < 14 && Math.abs(var1) < 66) {
                        super.V = 0;
                        this.A = 1;
                        this.B = 0;
                        this.z = 0;
                        return;
                     }

                     if ((this.C > super.T && !super.ag || this.C < super.T && super.ag) && var5 >= 66) {
                        super.V = 0;
                        super.ae = 1;
                        this.a((byte)0);
                        super.ag = !super.ag;
                        return;
                     }

                     if (super.ad == 0 && (super.ae != 2 || super.ad != 0)) {
                        return;
                     }

                     var10000 = this;
                  } else {
                     if (super.R == 3) {
                        if (this.A == 0) {
                           if (this.B < l[this.t]) {
                              this.l();
                           } else {
                              this.B = 0;
                              this.A = 1;
                           }
                        }

                        if (this.A == 1 && this.B++ > 10) {
                           if (super.ag && !this.s) {
                              this.s = true;
                           } else if (super.ag) {
                              this.s = super.ag = false;
                           } else {
                              super.ag = true;
                           }

                           this.A = 0;
                           this.B = 0;
                           return;
                        }

                        return;
                     }

                     if (super.R != 4) {
                        return;
                     }

                     if (this.v) {
                        super.ag = !super.ag;
                     } else if (this.x) {
                        super.ag = !super.ag;
                     } else if (this.w) {
                        super.ag = !super.ag;
                     }

                     if (Math.abs(this.D.O.nextInt()) % 20 == 0) {
                        super.ag = !super.ag;
                     }

                     var10000 = this;
                  }

                  var10000.j();
               }
            }
         }
      } else {
         super.V = super.U = 0;
      }
   }

   public final void e() {
      if (super.ae != 2) {
         if (super.R == -1) {
            super.ab = super.ac = 0;
         } else {
            super.ac++;
            if (super.ac > r[super.R][super.ad]) {
               super.ac = 0;
               super.ab++;
               if (super.ab >= q[super.R][super.ad]) {
                  if (super.ae == 0) {
                     super.ab = 0;
                  } else {
                     super.ab--;
                     super.ae = 2;
                  }

                  if (super.ad == 3) {
                     this.a((byte)0);
                     if (super.R == 1) {
                        super.ag = !super.ag;
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
      int var6 = super.Z;
      int var3 = this.c() + f.r - 11;
      int var4 = this.b() + f.s;
      if (var3 >= -22 && var3 <= 128 && var4 >= -22 && var4 <= 128) {
         if (var4 + 22 > 20 && var4 < 128) {
            int var7 = var4;
            var5 = 0;
            if (var7 < 20) {
               var5 = 20 - var7;
               var7 = 20;
            }

            var1.setClip(var3, var7, 22, 22 - var5);
            if (super.ah == 0 && super.ag) {
               if ((var6 & 1) > 0) {
                  byte var8 = 0;
                  if (this.s) {
                     var8 = 44;
                  }

                  var1.drawImage(f.ab, var3, var4 - p[super.R][super.ad][super.ab] * 22 - var8, 20);
                  return;
               }
            } else if ((var6 & 1) > 0) {
               this.D.a(var1, var2, var3, var4);
            }
         }
      }
   }

   private void j() {
      if (super.ad != 1) {
         this.a((byte)1);
      }

      super.ae = 0;
      super.V = (short)(super.ag ? n[super.R] : -n[super.R]);
   }

   private void k() {
      if (super.ad != 3) {
         if (this.D.X.ad != 10) {
            super.ag = false;
            if (this.D.X.b() - this.c() > 0) {
               super.ag = true;
            }

            if (--this.z < 0) {
               this.z = o[super.R];
               if (this.D.Q != 0) {
                  this.a((byte)3);
               }

               if (this.D.X.B > 0) {
                  return;
               }

               if (this.D
                     .a(
                        this.D.X.b() - 6 + 0,
                        this.D.X.c() + 3 + -6,
                        12,
                        20,
                        this.c() + (super.ag ? e[super.R] : -e[super.R] - g[super.R]),
                        this.b() + f[super.R],
                        g[super.R],
                        h[super.R]
                     )
                  && this.D.Q != 0) {
                  if (!f.i) {
                     this.D.X.aa = (byte)(this.D.X.aa - j[this.t]);
                  }

                  this.D.X.a((byte)9);
                  this.D.X.af = 0;
                  if (this.D.X.p == 2) {
                     this.D.X.p = 1;
                  }

                  this.D.e = true;
               }
            }
         }
      }
   }

   private void l() {
      if (--this.z < 0) {
         label60: {
            this.a((byte)3);
            super.ae = 1;
            f var10000;
            int var10001;
            int var10002;
            byte var10003;
            if (this.t == 6 || this.t == 9 || this.t == 12 || this.t == 15 || this.t == 18 || this.t == 21) {
               var10000 = this.D;
               var10001 = super.T;
               var10002 = this.b() + 11 << 8;
               var10003 = 0;
            } else if (this.t == 7 || this.t == 10 || this.t == 13 || this.t == 16 || this.t == 19 || this.t == 22) {
               var10000 = this.D;
               var10001 = super.T;
               var10002 = this.b() + 11 << 8;
               var10003 = 6;
            } else if (this.t != 7 && this.t != 10 && this.t != 13) {
               if (this.t != 17 && this.t != 20 && this.t != 23) {
                  break label60;
               }

               var10000 = this.D;
               var10001 = super.T;
               var10002 = this.b() + 11 << 8;
               var10003 = 3;
            } else {
               var10000 = this.D;
               var10001 = super.T;
               var10002 = this.b() + 11 << 8;
               var10003 = 18;
            }

            var10000.a(var10001, var10002, var10003, super.ag, this.s, super.ah, this.t);
         }

         this.z = o[super.R];
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
               q[var6][var7] = var2;
               r[var6][var7] = var3;
               a(var5, var4, var2);
               System.arraycopy(var4, 0, p[var6][var7], 0, var2);
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
