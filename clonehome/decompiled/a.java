import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;

public abstract class a extends GameCanvas implements Runnable {
   private long k;
   public MIDlet a;
   public e b;
   private boolean l;
   public boolean c;
   private boolean m;
   private boolean n;
   private int o = -1;
   private int[] p;
   private byte[] q;
   public byte[] d;
   private int r;
   private byte[] s;
   private short[] t;
   private f[] u;
   private char[] v;
   private Font w = Font.getFont(0, 0, 0);
   private Graphics x;
   private int y;
   private int z;
   private int A;
   private int B;
   private Image C;
   private int[] D;
   private int[] E;
   private short[] F;
   private int[] G;
   private int H;
   private int I;
   private int J;
   private int K;
   private boolean L;
   private short[] M;
   private short[] N;
   private short[] O;
   private short[] P;
   private int Q;
   private short[] R;
   private int S;
   private short[] T;
   private int U;
   private byte[] V;
   private int W;
   private byte[] X;
   private int Y;
   private short[] Z;
   private int aa;
   private static final byte[] ab = new byte[32];
   private volatile int ac;
   public volatile int e;
   public volatile int f;
   private volatile int ad;
   private volatile int ae;
   private volatile int af;
   public int g = 10;
   public int h = 10;
   public int i = 100;
   private boolean ag;
   private int ah;
   private int ai;
   private int aj;
   private int ak;
   public boolean j;
   private boolean al;
   private Thread am;
   private volatile boolean an;
   private volatile boolean ao;
   private volatile long ap;
   private volatile int aq = -1;
   private int ar = 2;

   static {
      System.currentTimeMillis();
      ab[1] = -1;
      ab[2] = -3;
      ab[3] = 35;
      ab[4] = -8;
      ab[5] = -4;
      ab[6] = -2;
      ab[8] = -5;
      ab[10] = 42;
      ab[13] = -36;
      ab[14] = -37;
      ab[15] = -11;

      for (int var0 = 0; var0 < 10; var0++) {
         ab[16 + var0] = (byte)(48 + var0);
      }

      ab[27] = -6;
      ab[29] = -7;
   }

   public abstract void a(Graphics var1);

   public abstract void a();

   public static byte[] a(int var0) {
      RecordStore var2 = null;

      byte[] var1;
      try {
         int var3 = (var2 = RecordStore.openRecordStore(String.valueOf(var0), false)).getNumRecords();
         int var4 = 0;

         for (int var5 = 1; var5 <= var3; var5++) {
            var4 += var2.getRecordSize(var5);
         }

         var1 = new byte[var4];
         var4 = 0;

         for (int var9 = 1; var9 <= var3; var9++) {
            var4 += var2.getRecord(var9, var1, var4);
         }
      } catch (Throwable var7) {
         var1 = (byte[])null;
      }

      if (var2 != null) {
         try {
            var2.closeRecordStore();
         } catch (Throwable var6) {
         }
      }

      return var1;
   }

   public static boolean a(int var0, byte[] var1) {
      String var2 = String.valueOf(var0);
      RecordStore var3 = null;
      boolean var4 = true;

      try {
         String[] var5;
         if ((var5 = RecordStore.listRecordStores()) != null) {
            for (int var6 = 0; var6 < var5.length; var6++) {
               if (var2.equals(var5[var6])) {
                  RecordStore.deleteRecordStore(var2);
                  break;
               }
            }
         }

         if (var1 != null) {
            var3 = RecordStore.openRecordStore(var2, true);
            int var12 = (var1.length + 786432 - 1) / 786432;
            int var7 = 0;

            for (int var8 = 1; var8 <= var12; var8++) {
               int var9;
               if ((var9 = var1.length - var7) > 786432) {
                  var9 = 786432;
               }

               var3.addRecord(var1, var7, var9);
               var7 += var9;
            }
         }
      } catch (Throwable var11) {
         var4 = false;
      }

      if (var3 != null) {
         try {
            var3.closeRecordStore();
         } catch (Throwable var10) {
         }
      }

      return var4;
   }

   public final synchronized void a(boolean var1) {
      if (var1 || !this.l) {
         if (var1) {
            byte[] var3;
            (var3 = new byte[3])[0] = (byte)(this.c ? 1 : 0);
            var3[1] = (byte)(this.m ? 1 : 0);
            var3[2] = (byte)(this.n ? 1 : 0);
            a(-9, var3);
            return;
         }

         this.l = true;
         byte[] var2;
         if ((var2 = a(-9)) == null || var2.length != 3) {
            var2 = new byte[]{-1, -1, -1};
            a(-9, var2);
         }

         this.c = var2[0] != 0;
         this.m = var2[1] != 0;
         this.n = var2[2] != 0;
      }
   }

   public final f[] b(int var1) {
      return this.b(var1, null);
   }

   public final f[] b(int var1, byte[] var2) {
      try {
         DataInputStream var6;
         int var3 = (var6 = this.e(var1)).readChar();
         var6.readByte();
         int var4 = 0;
         byte[] var7 = new byte[var6.readShort() - 4];
         int var5 = var6.readChar();
         char var8 = var6.readChar();
         var6.readFully(var7);
         boolean[] var9 = new boolean[16];

         for (int var18 = 0; var18 < 16; var18++) {
            var9[var18] = (var5 >> var18 & 1) == 0;
         }

         var5 = 5;
         var4 = 0;
         int[] var10 = new int[10];

         for (int var11 = 0; var11 < 3; var11++) {
            for (int var12 = 0; var12 < (var11 == 0 ? 2 : 4); var12++) {
               var10[var4++] = var9[var11] ? (var9[var5++] ? 2 : 1) : 0;
            }
         }

         Image var24 = this.c(var3, var2);
         short[] var25 = new short[10];
         f[] var13 = new f[var8];

         for (int var14 = 0; var14 < var8; var14++) {
            var13[var14] = new f(var24);
            var5 = 0;

            for (int var15 = 0; var15 < var25.length; var15++) {
               var4 = 0;
               if ((var3 = var10[var15] - 1) >= 0) {
                  var5 += var14 << var3;
                  if (var3 == 0) {
                     var4 = var7[var5];
                     if (var15 > 1 && var15 != 6 && var15 != 7) {
                        var4 &= 255;
                     }
                  } else {
                     var4 = (var7[var5] & 255) << 8 | var7[var5 + 1] & 255;
                  }

                  var5 += var8 - var14 << var3;
               }

               var25[var15] = (short)var4;
            }

            var13[var14].a(var25);
         }

         return var13;
      } catch (Exception var16) {
         return null;
      }
   }

   public final Image c(int var1) {
      return this.c(var1, null);
   }

   public final Image c(int var1, byte[] var2) {
      Image var3 = null;
      this.m(var1);
      var1 &= 1023;
      int var4 = this.l(var1);
      this.a(this.q, var4, var2);
      var3 = Image.createImage(this.q, var4, this.p[var1]);
      this.a(this.q, var4, var2);
      return var3;
   }

   private void a(byte[] var1, int var2, byte[] var3) {
      if (var3 != null) {
         DataInputStream var4 = new DataInputStream(new ByteArrayInputStream(var1, var2, var1.length - var2));

         try {
            this.b(var4);
            int var5 = 0;
            var2 += this.r;

            while (var5 < var3.length) {
               byte var6 = var1[var2];
               var1[var2++] = var3[var5];
               var3[var5++] = var6;
            }

            return;
         } catch (IOException var7) {
         }
      }
   }

   private int b(DataInputStream var1) throws IOException {
      var1.skip(8L);
      this.r = 8;

      while (true) {
         int var2 = var1.readInt();
         int var3 = var1.readInt();
         this.r += 8;
         if (1347179589 == var3) {
            return var2;
         }

         if (1229278788 == var3) {
            return -1;
         }

         var1.skip(var2 + 4);
         this.r += var2 + 4;
      }
   }

   public final byte[] d(int var1) {
      this.m(var1);
      var1 &= 1023;
      byte[] var2 = new byte[this.p[var1]];
      System.arraycopy(this.q, this.l(var1), var2, 0, var2.length);
      return var2;
   }

   public final DataInputStream e(int var1) {
      this.m(var1);
      var1 &= 1023;
      return new DataInputStream(new ByteArrayInputStream(this.q, this.l(var1), this.p[var1]));
   }

   private int l(int var1) {
      int var2 = 0;

      while (--var1 >= 0) {
         var2 += this.p[var1];
      }

      return var2;
   }

   private void m(int var1) {
      if ((var1 = var1 >> 10) != 0 && var1 != this.o) {
         if (var1 > 0) {
            try {
               DataInputStream var2 = new DataInputStream(this.getClass().getResourceAsStream("/RP" + var1));
               this.a(var2);
               var2.close();
            } catch (Exception var3) {
            }
         } else {
            this.a((DataInputStream)null);
         }

         this.o = var1;
      }
   }

   public final boolean a(DataInputStream var1) {
      this.p = null;
      this.d = null;
      this.q = null;
      if (var1 == null) {
         this.o = -1;
         return false;
      }

      this.o = 0;

      try {
         int var4 = var1.read();
         int var2 = 1 + (var4 & 1) << 1;
         char var9 = var1.readChar();
         byte[] var5 = new byte[var2 * var9];
         var1.readFully(var5);
         this.p = new int[var9];
         int var3 = 0;

         for (int var6 = 0; var6 < var9; var6++) {
            for (int var7 = var2 - 1; var7 >= 0; var7--) {
               this.p[var6] = this.p[var6] | (255 & var5[var3++]) << (var7 << 3);
            }
         }

         this.d = new byte[var9];
         var1.readFully(this.d);
         this.q = new byte[var1.readInt()];
         var1.readFully(this.q);
         return true;
      } catch (Exception var8) {
         return false;
      }
   }

   public final String f(int var1) {
      short var2 = this.t[var1];

      try {
         return new String(this.s, var2, this.t[var1 + 1] - var2, "UTF-8");
      } catch (Exception var3) {
         return null;
      }
   }

   public final void g(int var1) {
      if (var1 == -1) {
         this.s = null;
         this.t = null;
      } else {
         try {
            DataInputStream var2 = this.e(var1);
            this.t = new short[var2.readShort() + 1];
            int var3 = this.p[var1 & 1023];
            this.s = new byte[var3 - (this.t.length << 1)];
            short var4 = 0;

            int var5;
            for (var5 = 0; var5 < this.t.length - 1; var5++) {
               this.t[var5] = var4;
               short var7;
               if ((var7 = var2.readShort()) > 0) {
                  var2.readFully(this.s, var4, var7);
               }

               var4 += var7;
            }

            this.t[var5] = var4;
         } catch (Exception var6) {
         }
      }
   }

   public final void a(f[] var1) {
      this.u = var1;
      if (this.u != null && var1[3].h == 255) {
         var1[3].h = 0;
         short var2 = 0;

         for (int var3 = 0; var3 < var1.length; var3++) {
            f var4 = var1[var3];
            var2 = (short)(var2 + (var4.a << 8) + (var4.b & 255));
            var4.a = var4.e;
            var4.b = var4.f;
            var4.e = var2;
         }
      }
   }

   private void n(int var1) {
      if (this.v == null || this.v.length < var1) {
         this.v = new char[var1];
      }
   }

   public final int a(String var1) {
      return this.a(var1, 0, var1.length());
   }

   public final int a(String var1, int var2, int var3) {
      this.n(var3);

      for (int var4 = 0; var4 < var3; var4++) {
         this.v[var4] = var1.charAt(var2 + var4);
      }

      return this.a(this.v, 0, var3);
   }

   public final int a(char var1) {
      this.n(1);
      this.v[0] = var1;
      return this.a(this.v, 0, 1);
   }

   public final int a(char[] var1, int var2, int var3) {
      if (this.u != null) {
         var3 += var2;
         short var4 = 0;

         while (var2 < var3) {
            f var5 = this.b(var1[var2]);
            var4 += var5 != null ? var5.g : this.u[2].h;
            var2++;
         }

         return var4;
      } else {
         return var3 == 0 ? 0 : this.w.charsWidth(var1, var2, var3);
      }
   }

   private f b(char var1) {
      int var2 = 0;
      int var3 = this.u.length - 1;

      while (var2 <= var3) {
         int var4 = var2 + var3 >> 1;
         char var5;
         if ((var5 = (char)this.u[var4].e) < var1) {
            var2 = var4 + 1;
         } else {
            if (var5 <= var1) {
               return this.u[var4];
            }

            var3 = var4 - 1;
         }
      }

      return null;
   }

   public final int b() {
      return this.u != null ? this.u[1].h : this.w.getBaselinePosition();
   }

   public final int c() {
      return this.u != null ? this.u[0].h : this.w.getHeight();
   }

   public final void a(Graphics var1, String var2, int var3, int var4, int var5) {
      this.a(var1, var2, 0, var2.length(), var3, var4, var5);
   }

   public final void a(Graphics var1, String var2, int var3, int var4, int var5, int var6, int var7) {
      this.n(var4);

      for (int var8 = 0; var8 < var4; var8++) {
         this.v[var8] = var2.charAt(var3 + var8);
      }

      this.a(var1, this.v, 0, var4, var5, var6, var7);
   }

   public final void a(Graphics var1, char[] var2, int var3, int var4, int var5, int var6, int var7) {
      if ((var7 & 64) != 0) {
         var6 -= this.b();
      } else if ((var7 & 32) != 0) {
         var6 -= this.c();
      }

      if ((var7 & 9) != 0) {
         var5 -= this.a(var2, var3, var4) >> (var7 & 1);
      }

      if (this.u != null) {
         for (int var9 = var4 + var3; var3 < var9; var3++) {
            f var8;
            if ((var8 = this.b(var2[var3])) != null) {
               var8.a(var1, var5, var6, 0);
               var5 += var8.g;
            } else {
               var5 += this.u[2].h;
            }
         }
      } else {
         if (var4 != 0) {
            if (var1.getFont() != this.w) {
               var1.setFont(this.w);
            }

            var1.drawChars(var2, var3, var4, var5, var6, 20);
         }
      }
   }

   private void b(int var1, int var2, Image var3, int var4, int var5) {
      this.A = 0;
      this.B = 0;
      this.J = var1;
      this.K = var2;
      this.F = new short[var2 * var1];
      this.G = new int[0];
      this.D = null;
      this.a(var3, var4, var5);
   }

   private boolean b(Image var1, int var2, int var3) {
      this.H = var2;
      this.I = var3;
      var2 = var1.getWidth();
      var3 = var1.getHeight();
      int var4 = this.D == null ? 0 : this.D.length;
      int var5 = var2 / this.H * (var3 / this.I);
      if (var4 > var5) {
         this.a(0, 0, this.J, this.K, 0);
         this.G = new int[0];
      }

      var2 -= this.H;
      var3 -= this.I;
      this.C = var1;
      this.D = new int[var5];
      this.E = new int[var5];
      int var6 = 0;

      for (int var7 = 0; var7 <= var3; var7 += this.I) {
         for (int var8 = 0; var8 <= var2; var8 += this.H) {
            this.D[var6] = var8;
            this.E[var6] = var7;
            var6++;
         }
      }

      return var4 > var5;
   }

   private boolean f(int var1, int var2) {
      this.y = Math.max(0, var1);
      this.z = Math.max(0, var2);
      if (var1 >= 0 && var2 >= 0) {
         return false;
      }

      this.C = null;
      this.D = null;
      this.E = null;
      this.F = null;
      this.G = null;
      return true;
   }

   public final void a(int var1, int var2) {
      this.A = -var1;
      this.B = -var2;
   }

   private void a(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (!this.L) {
         this.b(var1, var2, var3, var4, var5, var6);
      }
   }

   private int o(int var1) {
      return var1 < 0 ? this.G[~var1] : var1;
   }

   private void b(int var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = var1 % this.H;
      int var8 = var2 % this.I;
      int var9 = var3;
      int var10 = var1 / this.H;
      int var11 = var2 / this.I;
      int var12;
      int var13 = var12 = var10 + var11 * this.J;
      int var14 = var6;
      int var15;
      if (var7 != 0) {
         if ((var15 = this.H - var7) > var5) {
            var15 = var5;
         }
      } else {
         var15 = 0;
      }

      if (var8 != 0) {
         int var16 = var5;
         int var17;
         if ((var17 = this.I - var8) > var6) {
            var17 = var6;
         }

         if (var7 != 0) {
            int var18 = this.F[var12];
            if ((var18 = this.o(var18)) > 0) {
               this.x.drawRegion(this.C, this.D[--var18] + var7, this.E[var18] + var8, var15, var17, 0, var3, var4, 20);
            }

            var12++;
            var16 -= var15;
            var3 += var15;
         }

         while (var16 >= this.H) {
            int var41 = this.F[var12];
            if ((var41 = this.o(var41)) > 0) {
               this.x.drawRegion(this.C, this.D[--var41], this.E[var41] + var8, this.H, var17, 0, var3, var4, 20);
            }

            var12++;
            var16 -= this.H;
            var3 += this.H;
         }

         if (var16 > 0) {
            int var44 = this.F[var12];
            if ((var44 = this.o(var44)) > 0) {
               this.x.drawRegion(this.C, this.D[--var44], this.E[var44] + var8, var16, var17, 0, var3, var4, 20);
            }
         }

         var14 -= var17;
         var12 = var13 += this.J;
         var3 = var9;
         var4 += var17;
      }

      while (var14 >= this.I) {
         int var19 = var5;
         if (var7 != 0) {
            int var21 = this.F[var12];
            if ((var21 = this.o(var21)) > 0) {
               this.x.drawRegion(this.C, this.D[--var21] + var7, this.E[var21], var15, this.I, 0, var3, var4, 20);
            }

            var12++;
            var19 -= var15;
            var3 += var15;
         }

         while (var19 >= this.H) {
            int var24 = this.F[var12];
            if ((var24 = this.o(var24)) > 0) {
               this.x.drawRegion(this.C, this.D[--var24], this.E[var24], this.H, this.I, 0, var3, var4, 20);
            }

            var12++;
            var19 -= this.H;
            var3 += this.H;
         }

         if (var19 > 0) {
            int var27 = this.F[var12];
            if ((var27 = this.o(var27)) > 0) {
               this.x.drawRegion(this.C, this.D[--var27], this.E[var27], var19, this.I, 0, var3, var4, 20);
            }
         }

         var12 = var13 += this.J;
         var3 = var9;
         var4 += this.I;
         var14 -= this.I;
      }

      if (var14 > 0) {
         int var20 = var5;
         if (var7 != 0) {
            int var30 = this.F[var12];
            if ((var30 = this.o(var30)) > 0) {
               this.x.drawRegion(this.C, this.D[--var30] + var7, this.E[var30], var15, var14, 0, var3, var4, 20);
            }

            var12++;
            var20 -= var15;
            var3 += var15;
         }

         while (var20 >= this.H) {
            int var33 = this.F[var12];
            if ((var33 = this.o(var33)) > 0) {
               this.x.drawRegion(this.C, this.D[--var33], this.E[var33], this.H, var14, 0, var3, var4, 20);
            }

            var12++;
            var20 -= this.H;
            var3 += this.H;
         }

         if (var20 > 0) {
            int var36 = this.F[var12];
            if ((var36 = this.o(var36)) > 0) {
               this.x.drawRegion(this.C, this.D[--var36], this.E[var36], var20, var14, 0, var3, var4, 20);
            }
         }
      }
   }

   public final void a(int var1, int var2, Image var3, int var4, int var5) {
      this.b(var1, var2, var3, var4, var5);
   }

   public final void a(Image var1, int var2, int var3) {
      this.b(var1, var2, var3);
   }

   public final void a(int var1, int var2, int var3, int var4, int var5) {
      int var6 = var2 * this.J + var1;

      for (int var7 = var2; var7 < var2 + var4; var6 += this.J) {
         int var8 = var6;

         for (int var9 = var1; var9 < var1 + var3; var8++) {
            this.F[var8] = (short)var5;
            var9++;
         }

         var7++;
      }
   }

   public final void b(int var1, int var2) {
      this.f(var1, var2);
   }

   public final void a(int var1, int var2, int var3) {
      this.F[var2 * this.J + var1] = (short)var3;
   }

   public final int h(int var1) {
      int[] var2 = this.G;
      this.G = new int[var2.length + 1];
      System.arraycopy(var2, 0, this.G, 0, var2.length);
      this.G[var2.length] = var1;
      return -this.G.length;
   }

   public final void c(int var1, int var2) {
      this.G[~var1] = var2;
   }

   public final void b(Graphics var1) {
      int var2;
      if (this.A < 0) {
         var2 = this.A;
      } else if ((var2 = this.A + this.y - this.J * this.H) < 0) {
         var2 = 0;
      }

      int var3;
      if (this.B < 0) {
         var3 = this.B;
      } else if ((var3 = this.B + this.z - this.K * this.I) < 0) {
         var3 = 0;
      }

      if (var2 != 0 || var3 != 0) {
         this.A -= var2;
         this.B -= var3;
         var1.translate(-var2, -var3);
      }

      int var5 = this.A;
      int var6 = this.B;
      int var7 = 0;
      int var8 = 0;
      int var9 = this.y;
      int var10 = this.z;
      int var4;
      if ((var4 = var1.getClipX()) > 0) {
         var5 += var4;
         var9 -= var4;
         var7 = var4;
      }

      if ((var4 = var7 + var9 - (var4 + var1.getClipWidth())) > 0) {
         var9 -= var4;
      }

      if ((var4 = var1.getClipY()) > 0) {
         var6 += var4;
         var10 -= var4;
         var8 = var4;
      }

      if ((var4 = var8 + var10 - (var4 + var1.getClipHeight())) > 0) {
         var10 -= var4;
      }

      if (var9 > 0 && var10 > 0) {
         this.x = var1;
         this.a(var5, var6, var7, var8, var9, var10);
         this.x = null;
      }

      if (var2 != 0 || var3 != 0) {
         this.A += var2;
         this.B += var3;
         var1.translate(var2, var3);
      }
   }

   public final void i(int var1) {
      try {
         DataInputStream var2 = this.e(var1);
         this.N = a(this.N, this.Q + 1);
         this.O = a(this.O, this.Q + 1);
         this.P = a(this.P, this.Q + 1);
         this.N[this.Q] = (short)var1;
         this.O[this.Q] = (short)this.S;
         this.P[this.Q] = (short)this.U;
         this.Q++;
         int var3 = var2.readChar() & 32767;
         char var4 = var2.readChar();
         this.V = a(this.V, this.W + var2.readChar());
         this.X = a(this.X, this.Y + var2.readChar());
         this.R = a(this.R, this.S + var3);
         this.T = a(this.T, this.U + var4);
         var2.readChar();
         int var5 = 0;
         int var6 = 0;
         byte[][] var7 = new byte[11][];

         for (int var8 = 0; var8 < 11; var8++) {
            boolean var9 = var8 != 8;
            int var10 = var3;
            if (var8 > 1) {
               var10 = var5;
            }

            if (var8 > 6) {
               var10 = var4;
            }

            if (var8 > 7) {
               var10 = var6;
            }

            byte[] var11 = new byte[var10];
            int var12 = 0;

            for (int var13 = 0; var13 < var10; var13++) {
               short var14;
               if ((var14 = var2.readByte()) == -128 && var9) {
                  var14 = var2.readShort();
               }

               int var15 = var12 + var14;
               if (var9) {
                  var12 = var15;
               }

               if (var8 == 1) {
                  var5 += var15;
               }

               if (var8 == 7) {
                  var6 += var15;
               }

               if (var8 == 2) {
                  int var16 = 0;

                  while (var16 < this.aa && this.Z[var16] != var15) {
                     var16++;
                  }

                  if (var16 == this.aa) {
                     this.Z = a(this.Z, this.aa + 1);
                     this.Z[this.aa++] = (short)var15;
                  }

                  var15 = var16;
               }

               var11[var13] = (byte)var15;
            }

            var7[var8] = var11;
         }

         var5 = 0;
         var6 = 0;

         for (int var20 = 0; var20 < var3; var20++) {
            this.R[this.S++] = (short)this.W;
            int var22 = var7[1][var20] & 255;
            this.V[this.W++] = var7[0][var20];

            for (this.V[this.W++] = (byte)var22; var22-- > 0; var5++) {
               for (int var24 = 2; var24 < 7; var24++) {
                  this.V[this.W++] = var7[var24][var5];
               }
            }
         }

         for (int var21 = 0; var21 < var4; var21++) {
            this.T[this.U++] = (short)this.Y;
            int var23 = var7[7][var21] & 255;

            for (this.X[this.Y++] = (byte)var23; var23-- > 0; var6++) {
               for (int var25 = 8; var25 < 11; var25++) {
                  this.X[this.Y++] = var7[var25][var6];
               }
            }
         }
      } catch (Exception var17) {
      }
   }

   public final void b(int var1, int var2, int var3) {
      short var4 = 0;

      while (var4 < this.Q && var2 != this.N[var4]) {
         var4++;
      }

      int var5 = this.O[var4] + var3;
      int var6 = var1 * 8;
      this.M = a(this.M, var6 + 8);
      short var7 = this.R[var5];
      this.M[var6 + 0] = 0;
      this.M[var6 + 1] = 0;
      this.M[var6 + 2] = this.V[var7 + 0];
      this.M[var6 + 3] = 0;
      this.M[var6 + 4] = 0;
      this.M[var6 + 5] = (short)(this.V[var7 + 2 + 4] & 0xFF);
      this.M[var6 + 6] = var7;
      this.M[var6 + 7] = var4;
   }

   public final boolean d(int var1, int var2) {
      int var3 = var1 * 8;
      int var4;
      if ((var4 = this.M[var3 + 2]) == 0) {
         return false;
      }

      int var5 = (this.M[var3 + 0] & '\uffff') + var2;
      int var6 = this.M[var3 + 1];
      short var7 = this.M[var3 + 6];
      int var8 = this.V[var7 + 1] & 255;
      int var9 = var7 + 2;
      int var10 = var6;
      int var12 = 0;

      int var11;
      while (var5 > (var11 = this.Z[this.V[var9 + var6 * 5 + 0]] & '\uffff')) {
         var5 -= var11;
         if (++var6 == var8) {
            if (var4 > 0) {
               if (--var4 == 0) {
                  var6--;
                  var5 = var11;
                  break;
               }
            }

            var6 = 0;
         }

         int var13;
         if ((var13 = this.V[var9 + var6 * 5 + 3] & 63) >= var12) {
            var12 = var13;
            var10 = var6;
         }
      }

      int var22 = this.V[var9 + var10 * 5 + 4] & 255;
      var9 += var6 * 5;
      int var14 = this.V[var9 + 1];
      int var15 = this.V[var9 + 2];
      if (this.V[var9 + 3] >> 6 > 0) {
         var9 -= 5;
         byte var17 = 0;
         byte var18 = 0;
         if (var6 != 0) {
            var17 = this.V[var9 + 1];
            var18 = this.V[var9 + 2];
         }

         int var19 = (var5 << 12) / Math.max(1, var11);
         var14 = var17 + ((var14 - var17) * var19 >> 12);
         var15 = var18 + ((var15 - var18) * var19 >> 12);
      }

      this.M[var3 + 0] = (short)var5;
      this.M[var3 + 1] = (short)var6;
      this.M[var3 + 2] = (short)var4;
      this.M[var3 + 3] = (short)var14;
      this.M[var3 + 4] = (short)var15;
      this.M[var3 + 5] = (short)var22;
      return var4 != 0;
   }

   public final void a(f[] var1, int var2, int var3, int var4, int var5, int[] var6) {
      int var7 = Integer.MAX_VALUE;
      int var8 = Integer.MAX_VALUE;
      int var9 = Integer.MIN_VALUE;
      int var10 = Integer.MIN_VALUE;
      int var11 = this.T[this.P[this.M[var2 * 8 + 7]] + this.M[var2 * 8 + 5]];
      int var12 = this.X[var11++] & 255;

      while (var12-- > 0) {
         int var13 = this.X[var11++] & 255;
         byte var14 = this.X[var11++];
         byte var15 = this.X[var11++];
         f var16;
         int var17 = (var16 = var1[var13]).a + var14;
         if ((var5 & 2) != 0) {
            var17 = -var17 - var16.c;
         }

         int var18 = var16.b + var15;
         if ((var5 & 1) != 0) {
            var18 = -var18 - var16.d;
         }

         var7 = Math.min(var7, var17);
         var8 = Math.min(var8, var18);
         var9 = Math.max(var9, var17 + var16.c);
         var10 = Math.max(var10, var18 + var16.d);
      }

      var6[0] = var3 + var7;
      var6[1] = var4 + var8;
      var6[2] = var9 - var7;
      var6[3] = var10 - var8;
   }

   public final void a(Graphics var1, f[] var2, int var3, int var4, int var5, int var6) {
      int var7 = this.T[this.P[this.M[var3 * 8 + 7]] + this.M[var3 * 8 + 5]];
      int var8 = this.X[var7++] & 255;

      while (var8-- > 0) {
         int var9 = this.X[var7++] & 255;
         byte var10 = this.X[var7++];
         byte var11 = this.X[var7++];
         f var12;
         f var16 = var12 = var2[var9];
         var16.a = (short)(var16.a + var10);
         var12.b = (short)(var12.b + var11);
         var12.a(var1, var4, var5, var6);
         var12.a = (short)(var12.a - var10);
         var12.b = (short)(var12.b - var11);
      }
   }

   private static short[] a(short[] var0, int var1) {
      if (var0 == null) {
         return new short[(var1 << 1) + 16];
      }

      if (var0.length >= var1) {
         return var0;
      }

      short[] var3 = new short[(var1 * 9 >> 3) + 8];
      System.arraycopy(var0, 0, var3, 0, var0.length);
      return var3;
   }

   private static byte[] a(byte[] var0, int var1) {
      if (var0 == null) {
         return new byte[(var1 << 1) + 16];
      }

      if (var0.length >= var1) {
         return var0;
      }

      byte[] var3 = new byte[var1 * 9 >> 3];
      System.arraycopy(var0, 0, var3, 0, var0.length);
      return var3;
   }

   public final boolean a(byte var1) {
      int var2 = 1 << var1;
      return (this.ac & var2) != 0;
   }

   public final boolean b(byte var1) {
      int var2 = 1 << var1;
      return (this.e & var2) != 0;
   }

   public final boolean d() {
      return this.e != 0;
   }

   public final synchronized void e() {
      this.ad = 0;
      this.ae = 0;
      this.af = this.af | this.ac;
      this.e = 0;
      this.ac = 0;
      this.f = 0;
   }

   public final synchronized void f() {
      this.e = this.ad;
      this.ad = 0;
      this.af = this.af & this.ae;
      this.ae = this.ae & ~this.af;
      this.ac = this.e | this.ae;
      this.f = this.af;
      this.af = 0;
   }

   private void a(int var1, boolean var2) {
      if (var1 != 0) {
         synchronized (this) {
            for (int var4 = 1; var4 < ab.length; var4++) {
               if (ab[var4] == var1) {
                  int var5 = 1 << var4;
                  if (var2) {
                     this.ad |= var5;
                     this.ae |= var5;
                     this.af &= ~var5;
                  } else {
                     this.af |= var5;
                  }
               }
            }
         }
      }
   }

   public void keyPressed(int var1) {
      this.a(var1, true);
      super.keyPressed(var1);
   }

   public void keyReleased(int var1) {
      this.a(var1, false);
      super.keyReleased(var1);
   }

   public void keyRepeated(int var1) {
      super.keyRepeated(var1);
   }

   public final void g() {
      this.g = this.h;
      this.ag = true;
   }

   private void d(Graphics var1) {
      if (var1 != null) {
         this.j = this.al;
         this.al = false;
         if (240 != this.ah || 320 != this.ai) {
            if (this.j) {
               int var3 = 320 - this.ai + 1 >>> 1;
               var1.setColor(-16777216);
               if (var3 > 0) {
                  if (this.ak > 0) {
                     var1.fillRect(0, 0, 240, this.ak);
                  }

                  var1.fillRect(0, 320 - var3, 240, var3);
               }

               if ((var3 = 240 - this.ah + 1 >>> 1) > 0) {
                  if (this.aj > 0) {
                     var1.fillRect(0, this.ak, this.aj, this.ai);
                  }

                  var1.fillRect(240 - var3, this.ak, var3, this.ai);
               }
            }

            var1.translate(this.aj, this.ak);
         }

         this.c(var1);
         this.a(var1);
      }
   }

   public final void e(int var1, int var2) {
      this.al = true;
      if (var1 <= 0) {
         var1 = 240;
      }

      if (var2 <= 0) {
         var2 = 320;
      }

      this.ah = var1;
      this.ai = var2;
      this.aj = 240 - var1;
      this.ak = 320 - var2;
      if (this.aj < 0) {
         this.aj++;
      }

      if (this.ak < 0) {
         this.ak++;
      }

      this.aj >>= 1;
      this.ak >>= 1;
   }

   public final void c(Graphics var1) {
      var1.setClip(0, 0, this.ah, this.ai);
   }

   public final void h() {
      this.al = true;
   }

   public void j(int var1) {
      if (var1 == 3) {
         if (this.b != null) {
            this.b.a();
         }

         this.ao = true;
         this.a.notifyDestroyed();
      }

      if (var1 == 0 || var1 == 1 || var1 == 2) {
         this.al = true;
         this.ag = true;
         this.e();
      }
   }

   public final void hideNotify() {
      this.k(1);
   }

   public final void showNotify() {
      this.k(2);
      if (this.am == null) {
         this.am = new Thread(this);
         this.am.start();
      }
   }

   public final synchronized void k(int var1) {
      if (var1 == 3) {
         this.j(var1);
      } else if (this.an && (var1 == 1 || var1 == 2)) {
         synchronized (this.am) {
            this.aq = var1;
            this.ap = System.currentTimeMillis();
         }
      }
   }

   private void i() {
      while (this.aq != -1) {
         if (this.ar != 1) {
            if (this.b != null) {
               this.b.c();
            }

            this.j(1);
            this.ar = 1;
         }

         synchronized (this.am) {
            if (this.aq == 1) {
               this.aq = -1;
            }
         }

         while (this.aq == 2 && this.isShown()) {
            if ((int)(System.currentTimeMillis() - this.ap) < 750) {
               try {
                  Thread.sleep(250L);
                  Thread.yield();
               } catch (Exception var3) {
               }
            } else {
               synchronized (this.am) {
                  if (this.aq != 2) {
                     continue;
                  }

                  this.aq = -1;
               }

               if (this.b != null) {
                  this.b.c();
               }

               this.j(2);
               this.ar = 2;
               this.j(5);
            }
         }
      }
   }

   public final void run() {
      this.an = true;
      this.j(0);

      for (; !this.ao; Thread.yield()) {
         this.i();
         if (this.isShown()) {
            long var1 = System.currentTimeMillis();
            if (this.ag) {
               this.ag = false;
               this.g = this.h;
            } else {
               int var3;
               if ((var3 = (int)(var1 - this.k)) > this.i || var3 < 0) {
                  var3 = this.i;
               }

               this.g = var3;
               if (this.g < this.h) {
                  int var4;
                  if ((var4 = this.h - this.g) < 10) {
                     var4 = 10;
                  }

                  this.g = this.h;

                  try {
                     Thread.sleep(var4);
                  } catch (Exception var6) {
                  }
               }
            }

            this.k = var1;
            this.a();
            if (this.b != null) {
               this.b.run();
            }

            if (this.ao) {
               return;
            }

            if (this.isShown()) {
               this.d(this.getGraphics());
               this.flushGraphics();
            }
         } else {
            try {
               Thread.sleep(100L);
            } catch (Exception var5) {
            }

            this.ag = true;
         }
      }
   }

   public a(MIDlet var1) {
      super(false);
      this.setFullScreenMode(true);
      this.a = var1;
      this.e(240, 320);
   }
}
