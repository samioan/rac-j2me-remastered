import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;

public final class c {
   public int a;
   public int b;
   public int c;
   public int d;
   public int e;
   public int f;
   public int g;
   public int h;
   public int i;
   public int j;
   public int k;
   public int l;
   public int m;
   public int n;
   public int o;
   public int p;
   public boolean q;
   public int r;
   public int s;
   public static byte[] t;
   public static byte[] u;
   public static byte[] v;
   public static byte[] w;
   public static byte[] x;
   public static byte[] y;
   public static byte[] z;
   public static byte[] A;
   public boolean B = false;
   public static byte[] C;
   public static byte[] D;
   public static byte[] E;
   public static byte[] F;
   public static final int[] G = new int[]{
      0,
      1,
      2,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      1,
      2,
      3,
      4,
      0,
      0,
      0,
      0,
      0,
      0,
      1,
      2,
      3,
      4,
      4,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      1,
      1,
      1,
      1,
      1,
      1,
      1,
      1,
      1,
      1,
      0,
      2,
      0,
      1,
      0,
      0,
      0,
      0,
      0,
      0,
      2,
      5,
      0,
      3,
      1,
      4,
      7,
      6,
      8,
      9,
      0,
      0,
      0,
      1,
      0,
      0,
      0,
      0,
      0,
      0
   };
   public static short[] H = new short[]{1024, 1344, 704, 0, 160, 704, 704, 704};
   public static byte[] I;
   public static byte[] J;
   public boolean K;
   public int L;
   public static b M;
   public int N;
   public boolean O;
   public boolean P;
   public boolean Q;
   public boolean R;
   public boolean S;
   public boolean T;
   public int U;
   public int V = 0;
   public int W = 0;
   public int X;
   public int Y;
   public int Z;
   public int aa;
   public int ab;
   public boolean ac;
   public boolean ad;
   public boolean ae = false;

   public c(b var1, int var2) {
      M = var1;
      this.a = -1;
      this.r = 0;
      this.aa = 0;
      this.q = true;
      this.K = false;
      this.m = var2;
      this.O = false;
      this.P = false;
      this.Q = false;
      this.X = 0;
   }

   public static void a() {
      J = new byte[320];
      t = new byte[8];
      u = new byte[8];
      v = new byte[8];
      w = new byte[8];
      x = new byte[8];
      y = new byte[8];
      z = new byte[8];
      A = new byte[8];
      a(2049);
   }

   public final int b() {
      return (this.e >> 8) + 38 - b.bm - -12;
   }

   public final int c() {
      return this.d >> 8;
   }

   private int f() {
      return ((this.d >> 8) + 12) / 57;
   }

   private int g() {
      return ((this.d >> 8) - 12) / 57;
   }

   private int a(boolean var1) {
      int var4 = b.bf;
      boolean var5 = false;
      if (var1) {
         this.U = b.g(this.h, this.c);
      }

      this.s = b.g(this.h, this.c + 1);

      int var3;
      for (var3 = this.c + 1; var3 < 18; var3++) {
         if ((b.eM[this.h] & 1 << var3) > 0) {
            var3 *= 38;
            var5 = true;
            break;
         }
      }

      if (!var5) {
         var3 = 684;
      }

      int var2 = this.d >> 8;

      for (int var6 = 0; var6 < 50; var6++) {
         if (b.bE[var6] != -1 && b.bC[var6] <= var2 + 8 && b.bC[var6] + var4 >= var2 - 8 && b.bD[var6] < var3 && b.bD[var6] > this.c * 38) {
            var3 = b.bD[var6];
         }
      }

      return var3;
   }

   private boolean h() {
      int var1 = b.bf;
      int var2 = b.bg;
      int var3 = this.d >> 8;
      int var4 = this.b();

      for (int var5 = 49; var5 >= 0; var5--) {
         if (b.bE[var5] >= 0
            && Math.abs(b.bC[var5] - var3) <= 57
            && Math.abs(b.bD[var5] - var4) <= 38
            && b.a(b.bC[var5], b.bD[var5], var1, var2, var3 - 8, var4, 16, 37)) {
            return true;
         }
      }

      return false;
   }

   public static void a(c var0, int var1, int var2) {
      M.b(var0.m, var0.b, G[var1 * 10 + var2]);
   }

   private void i() {
      this.h = this.c() / 57;
      int var10000 = this.a(true);
      int var1 = 0;
      var1 = var10000 / 38;
      this.Q = false;
      this.R = false;
      this.O = false;
      this.P = false;
      if (this.c + 1 < var1 && this.a != 3 || this.n == 2) {
         this.f += 256;
         this.e = this.e + this.f;
         this.c = (this.e >> 8) / 38;
         if (this.c + 1 == var1 && this.f > -1824) {
            this.n = 1;
            this.g = 0;
            if (this.a == 2 || this.a == 0 || this.a == 1 && this.n != 2) {
               a(this, this.a, 1);
            }

            this.f = 0;
            this.e = this.c * 38 << 8;
         }
      }

      if (this.c - 1 >= 0 && !M.h(this.h, this.c) && this.f <= 0) {
         this.f = -this.f;
      }

      if (this.a != 6
         && this.a != 7
         && this.a != 5
         && b.a(this.c() - t[this.a], this.b() + u[this.a], v[this.a], w[this.a], b.k() - b.ft, b.l() + -12 + b.fu, b.fv, b.fw)) {
         this.g = 0;
         if (this.f >= 0 && this.a != 3) {
            this.f = -(this.f >> 1);
            if (b.k() < this.c()) {
               this.g = 510;
               this.q = false;
            } else {
               this.g = -510;
               this.q = true;
            }
         } else {
            this.f = -this.f;
         }
      }

      if (this.g != 0) {
         int var2;
         if (this.q) {
            this.i = this.f();
            if ((var2 = this.g()) == this.h) {
               var2--;
            }
         } else {
            this.i = this.g();
            if ((var2 = this.f()) == this.h) {
               var2++;
            }
         }

         this.j = this.c;
         int var3 = this.d;
         int var4 = this.h;
         int var5;
         if ((var5 = (this.d >> 8) % 57) < 14 && !this.q) {
            this.h--;
         } else if (var5 > 43 && this.q) {
            this.h++;
         }

         this.d = this.d + (this.q ? 7296 : -7296);
         var10000 = this.a(false);
         int var6 = 0;
         var6 = var10000 / 38;
         this.P = var6 > var1;
         if (var6 != 27 && this.N != 0 && (this.P || this.Q || this.R)) {
            this.g = 0;
         }

         this.h = var4;
         this.d = var3;
         if (this.i > 0 && this.i < 27 && M.h(this.i, this.j)) {
            if (!M.h(this.h, this.j)) {
               if (this.c() % 57 < 28) {
                  this.d -= 3648;
               } else {
                  this.d += 3648;
               }
            }

            this.d = this.d + this.g;
            if (this.h()) {
               this.d = this.d - (this.g << 1);
               this.O = true;
            }
         } else {
            this.Q = true;
         }

         if (!M.h(var2, this.j)) {
            this.R = true;
         }
      }
   }

   public final void d() {
      if (this.n != 5 && this.l > 0) {
         if (this.n == 4 && this.N == 0 && this.a != 6 && this.a != 7 && this.a != 5) {
            if (++this.p > 10) {
               this.g = 0;
               this.o = 2;
               this.n = 0;
            }
         } else if (this.n != 7 && this.n != 6) {
            if (Math.abs(b.k() - this.c()) <= 399 && Math.abs(b.l() - this.b()) <= 342) {
               int var1 = b.k() - this.c();
               int var2 = b.l() + 38 - b.bm - this.b();
               this.T = var1 < 171
                  && M.h(this.h - 1, this.c - 1)
                  && M.h(this.h - 2, this.c - 1)
                  && var1 > 52
                  && Math.abs(var2) < 76
                  && (this.aa == 0 || this.a == 3);
               this.S = var1 < -52
                  && M.h(this.h + 1, this.c - 1)
                  && M.h(this.h + 2, this.c - 1)
                  && var1 > -171
                  && Math.abs(var2) < 76
                  && (this.aa == 0 || this.a == 3);
               if (this.a != 3 || Math.abs(b.k() - this.c()) <= 199 && Math.abs(b.l() - this.b()) <= 190) {
                  this.i();
                  if (this.s >= 26 && this.s <= 31 || this.U >= 26 && this.U <= 31) {
                     this.l--;
                     this.f = -304;
                     if (this.l <= 0) {
                        this.n = 0;
                        M.a(this.d, this.b() + 19 << 8, 21, this.q, false, 0, this.L);
                        this.o = 2;
                        this.a = -1;
                        return;
                     }
                  }

                  if (this.N != 0 && this.a != 3 && this.a != 0) {
                     this.g = this.N << 8;
                     this.f = 0;
                     this.n = 4;
                     if (this.a != 4) {
                        a(this, this.a, 4);
                     }

                     if (this.N > 0) {
                        this.N--;
                        this.q = true;
                     } else {
                        this.N++;
                        this.q = false;
                     }

                     if (this.Q) {
                        this.g = 0;
                     }
                  } else {
                     this.ad = false;
                     if (this.a == 0) {
                        if (this.O) {
                           this.q = !this.q;
                           this.aa = 10;
                        } else if (this.Q) {
                           if (this.U == -60) {
                              this.f = -510;
                              a(this, this.a, 2);
                              this.n = 2;
                              return;
                           }

                           this.q = !this.q;
                           this.aa = 10;
                        } else if (this.P) {
                           if (this.U == -61 && Math.abs(var1) > 28) {
                              this.f = H[this.a];
                              this.g = H[this.a] << 2;
                              if (!this.q) {
                                 this.g *= -1;
                              }

                              a(this, this.a, 2);
                              this.n = 2;
                              return;
                           }

                           if (this.U != -60) {
                              this.q = !this.q;
                           }
                        }

                        if (this.n != 2) {
                           this.ae = false;
                        }

                        if (this.n != 2 && Math.abs(var1) <= 57 && var2 < 0 && var2 >= -114 && this.L != 3) {
                           this.g = 0;
                           this.ae = true;
                           this.n = 2;
                           a(this, this.a, 2);
                           this.f = -2432;
                        } else if (this.T && var1 < 456) {
                           if (this.n != 1) {
                              a(this, this.a, 1);
                              this.n = 1;
                           }

                           if (this.L == 5) {
                              this.g = H[this.a] << 2;
                           } else {
                              this.g = H[this.a] << 1;
                           }

                           this.f = 1216;
                           this.q = true;
                           this.ae = false;
                        } else if (this.S && -var1 <= 456) {
                           if (this.n != 1) {
                              a(this, this.a, 1);
                              this.n = 1;
                           }

                           if (this.L == 5) {
                              this.g = -(H[this.a] << 2);
                           } else {
                              this.g = -(H[this.a] << 1);
                           }

                           this.f = 1216;
                           this.q = false;
                           this.ae = false;
                        }

                        if ((Math.abs(b.k() - this.c()) > 52 || Math.abs(b.l() + 38 - b.bm - this.b()) > 19)
                           && (var2 > -19 || var2 <= -76 || Math.abs(var1) > 28)) {
                           if (!this.ae) {
                              this.ae = false;
                              int var5 = this.g;
                              this.j();
                              if ((var5 > 0 || this.g <= 0) && (var5 < 0 || this.g >= 0)) {
                                 this.g = var5;
                              }
                           } else {
                              this.g = 0;
                           }
                        } else {
                           this.g = 0;
                           M.a(this.d, this.b() + 19 << 8, 21, this.q, false, 0, this.L);
                           this.a = -1;
                           int var3 = b.au;
                           if (M.di) {
                              var3 = 0;
                           }

                           int var4 = this.Y + var3 * 10 >> 5;
                           M.bM[var4] = M.bM[var4] & ~(1 << this.Y + var3 * 10 - (var4 << 5));
                        }

                        if (this.aa != 0) {
                           this.aa--;
                           return;
                        }
                     } else if (this.a == 1) {
                        if (var2 < 0 && var2 > -76 && Math.abs(var1) > 114 && this.L == 2) {
                           this.f = -2432;
                           this.g = -(H[this.a] << 1);
                           a(this, this.a, 2);
                           this.n = 2;
                           this.ab = 20;
                        } else if (this.O) {
                           this.q = !this.q;
                           if (this.n == 2) {
                              this.g = -this.g;
                           }
                        } else if (this.Q) {
                           if (this.U == -60 && Math.abs(var1) > 28 && this.L == 2) {
                              this.f = -2432;
                              this.g = H[this.a] << 1;
                              a(this, this.a, 2);
                              this.n = 2;
                              return;
                           }

                           if (this.n == 2) {
                              this.g >>= 1;
                           }

                           this.q = !this.q;
                        } else if (this.P) {
                           if (this.U == -61 && Math.abs(var1) > 28 && this.L == 2) {
                              this.f = -3328;
                              this.g = H[this.a] << 2;
                              if (!this.q) {
                                 this.g *= -1;
                              }

                              a(this, this.a, 2);
                              this.n = 2;
                              return;
                           }

                           if (this.U != -60) {
                              this.q = !this.q;
                           }
                        }

                        if ((this.n == 2 || !this.T || this.L == 0 || var1 <= 104 && !this.P || this.R || this.h())
                           && (var1 <= 52 || var2 >= 19 || var2 <= -76 || !this.h())) {
                           if (this.n != 2 && this.S && this.L != 0 && (var1 < -104 || this.P) && !this.R && !this.h()
                              || var1 < -52 && var2 < 19 && var2 > -76 && this.h()) {
                              if (this.n != 2) {
                                 a(this, this.a, 2);
                                 this.n = 2;
                              }

                              this.g = -H[this.a] << 2;
                              this.f = -608;
                              this.q = false;
                           }
                        } else {
                           if (this.n != 2) {
                              a(this, this.a, 2);
                              this.n = 2;
                           }

                           this.g = H[this.a] << 2;
                           this.f = -608;
                           this.q = true;
                        }

                        if (this.ab > 0) {
                           this.ab--;
                           return;
                        }

                        if (this.n == 2) {
                           return;
                        }

                        if (Math.abs(var1) > 52 || Math.abs(var2) > 52 || Math.abs(var2) >= 38) {
                           if (this.n == 2 && !this.Q) {
                              this.q = !this.q;
                              return;
                           }

                           this.j();
                           return;
                        }

                        this.g = 0;
                        if (b.ff == 10) {
                           return;
                        }

                        this.q = b.k() - this.c() > 0;
                        if (--this.X < 0) {
                           this.X = I[this.a];
                           if (this.n != 2 && this.n != 3) {
                              a(this, this.a, 3);
                              this.n = 3;
                              this.o = 1;
                           }

                           if (b.fN > 0) {
                              return;
                           }

                           if (b.a(
                              b.k() - b.ft,
                              b.l() + b.fu + -12,
                              b.fv,
                              b.fw,
                              this.c() + (this.q ? x[this.a] : -x[this.a] - z[this.a]),
                              this.b() + y[this.a],
                              z[this.a],
                              A[this.a]
                           )) {
                              if (!b.D) {
                                 b.fe = b.fe - D[this.L];
                              }

                              M.ay = 1;
                              b.fg = 0;
                              if (b.fB == 2) {
                                 b.fB = 1;
                              }

                              b.K = true;
                           }
                        }

                        if (this.n == 2) {
                           return;
                        }
                     } else if (this.a == 2) {
                        if (this.n == 0) {
                           this.W = this.W + b.v;
                           if (this.W >= 1100) {
                              this.o = 2;
                              this.ae = false;
                              this.W = 0;
                              this.q = !this.q;
                           }
                        }

                        if ((this.O || this.Q || this.P) && this.n != 0 && this.n != 3 && this.o != 2 && this.n != 2) {
                           a(this, this.a, 0);
                           this.n = 0;
                           this.g = 0;
                           this.o = 1;
                        }

                        Math.abs(this.ab - this.d);
                        this.V++;
                        if (this.T && this.n != 3 && var1 < 104 && Math.abs(var2) < 38 && !this.R && !this.P && !this.Q && this.V > 15) {
                           if (this.n != 2 && b.g((this.d >> 8) / 57 + 1, this.c + 1) != 27) {
                              a(this, this.a, 2);
                              this.n = 2;
                              this.o = 1;
                              this.g = -H[this.a] << 1;
                              this.f = -2432;
                              this.q = false;
                              this.V = 0;
                              this.ae = true;
                              return;
                           }
                        } else if (this.S && this.n != 3 && var1 > -104 && Math.abs(var2) < 38 && !this.R && !this.P && !this.Q && this.V > 15) {
                           if (this.n != 2 && b.g((this.d >> 8) / 57 + 1, this.c + 1) != 27) {
                              a(this, this.a, 2);
                              this.n = 2;
                              this.o = 1;
                              this.g = H[this.a] << 1;
                              this.f = -2432;
                              this.q = true;
                              this.V = 0;
                              this.ae = true;
                              return;
                           }
                        } else if (this.Z == 0) {
                           if (Math.abs(var2) <= 38 && Math.abs(var1) < 171 && this.n != 2) {
                              this.q = var1 > 0;
                              this.g = 0;
                              this.f = 0;
                              this.Z = 1;
                              this.aa = 0;
                              this.X = 0;
                              this.ae = false;
                              return;
                           }

                           if (this.n != 0 && this.n != 2 || this.o == 2 && this.n == 0) {
                              this.ae = false;
                              this.j();
                              return;
                           }
                        } else if (this.Z == 1 && this.n != 2 && this.n != 4) {
                           if (this.T && !this.q) {
                              this.q = true;
                           } else if (this.S && this.q) {
                              this.q = false;
                           }

                           if (this.aa < F[this.L] && this.Z != 0) {
                              this.k();
                              return;
                           }

                           if (this.o == 2) {
                              this.Z = 0;
                              return;
                           }

                           if (this.n != 0 && this.n != 2 && !this.ac) {
                              this.o = 1;
                              a(this, this.a, 0);
                              this.n = 0;
                              this.g = 0;
                              return;
                           }
                        }
                     } else if (this.a == 3) {
                        this.W = this.W + b.v;
                        if (this.Z == 0) {
                           if (this.aa >= F[this.L] || this.W <= 100 && this.L != 15 && this.L != 18 && this.L != 21) {
                              this.aa = 0;
                              this.Z = 1;
                           } else {
                              this.k();
                           }
                        }

                        if (this.Z == 1 && this.aa++ > 10) {
                           switch (this.r) {
                              case 0:
                                 if (-var2 > (var1 > 0 ? var1 : -var1)) {
                                    this.K = true;
                                    this.Z = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 if (this.T) {
                                    this.q = true;
                                    this.K = false;
                                    this.Z = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 if (this.S) {
                                    this.q = false;
                                    this.K = false;
                                    this.Z = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 this.Z = 1;
                                 return;
                              case 1:
                                 if (var2 > (var1 > 0 ? var1 : -var1)) {
                                    this.K = true;
                                    this.Z = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 if (this.T) {
                                    this.q = true;
                                    this.K = false;
                                    this.Z = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 if (this.S) {
                                    this.q = false;
                                    this.K = false;
                                    this.Z = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 this.Z = 1;
                                 return;
                              case 2:
                                 if (-var2 > (var1 > 0 ? var1 : -var1)) {
                                    this.q = true;
                                    this.K = false;
                                    this.Z = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 if (var2 > (var1 > 0 ? var1 : -var1)) {
                                    this.K = false;
                                    this.q = false;
                                    this.Z = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 if (this.T) {
                                    this.K = true;
                                    this.Z = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 this.Z = 1;
                              default:
                                 return;
                              case 3:
                                 if (-var2 > (var1 > 0 ? var1 : -var1)) {
                                    this.q = true;
                                    this.K = false;
                                    this.Z = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 if (var2 > (var1 > 0 ? var1 : -var1)) {
                                    this.K = false;
                                    this.q = false;
                                    this.Z = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 if (this.S) {
                                    this.K = true;
                                    this.Z = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 this.Z = 1;
                                 return;
                           }
                        }
                     } else {
                        if (this.a == 4) {
                           if (this.O) {
                              this.q = !this.q;
                           } else if (this.Q) {
                              this.q = !this.q;
                           } else if (this.P) {
                              this.q = !this.q;
                           }

                           this.j();
                           return;
                        }

                        if (this.a == 6 || this.a == 7 || this.a == 5) {
                           if (b.aw == 4) {
                              a(this, this.a, 0);
                              this.q = false;
                              return;
                           }

                           if (!b.O) {
                              if (-var2 > 171 && var1 > 0 && this.q) {
                                 this.q = false;
                              } else if (-var2 > 171 && var1 < 0 && !this.q) {
                                 this.q = true;
                              } else if (var1 > 28 && !b.O) {
                                 this.q = true;
                                 this.g = H[2];
                                 if (this.n != 1 && this.a == 6 && this.ac) {
                                    if (b.L) {
                                       a(this, this.a, 1);
                                    } else {
                                       a(this, this.a, 3);
                                    }

                                    this.n = 1;
                                 }
                              } else if (var1 < -28 && !b.O) {
                                 this.q = false;
                                 this.g = -H[2];
                                 if (this.n != 1 && this.a == 6 && this.ac) {
                                    if (b.L) {
                                       a(this, this.a, 1);
                                    } else {
                                       a(this, this.a, 3);
                                    }

                                    this.n = 1;
                                 }
                              }
                           } else {
                              this.g = 0;
                              if (this.a != 7) {
                                 this.n = 0;
                              }

                              if (this.a == 6) {
                                 if (!b.L) {
                                    a(this, this.a, 2);
                                 } else {
                                    a(this, this.a, 0);
                                 }
                              }
                           }

                           if (this.a == 7) {
                              if (!this.ac) {
                                 b.O = false;
                                 a(this, this.a, 0);
                                 this.n = 0;
                              }

                              if (-var2 < 38 && var2 < 0 || var2 > 0 && var2 < 38) {
                                 if ((this.q && var1 <= 71 && var1 >= 0 || !this.q && var1 >= -71 && var1 < 0) && this.n != 3) {
                                    this.B = false;
                                    this.W = 0;
                                    a(this, this.a, 3);
                                    this.n = 3;
                                    b.O = true;
                                    this.W = 0;
                                 }
                              } else if (this.n != 0 && this.ac) {
                                 this.n = 0;
                                 a(this, this.a, 0);
                                 b.O = false;
                                 this.W = 0;
                              }

                              if (this.n == 3) {
                                 this.W = this.W + b.v;
                                 if (this.W >= 700) {
                                    if (!this.B
                                       && b.a(
                                          b.k() - b.ft,
                                          b.l() + b.fu + -12,
                                          b.fv,
                                          b.fw,
                                          this.c() + (this.q ? x[this.a] : -x[this.a] - z[this.a]),
                                          this.b() + y[this.a],
                                          z[this.a],
                                          A[this.a]
                                       )) {
                                       b.fe = b.fe - D[this.L];
                                       b.K = true;
                                       if (b.ff != 10) {
                                          M.m(9);
                                          b.fg = 0;
                                       }

                                       this.W = 0;
                                       this.B = true;
                                       return;
                                    }

                                    if (this.W >= 1400) {
                                       this.B = false;
                                       return;
                                    }
                                 }
                              }
                           } else if (this.a == 5) {
                              this.W = this.W + b.v;
                              if (-var2 < 76) {
                                 if (this.q && var1 > 57 && var1 < 228 || !this.q && var1 < -57 && var1 > -228) {
                                    if (this.W > 1000) {
                                       a(this, this.a, 3);
                                       this.W = 0;
                                       this.n = 3;
                                       M.a(this.d + (this.q ? 7296 : -7296), this.b() + (b.bm >> 1) - 38 << 8, 4, this.q, this.K, this.r, this.L);
                                       return;
                                    }
                                 } else if (this.n != 0 && this.ac) {
                                    this.n = 0;
                                    if (b.O) {
                                       a(this, this.a, 1);
                                       return;
                                    }

                                    a(this, this.a, 0);
                                    return;
                                 }
                              } else if (this.n != 0 && this.ac) {
                                 this.n = 0;
                                 a(this, this.a, 0);
                                 return;
                              }
                           } else if (this.a == 6) {
                              if (!b.L
                                 && b.a(
                                    b.k() - b.ft,
                                    b.l() + b.fu + -12,
                                    b.fv,
                                    b.fw,
                                    this.c() + (this.q ? t[this.a] - v[this.a] : -t[this.a]),
                                    this.b() + u[this.a],
                                    v[this.a],
                                    w[this.a]
                                 )) {
                                 b.fe -= 4;
                                 b.K = true;
                                 return;
                              }

                              if (!b.L) {
                                 if (this.W > 800) {
                                    if (-var2 > (var1 > 0 ? var1 : -var1)) {
                                       a(this, this.a, 7);
                                       M.a(this.d + (this.q ? -6384 : 6384), this.b() - (b.bm >> 2) << 8, 2, this.q, true, this.r, this.L);
                                       this.W = 0;
                                       return;
                                    }

                                    if (this.T && this.q) {
                                       M.a(this.d + (this.q ? 7296 : -7296), this.b() + (b.bm >> 1) << 8, 2, this.q, false, this.r, this.L);
                                       a(this, this.a, 6);
                                       this.W = 0;
                                       return;
                                    }

                                    if (this.S && !this.q) {
                                       M.a(this.d + (this.q ? 7296 : -7296), this.b() + (b.bm >> 1) << 8, 2, this.q, false, this.r, this.L);
                                       a(this, this.a, 6);
                                       this.W = 0;
                                       return;
                                    }
                                 } else {
                                    this.W = this.W + b.v;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            } else {
               this.g = this.f = 0;
            }
         }
      } else {
         if (this.a == 7) {
            b.L = false;
            b.O = false;
            M.a(this.d + (this.q ? 3648 : -3648), (this.b() << 8) + 9728, 30, this.q, false, 0, this.L);
         } else if (this.a == 6) {
            b.N = false;
            M.a(this.d, this.b() + 19 << 8, 30, this.q, false, 0, this.L);
            M.cO = true;
            b.cX = b.cJ[17];
            M.l(b.cK[17]);
         } else if (this.a == 5) {
            b.M = false;
            M.a(this.d + (this.q ? -7296 : 7296), (this.b() << 8) - 4864, 30, this.q, false, 0, this.L);
            M.a(this.d + (this.q ? -14592 : 14592), (this.b() << 8) - 38, 30, this.q, false, 0, this.L);
            M.a(this.d + (this.q ? -3648 : 3648), (this.b() << 8) + 9728, 30, this.q, false, 0, this.L);
         }

         this.a = -1;
      }
   }

   public final void e() {
      if (this.o != 2) {
         if (this.a == -1) {
            M.a(this.d, this.b() + 19 << 8, 30, this.q, false, 0, this.L);
         } else if (this.a == 1 || this.a == 2 || this.a == 0 || this.a == 6 || this.a == 7 || this.a == 5 || this.a == 4) {
            this.ac = M.d(this.m, b.v);
         }
      }
   }

   public final void a(Graphics var1, int var2, int var3, int var4) {
      b.Z = var3;
      b.aa = var4;
      int var5 = this.c() + b.Z - 22;
      int var6 = this.b() + b.aa;
      if (var5 >= -44 && var5 <= 240 && var6 >= -b.bm && var6 <= 320) {
         f[] var7 = b.bh[this.a];
         int var8 = 0;
         if ((var2 & 1) > 0) {
            if (this.a != 3) {
               byte var9 = 0;
               if (!this.q) {
                  var9 = 2;
                  var5 += 57;
               }

               if (this.a == 1 || this.a == 2 || this.a == 0 || this.a == 6 || this.a == 7 || this.a == 5 || this.a == 4) {
                  M.a(var1, b.bh[this.a], this.m, var5, var6, var9);
                  return;
               }
            } else {
               if (this.K) {
                  byte var16 = J[(this.a * 8 + this.n) * 5];
                  var8 = var16 + 2;
               }

               byte var10 = 0;
               byte var11 = 0;
               int var12;
               byte var17;
               switch (this.r) {
                  case 1:
                     var17 = 0;
                     var12 = this.q ? 1 : 3;
                     break;
                  case 2:
                     var17 = 6;
                     var12 = this.q ? 2 : 3;
                     break;
                  case 3:
                     var17 = 6;
                     var12 = this.q ? 0 : 1;
                     break;
                  default:
                     var17 = 0;
                     var12 = this.q ? 0 : 2;
               }

               boolean var13 = (var12 & 2) > 0;
               boolean var14 = (var12 & 1) > 0;
               if (var13) {
                  var5 += 57;
               }

               if (var14) {
                  var6 += 50;
               }

               if ((var8 & 1) > 0) {
                  if (var17 == 0) {
                     if (!this.K) {
                        var10 = (byte)(this.q ? -1 : 1);
                     } else {
                        var11 = (byte)(var14 ? -1 : 1);
                     }
                  } else if (this.K) {
                     var10 = (byte)(var13 ? -1 : 1);
                  } else {
                     var11 = (byte)(this.q ? 1 : -1);
                  }
               }

               int var15 = this.r == 1 ? 4 : 0;
               var7[var17].a(var1, var5, var6 + var15, var12);
               var7[var17 + 1].a(var1, var5 + var10, var6 + var11 + var15, var12);
               var7[var17 + 2 + var8].a(var1, var5 + var10, var6 + var11 + var15, var12);
            }
         }
      }
   }

   private void j() {
      if (this.n != 1) {
         if (this.a == 1 || this.a == 2 || this.a == 0 || this.a == 4) {
            a(this, this.a, 1);
            this.n = 1;
         }

         this.n = 1;
      }

      this.o = 0;
      this.aa = 0;
      this.g = this.q ? H[this.a] : -H[this.a];
   }

   private void k() {
      if (--this.X < 0) {
         if (this.a == 2) {
            if (this.n != 3 && this.n != 2) {
               a(this, this.a, 3);
               this.n = 3;
            }

            this.o = 1;
         } else {
            this.n = 3;
         }

         int var1 = 0;
         int var2 = this.d;
         switch (this.a) {
            case 2:
               if (!this.K) {
                  var1 -= 2;
               }
               break;
            case 3:
               switch (this.r) {
                  case 0:
                     if (!this.K) {
                        var1 += 2;
                        var2 += (this.q ? 12 : -12) << 8;
                     } else {
                        var2 += (this.q ? -10 : 10) << 8;
                        var1 = -28;
                     }
                     break;
                  case 1:
                     if (!this.K) {
                        var1 = -12;
                        var2 += (this.q ? 12 : -12) << 8;
                     } else {
                        var2 += (this.q ? -10 : 6) << 8;
                        var1 += 28;
                     }
                     break;
                  case 2:
                     if (!this.K) {
                        var1 = 0 + ((this.q ? -12 : 12) << 1);
                        var2 -= 1792;
                     } else {
                        var1 = 0 - (this.q ? 12 : 0);
                        var2 += 5120;
                     }
                     break;
                  case 3:
                     if (!this.K) {
                        var1 = 0 + ((this.q ? -12 : 12) << 1);
                     } else {
                        var1 = 0 - (this.q ? 12 : 0);
                        var2 -= 5120;
                     }
               }
         }

         int var3 = this.b() + (b.bm >> 1) + var1 << 8;
         byte var4 = 0;
         if (this.a == 2) {
            var4 = (byte)(this.q ? 7168 : -7168);
         }

         if (this.L == 6 || this.L == 9 || this.L == 12 || this.L == 15 || this.L == 18 || this.L == 21) {
            M.a(var2 + var4, var3, 0, this.q, this.K, this.r, this.L);
         } else if (this.L == 7 || this.L == 10 || this.L == 13 || this.L == 16 || this.L == 19 || this.L == 22) {
            M.a(var2 + var4, var3, 6, this.q, this.K, this.r, this.L);
         } else if (this.L == 7 || this.L == 10 || this.L == 13) {
            M.a(var2 + var4, var3, 18, this.q, this.K, this.r, this.L);
         } else if (this.L == 17 || this.L == 20 || this.L == 23) {
            M.a(var2, var3, 3, this.q, this.K, this.r, this.L);
         }

         this.X = I[this.a];
         this.aa++;
      } else {
         if (this.a == 3) {
            this.W = 0;
         }

         this.o = 2;
      }
   }

   private static void a(DataInputStream var0, byte[] var1, int var2, boolean var3) {
      int var4;
      if (var3) {
         var4 = 44;
      } else {
         var4 = b.bm;
      }

      for (int var5 = 0; var5 < var2; var5++) {
         try {
            var1[var5] = var0.readByte();
         } catch (IOException var6) {
         }

         var1[var5] = (byte)(var1[var5] * var4 / 44);
      }
   }

   private static void a(int var0) {
      try {
         DataInputStream var1;
         a(var1 = M.e(var0), t, 8, true);
         a(var1, u, 8, false);
         a(var1, v, 8, true);
         a(var1, w, 8, false);
         a(var1, x, 8, true);
         a(var1, y, 8, false);
         a(var1, z, 8, true);
         a(var1, A, 8, false);
         var1.close();
      } catch (IOException var2) {
      }
   }
}
