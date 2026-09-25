// game.h -- port of goingmobile/src_a1/Game.java (class h, the 195KB
// engine). a1 moved the legacy build's in-Game menu system entirely into
// IntroManager (see intromanager.h), so Game itself is gameplay-only:
// tick/render/collision, the player/enemy/projectile pools, and save-data
// serialization.
//
// Scoped to milestone 3.1a's boot-chain needs: the real constructor and
// runBootStep() (all 42 steps -- asset loading plus constructing the
// Player/Enemy/LevelMap/Projectile pools, see player.h/enemy.h/
// levelmap.h/projectile.h's own header notes for why those are real-but-
// behavior-stubbed here too), t() (mapData.txt parsing), and sleep().
// Everything else is still just the call surface CanvasShell.java and
// ratchetandclank.java exercise (pause/resume/render/tick/keyPressed/
// keyReleased/writeSaveData/readSaveData/cu/c_/m_) -- Game's own ~20-
// screen in-level UI state machine (fields `b`/`d(Graphics)` etc., a
// SEPARATE state machine from IntroManager's pre-game menu, covering
// pause/store/results/game-over while playing) is gameplay-adjacent and
// deferred to milestone 3.2a along with the tick loop itself.
#pragma once
#include "midp.h"
#include "levelmap.h"
#include "player.h"
#include "enemy.h"
#include "projectile.h"
#include <initializer_list>

class ratchetandclank;

// java.util.Random (exact LCG semantics; boot only needs it to exist).
struct JRandom {
  uint64_t seed = 0;
  JRandom() { setSeed((uint64_t)currentTimeMillis()); }
  void setSeed(uint64_t s) { seed = (s ^ 0x5DEECE66DULL) & ((1ULL << 48) - 1); }
  int nextInt() { return next(32); }
  int next(int bits) {
    seed = (seed * 0x5DEECE66DULL + 0xB) & ((1ULL << 48) - 1);
    return (int)(seed >> (48 - bits));
  }
};

class Game {
 public:
  explicit Game(ratchetandclank* midlet);

  ratchetandclank* midlet;

  // --- boot-chain statics (confirmed, see Player.java/Enemy.java's own
  // cross-checks) ----------------------------------------------------------
  static jbyte tileWidth;
  static jbyte tileHeight;
  static jbyte hudHeight;
  static jbyte J;
  static jbyte K;
  static jbyte L;   // -16, sprite y bias
  static jint x;    // camera x offset
  static jint y;    // camera y offset
  static jbyte enemyPoolSize;
  static jbyte bb;
  static const String aR[4];  // background image paths, indexed by aQ

  // --- boot-chain image cache (obfuscated names, kept as in the
  // decompile -- see src_a1/Game.java's runBootStep for what each loads).
  static Image* am; static Image* an; static Image* ao; static Image* ap;
  static Image* aq; static Image* ar; static Image* as; static Image* at;
  static Image* au; static Image* av; static Image* aw; static Image* ax;
  static Image* ay; static Image* az; static Image* aA; static Image* aB;
  static Image* aC; static Image* aD; static Image* aE; static Image* aF;
  static Image** aG; static Image* aH; static Image* aI; static Image* aJ;
  static Image* aK; static Image* aL; static Image* aM; static Image* aN;
  static Image* aO; static Image* aP;
  static jbyte dk;
  static jint dA;
  static jshort v;
  static jshort w;

  // --- boot-chain instance state -------------------------------------------
  bool r = false;

  // --- gameplay-state fields (obfuscated names kept, semantics per Game.java) ---
  jbyte b = 0, c = 0;        // b: UI/gameplay state (0 playing, 17 intro cutscene, ...)
  bool e = false, g = false, m = false;
  jbyte h = 0, i = 0, j = 0, k = 0, n = 0, o = 0, p = 0, q = 0, s = 0;
  jint l = 176, u = 0, U = 0;
  jshort Y = -1;
  bool aa = false;
  jbyte ab = 1, ac = -1, ad = 0, ae = 0;
  jshort af = 0, ag = 0, ah = 0;
  jint aX = 0, aY = 0;
  jbyte bK = 0, bL = 0, bM = 0, bP = 0, bQ = 0, bX = 0, bY = 0;
  jint bV = 0, bW = 0;
  jbyte bz = 0, bA = 0, bB = 0;
  bool bZ = true;
  jbyte ca = 0, cb = 0, cc = 0, cd = 0, ce = 0, cf = 0;
  jint cr = 0;
  jlong cs = 0, ct = 0, cy = 0;
  jint cv = 0, cw = 0, cx = 0;
  bool cC = false, cD = false;
  String cE;
  jint cF = -1, cG = -1, cH = 0;
  jbyte cI = 0;
  jint cL = 0, cM = 0, cN = 0, cO = 0;
  jshort cP = 0;
  bool cS = false, cT = false, cV = false, cW = false, cX = false, cZ = false;
  jbyte cU = 0, cY = 0, da = 0, db = 0, dj = 0;
  jint di = 0, dl = 0, dC = 0;
  jint dT = -1, dU = -1;
  bool dV = false;
  jlong dW = 0;
  jint dY = 0, ea = 0;
  bool dZ = false, eb = false;
  static bool f, z, A, B, dF, dG, dH, dI;
  static jbyte C, dy, dB;
  static jint D, E, dm, dn, do_, dp, dq, dr, ds, dt, du, dv, dz, dD, dE;
  static jlong dw;
  jshort X = -1;    // current room index
  jbyte Z = 1;      // current level number (1..10; 6..10 are the second half)
  jbyte bw = 0;     // gate bitmask consulted by LevelMap::isWalkable
  jint bx = 0, by = 0, bT = 0, bU = 0, cJ = 0, cK = 0;  // per-room/item bitsets
  jint dX = -1;     // tentative, set by IntroManager::a_(jbyte) on entering the main menu
  bool d = false;   // dirty/redraw flag set by Player::updateAnimation
  jbyte M = 0;
  jbyte aS = 0, aT = 0, aV = 0, aW = 0;
  jint aU = 0;
  jint I = 0;
  jbyte aQ = 0;
  JRandom* random = nullptr;
  LevelMap* levelMap = nullptr;
  Player* player = nullptr;
  Enemy** enemies = nullptr;
  Projectile** playerProjectiles = nullptr;
  Projectile** enemyProjectiles = nullptr;

  // Obfuscated arrays allocated by runBootStep's step 41 -- sizes/types
  // only, semantics not yet derived (Game's own phase-1 isn't done; see
  // docs/CLASS_MAP.md's "Game" section).
  jbyte* bH = nullptr; jbyte* bI = nullptr; jint* bJ = nullptr;
  jbyte* bF = nullptr; jbyte* bG = nullptr; jbyte* bC = nullptr;
  jbyte* bD = nullptr; jbyte* bE = nullptr;
  jshort* bc = nullptr; jshort* bd = nullptr; jshort* be = nullptr;
  jshort* bf = nullptr; jshort* bg = nullptr;
  jint* bh = nullptr; jint* bi = nullptr;
  jshort* bj = nullptr; jshort* bk = nullptr;
  jshort* bl = nullptr; jshort* bm = nullptr; jbyte* bn = nullptr;
  jshort* bo = nullptr; jshort* bp = nullptr;
  jint* bu = nullptr;
  jshort* bq = nullptr; bool* bs = nullptr; jshort* br = nullptr;
  bool* bt = nullptr; jint* bv = nullptr;
  jint* cg = nullptr; jint* ch = nullptr; jbyte* ck = nullptr;
  jshort* ci = nullptr; jshort* cj = nullptr;
  jshort* cm = nullptr; jshort* cn = nullptr; jshort* co = nullptr;
  jshort* cp = nullptr; jbyte* cq = nullptr;
  static jint* dJ; static jint* dK; static bool* dL;
  jint* dc = nullptr;         // [13]
  jshort dd[62][3] = {};      // mapData.txt room table (t())
  jbyte de[43][2] = {};       // mapData.txt second table (t())
  jint* aZ = nullptr;         // [8]

  bool h_(int bit);              // cJ/cK unlock bitset test (0..39)
  bool f_(int level, int room);  // bT/bU per-room flag test
  void d_(int level, int room);  // sets bw bit 0 from bx/by room bitset
  void x_();                     // swap tile sheet aF to the background for LevelMap::startSubGrid
  void runBootStep(int step);
  void t();  // parses mapData.txt into dd/de
  static void sleep(int ms);
  static String readResourceText(const String& path);  // Game.a(String)

  // --- menu-screen rendering helpers (Game.a(...)/e(...)/k()/g()) --------
  // Game's own phase-1 read-through isn't done (see game.h's top-of-file
  // note and CLASS_MAP.md's "Game" section, deferred to milestone 3.2a) --
  // these are the handful of its methods IntroManager's menu screens call
  // into, given real SIMPLIFIED bodies rather than reverse-engineered
  // pixel-perfect ones (documented per call): a tile-clear rect, a
  // full-screen tiled backdrop, a word-wrap paragraph draw (this one IS
  // exact -- generic MIDP text layout, no Game internals needed), a %N
  // placeholder formatter (exact, pure string logic), and two int getters
  // approximated from their call-site usage (a content-area top y and a
  // content width). Revisit for pixel fidelity once Game's own phase-1
  // lands.
  void a_(Graphics* g, int x, int y, int w, int h);  // approx: clear-rect
  void e_(Graphics* g);                              // wpnhud frame at (j(),75)
  int a_(Graphics* g, const String& text, int x, int y, int anchor, int width);  // exact: word-wrap draw, returns new y
  static String a_(const String& fmt, std::initializer_list<String> args);      // exact: %0/%1 substitution
  int k_();  // approx: content-area top y (only game.g() call site is on the
             // unlock-code screen, out of scope -- see intromanager.cpp)

  // --- sprite-render helpers (transcribed from Game.java b/p/a(g,x,y,int)) ---
  void b_(Graphics* g, int x, int y, int w, int h);  // HUD/screen-clamped setClip
  void p_(int idx);                                   // cache sprite lookup for player(bb)/enemy idx
  void c_(Graphics* g, int enemyIndex, int x, int y, int yOffset);  // rotated/flipped enemy draw
  void a_(Graphics* g, int x, int y, int yOffset);    // mirrored player-sprite draw


  // --- level entry / spawn helpers (Game.java a/c/d/e/f/P/Q/u ...), see game_level.cpp ---
  void a_(int col, int row);                                   // bj[] zip-hook anchor
  void a_(int col, int row, bool isStart, int idx);            // zip-line endpoint
  void a_(int col, int row, int spriteRow, int rowKind, int idx);  // moving-platform slot
  void a_(int col, int row, jbyte enemyAnim, int slot);        // enemy spawn (slot -1 = first free)
  void a_(jbyte col, jbyte row);                               // bF[] switch anchor
  void a_(jbyte col, jbyte row, int tile);                     // bC[] gated-wall anchor
  void a_(int x, int y, int type, bool up, bool horiz, int dir, jbyte srcAnim);  // enemy projectile
  void c_(int x, int y, int kind);                             // bolt drop
  void d_(int col, int row, int kind);                         // moving-platform spawn (cq[])
  void e_(int x, int y, int type);                             // player projectile spawn
  void P_();
  void Q_();
  void u_();                                                    // new-game state reset
  void d_();                                                    // stats reset
  void f_();                                                    // stats arrays reset
  int  e_();                                                    // live enemy count
  void b_(int slot, int unused);                                // dJ[slot] = e_()
  void f_(int level);                                           // start a level
  int  b_(int x, int y, int unused);                            // ground y under (x,y)
  bool a_(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2);  // rect overlap
  int  D_(int a, int b) { return a < b ? a : b; }
  int  abs_(int v) { return v < 0 ? -v : v; }
  void n_();  // pause path (Game.n)
  void o_();  // resume path (Game.o)
  void d_(Graphics* g);            // pause menu (b==4)
  void f_(Graphics* g);            // sound toggle (b==5)
  void v_(Graphics* g);            // quit-to-desktop confirm (b==7)
  void u_(Graphics* g);            // quit-to-menu confirm (b==8)
  void h_(int key, int action);    // b==4 input
  void i_(int key, int action);    // b==5 input
  // --- level flow: results (b==18), world map (b==3), level-end (b==21), info (b==10) ---
  static const jbyte N[20], O[20], P[20], Q[20], T[13];
  static const jlong dx[17];
  static const jbyte cQ[20];
  static const jshort cR[20];
  static const jbyte df[16], dg[16], dh[16][8];
  static void a_(jbyte page);                    // Game.a(byte) static score computation
  int i_();                                      // content width (wpnhud width - 20)
  int i_(int level);                             // secrets collected in a level
  void drawResults(Graphics* g, jbyte page);
  void drawWorldMap(Graphics* g);
  void drawInfo(Graphics* g);
  void drawLevelEnd(Graphics* g);
  void resultsKey(int key, int action);
  void mapKey(int key, int action);
  void levelEndKey(int key, int action);
  void infoKey(int key, int action);
  void W_();                                     // cu = first unlocked map node
  void exitLevel(int target);                    // Game.a(int,boolean): -1 = leave level to the world map
  void loadLevel(int level, jshort room);        // Game.a(int,short)
  // --- store / challenge rooms (game_store.cpp) ---
  static const jint cl[12];
  int numText(Graphics* g, int idx, int num, int x, int y, bool sel);
  void X_();                                     // extras list entry
  int Y_();                                      // ammo refill cost, fills aZ[]
  void drawExtras(Graphics* g);
  void drawWeaponBuy(Graphics* g);
  void drawBuyConfirm(Graphics* g);
  void drawNoFunds(Graphics* g);
  void drawChallengeIntro(Graphics* g);
  void drawChallengeEnd(Graphics* g);
  void drawChallengeFail(Graphics* g);
  void extrasKey(int key, int action);
  void weaponBuyKey(int key, int action);
  void buyConfirmKey(int key, int action);
  void noFundsKey(int key, int action);
  void challengeIntroKey(int key, int action);
  void challengeEndKey(int key, int action);
  void startChallenge(int room);
  void A_();                                     // challenge-room tick
  // --- boss fight (game_boss.cpp) ---
  bool bossT = false;
  void bossInit();                               // Game.N()
  void bossIntro(jbyte step);                    // Game.b(byte), b==16
  void bossOutro(jbyte step);                    // Game.c(byte), b==24
  void bossEffect();                             // Game.U()
  void bossFire();                               // Game.L()
  void bossHit(int part);                        // Game.o(int)
  void bossUpdate();                             // Game.M()
  void bossRender(Graphics* g);                  // Game.F(g)
  void bossMaxRender(Graphics* g, jbyte frame);  // Game.c(g,byte)
  void drawEndStats(Graphics* g);                // b==2
  void drawRestartPrompt(Graphics* g);           // b==23
  void endStatsKey(int key, int action);
  void restartKey(int key, int action);
  void restartGame();                            // Game.O()
  // --- weapon wheel (game_wheel.cpp) ---
  static const jbyte R[8], S[8];
  static const jshort dM[7][2];
  void wheelOutline(Graphics* g, int ox, int oy);
  void wheelFill(Graphics* g, int ox, int oy);
  void drawWheel(Graphics* g);
  void wheelKey(int key, int action);
  void confirmScreen(Graphics* g, int titleIdx);
  int a_(Graphics* g, const String& text, int y, int left, int right);  // scrolling ticker
  void confirmKey(int key, int action, bool toDesktop);  // b==7 / b==8 input
  void y_();  // reload menu backdrop into aF
  void z_();  // reload current level tile sheet into aF
  void C_(int x, int y);                                        // boss-marker landing
  void updateCamera();

  // --- in-level render (Game.render's default branch) ---------------------
  void a_(Graphics* g);            // zip hooks
  void b_(Graphics* g);            // bX pickup
  void c_(Graphics* g);            // cc save-point
  void D_(Graphics* g);            // ca pickup
  void z_(Graphics* g);            // zip lines
  void A_(Graphics* g);            // moving platforms
  void B_(Graphics* g);            // breakable/pickup platforms
  void C_(Graphics* g);            // bolts
  void E_(Graphics* g);            // enemies, player, projectiles
  void G_(Graphics* g);            // gated walls / switches
  void x_(Graphics* g);            // HUD
  void y_(Graphics* g);            // weapon-tray strip

  // --- gameplay tick/input/collision (game_play.cpp) ----------------------
  static const jbyte cz[40];
  static const jshort cA[40];
  static const jbyte cB[109];
  static jbyte bR, bS;
  jbyte ec = 0, ed = 0, ee = 0;
  int  G_();                       // index of enemy overlapping the player, or -1
  bool D_();                       // moving platform beside the player
  bool E_();                       // platform directly ahead (facing) at player y
  bool F_();                       // enemy directly ahead on the player's row
  bool k_(int enemy);              // enemy hitbox overlaps player
  short B_();                      // zip-line/platform floor scan
  short C_();                      // pickup-platform floor scan
  void j_(int edge);               // room-edge transition request
  void V_();                       // finish room transition
  void w_();                       // respawn after death
  void g_(int bit);                // clear cJ/cK bit (hint/pickup consumed)
  void g_(int level, int room);    // collect a secret: clear bT/bU bit + message
  int  v_();                       // count of collected secrets
  void e_(int level, int room);    // clear bx/by room bit
  void r_(int msgIdx);             // start hint/dialogue message
  void S_();                       // advance/close hint message
  void H_(Graphics* g);            // hint panel background
  int  a_(Graphics* g, int offset);
  int  a_(Graphics* g, int offset, const String& text, int x, int y, int anchor, int width);
  void a_(int col, int row, int tile);      // swing-target setup
  void A_(int key, int action);    // in-level key press
  void B_(int key, int action);    // in-level key release
  void n_(int slot);               // remove moving platform slot
  void l_(int idx);                // enemy projectile vs platforms
  void m_(int idx);                // player projectile/melee vs platforms
  void H_();
  void I_();
  void J_();
  void K_();
  void b_();
  void c_();
  void l_();
  void a_();                       // zip-hook grab
  void q_(int enemy);              // player projectiles vs enemy
  void R_();                       // enemy projectiles vs player
  void T_();                       // charge/ambient per-tick counters
  void d_(jbyte step);             // level-0 intro cutscene step (Game.d(byte))

  // --- call surface CanvasShell.java/ratchetandclank.java exercise -------
  void pause();
  void resume();
  void render(Graphics* g);
  void tick();
  void keyPressed(int key);
  void keyReleased(int key);

  void writeSaveData(jbyte* buf);
  void readSaveData(const jbyte* buf);

  // Tentative -- see ratchetandclank.java's startNewGame/continueGame/
  // returnToIntro (cu/c(int,int)/m()), obfuscated pending Game's own
  // phase-1 pass.
  jint cu = 0;
  void c_(int a, int b);
  void m_();
};
