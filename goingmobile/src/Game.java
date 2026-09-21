import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import com.nokia.mid.ui.FullCanvas;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class Game extends FullCanvas implements Runnable {
   public static ratchetandclank a;
   private Display display;
   public byte b;
   public boolean c = false;
   public boolean d = false;
   public boolean e;
   public static int f = 35;
   public static int g = 100;
   public static byte h = 22;
   public static boolean i = false;
   public static boolean j = false;
   public static int k;
   public static int l;
   public static int m;
   public static Vector n;
   public static Vector o;
   public static Vector p;
   public static Vector q;
   public static int r = 0;
   public static int s = 0;
   public static boolean t = false;
   public static boolean u = false;
   public static boolean v = false;
   public static byte w;
   public static boolean x = false;
   public static int y = 0;
   public byte z;
   public static final String[] A = new String[]{"English", "Français", "Deutsch", "Italiano", "Español"};
   public static final String[] B = new String[]{"", "_fr", "_ge", "_it", "_sp"};
   public static byte C = 0;
   public byte D = 114;
   public StringBuffer E = new StringBuffer(14);
   private long lastMultitapTime = 0L;
   private int lastMultitapKey = -1;
   private int multitapCycle = 0;
   private static char[][] KEYPAD_CHARS = new char[][]{
      {'0'},
      {'_', '1'},
      {'a', 'b', 'c', '2', 'A', 'B', 'C'},
      {'d', 'e', 'f', '3', 'D', 'E', 'F'},
      {'g', 'h', 'i', '4', 'G', 'H', 'I'},
      {'j', 'k', 'l', '5', 'J', 'K', 'L'},
      {'m', 'n', 'o', '6', 'M', 'N', 'O'},
      {'p', 'q', 'r', 's', '7', 'P', 'Q', 'R', 'S'},
      {'t', 'u', 'v', '8', 'T', 'U', 'V'},
      {'w', 'x', 'y', 'z', '9', 'W', 'X', 'Y', 'Z'}
   };
   private String nameEntryWarning;
   public int[] F = new int[]{652482873, 766492548};
   public int[][] G = new int[2][8];
   private Image digitStripImage;
   public static Vector H = new Vector(1);
   private static byte[] menuSoftLeft;
   private static byte[] menuSoftRight;
   private static byte[] menuTitle;
   private static byte[] menuBackTarget;
   public static int I = 0;
   public static byte J = 0;
   public static byte K = 0;
   public static int L;
   public static int M = 0;
   public LevelMap N;
   public Random O;
   public short P = -1;
   public byte Q = 1;
   public byte R;
   public byte S;
   public short T;
   public short U;
   public short V = 0;
   public Enemy[] enemies;
   public Player player;
   public Projectile[] playerShots;
   public Projectile[] enemyShots;
   public static Image tileSetImage;
   public static Image enemySegmentImage;
   public long ac;
   public byte ad;
   public Image splashImage;
   public boolean af;
   public static Image hudIconImage;
   public static Image portraitImage;
   public static Image smallSpriteImage;
   public static Image playerImage;
   public static Image weaponImage;
   public static Image titaniumBoltImage;
   public static Image actorImage;
   public byte sprKind;
   public byte sprAnimState;
   public int sprAnimFrame;
   public byte sprFrameOffset;
   public byte sprOrientation;
   public int boltCount;
   public int[] storeAmmoNeeded;
   public static byte MAX_ENEMIES = 5;
   public static byte PLAYER_SLOT = MAX_ENEMIES;
   public short[] zipX1;
   public short[] zipY1;
   public short[] zipX2;
   public short[] zipY2;
   public short[] zipDx;
   public int[] zipDy;
   public int[] zipBaseY;
   public short[] titaniumBoltX;
   public short[] titaniumBoltY;
   public byte[] titaniumBoltType;
   public byte[] bossShellState;
   public byte[] bossShellTimer;
   public int[] bossHp;
   public byte bossAim = 0;
   public byte bossFireTick = 0;
   public byte bossFirePhase = 0;
   public static final byte[] BOSS_SHELL_COL = new byte[]{14, 14, 13, 13};
   public static final byte[] BOSS_SHELL_ROW = new byte[]{8, 9, 8, 9};
   public byte exitTileX;
   public byte exitTileY;
   public static byte TITANIUM_BOLTS_FOR_RYNO = 9;
   public static byte TITANIUM_BOLT_SLOTS = 60;
   public int area1BoltsTaken;
   public int area2BoltsTaken;
   public int[] pickupX;
   public int[] pickupY;
   public short[] pickupVx;
   public short[] pickupVy;
   public byte[] pickupType;
   public short[] platformX;
   public short[] platformY;
   public short[] platformDx;
   public short[] platformDy;
   public byte[] platformState;
   public int levelElapsedMs;
   public long lastTickMs;
   public byte bg;
   private byte cr;
   public int bh;
   public int bi = 0;
   public int bj = 0;
   public static final byte[] INFOLINK_MESSAGE_TICKS = new byte[]{
      1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1
   };
   public static final short[] INFOLINK_MESSAGE_IDS = new short[]{
      75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
   };
   public boolean weaponOverlayOpen = false;
   public String messageText;
   public int messagePortrait;
   public int messagePortraitFrame;
   public int messageScroll = -1;
   public int messageChar = -1;
   public int messageStringId;
   public byte messageTimer = 0;
   public int collectiblesTaken1;
   public int collectiblesTaken2;
   public int levelStateMask;
   public int introSeenMask;
   public boolean by;
   public boolean bz;
   public static int bA;
   public static int bB;
   public static int enemyKills;
   public static int baseScore;
   public static int boltsCollected;
   public static int levelsCleared;
   public static int bG;
   public static int bH;
   public static int specialKills;
   public static int timeBonus;
   public static long scoringStart;
   public static long timerStart;
   public static long levelTimeMs;
   public static long PAR_TIME_MS;
   public static int totalScore;
   public static int totalTitaniumBolts;
   public static int totalKills;
   public static int shotsFired;
   public static int enemyShotHits;
   public static int boxesHit;
   public static boolean noDamageBonus;
   public static boolean fastKillBonus;
   public static boolean allKilledBonus;
   public static boolean playerDamaged;
   public static int[] enemiesRemainingPerLevel;
   public static boolean[] enemiesCountedPerLevel;
   private Vector cs;
   private Vector ct;
   private int cu;
   private int cv;
   private int splashHoldoff;
   public int ca;
   private boolean isHidden;
   private long hiddenSince;
   public static int repaintDelay = 0;
   public long nowMs;
   public boolean repaintEachFrame;
   private boolean bossVulnerable;
   public final short[][] PORTRAIT_TABLE;
   public boolean scoringActive;

   private void k() {
      this.scoringActive = true;
      baseScore = 10000;
      bB = 0;
      bA = 0;
      enemyKills = 0;
      specialKills = 0;
      boltsCollected = 0;
      levelsCleared = 0;
      timeBonus = 0;
      timerStart = 0L;
      levelTimeMs = 0L;
      PAR_TIME_MS = 180000L;
      totalScore = 0;
      shotsFired = 0;
      enemyShotHits = 0;
      boxesHit = 0;
      noDamageBonus = false;
      fastKillBonus = false;
      playerDamaged = false;
      allKilledBonus = false;
      m();
      scoringStart = System.currentTimeMillis();
   }

   private int l() {
      int var1 = 0;

      for (int var2 = this.enemies.length - 1; var2 >= 0; var2--) {
         if (this.enemies[var2].kind != 4 && this.enemies[var2].kind != -1) {
            var1++;
         }
      }

      return var1;
   }

   private void f(int var1) {
      enemiesRemainingPerLevel[var1] = this.l();
   }

   private static void m() {
      for (int var0 = enemiesRemainingPerLevel.length - 1; var0 >= 0; var0--) {
         enemiesCountedPerLevel[var0] = false;
         enemiesRemainingPerLevel[var0] = 0;
      }
   }

   private static void a(byte var0) {
      int var10000;
      int var10001;
      switch (var0) {
         case 0:
            timerStart = System.currentTimeMillis();
            return;
         case 1:
            if (!playerDamaged) {
               if (shotsFired == 0) {
                  fastKillBonus = true;
               }

               if (shotsFired == enemyShotHits + boxesHit) {
                  noDamageBonus = true;
               }
            }

            if (specialKills == enemyKills) {
               allKilledBonus = true;
            }

            for (int var1 = enemiesRemainingPerLevel.length - 1; var1 >= 0; var1--) {
               if (enemiesRemainingPerLevel[var1] > 0) {
                  bB = bB + enemiesRemainingPerLevel[var1];
               }
            }

            baseScore = enemyKills * 100;
            totalScore = totalScore + baseScore;
            bG = boltsCollected * 1;
            bH = levelsCleared * 100000;
            totalScore = totalScore + bG;
            totalScore = totalScore + bH;
            levelTimeMs = timerStart - scoringStart;
            if (levelTimeMs < PAR_TIME_MS) {
               timeBonus = (int)((PAR_TIME_MS - levelTimeMs) / 1000L * 10L);
            }

            var10000 = totalScore;
            var10001 = timeBonus;
            break;
         case 2:
            if (fastKillBonus) {
               totalScore += 100000;
            }

            if (noDamageBonus) {
               totalScore += 100000;
            }

            if (allKilledBonus) {
               var10000 = totalScore;
               var10001 = 10000;
               break;
            }

            return;
         default:
            return;
      }

      totalScore = var10000 + var10001;
   }

   private int n() {
      int var2 = 0;

      for (int var1 = 0; var1 < 16; var1++) {
         if ((this.levelStateMask & 1 << var1) != 0) {
            var2++;
         }
      }

      return var2;
   }

   private void o() {
      this.ad = 0;
      this.splashImage = b("sony");
      this.digitStripImage = b("uc");
      this.af = true;
      this.b = 20;
      a("/o");
      J = 0;
      this.bg = 0;
      I = 0;
   }

   private void p() {
      boolean var1 = false;
      this.y();
      this.b = 19;
      this.bg = 0;
      I = 0;
      J = 21;
      a((byte)0);
      a((byte)1);
      a((byte)2);
      totalTitaniumBolts = totalTitaniumBolts + totalScore;
      q = ratchetandclank.c(ratchetandclank.strings[101 + (this.Q - 1)], 118);
      q.addElement(new String(""));
      a(ratchetandclank.c(this.i(63, enemyKills), 118));
      a(ratchetandclank.c(this.i(64, boltsCollected), 118));
      a(ratchetandclank.c(this.i(65, shotsFired), 118));
      a(ratchetandclank.c(this.i(66, enemyShotHits), 118));
      a(ratchetandclank.c(this.i(67, boxesHit), 118));
      a(ratchetandclank.c(ratchetandclank.strings[68] + " " + o((int)levelTimeMs), 118));
      a(ratchetandclank.c(this.i(111, totalScore), 118));
      q.addElement(new String(""));
      a(ratchetandclank.c(this.i(69, this.B()), 118));
      int var2 = 0;

      for (int var3 = 1; var3 < 8; var3++) {
         if ((this.player.ownedWeapons & 1 << var3) > 0) {
            var2++;
         }
      }

      a(ratchetandclank.c(this.i(70, var2), 118));
      l = q.size();
      k = 0;
   }

   private static void a(Vector var0) {
      while (var0.size() > 0) {
         Object var1 = var0.firstElement();
         q.addElement(var1);
         var0.removeElement(var1);
      }
   }

   private void q() {
      if (0 == this.b) {
         this.y();
         this.b = 19;
         this.bg = 0;
         I = 0;
         J = 9;
      }
   }

   private void r() {
      this.y();
      this.b = 19;
      this.bg = 0;
      I = 0;
      J = 8;
   }

   private void s() {
      this.y();
      a((byte)0);
      a((byte)1);
      a((byte)2);
      q = new Vector();
      Vector var10000;
      String[] var10001;
      byte var10002;
      if (a.a(ratchetandclank.strings[97]) < 118) {
         var10000 = q;
         var10001 = ratchetandclank.strings;
         var10002 = 97;
      } else {
         q.addElement(ratchetandclank.strings[98]);
         var10000 = q;
         var10001 = ratchetandclank.strings;
         var10002 = 99;
      }

      var10000.addElement(var10001[var10002]);
      q.addElement(new String(""));
      a(ratchetandclank.c(this.i(63, totalKills), 118));
      a(ratchetandclank.c(this.i(64, this.boltCount), 118));
      a(ratchetandclank.c(ratchetandclank.strings[68] + " " + o(this.levelElapsedMs), 118));
      a(ratchetandclank.c(this.i(111, totalTitaniumBolts), 118));
      a(ratchetandclank.c(this.i(69, this.B()), 118));
      l = q.size();
      k = 0;
      this.b = 19;
      this.bg = 0;
      I = 0;
      J = 20;
      this.ca = 10;
   }

   private static void a(String var0) {
      byte var1 = 0;
      byte var2 = 0;

      try {
         InputStream var7;
         menuSoftLeft = new byte[var1 = (byte)(var7 = var0.getClass().getResourceAsStream(var0)).read()];
         menuSoftRight = new byte[var1];
         menuTitle = new byte[var1];
         menuBackTarget = new byte[var1];

         for (int var8 = 0; var8 < var1; var8++) {
            var2 = (byte)var7.read();
            if (var8 == 2) {
               var2++;
            }

            menuTitle[var8] = (byte)var7.read();
            menuSoftLeft[var8] = (byte)var7.read();
            menuSoftRight[var8] = (byte)var7.read();
            menuBackTarget[var8] = (byte)var7.read();
            MenuItem[] var6 = new MenuItem[var2];

            for (int var9 = 0; var9 < var2; var9++) {
               byte var3;
               byte var4;
               byte var10000;
               if (var8 == 2 && var9 == var2 - 1) {
                  var3 = 0;
                  var4 = -112;
                  var10000 = -104;
               } else {
                  var3 = (byte)var7.read();
                  var4 = (byte)var7.read();
                  var10000 = (byte)var7.read();
               }

               byte var5 = var10000;
               var6[var9] = new MenuItem(var3, var5, var4);
            }

            H.addElement(var6);
         }

         var7.close();
      } catch (IOException var10) {
      }
   }

   private void a(Graphics var1) {
      if (this.splashImage != null) {
         Graphics var10000;
         int var10001;
         if (this.ad == 1) {
            var10000 = var1;
            var10001 = 16777215;
         } else {
            var10000 = var1;
            var10001 = 0;
         }

         var10000.setColor(var10001);
         var1.setClip(0, 0, 128, 128);
         var1.fillRect(0, 0, 128, 128);
         var1.drawImage(this.splashImage, 64, 64, 3);
         this.splashImage = null;
         System.gc();
      }
   }

   private static void a(Vector var0, Vector var1) {
      while (var1.size() > 0) {
         Object var2 = var1.firstElement();
         var0.addElement(var2);
         var1.removeElement(var2);
      }
   }

   private void b(Graphics var1) {
      if (this.D != 114) {
         Graphics var35;
         short var38;
         byte var41;
         if (this.D == 116) {
            var1.setClip(0, 0, 128, 128);
            var1.setColor(1052688);
            var1.fillRect(0, 0, 128, 128);
            this.a(var1, ratchetandclank.strings[133], 5);
            boolean var14 = false;
            a(var1, this.cs, false);
            var35 = var1;
            var38 = -1;
            var41 = 8;
         } else if (this.D == 117) {
            var1.setClip(0, 0, 128, 128);
            var1.setColor(0);
            var1.fillRect(0, 0, 128, 128);
            int var15 = this.a(var1, ratchetandclank.strings[133], 5);
            a(var1, this.ct, true);
            if (this.ct != null) {
               if (this.cu - k >= 0 && this.cu - k <= m - 1) {
                  this.b(var1, 1, (this.cu - k + 1) * 10 + var15);
               }

               if (this.cv - k >= 0 && this.cv - k <= m - 1) {
                  this.b(var1, 0, (this.cv - k + 1) * 10 + var15);
               }
            }

            var35 = var1;
            var38 = 140;
            var41 = 8;
         } else if (this.D == 115) {
            var1.setClip(0, 0, 128, 128);
            var1.setColor(0);
            var1.fillRect(0, 0, 128, 128);
            Game var36;
            Graphics var39;
            String var42;
            if (this.nameEntryWarning == null) {
               var36 = this;
               var39 = var1;
               var42 = ratchetandclank.strings[139];
            } else {
               var36 = this;
               var39 = var1;
               var42 = this.nameEntryWarning;
            }

            int var16 = var36.a(var39, var42, 0) + 5;
            String var18 = this.E.toString();
            if (System.currentTimeMillis() - this.lastMultitapTime > 1500L && this.E.length() < 14) {
               var18 = var18 + "|";
            }

            this.a(var1, var18, var16);
            var35 = var1;
            var38 = 140;
            var41 = 8;
         } else {
            if (this.D != 118 && this.D != 120) {
               return;
            }

            var1.setClip(0, 0, 128, 128);
            var1.setColor(0);
            var1.fillRect(0, 0, 128, 128);
            int var17 = 128 / a.fontLineHeight - 1;
            int var19 = 3;
            if (var17 < l + 1) {
               int var28 = 0;
               if (k > var17 / 2) {
                  var28 = k - var17 / 2;
               }

               if (k > l - (var17 - var17 / 2)) {
                  var28 = l - var17 + 1;
               }

               for (int var30 = 0; var30 < Math.min(var17, l); var30++) {
                  var19 = this.a(var1, A[var28 + var30], var19, var28 + var30 == k);
               }
            } else {
               var19 = 3 + (var17 - l - 1) * a.fontLineHeight / 2;

               for (int var27 = 0; var27 < l + 1; var27++) {
                  var19 = this.a(var1, A[var27], var19, var27 == k);
               }
            }

            if (this.D == 120) {
               var35 = var1;
               var38 = 7;
               var41 = 8;
            } else {
               var35 = var1;
               var38 = 7;
               var41 = -1;
            }
         }

         c(var35, var38, var41);
      } else {
         boolean var6 = false;
         Object var7 = null;
         int var8 = 0;
         var8 = ((Object[])(var7 = (MenuItem[])H.elementAt(J))).length;
         if (J == 8) {
            var8 = this.n();
         }

         var1.setClip(0, 0, 128, 128);
         var1.setColor(0);
         var1.fillRect(0, 0, 128, 128);
         int var22;
         if (J == 18 && (this.player.ownedWeapons & 1 << this.cr + 1) != 0) {
            var22 = this.a(var1, ratchetandclank.strings[48], 5);
            var6 = true;
         } else if (J == 0) {
            var22 = this.a(var1, ratchetandclank.strings[menuTitle[J]], 5);
            var22 = this.a(var1, ratchetandclank.strings[95], var22);
            var22 += 5;
         } else {
            var22 = this.a(var1, ratchetandclank.strings[menuTitle[J]], 5);
         }

         if (J == 17 && (this.player.ownedWeapons & 128) == 0) {
            var8--;
         }

         if (J == 18) {
            var22 += 2;
            var22 = this.a(var1, ratchetandclank.strings[49 + this.cr], var22, false);
            var22 = this.a(var1, 56, this.boltCount, var22, false);
            Game var10000;
            Graphics var10001;
            byte var10002;
            int var10003;
            if (var6) {
               var10000 = this;
               var10001 = var1;
               var10002 = 57;
               var10003 = this.storeAmmoNeeded[this.cr + 1];
            } else {
               var10000 = this;
               var10001 = var1;
               var10002 = 57;
               var10003 = Player.h[this.cr + 1];
            }

            var22 = var10000.a(var10001, var10002, var10003, var22, false);
         }

         L = (116 - var22 - 14) / 12;
         if (L < 2) {
            L = 2;
         }

         int var9 = var22;
         var22 = (116 - var9 - 12 * (var8 > L ? L : var8) >> 1) + var9;
         c(var1, menuSoftLeft[J], menuSoftRight[J]);
         if (var8 != 0) {
            int var10 = I;
            M = 0;

            for (int var2 = 0; var2 < L && var10 < ((Object[])var7).length; var10++) {
               byte var5 = ((MenuItem)((Object[])var7)[var10]).type;
               byte var3 = ((MenuItem)((Object[])var7)[var10]).stringId;
               int var11 = var22;
               boolean var12 = true;
               if (var5 != 0 && (var5 != 10 || (this.player.ownedWeapons & 128) == 0)) {
                  if (var5 >= 1 && var5 <= 3) {
                     var22 = this.a(var1, --var5, var22, this.bg == var2 + I);
                  } else {
                     label224:
                     if (var5 == 4) {
                        Graphics var34;
                        Vector var37;
                        boolean var40;
                        label222: {
                           switch (((MenuItem)((Object[])var7)[0]).stringId) {
                              case 96:
                                 var34 = var1;
                                 var37 = o;
                                 var40 = false;
                                 break label222;
                              case 97:
                                 var34 = var1;
                                 var37 = n;
                                 break;
                              case 98:
                                 var34 = var1;
                                 var37 = p;
                                 break;
                              case 99:
                                 var34 = var1;
                                 var37 = q;
                                 break;
                              default:
                                 break label224;
                           }

                           var40 = true;
                        }

                        a(var34, var37, var40);
                     } else if (var5 == 5) {
                        var22 = this.a(var1, ratchetandclank.strings[var3 + (a.soundEnabled ? 0 : 1)], var22, this.bg == var2 + I);
                     } else if (var5 == 6 && (this.levelStateMask & 1 << var10) != 0) {
                        var22 = this.a(var1, ratchetandclank.strings[var3], var22, this.bg == var2 + I);
                     } else if (var5 == 8) {
                        a(var1, q, true);
                     } else if (var5 != 9) {
                        var12 = false;
                     }
                  }
               } else {
                  var22 = this.a(var1, ratchetandclank.strings[var3 & 0xFF], var22, this.bg == var2 + I);
               }

               int var13;
               if ((var13 = (var22 - var11) / 10) > 1) {
                  M = var13 - 1;
               }

               if (var12) {
                  var22 += 2;
                  var2++;
               }
            }

            if (++K > 6) {
               DirectGraphics var33 = DirectUtils.getDirectGraphics(var1);
               if (I > 0) {
                  var33.fillTriangle(117, var9 + 4 + 7, 122, var9 + 4, 127, var9 + 4 + 7, -265783);
                  var33.fillTriangle(1, var9 + 4 + 7, 6, var9 + 4, 11, var9 + 4 + 7, -265783);
               }

               if (I + (L - M) < var8) {
                  var33.fillTriangle(117, 108, 122, 115, 127, 108, -265783);
                  var33.fillTriangle(1, 108, 6, 115, 11, 108, -265783);
               }

               if (K > 24) {
                  K = 0;
               }
            }
         }
      }
   }

   private int b(Graphics var1, int var2, int var3) {
      int var4 = this.digitStripImage.getHeight();
      int var5 = 128 - (8 * var4 + 14) - 8 >> 1;

      for (int var6 = 0; var6 < 8; var5 += var4 + 2) {
         var1.setClip(var5, var3, var4, var4);
         var1.drawImage(this.digitStripImage, var5 - this.G[var2][var6] * var4, var3, 20);
         var6++;
      }

      var1.setClip(0, 0, 128, 128);
      return var3 + 14;
   }

   private void f(int var1, int var2) {
      byte var3 = 0;
      byte var4 = -1;
      boolean var6 = true;
      if (this.ca > 0) {
         this.ca--;
      }

      MenuItem[] var7;
      int var5 = (var7 = (MenuItem[])H.elementAt(J)).length;
      if (J == 17 && (this.player.ownedWeapons & 128) == 0) {
         var5--;
      }

      if (var5 != 0) {
         var3 = var7[0].type;
         var6 = var7[this.bg].enabled;
         var4 = var7[this.bg].action;
      }

      if (J == 8) {
         var5 = this.n();
      }

      int var10000;
      label399: {
         if ((var2 == -5 || var1 == -6) && var6) {
            if (var4 != -1) {
               if (this.D != 114) {
                  if (this.D == 117) {
                     this.D = 114;
                     this.cs = this.ct = null;
                     this.cr = this.bg = 0;
                     I = 0;
                     return;
                  }

                  Game var20;
                  byte var21;
                  if (this.D == 115) {
                     if (this.E.length() < 4) {
                        this.nameEntryWarning = ratchetandclank.strings[137];
                        return;
                     }

                     this.nameEntryWarning = null;
                     var20 = this;
                     var21 = 117;
                  } else {
                     if (this.D != 118 && this.D != 120) {
                        return;
                     }

                     C = (byte)k;
                     var20 = this;
                     var21 = 119;
                  }

                  var20.D = var21;
                  return;
               }

               if (var4 == 0) {
                  this.weaponOverlayOpen = false;
               } else {
                  if (var4 == -105) {
                     Game var19;
                     byte var10001;
                     if (a.e(1) != 0) {
                        var19 = this;
                        var10001 = 115;
                     } else {
                        var19 = this;
                        var10001 = 116;
                     }

                     var19.D = var10001;
                     return;
                  }

                  if (var4 == -104) {
                     l = A.length - 1;
                     m = 0;
                     k = 0;
                     this.D = 120;
                     return;
                  }

                  if (var4 == 127) {
                     q = null;
                     p = null;
                     o = null;
                     n = null;
                     System.gc();
                     a(20);
                     this.h(1);
                     this.e = true;
                     this.b = 0;
                     this.w();
                     return;
                  }

                  if (var4 == 126) {
                     a.a();
                     return;
                  }

                  if (var4 == 125) {
                     if (a.saveSlotFlags[this.bg] == 0) {
                        return;
                     }

                     q = null;
                     p = null;
                     o = null;
                     n = null;
                     System.gc();
                     a(20);
                     this.A();
                     a.b(this.bg);
                     this.b = 19;
                     var4 = 8;
                  } else if (var4 == 124) {
                     if ((this.player.ownedWeapons & 1 << this.cr + 1) == 0) {
                        var4 = 19;
                        if (this.boltCount >= Player.h[this.cr + 1]) {
                           this.boltCount = this.boltCount - Player.h[this.cr + 1];
                           this.player.ownedWeapons = (byte)(this.player.ownedWeapons | 1 << this.cr + 1);
                           this.player.ammo[this.cr + 1] = Player.e[this.cr + 1];
                           var4 = 17;
                        }
                     } else {
                        var4 = 19;
                        if (this.boltCount >= this.storeAmmoNeeded[this.cr + 1]) {
                           this.boltCount = this.boltCount - this.storeAmmoNeeded[this.cr + 1];
                           this.player.ammo[this.cr + 1] = Player.d[(this.cr + 1) * 3 + this.player.weaponLevel[this.cr + 1]];
                           var4 = 17;
                        }
                     }
                  } else if (var4 == 123) {
                     a.c(this.cr);
                     var4 = menuBackTarget[J];
                  } else {
                     if (var4 == 122) {
                        x();
                        this.e = true;
                        this.b = 0;
                        this.w();
                        return;
                     }

                     if (var4 == 121) {
                        if (a.saveSlotFlags[this.bg] == 0) {
                           a.a(this.bg);
                           a.b();
                           var4 = 12;
                        } else {
                           var4 = 11;
                        }
                     } else if (var4 == 120) {
                        a.a(this.cr);
                        a.b();
                        var4 = 12;
                     } else if (var4 == 119) {
                        int var8 = 0;

                        for (int var9 = this.bg; var9 >= 0; var8++) {
                           if ((this.levelStateMask & 1 << var8) != 0) {
                              var9--;
                           }
                        }

                        if (var8 != 12) {
                           if (var8 == 11) {
                              var8++;
                           }

                           this.splashHoldoff = 5;
                           this.i(var8);
                           this.e = true;
                           this.b = 0;
                           this.w();
                           return;
                        }

                        var4 = var7[var8 - 1].action;
                     }
                  }
               }

               if (var4 != -1 && var7[this.bg].enabled) {
                  if (var4 == 4 && J == 3 && var3 == 1 && a.saveSlotFlags[this.bg] == 0) {
                     return;
                  }

                  J = var4;
                  this.cr = this.bg;
                  this.bg = 0;
                  I = 0;
                  if ((var7 = (MenuItem[])H.elementAt(J)).length != 0) {
                     var3 = var7[0].type;
                     if (var7[0].type == 0 && J == 0) {
                        if (!this.c) {
                           this.c = true;
                           if (a.soundEnabled && a.soundPlayer != null) {
                              a.soundPlayer.queue(6, -1);
                           }
                        }
                     } else if (var3 >= 1 && var3 <= 3) {
                        a.b();
                     } else {
                        label259:
                        if (var3 == 4) {
                           Vector var18;
                           switch (var7[0].stringId) {
                              case 96:
                                 if (o == null) {
                                    o = ratchetandclank.b("/help", 118);
                                 }

                                 var18 = o;
                                 break;
                              case 97:
                                 if (n == null) {
                                    n = ratchetandclank.b("/about", 118);

                                    for (int var16 = 0; var16 < n.size(); var16++) {
                                       int var10;
                                       String var17;
                                       if ((var10 = (var17 = (String)n.elementAt(var16)).indexOf("??")) > 0) {
                                          String var11;
                                          if ((var11 = a.getAppProperty("MIDlet-Version")) != null) {
                                             n.setElementAt(var17.substring(0, var10) + var11, var16);
                                          } else {
                                             n.setElementAt(var17.substring(0, var10) + "?.?.?", var16);
                                          }
                                          break;
                                       }
                                    }
                                 }

                                 var18 = n;
                                 break;
                              case 98:
                                 if (p == null) {
                                    p = ratchetandclank.b("/credits", 118);
                                 }

                                 var18 = p;
                                 break;
                              default:
                                 break label259;
                           }

                           l = var18.size();
                           k = 0;
                        }
                     }

                     if (J == 17) {
                        this.O();
                        return;
                     }

                     return;
                  }
               }

               return;
            }

            if (var3 == 5 && (J == 2 || J == 13)) {
               a.soundEnabled = !a.soundEnabled;
               repaintDelay = 5;
               return;
            }

            if (var2 == -5 && var3 == 8 && J == 21) {
               J = 8;
               this.cr = this.bg = 0;
            } else if (var2 == -5 && J == 12) {
               J = 10;
               this.cr = this.bg = 0;
            } else {
               if (var2 != -5 || J != 20 || this.ca != 0) {
                  return;
               }

               q = null;
               J = 0;
               if (!this.c) {
                  this.c = true;
                  if (a.soundEnabled && a.soundPlayer != null) {
                     a.soundPlayer.queue(6, -1);
                  }
               }

               this.cr = this.bg = 0;
            }
         } else {
            label419: {
               if (var1 != -7 && var1 != 0) {
                  if (var2 == -2) {
                     if (var3 != 4 && var3 != 8 && var3 != 9 && this.D == 114) {
                        if (this.bg >= var5 - 1) {
                           return;
                        }

                        this.bg++;
                        var10000 = this.bg >= I + (L - M) ? I + 1 : 0;
                        break label399;
                     }

                     if (k + m < l) {
                        k++;
                        return;
                     }

                     return;
                  }

                  if (var2 != -1) {
                     return;
                  }

                  if (var3 != 4 && var3 != 8 && var3 != 9 && this.D == 114) {
                     if (this.bg <= 0) {
                        return;
                     }

                     this.bg--;
                     if (I > 0) {
                        var10000 = I - 1;
                        break label399;
                     }
                     break label419;
                  }

                  if (k > 0) {
                     k--;
                     return;
                  }

                  return;
               }

               if (this.D != 114) {
                  if (this.D == 116) {
                     this.D = 114;
                     this.cs = this.ct = null;
                     this.cr = this.bg = 0;
                     I = 0;
                     H.elementAt(J);
                     return;
                  }

                  if (this.D == 115) {
                     this.cr = this.bg = 0;
                     I = 0;
                     this.nameEntryWarning = null;
                     this.D = 114;
                     return;
                  }

                  if (this.D == 117) {
                     this.ct = null;
                     this.D = 115;
                     return;
                  }

                  if (this.D == 120) {
                     this.D = 114;
                     this.cr = this.bg = 0;
                     I = 0;
                     H.elementAt(J);
                     m = (116 - (5 + a.fontLineHeight + 4)) / 10;
                  }

                  return;
               }

               if (menuBackTarget[J] == 122) {
                  x();
                  this.e = true;
                  this.b = 0;
                  this.w();
                  return;
               }

               if (menuBackTarget[J] != -1) {
                  if (J == 19) {
                     J = 17;
                  } else {
                     J = menuBackTarget[J];
                     if (J == 0 && !this.c) {
                        this.c = true;
                        if (a.soundEnabled && a.soundPlayer != null) {
                           a.soundPlayer.queue(6, -1);
                        }
                     }
                  }

                  this.cr = this.bg = 0;
                  I = 0;
                  if ((var7 = (MenuItem[])H.elementAt(J)).length != 0) {
                     var3 = var7[0].type;
                     if (var7[0].type >= 1 && var3 <= 3) {
                        a.b();
                        return;
                     }

                     return;
                  }
               }

               return;
            }
         }

         var10000 = 0;
      }

      I = var10000;
   }

   public Game(ratchetandclank var1) {
      short[] var10000 = new short[]{1, 1, 2, 2, 3, 3, 3, 4, 4, 5, 6, 6, 7, 8, 9, 9, 10, 12, 11, 13};
      var10000 = new short[]{0, 2, 0, 1, 0, 2, 4, 0, 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0};
      this.by = false;
      this.bz = false;
      this.splashHoldoff = 2;
      this.ca = 0;
      this.isHidden = false;
      this.hiddenSince = 0L;
      this.repaintEachFrame = true;
      this.PORTRAIT_TABLE = new short[][]{
         {0, 112, 1, 113, 0, 114},
         {1, 115, 0, 116, 1, 117},
         {0, 118, 1, 119},
         {1, 125},
         {0, 126},
         {1, 127},
         new short[0],
         {0, 128},
         new short[0],
         {1, 129, -1, 84},
         {1, 130, 0, 131, 1, 132},
         {2, 120, 1, 121, 2, 122, 0, 123, 2, 124, -1, 77}
      };
      a = var1;
      this.display = Display.getDisplay(a);
      this.ad = 0;
      this.af = true;
      this.b = 20;
      this.splashImage = b("sony");
   }

   private void t() {
      this.z();
      String var1;
      if ((var1 = a.getAppProperty("MIDlet-Spec-Code")) != null) {
         j = var1.equals("true");
      }
   }

   public final void keyPressed(int var1) {
      if (var1 != -10) {
         if (this.splashHoldoff <= 0) {
            if (this.D != 115 || (var1 < 48 || var1 > 57) && var1 != 42 && var1 != 0) {
               int var5;
               label77: {
                  var5 = 0;
                  int var10000;
                  if (var1 == 50) {
                     var10000 = -1;
                  } else if (var1 == 56) {
                     var10000 = -2;
                  } else if (var1 == 52) {
                     var10000 = -3;
                  } else if (var1 == 54) {
                     var10000 = -4;
                  } else if (var1 == 53) {
                     var10000 = -5;
                  } else {
                     if (var1 != -5 && (var1 == -6 || var1 == -7)) {
                        break label77;
                     }

                     var10000 = var1;
                  }

                  var5 = var10000;
               }

               switch (this.b) {
                  case 0:
                     this.h(var1, var5);
                     return;
                  case 19:
                     this.f(var1, var5);
                     return;
                  case 20:
                     this.ac = 0L;
               }
            } else {
               long var2 = System.currentTimeMillis();
               int var4 = var1 - 48;
               if (var1 == 42 || var1 == 0) {
                  this.lastMultitapTime = var2;
                  this.multitapCycle = 0;
                  this.lastMultitapKey = -1;
                  if (this.E.length() > 0) {
                     this.E.deleteCharAt(this.E.length() - 1);
                     return;
                  }
               } else if (var4 >= 0) {
                  if ((this.lastMultitapKey != var4 || var2 - this.lastMultitapTime > 1500L) && this.E.length() < this.E.capacity()) {
                     this.lastMultitapTime = var2;
                     this.multitapCycle = 0;
                     this.lastMultitapKey = var4;
                     this.E.append(KEYPAD_CHARS[var4][0]);
                     return;
                  }

                  if (this.lastMultitapKey == var4 && var2 - this.lastMultitapTime <= 1500L) {
                     this.lastMultitapTime = var2;
                     this.multitapCycle = (this.multitapCycle + 1) % KEYPAD_CHARS[var4].length;
                     this.lastMultitapKey = var4;
                     this.E.setCharAt(this.E.length() - 1, KEYPAD_CHARS[var4][this.multitapCycle]);
                     return;
                  }

                  this.lastMultitapTime = 0L;
                  this.multitapCycle = 0;
                  this.lastMultitapKey = -1;
               }
            }
         }
      }
   }

   public final void keyReleased(int var1) {
      if (var1 != -10) {
         if (this.splashHoldoff <= 0) {
            this.i();
         }
      }
   }

   private static void c(Graphics var0, int var1, int var2) {
      var0.setClip(0, 0, 128, 128);
      if (var1 >= 0) {
         String var3 = ratchetandclank.strings[var1];
         int var4 = a.fontLineHeight;
         int var5 = 128 - var4;
         int var6 = a.a(var3);
         var0.setColor(160);
         var0.fillRect(0, var5 - 1, var6 + 2, var4 + 1);
         var0.setColor(16777215);
         a.a(var0, var3, 1, var5, 20);
      }

      if (var2 >= 0) {
         String var7 = ratchetandclank.strings[var2];
         int var8 = a.fontLineHeight;
         int var9 = 128 - var8;
         int var10 = a.a(var7);
         var0.setColor(160);
         var0.fillRect(128 - var10 - 2, var9 - 1, var10 + 2, var8 + 1);
         var0.setColor(16777215);
         a.a(var0, var7, 128 - var10 - 1, var9, 20);
      }
   }

   public final void paint(Graphics var1) {
      if (this.splashHoldoff > 0) {
         this.splashHoldoff--;
      }

      if (this.b == 19) {
         this.b(var1);
      } else if (this.b == 20) {
         if (this.af) {
            this.af = false;
            this.a(var1);
         }
      } else {
         if (this.by) {
            this.player.invulnTimer = 1;
         }

         this.N.a(var1);
         this.d(var1);
         this.f(var1);
         this.g(var1);
         this.e(var1);
         this.h(var1);
         if (this.Q == 12) {
            this.i(var1);
         }

         if (this.e) {
            this.c(var1);
            this.e = false;
         }

         if (this.player.actionState == -1) {
            var1.setClip(59, 20, 11, 12);
            var1.drawImage(hudIconImage, 59, -64, 0);
         }

         if (this.weaponOverlayOpen) {
            j(var1);
            this.messageChar = this.a(var1, this.messageScroll);
         }
      }
   }

   private static void a(Graphics var0, Vector var1, boolean var2) {
      if (var1 != null) {
         if (var1.size() != 0) {
            int var3 = 5 + a.fontLineHeight + 4;
            var0.setColor(16777215);
            if (m < l) {
               int var4 = m * 10;
               int var5 = m * var4 / l;
               int var6 = var3 + k * (var4 - var5) / (l - m);
               var0.drawRect(122, var3, 3, var4);
               var0.fillRect(122, var6, 3, var5);
            }

            for (int var7 = k; var7 < l && var7 < k + m; var3 += 10) {
               String var8 = (String)var1.elementAt(var7);
               a.a(var0, var8, !var2 ? 2 : 118 - a.a(var8) >> 1, var3, 20);
               var7++;
            }
         }
      }
   }

   private void u() {
      for (int var3 = 3; var3 >= 0; var3--) {
         if (this.platformState[var3] != -1) {
            short[] var5;
         byte[] varBd;
            int var10001;
            int var10002;
            label35: {
               switch (this.platformState[var3]) {
                  case 0:
                     this.platformDx[var3]++;
                     if (this.platformDx[var3] <= 44) {
                        continue;
                     }

                     this.platformDx[var3]--;
                     varBd = this.platformState;
                     var10001 = var3;
                     var10002 = this.platformState[var3] + 2;
                     break label35;
                  case 1:
                     this.platformDy[var3]++;
                     if (this.platformDy[var3] <= 28) {
                        continue;
                     }

                     this.platformDy[var3]--;
                     varBd = this.platformState;
                     var10001 = var3;
                     var10002 = this.platformState[var3] + 2;
                     break label35;
                  case 2:
                     this.platformDx[var3]--;
                     if (this.platformDx[var3] >= 0) {
                        continue;
                     }

                     var5 = this.platformDx;
                     break;
                  case 3:
                     this.platformDy[var3]--;
                     if (this.platformDy[var3] < 0) {
                        var5 = this.platformDy;
                        break;
                     }
                  default:
                     continue;
               }

               var5[var3]++;
               varBd = this.platformState;
               var10001 = var3;
               var10002 = this.platformState[var3] - 2;
            }

            varBd[var10001] = (byte)var10002;
         }
      }
   }

   public static void a(int var0) {
      try {
         Thread.sleep(var0);
      } catch (Exception var2) {
      }
   }

   public final void showNotify() {
      this.b();
   }

   public final void hideNotify() {
      this.a();
   }

   public final void a() {
      if (!this.isHidden && System.currentTimeMillis() - this.hiddenSince > 200L) {
         this.isHidden = true;
         this.q();
         if (a.soundPlayer != null) {
            a.soundPlayer.stop();
         }

         if (this.player != null) {
            this.player.velY = 0;
            this.player.jumpPhase = -1;
         }

         this.bh = 0;
         this.bi = 0;
      }
   }

   public final void b() {
      if (this.isHidden) {
         this.isHidden = false;
         if (this.b == 20) {
            this.af = true;
         } else if (this.b == 19 && this.D != 118) {
            repaintDelay = 4;
         }

         this.hiddenSince = System.currentTimeMillis();
      }
   }

   public final void c() {
      this.display.setCurrent(this);
      new Thread(this).start();
      this.ac = System.currentTimeMillis();
   }

   public final void run() {
      label16: {
         this.nowMs = System.currentTimeMillis();
         if (!this.isHidden) {
            if (this.nowMs - this.lastTickMs <= 100L) {
               break label16;
            }

            this.v();
            if (this.repaintEachFrame) {
               this.repaint();
            }
         }

         this.lastTickMs = this.nowMs;
      }

      this.display.callSerially(this);
      Thread.yield();
   }

   private void v() {
      this.repaintEachFrame = true;
      if (repaintDelay > 0 && --repaintDelay == 0) {
         if (this.c && a.soundPlayer != null) {
            if (a.soundEnabled) {
               a.soundPlayer.queue(6, -1);
            } else {
               a.soundPlayer.stop();
            }
         }

         a.a((byte)(a.soundEnabled ? 1 : 0), 0);
      }

      this.M();
      if (this.b == 0 && !this.weaponOverlayOpen) {
         this.levelElapsedMs = (int)(this.levelElapsedMs + (this.nowMs - this.lastTickMs));
         this.F();
         this.u();
         this.player.j();
         this.player.k();
         if (this.player.invulnTimer <= 0) {
            this.K();
         } else {
            this.player.invulnTimer--;
         }

         this.w();
         this.G();

         for (int var1 = 9; var1 >= 0; var1--) {
            this.playerShots[var1].b(false);
            if (this.playerShots[var1].type != -1) {
               this.e(var1);
            }
         }

         for (int var4 = 9; var4 >= 0; var4--) {
            this.enemyShots[var4].b(true);
            if (this.enemyShots[var4].type != -1) {
               this.k(var4);
            }
         }

         for (int var5 = MAX_ENEMIES - 1; var5 >= 0; var5--) {
            if (this.enemies[var5].kind != -1) {
               if (this.Q == 12 && (this.enemies[var5].kind == 1 || this.enemies[var5].kind == 4)) {
                  if (this.enemies[var5].B > 80) {
                     this.enemies[var5].kind = -1;
                     continue;
                  }

                  this.enemies[var5].B++;
               }

               if (this.enemies[var5].animState != 5 && this.player.animState != 10) {
                  this.n(var5);
               }

               this.enemies[var5].d();
               this.enemies[var5].e();
            }
         }

         if (this.Q == 12) {
            this.I();
         }
      }

      if (this.D != 114) {
         if (this.D == 115) {
            return;
         }

         if (this.D == 117) {
            this.a(this.E.toString(), 0);
            this.a(this.E.toString(), 1);
            if (this.ct == null) {
               String var2 = new String("");
               String var3 = (this.E.length() > 7
                     ? new StringBuffer().append(this.E.toString().substring(0, 7)).append('^').append(this.E.toString().substring(7, this.E.length()))
                     : this.E)
                  .toString();
               this.ct = new Vector();
               a(this.ct, ratchetandclank.c(ratchetandclank.strings[141], 94));
               a(this.ct, ratchetandclank.c(var3, 94));
               this.ct.addElement(var2);
               a(this.ct, ratchetandclank.c(ratchetandclank.strings[142], 94));
               this.cu = this.ct.size();
               this.ct.addElement(var2);
               this.ct.addElement(var2);
               a(this.ct, ratchetandclank.c(ratchetandclank.strings[143], 94));
               this.cv = this.ct.size();
               this.ct.addElement(var2);
               this.ct.addElement(var2);
               a(this.ct, ratchetandclank.c(ratchetandclank.strings[135], 94));
               l = this.ct.size();
               k = 0;
               return;
            }
         } else if (this.D == 116) {
            if (this.cs == null) {
               this.cs = new Vector();
               a(this.cs, ratchetandclank.c(ratchetandclank.strings[134], 120));
               this.cs.addElement("");
               a(this.cs, ratchetandclank.c(ratchetandclank.strings[135], 120));
               l = this.cs.size();
               k = 0;
               return;
            }
         } else if (this.D == 119) {
            ratchetandclank.strings = null;
            q = null;
            p = null;
            o = null;
            n = null;
            ratchetandclank.strings = ratchetandclank.a("/m" + B[C], 145);
            a.a(C, 2);
            this.D = 114;
            m = (116 - (5 + a.fontLineHeight + 4)) / 10;
            if (a.soundEnabled && a.soundPlayer != null) {
               a.soundPlayer.queue(6, -1);
            }
         }
      }
   }

   private void a(String var1, int var2) {
      String var3 = var1.toUpperCase();
      int var5 = 0;
      int var7 = var3.length();

      for (int var8 = 0; var8 < var7; var8++) {
         char var6 = var3.charAt(var8);
         var5 += var6 * (var8 + 1);
      }

      int var4 = var5 * this.F[var2];

      for (int var12 = 0; var12 < 8; var12++) {
         int var9 = var12 * 4;
         int var11 = 15 << var9;
         int var10 = (var4 & var11) >> var9;
         this.G[var2][var12] = g(var10);
      }
   }

   private static int g(int var0) {
      if (var0 >= 0 && var0 <= 3) {
         return 0;
      } else if (var0 >= 4 && var0 <= 7) {
         return 1;
      } else {
         return var0 >= 8 && var0 <= 11 ? 2 : 3;
      }
   }

   private void w() {
      int var1 = 0;
      boolean var2 = false;
      short var4 = this.player.b();
      short var5 = this.player.c();
      if (!t) {
         if (this.player.facingRight) {
            var1 = -(var4 - 22);
         } else {
            var1 = -(var4 + 22 - 128);
         }
      } else {
         var1 = -(y - 64);
         if (var4 > y + 11 || var4 < y - 11 || this.player.c() == this.player.a(false)) {
            t = false;
         }
      }

      label46: {
         int var3 = -(u ? var5 + 22 - 128 + 15 : var5 + 22 + 28 - 128 - 15) - s;
         s += var3 >> 1;
         if (r > var1) {
            r -= 10;
            if (r >= var1) {
               break label46;
            }
         } else {
            if (r >= var1) {
               break label46;
            }

            r += 10;
            if (r <= var1) {
               break label46;
            }
         }

         r = var1;
      }

      if (r > 0) {
         r = 0;
      }

      if (r < -488) {
         r = -488;
      }

      if (s > 0) {
         s = 0;
      }

      if (s < -124) {
         s = -124;
      }
   }

   private static Image b(String var0) {
      try {
         return Image.createImage("/" + var0 + ".png");
      } catch (IOException var2) {
         return null;
      }
   }

   private static void x() {
      H.removeAllElements();
      if (tileSetImage == null) {
         tileSetImage = b("a");
      }

      if (actorImage == null) {
         actorImage = b("d");
      }

      if (enemySegmentImage == null) {
         enemySegmentImage = b("b");
      }

      if (playerImage == null) {
         playerImage = b("c");
      }

      if (titaniumBoltImage == null) {
         titaniumBoltImage = b("e");
      }

      if (weaponImage == null) {
         weaponImage = b("f");
      }

      if (hudIconImage == null) {
         hudIconImage = b("g");
      }

      if (portraitImage == null) {
         portraitImage = b("p");
      }

      if (smallSpriteImage == null) {
         smallSpriteImage = b("h");
      }
   }

   private void y() {
      tileSetImage = null;
      enemySegmentImage = null;
      playerImage = null;
      actorImage = null;
      titaniumBoltImage = null;
      weaponImage = null;
      hudIconImage = null;
      portraitImage = null;
      smallSpriteImage = null;
      System.gc();
      a("/o");
   }

   private void z() {
      this.O = new Random();
      C();
      this.N = new LevelMap(this);
      this.player = new Player(this);
      this.player.a();
      this.enemies = new Enemy[MAX_ENEMIES];
      byte var10000 = MAX_ENEMIES;

      byte var1;
      while ((var1 = (byte)(var10000 - 1)) >= 0) {
         this.enemies[var1] = new Enemy(this);
         var10000 = var1;
      }

      this.enemies[0].a();
      this.playerShots = new Projectile[10];

      for (int var2 = 9; var2 >= 0; var2--) {
         this.playerShots[var2] = new Projectile(this);
      }

      this.enemyShots = new Projectile[10];

      for (int var3 = 9; var3 >= 0; var3--) {
         this.enemyShots[var3] = new Projectile(this);
      }

      this.bossShellState = new byte[4];
      this.bossShellTimer = new byte[4];
      this.bossHp = new int[5];
      this.zipX1 = new short[4];
      this.zipY1 = new short[4];
      this.zipX2 = new short[4];
      this.zipY2 = new short[4];
      this.zipDx = new short[4];
      this.zipDy = new int[4];
      this.zipBaseY = new int[4];
      this.titaniumBoltX = new short[20];
      this.titaniumBoltY = new short[20];
      this.titaniumBoltType = new byte[20];
      this.pickupX = new int[6];
      this.pickupY = new int[6];
      this.pickupType = new byte[6];
      this.pickupVx = new short[6];
      this.pickupVy = new short[6];
      this.platformX = new short[4];
      this.platformY = new short[4];
      this.platformDx = new short[4];
      this.platformDy = new short[4];
      this.platformState = new byte[4];
      enemiesRemainingPerLevel = new int[12];
      enemiesCountedPerLevel = new boolean[12];
      this.storeAmmoNeeded = new int[8];
   }

   private void h(int var1) {
      x();
      this.player.actionState = -2;
      this.by = false;
      this.A();
      this.Q = (byte)var1;
      this.N.a(var1);
      this.P = LevelMap.a[this.Q];
      this.N.a(this.P, true);
      this.player.posX = this.R * 22 + 11 << 8;
      this.player.row = this.player.spawnRow = this.S;
      this.player.animFrame = 1;
      this.player.animCounter = 0;
      this.player.animState = 1;
      this.player.animRestart = 0;
      this.player.posInRow = 0;
      this.player.kind = 0;
      this.player.activeFlag = 1;
      this.player.health = 20;
      this.levelElapsedMs = 0;
      this.lastTickMs = System.currentTimeMillis();
      this.f(this.P);
      this.player.invulnTimer = 0;
      this.player.facingRight = true;
      this.boltCount = 0;
      this.player.setAnimState((byte)0);
      this.player.animRestart = 0;
      r = this.T;
      s = this.U;
      this.e = true;
      this.b = 19;
   }

   private void A() {
      this.k();
      this.area1BoltsTaken = -1;
      this.area2BoltsTaken = -1;
      this.collectiblesTaken1 = -1;
      this.introSeenMask = -1;
      this.collectiblesTaken2 = -1;
      this.levelStateMask = 2049;
      this.player.ownedWeapons = 3;
      this.player.currentWeapon = 1;

      for (int var1 = 7; var1 >= 0; var1--) {
         this.player.weaponXp[var1] = 0;
         this.player.weaponLevel[var1] = 0;
         this.player.ammo[var1] = Player.e[var1];
      }

      this.c = false;
      if (a.soundPlayer != null) {
         a.soundPlayer.stop();
      }
   }

   private void i(int var1) {
      x();
      this.player.actionState = -2;
      this.by = false;
      this.Q = (byte)var1;
      this.N.a(var1);
      this.player.facingRight = true;
      this.P = LevelMap.a[this.Q];
      this.player.invulnTimer = 10;
      this.f(this.P);
      this.N.a(this.P, true);
      if (this.Q == 12) {
         this.J();
      }

      this.player.posX = this.R * 22 + 11 << 8;
      this.player.row = this.player.spawnRow = this.S;
      this.player.animFrame = 1;
      this.player.animCounter = 0;
      this.player.animState = 1;
      this.player.animRestart = 0;
      this.player.posInRow = 0;
      this.player.kind = 0;
      this.player.activeFlag = 1;
      this.player.health = 20;
      this.lastTickMs = System.currentTimeMillis();
      this.e = true;
      r = this.T;
      s = this.U;
      this.k();
      this.b = 0;
      this.w();
   }

   public final void b(int var1) {
      if (var1 >= 0 && var1 <= 19) {
         this.collectiblesTaken1 &= ~(1 << var1);
      } else {
         if (var1 >= 20 && var1 <= 39) {
            this.collectiblesTaken2 &= ~(1 << var1 - 20);
         }
      }
   }

   public final boolean c(int var1) {
      if (var1 >= 0 && var1 <= 19) {
         return (this.collectiblesTaken1 & 1 << var1) != 0;
      } else {
         return var1 < 20 || var1 > 39 ? false : (this.collectiblesTaken2 & 1 << var1 - 20) != 0;
      }
   }

   public final boolean a(int var1, int var2) {
      int var3;
      int var4;
      if (var1 > 0 && var1 <= 5) {
         var3 = (var1 - 1) * 6 + var2;
         var4 = this.area1BoltsTaken;
      } else {
         if (var1 < 6 || var1 > 10) {
            return false;
         }

         var3 = (var1 - 6) * 6 + var2;
         var4 = this.area2BoltsTaken;
      }

      return (var4 & 1 << var3) != 0;
   }

   private int B() {
      int var2 = 0;

      for (int var1 = (TITANIUM_BOLT_SLOTS >> 1) - 1; var1 >= 0; var1--) {
         if ((this.area1BoltsTaken & 1 << var1) == 0) {
            var2++;
         }

         if ((this.area2BoltsTaken & 1 << var1) == 0) {
            var2++;
         }
      }

      return var2;
   }

   private void g(int var1, int var2) {
      if (var1 > 0 && var1 <= 5) {
         int var4 = (var1 - 1) * 6 + var2;
         this.area1BoltsTaken &= ~(1 << var4);
      } else if (var1 >= 6 && var1 <= 10) {
         int var3 = (var1 - 6) * 6 + var2;
         this.area2BoltsTaken &= ~(1 << var3);
      }

      if (this.B() >= TITANIUM_BOLTS_FOR_RYNO && (this.player.ownedWeapons & 128) == 0) {
         this.e(72, -1);
         this.player.ownedWeapons = (byte)(this.player.ownedWeapons | 128);
      } else {
         this.e(74, -1);
      }
   }

   public final void d() {
      if (a.soundEnabled && a.soundPlayer != null) {
         a.soundPlayer.queue(0, 1);
      }

      this.player.actionState = -2;
      this.P = this.V;
      this.f(this.P);
      this.N.a(this.V, false);
      this.player.invulnTimer = 10;
      this.player.posX = this.R * 22 + 11 << 8;
      this.player.row = this.S;
      r = this.T;
      s = this.U;
      this.player.posInRow = 0;
      this.player.health = 20;
      this.player.velX = this.player.velY = 0;
      this.player.setAnimState((byte)0);
      this.player.animRestart = 0;
      this.e = true;
      if (this.Q == 12) {
         this.bossShellTimer[0] = this.bossShellTimer[1] = this.bossShellTimer[2] = this.bossShellTimer[3] = 0;
         this.bossShellState[0] = this.bossShellState[1] = this.bossShellState[2] = this.bossShellState[3] = 0;
         this.bossHp[0] = this.bossHp[1] = this.bossHp[2] = this.bossHp[3] = f;
         this.bossHp[4] = g;
      }
   }

   private void j(int var1) {
      if (var1 == -1) {
         this.cr = this.bg = 0;
         this.r();
      }
   }

   private static void C() {
      m = (116 - (5 + a.fontLineHeight + 4)) / 10;
   }

   public final void a(int var1, int var2, int var3) {
      this.player.swingTargetX = var1 * 22 + 11 << 8;
      this.player.swingTargetY = var2 * 14 + 7 - 2 << 8;
      byte var4 = 2;
      if (!this.player.facingRight) {
         var4 = -2;
      }

      this.player.swingCurX = (this.player.posX >> 8) + var4 << 8;
      this.player.swingCurY = this.player.row * 14 + (this.player.posInRow >> 8) + 3 << 8;
      this.player.swingStepX = (this.player.swingTargetX - this.player.swingCurX) / 6;
      this.player.swingStepY = (this.player.swingTargetY - this.player.swingCurY) / 6;
      if (this.player.swingTargetY < this.player.swingCurY) {
         this.player.swingTargetType = (byte)var3;
         this.player.actionState = 0;
         this.player.setAnimState((byte)5);
         this.player.animRestart = 2;
      }
   }

   public static int b(int var0, int var1) {
      int var2 = 252;
      int var3 = var1 / 14;
      int var4;
      if ((var4 = var0 / 22) > -1 && var4 < LevelMap.columnSolidMasks.length && (LevelMap.columnSolidMasks[var4] & 1 << var3 + 1) > 0) {
         var2 = (var3 + 1) * 14;
      }

      return var2;
   }

   public final short e() {
      short var1 = 252;
      int var2 = 0;
      if (this.player.ladderExitTimer != 0) {
         return 252;
      }

      for (int var3 = 3; var3 >= 0; var3--) {
         if (this.platformState[var3] != -1
            && this.platformX[var3] + this.platformDx[var3] <= (this.player.posX >> 8) + 4
            && this.platformX[var3] + this.platformDx[var3] + 22 >= (this.player.posX >> 8) - 4
            && (var2 = this.platformY[var3] + this.platformDy[var3]) < var1
            && var2 > this.player.c()) {
            var1 = (short)var2;
            this.player.platformUnder = 0;
            Player var10000;
            byte var10001;
            if (this.platformState[var3] == 0) {
               var10000 = this.player;
               var10001 = 1;
            } else {
               if (this.platformState[var3] != 2) {
                  continue;
               }

               var10000 = this.player;
               var10001 = -1;
            }

            var10000.platformUnder = var10001;
         }
      }

      return (short)(var1 - 14);
   }

   public final short f() {
      short var1 = 252;
      short var2 = this.player.c();
      int var3 = this.player.posX >> 8;

      for (int var6 = 19; var6 >= 0; var6--) {
         if (this.titaniumBoltType[var6] != -1 && this.titaniumBoltY[var6] >= var2 && this.titaniumBoltX[var6] <= var3 + 3 && this.titaniumBoltX[var6] + 16 >= var3 - 3 && this.titaniumBoltY[var6] < var1) {
            var1 = this.titaniumBoltY[var6];
         }
      }

      return (short)(var1 - 14);
   }

   public final boolean g() {
      int var6 = this.player.posX >> 8;
      short var7 = this.player.c();

      for (int var10 = 19; var10 >= 0; var10--) {
         if (this.titaniumBoltType[var10] >= 0
            && Math.abs(this.titaniumBoltX[var10] - var6) <= 22
            && Math.abs(this.titaniumBoltY[var10] - var7) <= 14
            && this.a(this.titaniumBoltX[var10], this.titaniumBoltY[var10], 16, 16, var6 - 4, var7, 8, 13)) {
            return true;
         }
      }

      return false;
   }

   public final boolean d(int var1) {
      int var9 = this.player.b() + 0;
      int var10 = this.player.c() + -6;
      byte var2 = this.enemies[var1].kind;
      if (this.enemies[var1].kind != -1 && var2 != 4) {
         if (Math.abs(this.enemies[var1].posX - this.player.posX) <= 5632 && Math.abs(this.enemies[var1].row - this.player.row) <= 3 && this.enemies[var1].animState != 5) {
            short var3 = this.enemies[var1].c();
            short var4 = this.enemies[var1].b();
            byte var5 = Enemy.a[var2];
            byte var6 = Enemy.b[var2];
            byte var7 = Enemy.c[var2];
            byte var8 = Enemy.d[var2];
            return this.a(var3 - var5, var4 + var6, var7, var8, var9 - 6, var10 + 3, 12, 20);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean D() {
      int var3 = this.player.posX >> 8;
      short var4 = this.player.c();
      int var5 = 0;

      for (int var6 = 19; var6 >= 0; var6--) {
         if (this.titaniumBoltType[var6] >= 0
            && ((var5 = this.titaniumBoltX[var6] + 8) <= var3 || this.player.facingRight)
            && (var5 >= var3 || !this.player.facingRight)
            && Math.abs(var5 - var3) <= 22
            && this.titaniumBoltY[var6] == var4) {
            return true;
         }
      }

      return false;
   }

   private boolean E() {
      for (int var2 = MAX_ENEMIES - 1; var2 >= 0; var2--) {
         byte var1 = this.enemies[var2].kind;
         if (this.enemies[var2].kind != -1
            && var1 != 4
            && (this.enemies[var2].posX <= this.player.posX || this.player.facingRight)
            && (this.enemies[var2].posX >= this.player.posX || !this.player.facingRight)
            && Math.abs(this.enemies[var2].posX - this.player.posX) <= 5632
            && this.enemies[var2].animState != 5
            && this.player.row == this.enemies[var2].row) {
            return true;
         }
      }

      return false;
   }

   public final int h() {
      int var8 = this.player.b() + 0;
      int var9 = this.player.c() + -6;

      for (int var14 = MAX_ENEMIES - 1; var14 >= 0; var14--) {
         byte var1 = this.enemies[var14].kind;
         if (this.enemies[var14].kind != -1
            && var1 != 4
            && Math.abs(this.enemies[var14].posX - this.player.posX) <= 5632
            && Math.abs(this.enemies[var14].row - this.player.row) <= 3
            && this.enemies[var14].animState != 5) {
            short var2 = this.enemies[var14].c();
            short var3 = this.enemies[var14].b();
            byte var4 = Enemy.a[var1];
            byte var5 = Enemy.b[var1];
            byte var6 = Enemy.c[var1];
            byte var7 = Enemy.d[var1];
            if (this.a(var2 - var4, var3 + var5, var6, var7, var8 - 6, var9 + 3, 12, 20)) {
               return var14;
            }
         }
      }

      return -1;
   }

   // $VF: Irreducible bytecode was duplicated to produce valid code
   private void h(int var1, int var2) {
      if (this.weaponOverlayOpen) {
         if (var2 == -5) {
            if (this.messageScroll >= 0) {
               this.messageScroll = this.messageChar;
            }

            if (this.messageScroll < 0) {
               this.L();
               this.messageChar = 0;
            }
         }
      } else if (var1 == -6 || var1 == -7 || var1 == 0) {
         this.bg = 0;
         if (a.soundPlayer != null) {
            a.soundPlayer.stop();
         }

         this.q();
      } else if (this.player.animState == 10) {
         this.bh = this.bi = 0;
         this.bj &= -129;
      } else {
         Game var8;
         int var9;
         label294: {
            if (var2 == -1) {
               if (this.player.animState == 11) {
                  return;
               }

               byte var3 = 0;
               if (this.player.actionState == -1) {
                  this.player.l();
               } else {
                  label289:
                  if (this.player.jumpPhase >= 0) {
                     Player var10000;
                     boolean var10001;
                     if (this.player.g() && !this.player.facingRight) {
                        if (!t) {
                           t = true;
                           y = this.player.b() - 4 + 11;
                        }

                        var3 = -1;
                        this.player.velX = 1250;
                        this.player.jumpPhase = 0;
                        var10000 = this.player;
                        var10001 = true;
                     } else {
                        if (!this.player.h() || !this.player.facingRight) {
                           break label289;
                        }

                        if (!t) {
                           t = true;
                           y = this.player.b() + 4 - 11;
                        }

                        var3 = 1;
                        this.player.velX = -1250;
                        this.player.jumpPhase = 0;
                        var10000 = this.player;
                        var10001 = false;
                     }

                     var10000.facingRight = var10001;
                     this.player.setAnimState((byte)4);
                     this.bi = this.bh = 0;
                  }
               }

               if (this.player.jumpPhase >= 1 && var3 == 0) {
                  return;
               }

               var8 = this;
            } else {
               if (var2 == -2) {
                  if (this.player.animState == 5) {
                     return;
                  }

                  if (this.player.jumpPhase == 2) {
                     this.player.setAnimState((byte)3);
                     this.player.velX = 0;
                     this.player.velY = 0;
                     this.player.jumpPhase = 1;
                     if ((this.player.ownedWeapons & 1) > 0) {
                        this.player.meleeActive = false;
                        this.bi = 0;
                        this.player.setAnimState((byte)14);
                        this.player.animRestart = 1;
                        this.player.weaponPose = 1;
                        return;
                     }

                     return;
                  } else {
                     if ((this.player.ownedWeapons & 1) > 0 && this.player.jumpPhase != -1 && this.player.animState != 11 && this.player.animState != 14) {
                        this.player.meleeActive = false;
                        this.bi = 0;
                        this.player.setAnimState((byte)14);
                        this.player.animRestart = 1;
                        this.player.weaponPose = 1;
                        return;
                     }

                     if (v) {
                        this.j(w);
                        return;
                     }

                     if (this.player.onLadder) {
                        this.player.onLadder = false;
                        this.player.row++;
                        this.player.i();
                        this.player.ladderExitTimer = 10;
                        if ((this.player.ownedWeapons & 1) > 0 && this.player.animState != 14) {
                           this.player.meleeActive = false;
                           this.bi = 0;
                           this.player.setAnimState((byte)14);
                           this.player.animRestart = 1;
                           this.player.weaponPose = 1;
                           return;
                        }
                     } else if (this.player.f() && this.player.velY == 0 && this.player.animState != 14 && this.player.animState != 11) {
                        this.player.i();
                        this.player.posInRow = Player.k;
                        if ((this.player.ownedWeapons & 1) > 0) {
                           this.player.meleeActive = false;
                           this.bi = 0;
                           this.player.setAnimState((byte)14);
                           this.player.animRestart = 1;
                           this.player.weaponPose = 1;
                           return;
                        }

                        return;
                     }

                     return;
                  }
               }

               if (var2 == -3) {
                  this.bi = this.bh = var2;
                  if (t && this.player.b() < y + 11 && this.player.velX > 0) {
                     this.bi = this.bh = 0;
                     return;
                  }

                  return;
               }

               if (var2 == -4) {
                  this.bi = this.bh = var2;
                  if (t && this.player.b() > y - 11 && this.player.velX < 0) {
                     this.bi = this.bh = 0;
                     return;
                  }

                  return;
               }

               if (var1 == 53 || var2 == -5) {
                  boolean var7 = false;
                  if ((this.player.ownedWeapons & 1) > 0 && this.player.animState != 11 && this.player.animState != 14 && this.player.animState != 8 && (this.E() || this.D() || this.player.ammo[this.player.currentWeapon] <= 0)) {
                     this.bi = 0;
                     this.player.meleeActive = false;
                     this.player.weaponPose = 0;
                     this.player.n();
                     return;
                  }

                  if (this.player.ammo[this.player.currentWeapon] > 0 && this.bj == 0) {
                     this.bj = 129;
                     if (this.player.m()) {
                        if (a.soundEnabled && a.soundPlayer != null) {
                           a.soundPlayer.queue(5, 1);
                        }

                        shotsFired++;
                        return;
                     }
                  }

                  return;
               }

               if (var1 == 42) {
                  this.bi = 0;
                  if ((this.player.ownedWeapons & 1) > 0) {
                     this.player.meleeActive = false;
                     this.player.weaponPose = 0;
                     this.player.n();
                     return;
                  }

                  return;
               }

               if (var1 == 57) {
                  if (j) {
                     i = !i;
                     return;
                  }

                  return;
               }

               if (var1 == 48) {
                  if (j) {
                     this.player.ownedWeapons = -1;
                     return;
                  }

                  return;
               }

               if (var1 == 55) {
                  if (j) {
                     if (this.Q != 12) {
                        this.e(INFOLINK_MESSAGE_IDS[this.Q - 1], this.Q == 3 ? 11 : (this.Q == 10 ? 9 : -1));
                        this.b(this.Q - 1);
                        return;
                     }

                     this.bossHp[4] = 0;
                     return;
                  }

                  return;
               }

               if (var1 == 35) {
                  this.bg = 0;
                  if (this.player.currentWeapon != 0) {
                     if (++this.player.currentWeapon > 7) {
                        this.player.currentWeapon = 1;
                     }

                     while ((this.player.ownedWeapons & 1 << this.player.currentWeapon) == 0) {
                        if (++this.player.currentWeapon > 7) {
                           this.player.currentWeapon = 1;
                        }
                     }

                     this.e = true;
                     return;
                  }

                  return;
               }

               if (var1 == 49) {
                  if (t || this.player.animState == 11) {
                     return;
                  }

                  if (this.player.actionState >= 0) {
                     return;
                  }

                  if (this.player.animState != 12) {
                     this.player.setAnimState((byte)0);
                  }

                  if (this.player.jumpPhase < 0) {
                     this.player.velY = 2560;
                     this.player.jumpPhase++;
                  } else if (this.player.jumpPhase < 1) {
                     this.player.velY = 1920;
                     this.player.jumpPhase++;
                     this.player.animRestart = 1;
                     this.player.setAnimState((byte)13);
                  }

                  var8 = this;
                  var9 = -3;
                  break label294;
               }

               if (var1 == 51) {
                  if (t || this.player.animState == 11) {
                     return;
                  }

                  if (this.player.actionState >= 0) {
                     return;
                  }

                  if (this.player.animState != 12) {
                     this.player.setAnimState((byte)0);
                  }

                  if (this.player.jumpPhase < 0) {
                     this.player.velY = 2560;
                     this.player.jumpPhase++;
                  } else if (this.player.jumpPhase < 1) {
                     this.player.velY = 1920;
                     this.player.jumpPhase++;
                     this.player.animRestart = 1;
                     this.player.setAnimState((byte)13);
                  }

                  var8 = this;
                  var9 = -4;
                  break label294;
               }

               var8 = this;
            }

            var9 = var2;
         }

         var8.bh = var9;
      }
   }

   public final void i() {
      this.bj &= -129;
      if (this.player.animState != 8) {
         this.player.o();
      }

      this.bh = 0;
      this.bi = 0;
   }

   private void k(int var1) {
      byte var4 = this.enemyShots[var1].type;
      if (this.enemyShots[var1].type != -1 && var4 != 30 && (var4 < 15 || var4 > 17)) {
         int var5 = (this.enemyShots[var1].posX >> 8) - this.enemyShots[var1].halfWidth;
         int var6 = (this.enemyShots[var1].posY >> 8) - this.enemyShots[var1].halfHeight;
         int var7 = this.enemyShots[var1].halfWidth << 1;
         int var8 = this.enemyShots[var1].halfHeight << 1;

         for (int var9 = 19; var9 >= 0; var9--) {
            if (this.titaniumBoltType[var9] != -1
               && this.titaniumBoltX[var9] + r <= 128
               && this.titaniumBoltX[var9] + 16 + r >= 0
               && this.titaniumBoltY[var9] + s <= 128
               && this.titaniumBoltY[var9] + 16 + s >= 0
               && this.a(this.titaniumBoltX[var9], this.titaniumBoltY[var9], 16, 16, var5, var6, var7, var8)) {
               this.enemyShots[var1].a(true);
            }
         }
      }
   }

   public final void e(int var1) {
      if (var1 == -1) {
         for (int var10 = 19; var10 >= 0; var10--) {
            if (this.titaniumBoltType[var10] != -1
               && Math.abs(this.titaniumBoltX[var10] - (this.player.posX >> 8)) <= 33
               && Math.abs(this.titaniumBoltY[var10] - this.player.c()) <= 28
               && this.a(
                  this.titaniumBoltX[var10],
                  this.titaniumBoltY[var10],
                  16,
                  16,
                  (this.player.posX >> 8) + (this.player.facingRight ? Player.l[this.player.weaponPose] : -Player.l[this.player.weaponPose] - Player.n[this.player.weaponPose]),
                  this.player.c() + Player.m[this.player.weaponPose],
                  Player.n[this.player.weaponPose],
                  Player.o[this.player.weaponPose]
               )) {
               if (a.soundEnabled && a.soundPlayer != null) {
                  a.soundPlayer.queue(4, 1);
               }

               boxesHit++;
               Game var11;
               short var12;
               short var13;
               byte var14;
               if (this.titaniumBoltType[var10] == 0) {
                  this.b(this.titaniumBoltX[var10], this.titaniumBoltY[var10], 0);
                  var11 = this;
                  var12 = this.titaniumBoltX[var10];
                  var13 = this.titaniumBoltY[var10];
                  var14 = 0;
               } else {
                  var11 = this;
                  var12 = this.titaniumBoltX[var10];
                  var13 = this.titaniumBoltY[var10];
                  var14 = this.titaniumBoltType[var10];
               }

               var11.b(var12, var13, var14);
               this.d(this.titaniumBoltX[var10] + 8 << 8, this.titaniumBoltY[var10] + 8 << 8, 30);
               this.titaniumBoltType[var10] = -1;
            }
         }
      } else {
         byte var4 = this.playerShots[var1].type;
         if (this.playerShots[var1].type != -1 && var4 != 30 && (var4 < 15 || var4 > 17)) {
            int var5 = (this.playerShots[var1].posX >> 8) - this.playerShots[var1].halfWidth;
            int var6 = (this.playerShots[var1].posY >> 8) - this.playerShots[var1].halfHeight;
            int var7 = this.playerShots[var1].halfWidth << 1;
            int var8 = this.playerShots[var1].halfHeight << 1;

            for (int var9 = 19; var9 >= 0; var9--) {
               if (this.titaniumBoltType[var9] != -1
                  && this.titaniumBoltX[var9] + r <= 128
                  && this.titaniumBoltX[var9] + 16 + r >= 0
                  && this.titaniumBoltY[var9] + s <= 128
                  && this.titaniumBoltY[var9] + 16 + s >= 0
                  && this.a(this.titaniumBoltX[var9], this.titaniumBoltY[var9], 16, 16, var5, var6, var7, var8)) {
                  if (a.soundEnabled && a.soundPlayer != null) {
                     a.soundPlayer.queue(4, 1);
                  }

                  Game var10000;
                  short var10001;
                  short var10002;
                  byte var10003;
                  if (this.titaniumBoltType[var9] == 0) {
                     this.b(this.titaniumBoltX[var9], this.titaniumBoltY[var9], 0);
                     var10000 = this;
                     var10001 = this.titaniumBoltX[var9];
                     var10002 = this.titaniumBoltY[var9];
                     var10003 = 0;
                  } else {
                     var10000 = this;
                     var10001 = this.titaniumBoltX[var9];
                     var10002 = this.titaniumBoltY[var9];
                     var10003 = this.titaniumBoltType[var9];
                  }

                  var10000.b(var10001, var10002, var10003);
                  this.d(this.titaniumBoltX[var9] + 8 << 8, this.titaniumBoltY[var9] + 8 << 8, 30);
                  this.titaniumBoltType[var9] = -1;
                  this.playerShots[var1].a(false);
               }
            }
         }
      }
   }

   private void F() {
      for (int var3 = 5; var3 >= 0; var3--) {
         if (this.pickupType[var3] != -1) {
            this.pickupY[var3] = this.pickupY[var3] + 256;
            int var4;
            if ((var4 = (this.pickupX[var3] >> 8) / 22) <= -1) {
               this.pickupX[var3] = 2816;
            }

            if (var4 >= LevelMap.columnSolidMasks.length) {
               this.pickupX[var3] = (LevelMap.columnSolidMasks.length * 2 - 3) * 22 / 2 << 8;
            }

            int var5 = b(this.pickupX[var3] >> 8, this.pickupY[var3] >> 8);
            if ((this.pickupY[var3] >> 8) + 8 >= var5) {
               this.pickupY[var3] = var5 - 8 << 8;
            }
         }
      }
   }

   private void G() {
      if (this.exitTileX != -1) {
         if (this.a(this.player.b() + 0 - 6, this.player.c() + -6 + 3, 12, 20, this.exitTileX * 22 + 7, this.exitTileY * 14, 8, 8)) {
            this.g(this.Q, this.P);
            this.exitTileX = this.exitTileY = -1;
            levelsCleared++;
         }
      }
   }

   private void c(Graphics var1) {
      var1.setClip(0, 0, 128, 20);
      var1.setColor(0);
      var1.fillRect(0, 0, 128, 20);
      byte var2 = this.player.health;
      var1.setColor(16777215);
      if (this.player.currentWeapon != 6 && this.player.currentWeapon != 0) {
         a.a(var1, String.valueOf(this.player.ammo[this.player.currentWeapon]), 13, 5, 20);
      }

      a.a(var1, String.valueOf(this.boltCount), 114, 5, 24);
      var1.setClip(1, 4, 11, 12);
      var1.drawImage(hudIconImage, 1, 4 - (this.player.currentWeapon - 1) * 12, 20);
      var1.setClip(115, 4, 11, 12);
      var1.drawImage(hudIconImage, 115, -92, 20);
      var1.setClip(36, 4, 42, 12);
      var1.setColor(2381412);
      var1.drawRect(36, 4, 41, 11);
      var1.setColor(1371884);
      var1.fillRect(38, 6, var2 * 2 * 128 / 128 - 2, 8);
      var1.setColor(2394780);
      var1.drawRect(37, 5, var2 * 2 * 128 / 128 - 1, 9);
   }

   private void d(Graphics var1) {
      int var2 = r;
      int var3 = s + -6;

      for (int var4 = 3; var4 >= 0; var4--) {
         if (this.zipX1[var4] != -1) {
            int var5 = this.zipX1[var4] + var2;
            int var6 = this.zipY1[var4] + var3;
            int var7 = this.zipX2[var4] + var2;
            int var8 = this.zipY2[var4] + var3;
            if ((var5 <= 128 || var7 <= 128) && (var5 >= 0 || var7 >= 0) && (var6 <= 128 || var8 <= 128) && (var6 >= 0 || var8 >= 0)) {
               var1.setClip(0, 20, 128, 108);
               var1.setColor(12303291);
               var1.drawLine(var5, var6 - 1, var7, var8 - 1);
               var1.setColor(8947848);
               var1.drawLine(var5, var6, var7, var8);
               var1.setColor(5592405);
               var1.drawLine(var5, var6 + 1, var7, var8 + 1);
            }
         }
      }
   }

   private void e(Graphics var1) {
      int var4 = r;
      int var5 = s;

      for (int var7 = 3; var7 >= 0; var7--) {
         if (this.platformState[var7] >= 0) {
            int var8 = this.platformX[var7] + this.platformDx[var7] + var4;
            int var9 = this.platformY[var7] + this.platformDy[var7] + var5;
            if (var8 <= 128 && var8 + 22 >= 0 && var9 <= 128 && var9 + 14 >= 20) {
               int var6 = 0;
               if (var9 < 20) {
                  var6 = 20 - var9;
                  var9 = 20;
               }

               if (var6 < 14 && var6 >= 0) {
                  var1.setClip(var8, var9, 22, 14 - var6);
                  var1.drawImage(actorImage, var8, var9 - var6 - 182, 20);
               }
            }
         }
      }
   }

   private void f(Graphics var1) {
      int var4 = r;
      int var5 = s;

      for (int var6 = 0; var6 < 20; var6++) {
         if (this.titaniumBoltType[var6] >= 0) {
            int var8 = this.titaniumBoltX[var6] + var4;
            int var9 = this.titaniumBoltY[var6] + var5;
            if (var8 <= 128 && var8 + 16 >= 0 && var9 <= 128 && var9 + 16 >= 0) {
               int var7 = 0;
               if (var9 < 20) {
                  var7 = 20 - var9;
                  var9 = 20;
               }

               if (var7 < 16 && var7 >= 0) {
                  var1.setClip(var8, var9, 16, 16 - var7);
                  var1.drawImage(titaniumBoltImage, var8, var9 - this.titaniumBoltType[var6] * 16 - var7, 0);
               }
            }
         }
      }
   }

   private void g(Graphics var1) {
      int var4 = r;
      int var5 = s;

      for (int var7 = 5; var7 >= 0; var7--) {
         if (this.pickupType[var7] >= 0) {
            int var8 = (this.pickupX[var7] >> 8) + var4;
            int var9 = (this.pickupY[var7] >> 8) + var5;
            if (var8 <= 128 && var8 + 8 >= 0 && var9 <= 128 && var9 + 8 >= 0) {
               int var6 = 0;
               if (var9 < 20) {
                  var6 = 20 - var9;
                  var9 = 20;
               }

               if (var6 < 8 && var6 >= 0) {
                  var1.setClip(var8, var9, 8, 8 - var6);
                  var1.drawImage(smallSpriteImage, var8, var9 - this.pickupType[var7] * 8 - var6, 20);
               }
            }
         }
      }
   }

   private void h(Graphics var1) {
      for (int var2 = MAX_ENEMIES - 1; var2 >= 0; var2--) {
         if (this.enemies[var2].kind != -1) {
            this.enemies[var2].a(var1, var2);
         }
      }

      this.player.a(var1);

      for (int var3 = 9; var3 >= 0; var3--) {
         if (this.enemyShots[var3].type >= 0) {
            this.enemyShots[var3].a(var1, true);
         }
      }

      for (int var4 = 9; var4 >= 0; var4--) {
         if (this.playerShots[var4].type >= 0) {
            this.playerShots[var4].a(var1, false);
         }
      }
   }

   private void H() {
      this.bossVulnerable = true;

      for (int var1 = 9; var1 >= 0; var1--) {
         if (this.enemyShots[var1].type == -1) {
            this.enemyShots[var1].posX = 78848;
            this.enemyShots[var1].posY = 32256;
            this.enemyShots[var1].type = 31;
            this.enemyShots[var1].age = 0;
            this.enemyShots[var1].sourceAnim = 25;
            this.enemyShots[var1].halfWidth = Projectile.HALF_WIDTHS[31];
            this.enemyShots[var1].halfHeight = Projectile.HALF_HEIGHTS[31];
            Projectile var10000;
            int var10001;
            if (this.bossAim == 0 || this.bossAim == 1 || this.bossAim == 7) {
               var10000 = this.enemyShots[var1];
               var10001 = Projectile.SPEEDS[31] << 8;
            } else if (this.bossAim != 2 && this.bossAim != 6) {
               var10000 = this.enemyShots[var1];
               var10001 = -(Projectile.SPEEDS[31] << 8);
            } else {
               var10000 = this.enemyShots[var1];
               var10001 = 0;
            }

            var10000.vx = var10001;
            if (this.bossAim != 2 && this.bossAim != 1 && this.bossAim != 3) {
               if (this.bossAim != 0 && this.bossAim != 4) {
                  this.enemyShots[var1].vy = Projectile.SPEEDS[31] << 8;
                  return;
               }

               this.enemyShots[var1].vy = 0;
               return;
            }

            this.enemyShots[var1].vy = -(Projectile.SPEEDS[31] << 8);
            return;
         }
      }
   }

   public final void c(int var1, int var2) {
      this.bossVulnerable = false;
      int var3 = (var1 >> 8) / 22;
      int var4 = (var2 >> 8) / 14;
      if (!this.N.b(var3, var4)) {
         if (this.N.b(var3, var4 - 1)) {
            var4--;
         } else if (this.N.b(var3, var4 + 1)) {
            var4++;
         } else if (this.N.b(var3 + 1, var4)) {
            var3++;
         } else {
            if (!this.N.b(var3 - 1, var4)) {
               return;
            }

            var3--;
         }
      }

      byte var5 = this.player.d();
      byte var6 = this.player.e();
      byte var7 = this.player.posInRow < Player.k ? this.player.row : (byte)(this.player.row + 1);
      if (var3 == var5 && (var4 == var7 || var4 + 1 == var7)) {
         if (!this.N.b(var3 + 1, var4)) {
            return;
         }

         var3++;
      }

      if (var3 == var6 && (var4 == var7 || var4 + 1 == var7)) {
         if (!this.N.b(var3 - 1, var4)) {
            return;
         }

         var3--;
      }

      this.a(var3, var4, (byte)1, -1);
   }

   private void I() {
      if (this.bossHp[4] <= 0) {
         a.a((byte)1, 1);
         this.bg = 0;
         this.e(112 + (this.Q - 1), 10);
      } else {
         short var1 = this.player.b();
         int var2 = this.player.c() + -6 + 11;
         int var5 = var1 - 308;
         int var6 = var2 - 126;
         label86:
         if (this.bossFirePhase <= 2 && !this.bossVulnerable) {
            Game var10000;
            byte var10001;
            if (var5 > 14) {
               if (var6 > 14) {
                  var10000 = this;
                  var10001 = 7;
               } else if (var6 < -14) {
                  var10000 = this;
                  var10001 = 1;
               } else {
                  var10000 = this;
                  var10001 = 0;
               }
            } else if (var5 < -14) {
               if (var6 > 14) {
                  var10000 = this;
                  var10001 = 5;
               } else if (var6 < -14) {
                  var10000 = this;
                  var10001 = 3;
               } else {
                  var10000 = this;
                  var10001 = 4;
               }
            } else {
               this.bossAim = 2;
               if (var6 <= 0) {
                  break label86;
               }

               var10000 = this;
               var10001 = 6;
            }

            var10000.bossAim = var10001;
         }

         if (++this.bossFireTick > h) {
            this.bossFireTick = 0;
            if (++this.bossFirePhase > 12) {
               this.bossFirePhase = 0;
            }

            if (this.bossFirePhase > 2) {
               this.bossFireTick = (byte)(h >> 2);
               if (++this.bossAim > 7) {
                  this.bossAim = 0;
               }
            }

            this.H();
         }

         for (int var9 = 0; var9 < 4; var9++) {
            label95: {
               byte[] var10;
               int var11;
               byte var10002;
               if (this.bossShellState[var9] != 0 && this.bossHp[var9] > 0) {
                  if (--this.bossShellTimer[var9] >= 0) {
                     break label95;
                  }

                  var10 = this.bossShellState;
                  var11 = var9;
                  var10002 = 0;
               } else {
                  if (this.bossShellState[var9] == 2 || this.bossHp[var9] > 0 || --this.bossShellTimer[var9] >= 0) {
                     break label95;
                  }

                  var10 = this.bossShellState;
                  var11 = var9;
                  var10002 = 2;
               }

               var10[var11] = var10002;
            }

            this.l(var9);
         }

         if (this.bossHp[0] + this.bossHp[1] + this.bossHp[2] + this.bossHp[3] == 0) {
            this.l(4);
         }
      }
   }

   private void J() {
      this.bossAim = 4;
      this.bossShellTimer[0] = this.bossShellTimer[1] = this.bossShellTimer[2] = this.bossShellTimer[3] = 0;
      this.bossShellState[0] = this.bossShellState[1] = this.bossShellState[2] = this.bossShellState[3] = 0;
      this.bossHp[0] = this.bossHp[1] = this.bossHp[2] = this.bossHp[3] = f;
      this.bossHp[4] = g;
      this.bossFirePhase = 0;
   }

   private void l(int var1) {
      byte var4 = 22;
      byte var5 = 14;
      if (this.bossHp[var1] <= 0) {
         this.bossHp[var1] = 0;
      } else {
         int var2;
         byte[] var10000;
         int var10001;
         if (var1 == 4) {
            var4 = 44;
            var5 = 28;
            var2 = BOSS_SHELL_COL[2] * 22;
            var10000 = BOSS_SHELL_ROW;
            var10001 = 2;
         } else {
            var2 = BOSS_SHELL_COL[var1] * 22;
            var10000 = BOSS_SHELL_ROW;
            var10001 = var1;
         }

         int var3 = var10000[var10001] * 14;
         if (var2 + var4 + r >= 0 && var2 + r <= 128 && var3 + var5 + s >= 0 && var3 + s <= 128) {
            for (int var6 = 9; var6 >= 0; var6--) {
               byte var7 = this.playerShots[var6].type;
               if (this.playerShots[var6].type >= 0 && var7 != 30) {
                  int var10 = this.playerShots[var6].halfWidth;
                  int var11 = this.playerShots[var6].halfHeight;
                  int var8 = (this.playerShots[var6].posX >> 8) - var10;
                  int var9 = (this.playerShots[var6].posY >> 8) - var11;
                  if (this.a(var2, var3, var4, var5, var8, var9, var10 << 1, var11 << 1)) {
                     this.bossHp[var1] = this.bossHp[var1] - Projectile.DAMAGE_BY_TYPE[var7];
                     byte var10002;
                     if (var1 == 4) {
                        this.bossShellTimer[0] = this.bossShellTimer[1] = this.bossShellTimer[2] = this.bossShellTimer[3] = 5;
                        var10000 = this.bossShellState;
                        var10001 = 0;
                        var10002 = this.bossShellState[1] = this.bossShellState[2] = this.bossShellState[3] = 3;
                     } else {
                        this.bossShellTimer[var1] = 5;
                        var10000 = this.bossShellState;
                        var10001 = var1;
                        var10002 = 1;
                     }

                     var10000[var10001] = var10002;
                     this.playerShots[var6].a(false);
                     return;
                  }
               }
            }
         }
      }
   }

   public final void a(int var1, int var2, int var3, int var4) {
      for (int var5 = 0; var5 < 4; var5++) {
         this.b(var5, var1, var2, var3, var4);
      }

      if (this.bossHp[0] + this.bossHp[1] + this.bossHp[2] + this.bossHp[3] == 0) {
         this.b(4, var1, var2, var3, var4);
      }
   }

   private void b(int var1, int var2, int var3, int var4, int var5) {
      byte var8 = 22;
      byte var9 = 14;
      if (this.bossHp[var1] <= 0) {
         this.bossHp[var1] = 0;
      } else {
         int var6;
         byte[] var10000;
         int var10001;
         if (var1 == 4) {
            var8 = 44;
            var9 = 28;
            var6 = BOSS_SHELL_COL[2] * 22;
            var10000 = BOSS_SHELL_ROW;
            var10001 = 2;
         } else {
            var6 = BOSS_SHELL_COL[var1] * 22;
            var10000 = BOSS_SHELL_ROW;
            var10001 = var1;
         }

         int var7 = var10000[var10001] * 14;
         if (Math.abs(var6 - var2) <= 33 && Math.abs(var7 - var3) <= 14) {
            if (this.a(var6, var7, var8, var9, var2, var3, var4, var5)) {
               int var10002;
               int[] varHp;
               byte[] var10003;
               byte[] var10004;
               byte var10005;
               if (this.player.animState == 11) {
                  varHp = this.bossHp;
                  var10001 = var1;
                  var10002 = this.bossHp[var1];
                  var10003 = Player.i;
                  var10004 = this.player.weaponLevel;
                  var10005 = 1;
               } else {
                  varHp = this.bossHp;
                  var10001 = var1;
                  var10002 = this.bossHp[var1];
                  var10003 = Player.i;
                  var10004 = this.player.weaponLevel;
                  var10005 = 0;
               }

               varHp[var10001] = var10002 - var10003[var10004[var10005]];
               byte[] var11;
               byte var14;
               if (var1 == 4) {
                  this.bossShellTimer[0] = this.bossShellTimer[1] = this.bossShellTimer[2] = this.bossShellTimer[3] = 5;
                  var11 = this.bossShellState;
                  var10001 = 0;
                  var14 = this.bossShellState[1] = this.bossShellState[2] = this.bossShellState[3] = 3;
               } else {
                  this.bossShellTimer[var1] = 5;
                  var11 = this.bossShellState;
                  var10001 = var1;
                  var14 = 1;
               }

               var11[var10001] = var14;
            }
         }
      }
   }

   private void i(Graphics var1) {
      if (this.bossHp[4] > 0) {
         byte var4;
         byte var5;
         byte var6;
         int var7;
         short var8;
         int var9;
         int var12;
         int var14;
         label95: {
            var6 = 0;
            var7 = 0;
            var8 = 0;
            var9 = 0;
            var4 = 22;
            var5 = 14;
            var12 = 308 + r;
            var14 = 126 + s;
            short var10000;
            if (this.bossAim == 0) {
               var12 += 0;
               var14 -= 7;
               var10000 = 11;
            } else if (this.bossAim == 1) {
               var12 += -2;
               var14 -= 20;
               var10000 = 12;
            } else {
               if (this.bossAim == 2) {
                  var12 -= 7;
                  var14 -= 22;
                  var6 = 11;
                  var5 = 22;
                  var4 = 14;
                  var8 = 90;
                  break label95;
               }

               if (this.bossAim == 3) {
                  var12 -= 20;
                  var14 -= 20;
                  var7 = 12;
                  var8 = 8192;
                  break label95;
               }

               if (this.bossAim == 4) {
                  var12 -= 22;
                  var14 -= 7;
                  var7 = 11;
                  var8 = 8192;
                  break label95;
               }

               if (this.bossAim == 5) {
                  var12 -= 20;
                  var14 += 6;
                  var10000 = 180;
               } else {
                  if (this.bossAim == 6) {
                     var12 -= 7;
                     var14 += 0;
                     var8 = 270;
                     var5 = 22;
                     var4 = 14;
                     var6 = 2;
                     break label95;
                  }

                  var12 += -2;
                  var14 += 6;
                  var10000 = 16384;
               }

               var8 = var10000;
               var10000 = 1;
            }

            var7 = var10000;
         }

         if (var14 < 20) {
            var9 = 20 - var14;
            var14 = 20;
         }

         if (var9 < var5 && var9 >= 0) {
            var1.setClip(var12, var14, var4, var5 - var9);
            if (this.bossAim != 0 && this.bossAim != 1) {
               DirectUtils.getDirectGraphics(var1).drawImage(actorImage, var12 - var4 * var6, var14 - var5 * var7 - var9, 20, var8);
            } else {
               var1.drawImage(actorImage, var12, var14 - var5 * var7 - var9, 0);
            }
         }

         for (int var10 = 0; var10 < 4; var10++) {
            var9 = 0;
            var12 = BOSS_SHELL_COL[var10] * 22 + r;
            if ((var14 = BOSS_SHELL_ROW[var10] * 14 + s) < 20) {
               var9 = 20 - var14;
               var14 = 20;
            }

            if (var9 < 14 && var9 >= 0) {
               label66: {
                  int var20;
                  label65: {
                     var7 = 98;
                     var1.setClip(var12, var14, 22, 14 - var9);
                     short var19;
                     if (var10 == 1) {
                        var19 = 16384;
                     } else {
                        if (var10 == 2) {
                           var8 = 8192;
                           var20 = this.bossShellState[var10] + 7;
                           break label65;
                        }

                        if (var10 != 3) {
                           break label66;
                        }

                        var19 = 180;
                     }

                     var8 = var19;
                     var20 = 13 - (this.bossShellState[var10] + 7);
                  }

                  var7 = var20 * 14;
               }

               if (var10 == 0) {
                  var1.drawImage(actorImage, var12, var14 - 14 * this.bossShellState[var10] - var7 - var9, 0);
               } else {
                  DirectUtils.getDirectGraphics(var1).drawImage(actorImage, var12, var14 - var7 - var9, 20, var8);
               }
            }
         }
      }
   }

   private void m(int var1) {
      label55: {
         Game var10000;
         byte var10001;
         label54: {
            if (var1 == PLAYER_SLOT) {
               this.sprKind = this.player.kind;
               this.sprAnimState = this.player.animState;
               this.sprAnimFrame = this.player.animFrame;
               if (this.player.facingRight) {
                  var10000 = this;
                  var10001 = 0;
                  break label54;
               }

               var10000 = this;
            } else {
               if (var1 < 0) {
                  break label55;
               }

               this.sprKind = this.enemies[var1].kind;
               this.sprAnimState = this.enemies[var1].animState;
               this.sprAnimFrame = this.enemies[var1].animFrame;
               if (this.enemies[var1].subState != 0) {
                  if (this.enemies[var1].subState == 1) {
                     if (this.enemies[var1].facingRight) {
                        var10000 = this;
                        var10001 = 4;
                     } else {
                        var10000 = this;
                        var10001 = 2;
                     }
                  } else if (this.enemies[var1].subState == 2) {
                     if (this.enemies[var1].facingRight) {
                        var10000 = this;
                        var10001 = 6;
                     } else {
                        var10000 = this;
                        var10001 = 3;
                     }
                  } else {
                     if (this.enemies[var1].subState != 3) {
                        break label55;
                     }

                     if (this.enemies[var1].facingRight) {
                        var10000 = this;
                        var10001 = 1;
                     } else {
                        var10000 = this;
                        var10001 = 7;
                     }
                  }
                  break label54;
               }

               if (this.enemies[var1].facingRight) {
                  var10000 = this;
                  var10001 = 0;
                  break label54;
               }

               var10000 = this;
            }

            var10001 = 5;
         }

         var10000.sprOrientation = var10001;
      }

      if (this.sprKind >= 0) {
         if (var1 == PLAYER_SLOT) {
            this.sprFrameOffset = Player.ANIM_FRAMES[this.sprKind][this.sprAnimState][this.sprAnimFrame];
            return;
         }

         this.sprFrameOffset = Enemy.ANIM_FRAMES[this.sprKind][this.sprAnimState][this.sprAnimFrame];
         this.z = 18;
      }
   }

   public final void a(Graphics var1, int var2, int var3) {
      this.m(PLAYER_SLOT);
      DirectGraphics var4 = DirectUtils.getDirectGraphics(var1);
      if (this.sprOrientation == 5) {
         var4.drawImage(playerImage, var2, var3, 20, 8192);
      }
   }

   public final void a(Graphics var1, int var2, int var3, int var4) {
      this.m(var2);
      DirectGraphics var5 = DirectUtils.getDirectGraphics(var1);
      byte var6 = 0;
      if (this.enemies[var2].s) {
         var6 = 44;
      }

      DirectGraphics var10000;
      Image var10001;
      int var10002;
      int var10003;
      byte var10004;
      short var10005;
      switch (this.sprOrientation) {
         case 1:
            var10000 = var5;
            var10001 = enemySegmentImage;
            var10002 = var3 - this.sprFrameOffset * 22 - var6;
            var10003 = var4;
            var10004 = 20;
            var10005 = 90;
            break;
         case 2:
            var10000 = var5;
            var10001 = enemySegmentImage;
            var10002 = var3;
            var10003 = var4 - (this.z - 1 - this.sprFrameOffset) * 22 + var6;
            var10004 = 20;
            var10005 = 180;
            break;
         case 3:
            var10000 = var5;
            var10001 = enemySegmentImage;
            var10002 = var3 - (this.z - 1 - this.sprFrameOffset) * 22 + var6;
            var10003 = var4;
            var10004 = 20;
            var10005 = 270;
            break;
         case 4:
            var10000 = var5;
            var10001 = enemySegmentImage;
            var10002 = var3;
            var10003 = var4 - (this.z - 1 - this.sprFrameOffset) * 22 + var6;
            var10004 = 20;
            var10005 = 16384;
            break;
         case 5:
            var10000 = var5;
            var10001 = enemySegmentImage;
            var10002 = var3;
            var10003 = var4 - this.sprFrameOffset * 22 - var6;
            var10004 = 20;
            var10005 = 8192;
            break;
         case 6:
            var10000 = var5;
            var10001 = enemySegmentImage;
            var10002 = var3 - (this.z - 1 - this.sprFrameOffset) * 22 + var6;
            var10003 = var4;
            var10004 = 20;
            var10005 = 8282;
            break;
         case 7:
            var10000 = var5;
            var10001 = enemySegmentImage;
            var10002 = var3 - this.sprFrameOffset * 22 - var6;
            var10003 = var4;
            var10004 = 20;
            var10005 = 16474;
            break;
         default:
            return;
      }

      var10000.drawImage(var10001, var10002, var10003, var10004, var10005);
   }

   private static boolean e(int var0, int var1, int var2) {
      return var0 >= var1 && var0 <= var1 + var2;
   }

   public final boolean a(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      return (e(var5, var1, var3) || e(var1, var5, var7)) && (e(var6, var2, var4) || e(var2, var6, var8));
   }

   public final void a(int var1, int var2, boolean var3, int var4) {
      short var5 = (short)(var1 * 22 + 11);
      short var6 = (short)(var2 * 14 + 7);
      short[] var10000;
      if (var3) {
         this.zipX1[var4] = var5;
         var10000 = this.zipY1;
      } else {
         this.zipX2[var4] = var5;
         var10000 = this.zipY2;
      }

      var10000[var4] = var6;
      if (this.zipX1[var4] != -1 && this.zipX2[var4] != -1) {
         this.zipDy[var4] = this.zipY2[var4] - this.zipY1[var4] << 8;
         this.zipDx[var4] = (short)(this.zipX2[var4] - this.zipX1[var4]);
         this.zipBaseY[var4] = (this.zipY1[var4] << 8) - this.zipDy[var4] * this.zipX1[var4] / this.zipDx[var4];
      }
   }

   public final void a(int var1, int var2, int var3, int var4, int var5) {
      this.titaniumBoltType[var5] = (byte)var3;
      short[] var10000;
      int var10001;
      int var10002;
      int var10003;
      if (var4 == 2) {
         var10000 = this.titaniumBoltX;
         var10001 = var5;
         var10002 = var1 * 22;
         var10003 = 8;
      } else {
         var10000 = this.titaniumBoltX;
         var10001 = var5;
         var10002 = var1 * 22;
         var10003 = var4 * 16;
      }

      var10000[var10001] = (short)(var10002 + var10003);
      this.titaniumBoltY[var5] = (short)(var2 * 14);
   }

   public final void b(int var1, int var2, int var3) {
      for (int var4 = 5; var4 >= 0; var4--) {
         if (this.pickupType[var4] == -1) {
            byte[] var10000;
            int var10001;
            byte var10002;
            if (var3 == 0) {
               var10000 = this.pickupType;
               var10001 = var4;
               var10002 = (byte)(Math.abs(this.O.nextInt()) % 2);
            } else if (var3 == 1) {
               var10000 = this.pickupType;
               var10001 = var4;
               var10002 = 2;
            } else {
               var10000 = this.pickupType;
               var10001 = var4;
               var10002 = 3;
            }

            var10000[var10001] = var10002;
            this.pickupX[var4] = var1 + Math.abs(this.O.nextInt()) % 11 << 8;
            this.pickupY[var4] = var2 << 8;
            this.pickupVx[var4] = 0;
            this.pickupVy[var4] = 768;
            return;
         }
      }
   }

   public final void c(int var1, int var2, int var3) {
      for (int var4 = 3; var4 >= 0; var4--) {
         if (this.platformState[var4] == -1) {
            this.platformState[var4] = (byte)var3;
            if (var3 != 0 && var3 != 1) {
               if (var3 == 2) {
                  this.platformX[var4] = (short)((var1 - 2) * 22);
                  this.platformY[var4] = (short)(var2 * 14);
                  this.platformDx[var4] = 44;
                  this.platformDy[var4] = 0;
                  return;
               }

               this.platformX[var4] = (short)(var1 * 22);
               this.platformY[var4] = (short)((var2 - 2) * 14);
               this.platformDx[var4] = 0;
               this.platformDy[var4] = 28;
               return;
            }

            this.platformX[var4] = (short)(var1 * 22);
            this.platformY[var4] = (short)(var2 * 14);
            this.platformDx[var4] = 0;
            this.platformDy[var4] = 0;
            return;
         }
      }
   }

   public final void d(int var1, int var2, int var3) {
      for (int var4 = 9; var4 >= 0; var4--) {
         if (this.playerShots[var4].type == -1) {
            if (var3 >= 0 && var3 <= 14 || var3 >= 18 && var3 <= 20) {
               this.player.ammo[this.player.currentWeapon]--;
               this.e = true;
            }

            if (var3 >= 9 && var3 <= 11 || var3 >= 15 && var3 <= 17) {
               this.playerShots[var4].halfWidth = 55;
               this.playerShots[var4].halfHeight = 7;
               var1 += (this.player.facingRight ? 1 : -1) * 55 << 8;
            } else {
               this.playerShots[var4].halfWidth = Projectile.HALF_WIDTHS[var3];
               this.playerShots[var4].halfHeight = Projectile.HALF_HEIGHTS[var3];
            }

            this.playerShots[var4].prev2X = this.playerShots[var4].prevX = this.playerShots[var4].posX = var1;
            this.playerShots[var4].prev2Y = this.playerShots[var4].prevY = this.playerShots[var4].posY = var2;
            this.playerShots[var4].type = (byte)var3;
            this.playerShots[var4].vx = (this.player.facingRight ? 1 : -1) * Projectile.SPEEDS[var3] << 8;
            this.playerShots[var4].vy = 0;
            this.playerShots[var4].age = 0;
            this.playerShots[var4].facingRight = this.player.facingRight;
            this.playerShots[var4].spawnX = this.player.posX;
            if (var3 >= 3 && var3 <= 5) {
               this.playerShots[var4].vy = 512;
               return;
            }
            break;
         }
      }
   }

   public final void a(int var1, int var2, int var3, boolean var4, boolean var5, int var6, byte var7) {
      for (int var8 = 9; var8 >= 0; var8--) {
         if (this.enemyShots[var8].type == -1) {
            int var9;
            Projectile var10000;
            label86: {
               this.enemyShots[var8].posX = var1;
               this.enemyShots[var8].posY = var2;
               this.enemyShots[var8].type = (byte)var3;
               this.enemyShots[var8].age = 0;
               this.enemyShots[var8].sourceAnim = var7;
               this.enemyShots[var8].halfWidth = Projectile.HALF_WIDTHS[var3];
               this.enemyShots[var8].halfHeight = Projectile.HALF_HEIGHTS[var3];
               byte[] var10002;
               if (var6 != 0 && var6 != 1) {
                  if (var5) {
                     this.enemyShots[var8].vx = (short)((var6 == 3 ? -1 : 1) * Projectile.SPEEDS[var3] << 8);
                     var10000 = this.enemyShots[var8];
                     var9 = 0;
                     break label86;
                  }

                  this.enemyShots[var8].vx = 0;
                  var10000 = this.enemyShots[var8];
                  var9 = var4 ? -1 : 1;
                  var10002 = Projectile.SPEEDS;
               } else {
                  if (!var5) {
                     this.enemyShots[var8].vx = (short)((var4 ? 1 : -1) * Projectile.SPEEDS[var3] << 8);
                     var10000 = this.enemyShots[var8];
                     var9 = 0;
                     break label86;
                  }

                  this.enemyShots[var8].vx = 0;
                  if (var7 != 17 && var7 != 20 && var7 != 23) {
                     var10000 = this.enemyShots[var8];
                     var9 = var6 == 0 ? -1 : 1;
                     var10002 = Projectile.SPEEDS;
                  } else {
                     var10000 = this.enemyShots[var8];
                     var9 = var6 == 0 ? 1 : -1;
                     var10002 = Projectile.SPEEDS;
                  }
               }

               var9 = (short)(var9 * var10002[var3] << 8);
            }

            var10000.vy = var9;
            if (var3 == 32) {
               this.enemyShots[var8].prevX = var1;
               this.enemyShots[var8].prevY = var2;
               this.enemyShots[var8].prev2X = var1;
               this.enemyShots[var8].prev2Y = var2 - 14336;
               return;
            }
            break;
         }
      }
   }

   public final void a(int var1, int var2, byte var3, int var4) {
      if (var4 == -1) {
         var4 = MAX_ENEMIES - 1;

         while (var4 >= 0 && this.enemies[var4].kind != -1) {
            var4--;
         }

         if (var4 == -1) {
            return;
         }
      }

      Enemy var10000;
      byte var10001;
      if (var3 >= 0 && var3 <= 2) {
         var10000 = this.enemies[var4];
         var10001 = 1;
      } else if (var3 >= 3 && var3 <= 5) {
         var10000 = this.enemies[var4];
         var10001 = 0;
      } else if (var3 >= 6 && var3 <= 13) {
         var10000 = this.enemies[var4];
         var10001 = 2;
      } else {
         var10000 = this.enemies[var4];
         var10001 = 3;
      }

      var10000.kind = var10001;
      this.enemies[var4].posX = var1 * 22 + 11 << 8;
      this.enemies[var4].row = (byte)var2;
      this.enemies[var4].posInRow = this.enemies[var4].velX = this.enemies[var4].velY = 0;
      this.enemies[var4].health = Enemy.HP_BY_ANIM[var3];
      this.enemies[var4].subState = 0;
      this.enemies[var4].facingRight = true;
      if (this.Q == 12 && this.player.posX < this.enemies[var4].posX) {
         this.enemies[var4].facingRight = false;
      }

      this.enemies[var4].s = false;
      this.enemies[var4].B = 0;
      this.enemies[var4].bounceTimer = 0;
      this.enemies[var4].A = 0;
      this.enemies[var4].B = 0;
      this.enemies[var4].C = this.enemies[var4].posX;
      if (this.enemies[var4].kind == 3) {
         short var5;
         if ((var5 = this.N.a(var1 + 1, var2)) >= 4 && var5 <= 13) {
            this.enemies[var4].subState = 3;
         }

         if ((var5 = this.N.a(var1 - 1, var2)) >= 4 && var5 <= 13) {
            this.enemies[var4].subState = 2;
         }

         if ((var5 = this.N.a(var1, var2 - 1)) >= 4 && var5 <= 13) {
            this.enemies[var4].posInRow = 3072;
            this.enemies[var4].subState = 1;
         }
      }

      this.enemies[var4].animKind = var3;
      this.enemies[var4].setAnimState((byte)0);
      this.enemies[var4].animRestart = 1;
      this.enemies[var4].activeFlag = 1;
   }

   private void n(int var1) {
      if (this.enemies[var1].kind != -1 && this.enemies[var1].animState != 5) {
         byte var2 = 0;
         short var3 = this.enemies[var1].c();
         short var4 = this.enemies[var1].b();
         byte var5 = this.enemies[var1].kind;
         byte var6 = Enemy.a[var5];
         byte var7 = Enemy.b[var5];
         byte var8 = Enemy.c[var5];
         byte var9 = Enemy.d[var5];
         if (var3 - var6 + var8 + r >= 0 && var3 - var6 + r <= 128 && var4 + var7 + var9 + s >= 0 && var4 + var7 + s <= 128) {
            for (int var10 = 9; var10 >= 0; var10--) {
               byte var11 = this.playerShots[var10].type;
               if (this.playerShots[var10].type >= 0 && var11 != 30 && (this.playerShots[var10].a() || var11 >= 9 && var11 <= 11 || var11 >= 15 && var11 <= 17 || var11 == 2)) {
                  int var12 = (this.playerShots[var10].posX >> 8) - this.playerShots[var10].halfWidth;
                  int var13 = (this.playerShots[var10].posY >> 8) - this.playerShots[var10].halfHeight;
                  if (this.a(var3 - var6, var4 + var7, var8, var9, var12, var13, this.playerShots[var10].halfWidth << 1, this.playerShots[var10].halfHeight << 1)) {
                     var2 = this.playerShots[var10].b();
                     if (var5 != 4 && var5 != 3 || var2 != 6) {
                        int var14;
                        Enemy var10000;
                        byte var10001;
                        int var10002;
                        if (var11 < 9 || var11 > 11 || (var14 = Math.abs(this.player.b() - var3)) < 22) {
                           var10000 = this.enemies[var1];
                           var10001 = this.enemies[var1].health;
                           var10002 = Projectile.DAMAGE_BY_TYPE[var11];
                        } else if (var14 >= 44 && var11 <= 10) {
                           var10000 = this.enemies[var1];
                           var10001 = this.enemies[var1].health;
                           var10002 = Projectile.DAMAGE_BY_TYPE[var11] >> 2;
                        } else {
                           var10000 = this.enemies[var1];
                           var10001 = this.enemies[var1].health;
                           var10002 = Projectile.DAMAGE_BY_TYPE[var11] >> 1;
                        }

                        var10000.health = (byte)(var10001 - var10002);
                        this.playerShots[var10].a(false);
                        this.d(var3 << 8, (var4 << 8) + 2816, 30);
                        enemyShotHits++;
                        if (this.enemies[var1].health <= 0 && this.enemies[var1].animState != 5) {
                           if (a.soundEnabled && a.soundPlayer != null) {
                              a.soundPlayer.queue(1, 1);
                           }

                           if (var2 == 6 && var5 != 4) {
                              specialKills++;
                              this.enemies[var1].kind = 4;
                              this.enemies[var1].animKind = 24;
                              this.enemies[var1].health = Enemy.HP_BY_ANIM[24];

                              for (int var17 = 0; var17 < Enemy.BOLTS_DROPPED_BY_ANIM[24]; var17++) {
                                 this.b((short)(this.enemies[var1].posX >> 8), this.enemies[var1].b(), 0);
                              }

                              if (this.player.weaponLevel[6] < 2) {
                                 this.player.weaponXp[6]++;
                              }

                              if (this.player.weaponXp[6] >= 20) {
                                 this.player.weaponLevel[6]++;
                                 this.player.weaponXp[6] = 0;
                                 this.e(73, -1);
                                 enemyKills++;
                                 totalKills++;
                                 return;
                              }
                           } else {
                              enemyKills++;
                              totalKills++;
                              this.enemies[var1].setAnimState((byte)5);
                              this.enemies[var1].animRestart = 1;
                              if (var5 != 4) {
                                 for (int var16 = 0; var16 < Enemy.BOLTS_DROPPED_BY_ANIM[this.enemies[var1].animKind]; var16++) {
                                    this.b((short)(this.enemies[var1].posX >> 8), this.enemies[var1].b(), 0);
                                 }

                                 if (this.player.weaponLevel[var2] < 2) {
                                    this.player.weaponXp[var2]++;
                                 }

                                 if (this.player.weaponXp[var2] >= 20) {
                                    this.player.weaponLevel[var2]++;
                                    this.player.weaponXp[var2] = 0;
                                    this.e(73, -1);
                                    return;
                                 }
                              }
                           }
                        } else {
                           this.enemies[var1].setAnimState((byte)4);
                           this.enemies[var1].animHold = 0;
                        }

                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private void K() {
      int var1 = this.player.b() + 0;
      int var2 = this.player.c() + -6;

      for (int var7 = 9; var7 >= 0; var7--) {
         if (this.player.animState == 10) {
            return;
         }

         byte var8 = this.enemyShots[var7].type;
         if (this.enemyShots[var7].type != -1 && var8 != 30 && this.enemyShots[var7].a()) {
            int var9 = (this.enemyShots[var7].posX >> 8) - this.enemyShots[var7].halfWidth;
            int var10 = (this.enemyShots[var7].posY >> 8) - this.enemyShots[var7].halfHeight;
            if (this.a(var1 - 6, var2 + 3, 12, 20, var9, var10, this.enemyShots[var7].halfWidth << 1, this.enemyShots[var7].halfHeight << 1)) {
               byte var11 = this.enemyShots[var7].sourceAnim;
               if (var8 != 3 && var8 != 6 && var8 != 18) {
                  if (!i) {
                     this.player.health = (byte)(this.player.health - Enemy.DAMAGE_BY_ANIM[var11]);
                  }

                  if (!i) {
                     playerDamaged = true;
                  }

                  this.e = true;
                  this.player.setAnimState((byte)9);
                  this.player.animHold = 0;
                  if (this.player.jumpPhase == 2) {
                     this.player.jumpPhase = 1;
                  }

                  this.d(var1 << 8, (var2 << 8) + 2816, 30);
               }

               if (var8 == 32) {
                  this.player.invulnTimer = 10;
               }

               this.enemyShots[var7].a(true);
            }
         }
      }
   }

   public static int d(int var0, int var1) {
      return var0 < var1 ? var0 : var1;
   }

   private static void j(Graphics var0) {
      var0.setClip(0, 0, 128, 128);
      var0.setColor(16777215);
      var0.fillRect(0, 92, 128, 36);
   }

   private static int a(Graphics var0, int var1, String var2, int var3, int var4, int var5, int var6) {
      int var7 = 0;
      int var8 = 0;
      int var9 = 0;
      char[] var13 = new char[var2.length()];
      var2.getChars(0, var2.length(), var13, 0);
      var7 = 0;
      int var10 = var1;
      int var11 = var1;
      char var12 = '\u0000';
      boolean var14 = false;

      while (true) {
         var8 = 0;
         var9 = var11 - 1;
         boolean var15 = false;
         boolean var16 = false;

         label67: {
            while (var11 < var2.length()) {
               if ((var12 = var2.charAt(var11)) == ' ') {
                  var8 += a.a(var12);
                  var11++;
                  if (var7 + var8 > var6 && !var14) {
                     var16 = true;
                     break label67;
                  }

                  var14 = true;
                  break label67;
               }

               int var17 = a.a(var12);
               if (var7 + var8 + var17 > var6 && !var14) {
                  var16 = true;
                  break label67;
               }

               var8 += var17;
               var11++;
            }

            var15 = true;
         }

         if (var7 + var8 > var6 || var15 || var16) {
            if (var2.charAt(var10) == ' ') {
               var10++;
            }

            if (var7 + var8 > var6 && !var16) {
               var11 = var9;
            }

            if ((var5 & 1) > 0) {
               if (var11 - var10 > 0) {
                  a.a(var0, var13, var10, var11 - var10, 64, var4, var5);
               }

               return var11 + (var16 ? 0 : 1);
            } else {
               if (var11 - var10 > 0) {
                  a.a(var0, var13, var10, var11 - var10, var3, var4, var5);
               }

               return var11 + (var16 ? 0 : 1);
            }
         }

         var7 += var8;
      }
   }

   private int a(Graphics var1, int var2) {
      byte var3 = 3;
      byte var5 = 17;
      var1.setColor(0);
      var1.setClip(0, 0, 128, 128);
      if (this.messagePortrait >= 0 && this.PORTRAIT_TABLE[this.messagePortrait][this.messagePortraitFrame - 1] >= 0) {
         var3 = 26;
         var5 = 20;
         var1.setClip(0, 98, 24, 24);
         var1.drawImage(portraitImage, 0, 98 - 24 * this.PORTRAIT_TABLE[this.messagePortrait][this.messagePortraitFrame - 1], 20);
         var1.setClip(0, 0, 128, 128);
      }

      int var7 = this.messageText.length();
      if ((var2 = a(var1, var2, this.messageText, var3, 95, var5, 128 - var3)) >= var7) {
         return -1;
      }

      if ((var2 = a(var1, var2, this.messageText, var3, 105, var5, 128 - var3)) >= var7) {
         return -1;
      }

      int var11;
      return (var11 = a(var1, var2, this.messageText, var3, 115, var5, 128 - var3)) >= var7 ? -1 : var11;
   }

   public final void j() {
      this.scoringActive = false;
      if (this.Q <= 10) {
         if (ratchetandclank.strings[112 + (this.Q - 1)] != null) {
            if (!ratchetandclank.strings[112 + (this.Q - 1)].equals("")) {
               if ((this.introSeenMask & 1 << this.Q - 1) > 0) {
                  this.introSeenMask = this.introSeenMask & ~(1 << this.Q - 1);
                  if (this.Q != 7 && this.Q != 9 && this.Q != 10 && this.Q != 12) {
                     this.e(112 + (this.Q - 1), this.Q - 1);
                  }
               }
            }
         }
      }
   }

   public final void e(int var1, int var2) {
      this.player.invulnTimer = 0;
      if (a.soundEnabled && a.soundPlayer != null) {
         a.soundPlayer.queue(2, 1);
      }

      this.messageScroll = 0;
      this.messagePortrait = var2;
      this.messageStringId = var1;
      Game var10000;
      String var10001;
      if (var1 == 74) {
         var10000 = this;
         var10001 = ratchetandclank.strings[var1] + " " + this.B();
      } else {
         String[] var3;
         int var10002;
         if (this.messagePortrait >= 0) {
            this.messageTimer = (byte)(this.PORTRAIT_TABLE[this.messagePortrait].length >> 1);
            this.messagePortraitFrame = 1;
            var10000 = this;
            var3 = ratchetandclank.strings;
            var10002 = this.PORTRAIT_TABLE[this.messagePortrait][this.messagePortraitFrame];
         } else {
            var10000 = this;
            var3 = ratchetandclank.strings;
            var10002 = var1;
         }

         var10001 = var3[var10002];
      }

      var10000.messageText = var10001;
      if (this.messageText == "") {
         this.messageScroll = -1;
      }

      this.weaponOverlayOpen = true;
      this.bj = 0;
   }

   private void L() {
      this.weaponOverlayOpen = false;
      if (this.messagePortrait >= 0) {
         if (--this.messageTimer > 0) {
            this.player.invulnTimer = 0;
            this.messageScroll = 0;
            this.weaponOverlayOpen = true;
            this.messagePortraitFrame += 2;
            this.messageText = ratchetandclank.strings[this.PORTRAIT_TABLE[this.messagePortrait][this.messagePortraitFrame]];
            return;
         }

         if (this.Q != 3 && this.Q != 10 || this.messageStringId < INFOLINK_MESSAGE_IDS[0] || this.messageStringId > INFOLINK_MESSAGE_IDS[15] + INFOLINK_MESSAGE_TICKS[15]) {
            if (this.Q == 12) {
               this.s();
               return;
            }

            return;
         }
      } else {
         if (--this.messageTimer > 0) {
            this.e(this.messageStringId + 1, this.messagePortrait);
            return;
         }

         if (this.messageStringId < INFOLINK_MESSAGE_IDS[0] || this.messageStringId > INFOLINK_MESSAGE_IDS[15] + INFOLINK_MESSAGE_TICKS[15]) {
            return;
         }
      }

      this.levelStateMask = this.levelStateMask | 1 << this.Q;
      this.p();
   }

   public final void a(byte[] var1) {
      var1[0] = 1;
      ratchetandclank.a(this.levelStateMask, var1, 1);
      ratchetandclank.a(this.area1BoltsTaken, var1, 5);
      ratchetandclank.a(this.area2BoltsTaken, var1, 9);
      ratchetandclank.a(this.collectiblesTaken1, var1, 13);
      var1[17] = this.player.ownedWeapons;

      for (int var2 = 0; var2 < 8; var2++) {
         var1[18 + var2] = this.player.weaponLevel[var2];
         ratchetandclank.a(this.player.weaponXp[var2], var1, 26 + var2 * 2);
         ratchetandclank.a(this.player.ammo[var2], var1, 42 + var2 * 2);
      }

      ratchetandclank.a(this.boltCount, var1, 58);
      ratchetandclank.a(this.levelElapsedMs, var1, 195);
      ratchetandclank.a(this.collectiblesTaken2, var1, 204);
      ratchetandclank.a(totalTitaniumBolts, var1, 208);
      ratchetandclank.a(this.introSeenMask, var1, 212);
      ratchetandclank.a(totalKills, var1, 216);
   }

   public final void b(byte[] var1) {
      this.levelStateMask = ratchetandclank.a(var1, 1);
      this.area1BoltsTaken = ratchetandclank.a(var1, 5);
      this.area2BoltsTaken = ratchetandclank.a(var1, 9);
      this.collectiblesTaken1 = ratchetandclank.a(var1, 13);
      this.player.ownedWeapons = var1[17];

      for (int var2 = 0; var2 < 8; var2++) {
         this.player.weaponLevel[var2] = var1[18 + var2];
         this.player.weaponXp[var2] = ratchetandclank.b(var1, 26 + var2 * 2);
         this.player.ammo[var2] = ratchetandclank.b(var1, 42 + var2 * 2);
      }

      this.boltCount = ratchetandclank.a(var1, 58);
      this.levelElapsedMs = ratchetandclank.a(var1, 195);
      this.collectiblesTaken2 = ratchetandclank.a(var1, 204);
      totalTitaniumBolts = ratchetandclank.a(var1, 208);
      this.introSeenMask = ratchetandclank.a(var1, 212);
      totalKills = ratchetandclank.a(var1, 216);
      this.cr = this.bg = 0;
   }

   private void M() {
      if (this.b == 0) {
         int var1 = this.bj & 63;
         byte var2 = this.player.weaponLevel[this.player.currentWeapon];
         if (var1 > 0) {
            this.bj++;
            if (var1 >= Player.g[var2][this.player.currentWeapon]) {
               if ((this.bj & 128) == 0) {
                  this.bj = 0;
                  return;
               }

               this.bj = 129;
               if (this.player.m()) {
                  if (a.soundEnabled && a.soundPlayer != null) {
                     a.soundPlayer.queue(5, 1);
                  }

                  shotsFired++;
                  return;
               }
            }
         }
      } else if (this.b == 20 && this.ad < 3) {
         if (!this.d) {
            this.N();
         }

         if (this.ac + 2000L < System.currentTimeMillis()) {
            label49: {
               this.ac = System.currentTimeMillis();
               Game var10000;
               String var10001;
               if (this.ad == 0) {
                  var10000 = this;
                  var10001 = "hhg";
               } else {
                  if (this.ad != 1) {
                     break label49;
                  }

                  var10000 = this;
                  var10001 = "logo";
               }

               var10000.splashImage = b(var10001);
            }

            this.ad++;
            this.af = true;
            if (this.ad == 3) {
               this.af = false;
               this.c = true;
               this.b = 19;
               if (a.e(2) == -1) {
                  this.D = 118;
                  k = 0;
                  l = A.length - 1;
                  m = 0;
                  return;
               }

               C = (byte)Math.min(a.e(2), A.length - 1);
               ratchetandclank.strings = ratchetandclank.a("/m" + B[C], 145);
               if (a.soundEnabled && a.soundPlayer != null) {
                  a.soundPlayer.queue(6, -1);
               }
            }
         }
      }
   }

   private void N() {
      try {
         a.a("/f2.v", 10, 1);
      } catch (IOException var2) {
      }

      a.c();
      a.e();
      a.soundPlayer = new SoundPlayer();
      a.soundEnabled = a.e(0) > 0;
      a.saveSlotFlags = new byte[3];
      a.saveSlotTimes = new int[3];
      this.t();
      this.o();
      this.d = true;
      ratchetandclank.strings = ratchetandclank.a("/m", 145);
   }

   private int a(Graphics var1, String var2, int var3, boolean var4) {
      var1.setColor(var4 ? 16777215 : 2914559);
      return a(var1, var2, 0, var3, 17);
   }

   private int b(Graphics var1, String var2, int var3, boolean var4) {
      var1.setColor(var4 ? 16777215 : 8421504);
      return a(var1, var2, 0, var3, 17);
   }

   private int a(Graphics var1, byte var2, int var3, boolean var4) {
      boolean var5 = false;
      int var10000;
      if (a.saveSlotFlags[var2] == 0) {
         var10000 = this.b(var1, var2 + 1 + ratchetandclank.strings[31], var3, var4);
      } else {
         Object[] var6 = new Object[]{new Integer(var2 + 1), new String(o(a.saveSlotTimes[var2]))};
         String var7 = a(ratchetandclank.strings[32], var6);
         var10000 = this.a(var1, var7, var3, var4);
      }

      return var10000;
   }

   private int a(Graphics var1, int var2, int var3, int var4, boolean var5) {
      Object[] var6 = new Object[]{new Integer(var3)};
      String var7 = a(ratchetandclank.strings[var2], var6);
      return this.a(var1, var7, var4, var5);
   }

   private int a(Graphics var1, String var2, int var3) {
      var1.setColor(14481424);
      return a(var1, var2, 64, var3, 17);
   }

   private static String o(int var0) {
      int var1 = var0 / 1000;
      int var3;
      int var2 = (var3 = var0 / 60000) / 60;
      var0 = var3 % 60;
      var1 %= 60;
      return var2 + (var0 < 10 ? ":0" : ":") + var0 + (var1 < 10 ? ":0" : ":") + var1;
   }

   private String i(int var1, int var2) {
      Object[] var3 = new Object[]{new Integer(var2)};
      return a(ratchetandclank.strings[var1], var3);
   }

   private static String a(String var0, Object[] var1) {
      int var4 = 0;
      boolean var6 = false;
      StringBuffer var8 = new StringBuffer(var0.length());
      char[] var9 = new char[var0.length()];
      var0.getChars(0, var0.length(), var9, 0);

      for (int var10 = 0; var10 < var9.length; var10++) {
         char var2;
         char var3;
         int var5;
         if ((var2 = var9[var10]) == '%' && (var5 = var10 + 1) < var9.length && (var6 = Character.isDigit(var3 = var9[var5]))) {
            var4 = Character.digit(var3, 10);
         }

         if (var6) {
            var6 = false;
            var8.append(var1[var4]);
            var10++;
         } else {
            var8.append(var2);
         }
      }

      return var8.toString();
   }

   private static int a(Graphics var0, String var1, int var2, int var3, int var4) {
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      int var10 = a.fontLineHeight;
      char[] var12 = new char[var1.length()];
      var1.getChars(0, var1.length(), var12, 0);
      var5 = (var4 & 1) > 0 ? (var2 = 0) : var2;
      int var8 = 0;
      int var9 = 0;
      char var11 = '\u0000';

      do {
         var6 = 0;
         var7 = var9;

         while (var9 < var12.length) {
            if ((var11 = var12[var9]) == ' ') {
               var6 += a.a(var11);
               var9++;
               break;
            }

            var6 += a.a(var11);
            var9++;
         }

         if (var5 + var6 <= 128 && var9 != var12.length) {
            var5 += var6;
         } else {
            if (var5 + var6 > 128) {
               var9 = var7;
            }

            if (var9 - var8 > 0) {
               ratchetandclank var10000;
               Graphics var10001;
               char[] var10002;
               int var10003;
               int var10004;
               int var10005;
               if ((var4 & 1) > 0) {
                  var10000 = a;
                  var10001 = var0;
                  var10002 = var12;
                  var10003 = var8;
                  var10004 = var9 - var8;
                  var10005 = 64;
               } else {
                  var10000 = a;
                  var10001 = var0;
                  var10002 = var12;
                  var10003 = var8;
                  var10004 = var9 - var8;
                  var10005 = var2;
               }

               var10000.a(var10001, var10002, var10003, var10004, var10005, var3, var4);
            }

            var3 += var10;
            var5 = var2;
            var8 = var9;
         }
      } while (var9 < var12.length);

      return var3;
   }

   private int O() {
      int var1 = 0;
      int[] var2 = new int[8];

      for (int var3 = 0; var3 < 8; var3++) {
         var2[var3] = Player.d[var3 * 3 + this.player.weaponLevel[var3]] - this.player.ammo[var3];
         int[] var10000;
         int var10001;
         int var10002;
         switch (var3) {
            case 0:
            case 6:
               var10000 = this.storeAmmoNeeded;
               var10001 = var3;
               var10002 = 0;
               break;
            case 1:
               var1 += var2[var3] >> 1;
               var10000 = this.storeAmmoNeeded;
               var10001 = var3;
               var10002 = var2[var3] >> 1;
               break;
            case 2:
               if ((this.player.ownedWeapons & 1 << var3) <= 0) {
                  continue;
               }

               var1 += var2[var3] >> 1;
               var10000 = this.storeAmmoNeeded;
               var10001 = var3;
               var10002 = var2[var3] >> 1;
               break;
            case 3:
            case 4:
            case 5:
            case 7:
               if ((this.player.ownedWeapons & 1 << var3) > 0) {
                  var1 += var2[var3];
                  var10000 = this.storeAmmoNeeded;
                  var10001 = var3;
                  var10002 = var2[var3];
                  break;
               }
            default:
               continue;
         }

         var10000[var10001] = var10002;
      }

      return var1;
   }
}
