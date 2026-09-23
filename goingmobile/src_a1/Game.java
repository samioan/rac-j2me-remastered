import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

// Hand-transcribed from decompiled_a1/h.java (class h, 6622 lines / 195KB --
// the largest class in a1 by a wide margin, roughly as big as every other
// a1 class combined). Produced with a scripted substitution pass
// (../../AppData-style scratch script, same technique as IntroManager.java)
// rather than fully hand-derived member-by-member, given the size -- see
// CLASS_MAP.md's "Game" section for the full confirmed/tentative/obfuscated
// breakdown and the collisions the script caught (F/G/H/V/W were each
// *also* used as zero-arg or Graphics-arg method names on this class, on
// top of being static/instance field names -- the obfuscator reusing a
// letter for a field AND a method, same trap documented for Enemy/Player,
// just discovered mechanically here via a blanket rename + revert pass
// instead of manual review).
//
// Renamed (confirmed):
//  - class name, constructor
//  - midlet (a, ratchetandclank), levelMap (V, LevelMap), random (W,
//    java.util.Random), enemies (ai, Enemy[]), player (aj, Player),
//    playerProjectiles (ak, Projectile[]) / enemyProjectiles (al,
//    Projectile[]) -- TENTATIVE ordering/naming, by analogy with the
//    legacy build's confirmed Game.playerShots/enemyShots (../src/Game.java)
//    plus matching pool-creation order (ak allocated before al, same as
//    legacy's playerShots-before-enemyShots) and identical pool size (10)
//  - tileWidth/tileHeight/hudHeight (F/G/H, static) -- already independently
//    confirmed by Entity/Enemy/Player/Projectile/LevelMap each copying
//    these out of Game.F/G/H into their own same-named statics
//  - enemyPoolSize (ba, static, =10) -- confirmed via this.enemies = new
//    Enemy[ba] at the boot step, and Player.java/Projectile.java already
//    reference Game.ba as a pool-size loop bound
//  - sleep(int) (e(int), static) -- confirmed: try { Thread.sleep(var0); }
//    catch. Called from every long-running screen transition, and from
//    SoundPlayer.run()'s poll loop (SoundPlayer.java already has this
//    committed as the unrenamed `Game.e(30)` -- not fixed up here, see
//    "known follow-ups" below)
//  - abs(int) (d(int)) -- same single-line abs pattern as Player/Enemy's
//    own private copies
//  - runBootStep(int) throws Exception (a(int)) -- confirmed: a ~39-case
//    switch, one image/array-allocation step per case, each ending in a
//    call into IntroManager to advance its boot progress bar. This is
//    exactly the method IntroManager.java's header comment already
//    flagged ("Game.a(int) once per tick -- an initialization step
//    function") before Game's own phase-1 existed to confirm it
//  - writeSaveData(byte[]) / readSaveData(byte[]) (a(byte[]) / b(byte[]))
//    -- confirmed via their bodies exclusively calling
//    midlet.writeInt/writeShort/writeSoundAndLanguageSettings (write side)
//    or midlet.readInt/readShort (read side) at fixed buffer offsets
//  - pause() / resume() / render(Graphics) / tick() / keyPressed(int) /
//    keyReleased(int) (p/q/w(Graphics)/r/b(int)/c(int)) -- confirmed via
//    CanvasShell.java's already-committed dispatch calls
//    (.p()/.q()/.w(g)/.r()/.b(k)/.c(k) -- CanvasShell itself is NOT
//    touched here, see "known follow-ups"). tick() is the master
//    per-frame update: input-triggered level/menu transitions, the
//    ~33ms-gated main simulation step (camera, player, enemies, both
//    projectile pools, room-transition state), and the boss/end-of-level
//    dispatch by way of this.b (an obfuscated state byte -- TENTATIVE
//    "game/level screen state", left unrenamed, see below)
//  - updateCamera() (s()) -- confirmed via body: recomputes the static
//    x/y (camera scroll offset, already left obfuscated -- see below)
//    toward a player-centered target, clamped to level bounds
//
// Deliberately left obfuscated (this class's own dense internals -- same
// scoping call as IntroManager.java's own state machine, and the legacy
// build's own Game class): the ~150 remaining single/double-letter fields
// and methods, including the heavily-used state bytes `b` (game/level
// screen state -- TENTATIVE, drives most of tick()'s branching) and `Z`
// (TENTATIVE "current level id" -- already independently confirmed on the
// Enemy.java side to NOT be Entity.kind, see Enemy.java's bug-fix note),
// the camera offset statics x/y and their target-side flags z/A/B/C/D,
// the room/tile-effect tables cz/cA/cI/bw/bv, the moving-platform tables
// bg/bh/bi/bl/bm/bn, and the ~130 render/update helper methods (the
// Graphics-suffixed sequence e(Graphics) through w(Graphics) in
// particular looks like one render helper per HUD/menu element, mirroring
// IntroManager's own screen-drawing dispatch, but wasn't traced further).
//
// Known follow-ups (not done here, flagged for a future consistency pass):
//  - CanvasShell.java's 6 dispatch call sites (.p()/.q()/.w()/.r()/.b()/
//    .c()) and SoundPlayer.java's `Game.e(30)` still reference the
//    now-renamed pause/resume/render/tick/keyPressed/keyReleased/sleep by
//    their old obfuscated letters -- consistent with how Enemy.java was
//    never retroactively updated after Player.java renamed its own b()/
//    c() to x()/y(); left for a dedicated cross-file cleanup pass instead
//    of one-off touch-ups here
//  - ratchetandclank.java's own phase-1 is incomplete: this class calls
//    midlet.a(...) [3 arities, incl. one byte-arg = writeSoundAndLanguageSettings,
//    confirmed here], midlet.b(...) [2-arg = readShort, confirmed here],
//    midlet.c(int), midlet.d(int)/d(), midlet.e(int)/e(), midlet.k() --
//    none of the bare c/d/e/k call targets are in ratchetandclank.java's
//    already-documented method list, meaning that file's transcription
//    has more undocumented methods than just the earlier-found
//    playMenuLoopSound gap
//  - Projectile.java's header comment describes Game.e(int,int,int) as
//    enemy-shot-specific; that's a different `e` overload from the
//    sleep(int) confirmed here (arity distinguishes them), and remains
//    unconfirmed pending further tracing
public final class Game {
   public ratchetandclank midlet;
   public byte b;
   public byte c;
   public boolean d;
   public boolean e;
   public static boolean f = false;
   public boolean g;
   public byte h;
   public byte i;
   public byte j = 0;
   public byte k;
   public int l = 176;
   public boolean m;
   public byte n;
   public byte o;
   public byte p;
   public byte q;
   public boolean r;
   public byte s;
   public boolean t;
   public int u;
   public static short v;
   public static short w;
   public static int x = 0;
   public static int y = 0;
   public static boolean z = false;
   public static boolean A = false;
   public static boolean B = false;
   public static byte C;
   public static boolean D = false;
   public static int E = 0;
   public static byte tileWidth = 42;
   public static byte tileHeight = 28;
   public static byte hudHeight = 20;
   public int I;
   public static byte J = 44;
   public static byte K = 44;
   public static byte L = -16;
   public byte M;
   public static final byte[] N = new byte[]{6, 0, 0, 1, 2, 4, 5, 3, 13, 13, 9, 10, 9, 16, 12, 11, 8, 11, 19, 18};
   public static final byte[] O = new byte[]{2, 3, 4, 7, 5, 6, 0, 5, 16, 12, 11, 17, 14, 10, 15, 13, 13, 15, 4, 18};
   public static final byte[] P = new byte[]{19, 0, 18, 2, 12, 4, 5, 4, 7, 10, 3, 8, 11, 1, 15, 6, 6, 16, 9, 13};
   public static final byte[] Q = new byte[]{1, 13, 3, 10, 7, 7, 16, 8, 11, 18, 9, 12, 4, 19, 12, 14, 17, 14, 2, 0};
   public static final byte[] R = new byte[]{0, 2, 3, 5, 1, 7, 4, 6};
   public static final byte[] S = new byte[]{0, 4, 1, 2, 6, 3, 7, 5};
   public static final byte[] T = new byte[]{4, 3, 2, 3, 4, 4, 2, 3, 3, 2, 0, 0, 0};
   public int U = 0;
   public LevelMap levelMap;
   public Random random;
   public short X = -1;
   public short Y = -1;
   public byte Z = 1;
   public boolean aa = false;
   public byte ab = 1;
   public byte ac = -1;
   public byte ad;
   public byte ae;
   public short af;
   public short ag;
   public short ah = 0;
   public Enemy[] enemies;
   public Player player;
   public Projectile[] playerProjectiles;
   public Projectile[] enemyProjectiles;
   public static Image am;
   public static Image an;
   public static Image ao;
   public static Image ap;
   public static Image aq;
   public static Image ar;
   public static Image as;
   public static Image at;
   public static Image au;
   public static Image av;
   public static Image aw;
   public static Image ax;
   public static Image ay;
   public static Image az;
   public static Image aA;
   public static Image aB;
   public static Image aC;
   public static Image aD;
   public static Image aE;
   public static Image aF;
   public static Image[] aG;
   public static Image aH;
   public static Image aI;
   public static Image aJ;
   public static Image aK;
   public static Image aL;
   public static Image aM;
   public static Image aN;
   public static Image aO;
   public static Image aP;
   public byte aQ;
   public static final String[] aR = new String[]{"/bg_agclnk.png", "/bg_arena.png", "/bg_os.png", "/bg_menu.png"};
   public byte aS;
   public byte aT;
   public int aU;
   public byte aV;
   public byte aW;
   public int aX;
   public int aY;
   public int[] aZ;
   public static byte enemyPoolSize = 10;
   public static byte bb = enemyPoolSize;
   public short[] bc;
   public short[] bd;
   public short[] be;
   public short[] bf;
   public short[] bg;
   public int[] bh;
   public int[] bi;
   public short[] bj;
   public short[] bk;
   public short[] bl;
   public short[] bm;
   public byte[] bn;
   public short[] bo;
   public short[] bp;
   public short[] bq;
   public short[] br;
   public boolean[] bs;
   public boolean[] bt;
   public int[] bu;
   public int[] bv;
   public byte bw;
   public int bx;
   public int by;
   public byte bz;
   public int bA;
   public int bB;
   public byte[] bC;
   public byte[] bD;
   public byte[] bE;
   public byte[] bF;
   public byte[] bG;
   public byte[] bH;
   public byte[] bI;
   public int[] bJ;
   public byte bK = 0;
   public byte bL = 0;
   public byte bM = 0;
   public static final byte[] bN = new byte[]{14, 14, 13, 13};
   public static final byte[] bO = new byte[]{8, 9, 8, 9};
   public byte bP;
   public byte bQ;
   public static byte bR = 30;
   public static byte bS = 60;
   public int bT;
   public int bU;
   public int bV;
   public int bW;
   public byte bX;
   public byte bY;
   public boolean bZ = true;
   public byte ca;
   public byte cb;
   public byte cc;
   public byte cd;
   public byte ce;
   public byte cf;
   public int[] cg;
   public int[] ch;
   public short[] ci;
   public short[] cj;
   public byte[] ck;
   public static final int[] cl = new int[]{200, 400, 400, 600, 600, 600, 800, 800, 800, 1200, 1200, 2000};
   public short[] cm;
   public short[] cn;
   public short[] co;
   public short[] cp;
   public byte[] cq;
   public int cr;
   public long cs;
   public long ct;
   public byte cu;
   private byte ec;
   private byte ed = 0;
   private byte ee = 0;
   public int cv;
   public int cw = 0;
   public int cx = 0;
   public long cy = 0L;
   public static final byte[] cz = new byte[]{
      2, 4, 2, 13, 1, 1, 1, 1, 3, 1, 1, 1, 5, 4, 3, 3, 6, 5, 0, 0, 6, 1, 5, 1, 1, 1, 1, 1, 5, 1, 3, 7, 4, 3, 4, 3, 1, 1, 1, 1
   };
   public static final short[] cA = new short[]{
      104,
      106,
      110,
      112,
      125,
      126,
      127,
      128,
      129,
      132,
      133,
      134,
      135,
      140,
      144,
      147,
      150,
      156,
      0,
      0,
      161,
      167,
      168,
      173,
      174,
      175,
      176,
      177,
      178,
      183,
      184,
      187,
      194,
      199,
      202,
      206,
      209,
      209,
      210,
      211
   };
   public static final byte[] cB = new byte[]{
      1,
      0,
      1,
      0,
      1,
      0,
      1,
      0,
      1,
      2,
      1,
      2,
      0,
      1,
      2,
      0,
      1,
      2,
      1,
      0,
      2,
      -1,
      -1,
      -1,
      -1,
      0,
      1,
      0,
      -1,
      -1,
      0,
      0,
      1,
      0,
      1,
      0,
      1,
      0,
      1,
      0,
      0,
      1,
      0,
      0,
      1,
      0,
      1,
      2,
      1,
      0,
      2,
      -1,
      1,
      0,
      2,
      0,
      1,
      0,
      1,
      0,
      1,
      0,
      1,
      -1,
      1,
      0,
      1,
      0,
      1,
      -1,
      -1,
      -1,
      -1,
      -1,
      0,
      1,
      0,
      1,
      0,
      -1,
      4,
      0,
      4,
      3,
      0,
      3,
      0,
      3,
      1,
      3,
      0,
      1,
      0,
      1,
      -1,
      0,
      1,
      0,
      0,
      1,
      0,
      1,
      0,
      1,
      0,
      -1,
      -1,
      0,
      1
   };
   public boolean cC = false;
   public boolean cD = false;
   public String cE;
   public int cF = -1;
   public int cG = -1;
   public int cH;
   public byte cI = 0;
   public int cJ;
   public int cK;
   public int cL;
   public int cM;
   public int cN;
   public int cO;
   public short cP;
   public byte[] cQ = new byte[]{1, 1, 2, 2, 3, 3, 3, 4, 4, 5, 6, 6, 7, 8, 9, 9, 10, 12, 11, 13};
   public short[] cR = new short[]{0, 2, 0, 1, 0, 2, 4, 0, 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0};
   public boolean cS = false;
   public boolean cT = false;
   public byte cU;
   public boolean cV = false;
   public boolean cW = false;
   public boolean cX = false;
   public byte cY;
   public boolean cZ = false;
   public byte da;
   public byte db;
   public int[] dc;
   public short[][] dd;
   public byte[][] de;
   public final byte[] df = new byte[]{2, 18, 4, 7, 5, 1, 13, 10, 9, 12, 6, 15, 11, 8, 16, 17};
   public final byte[] dg = new byte[]{1, 1, 2, 1, 3, 8, 1, 2, 3, 3, 6, 2, 5, 2, 1, 2};
   public final byte[][] dh = new byte[][]{
      {0},
      {1},
      {2, 3},
      {4},
      {5, 6, 7},
      {8, 9, 10, 11, 12, 13, 14, 15},
      {16},
      {17, 18},
      {19, 20, 21},
      {22, 23, 24},
      {25, 26, 27, 28, 29, 30},
      {31, 32},
      {33, 34, 35, 36, 37},
      {38, 39},
      {40},
      {41, 42}
   };
   public int di;
   public byte dj;
   public static byte dk;
   public int dl;
   public static int dm;
   public static int dn;
   public static int do;
   public static int dp;
   public static int dq;
   public static int dr;
   public static int ds;
   public static int dt;
   public static int du;
   public static int dv;
   public static long dw;
   public static final long[] dx = new long[]{
      160000L, 120000L, 160000L, 130000L, 160000L, 130000L, 100000L, 140000L, 220000L, 180000L, 180000L, 110000L, 140000L, 140000L, 300000L, 150000L, 30000L
   };
   public static byte dy;
   public static int dz;
   public static int dA;
   public static byte dB;
   public int dC;
   public static int dD;
   public static int dE;
   public static boolean dF;
   public static boolean dG;
   public static boolean dH;
   public static boolean dI;
   public static int[] dJ;
   public static int[] dK;
   public static boolean[] dL;
   public static final short[][] dM = new short[][]{{24, 62}, {70, 42}, {117, 62}, {11, 112}, {128, 112}, {43, 154}, {97, 154}};
   public static final byte[] dN = new byte[]{0, 0, 10, 26, 36, 36, 26, 10};
   public static final byte[] dO = new byte[]{26, 10, 0, 0, 10, 26, 36, 36};
   public static final byte[] dP = new byte[]{3, 3, 10, 26, 33, 33, 26, 10};
   public static final byte[] dQ = new byte[]{26, 10, 3, 3, 10, 26, 33, 33};
   public static final byte[] dR = new byte[]{2, 2, 11, 26, 35, 35, 26, 11};
   public static final byte[] dS = new byte[]{25, 11, 2, 2, 11, 25, 34, 34};
   private int[] ef;
   private int[] eg;
   public int dT = -1;
   public int dU = -1;
   public boolean dV = false;
   public long dW = 0L;
   public int dX = -1;
   public int dY;
   public boolean dZ = false;
   public int ea;
   public boolean eb;

   public final void a(int var1, int var2) {
      for (int var3 = 0; var3 < 8; var3++) {
         if (this.bj[var3] == -1) {
            this.bj[var3] = (short)(var1 * tileWidth + (tileWidth >> 1));
            this.bk[var3] = (short)(var2 * tileHeight + (tileHeight >> 1));
            return;
         }
      }
   }

   public final void a(Graphics var1) {
      var1.setClip(0, hudHeight, 176, 220 - hudHeight);

      for (int var4 = 0; var4 < 8; var4++) {
         if (this.bj[var4] == -1) {
            return;
         }

         int var2 = this.bj[var4] + x;
         int var3 = this.bk[var4] + y;
         if (var2 < 176 && var3 < 220 && var2 + 22 >= 0 && var3 + 22 >= 0) {
            var1.drawImage(au, var2, var3, 3);
         }
      }
   }

   public final void a(Graphics var1, int var2, int var3, int var4, int var5) {
      if (var2 < 0) {
         var4 -= -var2;
         var2 = 0;
      }

      if (var3 < 0) {
         var5 -= -var3;
         var3 = 0;
      }

      if (var2 + var4 > 176) {
         var4 = 176 - var2;
      }

      if (var3 + var5 > 220) {
         var5 = 220 - var3;
      }

      var1.setClip(var2, var3, var4, var5);
   }

   public final void b(Graphics var1, int var2, int var3, int var4, int var5) {
      if (var2 < 0) {
         var4 -= -var2;
         var2 = 0;
      }

      if (var3 < hudHeight) {
         var5 -= hudHeight - var3;
         var3 = hudHeight;
      }

      if (var2 + var4 > 176) {
         var4 = 176 - var2;
      }

      if (var3 + var5 > 220) {
         var5 = 220 - var3;
      }

      var1.setClip(var2, var3, var4, var5);
   }

   public final void a() {
      short var1 = this.player.b();
      int var2 = this.player.c() + L;
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;
      int var8 = au.getWidth() >> 1;

      for (int var7 = 0; var7 < 8; var7++) {
         if (this.bj[var7] == -1) {
            return;
         }

         short var9 = this.bj[var7];
         short var10 = this.bk[var7];
         if (this.a(var1 - 11, var2 + 7, 18, 37, var9 - var8 + 10, var10 - var8, (var8 << 1) - 20, var8 << 1)) {
            this.player.ad = 0;
            this.player.ac = (short)(-b.n);
            this.player.a((byte)3);
            this.player.t = 10;
            this.player.s = 0;
            return;
         }
      }
   }

   public final void b(Graphics var1) {
      if (this.bX != -1) {
         boolean var2 = false;
         boolean var3 = false;
         int var4 = this.bX * tileWidth + x + (tileWidth - 19 >> 1);
         int var5 = this.bY * tileHeight + y + (tileHeight - 19 >> 1);
         if (var4 < 176 && var4 + 19 >= 0 && var5 < 220 && var5 + 19 >= 0) {
            boolean var6 = false;
            this.b(var1, var4, var5, 19, 19);
            var1.drawImage(aL, var4, var5 - 475 - 0, 0);
         }
      }
   }

   public final void b() {
      if (this.bX != -1) {
         boolean var1 = false;
         boolean var2 = false;
         int var3 = this.bX * tileWidth;
         int var4 = this.bY * tileHeight;
         if (this.a(this.player.b() - 11, this.player.c() + L + 7, 18, 37, var3, var4, 19, 19)) {
            this.bX = this.bY = -1;
            this.bZ = false;
            this.cC = true;
            this.cI = cz[34];
            this.r(cA[34]);
         }
      }
   }

   public final void c(Graphics var1) {
      if (this.cc != -1) {
         int var2 = ax.getWidth();
         int var3 = ax.getHeight() / 2;
         byte var4 = L;
         int var5 = this.cc * tileWidth + x;
         int var6 = this.cd * tileHeight + y + var4;
         if (var5 < 176 && var5 + var2 >= 0 && var6 < 220 && var6 + var3 >= 0) {
            boolean var7 = false;
            if (0 < var3) {
               this.b(var1, var5, var6, var2, var3 - 0);
               var1.drawImage(ax, var5, var6 - this.ce * var3 - 0, 0);
            }
         }
      }
   }

   public final void c() {
      this.cf--;
      if (this.cf == 0) {
         this.cf = 36;
      }

      if (this.ce == 1) {
         if (this.cf % 18 == 0) {
            this.ce = 0;
            return;
         }
      } else if (this.cf % 36 == 0) {
         this.ce = 1;
      }
   }

   public final void d() {
      dp = 10000;
      dn = 0;
      dm = 0;
      do = 0;
      du = 0;
      dq = 0;
      dr = 0;
      dv = 0;
      dw = 0L;
      dz = 0;
      dB = 0;
      dD = 0;
      dE = 0;
      dF = false;
      dG = false;
      dI = false;
      dH = false;
      this.f();
   }

   public final int e() {
      int var1 = 0;

      for (int var2 = this.enemies.length - 1; var2 >= 0; var2--) {
         if (this.enemies[var2].Z != 4 && this.enemies[var2].Z != -1) {
            var1++;
         }
      }

      return var1;
   }

   public final void b(int var1, int var2) {
      dJ[var1] = this.e();
   }

   public final void f() {
      for (int var1 = dJ.length - 1; var1 >= 0; var1--) {
         dL[var1] = false;
         dJ[var1] = 0;
         dK[var1] = 0;
      }
   }

   public static final void a(byte var0) {
      switch (var0) {
         case 0:
            return;
         case 1:
            if (!dI && dD != 0 && dD == dE) {
               dF = true;
            }

            if (do == 0) {
               dG = true;
            }

            for (int var1 = dJ.length - 1; var1 >= 0; var1--) {
               if (dJ[var1] > 0) {
                  dn = dn + dJ[var1];
               }
            }

            if (du == do && do > 0 && dn == 0) {
               dH = true;
            }

            dp = do * 100;
            dz = dz + dp;
            ds = dq * 1;
            dt = dr * 1000;
            dz = dz + ds;
            dz = dz + dt;
            if (dw < dx[dy]) {
               dv = (int)((dx[dy] - dw) / 1000L * 10L);
            }

            dz = dz + dv;
            return;
         case 2:
            if (dG) {
               dz += 100000;
            }

            if (dF) {
               dz += 100000;
            }

            if (dH) {
               dz += 10000;
            }
      }
   }

   public Game(ratchetandclank var1) {
      this.midlet = var1;
      Display.getDisplay(this.midlet);
   }

   public final void runBootStep(int var1) throws Exception {
      switch (var1) {
         case 4:
            try {
               ratchetandclank.smallFont = new Font("/f2.v", 10, 1);
               ratchetandclank.smallFontAlias = ratchetandclank.smallFont;
               ratchetandclank.largeFont = new Font("/f3.v", 13, 1);
               ratchetandclank.currentFont = ratchetandclank.smallFontAlias;
            } catch (IOException var3) {
            }

            this.midlet.introManager.d(2);
            return;
         case 5:
            this.random = new Random();
            dk = 0;
            aG = new Image[5];
            aG[0] = Image.createImage("/en_disdro.png");
            this.midlet.introManager.d(2);
            return;
         case 6:
            aG[1] = Image.createImage("/en_micbot.png");
            this.midlet.introManager.d(2);
            return;
         case 7:
            aG[2] = Image.createImage("/en_patbot.png");
            this.midlet.introManager.d(2);
            return;
         case 8:
            aG[3] = Image.createImage("/en_turret.png");
            this.midlet.introManager.d(2);
            return;
         case 9:
            aG[4] = Image.createImage("/en_boar.png");
            this.midlet.introManager.d(2);
            return;
         case 10:
            am = Image.createImage("/arrows.png");
            at = Image.createImage("/prtrts.png");
            this.midlet.introManager.d(2);
            return;
         case 11:
            an = Image.createImage("/hud.png");
            this.midlet.introManager.d(2);
            return;
         case 12:
            aq = Image.createImage("/wpnhud.png");
            this.midlet.introManager.d(2);
            return;
         case 13:
            ap = Image.createImage("/menuhl.png");
            this.midlet.introManager.d(2);
            return;
         case 14:
            ao = Image.createImage("/diabox.png");
            this.midlet.introManager.d(2);
            return;
         case 15:
            aA = Image.createImage("/bltctr.png");
            this.midlet.introManager.d(2);
            return;
         case 16:
            aE = Image.createImage("/icons.png");
            this.midlet.introManager.d(2);
            return;
         case 17:
            aC = Image.createImage("/doors.png");
            this.midlet.introManager.d(2);
            return;
         case 18:
            aM = Image.createImage("/explod.png");
            this.midlet.introManager.d(2);
            return;
         case 19:
            aJ = Image.createImage("/box.png");
            this.midlet.introManager.d(2);
            return;
         case 20:
            aI = Image.createImage("/weapon.png");
            this.midlet.introManager.d(2);
            return;
         case 21:
            aL = Image.createImage("/bolts.png");
            this.midlet.introManager.d(2);
            return;
         case 22:
            aD = Image.createImage("/pltfrm.png");
            this.midlet.introManager.d(2);
            return;
         case 23:
            aK = Image.createImage("/strtpt.png");
            this.midlet.introManager.d(2);
            return;
         case 24:
            aN = Image.createImage("/cnnbse.png");
            this.midlet.introManager.d(2);
            return;
         case 25:
            aO = Image.createImage("/cnnprj.png");
            this.midlet.introManager.d(2);
            return;
         case 26:
            aP = Image.createImage("/cnnbrl.png");
            this.midlet.introManager.d(2);
            return;
         case 27:
            av = Image.createImage("/maxmil.png");
            this.midlet.introManager.d(2);
            return;
         case 28:
            ar = Image.createImage("/mpicns.png");
            this.midlet.introManager.d(2);
            return;
         case 29:
            ay = Image.createImage("/flmbot.png");
            this.midlet.introManager.d(2);
            return;
         case 30:
            this.midlet.introManager.d(2);
            return;
         case 31:
            aB = Image.createImage("/wpnmnu.png");
            this.midlet.introManager.d(2);
            return;
         case 32:
            au = Image.createImage("/spike.png");
            this.midlet.introManager.d(2);
            return;
         case 33:
            aw = Image.createImage("/payola.png");
            this.midlet.introManager.d(2);
            return;
         case 34:
            ax = Image.createImage("/bncbot.png");
            this.midlet.introManager.d(2);
            return;
         case 35:
            as = Image.createImage("/menuhd.png");
            this.midlet.introManager.d(2);
            return;
         case 36:
            aH = Image.createImage("/ratcht.png");
            this.midlet.introManager.d(2);
            return;
         case 37:
            aF = Image.createImage(aR[3]);
            this.midlet.introManager.d(2);
            return;
         case 38:
            this.I = 0;

            while (this.I < 220 - tileHeight * 2) {
               this.I = this.I + tileHeight;
            }

            this.I += 12;
            this.r = false;
            dA = 0;
            v = (short)(28 * -tileWidth + 176);
            w = (short)(18 * -tileHeight + 220);
            if (10 > hudHeight) {
               hudHeight = 16;
            }

            this.levelMap = new LevelMap(this);
            this.midlet.introManager.d(7);
            return;
         case 39:
            this.player = new Player(this);
            this.player.a();
            this.midlet.introManager.d(5);
            return;
         case 40:
            this.enemies = new Enemy[enemyPoolSize];

            for (byte var2 = (byte)(enemyPoolSize - 1); var2 >= 0; var2--) {
               this.enemies[var2] = new Enemy(this);
            }

            this.enemies[0].a();
            this.midlet.introManager.d(5);
            this.playerProjectiles = new Projectile[10];

            for (int var4 = 9; var4 >= 0; var4--) {
               this.playerProjectiles[var4] = new Projectile(this);
            }

            this.enemyProjectiles = new Projectile[10];

            for (int var5 = 9; var5 >= 0; var5--) {
               this.enemyProjectiles[var5] = new Projectile(this);
            }

            this.midlet.introManager.d(2);
            return;
         case 41:
            this.bH = new byte[4];
            this.bI = new byte[4];
            this.bJ = new int[5];
            this.bF = new byte[3];
            this.bG = new byte[3];
            this.bC = new byte[3];
            this.bD = new byte[3];
            this.bE = new byte[3];
            this.bc = new short[4];
            this.bd = new short[4];
            this.be = new short[4];
            this.bf = new short[4];
            this.bg = new short[4];
            this.bh = new int[4];
            this.bi = new int[4];
            this.bj = new short[8];
            this.bk = new short[8];
            this.bl = new short[50];
            this.bm = new short[50];
            this.bn = new byte[50];
            this.bo = new short[50];
            this.bp = new short[50];
            this.bu = new int[10];
            this.bq = new short[50];
            this.bs = new boolean[50];
            this.br = new short[50];
            this.bt = new boolean[6];
            this.bv = new int[2];
            this.cg = new int[12];
            this.ch = new int[12];
            this.ck = new byte[12];
            this.ci = new short[12];
            this.cj = new short[12];
            this.cm = new short[4];
            this.cn = new short[4];
            this.co = new short[4];
            this.cp = new short[4];
            this.cq = new byte[4];
            dJ = new int[12];
            dK = new int[12];
            dL = new boolean[12];
            this.dc = new int[13];
            this.dd = new short[62][3];
            this.de = new byte[43][2];
            this.aZ = new int[8];
            this.midlet.introManager.d(5);
            return;
         case 42:
            this.t();
            this.aQ = 3;
            this.midlet.introManager.d(5);
      }
   }

   public final void keyPressed(int var1) {
      if (!this.m) {
         if (this.U <= 0) {
            if (this.dW <= 0L || System.currentTimeMillis() - this.dW >= 1000L) {
               int var2 = 0;
               if (var1 == 50) {
                  var2 = -1;
               } else if (var1 == 56) {
                  var2 = -2;
               } else if (var1 == 52) {
                  var2 = -3;
               } else if (var1 == 54) {
                  var2 = -4;
               } else if (var1 == 53) {
                  var2 = -5;
               } else {
                  var2 = var1;
               }

               this.d = true;
               switch (this.b) {
                  case 0:
                  case 16:
                  case 17:
                  case 24:
                     this.A(var1, var2);
                     return;
                  case 1:
                     this.t(var1, var2);
                     return;
                  case 2:
                     this.q(var1, var2);
                     return;
                  case 3:
                     this.m(var1, var2);
                     return;
                  case 4:
                     this.h(var1, var2);
                     return;
                  case 5:
                     this.i(var1, var2);
                     return;
                  case 6:
                     this.j(var1, var2);
                     return;
                  case 7:
                     this.z(var1, var2);
                     return;
                  case 8:
                     this.y(var1, var2);
                     return;
                  case 9:
                     this.w(var1, var2);
                     return;
                  case 10:
                     this.o(var1, var2);
                     return;
                  case 11:
                     this.k(var1, var2);
                     return;
                  case 12:
                     this.l(var1, var2);
                     return;
                  case 13:
                     this.p(var1, var2);
                     return;
                  case 14:
                     this.s(var1, var2);
                     return;
                  case 15:
                  case 19:
                     this.r(var1, var2);
                     return;
                  case 18:
                     this.n(var1, var2);
                     return;
                  case 21:
                     this.x(var1, var2);
                     return;
                  case 22:
                     this.v(var1, var2);
                     return;
                  case 23:
                     this.u(var1, var2);
                  case 20:
               }
            }
         }
      }
   }

   public final void keyReleased(int var1) {
      if (!this.m) {
         if (this.U <= 0) {
            int var2 = 0;
            if (var1 == 50) {
               var1 = -1;
            } else if (var1 == 52 || var1 == 49) {
               var1 = -3;
            } else if (var1 == 54 || var1 == 51) {
               var1 = -4;
            } else if (var1 == 56) {
               var1 = -2;
            }

            var2 = var1;
            this.B(var1, var2);
         }
      }
   }

   public final void d(Graphics var1) {
      ratchetandclank.largeFont.a(ratchetandclank.strings[7]);
      boolean var3 = false;
      ratchetandclank.largeFont.a(ratchetandclank.strings[8]);
      boolean var4 = false;
      this.midlet.introManager.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      int var2 = this.midlet.introManager.a(var1, ratchetandclank.strings[38]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      var2 += ratchetandclank.smallFont.a;
      int var6;
      this.dc[0] = var6 = ap.getHeight() / 2 + var2;
      this.dc[1] = var2 = ap.getHeight() / 2 + this.midlet.introManager.a(var1, ratchetandclank.strings[39], tileWidth + (tileWidth >> 1), var6, 0, this.cu == 0);
      int var8;
      this.dc[2] = var8 = ap.getHeight() / 2 + this.midlet.introManager.a(var1, ratchetandclank.strings[3], tileWidth + (tileWidth >> 1), var2, 0, this.cu == 1);
      this.dc[3] = var2 = ap.getHeight() / 2 + this.midlet.introManager.a(var1, ratchetandclank.strings[41], tileWidth + (tileWidth >> 1), var8, 0, this.cu == 2);
      this.midlet.introManager.a(var1, ratchetandclank.strings[42], tileWidth + (tileWidth >> 1), var2, 0, this.cu == 3);
      this.midlet.introManager.a(var1, 7, 8, this);
      this.midlet.introManager.a(var1, tileWidth >> 1, this.dc[this.cu]);
      var1.setColor(255, 255, 255);
      this.a(var1, ratchetandclank.strings[213 + this.o], this.I - 10, 20, 156);
   }

   public final int g() {
      return aq.getWidth();
   }

   public final int h() {
      return aq.getHeight();
   }

   public final int i() {
      return aq.getWidth() - 20;
   }

   public final int j() {
      return 176 - aq.getWidth() >> 1;
   }

   public final int k() {
      return 75;
   }

   public final void e(Graphics var1) {
      var1.drawImage(aq, this.j(), this.k(), 0);
   }

   public final void f(Graphics var1) {
      this.midlet.introManager.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.sleep(var1);
      this.midlet.introManager.a(var1, ratchetandclank.strings[27]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      this.dc[0] = 100;
      this.midlet.introManager.a(var1, this.midlet.soundEnabled ? ratchetandclank.strings[28] : ratchetandclank.strings[29], tileWidth + (tileWidth >> 1), 100, 0, this.cu == 0);
      this.midlet.introManager.a(var1, 7, 8, this);
      this.midlet.introManager.a(var1, tileWidth >> 1, this.dc[this.cu]);
   }

   public final void g(Graphics var1) {
      this.midlet.introManager.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.sleep(var1);
      this.midlet.introManager.a(var1, ratchetandclank.strings[283]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      this.dc[0] = 104;
      int var2;
      this.dc[1] = var2 = 4 + this.midlet.introManager.a(var1, (byte)0, tileWidth + (tileWidth >> 1), 104, 0, this.cu == 0);
      int var3;
      this.dc[2] = var3 = 4 + this.midlet.introManager.a(var1, (byte)1, tileWidth + (tileWidth >> 1), var2, 0, this.cu == 1);
      this.midlet.introManager.a(var1, (byte)2, tileWidth + (tileWidth >> 1), var3, 0, this.cu == 2);
      this.midlet.introManager.a(var1, 7, 8, this);
      this.midlet.introManager.a(var1, tileWidth >> 1, this.dc[this.cu]);
   }

   public final void h(Graphics var1) {
      int var2 = 0;
      this.midlet.introManager.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.sleep(var1);
      this.midlet.introManager.a(var1, ratchetandclank.strings[47]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      if (this.ec != 7) {
         this.player.L = (byte)(this.ec + 1);
      }

      this.player.a(var1, 0, 0, -20);
      var1.setClip(0, 0, 176, 220);
      if (this.ec == 7) {
         var2 = this.midlet.introManager.a(var1, ratchetandclank.strings[58], tileWidth + (tileWidth >> 1), 85, 0, false);
         var2 = this.midlet.introManager.a(var1, ratchetandclank.strings[295], tileWidth + (tileWidth >> 1), var2, 0, true);
         var2 = this.midlet.introManager.a(var1, 56, this.aX, tileWidth + (tileWidth >> 1), var2, 0, false);
         var2 = this.midlet.introManager.a(var1, 57, this.player.Q, tileWidth + (tileWidth >> 1), var2, 0, false);
      } else if ((this.player.O & 1 << this.ec + 1) != 0) {
         var2 = this.midlet.introManager.a(var1, ratchetandclank.strings[58], tileWidth + (tileWidth >> 1), 85, 0, false);
         var2 = this.midlet.introManager.a(var1, ratchetandclank.strings[49 + this.ec], tileWidth + (tileWidth >> 1), var2, 0, true);
         var2 = this.midlet.introManager.a(var1, 56, this.aX, tileWidth + (tileWidth >> 1), var2, 0, false);
         var2 = this.midlet.introManager.a(var1, 57, this.aZ[this.ec + 1], tileWidth + (tileWidth >> 1), var2, 0, false);
      } else {
         var2 = 85 + ratchetandclank.currentFont.a;
         var2 = this.midlet.introManager.a(var1, ratchetandclank.strings[49 + this.ec], tileWidth + (tileWidth >> 1), var2, 0, true);
         var2 = this.midlet.introManager.a(var1, 56, this.aX, tileWidth + (tileWidth >> 1), var2, 0, false);
         var2 = this.midlet.introManager.a(var1, 57, b.h[this.ec], tileWidth + (tileWidth >> 1), var2, 0, false);
      }

      int var13;
      this.dc[0] = var13 = var2 + ratchetandclank.currentFont.a / 2;
      this.dc[1] = var2 = this.midlet.introManager.a(var1, ratchetandclank.strings[11], tileWidth + (tileWidth >> 1), var13, 0, this.cu == 0) + 3;
      this.midlet.introManager.a(var1, ratchetandclank.strings[10], tileWidth + (tileWidth >> 1), var2, 0, this.cu == 1);
      ratchetandclank.currentFont = ratchetandclank.smallFontAlias;
      this.midlet.introManager.a(var1, tileWidth + (tileWidth >> 1), this.dc[this.cu]);
      this.midlet.introManager.a(var1, 7, 8, this);
   }

   public final void i(Graphics var1) {
      int var2 = 0;
      int var3 = 0;
      int var4 = aE.getHeight() / 12;
      int var5 = aE.getWidth();
      int var6 = 52;
      boolean var7 = false;
      this.midlet.introManager.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.midlet.introManager.a(var1, ratchetandclank.strings[26]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      this.sleep(var1);
      var2 = 85 + var4 + 4;
      if (this.cu != 7) {
         this.player.L = (byte)(this.cu + 1);
      }

      this.player.a(var1, 0, 0, -20);
      var1.setClip(0, 0, 176, 220);
      if ((this.cu != 2 || !this.h(3) || !this.h(8)) && (this.cu != 3 || !this.h(8))) {
         if (this.cu == 7) {
            var2 = this.midlet.introManager.a(var1, ratchetandclank.strings[58], tileWidth + (tileWidth >> 1), var2, 0, false);
            var2 = this.midlet.introManager.a(var1, ratchetandclank.strings[295], tileWidth + (tileWidth >> 1), var2, 0, true);
            var2 = this.midlet.introManager.a(var1, 56, this.aX, tileWidth + (tileWidth >> 1), var2, 0, false);
            var2 = this.midlet.introManager.a(var1, 57, this.player.Q, tileWidth + (tileWidth >> 1), var2, 0, false);
            if (this.player.Q <= 0) {
               this.midlet.introManager.a(var1, ratchetandclank.strings[257], tileWidth + (tileWidth >> 1), var2, 0, false);
            }
         } else if ((this.player.O & 1 << this.cu + 1) > 0) {
            var2 = this.midlet.introManager.a(var1, ratchetandclank.strings[58], tileWidth + (tileWidth >> 1), var2, 0, false);
            var2 = this.midlet.introManager.a(var1, ratchetandclank.strings[49 + this.cu], tileWidth + (tileWidth >> 1), var2, 0, true);
            var2 = this.midlet.introManager.a(var1, 56, this.aX, tileWidth + (tileWidth >> 1), var2, 0, false);
            var2 = this.midlet.introManager.a(var1, 57, this.aZ[this.cu + 1], tileWidth + (tileWidth >> 1), var2, 0, false);
            if (this.player.M[this.cu + 1] < b.d[3 * (this.cu + 1) + this.player.P[this.cu + 1]]) {
               Object[] var27 = new Object[]{new Integer(this.player.M[this.cu + 1]), new Integer(b.d[3 * (this.cu + 1) + this.player.P[this.cu + 1]])};
               String var29 = this.a(ratchetandclank.strings[255], var27);
               this.midlet.introManager.a(var1, var29, tileWidth + (tileWidth >> 1), var2, 0, false);
            } else {
               this.midlet.introManager.a(var1, ratchetandclank.strings[257], tileWidth + (tileWidth >> 1), var2, 0, false);
            }
         } else {
            var2 += 15;
            var2 = this.midlet.introManager.a(var1, ratchetandclank.strings[49 + this.cu], tileWidth + (tileWidth >> 1), var2, 0, true);
            var2 = this.midlet.introManager.a(var1, 56, this.aX, tileWidth + (tileWidth >> 1), var2, 0, false);
            this.midlet.introManager.a(var1, 57, b.h[this.cu], tileWidth + (tileWidth >> 1), var2, 0, false);
         }
      } else {
         var2 += 15;
         ratchetandclank.currentFont = ratchetandclank.smallFontAlias;
         var1.setColor(12632256);
         var2 = this.a(var1, ratchetandclank.strings[49 + this.cu], 88, var2, 17, this.i());
         ratchetandclank.currentFont = ratchetandclank.smallFont;
         Object[] var8 = new Object[]{new Integer(this.aX)};
         String var9 = this.a(ratchetandclank.strings[56], var8);
         var2 = this.a(var1, var9, 88, var2, 17, this.i());
         this.a(var1, ratchetandclank.strings[253], 88, var2, 17, this.i());
      }

      if ((var3 = this.cu + 1) == 7) {
         var3--;
         if ((this.player.O & 32) == 0) {
            var3--;
         }
      }

      if (this.cu == 7) {
         var3--;
         if ((this.player.O & 128) == 0) {
            var3--;
         }

         if ((this.player.O & 32) == 0) {
            var3--;
         }
      }

      this.midlet.introManager.b(var1, var3, 5 + ((this.player.O & 32) > 0 ? 1 : 0) + ((this.player.O & 128) > 0 ? 1 : 0));
      this.midlet.introManager.a(var1, 254, 8, this);
      var1.setClip(0, 85, 176, var4);
      if (this.dj > 6) {
         e.b.fillTriangle(42, 86, 37, 85 + (var4 >> 1), 42, 85 + var4 - 1, -14894388);
         e.b.drawTriangle(43, 85, 36, 85 + (var4 >> 1), 43, 85 + var4, -14581353);
      }

      for (int var28 = 0; var28 < 4 + ((this.player.O & 32) > 0 ? 1 : 0); var28++) {
         var1.drawImage(aE, var6, 85 - var28 * var4, 0);
         var6 += var5;
      }

      if ((this.player.O & 32) <= 0) {
         var6 += var5;
      }

      if ((this.player.O & 128) > 0) {
         var1.drawImage(aE, var6, 85 - 6 * var4, 0);
      }

      var6 += var5;
      var1.drawImage(aE, var6, 85 - 11 * var4, 0);
      if (this.dj > 6) {
         e.b.fillTriangle(var6 + var5 + 9, 86, var6 + var5 + 9, 85 + var4, var6 + var5 + 15, 85 + (var4 >> 1), -14894388);
         e.b.drawTriangle(var6 + var5 + 9, 85, var6 + var5 + 9, 85 + var4, var6 + var5 + 16, 85 + (var4 >> 1), -14581353);
      }

      var1.setColor(16777215);
      if (this.cu > 5) {
         var1.drawRect(52 + (this.cu - 1) * var5, 86, var5, var4 - 2);
      } else {
         var1.drawRect(52 + this.cu * var5, 86, var5, var4 - 2);
      }
   }

   public final void a(Graphics var1, byte var2) {
      int var3 = 0;
      boolean var4 = false;
      boolean var5 = false;
      this.midlet.introManager.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.sleep(var1);
      this.midlet.introManager.a(var1, ratchetandclank.strings[296]);
      var1.setColor(16777215);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      var1.getFont();
      var3 = 87;
      switch (var2) {
         case 0:
            this.a(var1, ratchetandclank.strings[this.dC], 18, 107, 17, this.i());
            break;
         case 1:
            var1.setClip(0, 0, 176, 220);
            var3 = this.a(var1, ratchetandclank.strings[297] + " " + do, 88, 85, 17, this.i());
            var3 = this.a(var1, ratchetandclank.strings[298] + " " + dq, 88, var3, 17, this.i());
            var3 = this.a(var1, ratchetandclank.strings[299] + " " + dr, 88, var3, 17, this.i());
            var3 = this.a(var1, ratchetandclank.strings[300] + " " + this.midlet.introManager.c((int)dw), 88, var3, 17, this.i());
            this.a(var1, ratchetandclank.strings[301] + " " + this.midlet.introManager.c((int)dx[dy]), 88, var3, 17, this.i());
            break;
         case 2:
            if (dG) {
               var3 = this.midlet.introManager.a(var1, ratchetandclank.strings[302], 88, 87, 17, false);
            }

            if (dF) {
               var3 = this.midlet.introManager.a(var1, ratchetandclank.strings[303], 88, var3, 17, false);
            }

            if (dH) {
               this.midlet.introManager.a(var1, ratchetandclank.strings[304], 88, var3, 17, false);
            }
      }

      if (var2 > 0) {
         this.midlet.introManager.a(var1, ratchetandclank.strings[305] + " " + dz, 88, 166, 17, true);
      }

      this.midlet.introManager.a(var1, 9, -1, this);
   }

   public final void j(Graphics var1) {
      try {
         this.midlet.introManager.a(var1, (byte)0);
         var1.setClip(0, 0, 176, 220);
         this.midlet.introManager.a(var1, ratchetandclank.strings[12]);
         int var9 = Math.min(176, 220) * 1024 / 176;
         boolean var10 = false;
         boolean var11 = false;
         short var12 = -1000;
         short var13 = 1000;

         for (int var4 = 0; var4 <= 19; var4++) {
            if (var13 > this.dd[var4][2]) {
               var13 = this.dd[var4][2];
            }

            if (var12 < this.dd[var4][2]) {
               var12 = this.dd[var4][2];
            }
         }

         int var14 = (205 - (var12 - var13) * var9 / 1024) / 2 - 3 * var13 * var9 / 1024 / 2;
         var1.drawImage(ao, (176 - ao.getWidth()) / 2, 220 - this.midlet.introManager.a() - ao.getHeight() + 8, 0);
         this.midlet.introManager.a(var1, ratchetandclank.strings[14 + this.cQ[this.cu] - 1], (176 - ao.getWidth()) / 2, 220 - this.midlet.introManager.a() - ao.getHeight() + 12, 0, true);

         for (int var22 = 0; var22 < 16; var22++) {
            if ((this.cL & 1 << this.df[var22]) != 0) {
               for (int var6 = 0; var6 < this.dg[var22]; var6++) {
                  byte var2 = this.de[this.dh[var22][var6]][0];
                  byte var3 = this.de[this.dh[var22][var6]][1];
                  int var15 = this.dd[var2][1] * var9 / 1024 + 0;
                  int var16 = this.dd[var2][2] * var9 / 1024 + var14;
                  int var17 = this.dd[var3][1] * var9 / 1024 + 0;
                  int var18 = this.dd[var3][2] * var9 / 1024 + var14;
                  var1.setColor(20, 186, 204);
                  var1.drawLine(var15, var16, var17, var18);
                  var1.setColor(36, 86, 100);
                  if (this.dd[var2][1] == this.dd[var3][1]) {
                     var1.drawLine(var15 - 1, var16, var17 - 1, var18);
                     var1.drawLine(var15 + 1, var16, var17 + 1, var18);
                  } else {
                     var1.drawLine(var15, var16 - 1, var17, var18 - 1);
                     var1.drawLine(var15, var16 + 1, var17, var18 + 1);
                  }
               }
            }
         }

         for (int var23 = 0; var23 <= 19; var23++) {
            short var5 = this.dd[var23][0];
            int var20 = this.dd[var23][1] * var9 / 1024 + 0;
            int var21 = this.dd[var23][2] * var9 / 1024 + var14;
            if (var5 >= 0 && var5 <= 16 || (var5 >= 17 && var5 <= 18 || var5 == 19 && this.cL != 1572865) && (this.cL & 1 << var5) > 0) {
               int var24 = 1;
               if (var5 >= 0 && var5 <= 16 && (this.cL & 1 << var5) > 0) {
                  var24 = 0;
               }

               if (var5 == 17) {
                  var24 = 6;
               } else if (var5 == 18) {
                  var24 = 2;
               } else if (var5 == 19) {
                  var24 = 4;
               }

               if (var5 == 17 && this.dj > 10) {
                  var24++;
               }

               if ((this.di & 1 << var5) > 0 && this.dj > 5) {
                  var1.setColor(255, 0, 0);
                  var1.setClip(0, 0, 176, 220);
                  if (var5 != 19 && var5 != 17) {
                     var1.fillArc(var20 + 4, var21 + 4, 11, 11, 0, 360);
                  } else {
                     var1.fillArc(var20 - 1, var21 - 1, 21, 21, 0, 360);
                  }
               }

               var1.setClip(var20, var21, 19, 19);
               var1.drawImage(ar, var20, var21 - var24 * 19, 0);
               if (this.cu == var5) {
                  var1.setColor(255, 255, 255);
                  var1.setClip(0, 0, 176, 220);
                  if (var5 != 19 && var5 != 17 && var5 != 18) {
                     var1.drawLine(var20 + 4, var21 + 4, var20 + 15, var21 + 4);
                     var1.drawLine(var20 + 4, var21 + 15, var20 + 15, var21 + 15);
                     var1.drawLine(var20 + 4, var21 + 15, var20 + 4, var21 + 4);
                     var1.drawLine(var20 + 15, var21 + 15, var20 + 15, var21 + 4);
                  } else {
                     var1.drawLine(var20, var21, var20 + 8, var21);
                     var1.drawLine(var20, var21, var20, var21 + 8);
                     var1.drawLine(var20 + 19, var21, var20 + 11, var21);
                     var1.drawLine(var20 + 19, var21, var20 + 19, var21 + 8);
                     var1.drawLine(var20, var21 + 19, var20 + 8, var21 + 19);
                     var1.drawLine(var20, var21 + 19, var20, var21 + 11);
                     var1.drawLine(var20 + 19, var21 + 19, var20 + 11, var21 + 19);
                     var1.drawLine(var20 + 19, var21 + 19, var20 + 19, var21 + 11);
                  }
               }
            }
         }

         var1.setClip(0, 0, 176, 220);
         if (this.cQ[this.cu] < 11) {
            ratchetandclank.currentFont = ratchetandclank.smallFont;
            byte var25 = T[this.cQ[this.cu] - 1];
            var1.drawImage(aA, this.midlet.introManager.b() / 2, 120 + var14, 0);
            var1.setColor(1370860);
            ratchetandclank.currentFont.a(var1, this.i(this.cQ[this.cu]) + "/" + var25, 7 + this.midlet.introManager.b() / 2, 137 + var14, 0);
         }
      } catch (Exception var19) {
      }

      this.midlet.introManager.a(var1, 7, -1, this);
   }

   public final void k(Graphics var1) {
      this.midlet.introManager.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.sleep(var1);
      this.midlet.introManager.a(var1, ratchetandclank.strings[43]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      this.a(var1, ratchetandclank.strings[285], 88, 110, 17, this.i());
      this.midlet.introManager.a(var1, 9, -1, this);
   }

   public final void l(Graphics var1) {
      int var2 = 0;
      boolean var3 = false;
      boolean var7 = false;
      var1.setClip(0, 0, 176, 220);
      var1.setColor(0);
      var1.fillRect(0, 0, 176, 220);
      this.midlet.introManager.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      var2 = this.midlet.introManager.a(var1, ratchetandclank.strings[75]) + (ratchetandclank.currentFont.a >> 1);
      var1.setClip(66, var2, 44, 44);
      var1.drawImage(az, 66, var2 - this.p * 44, 0);
      var1.setClip(0, 0, 176, 220);
      this.sleep(var1);
      var1.setColor(16777215);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      var2 = this.midlet.introManager.a(var1, ratchetandclank.strings[306] + " " + this.midlet.introManager.c(this.cr), 88, 85, 17, false);
      var2 = this.midlet.introManager.a(var1, ratchetandclank.strings[299] + " " + this.v(), 88, var2, 17, false);
      var2 += 5;
      this.midlet.introManager.a(var1, ratchetandclank.strings[307] + " " + dA, 88, var2, 17, true);
      this.midlet.introManager.a(var1, 9, -1, this);
   }

   public final void m(Graphics var1) {
      this.midlet.introManager.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.sleep(var1);
      this.midlet.introManager.a(var1, ratchetandclank.strings[308]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      this.a(var1, ratchetandclank.strings[309], 88, 131, 33, this.i());
      this.midlet.introManager.a(var1, 9, -1, this);
   }

   public final void n(Graphics var1) {
      this.midlet.introManager.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.sleep(var1);
      this.midlet.introManager.a(var1, ratchetandclank.strings[75]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      int var2;
      if (this.X < 11) {
         var2 = this.a(var1, ratchetandclank.strings[251], (176 - this.i()) / 2, 87, 0, this.i());
      } else {
         var2 = this.a(var1, ratchetandclank.strings[252], (176 - this.i()) / 2, 87, 0, this.i());
      }

      Object[] var3 = new Object[]{new Integer(cl[this.X])};
      this.cE = null;
      this.cE = this.a(ratchetandclank.strings[250], var3);
      this.a(var1, this.cE, (176 - this.i()) / 2, var2, 0, this.i());
      this.midlet.introManager.a(var1, 9, -1, this);
   }

   public final void o(Graphics var1) {
      this.midlet.introManager.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.sleep(var1);
      this.midlet.introManager.a(var1, ratchetandclank.strings[24]);
      int var2 = 87;
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      if (this.cW) {
         var2 = this.a(var1, ratchetandclank.strings[228], (176 - this.i()) / 2, 87, 0, this.i());
      }

      if (this.cX) {
         var2 = this.a(var1, ratchetandclank.strings[230], (176 - this.i()) / 2, var2, 0, this.i());
      }

      var2 = this.a(var1, ratchetandclank.strings[229], (176 - this.i()) / 2, var2, 0, this.i());
      if (this.cZ) {
         Object[] var3 = new Object[]{new String(ratchetandclank.strings[this.player.L + 48])};
         this.cE = null;
         this.cE = this.a(ratchetandclank.strings[231], var3);
         var2 = this.a(var1, this.cE, (176 - this.i()) / 2, var2, 0, this.i());
      }

      Object[] var5 = new Object[]{new Integer(cl[this.X])};
      this.cE = null;
      this.cE = this.a(ratchetandclank.strings[249], var5);
      this.a(var1, this.cE, (176 - this.i()) / 2, var2, 0, this.i());
      this.midlet.introManager.a(var1, 9, 8, this);
   }

   public final void b(Graphics var1, byte var2) {
      var1.setClip(0, 0, 176, 220);
      var1.setColor(0);
      var1.fillRect(0, 0, 176, 220);
      this.midlet.introManager.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.sleep(var1);
      this.midlet.introManager.a(var1, ratchetandclank.strings[24]);
      int var3 = this.k() + this.h() / 10;

      for (int var8 = 0; var8 < 12; var8++) {
         if ((this.cL & 1 << 20 + var8) != 0
            && (var3 = this.midlet.introManager.a(var1, ratchetandclank.strings[60 + var8], tileWidth + (tileWidth >> 1), var3, 0, this.cu == var8)) >= this.k() + this.h() - 2 * this.h() / 10) {
            if (var8 >= this.cu) {
               break;
            }

            this.sleep(var1);
            var3 = this.k() + this.h() / 10;
         }
      }

      if (this.cu != 11 && (this.cL & 1 << this.cu + 21) != 0) {
         int var4 = this.j() + this.g() - 7;
         int var6 = this.k() + this.h() - 20;
         e.b.fillTriangle(var4, var6, var4 - 10, var6, var4 - 5, var6 + 10, -15222068);
      }

      if (this.cu != 0 && (this.cL & 1 << this.cu + 19) != 0) {
         int var5 = this.j() + this.g() - 7;
         int var7 = this.k() + 20;
         e.b.fillTriangle(var5, var7, var5 - 10, var7, var5 - 5, var7 - 10, -15222068);
      }

      this.midlet.introManager.a(var1, 7, 8, this);
   }

   public final void p(Graphics var1) {
      this.midlet.introManager.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.sleep(var1);
      this.midlet.introManager.a(var1, ratchetandclank.strings[282]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      this.dc[0] = 100;
      int var2;
      this.dc[1] = var2 = ap.getHeight() / 2 + this.midlet.introManager.a(var1, ratchetandclank.strings[11], tileWidth + (tileWidth >> 1), 100, 0, this.cu == 0);
      this.midlet.introManager.a(var1, ratchetandclank.strings[10], tileWidth + (tileWidth >> 1), var2, 0, this.cu == 1);
      this.midlet.introManager.a(var1, 7, -1, this);
      this.midlet.introManager.a(var1, tileWidth >> 1, this.dc[this.cu]);
   }

   public final void q(Graphics var1) {
      aE.getWidth();
      aE.getHeight();
      boolean var4 = false;
      int var5 = hudHeight >> 1;
      var1.setClip(0, 0, 176, 220);
      var1.setColor(0);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      int var7 = 110 + var5;
      if (this.Z == 11) {
         var7 -= var5;
      }

      var1.drawImage(aB, 88, var7, 3);
      var1.setColor(2446442);
      int var9 = var7 - 116;

      for (int var10 = 1; var10 <= 7; var10++) {
         if ((this.player.O & 1 << var10) == 0) {
            this.b(var1, 0 + dM[var10 - 1][0], var9 + dM[var10 - 1][1]);
         }
      }

      if (this.player.L > 0) {
         this.a(var1, 0 + dM[this.player.L - 1][0], var9 + dM[this.player.L - 1][1]);
      }

      int var2 = 110 - 3 * ratchetandclank.currentFont.a / 2 + var5;
      var1.setColor(16777215);
      var2 = this.a(var1, ratchetandclank.strings[48 + this.player.L], 88, var2, 17, 2 * aB.getWidth() / 4);
      var2 = this.a(var1, ratchetandclank.strings[310] + " " + (this.player.P[this.player.L] + 1), 88, var2, 17, 2 * aB.getWidth() / 4);
      if (this.player.L != 6) {
         this.a(var1, ratchetandclank.strings[232] + ": " + this.player.M[this.player.L], 88, var2, 17, 2 * aB.getWidth() / 4);
      }
   }

   public final void a(Graphics var1, int var2, int var3) {
      boolean var4 = false;
      var1.setColor(1240814);

      for (int var5 = 0; var5 < 7; var5++) {
         var1.drawLine(dN[var5] + var2, dO[var5] + var3, dN[var5 + 1] + var2, dO[var5 + 1] + var3);
         var1.drawLine(dP[var5] + var2, dQ[var5] + var3, dP[var5 + 1] + var2, dQ[var5 + 1] + var3);
      }

      var1.drawLine(dN[7] + var2, dO[7] + var3, dN[0] + var2, dO[0] + var3);
      var1.drawLine(dP[7] + var2, dQ[7] + var3, dP[0] + var2, dQ[0] + var3);
   }

   public final void b(Graphics var1, int var2, int var3) {
      if (this.ef == null) {
         this.ef = new int[8];
      }

      if (this.eg == null) {
         this.eg = new int[8];
      }

      for (int var4 = 0; var4 < 8; var4++) {
         this.ef[var4] = dR[var4] + var2;
         this.eg[var4] = dS[var4] + var3;
      }

      e.b.fillPolygon(this.ef, 0, this.eg, 0, 8, -14330774);
   }

   public final void r(Graphics var1) {
      this.midlet.introManager.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.sleep(var1);
      this.midlet.introManager.a(var1, ratchetandclank.strings[59]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      this.midlet.introManager.a(var1, -1, 8, this);
   }

   public final void s(Graphics var1) {
      this.midlet.introManager.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.sleep(var1);
      this.midlet.introManager.a(var1, ratchetandclank.strings[44]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      this.dc[0] = 100;
      int var2;
      this.dc[1] = var2 = 4 + this.midlet.introManager.a(var1, ratchetandclank.strings[11], tileWidth + (tileWidth >> 1), 100, 0, this.cu == 0);
      this.midlet.introManager.a(var1, ratchetandclank.strings[10], tileWidth + (tileWidth >> 1), var2, 0, this.cu == 1);
      this.midlet.introManager.a(var1, 7, 8, this);
      this.midlet.introManager.a(var1, tileWidth >> 1, this.dc[this.cu]);
   }

   public final void t(Graphics var1) {
      this.midlet.introManager.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.sleep(var1);
      this.midlet.introManager.a(var1, ratchetandclank.strings[281]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      this.dc[0] = 100;
      int var2;
      this.dc[1] = var2 = this.midlet.introManager.a(var1, ratchetandclank.strings[40], tileWidth + (tileWidth >> 1), 100, 0, this.cu == 0) + 5;
      this.midlet.introManager.a(var1, ratchetandclank.strings[280], tileWidth + (tileWidth >> 1), var2, 0, this.cu == 1);
      this.midlet.introManager.a(var1, 7, -1, this);
      this.midlet.introManager.a(var1, tileWidth >> 1, this.dc[this.cu]);
   }

   public final void u(Graphics var1) {
      this.midlet.introManager.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.sleep(var1);
      this.midlet.introManager.a(var1, ratchetandclank.strings[45]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      this.dc[0] = 100;
      int var2;
      this.dc[1] = var2 = ap.getHeight() / 2 + this.midlet.introManager.a(var1, ratchetandclank.strings[11], tileWidth + (tileWidth >> 1), 100, 0, this.cu == 0);
      this.midlet.introManager.a(var1, ratchetandclank.strings[10], tileWidth + (tileWidth >> 1), var2, 0, this.cu == 1);
      this.midlet.introManager.a(var1, 7, 8, this);
      this.midlet.introManager.a(var1, tileWidth >> 1, this.dc[this.cu]);
   }

   public final void v(Graphics var1) {
      this.midlet.introManager.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.sleep(var1);
      this.midlet.introManager.a(var1, ratchetandclank.strings[46]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      this.dc[0] = 100;
      int var2;
      this.dc[1] = var2 = ap.getHeight() / 2 + this.midlet.introManager.a(var1, ratchetandclank.strings[11], tileWidth + (tileWidth >> 1), 100, 0, this.cu == 0);
      this.midlet.introManager.a(var1, ratchetandclank.strings[10], tileWidth + (tileWidth >> 1), var2, 0, this.cu == 1);
      this.midlet.introManager.a(var1, 7, 8, this);
      this.midlet.introManager.a(var1, tileWidth >> 1, this.dc[this.cu]);
   }

   public final void render(Graphics var1) {
      if (this.U <= 0) {
         if (!this.m) {
            if (this.dT != -1 || this.dU != -1 || this.dZ) {
               var1.setClip(0, 0, 176, 220);
               var1.setColor(0);
               var1.fillRect(0, 0, 176, 220);
               this.d = true;
               System.gc();
            } else if (aF != null) {
               switch (this.b) {
                  case 1:
                     this.b(var1, dk);
                     return;
                  case 2:
                     this.l(var1);
                     return;
                  case 3:
                     this.j(var1);
                     return;
                  case 4:
                     this.abs(var1);
                     return;
                  case 5:
                     this.f(var1);
                     return;
                  case 6:
                     this.g(var1);
                     return;
                  case 7:
                     this.v(var1);
                     return;
                  case 8:
                     this.u(var1);
                     return;
                  case 9:
                     this.s(var1);
                     return;
                  case 10:
                     this.k(var1);
                     return;
                  case 11:
                     this.i(var1);
                     return;
                  case 12:
                     this.h(var1);
                     return;
                  case 13:
                     this.r(var1);
                     return;
                  case 14:
                     this.o(var1);
                     return;
                  case 15:
                     this.n(var1);
                     return;
                  case 16:
                  case 17:
                  case 20:
                  default:
                     if (this.cS) {
                        this.player.E = 1;
                     }

                     this.levelMap.a(var1);
                     this.G(var1);
                     this.z(var1);
                     this.B(var1);
                     this.C(var1);
                     this.A(var1);
                     if (this.bX != -1) {
                        this.b(var1);
                     }

                     if (this.ca != -1) {
                        this.D(var1);
                     }

                     if (this.cc != -1) {
                        this.c(var1);
                     }

                     this.E(var1);
                     this.a(var1);
                     if (this.b == 16 && this.h == 4) {
                        this.c(var1, this.j);
                     }

                     if (this.Z == 12) {
                        this.F(var1);
                     }

                     if (this.b != 16 && this.b != 17 && this.b != 24) {
                        this.y(var1);
                     } else {
                        if (this.b == 24) {
                           this.F(var1);

                           for (int var2 = 9; var2 >= 0; var2--) {
                              this.playerProjectiles[var2].a(var1);
                           }
                        }

                        var1.setClip(0, 0, 176, hudHeight);
                        var1.setColor(0);
                        var1.fillRect(0, 0, 176, hudHeight);
                        var1.setClip(0, 220 - hudHeight, 176, hudHeight);
                        var1.setColor(0);
                        var1.fillRect(0, 220 - hudHeight, 176, hudHeight);
                        this.midlet.introManager.a(var1, -1, -1, this);
                     }

                     if (this.Z == 11) {
                        var1.setClip(0, 220 - hudHeight, 176, hudHeight);
                        var1.setColor(0);
                        var1.fillRect(0, 220 - hudHeight, 176, hudHeight);
                        ratchetandclank.currentFont = ratchetandclank.smallFont;
                        var1.setColor(1882828);
                        ratchetandclank.currentFont.a(var1, ratchetandclank.strings[311] + " " + this.db, 88, 220 - hudHeight + 3, 17);
                     }

                     if (this.e) {
                        this.x(var1);
                        this.e = false;
                     }

                     if (this.cD) {
                        this.H(var1);
                        this.cG = this.a(var1, this.cF);
                     }

                     if (this.n == 2 && this.dY > 0) {
                        var1.setColor(16777215);
                        ratchetandclank.currentFont = ratchetandclank.smallFont;
                        ratchetandclank.currentFont.a(var1, "" + 1000 / this.dY + "." + 100000 / this.dY % 100, 0, hudHeight, 20);
                     }

                     if (this.dX != -1) {
                        this.cu = (byte)this.dX;
                        this.q(var1);
                        this.b = 22;
                        this.dX = -1;
                     }

                     return;
                  case 18:
                     this.a(var1, dB);
                     return;
                  case 19:
                     this.m(var1);
                     return;
                  case 21:
                     this.t(var1);
                     return;
                  case 22:
                     this.q(var1);
                     return;
                  case 23:
                     this.p(var1);
               }
            }
         }
      }
   }

   public final void b(byte var1) {
      switch (var1) {
         case 0:
            this.player.n();
            this.d = true;
            this.player.ab += 1536;
            if (this.player.ab >> 8 > 9 * tileWidth) {
               this.h++;
               this.player.ac = -3584;
               this.player.ad = 512;
               return;
            }
            break;
         case 1:
            this.player.n();
            this.player.a((byte)2);
            this.d = true;
            this.player.ac = (short)(this.player.ac + 256);
            this.player.ag = this.player.ag + this.player.ac;
            this.player.ab = this.player.ab + this.player.ad;
            this.player.v();
            short var3 = this.player.a(false);
            if (this.player.c() >= var3 && this.player.ac > 0) {
               this.player.a((byte)4);
               this.player.ag = 0;
               this.player.ac = -3584;
               this.h++;
               return;
            }
            break;
         case 2:
            if (this.i >= 4) {
               this.h++;
               this.player.a((byte)1);
               return;
            }

            this.player.n();
            this.player.a((byte)2);
            this.d = true;
            this.player.ac = (short)(this.player.ac + 256);
            this.player.ag = this.player.ag + this.player.ac;
            this.player.v();
            short var2 = this.player.a(false);
            if (this.player.c() >= var2 && this.player.ac > 0) {
               this.player.a((byte)4);
               this.player.ag = 0;
               this.player.ac = -3584;
               this.player.am = 0;
               this.i++;
               return;
            }
            break;
         case 3:
            this.player.n();
            this.d = true;
            this.player.ab += 1536;
            if (this.player.ab >> 8 > 11 * tileWidth + (tileWidth >> 1)) {
               this.h++;
               this.player.a((byte)0);
               this.player.am = 0;
               this.player.n();
            }
      }
   }

   public final void c(byte var1) {
      switch (var1) {
         case 0:
            this.U();
            this.player.n();
            if (this.player.s != -1) {
               short var8 = this.player.a(false);
               this.d = true;
               this.player.ab = this.player.ab + this.player.ad;
               this.player.ac = (short)(this.player.ac + 256);
               this.player.ag = this.player.ag + this.player.ac;
               this.player.v();
               if (this.player.c() >= var8) {
                  this.player.a((byte)4);
                  this.player.ag = 0;
                  this.player.s = -1;
               }
            } else {
               if (this.player.aa <= 8 && this.player.ab >> 8 < 13 * tileWidth) {
                  if (this.player.ab >> 8 > 11 * tileWidth + (tileWidth >> 1)) {
                     this.player.a((byte)0);
                     this.player.am = 0;
                     this.s = 4;
                     return;
                  }

                  this.player.a((byte)1);
                  this.player.am = 0;
                  this.s = 3;
                  return;
               }

               if (this.player.ab >> 8 < 10 * tileWidth + (tileWidth >> 1)) {
                  this.s = 2;
                  this.player.ac = -3584;
                  return;
               }

               this.player.a((byte)1);
               this.player.am = 0;
               this.player.ao = false;
               this.player.ad = 0;
               this.player.ac = 0;
               this.t = false;
               this.s++;
            }

            this.U();
            return;
         case 1:
            short var6 = this.player.a(false);
            this.player.n();
            this.d = true;
            if (this.t) {
               var6 = this.player.a(false);
               this.d = true;
               this.player.ab = this.player.ab + this.player.ad;
               this.player.ac = (short)(this.player.ac + 256);
               this.player.ag = this.player.ag + this.player.ac;
               this.player.v();
               if (this.player.c() >= var6) {
                  this.player.a((byte)1);
                  this.player.ag = 0;
                  this.t = false;
               }
            } else {
               if (this.player.c() < var6) {
                  this.player.a((byte)3);
                  this.t = true;
               }

               this.player.ab -= 1536;
               if (this.player.ab >> 8 < 10 * tileWidth + (tileWidth >> 1)) {
                  this.player.a((byte)0);
                  this.s++;
                  this.player.ac = -3584;
               }
            }

            this.U();
            return;
         case 2:
            this.player.n();
            this.player.a((byte)2);
            this.d = true;
            this.player.ac = (short)(this.player.ac + 256);
            this.player.ag = this.player.ag + this.player.ac;
            this.player.v();
            short var5 = this.player.a(false);
            if (this.player.c() >= var5 && this.player.ac > 0) {
               this.player.a((byte)4);
               this.player.ag = 0;
               this.player.ac = -3584;
               this.player.am = 0;
               if (this.player.aa <= 8) {
                  this.s++;
                  this.player.ao = true;
                  this.player.a((byte)1);
               }
            }

            this.U();
            return;
         case 3:
            this.player.n();
            this.d = true;
            this.player.ab += 1536;
            if (this.player.ab >> 8 > 11 * tileWidth + (tileWidth >> 1)) {
               this.player.a((byte)0);
               this.player.am = 0;
               this.s++;
            }

            this.U();
            return;
         case 4:
            if (this.u < 50) {
               this.u++;
               this.U();
               return;
            }

            this.bJ[4] = 0;
            d.d[d.i[this.X]][14][8] = -126;
            d.d[d.i[this.X]][14][9] = -125;
            this.levelMap.f[14][9] = 22;
            this.levelMap.f[13][9] = 22;
            this.levelMap.f[12][9] = 20;
            this.levelMap.f[15][9] = 21;
            d.e[12] = d.e[12] | 512;
            d.e[13] = d.e[13] | 512;
            d.e[14] = d.e[14] | 512;
            d.e[15] = d.e[15] | 512;
            this.s++;
            return;
         case 5:
            this.cC = true;
            this.cI = cz[32];
            this.r(cA[32]);
            this.player.ac = -3172;
            this.player.ad = 512;
            this.s++;
            return;
         case 6:
            if (!this.cD) {
               this.player.n();
               this.player.a((byte)2);
               this.d = true;
               this.player.ac = (short)(this.player.ac + 256);
               this.player.ag = this.player.ag + this.player.ac;
               this.player.ab = this.player.ab + this.player.ad;
               this.player.v();
               short var2 = this.player.a(false);
               if (this.player.c() >= var2 && this.player.ac > 0) {
                  this.player.a((byte)1);
                  this.player.am = 0;
                  this.player.ag = 0;
                  this.player.ac = -3584;
                  this.s++;
                  return;
               }
            }
            break;
         case 7:
            this.player.n();
            this.d = true;
            this.player.ab += 1536;
            if (this.player.ab >> 8 > 14 * tileWidth + (tileWidth >> 1)) {
               this.player.a((byte)0);
               this.s++;
               return;
            }
            break;
         case 8:
            this.p = 0;
            this.y();
            if (az == null) {
               try {
                  az = Image.createImage("/clank.png");
               } catch (Exception var4) {
               }
            }

            dA = dA + dz;
            this.b = 2;
            this.player.ao = true;
            this.player.L = 1;
            this.player.a((byte)0);
            this.player.am = 0;
            this.player.aa = 1;
            this.player.ag = 1792;
            this.player.ab = 22528;
            this.player.ac = 0;
            this.player.ad = 0;
            this.player.E = 0;
            this.s++;
      }
   }

   private void U() {
      int var1 = tileWidth * 14 - 42;
      int var2 = 2 * tileWidth;
      int var3 = tileHeight * 9 - tileHeight;
      int var4 = 2 * tileHeight;
      this.e(var1 + this.abs(this.random.nextInt()) % var2 << 8, var3 + this.abs(this.random.nextInt()) % var4 << 8, 30);
   }

   public final void d(byte var1) {
      switch (var1) {
         case 0:
            this.k++;
            return;
         case 1:
            this.cC = true;
            this.cI = cz[20];
            this.r(cA[20]);
            this.k++;
            return;
         case 2:
            if (!this.cD) {
               this.a(3, 2, (byte)0, -1);
               this.enemies[9].ao = false;
               this.k++;
               return;
            }
            break;
         case 3:
            this.enemies[9].i();
            this.d = true;
            int var4 = this.enemies[9].a(false);
            this.enemies[9].ac = (short)(this.enemies[9].ac + 256);
            this.enemies[9].ag = this.enemies[9].ag + this.enemies[9].ac;
            this.enemies[9].v();
            if (this.enemies[9].b() + 2 * tileHeight >= var4 && this.enemies[9].ac > 0) {
               this.enemies[9].a((byte)0);
               this.enemies[9].am = 0;
               this.b = 0;
               this.cC = true;
               this.cI = cz[21];
               this.r(cA[21]);
               this.e = true;
               this.k++;
               this.updateCamera();
               return;
            }
            break;
         case 4:
            this.player.n();
            this.d = true;
            this.player.ab += 1536;
            if (this.player.ab >> 8 > 18 * tileWidth - (tileWidth >> 3)) {
               this.player.a((byte)0);
               this.player.ad = 1280;
               this.player.ag = 0;
               this.player.ac = -3584;
               this.k++;
               return;
            }
            break;
         case 5:
            this.cC = true;
            this.cI = cz[15];
            this.r(cA[15]);
            this.k++;
            return;
         case 6:
            if (this.cD) {
               this.player.a((byte)0);
               return;
            }

            this.player.n();
            this.player.a((byte)2);
            this.d = true;
            this.player.ac = (short)(this.player.ac + 384);
            this.player.ag = this.player.ag + this.player.ac;
            this.player.ab = this.player.ab + this.player.ad;
            this.player.v();
            if (this.player.c() >= 5 * tileHeight + (tileHeight >> 1) && this.player.ac > 0) {
               this.player.a((byte)6);
               int var3 = (this.abs(this.be[0] - this.bc[0]) << 8) / 2304;
               this.player.V = (this.bg[0] << 8) / var3;
               this.player.W = this.bh[0] / var3;
               this.player.X = this.bf[0];
               this.k++;
               return;
            }
            break;
         case 7:
            this.player.i();
            if (this.player.ab >= tileWidth * 25 << 8) {
               this.k++;
               this.player.ac = 0;
               this.player.ad = 0;
               this.player.a((byte)3);
               return;
            }
            break;
         case 8:
            this.player.n();
            this.d = true;
            short var2 = this.player.a(false);
            this.player.ac = (short)(this.player.ac + 256);
            if (this.player.c() + this.player.ac > var2 << 8) {
               this.player.ac = (short)((var2 << 8) - this.player.c());
            }

            this.player.ag = this.player.ag + this.player.ac;
            this.player.v();
            if (this.player.c() >= var2 && this.player.ac > 0) {
               this.player.a((byte)4);
               this.player.ag = 0;
               this.player.ac = 0;
               this.player.ad = 0;
               this.k++;
               return;
            }
            break;
         case 9:
            this.player.a((byte)0);
            this.e = true;
            this.a(3, 2, (byte)0, -1);
            this.ct = System.currentTimeMillis();
            this.b = 0;
            this.k++;
            this.updateCamera();
      }
   }

   public final void l() {
      int var1 = tileWidth << 1;
      int var2 = tileHeight << 1;

      for (int var3 = 3; var3 >= 0; var3--) {
         if (this.cq[var3] != -1) {
            boolean var4 = false;
            switch (this.cq[var3]) {
               case 0:
                  this.co[var3] = (short)(this.co[var3] + 2);
                  if (this.co[var3] > var1) {
                     this.co[var3] = (short)(this.co[var3] - 2);
                     this.cq[var3] = (byte)(this.cq[var3] + 2);
                  }
                  break;
               case 1:
                  this.cp[var3] = (short)(this.cp[var3] + 2);
                  if (this.cp[var3] > var2) {
                     this.cp[var3] = (short)(this.cp[var3] - 2);
                     this.cq[var3] = (byte)(this.cq[var3] + 2);
                  }
                  break;
               case 2:
                  this.co[var3] = (short)(this.co[var3] - 2);
                  if (this.co[var3] < 0) {
                     this.co[var3] = (short)(this.co[var3] + 2);
                     this.cq[var3] = (byte)(this.cq[var3] - 2);
                  }
                  break;
               case 3:
                  this.cp[var3] = (short)(this.cp[var3] - 2);
                  if (this.cp[var3] < 0) {
                     this.cp[var3] = (short)(this.cp[var3] + 2);
                     this.cq[var3] = (byte)(this.cq[var3] - 2);
                  }
            }
         }
      }
   }

   public final int abs(int var1) {
      return var1 < 0 ? var1 * -1 : var1;
   }

   public static final void sleep(int var0) {
      try {
         Thread.sleep(var0);
      } catch (Exception var2) {
      }
   }

   public final void c(int var1, int var2) {
      this.dT = var1;
      this.dU = var2;
   }

   public final void m() {
      System.gc();
      sleep(20);
      this.c = this.b;
      this.b = 4;
      this.cv = 0;
      this.cu = 0;
      System.gc();
   }

   public final void n() {
      if (!this.dV) {
         this.dV = true;
         this.midlet.d();
         this.d = true;
         this.dW = 0L;
         this.cv = 0;
         this.cw = 0;
         if (this.b == 0 || this.b == 17 || this.b == 16 || this.b == 24 || this.b == 22) {
            if (this.b == 22) {
               this.dX = this.cu;
               this.b = 0;
            }

            this.cu = 0;
            this.c = this.b;
            this.b = 4;
            aF = null;
         }

         System.gc();
      }
   }

   public final void o() {
      if (this.dV) {
         this.U = 4;
         if (aF == null) {
            this.y();
         }

         this.d = true;
         this.dV = false;
         this.dW = System.currentTimeMillis();
      }
   }

   public final void pause() {
      this.n();
   }

   public final void resume() {
      this.o();
      this.dV = false;
   }

   public final void tick() {
      if (this.player.ac == 0) {
         this.ea = 0;
      }

      if (this.dT != -1) {
         this.f(this.dT);
         this.dT = -1;
         this.d = true;
         System.currentTimeMillis();
      } else if (this.dU != -1) {
         this.midlet.e(this.dU);
         this.ac = (byte)this.dU;
         this.dU = -1;
         this.d = true;
         System.currentTimeMillis();
      }

      if (this.dV) {
         sleep(100);
      } else {
         if (this.U > 0 && --this.U > 0) {
            sleep(10);
         }

         this.cs = System.currentTimeMillis();
         if (this.cs - this.ct > 33L) {
            this.dY = (int)(this.cs - this.ct);
            this.T();
            if (this.b == 17) {
               this.abs(this.k);
               if (!this.cD) {
                  this.updateCamera();
               }
            }

            if (this.b == 24) {
               this.c(this.s);
               this.updateCamera();

               for (int var1 = 9; var1 >= 0; var1--) {
                  this.playerProjectiles[var1].b(false);
               }
            }

            if (this.b == 16) {
               for (int var2 = 9; var2 >= 0; var2--) {
                  this.playerProjectiles[var2].b(false);
               }

               this.b(this.h);
               this.updateCamera();
            }

            if (this.b == 2) {
               this.q++;
               if (this.q == 25) {
                  this.q = 80;
               }

               if (this.q > 84) {
                  this.q = 80;
                  if (this.p < 15) {
                     this.p++;
                  } else {
                     this.p--;
                  }
               }
            } else {
               this.q = 0;
            }

            if (this.b == 11 || this.b == 12) {
               this.player.n();
            }

            if (this.b == 3 || this.b == 1) {
               dw = 0L;
            }

            if (this.b == 0 && !this.cD) {
               dw = dw + (this.cs - this.ct);
               this.cr = (int)(this.cr + (this.cs - this.ct));
               this.H();
               this.J();
               this.l();
               this.c();
               this.K();
               this.b();
               this.player.l();
               this.player.n();
               if (this.player.E <= 0) {
                  this.R();
               } else {
                  this.player.E--;
               }

               this.updateCamera();
               this.I();
               this.a();

               for (int var3 = 9; var3 >= 0; var3--) {
                  this.playerProjectiles[var3].b(false);
                  if (this.playerProjectiles[var3].f != -1) {
                     this.m(var3);
                  }
               }

               for (int var4 = 9; var4 >= 0; var4--) {
                  this.enemyProjectiles[var4].b(true);
                  if (this.enemyProjectiles[var4].f != -1) {
                     this.l(var4);
                  }
               }

               this.db = 0;

               for (int var5 = enemyPoolSize - 1; var5 >= 0; var5--) {
                  if (this.enemies[var5].Z != -1) {
                     if (this.Z == 12 && (this.enemies[var5].Z == 1 || this.enemies[var5].Z == 4)) {
                        if (this.enemies[var5].E > 140) {
                           this.enemies[var5].Z = -1;
                           continue;
                        }

                        this.enemies[var5].E++;
                        if (this.enemies[var5].E == 1) {
                           this.enemies[var5].a((byte)6);
                           this.enemies[var5].am = 1;
                        } else if (this.enemies[var5].E == 137) {
                           this.enemies[var5].a((byte)7);
                           this.enemies[var5].am = 1;
                        }
                     }

                     if (this.enemies[var5].al != 5 && this.player.al != 10) {
                        this.q(var5);
                     }

                     this.enemies[var5].h();
                     this.enemies[var5].i();
                     if (this.Z == 11 && this.enemies[var5].Z != 4) {
                        this.db++;
                     }
                  }
               }

               if (this.Z == 0 && this.enemies[9].Z == -1 && this.player.c() >= this.player.a(false)) {
                  this.b = 17;
                  this.e = false;
                  this.bC[0] = 0;
                  this.player.a((byte)1);
                  this.player.am = 0;
                  this.player.ao = true;
               }

               if (this.Z == 12) {
                  if (!this.g && this.player.ab >> 8 >= 6 * tileWidth && this.player.ab >> 8 <= 9 * tileWidth && this.player.c() >= this.player.a(false)) {
                     this.b = 16;
                     this.e = false;
                     this.player.a((byte)1);
                     this.player.am = 0;
                  }

                  if (this.g) {
                     this.M();
                  }
               } else if (this.cV) {
                  this.A();
               }
            }

            this.ct = this.cs;
         } else if (this.d) {
            this.d = false;
         }

         if (this.dZ) {
            this.dZ = false;
            this.midlet.a();
         }
      }
   }

   public final void updateCamera() {
      int var1 = 0;
      int var2 = 0;
      short var4 = this.player.b();
      short var5 = this.player.c();
      if (!z) {
         if (this.player.ao) {
            var1 = -(var4 - tileWidth);
         } else {
            var1 = -(var4 + tileWidth - 176);
         }
      } else {
         var1 = -(E - 88);
         if (var4 > E + (tileWidth >> 1) || var4 < E - (tileWidth >> 1) || this.player.c() == this.player.a(false)) {
            z = false;
         }
      }

      if (A) {
         var2 = -(var5 + K - 220);
      } else {
         var2 = -(var5 + K + (tileHeight << 1) - 220);
      }

      int var3 = var2 - y;
      y += var3 >> 1;
      if (x > var1) {
         x -= 10;
         if (x < var1) {
            x = var1;
         }
      } else if (x < var1) {
         x += 10;
         if (x > var1) {
            x = var1;
         }
      }

      if (x > 0) {
         x = 0;
      }

      if (x < -(28 * tileWidth - 176)) {
         x = -(28 * tileWidth - 176);
      }

      if (y > 0) {
         y = 0;
      }

      if (y < -(18 * tileHeight - 220)) {
         y = -(18 * tileHeight - 220);
      }
   }

   public final void t() {
      int var4 = 0;
      String var7 = a("/mapData.txt");

      for (short var1 = 0; var1 < 62; var1++) {
         for (short var2 = 0; var2 < 3; var2++) {
            int var3 = var7.indexOf(",", var4);
            String var6 = var7.substring(var4, var3).trim();
            var4 = var3 + 1;
            this.dd[var1][var2] = Short.parseShort(var6);
         }

         var4 = var7.indexOf("\n", var4) + 1;
      }

      for (short var8 = 0; var8 < 43; var8++) {
         for (short var9 = 0; var9 < 2; var9++) {
            int var10 = var7.indexOf(",", var4);
            String var11 = var7.substring(var4, var10).trim();
            var4 = var10 + 1;
            this.de[var8][var9] = Byte.parseByte(var11);
         }

         var4 = var7.indexOf("\n", var4) + 1;
      }
   }

   public final void f(int var1) {
      this.player.u = -2;
      this.cS = false;
      this.cD = false;
      this.bZ = true;
      this.o = 13;
      this.k = 0;
      this.dC = 0;
      this.ab = 1;
      this.aa = false;
      this.u();
      if (var1 == 0) {
         this.Z = (byte)var1;
         this.levelMap.b(var1);
         this.X = d.a;
         this.di = 1;
         this.Y = this.X;
         this.levelMap.a(this.X, true);
         this.ad = 1;
         this.ae = 5;
         this.player.ab = this.ad * tileWidth + (J >> 1) << 8;
         this.player.aa = this.player.af = this.ae;
         this.player.aj = 1;
         this.player.ak = 0;
         this.player.al = 0;
         this.player.am = 0;
         this.player.ag = 0;
         this.player.Z = 0;
         this.player.ah = 1;
         this.player.ai = 20;
         this.cr = 0;
         this.ct = System.currentTimeMillis();
         this.b(this.X, 6364);
         this.player.E = 0;
         this.player.ao = true;
         this.d = true;
         this.aX = 0;
         this.player.a((byte)0);
         this.player.am = 0;
         this.ce = 1;
         this.cf = 18;
         this.player.M[1] = 0;
         x = -22;
         y = -32;
      }

      this.e = true;
      this.b = 17;
   }

   public final void u() {
      this.d();
      this.bw = -1;
      this.bx = -1;
      this.by = -1;
      this.bT = -1;
      this.bU = -1;
      this.cJ = -1;
      this.cK = -1;
      this.cL = 1572865;
      this.player.O = 3;
      this.player.L = 1;

      for (int var1 = 7; var1 >= 0; var1--) {
         this.player.N[var1] = 0;
         this.player.P[var1] = 0;
         this.player.M[var1] = b.e[var1];
      }

      for (int var2 = 0; var2 < 10; var2++) {
         this.bu[var2] = -1;
      }

      for (int var3 = 0; var3 < 6; var3++) {
         this.bt[var3] = false;
      }

      for (int var4 = 0; var4 < 2; var4++) {
         this.bv[var4] = -1;
      }

      this.cu = 0;
   }

   public final void a(int var1, short var2) {
      this.m = true;

      try {
         this.bz = this.bw;
         this.bA = this.bx;
         this.bB = this.by;
         this.bV = this.bT;
         this.bW = this.bU;
         this.cM = this.cJ;
         this.cN = this.cK;
         this.cO = this.cL;
         this.dl = this.di;
         this.aY = this.aX;
         this.player.R = this.player.L;
         this.da = this.player.O;
         this.cP = var2;

         for (int var3 = 0; var3 < 8; var3++) {
            this.player.S[var3] = this.player.N[var3];
            this.player.U[var3] = this.player.P[var3];
            this.player.T[var3] = this.player.M[var3];
         }

         this.player.u = -2;
         this.ab = 1;
         this.cS = false;
         this.cD = false;

         for (int var5 = 0; var5 < 10; var5++) {
            this.bu[var5] = -1;
         }

         for (int var6 = 0; var6 < 2; var6++) {
            this.bv[var6] = -1;
         }

         this.dC = 0;
         this.ct = System.currentTimeMillis();
         this.Z = (byte)var1;
         this.levelMap.b(var1);
         this.player.ao = true;
         this.X = var2;
         this.Y = this.X;
         this.player.E = 10;
         this.d = true;
         this.b(this.X, 6541);
         this.levelMap.a(this.X, true);
         if (this.Z == 12) {
            this.N();
         }

         this.player.ab = this.ad * tileWidth + (J >> 1) << 8;
         this.player.aa = this.player.af = this.ae;
         this.player.aj = 1;
         this.player.ak = 0;
         this.player.al = 0;
         this.player.am = 0;
         this.player.ag = 0;
         this.player.Z = 0;
         this.player.ah = 1;
         this.player.ai = 20;
         this.ct = System.currentTimeMillis();
         this.e = true;
         this.cu = 0;
         x = this.af;
         y = this.ag;
         this.d();
         this.cv = this.cw = 0;
         this.b = 0;
         this.updateCamera();
      } catch (Exception var4) {
      }

      this.m = false;
      if (var1 == 1) {
         this.midlet.d(this.ac);
      }
   }

   public final void g(int var1) {
      if (var1 >= 0 && var1 <= 19) {
         this.cJ &= ~(1 << var1);
         if ((var1 == 4 || var1 == 5 || var1 == 6 || var1 == 7 || var1 == 9 || var1 == 10)
            && !this.h(4)
            && !this.h(5)
            && !this.h(6)
            && !this.h(7)
            && !this.h(9)
            && !this.h(10)) {
            this.cJ |= 8;
            return;
         }
      } else if (var1 >= 20 && var1 <= 39) {
         this.cK &= ~(1 << var1 - 20);
      }
   }

   public final boolean h(int var1) {
      if (var1 >= 0 && var1 <= 19) {
         return (this.cJ & 1 << var1) != 0;
      } else {
         return var1 < 20 || var1 > 39 ? false : (this.cK & 1 << var1 - 20) != 0;
      }
   }

   public final void d(int var1, int var2) {
      int var3;
      int var4;
      if (var1 > 0 && var1 <= 5) {
         var3 = (var1 - 1) * 6 + var2;
         var4 = this.bx;
      } else {
         if (var1 < 6 || var1 > 10) {
            this.bw = (byte)(this.bw | 1);
            return;
         }

         var3 = (var1 - 5) * 6 + var2;
         var4 = this.by;
      }

      if ((var4 & 1 << var3) == 0) {
         this.bw = (byte)(this.bw & 254);
      } else {
         this.bw = (byte)(this.bw | 1);
      }
   }

   public final void e(int var1, int var2) {
      if (var1 > 0 && var1 <= 5) {
         int var4 = (var1 - 1) * 6 + var2;
         this.bx &= ~(1 << var4);
      } else {
         if (var1 >= 6 && var1 <= 10) {
            int var3 = (var1 - 5) * 6 + var2;
            this.by &= ~(1 << var3);
         }
      }
   }

   public final boolean f(int var1, int var2) {
      int var3;
      int var4;
      if (var1 > 0 && var1 <= 5) {
         var3 = (var1 - 1) * 6 + var2;
         var4 = this.bT;
      } else {
         if (var1 < 6 || var1 > 10) {
            return false;
         }

         var3 = (var1 - 6) * 6 + var2;
         var4 = this.bU;
      }

      return (var4 & 1 << var3) != 0;
   }

   public final int v() {
      int var2 = 0;

      for (int var1 = (bS >> 1) - 1; var1 >= 0; var1--) {
         if ((this.bT & 1 << var1) == 0) {
            var2++;
         }

         if ((this.bU & 1 << var1) == 0) {
            var2++;
         }
      }

      return var2;
   }

   public final int i(int var1) {
      int var4 = 0;
      if (var1 > 0 && var1 <= 5) {
         int var6 = (var1 - 1) * 6;

         for (int var5 = 0; var5 < 6; var5++) {
            if ((this.bT & 1 << var6 + var5) == 0) {
               var4++;
            }
         }
      } else if (var1 >= 6 && var1 <= 10) {
         int var3 = (var1 - 6) * 6;

         for (int var2 = 0; var2 < 6; var2++) {
            if ((this.bU & 1 << var3 + var2) == 0) {
               var4++;
            }
         }
      }

      return var4;
   }

   public final void g(int var1, int var2) {
      if (var1 > 0 && var1 <= 5) {
         int var4 = (var1 - 1) * 6 + var2;
         this.bT &= ~(1 << var4);
      } else if (var1 >= 6 && var1 <= 10) {
         int var3 = (var1 - 6) * 6 + var2;
         this.bU &= ~(1 << var3);
      }

      if (this.v() >= bR) {
         this.cC = false;
         this.r(72);
         this.player.O = (byte)(this.player.O | 128);
         this.da = (byte)(this.da | 128);
      } else if (this.v() == 1) {
         this.cC = false;
         this.r(74);
      } else {
         this.cC = false;
         this.r(227);
      }
   }

   public final void w() {
      this.player.u = -2;

      for (byte var1 = 0; var1 < 6; var1++) {
         this.bt[var1] = true;
      }

      this.X = this.ah;
      this.b(this.X, 6911);
      this.levelMap.a(this.ah, false);
      this.player.E = 10;
      this.player.ab = this.ad * tileWidth + (J >> 1) << 8;
      this.player.aa = this.ae;
      x = this.af;
      y = this.ag;
      this.player.ag = 0;
      this.player.ai = 20;
      this.player.ad = this.player.ac = 0;
      this.player.a((byte)0);
      this.player.am = 0;
      this.e = true;
      if (this.Z == 12) {
         this.bI[0] = this.bI[1] = this.bI[2] = this.bI[3] = 0;
         this.bH[0] = this.bH[1] = this.bH[2] = this.bH[3] = 0;
         this.bJ[0] = this.bJ[1] = this.bJ[2] = this.bJ[3] = 100;
         this.bJ[4] = 200;
      }
   }

   public final void a(int var1, boolean var2) {
      boolean var6 = false;
      if (var1 == -1) {
         this.ec = this.cu = 0;
         this.W();
         this.midlet.d(this.ac);
         this.y();
         this.b = 3;
         if (this.cV) {
            this.bv[0] = -1;
            this.bv[1] = -1;
            this.cV = false;
            this.X();
            this.b = 1;
            this.player.O = this.da;
         }
      }
   }

   public final void x() {
      if (this.aQ != d.b) {
         this.aQ = d.b;

         try {
            aF = null;
            System.gc();
            sleep(30);
            aF = Image.createImage(aR[this.aQ]);
            return;
         } catch (Exception var2) {
         }
      }
   }

   public final void y() {
      try {
         aF = null;
         System.gc();
         sleep(20);
         aF = Image.createImage(aR[3]);
      } catch (IOException var2) {
      }
   }

   public final void z() {
      try {
         aF = null;
         System.gc();
         sleep(20);
         aF = Image.createImage(aR[this.aQ]);
      } catch (IOException var2) {
      }
   }

   private void s(int var1) {
      if (var1 >= 0 && var1 <= 11) {
         this.cS = false;
         this.cD = false;
         this.cu = 0;
         this.Z = 11;
         this.aQ = 3;
         this.levelMap.b(11);
         this.b(this.X, 7229);

         for (int var2 = 0; var2 < 10; var2++) {
            this.bu[var2] = -1;
         }

         for (int var3 = 0; var3 < 6; var3++) {
            this.bt[var3] = false;
         }

         for (int var4 = 0; var4 < 2; var4++) {
            this.bv[var4] = -1;
         }

         this.levelMap.a(var1, true);
         this.aQ = 1;
         this.player.ab = this.ad * tileWidth + (J >> 1) << 8;
         this.player.aa = this.player.af = this.ae;
         this.player.aj = 1;
         this.player.ak = 0;
         this.player.al = 0;
         this.player.am = 0;
         this.player.ag = 0;
         this.player.Z = 0;
         this.player.ah = 1;
         this.player.ai = 20;
         this.ct = System.currentTimeMillis();
         this.e = true;
         this.player.ao = true;
         this.d = true;
         x = this.af;
         y = this.ag;

         for (int var5 = 0; var5 < 8; var5++) {
            this.player.M[var5] = b.d[var5 * 3 + this.player.P[var5]];
         }

         this.b = 0;
         this.updateCamera();
      }
   }

   public final void A() {
      if (this.cX && this.player.ai != 20) {
         this.player.ai = 0;
      } else {
         if (this.cW && this.cY++ > 80) {
            if (!f) {
               this.player.ai--;
            }

            this.cY = 0;
            this.e = true;
         }

         if (this.cZ && this.player.O != 1 && this.player.M[this.player.L] <= 0) {
            this.y();
            this.b = 19;
         } else {
            for (int var1 = enemyPoolSize - 1; var1 >= 0; var1--) {
               if (this.enemies[var1].ai > 0 && this.enemies[var1].Z != 4 && this.enemies[var1].Z != -1) {
                  return;
               }
            }
         }

         this.cW = false;
         this.cX = false;
         this.cY = 0;

         for (int var2 = 0; var2 < 10; var2++) {
            this.bu[var2] = -1;
         }

         for (int var3 = 0; var3 < 2; var3++) {
            this.bv[var3] = -1;
         }

         this.cu = 0;
         this.cV = false;
         this.player.O = this.da;
         if (this.b != 19) {
            this.y();
            this.b = 15;
            this.aX = this.aX + cl[this.X];
            if (this.aX > 999999) {
               this.aX = 999999;
            }

            if (this.X < 11) {
               this.cL = this.cL | 1 << 21 + this.X;
               return;
            }

            this.player.O = (byte)(this.player.O | 64);
         }
      }
   }

   public final void j(int var1) {
      switch (var1) {
         case 1:
            this.ed = 6;
            this.Y = d.h[this.X][0];
            break;
         case 2:
            this.ed = 5;
            this.Y = d.h[this.X][3];
         case 3:
         case 4:
         default:
            break;
         case 5:
            this.ed = 2;
            this.Y = d.h[this.X][2];
            break;
         case 6:
            this.ed = 1;
            this.Y = d.h[this.X][1];
      }

      if (this.Y != -1) {
         this.X = this.Y;
         this.V();
      }
   }

   private void V() {
      this.m = true;
      this.b(this.X, 7455);
      this.levelMap.a(this.X, false);
      switch (this.ed) {
         case 1:
            this.player.aa = this.player.af = 1;
            y = 0;
            break;
         case 2:
            this.player.ab = 1 * tileWidth + (J >> 1) << 8;
            x = 0;
         case 3:
         case 4:
         default:
            break;
         case 5:
            this.player.ab = 26 * tileWidth + (J >> 1) << 8;
            x = v;
            break;
         case 6:
            this.player.aa = this.player.af = 16;
            y = w;
      }

      this.m = false;
   }

   public final void a(int var1, int var2, int var3) {
      this.player.w = var1 * tileWidth + (tileWidth >> 1) + (this.player.ao ? -5 : 5) << 8;
      this.player.x = var2 * tileHeight + (tileHeight >> 1) - 2 << 8;
      int var4 = 4 * tileWidth / 44;
      if (!this.player.ao) {
         var4 = -var4;
      }

      int var5 = 10 * tileHeight / 44;
      this.player.y = (this.player.ab >> 8) + var4 << 8;
      this.player.z = this.player.aa * tileHeight + (this.player.ag >> 8) + var5 << 8;
      this.player.A = (this.player.w - this.player.y) / 6;
      this.player.B = (this.player.x - this.player.z) / 6;
      if (this.player.x < this.player.z) {
         this.player.tileWidth = (byte)var3;
         this.player.u = 0;
         this.player.a((byte)5);
         this.player.am = 2;
      }
   }

   public final int b(int var1, int var2, int var3) {
      int var4 = 18 * tileHeight;
      int var5 = var2 / tileHeight;
      if ((d.e[var1 / tileWidth] & 1 << var5 + 1) > 0) {
         var4 = (var5 + 1) * tileHeight;
      }

      return var4;
   }

   public final short B() {
      short var1 = (short)(18 * tileHeight);
      int var2 = 0;
      if (this.player.t != 0) {
         return var1;
      }

      for (int var3 = 3; var3 >= 0; var3--) {
         if (this.cq[var3] != -1
            && this.cm[var3] + this.co[var3] <= (this.player.ab >> 8) + 8
            && this.cm[var3] + this.co[var3] + tileWidth >= (this.player.ab >> 8) - 8
            && (var2 = this.cn[var3] + this.cp[var3]) < var1
            && var2 > this.player.c()) {
            var1 = (short)var2;
            this.player.hudHeight = 0;
            if (this.cq[var3] == 0) {
               this.player.hudHeight = 2;
            } else if (this.cq[var3] == 2) {
               this.player.hudHeight = -2;
            }
         }
      }

      return (short)(var1 - tileHeight);
   }

   public final short C() {
      short var1 = (short)(18 * tileHeight);
      short var2 = this.player.c();
      int var3 = this.player.ab >> 8;
      boolean var4 = false;
      int var5 = aJ.getWidth();

      for (int var6 = 49; var6 >= 0; var6--) {
         if (this.bn[var6] != -1 && this.bm[var6] >= var2 && this.bl[var6] <= var3 + 8 && this.bl[var6] + var5 >= var3 - 8 && this.bm[var6] < var1) {
            var1 = this.bm[var6];
         }
      }

      return (short)(var1 - tileHeight);
   }

   public final boolean D() {
      int var1 = aJ.getWidth();
      int var2 = aJ.getHeight() >> 3;
      byte var3 = tileWidth;
      byte var4 = tileHeight;
      boolean var5 = false;
      int var6 = this.player.ab >> 8;
      short var7 = this.player.c();
      boolean var8 = false;
      int var9 = var4 - 1;

      for (int var10 = 49; var10 >= 0; var10--) {
         if (this.bn[var10] >= 0
            && this.abs(this.bl[var10] - var6) <= var3
            && this.abs(this.bm[var10] - var7) <= var4
            && this.a(this.bl[var10], this.bm[var10], var1, var2, var6 - 8, var7, 16, var9)) {
            return true;
         }
      }

      return false;
   }

   public final boolean k(int var1) {
      short var9 = this.player.b();
      int var10 = this.player.c() + L;
      boolean var11 = false;
      boolean var12 = false;
      boolean var13 = false;
      boolean var14 = false;
      byte var2 = this.enemies[var1].Z;
      if (this.enemies[var1].Z != -1 && var2 != 4) {
         if (this.abs(this.enemies[var1].ab - this.player.ab) <= tileWidth << 8 && this.abs(this.enemies[var1].aa - this.player.aa) <= 3 && this.enemies[var1].al != 5) {
            short var3 = this.enemies[var1].c();
            short var4 = this.enemies[var1].b();
            byte var5 = f.a[var2];
            byte var6 = f.b[var2];
            byte var7 = f.c[var2];
            byte var8 = f.d[var2];
            return this.a(var3 - var5, var4 + var6, var7, var8, var9 - 11, var10 + 7, 18, 37);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public final boolean E() {
      int var1 = aJ.getWidth();
      byte var2 = tileWidth;
      int var3 = this.player.ab >> 8;
      short var4 = this.player.c();
      int var5 = 0;

      for (int var6 = 49; var6 >= 0; var6--) {
         if (this.bn[var6] >= 0
            && ((var5 = this.bl[var6] + (var1 >> 1)) <= var3 || this.player.ao)
            && (var5 >= var3 || !this.player.ao)
            && this.abs(var5 - var3) <= var2
            && this.bm[var6] == var4) {
            return true;
         }
      }

      return false;
   }

   public final boolean F() {
      for (int var2 = enemyPoolSize - 1; var2 >= 0; var2--) {
         byte var1 = this.enemies[var2].Z;
         if (this.enemies[var2].Z != -1
            && var1 != 4
            && (this.enemies[var2].ab <= this.player.ab || this.player.ao)
            && (this.enemies[var2].ab >= this.player.ab || !this.player.ao)
            && this.abs(this.enemies[var2].ab - this.player.ab) <= tileWidth << 8
            && this.enemies[var2].al != 5
            && this.player.aa == this.enemies[var2].aa) {
            return true;
         }
      }

      return false;
   }

   public final int G() {
      short var8 = this.player.b();
      int var9 = this.player.c() + L;
      boolean var10 = false;
      boolean var11 = false;
      boolean var12 = false;
      boolean var13 = false;

      for (int var14 = enemyPoolSize - 1; var14 >= 0; var14--) {
         byte var1 = this.enemies[var14].Z;
         if (this.enemies[var14].Z != -1
            && var1 != 4
            && this.abs(this.enemies[var14].ab - this.player.ab) <= tileWidth << 8
            && this.abs(this.enemies[var14].aa - this.player.aa) <= 3
            && this.enemies[var14].al != 5) {
            short var2 = this.enemies[var14].c();
            short var3 = this.enemies[var14].b();
            byte var4 = f.a[var1];
            byte var5 = f.b[var1];
            byte var6 = f.c[var1];
            byte var7 = f.d[var1];
            if (this.a(var2 - var4, var3 + var5, var6, var7, var8 - 11, var9 + 7, 18, 37)) {
               return var14;
            }
         }
      }

      return -1;
   }

   public final void h(int var1, int var2) {
      if (var2 == -1 || var1 == 50) {
         this.cu = this.a(this.cu, (byte)0, (byte)3);
      } else if (var2 != -2 && var1 != 56) {
         if (var1 == 53 || var2 == -5 || var1 == -6 || var1 == -7) {
            if (var1 == -7) {
               this.cu = 0;
            }

            this.midlet.c(3);
            if (this.cu != 0) {
               if (this.cu == 1) {
                  this.b = 5;
               } else if (this.cu == 2) {
                  this.b = 8;
               } else if (this.cu == 3) {
                  this.b = 7;
               }
            } else {
               if (this.c == 0 || this.c == 17 || this.c == 16 || this.c == 24) {
                  this.ct = System.currentTimeMillis();
                  this.z();
               }

               this.b = this.c;
               this.d = true;
               if (this.b == 0) {
                  this.e = true;
               }
            }

            this.cu = 0;
         }
      } else {
         this.cu = this.b(this.cu, (byte)3, (byte)0);
      }
   }

   public final void i(int var1, int var2) {
      if (var1 != 53 && var2 != -5 && var1 != -6) {
         if (var1 == -7) {
            this.cu = 0;
            this.b = 4;
            this.midlet.c(3);
            this.midlet.writeSoundAndLanguageSettings((byte)(this.midlet.soundEnabled ? 1 : 0));
         }
      } else if (this.cu == 0) {
         if (this.midlet.soundEnabled) {
            this.midlet.soundEnabled = false;
         } else {
            this.midlet.soundEnabled = true;
         }

         this.midlet.writeSoundAndLanguageSettings((byte)(this.midlet.soundEnabled ? 1 : 0));
         return;
      }
   }

   public final void j(int var1, int var2) {
      if (var2 == -1 || var1 == 50) {
         this.cu = this.a(this.cu, (byte)0, (byte)2);
      } else if (var2 == -2 || var1 == 56) {
         this.cu = this.b(this.cu, (byte)2, (byte)0);
      } else if (var1 != 53 && var2 != -5 && var1 != -6) {
         if (var1 == -7) {
            this.dZ = true;
         }
      } else {
         this.ec = this.cu;
         this.midlet.c(3);
         if (this.midlet.saveSlotFlags[this.cu] != 0) {
            this.b = 9;
         } else {
            this.f(0);
            this.ac = this.ec;
            this.midlet.e();
         }

         this.cu = 0;
      }
   }

   public final void k(int var1, int var2) {
      if (var1 == -7) {
         this.midlet.c(3);
         this.ec = this.cu = 0;
         this.W();
         this.player.L = this.cU;
         this.b = 3;
      } else {
         if (var1 != 52 && var2 != -3) {
            if (var1 == 54 || var2 == -4) {
               if (++this.cu > 7) {
                  this.cu = 0;
               }

               while (this.cu > 3 && (this.player.O & 1 << this.cu + 1) == 0 || this.cu == 5) {
                  if (this.cu == 7) {
                     return;
                  }

                  if (++this.cu > 7) {
                     this.cu = 0;
                  }
               }
            } else if (var1 == 53 || var2 == -5 || var1 == -6) {
               this.midlet.c(3);
               if (this.cu != 2 || !this.h(3)) {
                  if (this.cu == 3 && this.h(8)) {
                     return;
                  }

                  if (this.cu == 7 && this.player.Q <= 0) {
                     return;
                  }

                  if (this.cu != 7 && (this.player.O & 1 << this.cu + 1) == 0) {
                     this.b = 12;
                     this.ec = this.cu;
                     this.cu = 0;
                     return;
                  }

                  if (this.cu != 7 && this.player.M[this.cu + 1] >= b.d[3 * (this.cu + 1) + this.player.P[this.cu + 1]]) {
                     return;
                  }

                  this.b = 12;
                  this.ec = this.cu;
                  this.cu = 0;
               }
            }
         } else {
            if (--this.cu < 0) {
               this.cu = 7;
            }

            while (this.cu > 3 && (this.player.O & 1 << this.cu + 1) == 0 || this.cu == 5) {
               if (this.cu == 7) {
                  return;
               }

               if (--this.cu < 0) {
                  this.cu = 7;
               }
            }
         }
      }
   }

   public final void l(int var1, int var2) {
      if (var2 == -2 || var1 == 56) {
         this.cu = this.b(this.cu, (byte)1, (byte)0);
      } else if (var2 == -1 || var1 == 50) {
         this.cu = this.a(this.cu, (byte)0, (byte)1);
      } else if (var1 != 53 && var2 != -5 && var1 != -6) {
         if (var1 == -7) {
            this.midlet.c(3);
            this.cu = this.ec;
            this.b = 11;
            this.player.Q = this.Y();
         }
      } else {
         this.midlet.c(3);
         if (this.cu == 1) {
            if (this.ec == 7) {
               if (this.aX < this.player.Q) {
                  this.cu = 0;
                  this.b = 13;
                  return;
               }

               this.aX = this.aX - this.player.Q;

               for (int var3 = 1; var3 < 8; var3++) {
                  if ((this.player.O & 1 << var3) > 0) {
                     this.player.M[var3] = b.d[var3 * 3 + this.player.P[var3]];
                  }
               }
            } else if ((this.player.O & 1 << this.ec + 1) == 0) {
               if (this.aX < b.h[this.ec]) {
                  this.cu = 0;
                  this.b = 13;
                  return;
               }

               this.aX = this.aX - b.h[this.ec];
               this.player.O = (byte)(this.player.O | 1 << this.ec + 1);
            } else {
               if (this.aX < this.aZ[this.ec + 1]) {
                  this.cu = 0;
                  this.b = 13;
                  return;
               }

               this.aX = this.aX - this.aZ[this.ec + 1];
               this.player.M[this.ec + 1] = (short)(this.player.M[this.ec + 1] + b.d[(this.ec + 1) * 3 + this.player.P[this.ec + 1]]);
               if (this.player.M[this.ec + 1] > b.d[(this.ec + 1) * 3 + this.player.P[this.ec + 1]]) {
                  this.player.M[this.ec + 1] = b.d[(this.ec + 1) * 3 + this.player.P[this.ec + 1]];
               }
            }
         }

         this.cu = this.ec;
         this.b = 11;
         this.player.Q = this.Y();
      }
   }

   public final void m(int var1, int var2) {
      byte var3 = this.cu;
      boolean var4 = false;
      if (var2 == -1 || var1 == 50) {
         this.cu = N[this.cu];

         while ((this.cL & 1 << this.cu) == 0) {
            this.cu = N[this.cu];
         }

         if (this.cu == 19 && this.cL == 1572865) {
            this.cu = var3;
            return;
         }
      } else if (var2 == -2 || var1 == 56) {
         this.cu = O[this.cu];

         while ((this.cL & 1 << this.cu) == 0) {
            this.cu = O[this.cu];
         }

         if (this.cu == 19 && this.cL == 1572865) {
            this.cu = var3;
            return;
         }
      } else if (var2 == -4 || var1 == 54) {
         this.cu = Q[this.cu];

         while ((this.cL & 1 << this.cu) == 0) {
            this.cu = Q[this.cu];
         }

         if (this.cu == 19 && this.cL == 1572865) {
            this.cu = var3;
            return;
         }
      } else if (var2 == -3 || var1 == 52) {
         this.cu = P[this.cu];

         while ((this.cL & 1 << this.cu) == 0) {
            this.cu = P[this.cu];
         }

         if (this.cu == 19 && this.cL == 1572865) {
            this.cu = var3;
            return;
         }
      } else if (var1 == 53 || var2 == -5 || var1 == -6) {
         if (this.cu == 19) {
            if (this.cL != 1572865) {
               this.player.ao = true;
               this.player.a((byte)0);
               this.player.am = 0;
               this.player.aa = 2;
               this.player.ab = 22528;
               this.player.ac = 0;
               this.player.ad = 0;
               this.player.E = 0;
               this.player.ag = 0;
               this.b = 11;
               this.cU = this.player.L;
               this.player.Q = this.Y();
               this.cu = 0;
               return;
            }
         } else if (this.cu == 18) {
            this.bv[0] = -1;
            this.bv[1] = -1;
            this.X();
            this.b = 1;

            for (int var5 = 0; var5 < 8; var5++) {
               this.player.T[var5] = this.player.M[var5];
            }
         } else {
            if (this.cu == 0 && this.h(0)) {
               this.o = 0;
            } else if (this.cu == 1 && this.h(4)) {
               this.o = 5;
            } else if (this.cu == 2 && this.h(1)) {
               this.o = 0;
            } else if (this.cu == 3 && this.h(5)) {
               this.o = 5;
            } else if (this.cu == 4 && this.h(2)) {
               this.o = 1;
            } else if (this.cu == 5 && (this.bw & 2) != 0) {
               this.o = 3;
            } else if (this.cu == 6 && this.h(3)) {
               this.o = 3;
            } else if (this.cu == 7 && (this.bw & 4) != 0) {
               this.o = 2;
            } else if (this.cu == 8 && this.h(13)) {
               this.o = 0;
            } else if (this.cu == 9 && this.h(8)) {
               this.o = 0;
            } else if (this.cu == 10 && this.h(8)) {
               this.o = 5;
            } else if (this.cu == 11 && this.h(12)) {
               this.o = 0;
            } else if (this.cu == 12 && this.h(9)) {
               this.o = 5;
            } else if (this.cu == 13 && this.h(7)) {
               this.o = 5;
            } else if (this.cu == 14 && this.h(10)) {
               this.o = 5;
            } else if (this.cu == 15 && this.h(11)) {
               this.o = 4;
            } else if (this.cu == 16 && this.h(14)) {
               this.o = 0;
            } else if (this.cu == 17) {
               this.o = 6;
            } else {
               this.o = -1;
            }

            this.aQ = 3;
            this.a(this.cQ[this.cu], this.cR[this.cu]);
            this.cu = 0;
         }
      }
   }

   public final void n(int var1, int var2) {
      if (var1 == 53 || var2 == -5 || var1 == -6) {
         dB++;
         System.gc();
         sleep(50);
         if (dB == 3 || dB == 2 && !dG && !dF) {
            this.ec = this.cu = 0;
            this.b = 21;
            dB = 0;
            return;
         }

         a(dB);
      }
   }

   public final void o(int var1, int var2) {
      if (var1 == 53 || var2 == -5 || var1 == -6) {
         this.ec = this.cu = 0;
         if (this.c == 21) {
            this.W();
            this.b = 3;
         } else if (this.c == 15) {
            this.X();
            this.b = 1;
         }

         this.midlet.c(3);
      }
   }

   public final void p(int var1, int var2) {
      this.cu = 0;
      this.b = 11;
      this.player.Q = this.Y();
      this.midlet.c(3);
   }

   public final void q(int var1, int var2) {
      if (var1 == 53 || var2 == -5 || var1 == -6) {
         az = null;
         System.gc();
         sleep(20);
         if (!this.aa) {
            this.cu = 1;
            this.b = 23;
            return;
         }

         this.dZ = true;
      }
   }

   public final void r(int var1, int var2) {
      if (var1 == 53 || var2 == -5 || var1 == -6) {
         this.bv[0] = -1;
         this.bv[1] = -1;
         this.y();
         if (this.b == 19) {
            this.X();
            this.b = 1;
         } else if (this.b == 15) {
            this.c = this.b;
            this.b = 10;
            this.midlet.d(this.ac);
         }

         this.e = true;
         this.d = true;
      }
   }

   public final void s(int var1, int var2) {
      if (var1 != 53 && var2 != -5 && var1 != -6) {
         if (var1 == -7) {
            this.X();
            this.b = 1;
            this.e = true;
            this.d = true;
            this.player.O = this.da;
            this.cV = false;
         }
      } else {
         this.s(this.ec);
         this.e = true;
         this.d = true;
      }
   }

   public final void t(int var1, int var2) {
      if (var2 != -2 && var1 != 56) {
         if (var2 != -1 && var1 != 50) {
            if (var1 != 53 && var2 != -5 && var1 != -6) {
               if (var1 == -7) {
                  this.ec = this.cu = 0;
                  this.W();
                  this.b = 3;
                  if ((this.player.O & 1 << this.player.L) == 0 || this.player.L == 0) {
                     this.player.L = 1;
                  }

                  for (int var3 = 0; var3 < 8; var3++) {
                     this.player.M[var3] = this.player.T[var3];
                  }

                  this.midlet.c(3);
               }
            } else {
               this.da = this.player.O;
               this.cV = true;
               this.cW = false;
               this.cX = false;
               this.cZ = false;
               this.cY = 0;
               switch (this.cu) {
                  case 0:
                     this.o = 7;
                     break;
                  case 1:
                     this.o = 7;
                     break;
                  case 2:
                     this.cZ = true;
                     this.player.O = 2;
                     this.player.L = 1;
                     this.o = 12;
                     break;
                  case 3:
                     this.o = 7;
                     break;
                  case 4:
                     this.cZ = true;
                     this.player.O = 1;
                     this.player.L = 0;
                     this.o = 8;
                     break;
                  case 5:
                     this.cZ = true;
                     this.player.O = 2;
                     this.player.L = 1;
                     this.o = 12;
                     break;
                  case 6:
                     this.cW = true;
                     this.o = 9;
                     break;
                  case 7:
                     this.cX = true;
                     this.o = 10;
                     break;
                  case 8:
                     this.o = 7;
                     break;
                  case 9:
                     this.cX = true;
                     this.o = 10;
                     break;
                  case 10:
                     this.cW = true;
                     this.o = 9;
                     break;
                  case 11:
                     this.cZ = true;
                     this.player.O = 64;
                     this.player.L = 6;
                     this.o = 11;
               }

               this.X = this.cu;
               this.ec = this.cu;
               this.b = 14;
            }
         } else if (this.cu != 0) {
            this.cu--;
            if (this.cu >= 8) {
               dk = 2;
            } else if (this.cu >= 4) {
               dk = 1;
            } else {
               dk = 0;
            }
         }
      } else if ((this.cL & 1 << this.cu + 21) != 0 && this.cu != 11) {
         this.cu++;
         if (this.cu >= 8) {
            dk = 2;
         } else if (this.cu >= 4) {
            dk = 1;
         } else {
            dk = 0;
         }
      }
   }

   public final void u(int var1, int var2) {
      if (var2 == -1 || var1 == 50) {
         this.cu = this.a(this.cu, (byte)0, (byte)1);
      } else if (var2 != -2 && var1 != 56) {
         if (var1 == 53 || var2 == -5 || var1 == -6) {
            this.midlet.c(3);
            if (this.cu == 0) {
               this.dZ = true;
               if (dA > 0) {
                  return;
               }
            } else if (this.cu == 1) {
               this.O();
            }
         }
      } else {
         this.cu = this.b(this.cu, (byte)1, (byte)0);
      }
   }

   public final void v(int var1, int var2) {
      int var3 = 0;
      if (var1 != 54 && var1 != 50 && var2 != -4 && var2 != -1) {
         if (var1 == 52 || var1 == 56 || var2 == -3 || var2 == -2) {
            for (this.player.L = S[this.player.L]; (this.player.O & 1 << this.player.L) == 0 && var3 < 8; var3++) {
               this.player.L = S[this.player.L];
            }
         } else if (var2 == -5 || var1 == 53) {
            this.ec = this.cu = 0;
            this.ct = System.currentTimeMillis();
            this.b = 0;
            this.e = true;
            this.updateCamera();
         }
      } else {
         for (this.player.L = R[this.player.L]; (this.player.O & 1 << this.player.L) == 0 && var3 < 8; var3++) {
            this.player.L = R[this.player.L];
         }
      }
   }

   public final void w(int var1, int var2) {
      if (var2 == -1 || var1 == 50) {
         this.cu = this.a(this.cu, (byte)0, (byte)1);
      } else if (var2 == -2 || var1 == 56) {
         this.cu = this.b(this.cu, (byte)1, (byte)0);
      } else if (var1 != 53 && var2 != -5 && var1 != -6) {
         if (var1 == -7) {
            this.midlet.c(3);
            this.cu = 0;
            this.b = 6;
         }
      } else {
         this.midlet.c(3);
         if (this.cu == 0) {
            this.midlet.e();
            this.b = 6;
         } else if (this.cu == 1) {
            this.ac = this.ec;
            this.f(0);
         }

         this.cu = 0;
      }
   }

   public final void x(int var1, int var2) {
      if (var2 == -1 || var1 == 50) {
         this.cu = this.a(this.cu, (byte)0, (byte)1);
      } else if (var2 != -2 && var1 != 56) {
         if (var1 == 53 || var2 == -5 || var1 == -6) {
            this.midlet.c(3);
            if (this.cu == 0) {
               this.c = this.b;
               this.b = 10;
               dA = dA + dz;
               this.midlet.d(this.ac);
               return;
            }

            if (this.cu == 1) {
               this.bw = this.bz;
               this.bx = this.bA;
               this.by = this.bB;
               this.bT = this.bV;
               this.bU = this.bW;
               this.cJ = this.cM;
               this.cK = this.cN;
               this.cL = this.cO;
               this.di = this.dl;
               this.aX = this.aY;
               this.player.L = this.player.R;
               this.player.O = this.da;

               for (int var3 = 0; var3 < 8; var3++) {
                  this.player.N[var3] = this.player.S[var3];
                  this.player.P[var3] = this.player.U[var3];
                  this.player.M[var3] = this.player.T[var3];
               }

               if (this.Z == 1) {
                  this.bZ = true;
               }

               this.aQ = 3;
               this.a(this.Z, this.cP);
            }
         }
      } else {
         this.cu = this.b(this.cu, (byte)1, (byte)0);
      }
   }

   public final void y(int var1, int var2) {
      if (var2 == -1 || var1 == 50) {
         this.cu = this.a(this.cu, (byte)0, (byte)1);
      } else if (var2 != -2 && var1 != 56) {
         if (var1 != 53 && var2 != -5 && var1 != -6) {
            if (var1 == -7) {
               this.midlet.c(3);
               this.cu = 0;
               this.b = 4;
            }
         } else {
            if (this.cu == 0) {
               this.b = 4;
               this.cu = 0;
               this.midlet.c(3);
               return;
            }

            if (this.cu == 1) {
               this.cV = true;
               this.player.O = this.da;
               this.player.L = 1;
               this.cV = false;
               if (this.Z != 0) {
                  this.midlet.d(this.ac);
               }

               this.dZ = true;
               return;
            }
         }
      } else {
         this.cu = this.b(this.cu, (byte)1, (byte)0);
      }
   }

   public final void z(int var1, int var2) {
      if (var2 == -1 || var1 == 50) {
         this.cu = this.a(this.cu, (byte)0, (byte)1);
      } else if (var2 != -2 && var1 != 56) {
         if (var1 != 53 && var2 != -5 && var1 != -6) {
            if (var1 == -7) {
               this.cu = 0;
               this.b = 4;
               this.midlet.c(3);
            }
         } else {
            if (this.cu == 0) {
               this.b = 4;
               this.cu = 0;
               this.midlet.c(3);
               return;
            }

            if (this.cu == 1) {
               this.cV = true;
               this.player.O = this.da;
               this.player.L = 1;
               this.cV = false;
               if (this.Z != 0) {
                  this.midlet.d(this.ac);
               }

               this.midlet.c();
               this.midlet.notifyDestroyed();
               return;
            }
         }
      } else {
         this.cu = this.b(this.cu, (byte)1, (byte)0);
      }
   }

   public final void A(int var1, int var2) {
      if (this.cD) {
         if (var2 == -5 || var1 == 53) {
            if (this.cF >= 0) {
               this.cF = this.cG;
            }

            if (this.cF < 0) {
               this.S();
               this.cG = 0;
            }
         }
      } else if (this.b != 16 && this.b != 17 && this.b != 24) {
         if (var1 == -6 || var1 == -7) {
            this.cu = 0;
            this.c = this.b;
            this.b = 4;
            this.y();
         } else if (this.player.al == 10) {
            this.cv = this.cw = 0;
            this.cx &= -129;
            this.cy = 0L;
         } else {
            if (var2 == -1) {
               if (this.player.al == 11) {
                  return;
               }

               byte var7 = 0;
               if (this.player.u == -1 && this.player.al != 12) {
                  this.player.o();
               } else if (this.player.s >= 0) {
                  if (this.player.g() && !this.player.ao && this.ea != -1) {
                     if (!z) {
                        z = true;
                        E = this.player.b() - 8 + (tileWidth >> 1);
                     }

                     var7 = -1;
                     this.ea = -1;
                     this.player.ad = 2500;
                     this.player.s = 0;
                     this.player.ao = true;
                     this.player.a((byte)4);
                     this.cw = this.cv = 0;
                  } else if (this.player.h() && this.player.ao && this.ea != 1) {
                     if (!z) {
                        z = true;
                        E = this.player.b() + 8 - (tileWidth >> 1);
                     }

                     var7 = 1;
                     this.ea = 1;
                     this.player.ad = -2500;
                     this.player.s = 0;
                     this.player.ao = false;
                     this.player.a((byte)4);
                     this.cw = this.cv = 0;
                  }
               }

               if (this.player.s < 1 || var7 != 0) {
                  this.cv = var2;
                  return;
               }
            } else if (var2 == -2) {
               if (this.player.al == 5) {
                  return;
               }

               if (this.player.s == 2) {
                  this.player.a((byte)3);
                  this.player.ad = 0;
                  this.player.ac = 0;
                  this.player.s = 1;
                  if ((this.player.O & 1) > 0) {
                     this.cw = 0;
                     this.player.a((byte)14);
                     this.player.am = 1;
                     this.player.D = 1;
                     return;
                  }
               } else {
                  if ((this.player.O & 1) > 0 && this.player.s != -1 && this.player.al != 11 && this.player.al != 14) {
                     this.cw = 0;
                     this.player.a((byte)14);
                     this.player.am = 1;
                     this.player.D = 1;
                     return;
                  }

                  if (B) {
                     this.a(C, D);
                     return;
                  }

                  if (this.player.tileHeight) {
                     this.player.tileHeight = false;
                     this.player.aa++;
                     this.player.k();
                     this.player.t = 10;
                     if ((this.player.O & 1) > 0 && this.player.al != 14) {
                        this.cw = 0;
                        this.player.a((byte)14);
                        this.player.am = 1;
                        this.player.D = 1;
                        return;
                     }
                  } else if (this.player.f() && this.player.ac == 0 && this.player.al != 14 && this.player.al != 11) {
                     this.player.k();
                     this.player.aa++;
                     this.player.ag = tileHeight - 2;
                     if ((this.player.O & 1) > 0) {
                        this.cw = 0;
                        this.player.a((byte)14);
                        this.player.am = 1;
                        this.player.D = 1;
                        return;
                     }
                  }
               }
            } else if (var2 == -3) {
               this.cw = this.cv = var2;
               if (z && this.player.b() < E + (tileWidth >> 1) && this.player.ad > 0) {
                  this.cw = this.cv = 0;
                  return;
               }
            } else if (var2 == -4) {
               this.cw = this.cv = var2;
               if (z && this.player.b() > E - (tileWidth >> 1) && this.player.ad < 0) {
                  this.cw = this.cv = 0;
                  return;
               }
            } else if (var1 == 53 || var2 == -5) {
               byte var3 = this.player.u();
               byte var4 = (byte)((this.player.c() + K) / tileHeight);
               short var5;
               int var6 = var5 = this.levelMap.a(var3, var4 - 1);
               var6 -= 36;
               var6 = 1 << var6;
               if ((this.player.O & 1) > 0
                  && this.player.s == -1
                  && this.player.al != 11
                  && this.player.al != 14
                  && this.player.al != 8
                  && (this.F() || this.E() || this.player.M[this.player.L] <= 0 || (this.bw & var6) != 0 && var5 >= 36 && var5 <= 42)) {
                  this.cw = 0;
                  this.player.D = 0;
                  this.player.q();
                  return;
               }

               if (this.player.M[this.player.L] > 0 && this.cx == 0) {
                  this.player.p();
                  this.cx = 129;
                  dD++;
                  this.cy = System.currentTimeMillis();
                  return;
               }
            } else if (var1 == 42) {
               this.cw = 0;
               if ((this.player.O & 1) > 0 && this.player.s == -1) {
                  this.player.D = 0;
                  this.player.q();
                  return;
               }
            } else if (var1 == 35) {
               this.cu = 0;
               this.b = 22;
               if (this.player.L != 0) {
                  if (++this.player.L > 7) {
                     this.player.L = 1;
                  }

                  while ((this.player.O & 1 << this.player.L) == 0) {
                     if (++this.player.L > 7) {
                        this.player.L = 1;
                     }
                  }

                  this.e = true;
                  return;
               }
            } else {
               if (var1 == 55) {
                  if (!this.r) {
                     return;
                  }

                  if (this.Z == 12) {
                     this.bJ[4] = 0;
                     return;
                  }

                  this.cC = true;
                  this.cI = cz[this.Z - 1];
                  this.r(cA[this.Z - 1]);
                  this.g(this.Z - 1);
                  if (cA[this.Z - 1] == 112 && (this.bw & 2) == 0) {
                     this.dC = 248;
                     dy = 16;
                     return;
                  }

                  this.dC = 233 + (this.Z - 1);
                  dy = (byte)(this.Z - 1);
                  return;
               }

               if (var1 == 49) {
                  if (!z && this.player.al != 11) {
                     if (this.player.u < 0 && this.player.al != 5) {
                        if (this.player.al != 12) {
                           this.player.a((byte)0);
                        }

                        if (this.player.s < 0) {
                           this.player.ac = 3584;
                           this.player.s++;
                        } else if (this.player.s < 1) {
                           this.player.ac = 3072;
                           this.player.s++;
                           this.player.am = 1;
                           this.player.a((byte)13);
                        }

                        this.cv = -3;
                        return;
                     }

                     return;
                  }

                  return;
               }

               if (var1 == 51) {
                  if (!z && this.player.al != 11) {
                     if (this.player.u < 0 && this.player.al != 5) {
                        if (this.player.al != 12) {
                           this.player.a((byte)0);
                        }

                        if (this.player.s < 0) {
                           this.player.ac = 3584;
                           this.player.s++;
                        } else if (this.player.s < 1) {
                           this.player.ac = 3072;
                           this.player.s++;
                           this.player.am = 1;
                           this.player.a((byte)13);
                        }

                        this.cv = -4;
                        return;
                     }

                     return;
                  }

                  return;
               }

               if (var1 == 57) {
                  if (!this.r) {
                     return;
                  }

                  this.g(this.Z, this.X);
                  return;
               }

               if (var1 == 48) {
                  if (!this.r) {
                     return;
                  }

                  if (!f) {
                     f = true;
                     System.out.println("lifeCheat=" + f);
                     return;
                  }

                  if (this.cL == -1) {
                     this.bx = 0;
                     this.by = 0;
                     this.bw = 0;
                     return;
                  }

                  if (this.player.O == -1) {
                     this.cL = -1;
                     return;
                  }

                  this.player.O = -1;
               }
            }
         }
      }
   }

   public final void B(int var1, int var2) {
      if (var1 == 53 || var2 == -5) {
         this.cx &= -129;
         if (this.player.al != 8) {
            this.player.r();
         }

         this.cy = 0L;
      } else if ((this.cx & 128) != 0 && this.cy != 0L && System.currentTimeMillis() - this.cy > 5000L) {
         this.cx &= -129;
         if (this.player.al != 8) {
            this.player.r();
         }

         this.cy = 0L;
      }

      if (var2 == this.cv) {
         this.cv = 0;
      }

      if (var2 == this.cw) {
         this.cw = 0;
      }
   }

   public final void l(int var1) {
      int var2 = aJ.getWidth();
      int var3 = aJ.getHeight() >> 3;
      byte var4 = this.enemyProjectiles[var1].f;
      if (this.enemyProjectiles[var1].f != -1 && var4 != 30 && (var4 < 15 || var4 > 17)) {
         int var5 = (this.enemyProjectiles[var1].g >> 8) - this.enemyProjectiles[var1].p;
         int var6 = (this.enemyProjectiles[var1].h >> 8) - this.enemyProjectiles[var1].q;
         int var7 = this.enemyProjectiles[var1].p << 1;
         int var8 = this.enemyProjectiles[var1].q << 1;

         for (int var9 = 49; var9 >= 0; var9--) {
            if (this.bn[var9] != -1
               && this.bl[var9] + x <= 176
               && this.bl[var9] + var2 + x >= 0
               && this.bm[var9] + y <= 220
               && this.bm[var9] + var3 + y >= 0
               && this.a(this.bl[var9], this.bm[var9], var2, var3, var5, var6, var7, var8)) {
               this.enemyProjectiles[var1].a(true);
            }
         }
      }
   }

   public final void m(int var1) {
      int var2 = aJ.getWidth();
      int var3 = aJ.getHeight() >> 3;
      if (var1 == -1) {
         for (int var10 = 49; var10 >= 0; var10--) {
            if (this.bn[var10] != -1
               && this.abs(this.bl[var10] - (this.player.ab >> 8)) <= 3 * tileWidth >> 1
               && this.abs(this.bm[var10] - this.player.c()) <= tileHeight * 2
               && this.a(
                  this.bl[var10],
                  this.bm[var10] + this.bo[var10],
                  aJ.getWidth(),
                  aJ.getHeight() >> 2,
                  (this.player.ab >> 8) + (this.player.ao ? b.o[this.player.D] : -b.o[this.player.D] - b.q[this.player.D]),
                  this.player.c() + b.p[this.player.D],
                  b.q[this.player.D],
                  b.r[this.player.D]
               )) {
               this.midlet.c(4);
               this.n(var10);
               dE++;
               if (this.bn[var10] == 0) {
                  this.c(this.bl[var10] + (var2 >> 1), this.bm[var10] + this.bo[var10], 0);
                  this.c(this.bl[var10] + (var2 >> 1), this.bm[var10] + this.bo[var10], 0);
               } else {
                  this.c(this.bl[var10] + (var2 >> 1), this.bm[var10] + this.bo[var10], this.bn[var10]);
               }

               this.e(this.bl[var10] + (var2 >> 1) << 8, this.bm[var10] + (var3 >> 1) + this.bo[var10] << 8, 30);
               this.bn[var10] = -1;
            }
         }
      } else {
         byte var4 = this.playerProjectiles[var1].f;
         if (this.playerProjectiles[var1].f != -1 && var4 != 30 && (var4 < 15 || var4 > 17)) {
            int var5 = (this.playerProjectiles[var1].g >> 8) - this.playerProjectiles[var1].p;
            int var6 = (this.playerProjectiles[var1].h >> 8) - this.playerProjectiles[var1].q;
            int var7 = this.playerProjectiles[var1].p << 1;
            int var8 = this.playerProjectiles[var1].q << 1;

            for (int var9 = 49; var9 >= 0; var9--) {
               if (this.bn[var9] != -1
                  && this.bl[var9] + x <= 176
                  && this.bl[var9] + var2 + x >= 0
                  && this.bm[var9] + y <= 220
                  && this.bm[var9] + var3 + y >= 0
                  && this.a(this.bl[var9], this.bm[var9] + this.bo[var9], var2, var3, var5, var6, var7, var8)) {
                  this.n(var9);
                  dE++;
                  if (this.bn[var9] == 0) {
                     this.c(this.bl[var9] + (var2 >> 1), this.bm[var9] + this.bo[var9], 0);
                     this.c(this.bl[var9] + (var2 >> 1), this.bm[var9] + this.bo[var9], 0);
                  } else {
                     this.c(this.bl[var9] + (var2 >> 1), this.bm[var9] + this.bo[var9], this.bn[var9]);
                  }

                  this.e(this.bl[var9] + (var2 >> 1) << 8, this.bm[var9] + (var3 >> 1) + this.bo[var9] << 8, 30);
                  this.bn[var9] = -1;
                  this.playerProjectiles[var1].a(false);
               }
            }
         }
      }
   }

   public final void H() {
      boolean var1 = false;
      int var2 = tileHeight >> 3;
      boolean var3 = false;

      for (int var4 = 11; var4 >= 0; var4--) {
         if (this.ck[var4] != -1) {
            this.ch[var4] = this.ch[var4] + (var2 << 8);
            int var5 = this.b((this.cg[var4] >> 8) + 9, this.ch[var4] >> 8, tileHeight - 19);
            if ((this.ch[var4] >> 8) + 19 >= var5) {
               this.ch[var4] = var5 - 19 << 8;
            }
         }
      }
   }

   public final void I() {
      if (this.bP != -1) {
         boolean var1 = false;
         boolean var2 = false;
         int var3 = this.bP * tileWidth;
         int var4 = this.bQ * tileHeight;
         if (this.a(this.player.b() - 11, this.player.c() + L + 7, 18, 37, var3, var4, 19, 19)) {
            this.bP = this.bQ = -1;
            this.g(this.Z, this.X);
            dr++;
         }
      }
   }

   public final void x(Graphics var1) {
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      var1.setClip(0, 0, 176, hudHeight);
      var1.setColor(0);
      var1.fillRect(0, 0, 176, hudHeight);
      boolean var2 = false;
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;
      boolean var7 = false;
      int var8 = 176 - (tileWidth >> 1) + 2 - 5;
      boolean var9 = false;
      int var10 = aE.getHeight() / 12;
      int var11 = aE.getWidth();
      if (this.player.ai < 0) {
         this.player.ai = 0;
      }

      int var12 = 20 - this.player.ai;
      int var16 = var10 >> 1;
      var1.drawImage(an, 0, 0, 0);
      var1.setColor(255, 255, 255);
      byte var13 = this.player.L;
      if (this.player.L != 6 && var13 != 0) {
         ratchetandclank.currentFont.a(var1, String.valueOf(this.player.M[var13]), 3 + var11 + 6, 4, 0);
      }

      ratchetandclank.currentFont.a(var1, Integer.toString(this.aX), var8, 4, 24);
      var1.setColor(37, 84, 106);

      for (byte var17 = 0; var17 < var12; var17++) {
         int var14 = (var17 >> 1) * var16;
         int var15 = var17 % 2 * var16;
         var1.fillRect(51 + var14, 4 + var15, var16, var16);
      }

      if (this.player.L != 0) {
         this.a(var1, 3, 4, var11, var10);
         var1.drawImage(aE, 3, 4 - (this.player.L - 1) * var10, 0);
      }
   }

   public final void y(Graphics var1) {
      if (A) {
         int var2 = aE.getHeight() / 12;
         int var3 = aE.getWidth();
         var1.setClip(176 - var3 >> 1, hudHeight, var3, var2);
         var1.drawImage(aE, 176 - var3 >> 1, hudHeight - 7 * var2, 0);
      }
   }

   public final void z(Graphics var1) {
      int var2 = x;
      int var3 = y + L;

      for (int var4 = 3; var4 >= 0; var4--) {
         if (this.bc[var4] != -1) {
            int var5 = this.bc[var4] + var2;
            int var6 = this.bd[var4] + var3;
            int var7 = this.be[var4] + var2;
            int var8 = this.bf[var4] + var3;
            if ((var5 <= 176 || var7 <= 176) && (var5 >= 0 || var7 >= 0) && (var6 <= 220 || var8 <= 220) && (var6 >= 0 || var8 >= 0)) {
               var1.setClip(0, hudHeight, 176, 220 - hudHeight);
               var1.setColor(15658734);
               var1.drawLine(var5, var6 - 2, var7, var8 - 2);
               var1.setColor(12303291);
               var1.drawLine(var5, var6 - 1, var7, var8 - 1);
               var1.setColor(8947848);
               var1.drawLine(var5, var6, var7, var8);
               var1.setColor(5592405);
               var1.drawLine(var5, var6 + 1, var7, var8 + 1);
               var1.setColor(2236962);
               var1.drawLine(var5, var6 + 2, var7, var8 + 2);
            }
         }
      }
   }

   public final void A(Graphics var1) {
      byte var2 = tileWidth;
      byte var3 = tileHeight;

      for (int var5 = 3; var5 >= 0; var5--) {
         if (this.cq[var5] >= 0) {
            int var6 = this.cm[var5] + this.co[var5] + x;
            int var7 = this.cn[var5] + this.cp[var5] + y;
            if (var6 < 176 && var6 + var2 >= 0 && var7 < 220 && var7 + var3 >= 0) {
               boolean var4 = false;
               if (0 < var3) {
                  int var8 = this.aQ * tileHeight;
                  this.b(var1, var6, var7, var2, var3 - 0);
                  var1.drawImage(aD, var6, var7 - 0 - var8, 0);
               }
            }
         }
      }
   }

   public final void J() {
      int var3 = aJ.getWidth();
      int var4 = aJ.getHeight() >> 3;
      byte var5 = 8;
      short var6 = this.player.c();
      byte var7 = 16;
      byte var8 = K;

      for (int var1 = 49; var1 >= 0; var1--) {
         int var9 = this.player.ab >> 8;
         if (this.bs[var1] && this.bn[var1] != -1) {
            if (this.br[var1] == -1 || !this.bs[this.br[var1]]) {
               if (this.a(this.bl[var1], this.bm[var1] + this.bo[var1], var3, var4, var9 - var5, var6, var7, var8)) {
                  if (this.bn[var1] == 0) {
                     this.c(this.bl[var1] + (var3 >> 1), this.bm[var1] + this.bo[var1], 0);
                     this.c(this.bl[var1] + (var3 >> 1), this.bm[var1] + this.bo[var1], 0);
                  } else {
                     this.c(this.bl[var1] + (var3 >> 1), this.bm[var1] + this.bo[var1], this.bn[var1]);
                  }

                  this.n(var1);
                  this.e(this.bl[var1] + (var3 >> 1) << 8, this.bm[var1] + (var4 >> 1) + this.bo[var1] << 8, 30);
                  this.bn[var1] = -1;
                  continue;
               }

               var5 = 12;
               var7 = 24;

               for (int var2 = enemyPoolSize - 1; var2 >= 0; var2--) {
                  if (this.enemies[var2].Z >= 0) {
                     var9 = this.enemies[var2].ab >> 8;
                     var6 = this.enemies[var2].b();
                     if (var9 + 12 >= this.bl[var1]
                        && var9 - 12 <= this.bl[var1] + var3
                        && this.a(this.bl[var1], this.bm[var1] + this.bo[var1], var3, var4, var9 - 12, var6, 24, var8)) {
                        this.n(var1);
                        this.bs[var1] = false;
                        if (this.bn[var1] == 0) {
                           this.c(this.bl[var1] + (var3 >> 1), this.bm[var1] + this.bo[var1], 0);
                           this.c(this.bl[var1] + (var3 >> 1), this.bm[var1] + this.bo[var1], 0);
                        } else {
                           this.c(this.bl[var1] + (var3 >> 1), this.bm[var1] + this.bo[var1], this.bn[var1]);
                        }

                        this.e(this.bl[var1] + (var3 >> 1) << 8, this.bm[var1] + (var4 >> 1) + this.bo[var1] << 8, 30);
                        this.bn[var1] = -1;
                        break;
                     }
                  }
               }
            }

            this.bo[var1] = (short)(this.bo[var1] + 4);
            if (this.bn[var1] != -1 && this.bo[var1] + this.bm[var1] >= this.bp[var1]) {
               this.bm[var1] = this.bp[var1];
               this.bo[var1] = 0;
               this.bs[var1] = false;
            }
         }
      }
   }

   public final void n(int var1) {
      short var2 = this.bq[var1];
      short var3 = 0;
      boolean var5 = false;
      short var6 = this.X;
      if (this.cV) {
         var6 = 0;
      }

      this.bu[var1 + var6 * 50 >> 5] = this.bu[var1 + var6 * 50 >> 5] & ~(1 << var1 - (var1 >> 5 << 5));
      if (this.bs[var1] || var2 != -1 && this.bs[var2]) {
         var5 = true;
      }

      for (; var2 != -1; var2 = this.bq[var2]) {
         this.bs[var2] = true;
         if (var5) {
            if (this.br[var2] == var1) {
               var3 = this.bp[var2];
               this.bp[var2] = this.bp[var1];
            } else {
               short var4 = this.bp[var2];
               this.bp[var2] = var3;
               var3 = var4;
            }
         } else {
            this.bp[var2] = this.bm[this.br[var2]];
         }
      }

      if (this.bq[var1] != -1) {
         this.br[this.bq[var1]] = this.br[var1];
      }

      if (this.br[var1] != -1) {
         this.bq[this.br[var1]] = this.bq[var1];
      }

      this.bq[var1] = -1;
      this.br[var1] = -1;
   }

   public final void B(Graphics var1) {
      int var2 = aJ.getWidth();
      int var3 = aJ.getHeight() >> 3;
      int var4 = x;
      int var5 = y;

      for (int var6 = 0; var6 < 50; var6++) {
         if (this.bn[var6] >= 0) {
            int var8 = this.bl[var6] + var4;
            int var9 = this.bm[var6] + this.bo[var6] + var5;
            if (var8 < 176 && var8 + var2 >= 0 && var9 < 220 && var9 + var3 >= 0) {
               boolean var7 = false;
               if (0 < var3) {
                  this.b(var1, var8, var9, var2, var3 - 0);
                  var1.drawImage(aJ, var8, var9 - this.bn[var6] * var3 - 0, 0);
               }
            }
         }
      }
   }

   public final void C(Graphics var1) {
      boolean var2 = false;
      boolean var3 = false;
      int var4 = x;
      int var5 = y;

      for (int var7 = 11; var7 >= 0; var7--) {
         if (this.ck[var7] >= 0) {
            int var8 = (this.cg[var7] >> 8) + var4;
            int var9 = (this.ch[var7] >> 8) + var5;
            if (var8 < 176 && var8 + 19 >= 0 && var9 < 220 && var9 + 19 >= 0) {
               boolean var6 = false;
               this.b(var1, var8, var9, 19, 19);
               var1.drawImage(aL, var8, var9 - this.ck[var7] * 19 - 0, 0);
            }
         }
      }
   }

   public final void D(Graphics var1) {
      int var2 = aw.getWidth();
      int var3 = aw.getHeight();
      int var5 = this.ca * tileWidth + x;
      int var6 = this.cb * tileHeight + y;
      if (var5 < 176 && var5 + var2 >= 0 && var6 < 220 && var6 + var3 >= 0) {
         boolean var4 = false;
         if (0 < var3) {
            this.b(var1, var5, var6, var2, var3 - 0);
            var1.drawImage(aw, var5, var6 - 0, 0);
         }
      }
   }

   public final void K() {
      if (this.ca != -1) {
         int var1 = aw.getHeight();
         int var2 = aw.getWidth();
         int var3 = this.ca * tileWidth;
         int var4 = this.cb * tileHeight;
         if (this.a(this.player.b() - 11, this.player.c() + L + 7, 18, 37, var3, var4, var1, var2)) {
            this.ca = this.cb = -1;
            this.cC = true;
            this.cI = cz[11];
            this.r(cA[11]);
            this.g(11);
            this.dC = 261;
            dy = 11;
         }
      }
   }

   public final void E(Graphics var1) {
      for (int var2 = enemyPoolSize - 1; var2 >= 0; var2--) {
         if (this.enemies[var2].Z != -1) {
            this.enemies[var2].a(var1, var2, this.enemies[var2].ah, x, y);
         }
      }

      this.player.a(var1, this.player.ah, x, y);

      for (int var3 = 9; var3 >= 0; var3--) {
         this.enemyProjectiles[var3].a(var1);
      }

      for (int var4 = 9; var4 >= 0; var4--) {
         this.playerProjectiles[var4].a(var1);
      }
   }

   public final void L() {
      this.eb = true;

      for (int var1 = 9; var1 >= 0; var1--) {
         if (this.enemyProjectiles[var1].f == -1) {
            boolean var2 = false;
            this.enemyProjectiles[var1].g = tileWidth * 14 << 8;
            this.enemyProjectiles[var1].h = tileHeight * 9 << 8;
            this.enemyProjectiles[var1].f = 31;
            this.enemyProjectiles[var1].o = 0;
            this.enemyProjectiles[var1].r = 25;
            this.enemyProjectiles[var1].p = j.b[31];
            this.enemyProjectiles[var1].q = j.c[31];
            if (this.bK == 0 || this.bK == 1 || this.bK == 7) {
               this.enemyProjectiles[var1].m = j.d[31] << 8;
            } else if (this.bK != 2 && this.bK != 6) {
               this.enemyProjectiles[var1].m = -(j.d[31] << 8);
            } else {
               this.enemyProjectiles[var1].m = 0;
            }

            if (this.bK != 2 && this.bK != 1 && this.bK != 3) {
               if (this.bK != 0 && this.bK != 4) {
                  this.enemyProjectiles[var1].n = j.d[31] << 8;
                  return;
               }

               this.enemyProjectiles[var1].n = 0;
               return;
            }

            this.enemyProjectiles[var1].n = -(j.d[31] << 8);
            return;
         }
      }
   }

   public final void C(int var1, int var2) {
      this.eb = false;
      int var3 = (var1 >> 8) / tileWidth;
      int var4 = (var2 >> 8) / tileHeight;
      if (!this.levelMap.b(var3, var4)) {
         if (this.levelMap.b(var3, var4 - 1)) {
            var4--;
         } else if (this.levelMap.b(var3, var4 + 1)) {
            var4++;
         } else if (this.levelMap.b(var3 + 1, var4)) {
            var3++;
         } else {
            if (!this.levelMap.b(var3 - 1, var4)) {
               return;
            }

            var3--;
         }
      }

      this.a(var3, var4, (byte)1, -1);
   }

   public final void M() {
      if (this.bJ[4] <= 0) {
         this.midlet.k();
         this.Z = 100;
         this.cu = 0;
         this.b = 24;
         this.bJ[4] = 1;
         this.player.ac = 256;
         this.player.ao = true;

         for (int var11 = 0; var11 < enemyPoolSize; var11++) {
            this.enemies[var11].Z = -1;
         }

         for (int var12 = 9; var12 >= 0; var12--) {
            this.enemyProjectiles[var12].f = -1;
         }

         for (int var13 = 9; var13 >= 0; var13--) {
            this.enemyProjectiles[var13].f = -1;
         }

         for (int var14 = 11; var14 >= 0; var14--) {
            this.ck[var14] = -1;
         }
      } else {
         short var1 = this.player.b();
         int var2 = this.player.c() + L + (K >> 1);
         int var3 = tileWidth * 14;
         int var4 = tileHeight * 9;
         int var5 = var1 - var3;
         int var6 = var2 - var4;
         boolean var7 = false;
         byte var8 = tileHeight;
         if (this.bM <= 2 && !this.eb) {
            if (var5 > var8) {
               if (var6 > var8) {
                  this.bK = 7;
               } else if (var6 < -var8) {
                  this.bK = 1;
               } else {
                  this.bK = 0;
               }
            } else if (var5 < -var8) {
               if (var6 > var8) {
                  this.bK = 5;
               } else if (var6 < -var8) {
                  this.bK = 3;
               } else {
                  this.bK = 4;
               }
            } else {
               this.bK = 2;
               if (var6 > 0) {
                  this.bK = 6;
               }
            }
         }

         if (++this.bL > 30) {
            this.bL = 0;
            if (++this.bM > 12) {
               this.bM = 0;
            }

            if (this.bM > 2) {
               this.bL = 7;
               if (++this.bK > 7) {
                  this.bK = 0;
               }
            }

            this.L();
         }

         for (int var10 = 0; var10 < 8; var10++) {
            int var9 = var10 % 4;
            if (this.bH[var9] != 0 && this.bJ[var9] > 0) {
               if (--this.bI[var9] < 0) {
                  this.bH[var9] = 0;
               }
            } else if (this.bH[var9] != 2 && this.bJ[var9] <= 0 && --this.bI[var9] < 0) {
               this.bH[var9] = 2;
            }

            this.o(var9);
         }

         if (this.bJ[0] + this.bJ[1] + this.bJ[2] + this.bJ[3] == 0) {
            this.o(4);
         }
      }
   }

   public final void N() {
      this.h = 0;
      this.i = 0;
      this.g = false;
      this.s = 0;
      this.u = 0;
      this.bK = 4;
      this.bI[0] = this.bI[1] = this.bI[2] = this.bI[3] = 0;
      this.bH[0] = this.bH[1] = this.bH[2] = this.bH[3] = 0;
      this.bJ[0] = this.bJ[1] = this.bJ[2] = this.bJ[3] = 100;
      this.bJ[4] = 200;
      this.bM = 0;
   }

   public final void O() {
      if (this.aa) {
         this.dZ = true;
      } else {
         this.o = -1;
         this.player.u = -2;
         this.dC = 0;
         this.cS = false;
         this.Z = 1;
         this.bZ = false;
         this.aQ = 3;
         this.levelMap.b(this.Z);
         this.X = d.a;
         this.di = 1;
         this.Y = this.X;
         this.levelMap.a(this.X, true);
         this.player.ab = this.ad * tileWidth + (J >> 1) << 8;
         this.player.aa = this.player.af = this.ae;
         this.player.aj = 1;
         this.player.ak = 0;
         this.player.al = 0;
         this.player.am = 0;
         this.player.ag = 0;
         this.player.s = -1;
         this.player.Z = 0;
         this.player.ah = 1;
         this.player.ai = 20;
         x = this.af;
         y = this.ag;
         this.ct = System.currentTimeMillis();
         this.b(this.X, 11337);
         this.player.E = 0;
         this.player.ao = true;
         this.d = true;
         this.e = true;
         this.player.a((byte)0);
         this.player.am = 0;
         this.ce = 1;
         this.cf = 18;
         this.player.O = (byte)(this.player.O & -33);
         this.aa = true;
         this.bw = -1;
         this.bx = -1;
         this.by = -1;
         this.cJ = -1;
         this.cK = -1;
         this.cK &= -221;
         this.cL = 1572865;
         this.player.L = 1;
         this.b = 0;
         this.cv = this.cw = 0;
         this.updateCamera();
      }
   }

   public final void o(int var1) {
      int var4 = tileWidth;
      int var5 = tileHeight;
      if (this.bJ[var1] <= 0) {
         this.bJ[var1] = 0;
      } else {
         int var2;
         int var3;
         if (var1 == 4) {
            var4 = tileWidth << 1;
            var5 = tileHeight << 1;
            var2 = bN[2] * tileWidth;
            var3 = bO[2] * tileHeight;
         } else {
            var2 = bN[var1] * tileWidth;
            var3 = bO[var1] * tileHeight;
         }

         if (var2 + var4 + x >= 0 && var2 + x <= 176 && var3 + var5 + y >= 0 && var3 + y <= 220) {
            for (int var6 = 9; var6 >= 0; var6--) {
               byte var7 = this.playerProjectiles[var6].f;
               if (this.playerProjectiles[var6].f >= 0 && var7 != 30 && var7 != 15 && var7 != 16 && var7 != 17) {
                  int var10 = this.playerProjectiles[var6].p;
                  int var11 = this.playerProjectiles[var6].q;
                  int var8 = (this.playerProjectiles[var6].g >> 8) - var10;
                  int var9 = (this.playerProjectiles[var6].h >> 8) - var11;
                  if (this.a(var2, var3, var4, var5, var8, var9, var10 * 2, var11 * 2)) {
                     this.bJ[var1] = this.bJ[var1] - j.a[var7];
                     if (var1 == 4) {
                        this.bI[0] = this.bI[1] = this.bI[2] = this.bI[3] = 5;
                        this.bH[0] = this.bH[1] = this.bH[2] = this.bH[3] = 3;
                     } else {
                        this.bI[var1] = 5;
                        this.bH[var1] = 1;
                     }

                     this.playerProjectiles[var6].a(false);
                     return;
                  }
               }
            }
         }
      }
   }

   public final void F(Graphics var1) {
      if (this.bJ[4] > 0) {
         short var8 = 0;
         int var2 = tileWidth * 14 + x;
         int var3 = tileHeight * 9 + y;
         int var6 = aP.getWidth();
         int var7 = aP.getHeight() >> 1;
         int var4;
         int var5;
         switch (this.bK) {
            case 0:
               var2 += aN.getWidth() - var6 / 2;
               var3 -= var7 / 2;
               var4 = var2;
               var5 = var3;
               break;
            case 1:
               var2 += (aN.getWidth() >> 1) - (aN.getWidth() >> 3);
               var3 -= (aN.getHeight() >> 3) + 2 * var7 - (aN.getHeight() >> 5);
               var4 = var2;
               var5 = var3 + var7;
               break;
            case 2:
               var8 = 90;
               var2 -= var7 / 2;
               var3 -= (aN.getHeight() >> 2) + var6 / 2;
               var4 = var2;
               var5 = var3;
               break;
            case 3:
               var8 = 8192;
               var2 -= (aN.getWidth() >> 1) + var6 - (aN.getWidth() >> 3);
               var3 -= (aN.getHeight() >> 3) + 2 * var7 - (aN.getHeight() >> 5);
               var4 = var2;
               var5 = var3 + var7;
               break;
            case 4:
               var8 = 8192;
               var2 -= aN.getWidth() + var6 / 2;
               var3 -= var7 / 2;
               var4 = var2;
               var5 = var3;
               break;
            case 5:
               var8 = 180;
               var2 -= (aN.getWidth() >> 1) + var6 - (aN.getWidth() >> 3);
               var3 += (aN.getHeight() >> 3) - (aN.getHeight() >> 5);
               var4 = var2;
               var5 = var3;
               break;
            case 6:
               var8 = 270;
               var2 -= var7 + var7 / 2;
               var3 += (aN.getHeight() >> 2) - var6 / 2;
               var4 = var2 + var6;
               var5 = var3;
               break;
            default:
               var8 = 16384;
               var2 += (aN.getWidth() >> 1) - (aN.getWidth() >> 3);
               var3 += (aN.getHeight() >> 3) - (aN.getHeight() >> 5);
               var4 = var2;
               var5 = var3;
         }

         if (var4 < 176 && var5 < 220 && var4 + var6 > 0 && var5 + var7 > 0) {
            this.b(var1, var4, var5, var6, var7);
            e.b.drawImage(aP, var2, var3, 20, var8);
         }

         var6 = aN.getWidth();
         var7 = aN.getHeight() >> 2;

         for (int var9 = 0; var9 < 4; var9++) {
            var2 = tileWidth * 14 + x;
            var3 = tileHeight * 9 + y;
            var4 = tileWidth * 14 + x;
            var5 = tileHeight * 9 + y;
            var8 = 0;
            switch (var9) {
               case 0:
                  var3 -= (aN.getHeight() >> 2) + var7 * this.bH[var9];
                  var5 -= var7;
                  break;
               case 1:
                  var8 = 16384;
                  var3 -= 3 * (aN.getHeight() >> 2) - var7 * this.bH[var9];
                  break;
               case 2:
                  var8 = 8192;
                  var2 -= aN.getWidth();
                  var3 -= (aN.getHeight() >> 2) + var7 * this.bH[var9];
                  var4 -= var6;
                  var5 -= var7;
                  break;
               case 3:
                  var8 = 180;
                  var2 -= aN.getWidth();
                  var3 -= 3 * (aN.getHeight() >> 2) - var7 * this.bH[var9];
                  var4 -= var6;
            }

            if (var4 + var6 >= 0 && var4 <= 176 && var5 + var7 >= 0 && var5 <= 220) {
               this.b(var1, var4, var5, var6, var7);
               e.b.drawImage(aN, var2, var3, 20, var8);
            }
         }
      }
   }

   public final void c(Graphics var1, byte var2) {
      int var3 = av.getWidth();
      int var4 = av.getHeight() / 5;
      int var5 = tileWidth * 14 + x;
      int var6 = tileHeight * 7 + y;
      this.player.n();
      if (var5 - 3 < 176 && var6 < 220 && var5 - 3 + var3 >= 0 && var6 + var4 >= 0) {
         var1.setClip(var5 - (tileWidth >> 1), var6, var3, var4);
         var1.drawImage(av, var5 - 3, var6 - var4 * var2, 17);
      }

      if (!this.cD && var2 == 4 && !this.g) {
         this.cI = cz[31];
         this.cC = true;
         this.r(cA[31]);
      }
   }

   public final void p(int var1) {
      if (var1 == bb) {
         this.aS = this.player.Z;
         this.aT = this.player.al;
         this.aU = this.player.aj;
         if (this.player.ao) {
            this.aW = 0;
         } else {
            this.aW = 5;
         }
      } else if (var1 >= 0) {
         this.aS = this.enemies[var1].Z;
         this.aT = this.enemies[var1].al;
         this.aU = this.enemies[var1].aj;
         if (this.enemies[var1].ap != 0) {
            if (this.enemies[var1].ap == 1) {
               if (this.enemies[var1].ao) {
                  this.aW = 4;
               } else {
                  this.aW = 2;
               }
            } else if (this.enemies[var1].ap == 2) {
               if (this.enemies[var1].ao) {
                  this.aW = 6;
               } else {
                  this.aW = 3;
               }
            } else if (this.enemies[var1].ap == 3) {
               if (this.enemies[var1].ao) {
                  this.aW = 1;
               } else {
                  this.aW = 7;
               }
            }
         } else if (this.enemies[var1].ao) {
            this.aW = 0;
         } else {
            this.aW = 5;
         }
      }

      if (this.aS >= 0) {
         if (var1 == bb) {
            this.aV = b.I[this.aS][this.aT][this.aU];
            return;
         }

         this.aV = f.o[this.aS][this.aT][this.aU];
         this.M = (byte)(aG[this.aS].getHeight() / K);
      }
   }

   public final void a(Graphics var1, int var2, int var3, int var4) {
      this.p(bb);
      e.b.drawImage(aH, var2, var3 - var4, 20, 8192);
   }

   public final void c(Graphics var1, int var2, int var3, int var4, int var5) {
      this.p(var2);
      int var6 = 0;
      if (this.enemies[var2].u) {
         var6 = K << 1;
      }

      switch (this.aW) {
         case 1:
            e.b.drawImage(aG[this.aS], var3 - this.aV * K - var6, var4 - var5, 20, 90);
            return;
         case 2:
            e.b.drawImage(aG[this.aS], var3, var4 - (this.M - 1 - this.aV) * K - var5 + var6, 20, 180);
            return;
         case 3:
            e.b.drawImage(aG[this.aS], var3 - (this.M - 1 - this.aV) * K + var6, var4 - var5, 20, 270);
            return;
         case 4:
            e.b.drawImage(aG[this.aS], var3, var4 - (this.M - 1 - this.aV) * K - var5 + var6, 20, 16384);
            return;
         case 5:
            e.b.drawImage(aG[this.aS], var3, var4 - this.aV * K - var5 - var6, 20, 8192);
            return;
         case 6:
            e.b.drawImage(aG[this.aS], var3 - (this.M - 1 - this.aV) * K + var6, var4 - var5, 20, 8282);
            return;
         case 7:
            e.b.drawImage(aG[this.aS], var3 - this.aV * K - var6, var4 - var5, 20, 16474);
      }
   }

   private boolean f(int var1, int var2, int var3) {
      return var1 >= var2 && var1 <= var2 + var3;
   }

   public final boolean a(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      return (this.f(var5, var1, var3) || this.f(var1, var5, var7)) && (this.f(var6, var2, var4) || this.f(var2, var6, var8));
   }

   public final void a(int var1, int var2, boolean var3, int var4) {
      short var5 = (short)(var1 * tileWidth + (tileWidth >> 1));
      short var6 = (short)(var2 * tileHeight + (tileHeight >> 1));
      if (var3) {
         this.bc[var4] = var5;
         this.bd[var4] = var6;
      } else {
         this.be[var4] = var5;
         this.bf[var4] = var6;
      }

      if (this.bc[var4] != -1 && this.be[var4] != -1) {
         this.bh[var4] = this.bf[var4] - this.bd[var4] << 8;
         this.bg[var4] = (short)(this.be[var4] - this.bc[var4]);
         this.bi[var4] = (this.bd[var4] << 8) - this.bh[var4] * this.bc[var4] / this.bg[var4];
      }
   }

   public final void a(int var1, int var2, int var3, int var4, int var5) {
      short var6 = this.X;
      if (this.cV) {
         var6 = 0;
      }

      if (this.bt[var6] && var3 > 0) {
         this.bu[var5 + var6 * 50 >> 5] = this.bu[var5 + var6 * 50 >> 5] | 1 << var5 - (var5 >> 5 << 5);
      }

      if ((this.bu[var5 + var6 * 50 >> 5] & 1 << var5 - (var5 >> 5 << 5)) != 0) {
         this.bn[var5] = (byte)var3;
         if (var4 == 2) {
            this.bl[var5] = (short)(var1 * tileWidth + (aJ.getWidth() >> 1));
         } else {
            this.bl[var5] = (short)(var1 * tileWidth + var4 * aJ.getWidth());
         }

         this.bm[var5] = (short)(var2 * tileHeight);
         this.bp[var5] = this.bm[var5];
         this.bo[var5] = 0;
      }
   }

   public final void P() {
      boolean var7 = false;

      for (short var2 = 0; var2 < 50; var2++) {
         if (this.bn[var2] != -1) {
            var7 = true;
            short var4;
            int var5 = (var4 = this.bm[var2]) / tileHeight;
            int var6 = this.bl[var2] / tileWidth;

            short var3;
            while (var7 && ((var3 = this.levelMap.f[var6][var5 + 1]) < 19 || var3 > 27)) {
               for (short var1 = 0; var1 < 50; var1++) {
                  if (this.bn[var1] != -1 && var1 != var2 && this.bl[var1] == this.bl[var2] && this.bm[var1] == var4 + tileHeight) {
                     var7 = false;
                     break;
                  }
               }

               if (var7) {
                  this.bm[var2] = (short)(this.bm[var2] + tileHeight);
                  var4 += tileHeight;
                  var5++;
               }
            }
         }
      }
   }

   public final void Q() {
      this.P();

      for (short var2 = 49; var2 >= 0; var2--) {
         if (this.bn[var2] != -1) {
            short var3 = this.bm[var2];
            this.bq[var2] = this.br[var2] = -1;

            for (short var1 = 49; var1 >= 0; var1--) {
               if (this.bn[var1] != -1 && this.bl[var1] == this.bl[var2]) {
                  if (this.bm[var1] == var3 - tileHeight) {
                     this.bq[var2] = var1;
                     this.br[var1] = var2;
                  } else if (this.bm[var1] == var3 + tileHeight) {
                     this.br[var2] = var1;
                     this.bq[var1] = var2;
                  }
               }
            }
         }
      }
   }

   public final void c(int var1, int var2, int var3) {
      var1 -= 9;

      for (int var4 = 11; var4 >= 0; var4--) {
         if (this.ck[var4] == -1) {
            if (var3 == 0) {
               this.ck[var4] = (byte)(this.abs(this.random.nextInt()) % 2);
            } else if (var3 == 1) {
               this.ck[var4] = 2;
            } else {
               this.ck[var4] = 3;
            }

            this.cg[var4] = var1 << 8;
            this.ch[var4] = var2 << 8;
            this.ci[var4] = 0;
            this.cj[var4] = 768;
            return;
         }
      }
   }

   public final void d(int var1, int var2, int var3) {
      for (int var4 = 3; var4 >= 0; var4--) {
         if (this.cq[var4] == -1) {
            this.cq[var4] = (byte)var3;
            if (var3 != 0 && var3 != 1) {
               if (var3 == 2) {
                  this.cm[var4] = (short)((var1 - 2) * tileWidth);
                  this.cn[var4] = (short)(var2 * tileHeight);
                  this.co[var4] = (short)(tileWidth << 1);
                  this.cp[var4] = 0;
                  return;
               }

               this.cm[var4] = (short)(var1 * tileWidth);
               this.cn[var4] = (short)((var2 - 2) * tileHeight);
               this.co[var4] = 0;
               this.cp[var4] = (short)(tileHeight << 1);
               return;
            }

            this.cm[var4] = (short)(var1 * tileWidth);
            this.cn[var4] = (short)(var2 * tileHeight);
            this.co[var4] = 0;
            this.cp[var4] = 0;
            return;
         }
      }
   }

   public final void e(int var1, int var2, int var3) {
      for (int var4 = 9; var4 >= 0; var4--) {
         if (this.playerProjectiles[var4].f == -1) {
            if (var3 >= 0 && var3 <= 14 || var3 >= 18 && var3 <= 20) {
               this.player.M[this.player.L]--;
               this.e = true;
            }

            if (var3 >= 9 && var3 <= 11 || var3 >= 15 && var3 <= 17) {
               this.playerProjectiles[var4].p = 84;
               this.playerProjectiles[var4].q = 11;
               var1 += (this.player.ao ? 1 : -1) * 84 << 8;
            } else {
               this.playerProjectiles[var4].p = j.b[var3];
               this.playerProjectiles[var4].q = j.c[var3];
            }

            this.playerProjectiles[var4].k = this.playerProjectiles[var4].i = this.playerProjectiles[var4].g = var1;
            this.playerProjectiles[var4].l = this.playerProjectiles[var4].j = this.playerProjectiles[var4].h = var2;
            this.playerProjectiles[var4].f = (byte)var3;
            this.playerProjectiles[var4].m = (this.player.ao ? 1 : -1) * j.d[var3] << 8;
            this.playerProjectiles[var4].n = 0;
            this.playerProjectiles[var4].o = 0;
            this.playerProjectiles[var4].s = this.player.ao ? var1 - this.player.ab : this.player.ab - var1;
            if (var3 >= 3 && var3 <= 5) {
               this.playerProjectiles[var4].n = 768;
               return;
            }
            break;
         }
      }
   }

   public final void a(int var1, int var2, int var3, boolean var4, boolean var5, int var6, byte var7) {
      for (int var8 = 9; var8 >= 0; var8--) {
         if (this.enemyProjectiles[var8].f == -1) {
            this.enemyProjectiles[var8].g = var1;
            this.enemyProjectiles[var8].h = var2;
            this.enemyProjectiles[var8].f = (byte)var3;
            this.enemyProjectiles[var8].o = 0;
            this.enemyProjectiles[var8].r = var7;
            this.enemyProjectiles[var8].p = j.b[var3];
            this.enemyProjectiles[var8].q = j.c[var3];
            if (var6 != 0 && var6 != 1) {
               if (var5) {
                  this.enemyProjectiles[var8].m = (short)((var6 == 3 ? -1 : 1) * j.d[var3] << 8);
                  this.enemyProjectiles[var8].n = 0;
               } else {
                  this.enemyProjectiles[var8].m = 0;
                  this.enemyProjectiles[var8].n = (short)((var4 ? -1 : 1) * j.d[var3] << 8);
               }
            } else if (var5) {
               this.enemyProjectiles[var8].m = 0;
               if (var7 != 17 && var7 != 20 && var7 != 23) {
                  this.enemyProjectiles[var8].n = (short)((var6 == 0 ? -1 : 1) * j.d[var3] << 8);
               } else {
                  this.enemyProjectiles[var8].n = (short)((var6 == 0 ? 1 : -1) * j.d[var3] << 8);
               }
            } else {
               this.enemyProjectiles[var8].m = (short)((var4 ? 1 : -1) * j.d[var3] << 8);
               this.enemyProjectiles[var8].n = 0;
            }

            if (var3 == 32) {
               this.enemyProjectiles[var8].i = var1;
               this.enemyProjectiles[var8].j = var2;
               this.enemyProjectiles[var8].k = var1;
               this.enemyProjectiles[var8].l = var2 - (4 * tileHeight << 8);
               return;
            }
            break;
         }
      }
   }

   public final void a(int var1, int var2, byte var3, int var4) {
      if (var4 == -1) {
         var4 = enemyPoolSize - 1;

         while (var4 >= 0 && this.enemies[var4].Z != -1) {
            var4--;
         }

         if (var4 == -1) {
            return;
         }
      } else {
         short var5 = this.X;
         if (this.cV) {
            var5 = 0;
         }

         int var6 = var4 + var5 * 10 >> 5;
         if (this.bt[var5]) {
            this.bv[var6] = this.bv[var6] | 1 << var4 + var5 * 10 - (var6 << 5);
         }

         if ((this.bv[var6] & 1 << var4 + var5 * 10 - (var6 << 5)) == 0) {
            return;
         }
      }

      if (var3 >= 0 && var3 <= 2) {
         this.enemies[var4].Z = 1;
      } else if (var3 >= 3 && var3 <= 5) {
         this.enemies[var4].Z = 0;
      } else if (var3 >= 6 && var3 <= 13) {
         this.enemies[var4].Z = 2;
      } else {
         this.enemies[var4].Z = 3;
      }

      this.enemies[var4].ab = var1 * tileWidth + (tileWidth >> 1) << 8;
      this.enemies[var4].aa = (byte)var2;
      this.enemies[var4].ag = this.enemies[var4].ad = this.enemies[var4].ac = 0;
      this.enemies[var4].ai = f.i[var3];
      this.enemies[var4].ap = 0;
      this.enemies[var4].ao = true;
      if (this.Z == 12 && this.player.ab < this.enemies[var4].ab) {
         this.enemies[var4].ao = false;
      }

      this.enemies[var4].tileHeight = false;
      this.enemies[var4].u = false;
      this.enemies[var4].E = 0;
      this.enemies[var4].C = (byte)var4;
      this.enemies[var4].w = 0;
      this.enemies[var4].D = 0;
      this.enemies[var4].E = 0;
      this.enemies[var4].tileWidth = this.enemies[var4].ab;
      if (this.enemies[var4].Z == 1) {
         this.enemies[var4].tileWidth = 0;
      }

      if (this.enemies[var4].Z == 3) {
         this.enemies[var4].ao = false;
         short var7;
         if ((var7 = this.levelMap.a(var1 + 1, var2)) >= 19 && var7 <= 34) {
            this.enemies[var4].ap = 3;
         }

         if ((var7 = this.levelMap.a(var1 - 1, var2)) >= 19 && var7 <= 34) {
            this.enemies[var4].ap = 2;
         }

         if ((var7 = this.levelMap.a(var1, var2 - 1)) >= 19 && var7 <= 34) {
            this.enemies[var4].ag = 3072;
            this.enemies[var4].ap = 1;
         }
      }

      this.enemies[var4].v = var3;
      this.enemies[var4].a((byte)0);
      this.enemies[var4].am = 1;
      this.enemies[var4].ah = 1;
   }

   public final void a(byte var1, byte var2) {
      boolean var3 = false;

      for (int var4 = 0; var4 < 3; var4++) {
         if (this.bF[var4] == -1) {
            this.bF[var4] = var1;
            this.bG[var4] = var2;
            return;
         }
      }
   }

   public final void a(byte var1, byte var2, int var3) {
      boolean var4 = false;

      for (int var5 = 0; var5 < 3; var5++) {
         if (this.bC[var5] == 0) {
            switch (var3) {
               case -119:
                  this.bC[var5] = -128;
                  break;
               case 70:
                  this.bC[var5] = 1;
                  break;
               case 71:
                  this.bC[var5] = 2;
                  break;
               case 72:
                  this.bC[var5] = 4;
                  break;
               case 73:
                  this.bC[var5] = 8;
                  break;
               case 74:
                  this.bC[var5] = 16;
                  break;
               case 75:
                  this.bC[var5] = 32;
                  break;
               case 76:
                  this.bC[var5] = 64;
            }

            this.bD[var5] = var1;
            this.bE[var5] = (byte)(var2 - 1);
            return;
         }
      }
   }

   public final void G(Graphics var1) {
      int var6 = aC.getHeight() >> 2;
      int var7 = aC.getWidth();
      int var8 = 0;

      for (int var2 = 0; var2 < 3; var2++) {
         if (this.bC[var2] == -128 || this.bC[var2] != 0 && (this.bw & this.bC[var2]) != 0) {
            int var4 = this.bD[var2] * tileWidth + x + (tileWidth - var7 >> 1);
            int var5 = this.bE[var2] * tileHeight + y;
            if (var4 + var7 >= 0 && var4 < 176 && var5 + var6 >= 0 && var5 < 220) {
               if (this.bC[var2] >= 2 && this.bC[var2] <= 64) {
                  var8 = var6;
               } else if (this.bC[var2] == -128) {
                  var8 = var6 * 3;
                  if ((this.bw & -128) != 0) {
                     var8 = var6 << 1;
                  }
               }

               boolean var3 = false;
               if (0 < var6) {
                  this.b(var1, var4, var5, var7, var6 - 0);
                  var1.drawImage(aC, var4, var5 - 0 - var8, 0);
               }
            }
         }
      }

      for (int var10 = 0; var10 < 3; var10++) {
         if (this.bF[var10] != -1) {
            int var9 = this.levelMap.a(this.bF[var10], this.bG[var10]);
            var9 -= 36;
            var9 = 1 << var9;
            int var11 = this.bF[var10] * tileWidth + x + (tileWidth - 19 >> 1);
            int var12 = this.bG[var10] * tileHeight + y + tileHeight - 19;
            if (var11 + 19 >= 0 && var11 < 176 && var12 + 38 >= 0 && var12 < 220) {
               this.b(var1, var11, var12, 19, 38);
               var1.drawImage(aL, var11, var12 - 0 - ((this.bw & var9) == 0 ? 38 : 0) - 133, 0);
            }
         }
      }
   }

   public final void q(int var1) {
      if (this.enemies[var1].Z != -1 && this.enemies[var1].al != 5) {
         byte var2 = 0;
         short var3 = this.enemies[var1].c();
         short var4 = this.enemies[var1].b();
         byte var5 = this.enemies[var1].Z;
         byte var6 = f.a[var5];
         byte var7 = f.b[var5];
         byte var8 = f.c[var5];
         byte var9 = f.d[var5];
         if (var3 - var6 + var8 + x >= 0 && var3 - var6 + x <= 176 && var4 + var7 + var9 + y >= 0 && var4 + var7 + y <= 220) {
            for (int var10 = 9; var10 >= 0; var10--) {
               byte var11 = this.playerProjectiles[var10].f;
               if (this.playerProjectiles[var10].f >= 0
                  && var11 != 30
                  && (this.playerProjectiles[var10].b() || var11 >= 9 && var11 <= 11 || var11 >= 15 && var11 <= 17 || var11 == 2 || var11 >= 21 && var11 <= 29)) {
                  int var12 = (this.playerProjectiles[var10].g >> 8) - this.playerProjectiles[var10].p;
                  int var13 = (this.playerProjectiles[var10].h >> 8) - this.playerProjectiles[var10].q;
                  if (this.a(var3 - var6, var4 + var7, var8, var9, var12, var13, this.playerProjectiles[var10].p << 1, this.playerProjectiles[var10].q << 1)) {
                     var2 = this.playerProjectiles[var10].c();
                     if (var5 != 4 && var5 != 3 || var2 != 6) {
                        if (var11 >= 9 && var11 <= 11) {
                           int var14;
                           if ((var14 = this.abs(this.player.b() - var3)) < tileWidth) {
                              this.enemies[var1].ai = (byte)(this.enemies[var1].ai - j.a[var11]);
                           } else if (var14 >= tileWidth * 2 && var11 <= 10) {
                              this.enemies[var1].ai = (byte)(this.enemies[var1].ai - (j.a[var11] >> 2));
                           } else {
                              this.enemies[var1].ai = (byte)(this.enemies[var1].ai - (j.a[var11] >> 1));
                           }
                        } else {
                           this.enemies[var1].ai = (byte)(this.enemies[var1].ai - j.a[var11]);
                        }

                        this.playerProjectiles[var10].a(false);
                        this.e(var3 << 8, (var4 << 8) + (K << 7), 30);
                        dE++;
                        if (this.enemies[var1].ai <= 0 && this.enemies[var1].al != 5) {
                           short var18 = this.X;
                           if (this.cV) {
                              var18 = 0;
                           }

                           int var15 = var1 + var18 * 10 >> 5;
                           this.bv[var15] = this.bv[var15] & ~(1 << var1 + var18 * 10 - (var15 << 5));
                           if (var2 == 6 && var5 != 4) {
                              du++;

                              for (int var19 = 0; var19 < f.k[this.enemies[var1].v]; var19++) {
                                 this.c((short)(this.enemies[var1].ab >> 8), this.enemies[var1].b(), 0);
                              }

                              this.enemies[var1].Z = 4;
                              this.enemies[var1].v = 24;
                              this.enemies[var1].ai = f.i[24];
                              if (this.player.P[6] < 2) {
                                 this.player.N[6]++;
                              }

                              if (this.player.N[6] >= 20) {
                                 this.player.P[6]++;
                                 this.player.N[6] = 0;
                                 this.cC = false;
                                 this.r(73);
                                 do++;
                                 if (this.aa && ++this.ab > 10) {
                                    this.ab = 10;
                                 }

                                 dK[this.X]++;
                                 return;
                              }
                           } else {
                              do++;
                              if (this.aa && ++this.ab > 10) {
                                 this.ab = 10;
                              }

                              dK[this.X]++;
                              this.enemies[var1].a((byte)5);
                              this.enemies[var1].am = 1;
                              if (var5 != 4) {
                                 for (int var16 = 0; var16 < f.k[this.enemies[var1].v]; var16++) {
                                    this.c((short)(this.enemies[var1].ab >> 8), this.enemies[var1].b(), 0);
                                 }

                                 if (this.player.P[var2] < 2) {
                                    this.player.N[var2]++;
                                 }

                                 if (this.player.N[var2] >= 20) {
                                    this.player.P[var2]++;
                                    this.player.N[var2] = 0;
                                    this.cC = false;
                                    this.r(73);
                                    return;
                                 }
                              }
                           }
                        } else if (this.enemies[var1].al != 2) {
                           this.enemies[var1].a((byte)4);
                           this.enemies[var1].an = 0;
                        }

                        return;
                     }
                  }
               }
            }
         }
      }
   }

   public final void R() {
      short var1 = this.player.b();
      int var2 = this.player.c() + L;
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;

      for (int var7 = 9; var7 >= 0; var7--) {
         if (this.player.al == 10) {
            return;
         }

         byte var8 = this.enemyProjectiles[var7].f;
         if (this.enemyProjectiles[var7].f != -1 && var8 != 30 && this.enemyProjectiles[var7].b()) {
            int var9 = (this.enemyProjectiles[var7].g >> 8) - this.enemyProjectiles[var7].p;
            int var10 = (this.enemyProjectiles[var7].h >> 8) - this.enemyProjectiles[var7].q;
            if (this.a(var1 - 11, var2 + 7, 18, 37, var9, var10, this.enemyProjectiles[var7].p << 1, this.enemyProjectiles[var7].q << 1)) {
               byte var11 = this.enemyProjectiles[var7].r;
               if (var8 != 3 && var8 != 6 && var8 != 18) {
                  if (!f) {
                     this.player.ai = (byte)(this.player.ai - f.j[var11]);
                  }

                  this.ab = 1;
                  dI = true;
                  this.e = true;
                  this.player.a((byte)9);
                  this.player.an = 0;
                  if (this.player.s == 2) {
                     this.player.s = 1;
                  }

                  this.e(var1 << 8, (var2 << 8) + (K << 7), 30);
               }

               if (var8 == 32) {
                  this.player.E = 10;
               }

               this.enemyProjectiles[var7].a(true);
            }
         }
      }
   }

   public final int D(int var1, int var2) {
      return var1 < var2 ? var1 : var2;
   }

   public final void H(Graphics var1) {
      boolean var2 = false;
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      int var4 = 42;
      if (this.cC && cB[this.cH - 104] != -1 && 42 < at.getHeight() / 5 + 12) {
         var4 = at.getHeight() / 5 + 12;
      }

      int var3 = 220 - var4;
      var1.setClip(0, 0, 176, 220);
      var1.setColor(16777215);
      var1.fillRect(0, var3, 176, var4);
      if (this.cC && cB[this.cH - 104] != -1) {
         int var5 = at.getHeight() / 5;
         int var6 = at.getWidth();
         var1.setColor(52, 86, 92);
         var3 += var4 - var5 >> 1;
         var1.fillRect(6 + var6, var3, 176 - var6 - 12, 2);
         var1.fillRect(168, var3, 2, var5);
         var1.fillRect(6 + var6, var3 + var5 - 2, 176 - var6 - 12, 2);
         var1.setClip(6, var3, at.getWidth(), var5);
         var1.drawImage(at, 6, var3 - var5 * cB[this.cH - 104], 20);
      }
   }

   public final int a(Graphics var1, int var2, String var3, int var4, int var5, int var6, int var7) {
      int var8 = 0;
      int var9 = 0;
      int var10 = 0;
      char[] var14 = new char[var3.length()];
      var3.getChars(0, var3.length(), var14, 0);
      var8 = 0;
      int var11 = var2;
      int var12 = var2;
      char var13 = '\u0000';
      boolean var15 = false;

      while (true) {
         var9 = 0;
         var10 = var12 - 1;
         boolean var16 = false;
         boolean var17 = false;
         int var18 = -1;

         while (true) {
            if (var12 >= var3.length()) {
               var16 = true;
               break;
            }

            if ((var13 = var3.charAt(var12)) == ' ') {
               var9 += ratchetandclank.smallFont.a(var13);
               var12++;
               if (var8 + var9 > var7 && !var15) {
                  var17 = true;
                  break;
               }

               var15 = true;
               break;
            }

            if (var13 == '-') {
               var18 = var12;
            }

            int var19 = ratchetandclank.smallFont.a(var13);
            if (var8 + var9 + var19 > var7 && !var15) {
               var17 = true;
               break;
            }

            var9 += var19;
            var12++;
         }

         if (var8 + var9 > var7 || var16 || var17) {
            if (var3.charAt(var11) == ' ') {
               var11++;
            }

            if (var8 + var9 > var7) {
               if (!var17) {
                  var12 = var10;
               } else if (var18 >= 0) {
                  var12 = var18 + 1;
               }
            } else if (var17 && !var15 && var18 >= 0) {
               var12 = var18 + 1;
            }

            if ((var6 & 1) > 0) {
               if (var12 - var11 > 0) {
                  ratchetandclank.currentFont.a(var1, var14, var11, var12 - var11, 88, var5, var6);
               }

               return var12 + (var17 ? 0 : 1);
            } else {
               if (var12 - var11 > 0) {
                  if (ratchetandclank.q == 3) {
                     int var20;
                     String var25;
                     if ((var20 = (var25 = new String(var14, var11, var12 - var11)).indexOf("Informations-netzwerks")) >= 0) {
                        if (var20 == 0) {
                           ratchetandclank.currentFont.a(var1, "Informationsnetzwerks", var4, var5, var6);
                           if (var25.length() > 22) {
                              var1.drawSubstring(var25, 22, var25.length() - 22, var4 + ratchetandclank.smallFont.a("Informationsnetzwerks"), var5, var6);
                           }
                        } else {
                           var1.drawSubstring(var25, 0, var20, var4, var5, var6);
                           ratchetandclank.currentFont.a(var1, "Informationsnetzwerks", var4 + ratchetandclank.smallFont.a(var25, 0, var20), var5, var6);
                           if (var25.length() > var20 + 1 + 22) {
                              var1.drawSubstring(
                                 var25,
                                 var20 + 1 + 22,
                                 var25.length() - var20 - 1 - 22,
                                 var4 + ratchetandclank.smallFont.a(var25, 0, var20) + ratchetandclank.smallFont.a("Informationsnetzwerks"),
                                 var5,
                                 var6
                              );
                           }
                        }
                     } else {
                        ratchetandclank.currentFont.a(var1, var25, var4, var5, var6);
                     }
                  } else {
                     ratchetandclank.currentFont.a(var1, var14, var11, var12 - var11, var4, var5, var6);
                  }
               }

               return var12 + (var17 ? 0 : 1);
            }
         }

         var8 += var9;
      }
   }

   public final int a(Graphics var1, int var2) {
      int var3 = 6;
      byte var5 = 17;
      if (this.cC && cB[this.cH - 104] != -1) {
         var3 = at.getWidth() + 6 + 3;
         var5 = 20;
      }

      var1.setColor(0);
      var1.setClip(0, 0, 176, 220);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      int var6 = this.cE.length();
      int var4;
      if (this.cC && cB[this.cH - 104] != -1) {
         int var7 = Math.max(at.getHeight() / 5, 30);
         var4 = 220 - ((var7 - 30 >> 1) + 30 + 6);
      } else {
         var4 = 184;
      }

      if ((var2 = this.a(var1, var2, this.cE, var3, var4, var5, 176 - var3 - 6)) >= var6) {
         return -1;
      }

      if ((var2 = this.a(var1, var2, this.cE, var3, var4 + 10, var5, 176 - var3 - 6)) >= var6) {
         return -1;
      }

      int var10;
      return (var10 = this.a(var1, var2, this.cE, var3, var4 + 20, var5, 176 - var3 - 6 - 7)) >= var6 ? -1 : var10;
   }

   public final void r(int var1) {
      this.player.E = 0;
      this.cF = 0;
      this.cH = var1;
      if (var1 == 112 && !this.h(4) && !this.h(5) && !this.h(6) && !this.h(7) && !this.h(9) && !this.h(10)) {
         this.cI = cz[16];
         this.cH = 150;
         this.cE = null;
         this.cE = ratchetandclank.strings[150];
      } else if ((var1 == 125 || var1 == 126 || var1 == 127 || var1 == 128 || var1 == 132 || var1 == 133)
         && this.h(4)
         && this.h(5)
         && this.h(6)
         && this.h(7)
         && this.h(9)
         && this.h(10)) {
         this.cI = cz[17];
         this.cH = 156;
         this.cE = null;
         this.cE = ratchetandclank.strings[156];
      } else if (var1 == 125 || var1 == 126 || var1 == 127 || var1 == 128 || var1 == 132 || var1 == 133) {
         int var4 = 1;
         if (!this.h(4)) {
            var4++;
         }

         if (!this.h(5)) {
            var4++;
         }

         if (!this.h(6)) {
            var4++;
         }

         if (!this.h(7)) {
            var4++;
         }

         if (!this.h(9)) {
            var4++;
         }

         if (!this.h(10)) {
            var4++;
         }

         Object[] var3 = new Object[]{new Integer(var4)};
         this.cE = null;
         this.cE = this.a(ratchetandclank.strings[var1], var3);
      } else if (var1 == 227) {
         Object[] var2 = new Object[]{new Integer(this.v()), new Integer(bR)};
         this.cE = null;
         this.cE = this.a(ratchetandclank.strings[var1], var2);
      } else {
         this.cE = ratchetandclank.strings[var1];
      }

      if (this.cE == "") {
         this.cF = -1;
      }

      this.cD = true;
   }

   public final void S() {
      this.cD = false;
      if (this.cH == 104) {
         this.di = 524288;
         this.cL |= 4;
      } else if (this.cH == 106) {
         this.di = 16;
         this.cL |= 16;
         this.cL |= 262144;
      } else if (this.cH == 110) {
         this.di = 128;
         this.cL |= 128;
      } else if (this.cH == 112) {
         this.di = 10;
         this.cL |= 10;
         this.bw = (byte)(this.bw & -3);
      } else if (this.cH == 150) {
         this.cL |= 32768;
         this.di = 32768;
         this.player.O = (byte)(this.player.O | 32);
      } else if (this.cH == 125) {
         this.di &= -3;
         this.di |= 8192;
         this.cL |= 8192;
      } else if (this.cH == 126) {
         this.di &= -9;
         this.di |= 1024;
         this.cL |= 1024;
      } else if (this.cH == 127) {
         this.di &= -1025;
         if ((this.cJ & 128) == 0) {
            this.di |= 512;
            this.cL |= 512;
         }
      } else if (this.cH == 128) {
         this.di &= -8193;
         if ((this.cJ & 64) == 0) {
            this.di |= 512;
            this.cL |= 512;
         }
      } else if (this.cH == 129) {
         this.di = 20480;
         this.cL |= 4096;
         this.cL |= 16384;
      } else if (this.cH == 132) {
         this.di &= -4097;
         if (!this.h(4) && !this.h(5) && !this.h(6) && !this.h(7) && !this.h(9) && !this.h(10)) {
            this.cL |= 64;
            this.di = 64;
         }
      } else if (this.cH == 133) {
         this.di &= -16385;
         if (!this.h(4) && !this.h(5) && !this.h(6) && !this.h(7) && !this.h(9) && !this.h(10)) {
            this.cL |= 64;
            this.di = 64;
         }
      } else if (this.cH == 134) {
         this.cL |= 2048;
         this.di = 2048;
         this.bw = (byte)(this.bw & -9);
      } else if (this.cH == 135) {
         this.cL |= 256;
         this.di = 256;
         this.bw = (byte)(this.bw & -129);
      } else if (this.cH == 140) {
         this.di = 65536;
         this.cL |= 65536;
      } else if (this.cH == 144) {
         this.di = 131072;
         this.cL |= 131072;
      } else if (this.cH == 156) {
         if (!this.h(4)) {
            this.di &= -3;
            this.di |= 8192;
            this.cL |= 8192;
         } else {
            this.di &= -9;
            this.di |= 1024;
            this.cL |= 1024;
         }
      }

      if (--this.cI > 0) {
         this.r(this.cH + 1);
      } else {
         this.cC = false;
         this.cI = 0;
         if (this.cH >= 104 && this.cH <= 156 + cz[17] && this.Z != 0) {
            this.y();
            a(dB);
            this.b = 18;
         }

         if (this.cH + 1 == cA[33] + cz[33]) {
            this.y();
            a(dB);
            this.b = 18;
         }

         if (this.cH + 1 == cA[31] + cz[31]) {
            this.g = true;
         }

         if (this.cH + 1 == cA[32] + cz[32]) {
            this.player.a((byte)1);
            this.player.am = 0;
         }
      }
   }

   public final void writeSaveData(byte[] var1) {
      var1[0] = (byte)(this.Z == 0 ? 2 : 1);
      this.midlet.writeInt(this.cL, var1, 1);
      this.midlet.writeInt(this.bT, var1, 5);
      this.midlet.writeInt(this.bU, var1, 9);
      this.midlet.writeInt(this.cJ, var1, 13);
      var1[17] = this.player.O;

      for (int var2 = 0; var2 < 8; var2++) {
         var1[18 + var2] = this.player.P[var2];
         this.midlet.a(this.player.N[var2], var1, 26 + var2 * 2);
         this.midlet.a(this.player.M[var2], var1, 42 + var2 * 2);
      }

      this.midlet.writeInt(this.aX, var1, 58);
      var1[62] = this.bw;
      this.midlet.writeInt(this.bx, var1, 63);
      this.midlet.writeInt(this.by, var1, 67);
      this.midlet.writeInt(this.cr, var1, 195);
      var1[199] = (byte)(this.bZ ? 1 : 0);
      this.midlet.writeInt(this.di, var1, 200);
      this.midlet.writeInt(this.cK, var1, 204);
      this.midlet.writeInt(dA, var1, 208);
      var1[212] = (byte)(this.aa ? 1 : 0);
      var1[213] = this.ac;
   }

   public final void readSaveData(byte[] var1) {
      if (var1[0] == 2) {
         this.f(0);
      } else {
         this.cL = this.midlet.readInt(var1, 1);
         this.bT = this.midlet.readInt(var1, 5);
         this.bU = this.midlet.readInt(var1, 9);
         this.cJ = this.midlet.readInt(var1, 13);
         this.player.O = var1[17];

         for (int var2 = 0; var2 < 8; var2++) {
            this.player.P[var2] = var1[18 + var2];
            this.player.N[var2] = this.midlet.readShort(var1, 26 + var2 * 2);
            this.player.M[var2] = this.midlet.readShort(var1, 42 + var2 * 2);
         }

         this.aX = this.midlet.readInt(var1, 58);
         this.bw = var1[62];
         this.bx = this.midlet.readInt(var1, 63);
         this.by = this.midlet.readInt(var1, 67);
         this.cr = this.midlet.readInt(var1, 195);
         this.bZ = var1[199] == 1;
         this.di = this.midlet.readInt(var1, 200);
         this.cK = this.midlet.readInt(var1, 204);
         dA = this.midlet.readInt(var1, 208);
         this.aa = var1[212] != 0;
         this.ac = var1[213];
         this.ec = this.cu = 0;
         this.W();
         this.b = 3;
      }
   }

   public final void T() {
      if (this.b == 0) {
         int var1 = this.cx & 63;
         byte var2 = this.player.P[this.player.L];
         if (var1 > 0) {
            this.cx++;
            if (var1 >= b.g[var2][this.player.L]) {
               if ((this.cx & 128) != 0) {
                  this.cx = 129;
                  this.player.p();
               } else {
                  this.cx = 0;
               }
            }
         }
      }

      if (this.b == 11) {
         if ((this.di & 524288) > 0) {
            this.di = 4;
         }
      } else if (this.b == 16 && this.h == 4) {
         this.ee++;
         if (this.ee > 20) {
            if (this.g && this.j == 0) {
               this.b = 0;
               this.e = true;
               this.updateCamera();
            }

            if (this.g) {
               this.j--;
            } else {
               this.j++;
            }

            if (this.j > 4) {
               this.j = 4;
            }

            this.ee = 0;
         }

         this.d = true;
      }

      if (this.b != 0) {
         if (this.dj++ > 12) {
            this.dj = 0;
         }

         this.d = true;
      }
   }

   private byte a(byte var1, byte var2, byte var3) {
      return var1 > var2 ? --var1 : var3;
   }

   private byte b(byte var1, byte var2, byte var3) {
      return var1 < var2 ? ++var1 : var3;
   }

   public final String a(String var1, Object[] var2) {
      int var5 = 0;
      boolean var7 = false;
      StringBuffer var9 = new StringBuffer(var1.length());
      char[] var10 = new char[var1.length()];
      var1.getChars(0, var1.length(), var10, 0);

      for (int var11 = 0; var11 < var10.length; var11++) {
         char var3;
         char var4;
         int var6;
         if ((var3 = var10[var11]) == '%' && (var6 = var11 + 1) < var10.length && (var7 = Character.isDigit(var4 = var10[var6]))) {
            var5 = Character.digit(var4, 10);
         }

         if (var7) {
            var7 = false;
            var9.append(var2[var5]);
            var11++;
         } else {
            var9.append(var3);
         }
      }

      return var9.toString();
   }

   public final int a(Graphics var1, String var2, int var3, int var4, int var5) {
      int var6 = 0;
      int var7 = ratchetandclank.currentFont.a;
      var6 = ratchetandclank.currentFont.a(var2);
      var1.setClip(var4, 0, var5 - var4, 220);
      ratchetandclank.currentFont.a(var1, var2, this.l, var3, 20);
      this.l -= 2;
      if (this.l + var6 <= var4) {
         this.l = var5;
      }

      return var7;
   }

   private void W() {
      this.cu = 0;

      for (int var1 = 0; var1 < 19; var1++) {
         if ((this.di & 1 << var1) > 0) {
            return;
         }

         this.cu++;
      }
   }

   private void X() {
      this.cu = 0;
      dk = 0;
      if ((this.player.O & 64) <= 0) {
         for (int var1 = 0; var1 < 11; var1++) {
            if ((this.cL & 1 << 21 + var1) != 0) {
               this.cu++;
            }
         }

         if (this.cu >= 8) {
            dk = 2;
         } else if (this.cu >= 4) {
            dk = 1;
         } else {
            dk = 0;
         }
      }
   }

   private int Y() {
      int var2 = 0;
      int[] var1 = new int[8];

      for (int var3 = 0; var3 < 8; var3++) {
         var1[var3] = b.d[var3 * 3 + this.player.P[var3]] - this.player.M[var3];
         switch (var3) {
            case 0:
               this.aZ[var3] = 0;
               break;
            case 1:
               var2 += var1[var3];
               this.aZ[var3] = var1[var3];
               break;
            case 2:
               if ((this.player.O & 1 << var3) > 0) {
                  var2 += var1[var3];
                  this.aZ[var3] = var1[var3];
               }
               break;
            case 3:
               if ((this.player.O & 1 << var3) > 0) {
                  var2 += var1[var3] * 3;
                  this.aZ[var3] = var1[var3] * 3;
               }
               break;
            case 4:
               if ((this.player.O & 1 << var3) > 0) {
                  var2 += var1[var3] * 3;
                  this.aZ[var3] = var1[var3] * 3;
               }
               break;
            case 5:
               if ((this.player.O & 1 << var3) > 0) {
                  var2 += var1[var3] * 2;
                  this.aZ[var3] = var1[var3] * 2;
               }
               break;
            case 6:
               this.aZ[var3] = 0;
               break;
            case 7:
               if ((this.player.O & 1 << var3) > 0) {
                  var2 += var1[var3] * 5;
                  this.aZ[var3] = var1[var3] * 5;
               }
         }
      }

      return var2;
   }

   public final int a(Graphics var1, String var2, int var3, int var4, int var5, int var6) {
      int var7 = 0;
      int var8 = 0;
      int var9 = 0;
      int var12 = ratchetandclank.currentFont.a;
      char[] var14 = new char[var2.length()];
      var2.getChars(0, var2.length(), var14, 0);
      if ((var5 & 1) > 0) {
         var3 = 0;
         var7 = 0;
      } else {
         var7 = var3;
      }

      int var10 = 0;
      int var11 = 0;
      char var13 = '\u0000';
      boolean var15 = false;

      do {
         var8 = 0;
         boolean var16 = false;
         boolean var17 = false;
         int var18 = -1;
         var9 = var11;

         while (true) {
            if (var11 >= var14.length) {
               var16 = true;
               break;
            }

            if ((var13 = var14[var11]) == '.' || var13 == '/') {
               var18 = var11;
            }

            if (var13 == ' ') {
               var8 += ratchetandclank.currentFont.a(var13);
               var11++;
               if (var7 + var8 > var6 && !var15) {
                  var17 = true;
                  break;
               }

               var15 = true;
               break;
            }

            int var19 = ratchetandclank.currentFont.a(var13);
            if (var7 + var8 + var19 > var6 && !var15) {
               var17 = true;
               break;
            }

            var8 += var19;
            var11++;
         }

         if (var7 + var8 <= var6 && !var16 && !var17) {
            var7 += var8;
         } else {
            if (var7 + var8 > var6) {
               if (!var17) {
                  var11 = var9;
               } else if (var18 > 0) {
                  var11 = var18;
               }
            } else if (var17 && var18 > 0) {
               var11 = var18;
            }

            if (var11 - var10 > 0) {
               if ((var5 & 1) > 0) {
                  if (var11 - var10 > 0) {
                     ratchetandclank.currentFont.a(var1, var14, var10, var11 - var10, 88, var4, var5);
                  }
               } else if (var11 - var10 > 0) {
                  ratchetandclank.currentFont.a(var1, var14, var10, var11 - var10, var3, var4, var5);
               }
            }

            var4 += var12;
            var7 = var3;
            var15 = false;
            var10 = var11;
         }
      } while (var11 < var14.length);

      return var4;
   }

   public static final String a(String var0) {
      StringBuffer var3 = new StringBuffer();
      String var4 = "";

      try {
         InputStream var5;
         if ((var5 = var4.getClass().getResourceAsStream(var0)) == null) {
            return "";
         }

         InputStreamReader var6 = new InputStreamReader(var5);

         int var1;
         while ((var1 = var6.read()) != -1) {
            char var2 = (char)var1;
            var3.append(var2);
         }

         var4 = var3.toString();
         var6.close();
      } catch (IOException var7) {
      }

      System.gc();
      return var4;
   }
}
