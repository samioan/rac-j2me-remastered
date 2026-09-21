import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import com.nokia.mid.ui.FullCanvas;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class f extends FullCanvas implements Runnable {
   public static ratchetandclank a;
   private Display cg;
   public byte b;
   public boolean c = false;
   public boolean d = false;
   public boolean e;
   public static int f = 35;
   public static int g = 100;
   public static byte h = 22;
   public static boolean i = false;
   public static boolean j = false;
   public static int k;
   public static int l;
   public static int m;
   public static Vector n;
   public static Vector o;
   public static Vector p;
   public static Vector q;
   public static int r = 0;
   public static int s = 0;
   public static boolean t = false;
   public static boolean u = false;
   public static boolean v = false;
   public static byte w;
   public static boolean x = false;
   public static int y = 0;
   public byte z;
   public static final String[] A = new String[]{"English", "Français", "Deutsch", "Italiano", "Español"};
   public static final String[] B = new String[]{"", "_fr", "_ge", "_it", "_sp"};
   public static byte C = 0;
   public byte D = 114;
   public StringBuffer E = new StringBuffer(14);
   private long ch = 0L;
   private int ci = -1;
   private int cj = 0;
   private static char[][] ck = new char[][]{
      {'0'},
      {'_', '1'},
      {'a', 'b', 'c', '2', 'A', 'B', 'C'},
      {'d', 'e', 'f', '3', 'D', 'E', 'F'},
      {'g', 'h', 'i', '4', 'G', 'H', 'I'},
      {'j', 'k', 'l', '5', 'J', 'K', 'L'},
      {'m', 'n', 'o', '6', 'M', 'N', 'O'},
      {'p', 'q', 'r', 's', '7', 'P', 'Q', 'R', 'S'},
      {'t', 'u', 'v', '8', 'T', 'U', 'V'},
      {'w', 'x', 'y', 'z', '9', 'W', 'X', 'Y', 'Z'}
   };
   private String cl;
   public int[] F = new int[]{652482873, 766492548};
   public int[][] G = new int[2][8];
   private Image cm;
   public static Vector H = new Vector(1);
   private static byte[] cn;
   private static byte[] co;
   private static byte[] cp;
   private static byte[] cq;
   public static int I = 0;
   public static byte J = 0;
   public static byte K = 0;
   public static int L;
   public static int M = 0;
   public c N;
   public Random O;
   public short P = -1;
   public byte Q = 1;
   public byte R;
   public byte S;
   public short T;
   public short U;
   public short V = 0;
   public d[] W;
   public a X;
   public g[] Y;
   public g[] Z;
   public static Image aa;
   public static Image ab;
   public long ac;
   public byte ad;
   public Image ae;
   public boolean af;
   public static Image ag;
   public static Image ah;
   public static Image ai;
   public static Image aj;
   public static Image ak;
   public static Image al;
   public static Image am;
   public byte an;
   public byte ao;
   public int ap;
   public byte aq;
   public byte ar;
   public int as;
   public int[] at;
   public static byte au = 5;
   public static byte av = au;
   public short[] aw;
   public short[] ax;
   public short[] ay;
   public short[] az;
   public short[] aA;
   public int[] aB;
   public int[] aC;
   public short[] aD;
   public short[] aE;
   public byte[] aF;
   public byte[] aG;
   public byte[] aH;
   public int[] aI;
   public byte aJ = 0;
   public byte aK = 0;
   public byte aL = 0;
   public static final byte[] aM = new byte[]{14, 14, 13, 13};
   public static final byte[] aN = new byte[]{8, 9, 8, 9};
   public byte aO;
   public byte aP;
   public static byte aQ = 9;
   public static byte aR = 60;
   public int aS;
   public int aT;
   public int[] aU;
   public int[] aV;
   public short[] aW;
   public short[] aX;
   public byte[] aY;
   public short[] aZ;
   public short[] ba;
   public short[] bb;
   public short[] bc;
   public byte[] bd;
   public int be;
   public long bf;
   public byte bg;
   private byte cr;
   public int bh;
   public int bi = 0;
   public int bj = 0;
   public static final byte[] bk = new byte[]{
      1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1
   };
   public static final short[] bl = new short[]{
      75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
   };
   public boolean bm = false;
   public String bn;
   public int bo;
   public int bp;
   public int bq = -1;
   public int br = -1;
   public int bs;
   public byte bt = 0;
   public int bu;
   public int bv;
   public int bw;
   public int bx;
   public boolean by;
   public boolean bz;
   public static int bA;
   public static int bB;
   public static int bC;
   public static int bD;
   public static int bE;
   public static int bF;
   public static int bG;
   public static int bH;
   public static int bI;
   public static int bJ;
   public static long bK;
   public static long bL;
   public static long bM;
   public static long bN;
   public static int bO;
   public static int bP;
   public static int bQ;
   public static int bR;
   public static int bS;
   public static int bT;
   public static boolean bU;
   public static boolean bV;
   public static boolean bW;
   public static boolean bX;
   public static int[] bY;
   public static boolean[] bZ;
   private Vector cs;
   private Vector ct;
   private int cu;
   private int cv;
   private int cw;
   public int ca;
   private boolean cx;
   private long cy;
   public static int cb = 0;
   public long cc;
   public boolean cd;
   private boolean cz;
   private boolean cA;
   public final short[][] ce;
   public boolean cf;

   private void k() {
      this.cf = true;
      bD = 10000;
      bB = 0;
      bA = 0;
      bC = 0;
      bI = 0;
      bE = 0;
      bF = 0;
      bJ = 0;
      bL = 0L;
      bM = 0L;
      bN = 180000L;
      bO = 0;
      bR = 0;
      bS = 0;
      bT = 0;
      bU = false;
      bV = false;
      bX = false;
      bW = false;
      m();
      bK = System.currentTimeMillis();
   }

   private int l() {
      int var1 = 0;

      for (int var2 = this.W.length - 1; var2 >= 0; var2--) {
         if (this.W[var2].R != 4 && this.W[var2].R != -1) {
            var1++;
         }
      }

      return var1;
   }

   private void f(int var1) {
      bY[var1] = this.l();
   }

   private static void m() {
      for (int var0 = bY.length - 1; var0 >= 0; var0--) {
         bZ[var0] = false;
         bY[var0] = 0;
      }
   }

   private static void a(byte var0) {
      int var10000;
      int var10001;
      switch (var0) {
         case 0:
            bL = System.currentTimeMillis();
            return;
         case 1:
            if (!bX) {
               if (bR == 0) {
                  bV = true;
               }

               if (bR == bS + bT) {
                  bU = true;
               }
            }

            if (bI == bC) {
               bW = true;
            }

            for (int var1 = bY.length - 1; var1 >= 0; var1--) {
               if (bY[var1] > 0) {
                  bB = bB + bY[var1];
               }
            }

            bD = bC * 100;
            bO = bO + bD;
            bG = bE * 1;
            bH = bF * 100000;
            bO = bO + bG;
            bO = bO + bH;
            bM = bL - bK;
            if (bM < bN) {
               bJ = (int)((bN - bM) / 1000L * 10L);
            }

            var10000 = bO;
            var10001 = bJ;
            break;
         case 2:
            if (bV) {
               bO += 100000;
            }

            if (bU) {
               bO += 100000;
            }

            if (bW) {
               var10000 = bO;
               var10001 = 10000;
               break;
            }

            return;
         default:
            return;
      }

      bO = var10000 + var10001;
   }

   private int n() {
      int var2 = 0;

      for (int var1 = 0; var1 < 16; var1++) {
         if ((this.bw & 1 << var1) != 0) {
            var2++;
         }
      }

      return var2;
   }

   private void o() {
      this.ad = 0;
      this.ae = b("sony");
      this.cm = b("uc");
      this.af = true;
      this.b = 20;
      a("/o");
      J = 0;
      this.bg = 0;
      I = 0;
   }

   private void p() {
      boolean var1 = false;
      this.y();
      this.b = 19;
      this.bg = 0;
      I = 0;
      J = 21;
      a((byte)0);
      a((byte)1);
      a((byte)2);
      bP = bP + bO;
      q = ratchetandclank.c(ratchetandclank.e[101 + (this.Q - 1)], 118);
      q.addElement(new String(""));
      a(ratchetandclank.c(this.i(63, bC), 118));
      a(ratchetandclank.c(this.i(64, bE), 118));
      a(ratchetandclank.c(this.i(65, bR), 118));
      a(ratchetandclank.c(this.i(66, bS), 118));
      a(ratchetandclank.c(this.i(67, bT), 118));
      a(ratchetandclank.c(ratchetandclank.e[68] + " " + o((int)bM), 118));
      a(ratchetandclank.c(this.i(111, bO), 118));
      q.addElement(new String(""));
      a(ratchetandclank.c(this.i(69, this.B()), 118));
      int var2 = 0;

      for (int var3 = 1; var3 < 8; var3++) {
         if ((this.X.M & 1 << var3) > 0) {
            var2++;
         }
      }

      a(ratchetandclank.c(this.i(70, var2), 118));
      l = q.size();
      k = 0;
   }

   private static void a(Vector var0) {
      while (var0.size() > 0) {
         Object var1 = var0.firstElement();
         q.addElement(var1);
         var0.removeElement(var1);
      }
   }

   private void q() {
      if (0 == this.b) {
         this.y();
         this.b = 19;
         this.bg = 0;
         I = 0;
         J = 9;
      }
   }

   private void r() {
      this.y();
      this.b = 19;
      this.bg = 0;
      I = 0;
      J = 8;
   }

   private void s() {
      this.y();
      a((byte)0);
      a((byte)1);
      a((byte)2);
      q = new Vector();
      Vector var10000;
      String[] var10001;
      byte var10002;
      if (a.a(ratchetandclank.e[97]) < 118) {
         var10000 = q;
         var10001 = ratchetandclank.e;
         var10002 = 97;
      } else {
         q.addElement(ratchetandclank.e[98]);
         var10000 = q;
         var10001 = ratchetandclank.e;
         var10002 = 99;
      }

      var10000.addElement(var10001[var10002]);
      q.addElement(new String(""));
      a(ratchetandclank.c(this.i(63, bQ), 118));
      a(ratchetandclank.c(this.i(64, this.as), 118));
      a(ratchetandclank.c(ratchetandclank.e[68] + " " + o(this.be), 118));
      a(ratchetandclank.c(this.i(111, bP), 118));
      a(ratchetandclank.c(this.i(69, this.B()), 118));
      l = q.size();
      k = 0;
      this.b = 19;
      this.bg = 0;
      I = 0;
      J = 20;
      this.ca = 10;
   }

   private static void a(String var0) {
      byte var1 = 0;
      byte var2 = 0;

      try {
         InputStream var7;
         cn = new byte[var1 = (byte)(var7 = var0.getClass().getResourceAsStream(var0)).read()];
         co = new byte[var1];
         cp = new byte[var1];
         cq = new byte[var1];

         for (int var8 = 0; var8 < var1; var8++) {
            var2 = (byte)var7.read();
            if (var8 == 2) {
               var2++;
            }

            cp[var8] = (byte)var7.read();
            cn[var8] = (byte)var7.read();
            co[var8] = (byte)var7.read();
            cq[var8] = (byte)var7.read();
            e[] var6 = new e[var2];

            for (int var9 = 0; var9 < var2; var9++) {
               byte var3;
               byte var4;
               byte var10000;
               if (var8 == 2 && var9 == var2 - 1) {
                  var3 = 0;
                  var4 = -112;
                  var10000 = -104;
               } else {
                  var3 = (byte)var7.read();
                  var4 = (byte)var7.read();
                  var10000 = (byte)var7.read();
               }

               byte var5 = var10000;
               var6[var9] = new e(var3, var5, var4);
            }

            H.addElement(var6);
         }

         var7.close();
      } catch (IOException var10) {
      }
   }

   private void a(Graphics var1) {
      if (this.ae != null) {
         Graphics var10000;
         int var10001;
         if (this.ad == 1) {
            var10000 = var1;
            var10001 = 16777215;
         } else {
            var10000 = var1;
            var10001 = 0;
         }

         var10000.setColor(var10001);
         var1.setClip(0, 0, 128, 160);
         var1.fillRect(0, 0, 128, 160);
         var1.drawImage(this.ae, 64, 80, 3);
         this.ae = null;
         System.gc();
      }
   }

   private static void a(Vector var0, Vector var1) {
      while (var1.size() > 0) {
         Object var2 = var1.firstElement();
         var0.addElement(var2);
         var1.removeElement(var2);
      }
   }

   private void b(Graphics var1) {
      if (this.D != 114) {
         Graphics var35;
         short var38;
         byte var41;
         if (this.D == 116) {
            var1.setClip(0, 0, 128, 160);
            var1.setColor(1052688);
            var1.fillRect(0, 0, 128, 160);
            this.a(var1, ratchetandclank.e[133], 5);
            boolean var14 = false;
            a(var1, this.cs, false);
            var35 = var1;
            var38 = -1;
            var41 = 8;
         } else if (this.D == 117) {
            var1.setClip(0, 0, 128, 160);
            var1.setColor(0);
            var1.fillRect(0, 0, 128, 160);
            int var15 = this.a(var1, ratchetandclank.e[133], 5);
            a(var1, this.ct, true);
            if (this.ct != null) {
               if (this.cu - k >= 0 && this.cu - k <= m - 1) {
                  this.b(var1, 1, (this.cu - k + 1) * 10 + var15);
               }

               if (this.cv - k >= 0 && this.cv - k <= m - 1) {
                  this.b(var1, 0, (this.cv - k + 1) * 10 + var15);
               }
            }

            var35 = var1;
            var38 = 140;
            var41 = 8;
         } else if (this.D == 115) {
            var1.setClip(0, 0, 128, 160);
            var1.setColor(0);
            var1.fillRect(0, 0, 128, 160);
            f var36;
            Graphics var39;
            String var42;
            if (this.cl == null) {
               var36 = this;
               var39 = var1;
               var42 = ratchetandclank.e[139];
            } else {
               var36 = this;
               var39 = var1;
               var42 = this.cl;
            }

            int var16 = var36.a(var39, var42, 0) + 5;
            String var18 = this.E.toString();
            if (System.currentTimeMillis() - this.ch > 1500L && this.E.length() < 14) {
               var18 = var18 + "|";
            }

            this.a(var1, var18, var16);
            var35 = var1;
            var38 = 140;
            var41 = 8;
         } else {
            if (this.D != 118 && this.D != 120) {
               return;
            }

            var1.setClip(0, 0, 128, 160);
            var1.setColor(0);
            var1.fillRect(0, 0, 128, 160);
            int var17 = 160 / a.o - 1;
            int var19 = 3;
            if (var17 < l + 1) {
               int var28 = 0;
               if (k > var17 / 2) {
                  var28 = k - var17 / 2;
               }

               if (k > l - (var17 - var17 / 2)) {
                  var28 = l - var17 + 1;
               }

               for (int var30 = 0; var30 < Math.min(var17, l); var30++) {
                  var19 = this.a(var1, A[var28 + var30], var19, var28 + var30 == k);
               }
            } else {
               var19 = 3 + (var17 - l - 1) * a.o / 2;

               for (int var27 = 0; var27 < l + 1; var27++) {
                  var19 = this.a(var1, A[var27], var19, var27 == k);
               }
            }

            if (this.D == 120) {
               var35 = var1;
               var38 = 7;
               var41 = 8;
            } else {
               var35 = var1;
               var38 = 7;
               var41 = -1;
            }
         }

         c(var35, var38, var41);
      } else {
         boolean var6 = false;
         Object var7 = null;
         int var8 = 0;
         var8 = ((Object[])(var7 = (e[])H.elementAt(J))).length;
         if (J == 8) {
            var8 = this.n();
         }

         var1.setClip(0, 0, 128, 160);
         var1.setColor(0);
         var1.fillRect(0, 0, 128, 160);
         int var22;
         if (J == 18 && (this.X.M & 1 << this.cr + 1) != 0) {
            var22 = this.a(var1, ratchetandclank.e[48], 5);
            var6 = true;
         } else if (J == 0) {
            var22 = this.a(var1, ratchetandclank.e[cp[J]], 5);
            var22 = this.a(var1, ratchetandclank.e[95], var22);
            var22 += 5;
         } else {
            var22 = this.a(var1, ratchetandclank.e[cp[J]], 5);
         }

         if (J == 17 && (this.X.M & 128) == 0) {
            var8--;
         }

         if (J == 18) {
            var22 += 2;
            var22 = this.a(var1, ratchetandclank.e[49 + this.cr], var22, false);
            var22 = this.a(var1, 56, this.as, var22, false);
            f var10000;
            Graphics var10001;
            byte var10002;
            int var10003;
            if (var6) {
               var10000 = this;
               var10001 = var1;
               var10002 = 57;
               var10003 = this.at[this.cr + 1];
            } else {
               var10000 = this;
               var10001 = var1;
               var10002 = 57;
               var10003 = a.h[this.cr + 1];
            }

            var22 = var10000.a(var10001, var10002, var10003, var22, false);
         }

         L = (148 - var22 - 14) / 12;
         if (L < 2) {
            L = 2;
         }

         int var9 = var22;
         var22 = (148 - var9 - 12 * (var8 > L ? L : var8) >> 1) + var9;
         c(var1, cn[J], co[J]);
         if (var8 != 0) {
            int var10 = I;
            M = 0;

            for (int var2 = 0; var2 < L && var10 < ((Object[])var7).length; var10++) {
               byte var5 = ((e)((Object[])var7)[var10]).a;
               byte var3 = ((e)((Object[])var7)[var10]).c;
               int var11 = var22;
               boolean var12 = true;
               if (var5 != 0 && (var5 != 10 || (this.X.M & 128) == 0)) {
                  if (var5 >= 1 && var5 <= 3) {
                     var22 = this.a(var1, --var5, var22, this.bg == var2 + I);
                  } else {
                     label224:
                     if (var5 == 4) {
                        Graphics var34;
                        Vector var37;
                        boolean var40;
                        label222: {
                           switch (((e)((Object[])var7)[0]).c) {
                              case 96:
                                 var34 = var1;
                                 var37 = o;
                                 var40 = false;
                                 break label222;
                              case 97:
                                 var34 = var1;
                                 var37 = n;
                                 break;
                              case 98:
                                 var34 = var1;
                                 var37 = p;
                                 break;
                              case 99:
                                 var34 = var1;
                                 var37 = q;
                                 break;
                              default:
                                 break label224;
                           }

                           var40 = true;
                        }

                        a(var34, var37, var40);
                     } else if (var5 == 5) {
                        var22 = this.a(var1, ratchetandclank.e[var3 + (a.g ? 0 : 1)], var22, this.bg == var2 + I);
                     } else if (var5 == 6 && (this.bw & 1 << var10) != 0) {
                        var22 = this.a(var1, ratchetandclank.e[var3], var22, this.bg == var2 + I);
                     } else if (var5 == 8) {
                        a(var1, q, true);
                     } else if (var5 != 9) {
                        var12 = false;
                     }
                  }
               } else {
                  var22 = this.a(var1, ratchetandclank.e[var3 & 0xFF], var22, this.bg == var2 + I);
               }

               int var13;
               if ((var13 = (var22 - var11) / 10) > 1) {
                  M = var13 - 1;
               }

               if (var12) {
                  var22 += 2;
                  var2++;
               }
            }

            if (++K > 6) {
               DirectGraphics var33 = DirectUtils.getDirectGraphics(var1);
               if (I > 0) {
                  var33.fillTriangle(117, var9 + 4 + 7, 122, var9 + 4, 127, var9 + 4 + 7, -265783);
                  var33.fillTriangle(1, var9 + 4 + 7, 6, var9 + 4, 11, var9 + 4 + 7, -265783);
               }

               if (I + (L - M) < var8) {
                  var33.fillTriangle(117, 140, 122, 147, 127, 140, -265783);
                  var33.fillTriangle(1, 140, 6, 147, 11, 140, -265783);
               }

               if (K > 24) {
                  K = 0;
               }
            }
         }
      }
   }

   private int b(Graphics var1, int var2, int var3) {
      int var4 = this.cm.getHeight();
      int var5 = 128 - (8 * var4 + 14) - 8 >> 1;

      for (int var6 = 0; var6 < 8; var5 += var4 + 2) {
         var1.setClip(var5, var3, var4, var4);
         var1.drawImage(this.cm, var5 - this.G[var2][var6] * var4, var3, 20);
         var6++;
      }

      var1.setClip(0, 0, 128, 160);
      return var3 + 14;
   }

   private void f(int var1, int var2) {
      byte var3 = 0;
      byte var4 = -1;
      boolean var6 = true;
      if (this.ca > 0) {
         this.ca--;
      }

      e[] var7;
      int var5 = (var7 = (e[])H.elementAt(J)).length;
      if (J == 17 && (this.X.M & 128) == 0) {
         var5--;
      }

      if (var5 != 0) {
         var3 = var7[0].a;
         var6 = var7[this.bg].d;
         var4 = var7[this.bg].b;
      }

      if (J == 8) {
         var5 = this.n();
      }

      int var10000;
      label399: {
         if ((var2 == -5 || var1 == -6) && var6) {
            if (var4 != -1) {
               if (this.D != 114) {
                  if (this.D == 117) {
                     this.D = 114;
                     this.cs = this.ct = null;
                     this.cr = this.bg = 0;
                     I = 0;
                     return;
                  }

                  f var20;
                  byte var21;
                  if (this.D == 115) {
                     if (this.E.length() < 4) {
                        this.cl = ratchetandclank.e[137];
                        return;
                     }

                     this.cl = null;
                     var20 = this;
                     var21 = 117;
                  } else {
                     if (this.D != 118 && this.D != 120) {
                        return;
                     }

                     C = (byte)k;
                     var20 = this;
                     var21 = 119;
                  }

                  var20.D = var21;
                  return;
               }

               if (var4 == 0) {
                  this.bm = false;
               } else {
                  if (var4 == -105) {
                     f var19;
                     byte var10001;
                     if (a.d(1) != 0) {
                        var19 = this;
                        var10001 = 115;
                     } else {
                        var19 = this;
                        var10001 = 116;
                     }

                     var19.D = var10001;
                     return;
                  }

                  if (var4 == -104) {
                     l = A.length - 1;
                     m = 0;
                     k = 0;
                     this.D = 120;
                     return;
                  }

                  if (var4 == 127) {
                     q = null;
                     p = null;
                     o = null;
                     n = null;
                     System.gc();
                     a(20);
                     this.h(1);
                     this.e = true;
                     this.b = 0;
                     this.w();
                     return;
                  }

                  if (var4 == 126) {
                     a.a();
                     return;
                  }

                  if (var4 == 125) {
                     if (a.c[this.bg] == 0) {
                        return;
                     }

                     q = null;
                     p = null;
                     o = null;
                     n = null;
                     System.gc();
                     a(20);
                     this.A();
                     a.b(this.bg);
                     this.b = 19;
                     var4 = 8;
                  } else if (var4 == 124) {
                     if ((this.X.M & 1 << this.cr + 1) == 0) {
                        var4 = 19;
                        if (this.as >= a.h[this.cr + 1]) {
                           this.as = this.as - a.h[this.cr + 1];
                           this.X.M = (byte)(this.X.M | 1 << this.cr + 1);
                           this.X.K[this.cr + 1] = a.e[this.cr + 1];
                           var4 = 17;
                        }
                     } else {
                        var4 = 19;
                        if (this.as >= this.at[this.cr + 1]) {
                           this.as = this.as - this.at[this.cr + 1];
                           this.X.K[this.cr + 1] = a.d[(this.cr + 1) * 3 + this.X.N[this.cr + 1]];
                           var4 = 17;
                        }
                     }
                  } else if (var4 == 123) {
                     a.c(this.cr);
                     var4 = cq[J];
                  } else {
                     if (var4 == 122) {
                        x();
                        this.e = true;
                        this.b = 0;
                        this.w();
                        return;
                     }

                     if (var4 == 121) {
                        if (a.c[this.bg] == 0) {
                           a.a(this.bg);
                           a.b();
                           var4 = 12;
                        } else {
                           var4 = 11;
                        }
                     } else if (var4 == 120) {
                        a.a(this.cr);
                        a.b();
                        var4 = 12;
                     } else if (var4 == 119) {
                        int var8 = 0;

                        for (int var9 = this.bg; var9 >= 0; var8++) {
                           if ((this.bw & 1 << var8) != 0) {
                              var9--;
                           }
                        }

                        if (var8 != 12) {
                           if (var8 == 11) {
                              var8++;
                           }

                           this.cw = 5;
                           this.i(var8);
                           this.e = true;
                           this.b = 0;
                           this.w();
                           return;
                        }

                        var4 = var7[var8 - 1].b;
                     }
                  }
               }

               if (var4 != -1 && var7[this.bg].d) {
                  if (var4 == 4 && J == 3 && var3 == 1 && a.c[this.bg] == 0) {
                     return;
                  }

                  J = var4;
                  this.cr = this.bg;
                  this.bg = 0;
                  I = 0;
                  if ((var7 = (e[])H.elementAt(J)).length != 0) {
                     var3 = var7[0].a;
                     if (var7[0].a == 0 && J == 0) {
                        if (!this.c) {
                           this.c = true;
                           if (a.g && a.f != null) {
                              a.f.a(6, -1);
                           }
                        }
                     } else if (var3 >= 1 && var3 <= 3) {
                        a.b();
                     } else {
                        label259:
                        if (var3 == 4) {
                           Vector var18;
                           switch (var7[0].c) {
                              case 96:
                                 if (o == null) {
                                    o = ratchetandclank.b("/help", 118);
                                 }

                                 var18 = o;
                                 break;
                              case 97:
                                 if (n == null) {
                                    n = ratchetandclank.b("/about", 118);

                                    for (int var16 = 0; var16 < n.size(); var16++) {
                                       int var10;
                                       String var17;
                                       if ((var10 = (var17 = (String)n.elementAt(var16)).indexOf("??")) > 0) {
                                          String var11;
                                          if ((var11 = a.getAppProperty("MIDlet-Version")) != null) {
                                             n.setElementAt(var17.substring(0, var10) + var11, var16);
                                          } else {
                                             n.setElementAt(var17.substring(0, var10) + "?.?.?", var16);
                                          }
                                          break;
                                       }
                                    }
                                 }

                                 var18 = n;
                                 break;
                              case 98:
                                 if (p == null) {
                                    p = ratchetandclank.b("/credits", 118);
                                 }

                                 var18 = p;
                                 break;
                              default:
                                 break label259;
                           }

                           l = var18.size();
                           k = 0;
                        }
                     }

                     if (J == 17) {
                        this.O();
                        return;
                     }

                     return;
                  }
               }

               return;
            }

            if (var3 == 5 && (J == 2 || J == 13)) {
               a.g = !a.g;
               cb = 5;
               return;
            }

            if (var2 == -5 && var3 == 8 && J == 21) {
               J = 8;
               this.cr = this.bg = 0;
            } else if (var2 == -5 && J == 12) {
               J = 10;
               this.cr = this.bg = 0;
            } else {
               if (var2 != -5 || J != 20 || this.ca != 0) {
                  return;
               }

               q = null;
               J = 0;
               if (!this.c) {
                  this.c = true;
                  if (a.g && a.f != null) {
                     a.f.a(6, -1);
                  }
               }

               this.cr = this.bg = 0;
            }
         } else {
            label419: {
               if (var1 != -7 && var1 != 0) {
                  if (var2 == -2) {
                     if (var3 != 4 && var3 != 8 && var3 != 9 && this.D == 114) {
                        if (this.bg >= var5 - 1) {
                           return;
                        }

                        this.bg++;
                        var10000 = this.bg >= I + (L - M) ? I + 1 : 0;
                        break label399;
                     }

                     if (k + m < l) {
                        k++;
                        return;
                     }

                     return;
                  }

                  if (var2 != -1) {
                     return;
                  }

                  if (var3 != 4 && var3 != 8 && var3 != 9 && this.D == 114) {
                     if (this.bg <= 0) {
                        return;
                     }

                     this.bg--;
                     if (I > 0) {
                        var10000 = I - 1;
                        break label399;
                     }
                     break label419;
                  }

                  if (k > 0) {
                     k--;
                     return;
                  }

                  return;
               }

               if (this.D != 114) {
                  if (this.D == 116) {
                     this.D = 114;
                     this.cs = this.ct = null;
                     this.cr = this.bg = 0;
                     I = 0;
                     H.elementAt(J);
                     return;
                  }

                  if (this.D == 115) {
                     this.cr = this.bg = 0;
                     I = 0;
                     this.cl = null;
                     this.D = 114;
                     return;
                  }

                  if (this.D == 117) {
                     this.ct = null;
                     this.D = 115;
                     return;
                  }

                  if (this.D == 120) {
                     this.D = 114;
                     this.cr = this.bg = 0;
                     I = 0;
                     H.elementAt(J);
                     m = (148 - (5 + a.o + 4)) / 10;
                  }

                  return;
               }

               if (cq[J] == 122) {
                  x();
                  this.e = true;
                  this.b = 0;
                  this.w();
                  return;
               }

               if (cq[J] != -1) {
                  if (J == 19) {
                     J = 17;
                  } else {
                     J = cq[J];
                     if (J == 0 && !this.c) {
                        this.c = true;
                        if (a.g && a.f != null) {
                           a.f.a(6, -1);
                        }
                     }
                  }

                  this.cr = this.bg = 0;
                  I = 0;
                  if ((var7 = (e[])H.elementAt(J)).length != 0) {
                     var3 = var7[0].a;
                     if (var7[0].a >= 1 && var3 <= 3) {
                        a.b();
                        return;
                     }

                     return;
                  }
               }

               return;
            }
         }

         var10000 = 0;
      }

      I = var10000;
   }

   public f(ratchetandclank var1) {
      short[] var10000 = new byte[]{1, 1, 2, 2, 3, 3, 3, 4, 4, 5, 6, 6, 7, 8, 9, 9, 10, 12, 11, 13};
      var10000 = new short[]{0, 2, 0, 1, 0, 2, 4, 0, 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0};
      this.by = false;
      this.bz = false;
      this.cw = 2;
      this.ca = 0;
      this.cx = false;
      this.cy = 0L;
      this.cd = true;
      this.cz = false;
      this.ce = new short[][]{
         {0, 112, 1, 113, 0, 114},
         {1, 115, 0, 116, 1, 117},
         {0, 118, 1, 119},
         {1, 125},
         {0, 126},
         {1, 127},
         new short[0],
         {0, 128},
         new short[0],
         {1, 129, -1, 84},
         {1, 130, 0, 131, 1, 132},
         {2, 120, 1, 121, 2, 122, 0, 123, 2, 124, -1, 77}
      };
      a = var1;
      this.cg = Display.getDisplay(a);
      this.ad = 0;
      this.af = true;
      this.b = 20;
      this.ae = b("sony");
   }

   private void t() {
      this.z();
      String var1;
      if ((var1 = a.getAppProperty("MIDlet-Spec-Code")) != null) {
         j = var1.equals("true");
      }
   }

   public final void keyPressed(int var1) {
      if (var1 != -10) {
         if (!this.cx) {
            if (this.cw <= 0) {
               if (this.D != 115 || (var1 < 48 || var1 > 57) && var1 != 42 && var1 != 0) {
                  int var5;
                  label80: {
                     var5 = 0;
                     int var10000;
                     if (var1 == 50) {
                        var10000 = -1;
                     } else if (var1 == 56) {
                        var10000 = -2;
                     } else if (var1 == 52) {
                        var10000 = -3;
                     } else if (var1 == 54) {
                        var10000 = -4;
                     } else if (var1 == 53) {
                        var10000 = -5;
                     } else {
                        if (var1 != -5 && (var1 == -6 || var1 == -7)) {
                           break label80;
                        }

                        var10000 = var1;
                     }

                     var5 = var10000;
                  }

                  switch (this.b) {
                     case 0:
                        this.h(var1, var5);
                        return;
                     case 19:
                        this.f(var1, var5);
                        return;
                     case 20:
                        this.ac = 0L;
                  }
               } else {
                  long var2 = System.currentTimeMillis();
                  int var4 = var1 - 48;
                  if (var1 == 42 || var1 == 0) {
                     this.ch = var2;
                     this.cj = 0;
                     this.ci = -1;
                     if (this.E.length() > 0) {
                        this.E.deleteCharAt(this.E.length() - 1);
                        return;
                     }
                  } else if (var4 >= 0) {
                     if ((this.ci != var4 || var2 - this.ch > 1500L) && this.E.length() < this.E.capacity()) {
                        this.ch = var2;
                        this.cj = 0;
                        this.ci = var4;
                        this.E.append(ck[var4][0]);
                        return;
                     }

                     if (this.ci == var4 && var2 - this.ch <= 1500L) {
                        this.ch = var2;
                        this.cj = (this.cj + 1) % ck[var4].length;
                        this.ci = var4;
                        this.E.setCharAt(this.E.length() - 1, ck[var4][this.cj]);
                        return;
                     }

                     this.ch = 0L;
                     this.cj = 0;
                     this.ci = -1;
                  }
               }
            }
         }
      }
   }

   public final void keyReleased(int var1) {
      if (var1 != -10) {
         if (!this.cx) {
            if (this.cw <= 0) {
               this.i();
            }
         }
      }
   }

   private static void c(Graphics var0, int var1, int var2) {
      var0.setClip(0, 0, 128, 160);
      if (var1 >= 0) {
         String var3 = ratchetandclank.e[var1];
         int var4 = a.o;
         int var5 = 160 - var4;
         int var6 = a.a(var3);
         var0.setColor(160);
         var0.fillRect(0, var5 - 1, var6 + 2, var4 + 1);
         var0.setColor(16777215);
         a.a(var0, var3, 1, var5, 20);
      }

      if (var2 >= 0) {
         String var7 = ratchetandclank.e[var2];
         int var8 = a.o;
         int var9 = 160 - var8;
         int var10 = a.a(var7);
         var0.setColor(160);
         var0.fillRect(128 - var10 - 2, var9 - 1, var10 + 2, var8 + 1);
         var0.setColor(16777215);
         a.a(var0, var7, 128 - var10 - 1, var9, 20);
      }
   }

   public final void paint(Graphics var1) {
      if (!this.cx) {
         if (this.cw > 0) {
            this.cw--;
         }

         if (this.b == 19) {
            this.b(var1);
         } else if (this.b == 20) {
            if (this.af) {
               this.af = false;
               this.a(var1);
            }
         } else {
            if (this.by) {
               this.X.B = 1;
            }

            this.N.a(var1);
            this.d(var1);
            this.f(var1);
            this.g(var1);
            this.e(var1);
            this.h(var1);
            if (this.Q == 12) {
               this.i(var1);
            }

            if (this.e) {
               this.c(var1);
               this.e = false;
            }

            if (this.X.r == -1) {
               var1.setClip(59, 20, 11, 12);
               var1.drawImage(ag, 59, -64, 0);
            }

            if (this.bm) {
               j(var1);
               this.br = this.a(var1, this.bq);
            }
         }
      }
   }

   private static void a(Graphics var0, Vector var1, boolean var2) {
      if (var1 != null) {
         if (var1.size() != 0) {
            int var3 = 5 + a.o + 4;
            var0.setColor(16777215);
            if (m < l) {
               int var4 = m * 10;
               int var5 = m * var4 / l;
               int var6 = var3 + k * (var4 - var5) / (l - m);
               var0.drawRect(122, var3, 3, var4);
               var0.fillRect(122, var6, 3, var5);
            }

            for (int var7 = k; var7 < l && var7 < k + m; var3 += 10) {
               String var8 = (String)var1.elementAt(var7);
               a.a(var0, var8, !var2 ? 2 : 118 - a.a(var8) >> 1, var3, 20);
               var7++;
            }
         }
      }
   }

   private void u() {
      for (int var3 = 3; var3 >= 0; var3--) {
         if (this.bd[var3] != -1) {
            short[] var5;
            int var10001;
            int var10002;
            label35: {
               switch (this.bd[var3]) {
                  case 0:
                     this.bb[var3]++;
                     if (this.bb[var3] <= 44) {
                        continue;
                     }

                     this.bb[var3]--;
                     var5 = this.bd;
                     var10001 = var3;
                     var10002 = this.bd[var3] + 2;
                     break label35;
                  case 1:
                     this.bc[var3]++;
                     if (this.bc[var3] <= 28) {
                        continue;
                     }

                     this.bc[var3]--;
                     var5 = this.bd;
                     var10001 = var3;
                     var10002 = this.bd[var3] + 2;
                     break label35;
                  case 2:
                     this.bb[var3]--;
                     if (this.bb[var3] >= 0) {
                        continue;
                     }

                     var5 = this.bb;
                     break;
                  case 3:
                     this.bc[var3]--;
                     if (this.bc[var3] < 0) {
                        var5 = this.bc;
                        break;
                     }
                  default:
                     continue;
               }

               var5[var3]++;
               var5 = this.bd;
               var10001 = var3;
               var10002 = this.bd[var3] - 2;
            }

            var5[var10001] = (byte)var10002;
         }
      }
   }

   public static void a(int var0) {
      try {
         Thread.sleep(var0);
      } catch (Exception var2) {
      }
   }

   public final void showNotify() {
      this.b();
   }

   public final void hideNotify() {
      this.a();
   }

   public final void a() {
      if (!this.cx && System.currentTimeMillis() - this.cy > 200L) {
         this.cx = true;
         this.q();
         if (a.f != null) {
            a.f.a();
         }

         if (this.X != null) {
            this.X.U = 0;
            this.X.p = -1;
            if (this.X.r == 4) {
               this.X.r = 3;
            }
         }

         this.bh = 0;
         this.bi = 0;
      }
   }

   public final void b() {
      if (this.cx) {
         this.cx = false;
         if (this.b == 20) {
            this.af = true;
         } else if (this.b == 19 && this.D != 118) {
            cb = 4;
         }

         this.cy = System.currentTimeMillis();
      }
   }

   public final void c() {
      this.cg.setCurrent(this);
      new Thread(this).start();
      this.ac = System.currentTimeMillis();
   }

   public final void run() {
      label16: {
         this.cc = System.currentTimeMillis();
         if (!this.cx) {
            if (this.cc - this.bf <= 100L) {
               break label16;
            }

            this.v();
            if (this.cd) {
               this.repaint();
            }
         }

         this.bf = this.cc;
      }

      this.cg.callSerially(this);
      Thread.yield();
   }

   private void v() {
      this.cd = true;
      if (cb > 0 && --cb == 0) {
         if (this.c && a.f != null) {
            if (a.g) {
               a.f.a(6, -1);
            } else {
               a.f.a();
            }
         }

         a.a((byte)(a.g ? 1 : 0), 0);
      }

      this.M();
      if (this.b == 0 && !this.bm) {
         this.be = (int)(this.be + (this.cc - this.bf));
         this.F();
         this.u();
         this.X.j();
         this.X.k();
         if (this.X.B <= 0) {
            this.K();
         } else {
            this.X.B--;
         }

         this.w();
         this.G();

         for (int var1 = 9; var1 >= 0; var1--) {
            this.Y[var1].b(false);
            if (this.Y[var1].g != -1) {
               this.e(var1);
            }
         }

         for (int var4 = 9; var4 >= 0; var4--) {
            this.Z[var4].b(true);
            if (this.Z[var4].g != -1) {
               this.k(var4);
            }
         }

         for (int var5 = au - 1; var5 >= 0; var5--) {
            if (this.W[var5].R != -1) {
               if (this.Q == 12 && (this.W[var5].R == 1 || this.W[var5].R == 4)) {
                  if (this.W[var5].B > 80) {
                     this.W[var5].R = -1;
                     continue;
                  }

                  this.W[var5].B++;
               }

               if (this.W[var5].ad != 5 && this.X.ad != 10) {
                  this.n(var5);
               }

               this.W[var5].d();
               this.W[var5].e();
            }
         }

         if (this.Q == 12) {
            this.I();
         }
      }

      if (this.D != 114) {
         if (this.D == 115) {
            return;
         }

         if (this.D == 117) {
            this.a(this.E.toString(), 0);
            this.a(this.E.toString(), 1);
            if (this.ct == null) {
               String var2 = new String("");
               String var3 = (this.E.length() > 7
                     ? new StringBuffer().append(this.E.toString().substring(0, 7)).append('^').append(this.E.toString().substring(7, this.E.length()))
                     : this.E)
                  .toString();
               this.ct = new Vector();
               a(this.ct, ratchetandclank.c(ratchetandclank.e[141], 94));
               a(this.ct, ratchetandclank.c(var3, 94));
               this.ct.addElement(var2);
               a(this.ct, ratchetandclank.c(ratchetandclank.e[142], 94));
               this.cu = this.ct.size();
               this.ct.addElement(var2);
               this.ct.addElement(var2);
               a(this.ct, ratchetandclank.c(ratchetandclank.e[143], 94));
               this.cv = this.ct.size();
               this.ct.addElement(var2);
               this.ct.addElement(var2);
               a(this.ct, ratchetandclank.c(ratchetandclank.e[135], 94));
               l = this.ct.size();
               k = 0;
               return;
            }
         } else if (this.D == 116) {
            if (this.cs == null) {
               this.cs = new Vector();
               a(this.cs, ratchetandclank.c(ratchetandclank.e[134], 120));
               this.cs.addElement("");
               a(this.cs, ratchetandclank.c(ratchetandclank.e[135], 120));
               l = this.cs.size();
               k = 0;
               return;
            }
         } else if (this.D == 119) {
            ratchetandclank.e = null;
            q = null;
            p = null;
            o = null;
            n = null;
            ratchetandclank.e = ratchetandclank.a("/m" + B[C], 145);
            a.a(C, 2);
            this.D = 114;
            m = (148 - (5 + a.o + 4)) / 10;
            if (this.cz && a.g && a.f != null) {
               this.cz = false;
               a.f.a(6, -1);
            }
         }
      }
   }

   private void a(String var1, int var2) {
      String var3 = var1.toUpperCase();
      int var5 = 0;
      int var7 = var3.length();

      for (int var8 = 0; var8 < var7; var8++) {
         char var6 = var3.charAt(var8);
         var5 += var6 * (var8 + 1);
      }

      int var4 = var5 * this.F[var2];

      for (int var12 = 0; var12 < 8; var12++) {
         int var9 = var12 * 4;
         int var11 = 15 << var9;
         int var10 = (var4 & var11) >> var9;
         this.G[var2][var12] = g(var10);
      }
   }

   private static int g(int var0) {
      if (var0 >= 0 && var0 <= 3) {
         return 0;
      } else if (var0 >= 4 && var0 <= 7) {
         return 1;
      } else {
         return var0 >= 8 && var0 <= 11 ? 2 : 3;
      }
   }

   private void w() {
      int var1 = 0;
      boolean var2 = false;
      short var4 = this.X.b();
      short var5 = this.X.c();
      if (!t) {
         if (this.X.ag) {
            var1 = -(var4 - 22);
         } else {
            var1 = -(var4 + 22 - 128);
         }
      } else {
         var1 = -(y - 64);
         if (var4 > y + 11 || var4 < y - 11 || this.X.c() == this.X.a(false)) {
            t = false;
         }
      }

      label46: {
         int var3 = -(u ? var5 + 22 - 160 + 15 : var5 + 22 + 28 - 160 - 15) - s;
         s += var3 >> 1;
         if (r > var1) {
            r -= 10;
            if (r >= var1) {
               break label46;
            }
         } else {
            if (r >= var1) {
               break label46;
            }

            r += 10;
            if (r <= var1) {
               break label46;
            }
         }

         r = var1;
      }

      if (r > 0) {
         r = 0;
      }

      if (r < -488) {
         r = -488;
      }

      if (s > 0) {
         s = 0;
      }

      if (s < -92) {
         s = -92;
      }
   }

   private static Image b(String var0) {
      try {
         return Image.createImage("/" + var0 + ".png");
      } catch (IOException var2) {
         return null;
      }
   }

   private static void x() {
      H.removeAllElements();
      if (aa == null) {
         aa = b("a");
      }

      if (am == null) {
         am = b("d");
      }

      if (ab == null) {
         ab = b("b");
      }

      if (aj == null) {
         aj = b("c");
      }

      if (al == null) {
         al = b("e");
      }

      if (ak == null) {
         ak = b("f");
      }

      if (ag == null) {
         ag = b("g");
      }

      if (ah == null) {
         ah = b("p");
      }

      if (ai == null) {
         ai = b("h");
      }
   }

   private void y() {
      aa = null;
      ab = null;
      aj = null;
      am = null;
      al = null;
      ak = null;
      ag = null;
      ah = null;
      ai = null;
      System.gc();
      a("/o");
   }

   private void z() {
      this.O = new Random();
      C();
      this.N = new c(this);
      this.X = new a(this);
      this.X.a();
      this.W = new d[au];
      byte var10000 = au;

      byte var1;
      while ((var1 = (byte)(var10000 - 1)) >= 0) {
         this.W[var1] = new d(this);
         var10000 = var1;
      }

      this.W[0].a();
      this.Y = new g[10];

      for (int var2 = 9; var2 >= 0; var2--) {
         this.Y[var2] = new g(this);
      }

      this.Z = new g[10];

      for (int var3 = 9; var3 >= 0; var3--) {
         this.Z[var3] = new g(this);
      }

      this.aG = new byte[4];
      this.aH = new byte[4];
      this.aI = new int[5];
      this.aw = new short[4];
      this.ax = new short[4];
      this.ay = new short[4];
      this.az = new short[4];
      this.aA = new short[4];
      this.aB = new int[4];
      this.aC = new int[4];
      this.aD = new short[20];
      this.aE = new short[20];
      this.aF = new byte[20];
      this.aU = new int[6];
      this.aV = new int[6];
      this.aY = new byte[6];
      this.aW = new short[6];
      this.aX = new short[6];
      this.aZ = new short[4];
      this.ba = new short[4];
      this.bb = new short[4];
      this.bc = new short[4];
      this.bd = new byte[4];
      bY = new int[12];
      bZ = new boolean[12];
      this.at = new int[8];
   }

   private void h(int var1) {
      x();
      this.X.r = -2;
      this.by = false;
      this.A();
      this.Q = (byte)var1;
      this.N.a(var1);
      this.P = c.a[this.Q];
      this.N.a(this.P, true);
      this.X.T = this.R * 22 + 11 << 8;
      this.X.S = this.X.X = this.S;
      this.X.ab = 1;
      this.X.ac = 0;
      this.X.ad = 1;
      this.X.ae = 0;
      this.X.Y = 0;
      this.X.R = 0;
      this.X.Z = 1;
      this.X.aa = 20;
      this.be = 0;
      this.bf = System.currentTimeMillis();
      this.f(this.P);
      this.X.B = 0;
      this.X.ag = true;
      this.as = 0;
      this.X.a((byte)0);
      this.X.ae = 0;
      r = this.T;
      s = this.U;
      this.e = true;
      this.b = 19;
   }

   private void A() {
      this.k();
      this.aS = -1;
      this.aT = -1;
      this.bu = -1;
      this.bx = -1;
      this.bv = -1;
      this.bw = 2049;
      this.X.M = 3;
      this.X.J = 1;

      for (int var1 = 7; var1 >= 0; var1--) {
         this.X.L[var1] = 0;
         this.X.N[var1] = 0;
         this.X.K[var1] = a.e[var1];
      }

      this.c = false;
      if (a.f != null) {
         a.f.a();
      }
   }

   private void i(int var1) {
      x();
      this.X.r = -2;
      this.by = false;
      this.Q = (byte)var1;
      this.N.a(var1);
      this.X.ag = true;
      this.P = c.a[this.Q];
      this.X.B = 10;
      this.f(this.P);
      this.N.a(this.P, true);
      if (this.Q == 12) {
         this.J();
      }

      this.X.T = this.R * 22 + 11 << 8;
      this.X.S = this.X.X = this.S;
      this.X.ab = 1;
      this.X.ac = 0;
      this.X.ad = 1;
      this.X.ae = 0;
      this.X.Y = 0;
      this.X.R = 0;
      this.X.Z = 1;
      this.X.aa = 20;
      this.bf = System.currentTimeMillis();
      this.e = true;
      r = this.T;
      s = this.U;
      this.k();
      this.b = 0;
      this.w();
   }

   public final void b(int var1) {
      if (var1 >= 0 && var1 <= 19) {
         this.bu &= ~(1 << var1);
      } else {
         if (var1 >= 20 && var1 <= 39) {
            this.bv &= ~(1 << var1 - 20);
         }
      }
   }

   public final boolean c(int var1) {
      if (var1 >= 0 && var1 <= 19) {
         return (this.bu & 1 << var1) != 0;
      } else {
         return var1 < 20 || var1 > 39 ? false : (this.bv & 1 << var1 - 20) != 0;
      }
   }

   public final boolean a(int var1, int var2) {
      int var3;
      int var4;
      if (var1 > 0 && var1 <= 5) {
         var3 = (var1 - 1) * 6 + var2;
         var4 = this.aS;
      } else {
         if (var1 < 6 || var1 > 10) {
            return false;
         }

         var3 = (var1 - 6) * 6 + var2;
         var4 = this.aT;
      }

      return (var4 & 1 << var3) != 0;
   }

   private int B() {
      int var2 = 0;

      for (int var1 = (aR >> 1) - 1; var1 >= 0; var1--) {
         if ((this.aS & 1 << var1) == 0) {
            var2++;
         }

         if ((this.aT & 1 << var1) == 0) {
            var2++;
         }
      }

      return var2;
   }

   private void g(int var1, int var2) {
      if (var1 > 0 && var1 <= 5) {
         int var4 = (var1 - 1) * 6 + var2;
         this.aS &= ~(1 << var4);
      } else if (var1 >= 6 && var1 <= 10) {
         int var3 = (var1 - 6) * 6 + var2;
         this.aT &= ~(1 << var3);
      }

      if (this.B() >= aQ && (this.X.M & 128) == 0) {
         this.e(72, -1);
         this.X.M = (byte)(this.X.M | 128);
      } else {
         this.e(74, -1);
      }
   }

   public final void d() {
      if (a.g && a.f != null) {
         a.f.a(0, 1);
      }

      this.X.r = -2;
      this.P = this.V;
      this.f(this.P);
      this.N.a(this.V, false);
      this.X.B = 10;
      this.X.T = this.R * 22 + 11 << 8;
      this.X.S = this.S;
      r = this.T;
      s = this.U;
      this.X.Y = 0;
      this.X.aa = 20;
      this.X.V = this.X.U = 0;
      this.X.a((byte)0);
      this.X.ae = 0;
      this.e = true;
      if (this.Q == 12) {
         this.aH[0] = this.aH[1] = this.aH[2] = this.aH[3] = 0;
         this.aG[0] = this.aG[1] = this.aG[2] = this.aG[3] = 0;
         this.aI[0] = this.aI[1] = this.aI[2] = this.aI[3] = f;
         this.aI[4] = g;
      }
   }

   private void j(int var1) {
      if (var1 == -1) {
         this.cr = this.bg = 0;
         this.r();
      }
   }

   private static void C() {
      m = (148 - (5 + a.o + 4)) / 10;
   }

   public final void a(int var1, int var2, int var3) {
      this.X.t = var1 * 22 + 11 << 8;
      this.X.u = var2 * 14 + 7 - 2 << 8;
      byte var4 = 2;
      if (!this.X.ag) {
         var4 = -2;
      }

      this.X.v = (this.X.T >> 8) + var4 << 8;
      this.X.w = this.X.S * 14 + (this.X.Y >> 8) + 3 << 8;
      this.X.x = (this.X.t - this.X.v) / 6;
      this.X.y = (this.X.u - this.X.w) / 6;
      if (this.X.u < this.X.w) {
         this.X.C = (byte)var3;
         this.X.r = 0;
         this.X.a((byte)5);
         this.X.ae = 2;
      }
   }

   public static int b(int var0, int var1) {
      int var2 = 252;
      int var3 = var1 / 14;
      int var4;
      if ((var4 = var0 / 22) > -1 && var4 < c.e.length && (c.e[var4] & 1 << var3 + 1) > 0) {
         var2 = (var3 + 1) * 14;
      }

      return var2;
   }

   public final short e() {
      short var1 = 252;
      int var2 = 0;
      if (this.X.q != 0) {
         return 252;
      }

      for (int var3 = 3; var3 >= 0; var3--) {
         if (this.bd[var3] != -1
            && this.aZ[var3] + this.bb[var3] <= (this.X.T >> 8) + 4
            && this.aZ[var3] + this.bb[var3] + 22 >= (this.X.T >> 8) - 4
            && (var2 = this.ba[var3] + this.bc[var3]) < var1
            && var2 > this.X.c()) {
            var1 = (short)var2;
            this.X.E = 0;
            a var10000;
            byte var10001;
            if (this.bd[var3] == 0) {
               var10000 = this.X;
               var10001 = 1;
            } else {
               if (this.bd[var3] != 2) {
                  continue;
               }

               var10000 = this.X;
               var10001 = -1;
            }

            var10000.E = var10001;
         }
      }

      return (short)(var1 - 14);
   }

   public final short f() {
      short var1 = 252;
      short var2 = this.X.c();
      int var3 = this.X.T >> 8;

      for (int var6 = 19; var6 >= 0; var6--) {
         if (this.aF[var6] != -1 && this.aE[var6] >= var2 && this.aD[var6] <= var3 + 3 && this.aD[var6] + 16 >= var3 - 3 && this.aE[var6] < var1) {
            var1 = this.aE[var6];
         }
      }

      return (short)(var1 - 14);
   }

   public final boolean g() {
      int var6 = this.X.T >> 8;
      short var7 = this.X.c();

      for (int var10 = 19; var10 >= 0; var10--) {
         if (this.aF[var10] >= 0
            && Math.abs(this.aD[var10] - var6) <= 22
            && Math.abs(this.aE[var10] - var7) <= 14
            && this.a(this.aD[var10], this.aE[var10], 16, 16, var6 - 4, var7, 8, 13)) {
            return true;
         }
      }

      return false;
   }

   public final boolean d(int var1) {
      int var9 = this.X.b() + 0;
      int var10 = this.X.c() + -6;
      byte var2 = this.W[var1].R;
      if (this.W[var1].R != -1 && var2 != 4) {
         if (Math.abs(this.W[var1].T - this.X.T) <= 5632 && Math.abs(this.W[var1].S - this.X.S) <= 3 && this.W[var1].ad != 5) {
            short var3 = this.W[var1].c();
            short var4 = this.W[var1].b();
            byte var5 = d.a[var2];
            byte var6 = d.b[var2];
            byte var7 = d.c[var2];
            byte var8 = d.d[var2];
            return this.a(var3 - var5, var4 + var6, var7, var8, var9 - 6, var10 + 3, 12, 20);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean D() {
      int var3 = this.X.T >> 8;
      short var4 = this.X.c();
      int var5 = 0;

      for (int var6 = 19; var6 >= 0; var6--) {
         if (this.aF[var6] >= 0
            && ((var5 = this.aD[var6] + 8) <= var3 || this.X.ag)
            && (var5 >= var3 || !this.X.ag)
            && Math.abs(var5 - var3) <= 22
            && this.aE[var6] == var4) {
            return true;
         }
      }

      return false;
   }

   private boolean E() {
      for (int var2 = au - 1; var2 >= 0; var2--) {
         byte var1 = this.W[var2].R;
         if (this.W[var2].R != -1
            && var1 != 4
            && (this.W[var2].T <= this.X.T || this.X.ag)
            && (this.W[var2].T >= this.X.T || !this.X.ag)
            && Math.abs(this.W[var2].T - this.X.T) <= 5632
            && this.W[var2].ad != 5
            && this.X.S == this.W[var2].S) {
            return true;
         }
      }

      return false;
   }

   public final int h() {
      int var8 = this.X.b() + 0;
      int var9 = this.X.c() + -6;

      for (int var14 = au - 1; var14 >= 0; var14--) {
         byte var1 = this.W[var14].R;
         if (this.W[var14].R != -1
            && var1 != 4
            && Math.abs(this.W[var14].T - this.X.T) <= 5632
            && Math.abs(this.W[var14].S - this.X.S) <= 3
            && this.W[var14].ad != 5) {
            short var2 = this.W[var14].c();
            short var3 = this.W[var14].b();
            byte var4 = d.a[var1];
            byte var5 = d.b[var1];
            byte var6 = d.c[var1];
            byte var7 = d.d[var1];
            if (this.a(var2 - var4, var3 + var5, var6, var7, var8 - 6, var9 + 3, 12, 20)) {
               return var14;
            }
         }
      }

      return -1;
   }

   // $VF: Irreducible bytecode was duplicated to produce valid code
   private void h(int var1, int var2) {
      if (this.bm) {
         if (var2 == -5) {
            if (this.bq >= 0) {
               this.bq = this.br;
            }

            if (this.bq < 0) {
               this.L();
               this.br = 0;
            }
         }
      } else if (var1 == -6 || var1 == -7 || var1 == 0) {
         this.bg = 0;
         if (a.f != null) {
            a.f.a();
         }

         this.q();
      } else if (this.X.ad == 10) {
         this.bh = this.bi = 0;
         this.bj &= -129;
      } else {
         f var8;
         int var9;
         label294: {
            if (var2 == -1) {
               if (this.X.ad == 11) {
                  return;
               }

               byte var3 = 0;
               if (this.X.r == -1) {
                  this.X.l();
               } else {
                  label289:
                  if (this.X.p >= 0) {
                     a var10000;
                     boolean var10001;
                     if (this.X.g() && !this.X.ag) {
                        if (!t) {
                           t = true;
                           y = this.X.b() - 4 + 11;
                        }

                        var3 = -1;
                        this.X.V = 1250;
                        this.X.p = 0;
                        var10000 = this.X;
                        var10001 = true;
                     } else {
                        if (!this.X.h() || !this.X.ag) {
                           break label289;
                        }

                        if (!t) {
                           t = true;
                           y = this.X.b() + 4 - 11;
                        }

                        var3 = 1;
                        this.X.V = -1250;
                        this.X.p = 0;
                        var10000 = this.X;
                        var10001 = false;
                     }

                     var10000.ag = var10001;
                     this.X.a((byte)4);
                     this.bi = this.bh = 0;
                  }
               }

               if (this.X.p >= 1 && var3 == 0) {
                  return;
               }

               var8 = this;
            } else {
               if (var2 == -2) {
                  if (this.X.ad == 5) {
                     return;
                  }

                  if (this.X.p == 2) {
                     this.X.a((byte)3);
                     this.X.V = 0;
                     this.X.U = 0;
                     this.X.p = 1;
                     if ((this.X.M & 1) > 0) {
                        this.X.F = false;
                        this.bi = 0;
                        this.X.a((byte)14);
                        this.X.ae = 1;
                        this.X.A = 1;
                        return;
                     }

                     return;
                  } else {
                     if ((this.X.M & 1) > 0 && this.X.p != -1 && this.X.ad != 11 && this.X.ad != 14) {
                        this.X.F = false;
                        this.bi = 0;
                        this.X.a((byte)14);
                        this.X.ae = 1;
                        this.X.A = 1;
                        return;
                     }

                     if (v) {
                        this.j(w);
                        return;
                     }

                     if (this.X.D) {
                        this.X.D = false;
                        this.X.S++;
                        this.X.i();
                        this.X.q = 10;
                        if ((this.X.M & 1) > 0 && this.X.ad != 14) {
                           this.X.F = false;
                           this.bi = 0;
                           this.X.a((byte)14);
                           this.X.ae = 1;
                           this.X.A = 1;
                           return;
                        }
                     } else if (this.X.f() && this.X.U == 0 && this.X.ad != 14 && this.X.ad != 11) {
                        this.X.i();
                        this.X.Y = a.k;
                        if ((this.X.M & 1) > 0) {
                           this.X.F = false;
                           this.bi = 0;
                           this.X.a((byte)14);
                           this.X.ae = 1;
                           this.X.A = 1;
                           return;
                        }

                        return;
                     }

                     return;
                  }
               }

               if (var2 == -3) {
                  this.bi = this.bh = var2;
                  if (t && this.X.b() < y + 11 && this.X.V > 0) {
                     this.bi = this.bh = 0;
                     return;
                  }

                  return;
               }

               if (var2 == -4) {
                  this.bi = this.bh = var2;
                  if (t && this.X.b() > y - 11 && this.X.V < 0) {
                     this.bi = this.bh = 0;
                     return;
                  }

                  return;
               }

               if (var1 == 53 || var2 == -5) {
                  boolean var7 = false;
                  if ((this.X.M & 1) > 0 && this.X.ad != 11 && this.X.ad != 14 && this.X.ad != 8 && (this.E() || this.D() || this.X.K[this.X.J] <= 0)) {
                     this.bi = 0;
                     this.X.F = false;
                     this.X.A = 0;
                     this.X.n();
                     return;
                  }

                  if (this.X.K[this.X.J] > 0 && this.bj == 0) {
                     this.bj = 129;
                     if (this.X.m()) {
                        if (a.g && a.f != null) {
                           a.f.a(5, 1);
                        }

                        bR++;
                        return;
                     }
                  }

                  return;
               }

               if (var1 == 42) {
                  this.bi = 0;
                  if ((this.X.M & 1) > 0) {
                     this.X.F = false;
                     this.X.A = 0;
                     this.X.n();
                     return;
                  }

                  return;
               }

               if (var1 == 57) {
                  if (j) {
                     i = !i;
                     return;
                  }

                  return;
               }

               if (var1 == 48) {
                  if (j) {
                     this.X.M = -1;
                     return;
                  }

                  return;
               }

               if (var1 == 55) {
                  if (j) {
                     if (this.Q != 12) {
                        this.e(bl[this.Q - 1], this.Q == 3 ? 11 : (this.Q == 10 ? 9 : -1));
                        this.b(this.Q - 1);
                        return;
                     }

                     this.aI[4] = 0;
                     return;
                  }

                  return;
               }

               if (var1 == 35) {
                  this.bg = 0;
                  if (this.X.J != 0) {
                     if (++this.X.J > 7) {
                        this.X.J = 1;
                     }

                     while ((this.X.M & 1 << this.X.J) == 0) {
                        if (++this.X.J > 7) {
                           this.X.J = 1;
                        }
                     }

                     this.e = true;
                     return;
                  }

                  return;
               }

               if (var1 == 49) {
                  if (t || this.X.ad == 11) {
                     return;
                  }

                  if (this.X.r >= 0) {
                     return;
                  }

                  if (this.X.ad != 12) {
                     this.X.a((byte)0);
                  }

                  if (this.X.p < 0) {
                     this.X.U = 2560;
                     this.X.p++;
                  } else if (this.X.p < 1) {
                     this.X.U = 1920;
                     this.X.p++;
                     this.X.ae = 1;
                     this.X.a((byte)13);
                  }

                  var8 = this;
                  var9 = -3;
                  break label294;
               }

               if (var1 == 51) {
                  if (t || this.X.ad == 11) {
                     return;
                  }

                  if (this.X.r >= 0) {
                     return;
                  }

                  if (this.X.ad != 12) {
                     this.X.a((byte)0);
                  }

                  if (this.X.p < 0) {
                     this.X.U = 2560;
                     this.X.p++;
                  } else if (this.X.p < 1) {
                     this.X.U = 1920;
                     this.X.p++;
                     this.X.ae = 1;
                     this.X.a((byte)13);
                  }

                  var8 = this;
                  var9 = -4;
                  break label294;
               }

               var8 = this;
            }

            var9 = var2;
         }

         var8.bh = var9;
      }
   }

   public final void i() {
      this.bj &= -129;
      if (this.X.ad != 8) {
         this.X.o();
      }

      this.bh = 0;
      this.bi = 0;
   }

   private void k(int var1) {
      byte var4 = this.Z[var1].g;
      if (this.Z[var1].g != -1 && var4 != 30 && (var4 < 15 || var4 > 17)) {
         int var5 = (this.Z[var1].h >> 8) - this.Z[var1].q;
         int var6 = (this.Z[var1].i >> 8) - this.Z[var1].r;
         int var7 = this.Z[var1].q << 1;
         int var8 = this.Z[var1].r << 1;

         for (int var9 = 19; var9 >= 0; var9--) {
            if (this.aF[var9] != -1
               && this.aD[var9] + r <= 128
               && this.aD[var9] + 16 + r >= 0
               && this.aE[var9] + s <= 160
               && this.aE[var9] + 16 + s >= 0
               && this.a(this.aD[var9], this.aE[var9], 16, 16, var5, var6, var7, var8)) {
               this.Z[var1].a(true);
            }
         }
      }
   }

   public final void e(int var1) {
      if (var1 == -1) {
         for (int var10 = 19; var10 >= 0; var10--) {
            if (this.aF[var10] != -1
               && Math.abs(this.aD[var10] - (this.X.T >> 8)) <= 33
               && Math.abs(this.aE[var10] - this.X.c()) <= 28
               && this.a(
                  this.aD[var10],
                  this.aE[var10],
                  16,
                  16,
                  (this.X.T >> 8) + (this.X.ag ? a.l[this.X.A] : -a.l[this.X.A] - a.n[this.X.A]),
                  this.X.c() + a.m[this.X.A],
                  a.n[this.X.A],
                  a.o[this.X.A]
               )) {
               if (a.g && a.f != null) {
                  a.f.a(4, 1);
               }

               bT++;
               f var11;
               short var12;
               short var13;
               byte var14;
               if (this.aF[var10] == 0) {
                  this.b(this.aD[var10], this.aE[var10], 0);
                  var11 = this;
                  var12 = this.aD[var10];
                  var13 = this.aE[var10];
                  var14 = 0;
               } else {
                  var11 = this;
                  var12 = this.aD[var10];
                  var13 = this.aE[var10];
                  var14 = this.aF[var10];
               }

               var11.b(var12, var13, var14);
               this.d(this.aD[var10] + 8 << 8, this.aE[var10] + 8 << 8, 30);
               this.aF[var10] = -1;
            }
         }
      } else {
         byte var4 = this.Y[var1].g;
         if (this.Y[var1].g != -1 && var4 != 30 && (var4 < 15 || var4 > 17)) {
            int var5 = (this.Y[var1].h >> 8) - this.Y[var1].q;
            int var6 = (this.Y[var1].i >> 8) - this.Y[var1].r;
            int var7 = this.Y[var1].q << 1;
            int var8 = this.Y[var1].r << 1;

            for (int var9 = 19; var9 >= 0; var9--) {
               if (this.aF[var9] != -1
                  && this.aD[var9] + r <= 128
                  && this.aD[var9] + 16 + r >= 0
                  && this.aE[var9] + s <= 160
                  && this.aE[var9] + 16 + s >= 0
                  && this.a(this.aD[var9], this.aE[var9], 16, 16, var5, var6, var7, var8)) {
                  if (a.g && a.f != null) {
                     a.f.a(4, 1);
                  }

                  f var10000;
                  short var10001;
                  short var10002;
                  byte var10003;
                  if (this.aF[var9] == 0) {
                     this.b(this.aD[var9], this.aE[var9], 0);
                     var10000 = this;
                     var10001 = this.aD[var9];
                     var10002 = this.aE[var9];
                     var10003 = 0;
                  } else {
                     var10000 = this;
                     var10001 = this.aD[var9];
                     var10002 = this.aE[var9];
                     var10003 = this.aF[var9];
                  }

                  var10000.b(var10001, var10002, var10003);
                  this.d(this.aD[var9] + 8 << 8, this.aE[var9] + 8 << 8, 30);
                  this.aF[var9] = -1;
                  this.Y[var1].a(false);
               }
            }
         }
      }
   }

   private void F() {
      for (int var3 = 5; var3 >= 0; var3--) {
         if (this.aY[var3] != -1) {
            this.aV[var3] = this.aV[var3] + 256;
            int var4;
            if ((var4 = (this.aU[var3] >> 8) / 22) <= -1) {
               this.aU[var3] = 2816;
            }

            if (var4 >= c.e.length) {
               this.aU[var3] = (c.e.length * 2 - 3) * 22 / 2 << 8;
            }

            int var5 = b(this.aU[var3] >> 8, this.aV[var3] >> 8);
            if ((this.aV[var3] >> 8) + 8 >= var5) {
               this.aV[var3] = var5 - 8 << 8;
            }
         }
      }
   }

   private void G() {
      if (this.aO != -1) {
         if (this.a(this.X.b() + 0 - 6, this.X.c() + -6 + 3, 12, 20, this.aO * 22 + 7, this.aP * 14, 8, 8)) {
            this.g(this.Q, this.P);
            this.aO = this.aP = -1;
            this.N.a();
            bF++;
         }
      }
   }

   private void c(Graphics var1) {
      var1.setClip(0, 0, 128, 20);
      var1.setColor(0);
      var1.fillRect(0, 0, 128, 20);
      int var2 = this.X.aa;
      var1.setColor(16777215);
      if (this.X.J != 6 && this.X.J != 0) {
         a.a(var1, String.valueOf(this.X.K[this.X.J]), 13, 6, 20);
      }

      a.a(var1, String.valueOf(this.as), 114, 6, 24);
      var1.setClip(1, 4, 11, 12);
      var1.drawImage(ag, 1, 4 - (this.X.J - 1) * 12, 20);
      var1.setClip(115, 4, 11, 12);
      var1.drawImage(ag, 115, -92, 20);
      var1.setClip(36, 4, 42, 12);
      var1.setColor(2381412);
      var1.drawRect(36, 4, 41, 11);
      var2 = (var2 * 40 + 10) / 20;
      var1.setColor(2394780);
      var1.drawRect(37, 5, var2 - 1, 9);
      var1.setColor(1371884);
      var1.fillRect(38, 6, var2 - 2, 8);
   }

   private void d(Graphics var1) {
      int var2 = r;
      int var3 = s + -6;

      for (int var4 = 3; var4 >= 0; var4--) {
         if (this.aw[var4] != -1) {
            int var5 = this.aw[var4] + var2;
            int var6 = this.ax[var4] + var3;
            int var7 = this.ay[var4] + var2;
            int var8 = this.az[var4] + var3;
            if ((var5 <= 128 || var7 <= 128) && (var5 >= 0 || var7 >= 0) && (var6 <= 160 || var8 <= 160) && (var6 >= 0 || var8 >= 0)) {
               var1.setClip(0, 20, 128, 140);
               var1.setColor(12303291);
               var1.drawLine(var5, var6 - 1, var7, var8 - 1);
               var1.setColor(8947848);
               var1.drawLine(var5, var6, var7, var8);
               var1.setColor(5592405);
               var1.drawLine(var5, var6 + 1, var7, var8 + 1);
            }
         }
      }
   }

   private void e(Graphics var1) {
      int var4 = r;
      int var5 = s;

      for (int var7 = 3; var7 >= 0; var7--) {
         if (this.bd[var7] >= 0) {
            int var8 = this.aZ[var7] + this.bb[var7] + var4;
            int var9 = this.ba[var7] + this.bc[var7] + var5;
            if (var8 <= 128 && var8 + 22 >= 0 && var9 <= 160 && var9 + 14 >= 20) {
               int var6 = 0;
               if (var9 < 20) {
                  var6 = 20 - var9;
                  var9 = 20;
               }

               if (var6 < 14 && var6 >= 0) {
                  var1.setClip(var8, var9, 22, 14 - var6);
                  var1.drawImage(am, var8, var9 - var6 - 182, 20);
               }
            }
         }
      }
   }

   private void f(Graphics var1) {
      int var4 = r;
      int var5 = s;

      for (int var6 = 0; var6 < 20; var6++) {
         if (this.aF[var6] >= 0) {
            int var8 = this.aD[var6] + var4;
            int var9 = this.aE[var6] + var5;
            if (var8 <= 128 && var8 + 16 >= 0 && var9 <= 160 && var9 + 16 >= 0) {
               int var7 = 0;
               if (var9 < 20) {
                  var7 = 20 - var9;
                  var9 = 20;
               }

               if (var7 < 16 && var7 >= 0) {
                  var1.setClip(var8, var9, 16, 16 - var7);
                  var1.drawImage(al, var8, var9 - this.aF[var6] * 16 - var7, 0);
               }
            }
         }
      }
   }

   private void g(Graphics var1) {
      int var4 = r;
      int var5 = s;

      for (int var7 = 5; var7 >= 0; var7--) {
         if (this.aY[var7] >= 0) {
            int var8 = (this.aU[var7] >> 8) + var4;
            int var9 = (this.aV[var7] >> 8) + var5;
            if (var8 <= 128 && var8 + 8 >= 0 && var9 <= 160 && var9 + 8 >= 0) {
               int var6 = 0;
               if (var9 < 20) {
                  var6 = 20 - var9;
                  var9 = 20;
               }

               if (var6 < 8 && var6 >= 0) {
                  var1.setClip(var8, var9, 8, 8 - var6);
                  var1.drawImage(ai, var8, var9 - this.aY[var7] * 8 - var6, 20);
               }
            }
         }
      }
   }

   private void h(Graphics var1) {
      for (int var2 = au - 1; var2 >= 0; var2--) {
         if (this.W[var2].R != -1) {
            this.W[var2].a(var1, var2);
         }
      }

      this.X.a(var1);

      for (int var3 = 9; var3 >= 0; var3--) {
         if (this.Z[var3].g >= 0) {
            this.Z[var3].a(var1, true);
         }
      }

      for (int var4 = 9; var4 >= 0; var4--) {
         if (this.Y[var4].g >= 0) {
            this.Y[var4].a(var1, false);
         }
      }
   }

   private void H() {
      this.cA = true;

      for (int var1 = 9; var1 >= 0; var1--) {
         if (this.Z[var1].g == -1) {
            this.Z[var1].h = 78848;
            this.Z[var1].i = 32256;
            this.Z[var1].g = 31;
            this.Z[var1].p = 0;
            this.Z[var1].s = 25;
            this.Z[var1].q = g.b[31];
            this.Z[var1].r = g.c[31];
            g var10000;
            int var10001;
            if (this.aJ == 0 || this.aJ == 1 || this.aJ == 7) {
               var10000 = this.Z[var1];
               var10001 = g.d[31] << 8;
            } else if (this.aJ != 2 && this.aJ != 6) {
               var10000 = this.Z[var1];
               var10001 = -(g.d[31] << 8);
            } else {
               var10000 = this.Z[var1];
               var10001 = 0;
            }

            var10000.n = var10001;
            if (this.aJ != 2 && this.aJ != 1 && this.aJ != 3) {
               if (this.aJ != 0 && this.aJ != 4) {
                  this.Z[var1].o = g.d[31] << 8;
                  return;
               }

               this.Z[var1].o = 0;
               return;
            }

            this.Z[var1].o = -(g.d[31] << 8);
            return;
         }
      }
   }

   public final void c(int var1, int var2) {
      this.cA = false;
      int var3 = (var1 >> 8) / 22;
      int var4 = (var2 >> 8) / 14;
      if (!this.N.b(var3, var4)) {
         if (this.N.b(var3, var4 - 1)) {
            var4--;
         } else if (this.N.b(var3, var4 + 1)) {
            var4++;
         } else if (this.N.b(var3 + 1, var4)) {
            var3++;
         } else {
            if (!this.N.b(var3 - 1, var4)) {
               return;
            }

            var3--;
         }
      }

      byte var5 = this.X.d();
      byte var6 = this.X.e();
      byte var7 = this.X.Y < a.k ? this.X.S : (byte)(this.X.S + 1);
      if (var3 == var5 && (var4 == var7 || var4 + 1 == var7)) {
         if (!this.N.b(var3 + 1, var4)) {
            return;
         }

         var3++;
      }

      if (var3 == var6 && (var4 == var7 || var4 + 1 == var7)) {
         if (!this.N.b(var3 - 1, var4)) {
            return;
         }

         var3--;
      }

      this.a(var3, var4, (byte)1, -1);
   }

   private void I() {
      if (this.aI[4] <= 0) {
         a.a((byte)1, 1);
         this.bg = 0;
         this.e(112 + (this.Q - 1), 10);
      } else {
         short var1 = this.X.b();
         int var2 = this.X.c() + -6 + 11;
         int var5 = var1 - 308;
         int var6 = var2 - 126;
         label86:
         if (this.aL <= 2 && !this.cA) {
            f var10000;
            byte var10001;
            if (var5 > 14) {
               if (var6 > 14) {
                  var10000 = this;
                  var10001 = 7;
               } else if (var6 < -14) {
                  var10000 = this;
                  var10001 = 1;
               } else {
                  var10000 = this;
                  var10001 = 0;
               }
            } else if (var5 < -14) {
               if (var6 > 14) {
                  var10000 = this;
                  var10001 = 5;
               } else if (var6 < -14) {
                  var10000 = this;
                  var10001 = 3;
               } else {
                  var10000 = this;
                  var10001 = 4;
               }
            } else {
               this.aJ = 2;
               if (var6 <= 0) {
                  break label86;
               }

               var10000 = this;
               var10001 = 6;
            }

            var10000.aJ = var10001;
         }

         if (++this.aK > h) {
            this.aK = 0;
            if (++this.aL > 12) {
               this.aL = 0;
            }

            if (this.aL > 2) {
               this.aK = (byte)(h >> 2);
               if (++this.aJ > 7) {
                  this.aJ = 0;
               }
            }

            this.H();
         }

         for (int var9 = 0; var9 < 4; var9++) {
            label95: {
               byte[] var10;
               int var11;
               byte var10002;
               if (this.aG[var9] != 0 && this.aI[var9] > 0) {
                  if (--this.aH[var9] >= 0) {
                     break label95;
                  }

                  var10 = this.aG;
                  var11 = var9;
                  var10002 = 0;
               } else {
                  if (this.aG[var9] == 2 || this.aI[var9] > 0 || --this.aH[var9] >= 0) {
                     break label95;
                  }

                  var10 = this.aG;
                  var11 = var9;
                  var10002 = 2;
               }

               var10[var11] = var10002;
            }

            this.l(var9);
         }

         if (this.aI[0] + this.aI[1] + this.aI[2] + this.aI[3] == 0) {
            this.l(4);
         }
      }
   }

   private void J() {
      this.aJ = 4;
      this.aH[0] = this.aH[1] = this.aH[2] = this.aH[3] = 0;
      this.aG[0] = this.aG[1] = this.aG[2] = this.aG[3] = 0;
      this.aI[0] = this.aI[1] = this.aI[2] = this.aI[3] = f;
      this.aI[4] = g;
      this.aL = 0;
   }

   private void l(int var1) {
      byte var4 = 22;
      byte var5 = 14;
      if (this.aI[var1] <= 0) {
         this.aI[var1] = 0;
      } else {
         int var2;
         byte[] var10000;
         int var10001;
         if (var1 == 4) {
            var4 = 44;
            var5 = 28;
            var2 = aM[2] * 22;
            var10000 = aN;
            var10001 = 2;
         } else {
            var2 = aM[var1] * 22;
            var10000 = aN;
            var10001 = var1;
         }

         int var3 = var10000[var10001] * 14;
         if (var2 + var4 + r >= 0 && var2 + r <= 128 && var3 + var5 + s >= 0 && var3 + s <= 160) {
            for (int var6 = 9; var6 >= 0; var6--) {
               byte var7 = this.Y[var6].g;
               if (this.Y[var6].g >= 0 && var7 != 30) {
                  int var10 = this.Y[var6].q;
                  int var11 = this.Y[var6].r;
                  int var8 = (this.Y[var6].h >> 8) - var10;
                  int var9 = (this.Y[var6].i >> 8) - var11;
                  if (this.a(var2, var3, var4, var5, var8, var9, var10 << 1, var11 << 1)) {
                     this.aI[var1] = this.aI[var1] - g.a[var7];
                     byte var10002;
                     if (var1 == 4) {
                        this.aH[0] = this.aH[1] = this.aH[2] = this.aH[3] = 5;
                        var10000 = this.aG;
                        var10001 = 0;
                        var10002 = this.aG[1] = this.aG[2] = this.aG[3] = 3;
                     } else {
                        this.aH[var1] = 5;
                        var10000 = this.aG;
                        var10001 = var1;
                        var10002 = 1;
                     }

                     var10000[var10001] = var10002;
                     this.Y[var6].a(false);
                     return;
                  }
               }
            }
         }
      }
   }

   public final void a(int var1, int var2, int var3, int var4) {
      for (int var5 = 0; var5 < 4; var5++) {
         this.b(var5, var1, var2, var3, var4);
      }

      if (this.aI[0] + this.aI[1] + this.aI[2] + this.aI[3] == 0) {
         this.b(4, var1, var2, var3, var4);
      }
   }

   private void b(int var1, int var2, int var3, int var4, int var5) {
      byte var8 = 22;
      byte var9 = 14;
      if (this.aI[var1] <= 0) {
         this.aI[var1] = 0;
      } else {
         int var6;
         byte[] var10000;
         int var10001;
         if (var1 == 4) {
            var8 = 44;
            var9 = 28;
            var6 = aM[2] * 22;
            var10000 = aN;
            var10001 = 2;
         } else {
            var6 = aM[var1] * 22;
            var10000 = aN;
            var10001 = var1;
         }

         int var7 = var10000[var10001] * 14;
         if (Math.abs(var6 - var2) <= 33 && Math.abs(var7 - var3) <= 14) {
            if (this.a(var6, var7, var8, var9, var2, var3, var4, var5)) {
               int var10002;
               byte[] var10003;
               byte[] var10004;
               byte var10005;
               if (this.X.ad == 11) {
                  var10000 = this.aI;
                  var10001 = var1;
                  var10002 = this.aI[var1];
                  var10003 = a.i;
                  var10004 = this.X.N;
                  var10005 = 1;
               } else {
                  var10000 = this.aI;
                  var10001 = var1;
                  var10002 = this.aI[var1];
                  var10003 = a.i;
                  var10004 = this.X.N;
                  var10005 = 0;
               }

               var10000[var10001] = var10002 - var10003[var10004[var10005]];
               byte[] var11;
               byte var14;
               if (var1 == 4) {
                  this.aH[0] = this.aH[1] = this.aH[2] = this.aH[3] = 5;
                  var11 = this.aG;
                  var10001 = 0;
                  var14 = this.aG[1] = this.aG[2] = this.aG[3] = 3;
               } else {
                  this.aH[var1] = 5;
                  var11 = this.aG;
                  var10001 = var1;
                  var14 = 1;
               }

               var11[var10001] = var14;
            }
         }
      }
   }

   private void i(Graphics var1) {
      if (this.aI[4] > 0) {
         byte var4;
         byte var5;
         byte var6;
         byte var7;
         short var8;
         int var9;
         int var12;
         int var14;
         label95: {
            var6 = 0;
            var7 = 0;
            var8 = 0;
            var9 = 0;
            var4 = 22;
            var5 = 14;
            var12 = 308 + r;
            var14 = 126 + s;
            byte var10000;
            if (this.aJ == 0) {
               var12 += 0;
               var14 -= 7;
               var10000 = 11;
            } else if (this.aJ == 1) {
               var12 += -2;
               var14 -= 20;
               var10000 = 12;
            } else {
               if (this.aJ == 2) {
                  var12 -= 7;
                  var14 -= 22;
                  var6 = 11;
                  var5 = 22;
                  var4 = 14;
                  var8 = 90;
                  break label95;
               }

               if (this.aJ == 3) {
                  var12 -= 20;
                  var14 -= 20;
                  var7 = 12;
                  var8 = 8192;
                  break label95;
               }

               if (this.aJ == 4) {
                  var12 -= 22;
                  var14 -= 7;
                  var7 = 11;
                  var8 = 8192;
                  break label95;
               }

               if (this.aJ == 5) {
                  var12 -= 20;
                  var14 += 6;
                  var10000 = 180;
               } else {
                  if (this.aJ == 6) {
                     var12 -= 7;
                     var14 += 0;
                     var8 = 270;
                     var5 = 22;
                     var4 = 14;
                     var6 = 2;
                     break label95;
                  }

                  var12 += -2;
                  var14 += 6;
                  var10000 = 16384;
               }

               var8 = var10000;
               var10000 = 1;
            }

            var7 = var10000;
         }

         if (var14 < 20) {
            var9 = 20 - var14;
            var14 = 20;
         }

         if (var9 < var5 && var9 >= 0) {
            var1.setClip(var12, var14, var4, var5 - var9);
            if (this.aJ != 0 && this.aJ != 1) {
               DirectUtils.getDirectGraphics(var1).drawImage(am, var12 - var4 * var6, var14 - var5 * var7 - var9, 20, var8);
            } else {
               var1.drawImage(am, var12, var14 - var5 * var7 - var9, 0);
            }
         }

         for (int var10 = 0; var10 < 4; var10++) {
            var9 = 0;
            var12 = aM[var10] * 22 + r;
            if ((var14 = aN[var10] * 14 + s) < 20) {
               var9 = 20 - var14;
               var14 = 20;
            }

            if (var9 < 14 && var9 >= 0) {
               label66: {
                  int var20;
                  label65: {
                     var7 = 98;
                     var1.setClip(var12, var14, 22, 14 - var9);
                     short var19;
                     if (var10 == 1) {
                        var19 = 16384;
                     } else {
                        if (var10 == 2) {
                           var8 = 8192;
                           var20 = this.aG[var10] + 7;
                           break label65;
                        }

                        if (var10 != 3) {
                           break label66;
                        }

                        var19 = 180;
                     }

                     var8 = var19;
                     var20 = 13 - (this.aG[var10] + 7);
                  }

                  var7 = var20 * 14;
               }

               if (var10 == 0) {
                  var1.drawImage(am, var12, var14 - 14 * this.aG[var10] - var7 - var9, 0);
               } else {
                  DirectUtils.getDirectGraphics(var1).drawImage(am, var12, var14 - var7 - var9, 20, var8);
               }
            }
         }
      }
   }

   private void m(int var1) {
      label55: {
         f var10000;
         byte var10001;
         label54: {
            if (var1 == av) {
               this.an = this.X.R;
               this.ao = this.X.ad;
               this.ap = this.X.ab;
               if (this.X.ag) {
                  var10000 = this;
                  var10001 = 0;
                  break label54;
               }

               var10000 = this;
            } else {
               if (var1 < 0) {
                  break label55;
               }

               this.an = this.W[var1].R;
               this.ao = this.W[var1].ad;
               this.ap = this.W[var1].ab;
               if (this.W[var1].ah != 0) {
                  if (this.W[var1].ah == 1) {
                     if (this.W[var1].ag) {
                        var10000 = this;
                        var10001 = 4;
                     } else {
                        var10000 = this;
                        var10001 = 2;
                     }
                  } else if (this.W[var1].ah == 2) {
                     if (this.W[var1].ag) {
                        var10000 = this;
                        var10001 = 6;
                     } else {
                        var10000 = this;
                        var10001 = 3;
                     }
                  } else {
                     if (this.W[var1].ah != 3) {
                        break label55;
                     }

                     if (this.W[var1].ag) {
                        var10000 = this;
                        var10001 = 1;
                     } else {
                        var10000 = this;
                        var10001 = 7;
                     }
                  }
                  break label54;
               }

               if (this.W[var1].ag) {
                  var10000 = this;
                  var10001 = 0;
                  break label54;
               }

               var10000 = this;
            }

            var10001 = 5;
         }

         var10000.ar = var10001;
      }

      if (this.an >= 0) {
         if (var1 == av) {
            this.aq = a.G[this.an][this.ao][this.ap];
            return;
         }

         this.aq = d.p[this.an][this.ao][this.ap];
         this.z = 18;
      }
   }

   public final void a(Graphics var1, int var2, int var3) {
      this.m(av);
      DirectGraphics var4 = DirectUtils.getDirectGraphics(var1);
      if (this.ar == 5) {
         var4.drawImage(aj, var2, var3, 20, 8192);
      }
   }

   public final void a(Graphics var1, int var2, int var3, int var4) {
      this.m(var2);
      DirectGraphics var5 = DirectUtils.getDirectGraphics(var1);
      byte var6 = 0;
      if (this.W[var2].s) {
         var6 = 44;
      }

      DirectGraphics var10000;
      Image var10001;
      int var10002;
      int var10003;
      byte var10004;
      short var10005;
      switch (this.ar) {
         case 1:
            var10000 = var5;
            var10001 = ab;
            var10002 = var3 - this.aq * 22 - var6;
            var10003 = var4;
            var10004 = 20;
            var10005 = 90;
            break;
         case 2:
            var10000 = var5;
            var10001 = ab;
            var10002 = var3;
            var10003 = var4 - (this.z - 1 - this.aq) * 22 + var6;
            var10004 = 20;
            var10005 = 180;
            break;
         case 3:
            var10000 = var5;
            var10001 = ab;
            var10002 = var3 - (this.z - 1 - this.aq) * 22 + var6;
            var10003 = var4;
            var10004 = 20;
            var10005 = 270;
            break;
         case 4:
            var10000 = var5;
            var10001 = ab;
            var10002 = var3;
            var10003 = var4 - (this.z - 1 - this.aq) * 22 + var6;
            var10004 = 20;
            var10005 = 16384;
            break;
         case 5:
            var10000 = var5;
            var10001 = ab;
            var10002 = var3;
            var10003 = var4 - this.aq * 22 - var6;
            var10004 = 20;
            var10005 = 8192;
            break;
         case 6:
            var10000 = var5;
            var10001 = ab;
            var10002 = var3 - (this.z - 1 - this.aq) * 22 + var6;
            var10003 = var4;
            var10004 = 20;
            var10005 = 8282;
            break;
         case 7:
            var10000 = var5;
            var10001 = ab;
            var10002 = var3 - this.aq * 22 - var6;
            var10003 = var4;
            var10004 = 20;
            var10005 = 16474;
            break;
         default:
            return;
      }

      var10000.drawImage(var10001, var10002, var10003, var10004, var10005);
   }

   private static boolean e(int var0, int var1, int var2) {
      return var0 >= var1 && var0 <= var1 + var2;
   }

   public final boolean a(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      return (e(var5, var1, var3) || e(var1, var5, var7)) && (e(var6, var2, var4) || e(var2, var6, var8));
   }

   public final void a(int var1, int var2, boolean var3, int var4) {
      short var5 = (short)(var1 * 22 + 11);
      short var6 = (short)(var2 * 14 + 7);
      short[] var10000;
      if (var3) {
         this.aw[var4] = var5;
         var10000 = this.ax;
      } else {
         this.ay[var4] = var5;
         var10000 = this.az;
      }

      var10000[var4] = var6;
      if (this.aw[var4] != -1 && this.ay[var4] != -1) {
         this.aB[var4] = this.az[var4] - this.ax[var4] << 8;
         this.aA[var4] = (short)(this.ay[var4] - this.aw[var4]);
         this.aC[var4] = (this.ax[var4] << 8) - this.aB[var4] * this.aw[var4] / this.aA[var4];
      }
   }

   public final void a(int var1, int var2, int var3, int var4, int var5) {
      this.aF[var5] = (byte)var3;
      short[] var10000;
      int var10001;
      int var10002;
      int var10003;
      if (var4 == 2) {
         var10000 = this.aD;
         var10001 = var5;
         var10002 = var1 * 22;
         var10003 = 8;
      } else {
         var10000 = this.aD;
         var10001 = var5;
         var10002 = var1 * 22;
         var10003 = var4 * 16;
      }

      var10000[var10001] = (short)(var10002 + var10003);
      this.aE[var5] = (short)(var2 * 14);
   }

   public final void b(int var1, int var2, int var3) {
      for (int var4 = 5; var4 >= 0; var4--) {
         if (this.aY[var4] == -1) {
            byte[] var10000;
            int var10001;
            byte var10002;
            if (var3 == 0) {
               var10000 = this.aY;
               var10001 = var4;
               var10002 = (byte)(Math.abs(this.O.nextInt()) % 2);
            } else if (var3 == 1) {
               var10000 = this.aY;
               var10001 = var4;
               var10002 = 2;
            } else {
               var10000 = this.aY;
               var10001 = var4;
               var10002 = 3;
            }

            var10000[var10001] = var10002;
            this.aU[var4] = var1 + Math.abs(this.O.nextInt()) % 11 << 8;
            this.aV[var4] = var2 << 8;
            this.aW[var4] = 0;
            this.aX[var4] = 768;
            return;
         }
      }
   }

   public final void c(int var1, int var2, int var3) {
      for (int var4 = 3; var4 >= 0; var4--) {
         if (this.bd[var4] == -1) {
            this.bd[var4] = (byte)var3;
            if (var3 != 0 && var3 != 1) {
               if (var3 == 2) {
                  this.aZ[var4] = (short)((var1 - 2) * 22);
                  this.ba[var4] = (short)(var2 * 14);
                  this.bb[var4] = 44;
                  this.bc[var4] = 0;
                  return;
               }

               this.aZ[var4] = (short)(var1 * 22);
               this.ba[var4] = (short)((var2 - 2) * 14);
               this.bb[var4] = 0;
               this.bc[var4] = 28;
               return;
            }

            this.aZ[var4] = (short)(var1 * 22);
            this.ba[var4] = (short)(var2 * 14);
            this.bb[var4] = 0;
            this.bc[var4] = 0;
            return;
         }
      }
   }

   public final void d(int var1, int var2, int var3) {
      for (int var4 = 9; var4 >= 0; var4--) {
         if (this.Y[var4].g == -1) {
            if (var3 >= 0 && var3 <= 14 || var3 >= 18 && var3 <= 20) {
               this.X.K[this.X.J]--;
               this.e = true;
            }

            if (var3 >= 9 && var3 <= 11 || var3 >= 15 && var3 <= 17) {
               this.Y[var4].q = 55;
               this.Y[var4].r = 7;
               var1 += (this.X.ag ? 1 : -1) * 55 << 8;
            } else {
               this.Y[var4].q = g.b[var3];
               this.Y[var4].r = g.c[var3];
            }

            this.Y[var4].l = this.Y[var4].j = this.Y[var4].h = var1;
            this.Y[var4].m = this.Y[var4].k = this.Y[var4].i = var2;
            this.Y[var4].g = (byte)var3;
            this.Y[var4].n = (this.X.ag ? 1 : -1) * g.d[var3] << 8;
            this.Y[var4].o = 0;
            this.Y[var4].p = 0;
            this.Y[var4].t = this.X.ag;
            this.Y[var4].u = this.X.T;
            if (var3 >= 3 && var3 <= 5) {
               this.Y[var4].o = 512;
               return;
            }
            break;
         }
      }
   }

   public final void a(int var1, int var2, int var3, boolean var4, boolean var5, int var6, byte var7) {
      for (int var8 = 9; var8 >= 0; var8--) {
         if (this.Z[var8].g == -1) {
            int var9;
            g var10000;
            label86: {
               this.Z[var8].h = var1;
               this.Z[var8].i = var2;
               this.Z[var8].g = (byte)var3;
               this.Z[var8].p = 0;
               this.Z[var8].s = var7;
               this.Z[var8].q = g.b[var3];
               this.Z[var8].r = g.c[var3];
               byte[] var10002;
               if (var6 != 0 && var6 != 1) {
                  if (var5) {
                     this.Z[var8].n = (short)((var6 == 3 ? -1 : 1) * g.d[var3] << 8);
                     var10000 = this.Z[var8];
                     var9 = 0;
                     break label86;
                  }

                  this.Z[var8].n = 0;
                  var10000 = this.Z[var8];
                  var9 = var4 ? -1 : 1;
                  var10002 = g.d;
               } else {
                  if (!var5) {
                     this.Z[var8].n = (short)((var4 ? 1 : -1) * g.d[var3] << 8);
                     var10000 = this.Z[var8];
                     var9 = 0;
                     break label86;
                  }

                  this.Z[var8].n = 0;
                  if (var7 != 17 && var7 != 20 && var7 != 23) {
                     var10000 = this.Z[var8];
                     var9 = var6 == 0 ? -1 : 1;
                     var10002 = g.d;
                  } else {
                     var10000 = this.Z[var8];
                     var9 = var6 == 0 ? 1 : -1;
                     var10002 = g.d;
                  }
               }

               var9 = (short)(var9 * var10002[var3] << 8);
            }

            var10000.o = var9;
            if (var3 == 32) {
               this.Z[var8].j = var1;
               this.Z[var8].k = var2;
               this.Z[var8].l = var1;
               this.Z[var8].m = var2 - 14336;
               return;
            }
            break;
         }
      }
   }

   public final void a(int var1, int var2, byte var3, int var4) {
      if (var4 == -1) {
         var4 = au - 1;

         while (var4 >= 0 && this.W[var4].R != -1) {
            var4--;
         }

         if (var4 == -1) {
            return;
         }
      }

      d var10000;
      byte var10001;
      if (var3 >= 0 && var3 <= 2) {
         var10000 = this.W[var4];
         var10001 = 1;
      } else if (var3 >= 3 && var3 <= 5) {
         var10000 = this.W[var4];
         var10001 = 0;
      } else if (var3 >= 6 && var3 <= 13) {
         var10000 = this.W[var4];
         var10001 = 2;
      } else {
         var10000 = this.W[var4];
         var10001 = 3;
      }

      var10000.R = var10001;
      this.W[var4].T = var1 * 22 + 11 << 8;
      this.W[var4].S = (byte)var2;
      this.W[var4].Y = this.W[var4].V = this.W[var4].U = 0;
      this.W[var4].aa = d.i[var3];
      this.W[var4].ah = 0;
      this.W[var4].ag = true;
      if (this.Q == 12 && this.X.T < this.W[var4].T) {
         this.W[var4].ag = false;
      }

      this.W[var4].s = false;
      this.W[var4].B = 0;
      this.W[var4].u = 0;
      this.W[var4].A = 0;
      this.W[var4].B = 0;
      this.W[var4].C = this.W[var4].T;
      if (this.W[var4].R == 3) {
         short var5;
         if ((var5 = this.N.a(var1 + 1, var2)) >= 4 && var5 <= 13) {
            this.W[var4].ah = 3;
         }

         if ((var5 = this.N.a(var1 - 1, var2)) >= 4 && var5 <= 13) {
            this.W[var4].ah = 2;
         }

         if ((var5 = this.N.a(var1, var2 - 1)) >= 4 && var5 <= 13) {
            this.W[var4].Y = 3072;
            this.W[var4].ah = 1;
         }
      }

      this.W[var4].t = var3;
      this.W[var4].a((byte)0);
      this.W[var4].ae = 1;
      this.W[var4].Z = 1;
   }

   private void n(int var1) {
      if (this.W[var1].R != -1 && this.W[var1].ad != 5) {
         byte var2 = 0;
         short var3 = this.W[var1].c();
         short var4 = this.W[var1].b();
         byte var5 = this.W[var1].R;
         byte var6 = d.a[var5];
         byte var7 = d.b[var5];
         byte var8 = d.c[var5];
         byte var9 = d.d[var5];
         if (var3 - var6 + var8 + r >= 0 && var3 - var6 + r <= 128 && var4 + var7 + var9 + s >= 0 && var4 + var7 + s <= 160) {
            for (int var10 = 9; var10 >= 0; var10--) {
               byte var11 = this.Y[var10].g;
               if (this.Y[var10].g >= 0 && var11 != 30 && (this.Y[var10].a() || var11 >= 9 && var11 <= 11 || var11 >= 15 && var11 <= 17 || var11 == 2)) {
                  int var12 = (this.Y[var10].h >> 8) - this.Y[var10].q;
                  int var13 = (this.Y[var10].i >> 8) - this.Y[var10].r;
                  if (this.a(var3 - var6, var4 + var7, var8, var9, var12, var13, this.Y[var10].q << 1, this.Y[var10].r << 1)) {
                     var2 = this.Y[var10].b();
                     if (var5 != 4 && var5 != 3 || var2 != 6) {
                        int var14;
                        d var10000;
                        byte var10001;
                        int var10002;
                        if (var11 < 9 || var11 > 11 || (var14 = Math.abs(this.X.b() - var3)) < 22) {
                           var10000 = this.W[var1];
                           var10001 = this.W[var1].aa;
                           var10002 = g.a[var11];
                        } else if (var14 >= 44 && var11 <= 10) {
                           var10000 = this.W[var1];
                           var10001 = this.W[var1].aa;
                           var10002 = g.a[var11] >> 2;
                        } else {
                           var10000 = this.W[var1];
                           var10001 = this.W[var1].aa;
                           var10002 = g.a[var11] >> 1;
                        }

                        var10000.aa = (byte)(var10001 - var10002);
                        this.Y[var10].a(false);
                        this.d(var3 << 8, (var4 << 8) + 2816, 30);
                        bS++;
                        if (this.W[var1].aa <= 0 && this.W[var1].ad != 5) {
                           if (a.g && a.f != null) {
                              a.f.a(1, 1);
                           }

                           if (var2 == 6 && var5 != 4) {
                              bI++;
                              this.W[var1].R = 4;
                              this.W[var1].t = 24;
                              this.W[var1].aa = d.i[24];

                              for (int var17 = 0; var17 < d.k[24]; var17++) {
                                 this.b((short)(this.W[var1].T >> 8), this.W[var1].b(), 0);
                              }

                              if (this.X.N[6] < 2) {
                                 this.X.L[6]++;
                              }

                              if (this.X.L[6] >= 20) {
                                 this.X.N[6]++;
                                 this.X.L[6] = 0;
                                 this.e(73, -1);
                                 bC++;
                                 bQ++;
                                 return;
                              }
                           } else {
                              bC++;
                              bQ++;
                              this.W[var1].a((byte)5);
                              this.W[var1].ae = 1;
                              if (var5 != 4) {
                                 for (int var16 = 0; var16 < d.k[this.W[var1].t]; var16++) {
                                    this.b((short)(this.W[var1].T >> 8), this.W[var1].b(), 0);
                                 }

                                 if (this.X.N[var2] < 2) {
                                    this.X.L[var2]++;
                                 }

                                 if (this.X.L[var2] >= 20) {
                                    this.X.N[var2]++;
                                    this.X.L[var2] = 0;
                                    this.e(73, -1);
                                    return;
                                 }
                              }
                           }
                        } else {
                           this.W[var1].a((byte)4);
                           this.W[var1].af = 0;
                        }

                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private void K() {
      int var1 = this.X.b() + 0;
      int var2 = this.X.c() + -6;

      for (int var7 = 9; var7 >= 0; var7--) {
         if (this.X.ad == 10) {
            return;
         }

         byte var8 = this.Z[var7].g;
         if (this.Z[var7].g != -1 && var8 != 30 && this.Z[var7].a()) {
            int var9 = (this.Z[var7].h >> 8) - this.Z[var7].q;
            int var10 = (this.Z[var7].i >> 8) - this.Z[var7].r;
            if (this.a(var1 - 6, var2 + 3, 12, 20, var9, var10, this.Z[var7].q << 1, this.Z[var7].r << 1)) {
               byte var11 = this.Z[var7].s;
               if (var8 != 3 && var8 != 6 && var8 != 18) {
                  if (!i) {
                     this.X.aa = (byte)(this.X.aa - d.j[var11]);
                  }

                  if (!i) {
                     bX = true;
                  }

                  this.e = true;
                  this.X.a((byte)9);
                  this.X.af = 0;
                  if (this.X.p == 2) {
                     this.X.p = 1;
                  }

                  this.d(var1 << 8, (var2 << 8) + 2816, 30);
               }

               if (var8 == 32) {
                  this.X.B = 10;
               }

               this.Z[var7].a(true);
            }
         }
      }
   }

   public static int d(int var0, int var1) {
      return var0 < var1 ? var0 : var1;
   }

   private static void j(Graphics var0) {
      var0.setClip(0, 0, 128, 160);
      var0.setColor(16777215);
      var0.fillRect(0, 124, 128, 36);
   }

   private static int a(Graphics var0, int var1, String var2, int var3, int var4, int var5, int var6) {
      int var7 = 0;
      int var8 = 0;
      int var9 = 0;
      char[] var13 = new char[var2.length()];
      var2.getChars(0, var2.length(), var13, 0);
      var7 = 0;
      int var10 = var1;
      int var11 = var1;
      char var12 = '\u0000';
      boolean var14 = false;

      while (true) {
         var8 = 0;
         var9 = var11 - 1;
         boolean var15 = false;
         boolean var16 = false;

         label67: {
            while (var11 < var2.length()) {
               if ((var12 = var2.charAt(var11)) == ' ') {
                  var8 += a.a(var12);
                  var11++;
                  if (var7 + var8 > var6 && !var14) {
                     var16 = true;
                     break label67;
                  }

                  var14 = true;
                  break label67;
               }

               int var17 = a.a(var12);
               if (var7 + var8 + var17 > var6 && !var14) {
                  var16 = true;
                  break label67;
               }

               var8 += var17;
               var11++;
            }

            var15 = true;
         }

         if (var7 + var8 > var6 || var15 || var16) {
            if (var2.charAt(var10) == ' ') {
               var10++;
            }

            if (var7 + var8 > var6 && !var16) {
               var11 = var9;
            }

            if ((var5 & 1) > 0) {
               if (var11 - var10 > 0) {
                  a.a(var0, var13, var10, var11 - var10, 64, var4, var5);
               }

               return var11 + (var16 ? 0 : 1);
            } else {
               if (var11 - var10 > 0) {
                  a.a(var0, var13, var10, var11 - var10, var3, var4, var5);
               }

               return var11 + (var16 ? 0 : 1);
            }
         }

         var7 += var8;
      }
   }

   private int a(Graphics var1, int var2) {
      byte var3 = 3;
      byte var5 = 17;
      var1.setColor(0);
      var1.setClip(0, 0, 128, 160);
      if (this.bo >= 0 && this.ce[this.bo][this.bp - 1] >= 0) {
         var3 = 26;
         var5 = 20;
         var1.setClip(0, 130, 24, 24);
         var1.drawImage(ah, 0, 130 - 24 * this.ce[this.bo][this.bp - 1], 20);
         var1.setClip(0, 0, 128, 160);
      }

      int var7 = this.bn.length();
      if ((var2 = a(var1, var2, this.bn, var3, 127, var5, 128 - var3)) >= var7) {
         return -1;
      }

      if ((var2 = a(var1, var2, this.bn, var3, 137, var5, 128 - var3)) >= var7) {
         return -1;
      }

      int var11;
      return (var11 = a(var1, var2, this.bn, var3, 147, var5, 128 - var3)) >= var7 ? -1 : var11;
   }

   public final void j() {
      this.cf = false;
      if (this.Q <= 10) {
         if (ratchetandclank.e[112 + (this.Q - 1)] != null) {
            if (!ratchetandclank.e[112 + (this.Q - 1)].equals("")) {
               if ((this.bx & 1 << this.Q - 1) > 0) {
                  this.bx = this.bx & ~(1 << this.Q - 1);
                  if (this.Q != 7 && this.Q != 9 && this.Q != 10 && this.Q != 12) {
                     this.e(112 + (this.Q - 1), this.Q - 1);
                  }
               }
            }
         }
      }
   }

   public final void e(int var1, int var2) {
      this.X.B = 0;
      if (a.g && a.f != null) {
         a.f.a(2, 1);
      }

      this.bq = 0;
      this.bo = var2;
      this.bs = var1;
      f var10000;
      String var10001;
      if (var1 == 74) {
         var10000 = this;
         var10001 = ratchetandclank.e[var1] + " " + this.B();
      } else {
         String[] var3;
         int var10002;
         if (this.bo >= 0) {
            this.bt = (byte)(this.ce[this.bo].length >> 1);
            this.bp = 1;
            var10000 = this;
            var3 = ratchetandclank.e;
            var10002 = this.ce[this.bo][this.bp];
         } else {
            var10000 = this;
            var3 = ratchetandclank.e;
            var10002 = var1;
         }

         var10001 = var3[var10002];
      }

      var10000.bn = var10001;
      if (this.bn == "") {
         this.bq = -1;
      }

      this.bm = true;
      this.bj = 0;
   }

   private void L() {
      this.bm = false;
      if (this.bo >= 0) {
         if (--this.bt > 0) {
            this.X.B = 0;
            this.bq = 0;
            this.bm = true;
            this.bp += 2;
            this.bn = ratchetandclank.e[this.ce[this.bo][this.bp]];
            return;
         }

         if (this.Q != 3 && this.Q != 10 || this.bs < bl[0] || this.bs > bl[15] + bk[15]) {
            if (this.Q == 12) {
               this.s();
               return;
            }

            return;
         }
      } else {
         if (--this.bt > 0) {
            this.e(this.bs + 1, this.bo);
            return;
         }

         if (this.bs < bl[0] || this.bs > bl[15] + bk[15]) {
            return;
         }
      }

      this.bw = this.bw | 1 << this.Q;
      this.p();
   }

   public final void a(byte[] var1, int var2) {
      var1[var2 + 0] = 1;
      ratchetandclank.a(this.bw, var1, var2 + 1);
      ratchetandclank.a(this.aS, var1, var2 + 5);
      ratchetandclank.a(this.aT, var1, var2 + 9);
      ratchetandclank.a(this.bu, var1, var2 + 13);
      var1[var2 + 17] = this.X.M;

      for (int var3 = 0; var3 < 8; var3++) {
         var1[var2 + 18 + var3] = this.X.N[var3];
         ratchetandclank.a(this.X.L[var3], var1, var2 + 26 + var3 * 2);
         ratchetandclank.a(this.X.K[var3], var1, var2 + 42 + var3 * 2);
      }

      ratchetandclank.a(this.as, var1, var2 + 58);
      ratchetandclank.a(this.be, var1, var2 + 195);
      ratchetandclank.a(this.bv, var1, var2 + 204);
      ratchetandclank.a(bP, var1, var2 + 208);
      ratchetandclank.a(this.bx, var1, var2 + 212);
      ratchetandclank.a(bQ, var1, var2 + 216);
   }

   public final void b(byte[] var1, int var2) {
      this.bw = ratchetandclank.a(var1, var2 + 1);
      this.aS = ratchetandclank.a(var1, var2 + 5);
      this.aT = ratchetandclank.a(var1, var2 + 9);
      this.bu = ratchetandclank.a(var1, var2 + 13);
      this.X.M = var1[var2 + 17];

      for (int var3 = 0; var3 < 8; var3++) {
         this.X.N[var3] = var1[var2 + 18 + var3];
         this.X.L[var3] = ratchetandclank.b(var1, var2 + 26 + var3 * 2);
         this.X.K[var3] = ratchetandclank.b(var1, var2 + 42 + var3 * 2);
      }

      this.as = ratchetandclank.a(var1, var2 + 58);
      this.be = ratchetandclank.a(var1, var2 + 195);
      this.bv = ratchetandclank.a(var1, var2 + 204);
      bP = ratchetandclank.a(var1, var2 + 208);
      this.bx = ratchetandclank.a(var1, var2 + 212);
      bQ = ratchetandclank.a(var1, var2 + 216);
      this.cr = this.bg = 0;
   }

   private void M() {
      if (this.b == 0) {
         int var1 = this.bj & 63;
         byte var2 = this.X.N[this.X.J];
         if (var1 > 0) {
            this.bj++;
            if (var1 >= a.g[var2][this.X.J]) {
               if ((this.bj & 128) == 0) {
                  this.bj = 0;
                  return;
               }

               this.bj = 129;
               if (this.X.m()) {
                  if (a.g && a.f != null) {
                     a.f.a(5, 1);
                  }

                  bR++;
                  return;
               }
            }
         }
      } else if (this.b == 20 && this.ad < 3) {
         if (!this.d) {
            this.N();
         }

         if (this.ac + 2000L < System.currentTimeMillis()) {
            label49: {
               this.ac = System.currentTimeMillis();
               f var10000;
               String var10001;
               if (this.ad == 0) {
                  var10000 = this;
                  var10001 = "hhg";
               } else {
                  if (this.ad != 1) {
                     break label49;
                  }

                  var10000 = this;
                  var10001 = "logo";
               }

               var10000.ae = b(var10001);
            }

            this.ad++;
            this.af = true;
            if (this.ad == 3) {
               this.af = false;
               this.c = true;
               this.b = 19;
               if (a.d(2) == -1) {
                  this.D = 118;
                  k = 0;
                  l = A.length - 1;
                  m = 0;
                  this.cz = true;
                  return;
               }

               C = (byte)Math.min(a.d(2), A.length - 1);
               ratchetandclank.e = ratchetandclank.a("/m" + B[C], 145);
               if (a.g && a.f != null) {
                  a.f.a(6, -1);
               }
            }
         }
      }
   }

   private void N() {
      try {
         a.a("/f2.v", 10, 1);
      } catch (IOException var2) {
      }

      a.c();
      a.f = new b();
      a.g = a.d(0) > 0;
      a.c = new byte[3];
      a.d = new int[3];
      this.t();
      this.o();
      this.d = true;
      ratchetandclank.e = ratchetandclank.a("/m" + B[0], 145);
   }

   private int a(Graphics var1, String var2, int var3, boolean var4) {
      var1.setColor(var4 ? 16777215 : 2914559);
      return a(var1, var2, 0, var3, 17);
   }

   private int b(Graphics var1, String var2, int var3, boolean var4) {
      var1.setColor(var4 ? 16777215 : 8421504);
      return a(var1, var2, 0, var3, 17);
   }

   private int a(Graphics var1, byte var2, int var3, boolean var4) {
      boolean var5 = false;
      int var10000;
      if (a.c[var2] == 0) {
         var10000 = this.b(var1, var2 + 1 + ratchetandclank.e[31], var3, var4);
      } else {
         Object[] var6 = new Object[]{new Integer(var2 + 1), new String(o(a.d[var2]))};
         String var7 = a(ratchetandclank.e[32], var6);
         var10000 = this.a(var1, var7, var3, var4);
      }

      return var10000;
   }

   private int a(Graphics var1, int var2, int var3, int var4, boolean var5) {
      Object[] var6 = new Object[]{new Integer(var3)};
      String var7 = a(ratchetandclank.e[var2], var6);
      return this.a(var1, var7, var4, var5);
   }

   private int a(Graphics var1, String var2, int var3) {
      var1.setColor(14481424);
      return a(var1, var2, 64, var3, 17);
   }

   private static String o(int var0) {
      int var1 = var0 / 1000;
      int var3;
      int var2 = (var3 = var0 / 60000) / 60;
      var0 = var3 % 60;
      var1 %= 60;
      return var2 + (var0 < 10 ? ":0" : ":") + var0 + (var1 < 10 ? ":0" : ":") + var1;
   }

   private String i(int var1, int var2) {
      Object[] var3 = new Object[]{new Integer(var2)};
      return a(ratchetandclank.e[var1], var3);
   }

   private static String a(String var0, Object[] var1) {
      int var4 = 0;
      boolean var6 = false;
      StringBuffer var8 = new StringBuffer(var0.length());
      char[] var9 = new char[var0.length()];
      var0.getChars(0, var0.length(), var9, 0);

      for (int var10 = 0; var10 < var9.length; var10++) {
         char var2;
         char var3;
         int var5;
         if ((var2 = var9[var10]) == '%' && (var5 = var10 + 1) < var9.length && (var6 = Character.isDigit(var3 = var9[var5]))) {
            var4 = Character.digit(var3, 10);
         }

         if (var6) {
            var6 = false;
            var8.append(var1[var4]);
            var10++;
         } else {
            var8.append(var2);
         }
      }

      return var8.toString();
   }

   private static int a(Graphics var0, String var1, int var2, int var3, int var4) {
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      int var10 = a.o;
      char[] var12 = new char[var1.length()];
      var1.getChars(0, var1.length(), var12, 0);
      var5 = (var4 & 1) > 0 ? (var2 = 0) : var2;
      int var8 = 0;
      int var9 = 0;
      char var11 = '\u0000';

      do {
         var6 = 0;
         var7 = var9;

         while (var9 < var12.length) {
            if ((var11 = var12[var9]) == ' ') {
               var6 += a.a(var11);
               var9++;
               break;
            }

            var6 += a.a(var11);
            var9++;
         }

         if (var5 + var6 <= 128 && var9 != var12.length) {
            var5 += var6;
         } else {
            if (var5 + var6 > 128) {
               var9 = var7;
            }

            if (var9 - var8 > 0) {
               ratchetandclank var10000;
               Graphics var10001;
               char[] var10002;
               int var10003;
               int var10004;
               int var10005;
               if ((var4 & 1) > 0) {
                  var10000 = a;
                  var10001 = var0;
                  var10002 = var12;
                  var10003 = var8;
                  var10004 = var9 - var8;
                  var10005 = 64;
               } else {
                  var10000 = a;
                  var10001 = var0;
                  var10002 = var12;
                  var10003 = var8;
                  var10004 = var9 - var8;
                  var10005 = var2;
               }

               var10000.a(var10001, var10002, var10003, var10004, var10005, var3, var4);
            }

            var3 += var10;
            var5 = var2;
            var8 = var9;
         }
      } while (var9 < var12.length);

      return var3;
   }

   private int O() {
      int var1 = 0;
      int[] var2 = new int[8];

      for (int var3 = 0; var3 < 8; var3++) {
         var2[var3] = a.d[var3 * 3 + this.X.N[var3]] - this.X.K[var3];
         int[] var10000;
         int var10001;
         int var10002;
         switch (var3) {
            case 0:
            case 6:
               var10000 = this.at;
               var10001 = var3;
               var10002 = 0;
               break;
            case 1:
               var1 += var2[var3] >> 1;
               var10000 = this.at;
               var10001 = var3;
               var10002 = var2[var3] >> 1;
               break;
            case 2:
               if ((this.X.M & 1 << var3) <= 0) {
                  continue;
               }

               var1 += var2[var3] >> 1;
               var10000 = this.at;
               var10001 = var3;
               var10002 = var2[var3] >> 1;
               break;
            case 3:
            case 4:
            case 5:
            case 7:
               if ((this.X.M & 1 << var3) > 0) {
                  var1 += var2[var3];
                  var10000 = this.at;
                  var10001 = var3;
                  var10002 = var2[var3];
                  break;
               }
            default:
               continue;
         }

         var10000[var10001] = var10002;
      }

      return var1;
   }
}
