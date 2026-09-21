// Hand-written, faithfully renamed from decompiled/b.java (class b).
// The game's whole audio layer: loads the seven .mid/.wav cues into memory
// at startup and plays them one at a time on a background thread through
// the MIDP-2.0 MMAPI (javax.microedition.media).
//
// queue(id, loop) just records the request (first one wins until finished
// or stopped); the run() loop creates the Player, prefetches it and starts
// it. `loop == -1` means "play once and mark the pending sound as finished
// when endOfMedia fires" (the -2/-1 pending dance in run()).
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;

public final class SoundPlayer implements Runnable, PlayerListener {
   // Sound ids, in load order (index into soundFiles/soundData).
   public static final int PORT = 0;
   public static final int KILLEN = 1;
   public static final int BUBBLE = 2;
   public static final int DEATH = 3;
   public static final int BOX = 4;
   public static final int SHOOT = 5;
   public static final int MENU = 6;

   private static final String[] soundFiles =
      new String[]{"port", "killen", "bubble", "death", "box", "shoot", "menu"};
   // 0 = MIDI (audio/midi), anything else = WAV (audio/x-wav); all shipped
   // cues are .mid in this build, so every entry is 0.
   private static final int[] soundFormats = new int[]{0, 0, 0, 0, 0, 0, 0};
   private final byte[][] soundData = new byte[soundFiles.length][];
   public Thread thread;
   private Player player;
   private int pendingSound = -1;
   private int pendingLoop = -1;

   public SoundPlayer() {
      byte[] var1 = new byte[8192];

      for (int var2 = 0; var2 < soundFiles.length; var2++) {
         try {
            InputStream var3 = System.out.getClass().getResourceAsStream(
               "/" + soundFiles[var2] + (soundFormats[var2] == 0 ? ".mid" : ".wav"));
            int var4 = var3.read(var1);
            var3.close();
            this.soundData[var2] = new byte[var4];
            System.arraycopy(var1, 0, this.soundData[var2], 0, var4);
         } catch (Throwable var5) {
         }
      }

      this.thread = new Thread(this);
      this.thread.start();
   }

   private void stopPlayer() {
      if (this.player != null) {
         try {
            this.player.stop();
         } catch (Throwable var3) {
         }

         try {
            this.player.close();
         } catch (Throwable var2) {
         }

         this.player = null;
      }
   }

   public final void queue(int var1, int var2) {
      if (this.pendingSound == -1) {
         this.pendingSound = var1;
         this.pendingLoop = var2;
      }
   }

   public final void stop() {
      this.pendingSound = -1;
      this.stopPlayer();
   }

   public final void run() {
      while (Thread.currentThread() == this.thread) {
         Game.a(30);
         if (this.pendingSound <= -1) {
            if (this.pendingSound == -2) {
               this.pendingSound = -1;
               this.stopPlayer();
            }
         } else if (this.player != null) {
            this.stopPlayer();
         } else {
            try {
               this.player = Manager.createPlayer(
                  new ByteArrayInputStream(this.soundData[this.pendingSound]),
                  soundFormats[this.pendingSound] == 0 ? "audio/midi" : "audio/x-wav");
               this.player.addPlayerListener(this);
               this.player.realize();
               this.player.prefetch();
               if (this.pendingLoop != 0) {
                  this.player.setLoopCount(this.pendingLoop);
               }

               this.player.start();
               this.pendingSound = -1;
            } catch (Exception var2) {
               if (this.pendingLoop != -1) {
                  this.pendingSound = -1;
               }

               this.stopPlayer();
            }
         }
      }
   }

   public final void playerUpdate(Player var1, String var2, Object var3) {
      if (var2.equals("endOfMedia") && this.pendingLoop != -1) {
         this.pendingSound = -2;
      }
   }
}
