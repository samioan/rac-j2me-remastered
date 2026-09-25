import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Random;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

// $VF: renamed from: b
public final class Game extends Engine {
   // $VF: renamed from: k boolean
   public static boolean leftHeld;
   // $VF: renamed from: l boolean
   public static boolean rightHeld;
   // $VF: renamed from: m boolean
   public static boolean upPressed;
   // $VF: renamed from: n boolean
   public static boolean downPressed;
   // $VF: renamed from: o boolean
   public static boolean fireHeld;
   // $VF: renamed from: p boolean
   public static boolean bootBarVisible = false;
   public static boolean q = false;
   public static boolean r = false;
   // $VF: renamed from: s int
   public static int centerX;
   // $VF: renamed from: t boolean
   public static boolean booted;
   // $VF: renamed from: u javax.microedition.lcdui.Image
   public static Image splashImage;
   // $VF: renamed from: v int
   public static int deltaTime;
   // $VF: renamed from: w int
   public static int tickAccum;
   // $VF: renamed from: x int
   public static int keyEvent;
   // $VF: renamed from: y int
   public static int keyReleaseEvent;
   // $VF: renamed from: z int
   public static int bootTimer;
   // $VF: renamed from: A int
   public static int bootStep;
   // $VF: renamed from: B e
   public SoundPlayer audio;
   // $VF: renamed from: C int
   public static int lineHeight;
   // $VF: renamed from: D boolean
   public static boolean invincible = false;
   // $VF: renamed from: E boolean
   public static boolean cheatMode = false;
   public static int F = 0;
   // $VF: renamed from: G RatchetMIDlet
   public static RatchetMIDlet ratchet;
   public static boolean H = false;
   // $VF: renamed from: I int
   public static int state;
   // $VF: renamed from: J int
   public static int prevState;
   // $VF: renamed from: K boolean
   public static boolean hudDirty;
   public static boolean L = true;
   public static boolean M = true;
   public static boolean N = true;
   public static boolean O = false;
   public static int P;
   public static int Q;
   public static int R;
   public static int S;
   public int T = 240;
   public int U;
   public int V;
   public int W;
   // $VF: renamed from: X int
   public static int cameraMinX;
   // $VF: renamed from: Y int
   public static int cameraMinY;
   // $VF: renamed from: Z int
   public static int cameraX = 0;
   // $VF: renamed from: aa int
   public static int cameraY = 0;
   public static boolean ab = false;
   public static boolean ac = false;
   public static boolean ad = false;
   public static int ae;
   public static int af = 0;
   public static String ag;
   public static String ah;
   public static int ai = -1;
   public static int aj = -1;
   public int ak;
   // $VF: renamed from: al byte[]
   public static byte[] navUp;
   // $VF: renamed from: am byte[]
   public static byte[] navDown;
   // $VF: renamed from: an byte[]
   public static byte[] navLeft;
   // $VF: renamed from: ao byte[]
   public static byte[] navRight;
   // $VF: renamed from: ap byte[]
   public static byte[] weaponNext;
   // $VF: renamed from: aq byte[]
   public static byte[] weaponPrev;
   // $VF: renamed from: ar byte[]
   public static byte[] collectiblesPerLevel;
   public static int[] as = new int[]{0, 1, 2, 3};
   public static Random at;
   // $VF: renamed from: au int
   public static int sectionId = -1;
   // $VF: renamed from: av int
   public static int targetSection = -1;
   // $VF: renamed from: aw int
   public static int worldId = 1;
   // $VF: renamed from: ax int
   public static int saveSlot = -1;
   // $VF: renamed from: ay int
   public int boltMultiplier = 1;
   // $VF: renamed from: az boolean
   public static boolean bonusMode = false;
   // $VF: renamed from: aA int
   public static int spawnCol;
   // $VF: renamed from: aB int
   public static int spawnRow;
   // $VF: renamed from: aC int
   public static int spawnCameraX;
   // $VF: renamed from: aD int
   public static int spawnCameraY;
   // $VF: renamed from: aE int
   public static int spawnSection = 0;
   // $VF: renamed from: aF c[]
   public Enemy[] enemies;
   // $VF: renamed from: aG d[]
   public Projectile[] playerShots;
   // $VF: renamed from: aH d[]
   public Projectile[] enemyShots;
   public static Sprite[] aI;
   // $VF: renamed from: aJ javax.microedition.lcdui.Image
   public static Image hudBarImage;
   public static Sprite[] aK;
   // $VF: renamed from: aL f[]
   public static Sprite[] mapNodeSprites;
   public static Image aM;
   public static Sprite[] aN;
   // $VF: renamed from: aO javax.microedition.lcdui.Image
   public static Image portraitSheet;
   public static int aP = 186;
   public static int aQ = 126;
   public static Sprite[] aR;
   public static Sprite[] aS;
   // $VF: renamed from: aT f[]
   public static Sprite[] weaponWheelSprites;
   public static Image aU;
   // $VF: renamed from: aV javax.microedition.lcdui.Image
   public static Image lockIcon;
   // $VF: renamed from: aW f[]
   public static Sprite[] doorSprites;
   // $VF: renamed from: aX f[]
   public static Sprite[] platformSprites;
   public static Sprite[] aY;
   // $VF: renamed from: aZ f[]
   public static Sprite[] fontMain;
   // $VF: renamed from: ba f[]
   public static Sprite[] fontSelected;
   // $VF: renamed from: bb f[]
   public static Sprite[] fontHighlight;
   // $VF: renamed from: bc f[]
   public static Sprite[] crateSprites;
   // $VF: renamed from: bd javax.microedition.lcdui.Image
   public static Image weaponIconSheet;
   // $VF: renamed from: be f[]
   public static Sprite[] checkpointSprites;
   // $VF: renamed from: bf int
   public static int crateW;
   // $VF: renamed from: bg int
   public static int crateH;
   // $VF: renamed from: bh f[][]
   public static Sprite[][] enemySprites;
   // $VF: renamed from: bi f[]
   public static Sprite[] itemSprites;
   // $VF: renamed from: bj f[]
   public static Sprite[] swingshotSprites;
   public static Sprite[] bk;
   public static int bl;
   // $VF: renamed from: bm int
   public static int spriteBaseHeight;
   // $VF: renamed from: bn int
   public int tileset;
   public static final int[] bo = new int[]{2085, 2088, 2089, 1062};
   // $VF: renamed from: bp int
   public static int bolts;
   // $VF: renamed from: bq int[]
   public static int[] refillPrices;
   // $VF: renamed from: br int
   public int levelStartBolts;
   // $VF: renamed from: bs int
   public static int maxEnemies = 10;
   public static int bt = 20;
   // $VF: renamed from: bu int[]
   public static int[] ziplineX1;
   // $VF: renamed from: bv int[]
   public static int[] ziplineY1;
   // $VF: renamed from: bw int[]
   public static int[] ziplineX2;
   // $VF: renamed from: bx int[]
   public static int[] ziplineY2;
   // $VF: renamed from: by int[]
   public static int[] ziplineDx;
   // $VF: renamed from: bz int[]
   public static int[] ziplineDy;
   // $VF: renamed from: bA int[]
   public static int[] ziplineIntercept;
   public static DataInputStream bB;
   // $VF: renamed from: bC short[]
   public static short[] crateX;
   // $VF: renamed from: bD short[]
   public static short[] crateY;
   // $VF: renamed from: bE byte[]
   public static byte[] crateType;
   // $VF: renamed from: bF short[]
   public short[] crateFallOffset;
   // $VF: renamed from: bG short[]
   public short[] crateRestY;
   // $VF: renamed from: bH short[]
   public short[] crateAbove;
   // $VF: renamed from: bI short[]
   public short[] crateBelow;
   // $VF: renamed from: bJ boolean[]
   public boolean[] crateFalling;
   // $VF: renamed from: bK boolean[]
   public boolean[] sectionFirstVisit;
   // $VF: renamed from: bL int[]
   public int[] crateBits;
   // $VF: renamed from: bM int[]
   public int[] enemyBits;
   // $VF: renamed from: bN int
   public static int doorBits = -1;
   // $VF: renamed from: bO int
   public static int sectionSwitchBitsA = -1;
   // $VF: renamed from: bP int
   public static int sectionSwitchBitsB = -1;
   // $VF: renamed from: bQ int
   public static int sectionSwitchBitsC = -1;
   // $VF: renamed from: bR int
   public static int levelStartDoorBits = -1;
   // $VF: renamed from: bS int
   public static int levelStartSwitchBitsA = -1;
   // $VF: renamed from: bT int
   public static int levelStartSwitchBitsB = -1;
   // $VF: renamed from: bU int
   public static int levelStartSwitchBitsC = -1;
   // $VF: renamed from: bV byte[]
   public byte[] doorBit;
   // $VF: renamed from: bW byte[]
   public byte[] doorCol;
   // $VF: renamed from: bX byte[]
   public byte[] doorRow;
   // $VF: renamed from: bY byte[]
   public byte[] switchCol;
   // $VF: renamed from: bZ byte[]
   public byte[] switchRow;
   public static byte[][] ca;
   public static byte[][] cb;
   public static byte[][] cc;
   public static byte[][] cd;
   public static byte[][] ce;
   public static byte[][] cf;
   public static int cg;
   public int ch;
   // $VF: renamed from: ci int
   public static int collectibleCol;
   // $VF: renamed from: cj int
   public static int collectibleRow;
   public static int ck = 13;
   public static int cl = 60;
   // $VF: renamed from: cm int
   public static int collectibleMask = -1;
   // $VF: renamed from: cn int
   public static int levelStartCollectibles = -1;
   // $VF: renamed from: co int
   public int bonusCol;
   // $VF: renamed from: cp int
   public int bonusRow;
   public static boolean cq = true;
   // $VF: renamed from: cr int[]
   public static int[] pickupX;
   // $VF: renamed from: cs int[]
   public static int[] pickupY;
   // $VF: renamed from: ct short[]
   public static short[] pickupVelX;
   // $VF: renamed from: cu short[]
   public static short[] pickupVelY;
   // $VF: renamed from: cv byte[]
   public static byte[] pickupKind;
   // $VF: renamed from: cw int[]
   public static int[] arenaReward = new int[]{200, 400, 400, 600, 600, 600, 800, 800, 800, 1200, 1200, 2000};
   // $VF: renamed from: cx short[]
   public static short[] platformOriginX;
   // $VF: renamed from: cy short[]
   public static short[] platformOriginY;
   // $VF: renamed from: cz short[]
   public static short[] platformOffsetX;
   // $VF: renamed from: cA short[]
   public static short[] platformOffsetY;
   // $VF: renamed from: cB byte[]
   public static byte[] platformDir;
   // $VF: renamed from: cC int
   public static int playTimeMs;
   // $VF: renamed from: cD int
   public static int menuCursor;
   // $VF: renamed from: cE int
   public static int menuChoice;
   public int cF = 0;
   // $VF: renamed from: cG int
   public static int inputIntent;
   // $VF: renamed from: cH int
   public static int lastIntent = 0;
   // $VF: renamed from: cI int
   public static int fireCooldown = 0;
   public static byte[] cJ;
   public static final short[] cK = new short[]{
      117,
      131,
      135,
      0,
      0,
      142,
      148,
      155,
      156,
      158,
      0,
      160,
      167,
      168,
      171,
      173,
      176,
      184,
      0,
      0,
      120,
      123,
      124,
      125,
      126,
      127,
      128,
      129,
      138,
      151,
      164,
      169,
      181,
      183,
      189,
      190,
      191,
      195,
      198,
      201,
      203,
      205,
      207,
      210,
      213,
      214,
      217,
      220,
      223,
      226,
      229,
      230
   };
   public static byte[] cL;
   // $VF: renamed from: cM byte[]
   public static byte[] nodeWorld;
   // $VF: renamed from: cN byte[]
   public static byte[] nodeSection;
   // $VF: renamed from: cO boolean
   public boolean messageHasPortrait = false;
   // $VF: renamed from: cP boolean
   public boolean messageActive = false;
   // $VF: renamed from: cQ java.lang.String
   public String messageText;
   // $VF: renamed from: cR int
   public static int messageCursor = -1;
   // $VF: renamed from: cS int
   public static int messageNext = -1;
   // $VF: renamed from: cT int
   public static int messageId = 0;
   public static int cU;
   public static boolean cV = true;
   // $VF: renamed from: cW int
   public static int messageFirst = -1;
   // $VF: renamed from: cX int
   public static int messageCount = 0;
   // $VF: renamed from: cY int
   public static int hintFlagsLow = -1;
   // $VF: renamed from: cZ int
   public static int hintFlagsHigh = -1;
   // $VF: renamed from: da int
   public static int unlockFlags = -1;
   // $VF: renamed from: db int
   public static int levelStartHintLow = -1;
   // $VF: renamed from: dc int
   public static int levelStartHintHigh = -1;
   // $VF: renamed from: dd int
   public static int levelStartUnlocks = 0;
   // $VF: renamed from: de int
   public static int retrySection;
   public static boolean df = false;
   public static boolean dg = false;
   public static int dh;
   // $VF: renamed from: di boolean
   public boolean inArena = false;
   // $VF: renamed from: dj boolean
   public boolean arenaHealthDrain = false;
   // $VF: renamed from: dk boolean
   public boolean arenaNoDamage = false;
   // $VF: renamed from: dl int
   public int arenaDrainTimer;
   // $VF: renamed from: dm boolean
   public boolean arenaLimitedAmmo = false;
   // $VF: renamed from: dn int
   public int levelStartWeapons = 3;
   // $VF: renamed from: do int
   public int arenaEnemiesLeft;
   // $VF: renamed from: dp int[]
   public int[] menuItemY;
   // $VF: renamed from: dq short[][]
   public short[][] mapNodes;
   // $VF: renamed from: dr byte[][]
   public byte[][] mapEdges;
   // $VF: renamed from: ds byte[]
   public static byte[] nodeRequiredBit;
   // $VF: renamed from: dt byte[]
   public static byte[] nodeEdgeCount;
   // $VF: renamed from: du byte[]
   public final byte[] edgeIndex = new byte[]{
      0,
      1,
      2,
      3,
      -1,
      -1,
      4,
      5,
      6,
      7,
      8,
      -1,
      9,
      10,
      -1,
      11,
      12,
      -1,
      13,
      -1,
      -1,
      14,
      15,
      16,
      17,
      18,
      -1,
      19,
      -1,
      -1,
      20,
      21,
      -1,
      22,
      -1,
      -1,
      23,
      24,
      -1,
      25,
      -1,
      -1
   };
   // $VF: renamed from: dv int
   public static int mapHighlight;
   public int dw;
   public static int dx;
   // $VF: renamed from: dy int
   public int levelStartMapHighlight = 1;
   // $VF: renamed from: dz int
   public static int deathCount;
   public static int dA;
   // $VF: renamed from: dB int
   public static int killCount;
   public static int dC;
   // $VF: renamed from: dD int
   public static int boltsCollected;
   // $VF: renamed from: dE int
   public static int collectiblesFound;
   public static int dF;
   public static int dG;
   public static int dH;
   public static int dI;
   // $VF: renamed from: dJ int
   public static int levelTimeMs;
   // $VF: renamed from: dK byte[]
   public static byte[] parTimes;
   public static int dL;
   // $VF: renamed from: dM int
   public static int levelScore;
   // $VF: renamed from: dN int
   public static int totalScore;
   public static int dO;
   // $VF: renamed from: dP int
   public static int resultsPage;
   // $VF: renamed from: dQ int
   public static int shotsFired;
   public int dR;
   // $VF: renamed from: dS int
   public static int shotsHit;
   // $VF: renamed from: dT boolean
   public static boolean accuracyBonus;
   // $VF: renamed from: dU boolean
   public static boolean pacifistBonus;
   // $VF: renamed from: dV boolean
   public static boolean noMissBonus;
   // $VF: renamed from: dW boolean
   public static boolean tookDamage;
   // $VF: renamed from: dX int[]
   public static int[] sectionEnemyCount;
   // $VF: renamed from: dY boolean[]
   public static boolean[] sectionEnemyFlag;
   public static byte[] dZ;
   public static final byte[] ea = new byte[]{0, 0, 10, 26, 36, 36, 26, 10};
   public static final byte[] eb = new byte[]{26, 10, 0, 0, 10, 26, 36, 36};
   public static final byte[] ec = new byte[]{3, 3, 10, 26, 33, 33, 26, 10};
   public static final byte[] ed = new byte[]{26, 10, 3, 3, 10, 26, 33, 33};
   public static final int[] ee = new int[]{1067, 2086, 2087};
   // $VF: renamed from: ef long
   public static long bootStartTime;
   private static int gx = 0;
   private static boolean gy;
   private int gz;
   private boolean gA = false;
   public int eg;
   public int eh;
   public int ei;
   public int ej;
   public int ek;
   public int el;
   // $VF: renamed from: em int
   public int loadedTileset = -1;
   public static int[] en = new int[10];
   public static int eo = 0;
   public static final byte[] ep = new byte[]{
      0,
      0,
      0,
      0,
      1,
      2,
      2,
      2,
      2,
      2,
      3,
      5,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      4,
      5,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      4,
      5,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      4,
      5,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      4,
      5,
      9,
      9,
      9,
      9,
      10,
      11,
      11,
      11,
      11,
      11,
      12,
      5,
      0,
      0,
      0,
      0,
      1,
      2,
      2,
      2,
      2,
      2,
      3,
      5,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      7,
      8,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      6,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      6,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      16,
      17,
      9,
      9,
      9,
      9,
      10,
      11,
      11,
      11,
      11,
      11,
      12,
      5
   };
   public static int eq;
   // $VF: renamed from: er int
   public int screen;
   public int es;
   public int et;
   public int eu;
   public int ev;
   public String ew;
   public boolean ex;
   public static int ey = 0;
   public static int ez = 0;
   public static int eA = -1;
   public static int eB;
   public static byte[] eC = new byte[15];
   public static byte[] eD = new byte[10];
   public static byte[] eE;
   public static boolean eF = true;
   public static boolean eG;
   public static byte[] eH;
   public static int eI;
   private int gB;
   // $VF: renamed from: eJ int
   public static int startSection;
   // $VF: renamed from: eK int
   public static int tilesetId;
   // $VF: renamed from: eL byte[][]
   public static byte[][] sectionTiles;
   // $VF: renamed from: eM int[]
   public static int[] solidColumnMask;
   // $VF: renamed from: eN byte[][]
   public byte[][] tiles;
   // $VF: renamed from: eO int
   public static int sectionCount;
   // $VF: renamed from: eP byte[]
   public static byte[] sectionExits;
   // $VF: renamed from: eQ byte[]
   public static byte[] sectionTileBase;
   public static int eR;
   public static byte[] eS;
   public static byte[] eT;
   public static final byte[] eU = new byte[]{0, 2, 1, 3};
   // $VF: renamed from: eV int[]
   public static final int[] levelInfoResIds = new int[]{2067, 2067, 2075, 2076, 2077, 2078, 2079, 2080, 2081, 2068, 2069, 2070, 2071, 2072, 2073, 2074, 2074};
   // $VF: renamed from: eW int[]
   public static final int[] levelTileResIds = new int[]{2050, 2050, 2058, 2059, 2060, 2061, 2062, 2063, 2064, 2051, 2052, 2053, 2054, 2055, 2056, 2057, 2057};
   // $VF: renamed from: eX int
   public static int playerRow;
   // $VF: renamed from: eY int
   public static int playerX;
   // $VF: renamed from: eZ int
   public static int playerY;
   // $VF: renamed from: fa int
   public static int velY;
   // $VF: renamed from: fb int
   public static int velX;
   // $VF: renamed from: fc int
   public static int stepCol;
   // $VF: renamed from: fd int
   public static int footRow;
   // $VF: renamed from: fe int
   public static int health;
   // $VF: renamed from: ff int
   public static int playerAnim;
   // $VF: renamed from: fg int
   public static int hurtTimer;
   // $VF: renamed from: fh boolean
   public static boolean facingRight;
   public static int fi;
   // $VF: renamed from: fj f[]
   public static Sprite[] weaponSprites;
   // $VF: renamed from: fk f[]
   public static Sprite[] playerSprites;
   // $VF: renamed from: fl short[]
   public static final short[] maxAmmoTable = new short[]{0, 0, 0, 100, 150, 200, 35, 45, 60, 25, 30, 40, 40, 55, 70, 200, 250, 300, 1, 1, 1, 70, 90, 110};
   // $VF: renamed from: fm short[]
   public static final short[] startAmmo = new short[]{0, 50, 20, 15, 20, 100, 1, 30};
   // $VF: renamed from: fn short[]
   public static final short[] ammoPerPickup = new short[]{0, 15, 7, 5, 8, 30, 1, 10};
   // $VF: renamed from: fo byte[]
   public static byte[] fireDelay;
   // $VF: renamed from: fp int[]
   public static final int[] weaponPrices = new int[]{50, 800, 2500, 7500, 0};
   // $VF: renamed from: fq byte[]
   public static final byte[] meleeDamage = new byte[]{3, 8, 8};
   // $VF: renamed from: fr int
   public static int maxFallSpeed;
   // $VF: renamed from: fs int
   public static int runSpeed;
   // $VF: renamed from: ft int
   public static int hitboxX;
   // $VF: renamed from: fu int
   public static int hitboxY;
   // $VF: renamed from: fv int
   public static int hitboxW;
   // $VF: renamed from: fw int
   public static int hitboxH;
   // $VF: renamed from: fx byte[]
   public static byte[] meleeBoxX;
   // $VF: renamed from: fy byte[]
   public static byte[] meleeBoxY;
   // $VF: renamed from: fz byte[]
   public static byte[] meleeBoxW;
   // $VF: renamed from: fA byte[]
   public static byte[] meleeBoxH;
   // $VF: renamed from: fB int
   public static int airState;
   public static int fC;
   // $VF: renamed from: fD int
   public static int swingshotState;
   public static int fE;
   // $VF: renamed from: fF int
   public static int swingshotTargetX;
   // $VF: renamed from: fG int
   public static int swingshotTargetY;
   // $VF: renamed from: fH int
   public static int swingshotX;
   // $VF: renamed from: fI int
   public static int swingshotY;
   // $VF: renamed from: fJ int
   public static int swingshotStepX;
   // $VF: renamed from: fK int
   public static int swingshotStepY;
   // $VF: renamed from: fL int
   public static int attackTimer;
   // $VF: renamed from: fM int
   public static int attackFrame;
   // $VF: renamed from: fN int
   public static int invulnFrames;
   // $VF: renamed from: fO int
   public static int swingshotTile;
   // $VF: renamed from: fP boolean
   public static boolean onMovingPlatform;
   // $VF: renamed from: fQ int
   public static int platformDrift;
   // $VF: renamed from: fR int
   public static int currentWeapon = 1;
   // $VF: renamed from: fS short[]
   public static short[] ammo;
   // $VF: renamed from: fT short[]
   public static short[] weaponKills;
   // $VF: renamed from: fU int
   public static int weaponsOwned = 3;
   // $VF: renamed from: fV byte[]
   public static byte[] weaponLevel;
   // $VF: renamed from: fW int
   public static int refillAllPrice;
   // $VF: renamed from: fX int
   public int levelStartWeapon = 1;
   // $VF: renamed from: fY short[]
   public short[] levelStartKills;
   // $VF: renamed from: fZ short[]
   public short[] levelStartAmmo;
   // $VF: renamed from: ga byte[]
   public byte[] levelStartLevels;
   // $VF: renamed from: gb boolean
   public boolean atSectionEdge;
   public int gc;
   public int gd;
   public int ge;
   // $VF: renamed from: gf boolean
   public boolean animPlaying = false;
   public static boolean gg = true;
   public static String gh = "";
   public int[] gi = new int[]{652482873, 766492548};
   public int[] gj = new int[16];
   public static String[] gk;
   public static int gl;
   public static int gm;
   public static int gn;
   public static int go;
   public static String[] gp;
   public static byte[] gq;
   public static int gr;
   public static int gs;
   public static int gt;
   public static int gu;
   public static boolean gv;
   public static boolean gw = false;

   static {
      System.currentTimeMillis();
      int[] var10000 = new int[]{2082, 2083, 2084};
   }

   // $VF: renamed from: m () byte[]
   private static byte[] readByteArray() {
      byte var0 = -1;
      byte[] var1 = null;

      try {
         var0 = bB.readByte();
      } catch (IOException var4) {
      }

      var1 = new byte[var0];

      for (int var2 = 0; var2 < var0; var2++) {
         try {
            var1[var2] = bB.readByte();
         } catch (IOException var3) {
         }
      }

      return var1;
   }

   // $VF: renamed from: n (int) void
   private void loadGameTables(int var1) {
      try {
         bB = this.openResource(var1);
      } catch (Exception var3) {
      }

      Enemy.maxHealth = readByteArray();
      Enemy.attackDamage = readByteArray();
      Enemy.boltDrop = readByteArray();
      Enemy.burstCount = readByteArray();
      Enemy.attackCooldown = readByteArray();
      Projectile.damage = readByteArray();
      Projectile.halfWidths = readByteArray();
      Projectile.halfHeights = readByteArray();
      Projectile.speeds = readByteArray();
      navUp = readByteArray();
      navDown = readByteArray();
      navLeft = readByteArray();
      navRight = readByteArray();
      weaponNext = readByteArray();
      weaponPrev = readByteArray();
      collectiblesPerLevel = readByteArray();
      cJ = readByteArray();
      cL = readByteArray();
      nodeWorld = readByteArray();
      nodeSection = readByteArray();
      nodeRequiredBit = readByteArray();
      nodeEdgeCount = readByteArray();
      parTimes = readByteArray();
      eH = readByteArray();
      dZ = readByteArray();
      eS = readByteArray();
      eT = readByteArray();
      fireDelay = readByteArray();
      bB = null;

      for (int var2 = 0; var2 < en.length; var2++) {
         en[var2] = -1;
      }
   }

   // $VF: renamed from: n () void
   private void checkBonusPickup() {
      if (this.bonusCol != -1) {
         int var1 = this.ak;
         int var2 = bl;
         int var3 = this.bonusCol * 57;
         int var4 = this.bonusRow * 38;
         if (rectsOverlap(playerPixelX() - hitboxX, playerPixelY() + -12 + hitboxY, hitboxW, hitboxH, var3, var4, var1, var2)) {
            this.bonusCol = this.bonusRow = -1;
            cq = false;
            this.messageHasPortrait = true;
            messageCount = cJ[34];
            this.showMessage(cK[34]);
         }
      }
   }

   // $VF: renamed from: o () void
   private static void resetLevelStats() {
      dC = 10000;
      dA = 0;
      deathCount = 0;
      killCount = 0;
      dH = 0;
      boltsCollected = 0;
      collectiblesFound = 0;
      dI = 0;
      levelTimeMs = 0;
      levelScore = 0;
      resultsPage = 0;
      shotsFired = 0;
      shotsHit = 0;
      accuracyBonus = false;
      pacifistBonus = false;
      tookDamage = false;
      noMissBonus = false;
      clearKillCounters();
   }

   // $VF: renamed from: p () int
   private int countLiveEnemies() {
      int var1 = 0;

      for (int var2 = this.enemies.length - 1; var2 >= 0; var2--) {
         if (this.enemies[var2].type != 4 && this.enemies[var2].type != -1) {
            var1++;
         }
      }

      return var1;
   }

   // $VF: renamed from: q () void
   private static void clearKillCounters() {
      for (int var0 = sectionEnemyCount.length - 1; var0 >= 0; var0--) {
         sectionEnemyFlag[var0] = false;
         sectionEnemyCount[var0] = 0;
      }
   }

   // $VF: renamed from: o (int) void
   private static void computeScore(int var0) {
      switch (var0) {
         case 0:
            return;
         case 1:
            if (!tookDamage && shotsFired != 0 && shotsFired == shotsHit) {
               accuracyBonus = true;
            }

            if (killCount == 0) {
               pacifistBonus = true;
            }

            for (int var1 = sectionEnemyCount.length - 1; var1 >= 0; var1--) {
               if (sectionEnemyCount[var1] > 0) {
                  dA = dA + sectionEnemyCount[var1];
               }
            }

            if (dH == killCount && killCount > 0 && dA == 0) {
               noMissBonus = true;
            }

            dC = killCount * 100;
            levelScore = levelScore + dC;
            dF = boltsCollected * 1;
            dG = collectiblesFound * 1000;
            levelScore = levelScore + dF;
            levelScore = levelScore + dG;
            if (levelTimeMs < (parTimes[dL] & 255)) {
               dI = ((parTimes[dL] & 255) - levelTimeMs) / 1000 * 10;
            }

            levelScore = levelScore + dI;
            return;
         case 2:
            if (pacifistBonus) {
               levelScore += 100000;
            }

            if (accuracyBonus) {
               levelScore += 100000;
            }

            if (noMissBonus) {
               levelScore += 10000;
            }
      }
   }

   public Game(RatchetMIDlet var1) {
      super(var1);
      ratchet = var1;
      this.tileset = 3;
      super.minFrameTime = 50;
      super.maxFrameTime = 100;
      centerX = 120;
   }

   // $VF: renamed from: r () void
   private void handleInput() {
      leftHeld = this.isKeyHeld((byte)2) || this.isKeyHeld((byte)17) || this.isKeyHeld((byte)20);
      rightHeld = this.isKeyHeld((byte)5) || this.isKeyHeld((byte)19) || this.isKeyHeld((byte)22);
      downPressed = this.isKeyPressed((byte)6) || this.isKeyPressed((byte)24);
      upPressed = this.isKeyPressed((byte)1) || this.isKeyPressed((byte)18) || this.isKeyPressed((byte)17) || this.isKeyPressed((byte)19);
      fireHeld = this.isKeyHeld((byte)8) || this.isKeyHeld((byte)21);
      int var1 = keyEvent;
      if (state != 0 && state != 16 && state != 17 && state != 24) {
         if (state == 4) {
            moveCursor(var1, 0, 3);
            if (var1 == 8 || var1 == 27) {
               this.audio.play(4);
               if (menuCursor == 0) {
                  if (prevState == 0 || prevState == 17 || prevState == 16 || prevState == 24) {
                     this.loadTileset();
                  }

                  state = prevState;
                  hudDirty = true;
               } else if (menuCursor == 1) {
                  state = 5;
               } else if (menuCursor == 2) {
                  state = 8;
               } else if (menuCursor == 3) {
                  state = 7;
               }

               menuCursor = 0;
               return;
            }

            if (var1 == 29) {
               this.loadTileset();
               state = 0;
               hudDirty = true;
               return;
            }
         } else if (state == 5) {
            if (var1 != 8 && var1 != 27) {
               if (var1 == 29) {
                  menuCursor = 0;
                  state = 4;
                  this.audio.play(4);
                  return;
               }
            } else if (menuCursor == 0) {
               this.audio.setEnabled(!this.audio.isEnabled());
               return;
            }
         } else if (state == 6) {
            moveCursor(var1, 0, 2);
            if (var1 == 8 || var1 == 27) {
               menuChoice = menuCursor;
               if (RatchetMIDlet.slotInUse[menuCursor] != 0) {
                  state = 9;
               } else {
                  saveSlot = menuChoice;
                  this.startNewGame(0);
                  RatchetMIDlet.refreshSlotSummaries();
               }

               menuCursor = 0;
               return;
            }

            if (var1 == 29) {
               this.enterMainMenu();
               this.gotoTitle();
               return;
            }
         } else if (state == 7) {
            moveCursor(var1, 0, 1);
            if (var1 != 8 && var1 != 27) {
               if (var1 == 29) {
                  menuCursor = 0;
                  state = 4;
                  this.audio.play(4);
                  return;
               }
            } else {
               if (menuCursor == 0) {
                  state = 4;
                  menuCursor = 0;
                  this.audio.play(4);
                  return;
               }

               if (menuCursor == 1) {
                  this.inArena = true;
                  weaponsOwned = this.levelStartWeapons;
                  currentWeapon = 1;
                  playerSprites[playerSprites.length - 1] = weaponSprites[currentWeapon];
                  this.inArena = false;
                  if (worldId != 0) {
                     ratchet.saveSlot(saveSlot);
                  }

                  this.audio.stop();
                  ratchet.destroyApp(true);
                  return;
               }
            }
         } else if (state == 8) {
            moveCursor(var1, 0, 1);
            if (var1 != 8 && var1 != 27) {
               if (var1 == 29) {
                  this.audio.play(4);
                  menuCursor = 0;
                  state = 4;
                  return;
               }
            } else {
               if (menuCursor == 0) {
                  state = 4;
                  menuCursor = 0;
                  return;
               }

               if (menuCursor == 1) {
                  this.inArena = true;
                  weaponsOwned = weaponsOwned | this.levelStartWeapons;
                  currentWeapon = 1;
                  playerSprites[playerSprites.length - 1] = weaponSprites[currentWeapon];
                  this.inArena = false;
                  if (worldId != 0) {
                     ratchet.saveSlot(saveSlot);
                  }

                  this.enterMainMenu();
                  this.gotoTitle();
                  return;
               }
            }
         } else if (state == 9) {
            moveCursor(var1, 0, 1);
            if (var1 == 8 || var1 == 27) {
               this.audio.play(4);
               if (menuCursor == 0) {
                  RatchetMIDlet.refreshSlotSummaries();
                  state = 6;
               } else if (menuCursor == 1) {
                  saveSlot = menuChoice;
                  this.startNewGame(0);
               }

               menuCursor = 0;
               return;
            }

            if (var1 == 29) {
               this.audio.play(4);
               menuCursor = 0;
               state = 6;
               return;
            }
         } else if (state == 10) {
            if (var1 == 8 || var1 == 27) {
               menuCursor = 0;
               menuChoice = 0;
               if (prevState == 21) {
                  focusMapNode();
                  state = 3;
               } else if (prevState == 15) {
                  initArenaMenu();
                  state = 1;
               }

               this.audio.play(4);
               return;
            }
         } else if (state == 11) {
            if (var1 == 29) {
               this.audio.play(4);
               menuCursor = 0;
               menuChoice = 0;
               focusMapNode();
               currentWeapon = dh;
               playerSprites[playerSprites.length - 1] = weaponSprites[currentWeapon];
               state = 3;
               return;
            }

            if (var1 != 52 && var1 != 2) {
               if (var1 != 54 && var1 != 5) {
                  if (var1 != 8) {
                     return;
                  }

                  this.audio.play(4);
                  if (menuCursor == 2 && hasHintFlag(5) || menuCursor == 3 && hasHintFlag(6) || menuCursor == 7 && refillAllPrice <= 0) {
                     return;
                  }

                  if (menuCursor != 7 && (weaponsOwned & 1 << menuCursor + 1) == 0) {
                     state = 12;
                     menuChoice = menuCursor;
                     menuCursor = 0;
                     return;
                  }

                  if (menuCursor == 7 || ammo[menuCursor + 1] < maxAmmoTable[3 * (menuCursor + 1) + weaponLevel[menuCursor + 1]]) {
                     state = 12;
                     menuChoice = menuCursor;
                     menuCursor = 0;
                     return;
                  }

                  return;
               }

               if (++menuCursor > 7) {
                  menuCursor = 0;
               }

               while (menuCursor > 3 && (weaponsOwned & 1 << menuCursor + 1) == 0 || menuCursor == 5) {
                  if (menuCursor == 7) {
                     return;
                  }

                  if (++menuCursor > 7) {
                     menuCursor = 0;
                  }
               }

               return;
            }

            if (--menuCursor < 0) {
               menuCursor = 7;
            }

            while (menuCursor > 3 && (weaponsOwned & 1 << menuCursor + 1) == 0 || menuCursor == 5) {
               if (menuCursor == 7) {
                  return;
               }

               if (--menuCursor < 0) {
                  menuCursor = 7;
               }
            }

            return;
         } else if (state == 3) {
            int var6 = 0;
            int var7 = menuCursor;
            if (var1 != 1 && var1 != 50) {
               if (var1 != 6 && var1 != 56) {
                  if (var1 != 5 && var1 != 54) {
                     if (var1 != 2 && var1 != 52) {
                        if (var1 != 8 && var1 != 27) {
                           if (var1 == 29) {
                              if (worldId != 0) {
                                 ratchet.saveSlot(saveSlot);
                              }

                              this.enterMainMenu();
                              this.gotoTitle();
                           }
                        } else if (menuCursor == 19) {
                           facingRight = true;
                           this.setPlayerAnim(0);
                           playerRow = 2;
                           playerX = 30720;
                           velY = 0;
                           velX = 0;
                           invulnFrames = 0;
                           playerY = playerRow * 38 << 8;
                           state = 11;
                           dh = currentWeapon;
                           refillAllPrice = computeRefillPrices();
                           menuCursor = 0;
                        } else if (menuCursor == 18) {
                           this.enemyBits[0] = -1;
                           this.enemyBits[1] = -1;
                           initArenaMenu();
                           state = 1;
                           System.arraycopy(ammo, 0, this.levelStartAmmo, 0, 8);
                        } else {
                           if (menuCursor == 0 && hasHintFlag(0)) {
                              this.U = 0;
                           } else if (menuCursor == 1 && hasHintFlag(1)) {
                              this.U = 1;
                           } else if (menuCursor == 2 && hasHintFlag(2)) {
                              this.U = 2;
                           } else if (menuCursor == 3 && hasHintFlag(5)) {
                              this.U = 3;
                           } else if (menuCursor == 4 && hasHintFlag(7)) {
                              this.U = 4;
                           } else if (menuCursor == 5 && hasHintFlag(8)) {
                              this.U = 5;
                           } else if (menuCursor == 6 && hasHintFlag(9)) {
                              this.U = 6;
                           } else if (menuCursor == 3 && hasHintFlag(6)) {
                              this.U = 7;
                           } else if (menuCursor == 7 && hasHintFlag(11)) {
                              this.U = 8;
                           } else if (menuCursor == 8 && hasHintFlag(12)) {
                              this.U = 9;
                           } else if (menuCursor == 9 && hasHintFlag(13)) {
                              this.U = 10;
                           } else if (menuCursor == 10 && hasHintFlag(14)) {
                              this.U = 11;
                           } else if (menuCursor == 11 && hasHintFlag(15)) {
                              this.U = 12;
                           } else if (menuCursor == 12 && hasHintFlag(16)) {
                              this.U = 13;
                           } else if (menuCursor == 17 && hasHintFlag(17)) {
                              this.U = 14;
                           } else {
                              this.U = -1;
                           }

                           this.tileset = 3;
                           this.enterLevel(nodeWorld[menuCursor], nodeSection[menuCursor]);
                           this.audio.play(5);
                           menuCursor = 0;
                        }
                     } else {
                        menuCursor = navLeft[menuCursor];

                        while ((unlockFlags & 1 << menuCursor) == 0) {
                           menuCursor = navLeft[menuCursor];
                           if (++var6 >= 15) {
                              menuCursor = var7;
                              break;
                           }
                        }
                     }
                  } else {
                     menuCursor = navRight[menuCursor];

                     while ((unlockFlags & 1 << menuCursor) == 0) {
                        menuCursor = navRight[menuCursor];
                        if (++var6 >= 15) {
                           menuCursor = var7;
                           break;
                        }
                     }
                  }
               } else {
                  menuCursor = navDown[menuCursor];

                  while ((unlockFlags & 1 << menuCursor) == 0) {
                     menuCursor = navDown[menuCursor];
                     if (++var6 >= 15) {
                        menuCursor = var7;
                        break;
                     }
                  }
               }
            } else {
               menuCursor = navUp[menuCursor];

               while ((unlockFlags & 1 << menuCursor) == 0) {
                  menuCursor = navUp[menuCursor];
                  if (++var6 >= 15) {
                     menuCursor = var7;
                     break;
                  }
               }
            }

            if (mapHighlight < 1 << menuCursor && menuCursor == 7 && var7 < 17) {
               menuCursor = var7 + 1;
               return;
            }

            if (mapHighlight < 1 << menuCursor && menuCursor < 18 && menuCursor > 7) {
               menuCursor--;
               return;
            }
         } else if (state == 12) {
            moveCursor(var1, 0, 1);
            if (var1 == 8 || var1 == 27) {
               this.audio.play(4);
               if (menuCursor == 1) {
                  if (menuChoice == 7) {
                     if (bolts < refillAllPrice) {
                        menuCursor = 0;
                        state = 13;
                        return;
                     }

                     bolts = bolts - refillAllPrice;

                     for (int var5 = 1; var5 < 8; var5++) {
                        if ((weaponsOwned & 1 << var5) > 0) {
                           ammo[var5] = maxAmmoTable[var5 * 3 + weaponLevel[var5]];
                        }
                     }
                  } else if ((weaponsOwned & 1 << menuChoice + 1) == 0) {
                     if (bolts < weaponPrices[menuChoice]) {
                        menuCursor = 0;
                        state = 13;
                        return;
                     }

                     bolts = bolts - weaponPrices[menuChoice];
                     weaponsOwned = weaponsOwned | 1 << menuChoice + 1;
                  } else {
                     if (bolts < refillPrices[menuChoice + 1]) {
                        menuCursor = 0;
                        state = 13;
                        return;
                     }

                     bolts = bolts - refillPrices[menuChoice + 1];
                     ammo[menuChoice + 1] = (short)(ammo[menuChoice + 1] + maxAmmoTable[(menuChoice + 1) * 3 + weaponLevel[menuChoice + 1]]);
                     if (ammo[menuChoice + 1] > maxAmmoTable[(menuChoice + 1) * 3 + weaponLevel[menuChoice + 1]]) {
                        ammo[menuChoice + 1] = maxAmmoTable[(menuChoice + 1) * 3 + weaponLevel[menuChoice + 1]];
                     }
                  }
               }

               menuCursor = menuChoice;
               state = 11;
               refillAllPrice = computeRefillPrices();
               return;
            }

            if (var1 == 29) {
               this.audio.play(4);
               menuCursor = menuChoice;
               state = 11;
               refillAllPrice = computeRefillPrices();
               return;
            }
         } else {
            if (state == 13) {
               menuCursor = 0;
               state = 11;
               refillAllPrice = computeRefillPrices();
               this.audio.play(4);
               return;
            }

            if (state == 1) {
               if (var1 != 6 && var1 != 56) {
                  if (var1 != 1 && var1 != 50) {
                     if (var1 != 8 && var1 != 27) {
                        if (var1 == 29) {
                           menuCursor = 0;
                           menuChoice = 0;
                           focusMapNode();
                           state = 3;
                           if ((weaponsOwned & 1 << currentWeapon) == 0 || currentWeapon == 0) {
                              currentWeapon = 1;
                              playerSprites[playerSprites.length - 1] = weaponSprites[currentWeapon];
                           }

                           System.arraycopy(this.levelStartAmmo, 0, ammo, 0, 8);
                           this.audio.play(4);
                           return;
                        }

                        return;
                     }

                     this.levelStartWeapons = weaponsOwned;
                     this.inArena = true;
                     this.arenaHealthDrain = false;
                     this.arenaNoDamage = false;
                     this.arenaLimitedAmmo = false;
                     this.arenaDrainTimer = 0;
                     switch (menuCursor) {
                        case 0:
                           this.U = 16;
                           break;
                        case 1:
                           this.U = 16;
                           break;
                        case 2:
                           this.arenaLimitedAmmo = true;
                           weaponsOwned = 2;
                           currentWeapon = 1;
                           this.U = 21;
                           break;
                        case 3:
                           this.U = 16;
                           break;
                        case 4:
                           this.arenaLimitedAmmo = true;
                           weaponsOwned = 1;
                           currentWeapon = 0;
                           this.U = 17;
                           break;
                        case 5:
                           this.arenaLimitedAmmo = true;
                           weaponsOwned = 2;
                           currentWeapon = 1;
                           this.U = 21;
                           break;
                        case 6:
                           this.arenaHealthDrain = true;
                           this.U = 18;
                           break;
                        case 7:
                           this.arenaNoDamage = true;
                           this.U = 19;
                           break;
                        case 8:
                           this.U = 16;
                           break;
                        case 9:
                           this.arenaNoDamage = true;
                           this.U = 19;
                           break;
                        case 10:
                           this.arenaHealthDrain = true;
                           this.U = 18;
                           break;
                        case 11:
                           this.arenaLimitedAmmo = true;
                           weaponsOwned = 64;
                           currentWeapon = 6;
                           this.U = 20;
                     }

                     playerSprites[playerSprites.length - 1] = weaponSprites[currentWeapon];
                     sectionId = menuCursor;
                     menuChoice = menuCursor;
                     state = 14;
                     return;
                  }

                  if (menuCursor == 0) {
                     return;
                  }

                  menuCursor--;
                  if (menuCursor >= 8) {
                     dx = 2;
                     return;
                  }

                  if (menuCursor >= 4) {
                     dx = 1;
                     return;
                  }

                  dx = 0;
                  return;
               }

               if ((unlockFlags & 1 << menuCursor + 21) != 0 && menuCursor != 11) {
                  menuCursor++;
                  if (menuCursor >= 8) {
                     dx = 2;
                     return;
                  }

                  if (menuCursor >= 4) {
                     dx = 1;
                     return;
                  }

                  dx = 0;
                  return;
               }

               return;
            } else if (state == 2) {
               if (var1 == 8 || var1 == 27) {
                  if (!bonusMode) {
                     menuCursor = 1;
                     state = 23;
                     return;
                  }

                  this.enterMainMenu();
                  this.gotoTitle();
                  if (totalScore > 0) {
                     return;
                  }
               }
            } else if (state == 14) {
               if (var1 == 8 || var1 == 27) {
                  this.startArena(menuChoice);
                  hudDirty = true;
                  return;
               }

               if (var1 == 29) {
                  initArenaMenu();
                  state = 1;
                  hudDirty = true;
                  weaponsOwned = this.levelStartWeapons;
                  this.inArena = false;
                  return;
               }
            } else if (state != 15 && state != 19) {
               if (state == 18) {
                  if (var1 == 8 || var1 == 27) {
                     resultsPage++;
                     if (resultsPage != 3 && (resultsPage != 2 || pacifistBonus || accuracyBonus)) {
                        computeScore(resultsPage);
                        return;
                     }

                     menuCursor = 0;
                     menuChoice = 0;
                     state = 21;
                     resultsPage = 0;
                     return;
                  }
               } else if (state == 21) {
                  moveCursor(var1, 0, 1);
                  if (var1 == 8 || var1 == 27) {
                     this.audio.play(4);
                     if (menuCursor == 0) {
                        prevState = state;
                        state = 10;
                        totalScore = totalScore + levelScore;
                        ratchet.saveSlot(saveSlot);
                        return;
                     }

                     if (menuCursor == 1) {
                        doorBits = levelStartDoorBits;
                        sectionSwitchBitsA = levelStartSwitchBitsA;
                        sectionSwitchBitsB = levelStartSwitchBitsB;
                        sectionSwitchBitsC = levelStartSwitchBitsC;
                        collectibleMask = levelStartCollectibles;
                        hintFlagsLow = levelStartHintLow;
                        hintFlagsHigh = levelStartHintHigh;
                        unlockFlags = levelStartUnlocks;
                        mapHighlight = this.levelStartMapHighlight;
                        bolts = this.levelStartBolts;
                        currentWeapon = this.levelStartWeapon;
                        playerSprites[playerSprites.length - 1] = weaponSprites[currentWeapon];
                        weaponsOwned = this.levelStartWeapons;

                        for (int var2 = 0; var2 < 8; var2++) {
                           weaponKills[var2] = this.levelStartKills[var2];
                           weaponLevel[var2] = this.levelStartLevels[var2];
                           ammo[var2] = this.levelStartAmmo[var2];
                        }

                        if (worldId == 1) {
                           cq = true;
                        }

                        cg = this.ch;
                        this.tileset = 3;
                        this.enterLevel(worldId, retrySection);
                        this.audio.play(5);
                        return;
                     }
                  }
               } else {
                  if (state == 22) {
                     int var4 = 0;
                     int var3 = currentWeapon;
                     switch (var1) {
                        case 1:
                        case 5:
                        case 50:
                        case 54:
                           for (currentWeapon = weaponNext[currentWeapon]; (weaponsOwned & 1 << currentWeapon) == 0 && var4 < 8; var4++) {
                              currentWeapon = weaponNext[currentWeapon];
                           }
                           break;
                        case 2:
                        case 6:
                        case 52:
                        case 56:
                           for (currentWeapon = weaponPrev[currentWeapon]; (weaponsOwned & 1 << currentWeapon) == 0 && var4 < 8; var4++) {
                              currentWeapon = weaponPrev[currentWeapon];
                           }
                           break;
                        case 8:
                        case 53:
                           menuCursor = 0;
                           menuChoice = 0;
                           state = 0;
                           hudDirty = true;
                     }

                     if ((weaponsOwned & 1 << currentWeapon) == 0) {
                        currentWeapon = var3;
                     }

                     playerSprites[playerSprites.length - 1] = weaponSprites[currentWeapon];
                     return;
                  }

                  if (state == 23) {
                     moveCursor(var1, 0, 1);
                     if (var1 == 8 || var1 == 27) {
                        this.audio.play(4);
                        if (menuCursor == 0) {
                           this.enterMainMenu();
                           this.gotoTitle();
                           return;
                        }

                        if (menuCursor == 1) {
                           this.startBonusMode();
                        }
                     }
                  }
               }
            } else if (var1 == 8 || var1 == 27) {
               this.enemyBits[0] = -1;
               this.enemyBits[1] = -1;
               if (state == 19) {
                  initArenaMenu();
                  state = 1;
               } else if (state == 15) {
                  prevState = state;
                  state = 10;
                  ratchet.saveSlot(saveSlot);
               }

               hudDirty = true;
               return;
            }
         }
      } else {
         this.handleGameplayInput(var1);
      }
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics, int, int) void
   private static void drawWeaponHighlight(Graphics var0, int var1, int var2) {
      boolean var3 = false;
      var0.setColor(15658514);

      for (int var4 = 0; var4 < 7; var4++) {
         var0.drawLine(ea[var4] + var1, eb[var4] + var2, ea[var4 + 1] + var1, eb[var4 + 1] + var2);
         var0.drawLine(ec[var4] + var1, ed[var4] + var2, ec[var4 + 1] + var1, ed[var4 + 1] + var2);
      }

      var0.drawLine(ea[7] + var1, eb[7] + var2, ea[0] + var1, eb[0] + var2);
      var0.drawLine(ec[7] + var1, ed[7] + var2, ec[0] + var1, ed[0] + var2);
   }

   // $VF: renamed from: b (javax.microedition.lcdui.Graphics, int, int) void
   private static void drawLockIcon(Graphics var0, int var1, int var2) {
      var0.drawImage(lockIcon, var1 + 2, var2 + 2, 20);
   }

   // $VF: renamed from: s () void
   private static void updatePlatforms() {
      for (int var0 = 3; var0 >= 0; var0--) {
         if (platformDir[var0] != -1) {
            switch (platformDir[var0]) {
               case 0:
                  platformOffsetX[var0] = (short)(platformOffsetX[var0] + 2);
                  if (platformOffsetX[var0] > 114) {
                     platformOffsetX[var0] = (short)(platformOffsetX[var0] - 2);
                     platformDir[var0] = (byte)(platformDir[var0] + 2);
                  }
                  break;
               case 1:
                  platformOffsetY[var0] = (short)(platformOffsetY[var0] + 2);
                  if (platformOffsetY[var0] > 76) {
                     platformOffsetY[var0] = (short)(platformOffsetY[var0] - 2);
                     platformDir[var0] = (byte)(platformDir[var0] + 2);
                  }
                  break;
               case 2:
                  platformOffsetX[var0] = (short)(platformOffsetX[var0] - 2);
                  if (platformOffsetX[var0] < 0) {
                     platformOffsetX[var0] = (short)(platformOffsetX[var0] + 2);
                     platformDir[var0] = (byte)(platformDir[var0] - 2);
                  }
                  break;
               case 3:
                  platformOffsetY[var0] = (short)(platformOffsetY[var0] - 2);
                  if (platformOffsetY[var0] < 0) {
                     platformOffsetY[var0] = (short)(platformOffsetY[var0] + 2);
                     platformDir[var0] = (byte)(platformDir[var0] - 2);
                  }
            }
         }
      }
   }

   // $VF: renamed from: j (int) void
   public final void onLifecycle(int var1) {
      if (var1 == 3) {
         if (this.audio != null) {
            this.audio.close();
         }
      } else if (var1 != 0) {
         if (var1 == 1) {
            this.onPause();
         } else if (var1 == 2) {
            this.requestClear();
            if (state == 105) {
               this.audio.play(3);
            }
         }
      }

      super.onLifecycle(var1);
   }

   // $VF: renamed from: t () void
   private void onPause() {
      if (this.screen != 15 && this.audio != null) {
         this.audio.stop();
      }

      if (state == 0 || state == 17 || state == 16 || state == 24) {
         prevState = state;
         state = 4;
         this.gA = true;
      }

      velY = 0;
      airState = -1;
      inputIntent = 0;
      lastIntent = 0;
      this.clearKeys();
   }

   // $VF: renamed from: i () void
   public final void onStartApp() {
      if (booted) {
         if (state != 4) {
            state = 105;
         }

         menuCursor = 0;
         this.requestClear();
      } else {
         state = 99;
      }
   }

   // $VF: renamed from: d (javax.microedition.lcdui.Graphics) void
   private void fillBlack(Graphics var1) {
      this.resetClip(var1);
      var1.setColor(0);
      var1.fillRect(0, 0, 240, 320);
   }

   // $VF: renamed from: a () void
   public final void update() {
      deltaTime = super.frameTime;
      switch (state) {
         case 99:
            bootTimer = 2000;
            state++;
            bootStartTime = System.currentTimeMillis();
            splashImage = this.loadImage(ee[state - 100]);
            bootStep = 0;
            this.loadGameTables(1034);
            break;
         case 100:
         case 101:
         case 102:
            this.requestClear();
            bootTimer = bootTimer - deltaTime;
            this.pollKeys();
            if (this.anyKeyPressed()) {
               bootTimer = 0;
            }

            if (bootTimer <= 0 && bootStep > 16) {
               bootTimer = 2000;
               state++;
               int var3;
               if (state < 103 && (var3 = state - 100) < 2) {
                  splashImage = this.loadImage(ee[var3]);
               }
            }

            if (bootStep <= 16) {
               this.loadAssetsStep();
            }
            break;
         case 103:
            this.requestClear();
            state++;
            break;
         case 104:
            booted = true;
            state++;
            this.resetFrameTimingNow();
            this.enterMainMenu();
            this.audio.play(3);
            splashImage = null;
            break;
         case 105:
            gx = gx + deltaTime;
            if (gx > 4000) {
               gx = 0;
               gy ^= true;
            }

            tickAccum = tickAccum + deltaTime;
            if (tickAccum > 40) {
               tickAccum -= 40;
               this.pollKeys();
               if (F <= 0) {
                  this.translateKeys();
                  if (!q) {
                     this.enemies[0].type = 1;
                     this.enemies[1].type = 1;
                     this.enemies[2].type = 1;
                     this.enemies[3].type = 1;
                     this.startAnim(this.enemies[0].slot, 1024, 1);
                     this.stepAnim(this.enemies[0].slot, 258);
                     this.startAnim(this.enemies[1].slot, 1024, 1);
                     this.stepAnim(this.enemies[1].slot, 470);
                     this.startAnim(this.enemies[2].slot, 1024, 1);
                     this.stepAnim(this.enemies[2].slot, 84);
                     this.startAnim(this.enemies[3].slot, 1024, 1);
                     q = true;
                  }

                  this.updateMenuScreen();
                  this.handleScreenInput();
               } else {
                  F -= 40;
               }

               this.endInputFrame();
            }

            H = true;
            break;
         default:
            tickAccum = tickAccum + deltaTime;
            q = false;
            if (tickAccum > 40 || H) {
               H = false;

               while (tickAccum >= 40) {
                  tickAccum -= 40;
               }

               this.pollKeys();
               this.translateKeys();
               this.handleInput();
               this.updateAutofire();
               if (state == 17) {
                  state = 0;
               }

               if (state == 2) {
                  this.W++;
                  if (this.W == 15) {
                     this.W = 80;
                  }

                  if (this.W > 82) {
                     this.W = 80;
                     if (this.V < 15) {
                        this.V++;
                     } else {
                        this.V--;
                     }
                  }
               } else {
                  this.W = 0;
               }

               if (state == 11 || state == 12) {
                  this.updatePlayer(true);
               }

               if (state == 3 || state == 1) {
                  levelTimeMs = 0;
               }

               if (state == 0 && !this.messageActive) {
                  levelTimeMs = levelTimeMs + deltaTime;
                  playTimeMs = playTimeMs + deltaTime;
                  S = playerX / 57 >> 8;
                  this.updatePickupsFall();
                  this.updateFallingCrates();
                  updatePlatforms();
                  this.checkBonusPickup();
                  this.updatePlayer(false);
                  if (invulnFrames <= 0) {
                     this.checkPlayerHit();
                  } else {
                     invulnFrames--;
                  }

                  this.updateCamera();
                  this.checkCollectiblePickup();

                  for (int var1 = 9; var1 >= 0; var1--) {
                     this.playerShots[var1].update(false);
                     this.enemyShots[var1].update(true);
                     if (this.playerShots[var1].type != -1) {
                        this.playerShotHitsCrates(var1);
                     }

                     if (this.enemyShots[var1].type != -1) {
                        this.enemyShotHitsCrates(var1);
                     }
                  }

                  this.arenaEnemiesLeft = 0;

                  for (int var2 = maxEnemies - 1; var2 >= 0; var2--) {
                     if (this.enemies[var2].type != -1) {
                        if (this.enemies[var2].state != 5 && playerAnim != 10) {
                           this.checkEnemyHit(var2);
                        }

                        this.enemies[var2].update();
                        this.enemies[var2].updateAnim();
                        if (worldId == 16 && this.enemies[var2].type != 4) {
                           this.arenaEnemiesLeft++;
                        }
                     }
                  }

                  if (this.inArena) {
                     this.checkArenaGoal();
                  }
               }

               this.endInputFrame();
            }
      }

      this.gz = this.gz + deltaTime;
      if (this.gz > 10000) {
         this.gz = 0;
      }
   }

   // $VF: renamed from: u () void
   private void translateKeys() {
      int var1 = super.keysPressed;
      if (super.keysPressed != 0) {
         if ((var1 & 2097408) != 0) {
            keyEvent = 8;
         }

         if ((var1 & 4194336) != 0) {
            keyEvent = 5;
         }

         if ((var1 & 1048580) != 0) {
            keyEvent = 2;
         }

         if ((var1 & 262146) != 0) {
            keyEvent = 1;
         }

         if ((var1 & 16777280) != 0) {
            keyEvent = 6;
         }

         if ((var1 & 131072) != 0) {
            keyEvent = 49;
         }

         if ((var1 & 524288) != 0) {
            keyEvent = 51;
         }

         if ((var1 & 8388608) != 0) {
            keyEvent = 55;
         }

         if ((var1 & 33554432) != 0) {
            keyEvent = 57;
         }

         if ((var1 & 65536) != 0) {
            keyEvent = 48;
         }

         if ((var1 & 1024) != 0) {
            keyEvent = 42;
         }

         if ((var1 & 8) != 0) {
            keyEvent = 35;
         }

         if ((var1 & 134217728) != 0) {
            keyEvent = 27;
         }

         if ((var1 & 604012560) != 0) {
            keyEvent = 29;
            return;
         }
      } else {
         keyEvent = 0;
      }
   }

   // $VF: renamed from: v () void
   private void translateReleaseKeys() {
      keyReleaseEvent = 0;
      int var1 = super.keysReleased;
      if (super.keysReleased != 0) {
         if ((var1 & 2097408) != 0) {
            setKeyRelease(8);
         }

         if ((var1 & 4718624) != 0) {
            setKeyRelease(5);
         }

         if ((var1 & 1179652) != 0) {
            setKeyRelease(2);
         }

         if ((var1 & 262146) != 0) {
            setKeyRelease(1);
         }

         if ((var1 & 16777280) != 0) {
            setKeyRelease(6);
         }

         if ((var1 & 8388608) != 0) {
            setKeyRelease(55);
         }

         if ((var1 & 33554432) != 0) {
            setKeyRelease(57);
         }

         if ((var1 & 65536) != 0) {
            setKeyRelease(48);
         }

         if ((var1 & 1024) != 0) {
            setKeyRelease(42);
         }

         if ((var1 & 8) != 0) {
            setKeyRelease(35);
         }

         if ((var1 & 134217728) != 0) {
            setKeyRelease(27);
         }

         if ((var1 & 536870912) != 0) {
            setKeyRelease(29);
         }
      }
   }

   // $VF: renamed from: p (int) void
   private static void setKeyRelease(int var0) {
      if (keyReleaseEvent == 0) {
         keyReleaseEvent = var0;
      }
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics) void
   public final void render(Graphics var1) {
      if (this.gA && state == 4) {
         this.gA = false;
      }

      this.resetClip(var1);
      switch (state) {
         case 100:
         case 101:
         case 102:
         case 103:
         case 104:
            if (super.bordersCleared) {
               drawSplash(var1);
               return;
            }

            drawLoadingBar(var1);
            return;
         case 105:
            this.renderMenuScreen(var1);
            return;
         default:
            if (state == 4) {
               boolean var107 = false;
               this.c(var1, 1);
               int var10000 = this.drawHeading(var1, 43);
               int var86 = 0;
               var86 = var10000 + lineHeight;
               var86 = 6 + var86;
               this.menuItemY[0] = var86;
               var86 = 6 + this.a(var1, this.getString(44), var86, menuCursor == 0, 16777215);
               this.menuItemY[1] = var86;
               var86 = 6 + this.a(var1, this.getString(4), var86, menuCursor == 1, 16777215);
               this.menuItemY[2] = var86;
               var86 = 6 + this.a(var1, this.getString(46), var86, menuCursor == 2, 16777215);
               this.menuItemY[3] = var86;
               this.a(var1, this.getString(47), var86, menuCursor == 3, 16777215);
               this.drawSoftKeys(var1, 8, 9);
               this.d(var1, this.menuItemY[menuCursor]);
               this.setFont(fontMain);
               this.drawTicker(var1, this.getString(232 + this.U), 286, 55, 185);
            } else if (state == 5) {
               boolean var85 = false;
               this.e(var1, 32);
               this.menuItemY[0] = 174;
               this.a(var1, this.getString(this.audio.isEnabled() ? 33 : 34), 174, menuCursor == 0, 16777215);
               this.drawSoftKey(var1, 8, true);
               this.drawSoftKey(var1, 9, false);
               this.d(var1, this.menuItemY[menuCursor]);
            } else if (state == 6) {
               int var82 = 0;
               this.e(var1, 313);
               boolean var106 = false;
               this.menuItemY[0] = 178;
               var82 = 6 + this.drawSaveSlot(var1, 0, 85, 178, menuCursor == 0);
               this.menuItemY[1] = var82;
               var82 = 6 + this.drawSaveSlot(var1, 1, 85, var82, menuCursor == 1);
               this.menuItemY[2] = var82;
               this.drawSaveSlot(var1, 2, 85, var82, menuCursor == 2);
               this.drawSoftKeys(var1, 8, 9);
               this.d(var1, this.menuItemY[menuCursor]);
            } else if (state == 7) {
               int var80 = 0;
               this.e(var1, 51);
               boolean var105 = false;
               this.menuItemY[0] = 174;
               var80 = 6 + this.a(var1, this.getString(12), 174, menuCursor == 0, 16777215);
               this.menuItemY[1] = var80;
               this.a(var1, this.getString(11), var80, menuCursor == 1, 16777215);
               this.drawSoftKeys(var1, 8, 9);
               this.d(var1, this.menuItemY[menuCursor]);
            } else if (state == 8) {
               int var78 = 0;
               this.e(var1, 50);
               boolean var104 = false;
               this.menuItemY[0] = 174;
               var78 = 6 + this.a(var1, this.getString(12), 174, menuCursor == 0, 16777215);
               this.menuItemY[1] = var78;
               this.a(var1, this.getString(11), var78, menuCursor == 1, 16777215);
               this.drawSoftKeys(var1, 8, 9);
               this.d(var1, this.menuItemY[menuCursor]);
            } else if (state == 9) {
               int var76 = 0;
               this.e(var1, 49);
               boolean var103 = false;
               this.menuItemY[0] = 174;
               var76 = 6 + this.a(var1, this.getString(12), 174, menuCursor == 0, 16777215);
               this.menuItemY[1] = var76;
               this.a(var1, this.getString(11), var76, menuCursor == 1, 16777215);
               this.drawSoftKeys(var1, 8, 9);
               this.d(var1, this.menuItemY[menuCursor]);
            } else if (state == 10) {
               boolean var75 = false;
               this.e(var1, 48);
               var1.setColor(16777215);
               this.setFont(fontMain);
               this.a(var1, this.getString(315), 42, 184, 17, 198);
               this.drawSoftKey(var1, 10, true);
            } else if (state == 11) {
               int var59 = 0;
               int var101 = 0;
               int var115 = weaponIconSheet.getHeight() / 12;
               int var124 = weaponIconSheet.getWidth();
               int var128 = 76;
               boolean var132 = false;
               this.e(var1, 31);
               var59 = 159 + var115 + 4;
               this.setFont(fontMain);
               if (menuCursor != 7) {
                  currentWeapon = menuCursor + 1;
                  playerSprites[playerSprites.length - 1] = weaponSprites[currentWeapon];
               }

               this.drawPlayer(var1, 0, -20);
               this.resetClip(var1);
               if ((menuCursor != 2 || !hasHintFlag(5)) && (menuCursor != 3 || !hasHintFlag(6))) {
                  if (menuCursor == 7) {
                     var59 = this.a(var1, this.getString(63), var59, false, 16777215);
                     var59 = this.a(var1, this.getString(316), var59, true);
                     var59 = this.a(var1, 61, bolts, 85, var59, false);
                     var59 = this.a(var1, 62, refillAllPrice, 85, var59, false);
                     if (refillAllPrice <= 0) {
                        this.a(var1, this.getString(287), var59, false, 16777215);
                     }
                  } else if ((weaponsOwned & 1 << menuCursor + 1) > 0) {
                     var59 = this.a(var1, this.getString(63), var59, false, 16777215);
                     var59 = this.a(var1, this.getString(54 + menuCursor), var59, true);
                     var59 = this.a(var1, 61, bolts, 85, var59, false);
                     var59 = this.a(var1, 62, refillPrices[menuCursor + 1], 85, var59, false);
                     if (ammo[menuCursor + 1] < maxAmmoTable[3 * (menuCursor + 1) + weaponLevel[menuCursor + 1]]) {
                        String var136 = RatchetMIDlet.substitute(
                           this.getString(285), ammo[menuCursor + 1], String.valueOf(maxAmmoTable[3 * (menuCursor + 1) + weaponLevel[menuCursor + 1]])
                        );
                        this.a(var1, var136, var59, false, 16777215);
                     } else {
                        this.a(var1, this.getString(287), var59, false, 16777215);
                     }
                  } else {
                     var59 += 15;
                     var59 = this.a(var1, this.getString(54 + menuCursor), var59, true);
                     var59 = this.a(var1, 61, bolts, 85, var59, false);
                     this.a(var1, 62, weaponPrices[menuCursor], 85, var59, false);
                  }
               } else {
                  var59 += 15;
                  var59 = this.b(var1, this.getString(54 + menuCursor), var59, 17);
                  String var135 = RatchetMIDlet.substitute(this.getString(61), bolts, null);
                  var59 = this.b(var1, var135, var59, 17);
                  this.b(var1, this.getString(283), var59, 17);
               }

               if ((var101 = menuCursor + 1) == 7) {
                  var101--;
                  if ((weaponsOwned & 32) == 0) {
                     var101--;
                  }
               }

               if (menuCursor == 7) {
                  var101--;
                  if ((weaponsOwned & 128) == 0) {
                     var101--;
                  }

                  if ((weaponsOwned & 32) == 0) {
                     var101--;
                  }
               }

               this.drawPageIndicator(var1, var101, 5 + ((weaponsOwned & 32) > 0 ? 1 : 0) + ((weaponsOwned & 128) > 0 ? 1 : 0));
               this.drawSoftKey(var1, 9, false);
               if (this.dw > 6) {
                  this.resetClip(var1);
                  aR[6].draw(var1, 66, 160, 0);
                  aR[5].draw(var1, 174, 160, 0);
               }

               var1.setClip(0, 159, 240, var115);

               for (int var137 = 0; var137 < 4 + ((weaponsOwned & 32) > 0 ? 1 : 0); var137++) {
                  var1.drawImage(weaponIconSheet, var128, 159 - var137 * var115, 0);
                  var128 += var124 + 2;
               }

               if ((weaponsOwned & 32) <= 0) {
                  var128 += var124 + 2;
               }

               if ((weaponsOwned & 128) > 0) {
                  var1.drawImage(weaponIconSheet, var128, 159 - 6 * var115, 0);
               }

               var128 += var124 + 2;
               var1.drawImage(weaponIconSheet, var128, 159 - 11 * var115, 0);
               var1.setColor(16777215);
               this.resetClip(var1);
               if (menuCursor > 5) {
                  var1.drawRect(78 + (menuCursor - 1) * (var124 + 2) - 3, 158, var124 + 1, var115 + 2);
               } else {
                  var1.drawRect(78 + menuCursor * (var124 + 2) - 3, 158, var124 + 1, var115 + 2);
               }
            } else if (state == 3) {
               gx = gx + deltaTime;
               if (gx > 4000) {
                  gx = 0;
                  gy ^= true;
               }

               this.resetClip(var1);
               this.c(var1, 0);
               boolean var58 = false;
               boolean var100 = false;
               this.drawHeading(var1, 13);
               String var138 = this.getString(15 + nodeWorld[menuCursor] - 1);
               int var143 = 0;
               int var146 = aK[0].width << 1;
               short var149 = aK[0].height;
               int var150 = 240 - var146 >> 1;
               int var152 = 254 - (var149 >> 1) + 4;
               var1.setClip(var150, var152, var146, var149);
               aK[0].draw(var1, var150, var152, 0);
               aK[0].draw(var1, var150 + var146, var152, 2);
               var150 += 11;
               var146 -= 22;
               var1.setClip(var150, var152, var146, var149);
               var1.setColor(16060689);
               this.setFont(fontHighlight);
               int var15 = this.stringWidth(var138);
               int var16 = 0;
               if (var15 <= var146) {
                  var16 = var146 - var15 >> 1;
               } else {
                  int var17 = Math.max(0, Math.min(2000, gx - 1000));
                  var16 = (var146 - var15) * (gy ? 2000 - var17 : var17) / 2000;
               }

               this.drawString(var1, var138, var150 + var16, 253, 20);
               this.resetClip(var1);

               for (int var126 = 0; var126 < 15; var126++) {
                  if ((unlockFlags & 1 << nodeRequiredBit[var126]) != 0 && var126 - 1 >= 0) {
                     for (int var133 = 0; var133 < nodeEdgeCount[var126 - 1]; var133++) {
                        byte var112 = this.mapEdges[this.edgeIndex[(var126 - 1) * 3 + var133]][0];
                        byte var121 = this.mapEdges[this.edgeIndex[(var126 - 1) * 3 + var133]][1];
                        var1.setColor(16060689);
                        var1.drawLine(this.mapNodes[var112][1], this.mapNodes[var112][2], this.mapNodes[var121][1], this.mapNodes[var121][2]);
                        var1.setColor(8388608);
                        if (this.mapNodes[var112][1] == this.mapNodes[var121][1]) {
                           var1.drawLine(this.mapNodes[var112][1] - 1, this.mapNodes[var112][2], this.mapNodes[var121][1] - 1, this.mapNodes[var121][2]);
                           var1.drawLine(this.mapNodes[var112][1] + 1, this.mapNodes[var112][2], this.mapNodes[var121][1] + 1, this.mapNodes[var121][2]);
                        } else {
                           var1.drawLine(this.mapNodes[var112][1], this.mapNodes[var112][2] - 1, this.mapNodes[var121][1], this.mapNodes[var121][2] - 1);
                           var1.drawLine(this.mapNodes[var112][1], this.mapNodes[var112][2] + 1, this.mapNodes[var121][1], this.mapNodes[var121][2] + 1);
                        }
                     }
                  }
               }

               for (int var127 = 0; var127 <= 19; var127++) {
                  int var113 = this.mapNodes[var127][1];
                  int var122 = this.mapNodes[var127][2];
                  short var131;
                  if ((var131 = this.mapNodes[var127][0]) >= 0 && var131 <= 12 || var131 >= 17 && var131 <= 19 && (unlockFlags & 1 << var131) > 0) {
                     int var134 = 1;
                     if (var131 >= 0 && var131 <= 6 && (unlockFlags & 1 << var131) > 0) {
                        var134 = 0;
                     }

                     if (var131 >= 7 && var131 <= 12 && (unlockFlags & 1 << var131 + 1) > 0) {
                        var134 = 0;
                     }

                     if (var131 == 17) {
                        var134 = 6;
                     } else if (var131 == 18) {
                        var134 = 2;
                        var113 += 4;
                     } else if (var131 == 19) {
                        var134 = 4;
                        var113 += 4;
                     }

                     if (var131 == 17 && this.dw > 10) {
                        var134++;
                     }

                     int var139 = mapNodeSprites[var134].height + 4;
                     if ((mapHighlight & 1 << var131) > 0 && this.dw > 5) {
                        var1.setColor(255, 155, 0);
                        this.resetClip(var1);
                        if (var131 != 17 && var131 != 19) {
                           var1.fillArc(var113 + 4, var122 + 4, var139, var139, 0, 360);
                        } else {
                           var1.fillArc(var113 - 2, var122 - 2, var139, var139, 0, 360);
                        }
                     }

                     mapNodeSprites[var134].draw(var1, var113, var122, 0);
                     if (menuCursor == var131) {
                        var1.setColor(16060689);
                        if (var131 != 17 && var131 != 18 && var131 != 19) {
                           var139--;
                           var113 += 4;
                           var122 += 4;
                           var1.drawLine(var113, var122, var113 + var139, var122);
                           var1.drawLine(var113, var122 + var139, var113 + var139, var122 + var139);
                           var1.drawLine(var113, var122 + var139, var113, var122);
                           var1.drawLine(var113 + var139, var122 + var139, var113 + var139, var122);
                        } else {
                           int var140 = var139 - 4;
                           var146 = (var143 = var140 - 3 >> 1) + 3;
                           var1.drawLine(var113, var122, var113 + var143, var122);
                           var1.drawLine(var113, var122, var113, var122 + var143);
                           var1.drawLine(var113 + var140, var122, var113 + var146, var122);
                           var1.drawLine(var113 + var140, var122, var113 + var140, var122 + var143);
                           var1.drawLine(var113, var122 + var140, var113 + var143, var122 + var140);
                           var1.drawLine(var113, var122 + var140, var113, var122 + var146);
                           var1.drawLine(var113 + var140, var122 + var140, var113 + var146, var122 + var140);
                           var1.drawLine(var113 + var140, var122 + var140, var113 + var140, var122 + var146);
                        }
                     }
                  }
               }

               this.resetClip(var1);
               if (nodeWorld[menuCursor] < 15) {
                  var1.drawImage(aU, 37, 212, 0);
                  var1.setColor(16060689);
                  byte var142 = collectiblesPerLevel[nodeWorld[menuCursor] - 1];
                  this.drawString(var1, collectedFlag(nodeWorld[menuCursor]) + "/" + var142, 42, 229, 0);
               }

               this.drawSoftKeys(var1, 10, 448);
            } else if (state == 12) {
               this.e(var1, 52);
               int var44 = 0;
               var44 = 149 + lineHeight;
               if (menuChoice != 7) {
                  currentWeapon = menuChoice + 1;
                  playerSprites[playerSprites.length - 1] = weaponSprites[currentWeapon];
               }

               this.drawPlayer(var1, 0, -20);
               this.resetClip(var1);
               if (menuChoice == 7) {
                  var44 = this.a(var1, this.getString(63), var44, false, 16777215);
                  var44 = this.a(var1, this.getString(316), var44, true);
                  var44 = this.a(var1, 61, bolts, 85, var44, false);
                  var44 = this.a(var1, 62, refillAllPrice, 85, var44, false);
               } else if ((weaponsOwned & 1 << menuChoice + 1) != 0) {
                  var44 = this.a(var1, this.getString(63), var44, false, 16777215);
                  var44 = this.a(var1, this.getString(54 + menuChoice), var44, true);
                  var44 = this.a(var1, 61, bolts, 85, var44, false);
                  var44 = this.a(var1, 62, refillPrices[menuChoice + 1], 85, var44, false);
               } else {
                  var44 += 15;
                  var44 = this.a(var1, this.getString(54 + menuChoice), var44, true);
                  var44 = this.a(var1, 61, bolts, 85, var44, false);
                  var44 = this.a(var1, 62, weaponPrices[menuChoice], 85, var44, false);
               }

               boolean var99 = false;
               var44 += lineHeight >> 2;
               this.menuItemY[0] = var44 + 6;
               var44 = 6 + this.a(var1, this.getString(12), var44, menuCursor == 0, 16777215);
               this.menuItemY[1] = var44;
               this.a(var1, this.getString(11), var44, menuCursor == 1, 16777215);
               this.d(var1, this.menuItemY[menuCursor]);
               this.drawSoftKeys(var1, 8, 9);
            } else if (state == 13) {
               this.e(var1, 64);
               this.drawSoftKey(var1, 9, false);
            } else if (state == 1) {
               this.fillBlack(var1);
               int var42 = this.e(var1, 29);
               var42 += 149;
               if (dx == 0) {
                  this.menuItemY[0] = var42;
                  if ((unlockFlags & 1048576) != 0) {
                     var42 = this.a(var1, this.getString(65), var42, menuCursor == 0);
                     this.menuItemY[1] = var42;
                  }

                  if ((unlockFlags & 2097152) != 0) {
                     var42 = this.a(var1, this.getString(66), var42, menuCursor == 1);
                     this.menuItemY[2] = var42;
                  }

                  if ((unlockFlags & 4194304) != 0) {
                     var42 = this.a(var1, this.getString(67), var42, menuCursor == 2);
                     this.menuItemY[3] = var42;
                  }

                  if ((unlockFlags & 8388608) != 0) {
                     this.a(var1, this.getString(68), var42, menuCursor == 3);
                  }
               } else if (dx == 1) {
                  this.menuItemY[0] = var42;
                  if ((unlockFlags & 16777216) != 0) {
                     var42 = this.a(var1, this.getString(69), var42, menuCursor == 4);
                     this.menuItemY[1] = var42;
                  }

                  if ((unlockFlags & 33554432) != 0) {
                     var42 = this.a(var1, this.getString(70), var42, menuCursor == 5);
                     this.menuItemY[2] = var42;
                  }

                  if ((unlockFlags & 67108864) != 0) {
                     var42 = this.a(var1, this.getString(71), var42, menuCursor == 6);
                     this.menuItemY[3] = var42;
                  }

                  if ((unlockFlags & 134217728) != 0) {
                     this.a(var1, this.getString(72), var42, menuCursor == 7);
                  }
               } else if (dx == 2) {
                  this.menuItemY[0] = var42;
                  if ((unlockFlags & 268435456) != 0) {
                     var42 = this.a(var1, this.getString(73), var42, menuCursor == 8);
                     this.menuItemY[1] = var42;
                  }

                  if ((unlockFlags & 536870912) != 0) {
                     var42 = this.a(var1, this.getString(74), var42, menuCursor == 9);
                     this.menuItemY[2] = var42;
                  }

                  if ((unlockFlags & 1073741824) != 0) {
                     var42 = this.a(var1, this.getString(75), var42, menuCursor == 10);
                     this.menuItemY[3] = var42;
                  }

                  if ((unlockFlags & -2147483648) != 0) {
                     this.a(var1, this.getString(76), var42, menuCursor == 11);
                  }
               }

               DirectGraphics var130 = DirectUtils.getDirectGraphics(var1);
               if (menuCursor != 11 && (unlockFlags & 1 << menuCursor + 21) != 0) {
                  boolean var120 = false;
                  boolean var98 = false;
                  var130.fillTriangle(220, 275, 210, 275, 215, 285, 16746544);
                  var130.drawTriangle(220, 275, 210, 275, 215, 285, 12539914);
               }

               if (menuCursor != 0 && (unlockFlags & 1 << menuCursor + 19) != 0) {
                  boolean var125 = false;
                  boolean var111 = false;
                  var130.fillTriangle(220, 147, 210, 147, 215, 137, 16746544);
                  var130.drawTriangle(220, 147, 210, 147, 215, 137, 12539914);
               }

               this.drawSoftKeys(var1, 8, 9);
            } else if (state == 2) {
               int var38 = 0;
               int var97 = centerX;
               short var110 = aS[14].width;
               this.fillBlack(var1);
               this.c(var1, 0);
               var38 = this.drawHeading(var1, 80) + (var1.getFont().getHeight() + 0 >> 1);
               this.resetClip(var1);
               aS[this.V].draw(var1, var97 - (var110 >> 1), var38, 0);
               var1.setColor(9114112);
               var1.fillRect((240 - aP >> 1) + 3, 156, aP - 5, aQ - 12);

               for (int var119 = 0; var119 < 28; var119++) {
                  aN[var119].draw(var1, (240 - aP >> 1) - 5, 149, 0);
               }

               var1.setColor(16777215);
               this.setFont(fontMain);
               var38 = this.a(var1, this.getString(327) + formatTime(playTimeMs), 164, false, 16777215);
               var38 = this.a(var1, this.getString(320) + collectedCount(), var38, false, 16777215) + lineHeight;
               this.a(var1, this.getString(328) + totalScore, var38, true);
               this.drawSoftKey(var1, 10, true);
            } else if (state == 14) {
               int var36 = 161;
               this.e(var1, 29);
               var1.setColor(16777215);
               this.setFont(fontMain);
               if (this.arenaHealthDrain) {
                  var36 = this.a(var1, this.getString(256), 40, 161, 0, 200);
               }

               if (this.arenaNoDamage) {
                  var36 = this.a(var1, this.getString(258), 40, var36, 0, 200);
               }

               var36 = this.a(var1, this.getString(257), 40, var36, 0, 200);
               if (this.arenaLimitedAmmo) {
                  this.messageText = RatchetMIDlet.substitute(this.getString(259), 0, this.getString(currentWeapon + 53));
                  var36 = this.a(var1, this.messageText, 40, var36, 0, 200);
               }

               this.messageText = RatchetMIDlet.substitute(this.getString(279), arenaReward[sectionId], null);
               this.a(var1, this.messageText, 40, var36, 0, 200);
               this.drawSoftKeys(var1, 10, 9);
            } else if (state == 15) {
               int var34 = 0;
               this.e(var1, 80);
               var1.setColor(16777215);
               this.setFont(fontMain);
               if (sectionId < 11) {
                  var34 = this.a(var1, this.getString(281), 40, 161, 0, 200);
               } else {
                  var34 = this.a(var1, this.getString(282), 40, 161, 0, 200);
               }

               this.messageText = RatchetMIDlet.substitute(this.getString(280), arenaReward[sectionId], null);
               this.a(var1, this.messageText, 40, var34, 0, 200);
               this.drawSoftKey(var1, 10, true);
            } else if (state == 19) {
               boolean var33 = false;
               this.e(var1, 329);
               var1.setColor(9114112);
               var1.fillRect((240 - aP >> 1) + 3, 156, aP - 5, aQ - 12);

               for (int var96 = 0; var96 < 28; var96++) {
                  aN[var96].draw(var1, (240 - aP >> 1) - 5, 149, 0);
               }

               var1.setColor(16777215);
               this.setFont(fontMain);
               this.b(var1, this.getString(435), 151 + (aQ >> 1), 33);
               this.drawSoftKey(var1, 10, true);
            } else if (state == 18) {
               int var26 = 0;
               boolean var95 = false;
               boolean var109 = false;
               boolean var118 = false;
               this.e(var1, 317);
               var1.setColor(16777215);
               var26 = 161;
               this.setFont(fontMain);
               switch (resultsPage) {
                  case 0:
                     var26 = 161 + lineHeight * 2;
                     var26 = this.a(var1, this.getString(this.dR), var26, 17);
                     break;
                  case 1:
                     this.resetClip(var1);
                     var26 = this.b(var1, this.getString(318) + killCount, 159, 17);
                     var26 = this.b(var1, this.getString(319) + boltsCollected, var26, 17);
                     var26 = this.b(var1, this.getString(320) + collectiblesFound, var26, 17);
                     var26 = this.b(var1, this.getString(321) + formatTime(levelTimeMs), var26, 17);
                     var26 = this.b(var1, this.getString(322) + formatTime(parTimes[dL] & 255), var26, 17);
                     break;
                  case 2:
                     if (pacifistBonus) {
                        var26 = this.a(var1, this.getString(323), 161, false, 16777215);
                     }

                     if (accuracyBonus) {
                        var26 = this.a(var1, this.getString(324), var26, false, 16777215);
                     }

                     if (noMissBonus) {
                        var26 = this.a(var1, this.getString(325), var26, false, 16777215);
                     }
               }

               if (resultsPage > 0) {
                  this.a(var1, this.getString(326) + levelScore, var26, false);
               }

               this.drawSoftKey(var1, 10, true);
            } else if (state == 21) {
               int var24 = 0;
               this.e(var1, 311);
               this.menuItemY[0] = 174;
               var24 = this.a(var1, this.getString(45), 174, menuCursor == 0, 16777215) + 3;
               this.menuItemY[1] = var24;
               this.a(var1, this.getString(310), var24, menuCursor == 1, 16777215);
               this.drawSoftKey(var1, 8, true);
               this.d(var1, this.menuItemY[menuCursor]);
            } else if (state == 22) {
               boolean var94 = false;
               this.resetClip(var1);
               var1.setColor(0);
               short var108 = 170;
               if (worldId == 16) {
                  var108 = 160;
               }

               var1.setColor(8194560);
               var1.fillArc(centerX - 50, var108 - 50, 100, 100, 0, 360);

               for (int var116 = 0; var116 <= 6; var116++) {
                  weaponWheelSprites[var116].draw(var1, centerX, var108, 0);
               }

               for (int var117 = 1; var117 <= 7; var117++) {
                  if ((weaponsOwned & 1 << var117) == 0) {
                     drawLockIcon(var1, centerX + dZ[(var117 << 1) - 2], var108 + dZ[(var117 << 1) - 1]);
                  }
               }

               if (currentWeapon >= 1) {
                  drawWeaponHighlight(var1, centerX + dZ[(currentWeapon << 1) - 2], var108 + dZ[(currentWeapon << 1) - 1]);
               }

               int var21;
               if (currentWeapon != 6) {
                  var21 = 160 - (var1.getFont().getHeight() + 0 + (var1.getFont().getHeight() + 0 >> 1)) + 10 + 6;
               } else {
                  var21 = 160 - (var1.getFont().getHeight() + 0) + 10;
               }

               if (worldId == 16) {
                  var21 -= 10;
               }

               var1.setColor(16777215);
               var21 = this.a(var1, this.getString(53 + currentWeapon), var21, 17);
               var21 = this.a(var1, this.getString(331) + (weaponLevel[currentWeapon] + 1), var21, 17);
               if (currentWeapon != 6) {
                  this.b(var1, this.getString(260) + ": " + ammo[currentWeapon], var21, 17);
               }
            } else if (state == 23) {
               this.e(var1, -1);
               var1.setColor(14614528);
               this.a(var1, this.getString(312), 42, 5, 17, 198);
               int var19 = 0;
               boolean var93 = false;
               this.menuItemY[0] = 174;
               var19 = 6 + this.a(var1, this.getString(12), 174, menuCursor == 0, 16777215);
               this.menuItemY[1] = var19;
               this.a(var1, this.getString(11), var19, menuCursor == 1, 16777215);
               this.drawSoftKey(var1, 8, true);
               this.d(var1, this.menuItemY[menuCursor]);
            } else {
               if (!H) {
                  if (df) {
                     invulnFrames = 1;
                  }

                  this.drawTileLayer(var1);
                  var1.setClip(0, 21, 240, 299);
                  this.drawWorldObjects(var1);
                  this.drawEntities(var1);
                  if (worldId == 16) {
                     this.resetClip(var1);
                     var1.setColor(0);
                     var1.fillRect(0, 299, 240, 21);
                     this.setFont(fontHighlight);
                     this.drawString(var1, this.getString(332) + String.valueOf(this.arenaEnemiesLeft), centerX, 302, 17);
                  }

                  if (hudDirty) {
                     this.setFont(fontMain);
                     this.resetClip(var1);
                     var1.setColor(0);
                     var1.fillRect(0, 0, 240, 21);
                     boolean var2 = false;
                     boolean var3 = false;
                     boolean var4 = false;
                     boolean var5 = false;
                     boolean var6 = false;
                     boolean var7 = false;
                     int var8 = weaponIconSheet.getHeight() / 12;
                     int var9 = weaponIconSheet.getWidth();
                     if (health < 0) {
                        health = 0;
                     }

                     int var10 = 20 - health;
                     int var14 = var8 >> 1;
                     var1.drawImage(hudBarImage, 0, 0, 0);
                     var1.setColor(255, 255, 255);
                     int var11 = currentWeapon;
                     if (currentWeapon != 6 && var11 != 0) {
                        if (ai != ammo[var11]) {
                           ai = ammo[var11];
                           ag = String.valueOf(ammo[var11]);
                        }

                        this.drawString(var1, ag, 3 + var9 + 6, 4, 0);
                     }

                     if (aj != bolts) {
                        aj = bolts;
                        ah = String.valueOf(bolts);
                     }

                     this.drawString(var1, ah, 214, 4, 24);
                     var1.setColor(165, 19, 0);

                     for (int var145 = 0; var145 < var10; var145++) {
                        int var12 = (var145 >> 1) * var14;
                        int var13 = (var145 & 1) * var14;
                        var1.fillRect(83 + var12, 6 + var13 - 1, var14, var14);
                     }

                     if (currentWeapon != 0) {
                        var1.setClip(3, 4, var9, var8);
                        var1.drawImage(weaponIconSheet, 3, 4 - (currentWeapon - 1) * var8, 0);
                     }

                     hudDirty = false;
                  }

                  if (ac) {
                     int var18 = weaponIconSheet.getHeight() / 12;
                     int var92 = weaponIconSheet.getWidth();
                     var1.setClip(240 - var92 >> 1, 21, var92, var18);
                     var1.drawImage(weaponIconSheet, 240 - var92 >> 1, 21 - 7 * var18, 0);
                  }

                  if (this.messageActive) {
                     this.drawMessageBox(var1);
                     messageNext = this.drawMessageText(var1, messageCursor);
                     hudDirty = true;
                  }
               }
            }
      }
   }

   // $VF: renamed from: e (javax.microedition.lcdui.Graphics) void
   private static void drawSplash(Graphics var0) {
      Image var1 = splashImage;
      splashImage = null;
      if (var1 != null) {
         var0.setColor(0);
         var0.fillRect(0, 0, 240, 320);
         var0.drawImage(var1, 240 - var1.getWidth() >> 1, 320 - var1.getHeight() >> 1, 20);
      }

      if (bootStep <= 16) {
         drawLoadingBar(var0);
      }
   }

   // $VF: renamed from: f (javax.microedition.lcdui.Graphics) void
   private static void drawLoadingBar(Graphics var0) {
      if (bootBarVisible) {
         var0.setColor(13369344);
         var0.fillRect(39, 298, (163 * bootStep >> 4) - 1, 9);
         var0.setColor(16777215);
         var0.drawRect(38, 297, 163, 10);
      }
   }

   // $VF: renamed from: w () void
   private void updateCamera() {
      int var1 = 0;
      int var2 = 0;
      int var4 = playerPixelX();
      int var5 = playerPixelY();
      if (!ab) {
         if (facingRight) {
            var1 = -(var4 - 57);
         } else {
            var1 = -(var4 + 57 - 240);
         }
      } else {
         var1 = -(af - centerX);
         if (var4 > af + 28 || var4 < af - 28 || playerPixelY() == this.groundY(false)) {
            ab = false;
         }
      }

      if (ac) {
         var2 = -(var5 + 44 - 320);
      } else {
         var2 = -(var5 + 44 + 76 - 320);
      }

      int var3 = var2 - cameraY;
      cameraY += var3 >> 1;
      if (cameraX > var1) {
         cameraX -= 10;
         if (cameraX < var1) {
            cameraX = var1;
         }
      } else if (cameraX < var1) {
         cameraX += 10;
         if (cameraX > var1) {
            cameraX = var1;
         }
      }

      if (cameraX > 0) {
         cameraX = 0;
      }

      if (cameraX < -1356) {
         cameraX = -1356;
      }

      if (cameraY > 0) {
         cameraY = 0;
      }

      if (cameraY < -364) {
         cameraY = -364;
      }

      this.setScroll(cameraX, cameraY - 21);
   }

   // $VF: renamed from: x () void
   private void loadAssetsStep() {
      switch (bootStep) {
         case 0:
            playerSprites = this.loadSprites(1049);
            this.loadAnimSet(1028);
            int[] var1 = new int[4];
            this.startAnim(bt + 1, 1028, 0);
            this.getAnimBounds(playerSprites, bt + 1, 0, 0, 0, var1);
            spriteBaseHeight = var1[3] + var1[1];
            dx = 0;
            break;
         case 1:
            if (System.currentTimeMillis() - bootStartTime > 500L) {
               bootBarVisible = true;
            }
            break;
         case 2:
            weaponWheelSprites = this.loadSprites(1058);
            aY = this.loadSprites(1036);
            break;
         case 3:
            enemySprites = new Sprite[8][];
            enemySprites[2] = this.loadSprites(1053);
            this.loadAnimSet(1029);
            portraitSheet = this.loadImage(1089);
            break;
         case 4:
            enemySprites[1] = this.loadSprites(1037);
            this.loadAnimSet(1024);
            break;
         case 5:
            enemySprites[3] = this.loadSprites(1057);
            fontMain = this.loadSprites(1040);
            byte[] var2 = this.getResourceBytes(1095);
            fontHighlight = this.loadSprites(1040, var2);
            var2 = this.getResourceBytes(1094);
            fontSelected = this.loadSprites(1040, var2);
            this.setFont(fontHighlight);
            break;
         case 6:
            enemySprites[4] = this.loadSprites(1054);
            this.loadAnimSet(1030);
            aN = this.loadSprites(1051);
            aM = this.loadImage(1084);
            break;
         case 7:
            enemySprites[0] = this.loadSprites(1038);
            this.loadAnimSet(1025);
            break;
         case 8:
            aR = this.loadSprites(1046);
            hudBarImage = this.loadImage(1087);
            aI = this.loadSprites(1056);
            aK = this.loadSprites(1055);
            aU = this.loadImage(1082);
            break;
         case 9:
            weaponSprites = this.loadSprites(1052);
            Projectile.sprites = this.loadSprites(1044);
            this.loadAnimSet(1027);
            break;
         case 10:
            itemSprites = this.loadSprites(1041);
            eo = itemSprites[5].width / 3;
            swingshotSprites = this.loadSprites(1048);
            platformSprites = this.loadSprites(1047);
            checkpointSprites = this.loadSprites(1050);
            mapNodeSprites = this.loadSprites(1045);
            break;
         case 11:
            at = new Random();
            bl = itemSprites[7].width;
            this.ak = itemSprites[7].height;
            weaponIconSheet = this.loadImage(1088);
            doorSprites = this.loadSprites(1043);
            crateSprites = this.loadSprites(1042);
            crateW = crateSprites[0].width;
            crateH = crateSprites[0].height;
            break;
         case 12:
            this.loadAnimSet(1031);
            this.loadAnimSet(1032);
            this.loadAnimSet(1033);
            this.loadAnimSet(1026);
            bk = this.loadSprites(1039);
            this.startAnim(maxEnemies, 1026, 0);
            enemySprites[7] = this.loadSprites(1059);
            enemySprites[5] = this.loadSprites(1060);
            enemySprites[6] = this.loadSprites(1061);
            lineHeight = this.fontHeight();
            System.gc();
            break;
         case 13:
            this.loadStringTable(2098);
            lockIcon = this.loadImage(2090);
            totalScore = 0;
            cameraMinX = -1356;
            cameraMinY = -364;
            break;
         case 14:
            eR = 0;
            sectionExits = new byte[48];
            sectionTileBase = new byte[12];
            sectionTiles = new byte[336][18];
            solidColumnMask = new int[28];
            this.tiles = new byte[28][18];
            break;
         case 15:
            this.enemies = new Enemy[maxEnemies];

            for (int var3 = maxEnemies - 1; var3 >= 0; var3--) {
               this.enemies[var3] = new Enemy(this, var3);
            }

            Enemy.loadStats();
            this.playerShots = new Projectile[10];
            this.enemyShots = new Projectile[10];

            for (int var5 = 9; var5 >= 0; var5--) {
               this.playerShots[var5] = new Projectile(this);
               this.enemyShots[var5] = new Projectile(this);
            }

            this.audio = new SoundPlayer(this, 7);

            for (int var6 = 0; var6 < 7; var6++) {
               this.audio.load(var6, 2091 + var6, var6 == 3 ? -1 : 1, 0);
            }
            break;
         case 16:
            this.switchCol = new byte[3];
            this.switchRow = new byte[3];
            this.doorBit = new byte[3];
            this.doorCol = new byte[3];
            this.doorRow = new byte[3];
            ca = new byte[2][10];
            cb = new byte[2][10];
            cc = new byte[2][10];
            cd = new byte[2][10];
            ce = new byte[2][10];
            cf = new byte[2][10];
            ziplineX1 = new int[4];
            ziplineY1 = new int[4];
            ziplineX2 = new int[4];
            ziplineY2 = new int[4];
            ziplineDx = new int[4];
            ziplineDy = new int[4];
            ziplineIntercept = new int[4];
            crateX = new short[50];
            crateY = new short[50];
            crateType = new byte[50];
            this.crateFallOffset = new short[50];
            this.crateRestY = new short[50];
            this.crateBits = new int[10];
            this.crateAbove = new short[50];
            this.crateFalling = new boolean[50];
            this.crateBelow = new short[50];
            this.sectionFirstVisit = new boolean[6];
            this.enemyBits = new int[2];
            pickupX = new int[12];
            pickupY = new int[12];
            pickupKind = new byte[12];
            pickupVelX = new short[12];
            pickupVelY = new short[12];
            platformOriginX = new short[4];
            platformOriginY = new short[4];
            platformOffsetX = new short[4];
            platformOffsetY = new short[4];
            platformDir = new byte[4];
            sectionEnemyCount = new int[12];
            sectionEnemyFlag = new boolean[12];
            this.menuItemY = new int[14];
            this.mapNodes = new short[49][3];
            this.mapEdges = new byte[28][2];
            refillPrices = new int[8];
            this.initPlayer();
            this.loadWorldMapData();
      }

      bootStep++;
   }

   // $VF: renamed from: y () void
   private void loadWorldMapData() {
      try {
         DataInputStream var1 = this.openResource(1035);

         for (int var2 = 0; var2 < 49; var2++) {
            for (int var3 = 0; var3 < 3; var3++) {
               this.mapNodes[var2][var3] = (short)((byte)var1.read() & 0xFF);
               if (var3 == 1) {
                  this.mapNodes[var2][var3] = (short)(this.mapNodes[var2][var3] * 240 / 176);
               } else if (var3 == 2) {
                  this.mapNodes[var2][var3] = (short)(this.mapNodes[var2][var3] * 320 / 208);
               }
            }
         }

         for (int var5 = 0; var5 < 28; var5++) {
            for (int var6 = 0; var6 < 2; var6++) {
               this.mapEdges[var5][var6] = (byte)var1.read();
            }
         }
      } catch (Exception var4) {
      }
   }

   // $VF: renamed from: q (int) void
   private void startNewGame(int var1) {
      swingshotState = -2;
      df = false;
      this.messageActive = false;
      cq = true;
      this.U = 0;
      this.dR = 0;
      this.boltMultiplier = 1;
      bonusMode = false;
      this.resetProgress();
      if (var1 == 0 || cheatMode) {
         this.loadLevelTiles(var1);
         sectionId = startSection;
         mapHighlight = 1;
         targetSection = sectionId;
         this.buildSection(sectionId, true);
         spawnCol = 2;
         spawnRow = 13;
         if (cheatMode && var1 != 0) {
            spawnCol = 6;
            spawnRow = 13;

            for (int var2 = 0; var2 < weaponLevel.length; var2++) {
               weaponLevel[var2] = 2;
               this.levelStartLevels[var2] = 2;
            }
         }

         playerX = spawnCol * 57 + 22 << 8;
         playerRow = footRow = spawnRow;
         playerAnim = 0;
         this.startAnim(bt + 1, 1028, 0);
         playerY = playerRow * 38 << 8;
         health = 20;
         playTimeMs = 0;
         sectionEnemyCount[sectionId] = this.countLiveEnemies();
         invulnFrames = 10;
         facingRight = true;
         bolts = 0;
         this.setPlayerAnim(0);
         ammo[1] = 100;
         this.levelStartAmmo[1] = 100;
         cameraX = -22;
         cameraY = -256;
      }

      hudDirty = true;
      state = 17;
   }

   // $VF: renamed from: z () void
   private void resetProgress() {
      resetLevelStats();
      worldId = 1;
      doorBits = -1;
      sectionSwitchBitsA = -1;
      sectionSwitchBitsB = -1;
      sectionSwitchBitsC = -1;
      levelStartDoorBits = -1;
      levelStartSwitchBitsA = -1;
      levelStartSwitchBitsB = -1;
      levelStartSwitchBitsC = -1;
      cg = -1;
      this.ch = -1;
      collectibleMask = -1;
      hintFlagsLow = -1;
      hintFlagsHigh = -1;
      levelStartCollectibles = -1;
      levelStartHintLow = -1;
      levelStartHintHigh = -1;
      levelStartUnlocks = 1;
      this.levelStartMapHighlight = 1;
      this.levelStartBolts = 0;
      this.levelStartWeapon = 1;
      this.levelStartWeapons = 3;

      for (int var2 = 0; var2 < 8; var2++) {
         weaponKills[var2] = this.levelStartKills[var2] = 0;
         weaponLevel[var2] = this.levelStartLevels[var2] = 0;
         ammo[var2] = this.levelStartAmmo[var2] = 0;
      }

      unlockFlags = 1;
      weaponsOwned = 3;
      currentWeapon = 1;
      playerSprites[playerSprites.length - 1] = weaponSprites[currentWeapon];

      for (int var1 = 7; var1 >= 0; var1--) {
         weaponKills[var1] = 0;
         weaponLevel[var1] = 0;
         ammo[var1] = startAmmo[var1];
      }

      for (int var3 = 0; var3 < 10; var3++) {
         this.crateBits[var3] = -1;
      }

      for (int var4 = 0; var4 < 6; var4++) {
         this.sectionFirstVisit[var4] = false;
      }

      for (int var5 = 0; var5 < 2; var5++) {
         this.enemyBits[var5] = -1;
      }

      menuCursor = 0;
   }

   // $VF: renamed from: i (int, int) void
   private void enterLevel(int var1, int var2) {
      levelStartDoorBits = doorBits;
      levelStartSwitchBitsA = sectionSwitchBitsA;
      levelStartSwitchBitsB = sectionSwitchBitsB;
      levelStartSwitchBitsC = sectionSwitchBitsC;
      levelStartCollectibles = collectibleMask;
      levelStartHintLow = hintFlagsLow;
      levelStartHintHigh = hintFlagsHigh;
      levelStartUnlocks = unlockFlags;
      this.levelStartMapHighlight = mapHighlight;
      this.levelStartBolts = bolts;
      this.levelStartWeapon = currentWeapon;
      this.levelStartWeapons = weaponsOwned;
      retrySection = var2;

      for (int var3 = 0; var3 < 8; var3++) {
         this.levelStartKills[var3] = weaponKills[var3];
         this.levelStartLevels[var3] = weaponLevel[var3];
         this.levelStartAmmo[var3] = ammo[var3];
      }

      this.ch = cg;
      swingshotState = -2;
      df = false;
      this.messageActive = false;

      for (int var4 = 0; var4 < 10; var4++) {
         this.crateBits[var4] = -1;
      }

      for (int var5 = 0; var5 < 2; var5++) {
         this.enemyBits[var5] = -1;
      }

      state = 0;
      worldId = var1;
      this.loadLevelTiles(var1);
      facingRight = true;
      this.boltMultiplier = 1;
      this.dR = 0;
      sectionId = var2;
      menuCursor = 0;
      targetSection = sectionId;
      invulnFrames = 10;
      sectionEnemyCount[sectionId] = this.countLiveEnemies();
      this.buildSection(sectionId, true);
      playerX = spawnCol * 57 + 22 << 8;
      playerRow = footRow = spawnRow;
      playerAnim = 0;
      this.startAnim(bt + 1, 1028, 0);
      playerY = playerRow * 38 << 8;
      health = 20;
      hudDirty = true;
      cameraX = spawnCameraX;
      cameraY = spawnCameraY;
      resetLevelStats();
      lastIntent = 0;
      inputIntent = 0;
   }

   // $VF: renamed from: r (int) void
   private static void clearHintFlag(int var0) {
      if (var0 >= 0 && var0 <= 19) {
         hintFlagsLow &= ~(1 << var0);
      } else {
         if (var0 >= 20 && var0 <= 51) {
            hintFlagsHigh &= ~(1 << var0 - 32);
         }
      }
   }

   // $VF: renamed from: s (int) boolean
   private static boolean hasHintFlag(int var0) {
      if (var0 >= 0 && var0 <= 19) {
         return (hintFlagsLow & 1 << var0) != 0;
      } else {
         return var0 < 20 || var0 > 51 ? false : (hintFlagsHigh & 1 << var0 - 32) != 0;
      }
   }

   // $VF: renamed from: j (int, int) void
   private static void updateSectionLock(int var0, int var1) {
      int var2;
      int var3;
      if (var0 >= 1 && var0 <= 5) {
         var2 = (var0 - 1) * 6 + var1;
         var3 = sectionSwitchBitsA;
      } else if (var0 >= 6 && var0 <= 10) {
         var2 = (var0 - 6) * 6 + var1;
         var3 = sectionSwitchBitsB;
      } else {
         if (var0 < 11 || var0 > 15) {
            doorBits |= 1;
            return;
         }

         var2 = (var0 - 11) * 6 + var1;
         var3 = sectionSwitchBitsC;
      }

      if ((var3 & 1 << var2) == 0) {
         doorBits &= 254;
      } else {
         doorBits |= 1;
      }
   }

   // $VF: renamed from: k (int, int) void
   private static void clearSectionSwitch(int var0, int var1) {
      if (var0 >= 0 && var0 <= 5) {
         int var4 = (var0 - 1) * 6 + var1;
         sectionSwitchBitsA &= ~(1 << var4);
      } else if (var0 >= 6 && var0 <= 10) {
         int var3 = (var0 - 6) * 6 + var1;
         sectionSwitchBitsB &= ~(1 << var3);
      } else {
         if (var0 >= 11 && var0 <= 15) {
            int var2 = (var0 - 11) * 6 + var1;
            sectionSwitchBitsC &= ~(1 << var2);
         }
      }
   }

   // $VF: renamed from: t (int) boolean
   private static boolean collectibleAvailable(int var0) {
      return (collectibleMask & 1 << var0) != 0;
   }

   // $VF: renamed from: A () int
   private static int collectedCount() {
      int var1 = 0;

      for (int var0 = (cl >> 1) - 1; var0 >= 0; var0--) {
         if ((collectibleMask & 1 << var0) == 0) {
            var1++;
         }
      }

      return var1;
   }

   // $VF: renamed from: u (int) int
   private static int collectedFlag(int var0) {
      return (collectibleMask & 1 << var0) == 0 ? 1 : 0;
   }

   // $VF: renamed from: B () void
   private void respawn() {
      this.audio.play(5);
      swingshotState = -2;

      for (int var1 = 0; var1 < 6; var1++) {
         this.sectionFirstVisit[var1] = true;
      }

      sectionId = spawnSection;
      sectionEnemyCount[sectionId] = this.countLiveEnemies();
      this.buildSection(spawnSection, false);
      invulnFrames = 10;
      playerX = spawnCol * 57 + 22 << 8;
      playerRow = spawnRow;
      cameraX = spawnCameraX;
      cameraY = spawnCameraY;
      playerY = playerRow * 38 << 8;
      health = 20;
      velY = 0;
      velX = 0;
      this.setPlayerAnim(0);
      hudDirty = true;
   }

   // $VF: renamed from: v (int) void
   private void exitToMap(int var1) {
      this.audio.play(5);
      if (var1 == -1) {
         menuCursor = 0;
         menuChoice = 0;
         focusMapNode();
         ratchet.saveSlot(saveSlot);
         state = 3;
         if (this.inArena) {
            this.enemyBits[0] = -1;
            this.enemyBits[1] = -1;
            this.inArena = false;
            initArenaMenu();
            state = 1;
            weaponsOwned = this.levelStartWeapons;
         }
      }
   }

   // $VF: renamed from: C () void
   private void applyTilesetChange() {
      if (this.tileset != tilesetId) {
         this.tileset = tilesetId;
         this.loadTileset();
      }
   }

   // $VF: renamed from: D () void
   private void loadTileset() {
      if (this.tileset != 3) {
         if (this.loadedTileset != this.tileset) {
            this.loadedTileset = this.tileset;
            this.initTileMap(28, 18, this.loadImage(bo[this.tileset]), 57, 38);
            this.setViewSize(240, 299);
            this.eg = 0;
            this.eh = 0;
            this.ei = 0;
            if (this.tileset == 0 || this.tileset == 1 || this.tileset == 2) {
               this.eg = this.addAnimatedTile(22);
               this.eh = this.addAnimatedTile(24);
               this.ei = this.addAnimatedTile(27);
               this.ej = this.addAnimatedTile(28);
               this.ek = this.addAnimatedTile(29);
               this.el = this.addAnimatedTile(30);
            }
         }
      }
   }

   // $VF: renamed from: w (int) void
   private void startArena(int var1) {
      if (var1 >= 0 && var1 <= 11) {
         this.tileset = 1;
         this.enterMainMenu();
         df = false;
         this.messageActive = false;
         menuCursor = 0;
         state = 0;
         worldId = 16;
         this.applyTilesetChange();
         this.loadLevelTiles(16);
         sectionEnemyCount[sectionId] = this.countLiveEnemies();

         for (int var2 = 0; var2 < 10; var2++) {
            this.crateBits[var2] = -1;
         }

         for (int var3 = 0; var3 < 6; var3++) {
            this.sectionFirstVisit[var3] = false;
         }

         for (int var4 = 0; var4 < 2; var4++) {
            this.enemyBits[var4] = -1;
         }

         this.buildSection(var1, true);
         playerX = spawnCol * 57 + 22 << 8;
         playerRow = footRow = spawnRow;
         playerAnim = 0;
         this.startAnim(bt + 1, 1028, 0);
         playerY = playerRow * 38 << 8;
         health = 20;
         hudDirty = true;
         facingRight = true;
         cameraX = spawnCameraX;
         cameraY = spawnCameraY;

         for (int var5 = 0; var5 < 8; var5++) {
            ammo[var5] = maxAmmoTable[var5 * 3 + weaponLevel[var5]];
         }
      }
   }

   // $VF: renamed from: E () void
   private void checkArenaGoal() {
      if (this.arenaNoDamage && health != 20) {
         health = 0;
      } else {
         if (this.arenaHealthDrain && this.arenaDrainTimer++ > 80) {
            health--;
            this.arenaDrainTimer = 0;
            hudDirty = true;
         }

         if (this.arenaLimitedAmmo && weaponsOwned != 1 && ammo[currentWeapon] <= 0) {
            state = 19;
         } else {
            for (int var1 = maxEnemies - 1; var1 >= 0; var1--) {
               if (this.enemies[var1].health > 0 && this.enemies[var1].type != 4 && this.enemies[var1].type != -1) {
                  return;
               }
            }
         }

         this.arenaHealthDrain = false;
         this.arenaNoDamage = false;
         this.arenaDrainTimer = 0;

         for (int var2 = 0; var2 < 10; var2++) {
            this.crateBits[var2] = -1;
         }

         for (int var3 = 0; var3 < 2; var3++) {
            this.enemyBits[var3] = -1;
         }

         menuCursor = 0;
         this.inArena = false;
         weaponsOwned = this.levelStartWeapons;
         if (state != 19) {
            state = 15;
            bolts = bolts + arenaReward[sectionId];
            if (bolts > 999999) {
               bolts = 999999;
            }

            if (sectionId < 11) {
               unlockFlags = unlockFlags | 1 << 21 + sectionId;
               return;
            }

            weaponsOwned |= 64;
         }
      }
   }

   // $VF: renamed from: x (int) void
   private void switchSection(int var1) {
      switch (var1) {
         case 1:
            this.cF = 6;
            targetSection = sectionExits[sectionId * 4 + 0];
            break;
         case 2:
            this.cF = 5;
            targetSection = sectionExits[sectionId * 4 + 3];
         case 3:
         case 4:
         default:
            break;
         case 5:
            this.cF = 2;
            targetSection = sectionExits[sectionId * 4 + 2];
            break;
         case 6:
            this.cF = 1;
            targetSection = sectionExits[sectionId * 4 + 1];
      }

      if (targetSection != -1) {
         sectionId = targetSection;
         sectionEnemyCount[sectionId] = this.countLiveEnemies();
         this.buildSection(sectionId, false);
         switch (this.cF) {
            case 1:
               footRow = 1;
               playerRow = 1;
               cameraY = 0;
               break;
            case 2:
               playerX = 20224;
               cameraX = 0;
            case 3:
            case 4:
            default:
               break;
            case 5:
               playerX = 399616;
               cameraX = cameraMinX;
               break;
            case 6:
               footRow = 16;
               playerRow = footRow;
               cameraY = cameraMinY;
         }

         playerY = playerRow * 38 << 8;
      }
   }

   // $VF: renamed from: c (int, int, int) void
   private void startSwingshot(int var1, int var2, int var3) {
      swingshotTargetX = var1 * 57 + 28 + (facingRight ? -5 : 5) << 8;
      swingshotTargetY = var2 * 38 + 19 - 2 << 8;
      byte var4 = 5;
      if (!facingRight) {
         var4 = -5;
      }

      swingshotX = (playerX >> 8) + var4 << 8;
      swingshotY = playerY + 2048;
      swingshotStepX = swingshotTargetX - swingshotX >> 3;
      swingshotStepY = swingshotTargetY - swingshotY >> 3;
      if (swingshotTargetY < swingshotY) {
         swingshotTile = var3;
         swingshotState = 0;
         this.setPlayerAnim(5);
      }
   }

   // $VF: renamed from: f (int, int) int
   public static int getFloorY(int var0, int var1) {
      int var2 = 684;
      int var3 = var1 / 38;
      int var4;
      if ((var4 = var0 / 57) < 28 && var4 >= 0 && var3 < 18 && var3 >= 0) {
         if ((solidColumnMask[var4] & 1 << var3 + 1) > 0) {
            var2 = (var3 + 1) * 38;
         }

         return var2;
      } else {
         return 684;
      }
   }

   // $VF: renamed from: F () int
   private static int platformFloorY() {
      int var0 = 684;
      int var1 = 0;
      if (fC != 0) {
         return 684;
      }

      for (int var2 = 3; var2 >= 0; var2--) {
         if (platformDir[var2] != -1
            && platformOriginX[var2] + platformOffsetX[var2] <= (playerX >> 8) + 8
            && platformOriginX[var2] + platformOffsetX[var2] + 57 >= (playerX >> 8) - 8
            && (var1 = platformOriginY[var2] + platformOffsetY[var2]) < var0
            && var1 > playerPixelY()) {
            var0 = var1;
            platformDrift = 0;
            if (platformDir[var2] == 0) {
               platformDrift = 2;
            } else if (platformDir[var2] == 2) {
               platformDrift = -2;
            }
         }
      }

      return var0 - 38;
   }

   // $VF: renamed from: G () int
   private static int crateFloorY() {
      short var0 = 684;
      boolean var1 = false;
      int var2 = playerPixelY();
      int var3 = playerX >> 8;
      int var4 = crateW;

      for (int var5 = 49; var5 >= 0; var5--) {
         if (crateType[var5] != -1 && crateY[var5] >= var2 && crateX[var5] <= var3 + 8 && crateX[var5] + var4 >= var3 - 8 && crateY[var5] < var0) {
            var0 = crateY[var5];
            var1 = true;
         }
      }

      return var1 ? var0 - 38 + -12 : var0 - 38;
   }

   // $VF: renamed from: H () boolean
   private static boolean hitsCrate() {
      int var0 = crateW;
      int var1 = crateH;
      int var2 = playerX >> 8;
      int var3 = playerPixelY();

      for (int var4 = 49; var4 >= 0; var4--) {
         if (crateType[var4] >= 0
            && Math.abs(crateX[var4] - var2) <= 57
            && Math.abs(crateY[var4] - var3) <= 38
            && rectsOverlap(crateX[var4], crateY[var4], var0, var1, var2 - 8, var3, 16, 37)) {
            return true;
         }
      }

      return false;
   }

   // $VF: renamed from: I () boolean
   private boolean meleeTargetAhead() {
      int var1 = crateW;
      int var2 = playerX >> 8;
      int var3 = playerPixelY();
      int var4 = 0;

      for (int var5 = 49; var5 >= 0; var5--) {
         if (crateType[var5] >= 0
            && ((var4 = crateX[var5] + (var1 >> 1)) <= var2 || facingRight)
            && (var4 >= var2 || !facingRight)
            && Math.abs(var4 - var2) <= 57
            && crateY[var5] + -12 == var3) {
            return true;
         }
      }

      for (int var8 = maxEnemies - 1; var8 >= 0; var8--) {
         int var6 = this.enemies[var8].type;
         if (this.enemies[var8].type != -1
            && var6 != 4
            && (this.enemies[var8].worldX <= playerX || facingRight)
            && (this.enemies[var8].worldX >= playerX || !facingRight)
            && Math.abs(this.enemies[var8].worldX - playerX) <= 14592
            && this.enemies[var8].state != 5
            && playerRow == this.enemies[var8].row) {
            return true;
         }
      }

      return false;
   }

   // $VF: renamed from: y (int) boolean
   private boolean touchesEnemy(int var1) {
      int var9 = playerPixelX();
      int var10 = playerPixelY() + -12;
      int var11 = hitboxX;
      int var12 = hitboxY;
      int var13 = hitboxW;
      int var14 = hitboxH;

      for (int var15 = var1 < 0 ? maxEnemies - 1 : var1; var15 >= (var1 < 0 ? 0 : var1); var15--) {
         int var2 = this.enemies[var15].type;
         if (this.enemies[var15].type != -1
            && var2 != 4
            && Math.abs(this.enemies[var15].worldX - playerX) <= 14592
            && Math.abs(this.enemies[var15].row - playerRow) <= 3
            && this.enemies[var15].state != 5) {
            int var3 = this.enemies[var15].pixelX();
            int var4 = this.enemies[var15].feetY();
            byte var5 = Enemy.bodyOffsetX[var2];
            byte var6 = Enemy.bodyOffsetY[var2];
            byte var7 = Enemy.bodyWidth[var2];
            byte var8 = Enemy.bodyHeight[var2];
            if (rectsOverlap(var3 - var5, var4 + var6, var7, var8, var9 - var11, var10 + var12, var13, var14)) {
               return true;
            }
         }
      }

      return false;
   }

   // $VF: renamed from: d (int, int, int) void
   private static void moveCursor(int var0, int var1, int var2) {
      if (var0 == 1) {
         menuCursor = cursorPrev(menuCursor, var1, var2);
      } else {
         if (var0 == 6) {
            menuCursor = cursorNext(menuCursor, var2, var1);
         }
      }
   }

   // $VF: renamed from: z (int) void
   private void handleGameplayInput(int var1) {
      if (this.messageActive) {
         if (var1 == 8) {
            if (messageCursor >= 0) {
               messageCursor = messageNext;
            }

            if (messageCursor < 0) {
               if (cU < messageId) {
                  cU = messageId;
               } else {
                  cV = true;
               }

               if (ad) {
                  this.exitToMap(ae);
               }

               this.closeMessage();
               messageNext = 0;
               return;
            }
         } else if (upPressed) {
            if (ad) {
               this.closeMessage();
               return;
            }

            if (messageId > messageFirst) {
               messageId--;
               messageCount++;
               if (messageId < cU) {
                  cV = false;
               }

               messageNext = 0;
               messageCursor = 0;
               this.messageText = this.getString(messageId);
            }
         }
      } else if (state != 16 && state != 17 && state != 24) {
         if (this.isKeyPressed((byte)27)
            || this.isKeyPressed((byte)4)
            || this.isKeyPressed((byte)29)
            || this.isKeyPressed((byte)26)
            || this.isKeyPressed((byte)15)) {
            menuCursor = 0;
            prevState = state;
            state = 4;
         } else if (playerAnim == 10) {
            lastIntent = 0;
            inputIntent = 0;
            fireCooldown &= -129;
         } else {
            if (upPressed) {
               if (playerAnim == 11) {
                  return;
               }

               byte var2 = 0;
               if (swingshotState == -1) {
                  this.trySwingshot();
               } else if (airState >= 0) {
                  if (this.atLeftEdge() && !facingRight) {
                     if (!ab) {
                        ab = true;
                        af = playerPixelX() - 8 + 28;
                     }

                     var2 = -1;
                     velX = 3200;
                     airState = 0;
                     facingRight = true;
                     this.setPlayerAnim(4);
                     inputIntent = 0;
                     lastIntent = 0;
                     this.animPlaying = false;
                  } else if (this.atRightEdge() && facingRight) {
                     if (!ab) {
                        ab = true;
                        af = playerPixelX() + 8 - 28;
                     }

                     var2 = 1;
                     velX = -3200;
                     airState = 0;
                     facingRight = false;
                     this.setPlayerAnim(4);
                     inputIntent = 0;
                     lastIntent = 0;
                     this.animPlaying = false;
                  }
               }

               if (airState < 1 || var2 != 0) {
                  inputIntent = var1;
               }
            } else if (downPressed) {
               if (playerAnim == 5) {
                  return;
               }

               if (airState == 2) {
                  this.setPlayerAnim(3);
                  velX = 0;
                  velY = 0;
                  airState = 1;
                  if ((weaponsOwned & 1) > 0) {
                     lastIntent = 0;
                     this.setPlayerAnim(14);
                     attackFrame = 1;
                  }
               } else if ((weaponsOwned & 1) > 0 && airState != -1 && playerAnim != 11 && playerAnim != 14) {
                  lastIntent = 0;
                  this.setPlayerAnim(14);
                  attackFrame = 1;
               } else if (ad) {
                  invulnFrames = 0;
                  this.showMessage(358);
               } else if (onMovingPlatform) {
                  onMovingPlatform = false;
                  playerRow++;
                  this.startFall();
                  fC = 12;
                  if ((weaponsOwned & 1) > 0 && playerAnim != 14) {
                     lastIntent = 0;
                     this.setPlayerAnim(14);
                     attackFrame = 1;
                  }
               } else if (this.onOneWayTile() && velY == 0 && playerAnim != 14 && playerAnim != 11) {
                  this.startFall();
                  playerRow++;
                  playerY = playerRow * 38 + 2 << 8;
                  if ((weaponsOwned & 1) > 0) {
                     lastIntent = 0;
                     this.setPlayerAnim(14);
                     attackFrame = 1;
                  }
               }
            } else if (leftHeld) {
               inputIntent = 2;
               lastIntent = 2;
               if (ab && playerPixelX() < af + 28 && velX > 0) {
                  inputIntent = 0;
                  lastIntent = 0;
               }
            } else if (rightHeld) {
               inputIntent = 5;
               lastIntent = 5;
               if (ab && playerPixelX() > af - 28 && velX < 0) {
                  inputIntent = 0;
                  lastIntent = 0;
               }
            }

            if (fireHeld) {
               int var6 = S;
               inputIntent = 8;
               int var3 = (playerPixelY() + 44) / 38;
               int var4;
               int var5 = var4 = getRawTile(var6, var3 - 1);
               var4 -= 60;
               var4 = 1 << var4;
               if ((weaponsOwned & 1) > 0
                  && playerAnim != 11
                  && playerAnim != 14
                  && playerAnim != 8
                  && (this.meleeTargetAhead() || ammo[currentWeapon] <= 0 || (doorBits & var4) != 0 && var5 >= 60 && var5 <= 66)) {
                  lastIntent = 0;
                  attackFrame = 0;
                  this.meleeAttack();
                  return;
               }

               if (ammo[currentWeapon] > 0 && fireCooldown == 0 && playerAnim != 8) {
                  fireCooldown = 129;
                  if (this.fireWeapon()) {
                     shotsFired++;
                     return;
                  }
               }
            } else if (this.isKeyPressed((byte)10)) {
               lastIntent = 0;
               if ((weaponsOwned & 1) > 0) {
                  attackFrame = 0;
                  this.meleeAttack();
                  return;
               }
            } else if (this.isKeyPressed((byte)25)) {
               if (cheatMode) {
                  health = 20;
                  addAmmo(1);
                  addAmmo(1);
                  addAmmo(1);
                  bolts += 5000;
                  hudDirty = true;
                  return;
               }
            } else if (!this.isKeyPressed((byte)48)) {
               if (var1 == 55) {
                  return;
               }

               if (this.isKeyPressed((byte)3)) {
                  menuCursor = 0;
                  state = 22;
                  return;
               }

               if (leftHeld && upPressed) {
                  if (!ab && playerAnim != 11 && swingshotState < 0) {
                     if (playerAnim != 12) {
                        this.setPlayerAnim(0);
                     }

                     if (airState < 0) {
                        velY = 4864;
                        airState++;
                     } else if (airState < 1) {
                        velY = 4096;
                        airState++;
                        this.setPlayerAnim(13);
                     }

                     inputIntent = 2;
                     return;
                  }

                  return;
               }

               if (rightHeld && upPressed) {
                  if (ab || playerAnim == 11 || swingshotState >= 0) {
                     return;
                  }

                  if (playerAnim != 12) {
                     this.setPlayerAnim(0);
                  }

                  if (airState < 0) {
                     velY = 4864;
                     airState++;
                  } else if (airState < 1) {
                     velY = 4096;
                     airState++;
                     this.setPlayerAnim(13);
                  }

                  inputIntent = 5;
               }
            }
         }
      }
   }

   // $VF: renamed from: J () void
   private void endInputFrame() {
      this.translateReleaseKeys();
      if (!fireHeld) {
         fireCooldown &= -129;
         if (playerAnim != 8 && playerAnim == 7) {
            this.endAttackAnim();
         }
      }

      if (!leftHeld && !rightHeld) {
         lastIntent = 0;
         inputIntent = 0;
      }
   }

   // $VF: renamed from: A (int) void
   private void enemyShotHitsCrates(int var1) {
      int var2 = crateW;
      short var3 = crateSprites[0].height;
      int var4 = this.enemyShots[var1].type;
      if (this.enemyShots[var1].type != -1 && var4 != 30 && (var4 < 15 || var4 > 17)) {
         int var5 = (this.enemyShots[var1].worldX >> 8) - this.enemyShots[var1].halfW;
         int var6 = (this.enemyShots[var1].worldY >> 8) - this.enemyShots[var1].halfH;
         int var7 = this.enemyShots[var1].halfW << 1;
         int var8 = this.enemyShots[var1].halfH << 1;

         for (int var9 = 49; var9 >= 0; var9--) {
            if (crateType[var9] != -1
               && crateX[var9] + cameraX <= 240
               && crateX[var9] + var2 + cameraX >= 0
               && crateY[var9] + cameraY <= 320
               && crateY[var9] + var3 + cameraY >= 0
               && rectsOverlap(crateX[var9], crateY[var9] + this.crateFallOffset[var9], var2, var3, var5, var6, var7, var8)) {
               this.enemyShots[var1].detonate(true);
            }
         }
      }
   }

   // $VF: renamed from: B (int) void
   private void playerShotHitsCrates(int var1) {
      int var2 = crateW;
      short var3 = crateSprites[0].height;
      int var4 = crateSprites[0].height >> 1;
      if (var1 == -1) {
         for (int var11 = 49; var11 >= 0; var11--) {
            if (crateType[var11] != -1
               && Math.abs(crateX[var11] - (playerX >> 8)) <= 85
               && Math.abs(crateY[var11] - playerPixelY()) <= 76
               && rectsOverlap(
                  crateX[var11],
                  crateY[var11] + this.crateFallOffset[var11],
                  var2,
                  var3,
                  (playerX >> 8) + (facingRight ? meleeBoxX[attackFrame] : -meleeBoxX[attackFrame] - meleeBoxW[attackFrame]),
                  playerPixelY() + meleeBoxY[attackFrame],
                  meleeBoxW[attackFrame],
                  meleeBoxH[attackFrame]
               )) {
               var3 = crateSprites[0].height;
               this.audio.play(0);
               this.removeCrate(var11);
               shotsHit++;
               if (crateType[var11] == 0) {
                  spawnPickup(crateX[var11] + (var2 >> 1), crateY[var11] + this.crateFallOffset[var11], 0);
                  spawnPickup(crateX[var11] + (var2 >> 1), crateY[var11] + this.crateFallOffset[var11], 0);
               } else {
                  spawnPickup(crateX[var11] + (var2 >> 1), crateY[var11] + this.crateFallOffset[var11], crateType[var11]);
               }

               this.spawnPlayerProjectile(crateX[var11] + (var2 >> 1) << 8, crateY[var11] + var4 + this.crateFallOffset[var11] - (var3 >> 1) << 8, 30, true);
               crateType[var11] = -1;
            }
         }
      } else {
         int var5 = this.playerShots[var1].type;
         if (this.playerShots[var1].type != -1 && var5 != 30 && (var5 < 15 || var5 > 17)) {
            int var6 = (this.playerShots[var1].worldX >> 8) - this.playerShots[var1].halfW;
            int var7 = (this.playerShots[var1].worldY >> 8) - this.playerShots[var1].halfH - -12;
            int var8 = this.playerShots[var1].halfW << 1;
            int var9 = this.playerShots[var1].halfH << 1;

            for (int var10 = 49; var10 >= 0; var10--) {
               if (crateType[var10] != -1
                  && crateX[var10] + cameraX <= 240
                  && crateX[var10] + var2 + cameraX >= 0
                  && crateY[var10] + cameraY <= 320
                  && crateY[var10] + var3 + cameraY >= 0
                  && rectsOverlap(crateX[var10], crateY[var10] + this.crateFallOffset[var10], var2, var3, var6, var7, var8, var9)) {
                  this.audio.play(0);
                  this.removeCrate(var10);
                  if (crateType[var10] == 0) {
                     spawnPickup(crateX[var10] + (var2 >> 1), crateY[var10] + this.crateFallOffset[var10], 0);
                     spawnPickup(crateX[var10] + (var2 >> 1), crateY[var10] + this.crateFallOffset[var10], 0);
                  } else {
                     spawnPickup(crateX[var10] + (var2 >> 1), crateY[var10] + this.crateFallOffset[var10], crateType[var10]);
                  }

                  shotsHit++;
                  this.spawnPlayerProjectile(crateX[var10] + (var2 >> 1) << 8, crateY[var10] + var4 + this.crateFallOffset[var10] - (var3 >> 2) << 8, 30, true);
                  crateType[var10] = -1;
                  this.playerShots[var1].detonate(false);
               }
            }
         }
      }
   }

   // $VF: renamed from: K () void
   private void updatePickupsFall() {
      int var1 = this.ak;
      int var2 = bl >> 1;

      for (int var3 = 11; var3 >= 0; var3--) {
         if (pickupKind[var3] != -1) {
            pickupY[var3] = pickupY[var3] + 1024;
            int var4 = getFloorY((pickupX[var3] >> 8) + var2, pickupY[var3] >> 8);
            if ((pickupY[var3] >> 8) + var1 >= var4) {
               pickupY[var3] = var4 - var1 << 8;
            }
         }
      }
   }

   // $VF: renamed from: L () void
   private void checkCollectiblePickup() {
      if (collectibleCol != -1) {
         int var1 = this.ak;
         int var2 = bl;
         int var3 = collectibleCol * 57;
         int var4 = collectibleRow * 38;
         if (rectsOverlap(playerPixelX() - hitboxX, playerPixelY() + -12 + hitboxY, hitboxW, hitboxH, var3, var4, var1, var2)) {
            collectibleRow = -1;
            collectibleCol = -1;
            collectibleMask = collectibleMask & ~(1 << worldId);
            if (collectedCount() >= ck && (weaponsOwned & 128) == 0) {
               this.messageHasPortrait = false;
               this.showMessage(77);
               weaponsOwned |= 128;
            } else if (collectedCount() == 1) {
               this.messageHasPortrait = false;
               this.showMessage(79);
            } else {
               this.messageHasPortrait = false;
               this.showMessage(255);
            }

            collectiblesFound++;
         }
      }
   }

   // $VF: renamed from: M () void
   private void updateFallingCrates() {
      int var3 = crateW;
      short var4 = crateSprites[0].height;
      byte var5 = 8;
      int var6 = playerX >> 8;
      int var7 = playerPixelY();
      byte var8 = 16;

      for (int var1 = 49; var1 >= 0; var1--) {
         if (this.crateFalling[var1] && crateType[var1] != -1) {
            if (this.crateBelow[var1] == -1 || !this.crateFalling[this.crateBelow[var1]]) {
               if (rectsOverlap(crateX[var1], crateY[var1] + this.crateFallOffset[var1], var3, var4, var6 - var5, var7, var8, 44)) {
                  if (crateType[var1] == 0) {
                     spawnPickup(crateX[var1] + (var3 >> 1), crateY[var1] + this.crateFallOffset[var1], 0);
                     spawnPickup(crateX[var1] + (var3 >> 1), crateY[var1] + this.crateFallOffset[var1], 0);
                  } else {
                     spawnPickup(crateX[var1] + (var3 >> 1), crateY[var1] + this.crateFallOffset[var1], crateType[var1]);
                  }

                  this.removeCrate(var1);
                  this.spawnPlayerProjectile(crateX[var1] + (var3 >> 1) << 8, crateY[var1] + (var4 >> 1) + this.crateFallOffset[var1] << 8, 30, true);
                  var6++;
                  crateType[var1] = -1;
                  var6--;
                  continue;
               }

               var5 = 12;
               var8 = 24;

               for (int var2 = maxEnemies - 1; var2 >= 0; var2--) {
                  if (this.enemies[var2].type >= 0) {
                     var6 = this.enemies[var2].worldX >> 8;
                     var7 = this.enemies[var2].feetY();
                     if (var6 + 12 >= crateX[var1]
                        && var6 - 12 <= crateX[var1] + var3
                        && rectsOverlap(crateX[var1], crateY[var1] + this.crateFallOffset[var1], var3, var4, var6 - 12, var7, 24, 44)) {
                        this.removeCrate(var1);
                        this.crateFalling[var1] = false;
                        if (crateType[var1] == 0) {
                           spawnPickup(crateX[var1] + (var3 >> 1), crateY[var1] + this.crateFallOffset[var1], 0);
                           spawnPickup(crateX[var1] + (var3 >> 1), crateY[var1] + this.crateFallOffset[var1], 0);
                        } else {
                           spawnPickup(crateX[var1] + (var3 >> 1), crateY[var1] + this.crateFallOffset[var1], crateType[var1]);
                        }

                        this.spawnPlayerProjectile(crateX[var1] + (var3 >> 1) << 8, crateY[var1] + (var4 >> 1) + this.crateFallOffset[var1] << 8, 30, true);
                        crateType[var1] = -1;
                        break;
                     }
                  }
               }
            }

            this.crateFallOffset[var1] = (short)(this.crateFallOffset[var1] + 4);
            if (crateType[var1] != -1 && this.crateFallOffset[var1] + crateY[var1] >= this.crateRestY[var1]) {
               crateY[var1] = this.crateRestY[var1];
               this.crateFallOffset[var1] = 0;
               this.crateFalling[var1] = false;
            }
         }
      }
   }

   // $VF: renamed from: C (int) void
   private void removeCrate(int var1) {
      short var2 = this.crateAbove[var1];
      short var3 = 0;
      boolean var5 = false;
      int var6 = sectionId;
      if (this.inArena) {
         var6 = 0;
      }

      this.crateBits[var1 + var6 * 50 >> 5] = this.crateBits[var1 + var6 * 50 >> 5] & ~(1 << var1 - (var1 >> 5 << 5));
      if (this.crateFalling[var1] || var2 != -1 && this.crateFalling[var2]) {
         var5 = true;
      }

      for (; var2 != -1; var2 = this.crateAbove[var2]) {
         this.crateFalling[var2] = true;
         if (var5) {
            if (this.crateBelow[var2] == var1) {
               var3 = this.crateRestY[var2];
               this.crateRestY[var2] = this.crateRestY[var1];
            } else {
               short var4 = this.crateRestY[var2];
               this.crateRestY[var2] = var3;
               var3 = var4;
            }
         } else {
            this.crateRestY[var2] = crateY[this.crateBelow[var2]];
         }
      }

      if (this.crateAbove[var1] != -1) {
         this.crateBelow[this.crateAbove[var1]] = this.crateBelow[var1];
      }

      if (this.crateBelow[var1] != -1) {
         this.crateAbove[this.crateBelow[var1]] = this.crateAbove[var1];
      }

      this.crateAbove[var1] = -1;
      this.crateBelow[var1] = -1;
   }

   // $VF: renamed from: g (javax.microedition.lcdui.Graphics) void
   private void drawWorldObjects(Graphics var1) {
      int var2 = bl;
      int var3 = this.ak;
      int var4 = cameraX;
      int var5 = cameraY;

      for (int var8 = 11; var8 >= 0; var8--) {
         if (pickupKind[var8] >= 0) {
            int var6 = (pickupX[var8] >> 8) + var4;
            int var7 = (pickupY[var8] >> 8) + var5 - -12;
            if (var6 <= 240 && var6 + var2 >= 0 && var7 <= 320 && var7 + var3 >= 0) {
               itemSprites[pickupKind[var8]].draw(var1, var6, var7, 0);
            }
         }
      }

      int var28 = crateW;
      short var9 = crateSprites[0].height;

      for (int var10 = 0; var10 < 50; var10++) {
         if (crateType[var10] >= 0) {
            int var18 = crateX[var10] + var4;
            int var23 = crateY[var10] + this.crateFallOffset[var10] + var5;
            if (var18 <= 240 && var18 + var28 >= 0 && var23 <= 320 && var23 + var9 >= 0) {
               crateSprites[crateType[var10]].draw(var1, var18, var23, 0);
            }
         }
      }

      var5 = cameraY + -12;

      for (int var11 = 3; var11 >= 0; var11--) {
         if (ziplineX1[var11] != -1) {
            int var19 = ziplineX1[var11] + var4;
            int var24 = ziplineY1[var11] + var5;
            int var12 = ziplineX2[var11] + var4;
            int var13 = ziplineY2[var11] + var5;
            if ((var19 <= 240 || var12 <= 240) && (var19 >= 0 || var12 >= 0) && (var24 <= 320 || var13 <= 320) && (var24 >= 0 || var13 >= 0)) {
               var1.setColor(15658734);
               var1.drawLine(var19, var24 - 2, var12, var13 - 2);
               var1.setColor(12303291);
               var1.drawLine(var19, var24 - 1, var12, var13 - 1);
               var1.setColor(8947848);
               var1.drawLine(var19, var24, var12, var13);
               var1.setColor(5592405);
               var1.drawLine(var19, var24 + 1, var12, var13 + 1);
               var1.setColor(2236962);
               var1.drawLine(var19, var24 + 2, var12, var13 + 2);
            }
         }
      }

      int var32 = doorSprites[0].height;
      int var34 = doorSprites[0].width;

      for (int var29 = 0; var29 < 3; var29++) {
         byte var36 = 0;
         int var20 = this.doorCol[var29] * 57 + cameraX + (57 - var34 >> 1);
         int var25 = this.doorRow[var29] * 38 + cameraY;
         if (this.doorBit[var29] != 0) {
            if (var20 + var34 >= 0 && var20 <= 240 && var25 + var32 >= 0 && var25 <= 320) {
               if (this.doorBit[var29] >= 2 && this.doorBit[var29] <= 64) {
                  var36 = 1;
               } else if (this.doorBit[var29] == -128) {
                  var36 = 3;
                  if ((doorBits & -128) != 0) {
                     var36 = 2;
                  }
               }

               if (var36 != 2) {
                  doorSprites[4].draw(var1, var20, var25, 0);
               }

               if ((doorBits & this.doorBit[var29]) != 0) {
                  doorSprites[var36].draw(var1, var20, var25, 0);
               }
            }
         } else if ((doorBits & this.doorBit[var29]) != 0) {
            doorSprites[4].draw(var1, var20, var25, 0);
         }
      }

      var32 = this.ak << 1;
      var34 = bl;

      for (int var30 = 2; var30 >= 0; var30--) {
         if (this.switchCol[var30] != -1) {
            int var14 = getRawTile(this.switchCol[var30], this.switchRow[var30]);
            var14 -= 60;
            var14 = 1 << var14;
            int var15 = this.switchCol[var30] * 57;
            int var16 = this.switchRow[var30] * 38;
            int var21 = var15 + cameraX + (57 - var34 >> 1);
            int var26 = var16 + cameraY + 38 - (var32 >> 1);
            if (var21 + var34 >= 0 && var21 <= 240 && var26 + var32 >= 0 && var26 <= 320) {
               if (rectsOverlap(
                  playerPixelX() - (hitboxX << 1), playerPixelY() + -12, hitboxW << 1, hitboxH, var15, var16, itemSprites[5].width, itemSprites[5].height
               )) {
                  this.drawAnim(var1, bk, maxEnemies, var21 + eo, var26 - itemSprites[5].height, 0);
                  this.stepAnim(maxEnemies, deltaTime);
               }

               itemSprites[5 + ((doorBits & var14) == 0 ? 1 : 0)].draw(var1, var21, var26 - -12, 0);
            }
         }
      }

      for (int var31 = 3; var31 >= 0; var31--) {
         if (platformDir[var31] >= 0) {
            int var22 = platformOriginX[var31] + platformOffsetX[var31] + var4;
            int var27 = platformOriginY[var31] + platformOffsetY[var31] + var5;
            if (var22 <= 240 && var22 + 57 >= 0 && var27 <= 320 && var27 + 38 >= 21) {
               platformSprites[this.tileset == 1 ? 2 : 0].draw(var1, var22, var27 - -10, 0);
            }
         }
      }
   }

   // $VF: renamed from: h (javax.microedition.lcdui.Graphics) void
   private void drawEntities(Graphics var1) {
      var1.setClip(0, 21, 240, 299);

      for (int var2 = maxEnemies - 1; var2 >= 0; var2--) {
         if (this.enemies[var2].type != -1) {
            this.enemies[var2].draw(var1, this.enemies[var2].drawFlags, cameraX, cameraY);
         }
      }

      this.drawPlayer(var1, cameraX, cameraY);

      for (int var3 = 9; var3 >= 0; var3--) {
         if (this.enemyShots[var3].type >= 0) {
            this.enemyShots[var3].draw(var1, this);
         }

         if (this.playerShots[var3].type >= 0) {
            this.playerShots[var3].draw(var1, this);
         }
      }
   }

   // $VF: renamed from: N () void
   private void enterMainMenu() {
      this.setScreen(1);
      this.tileset = 3;
      this.loadTileset();
   }

   // $VF: renamed from: O () void
   private void startBonusMode() {
      if (bonusMode) {
         this.enterMainMenu();
         this.gotoTitle();
      } else {
         this.U = -1;
         swingshotState = -2;
         this.dR = 0;
         df = false;
         worldId = 1;
         cq = false;
         this.tileset = 3;
         this.loadLevelTiles(worldId);
         sectionId = startSection;
         mapHighlight = 1;
         targetSection = sectionId;
         this.buildSection(sectionId, true);
         playerX = spawnCol * 57 + 22 << 8;
         playerRow = footRow = spawnRow;
         playerAnim = 0;
         this.startAnim(bt + 1, 1028, 0);
         playerY = playerRow * 38 << 8;
         airState = -1;
         health = 20;
         cameraX = spawnCameraX;
         cameraY = spawnCameraY;
         sectionEnemyCount[sectionId] = this.countLiveEnemies();
         invulnFrames = 10;
         facingRight = true;
         hudDirty = true;
         this.setPlayerAnim(0);
         weaponsOwned &= -33;
         bonusMode = true;
         totalScore = collectedCount() * 1000;
         doorBits = -1;
         sectionSwitchBitsA = -1;
         sectionSwitchBitsB = -1;
         sectionSwitchBitsC = -1;
         cg = -1;
         hintFlagsLow = -1;
         hintFlagsHigh = -1;
         hintFlagsHigh &= -221;
         unlockFlags = 0;
         currentWeapon = 1;
         playerSprites[playerSprites.length - 1] = weaponSprites[currentWeapon];
         state = 0;
         lastIntent = 0;
         inputIntent = 0;
         this.audio.stop();
         this.audio.stop();
      }
   }

   // $VF: renamed from: a (int, int, int, int, int, int, int, int) boolean
   public static boolean rectsOverlap(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      return var0 + var2 > var4 && var4 + var6 > var0 && var1 + var3 > var5 && var5 + var7 > var1;
   }

   // $VF: renamed from: a (int, int, boolean, int) void
   private static void setZiplineEndpoint(int var0, int var1, boolean var2, int var3) {
      int var4 = var0 * 57 + 28;
      int var5 = var1 * 38 + 19;
      if (var2) {
         ziplineX1[var3] = var4;
         ziplineY1[var3] = var5;
      } else {
         ziplineX2[var3] = var4;
         ziplineY2[var3] = var5;
      }

      if (ziplineX1[var3] != -1 && ziplineX2[var3] != -1) {
         ziplineDy[var3] = ziplineY2[var3] - ziplineY1[var3] << 8;
         ziplineDx[var3] = ziplineX2[var3] - ziplineX1[var3];
         ziplineIntercept[var3] = (ziplineY1[var3] << 8) - ziplineDy[var3] * ziplineX1[var3] / ziplineDx[var3];
      }
   }

   // $VF: renamed from: b (int, int, int, int, int) void
   private void placeCrate(int var1, int var2, int var3, int var4, int var5) {
      int var6 = sectionId;
      if (this.inArena) {
         var6 = 0;
      }

      if (this.sectionFirstVisit[var6] && var3 > 0) {
         this.crateBits[var5 + var6 * 50 >> 5] = this.crateBits[var5 + var6 * 50 >> 5] | 1 << var5 - (var5 >> 5 << 5);
      }

      if ((this.crateBits[var5 + var6 * 50 >> 5] & 1 << var5 - (var5 >> 5 << 5)) != 0) {
         crateType[var5] = (byte)var3;
         if (var4 == 2) {
            crateX[var5] = (short)(var1 * 57 + (crateW >> 1));
         } else {
            crateX[var5] = (short)(var1 * 57 + var4 * crateW);
         }

         crateY[var5] = (short)(var2 * 38 - -12);
         this.crateRestY[var5] = crateY[var5];
         this.crateFallOffset[var5] = 0;
      }
   }

   // $VF: renamed from: P () void
   private void settleCrates() {
      boolean var7 = false;

      for (short var2 = 0; var2 < 50; var2++) {
         if (crateType[var2] != -1) {
            var7 = true;
            short var4;
            int var5 = (var4 = crateY[var2]) / 38;
            int var6 = crateX[var2] / 57;

            byte var3;
            while (var7 && var5 + 1 < 18 && ((var3 = this.tiles[var6][var5 + 1]) < 32 || var3 > 58)) {
               for (short var1 = 0; var1 < 50; var1++) {
                  if (crateType[var1] != -1 && var1 != var2 && crateX[var1] == crateX[var2] && crateY[var1] == var4 + 38) {
                     var7 = false;
                     break;
                  }
               }

               if (var7) {
                  crateY[var2] = (short)(crateY[var2] + 38);
                  var4 += 38;
                  var5++;
               }
            }
         }
      }

      for (short var9 = 49; var9 >= 0; var9--) {
         if (crateType[var9] != -1) {
            short var10 = crateY[var9];
            this.crateAbove[var9] = this.crateBelow[var9] = -1;

            for (short var8 = 49; var8 >= 0; var8--) {
               if (crateType[var8] != -1 && crateX[var8] == crateX[var9]) {
                  if (crateY[var8] == var10 - 38) {
                     this.crateAbove[var9] = var8;
                     this.crateBelow[var8] = var9;
                  } else if (crateY[var8] == var10 + 38) {
                     this.crateBelow[var9] = var8;
                     this.crateAbove[var8] = var9;
                  }
               }
            }
         }
      }
   }

   // $VF: renamed from: e (int, int, int) void
   private static void spawnPickup(int var0, int var1, int var2) {
      var0 -= bl >> 1;

      for (int var3 = 11; var3 >= 0; var3--) {
         if (pickupKind[var3] == -1) {
            if (var2 == 0) {
               pickupKind[var3] = (byte)(Math.abs(at.nextInt()) & 1);
            } else if (var2 == 1) {
               pickupKind[var3] = 2;
            } else {
               pickupKind[var3] = 3;
            }

            pickupX[var3] = var0 << 8;
            pickupY[var3] = var1 << 8;
            pickupVelX[var3] = 0;
            pickupVelY[var3] = 768;
            return;
         }
      }
   }

   // $VF: renamed from: a (int, int, int, boolean) void
   public final void spawnPlayerProjectile(int var1, int var2, int var3, boolean var4) {
      for (int var5 = 9; var5 >= 0; var5--) {
         if (this.playerShots[var5].type == -1) {
            if (var3 >= 0 && var3 <= 14 || var3 >= 18 && var3 <= 20) {
               ammo[currentWeapon]--;
               hudDirty = true;
            }

            if (var3 >= 21 && var3 <= 30 && this.playerShots[var5].animSlot < 0) {
               int var6 = at.nextInt();
               var1 += (var6 & 7) << 8;
               var2 += (var6 & 56) >> 3 << 8;

               for (int var7 = 1; var7 < en.length; var7++) {
                  if (en[var7] < 0) {
                     this.playerShots[var5].animSlot = var7;
                     en[var7] = var7;
                     this.startAnim(maxEnemies + var7, 1027, (var6 & 448) >> 7);
                     break;
                  }
               }

               if (var4) {
                  this.spawnPlayerProjectile(var1, var2, 30, false);
                  this.spawnPlayerProjectile(var1, var2, 30, false);
                  this.spawnPlayerProjectile(var1, var2, 30, false);
               }
            }

            if (var3 >= 9 && var3 <= 11 || var3 >= 15 && var3 <= 17) {
               this.playerShots[var5].halfW = 84;
               this.playerShots[var5].halfH = 11;
               var1 += (facingRight ? 1 : -1) * 84 << 8;
            } else {
               this.playerShots[var5].halfW = Projectile.halfWidths[var3];
               this.playerShots[var5].halfH = Projectile.halfHeights[var3];
            }

            this.playerShots[var5].trailX2 = this.playerShots[var5].trailX1 = this.playerShots[var5].worldX = var1;
            this.playerShots[var5].trailY2 = this.playerShots[var5].trailY1 = this.playerShots[var5].worldY = var2;
            this.playerShots[var5].type = var3;
            this.playerShots[var5].velX = (facingRight ? 1 : -1) * Projectile.speeds[var3] << 8;
            this.playerShots[var5].velY = 0;
            this.playerShots[var5].age = 0;
            if (var3 >= 3 && var3 <= 5) {
               this.playerShots[var5].velY = 768;
               return;
            }
            break;
         }
      }
   }

   // $VF: renamed from: a (int, int, int, boolean, boolean, int, int) void
   public final void spawnEnemyProjectile(int var1, int var2, int var3, boolean var4, boolean var5, int var6, int var7) {
      for (int var8 = 9; var8 >= 0; var8--) {
         if (this.enemyShots[var8].type == -1) {
            if (var6 == 0 || var6 == 1) {
               var1 += (var4 ? 12 : -12) << 8;
            }

            this.enemyShots[var8].worldX = var1;
            this.enemyShots[var8].worldY = var2;
            this.enemyShots[var8].type = var3;
            this.enemyShots[var8].age = 0;
            this.enemyShots[var8].sourceEnemy = var7;
            this.enemyShots[var8].halfW = Projectile.halfWidths[var3];
            this.enemyShots[var8].halfH = Projectile.halfHeights[var3];
            if (var6 != 0 && var6 != 1) {
               if (var5) {
                  this.enemyShots[var8].velX = (var6 == 3 ? -1 : 1) * Projectile.speeds[var3] << 8;
                  this.enemyShots[var8].velY = 0;
               } else {
                  this.enemyShots[var8].velX = 0;
                  this.enemyShots[var8].velY = (var4 ? -1 : 1) * Projectile.speeds[var3] << 8;
               }
            } else if (var5) {
               this.enemyShots[var8].velX = 0;
               if (var7 != 17 && var7 != 20 && var7 != 23 && var7 != 27) {
                  this.enemyShots[var8].velY = (var6 == 0 ? -1 : 1) * Projectile.speeds[var3] << 8;
               } else {
                  this.enemyShots[var8].velY = (var6 == 0 ? 1 : -1) * Projectile.speeds[var3] << 8;
               }
            } else {
               this.enemyShots[var8].velX = (var4 ? 1 : -1) * Projectile.speeds[var3] << 8;
               if (var7 == 27) {
                  this.enemyShots[var8].velX = (var4 ? 2 : -2) * Projectile.speeds[var3] << 8;
               }

               this.enemyShots[var8].velY = 0;
            }

            if (var3 >= 21 && var3 <= 30 && this.playerShots[var8].animSlot < 0) {
               int var9 = at.nextInt();
               var1 += (var9 & 7) << 8;
               var2 += (var9 & 56) >> 3 << 8;

               for (int var10 = 1; var10 < en.length; var10++) {
                  if (en[var10] < 0) {
                     this.playerShots[var8].animSlot = var10;
                     en[var10] = var10;
                     this.startAnim(maxEnemies + var10, 1027, (var9 & 448) >> 7);
                     break;
                  }
               }

               this.spawnPlayerProjectile(var1, var2, 30, false);
               this.spawnPlayerProjectile(var1, var2, 30, false);
               this.spawnPlayerProjectile(var1, var2, 30, false);
               return;
            }
            break;
         }
      }
   }

   // $VF: renamed from: a (int, int, int, int) void
   private void spawnEnemy(int var1, int var2, int var3, int var4) {
      if (var4 == -1) {
         var4 = maxEnemies - 1;

         while (var4 >= 0 && this.enemies[var4].type != -1) {
            var4--;
         }

         if (var4 == -1) {
            return;
         }
      } else {
         int var5 = sectionId;
         if (this.inArena) {
            var5 = 0;
         }

         int var6 = var4 + var5 * 10 >> 5;
         if (this.sectionFirstVisit[var5]) {
            this.enemyBits[var6] = this.enemyBits[var6] | 1 << var4 + var5 * 10 - (var6 << 5);
         }

         if ((this.enemyBits[var6] & 1 << var4 + var5 * 10 - (var6 << 5)) == 0) {
            return;
         }
      }

      if (var3 == 28) {
         P = var4;
      }

      if (var3 == 27) {
         Q = var4;
      }

      if (var3 == 26) {
         R = var4;
      }

      if (var3 >= 0 && var3 <= 2) {
         this.enemies[var4].type = 1;
         this.enemies[var4].animSetId = 1024;
         Enemy.playAnim(this.enemies[var4], 1, 1);
      } else if (var3 >= 3 && var3 <= 5) {
         this.enemies[var4].type = 0;
         this.enemies[var4].animSetId = 1025;
         Enemy.playAnim(this.enemies[var4], 0, 1);
      } else if (var3 >= 6 && var3 <= 13) {
         this.enemies[var4].type = 2;
         this.enemies[var4].animSetId = 1029;
         Enemy.playAnim(this.enemies[var4], 2, 1);
      } else if (var3 == 27) {
         this.enemies[var4].type = 5;
         this.enemies[var4].animSetId = 1032;
         Enemy.playAnim(this.enemies[var4], 5, 0);
      } else if (var3 == 28) {
         this.enemies[var4].type = 6;
         this.enemies[var4].animSetId = 1033;
         Enemy.playAnim(this.enemies[var4], 6, 2);
      } else if (var3 == 26) {
         this.enemies[var4].type = 7;
         this.enemies[var4].animSetId = 1031;
         Enemy.playAnim(this.enemies[var4], 7, 0);
      } else {
         this.enemies[var4].type = 3;
      }

      this.enemies[var4].worldX = var1 * 57 + 28 << 8;
      this.enemies[var4].col = var1;
      this.enemies[var4].row = var2;
      this.enemies[var4].velX = this.enemies[var4].velY = 0;
      this.enemies[var4].worldY = var2 * 38 << 8;
      this.enemies[var4].health = Enemy.maxHealth[var3];
      this.enemies[var4].mount = 0;
      this.enemies[var4].facingRight = worldId != 12 || playerX >= this.enemies[var4].worldX;
      this.enemies[var4].meleeHit = false;
      this.enemies[var4].vertical = false;
      this.enemies[var4].aa = 0;
      this.enemies[var4].spawnIndex = var4;
      this.enemies[var4].knockback = 0;
      this.enemies[var4].engaged = 0;
      this.enemies[var4].aa = 0;
      this.enemies[var4].homeX = this.enemies[var4].worldX;
      if (this.enemies[var4].type == 1) {
         this.enemies[var4].homeX = 0;
      }

      if (this.enemies[var4].type == 3) {
         this.enemies[var4].facingRight = false;
         int var7;
         if ((var7 = getRawTile(var1 + 1, var2)) >= 46 && var7 <= 58) {
            this.enemies[var4].mount = 3;
         }

         if ((var7 = getRawTile(var1 - 1, var2)) >= 46 && var7 <= 58) {
            this.enemies[var4].mount = 2;
         }

         if ((var7 = getRawTile(var1, var2 - 1)) >= 46 && var7 <= 58) {
            this.enemies[var4].worldY = this.enemies[var4].row * 38 + 12 << 8;
            this.enemies[var4].mount = 1;
         }
      }

      this.enemies[var4].spawnCode = var3;
      this.enemies[var4].state = 0;
      this.enemies[var4].animPhase = 1;
      this.enemies[var4].drawFlags = 1;
   }

   // $VF: renamed from: a (byte, byte) void
   private void addSwitch(byte var1, byte var2) {
      boolean var3 = false;

      for (int var4 = 0; var4 < 3; var4++) {
         if (this.switchCol[var4] == -1) {
            this.switchCol[var4] = var1;
            this.switchRow[var4] = var2;
            return;
         }
      }
   }

   // $VF: renamed from: a (byte, byte, int) void
   private void addDoor(byte var1, byte var2, int var3) {
      boolean var4 = false;

      for (int var5 = 0; var5 < 3; var5++) {
         if (this.doorBit[var5] == 0) {
            if (var3 == 94) {
               this.doorBit[var5] = 1;
            }

            if (var3 == 95) {
               this.doorBit[var5] = 2;
            }

            if (var3 == 96) {
               this.doorBit[var5] = 4;
            }

            if (var3 == 97) {
               this.doorBit[var5] = 8;
            }

            if (var3 == 98) {
               this.doorBit[var5] = 16;
            }

            if (var3 == 99) {
               this.doorBit[var5] = 32;
            }

            if (var3 == 100) {
               this.doorBit[var5] = 64;
            }

            if (var3 == -95) {
               this.doorBit[var5] = -128;
            }

            this.doorCol[var5] = var1;
            this.doorRow[var5] = (byte)(var2 - 1);
            return;
         }
      }
   }

   // $VF: renamed from: D (int) void
   private void checkEnemyHit(int var1) {
      if (this.enemies[var1].type != -1 && this.enemies[var1].state != 5) {
         int var2 = 0;
         int var3 = this.enemies[var1].pixelX();
         int var4 = this.enemies[var1].feetY();
         int var5 = this.enemies[var1].type;
         byte var6 = Enemy.bodyOffsetX[var5];
         byte var7 = Enemy.bodyOffsetY[var5];
         byte var8 = Enemy.bodyWidth[var5];
         byte var9 = Enemy.bodyHeight[var5];
         if (var3 - var6 + var8 + cameraX >= 0 && var3 - var6 + cameraX <= 240 && var4 + var7 + var9 + cameraY >= 0 && var4 + var7 + cameraY <= 320) {
            for (int var10 = 9; var10 >= 0; var10--) {
               int var11 = this.playerShots[var10].type;
               if (this.playerShots[var10].type >= 0
                  && var11 != 30
                  && (!this.playerShots[var10].detonated || var11 >= 9 && var11 <= 11 || var11 >= 15 && var11 <= 17 || var11 == 2 || var11 >= 21 && var11 <= 29)
                  )
                {
                  int var12 = (this.playerShots[var10].worldX >> 8) - this.playerShots[var10].halfW;
                  int var13 = (this.playerShots[var10].worldY >> 8) - this.playerShots[var10].halfH;
                  if (rectsOverlap(var3 - var6, var4 + var7, var8, var9, var12, var13, this.playerShots[var10].halfW << 1, this.playerShots[var10].halfH << 1)) {
                     var2 = this.playerShots[var10].weaponClass();
                     if (var5 != 4 && var5 != 3 || var2 != 6) {
                        if (var11 >= 9 && var11 <= 11 && this.enemies[var1].type != 6 && this.enemies[var1].type != 7 && this.enemies[var1].type != 5) {
                           int var14;
                           if ((var14 = Math.abs(playerPixelX() - var3)) < 57) {
                              this.enemies[var1].health = this.enemies[var1].health - Projectile.damage[var11];
                           } else if (var14 >= 114 && var11 <= 10) {
                              this.enemies[var1].health = this.enemies[var1].health - (Projectile.damage[var11] >> 2);
                           } else {
                              this.enemies[var1].health = this.enemies[var1].health - (Projectile.damage[var11] >> 1);
                           }
                        } else if (this.enemies[var1].type != 6 && this.enemies[var1].type != 7 && this.enemies[var1].type != 5) {
                           this.enemies[var1].health = this.enemies[var1].health - Projectile.damage[var11];
                           if (this.enemies[var1].type != 6 && this.enemies[var1].type != 7 && this.enemies[var1].type != 5 && this.enemies[var1].type != 3) {
                              Enemy.playAnim(this.enemies[var1], this.enemies[var1].type, 4);
                           }
                        } else if (worldId > 10) {
                           if (L) {
                              this.enemies[R].health = this.enemies[R].health - (Projectile.damage[var11] >> 1);
                              if (this.enemies[var1].type == 7 && this.enemies[var1].health < 0) {
                                 this.enemies[P].health = Enemy.maxHealth[28];
                              }

                              this.enemies[Q].health = Enemy.maxHealth[27];
                           } else if (M) {
                              this.enemies[Q].health = this.enemies[Q].health - (Projectile.damage[var11] >> 1);
                              this.enemies[P].health = Enemy.maxHealth[28];
                           } else if (N) {
                              this.enemies[P].health = this.enemies[P].health - (Projectile.damage[var11] >> 1);
                           }
                        }

                        this.playerShots[var10].detonate(false);
                        if (this.enemies[var1].type == 3 && this.enemies[var1].mount == 1) {
                           var4 -= var9 >> 1;
                        }

                        if (worldId >= 10 || this.enemies[var1].type != 5 && this.enemies[var1].type != 7 && this.enemies[var1].type != 6) {
                           this.spawnPlayerProjectile(var3 << 8, (var4 << 8) + 5632, 30, true);
                        }

                        shotsHit++;
                        if (this.enemies[var1].health <= 0 && this.enemies[var1].state != 5) {
                           int var18 = sectionId;
                           if (this.inArena) {
                              var18 = 0;
                           }

                           int var15 = var1 + var18 * 10 >> 5;
                           this.enemyBits[var15] = this.enemyBits[var15] & ~(1 << var1 + var18 * 10 - (var15 << 5));
                           if (var2 == 6 && var5 != 4) {
                              dH++;

                              for (int var19 = 0; var19 < Enemy.boltDrop[this.enemies[var1].spawnCode]; var19++) {
                                 spawnPickup(this.enemies[var1].worldX >> 8, this.enemies[var1].feetY(), 0);
                              }

                              this.enemies[var1].type = 4;
                              this.enemies[var1].animSetId = 1030;
                              this.enemies[var1].spawnCode = 24;
                              this.enemies[var1].health = Enemy.maxHealth[24];
                              this.enemies[var1].state = 1;
                              this.startAnim(this.enemies[var1].slot, 1030, 1);
                              if (weaponLevel[6] < 2) {
                                 weaponKills[6]++;
                              }

                              if (weaponKills[6] >= 20) {
                                 weaponLevel[6]++;
                                 weaponKills[6] = 0;
                                 this.messageHasPortrait = false;
                                 this.showMessage(78);
                                 killCount++;
                                 dO++;
                                 if (bonusMode && ++this.boltMultiplier > 10) {
                                    this.boltMultiplier = 10;
                                    return;
                                 }
                              }
                           } else {
                              killCount++;
                              dO++;
                              if (bonusMode && ++this.boltMultiplier > 10) {
                                 this.boltMultiplier = 10;
                              }

                              if (this.enemies[var1].type == 7) {
                                 L = false;
                              } else if (this.enemies[var1].type == 6) {
                                 N = false;
                              } else if (this.enemies[var1].type == 5) {
                                 M = false;
                              }

                              this.enemies[var1].state = 5;
                              this.enemies[var1].animPhase = 1;
                              if (var5 != 4) {
                                 for (int var16 = 0; var16 < Enemy.boltDrop[this.enemies[var1].spawnCode]; var16++) {
                                    spawnPickup(this.enemies[var1].worldX >> 8, this.enemies[var1].feetY(), 0);
                                 }

                                 if (weaponLevel[var2] < 2) {
                                    weaponKills[var2]++;
                                 }

                                 if (weaponKills[var2] >= 20) {
                                    weaponLevel[var2]++;
                                    weaponKills[var2] = 0;
                                    this.messageHasPortrait = false;
                                    this.showMessage(78);
                                    return;
                                 }
                              }
                           }
                        } else if (this.enemies[var1].state != 2 && this.enemies[var1].type != 4) {
                           this.enemies[var1].state = 4;
                           this.enemies[var1].hurtTimer = 0;
                        }

                        return;
                     }
                  }
               }
            }
         }
      }
   }

   // $VF: renamed from: Q () void
   private void checkPlayerHit() {
      int var1 = playerPixelX();
      int var2 = playerPixelY() + -12;
      int var3 = hitboxX;
      int var4 = hitboxY;
      int var5 = hitboxW;
      int var6 = hitboxH;

      for (int var7 = 9; var7 >= 0; var7--) {
         if (playerAnim == 10) {
            return;
         }

         int var8 = this.enemyShots[var7].type;
         if (this.enemyShots[var7].type != -1 && var8 != 30 && !this.enemyShots[var7].detonated) {
            int var9 = (this.enemyShots[var7].worldX >> 8) - this.enemyShots[var7].halfW;
            int var10 = (this.enemyShots[var7].worldY >> 8) - this.enemyShots[var7].halfH;
            if (rectsOverlap(var1 - var3, var2 + var4, var5, var6, var9, var10, this.enemyShots[var7].halfW << 1, this.enemyShots[var7].halfH << 1)) {
               int var11 = this.enemyShots[var7].sourceEnemy;
               if (var8 != 3 && var8 != 6 && var8 != 18) {
                  if (!invincible) {
                     if (var11 == 2) {
                        health -= 2;
                     } else {
                        health = health - Enemy.attackDamage[var11];
                     }

                     this.boltMultiplier = 1;
                     tookDamage = true;
                  }

                  hudDirty = true;
                  this.setPlayerAnim(9);
                  hurtTimer = 0;
                  if (airState == 2) {
                     airState = 1;
                  }

                  this.spawnPlayerProjectile(var1 << 8, (var2 << 8) + 6144, 30, true);
               }

               this.enemyShots[var7].detonate(true);
            }
         }
      }
   }

   // $VF: renamed from: i (javax.microedition.lcdui.Graphics) void
   private void drawMessageBox(Graphics var1) {
      this.fontHeight();
      int var4 = portraitSheet.getHeight() / 5;
      int var5 = portraitSheet.getWidth();
      int var3 = var4 + 4;
      if (this.messageHasPortrait && cL[messageId - 117] != -1 && var3 < var4 + 4) {
         var3 = var4 + 4;
      }

      int var2 = 320 - var3;
      this.resetClip(var1);
      if (messageId >= 191 && messageId <= 194) {
         int var6 = (playerX >> 8) - 22 + cameraX;
         int var7 = 228 + cameraY + -12;
         this.drawAnim(var1, playerSprites, bt + 1, var6 + 142, var7, 2);
      }

      var1.setColor(0, 0, 0);
      var1.fillRect(0, var2, 240, var3);
      if (this.messageHasPortrait && cL[messageId - 117] != -1) {
         var2 += var3 - var4 >> 1;
         var1.setColor(9114112);
         int var9 = var2 + var4 - 1;
         var1.drawLine(2 + var5, var2 + 1, 232, var2 + 1);
         var1.drawLine(231, var2 + 1, 234, var2 + 5);
         var1.drawLine(234, var2 + 2, 234, var9 - 9);
         var1.drawLine(234, var9 - 9, 225, var9);
         var1.drawLine(224, var9 - 1, 2 + var5, var9 - 1);
         var1.drawLine(2 + var5, var2, 232, var2);
         var1.drawLine(233, var2 + 1, 233, var9 - 9);
         var1.drawLine(233, var9 - 9, 224, var9);
         var1.drawLine(224, var9, 2 + var5, var9);
         var1.setClip(2, var2, portraitSheet.getWidth(), var4);
         var1.drawImage(portraitSheet, 2, var2 - var4 * cL[messageId - 117], 20);
      }
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics, int, java.lang.String, int, int, int, int) int
   private int drawTextChunk(Graphics var1, int var2, String var3, int var4, int var5, int var6, int var7) {
      int var11 = var2;
      char[] var13 = new char[var3.length()];
      var3.getChars(0, var3.length(), var13, 0);
      int var8 = 0;

      do {
         int var9 = 0;
         int var10 = var11;
         char var12 = 0;

         while (var12 != ' ' && var12 != 166 && var11 < var3.length() && var8 + var9 <= var7) {
            var12 = var3.charAt(var11++);
            var9 += this.charWidth(var12);
         }

         if (var8 + var9 > var7 || var11 >= var3.length() || var12 == 166) {
            if (var3.charAt(var2) == ' ' || var3.charAt(var2) == 166) {
               var2++;
            }

            if (var8 + var9 > var7) {
               if (var10 > var2) {
                  var11 = var10;
               } else {
                  var11--;
               }
            }

            if ((var6 & 1) > 0) {
               if (var11 - var2 > 0) {
                  this.drawString(var1, var13, var2, var11 - var2, centerX, var5, var6);
               }

               return var11;
            } else {
               if (var11 - var2 > 0) {
                  this.drawString(var1, var13, var2, var11 - var2, var4, var5, var6);
               }

               return var11;
            }
         }

         var8 += var9;
      } while (var11 < var3.length());

      return var11;
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics, int) int
   private int drawMessageText(Graphics var1, int var2) {
      byte var4 = 17;
      int var7 = 2;
      if (this.messageHasPortrait && cL[messageId - 117] != -1) {
         var7 = portraitSheet.getWidth() + 4;
         var4 = 20;
      }

      var1.setColor(16777215);
      this.setFont(fontMain);
      this.resetClip(var1);
      int var5 = this.fontHeight();
      int var3 = 320 - (var5 * 3 + 2);
      int var6 = this.messageText.length();
      if ((var2 = this.drawTextChunk(var1, var2, this.messageText, var7, var3, var4, 240 - var7 - 2)) >= var6) {
         return -1;
      }

      int var9;
      return (var9 = this.drawTextChunk(var1, var2, this.messageText, var7, var3 + var5, var4, 240 - var7 - 2)) >= var6 ? -1 : var9;
   }

   // $VF: renamed from: l (int) void
   public final void showMessage(int var1) {
      this.audio.play(1);
      messageCursor = 0;
      if (cV) {
         if (messageFirst <= 0) {
            messageFirst = var1;
         }

         messageId = var1;
         if (var1 == 255) {
            this.messageText = RatchetMIDlet.substitute(this.getString(var1), collectedCount(), "" + ck);
         } else {
            this.messageText = this.getString(var1);
         }

         if (this.messageText.equals("")) {
            messageCursor = -1;
         }
      }

      this.messageActive = true;
   }

   // $VF: renamed from: R () void
   private void closeMessage() {
      this.messageActive = false;
      if (cV) {
         if (messageId == 117) {
            mapHighlight = 524288;
            unlockFlags |= 524288;
         } else if (messageId == 131) {
            mapHighlight = 4;
            unlockFlags |= 4;
            unlockFlags |= 262144;
            unlockFlags |= 1048576;
         } else if (messageId == 135) {
            mapHighlight = 8;
            weaponsOwned |= 32;
            unlockFlags |= 8;
         } else if (messageId == 142) {
            mapHighlight = 16;
            unlockFlags |= 16;
         } else if (messageId == 148) {
            mapHighlight = 8;
            mapHighlight = 128;
            unlockFlags |= 256;
         } else if (messageId == 155) {
            unlockFlags |= 32;
            mapHighlight = 32;
         } else if (messageId == 156) {
            mapHighlight = 64;
            unlockFlags |= 64;
         } else if (messageId == 158) {
            unlockFlags |= 128;
            doorBits &= -129;
            mapHighlight = 8;
         } else if (messageId == 160) {
            unlockFlags |= 512;
            mapHighlight = 256;
         } else if (messageId == 167) {
            unlockFlags |= 1024;
            mapHighlight = 512;
         } else if (messageId == 168) {
            unlockFlags |= 2048;
            mapHighlight = 1024;
         } else if (messageId == 171) {
            mapHighlight = 2048;
            unlockFlags |= 4096;
         } else if (messageId == 173) {
            unlockFlags |= 8192;
            mapHighlight = 4096;
         } else if (messageId == 176) {
            unlockFlags |= 16384;
            mapHighlight = 131072;
            unlockFlags |= 131072;
         } else if (messageId == 184) {
            unlockFlags |= 32768;
            mapHighlight = 16384;
         }

         this.messageHasPortrait = false;
         if (--messageCount > 0) {
            this.messageHasPortrait = true;
            this.showMessage(messageId + 1);
            return;
         }

         messageCount = 0;
         if (messageId == 117 + cJ[0] - 1
            || messageId == 131 + cJ[1] - 1
            || messageId == 135 + cJ[2] - 1
            || messageId == 142 + cJ[5] - 1
            || messageId == 148 + cJ[6] - 1
            || messageId == 155 + cJ[7] - 1
            || messageId == 156 + cJ[8] - 1
            || messageId == 158 + cJ[9] - 1
            || messageId == 160 + cJ[11] - 1
            || messageId == 167 + cJ[12] - 1
            || messageId == 168 + cJ[13] - 1
            || messageId == 171 + cJ[14] - 1
            || messageId == 173 + cJ[15] - 1
            || messageId == 176 + cJ[16] - 1
            || messageId == 184 + cJ[17] - 1) {
            if (messageId == 184 + cJ[17] - 1) {
               RatchetMIDlet.markGameCompleted();
               state = 2;
               aS = this.loadSprites(3072);
            } else {
               computeScore(resultsPage);
               state = 18;
            }
         }

         if (messageId + 1 == cK[32] + cJ[32]) {
            this.setPlayerAnim(1);
         }

         messageFirst = -1;
      }
   }

   // $VF: renamed from: S () void
   private void updateAutofire() {
      int var1;
      if (state == 0 && (var1 = fireCooldown & 63) > 0) {
         fireCooldown++;
         if (var1 >= fireDelay[currentWeapon]) {
            if ((fireCooldown & 128) != 0) {
               fireCooldown = 129;
               if (this.fireWeapon()) {
                  shotsFired++;
               }
            } else {
               fireCooldown = 0;
            }
         }
      }

      if (state == 11 && (mapHighlight & 524288) > 0) {
         mapHighlight = 2;
         unlockFlags |= 2;
         unlockFlags |= 1;
      }

      if (state != 0 && this.dw++ > 12) {
         this.dw = 0;
      }
   }

   // $VF: renamed from: c (javax.microedition.lcdui.Graphics, int, int) void
   private void drawSoftKeys(Graphics var1, int var2, int var3) {
      if (var2 != -1) {
         this.drawSoftKey(var1, var2, true);
      }

      if (var3 != -1) {
         this.drawSoftKey(var1, var3, false);
      }
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics, int, boolean) void
   private void drawSoftKey(Graphics var1, int var2, boolean var3) {
      int var4 = this.fontHeight();
      String var5;
      char[] var6 = new char[(var5 = this.getString(var2)).length()];
      var5.getChars(0, var6.length, var6, 0);
      int var7;
      int var8 = var7 = this.charWidth('.');

      int var9;
      for (var9 = 0; var9 < var6.length; var9++) {
         int var10 = this.charWidth(var6[var9]);
         if (var8 + var10 > 125) {
            break;
         }

         var8 += var10;
      }

      if (var9 < var6.length) {
         var6[var9++] = '.';
      } else {
         var8 -= var7;
      }

      int var12 = var3 ? 2 : 240 - var8 - 1;
      int var11 = 320 - var4 - var4 / 2;
      var11 += 2;
      this.setFont(fontHighlight);
      this.drawString(var1, var6, 0, var9, var12, var11, 20);
   }

   private int a(Graphics var1, String var2, int var3, boolean var4, int var5) {
      var1.setColor(var4 ? 8388608 : var5);
      if (var4) {
         this.setFont(fontSelected);
         this.ew = var2;
         var2 = "";
      } else if (var5 == 0) {
         this.setFont(fontSelected);
      } else if (var5 == 14614528 || var5 == 10027008) {
         this.setFont(fontHighlight);
      } else if (var5 == 16777215) {
         this.setFont(fontMain);
      }

      return this.b(var1, var2, var3, 17);
   }

   private int a(Graphics var1, String var2, int var3, boolean var4) {
      var1.setColor(16777215);
      this.setFont(fontMain);
      if (var4) {
         this.ew = var2;
         this.setFont(fontSelected);
      }

      return this.b(var1, var2, var3, 17);
   }

   private int b(Graphics var1, String var2, int var3, boolean var4, int var5) {
      var1.setColor(var5);
      if (var5 == 16777215) {
         this.setFont(fontMain);
      } else if (var5 == 14614528) {
         this.setFont(fontHighlight);
      } else {
         this.setFont(fontSelected);
      }

      if (var4) {
         this.ew = var2;
         var2 = "";
      }

      return this.a(var1, var2, var3, 17);
   }

   private int c(Graphics var1, String var2, int var3, boolean var4, int var5) {
      var1.setColor(var4 ? var5 : 14614528);
      if (var4) {
         if (var5 == 16777215) {
            this.setFont(fontMain);
         } else {
            this.setFont(fontSelected);
         }

         this.ew = var2;
         var2 = "";
      } else if (var5 == 14614528) {
         this.setFont(fontHighlight);
      } else {
         this.setFont(fontMain);
      }

      return this.b(var1, var2, var3, 17);
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics, int, int, int, boolean) int
   private int drawSaveSlot(Graphics var1, int var2, int var3, int var4, boolean var5) {
      int var6 = 0;
      if (RatchetMIDlet.slotInUse[var2] == 0) {
         var6 = this.c(var1, String.valueOf(var2 + 1) + this.getString(36), var4, var5, 16777215);
      } else {
         String var7 = RatchetMIDlet.substitute(this.getString(37), var2 + 1, formatTime(RatchetMIDlet.slotPlayTime[var2]));
         var6 = this.a(var1, var7, var4, var5, 16777215);
      }

      return var6;
   }

   private int a(Graphics var1, int var2, int var3, int var4, int var5, boolean var6) {
      String var8 = RatchetMIDlet.substitute(this.getString(var2), var3, null);
      return this.a(var1, var8, var5, var6, 16777215);
   }

   // $VF: renamed from: b (javax.microedition.lcdui.Graphics, int) int
   private int drawHeading(Graphics var1, int var2) {
      var1.setColor(14614528);
      this.setFont(fontHighlight);
      String var10002 = this.getString(var2);
      this.getWidth();
      return this.a(var1, var10002, 5, 17);
   }

   // $VF: renamed from: d (javax.microedition.lcdui.Graphics, int, int) void
   private void drawPageIndicator(Graphics var1, int var2, int var3) {
      int var4 = lineHeight;
      String var5 = RatchetMIDlet.substitute(this.getString(41), var2, String.valueOf(var3));
      int var6 = 320 - (var4 + (var4 >> 1)) + 3;
      this.getWidth();
      this.a(var1, var5, var6, false, 14614528);
   }

   // $VF: renamed from: E (int) java.lang.String
   private static String formatTime(int var0) {
      int var1;
      int var2;
      int var3 = (var2 = (var1 = var0 / 1000) / 60) / 60;
      var1 %= 60;
      var2 %= 60;
      return String.valueOf(var3) + (var2 < 10 ? ":0" : ":") + var2 + (var1 < 10 ? ":0" : ":") + var1;
   }

   // $VF: renamed from: f (int, int, int) int
   private static int cursorPrev(int var0, int var1, int var2) {
      return var0 > var1 ? var0 - 1 : var2;
   }

   // $VF: renamed from: g (int, int, int) int
   private static int cursorNext(int var0, int var1, int var2) {
      return var0 < var1 ? var0 + 1 : var2;
   }

   // $VF: renamed from: b (javax.microedition.lcdui.Graphics, java.lang.String, int, int, int) void
   private void drawTicker(Graphics var1, String var2, int var3, int var4, int var5) {
      int var6 = 0;
      var1.getFont().getHeight();
      var6 = this.stringWidth(var2);
      var1.setClip(var4, 0, var5 - var4, 320);
      this.drawString(var1, var2, this.T, var3, 20);
      this.T -= 2;
      if (this.T + var6 <= var4) {
         this.T = var5;
      }
   }

   // $VF: renamed from: j () void
   public static void focusMapNode() {
      menuCursor = 0;

      for (int var0 = 0; var0 < 19; var0++) {
         if ((mapHighlight & 1 << var0) > 0) {
            return;
         }

         menuCursor++;
      }
   }

   // $VF: renamed from: T () void
   private static void initArenaMenu() {
      menuCursor = 0;
      dx = 0;
      if ((weaponsOwned & 64) <= 0) {
         for (int var0 = 0; var0 < 11; var0++) {
            if ((unlockFlags & 1 << 21 + var0) != 0) {
               menuCursor++;
            }
         }

         if (menuCursor >= 8) {
            dx = 2;
         } else if (menuCursor >= 4) {
            dx = 1;
         } else {
            dx = 0;
         }
      }
   }

   private int a(Graphics var1, String var2, int var3, int var4) {
      int var5 = (var4 & 1) > 0 ? 80 : 42;
      return this.a(var1, var2, var5 >> 1, var3, var4, 240 - (var5 >> 1));
   }

   private int b(Graphics var1, String var2, int var3, int var4) {
      return this.a(var1, var2, 0, var3, var4, 240);
   }

   // $VF: renamed from: U () int
   private static int computeRefillPrices() {
      int var1 = 0;

      for (int var2 = 0; var2 < 8; var2++) {
         int var0 = maxAmmoTable[var2 * 3 + weaponLevel[var2]] - ammo[var2];
         switch (var2) {
            case 0:
               refillPrices[var2] = 0;
               break;
            case 1:
               var1 += var0 >> 0;
               refillPrices[var2] = var0 >> 0;
               break;
            case 2:
               if ((weaponsOwned & 1 << var2) > 0) {
                  var1 += var0 >> 0;
                  refillPrices[var2] = var0 >> 0;
               }
               break;
            case 3:
               if ((weaponsOwned & 1 << var2) > 0) {
                  var1 += var0 * 3;
                  refillPrices[var2] = var0 * 3;
               }
               break;
            case 4:
               if ((weaponsOwned & 1 << var2) > 0) {
                  var1 += var0 * 3;
                  refillPrices[var2] = var0 * 3;
               }
               break;
            case 5:
               if ((weaponsOwned & 1 << var2) > 0) {
                  var1 += var0 * 2;
                  refillPrices[var2] = var0 * 2;
               }
               break;
            case 6:
               refillPrices[var2] = 0;
               break;
            case 7:
               if ((weaponsOwned & 1 << var2) > 0) {
                  var1 += var0 * 5;
                  refillPrices[var2] = var0 * 5;
               }
         }
      }

      return var1;
   }

   private int a(Graphics var1, String var2, int var3, int var4, int var5, int var6) {
      int var7 = 0;
      int var8 = 0;
      int var9 = 0;
      int var12 = this.fontHeight();
      char[] var14 = new char[var2.length()];
      var2.getChars(0, var2.length(), var14, 0);
      var7 = var3;
      int var10 = 0;
      int var11 = 0;
      char var13 = '\u0000';

      do {
         var8 = 0;
         var9 = var11;

         while (var11 < var14.length) {
            if ((var13 = var14[var11]) == ' ') {
               var8 += this.charWidth(var13);
               var11++;
               break;
            }

            var8 += this.charWidth(var13);
            var11++;
         }

         if (var7 + var8 <= var6 && var11 != var14.length) {
            var7 += var8;
         } else {
            if (var7 + var8 > var6 && var9 != var10) {
               var11 = var9;
            }

            if ((var5 & 1) > 0) {
               this.drawString(var1, var14, var10, var11 - var10, var6 + var3 >> 1, var4, var5);
            } else {
               this.drawString(var1, var14, var10, var11 - var10, var3, var4, var5);
            }

            var4 += var12;
            var7 = var3;
            var10 = var11;
         }
      } while (var11 < var14.length);

      return var4;
   }

   // $VF: renamed from: F (int) void
   private void setScreen(int var1) {
      this.es = 3;
      if (var1 == 1) {
         facingRight = true;
         eq = 90;
         if (!this.arenaLimitedAmmo) {
            currentWeapon = 1;
            playerSprites[playerSprites.length - 1] = weaponSprites[currentWeapon];
         }

         this.setPlayerAnim(1);
         playerRow = 6;
         playerY = playerRow * 38 - 8 << 8;
         playerX = -30720;
         velY = 0;
         velX = 2048;
         invulnFrames = 0;

         for (int var2 = 0; var2 <= 3; var2++) {
            this.enemies[var2].type = 1;
            this.enemies[var2].spawnCode = 0;
            this.enemies[var2].facingRight = false;
            this.enemies[var2].vertical = false;
            this.enemies[var2].state = 1;
            this.enemies[var2].animPhase = 0;
            this.enemies[var2].row = playerRow;
            this.enemies[var2].velY = 0;
            this.enemies[var2].velX = 0;
            this.enemies[var2].health = 1;
            this.enemies[var2].drawFlags = 1;
            this.enemies[var2].mount = 0;
            this.enemies[var2].worldY = playerRow * 38 - 8 << 8;
         }

         this.enemies[0].facingRight = true;
         this.enemies[0].worldX = -10752;
         this.enemies[0].velX = 2048;
         this.enemies[1].worldX = 131328;
         this.enemies[2].worldX = 145920;
         this.enemies[3].worldX = 160512;
      }

      if (var1 == 5) {
         for (int var3 = 0; var3 < maxEnemies; var3++) {
            this.enemies[var3].type = -1;
         }

         facingRight = true;
         currentWeapon = 0;
         playerSprites[playerSprites.length - 1] = weaponSprites[currentWeapon];
         this.setPlayerAnim(8);
         playerRow = 2;
         playerX = centerX << 8;
         velY = 0;
         velX = 0;
         invulnFrames = 0;
         playerY = playerRow * 38 - 5 << 8;
         weaponsOwned = -1;
         this.eu = 0;
         this.gB = -1;
      }

      if (var1 == 17) {
         this.computeCodeSymbols(gh, 0);
         this.computeCodeSymbols(gh, 1);
      }

      this.screen = var1;
      menuCursor = 0;
      if (var1 == 4 || var1 == 6 || var1 == 11) {
         RatchetMIDlet.refreshSlotSummaries();
      }
   }

   private void c(Graphics var1, int var2) {
      this.fillBlack(var1);
      f(var1, var2);
      this.resetClip(var1);
   }

   // $VF: renamed from: V () void
   private void handleScreenInput() {
      int var1 = keyEvent;
      if (keyEvent != 0) {
         switch (this.screen) {
            case 1:
               if (this.b(var1, 0, r && gw ? 7 : 6, 9)) {
                  if (menuCursor == 0) {
                     this.setScreen(11);
                  } else if (menuCursor == 1) {
                     this.setScreen(4);
                  } else if (menuCursor == 2 && gg) {
                     if (!RatchetMIDlet.isGameCompleted()) {
                        this.setScreen(16);
                     } else {
                        int var6 = 0;

                        while (var6 < eC.length) {
                           eC[var6++] = 32;
                        }

                        var6 = 0;

                        while (var6 < eD.length) {
                           eD[var6++] = 32;
                        }

                        eE = eC;
                        gk = new String[2];
                        gk[0] = new String(eC);
                        gk[1] = new String(eD);
                        eF = true;
                        an();
                        this.setScreen(19);
                     }
                  } else if (menuCursor != 3 || !r || !gw) {
                     if ((menuCursor != 4 || !r || !gw) && (menuCursor != 3 || r && gw)) {
                        if ((menuCursor != 4 || r && gw) && (menuCursor != 5 || !r || !gw)) {
                           if ((menuCursor != 5 || r && gw) && (menuCursor != 6 || !r || !gw)) {
                              if (menuCursor == 6 && (!r || !gw) || menuCursor == 7 && r && gw) {
                                 this.setScreen(9);
                              }
                           } else {
                              this.setScreen(8);
                           }
                        } else {
                           this.setScreen(5);
                        }
                     } else {
                        this.setScreen(2);
                     }
                  }
               }

               if (var1 == 57 && cheatMode) {
                  this.ex = true;
                  saveSlot = 2;
                  RatchetMIDlet.clearSlot(saveSlot);

                  for (int var8 = 0; var8 < weaponLevel.length; var8++) {
                     weaponLevel[var8] = 2;
                     this.levelStartLevels[var8] = 2;
                  }

                  this.startGameFromMenu(14);
                  weaponsOwned = -1;
                  return;
               }

               return;
            case 2:
               if (this.b(var1, 0, 1, 1)) {
                  switch (menuCursor) {
                     case 0:
                        this.audio.setEnabled(!this.audio.isEnabled());
                        if (this.audio.isEnabled()) {
                           this.audio.play(3);
                        }

                        this.es |= 3;
                        return;
                     case 1:
                        this.setScreen(6);
                     default:
                        return;
                  }
               }

               if (var1 != 29 && var1 != 15) {
                  return;
               }

               return;
            case 3:
            case 10:
            case 12:
            case 13:
            case 14:
            case 18:
            default:
               return;
            case 4:
               if (this.b(var1, 0, 2, 1)) {
                  if (RatchetMIDlet.slotInUse[menuCursor] == 0) {
                     return;
                  }

                  this.loadGameFromMenu(menuCursor);
                  return;
               }

               return;
            case 5:
               if (var1 != 52 && var1 != 2 && var1 != 29) {
                  if (var1 != 54 && var1 != 5 && var1 != 27) {
                     if (var1 == 8) {
                        this.setScreen(1);
                     } else if (var1 == 50 || var1 == 1) {
                        this.scrollText(true);
                     } else if (var1 == 56 || var1 == 6) {
                        this.scrollText(false);
                     }
                  } else if (menuCursor < 13) {
                     menuCursor++;
                     if (menuCursor > 3 && menuCursor < 11) {
                        currentWeapon++;
                     } else if (menuCursor == 3) {
                        currentWeapon = 0;
                        this.setPlayerAnim(8);
                     } else if (menuCursor == 11) {
                        currentWeapon = 7;
                     }

                     for (int var5 = 9; var5 >= 0; var5--) {
                        this.playerShots[var5].reset();
                     }

                     this.ev = 0;
                     this.eu = 0;
                     this.es |= 3;
                  } else {
                     ap();
                     this.setScreen(1);
                  }
               } else if (menuCursor > 0) {
                  menuCursor--;
                  if (menuCursor > 3 && menuCursor < 11) {
                     currentWeapon--;
                  } else if (menuCursor == 3) {
                     currentWeapon = 0;
                     this.setPlayerAnim(8);
                  } else if (menuCursor == 11) {
                     currentWeapon = 7;
                  }

                  for (int var4 = 9; var4 >= 0; var4--) {
                     this.playerShots[var4].reset();
                  }

                  this.ev = 0;
                  this.eu = 0;
                  this.es |= 3;
               } else {
                  ap();
                  this.setScreen(1);
               }

               playerSprites[playerSprites.length - 1] = weaponSprites[currentWeapon];
               return;
            case 6:
               if (this.b(var1, 0, 2, 2)) {
                  if (RatchetMIDlet.slotInUse[menuCursor] == 0) {
                     return;
                  }

                  menuChoice = menuCursor;
                  this.ex = false;
                  this.setScreen(7);
                  return;
               }

               return;
            case 7:
               if (this.b(var1, 0, 1, 6)) {
                  if (menuCursor == 0) {
                     if (this.ex) {
                        this.setScreen(11);
                        return;
                     }

                     this.setScreen(6);
                     return;
                  }

                  if (menuCursor == 1) {
                     if (this.ex) {
                        saveSlot = menuChoice;
                        menuChoice = -1;
                        this.startGameFromMenu(0);
                        return;
                     }

                     RatchetMIDlet.clearSlot(menuChoice);
                     menuChoice = -1;
                     RatchetMIDlet.refreshSlotSummaries();
                     this.es |= 3;
                     this.setScreen(6);
                     return;
                  }
               } else if (var1 == 29) {
                  if (this.ex) {
                     this.setScreen(11);
                     return;
                  }

                  this.setScreen(6);
                  return;
               }

               return;
            case 8:
               if (var1 != 52 && var1 != 2 && var1 != 29) {
                  if (var1 != 54 && var1 != 5 && var1 != 27) {
                     if (var1 != 8) {
                        return;
                     }
                     break;
                  }

                  if (menuCursor < 5) {
                     menuCursor++;
                     this.es |= 3;
                     return;
                  }

                  this.setScreen(1);
                  return;
               }

               if (menuCursor > 0) {
                  menuCursor--;
                  this.es |= 3;
                  return;
               }

               this.setScreen(1);
               return;
            case 9:
               if (!this.b(var1, 0, 1, 1)) {
                  return;
               }

               if (menuCursor != 0) {
                  if (menuCursor == 1) {
                     this.audio.stop();
                     ratchet.destroyApp(true);
                     return;
                  }

                  return;
               }
               break;
            case 11:
               if (this.b(var1, 0, 2, 1)) {
                  if (RatchetMIDlet.slotInUse[menuCursor] == 0) {
                     saveSlot = menuCursor;
                     RatchetMIDlet.clearSlot(saveSlot);
                     this.startGameFromMenu(0);
                     return;
                  }

                  menuChoice = menuCursor;
                  this.ex = true;
                  this.setScreen(7);
                  return;
               }

               return;
            case 15:
            case 17:
               if (var1 == 29) {
                  this.setScreen(15);
                  return;
               }

               if (var1 == 27) {
                  this.setScreen(1);
               }

               return;
            case 16:
               if (var1 == 29 || var1 == 26 || var1 == 15 || var1 == 8) {
                  this.setScreen(1);
                  return;
               }

               return;
            case 19:
               if (keyEvent == 0) {
                  return;
               }

               int var2 = super.keysPressed;
               eG = (super.keysPressed & 67043328) != 0;
               this.requestClear();
               if (keyEvent == 6 && !eG) {
                  eF = false;
                  eE = eD;
                  an();
                  return;
               }

               if (keyEvent == 1 && !eG) {
                  eF = true;
                  eE = eC;
                  an();
                  return;
               }

               if (eG) {
                  int var9 = var2 / 65536;

                  for (eB = 0; (var9 & 1) == 0; eB++) {
                     var9 >>= 1;
                  }

                  if (eA != -1) {
                     if (eA == eB && tickAccum - eI <= 1000) {
                        d(false);
                        this.ao();
                        return;
                     }

                     if (ey < eE.length - 1) {
                        ey++;
                     }

                     eA = eB;
                     d(true);
                     this.ao();
                     return;
                  }

                  eA = eB;
                  d(true);
                  this.ao();
                  return;
               }

               if (keyEvent != 15 && (keyEvent != 2 || eG)) {
                  if (keyEvent == 5 && !eG) {
                     eI = 0;
                     return;
                  }

                  if (keyEvent != 27 && keyEvent != 8) {
                     if (keyEvent != 29 && var1 != 29) {
                        return;
                     }
                     break;
                  }

                  gh = new String(eC).trim();
                  if (gh.length() >= 4 && gh.length() < 16) {
                     this.setScreen(17);
                  } else {
                     this.setScreen(20);
                  }

                  this.requestClear();
                  return;
               }

               an();
               int var3 = ey - 1;
               if (ey == eE.length - 1 && eE[ey] != 32) {
                  var3++;
               }

               eE[Math.max(0, var3)] = 32;
               an();
               gk[0] = new String(eC);
               this.requestClear();
               return;
            case 20:
               if (var1 == 27 || var1 == 8) {
                  this.setScreen(1);
                  return;
               }

               return;
         }

         this.setScreen(1);
      }
   }

   private boolean b(int var1, int var2, int var3, int var4) {
      boolean var5 = false;
      if (var1 == 1) {
         menuCursor = cursorPrev(menuCursor, var2, var3);
         this.es |= 3;
      } else if (var1 == 6) {
         menuCursor = cursorNext(menuCursor, var3, var2);
         this.es |= 3;
      } else if (var1 == 29) {
         this.es |= 3;
         this.setScreen(var4);
      } else if (var1 == 8 || var1 == 27) {
         var5 = true;
      }

      return var5;
   }

   // $VF: renamed from: j (javax.microedition.lcdui.Graphics) void
   private void renderMenuScreen(Graphics var1) {
      int var2 = 0;
      int var3 = 0;
      int var4 = 0;
      switch (this.screen) {
         case 1:
            this.c(var1, 0);
            this.drawPlayer(var1, 0, -4);
            this.enemies[0].draw(var1, this.enemies[0].drawFlags, 0, -4);
            this.enemies[1].draw(var1, this.enemies[1].drawFlags, 0, -4);
            this.enemies[2].draw(var1, this.enemies[2].drawFlags, 0, -4);
            this.enemies[3].draw(var1, this.enemies[3].drawFlags, 0, -4);
            this.resetClip(var1);
            var1.drawImage(aM, centerX, 4, 17);
            var2 = 4 + aM.getHeight() + (lineHeight >> 1);
            var3 = 0;
            var2 = 6 + var2;
            var4++;
            this.menuItemY[0] = var2;
            var2 = 6 + this.a(var1, this.getString(2), var2, menuCursor == 0, 14614528);
            var4++;
            this.menuItemY[1] = var2;
            var3++;
            var2 = 6 + this.a(var1, this.getString(3), var2, menuCursor == 1, 14614528);
            var4++;
            this.menuItemY[2] = var2;
            if (gg) {
               var3++;
               var2 = 6 + this.a(var1, this.getString(363), var2, menuCursor == 2, 14614528);
               var4++;
               this.menuItemY[3] = var2;
            }

            if (r) {
               var3++;
               var2 = 6 + this.a(var1, this.getString(362), var2, menuCursor == var3, 14614528);
               this.menuItemY[var4++] = var2;
            }

            if (!gw) {
               var3++;
            }

            var2 = 6 + this.a(var1, this.getString(4), var2, menuCursor == var3, 14614528);
            this.menuItemY[var4++] = var2;
            var3++;
            var2 = 6 + this.a(var1, this.getString(5), var2, menuCursor == var3, 14614528);
            this.menuItemY[var4++] = var2;
            var3++;
            var2 = 6 + this.a(var1, this.getString(6), var2, menuCursor == var3, 14614528);
            this.menuItemY[var4] = var2;
            var3++;
            this.a(var1, this.getString(7), var2, menuCursor == var3, 14614528);
            this.drawSoftKeys(var1, 8, 7);
            this.d(var1, this.menuItemY[menuCursor]);
            return;
         case 2:
            this.e(var1, 32);
            this.menuItemY[0] = 176;
            this.menuItemY[1] = 6 + this.a(var1, this.audio.isEnabled() ? this.getString(33) : this.getString(34), 176, menuCursor == 0, 16777215);
            var2 = this.menuItemY[1];
            this.a(var1, this.getString(38), var2, menuCursor == 1, 16777215);
            this.drawSoftKeys(var1, 8, 9);
            this.d(var1, this.menuItemY[menuCursor]);
            return;
         case 3:
         case 10:
         case 12:
         case 13:
         case 14:
         default:
            break;
         case 4:
            this.e(var1, 3);
            this.menuItemY[0] = 176;
            var2 = 6 + this.drawSaveSlot(var1, 0, cameraX + 57 + 28, 176, menuCursor == 0);
            this.menuItemY[1] = var2;
            var2 = 6 + this.drawSaveSlot(var1, 1, cameraX + 57 + 28, var2, menuCursor == 1);
            this.menuItemY[2] = var2;
            this.drawSaveSlot(var1, 2, cameraX + 57 + 28, var2, menuCursor == 2);
            this.drawSoftKeys(var1, 8, 9);
            this.d(var1, this.menuItemY[menuCursor]);
            return;
         case 5:
            int var5 = centerX - (aP >> 1);
            int var6 = centerX + (aP >> 1) - 45;
            this.e(var1, 5);
            var2 = 5 + lineHeight + 2;
            if (menuCursor < 3) {
               this.a(var1, this.getString(81), var2, false, 14614528);
            } else if (menuCursor < 11) {
               this.a(var1, this.getString(292), var2, false, 14614528);
            } else {
               this.a(var1, this.getString(301), var2, false, 14614528);
            }

            var2 = 149 + lineHeight;
            this.setFont(fontMain);
            if (menuCursor != this.gB) {
               this.gB = menuCursor;
               int[] var88 = (int[])null;
               if (menuCursor == 0) {
                  var88 = new int[]{82, 83, 84, 85};
               }

               if (menuCursor == 1) {
                  var88 = new int[]{288, 289, 86};
               }

               if (menuCursor == 2) {
                  var88 = new int[]{87, 88, 290};
               }

               if (var88 == null) {
                  ap();
               } else {
                  byte[] var8 = new byte[var88.length];

                  for (int var9 = 0; var9 < var8.length; var9++) {
                     var8[var9] = (byte)(var8[var9] | 2);
                  }

                  this.layoutScrollText(var88, var8, var5, var2, var6 - var5 - 3, 93, 30, 16777215, 16777215);
               }
            }

            if (menuCursor <= 2) {
               this.drawScrollText(var1);
            } else if (menuCursor == 3) {
               var2 = this.b(var1, this.getString(53), var2, false, 16777215) + lineHeight;
               this.a(var1, this.getString(293), var5, var2, 0, var6);
            } else if (menuCursor == 4) {
               var2 = this.b(var1, this.getString(54), var2, false, 16777215) + lineHeight;
               this.a(var1, this.getString(294), var5, var2, 0, var6);
            } else if (menuCursor == 5) {
               var2 = this.b(var1, this.getString(55), var2, false, 16777215) + lineHeight;
               this.a(var1, this.getString(295), var5, var2, 0, var6);
            } else if (menuCursor == 6) {
               var2 = this.b(var1, this.getString(56), var2, false, 16777215) + lineHeight;
               this.a(var1, this.getString(296), var5, var2, 0, var6);
            } else if (menuCursor == 7) {
               var2 = this.b(var1, this.getString(57), var2, false, 16777215) + lineHeight;
               this.a(var1, this.getString(297), var5, var2, 0, var6);
            } else if (menuCursor == 8) {
               var2 = this.b(var1, this.getString(58), var2, false, 16777215) + lineHeight;
               this.a(var1, this.getString(298), var5, var2, 0, var6);
            } else if (menuCursor == 9) {
               var2 = this.b(var1, this.getString(59), var2, false, 16777215) + lineHeight;
               this.a(var1, this.getString(299), var5, var2, 0, var6);
            } else if (menuCursor == 10) {
               var2 = this.b(var1, this.getString(60), var2, false, 16777215) + lineHeight;
               this.a(var1, this.getString(300), var5, var2, 0, var6);
            } else if (menuCursor == 11) {
               this.setFont(fontMain);
               var2 = this.a(var1, crateSprites[0], var2, 302);
               this.a(var1, this.getString(305), var5, var2, 0, var6);
            } else if (menuCursor == 12) {
               this.setFont(fontMain);
               var2 = this.a(var1, crateSprites[1], var2, 303);
               this.a(var1, this.getString(306), var5, var2, 0, var6);
            } else if (menuCursor == 13) {
               this.setFont(fontMain);
               var2 = this.a(var1, crateSprites[2], var2, 304);
               this.a(var1, this.getString(307), var5, var2, 0, var6);
            }

            this.drawPageIndicator(var1, menuCursor + 1, 14);
            this.drawSoftKeys(var1, 10, 9);
            if (menuCursor > 2 && menuCursor < 11) {
               this.drawPlayer(var1, 0, -8);
               if (menuCursor > 3) {
                  for (int var89 = 9; var89 >= 0; var89--) {
                     if (this.playerShots[var89].type >= 0) {
                        this.playerShots[var89].draw(var1, this);
                     }
                  }

                  return;
               }
            }
            break;
         case 6:
            this.e(var1, 38);
            this.menuItemY[0] = 176;
            var2 = 6 + this.drawSaveSlot(var1, 0, cameraX + 57 + 28, 176, menuCursor == 0);
            this.menuItemY[1] = var2;
            int var53;
            this.menuItemY[2] = var53 = 6 + this.drawSaveSlot(var1, 1, cameraX + 57 + 28, var2, menuCursor == 1);
            this.menuItemY[2] = var53;
            this.drawSaveSlot(var1, 2, cameraX + 57 + 28, var53, menuCursor == 2);
            this.drawSoftKeys(var1, 8, 9);
            this.d(var1, this.menuItemY[menuCursor]);
            return;
         case 7:
            this.e(var1, -1);
            var1.setColor(14614528);
            this.a(var1, this.getString(39), 42, 5, 17, 198);
            this.menuItemY[0] = 176;
            var2 = 6 + this.a(var1, this.getString(12), 176, menuCursor == 0, 16777215);
            this.menuItemY[1] = var2;
            this.a(var1, this.getString(11), var2, menuCursor == 1, 16777215);
            this.drawSoftKeys(var1, 8, 9);
            this.d(var1, this.menuItemY[menuCursor]);
            return;
         case 8:
            var2 = 0;
            this.c(var1, 0);
            if (menuCursor != 0) {
               var2 = this.drawHeading(var1, 6);
            } else {
               var1.setColor(14614528);
            }

            this.setFont(fontHighlight);
            var2 += lineHeight;
            if (menuCursor == 0) {
               this.a(var1, this.getString(314), 42, var2, 17, 198);
            } else if (menuCursor == 1) {
               var2 = this.a(var1, this.getString(91), 42, var2, 17, 198);
               var2 = this.a(var1, this.getString(92), 42, var2, 17, 198) + lineHeight * 2;
               this.a(var1, this.getString(93), 42, var2, 17, 198);
            } else if (menuCursor == 2) {
               var2 = this.b(var1, this.getString(94), var2, false, 14614528) + lineHeight;
               var2 = this.b(var1, this.getString(95), var2, false, 14614528);
               var2 = this.b(var1, this.getString(96), var2, false, 14614528) + lineHeight;
               var2 = this.b(var1, this.getString(97), var2, false, 14614528);
               this.b(var1, this.getString(98), var2, false, 14614528);
            } else if (menuCursor == 3) {
               var2 = this.b(var1, this.getString(99), var2, false, 14614528);
               var2 = this.b(var1, this.getString(100), var2, false, 14614528) + lineHeight;
               var2 = this.b(var1, this.getString(101), var2, false, 14614528);
               var2 = this.b(var1, this.getString(102), var2, false, 14614528) + lineHeight;
               var2 = this.b(var1, this.getString(108), var2, false, 14614528);
               var2 = this.b(var1, this.getString(109), var2, false, 14614528);
               var2 = this.b(var1, this.getString(110), var2, false, 14614528);
               this.b(var1, this.getString(111), var2, false, 14614528);
            } else if (menuCursor == 4) {
               var2 = this.b(var1, this.getString(103), var2, false, 14614528);
               var2 = this.b(var1, this.getString(104), var2, false, 14614528);
               var2 = this.b(var1, this.getString(105), var2, false, 14614528);
               var2 = this.b(var1, this.getString(106), var2, false, 14614528);
               var2 = this.b(var1, this.getString(107), var2, false, 14614528) + lineHeight;
               var2 = this.b(var1, this.getString(112), var2, false, 14614528);
               var2 = this.b(var1, this.getString(113), var2, false, 14614528);
               this.b(var1, this.getString(114), var2, false, 14614528);
            } else if (menuCursor == 5) {
               var2 += lineHeight;
               var2 = this.a(var1, this.getString(89), 42, var2, 17, 198);
               String var7 = this.getString(90);
               var7 = var7 + "1.0.22";
               var2 = this.a(var1, var7, 42, var2, 17, 198) + lineHeight * 2;
               var2 = this.b(var1, this.getString(115), var2, false, 14614528);
               this.b(var1, this.getString(116), var2, false, 14614528);
            }

            this.drawPageIndicator(var1, menuCursor + 1, 6);
            this.drawSoftKeys(var1, 10, 9);
            return;
         case 9:
            this.e(var1, 40);
            this.menuItemY[0] = 176;
            var2 = 6 + this.a(var1, this.getString(12), 176, menuCursor == 0, 16777215);
            this.menuItemY[1] = var2;
            this.a(var1, this.getString(11), var2, menuCursor == 1, 16777215);
            this.drawSoftKeys(var1, 8, 9);
            this.d(var1, this.menuItemY[menuCursor]);
            return;
         case 11:
            this.e(var1, 313);
            this.menuItemY[0] = 180;
            var2 = 6 + this.drawSaveSlot(var1, 0, cameraX + 57 + 28, 180, menuCursor == 0);
            this.menuItemY[1] = var2;
            var2 = 6 + this.drawSaveSlot(var1, 1, cameraX + 57 + 28, var2, menuCursor == 1);
            this.menuItemY[2] = var2;
            this.drawSaveSlot(var1, 2, cameraX + 57 + 28, var2, menuCursor == 2);
            this.drawSoftKeys(var1, 8, 9);
            this.d(var1, this.menuItemY[menuCursor]);
            return;
         case 15:
         case 17:
            this.c(var1, 0);
            this.resetClip(var1);
            var2 = this.drawHeading(var1, 363) + (lineHeight >> 1);
            var2 = this.a(var1, this.getString(364), var2, false, 14614528);
            var2 = this.a(var1, gh, var2, false, 14614528) + (var1.getFont().getHeight() + 0 >> 1);
            var1.setColor(14614528);
            var2 = this.drawCodeRow(var1, 1, var2) + (var1.getFont().getHeight() + 0 >> 1);
            this.drawCodeRow(var1, 0, var2);
            var1.getFont().getHeight();
            this.drawSoftKeys(var1, 9, -1);
            return;
         case 16:
            this.c(var1, 0);
            this.resetClip(var1);
            var2 = this.drawHeading(var1, 363) + lineHeight;
            var2 = this.b(var1, this.getString(365), var2, false, 14614528);
            var2 += 10;
            var2 = this.a(var1, this.getString(366), var2, false, 14614528);
            var2 = this.a(var1, this.getString(367), var2, false, 14614528);
            this.a(var1, this.getString(368), var2, false, 14614528);
            this.drawSoftKey(var1, 9, false);
            return;
         case 18:
            return;
         case 19:
            this.c(var1, 0);
            var2 = this.drawHeading(var1, 434) + lineHeight * 4;
            var2 = this.b(var1, this.getString(369), var2, false, 14614528) + lineHeight;
            this.b(var1, gk[0].trim(), var2, false, 14614528);
            this.drawSoftKeys(var1, 372, 403);
            break;
         case 20:
            this.c(var1, 0);
            var2 = this.drawHeading(var1, 434) + lineHeight * 4;
            if (gh.length() < 4) {
               this.b(var1, this.getString(430), var2, false, 14614528);
            } else {
               this.b(var1, this.getString(431), var2, false, 14614528);
            }

            this.drawSoftKeys(var1, 44, -1);
            return;
      }
   }

   // $VF: renamed from: W () void
   private void updateMenuScreen() {
      switch (this.screen) {
         case 0:
            this.setScreen(1);
            return;
         case 1:
            if (eq <= 100) {
               eq++;
               return;
            }

            this.updatePlayer(true);
            this.stepAnim(this.enemies[0].slot, deltaTime);
            this.stepAnim(this.enemies[1].slot, deltaTime);
            this.stepAnim(this.enemies[2].slot, deltaTime);
            this.stepAnim(this.enemies[3].slot, deltaTime);
            this.es |= 1;
            playerX = playerX + velX;
            this.enemies[0].worldX = this.enemies[0].worldX + this.enemies[0].velX;
            this.enemies[1].worldX = this.enemies[1].worldX + this.enemies[1].velX;
            this.enemies[2].worldX = this.enemies[2].worldX + this.enemies[2].velX;
            this.enemies[3].worldX = this.enemies[3].worldX + this.enemies[3].velX;
            if (playerX >> 8 > 399 && facingRight) {
               facingRight = false;
               velX *= -1;
               this.enemies[0].facingRight = false;
               this.enemies[0].velX *= -1;
               this.enemies[0].worldX = 182272;
               this.enemies[1].velX = -2048;
               this.enemies[2].velX = -2048;
               this.enemies[3].velX = -2048;
               return;
            }

            if (this.enemies[0].worldX >> 8 < -10 && !this.enemies[0].facingRight) {
               eq = 0;
               facingRight = true;
               velX *= -1;
               playerX = -30720;
               this.enemies[0].velX *= -1;
               this.enemies[0].facingRight = true;
               this.enemies[0].worldX = -10752;
               this.enemies[1].worldX = 138496;
               this.enemies[1].velX = 0;
               this.enemies[2].worldX = 153088;
               this.enemies[2].velX = 0;
               this.enemies[3].worldX = 167680;
               this.enemies[3].velX = 0;
               return;
            }
         case 2:
         case 3:
         case 4:
         default:
            break;
         case 5:
            this.es |= 1;
            this.updatePlayer(true);
            if (menuCursor == 3) {
               if (this.eu > 70) {
                  this.setPlayerAnim(0);
                  this.eu = 0;
               } else if (this.eu == 1 || this.eu == 10 || this.eu == 20) {
                  this.setPlayerAnim(8);
               }

               this.eu++;
               return;
            }

            if (this.ev < 3) {
               if (menuCursor > 3 && menuCursor < 11 && this.eu > 10) {
                  this.fireWeapon();
                  this.eu = 0;
                  this.ev++;

                  for (int var1 = 0; var1 < 8; var1++) {
                     if (ammo[var1] == 0) {
                        ammo[var1] = 30;
                     }
                  }
               }
            } else if (this.eu > 50) {
               this.ev = 0;
               this.eu = 0;
            } else if (this.eu == 1) {
               this.setPlayerAnim(0);
            }

            this.eu++;

            for (int var2 = 9; var2 >= 0; var2--) {
               a(this.playerShots[var2]);
            }
      }
   }

   private void d(Graphics var1, int var2) {
      int var3 = aI[0].width << 1;
      short var4 = aI[0].height;
      int var5 = 240 - var3 >> 1;
      int var6 = var2 - (var4 >> 1) + 3 - var4 * this.et;
      var1.setClip(var5, var6, var3, var4);
      aI[0].draw(var1, var5, var6, 0);
      aI[0].draw(var1, var5 + var3, var6, 2);
      var5 += 3;
      var3 -= 6;
      var1.setClip(var5, var6, var3, var4);
      var1.setColor(8388608);
      this.setFont(fontSelected);
      int var7 = this.stringWidth(this.ew);
      int var8 = 0;
      if (var7 <= var3) {
         var8 = var3 - var7 >> 1;
      } else {
         int var9 = Math.max(0, Math.min(2000, gx - 1000));
         var8 = (var3 - var7) * (gy ? 2000 - var9 : var9) / 2000;
      }

      this.drawString(var1, this.ew, var5 + var8, var2 - 1, 20);
      this.resetClip(var1);
   }

   private int e(Graphics var1, int var2) {
      int var3 = 0;
      this.c(var1, 0);
      var1.setColor(9114112);
      this.setFont(fontHighlight);
      var1.fillRect((240 - aP >> 1) + 3, 156, aP - 6, aQ - 11);

      for (int var4 = 0; var4 < 28; var4++) {
         aN[var4].draw(var1, (240 - aP >> 1) - 5, 149, 0);
      }

      if (var2 != -1) {
         var3 = this.drawHeading(var1, var2);
      }

      return var3;
   }

   private int a(Graphics var1, Sprite var2, int var3, int var4) {
      var1.setClip(240 - crateW >> 1, 55, crateW, crateH);
      var2.draw(var1, 240 - crateW >> 1, 55, 0);
      this.resetClip(var1);
      return this.a(var1, this.getString(var4), var3, false, 16777215) + var1.getFont().getHeight() + 0;
   }

   private static void f(Graphics var0, int var1) {
      int var3 = 0;
      boolean var5 = false;
      var3 = 0;

      for (int var8 = -16; var3 < 12; var8 += 28) {
         int var4 = -6;

         for (int var2 = 0; var2 < 6; var4 += 42) {
            if (ep[(var1 * 6 + var2) * 12 + var3] >= 0) {
               if (aY == null || var1 > 1 || var2 > 5 || var3 > 11) {
                  menuCursor = 0;
               }

               boolean var6 = ep[(var1 * 6 + var2) * 12 + var3] > 8;
               aY[var6 ? ep[(var1 * 6 + var2) * 12 + var3] - 9 : ep[(var1 * 6 + var2) * 12 + var3]].draw(var0, var6 ? var4 + 42 : var4, var8, var6 ? 2 : 0);
            } else {
               var0.setColor(0);
               var0.fillRect(var4, var8, 42, 28);
            }

            var2++;
         }

         var3++;
      }
   }

   private static void a(Projectile var0) {
      if (var0.type != -1) {
         if (var0.type >= 6 && var0.type <= 8) {
            var0.worldX = var0.worldX + var0.velX;
            int var6 = var0.worldX >> 8;
            var0.worldY = var0.worldY + var0.velY;
            if (var6 > 240) {
               var0.reset();
               return;
            }
         } else if (var0.type >= 0 && var0.type <= 2) {
            var0.worldX = var0.worldX + var0.velX;
            int var5 = var0.worldX >> 8;
            var0.worldY = var0.worldY + var0.velY;
            if (var5 > 240) {
               var0.reset();
               return;
            }
         } else {
            if (var0.type >= 3 && var0.type <= 5) {
               var0.worldX = var0.worldX + var0.velX;
               int var4 = var0.worldX >> 8;
               if (var0.worldY >> 8 > 320 || var4 > 240) {
                  var0.reset();
               }

               var0.worldY = var0.worldY - var0.velY;
               var0.velY -= 256;
               return;
            }

            if (var0.type >= 9 && var0.type <= 11) {
               if (var0.age++ == 2) {
                  var0.reset();
                  return;
               }
            } else if (var0.type >= 12 && var0.type <= 14) {
               var0.homeInOnTarget();
               int var3;
               if ((var3 = var0.worldX >> 8) > 240) {
                  var0.reset();
                  return;
               }
            } else if (var0.type >= 15 && var0.type <= 17) {
               if (var0.age++ == 3) {
                  var0.reset();
                  return;
               }
            } else if (var0.type >= 18 && var0.type <= 20) {
               var0.homeInOnTarget();
               int var1;
               if ((var1 = var0.worldX >> 8) > 240) {
                  var0.reset();
               }
            }
         }
      }
   }

   // $VF: renamed from: X () void
   private void gotoTitle() {
      state = 105;
      this.audio.play(3);
      menuCursor = 0;
      this.requestClear();
   }

   // $VF: renamed from: G (int) void
   private void startGameFromMenu(int var1) {
      this.audio.stop();
      RatchetMIDlet.refreshSlotSummaries();
      menuCursor = 0;
      this.startNewGame(var1);
      this.requestClear();
   }

   // $VF: renamed from: H (int) void
   private void loadGameFromMenu(int var1) {
      this.audio.stop();
      ratchet.loadSlot(var1);
      this.requestClear();
   }

   // $VF: renamed from: g (int, int) int
   public static int getRawTile(int var0, int var1) {
      return var0 >= 0 && var0 < 28 && var1 >= 0 && var1 < 18 ? sectionTiles[sectionTileBase[sectionId] * 28 + var0][var1] : -1;
   }

   // $VF: renamed from: I (int) void
   private void loadLevelInfo(int var1) {
      try {
         DataInputStream var2;
         sectionCount = (byte)(var2 = this.openResource(var1)).read();
         tilesetId = (byte)var2.read();
         retrySection = startSection;
         startSection = (byte)var2.read();

         for (int var3 = 0; var3 < sectionCount; var3++) {
            for (int var4 = 0; var4 < 4; var4++) {
               sectionExits[var3 * 4 + var4] = (byte)var2.read();
            }
         }

         for (int var6 = 0; var6 < sectionCount; var6++) {
            sectionTileBase[var6] = (byte)var2.read();
         }
      } catch (Exception var5) {
      }
   }

   // $VF: renamed from: J (int) void
   private void loadLevelTiles(int var1) {
      this.loadLevelInfo(levelInfoResIds[var1]);
      this.applyTilesetChange();

      try {
         DataInputStream var2 = this.openResource(levelTileResIds[var1]);

         for (int var3 = 0; var3 < sectionCount; var3++) {
            for (int var4 = 0; var4 < 28; var4++) {
               for (int var5 = 0; var5 < 18; var5++) {
                  int var6 = var2.read();
                  sectionTiles[var3 * 28 + var4][var5] = (byte)(var6 - 32);
               }
            }
         }

         var2.close();
      } catch (IOException var7) {
      }
   }

   // $VF: renamed from: a (int, boolean) void
   private void buildSection(int var1, boolean var2) {
      int var3 = 0;
      int var4 = 0;
      boolean var7 = false;
      boolean var8 = false;
      boolean var9 = false;
      sectionId = var1;
      updateSectionLock(worldId, var1);

      for (int var10 = 3; var10 >= 0; var10--) {
         platformDir[var10] = -1;
      }

      for (int var21 = 49; var21 >= 0; var21--) {
         this.crateAbove[var21] = this.crateBelow[var21] = -1;
         this.crateFallOffset[var21] = 0;
         this.crateFalling[var21] = false;
         crateType[var21] = -1;
      }

      for (int var22 = maxEnemies - 1; var22 >= 0; var22--) {
         this.enemies[var22].type = -1;
      }

      R = -1;
      Q = -1;
      P = -1;
      O = false;
      L = true;
      M = true;
      N = true;
      collectibleCol = -1;
      collectibleRow = -1;

      for (int var23 = 3; var23 >= 0; var23--) {
         ziplineX1[var23] = -1;
         ziplineX2[var23] = -1;
      }

      for (int var24 = 11; var24 >= 0; var24--) {
         pickupKind[var24] = -1;
      }

      for (int var25 = 2; var25 >= 0; var25--) {
         this.doorBit[var25] = 0;
         this.switchCol[var25] = -1;
      }

      for (int var26 = 9; var26 >= 0; var26--) {
         this.enemyShots[var26].type = -1;
      }

      for (int var27 = 9; var27 >= 0; var27--) {
         this.playerShots[var27].type = -1;
      }

      this.bonusCol = -1;
      this.bonusRow = -1;
      dg = true;
      dg = false;

      for (int var28 = 27; var28 >= 0; var28--) {
         for (int var11 = 17; var11 >= 0; var11--) {
            int var5;
            label333: {
               var5 = getRawTile(var28, var11);
               if (!var7) {
                  if (!var8) {
                     break label333;
                  }

                  if ((var5 & 1) == 0 && var5 < 12) {
                     this.tiles[var28][var11 + 1] = (byte)(var5 + 1);
                     break label333;
                  }
               }

               this.tiles[var28][var11 + 1] = (byte)var5;
            }

            if (var9) {
               int var6 = var5;
               if (this.tiles[var28][var11 + 2] >= 32) {
                  for (int var31 = 1; var6 >= 71 && var6 <= 79; var31++) {
                     var6 = getRawTile(var28, var11 - var31);
                  }

                  if ((var6 & 1) == 0 && var6 < 12) {
                     this.tiles[var28][var11 + 1] = (byte)(var6 + 1);
                  } else {
                     this.tiles[var28][var11 + 1] = (byte)var6;
                  }
               } else if (var6 >= 71 && var6 <= 79) {
                  for (int var12 = 1; var6 >= 71 && var6 <= 79; var12++) {
                     var6 = getRawTile(var28, var11 - var12);
                  }

                  this.tiles[var28][var11 + 1] = (byte)var6;
               }
            }

            var7 = false;
            var8 = false;
            var9 = false;
            if (var5 >= 71 && var5 <= 79) {
               var7 = true;
               var9 = true;
               if (var5 == 71) {
                  this.placeCrate(var28, var11, 0, 0, var3);
               } else if (var5 == 72) {
                  this.placeCrate(var28, var11, 2, 0, var3);
               } else if (var5 == 73) {
                  this.placeCrate(var28, var11, 1, 0, var3);
               } else if (var5 == 74) {
                  this.placeCrate(var28, var11, 0, 0, var3++);
                  this.placeCrate(var28, var11, 0, 1, var3);
               } else if (var5 == 75) {
                  this.placeCrate(var28, var11, 0, 0, var3++);
                  this.placeCrate(var28, var11, 2, 1, var3);
               } else if (var5 == 76) {
                  this.placeCrate(var28, var11, 0, 0, var3++);
                  this.placeCrate(var28, var11, 1, 1, var3);
               } else if (var5 == 77) {
                  this.placeCrate(var28, var11, 0, 2, var3);
               } else if (var5 == 78) {
                  this.placeCrate(var28, var11, 2, 2, var3);
               } else if (var5 == 79) {
                  this.placeCrate(var28, var11, 1, 2, var3);
               }

               var3++;
            } else if (var5 == -61 || var5 == -60 || var5 == -59) {
               this.tiles[var28][var11] = 1;
            } else if (var5 == 59) {
               if (collectibleAvailable(worldId)) {
                  collectibleCol = var28;
                  collectibleRow = var11;
               }

               var7 = true;
            } else if (var5 >= -93 && var5 <= -62) {
               var8 = true;
            } else if (var5 == -56) {
               if (cq) {
                  this.bonusCol = var28;
                  this.bonusRow = var11;
               }

               var8 = true;
            } else if (var5 == 123) {
               var8 = true;
            } else if (var5 == 124) {
               this.tiles[var28][var11] = this.tiles[var28 + 1][var11];
            } else if (var5 == 80) {
               this.tiles[var28][var11] = 32;
            } else if (var5 == 81) {
               this.tiles[var28][var11] = 34;
            } else if (var5 == 82) {
               this.tiles[var28][var11] = 36;
            } else if (var5 == 83) {
               this.tiles[var28][var11] = 38;
            } else if (var5 == 84) {
               this.tiles[var28][var11] = 39;
            } else if (var5 == 85) {
               this.tiles[var28][var11] = 40;
            } else if (var5 == -101) {
               if (var2) {
                  spawnSection = var1;
                  spawnCol = var28;
                  spawnRow = var11 - 1;
                  if (var28 < 1) {
                     spawnCameraX = 0;
                  } else {
                     spawnCameraX = -57 * (var28 - 1);
                  }

                  if (var11 < 6) {
                     spawnCameraY = 0;
                  } else {
                     spawnCameraY = -38 * (var11 - 5);
                  }
               }

               this.tiles[var28][var11] = 38;
            } else if (var5 == -103) {
               this.tiles[var28][var11] = 0;
            } else if (var5 == -102) {
               this.tiles[var28][var11] = 1;
            } else if (var5 >= 86 && var5 <= 93) {
               var7 = true;
               int var33 = var5 - 86;
               setZiplineEndpoint(var28, var11, (var33 & 1) == 0, var33 >> 1);
            } else if (var5 == 121 || var5 == 122) {
               var7 = true;
            } else if (var5 == -100) {
               var8 = true;
            } else if (var5 >= 67 && var5 <= 70) {
               var7 = true;
               if (getRawTile(var28, var11 + 1) >= 32) {
                  var8 = true;
                  var7 = false;
               }

               byte var32 = eU[var5 - 67];

               for (int var13 = 3; var13 >= 0; var13--) {
                  if (platformDir[var13] == -1) {
                     platformDir[var13] = (byte)var32;
                     if (var32 != 0 && var32 != 1) {
                        if (var32 == 2) {
                           platformOriginX[var13] = (short)((var28 - 2) * 57);
                           platformOriginY[var13] = (short)(var11 * 38);
                           platformOffsetX[var13] = 114;
                           platformOffsetY[var13] = 0;
                        } else {
                           platformOriginX[var13] = (short)(var28 * 57);
                           platformOriginY[var13] = (short)((var11 - 2) * 38);
                           platformOffsetX[var13] = 0;
                           platformOffsetY[var13] = 76;
                        }
                        break;
                     }

                     platformOriginX[var13] = (short)(var28 * 57);
                     platformOriginY[var13] = (short)(var11 * 38);
                     platformOffsetX[var13] = 0;
                     platformOffsetY[var13] = 0;
                     break;
                  }
               }
            } else if (var5 >= -118 && var5 <= -105) {
               var8 = true;
               this.spawnEnemy(var28, var11, eT[var5 - -118], var4++);
            } else if (var5 == -104) {
               var8 = true;
               this.spawnEnemy(var28, var11, 13, var4++);
            } else if (var5 >= -130 && var5 <= -119) {
               if ((var5 - -130 & 1) == 0) {
                  var7 = true;
               } else {
                  this.tiles[var28][var11] = this.tiles[var28 + 1][var11];
               }

               this.spawnEnemy(var28, var11, eS[var5 - -130], var4++);
            } else if ((var5 < 94 || var5 > 100) && var5 != -95) {
               if (var5 >= 60 && var5 <= 66) {
                  var8 = true;
                  this.addSwitch((byte)var28, (byte)var11);
               } else if (var5 >= 101 && var5 <= 120) {
                  var8 = true;
               } else if (var5 > 58) {
                  var7 = true;
               } else {
                  this.tiles[var28][var11] = (byte)var5;
               }
            } else {
               this.tiles[var28][var11] = 1;
               this.addDoor((byte)var28, (byte)var11, var5);
            }
         }
      }

      if (worldId == 4 && sectionId == 1 && hasHintFlag(5)) {
         this.spawnEnemy(2, 9, 26, var4++);
         this.spawnEnemy(2, 9, 27, var4++);
         this.spawnEnemy(2, 9, 28, var4++);
      }

      if (worldId == 14 && sectionId == 1) {
         this.spawnEnemy(15, 8, 26, var4++);
         this.spawnEnemy(15, 8, 27, var4++);
         this.spawnEnemy(15, 8, 28, var4);
      }

      for (int var34 = 27; var34 >= 0; var34--) {
         for (int var35 = 17; var35 >= 0; var35--) {
            byte var14 = this.tiles[var34][var35];
            int var15;
            if (this.eg == 0 || var14 != 22 && var14 != 23) {
               if (this.eh == 0 || var14 != 24 && var14 != 25) {
                  if (this.ei != 0 && var14 == 27) {
                     var15 = this.ei;
                  } else if (this.ej != 0 && var14 == 28) {
                     var15 = this.ej;
                  } else if (this.ek != 0 && var14 == 29) {
                     var15 = this.ek;
                  } else if (this.el != 0 && var14 == 30) {
                     var15 = this.el;
                  } else {
                     var15 = var14 + 1;
                  }
               } else {
                  var15 = this.eh;
               }
            } else {
               var15 = this.eg;
            }

            this.setTile(var34, var35, var15);
         }
      }

      for (int var29 = 27; var29 >= 0; var29--) {
         solidColumnMask[var29] = 0;

         for (int var30 = 0; var30 < 18; var30++) {
            byte var20;
            if ((var20 = this.tiles[var29][var30]) >= 32 && var20 <= 58 || var20 >= 46 && var20 <= 58) {
               solidColumnMask[var29] = solidColumnMask[var29] | 1 << var30;
            }
         }
      }

      this.settleCrates();
      if (this.inArena) {
         this.sectionFirstVisit[0] = false;
      } else {
         this.sectionFirstVisit[sectionId] = false;
      }
   }

   // $VF: renamed from: k (javax.microedition.lcdui.Graphics) void
   private void drawTileLayer(Graphics var1) {
      int var2 = cameraX;
      int var3 = cameraY;
      int var4 = -var2 / 57;
      int var5 = -var3 / 38;
      int var6 = var4;
      int var7 = var5;
      int var8 = var4 * 57 + var2;
      int var9 = var5 * 38 + var3;
      int var13 = var8;
      int var14 = var9;
      var6 += 6;
      var7 += 10;
      if (var6 > 28) {
         var6 = 28;
      }

      if (var7 > 18) {
         var7 = 18;
      }

      if (eR == 0 || eR == 6) {
         boolean var18 = eR == 0;
         if (this.eg != 0) {
            this.setAnimatedTile(this.eg, (var18 ? 22 : 23) + 1);
         }

         if (this.eh != 0) {
            this.setAnimatedTile(this.eh, (var18 ? 24 : 25) + 1);
         }

         for (int var19 = 0; var19 < 4; var19++) {
            this.setAnimatedTile(this.ei - var19, 27 + as[var19] + 1);
            if (as[var19] >= 3) {
               as[var19] = 0;
            } else {
               as[var19]++;
            }
         }
      }

      if (++eR >= 12) {
         eR = 0;
      }

      var1.translate(0, 21);
      this.drawTileMap(var1);
      var1.translate(0, -21);
      var1.setClip(0, 21, 240, 299);

      for (int var10 = var4; var10 < var6; var10++) {
         for (int var11 = var5; var11 < var7; var11++) {
            if (this.tiles[var10][var11] <= 58) {
               int var26 = 0;
               if (var14 < 21) {
                  var26 = 21 - var14;
               }

               if (var26 < 38) {
               }
            } else {
               var1.setColor(0);
               var1.fillRect(var13, var14, 57, 38);
            }

            int var16 = getRawTile(var10, var11);
            int var27 = getRawTile(var10, var11 - 1);
            if (var16 > -103 && var16 <= -101) {
               int var25 = var14;
               int var30 = var16 - -102;
               if (eR >> 1 > 2 && var30 == 0) {
                  var30 += 2;
               }

               if (ad && var16 == -102) {
                  this.drawAnim(var1, bk, maxEnemies, var13 + 28 - 7, var25 - 19, 0);
                  this.stepAnim(maxEnemies, deltaTime);
               }

               checkpointSprites[var30].draw(var1, var13 + 28, var25, 0);
            } else if (var16 == 121 || var16 == 122) {
               int var12 = this.ak;
               int var24 = var14 + (38 - var12 >> 1);
               itemSprites[7].draw(var1, var13 + 28, var24, 0);
            } else if (var16 >= -100 && var16 <= -96 && worldId > 0 && worldId <= 10) {
               int var23 = var14;
               byte var29 = 3;
               if (var16 == -100) {
                  var29 = 4;
               } else {
                  var23 += 38;
               }

               aR[var29].draw(var1, var13 + 28, var23, 0);
            } else if (var16 == 59 && collectibleAvailable(worldId)) {
               int var22 = var14;
               itemSprites[4].draw(var1, var13 + 28, var22, 0);
            } else if (var27 >= 101 && var27 <= 120) {
               int var17 = var14 + 38;
               int var28 = hasHintFlag(var16 - 101) ? 3 : 4;
               if (var16 == 104) {
                  var28 += 2;
               }

               var1.setClip(0, 21, 240, 299);
               if (var28 != -1) {
                  crateSprites[var28].draw(var1, var13 + 28, var17 - -12 - 38, 0);
               }
            }

            var14 += 38;
         }

         var14 = var9;
         var13 += 57;
      }
   }

   // $VF: renamed from: h (int, int) boolean
   public final boolean isPassable(int var1, int var2) {
      if (var1 >= 0 && var1 < 28 && var2 >= 0 && var2 < 18) {
         byte var3 = this.tiles[var1][var2];
         int var4 = getRawTile(var1, var2);
         int var5 = getRawTile(var1, var2 + 1);
         int var6 = 1 << var4 - 94;
         int var7 = 1 << var5 - 94;
         if ((var4 < 94 || var4 > 100 || (doorBits & var6) == 0) && (var5 < 94 || var5 > 100 || (doorBits & var7) == 0)) {
            if ((var4 == -95 || var5 == -95) && (doorBits & 128) != 0) {
               return false;
            } else {
               return var3 >= 32 && var3 <= 37 ? true : var3 >= 0 && var3 <= 25;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   // $VF: renamed from: m (int) void
   public final void setPlayerAnim(int var1) {
      playerAnim = var1;
      this.startAnim(bt + 1, 1028, var1);
   }

   // $VF: renamed from: Y () void
   private void initPlayer() {
      runSpeed = 2048;
      airState = -1;
      swingshotState = -2;
      attackTimer = -1;
      attackFrame = 0;
      onMovingPlatform = false;
      fC = 0;
      platformDrift = 0;
      ammo = new short[8];
      weaponKills = new short[8];
      weaponLevel = new byte[8];
      this.atSectionEdge = false;
      this.levelStartAmmo = new short[8];
      this.levelStartKills = new short[8];
      this.levelStartLevels = new byte[8];
      meleeBoxX = new byte[2];
      meleeBoxY = new byte[2];
      meleeBoxW = new byte[2];
      meleeBoxH = new byte[2];
      velY = 0;
      velX = 0;
      fi = 0;
      facingRight = true;
      health = 20;
      maxFallSpeed = 4864;
      this.loadPlayerHitboxes(2066);
   }

   // $VF: renamed from: k () int
   public static int playerPixelX() {
      return playerX >> 8;
   }

   // $VF: renamed from: l () int
   public static int playerPixelY() {
      return playerY >> 8;
   }

   // $VF: renamed from: Z () int
   private static int rightColumn() {
      int var0;
      while ((var0 = ((playerX >> 8) + 8) / 57) < 0 || var0 >= 28) {
         if (var0 < 0) {
            playerX += 14592;
         } else {
            playerX -= 14592;
         }
      }

      return var0;
   }

   // $VF: renamed from: aa () int
   private static int leftColumn() {
      int var0;
      while ((var0 = ((playerX >> 8) - 8) / 57) < 0 || var0 >= 28) {
         if (var0 < 0) {
            playerX += 14592;
         } else {
            playerX -= 14592;
         }
      }

      return var0;
   }

   // $VF: renamed from: b (boolean) int
   private int groundY(boolean var1) {
      int var2 = 646;
      byte var3 = 3;
      if (var1) {
         var3 = 15;
      }

      if (playerY - (playerRow * 38 << 8) > var3 << 8) {
         if ((solidColumnMask[rightColumn()] & 1 << playerRow + 2) > 0 || (solidColumnMask[leftColumn()] & 1 << playerRow + 2) > 0) {
            var2 = (playerRow + 1) * 38;
         }
      } else if ((solidColumnMask[rightColumn()] & 1 << playerRow + 1) > 0 || (solidColumnMask[leftColumn()] & 1 << playerRow + 1) > 0) {
         var2 = playerRow * 38;
      }

      int var5 = crateFloorY();
      int var6 = var2 > var5 ? var5 : var2;
      int var7 = platformFloorY();
      onMovingPlatform = false;
      if (var7 < var6) {
         onMovingPlatform = true;
         return var7;
      } else {
         return var6;
      }
   }

   // $VF: renamed from: ab () boolean
   private boolean onOneWayTile() {
      int var1;
      int var2 = (var1 = playerX >> 8) / 57;
      int var3;
      if ((var3 = var1 % 57) < 0) {
         if (this.tiles[var2 - 1][playerRow + 1] < 32 || this.tiles[var2 - 1][playerRow + 1] > 37) {
            return false;
         }

         if (var3 > 57 && (this.tiles[var2 + 1][playerRow + 1] < 32 || this.tiles[var2 + 1][playerRow + 1] > 37)) {
            return false;
         }
      }

      return this.tiles[var2][playerRow + 1] >= 32 && this.tiles[var2][playerRow + 1] <= 37;
   }

   // $VF: renamed from: ac () boolean
   private boolean atLeftEdge() {
      return !this.isPassable((playerX >> 8) / 57 - 1, playerRow + 1) && ((playerX >> 8) - 8) % 57 < 8;
   }

   // $VF: renamed from: ad () boolean
   private boolean atRightEdge() {
      return !this.isPassable((playerX >> 8) / 57 + 1, playerRow + 1) && ((playerX >> 8) + 8) % 57 > 49;
   }

   // $VF: renamed from: ae () void
   private void updateZipline() {
      if (inputIntent == 1) {
         this.setPlayerAnim(2);
         inputIntent = facingRight ? 5 : 2;
         velY = 4864;
         velX = 0;
         airState = 0;
         if (inputIntent == 1) {
            inputIntent = 0;
         }

         if (lastIntent == 1) {
            lastIntent = 0;
         }
      } else {
         playerX = playerX + this.gc;
         playerY = playerY + this.gd;
         playerRow = playerY / 38 >> 8;
         if (playerPixelY() + spriteBaseHeight > this.ge) {
            this.setPlayerAnim(3);
            velX = 0;
            inputIntent = 0;
            lastIntent = 0;
         }
      }
   }

   // $VF: renamed from: af () void
   private void updateSwingshot() {
      if (swingshotState == 0) {
         int var8 = this.groundY(true);
         velY -= 128;
         playerY = playerY - velY;
         playerRow = playerY / 38 >> 8;
         if (playerPixelY() >= var8) {
            playerRow = var8 / 38;
            playerY = playerRow * 38 << 8;
         }

         swingshotX = swingshotX + swingshotStepX;
         swingshotY = swingshotY + swingshotStepY;
         if (swingshotY < swingshotTargetY || Math.abs(swingshotX - swingshotTargetX) < 1280 && Math.abs(swingshotY - swingshotTargetY) < 1280) {
            if (swingshotTile == 121) {
               swingshotState = 1;
            } else {
               swingshotState = 3;
               if (airState == -1) {
                  velY = 1140;
                  swingshotState = 2;
               }
            }

            byte var11 = 11;
            if (!facingRight) {
               var11 = -11;
            }

            boolean var14 = false;
            int var15 = (playerX >> 8) + var11 << 8;
            int var16 = (playerY >> 8) + 5 << 8;
            swingshotStepX = swingshotTargetX - var15 >> 3;
            swingshotStepY = swingshotTargetY - var16 >> 3;
            return;
         }
      } else if (swingshotState == 1) {
         playerX = playerX + swingshotStepX;
         playerY = playerY + swingshotStepY;
         playerRow = playerY / 38 >> 8;
         velY = 0;
         if (playerPixelY() << 8 < swingshotTargetY
            || Math.abs((playerPixelX() << 8) - swingshotTargetX) < 1280 && Math.abs((playerPixelY() << 8) - swingshotTargetY) < 1280) {
            this.setPlayerAnim(3);
            swingshotState = -2;
            fE = 0;
            ac = false;
            inputIntent = 0;
            lastIntent = 0;
            return;
         }
      } else if (swingshotState == 2) {
         velY -= 128;
         playerY = playerY - velY;
         playerRow = playerY / 38 >> 8;
         if (velY < 0) {
            swingshotState = 3;
            return;
         }
      } else if (swingshotState == 3) {
         int var1 = (swingshotTargetY >> 8) - playerPixelY();
         int var2 = (swingshotTargetX >> 8) - playerPixelX();
         boolean var3 = false;
         swingshotState = 5;
         velX = 0;
         velY = 0;
         if (var2 * var2 + var1 * var1 > 10474) {
            velX = var2 << 4;
            velY = var1 << 4;
            swingshotState = 4;
            return;
         }
      } else {
         if (swingshotState == 4) {
            int var7 = (swingshotTargetY >> 8) - playerPixelY();
            int var10 = (swingshotTargetX >> 8) - playerPixelX();
            boolean var13 = false;
            if ((var10 << 1) + (var7 << 1) > 10474) {
               playerX = playerX + velX;
               playerY = playerY + velY;
               playerRow = playerY / 38 >> 8;
               return;
            }

            swingshotState = 5;
            velY = 0;
            velX = 0;
            return;
         }

         if (swingshotState == 5) {
            short var6 = 0;
            short var9 = 0;
            int var12 = playerPixelY() - (swingshotTargetY >> 8) + 5;
            int var4;
            int var5;
            if ((var4 = facingRight ? (swingshotTargetX >> 8) - (playerPixelX() + 9) : playerPixelX() - 9 - (swingshotTargetX >> 8)) == 0) {
               var5 = 2000;
            } else {
               var5 = (var12 << 8) / var4;
            }

            if (var5 < -1945 || var5 > 1945) {
               var6 = 2880;
               var9 = 0;
            } else if (var5 > 616) {
               var6 = 2781;
               var9 = 447;
            } else if (var5 > 333) {
               var6 = 2494;
               var9 = 864;
            } else if (var5 > -10) {
               var6 = 2036;
               var9 = 1222;
            } else if (var5 > -334) {
               var6 = 2036;
               var9 = -1222;
            } else if (var5 > -617) {
               var6 = 2494;
               var9 = -864;
            } else if (var5 > -1946) {
               var6 = 2781;
               var9 = -447;
            }

            if (facingRight) {
               playerX += var6;
            } else {
               playerX -= var6;
            }

            playerY += var9;
            velY = 447;
            playerRow = playerY / 38 >> 8;
            if (var5 < 0 && var5 > -150) {
               this.setPlayerAnim(2);
               swingshotState = -1;
               fE = 15;
               inputIntent = 0;
               lastIntent = 0;
            }
         }
      }
   }

   // $VF: renamed from: ag () void
   private void startFall() {
      this.setPlayerAnim(2);
      airState = 0;
      fi = 0;
   }

   // $VF: renamed from: ah () void
   private void updateEnvironment() {
      int var1 = S;
      int var2 = playerRow + 1;
      if (fE > 0) {
         hudDirty = true;
         ac = true;
         if (--fE <= 0) {
            swingshotState = -2;
            fE = 0;
            ac = false;
         }
      }

      int var3 = getRawTile(var1, var2);
      ad = false;
      if (var3 >= 80 && var3 <= 85) {
         if (worldId != 10 || var1 != 17 || var2 != 12 || var3 != 58) {
            swingshotState = -1;
            fE = 10;
         }
      } else if (var3 >= 26 && var3 <= 31 && playerAnim != 10) {
         velY = 4864;
         airState = 0;
         this.boltMultiplier = 1;
         this.setPlayerAnim(2);
         this.spawnPlayerProjectile(playerX, playerPixelY() << 8, 30, true);
         if (!invincible) {
            health -= 4;
         }

         swingshotState = -2;
         fE = 0;
         ac = false;
         hudDirty = true;
      } else if (var3 == -101) {
         ad = true;
         ae = -1;
      } else {
         for (int var4 = --var2 + 2; var4 >= var2; var4--) {
            if ((var3 = getRawTile(var1, var4)) == 123 || var3 == 124) {
               spawnSection = sectionId;
               spawnCol = var1;
               spawnRow = var2;
               if (var1 < 1) {
                  spawnCameraX = 0;
               } else {
                  spawnCameraX = -57 * (var1 - 1);
               }

               if (var2 < 6) {
                  spawnCameraY = 0;
               } else {
                  spawnCameraY = -38 * (var2 - 5);
               }
               break;
            }

            if (var3 >= 101 && var3 <= 120 && hasHintFlag(var3 - 101)) {
               this.messageHasPortrait = true;
               messageCount = cJ[var3 - 101];
               this.showMessage(cK[var3 - 101]);
               clearHintFlag(var3 - 101);
               this.dR = 261 + (var3 - 101);
               dL = var3 - 101;
               return;
            }

            if (var3 >= -93 && var3 <= -62 && hasHintFlag(var3 - -93 + 20)) {
               if (var3 - -93 == 18 && (weaponsOwned & 32) == 0) {
                  return;
               }

               this.messageHasPortrait = true;
               messageCount = cJ[var3 - -93 + 20];
               this.showMessage(cK[var3 - -93 + 20]);
               clearHintFlag(var3 - -93 + 20);
               return;
            }
         }
      }

      if (playerRow >= 17 && dg) {
         df = true;
         this.setPlayerAnim(10);
      } else {
         if (this.atSectionEdge) {
            this.atSectionEdge = false;
            if (rightColumn() == 0 && !facingRight) {
               this.switchSection(2);
               return;
            }

            if (leftColumn() >= 27 && facingRight) {
               this.switchSection(5);
               return;
            }

            if (playerRow >= 17 && velY <= 0) {
               this.switchSection(6);
               return;
            }

            if (playerRow == 0 && playerY - playerRow * 38 < 1280) {
               this.switchSection(1);
            }
         }
      }
   }

   // $VF: renamed from: c (boolean) void
   private void updatePlayer(boolean var1) {
      if (!var1) {
         if (velY <= -maxFallSpeed) {
            velY = -maxFallSpeed;
         }

         if (playerAnim == 10) {
            inputIntent = 0;
            lastIntent = 0;
            fireCooldown &= -129;
         }

         if (playerAnim == 10 && !this.animPlaying) {
            this.respawn();
            if (df) {
               df = false;
            }

            deathCount++;
            return;
         }

         if (health <= 0 && playerAnim != 10) {
            this.audio.play(2);
            health = 0;
            this.setPlayerAnim(10);
            deathCount++;
            return;
         }

         if (swingshotState >= 0) {
            this.updateSwingshot();
         } else {
            if (playerAnim == 6) {
               this.updateZipline();
            } else {
               if (playerAnim == 11) {
                  inputIntent = 0;
                  lastIntent = 0;
                  velX = 0;
                  attackFrame = 1;
                  this.meleeAttack();
               } else if (playerAnim == 9) {
                  if (++hurtTimer > 6) {
                     if (airState == -1) {
                        this.setPlayerAnim(0);
                     } else {
                        velX = 0;
                        velY = 0;
                        this.setPlayerAnim(3);
                        airState = 0;
                     }
                  } else {
                     inputIntent = 0;
                  }
               }

               if (rightHeld || playerAnim == 8 && facingRight) {
                  if (playerAnim == 4) {
                     inputIntent = 0;
                  }

                  if (!facingRight || playerAnim != 1) {
                     if (airState == 2) {
                        runSpeed = 1024;
                     } else if (playerAnim == 8) {
                        runSpeed = 512;
                     } else if (airState > -1) {
                        runSpeed = 2048;
                     } else {
                        this.setPlayerAnim(1);
                        runSpeed = 2048;
                     }

                     velX = runSpeed;
                     facingRight = true;
                  }
               } else if (leftHeld || playerAnim == 8 && !facingRight) {
                  if (playerAnim == 4) {
                     inputIntent = 0;
                  }

                  if (facingRight || playerAnim != 1) {
                     if (airState == 2) {
                        runSpeed = 1024;
                     } else if (playerAnim == 8) {
                        runSpeed = 512;
                     } else if (airState > -1) {
                        runSpeed = 2048;
                     } else {
                        this.setPlayerAnim(1);
                        runSpeed = 2048;
                     }

                     velX = -runSpeed;
                     facingRight = false;
                  }
               } else if (lastIntent == 0) {
                  if (airState == 2) {
                     velX = facingRight ? 1024 : -1024;
                  } else {
                     label526: {
                        if (airState >= 0) {
                           if (velX > 0) {
                              velX -= 164;
                           } else if (velX < 0) {
                              velX += 164;
                           }

                           if (velX > 164 || velX < -164) {
                              break label526;
                           }
                        }

                        velX = 0;
                     }
                  }

                  if (playerAnim == 1) {
                     this.setPlayerAnim(0);
                  }
               }

               if (inputIntent == 1) {
                  if (airState == -1) {
                     velY = 4864;
                     airState++;
                  } else if (airState == 0 && this.animPlaying) {
                     velY = 4096;
                     this.setPlayerAnim(13);
                     airState++;
                  }

                  inputIntent = 0;
                  if (lastIntent == 1) {
                     lastIntent = 0;
                  }
               }

               if (playerAnim != 4 || airState < 0) {
                  stepCol = facingRight ? rightColumn() : leftColumn();
                  if (playerY - (playerRow * 38 << 8) < runSpeed) {
                     footRow = playerRow;
                  } else {
                     footRow = playerRow + 1;
                  }

                  if (stepCol >= 0
                     && stepCol <= 27
                     && this.isPassable(stepCol, footRow)
                     && (playerRow < 17 || playerRow == 17 && playerY - (playerRow * 38 << 8) == 0)) {
                     playerX = playerX + velX;
                     stepCol = facingRight ? rightColumn() : leftColumn();
                     if (!facingRight) {
                        rightColumn();
                     } else {
                        leftColumn();
                     }

                     if (this.touchesEnemy(-1)) {
                        playerX = playerX - (velX * 3 >> 1);
                     } else if (hitsCrate()) {
                        playerX = playerX - velX;
                     } else if (!this.isPassable(stepCol, footRow)) {
                        playerX = playerX - velX;
                     }
                  }

                  if (stepCol >= 27) {
                     this.atSectionEdge = true;
                  }

                  if (stepCol == 0) {
                     this.atSectionEdge = true;
                  }

                  if (attackTimer >= 0 && playerAnim != 11 && ++attackTimer > 15) {
                     attackTimer = -1;
                     attackFrame = 0;
                  }

                  int var2;
                  if ((var2 = this.groundY(false)) - playerPixelY() > 19) {
                     onMovingPlatform = false;
                  }

                  if (onMovingPlatform && airState == -1) {
                     playerRow = var2 / 38;
                     playerY = playerRow * 38 + var2 % 38 << 8;
                     playerX = playerX + (platformDrift << 8);
                     velY = 0;
                  } else if ((airState >= 0 || playerPixelY() < var2) && fi == 0) {
                     if (airState == 2) {
                        velY = -1024;
                     } else {
                        velY -= 512;
                     }

                     playerY = playerY - velY;
                     if (velY > 0 && (!this.isPassable(rightColumn(), playerRow) || !this.isPassable(leftColumn(), playerRow))) {
                        playerY = playerY + velY;
                        playerRow = (playerY >> 8) / 38;
                        velY = 0;
                     }

                     if (playerY - (playerRow * 38 << 8) >> 8 < 0) {
                        playerRow--;
                        if (playerRow < 0) {
                           playerRow = 0;
                           playerY = 0;
                        }

                        if (playerRow == 0) {
                           this.atSectionEdge = true;
                        }
                     } else if (playerPixelY() - playerRow * 38 > 38) {
                        playerRow++;
                     }

                     if (airState == -1) {
                        airState = 0;
                     }

                     if (playerAnim != 11 && playerAnim != 14 && playerAnim != 10) {
                        if (airState == 0 && playerAnim != 8 && playerAnim != 2) {
                           this.setPlayerAnim(2);
                        }

                        if (inputIntent == 2 || !facingRight) {
                           facingRight = false;
                        } else if (inputIntent == 5 || facingRight) {
                           facingRight = true;
                        }

                        if (velY <= 0 && airState == 1 && !ab && playerAnim == 12) {
                           airState = 2;
                        } else if (velY <= -1024 && airState != 2) {
                           this.setPlayerAnim(3);
                        }
                     }
                  }

                  if (fi == 0) {
                     if (playerAnim != 11 && velY < 0 && playerAnim != 10) {
                        for (int var3 = 0; var3 < maxEnemies; var3++) {
                           Enemy var4;
                           if ((var4 = this.enemies[var3]).type != 6 && var4.type != 5 && var4.type != 7) {
                              int var5 = playerX / 57 >> 8;
                              if (this.touchesEnemy(var3) && var4.feetY() > playerPixelY()) {
                                 this.setPlayerAnim(3);
                                 if (var4.type != 6 && var4.type != 5 && var4.type != 7) {
                                    if (playerPixelX() > var4.pixelX()) {
                                       var4.knockback = -10;
                                       if (this.isPassable(var5 + 1, playerRow + 1) && this.isPassable(var5, playerRow + 1)) {
                                          velY = 0;
                                          velX = 0;
                                          airState = 0;
                                          if (this.isPassable(var5 + 1, playerRow)) {
                                             playerX += 510;
                                          } else {
                                             playerX -= 510;
                                          }
                                       } else {
                                          airState = -1;
                                       }
                                    } else {
                                       var4.knockback = 10;
                                       if (this.isPassable(var5 - 1, playerRow + 1) && this.isPassable(var5, playerRow + 1)) {
                                          velY = 0;
                                          velX = 0;
                                          airState = 0;
                                          if (this.isPassable(var5 - 1, playerRow)) {
                                             playerX -= 510;
                                          } else {
                                             playerX += 510;
                                          }
                                       } else {
                                          airState = -1;
                                       }
                                    }
                                 } else {
                                    label315: {
                                       if (playerPixelX() > var4.pixelX()) {
                                          if (!this.isPassable(var5 + 1, playerRow)) {
                                             playerX -= 510;
                                             break label315;
                                          }
                                       } else if (this.isPassable(var5 - 1, playerRow)) {
                                          playerX -= 510;
                                          break label315;
                                       }

                                       playerX += 510;
                                    }

                                    if (var4.type == 1 && var4.state == 2) {
                                       health = health - Enemy.attackDamage[var4.type];
                                    }
                                 }
                              }
                           }
                        }
                     }

                     if (velY < 0 && playerPixelY() >= var2 && this.isPassable((playerX >> 8) / 57, playerRow)) {
                        playerRow = var2 / 38;
                        playerY = var2 << 8;
                        this.atSectionEdge = true;
                        velY = 0;
                        velX = 0;
                        airState = -1;
                        if (playerAnim == 3 || playerAnim == 2 || playerAnim >= 11 && playerAnim <= 14) {
                           if (airState == 2) {
                              airState = 1;
                           }

                           this.setPlayerAnim(4);
                        }
                     }
                  }
               } else if (this.animPlaying) {
                  this.setPlayerAnim(2);
                  velY = 4864;
               }

               if (playerAnim != 10) {
                  this.updatePickups();
                  if (fC != 0) {
                     fC--;
                  } else {
                     int var8 = playerX >> 8;
                     if (airState >= 0 && velY < 0) {
                        for (int var9 = 3; var9 >= 0; var9--) {
                           boolean var10;
                           int var11;
                           if (ziplineX1[var9] != -1
                              && (
                                 (var10 = ziplineX2[var9] - ziplineX1[var9] > 0) && var8 > ziplineX1[var9] && var8 < ziplineX2[var9]
                                    || !var10 && var8 < ziplineX1[var9] && var8 > ziplineX2[var9]
                              )
                              && Math.abs(
                                    (var11 = ziplineDy[var9] * var8 / ziplineDx[var9] + ziplineIntercept[var9])
                                       - (playerPixelY() + (spriteBaseHeight >> 1) << 8)
                                 )
                                 < -velY) {
                              int var12;
                              playerRow = ((var12 = var11 - (spriteBaseHeight << 8)) >> 8) / 38;
                              playerY = var12;
                              airState = -1;
                              this.setPlayerAnim(6);
                              velY = 0;
                              velX = 0;
                              facingRight = var10;
                              int var7 = (Math.abs(ziplineX2[var9] - ziplineX1[var9]) << 8) / 2048;
                              this.gc = (ziplineDx[var9] << 8) / var7;
                              this.gd = ziplineDy[var9] / var7;
                              this.ge = ziplineY2[var9];
                              break;
                           }
                        }
                     }
                  }
               }
            }

            this.updateEnvironment();
         }
      }

      this.animPlaying = this.stepAnim(bt + 1, deltaTime);
      if (playerAnim == 14) {
         velY = 0;
      }

      if (!this.animPlaying) {
         this.endAttackAnim();
      }
   }

   // $VF: renamed from: e (javax.microedition.lcdui.Graphics, int, int) void
   private void drawPlayer(Graphics var1, int var2, int var3) {
      cameraX = var2;
      cameraY = var3;
      int var4 = (playerX >> 8) - 22 + cameraX;
      int var5 = (playerY >> 8) + cameraY + -12;
      if (swingshotState >= 0) {
         int var7 = this.ak;
         int var6 = bl;
         byte var9 = 33;
         if (!facingRight) {
            var9 = 24;
         }

         int var10 = var4 + var9 - (var6 >> 1) << 8;
         int var11 = var5 + 8 - (var7 >> 1) << 8;
         int var12 = swingshotX + (cameraX << 8);
         int var13 = swingshotY + (cameraY << 8);
         if (swingshotState == 0) {
            var11 += 2304;
         }

         if (facingRight) {
            var12 -= var6 << 6;
         } else {
            var12 -= var6 << 7;
         }

         int var14 = (var12 - var10) / 19;
         int var15 = (var13 - var11) / 19;

         for (int var16 = 0; var16 < 20; var16++) {
            int var8 = var11;
            swingshotSprites[1].draw(var1, var10 >> 8, var8 >> 8, 0);
            var10 += var14;
            var11 += var15;
         }

         int var21 = var10 - var14 >> 8;
         int var18 = (var11 - var15 >> 8) - (var7 >> 1);
         if (facingRight) {
            swingshotSprites[0].draw(var1, var21, var18, 0);
         } else {
            swingshotSprites[0].draw(var1, var21 + 24, var18, 2);
         }
      }

      if ((invulnFrames & 1) != 1) {
         byte var19 = 0;
         if (!facingRight || fi != 0) {
            var19 = 2;
         }

         this.drawAnim(var1, playerSprites, bt + 1, var4 + 22, var5, var19);
         var1.setColor(255, 0, 0);
         if (currentWeapon == 0) {
            return;
         }
      }
   }

   // $VF: renamed from: ai () void
   private void trySwingshot() {
      boolean var1 = true;
      if (facingRight) {
         int var2 = rightColumn();
         if ((playerX >> 8) % 57 > 28) {
            var2++;
         }

         for (int var3 = var2; var3 < var2 + 3; var3++) {
            if (!var1) {
               return;
            }

            for (int var4 = playerRow; var4 >= playerRow - 3 && var1; var4--) {
               int var5;
               if (var3 >= 0 && var4 >= 0 && ((var5 = getRawTile(var3, var4)) == 121 || var5 == 122)) {
                  this.startSwingshot(var3, var4, var5);
                  var1 = false;
               }
            }
         }
      } else {
         int var6 = leftColumn();
         if ((playerX >> 8) % 57 < 28) {
            var6--;
         }

         for (int var7 = var6; var7 > var6 - 3 && var1; var7--) {
            for (int var8 = playerRow; var8 >= playerRow - 3 && var1; var8--) {
               int var9;
               if (var7 >= 0 && var8 >= 0 && ((var9 = getRawTile(var7, var8)) == 121 || var9 == 122)) {
                  this.startSwingshot(var7, var8, var9);
                  var1 = false;
               }
            }
         }
      }
   }

   // $VF: renamed from: aj () boolean
   private boolean fireWeapon() {
      byte var1 = weaponLevel[currentWeapon];
      if (ammo[currentWeapon] <= 0) {
         return false;
      }

      if (playerAnim == 0) {
         this.setPlayerAnim(7);
      }

      int var2 = facingRight ? 7168 : -7140;
      int var3 = facingRight ? 7168 : -7168;
      int var4 = (playerPixelY() << 8) + 6144;
      switch (currentWeapon) {
         case 1:
            this.spawnPlayerProjectile(playerX + var2, var4, 0 + var1, false);
            break;
         case 2:
            this.spawnPlayerProjectile(playerX + var2, var4, 3 + var1, false);
            break;
         case 3:
            var4 += 1280;
            this.spawnPlayerProjectile(playerX + var2, var4, 6 + var1, false);
            break;
         case 4:
            var4 += 768;
            this.spawnPlayerProjectile(playerX + var3, var4, 9 + var1, false);
            if (ammo[currentWeapon] > 0 && var1 == 2) {
               this.spawnPlayerProjectile(playerX + var3, var4, 9 + var1, false);
            }
            break;
         case 5:
            var4 += 1280;
            this.spawnPlayerProjectile(playerX + var3, var4 + (ammo[currentWeapon] & 1) * 1536, 12 + var1, false);
            break;
         case 6:
            var4 += 768;
            this.spawnPlayerProjectile(playerX + var3, var4, 15 + var1, false);
            break;
         case 7:
            this.spawnPlayerProjectile(playerX + var2, var4, 18 + var1, false);
      }

      if (this.screen != 5) {
         this.audio.play(6);
      }

      return true;
   }

   // $VF: renamed from: ak () void
   private void meleeAttack() {
      if (playerAnim < 4 || playerAnim == 11) {
         this.playerShotHitsCrates(-1);
         if (playerAnim != 11) {
            this.setPlayerAnim(8);
         }

         int var3 = bl;
         int var4 = this.ak;

         for (int var6 = 2; var6 >= 0; var6--) {
            if (this.switchCol[var6] != -1) {
               int var1 = this.switchCol[var6] * 57 + (57 - var3 >> 1);
               int var2 = this.switchRow[var6] * 38;
               if (Math.abs(var1 - playerPixelX()) <= 85
                  && Math.abs(var2 - playerPixelY()) <= 38
                  && rectsOverlap(
                     var1,
                     var2,
                     var3,
                     var4,
                     (playerX >> 8) + (facingRight ? meleeBoxX[attackFrame] : -meleeBoxX[attackFrame] - meleeBoxW[attackFrame]),
                     playerPixelY() + meleeBoxY[attackFrame],
                     meleeBoxW[attackFrame],
                     meleeBoxH[attackFrame]
                  )) {
                  int var7 = getRawTile(this.switchCol[var6], this.switchRow[var6]);
                  var7 -= 60;
                  doorBits &= ~(1 << var7);
                  if (var7 == 0) {
                     clearSectionSwitch(worldId, sectionId);
                  }
               }
            }
         }

         for (int var15 = maxEnemies - 1; var15 >= 0; var15--) {
            int var5 = this.enemies[var15].type;
            if (this.enemies[var15].type != -1 && this.enemies[var15].state != 5) {
               int var11 = this.enemies[var15].pixelX() - Enemy.bodyOffsetX[var5];
               int var12 = this.enemies[var15].feetY() + Enemy.bodyOffsetY[var5];
               byte var13 = Enemy.bodyWidth[var5];
               byte var14 = Enemy.bodyHeight[var5];
               if (Math.abs(var11 - playerPixelX()) <= 85
                  && Math.abs(var12 - playerPixelY()) <= 38
                  && rectsOverlap(
                     var11,
                     var12,
                     var13,
                     var14,
                     (playerX >> 8) + (facingRight ? meleeBoxX[attackFrame] : -meleeBoxX[attackFrame] - meleeBoxW[attackFrame]),
                     playerPixelY() + meleeBoxY[attackFrame],
                     meleeBoxW[attackFrame],
                     meleeBoxH[attackFrame]
                  )
                  && (!this.enemies[var15].meleeHit || playerAnim != 11)) {
                  if (this.enemies[var15].type == 6 || this.enemies[var15].type == 5 || this.enemies[var15].type == 7) {
                     return;
                  }

                  if (playerAnim == 11) {
                     this.enemies[var15].meleeHit = true;
                  }

                  if (playerAnim == 11) {
                     this.enemies[var15].health = this.enemies[var15].health - meleeDamage[weaponLevel[1]];
                  } else {
                     this.enemies[var15].health = this.enemies[var15].health - meleeDamage[weaponLevel[0]];
                  }

                  if (this.enemies[var15].state != 2 && this.enemies[var15].type != 6 && this.enemies[var15].type != 5 && this.enemies[var15].type != 7) {
                     if (this.enemies[var15].worldX > playerX) {
                        this.enemies[var15].knockback = 10;
                     } else {
                        this.enemies[var15].knockback = -10;
                     }
                  }

                  if (this.enemies[var15].health <= 0) {
                     int var8 = sectionId;
                     if (this.inArena) {
                        var8 = 0;
                     }

                     int var9 = var15 + var8 * 10 >> 5;
                     this.enemyBits[var9] = this.enemyBits[var9] & ~(1 << var15 + var8 * 10 - (var9 << 5));
                     this.enemies[var15].state = 5;
                     this.enemies[var15].animPhase = 1;
                     if (this.enemies[var15].type == 7) {
                        O = false;
                     }

                     if (var5 != 4) {
                        killCount++;
                        dO++;
                        if (bonusMode && ++this.boltMultiplier > 10) {
                           this.boltMultiplier = 10;
                        }

                        if (worldId != 0) {
                           for (int var10 = 0; var10 < Enemy.boltDrop[this.enemies[var15].spawnCode]; var10++) {
                              spawnPickup(this.enemies[var15].pixelX(), this.enemies[var15].feetY(), 0);
                           }
                        }
                     }
                  } else if (this.enemies[var15].state != 2) {
                     this.enemies[var15].state = 4;
                     this.enemies[var15].hurtTimer = 0;
                  }
               }
            }
         }

         if (airState == -1) {
            inputIntent = 0;
         }
      }
   }

   // $VF: renamed from: al () void
   private void endAttackAnim() {
      attackFrame = 0;
      if (airState >= 0 && playerAnim == 4) {
         this.setPlayerAnim(2);
      } else if (playerAnim == 7 || playerAnim == 8 && !this.animPlaying || playerAnim == 9 || playerAnim == 0 || playerAnim == 4) {
         this.setPlayerAnim(0);
      } else if (playerAnim == 13) {
         this.setPlayerAnim(12);
      } else {
         if (playerAnim == 14 && !this.animPlaying) {
            this.setPlayerAnim(11);
         }
      }
   }

   // $VF: renamed from: am () void
   private void updatePickups() {
      int var1 = playerPixelY();

      for (int var2 = 11; var2 >= 0; var2--) {
         byte var3 = pickupKind[var2];
         int var4 = pickupX[var2];
         short var5 = pickupVelX[var2];
         if (var3 != -1 && (health != 20 || var3 != 2)) {
            char var6 = '躀';
            if (var5 != 0 || Math.abs(var4 - playerX) <= var6 && Math.abs((pickupY[var2] >> 8) - var1) <= spriteBaseHeight) {
               if (var4 > playerX) {
                  var5 = (short)(var5 - 256);
               } else {
                  var5 = (short)(var5 + 256);
               }

               if ((var4 = var4 + var5) >= 401152) {
                  var4 = 401152;
               }

               int var7;
               if ((var7 = Math.abs(var4 - playerX)) <= 3648) {
                  hudDirty = true;
                  if (var3 <= 1) {
                     bolts = bolts + 10 * this.boltMultiplier;
                     boltsCollected = boltsCollected + 10 * this.boltMultiplier;
                     if (bolts > 999999) {
                        bolts = 999999;
                     }
                  } else if (var3 == 2) {
                     health += 4;
                     if (health > 20) {
                        health = 20;
                     }
                  } else {
                     addAmmo(1);
                  }

                  pickupKind[var2] = -1;
                  return;
               }

               int var8 = pickupY[var2] - (playerPixelY() + 19 << 8);
               if (var7 != 0) {
                  pickupY[var2] = pickupY[var2] + var8 * var5 * (var5 >= 0 ? -1 : 1) / var7;
               }

               pickupX[var2] = var4;
               pickupVelX[var2] = var5;
            }
         }
      }
   }

   // $VF: renamed from: K (int) void
   private static void addAmmo(int var0) {
      int var1 = 1;
      short var2 = 9999;

      for (int var3 = 1; var3 < 8; var3++) {
         if (var3 != 6 && (weaponsOwned & 1 << var3) > 0 && ammo[var3] < var2 && ammo[var3] != maxAmmoTable[var3 * 3 + weaponLevel[var3]]) {
            var2 = ammo[var3];
            var1 = var3;
         }
      }

      if (ammo[currentWeapon] < maxAmmoTable[currentWeapon * 3 + weaponLevel[currentWeapon]]) {
         ammo[currentWeapon] = (short)(ammo[currentWeapon] + var0 * ammoPerPickup[currentWeapon]);
         var1 = currentWeapon;
      } else {
         ammo[var1] = (short)(ammo[var1] + var0 * ammoPerPickup[var1]);
      }

      if (ammo[var1] > maxAmmoTable[var1 * 3 + weaponLevel[var1]]) {
         ammo[var1] = maxAmmoTable[var1 * 3 + weaponLevel[var1]];
      }
   }

   // $VF: renamed from: a (java.io.DataInputStream, byte[], int) void
   private static void readScaledBytes(DataInputStream var0, byte[] var1, int var2) {
      try {
         for (int var3 = 0; var3 < var2; var3++) {
            var1[var3] = var0.readByte();
            var1[var3] = (byte)(var1[var3] * spriteBaseHeight / 44);
         }
      } catch (IOException var4) {
      }
   }

   // $VF: renamed from: L (int) void
   private void loadPlayerHitboxes(int var1) {
      byte[] var2 = new byte[2];

      try {
         DataInputStream var3;
         readScaledBytes(var3 = this.openResource(var1), var2, 1);
         hitboxX = var2[0];
         readScaledBytes(var3, var2, 1);
         hitboxY = var2[0];
         readScaledBytes(var3, var2, 1);
         hitboxW = var2[0];
         readScaledBytes(var3, var2, 1);
         hitboxH = var2[0];
         readScaledBytes(var3, meleeBoxX, 2);
         readScaledBytes(var3, meleeBoxY, 2);
         readScaledBytes(var3, meleeBoxW, 2);
         readScaledBytes(var3, meleeBoxH, 2);
         var3.close();
      } catch (IOException var4) {
      }
   }

   // $VF: renamed from: a (java.lang.String, int) void
   private void computeCodeSymbols(String var1, int var2) {
      String var3 = var1.toUpperCase();
      int var5 = 0;
      int var7 = var3.length();

      for (int var8 = 0; var8 < var7; var8++) {
         char var6 = var3.charAt(var8);
         var5 += var6 * (var8 + 1);
      }

      int var4 = var5 * this.gi[var2];

      for (int var12 = 0; var12 < 8; var12++) {
         int var9 = var12 * 4;
         int var11 = 15 << var9;
         int var10 = (var4 & var11) >> var9;
         this.gj[var2 * 8 + var12] = nibbleToSymbol(var10);
      }
   }

   // $VF: renamed from: M (int) int
   private static int nibbleToSymbol(int var0) {
      if (var0 >= 0 && var0 <= 3) {
         return 0;
      } else if (var0 >= 4 && var0 <= 7) {
         return 1;
      } else {
         return var0 >= 8 && var0 <= 11 ? 2 : 3;
      }
   }

   // $VF: renamed from: f (javax.microedition.lcdui.Graphics, int, int) int
   private int drawCodeRow(Graphics var1, int var2, int var3) {
      var3 = this.b(var1, this.getString(var2 == 0 ? 333 : 334), var3, 17);
      this.resetClip(var1);
      int var4 = 51;

      for (int var5 = 0; var5 < 8; var4 += 18) {
         aR[7 + this.gj[var2 * 8 + var5]].draw(var1, var4, var3, 0);
         var5++;
      }

      this.resetClip(var1);
      return var3 + 14;
   }

   private static void an() {
      ey = 0;

      while (eE[ey] != 32 && ey < eE.length - 1) {
         ey++;
      }

      eA = -1;
   }

   private void ao() {
      if (eF) {
         eI = tickAccum;
         eE[ey] = eH[ez];
         gk[0] = new String(eC);
      } else {
         eI = tickAccum;
         eE[ey] = (byte)(eB + 48);
         gk[1] = new String(eD);
      }

      this.requestClear();
   }

   private static void d(boolean var0) {
      if (var0) {
         ez = 0;
         int var1 = 0;

         while (eB > var1) {
            if (eH[ez++] < 0) {
               var1++;
            }
         }
      } else {
         ez++;
         if (eH[ez] < 0) {
            ez = ez + eH[ez];
         }
      }
   }

   // $VF: renamed from: a (int[], byte[], int, int, int, int, int, int, int) void
   private void layoutScrollText(int[] var1, byte[] var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      gp = new String[var7];
      gq = new byte[var7];
      gl = var3;
      gm = var4;
      gn = var5;
      go = var6;
      gs = var8;
      gt = var9;
      var5 -= 3;
      int var11 = 0;
      this.requestClear();
      int var12 = 0;
      String var13 = "";

      for (int var10 = 0; var10 < var1.length; var10++) {
         byte var14 = var2[var10];
         int var16;
         if ((var16 = var1[var10]) >= 0) {
            var13 = this.getString(var1[var10]);
         } else if (var16 == -1) {
            gp[var12] = "";
            gq[var12++] = var14;
            var13 = "";
         } else if (var16 == -3) {
            var13 = gk[var11++];
         }

         int var15 = var13.length();
         int var17 = 0;
         int var18 = 0;

         while (var17 < var15) {
            for (int var19 = 0; var18 < var15 && var19 <= var5 - 2 - 4; var18++) {
               var19 += this.charWidth(var13.charAt(var18));
            }

            int var20;
            label50: {
               int var10000;
               if (var18 == var15) {
                  var10000 = var15 - 1;
               } else {
                  if ((var20 = var13.lastIndexOf(32, var18)) != -1) {
                     break label50;
                  }

                  var10000 = var18 > var15 - 1 ? var15 - 1 : var18;
               }

               var20 = var10000;
            }

            gp[var12] = var13.substring(var17, var20 + 1);
            gq[var12++] = var14;
            var18 = var17 = var20 + 1;
         }
      }

      gp[var12++] = "";
      gr = var12;
      gu = 0;
      gv = false;
   }

   private static void ap() {
      gp = null;
      gq = null;
      gk = null;
   }

   // $VF: renamed from: e (boolean) void
   private void scrollText(boolean var1) {
      if (var1) {
         if (gu > 0) {
            gu--;
            this.requestClear();
            return;
         }
      } else if (gu < gr - 1 && !gv) {
         gu++;
         this.requestClear();
      }
   }

   // $VF: renamed from: l (javax.microedition.lcdui.Graphics) void
   private void drawScrollText(Graphics var1) {
      var1.setClip(gl, gm, gn, go);
      int var3 = gm;

      int var2;
      for (var2 = gu; var2 < gr && var3 < gm + go; var2++) {
         byte var4 = gq[var2];
         int var5 = 16 | ((var4 & 2) != 0 ? 4 : 1);
         int var6 = gl + ((var4 & 2) != 0 ? 0 : gn - 4 >> 1) + 1;
         if ((var4 & 128) != 0) {
            var1.setColor(16777215);
            this.drawString(var1, gp[var2], var6 + 1, var3 + 1, var5);
         }

         var1.setColor((gq[var2] & 64) != 0 ? gt : gs);
         this.drawString(var1, gp[var2], var6, var3 + 1, var5);
         var3 += lineHeight;
      }

      gv = var2 == gr;
      this.resetClip(var1);
      if (!gv || gu != 0) {
         var1.setColor(gs);
         var1.drawRect(gl + gn, gm, 4, go);
         int var7 = gm + gu * go / gr;
         int var8 = gm + var2 * go / gr;
         var1.fillRect(gl + gn + 1, var7, 3, var8 - var7);
      }
   }
}
