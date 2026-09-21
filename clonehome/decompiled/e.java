import java.io.ByteArrayInputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;

public final class e implements Runnable {
   private a a;
   private int b;
   private volatile boolean c;
   private int d;
   private int e;
   private long f;
   private int[] g;
   private int[] h;
   private Player[] i;
   private Player j;

   public final synchronized void a() {
      if (!this.c) {
         this.c = true;
         this.b(-1);
      }
   }

   public final boolean b() {
      this.a.a(false);
      return !this.c && this.a.c;
   }

   public final void a(boolean var1) {
      if (this.b() != var1) {
         this.c();
         this.a.c = var1;
         this.a.a(true);
      }
   }

   private static String a(byte var0) {
      if (var0 == 14) {
         return "video/3gp";
      } else if (var0 == 17) {
         return "video/h264";
      } else if (var0 == 19) {
         return "video/mpeg4";
      } else if (var0 == 1) {
         return "audio/x-wav";
      } else if (var0 == 2) {
         return "audio/mpeg";
      } else if (var0 == 3) {
         return "audio/amr";
      } else if (var0 == 8) {
         return "audio/x-tone-seq";
      } else {
         return var0 == 9 ? "audio/imelody" : "audio/midi";
      }
   }

   public final synchronized void run() {
      if (this.b()) {
         int var1 = this.d;
         if (this.d >= 0) {
            if (this.e >= 0) {
               this.c();
            }

            if (System.currentTimeMillis() - this.f < 100L) {
               this.d = var1;
               return;
            }

            this.d = -1;
            Player var2 = this.i[var1];

            try {
               var2.prefetch();
            } catch (Throwable var6) {
               return;
            }

            try {
               var2.setMediaTime(0L);
            } catch (Throwable var5) {
            }

            try {
               var2.setLoopCount(this.g[var1]);
            } catch (Throwable var4) {
            }

            try {
               var2.start();
            } catch (Throwable var3) {
               return;
            }

            this.e = var1;
            this.j = var2;
         }
      }
   }

   public final synchronized void a(int var1, int var2, int var3, int var4) {
      if (!this.c) {
         if (this.i[var1] != null) {
            this.b(var1);
         }

         this.g[var1] = var3;
         this.h[var1] = var4;

         try {
            byte[] var5 = this.a.d(var2);
            this.i[var1] = Manager.createPlayer(new ByteArrayInputStream(var5), a(this.a.d[var2 & 1023]));
            this.i[var1].realize();
            this.i[var1].prefetch();
            return;
         } catch (Throwable var6) {
         }
      }
   }

   private synchronized void b(int var1) {
      if (var1 == -1) {
         for (int var2 = 0; var2 < this.b; var2++) {
            this.b(var2);
         }
      } else {
         if (this.e == var1 || this.d == var1) {
            this.c();
         }

         if (this.i[var1] != null) {
            this.i[var1].close();
            this.i[var1] = null;
         }
      }
   }

   public final void a(int var1) {
      if (this.b() && this.i[var1] != null && (this.d < 0 || this.h[this.d] <= this.h[var1])) {
         int var2 = this.e;
         Player var3 = this.j;
         if (this.j != null && var3.getState() == 400 && this.h[var2] > this.h[var1]) {
            return;
         }

         this.d = var1;
      }
   }

   public final synchronized void c() {
      if (this.j != null) {
         try {
            if (this.j.getState() == 400) {
               this.j.stop();
               this.f = System.currentTimeMillis();
            }
         } catch (Throwable var1) {
         }

         this.j = null;
      }

      this.d = -1;
      this.e = -1;
   }

   public e(a var1, int var2) {
      this.a = var1;
      this.d = -1;
      this.e = -1;
      this.b = var2;
      this.g = new int[var2];
      this.h = new int[var2];
      this.i = new Player[var2];
      var1.b = this;
   }
}
