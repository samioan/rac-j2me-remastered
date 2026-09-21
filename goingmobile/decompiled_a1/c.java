import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;

public final class c implements Runnable, PlayerListener {
   private static final String[] c = new String[]{"death", "bubble", "shoot", "msound", "box", "menu"};
   private static final int[] d = new int[]{1, 1, 1, 1, 1, 0};
   public byte[][] a = new byte[c.length][];
   public Player b;
   private int e = -1;
   private int f = -1;
   private int g = -1;
   private Thread h;

   public c() {
      this.c();
   }

   private void c() {
      byte[] var1 = new byte[8192];

      for (int var2 = 0; var2 < c.length; var2++) {
         try {
            InputStream var3;
            if (d[var2] == 0) {
               var3 = System.out.getClass().getResourceAsStream("/" + c[var2] + ".mid");
            } else {
               var3 = System.out.getClass().getResourceAsStream("/" + c[var2] + ".wav");
            }

            int var4 = var3.read(var1);
            var3.close();
            this.a[var2] = new byte[var4];
            System.arraycopy(var1, 0, this.a[var2], 0, var4);
         } catch (Throwable var5) {
         }
      }

      this.h = new Thread(this);
      this.h.start();
   }

   public final void a() {
      if (this.b != null) {
         try {
            this.b.stop();
         } catch (Throwable var4) {
         }

         try {
            this.b.deallocate();
         } catch (Throwable var3) {
         }

         try {
            this.b.close();
         } catch (Throwable var2) {
         }

         this.b = null;
      }
   }

   public final void a(int var1) {
      this.a(var1, 0);
   }

   public final boolean a(boolean var1) {
      this.a(5, -1);
      return true;
   }

   public final void a(int var1, int var2) {
      if (var1 > -1 && var1 < c.length && this.e == -1) {
         this.e = var1;
         if (var2 == 0) {
            var2 = 1;
         }

         this.f = var2;
      }
   }

   public final void b() {
      this.e = -1;
      if (this.b != null && this.b.getState() == 400) {
         try {
            this.b.stop();
            this.b.setMediaTime(0L);
            return;
         } catch (Throwable var2) {
         }
      }
   }

   public final void run() {
      while (Thread.currentThread() == this.h) {
         h.e(30);
         if (this.e > -1) {
            if (this.e != this.g && this.b != null) {
               this.a();
            } else if (this.e == this.g && this.b != null && this.b.getState() == 400) {
               if (this.f == -1) {
                  this.e = -1;
               }
            } else if (this.b == null) {
               try {
                  if (d[this.e] == 0) {
                     this.b = Manager.createPlayer(new ByteArrayInputStream(this.a[this.e]), "audio/midi");
                  } else {
                     this.b = Manager.createPlayer(new ByteArrayInputStream(this.a[this.e]), "audio/x-wav");
                  }

                  this.g = this.e;
                  this.b.addPlayerListener(this);
                  this.b.realize();
                  this.b.prefetch();
                  if (this.f != 0) {
                     this.b.setLoopCount(this.f);
                  }
               } catch (Exception var2) {
                  this.e = -1;
                  this.a();
               }
            } else {
               try {
                  this.b.start();
                  this.e = -1;
               } catch (Exception var3) {
                  this.e = -1;
                  this.a();
               }
            }
         }
      }
   }

   public final void playerUpdate(Player var1, String var2, Object var3) {
      var2.equals("endOfMedia");
   }
}
