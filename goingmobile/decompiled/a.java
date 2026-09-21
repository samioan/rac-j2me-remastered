import com.nokia.mid.ui.DirectUtils;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;

public final class a extends h {
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
   private f ai;
   public static short j;
   public static short k;
   public static final byte[] l = new byte[]{4, -7};
   public static final byte[] m = new byte[]{3, 10};
   public static final byte[] n = new byte[]{5, 8};
   public static final byte[] o = new byte[]{16, 13};
   public byte p;
   public byte q;
   public byte r;
   public byte s;
   public int t;
   public int u;
   public int v;
   public int w;
   public int x;
   public int y;
   public byte z;
   public byte A;
   public byte B;
   public byte C;
   public boolean D;
   public short E;
   public boolean F;
   public static byte[][][] G;
   public static byte[][] H;
   public static byte[][] I;
   public byte J;
   public short[] K;
   public short[] L;
   public byte M;
   public byte[] N;
   public int O;
   public int P;
   public int Q;

   public a(f var1) {
      this.ai = var1;
      k = 1280;
      this.p = -1;
      this.r = -2;
      this.z = -1;
      this.A = 0;
      this.D = false;
      this.q = 0;
      this.E = 0;
      this.K = new short[8];
      this.L = new short[8];
      this.N = new byte[8];
      super.U = 0;
      super.V = 0;
      super.ah = 0;
      super.ag = true;
      super.aa = 20;
      j = 1792;
   }

   public final void a() {
      if (G == null) {
         G = new byte[1][15][4];
      }

      if (H == null) {
         H = new byte[1][15];
      }

      if (I == null) {
         I = new byte[1][15];
      }

      a("/r");
   }

   public final short b() {
      return (short)(super.T >> 8);
   }

   public final short c() {
      return (short)(super.S * 14 + (super.Y >> 8));
   }

   public final byte d() {
      return (byte)(((super.T >> 8) + 4) / 22);
   }

   public final byte e() {
      return (byte)(((super.T >> 8) - 4) / 22);
   }

   public final short a(boolean var1) {
      int var2 = 238;
      byte var3 = 1;
      if (var1) {
         var3 = 5;
      }

      label28: {
         int var10000;
         if (super.Y > var3 << 8) {
            if ((c.e[this.d()] & 1 << super.S + 2) <= 0 && (c.e[this.e()] & 1 << super.S + 2) <= 0) {
               break label28;
            }

            var10000 = super.S + 1;
         } else {
            if ((c.e[this.d()] & 1 << super.S + 1) <= 0 && (c.e[this.e()] & 1 << super.S + 1) <= 0) {
               break label28;
            }

            var10000 = super.S;
         }

         var2 = var10000 * 14;
      }

      short var4 = this.ai.f();
      short var5 = (short)f.d(var2, var4);
      short var6 = this.ai.e();
      this.D = false;
      if (var6 < var5) {
         this.D = true;
         return var6;
      } else {
         return var5;
      }
   }

   public final boolean f() {
      int var1;
      int var2 = (var1 = super.T >> 8) / 22;
      int var3;
      if ((var3 = var1 % 22) < 5) {
         if (this.ai.N.f[var2 - 1][super.S + 1] != 4) {
            return false;
         }
      } else if (var3 > 17 && this.ai.N.f[var2 + 1][super.S + 1] != 4) {
         return false;
      }

      return this.ai.N.f[var2][super.S + 1] == 4;
   }

   public final boolean g() {
      return !this.ai.N.b(this.p() - 1, super.S + 1) && ((super.T >> 8) - 4) % 22 < 4;
   }

   public final boolean h() {
      return !this.ai.N.b(this.p() + 1, super.S + 1) && ((super.T >> 8) + 4) % 22 > 18;
   }

   private void r() {
      if (this.ai.bh == -1) {
         this.a((byte)2);
         this.ai.bh = super.ag ? -4 : -3;
         super.U = 2560;
         super.V = 0;
         this.p = 0;
         this.ai.i();
      } else {
         super.T = super.T + this.O;
         super.Y = super.Y + this.P;
         this.q();
         if (this.c() + 22 > this.Q) {
            this.a((byte)3);
            super.V = 0;
            this.ai.bi = this.ai.bh = 0;
         }
      }
   }

   private void s() {
      if (this.r == 0) {
         short var8 = this.a(true);
         super.U = (short)(super.U - 128);
         super.Y = super.Y - super.U;
         this.q();
         if (this.c() >= var8) {
            super.S = (byte)(var8 / 14);
            super.Y = (short)(var8 % 14);
         }

         this.v = this.v + this.x;
         this.w = this.w + this.y;
         if (this.w < this.u || Math.abs(this.v - this.t) < 1280 && Math.abs(this.w - this.u) < 1280) {
            label80: {
               a var19;
               byte var21;
               if (this.C == 97) {
                  var19 = this;
                  var21 = 1;
               } else {
                  this.r = 3;
                  if (this.p != -1) {
                     break label80;
                  }

                  super.U = 420;
                  var19 = this;
                  var21 = 2;
               }

               var19.r = var21;
            }

            byte var11 = 4;
            if (!super.ag) {
               var11 = -4;
            }

            boolean var14 = false;
            int var15 = (super.T >> 8) + var11 << 8;
            int var16 = super.S * 14 + (super.Y >> 8) + 1 << 8;
            this.x = (this.t - var15) / 10;
            this.y = (this.u - var16) / 10;
            return;
         }
      } else {
         if (this.r == 1) {
            super.ab = 1;
            super.T = super.T + this.x;
            super.Y = super.Y + this.y;
            this.q();
            super.U = 0;
            if (this.c() << 8 >= this.u && (Math.abs((this.b() << 8) - this.t) >= 1280 || Math.abs((this.c() << 8) - this.u) >= 1280)) {
               return;
            }

            this.a((byte)3);
            this.r = -2;
            this.s = 0;
            f.u = false;
         } else {
            if (this.r == 2) {
               super.ab = 1;
               super.U = (short)(super.U - 128);
               super.Y = super.Y - super.U;
               this.q();
               if (super.U < 0) {
                  this.r = 3;
                  return;
               }

               return;
            }

            if (this.r == 3) {
               int var7 = (this.u >> 8) - this.c();
               int var10 = (this.t >> 8) - this.b();
               boolean var13 = false;
               this.r = 5;
               super.V = 0;
               super.U = 0;
               if (var10 * var10 + var7 * var7 > 1530) {
                  super.V = (short)(var10 << 4);
                  super.U = (short)(var7 << 4);
                  this.r = 4;
                  return;
               }

               return;
            }

            if (this.r == 4) {
               int var6 = (this.u >> 8) - this.c();
               int var9 = (this.t >> 8) - this.b();
               boolean var12 = false;
               if (var9 * var9 + var6 * var6 > 1530) {
                  super.T = super.T + super.V;
                  super.Y = super.Y + super.U;
                  this.q();
                  return;
               }

               this.r = 5;
               super.U = 0;
               super.V = 0;
               return;
            }

            if (this.r != 5) {
               return;
            }

            super.ab = 1;
            short var1 = 0;
            short var2 = 0;
            int var3 = this.c() - (this.u >> 8) + 1;
            int var10000;
            int var10001;
            if (super.ag) {
               var10000 = this.t >> 8;
               var10001 = this.b() + 9;
            } else {
               var10000 = this.b() - 9;
               var10001 = this.t >> 8;
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

            a var18;
            if (super.ag) {
               var18 = this;
               var10001 = super.T + var1;
            } else {
               var18 = this;
               var10001 = super.T - var1;
            }

            var18.T = var10001;
            super.Y += var2;
            super.U = 447;
            this.q();
            if (var5 >= 0 || var5 <= -150) {
               return;
            }

            this.a((byte)2);
            super.ae = 0;
            this.r = -1;
            this.s = 15;
         }

         this.ai.bi = this.ai.bh = 0;
      }
   }

   public final void i() {
      this.a((byte)2);
      this.p = 0;
      super.ah = 0;
   }

   public final void j() {
      if (super.U <= -j) {
         super.U = (short)(-j);
      }

      if (super.ad == 10 && super.ae == 2) {
         this.ai.d();
         if (this.ai.by) {
            this.ai.by = false;
         }

         f.bA++;
      } else if (super.ad == 10) {
         this.ai.bi = this.ai.bh = 0;
         this.ai.bj &= -129;
      } else if (super.aa <= 0 && super.ad != 10) {
         if (f.a.g && f.a.f != null) {
            f.a.f.a(3, 1);
         }

         super.aa = 0;
         super.ae = 1;
         this.a((byte)10);
         f.bA++;
      } else if (this.r >= 0) {
         this.s();
         this.v();
      } else if (super.ad == 6) {
         this.r();
         this.t();
         this.v();
      } else {
         if (super.ad == 11 && !this.F) {
            this.ai.bi = this.ai.bh = 0;
            super.V = 0;
            this.A = 1;
            this.n();
         } else if (super.ad == 9) {
            if (++super.af > 6) {
               if (this.p == -1) {
                  this.a((byte)0);
               } else {
                  super.V = 0;
                  super.U = 0;
                  this.a((byte)3);
                  this.p = 0;
               }
            } else {
               this.ai.bh = 0;
            }
         }

         if (this.ai.bh == -4 || this.ai.bi == -4 || super.ad == 8 && super.ag) {
            if (super.ad == 4) {
               this.ai.bh = 0;
            }

            if (!super.ag || super.ad != 1) {
               if (this.p == 2) {
                  k = 512;
               } else if (super.ad == 8) {
                  k = 256;
               } else if (this.p > -1) {
                  k = 1280;
               } else {
                  this.a((byte)1);
                  k = 1280;
                  super.ae = 0;
               }

               super.V = k;
               super.ag = true;
            }
         } else if (this.ai.bh == -3 || this.ai.bi == -3 || super.ad == 8 && !super.ag) {
            if (super.ad == 4) {
               this.ai.bh = 0;
            }

            if (super.ag || super.ad != 1) {
               if (this.p == 2) {
                  k = 512;
               } else if (super.ad == 8) {
                  k = 256;
               } else if (this.p > -1) {
                  k = 1280;
               } else {
                  this.a((byte)1);
                  k = 1280;
                  super.ae = 0;
               }

               super.V = (short)(-k);
               super.ag = false;
            }
         } else if (this.ai.bh == 0) {
            if (this.p == 2) {
               super.V = (short)(super.ag ? 512 : -512);
            } else {
               label520: {
                  if (this.p >= 0) {
                     label447: {
                        a var10000;
                        int var10001;
                        if (super.V > 0) {
                           var10000 = this;
                           var10001 = super.V - 98;
                        } else {
                           if (super.V >= 0) {
                              break label447;
                           }

                           var10000 = this;
                           var10001 = super.V + 98;
                        }

                        var10000.V = (short)var10001;
                     }

                     if (super.V > 98 || super.V < -98) {
                        break label520;
                     }
                  }

                  super.V = 0;
               }
            }

            if (super.ad == 1) {
               this.a((byte)0);
            }
         }

         if (this.ai.bh == -1) {
            label434: {
               if (this.p == -1) {
                  super.U = 2560;
               } else {
                  if (this.p != 0) {
                     break label434;
                  }

                  if (f.t) {
                     super.U = 2560;
                  } else {
                     super.U = 1920;
                     if (super.ad != 8 && super.ad != 11) {
                        super.ae = 1;
                        this.a((byte)13);
                     }
                  }
               }

               this.p++;
            }

            this.ai.i();
         }

         if (super.ad == 4 && this.p >= 0) {
            if (super.ac > 2) {
               this.a((byte)2);
            }

            super.ae = 0;
         } else {
            label427:
            if (super.V != 0) {
               super.W = super.ag ? this.d() : this.e();
               a var10;
               byte var15;
               if (super.Y < k) {
                  var10 = this;
                  var15 = super.S;
               } else {
                  var10 = this;
                  var15 = (byte)(super.S + 1);
               }

               var10.X = (byte)var15;
               if (super.W >= 0 && super.W <= 27 && this.ai.N.b(super.W, super.X) && (super.S < 17 || super.S == 17 && super.Y == 0)) {
                  super.T = super.T + super.V;
                  super.W = super.ag ? this.d() : this.e();
                  if (this.ai.h() == -1 && !this.ai.g() && this.ai.N.b(super.W, super.X)) {
                     break label427;
                  }

                  var10 = this;
                  var15 = super.T - super.V;
               } else {
                  super.V = 0;
                  if (super.ag) {
                     if (this.d() != super.W) {
                        break label427;
                     }

                     var10 = this;
                     var15 = super.W * 22 - 4 - 1;
                  } else {
                     if (this.e() != super.W) {
                        break label427;
                     }

                     var10 = this;
                     var15 = super.W * 22 + 22 + 4 + 1;
                  }

                  var15 <<= 8;
               }

               var10.T = var15;
            }

            if (this.z >= 0 && super.ad != 11) {
               this.u();
            }

            short var1;
            if ((var1 = this.a(false)) - this.c() > 7) {
               this.D = false;
            }

            if (this.D && this.p == -1) {
               if (super.ad == 3) {
                  super.ad = 0;
               }

               super.S = (byte)((var1 + 1) / 14);
               super.Y = (short)((var1 + 1) % 14 << 8);
               super.T = super.T + (this.E << 8);
               super.U = 0;
               if (!this.ai.N.b(this.e(), super.S)) {
                  super.T += 256;
               } else if (!this.ai.N.b(this.d(), super.S)) {
                  super.T -= 256;
               }
            } else {
               if ((this.p >= 0 || this.c() < var1) && super.ah == 0) {
                  a var12;
                  short var18;
                  if (this.p == 2) {
                     var12 = this;
                     var18 = -768;
                  } else {
                     var12 = this;
                     var18 = (short)(super.U - 384);
                  }

                  var12.U = var18;
                  super.Y = super.Y - super.U;
                  if (super.U > 0 && (!this.ai.N.b(this.d(), super.S) || !this.ai.N.b(this.e(), super.S))) {
                     super.Y = super.Y + super.U;
                     super.U = 0;
                  }

                  if (super.Y < 0) {
                     super.Y += 3584;
                     super.S--;
                     if (super.S < 0) {
                        super.S = 0;
                        super.Y = 0;
                     }

                     if (super.S == 0) {
                     }
                  } else if (super.Y > 3584) {
                     super.Y -= 3584;
                     super.S++;
                  }

                  if (this.p == -1) {
                     this.p = 0;
                  }

                  if (super.ad != 11 && super.ad != 14) {
                     if (this.p == 0 && super.ad != 8) {
                        this.a((byte)2);
                     }

                     label543: {
                        boolean var19;
                        if (this.ai.bh != -3 && super.ag) {
                           if (this.ai.bh != -4 && !super.ag) {
                              break label543;
                           }

                           var12 = this;
                           var19 = true;
                        } else {
                           var12 = this;
                           var19 = false;
                        }

                        var12.ag = var19;
                     }

                     if (super.U <= 0 && this.p == 1 && !f.t && super.ad == 12) {
                        this.p = 2;
                     } else if (super.U <= -1024 && this.p != 2) {
                        this.a((byte)3);
                     }
                  }
               }

               if (super.ah == 0) {
                  if (super.ad != 11 && super.U < 0 && super.ad != 10) {
                     for (int var2 = 0; var2 < f.au; var2++) {
                        if (this.ai.W[var2].ad != 2 && this.ai.d(var2) && this.ai.W[var2].b() > this.c()) {
                           super.U = 0;
                           super.V = 0;
                           this.a((byte)3);
                           this.p = 0;
                           a var14;
                           int var20;
                           if (this.b() > this.ai.W[var2].c()) {
                              this.ai.W[var2].u = -10;
                              if (this.ai.N.b(this.d(), super.S)) {
                                 super.T += 510;
                              }

                              if (this.ai.N.b(this.d(), super.S)) {
                                 continue;
                              }

                              var14 = this;
                              var20 = super.T - 510;
                           } else {
                              this.ai.W[var2].u = 10;
                              if (this.ai.N.b(this.e(), super.S)) {
                                 super.T -= 510;
                              }

                              if (this.ai.N.b(this.e(), super.S)) {
                                 continue;
                              }

                              var14 = this;
                              var20 = super.T + 510;
                           }

                           var14.T = var20;
                        }
                     }
                  }

                  if (this.c() >= var1 && super.U < 0) {
                     super.S = (byte)(var1 / 14);
                     super.Y = (short)(var1 % 14 << 8);
                     super.U = 0;
                     this.p = -1;
                     if (super.ad == 3 || super.ad == 2 || super.ad >= 11 && super.ad <= 14) {
                        if (this.p == 2) {
                           this.p = 1;
                        }

                        this.a((byte)4);
                        super.ae = 1;
                     }
                  }
               }
            }

            if (super.ad != 10) {
               this.v();
               if (this.q == 0) {
                  int var8 = super.T >> 8;
                  if (this.p >= 0 && super.U < 0) {
                     for (int var3 = 3; var3 >= 0; var3--) {
                        boolean var4;
                        int var5;
                        if (this.ai.aw[var3] != -1
                           && (
                              (var4 = this.ai.ay[var3] - this.ai.aw[var3] > 0) && var8 > this.ai.aw[var3] && var8 < this.ai.ay[var3]
                                 || !var4 && var8 < this.ai.aw[var3] && var8 > this.ai.ay[var3]
                           )
                           && Math.abs((var5 = this.ai.aB[var3] * var8 / this.ai.aA[var3] + this.ai.aC[var3]) - (this.c() + 11 << 8)) < -super.U) {
                           var5 -= 5632;
                           super.S = (byte)((var5 >> 8) / 14);
                           super.Y = (short)((var5 >> 8) % 14 << 8);
                           this.p = -1;
                           this.a((byte)6);
                           super.V = super.U = 0;
                           super.ag = var4;
                           int var7 = (Math.abs(this.ai.ay[var3] - this.ai.aw[var3]) << 8) / 1920;
                           this.O = (this.ai.aA[var3] << 8) / var7;
                           this.P = this.ai.aB[var3] / var7;
                           this.Q = this.ai.az[var3];
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
      byte var1 = this.p();
      byte var2 = (byte)((this.c() + 22) / 14);
      if (this.s > 0) {
         this.ai.e = true;
         f.u = true;
         if (--this.s <= 0) {
            this.r = -2;
            this.s = 0;
            f.u = false;
         }
      }

      short var3 = this.ai.N.a(var1, var2);
      f.v = false;
      if (var3 >= 56 && var3 <= 61) {
         if (this.ai.Q != 10 || var1 != 17 || var2 != 12 || var3 != 58) {
            this.r = -1;
            this.s = 10;
         }
      } else if (var3 == 8) {
         super.U = 2560;
         this.p = 0;
         super.ae = 1;
         this.a((byte)2);
         this.ai.d(super.T, this.c() << 8, 30);
         if (!f.i) {
            super.aa--;
         }

         this.ai.e = true;
      } else if (var3 != -125 && var3 != -122) {
         int var10000 = --var2 + 2;

         byte var4;
         while ((var4 = (byte)var10000) >= var2) {
            if ((var3 = this.ai.N.a(var1, var4)) == 99 || var3 == 100) {
               this.ai.V = this.ai.P;
               this.ai.R = var1;
               this.ai.S = var2;
               f var7;
               short var10001;
               if (var1 < 1) {
                  var7 = this.ai;
                  var10001 = 0;
               } else {
                  var7 = this.ai;
                  var10001 = (short)(-22 * (var1 - 1));
               }

               var7.T = var10001;
               if (var2 < 6) {
                  this.ai.U = 0;
               } else {
                  this.ai.U = (short)(-14 * (var2 - 5));
               }
               break;
            }

            if (var3 >= 77 && var3 <= 96 && this.ai.c(var3 - 77)) {
               this.ai.bt = f.bk[var3 - 77];
               this.ai.e(f.bl[var3 - 77], this.ai.Q == 3 ? 11 : (this.ai.Q == 10 ? 9 : -1));
               this.ai.b(var3 - 77);
               return;
            }

            var10000 = var4 - 1;
         }
      } else {
         f.v = true;
         f.x = false;
         f.w = -1;
         if (this.ai.cf) {
            this.ai.j();
         }
      }

      if (super.S >= 17 && this.ai.bz) {
         this.ai.by = true;
         this.a((byte)10);
         super.ae = 1;
      }
   }

   public final void k() {
      if (super.ae != 2) {
         if (super.ad == 14) {
            super.U = 0;
         }

         super.ac++;
         if (super.ac > I[super.R][super.ad]) {
            super.ac = 0;
            super.ab++;
            if (super.ab >= H[super.R][super.ad]) {
               if (super.ae == 0) {
                  super.ab = 0;
                  return;
               }

               super.ab--;
               super.ae = 2;
               this.o();
            }
         }
      }
   }

   public final void a(Graphics var1) {
      int var2 = (super.T >> 8) - 11 + f.r + 0;
      int var3 = super.S * 14 + (super.Y >> 8) + f.s + -6;
      if (super.ad == 6) {
         var3 += 7;
      }

      if (this.r >= 0) {
         byte var9 = 13;
         if (!super.ag) {
            var9 = 9;
         }

         boolean var10 = false;
         int var11 = var2 + var9 - 4 << 8;
         int var12 = var3 + 3 - 4 << 8;
         int var13 = this.v + (f.r << 8);
         int var14 = this.w + (f.s << 8);
         if (this.r == 0) {
            var12 += 2304;
         }

         int var10000;
         short var10001;
         if (super.ag) {
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
               var1.drawImage(f.ai, var11 >> 8, (var7 >> 8) - 48 - var4, 0);
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
            if (super.ag) {
               var1.drawImage(f.ai, var31, var25 - 40 - var22, 0);
            } else {
               DirectUtils.getDirectGraphics(var1).drawImage(f.ai, var31, var25 - 40 - var22, 0, 8192);
            }
         }
      }

      if (var3 + 22 > 20 && var3 < 128 && this.B % 2 == 0) {
         int var26 = var3;
         int var23 = 0;
         if (var26 < 20) {
            var23 = 20 - var26;
            var26 = 20;
         }

         byte var27 = G[super.R][super.ad][super.ab];
         var1.setClip(var2 + (c[60 + var27] & 255), var26 + (c[75 + var27] & 255), c[30 + var27] & 255, (c[45 + var27] & 255) - var23);
         if (super.ah == 0 && super.ag) {
            var1.drawImage(f.aj, var2 + (c[60 + var27] & 255) - (c[0 + var27] & 255), var3 + (c[75 + var27] & 255) - (c[15 + var27] & 255), 20);
         } else {
            this.ai
               .a(
                  var1,
                  var2 + (c[60 + var27] & 255) - (f.aj.getWidth() - (c[0 + var27] & 255) - (c[30 + var27] & 255)),
                  var3 + (c[75 + var27] & 255) - (c[15 + var27] & 255)
               );
         }
      }

      if (this.J != 0) {
         byte var28 = a[G[super.R][super.ad][super.ab]];
         byte var29 = b[G[super.R][super.ad][super.ab]];
         if (super.ad == 6) {
            var29 += 7;
         }

         if (var28 != 44 && var29 != 44) {
            var2 = (super.T >> 8) + f.r + 0 - var28 - 12;
            if (super.ag) {
               var2 = (super.T >> 8) + f.r + 0 + var28;
            }

            var3 = super.S * 14 + (super.Y >> 8) + f.s + -6 + var29;
            int var24 = 0;
            if (var3 < 20) {
               var24 = 20 - var3;
               var3 = 20;
            }

            if (var24 < 12 && var24 >= 0) {
               int var30 = this.J - 1;
               var1.setClip(var2, var3, 12, 12 - var24);
               if (super.ag) {
                  var1.drawImage(f.ak, var2, var3 - var30 * 12 - var24, 0);
                  return;
               }

               DirectUtils.getDirectGraphics(var1).drawImage(f.ak, var2, var3 - var30 * 12 - var24, 0, 8192);
            }
         }
      }
   }

   public final void l() {
      boolean var1 = true;
      if (super.ag) {
         int var2 = this.d();
         if ((super.T >> 8) % 22 > 11) {
            var2++;
         }

         for (int var3 = var2; var3 < var2 + 3 && var1; var3++) {
            for (int var4 = super.S; var4 >= super.S - 3 && var1; var4--) {
               short var5;
               if (var3 >= 0 && var4 >= 0 && ((var5 = this.ai.N.a(var3, var4)) == 97 || var5 == 98)) {
                  this.ai.a(var3, var4, var5);
                  var1 = false;
               }
            }
         }
      } else {
         int var6 = this.e();
         if ((super.T >> 8) % 22 < 11) {
            var6--;
         }

         for (int var7 = var6; var7 > var6 - 3 && var1; var7--) {
            for (int var8 = super.S; var8 >= super.S - 3 && var1; var8--) {
               short var9;
               if (var7 >= 0 && var8 >= 0 && ((var9 = this.ai.N.a(var7, var8)) == 97 || var9 == 98)) {
                  this.ai.a(var7, var8, var9);
                  var1 = false;
               }
            }
         }
      }
   }

   public final boolean m() {
      byte var1 = this.N[this.J];
      if (this.K[this.J] <= 0) {
         return false;
      }

      if (super.ad != 6 && super.ad != 7 && super.ad != 12 && super.ad != 13) {
         this.a((byte)7);
         super.ae = 0;
      }

      f var10000;
      int var10001;
      int var10002;
      byte var10003;
      switch (this.J) {
         case 0:
         default:
            return true;
         case 1:
            var10000 = this.ai;
            var10001 = super.T + (super.ag ? 2816 : -2816);
            var10002 = (this.c() << 8) + 1024;
            var10003 = 0;
            break;
         case 2:
            var10000 = this.ai;
            var10001 = super.T + (super.ag ? 256 : -256);
            var10002 = (this.c() << 8) + 1024;
            var10003 = 3;
            break;
         case 3:
            var10000 = this.ai;
            var10001 = super.T + (super.ag ? 256 : -256);
            var10002 = (this.c() << 8) + 1024;
            var10003 = 6;
            break;
         case 4:
            this.ai.d(super.T + (super.ag ? 2816 : -2816), (this.c() << 8) + 1024, 9 + var1);
            if (this.K[this.J] <= 0 || var1 != 2) {
               return true;
            }

            var10000 = this.ai;
            var10001 = super.T + (super.ag ? 2816 : -2816);
            var10002 = (this.c() << 8) + 1024;
            var10003 = 9;
            break;
         case 5:
            var10000 = this.ai;
            var10001 = super.T + (super.ag ? 256 : -256);
            var10002 = (this.c() << 8) + 1024 + this.K[this.J] % 2 * 1024;
            var10003 = 12;
            break;
         case 6:
            var10000 = this.ai;
            var10001 = super.T + (super.ag ? 256 : -256);
            var10002 = (this.c() << 8) + 1024;
            var10003 = 15;
            break;
         case 7:
            var10000 = this.ai;
            var10001 = super.T + (super.ag ? 256 : -256);
            var10002 = (this.c() << 8) + 1024;
            var10003 = 18;
      }

      var10000.d(var10001, var10002, var10003 + var1);
      return true;
   }

   public final void n() {
      if (super.ad < 4 || super.ad == 11) {
         this.ai.e(-1);
         if (super.ad != 11) {
            super.ae = 1;
            this.a((byte)8);
         }

         byte var3 = 0;
         byte var4 = 0;

         for (int var6 = f.au - 1; var6 >= 0; var6--) {
            byte var5 = this.ai.W[var6].R;
            if (this.ai.W[var6].R != -1 && this.ai.W[var6].ad != 5) {
               int var1 = this.ai.W[var6].c() - d.a[var5];
               int var2 = this.ai.W[var6].b() + d.b[var5];
               var3 = d.c[var5];
               var4 = d.d[var5];
               if (Math.abs(var1 - this.b()) <= 33
                  && Math.abs(var2 - this.c()) <= 14
                  && this.ai
                     .a(var1, var2, var3, var4, (super.T >> 8) + (super.ag ? l[this.A] : -l[this.A] - n[this.A]), this.c() + m[this.A], n[this.A], o[this.A])) {
                  this.F = true;
                  d var10000;
                  byte var10001;
                  byte[] var10002;
                  byte[] var10003;
                  byte var10004;
                  if (super.ad == 11) {
                     var10000 = this.ai.W[var6];
                     var10001 = this.ai.W[var6].aa;
                     var10002 = i;
                     var10003 = this.N;
                     var10004 = 1;
                  } else {
                     var10000 = this.ai.W[var6];
                     var10001 = this.ai.W[var6].aa;
                     var10002 = i;
                     var10003 = this.N;
                     var10004 = 0;
                  }

                  var10000.aa = (byte)(var10001 - var10002[var10003[var10004]]);
                  if (this.ai.W[var6].T > super.T) {
                     var10000 = this.ai.W[var6];
                     var10001 = 10;
                  } else {
                     var10000 = this.ai.W[var6];
                     var10001 = -10;
                  }

                  var10000.u = var10001;
                  if (this.ai.W[var6].aa <= 0) {
                     if (f.a.g && f.a.f != null) {
                        f.a.f.a(3, 1);
                     }

                     this.ai.W[var6].a((byte)5);
                     this.ai.W[var6].ae = 1;
                     if (var5 != 4) {
                        f.bC++;
                        f.bQ++;
                        if (this.ai.Q != 0) {
                           for (int var7 = 0; var7 < d.k[this.ai.W[var6].t]; var7++) {
                              this.ai.b(this.ai.W[var6].c(), this.ai.W[var6].b(), 0);
                           }
                        }
                     }
                  } else {
                     this.ai.W[var6].a((byte)4);
                     this.ai.W[var6].af = 0;
                  }
               }
            }
         }

         if (this.ai.Q == 12) {
            this.ai.a((super.T >> 8) + (super.ag ? l[this.A] : -l[this.A] - n[this.A]), this.c() + m[this.A], n[this.A], o[this.A]);
         }

         if (this.p == -1) {
            this.ai.bh = 0;
         }
      }
   }

   public final void o() {
      this.A = 0;
      a var10000;
      byte var10001;
      if (super.ad == 7 || super.ad == 8 || super.ad == 9 || super.ad == 0 || super.ad == 4) {
         super.ae = 0;
         var10000 = this;
         var10001 = 0;
      } else if (super.ad == 13) {
         super.ae = 0;
         var10000 = this;
         var10001 = 12;
      } else {
         if (super.ad != 14) {
            return;
         }

         super.ae = 0;
         var10000 = this;
         var10001 = 11;
      }

      var10000.a(var10001);
   }

   private void u() {
      if (++this.z > 15) {
         this.z = -1;
         this.A = 0;
      }
   }

   private void v() {
      int var1 = this.c() + 3 + 20;

      for (int var2 = 5; var2 >= 0; var2--) {
         byte var3 = this.ai.aY[var2];
         int var4 = this.ai.aU[var2];
         short var5 = this.ai.aW[var2];
         if (var3 != -1 && (super.aa != 20 || var3 != 2) && (var5 != 0 || Math.abs(var4 - super.T) <= 14080 && Math.abs((this.ai.aV[var2] >> 8) - var1) <= 22)) {
            label61: {
               int var10000;
               if (var4 > super.T) {
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
            if ((var7 = Math.abs(var4 - super.T)) <= 1408) {
               this.ai.e = true;
               if (var3 <= 1) {
                  this.ai.as += 10;
                  f.bE += 10;
                  if (this.ai.as > 99999) {
                     this.ai.as = 99999;
                  }
               } else if (var3 == 2) {
                  super.aa = (byte)(super.aa + 4);
                  if (super.aa > 20) {
                     super.aa = 20;
                  }
               } else {
                  this.a(1);
               }

               this.ai.aY[var2] = -1;
               return;
            }

            int var8 = this.ai.aV[var2] - (this.c() + 7 << 8);
            if (var7 != 0) {
               this.ai.aV[var2] = this.ai.aV[var2] + var8 * var5 * (var5 >= 0 ? -1 : 1) / var7;
            }

            this.ai.aU[var2] = var4;
            this.ai.aW[var2] = var5;
         }
      }
   }

   private void a(int var1) {
      int var2 = 1;
      short var3 = 9999;

      for (int var4 = 1; var4 < 8; var4++) {
         if (var4 != 6 && (this.M & 1 << var4) > 0 && this.K[var4] < var3 && this.K[var4] != d[var4 * 3 + this.N[var4]]) {
            var3 = this.K[var4];
            var2 = var4;
         }
      }

      this.K[var2] = (short)(this.K[var2] + var1 * f[var2]);
      if (this.K[var2] > d[var2 * 3 + this.N[var2]]) {
         this.K[var2] = d[var2 * 3 + this.N[var2]];
      }
   }

   private static void a(String var0) {
      byte[] var3 = new byte[4];

      try {
         InputStream var4 = var3.getClass().getResourceAsStream(var0);

         for (int var5 = 0; var5 < 15; var5++) {
            byte var1 = (byte)var4.read();
            byte var2 = (byte)var4.read();
            H[0][var5] = var1;
            I[0][var5] = var2;

            try {
               for (int var6 = 0; var6 < var1; var6++) {
                  var3[var6] = (byte)var4.read();
               }
            } catch (IOException var7) {
            }

            System.arraycopy(var3, 0, G[0][var5], 0, var1);
         }

         var4.close();
      } catch (IOException var8) {
      }
   }
}
