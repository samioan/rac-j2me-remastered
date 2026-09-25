import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public class RatchetMIDlet extends MIDlet {
   // $VF: renamed from: a b
   public static Game game;
   // $VF: renamed from: h boolean
   private static boolean started;
   // $VF: renamed from: b byte[]
   public static byte[] slotInUse;
   // $VF: renamed from: c int[]
   public static int[] slotPlayTime;
   // $VF: renamed from: d int[]
   public static final int[] recordIds = new int[]{1, 2, 3, 4};
   // $VF: renamed from: e byte[]
   public static byte[] saveBuffer = new byte[218];
   // $VF: renamed from: f byte[]
   public static byte[] settingsRecord = new byte[3];
   // $VF: renamed from: g boolean
   public static boolean settingsLoaded = false;

   public RatchetMIDlet() {
      slotInUse = new byte[3];
      slotPlayTime = new int[3];
      game = new Game(this);
      Game.state = 99;
      String var1;
      if ((var1 = this.getAppProperty("Cheat-Mode")) != null) {
         Game.cheatMode = var1.equals("true");
      }
   }

   // $VF: renamed from: a () void
   public static void refreshSlotSummaries() {
      for (int var0 = 0; var0 < 3; var0++) {
         saveBuffer = Engine.readRecord(recordIds[var0]);
         if (saveBuffer == null) {
            saveBuffer = new byte[218];
         }

         slotInUse[var0] = saveBuffer[0];
         slotPlayTime[var0] = readInt(saveBuffer, 195);
      }
   }

   // $VF: renamed from: a (int, byte[], int) void
   public static void writeInt(int var0, byte[] var1, int var2) {
      var1[var2++] = (byte)(var0 >> 24 & 0xFF);
      var1[var2++] = (byte)(var0 >> 16 & 0xFF);
      var1[var2++] = (byte)(var0 >> 8 & 0xFF);
      var1[var2] = (byte)(var0 & 0xFF);
   }

   // $VF: renamed from: a (byte[], int) int
   public static int readInt(byte[] var0, int var1) {
      return ((var0[0 + var1] & 0xFF) << 24) + ((var0[1 + var1] & 0xFF) << 16) + ((var0[2 + var1] & 0xFF) << 8) + (var0[3 + var1] & 0xFF);
   }

   // $VF: renamed from: a (short, byte[], int) void
   public static void writeShort(short var0, byte[] var1, int var2) {
      var1[var2++] = (byte)(var0 >> 8);
      var1[var2] = (byte)var0;
   }

   // $VF: renamed from: b (byte[], int) short
   public static short readShort(byte[] var0, int var1) {
      return (short)(((var0[var1] & 255) << 8) + (var0[var1 + 1] & 255));
   }

   // $VF: renamed from: a (int) void
   public final void saveSlot(int var1) {
      if (Game.unlockFlags != 0) {
         serializeSave(saveBuffer);
         Engine.writeRecord(recordIds[var1], saveBuffer);
      }
   }

   // $VF: renamed from: b (int) void
   public final void loadSlot(int var1) {
      saveBuffer = Engine.readRecord(recordIds[var1]);
      deserializeSave(saveBuffer);
   }

   // $VF: renamed from: c (int) void
   public static void clearSlot(int var0) {
      for (int var1 = 0; var1 < 218; var1++) {
         saveBuffer[var1] = 0;
      }

      Engine.writeRecord(recordIds[var0], saveBuffer);
   }

   // $VF: renamed from: b () boolean
   public static boolean isGameCompleted() {
      if (!settingsLoaded) {
         settingsRecord = Engine.readRecord(recordIds[3]);
         settingsLoaded = true;
      }

      return settingsRecord != null ? settingsRecord[1] != 0 : false;
   }

   // $VF: renamed from: c () void
   public static void markGameCompleted() {
      byte[] var0;
      if ((var0 = Engine.readRecord(recordIds[3])) != null) {
         settingsRecord = var0;
      }

      settingsRecord[1] = 1;
      if (!Engine.writeRecord(recordIds[3], settingsRecord)) {
         game.requestClear();
         Game.state = 105;
      }
   }

   public void pauseApp() {
      game.hideNotify();
   }

   public void startApp() {
      game.onStartApp();
      if (!started) {
         started = true;
         Display.getDisplay(this).setCurrent(game);
      }
   }

   public void destroyApp(boolean var1) {
      switch (Game.state) {
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
            if (Game.worldId != 0 && Game.saveSlot >= 0 && Game.saveSlot < 3) {
               this.saveSlot(Game.saveSlot);
            }
         case 5:
         case 6:
         case 7:
         case 9:
         case 10:
         case 16:
         case 17:
         default:
            game.onLifecycle(3);
      }
   }

   // $VF: renamed from: a (byte[]) void
   public static void serializeSave(byte[] var0) {
      var0[0] = 1;
      writeInt(Game.unlockFlags, var0, 1);
      writeInt(Game.collectibleMask, var0, 5);
      writeInt(Game.hintFlagsLow, var0, 9);
      var0[13] = (byte)Game.weaponsOwned;

      for (int var1 = 0; var1 < 8; var1++) {
         var0[14 + var1] = Game.weaponLevel[var1];
         writeShort(Game.weaponKills[var1], var0, 22 + var1 * 2);
         writeShort(Game.ammo[var1], var0, 38 + var1 * 2);
      }

      writeInt(Game.bolts, var0, 54);
      var0[58] = (byte)Game.doorBits;
      writeInt(Game.sectionSwitchBitsA, var0, 59);
      writeInt(Game.sectionSwitchBitsB, var0, 63);
      writeInt(Game.sectionSwitchBitsC, var0, 67);

      for (int var2 = 0; var2 < 10; var2++) {
         var0[71 + var2] = Game.ca[0][var2];
         var0[91 + var2] = Game.cb[0][var2];
         var0[111 + var2] = Game.cc[0][var2];
         var0[131 + var2] = Game.cd[0][var2];
         var0[151 + var2] = Game.ce[0][var2];
         var0[171 + var2] = Game.cf[0][var2];
         var0[71 + var2 + 10] = Game.ca[1][var2];
         var0[91 + var2 + 10] = Game.cb[1][var2];
         var0[111 + var2 + 10] = Game.cc[1][var2];
         var0[131 + var2 + 10] = Game.cd[1][var2];
         var0[151 + var2 + 10] = Game.ce[1][var2];
         var0[171 + var2 + 10] = Game.cf[1][var2];
      }

      writeInt(Game.cg, var0, 191);
      writeInt(Game.playTimeMs, var0, 195);
      var0[199] = (byte)(Game.cq ? 1 : 0);
      writeInt(Game.mapHighlight, var0, 200);
      writeInt(Game.hintFlagsHigh, var0, 204);
      writeInt(Game.totalScore, var0, 208);
      var0[216] = (byte)(Game.bonusMode ? 1 : 0);
      var0[217] = (byte)Game.saveSlot;
   }

   // $VF: renamed from: b (byte[]) void
   public static void deserializeSave(byte[] var0) {
      Game.unlockFlags = readInt(var0, 1);
      Game.collectibleMask = readInt(var0, 5);
      Game.hintFlagsLow = readInt(var0, 9);
      Game.weaponsOwned = var0[13];

      for (int var1 = 0; var1 < 8; var1++) {
         Game.weaponLevel[var1] = var0[14 + var1];
         Game.weaponKills[var1] = readShort(var0, 22 + var1 * 2);
         Game.ammo[var1] = readShort(var0, 38 + var1 * 2);
      }

      Game.bolts = readInt(var0, 54);
      Game.doorBits = var0[58];
      Game.sectionSwitchBitsA = readInt(var0, 59);
      Game.sectionSwitchBitsB = readInt(var0, 63);
      Game.sectionSwitchBitsC = readInt(var0, 67);

      for (int var2 = 0; var2 < 10; var2++) {
         Game.ca[0][var2] = var0[71 + var2];
         Game.cb[0][var2] = var0[91 + var2];
         Game.cc[0][var2] = var0[111 + var2];
         Game.cd[0][var2] = var0[131 + var2];
         Game.ce[0][var2] = var0[151 + var2];
         Game.cf[0][var2] = var0[171 + var2];
         Game.ca[1][var2] = var0[71 + var2 + 10];
         Game.cb[1][var2] = var0[91 + var2 + 10];
         Game.cc[1][var2] = var0[111 + var2 + 10];
         Game.cd[1][var2] = var0[131 + var2 + 10];
         Game.ce[1][var2] = var0[151 + var2 + 10];
         Game.cf[1][var2] = var0[171 + var2 + 10];
      }

      Game.cg = readInt(var0, 191);
      Game.playTimeMs = readInt(var0, 195);
      Game.cq = var0[199] == 1;
      Game.mapHighlight = readInt(var0, 200);
      Game.hintFlagsHigh = readInt(var0, 204);
      Game.totalScore = readInt(var0, 208);
      Game.bonusMode = var0[216] != 0;
      Game.saveSlot = var0[217];
      Game.menuCursor = 0;
      Game.menuChoice = 0;
      Game.focusMapNode();
      Game.state = 3;
   }

   // $VF: renamed from: a (java.lang.String, int, java.lang.String) java.lang.String
   public static String substitute(String var0, int var1, String var2) {
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
