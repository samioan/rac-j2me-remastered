import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStore;

public class ratchetandclank extends MIDlet {
   public boolean a = false;
   public static f b;
   public byte[] c;
   public int[] d;
   public static String[] e;
   public b f;
   public boolean g;
   public byte[] h;
   public static Vector i;
   public static int j;
   public static int k;
   public static int l;
   public static int m;
   public static StringBuffer n;
   private short[][] p;
   public int o;
   private int q;

   public void startApp() throws MIDletStateChangeException {
      if (!this.a) {
         this.a = true;

         try {
            f.f = Integer.parseInt(this.getAppProperty("BOSS_HIT_POINT_SHELL"));
         } catch (Exception var4) {
         }

         try {
            f.g = Integer.parseInt(this.getAppProperty("BOSS_HIT_POINT_KERNEL"));
         } catch (Exception var3) {
         }

         try {
            f.h = Byte.parseByte(this.getAppProperty("BOSS_FIRE_RATE"));
         } catch (Exception var2) {
         }

         Display.getDisplay(this);
         b = new f(this);
         b.c();
      } else {
         b.b();
      }
   }

   public void pauseApp() {
      b.a();
   }

   public final void a() {
      if (this.f != null) {
         this.f.a();
         this.f.a = null;
      }

      try {
         this.destroyApp(true);
      } catch (Exception var2) {
      }

      this.notifyDestroyed();
   }

   public void destroyApp(boolean var1) throws MIDletStateChangeException {
      b = null;
   }

   public final void b() {
      for (int var1 = 0; var1 < 3; var1++) {
         this.c[var1] = this.h[220 * var1 + 0];
         this.d[var1] = a(this.h, 220 * var1 + 195);
      }
   }

   public static void a(int var0, byte[] var1, int var2) {
      var1[0 + var2] = (byte)(var0 >> 24 & 0xFF);
      var1[1 + var2] = (byte)(var0 >> 16 & 0xFF);
      var1[2 + var2] = (byte)(var0 >> 8 & 0xFF);
      var1[3 + var2] = (byte)(var0 & 0xFF);
   }

   public static int a(byte[] var0, int var1) {
      return (var0[var1] & 0xFF) << 24 | (var0[1 + var1] & 0xFF) << 16 | (var0[2 + var1] & 0xFF) << 8 | var0[3 + var1] & 0xFF;
   }

   public static void a(short var0, byte[] var1, int var2) {
      var1[var2++] = (byte)(var0 >> 8);
      var1[var2] = (byte)var0;
   }

   public static short b(byte[] var0, int var1) {
      return (short)((var0[var1] & 255) << 8 | var0[1 + var1] & 0xFF);
   }

   public final void c() {
      RecordStore var1 = null;

      try {
         var1 = RecordStore.openRecordStore("RANDCSm", false);
         this.h = var1.getRecord(1);

         try {
            var1.closeRecordStore();
         } catch (Exception var5) {
         }

         if (this.h.length != 663) {
            this.d();
         }
      } catch (Exception var6) {
         try {
            var1.closeRecordStore();
         } catch (Exception var4) {
         }

         this.d();
      }
   }

   public final void d() {
      try {
         RecordStore.deleteRecordStore("RANDCSm");
      } catch (Exception var5) {
      }

      this.h = new byte[663];
      this.h[660] = 1;
      this.h[661] = 0;
      this.h[662] = -1;
      RecordStore var1 = null;

      try {
         (var1 = RecordStore.openRecordStore("RANDCSm", true)).addRecord(this.h, 0, this.h.length);

         try {
            var1.closeRecordStore();
         } catch (Exception var4) {
            return;
         }
      } catch (Exception var7) {
         try {
            var1.closeRecordStore();
            return;
         } catch (Exception var6) {
         }
      }
   }

   public final void e() {
      RecordStore var1 = null;

      try {
         (var1 = RecordStore.openRecordStore("RANDCSm", false)).setRecord(1, this.h, 0, this.h.length);

         try {
            var1.closeRecordStore();
         } catch (Exception var4) {
            return;
         }
      } catch (Exception var6) {
         try {
            var1.closeRecordStore();
            return;
         } catch (Exception var5) {
         }
      }
   }

   public final void a(int var1) {
      try {
         b.a(this.h, 220 * var1);
         this.e();
      } catch (Exception var3) {
      }
   }

   public final void b(int var1) {
      try {
         b.b(this.h, 220 * var1);
      } catch (Exception var3) {
      }
   }

   public final void c(int var1) {
      for (int var2 = 0; var2 < 220; var2++) {
         this.h[220 * var1 + var2] = 0;
      }

      this.e();
   }

   public final byte d(int var1) {
      return this.h[660 + var1];
   }

   public final void a(byte var1, int var2) {
      this.h[660 + var2] = var1;
      this.e();
   }

   public static String[] a(String var0, int var1) {
      StringBuffer var5 = new StringBuffer();
      String[] var6 = new String[var1];

      try {
         InputStream var7;
         if ((var7 = "".getClass().getResourceAsStream(var0 + ".txt")) == null) {
            return null;
         }

         for (int var2 = 0; var2 < var1; var2++) {
            var5.setLength(0);

            int var3;
            while ((var3 = var7.read()) != -1) {
               char var4;
               if ((var4 = (char)var3) != '\r') {
                  if (var4 == '\n') {
                     var6[var2] = var5.toString().trim();
                     break;
                  }

                  var5.append(var4);
               }
            }
         }

         var7.close();
         System.gc();
      } catch (IOException var8) {
      }

      return var6;
   }

   public static void a(char var0, int var1) {
      label45: {
         if (var0 == ' ') {
            l = k;
            m = m + f.a.a(var0);
            n.append(var0);
         } else {
            if (var0 == '^') {
               l = k;
               return;
            }

            if (var0 == '\n') {
               break label45;
            }

            m = m + f.a.a(var0);
            n.append(var0);
         }

         k++;
      }

      if (m >= var1 || var0 == '\n') {
         if (m >= var1) {
            k = l;
         }

         Vector var10000;
         String var10001;
         if (m == 0) {
            var10000 = i;
            var10001 = new String("");
         } else {
            char[] var2 = new char[k - j];
            n.getChars(j, k, var2, 0);
            var10000 = i;
            var10001 = new String(var2);
         }

         var10000.addElement(var10001);
         int var3;
         if (m >= var1) {
            if (n.charAt(k) == ' ') {
               n.delete(j, k + 1);
            } else {
               n.delete(j, k);
            }

            j = 0;
            l = 0;
            k = n.length();
            var3 = f.a.a(n.toString());
         } else {
            n.setLength(0);
            j = 0;
            l = 0;
            k = 0;
            var3 = 0;
         }

         m = var3;
      }
   }

   public static Vector b(String var0, int var1) {
      m = 0;
      l = 0;
      k = 0;
      j = 0;
      i = new Vector();
      n = new StringBuffer();

      try {
         InputStream var4;
         if ((var4 = "".getClass().getResourceAsStream(var0 + f.B[f.C] + ".txt")) == null) {
            return null;
         }

         int var2;
         while ((var2 = var4.read()) != -1) {
            char var3;
            if ((var3 = (char)var2) != '\r') {
               a(var3, var1);
            }
         }

         var4.close();
         System.gc();
      } catch (IOException var5) {
      }

      Vector var6 = i;
      i = null;
      n = null;
      return var6;
   }

   public static Vector c(String var0, int var1) {
      m = 0;
      l = 0;
      k = 0;
      j = 0;
      i = new Vector();
      n = new StringBuffer();

      for (int var3 = 0; var3 <= var0.length(); var3++) {
         char var2;
         if (var3 >= var0.length() || (var2 = var0.charAt(var3)) == 166) {
            var2 = '\n';
         }

         a(var2, var1);
      }

      Vector var4 = i;
      i = null;
      n = null;
      return var4;
   }

   public final void a(String var1, int var2, int var3) throws IOException {
      byte[] var4 = new byte[2048];
      InputStream var5;
      (var5 = System.out.getClass().getResourceAsStream(var1)).read(var4);
      var5.close();
      this.p = new short[192][];
      int var7 = 0;

      for (int var8 = 0; var8 < this.p.length; var8++) {
         byte var6 = var4[var7++];
         this.p[var8] = new short[(var6 & 15) + 1];
         this.p[var8][0] = (short)((var6 & 240) >> 4);

         for (int var9 = 0; var9 < (var6 & 15); var9++) {
            this.p[var8][var9 + 1] = (short)((var4[var7++] & 255) << 8 | var4[var7++] & 0xFF);
         }

         this.o = var2;
         this.q = var3;
      }
   }

   private static int a(char[] var0, String var1, int var2) {
      label30: {
         char var10000;
         if (var0 != null) {
            var10000 = var0[var2];
         } else {
            if (var1 == null) {
               break label30;
            }

            var10000 = var1.charAt(var2);
         }

         var2 = var10000;
      }

      if (var2 < 32) {
         return 0;
      } else if (var2 < 128) {
         return var2 - 32;
      } else if (var2 < 160) {
         return 0;
      } else {
         return var2 < 256 ? var2 - 64 : 0;
      }
   }

   public final void a(Graphics var1, char[] var2, int var3, int var4, int var5, int var6, int var7) {
      this.a(var1, var2, null, var3, var4, var5, var6, var7);
   }

   public final void a(Graphics var1, String var2, int var3, int var4, int var5) {
      this.a(var1, null, var2, 0, -1, var3, var4, var5);
   }

   private void a(Graphics var1, char[] var2, String var3, int var4, int var5, int var6, int var7, int var8) {
      label74:
      if (var5 < 0) {
         int var10000;
         if (var2 != null) {
            var10000 = var2.length;
         } else {
            if (var3 == null) {
               break label74;
            }

            var10000 = var3.length();
         }

         var5 = var10000 - var4;
      }

      label67: {
         int var20;
         int var10001;
         switch (var8 & 114) {
            case 2:
               var20 = var7;
               var10001 = this.o / 2;
               break;
            case 32:
               var20 = var7;
               var10001 = this.o;
               break;
            default:
               break label67;
         }

         var7 = var20 - var10001;
      }

      int var9 = 0;
      switch (var8 & 13) {
         case 1:
            for (int var17 = var4; var17 < var4 + var5; var17++) {
               var9 += this.p[a(var2, var3, var17)][0] + this.q;
            }

            var6 -= (var9 - this.q) / 2;
         case 0:
         case 4:
            for (int var18 = var4; var18 < var4 + var5; var18++) {
               int var16 = a(var2, var3, var18);

               for (int var19 = this.p[var16].length - 1; var19 >= 1; var19--) {
                  short var15 = this.p[var16][var19];
                  var1.fillRect(var6 + (var15 >> 12 & 15), var7 + (var15 >> 8 & 15), var15 >> 4 & 15, var15 & 15);
               }

               var6 += this.p[var16][0] + this.q;
            }
         case 2:
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            break;
         case 8:
            var6 += this.q;

            for (int var11 = var4 + var5 - 1; var11 >= var4; var11--) {
               int var10 = a(var2, var3, var11);
               var6 -= this.p[var10][0] + this.q;

               for (int var12 = this.p[var10].length - 1; var12 >= 1; var12--) {
                  short var14 = this.p[var10][var12];
                  var1.fillRect(var6 + (var14 >> 12 & 15), var7 + (var14 >> 8 & 15), var14 >> 4 & 15, var14 & 15);
               }
            }
      }
   }

   public final int a(String var1) {
      return this.b(var1, 0, -1);
   }

   private int b(String var1, int var2, int var3) {
      if (var3 < 0) {
         var3 = var1.length() - var2;
      }

      int var4 = 0;

      for (int var5 = var2; var5 < var2 + var3; var5++) {
         var4 += this.p[a(null, var1, var5)][0] + this.q;
      }

      return var4 - this.q;
   }

   public final int a(char var1) {
      return this.p[a(null, null, var1)][0] + this.q;
   }
}
