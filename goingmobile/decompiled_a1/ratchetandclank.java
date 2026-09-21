import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Display;
import javax.microedition.media.Manager;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class ratchetandclank extends MIDlet {
   public Display a;
   public g b;
   public h c;
   public e d;
   public static a e;
   public static a f;
   public static a g;
   public static a h;
   public boolean i = false;
   public boolean j = false;
   public boolean k;
   private c s;
   public byte[] l;
   public int[] m;
   public static String[] n;
   private int[] t = new int[]{1, 2, 3, 4};
   public byte[] o = new byte[214];
   private byte[] u = new byte[3];
   public boolean p = false;
   public static int q = -1;
   public static final String[] r = new String[]{"txt_en", "txt_fr", "txt_it", "txt_gr", "txt_sp"};

   public ratchetandclank() {
      System.gc();
      this.a = Display.getDisplay(this);
      this.f();
      q = this.j();
      this.l = new byte[3];
      this.m = new int[3];
      this.s = new c();
      if (this.i() > 0) {
         this.k = true;
      } else {
         this.k = false;
      }

      this.b = new g(this);
   }

   public final void startApp() throws MIDletStateChangeException {
      if (!this.j) {
         this.j = true;
         this.b.q = true;
         this.d = new e(this);
         this.d.setFullScreenMode(true);

         try {
            this.s.b = Manager.createPlayer(new ByteArrayInputStream(this.s.a[0]), "audio/midi");
         } catch (Exception var2) {
         }

         this.a.setCurrent(this.d);
         this.d.b();
      }
   }

   public final void pauseApp() {
      if (this.d != null) {
         this.d.hideNotify();
      }
   }

   public final void destroyApp(boolean var1) throws MIDletStateChangeException {
      this.b = null;
      this.c = null;
   }

   public final void a(int var1) {
      this.c();
      this.e();
      this.b.a(true);
      this.c.cu = 0;
      this.c.c(var1, -1);
      this.i = true;
   }

   public final void b(int var1) {
      this.c();
      this.b.a(false);
      this.c.c(-1, var1);
      this.i = true;
   }

   public final void a() {
      this.d();
      this.c.m();
      this.i = false;
      this.b.c();
   }

   public final void b() {
      this.s.a();
   }

   public final void c(int var1) {
      if (this.k) {
         this.s.a(var1);
      }
   }

   public final boolean a(boolean var1) {
      if (!this.k) {
         return true;
      } else {
         return !this.i && !this.b.p ? this.s.a(var1) : true;
      }
   }

   public final void c() {
      this.s.a();
   }

   public final void d() {
      this.s.b();
   }

   public final void e() {
      for (int var1 = 0; var1 < 3; var1++) {
         this.g(var1);
         this.l[var1] = this.o[0];
         this.m[var1] = this.a(this.o, 195);
      }
   }

   public final void a(int var1, byte[] var2, int var3) {
      var2[0 + var3] = (byte)(var1 >> 24 & 0xFF);
      var2[1 + var3] = (byte)(var1 >> 16 & 0xFF);
      var2[2 + var3] = (byte)(var1 >> 8 & 0xFF);
      var2[3 + var3] = (byte)(var1 & 0xFF);
   }

   public final int a(byte[] var1, int var2) {
      int var3 = var1[0 + var2] < 0 ? var1[0 + var2] + 256 : var1[0 + var2];
      int var4 = var1[1 + var2] < 0 ? var1[1 + var2] + 256 : var1[1 + var2];
      int var5 = var1[2 + var2] < 0 ? var1[2 + var2] + 256 : var1[2 + var2];
      int var6 = var1[3 + var2] < 0 ? var1[3 + var2] + 256 : var1[3 + var2];
      return var3 << 24 | var4 << 16 | var5 << 8 | var6;
   }

   public final void a(short var1, byte[] var2, int var3) {
      var2[var3++] = (byte)(var1 >> 8);
      var2[var3] = (byte)var1;
   }

   public final short b(byte[] var1, int var2) {
      return (short)(
         (short)(var1[0 + var2] < 0 ? var1[0 + var2] + 256 : var1[0 + var2]) << 8 | (short)(var1[1 + var2] < 0 ? var1[1 + var2] + 256 : var1[1 + var2])
      );
   }

   public final void f() {
      RecordStore var1 = null;

      try {
         if ((var1 = RecordStore.openRecordStore("RANDCSm", true)).getNumRecords() < 4) {
            var1.closeRecordStore();
            this.g();
         } else {
            for (int var3 = 0; var3 < 4; var3++) {
               if (var3 < 3 && var1.getRecordSize(this.t[var3]) != 214 || var3 == 3 && var1.getRecordSize(this.t[var3]) != 3) {
                  var1.closeRecordStore();
                  this.g();
                  return;
               }
            }

            var1.closeRecordStore();
         }
      } catch (RecordStoreException var5) {
         try {
            var1.closeRecordStore();
         } catch (Exception var4) {
         }

         this.g();
      }
   }

   public final void g() {
      try {
         RecordStore.deleteRecordStore("RANDCSm");
      } catch (RecordStoreException var5) {
      }

      RecordStore var1 = null;

      try {
         var1 = RecordStore.openRecordStore("RANDCSm", true);

         for (int var2 = 0; var2 < 3; var2++) {
            var1.addRecord(this.o, 0, 214);
         }

         this.u[0] = 1;
         this.u[2] = 0;
         this.u[1] = -1;
         var1.addRecord(this.u, 0, 3);
         var1.closeRecordStore();
      } catch (RecordStoreException var6) {
         try {
            var1.closeRecordStore();
         } catch (Exception var4) {
         }
      }
   }

   public final void d(int var1) {
      RecordStore var2 = null;

      try {
         var2 = RecordStore.openRecordStore("RANDCSm", false);
         this.c.a(this.o);
         var2.setRecord(this.t[var1], this.o, 0, 214);
         var2.closeRecordStore();
      } catch (Exception var6) {
         try {
            var2.closeRecordStore();
         } catch (Exception var5) {
         }
      }
   }

   public final void e(int var1) {
      RecordStore var2 = null;

      try {
         (var2 = RecordStore.openRecordStore("RANDCSm", false)).getRecord(this.t[var1], this.o, 0);
         var2.closeRecordStore();
         this.c.b(this.o);
      } catch (Exception var6) {
         try {
            var2.closeRecordStore();
         } catch (Exception var5) {
         }
      }
   }

   public final void f(int var1) {
      RecordStore var2 = null;

      try {
         var2 = RecordStore.openRecordStore("RANDCSm", false);

         for (int var3 = 0; var3 < 214; var3++) {
            this.o[var3] = 0;
         }

         var2.setRecord(this.t[var1], this.o, 0, 214);
         var2.closeRecordStore();
      } catch (Exception var6) {
         try {
            var2.closeRecordStore();
         } catch (Exception var5) {
         }
      }
   }

   public final void g(int var1) {
      RecordStore var2 = null;

      try {
         (var2 = RecordStore.openRecordStore("RANDCSm", false)).getRecord(this.t[var1], this.o, 0);
         var2.closeRecordStore();
      } catch (Exception var6) {
         try {
            var2.closeRecordStore();
         } catch (Exception var5) {
         }
      }
   }

   public final boolean h() {
      if (!this.p) {
         try {
            RecordStore var1;
            (var1 = RecordStore.openRecordStore("RANDCSm", false)).getRecord(this.t[3], this.u, 0);
            this.p = true;
            var1.closeRecordStore();
         } catch (Exception var2) {
         }
      }

      String var3;
      return (var3 = this.getAppProperty("GameIsWon")) != null ? var3.equals("true") : this.u[2] != 0;
   }

   public final byte i() {
      if (!this.p) {
         try {
            RecordStore var1;
            (var1 = RecordStore.openRecordStore("RANDCSm", false)).getRecord(this.t[3], this.u, 0);
            this.p = true;
            var1.closeRecordStore();
         } catch (Exception var2) {
         }
      }

      return this.u[0];
   }

   public final byte j() {
      if (!this.p) {
         try {
            RecordStore var1;
            (var1 = RecordStore.openRecordStore("RANDCSm", false)).getRecord(this.t[3], this.u, 0);
            this.p = true;
            var1.closeRecordStore();
         } catch (Exception var2) {
         }
      }

      return this.u[1];
   }

   public final void a(byte var1) {
      try {
         RecordStore var2;
         (var2 = RecordStore.openRecordStore("RANDCSm", false)).getRecord(this.t[3], this.u, 0);
         this.u[0] = var1;
         this.u[1] = (byte)q;
         var2.setRecord(this.t[3], this.u, 0, 3);
         var2.closeRecordStore();
      } catch (RecordStoreNotOpenException var3) {
      } catch (InvalidRecordIDException var4) {
      } catch (RecordStoreException var5) {
      }
   }

   public final void k() {
      try {
         RecordStore var1;
         (var1 = RecordStore.openRecordStore("RANDCSm", false)).getRecord(this.t[3], this.u, 0);
         this.u[2] = 1;
         var1.setRecord(this.t[3], this.u, 0, 3);
         var1.closeRecordStore();
      } catch (Exception var2) {
      }
   }

   public final void l() {
      n = new String[321];
      System.gc();
      int var1 = 0;
      int var2 = 0;
      int var3 = 0;
      StringBuffer var5 = new StringBuffer();
      new String();
      String var6 = null;

      try {
         InputStream var7 = this.getClass().getResourceAsStream(r[q == -1 ? 0 : q] + ".txt");

         while ((var1 = var7.read()) != -1) {
            var5.append((char)var1);
         }

         var6 = var5.toString();

         for (int var8 = 0; var8 < 321; var8++) {
            var2 = var6.indexOf("\n", var3);
            String var4;
            if (!(var4 = var6.substring(var3, var2).trim()).equals("")) {
               n[var8] = var4;
            }

            var3 = var2 + 1;
         }

         var7.close();
      } catch (IOException var9) {
      }

      System.gc();
   }
}
