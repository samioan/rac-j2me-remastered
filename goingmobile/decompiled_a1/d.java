import java.io.InputStream;
import javax.microedition.lcdui.Graphics;

public final class d {
   public static byte a;
   public static byte b;
   public h c;
   public static byte[][][] d;
   public static int[] e;
   public byte[][] f;
   public static byte g;
   public static byte[][] h;
   public static byte[] i;
   public static byte j;
   public static byte k;
   public static byte l;
   public static final byte[] m = new byte[]{15, 15, 16, 17, 18, 18, 19, 20, 21, 21, 22, 23};
   public static final byte[] n = new byte[]{3, 4, 5, 0, 1, 2, 6, 7, 7, 9, 10, 10, 12, 13};
   public static final byte[] o = new byte[]{0, 2, 1, 3};
   public static final byte[][] p = new byte[][]{
      {1, -1, -1, -1, -1, 0, 2},
      {6, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, 5, 3, -1, -1, -1, 4, 0, 1, 2, 3, 4, 5, 0},
      {4, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, -1, 2, 0, 1, 2, 3, 1},
      {5, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, -1, 3, 4, 0, 1, 2, 3, 2},
      {6, -1, -1, 1, -1, -1, 2, 3, 0, 1, -1, 4, -1, -1, 4, -1, 1, 3, -1, 5, 2, -1, -1, -1, 4, 0, 1, 3, 2, 4, 5, 2},
      {5, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, -1, 3, 2, 1, 0, 3, 4, 2},
      {5, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, -1, 3, 0, 1, 2, 3, 4, 0},
      {3, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, -1, 1, 0, 2, 1, 0},
      {4, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, -1, 2, 0, 1, 2, 3, 1},
      {6, -1, 1, 2, -1, 0, -1, 3, -1, -1, 3, 4, 0, 2, -1, -1, 1, -1, -1, 5, 2, -1, -1, -1, 4, 0, 4, 3, 5, 1, 2, 1},
      {6, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, 5, 3, -1, -1, -1, 4, 0, 1, 2, 3, 5, 4, 2},
      {
            12,
            -1,
            1,
            8,
            -1,
            0,
            2,
            9,
            -1,
            1,
            3,
            10,
            -1,
            2,
            4,
            11,
            -1,
            3,
            5,
            -1,
            -1,
            4,
            6,
            -1,
            -1,
            5,
            7,
            -1,
            -1,
            6,
            -1,
            -1,
            -1,
            -1,
            9,
            -1,
            0,
            8,
            10,
            -1,
            1,
            9,
            11,
            -1,
            2,
            10,
            -1,
            -1,
            3,
            0,
            1,
            2,
            3,
            0,
            4,
            1,
            2,
            5,
            3,
            4,
            5,
            1
      },
      {1, -1, -1, -1, -1, 0, 2}
   };

   public d(h var1) {
      this.c = var1;
      j = h.F;
      k = h.G;
      l = 0;
      h = new byte[12][4];
      i = new byte[12];
      d = new byte[12][28][18];
      e = new int[28];
      this.f = new byte[28][18];
   }

   public final short a(int var1, int var2) {
      return var1 >= 0 && var1 < 28 && var2 >= 0 && var2 < 18 ? d[i[this.c.X]][var1][var2] : -1;
   }

   public final void a(int var1) {
      byte[] var2 = p[var1];
      int var5 = 0;
      var5++;
      g = var2[0];

      for (int var3 = 0; var3 < g; var3++) {
         for (int var4 = 0; var4 < 4; var4++) {
            h[var3][var4] = var2[var5++];
         }
      }

      for (int var6 = 0; var6 < g; var6++) {
         i[var6] = var2[var5++];
      }

      a = 0;
      b = var2[var5];
   }

   public final void b(int var1) {
      this.a(var1);
      this.c.x();

      try {
         InputStream var2 = this.getClass().getResourceAsStream("/level" + var1 + ".bin");

         for (int var3 = 0; var3 < g; var3++) {
            for (int var4 = 0; var4 < 28; var4++) {
               for (int var5 = 0; var5 < 18; var5++) {
                  int var6 = var2.read() - 32;
                  d[var3][var4][var5] = (byte)var6;
               }
            }
         }

         var2.close();
      } catch (Exception var7) {
      }
   }

   public final void a(int var1, boolean var2) {
      int var3 = 0;
      int var4 = 0;
      boolean var7 = false;
      boolean var8 = false;
      boolean var9 = false;
      this.c.X = (short)var1;
      this.c.d(this.c.Z, var1);

      for (int var10 = 3; var10 >= 0; var10--) {
         this.c.cq[var10] = -1;
      }

      for (int var14 = 49; var14 >= 0; var14--) {
         this.c.bq[var14] = this.c.br[var14] = -1;
         this.c.bo[var14] = 0;
         this.c.bs[var14] = false;
         this.c.bn[var14] = -1;
      }

      for (int var15 = h.ba - 1; var15 >= 0; var15--) {
         this.c.ai[var15].Z = -1;
      }

      this.c.bP = -1;
      this.c.bQ = -1;

      for (int var16 = 3; var16 >= 0; var16--) {
         this.c.bc[var16] = -1;
         this.c.be[var16] = -1;
      }

      for (int var17 = 11; var17 >= 0; var17--) {
         this.c.ck[var17] = -1;
      }

      for (int var18 = 2; var18 >= 0; var18--) {
         this.c.bC[var18] = 0;
         this.c.bF[var18] = -1;
      }

      for (int var19 = 9; var19 >= 0; var19--) {
         this.c.al[var19].f = -1;
      }

      for (int var20 = 9; var20 >= 0; var20--) {
         this.c.ak[var20].f = -1;
      }

      this.c.cc = -1;
      this.c.cd = -1;
      this.c.ca = -1;
      this.c.cb = -1;
      this.c.bX = -1;
      this.c.bY = -1;
      this.c.cT = true;
      if (h[var1][1] != -1) {
         this.c.cT = false;
      }

      for (int var21 = 7; var21 >= 0; var21--) {
         this.c.bj[var21] = -1;
      }

      for (int var22 = 27; var22 >= 0; var22--) {
         for (int var11 = 17; var11 >= 0; var11--) {
            short var5 = this.a(var22, var11);
            if (var7) {
               this.f[var22][var11 + 1] = (byte)var5;
            } else if (var8) {
               if (var5 % 2 == 0 && var5 < 12) {
                  this.f[var22][var11 + 1] = (byte)(var5 + 1);
               } else {
                  this.f[var22][var11 + 1] = (byte)var5;
               }
            }

            if (var9) {
               short var6 = var5;
               if (this.f[var22][var11 + 2] >= 19) {
                  for (int var23 = 1; var6 >= 47 && var6 <= 55; var23++) {
                     var6 = this.a(var22, var11 - var23);
                  }

                  if (var6 % 2 == 0 && var6 < 12) {
                     this.f[var22][var11 + 1] = (byte)(var6 + 1);
                  } else {
                     this.f[var22][var11 + 1] = (byte)var6;
                  }
               } else if (var6 >= 47 && var6 <= 55) {
                  for (int var12 = 1; var6 >= 47 && var6 <= 55; var12++) {
                     var6 = this.a(var22, var11 - var12);
                  }

                  this.f[var22][var11 + 1] = (byte)var6;
               }
            }

            var7 = false;
            var8 = false;
            var9 = false;
            if (var5 >= 47 && var5 <= 55) {
               var7 = true;
               var9 = true;
               switch (var5) {
                  case 47:
                     this.c.a(var22, var11, 0, 0, var3);
                     break;
                  case 48:
                     this.c.a(var22, var11, 2, 0, var3);
                     break;
                  case 49:
                     this.c.a(var22, var11, 1, 0, var3);
                     break;
                  case 50:
                     this.c.a(var22, var11, 0, 0, var3++);
                     this.c.a(var22, var11, 0, 1, var3);
                     break;
                  case 51:
                     this.c.a(var22, var11, 0, 0, var3++);
                     this.c.a(var22, var11, 2, 1, var3);
                     break;
                  case 52:
                     this.c.a(var22, var11, 0, 0, var3++);
                     this.c.a(var22, var11, 1, 1, var3);
                     break;
                  case 53:
                     this.c.a(var22, var11, 0, 2, var3);
                     break;
                  case 54:
                     this.c.a(var22, var11, 2, 2, var3);
                     break;
                  case 55:
                     this.c.a(var22, var11, 1, 2, var3);
               }

               var3++;
            } else if (var5 == -97 || var5 == -96 || var5 == -95) {
               this.f[var22][var11] = 1;
            } else if (var5 == -98) {
               this.f[var22][var11] = 0;
               this.c.a(var22, var11);
            } else if (var5 == 35) {
               if (this.c.f(this.c.Z, this.c.X)) {
                  this.c.bP = (byte)var22;
                  this.c.bQ = (byte)var11;
               }

               var7 = true;
            } else if (var5 >= -118 && var5 <= -99) {
               var8 = true;
            } else if (var5 == -93) {
               if (this.c.h(11)) {
                  this.c.ca = (byte)var22;
                  this.c.cb = (byte)var11;
               }

               var8 = true;
            } else if (var5 == -94) {
               this.c.cc = (byte)var22;
               this.c.cd = (byte)var11;
               var8 = true;
            } else if (var5 == -92) {
               if (this.c.bZ) {
                  this.c.bX = (byte)var22;
                  this.c.bY = (byte)var11;
               }

               var8 = true;
            } else if (var5 == 99) {
               var8 = true;
            } else if (var5 == 100) {
               this.f[var22][var11] = this.f[var22 + 1][var11];
            } else if (var5 == 56) {
               this.f[var22][var11] = 27;
            } else if (var5 == 57) {
               this.f[var22][var11] = 22;
            } else if (var5 == 58) {
               this.f[var22][var11] = 20;
            } else if (var5 == 59) {
               this.f[var22][var11] = 21;
            } else if (var5 == 60) {
               this.f[var22][var11] = 22;
            } else if (var5 == 61) {
               this.f[var22][var11] = 22;
            } else if (var5 == -125) {
               if (var2) {
                  this.c.ah = (byte)var1;
                  this.c.ad = (byte)var22;
                  this.c.ae = (byte)(var11 - 1);
                  if (var22 < 1) {
                     this.c.af = 0;
                  } else {
                     this.c.af = (short)(-j * (var22 - 1));
                  }

                  if (var11 < 6) {
                     this.c.ag = 0;
                  } else {
                     this.c.ag = (short)(-k * (var11 - 5));
                  }
               }

               this.f[var22][var11] = 22;
            } else if (var5 == -127) {
               this.f[var22][var11] = 0;
            } else if (var5 == -126) {
               this.f[var22][var11] = 1;
            } else if (var5 >= 62 && var5 <= 69) {
               var7 = true;
               int var25 = var5 - 62;
               this.c.a(var22, var11, var25 % 2 == 0, var25 >> 1);
            } else if (var5 == 97 || var5 == 98) {
               var7 = true;
            } else if (var5 == -124) {
               var8 = true;
            } else if (var5 >= 43 && var5 <= 46) {
               var7 = true;
               if (this.a(var22, var11 + 1) >= 19) {
                  var8 = true;
                  var7 = false;
               }

               this.c.d(var22, var11, o[var5 - 43]);
            } else if (var5 >= 114 && var5 <= 127) {
               var8 = true;
               this.c.a(var22, var11, n[var5 - 114], var4++);
            } else if (var5 == -128) {
               var8 = true;
               this.c.a(var22, var11, (byte)13, var4++);
            } else if (var5 >= 102 && var5 <= 113) {
               if ((var5 - 102) % 2 == 0) {
                  var7 = true;
               } else {
                  this.f[var22][var11] = this.f[var22 + 1][var11];
               }

               this.c.a(var22, var11, m[var5 - 102], var4++);
            } else if ((var5 < 70 || var5 > 76) && var5 != -119) {
               if (var5 >= 36 && var5 <= 42) {
                  var8 = true;
                  this.c.a((byte)var22, (byte)var11);
               } else if (var5 >= 77 && var5 <= 96) {
                  var8 = true;
               } else if (var5 == 101) {
                  var7 = true;
                  int var24 = var22 * j + (j >> 1);
                  int var13 = (var11 + 1) * k + (k >> 1);
                  this.c.a(var24 << 8, var13 << 8, 32, true, true, 0, (byte)26);
               } else if (var5 > 34) {
                  var7 = true;
               } else {
                  this.f[var22][var11] = (byte)var5;
               }
            } else {
               this.f[var22][var11] = 1;
               this.c.a((byte)var22, (byte)var11, var5);
            }
         }
      }

      this.a();
      this.c.Q();
      if (this.c.cV) {
         this.c.bt[0] = false;
      } else {
         this.c.bt[this.c.X] = false;
      }
   }

   public final void a() {
      for (byte var2 = 27; var2 >= 0; var2--) {
         e[var2] = 0;

         for (byte var3 = 0; var3 < 18; var3++) {
            short var1;
            if ((var1 = this.f[var2][var3]) >= 19 && var1 <= 27 || var1 >= 19 && var1 <= 34) {
               e[var2] = e[var2] | 1 << var3;
            }
         }
      }
   }

   public final void a(Graphics var1) {
      int var2 = h.x;
      int var3 = h.y;
      byte var4 = (byte)(-var2 / j);
      byte var5 = (byte)(-var3 / k);
      byte var6 = var4;
      byte var7 = var5;
      int var8 = var4 * j + var2;
      int var9 = var5 * k + var3;
      int var14 = var8;
      int var15 = var9;
      var6 = (byte)(var6 + 176 / j + 2);
      var7 = (byte)(var7 + 220 / k + 2);
      if (var6 > 28) {
         var6 = 28;
      }

      if (var7 > 18) {
         var7 = 18;
      }

      if (++l >= 12) {
         l = 0;
      }

      try {
         for (byte var10 = var4; var10 < var6; var10++) {
            for (byte var11 = var5; var11 < var7; var11++) {
               if (var14 < 176 && var15 < 220) {
                  short var16;
                  if ((var16 = this.f[var10][var11]) <= 34) {
                     int var18 = var15;
                     boolean var19 = false;
                     if (0 < k) {
                        if (l >> 1 > 2) {
                           if (var16 == 15 || var16 == 17) {
                              var16++;
                           } else if (var16 == 16 || var16 == 18) {
                              var16--;
                           }
                        }

                        if (this.c.aQ != 0 && var16 == 26) {
                           if (l < 6) {
                              var16 = 13;
                           } else {
                              var16 = 14;
                           }
                        }

                        this.c.b(var1, var14, var18, j, k - 0);
                        var1.drawImage(h.aF, var14, var18 - var16 * k - 0, 0);
                     }
                  } else {
                     var1.setColor(0);
                     var1.fillRect(var14, var15, j, k);
                  }

                  short var17;
                  if ((var17 = this.a(var10, var11)) > -127 && var17 <= -125) {
                     int var24 = h.aK.getWidth();
                     byte var25 = k;
                     int var29 = var15;
                     int var30 = var17 - -126;
                     if (l >> 1 > 2) {
                        var30 += 2;
                     }

                     if (0 < var25) {
                        this.c.b(var1, var14 + (j - var24 >> 1), var29, var24, var25 - 0);
                        var1.drawImage(h.aK, var14 + (j - var24 >> 1), var29 - 0 - var25 * var30, 0);
                     }
                  } else if (var17 == 97 || var17 == 98) {
                     int var28 = var15 + (k - 19 >> 1);
                     this.c.b(var1, var14 + (j - 19 >> 1), var28, 19, 19);
                     var1.drawImage(h.aL, var14 + (j - 19 >> 1), var28 - 0 - 209, 0);
                  } else if (var17 == 35 && this.c.f(this.c.Z, this.c.X)) {
                     int var27 = var15;
                     this.c.b(var1, var14 + (j - 19 >> 1), var27, 19, 19);
                     var1.drawImage(h.aL, var14 + (j - 19 >> 1), var27 - 0 - 76, 0);
                  } else if (var17 >= 77 && var17 <= 96) {
                     int var12 = h.aJ.getWidth();
                     int var13 = h.aJ.getHeight() >> 3;
                     int var26 = var15 + k - var13;
                     int var20 = this.c.h(var17 - 77) ? 3 * var13 : 4 * var13;
                     if (var17 >= 81 && var17 <= 87 && var17 != 85) {
                        var20 = this.c.h(var17 - 77) ? 7 * var13 : 8 * var13;
                     }

                     if (var17 == 80) {
                        var20 += 2 * var13;
                     }

                     if (0 < var13) {
                        this.c.b(var1, var14 + (j - var12 >> 1), var26, var12, var13 - 0);
                        var1.drawImage(h.aJ, var14 + (j - var12 >> 1), var26 - 0 - var20, 0);
                     }
                  }

                  var15 += k;
               } else {
                  var15 += k;
               }
            }

            var15 = var9;
            var14 += j;
         }
      } catch (Exception var21) {
      }
   }

   public final boolean b(int var1, int var2) {
      if (var1 >= 0 && var1 < 28 && var2 >= 0 && var2 < 18) {
         byte var3 = this.f[var1][var2];
         short var4 = this.a(var1, var2);
         short var5 = this.a(var1, var2 + 1);
         int var6 = var4 - 70;
         int var7 = var5 - 70;
         var6 = 1 << var6;
         var7 = 1 << var7;
         if ((var4 < 70 || var4 > 76 || (this.c.bw & var6) == 0) && (var5 < 70 || var5 > 76 || (this.c.bw & var7) == 0)) {
            return (var4 == -119 || var5 == -119) && (this.c.bw & 128) != 0 ? false : var3 >= 0 && var3 <= 19;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
