// Hand-written, faithfully renamed from decompiled_a1/ratchetandclank.java.
// The MIDlet. Same role as the legacy build's ratchetandclank (RMS saves,
// settings, string table) but a1 factors the font and canvas out into
// their own classes (Font, CanvasShell) and adds the intro/unlock-code
// manager (IntroManager, class-level rename only -- members still
// obfuscated, original `g`) and Game (class-level rename only, original
// `h`) as the two top-level controller objects CanvasShell delegates to.
// Class-level renames are applied everywhere as soon as
// BUILD_COMPARISON.md confirms them (same policy the legacy build's
// tools/rename_gm.py used), even before that class's own member-level
// phase-1 pass is done -- so `this.game.cu`/`.c(...)` etc. below
// reference obfuscated Game members through the renamed class.
//
// Save format differs from the legacy build's: 214-byte game-slot
// records here (220 in the legacy build), same 3-slot + 1-settings-record
// RMS layout (`RANDCSm`, record ids {1,2,3,4}). Settings record is the
// same 3 bytes as the legacy build (sound-enabled flag, language index,
// game-won flag), confirmed by h()/i()/j()/a(byte)/k() below.
//
// RMS method-quadruplet naming follows the legacy build's convention
// (write/read/clear/read-raw) even though the obfuscated letters differ
// (d/e/f/g here vs a/b/c/d there) -- same four-method shape, confirmed
// by reading each body.
//
// The game-flow orchestration methods (startNewGame/continueGame/
// returnToIntro/the sound-wrapper no-arg methods) are TENTATIVE --
// named from the shape of what they call, not confirmed against Game's
// (`h`) or IntroManager's (`g`) own confirmed behavior yet (neither
// class has had its phase-1 pass). Revisit once those are done.
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
   public Display display;
   public IntroManager introManager;
   public Game game;
   public CanvasShell canvas;
   // The "currently active" font pointer, reassigned before draw calls
   // (confirmed at decompiled_a1/g.java:1162 etc.) between smallFont/
   // smallFontAlias/largeFont below.
   public static Font currentFont;
   public static Font largeFont;   // f3.v, line height 13 (see Font.java)
   // smallFontAlias always equals smallFont (decompiled_a1/h.java:715,
   // `ratchetandclank.g = ratchetandclank.h;`, never reassigned
   // elsewhere) -- functionally a dead alias, kept for parity with the
   // obfuscated original; g.java:1185's `var6 ? smallFontAlias :
   // smallFont` conditional is therefore a no-op in this build.
   public static Font smallFontAlias;
   public static Font smallFont;   // f2.v, line height 10 (byte-identical to the legacy build's font asset)
   public boolean gameStarted = false;
   private boolean midletStarted = false;
   public boolean soundEnabled;
   private SoundPlayer soundPlayer;
   public byte[] saveSlotFlags;
   public int[] saveSlotTimes;
   public static String[] strings;
   private int[] SAVE_RECORD_IDS = new int[]{1, 2, 3, 4};
   public byte[] saveBuffer = new byte[214];
   private byte[] settings = new byte[3];
   public boolean settingsLoaded = false;
   public static int language = -1;
   public static final String[] LANGUAGE_FILES = new String[]{"txt_en", "txt_fr", "txt_it", "txt_gr", "txt_sp"};

   public ratchetandclank() {
      System.gc();
      this.display = Display.getDisplay(this);
      this.verifySaveStore();
      language = this.readLanguageSetting();
      this.saveSlotFlags = new byte[3];
      this.saveSlotTimes = new int[3];
      this.soundPlayer = new SoundPlayer();
      if (this.readSoundSetting() > 0) {
         this.soundEnabled = true;
      } else {
         this.soundEnabled = false;
      }

      this.introManager = new IntroManager(this);
   }

   public final void startApp() throws MIDletStateChangeException {
      if (!this.midletStarted) {
         this.midletStarted = true;
         this.introManager.q = true;
         this.canvas = new CanvasShell(this);
         this.canvas.setFullScreenMode(true);

         try {
            this.soundPlayer.player = Manager.createPlayer(new ByteArrayInputStream(this.soundPlayer.soundData[0]), "audio/midi");
         } catch (Exception var2) {
         }

         this.display.setCurrent(this.canvas);
         this.canvas.startLoop();
      }
   }

   public final void pauseApp() {
      if (this.canvas != null) {
         this.canvas.hideNotify();
      }
   }

   public final void destroyApp(boolean var1) throws MIDletStateChangeException {
      this.introManager = null;
      this.game = null;
   }

   // Tentative: "start a new game at level var1" -- calls introManager's
   // boolean-arg method with true (vs. continueGame's false), then
   // Game.c(var1, -1) (obfuscated, unconfirmed pending Game's phase-1).
   public final void startNewGame(int var1) {
      this.stopSoundHard();
      this.refreshSaveSlotSummaries();
      this.introManager.a(true);
      this.game.cu = 0;
      this.game.c(var1, -1);
      this.gameStarted = true;
   }

   // Tentative: "continue/load a saved game at slot var1" -- mirror of
   // startNewGame with the Game.c(int,int) arguments swapped.
   public final void continueGame(int var1) {
      this.stopSoundHard();
      this.introManager.a(false);
      this.game.c(-1, var1);
      this.gameStarted = true;
   }

   // Tentative: "return from gameplay to the intro/menu" -- soft-stops
   // sound, calls Game.m() (obfuscated, unconfirmed), clears
   // gameStarted, reactivates the intro manager.
   public final void returnToIntro() {
      this.stopSoundSoft();
      this.game.m();
      this.gameStarted = false;
      this.introManager.c();
   }

   // Called from CanvasShell.hideNotify() (`this.midlet.b()`).
   public final void stopSoundHard() {
      this.soundPlayer.haltPlayer();
   }

   public final void playSoundIfEnabled(int var1) {
      if (this.soundEnabled) {
         this.soundPlayer.queue(var1);
      }
   }

   // Gap fix: this method was missing from the initial transcription of
   // this file (caught while writing IntroManager.java, which calls it
   // as `this.midlet.playMenuLoopSound(...)`). `introManager.p` is
   // IntroManager's own not-yet-renamed field (set/cleared around
   // splash-image loading, per IntroManager.java's header -- read here
   // as a "still busy loading" guard).
   public final boolean playMenuLoopSound(boolean var1) {
      if (!this.soundEnabled) {
         return true;
      } else {
         return !this.gameStarted && !this.introManager.p ? this.soundPlayer.playMenuLoop(var1) : true;
      }
   }

   // Tentative: same body as stopSoundHard() (decompiler-confirmed
   // duplicate, not a rename artifact) -- kept as a distinct method since
   // the original bytecode had it as a separate obfuscated name (`c()`);
   // called from startNewGame/continueGame before starting the level.
   public final void stopSoundHardOnLevelStart() {
      this.soundPlayer.haltPlayer();
   }

   public final void stopSoundSoft() {
      this.soundPlayer.stop();
   }

   public final void refreshSaveSlotSummaries() {
      for (int var1 = 0; var1 < 3; var1++) {
         this.readSaveSlotRaw(var1);
         this.saveSlotFlags[var1] = this.saveBuffer[0];
         this.saveSlotTimes[var1] = this.readInt(this.saveBuffer, 195);
      }
   }

   public final void writeInt(int var1, byte[] var2, int var3) {
      var2[0 + var3] = (byte)(var1 >> 24 & 0xFF);
      var2[1 + var3] = (byte)(var1 >> 16 & 0xFF);
      var2[2 + var3] = (byte)(var1 >> 8 & 0xFF);
      var2[3 + var3] = (byte)(var1 & 0xFF);
   }

   public final int readInt(byte[] var1, int var2) {
      int var3 = var1[0 + var2] < 0 ? var1[0 + var2] + 256 : var1[0 + var2];
      int var4 = var1[1 + var2] < 0 ? var1[1 + var2] + 256 : var1[1 + var2];
      int var5 = var1[2 + var2] < 0 ? var1[2 + var2] + 256 : var1[2 + var2];
      int var6 = var1[3 + var2] < 0 ? var1[3 + var2] + 256 : var1[3 + var2];
      return var3 << 24 | var4 << 16 | var5 << 8 | var6;
   }

   public final void writeShort(short var1, byte[] var2, int var3) {
      var2[var3++] = (byte)(var1 >> 8);
      var2[var3] = (byte)var1;
   }

   public final short readShort(byte[] var1, int var2) {
      return (short)(
         (short)(var1[0 + var2] < 0 ? var1[0 + var2] + 256 : var1[0 + var2]) << 8 | (short)(var1[1 + var2] < 0 ? var1[1 + var2] + 256 : var1[1 + var2])
      );
   }

   // Verifies the RANDCSm store exists with the right record sizes (3 x
   // 214-byte game slots + 1 x 3-byte settings record); rebuilds it from
   // scratch otherwise. Same role as the legacy build's store-verify
   // method, different record size (214 vs 220).
   public final void verifySaveStore() {
      RecordStore var1 = null;

      try {
         if ((var1 = RecordStore.openRecordStore("RANDCSm", true)).getNumRecords() < 4) {
            var1.closeRecordStore();
            this.rebuildSaveStore();
         } else {
            for (int var3 = 0; var3 < 4; var3++) {
               if (var3 < 3 && var1.getRecordSize(this.SAVE_RECORD_IDS[var3]) != 214 || var3 == 3 && var1.getRecordSize(this.SAVE_RECORD_IDS[var3]) != 3) {
                  var1.closeRecordStore();
                  this.rebuildSaveStore();
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

         this.rebuildSaveStore();
      }
   }

   public final void rebuildSaveStore() {
      try {
         RecordStore.deleteRecordStore("RANDCSm");
      } catch (RecordStoreException var5) {
      }

      RecordStore var1 = null;

      try {
         var1 = RecordStore.openRecordStore("RANDCSm", true);

         for (int var2 = 0; var2 < 3; var2++) {
            var1.addRecord(this.saveBuffer, 0, 214);
         }

         this.settings[0] = 1;
         this.settings[2] = 0;
         this.settings[1] = -1;
         var1.addRecord(this.settings, 0, 3);
         var1.closeRecordStore();
      } catch (RecordStoreException var6) {
         try {
            var1.closeRecordStore();
         } catch (Exception var4) {
         }
      }
   }

   public final void writeSaveSlot(int var1) {
      RecordStore var2 = null;

      try {
         var2 = RecordStore.openRecordStore("RANDCSm", false);
         this.game.writeSaveData(this.saveBuffer);
         var2.setRecord(this.SAVE_RECORD_IDS[var1], this.saveBuffer, 0, 214);
         var2.closeRecordStore();
      } catch (Exception var6) {
         try {
            var2.closeRecordStore();
         } catch (Exception var5) {
         }
      }
   }

   public final void readSaveSlot(int var1) {
      RecordStore var2 = null;

      try {
         (var2 = RecordStore.openRecordStore("RANDCSm", false)).getRecord(this.SAVE_RECORD_IDS[var1], this.saveBuffer, 0);
         var2.closeRecordStore();
         this.game.readSaveData(this.saveBuffer);
      } catch (Exception var6) {
         try {
            var2.closeRecordStore();
         } catch (Exception var5) {
         }
      }
   }

   public final void clearSaveSlot(int var1) {
      RecordStore var2 = null;

      try {
         var2 = RecordStore.openRecordStore("RANDCSm", false);

         for (int var3 = 0; var3 < 214; var3++) {
            this.saveBuffer[var3] = 0;
         }

         var2.setRecord(this.SAVE_RECORD_IDS[var1], this.saveBuffer, 0, 214);
         var2.closeRecordStore();
      } catch (Exception var6) {
         try {
            var2.closeRecordStore();
         } catch (Exception var5) {
         }
      }
   }

   public final void readSaveSlotRaw(int var1) {
      RecordStore var2 = null;

      try {
         (var2 = RecordStore.openRecordStore("RANDCSm", false)).getRecord(this.SAVE_RECORD_IDS[var1], this.saveBuffer, 0);
         var2.closeRecordStore();
      } catch (Exception var6) {
         try {
            var2.closeRecordStore();
         } catch (Exception var5) {
         }
      }
   }

   // settings[2] -- game-won flag; overridable by the `GameIsWon`
   // manifest/JAD property (not present in the shipped a1 jar -- see
   // ROADMAP.md's target section -- so this falls through to the RMS
   // value in practice).
   public final boolean isGameWon() {
      if (!this.settingsLoaded) {
         try {
            RecordStore var1;
            (var1 = RecordStore.openRecordStore("RANDCSm", false)).getRecord(this.SAVE_RECORD_IDS[3], this.settings, 0);
            this.settingsLoaded = true;
            var1.closeRecordStore();
         } catch (Exception var2) {
         }
      }

      String var3;
      return (var3 = this.getAppProperty("GameIsWon")) != null ? var3.equals("true") : this.settings[2] != 0;
   }

   // settings[0] -- sound-enabled flag (read as a byte, used as boolean via `> 0`).
   public final byte readSoundSetting() {
      if (!this.settingsLoaded) {
         try {
            RecordStore var1;
            (var1 = RecordStore.openRecordStore("RANDCSm", false)).getRecord(this.SAVE_RECORD_IDS[3], this.settings, 0);
            this.settingsLoaded = true;
            var1.closeRecordStore();
         } catch (Exception var2) {
         }
      }

      return this.settings[0];
   }

   // settings[1] -- selected language index into LANGUAGE_FILES.
   public final byte readLanguageSetting() {
      if (!this.settingsLoaded) {
         try {
            RecordStore var1;
            (var1 = RecordStore.openRecordStore("RANDCSm", false)).getRecord(this.SAVE_RECORD_IDS[3], this.settings, 0);
            this.settingsLoaded = true;
            var1.closeRecordStore();
         } catch (Exception var2) {
         }
      }

      return this.settings[1];
   }

   public final void writeSoundAndLanguageSettings(byte var1) {
      try {
         RecordStore var2;
         (var2 = RecordStore.openRecordStore("RANDCSm", false)).getRecord(this.SAVE_RECORD_IDS[3], this.settings, 0);
         this.settings[0] = var1;
         this.settings[1] = (byte)language;
         var2.setRecord(this.SAVE_RECORD_IDS[3], this.settings, 0, 3);
         var2.closeRecordStore();
      } catch (RecordStoreNotOpenException var3) {
      } catch (InvalidRecordIDException var4) {
      } catch (RecordStoreException var5) {
      }
   }

   public final void markGameWon() {
      try {
         RecordStore var1;
         (var1 = RecordStore.openRecordStore("RANDCSm", false)).getRecord(this.SAVE_RECORD_IDS[3], this.settings, 0);
         this.settings[2] = 1;
         var1.setRecord(this.SAVE_RECORD_IDS[3], this.settings, 0, 3);
         var1.closeRecordStore();
      } catch (Exception var2) {
      }
   }

   // Loads the 321-entry string table for the selected language
   // (LANGUAGE_FILES[language]+".txt"), one string per newline-terminated
   // line (blank lines leave that index null). Same format/line count
   // convention as the legacy build's string loader.
   public final void loadStrings() {
      strings = new String[321];
      System.gc();
      int var1 = 0;
      int var2 = 0;
      int var3 = 0;
      StringBuffer var5 = new StringBuffer();
      new String();
      String var6 = null;

      try {
         InputStream var7 = this.getClass().getResourceAsStream(LANGUAGE_FILES[language == -1 ? 0 : language] + ".txt");

         while ((var1 = var7.read()) != -1) {
            var5.append((char)var1);
         }

         var6 = var5.toString();

         for (int var8 = 0; var8 < 321; var8++) {
            var2 = var6.indexOf("\n", var3);
            String var4;
            if (!(var4 = var6.substring(var3, var2).trim()).equals("")) {
               strings[var8] = var4;
            }

            var3 = var2 + 1;
         }

         var7.close();
      } catch (IOException var9) {
      }

      System.gc();
   }
}
