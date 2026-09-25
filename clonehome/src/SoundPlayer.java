import java.io.ByteArrayInputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;

// $VF: renamed from: e
public final class SoundPlayer implements Runnable {
   // $VF: renamed from: a a
   private Engine engine;
   // $VF: renamed from: b int
   private int slotCount;
   // $VF: renamed from: c boolean
   private volatile boolean closed;
   // $VF: renamed from: d int
   private int pendingSlot;
   // $VF: renamed from: e int
   private int currentSlot;
   // $VF: renamed from: f long
   private long lastStopTime;
   // $VF: renamed from: g int[]
   private int[] loopCounts;
   // $VF: renamed from: h int[]
   private int[] priorities;
   // $VF: renamed from: i javax.microedition.media.Player[]
   private Player[] players;
   // $VF: renamed from: j javax.microedition.media.Player
   private Player currentPlayer;

   // $VF: renamed from: a () void
   public final synchronized void close() {
      if (!this.closed) {
         this.closed = true;
         this.unload(-1);
      }
   }

   // $VF: renamed from: b () boolean
   public final boolean isEnabled() {
      this.engine.syncSettings(false);
      return !this.closed && this.engine.soundEnabled;
   }

   // $VF: renamed from: a (boolean) void
   public final void setEnabled(boolean var1) {
      if (this.isEnabled() != var1) {
         this.stop();
         this.engine.soundEnabled = var1;
         this.engine.syncSettings(true);
      }
   }

   // $VF: renamed from: a (byte) java.lang.String
   private static String mimeType(byte var0) {
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
      if (this.isEnabled()) {
         int var1 = this.pendingSlot;
         if (this.pendingSlot >= 0) {
            if (this.currentSlot >= 0) {
               this.stop();
            }

            if (System.currentTimeMillis() - this.lastStopTime < 100L) {
               this.pendingSlot = var1;
               return;
            }

            this.pendingSlot = -1;
            Player var2 = this.players[var1];

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
               var2.setLoopCount(this.loopCounts[var1]);
            } catch (Throwable var4) {
            }

            try {
               var2.start();
            } catch (Throwable var3) {
               return;
            }

            this.currentSlot = var1;
            this.currentPlayer = var2;
         }
      }
   }

   // $VF: renamed from: a (int, int, int, int) void
   public final synchronized void load(int var1, int var2, int var3, int var4) {
      if (!this.closed) {
         if (this.players[var1] != null) {
            this.unload(var1);
         }

         this.loopCounts[var1] = var3;
         this.priorities[var1] = var4;

         try {
            byte[] var5 = this.engine.getResourceBytes(var2);
            this.players[var1] = Manager.createPlayer(new ByteArrayInputStream(var5), mimeType(this.engine.resourceTypes[var2 & 1023]));
            this.players[var1].realize();
            this.players[var1].prefetch();
            return;
         } catch (Throwable var6) {
         }
      }
   }

   // $VF: renamed from: b (int) void
   private synchronized void unload(int var1) {
      if (var1 == -1) {
         for (int var2 = 0; var2 < this.slotCount; var2++) {
            this.unload(var2);
         }
      } else {
         if (this.currentSlot == var1 || this.pendingSlot == var1) {
            this.stop();
         }

         if (this.players[var1] != null) {
            this.players[var1].close();
            this.players[var1] = null;
         }
      }
   }

   // $VF: renamed from: a (int) void
   public final void play(int var1) {
      if (this.isEnabled() && this.players[var1] != null && (this.pendingSlot < 0 || this.priorities[this.pendingSlot] <= this.priorities[var1])) {
         int var2 = this.currentSlot;
         Player var3 = this.currentPlayer;
         if (this.currentPlayer != null && var3.getState() == 400 && this.priorities[var2] > this.priorities[var1]) {
            return;
         }

         this.pendingSlot = var1;
      }
   }

   // $VF: renamed from: c () void
   public final synchronized void stop() {
      if (this.currentPlayer != null) {
         try {
            if (this.currentPlayer.getState() == 400) {
               this.currentPlayer.stop();
               this.lastStopTime = System.currentTimeMillis();
            }
         } catch (Throwable var1) {
         }

         this.currentPlayer = null;
      }

      this.pendingSlot = -1;
      this.currentSlot = -1;
   }

   public SoundPlayer(Engine var1, int var2) {
      this.engine = var1;
      this.pendingSlot = -1;
      this.currentSlot = -1;
      this.slotCount = var2;
      this.loopCounts = new int[var2];
      this.priorities = new int[var2];
      this.players = new Player[var2];
      var1.soundPlayer = this;
   }
}
