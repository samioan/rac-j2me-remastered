import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public class RatchetMIDlet extends MIDlet {
   public static b a;
   private static boolean h;
   public static byte[] b;
   public static int[] c;
   public static final int[] d = new int[]{1, 2, 3, 4};
   public static byte[] e = new byte[218];
   public static byte[] f = new byte[3];
   public static boolean g = false;

   public RatchetMIDlet() {
      b = new byte[3];
      c = new int[3];
      a = new b(this);
      b.I = 99;
      String var1;
      if ((var1 = this.getAppProperty("Cheat-Mode")) != null) {
         b.E = var1.equals("true");
      }
   }

   public static void a() {
      for (int var0 = 0; var0 < 3; var0++) {
         e = a.a(d[var0]);
         if (e == null) {
            e = new byte[218];
         }

         b[var0] = e[0];
         c[var0] = a(e, 195);
      }
   }

   public static void a(int var0, byte[] var1, int var2) {
      var1[var2++] = (byte)(var0 >> 24 & 0xFF);
      var1[var2++] = (byte)(var0 >> 16 & 0xFF);
      var1[var2++] = (byte)(var0 >> 8 & 0xFF);
      var1[var2] = (byte)(var0 & 0xFF);
   }

   public static int a(byte[] var0, int var1) {
      return ((var0[0 + var1] & 0xFF) << 24) + ((var0[1 + var1] & 0xFF) << 16) + ((var0[2 + var1] & 0xFF) << 8) + (var0[3 + var1] & 0xFF);
   }

   public static void a(short var0, byte[] var1, int var2) {
      var1[var2++] = (byte)(var0 >> 8);
      var1[var2] = (byte)var0;
   }

   public static short b(byte[] var0, int var1) {
      return (short)(((var0[var1] & 255) << 8) + (var0[var1 + 1] & 255));
   }

   public final void a(int var1) {
      if (b.da != 0) {
         a(e);
         a.a(d[var1], e);
      }
   }

   public final void b(int var1) {
      e = a.a(d[var1]);
      b(e);
   }

   public static void c(int var0) {
      for (int var1 = 0; var1 < 218; var1++) {
         e[var1] = 0;
      }

      a.a(d[var0], e);
   }

   public static boolean b() {
      if (!g) {
         f = a.a(d[3]);
         g = true;
      }

      return f != null ? f[1] != 0 : false;
   }

   public static void c() {
      byte[] var0;
      if ((var0 = a.a(d[3])) != null) {
         f = var0;
      }

      f[1] = 1;
      if (!a.a(d[3], f)) {
         a.h();
         b.I = 105;
      }
   }

   public void pauseApp() {
      a.hideNotify();
   }

   public void startApp() {
      a.i();
      if (!h) {
         h = true;
         Display.getDisplay(this).setCurrent(a);
      }
   }

   public void destroyApp(boolean var1) {
      switch (b.I) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
         case 8:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 18:
         case 19:
            if (b.aw != 0 && b.ax >= 0 && b.ax < 3) {
               this.a(b.ax);
            }
         case 5:
         case 6:
         case 7:
         case 9:
         case 10:
         case 16:
         case 17:
         default:
            a.j(3);
      }
   }

   public static void a(byte[] var0) {
      var0[0] = 1;
      a(b.da, var0, 1);
      a(b.cm, var0, 5);
      a(b.cY, var0, 9);
      var0[13] = (byte)b.fU;

      for (int var1 = 0; var1 < 8; var1++) {
         var0[14 + var1] = b.fV[var1];
         a(b.fT[var1], var0, 22 + var1 * 2);
         a(b.fS[var1], var0, 38 + var1 * 2);
      }

      a(b.bp, var0, 54);
      var0[58] = (byte)b.bN;
      a(b.bO, var0, 59);
      a(b.bP, var0, 63);
      a(b.bQ, var0, 67);

      for (int var2 = 0; var2 < 10; var2++) {
         var0[71 + var2] = b.ca[0][var2];
         var0[91 + var2] = b.cb[0][var2];
         var0[111 + var2] = b.cc[0][var2];
         var0[131 + var2] = b.cd[0][var2];
         var0[151 + var2] = b.ce[0][var2];
         var0[171 + var2] = b.cf[0][var2];
         var0[71 + var2 + 10] = b.ca[1][var2];
         var0[91 + var2 + 10] = b.cb[1][var2];
         var0[111 + var2 + 10] = b.cc[1][var2];
         var0[131 + var2 + 10] = b.cd[1][var2];
         var0[151 + var2 + 10] = b.ce[1][var2];
         var0[171 + var2 + 10] = b.cf[1][var2];
      }

      a(b.cg, var0, 191);
      a(b.cC, var0, 195);
      var0[199] = (byte)(b.cq ? 1 : 0);
      a(b.dv, var0, 200);
      a(b.cZ, var0, 204);
      a(b.dN, var0, 208);
      var0[216] = (byte)(b.az ? 1 : 0);
      var0[217] = (byte)b.ax;
   }

   public static void b(byte[] var0) {
      b.da = a(var0, 1);
      b.cm = a(var0, 5);
      b.cY = a(var0, 9);
      b.fU = var0[13];

      for (int var1 = 0; var1 < 8; var1++) {
         b.fV[var1] = var0[14 + var1];
         b.fT[var1] = b(var0, 22 + var1 * 2);
         b.fS[var1] = b(var0, 38 + var1 * 2);
      }

      b.bp = a(var0, 54);
      b.bN = var0[58];
      b.bO = a(var0, 59);
      b.bP = a(var0, 63);
      b.bQ = a(var0, 67);

      for (int var2 = 0; var2 < 10; var2++) {
         b.ca[0][var2] = var0[71 + var2];
         b.cb[0][var2] = var0[91 + var2];
         b.cc[0][var2] = var0[111 + var2];
         b.cd[0][var2] = var0[131 + var2];
         b.ce[0][var2] = var0[151 + var2];
         b.cf[0][var2] = var0[171 + var2];
         b.ca[1][var2] = var0[71 + var2 + 10];
         b.cb[1][var2] = var0[91 + var2 + 10];
         b.cc[1][var2] = var0[111 + var2 + 10];
         b.cd[1][var2] = var0[131 + var2 + 10];
         b.ce[1][var2] = var0[151 + var2 + 10];
         b.cf[1][var2] = var0[171 + var2 + 10];
      }

      b.cg = a(var0, 191);
      b.cC = a(var0, 195);
      b.cq = var0[199] == 1;
      b.dv = a(var0, 200);
      b.cZ = a(var0, 204);
      b.dN = a(var0, 208);
      b.az = var0[216] != 0;
      b.ax = var0[217];
      b.cD = 0;
      b.cE = 0;
      b.j();
      b.I = 3;
   }

   public static String a(String var0, int var1, String var2) {
      int var5 = 0;
      boolean var7 = false;
      StringBuffer var8 = new StringBuffer(var0.length());
      char[] var9 = new char[var0.length()];
      var0.getChars(0, var0.length(), var9, 0);

      for (int var10 = 0; var10 < var9.length; var10++) {
         char var3;
         char var4;
         int var6;
         if ((var3 = var9[var10]) == '%' && (var6 = var10 + 1) < var9.length && (var7 = Character.isDigit(var4 = var9[var6]))) {
            var5 = Character.digit(var4, 10);
         }

         if (var7) {
            var7 = false;
            if (var5 == 0) {
               var8.append(var1);
            } else {
               var8.append(var2);
            }

            var10++;
         } else {
            var8.append(var3);
         }
      }

      return var8.toString();
   }
}
