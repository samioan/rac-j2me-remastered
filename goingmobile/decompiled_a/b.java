import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;

public final class b implements Runnable, PlayerListener {
   private static final String[] b = new String[]{"port", "killen", "bubble", "death", "box", "shoot", "menu"};
   public Thread a;
   private static final int[] c = new int[]{0, 0, 0, 0, 0, 0, 0};
   private byte[][] d = new byte[b.length][];
   private Player e;
   private int f = -1;
   private int g = -1;

   public b() {
      byte[] var1 = new byte[8192];

      for (int var2 = 0; var2 < b.length; var2++) {
         try {
            Class var10000;
            StringBuffer var10001;
            String var10002;
            if (c[var2] == 0) {
               var10000 = System.out.getClass();
               var10001 = new StringBuffer().append("/").append(b[var2]);
               var10002 = ".mid";
            } else {
               var10000 = System.out.getClass();
               var10001 = new StringBuffer().append("/").append(b[var2]);
               var10002 = ".wav";
            }

            InputStream var3;
            int var4 = (var3 = var10000.getResourceAsStream(var10001.append(var10002).toString())).read(var1);
            var3.close();
            this.d[var2] = new byte[var4];
            System.arraycopy(var1, 0, this.d[var2], 0, var4);
         } catch (Throwable var5) {
         }
      }

      this.a = new Thread(this);
      this.a.start();
   }

   private void b() {
      if (this.e != null) {
         try {
            this.e.stop();
         } catch (Throwable var3) {
         }

         try {
            this.e.close();
         } catch (Throwable var2) {
         }

         this.e = null;
      }
   }

   public final void a(int var1, int var2) {
      if (this.f == -1) {
         this.f = var1;
         this.g = var2;
      }
   }

   public final void a() {
      this.f = -1;
      this.b();
   }

   public final void run() {
      while (Thread.currentThread() == this.a) {
         f.a(30);
         if (this.f <= -1) {
            if (this.f == -2) {
               this.f = -1;
               this.b();
            }
         } else if (this.e != null) {
            if (this.e.getState() != 400) {
               this.b();
            }
         } else {
            try {
               b var10000;
               ByteArrayInputStream var10001;
               String var10002;
               if (c[this.f] == 0) {
                  var10000 = this;
                  var10001 = new ByteArrayInputStream(this.d[this.f]);
                  var10002 = "audio/midi";
               } else {
                  var10000 = this;
                  var10001 = new ByteArrayInputStream(this.d[this.f]);
                  var10002 = "audio/x-wav";
               }

               var10000.e = Manager.createPlayer(var10001, var10002);
               this.e.addPlayerListener(this);
               this.e.realize();
               this.e.prefetch();
               if (this.g != 0) {
                  this.e.setLoopCount(this.g);
               }

               this.e.start();
               this.f = -1;
            } catch (Exception var2) {
               if (this.g != -1) {
                  this.f = -1;
               }

               this.b();
            }
         }
      }
   }

   public final void playerUpdate(Player var1, String var2, Object var3) {
      if (var2.equals("endOfMedia") && this.g != -1) {
         this.f = -2;
      }
   }
}
