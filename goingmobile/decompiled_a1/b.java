import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;

public final class b extends i {
   public static final byte[] a = new byte[]{-2, -2, -2, 2, 0, -1, 1, -1, -2, 0, 0, -1, 0, 44, 44, 44, 44, 44, 44, -2, -3, 0, 0, 0, 44, 44, 44, 44};
   public static final byte[] b = new byte[]{15, 13, 13, 12, 13, 14, 15, 7, 7, 12, 17, 20, 12, 44, 44, 44, 44, 44, 44, 14, 15, 12, 13, 13, 44, 44, 44, 44};
   public static final byte[][] c = new byte[][]{
      {0, 0, 20, 35, 11, 9},
      {0, 119, 20, 36, 11, 8},
      {20, 119, 20, 36, 11, 8},
      {20, 0, 25, 35, 8, 9},
      {0, -101, 30, 31, 5, 8},
      {108, 0, 27, 37, 7, 7},
      {57, 120, 23, 34, 9, 10},
      {0, 79, 20, 40, 10, 1},
      {30, -101, 25, 31, 9, 4},
      {-121, 0, 23, 39, 7, 5},
      {68, -102, 23, 29, 10, 15},
      {40, 119, 17, 36, 13, 8},
      {91, 35, 20, 43, 10, 1},
      {0, 35, 30, 44, 0, 0},
      {111, 39, 44, 42, 0, 2},
      {108, 121, 43, 28, 1, 16},
      {125, 81, 24, 36, 8, 2},
      {20, 79, 44, 40, 0, 0},
      {64, 79, 36, 41, 7, 3},
      {45, 0, 26, 35, 9, 9},
      {80, 120, 28, 34, 7, 10},
      {100, 81, 25, 40, 6, 3},
      {30, 35, 31, 44, 1, 0},
      {61, 35, 31, 44, 1, 0},
      {91, -102, 24, 33, 11, 9},
      {71, 0, 37, 35, 7, 9},
      {115, -107, 41, 27, 2, 16},
      {115, -80, 40, 11, 1, 33}
   };
   public static final short[] d = new short[]{0, 0, 0, 100, 150, 200, 35, 45, 60, 25, 30, 40, 40, 55, 70, 200, 250, 300, 1, 1, 1, 70, 90, 110};
   public static final short[] e = new short[]{0, 50, 20, 15, 20, 100, 1, 30};
   public static final short[] f = new short[]{0, 15, 7, 5, 8, 30, 1, 10};
   public static final byte[][] g = new byte[][]{{0, 4, 16, 20, 16, 5, 4, 5}, {0, 4, 16, 20, 16, 5, 4, 5}, {0, 4, 16, 20, 16, 5, 4, 5}};
   public static final int[] h = new int[]{50, 800, 2500, 18000, 0};
   public static final byte[] i = new byte[]{3, 8, 8};
   private h aq;
   public static byte j;
   public static byte k;
   public static short l;
   public static short m;
   public static short n;
   public static final byte[] o = new byte[]{14, -11};
   public static final byte[] p = new byte[]{4, 17};
   public static final byte[] q = new byte[]{10, 20};
   public static final byte[] r = new byte[]{10, 50};
   public byte s;
   public byte t;
   public byte u;
   public byte v;
   public int w;
   public int x;
   public int y;
   public int z;
   public int A;
   public int B;
   public byte C;
   public byte D;
   public byte E;
   public byte F;
   public boolean G;
   public short H;
   private boolean ar;
   public static byte[][][] I;
   public static byte[][] J;
   public static byte[][] K;
   public byte L;
   public short[] M;
   public short[] N;
   public byte O;
   public byte[] P;
   public int Q;
   public byte R;
   public short[] S;
   public short[] T;
   public byte[] U;
   public int V;
   public int W;
   public int X;
   public boolean Y;

   public b(h var1) {
      this.aq = var1;
      j = h.F;
      k = h.G;
      l = h.H;
      n = 1536;
      this.s = -1;
      this.u = -2;
      this.C = -1;
      this.D = 0;
      this.G = false;
      this.t = 0;
      this.H = 0;
      this.ar = false;
      this.M = new short[8];
      this.N = new short[8];
      this.P = new byte[8];
      this.T = new short[8];
      this.S = new short[8];
      this.U = new byte[8];
      super.ac = 0;
      super.ad = 0;
      super.ap = 0;
      super.ao = true;
      super.ai = 20;
      m = (short)(k >> 1 << 8);
   }

   public final void a() {
      if (I == null) {
         I = new byte[1][15][4];
      }

      if (J == null) {
         J = new byte[1][15];
      }

      if (K == null) {
         K = new byte[1][15];
      }

      this.a("/player.bin");
   }

   public final short b() {
      return (short)(super.ab >> 8);
   }

   public final short c() {
      return (short)(super.aa * k + (super.ag >> 8));
   }

   public final byte d() {
      return (byte)(((super.ab >> 8) + 8) / j);
   }

   public final byte e() {
      return (byte)(((super.ab >> 8) - 8) / j);
   }

   public final short a(boolean var1) {
      int var2 = 17 * k;
      int var3 = 4 * k / 44;
      if (var1) {
         var3 = 18 * k / 44;
      }

      if (super.ag > var3 << 8) {
         if ((d.e[this.d()] & 1 << super.aa + 2) > 0 || (d.e[this.e()] & 1 << super.aa + 2) > 0) {
            var2 = (super.aa + 1) * k;
         }
      } else if ((d.e[this.d()] & 1 << super.aa + 1) > 0 || (d.e[this.e()] & 1 << super.aa + 1) > 0) {
         var2 = super.aa * k;
      }

      short var4 = this.aq.C();
      short var5 = (short)this.aq.D(var2, var4);
      short var6 = this.aq.B();
      this.G = false;
      if (var6 < var5) {
         this.G = true;
         return var6;
      } else {
         return var5;
      }
   }

   public final boolean f() {
      int var1;
      int var2 = (var1 = super.ab >> 8) / j;
      int var3;
      if ((var3 = var1 % j) < 10) {
         if (this.aq.V.f[var2 - 1][super.aa + 1] != 19) {
            return false;
         }
      } else if (var3 > j - 10 && this.aq.V.f[var2 + 1][super.aa + 1] != 19) {
         return false;
      }

      return this.aq.V.f[var2][super.aa + 1] == 19;
   }

   public final boolean g() {
      return !this.aq.V.b(this.u() - 1, super.aa + 1) && ((super.ab >> 8) - 8) % j < 8;
   }

   public final boolean h() {
      return !this.aq.V.b(this.u() + 1, super.aa + 1) && ((super.ab >> 8) + 8) % j > j - 8;
   }

   public final void i() {
      if (this.aq.cv == -1) {
         this.a((byte)2);
         this.aq.cv = super.ao ? -4 : -3;
         super.ac = 3584;
         super.ad = 0;
         this.s = 0;
         this.aq.B(0, -1);
      } else {
         super.ab = super.ab + this.V;
         super.ag = super.ag + this.W;
         this.v();
         if (this.c() + h.K > this.X) {
            this.a((byte)3);
            super.ad = 0;
            this.aq.cw = this.aq.cv = 0;
         }
      }
   }

   public final void j() {
      if (this.u == 0) {
         short var8 = this.a(true);
         super.ac = (short)(super.ac - 128);
         super.ag = super.ag - super.ac;
         this.v();
         if (this.c() >= var8) {
            super.aa = (byte)(var8 / k);
            super.ag = (short)(var8 % k);
         }

         this.y = this.y + this.A;
         this.z = this.z + this.B;
         if (this.z < this.x || this.a(this.y - this.w) < 1280 && this.a(this.z - this.x) < 1280) {
            if (this.F == 97) {
               this.u = 1;
            } else {
               this.u = 3;
               if (this.s == -1) {
                  super.ac = (short)(1320 * k / 44);
                  this.u = 2;
               }
            }

            int var11 = 9 * j / 44;
            if (!super.ao) {
               var11 = -var11;
            }

            int var14 = 6 * k / 44;
            int var15 = (super.ab >> 8) + var11 << 8;
            int var16 = super.aa * k + (super.ag >> 8) + var14 << 8;
            this.A = (this.w - var15) / 10;
            this.B = (this.x - var16) / 10;
            return;
         }
      } else if (this.u == 1) {
         super.aj = 1;
         super.ab = super.ab + this.A;
         super.ag = super.ag + this.B;
         this.v();
         super.ac = 0;
         if (this.c() << 8 < this.x || this.a((this.b() << 8) - this.w) < 1280 && this.a((this.c() << 8) - this.x) < 1280) {
            this.a((byte)3);
            this.u = -2;
            this.v = 0;
            h.A = false;
            this.aq.cw = this.aq.cv = 0;
            return;
         }
      } else if (this.u == 2) {
         super.aj = 1;
         super.ac = (short)(super.ac - 128);
         super.ag = super.ag - super.ac;
         this.v();
         if (super.ac < 0) {
            this.u = 3;
            return;
         }
      } else if (this.u == 3) {
         int var1 = (this.x >> 8) - this.c();
         int var2 = (this.w >> 8) - this.b();
         int var3 = (j + (j >> 1)) * (j + (j >> 1)) + (k + (k >> 1)) * (k + (k >> 1));
         this.u = 5;
         super.ad = 0;
         super.ac = 0;
         if (var2 * var2 + var1 * var1 > var3) {
            super.ad = (short)(var2 << 4);
            super.ac = (short)(var1 << 4);
            this.u = 4;
            return;
         }
      } else {
         if (this.u == 4) {
            int var7 = (this.x >> 8) - this.c();
            int var10 = (this.w >> 8) - this.b();
            int var13 = (j + (j >> 1)) * (j + (j >> 1)) + (k + (k >> 1)) * (k + (k >> 1));
            if (var10 * var10 + var7 * var7 > var13) {
               super.ab = super.ab + super.ad;
               super.ag = super.ag + super.ac;
               this.v();
               return;
            }

            this.u = 5;
            super.ac = 0;
            super.ad = 0;
            return;
         }

         if (this.u == 5) {
            super.aj = 1;
            short var6 = 0;
            short var9 = 0;
            int var12 = this.c() - (this.x >> 8) + 6 * k / 44;
            int var4;
            int var5;
            if ((var4 = super.ao ? (this.w >> 8) - (this.b() + 9) : this.b() - 9 - (this.w >> 8)) == 0) {
               var5 = 2000;
            } else {
               var5 = (var12 << 8) / var4;
            }

            if (var5 < -1945 || var5 > 1945) {
               var6 = 2880;
               var9 = 0;
            } else if (var5 > 616) {
               var6 = 2781;
               var9 = 447;
            } else if (var5 > 333) {
               var6 = 2494;
               var9 = 864;
            } else if (var5 > -10) {
               var6 = 2036;
               var9 = 1222;
            } else if (var5 > -334) {
               var6 = 2036;
               var9 = -1222;
            } else if (var5 > -617) {
               var6 = 2494;
               var9 = -864;
            } else if (var5 > -1946) {
               var6 = 2781;
               var9 = -447;
            }

            if (super.ao) {
               super.ab += var6;
            } else {
               super.ab -= var6;
            }

            super.ag += var9;
            super.ac = 447;
            this.v();
            if (var5 < 0 && var5 > -150) {
               this.a((byte)2);
               super.am = 0;
               this.u = -1;
               this.v = 15;
               this.aq.cw = this.aq.cv = 0;
            }
         }
      }
   }

   public final void k() {
      this.a((byte)2);
      this.s = 0;
      super.ap = 0;
   }

   public final void l() {
      this.aq.d = true;
      if (super.ac <= -m) {
         super.ac = (short)(-m);
      }

      if (super.al == 10 && super.am == 2) {
         this.aq.w();
         if (this.aq.cS) {
            this.aq.cS = false;
         }

         h.dm++;
      } else if (super.al == 10) {
         this.aq.cw = this.aq.cv = 0;
         this.aq.cx &= -129;
      } else if (super.ai <= 0 && super.al != 10) {
         this.aq.a.c(0);
         super.ai = 0;
         super.am = 1;
         this.a((byte)10);
         h.dm++;
      } else if (this.u >= 0) {
         this.j();
         this.t();
      } else if (super.al == 6) {
         this.i();
         this.t();
         this.m();
      } else {
         if (super.al == 11) {
            this.aq.cw = this.aq.cv = 0;
            super.ad = 0;
            this.D = 1;
            this.q();
         } else if (super.al == 9) {
            if (++super.an > 6) {
               if (this.s == -1) {
                  this.a((byte)0);
               } else {
                  super.ad = 0;
                  super.ac = 0;
                  this.a((byte)3);
                  this.s = 0;
               }
            } else {
               this.aq.cv = 0;
            }
         }

         if (this.aq.cv == -4 || this.aq.cw == -4 || super.al == 8 && super.ao) {
            if (super.al == 4) {
               this.aq.cv = 0;
            }

            if (!super.ao || super.al != 1) {
               if (this.s == 2) {
                  n = 768;
               } else if (super.al == 8) {
                  n = 384;
               } else if (this.s > -1) {
                  n = 1536;
               } else {
                  this.a((byte)1);
                  n = 1536;
                  super.am = 0;
               }

               super.ad = n;
               super.ao = true;
            }
         } else if (this.aq.cv == -3 || this.aq.cw == -3 || super.al == 8 && !super.ao) {
            if (super.al == 4) {
               this.aq.cv = 0;
            }

            if (super.ao || super.al != 1) {
               if (this.s == 2) {
                  n = 768;
               } else if (super.al == 8) {
                  n = 384;
               } else if (this.s > -1) {
                  n = 1536;
               } else {
                  this.a((byte)1);
                  n = 1536;
                  super.am = 0;
               }

               super.ad = (short)(-n);
               super.ao = false;
            }
         } else if (this.aq.cv == 0) {
            if (this.s == 2) {
               super.ad = (short)(super.ao ? 768 : -768);
            } else if (this.s >= 0) {
               if (super.ad > 0) {
                  super.ad = (short)(super.ad - 196);
               } else if (super.ad < 0) {
                  super.ad = (short)(super.ad + 196);
               }

               if (super.ad <= 196 && super.ad >= -196) {
                  super.ad = 0;
               }
            } else {
               super.ad = 0;
            }

            if (super.al == 1) {
               this.a((byte)0);
            }
         }

         if (this.aq.cv == -1) {
            if (this.s == -1) {
               super.ac = 3584;
               this.s++;
            } else if (this.s == 0) {
               if (h.z) {
                  super.ac = 3584;
               } else {
                  super.ac = 3072;
                  super.am = 1;
                  this.a((byte)13);
               }

               this.s++;
            }

            this.aq.B(0, -1);
         }

         if (super.al == 4 && this.s >= 0) {
            if (super.ak > 2) {
               this.a((byte)2);
            }

            super.am = 0;
         } else {
            if (super.ad != 0) {
               super.ae = super.ao ? this.d() : this.e();
               if (super.ag < n) {
                  super.af = super.aa;
               } else {
                  super.af = (byte)(super.aa + 1);
               }

               if (super.ae >= 0 && super.ae <= 27 && this.aq.V.b(super.ae, super.af) && (super.aa < 17 || super.aa == 17 && super.ag == 0)) {
                  int var1 = super.ab;
                  super.ab = super.ab + super.ad;
                  super.ae = super.ao ? this.d() : this.e();
                  if (this.aq.G() != -1) {
                     super.ab = super.ab - super.ad * 3 / 2;
                     if (!this.aq.V.b((super.ab >> 8) / j, super.af)) {
                        super.ab = var1;
                     }
                  } else if (this.aq.D()) {
                     super.ab = super.ab - super.ad;
                  } else if (!this.aq.V.b(super.ae, super.af)) {
                     super.ab = super.ab - super.ad;
                  }
               } else {
                  super.ad = 0;
                  if (super.ao) {
                     if (this.d() == super.ae) {
                        super.ab = super.ae * j - 8 - 1 << 8;
                     }
                  } else if (this.e() == super.ae) {
                     super.ab = super.ae * j + j + 8 + 1 << 8;
                  }
               }

               if (super.ae >= 27) {
                  this.ar = true;
               }

               if (super.ae == 0) {
                  this.ar = true;
               }
            }

            if (this.C >= 0 && super.al != 11) {
               this.s();
            }

            short var8;
            if ((var8 = this.a(false)) - this.c() > k >> 1) {
               this.G = false;
            }

            if (this.Y && !this.G) {
               this.Y = false;
               if (this.s == -1) {
                  super.ag = 0;
               }
            }

            if (this.G && this.s == -1) {
               this.Y = this.G;
               super.aa = (byte)(var8 / k);
               super.ag = (short)(var8 % k << 8);
               super.ab = super.ab + (this.H << 8);
               super.ac = 0;
               if (!this.aq.V.b(this.e(), super.aa)) {
                  super.ab = this.b() + 1 << 8;
               } else if (!this.aq.V.b(this.d(), super.aa)) {
                  super.ab = this.b() - 1 << 8;
               }
            } else {
               if ((this.s >= 0 || this.c() < var8) && super.ap == 0) {
                  if (this.s == 2) {
                     super.ac = -768;
                  } else {
                     super.ac = (short)(super.ac - 384);
                  }

                  super.ag = super.ag - super.ac;
                  if (super.ac > 0 && (!this.aq.V.b(this.d(), super.aa) || !this.aq.V.b(this.e(), super.aa))) {
                     super.ag = super.ag + super.ac;
                     super.ac = 0;
                  }

                  if (super.ag < 0) {
                     super.ag = super.ag + (k << 8);
                     super.aa--;
                     if (super.aa < 0) {
                        super.aa = 0;
                        super.ag = 0;
                     }

                     if (super.aa == 0) {
                        this.ar = true;
                     }
                  } else if (super.ag > k << 8) {
                     super.ag = super.ag - (k << 8);
                     super.aa++;
                  }

                  if (this.s == -1) {
                     this.s = 0;
                  }

                  if (super.al != 11 && super.al != 14) {
                     if (this.s == 0) {
                        this.a((byte)2);
                     }

                     if (this.aq.cv == -3 || !super.ao) {
                        super.ao = false;
                     } else if (this.aq.cv == -4 || super.ao) {
                        super.ao = true;
                     }

                     if (super.ac <= 0 && this.s == 1 && !h.z && super.al == 12) {
                        this.s = 2;
                     } else if (super.ac <= -1024 && this.s != 2) {
                        this.a((byte)3);
                     }
                  }
               }

               if (super.ap == 0) {
                  if (super.al != 11 && super.ac < 0 && super.al != 10) {
                     for (int var2 = 0; var2 < h.ba; var2++) {
                        if (this.aq.ai[var2].al != 2 && this.aq.k(var2) && this.aq.ai[var2].b() > this.c()) {
                           this.a((byte)3);
                           if (this.b() > this.aq.ai[var2].c()) {
                              this.aq.ai[var2].w = -10;
                              if (this.aq.V.b((super.ab >> 8) / j + 1, super.aa + 1) && this.aq.V.b((super.ab >> 8) / j, super.aa + 1)) {
                                 super.ac = 0;
                                 super.ad = 0;
                                 this.s = 0;
                                 if (this.aq.V.b((super.ab >> 8) / j + 1, super.aa)) {
                                    super.ab += 510;
                                 }

                                 if (!this.aq.V.b((super.ab >> 8) / j + 1, super.aa)) {
                                    super.ab -= 510;
                                 }
                              } else {
                                 this.s = -1;
                              }
                           } else {
                              this.aq.ai[var2].w = 10;
                              if (this.aq.V.b((super.ab >> 8) / j - 1, super.aa + 1) && this.aq.V.b((super.ab >> 8) / j, super.aa + 1)) {
                                 super.ac = 0;
                                 super.ad = 0;
                                 this.s = 0;
                                 if (this.aq.V.b((super.ab >> 8) / j - 1, super.aa)) {
                                    super.ab -= 510;
                                 }

                                 if (!this.aq.V.b((super.ab >> 8) / j - 1, super.aa)) {
                                    super.ab += 510;
                                 }
                              } else {
                                 this.s = -1;
                              }
                           }
                        }
                     }
                  }

                  if (this.c() >= var8 && super.ac < 0) {
                     super.aa = (byte)(var8 / k);
                     super.ag = (short)(var8 % k << 8);
                     this.ar = true;
                     super.ac = 0;
                     this.s = -1;
                     if (super.al == 3 || super.al == 2 || super.al >= 11 && super.al <= 14) {
                        if (this.s == 2) {
                           this.s = 1;
                        }

                        this.a((byte)4);
                        super.am = 1;
                     }
                  }
               }
            }

            if (super.al != 10) {
               this.t();
               if (this.t == 0) {
                  int var9 = super.ab >> 8;
                  if (this.s >= 0 && super.ac < 0) {
                     for (int var3 = 3; var3 >= 0; var3--) {
                        boolean var4;
                        if (this.aq.bc[var3] != -1
                           && (
                              (var4 = this.aq.be[var3] - this.aq.bc[var3] > 0) && var9 > this.aq.bc[var3] && var9 < this.aq.be[var3]
                                 || !var4 && var9 < this.aq.bc[var3] && var9 > this.aq.be[var3]
                           )) {
                           int var5;
                           int var6 = (var5 = this.aq.bh[var3] * var9 / this.aq.bg[var3] + this.aq.bi[var3]) - (this.c() + (h.K >> 1) << 8);
                           if (this.a(var6) < -super.ac) {
                              var5 -= h.K << 8;
                              super.aa = (byte)((var5 >> 8) / k);
                              super.ag = (short)((var5 >> 8) % k << 8);
                              this.s = -1;
                              this.a((byte)6);
                              super.ad = super.ac = 0;
                              super.ao = var4;
                              int var7 = (this.a(this.aq.be[var3] - this.aq.bc[var3]) << 8) / 2304;
                              this.V = (this.aq.bg[var3] << 8) / var7;
                              this.W = this.aq.bh[var3] / var7;
                              this.X = this.aq.bf[var3];
                              break;
                           }
                        }
                     }
                  }
               } else {
                  this.t--;
               }

               this.m();
            }
         }
      }
   }

   public final void m() {
      byte var1 = this.u();
      byte var2 = (byte)((this.c() + h.K) / k);
      if (this.v > 0) {
         this.aq.e = true;
         h.A = true;
         if (--this.v <= 0) {
            this.u = -2;
            this.v = 0;
            h.A = false;
         }
      }

      short var3 = this.aq.V.a(var1, var2);
      h.B = false;
      if (var3 >= 56 && var3 <= 61) {
         this.u = -1;
         this.v = 10;
      } else if (var3 == 26) {
         super.ac = 3584;
         this.s = 0;
         this.aq.ab = 1;
         super.am = 1;
         this.a((byte)2);
         this.aq.e(super.ab, this.c() << 8, 30);
         if (!h.f) {
            super.ai = (byte)(super.ai - 4);
         }

         this.u = -2;
         this.v = 0;
         h.A = false;
         this.aq.e = true;
      } else if (var3 == -125) {
         h.B = true;
         h.D = false;
         h.C = -1;
      } else {
         for (byte var4 = (byte)(--var2 + 2); var4 >= var2; var4--) {
            if ((var3 = this.aq.V.a(var1, var4)) == 99 || var3 == 100) {
               this.aq.ah = this.aq.X;
               this.aq.ad = var1;
               this.aq.ae = var2;
               if (var1 < 1) {
                  this.aq.af = 0;
               } else {
                  this.aq.af = (short)(-j * (var1 - 1));
               }

               if (var2 < 6) {
                  this.aq.ag = 0;
               } else {
                  this.aq.ag = (short)(-k * (var2 - 5));
               }
               break;
            }

            if (var3 >= 77 && var3 <= 96 && this.aq.h(var3 - 77)) {
               this.aq.cC = true;
               this.aq.cI = h.cz[var3 - 77];
               this.aq.r(h.cA[var3 - 77]);
               this.aq.g(var3 - 77);
               if (h.cA[var3 - 77] == 112 && (this.aq.bw & 2) == 0) {
                  this.aq.dC = 248;
                  h.dy = 16;
                  return;
               }

               this.aq.dC = 233 + (var3 - 77);
               h.dy = (byte)(var3 - 77);
               return;
            }

            if (var3 >= -118 && var3 <= -99 && this.aq.h(var3 - -118 + 20)) {
               if (var3 - -118 == 18 && (this.O & 32) == 0) {
                  return;
               }

               if (var3 - -118 != 19 || !this.aq.h(4) && !this.aq.h(5) && !this.aq.h(6) && !this.aq.h(7) && !this.aq.h(9) && !this.aq.h(10)) {
                  this.aq.cC = true;
                  this.aq.cI = h.cz[var3 - -118 + 20];
                  this.aq.r(h.cA[var3 - -118 + 20]);
                  this.aq.g(var3 - -118 + 20);
                  return;
               }

               return;
            }

            if (var3 == -94 && !this.aq.h(11) && this.aq.h(30)) {
               this.aq.cC = true;
               this.aq.cI = h.cz[30];
               this.aq.r(h.cA[30]);
               this.aq.g(30);
               return;
            }
         }
      }

      if (super.aa >= 17 && this.aq.cT) {
         this.aq.cS = true;
         this.a((byte)10);
         super.am = 1;
      } else {
         if (this.ar) {
            this.ar = false;
            if (this.d() == 0 && !super.ao) {
               this.aq.j(2);
               return;
            }

            if (this.e() >= 27 && super.ao) {
               this.aq.j(5);
               return;
            }

            if (super.aa >= 17 && super.ac <= 0) {
               this.aq.j(6);
               return;
            }

            if (super.aa == 0 && super.ag < 1280) {
               this.aq.j(1);
            }
         }
      }
   }

   public final void n() {
      if (super.am != 2) {
         if (super.al == 14) {
            super.ac = 0;
         }

         super.ak++;
         if (super.ak > K[super.Z][super.al]) {
            super.ak = 0;
            super.aj++;
            if (super.aj >= J[super.Z][super.al]) {
               if (super.am == 0) {
                  super.aj = 0;
               } else {
                  super.aj--;
                  super.am = 2;
                  this.r();
               }
            }

            this.aq.d = true;
         }
      }
   }

   public final void a(Graphics var1, int var2, int var3, int var4) {
      h.x = (short)var3;
      h.y = (short)var4;
      int var5 = (super.ab >> 8) - (h.J >> 1) + h.x;
      int var6 = super.aa * k + (super.ag >> 8) + h.y + h.L;
      if (super.al == 6) {
         var6 += k >> 1;
      }

      if (this.u >= 0) {
         boolean var9 = false;
         boolean var8 = false;
         int var11 = 26 * j / 44;
         if (!super.ao) {
            var11 = j - var11;
         }

         int var12 = 10 * k / 44;
         int var13 = var5 + var11 - 9 << 8;
         int var14 = var6 + var12 - 9 << 8;
         int var15 = this.y + (h.x << 8);
         int var16 = this.z + (h.y << 8);
         if (this.u == 0) {
            var14 += 2304;
         }

         if (super.ao) {
            var15 -= 1216;
         } else {
            var15 -= 2432;
         }

         int var17 = (var15 - var13) / 19;
         int var18 = (var16 - var14) / 19;
         boolean var19 = false;

         for (int var32 = 0; var32 < 20; var32++) {
            boolean var7 = false;
            int var10 = var14;
            int var20 = var13 >> 8;
            int var21 = var10 >> 8;
            if (var20 < 176 && var21 < 220 && var20 + 19 >= 0 && var21 + 19 >= 0) {
               this.aq.b(var1, var20, var21, 19, 19);
               var1.drawImage(h.aL, var20, var21 - 114 - 0, 0);
            }

            var13 += var17;
            var14 += var18;
         }

         int var33 = var13 - var17 >> 8;
         int var25 = (var14 - var18 >> 8) - 9;
         if (var33 < 176 && var25 < 220 && var33 + 19 >= 0 && var25 + 19 >= 0) {
            this.aq.b(var1, var33, var25, 19, 19);
            if (super.ao) {
               var1.drawImage(h.aL, var33, var25 - 95 - 0, 20);
            } else {
               e.b.drawImage(h.aL, var33, var25 - 95 - 0, 20, 8192);
            }
         }
      }

      if (var6 + h.K > l && var6 < 220 && this.E % 2 == 0) {
         byte var26 = I[super.Z][super.al][super.aj];
         byte[] var28 = c[var26];
         if (var5 + (var28[4] & 255) < 176
            && var6 - 0 + (var28[5] & 255) < 220
            && var5 + (var28[4] & 255) + (var28[2] & 255) >= 0
            && var6 - 0 + (var28[5] & 255) + (var28[3] & 255) >= 0) {
            this.aq.b(var1, var5 + (var28[4] & 255), var6 + (var28[5] & 255), var28[2] & 255, (var28[3] & 255) - 0);
            if (super.ap == 0 && super.ao) {
               var1.drawImage(h.aH, var5 + (var28[4] & 255) - (var28[0] & 255), var6 - 0 + (var28[5] & 255) - (var28[1] & 255), 20);
            } else {
               this.aq
                  .a(var1, var5 + (var28[4] & 255) - (h.aH.getWidth() - (var28[0] & 255) - (var28[2] & 255)), var6 + (var28[5] & 255) - (var28[1] & 255), 0);
            }
         }
      }

      if (this.L != 0) {
         byte var27 = a[I[super.Z][super.al][super.aj]];
         int var29 = b[I[super.Z][super.al][super.aj]];
         if (super.al == 6) {
            var29 += k >> 1;
         }

         if (var27 != 44 && var29 != 44) {
            int var24 = h.aI.getWidth();
            if (super.ao) {
               var5 = (super.ab >> 8) + h.x + var27;
            } else {
               var5 = (super.ab >> 8) + h.x - var27 - var24;
            }

            var6 = super.aa * k + (super.ag >> 8) + h.y + h.L + var29;
            int var30 = this.L - 1;
            this.aq.b(var1, var5, var6, var24, 25);
            if (super.ao) {
               var1.drawImage(h.aI, var5, var6 - var30 * 25 - 0, 20);
            } else {
               e.b.drawImage(h.aI, var5, var6 - var30 * 25 - 0, 20, 8192);
            }
         }
      }
   }

   public final void o() {
      if (super.ao) {
         int var1 = this.d();
         if ((super.ab >> 8) % j > j >> 1) {
            var1++;
         }

         for (int var2 = var1; var2 < var1 + 3; var2++) {
            for (int var3 = super.aa; var3 >= super.aa - 3; var3--) {
               short var4;
               if (var2 >= 0 && var3 >= 0 && ((var4 = this.aq.V.a(var2, var3)) == 97 || var4 == 98)) {
                  this.aq.a(var2, var3, var4);
               }
            }
         }
      } else {
         int var5 = this.e();
         if ((super.ab >> 8) % j < j >> 1) {
            var5--;
         }

         for (int var6 = var5; var6 > var5 - 3; var6--) {
            for (int var7 = super.aa; var7 >= super.aa - 3; var7--) {
               short var8;
               if (var6 >= 0 && var7 >= 0 && ((var8 = this.aq.V.a(var6, var7)) == 97 || var8 == 98)) {
                  this.aq.a(var6, var7, var8);
               }
            }
         }
      }
   }

   public final void p() {
      byte var1 = this.P[this.L];
      if (super.al != 6 && super.al != 7 && super.al != 12 && super.al != 13) {
         this.a((byte)7);
         super.am = 0;
      }

      if (this.M[this.L] > 0) {
         if (this.aq.a.i) {
            this.aq.a.c(2);
         }

         switch (this.L) {
            case 0:
               return;
            case 1:
               this.aq.e(super.ab + (super.ao ? 4608 : -4608), (this.c() << 8) + 2048, 0 + var1);
               return;
            case 2:
               this.aq.e(super.ab + (super.ao ? 4608 : -4608), (this.c() << 8) + 2048, 3 + var1);
               return;
            case 3:
               this.aq.e(super.ab + (super.ao ? 4608 : -4608), (this.c() << 8) + 2048, 6 + var1);
               return;
            case 4:
               this.aq.e(super.ab + (super.ao ? 4608 : -4608), (this.c() << 8) + 2048, 9 + var1);
               if (this.M[this.L] > 0 && var1 == 2) {
                  this.aq.e(super.ab + (super.ao ? 4608 : -4608), (this.c() << 8) + 2048, 9 + var1);
                  return;
               }
               break;
            case 5:
               this.aq.e(super.ab + (super.ao ? 4608 : -4608), (this.c() << 8) + 2048 + this.M[this.L] % 2 * 1024, 12 + var1);
               return;
            case 6:
               this.aq.e(super.ab + (super.ao ? 4608 : -4608), (this.c() << 8) + 2048, 15 + var1);
               return;
            case 7:
               this.aq.e(super.ab + (super.ao ? 4608 : -4608), (this.c() << 8) + 2048, 18 + var1);
         }
      }
   }

   public final void q() {
      if (super.al < 4 || super.al == 11) {
         this.aq.m(-1);
         if (super.al != 11) {
            super.am = 1;
            this.a((byte)8);
         }

         byte var3 = 0;
         byte var4 = 0;

         for (int var6 = 2; var6 >= 0; var6--) {
            if (this.aq.bF[var6] != -1) {
               int var1 = this.aq.bF[var6] * j + (j - 19 >> 1);
               int var2 = this.aq.bG[var6] * k;
               if (this.a(var1 - this.b()) <= 3 * j >> 1
                  && this.a(var2 - this.c()) <= k
                  && this.aq
                     .a(var1, var2, 19, 19, (super.ab >> 8) + (super.ao ? o[this.D] : -o[this.D] - q[this.D]), this.c() + p[this.D], q[this.D], r[this.D])) {
                  int var7 = this.aq.V.a(this.aq.bF[var6], this.aq.bG[var6]);
                  var7 -= 36;
                  if (var7 == 2 && (this.aq.bw & 1 << var7) > 0) {
                     this.aq.cC = true;
                     this.aq.di = 32;
                     this.aq.cL |= 32;
                     this.aq.cI = h.cz[33];
                     this.aq.r(h.cA[33]);
                     this.aq.dC = 256;
                     h.dy = 15;
                  }

                  this.aq.bw = (byte)(this.aq.bw & ~(1 << var7));
                  if (var7 == 0) {
                     this.aq.e(this.aq.Z, this.aq.X);
                  }
               }
            }
         }

         for (int var15 = h.ba - 1; var15 >= 0; var15--) {
            byte var5 = this.aq.ai[var15].Z;
            if (this.aq.ai[var15].Z != -1 && this.aq.ai[var15].al != 5) {
               int var11 = this.aq.ai[var15].c() - f.a[var5];
               int var12 = this.aq.ai[var15].b() + f.b[var5];
               var3 = f.c[var5];
               var4 = f.d[var5];
               if (this.a(var11 - this.b()) <= 3 * j >> 1
                  && this.a(var12 - this.c()) <= k
                  && this.aq
                     .a(var11, var12, var3, var4, (super.ab >> 8) + (super.ao ? o[this.D] : -o[this.D] - q[this.D]), this.c() + p[this.D], q[this.D], r[this.D])
                  && (!this.aq.ai[var15].G || super.al != 11)) {
                  if (super.al == 11) {
                     this.aq.ai[var15].G = true;
                  }

                  if (super.al == 11) {
                     this.aq.ai[var15].ai = (byte)(this.aq.ai[var15].ai - i[this.P[1]]);
                  } else {
                     this.aq.ai[var15].ai = (byte)(this.aq.ai[var15].ai - i[this.P[0]]);
                  }

                  if (this.aq.ai[var15].al != 2) {
                     if (this.aq.ai[var15].ab > super.ab) {
                        this.aq.ai[var15].w = 10;
                     } else {
                        this.aq.ai[var15].w = -10;
                     }
                  }

                  if (this.aq.ai[var15].ai <= 0) {
                     short var8 = this.aq.X;
                     if (this.aq.cV) {
                        var8 = 0;
                     }

                     int var9 = var15 + var8 * 10 >> 5;
                     this.aq.bv[var9] = this.aq.bv[var9] & ~(1 << var15 + var8 * 10 - (var9 << 5));
                     this.aq.ai[var15].a((byte)5);
                     this.aq.ai[var15].am = 1;
                     if (var5 != 4) {
                        h.do++;
                        if (this.aq.aa && ++this.aq.ab > 10) {
                           this.aq.ab = 10;
                        }

                        if (this.aq.Z != 0) {
                           for (int var10 = 0; var10 < f.k[this.aq.ai[var15].v]; var10++) {
                              this.aq.c(this.aq.ai[var15].c(), this.aq.ai[var15].b(), 0);
                           }
                        }
                     }
                  } else if (this.aq.ai[var15].al != 2) {
                     this.aq.ai[var15].a((byte)4);
                     this.aq.ai[var15].an = 0;
                  }
               }
            }
         }

         if (this.s == -1) {
            this.aq.cv = 0;
         }
      }
   }

   public final void r() {
      this.D = 0;
      if (super.al == 7 || super.al == 8 || super.al == 9 || super.al == 0 || super.al == 4) {
         super.am = 0;
         this.a((byte)0);
      } else if (super.al == 13) {
         super.am = 0;
         this.a((byte)12);
      } else {
         if (super.al == 14) {
            super.am = 0;
            this.a((byte)11);
         }
      }
   }

   public final void s() {
      if (++this.C > 15) {
         this.C = -1;
         this.D = 0;
      }
   }

   public final void t() {
      int var1 = this.c() + 7 + 37;

      for (int var2 = 11; var2 >= 0; var2--) {
         byte var3 = this.aq.ck[var2];
         int var4 = this.aq.cg[var2];
         short var5 = this.aq.ci[var2];
         int var6 = 0;
         if (var3 != -1 && (super.ai != 20 || var3 != 2)) {
            var6 = j * 5 << 7;
            if (var5 != 0 || this.a(var4 - super.ab) <= var6 && this.a((this.aq.ch[var2] >> 8) - var1) <= h.K) {
               if (var4 > super.ab) {
                  var5 = (short)(var5 - 256);
               } else {
                  var5 = (short)(var5 + 256);
               }

               if ((var4 = var4 + var5) >= j * 27 + (j >> 1) << 8) {
                  var4 = j * 27 + (j >> 1) << 8;
               }

               int var7;
               if ((var7 = this.a(var4 - super.ab)) <= j << 6) {
                  this.aq.e = true;
                  if (var3 <= 1) {
                     this.aq.aX = this.aq.aX + 10 * this.aq.ab;
                     h.dq = h.dq + 10 * this.aq.ab;
                     if (this.aq.aX > 999999) {
                        this.aq.aX = 999999;
                     }
                  } else if (var3 == 2) {
                     super.ai = (byte)(super.ai + 4);
                     if (super.ai > 20) {
                        super.ai = 20;
                     }
                  } else {
                     this.b(1);
                  }

                  this.aq.ck[var2] = -1;
                  return;
               }

               int var8 = this.aq.ch[var2] - (this.c() + (k >> 1) << 8);
               if (var7 != 0) {
                  this.aq.ch[var2] = this.aq.ch[var2] + var8 * var5 * (var5 >= 0 ? -1 : 1) / var7;
               }

               this.aq.cg[var2] = var4;
               this.aq.ci[var2] = var5;
            }
         }
      }
   }

   private void b(int var1) {
      int var2 = 1;
      short var3 = 9999;

      for (int var4 = 1; var4 < 8; var4++) {
         if (var4 != 6 && (this.O & 1 << var4) > 0 && this.M[var4] < var3 && this.M[var4] != d[var4 * 3 + this.P[var4]]) {
            var3 = this.M[var4];
            var2 = var4;
         }
      }

      this.M[var2] = (short)(this.M[var2] + var1 * f[var2]);
      if (this.M[var2] > d[var2 * 3 + this.P[var2]]) {
         this.M[var2] = d[var2 * 3 + this.P[var2]];
      }
   }

   private void a(String var1) {
      byte[] var4 = new byte[4];

      try {
         InputStream var5 = var4.getClass().getResourceAsStream(var1);

         for (int var6 = 0; var6 < 15; var6++) {
            byte var2 = (byte)var5.read();
            byte var3 = (byte)var5.read();
            J[0][var6] = var2;
            K[0][var6] = var3;
            this.a(var5, var4, var2);
            System.arraycopy(var4, 0, I[0][var6], 0, var2);
         }

         var5.close();
      } catch (IOException var7) {
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
}
