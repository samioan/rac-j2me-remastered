// Hand-written, faithfully renamed from decompiled_a1/c.java (class c).
// a1's equivalent of the legacy build's SoundPlayer (decompiled/b.java,
// see ../src/SoundPlayer.java) -- same MMAPI background-thread design, but
// a1 mixes .wav cues in with .mid (soundFormats[i] != 0 -> "audio/x-wav",
// matching BUILD_COMPARISON.md's ".wav sound variants" note; only "menu"
// stays .mid here). Six cues instead of the legacy build's seven -- no
// PORT/KILLEN-equivalent id, "msound" is unidentified (likely a generic
// hit/impact cue; not confirmed against any m*.txt/txt_*.txt string).
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;

public final class SoundPlayer implements Runnable, PlayerListener {
   public static final int DEATH = 0;
   public static final int BUBBLE = 1;
   public static final int SHOOT = 2;
   public static final int MSOUND = 3; // unconfirmed role
   public static final int BOX = 4;
   public static final int MENU = 5;

   private static final String[] soundFiles =
      new String[]{"death", "bubble", "shoot", "msound", "box", "menu"};
   // 0 = MIDI (audio/midi), else WAV (audio/x-wav). Only "menu" is 0 here.
   private static final int[] soundFormats = new int[]{1, 1, 1, 1, 1, 0};
   public byte[][] soundData = new byte[soundFiles.length][];
   public Player player;
   private int pendingSound = -1;
   private int pendingLoop = -1;
   private int playingSound = -1;
   private Thread thread;

   public SoundPlayer() {
      this.load();
   }

   private void load() {
      byte[] var1 = new byte[8192];

      for (int var2 = 0; var2 < soundFiles.length; var2++) {
         try {
            InputStream var3;
            if (soundFormats[var2] == 0) {
               var3 = System.out.getClass().getResourceAsStream("/" + soundFiles[var2] + ".mid");
            } else {
               var3 = System.out.getClass().getResourceAsStream("/" + soundFiles[var2] + ".wav");
            }

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

   // PUBLIC (confirmed) -- ratchetandclank.java calls this directly
   // (`this.s.a()`, no-arg) to hard-silence audio, e.g. on hideNotify.
   // The internal run() loop also calls it when switching cues mid-stream.
   public final void haltPlayer() {
      if (this.player != null) {
         try {
            this.player.stop();
         } catch (Throwable var4) {
         }

         try {
            this.player.deallocate();
         } catch (Throwable var3) {
         }

         try {
            this.player.close();
         } catch (Throwable var2) {
         }

         this.player = null;
      }
   }

   public final void queue(int var1) {
      this.queue(var1, 0);
   }

   // Unconfirmed role -- forces MENU to play looped (-1) regardless of the
   // boolean argument, which is otherwise unused; always returns true.
   // Signature suggests a listener/callback adapter (e.g. a Command
   // handler), not confirmed against any call site's real purpose.
   public final boolean playMenuLoop(boolean var1) {
      this.queue(MENU, -1);
      return true;
   }

   public final void queue(int var1, int var2) {
      if (var1 > -1 && var1 < soundFiles.length && this.pendingSound == -1) {
         this.pendingSound = var1;
         if (var2 == 0) {
            var2 = 1;
         }

         this.pendingLoop = var2;
      }
   }

   public final void stop() {
      this.pendingSound = -1;
      if (this.player != null && this.player.getState() == 400) {
         try {
            this.player.stop();
            this.player.setMediaTime(0L);
            return;
         } catch (Throwable var2) {
         }
      }
   }

   public final void run() {
      while (Thread.currentThread() == this.thread) {
         Game.sleep(30);
         if (this.pendingSound > -1) {
            if (this.pendingSound != this.playingSound && this.player != null) {
               this.haltPlayer();
            } else if (this.pendingSound == this.playingSound && this.player != null && this.player.getState() == 400) {
               if (this.pendingLoop == -1) {
                  this.pendingSound = -1;
               }
            } else if (this.player == null) {
               try {
                  if (soundFormats[this.pendingSound] == 0) {
                     this.player = Manager.createPlayer(new ByteArrayInputStream(this.soundData[this.pendingSound]), "audio/midi");
                  } else {
                     this.player = Manager.createPlayer(new ByteArrayInputStream(this.soundData[this.pendingSound]), "audio/x-wav");
                  }

                  this.playingSound = this.pendingSound;
                  this.player.addPlayerListener(this);
                  this.player.realize();
                  this.player.prefetch();
                  if (this.pendingLoop != 0) {
                     this.player.setLoopCount(this.pendingLoop);
                  }
               } catch (Exception var2) {
                  this.pendingSound = -1;
                  this.haltPlayer();
               }
            } else {
               try {
                  this.player.start();
                  this.pendingSound = -1;
               } catch (Exception var3) {
                  this.pendingSound = -1;
                  this.haltPlayer();
               }
            }
         }
      }
   }

   // Unlike the legacy build's playerUpdate, this evaluates the
   // endOfMedia comparison but discards the result -- a no-op in this
   // build (decompiler-confirmed, not a rename artifact); loop-end
   // cleanup instead happens in run()'s own getState()==400 check.
   public final void playerUpdate(Player var1, String var2, Object var3) {
      var2.equals("endOfMedia");
   }
}
