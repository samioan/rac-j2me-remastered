// game.h -- port of goingmobile/src/Game.java (class f): the game itself.
// FullCanvas (128x128) + the tick/paint loop, menus, splash chain, scoring,
// save format, and the player/enemy/projectile pools.
//
// Naming: obfuscated methods keep their letter + trailing '_' (C++ cannot
// overload a method name against a field of the same name). Renamed members
// (docs/CLASS_MAP.md) keep their phase-1 names. Milestone 3.1 (this commit)
// ports the boot/splash/menu/save surface; gameplay internals are declared
// here and stubbed in game.cpp until milestone 3.2.
#pragma once
#include "midp.h"
#include "midlet.h"
#include "levelmap.h"
#include "player.h"
#include "enemy.h"
#include "projectile.h"

struct MenuItem {
  jbyte type = 0;
  jbyte action = 0;
  jbyte stringId = 0;
  bool enabled = true;
  MenuItem() {}
  MenuItem(jbyte t, jbyte a, jbyte s) : type(t), action(a), stringId(s), enabled(true) {}
};

// Java java.util.Random (exact semantics, for gameplay parity in 3.2).
struct JRandom {
  uint64_t seed = 0;
  JRandom() { setSeed(88675123); }
  void setSeed(uint64_t s) { seed = (s ^ 0x5DEECE66DULL) & ((1ULL << 48) - 1); }
  int next(int bits) {
    seed = (seed * 0x5DEECE66DULL + 0xB) & ((1ULL << 48) - 1);
    return (int)(seed >> (48 - bits));
  }
  int nextInt() { return next(32); }
  int nextInt(int n) {  // Java nextInt(bound)
    if (n <= 0) return 0;
    if ((n & -n) == n) return (int)((n * (uint64_t)next(31)) >> 31);
    int bits, val;
    do { bits = next(31); val = bits % n; } while (bits - val + (n - 1) < 0);
    return val;
  }
};

class Game {
 public:
  // --- statics (as in the decompile) --------------------------------------
  static ratchetandclank* a;          // the MIDlet
  static jint f;                      // BOSS_HIT_POINT_SHELL (35)
  static jint g;                      // BOSS_HIT_POINT_KERNEL (100)
  static jbyte h;                     // BOSS_FIRE_RATE (22)
  static bool i, j;
  static int k, l, m;                  // text-screen cursor state
  static std::vector<String>* n;       // help text
  static std::vector<String>* o;       // about text
  static std::vector<String>* p;       // credits text
  static std::vector<String>* q;       // message/results text
  static int r, s;                     // camera x/y
  static bool t, u, v;
  static jbyte w;
  static bool x;
  static int y;
  static const char* A[5];             // language names
  static const char* B[5];             // language suffixes
  static jbyte C;                      // selected language
  static std::vector<std::vector<MenuItem>> H;  // menu pages (from /o)
  static jbyte* menuSoftLeft;
  static jbyte* menuSoftRight;
  static jbyte* menuTitle;
  static jbyte* menuBackTarget;
  static int I, J, K, L, M;            // menu cursor / page / scroll state
  static Image* tileSetImage;
  static Image* enemySegmentImage;
  static Image* hudIconImage;
  static Image* portraitImage;
  static Image* smallSpriteImage;
  static Image* playerImage;
  static Image* weaponImage;
  static Image* titaniumBoltImage;
  static Image* actorImage;
  static const jbyte INFOLINK_MESSAGE_TICKS[40];
  static const jshort INFOLINK_MESSAGE_IDS[40];
  static jbyte MAX_ENEMIES;
  static jbyte PLAYER_SLOT;
  static jbyte TITANIUM_BOLTS_FOR_RYNO;
  static jbyte TITANIUM_BOLT_SLOTS;
  static int repaintDelay;
  static int bA, bB, bG, bH;
  static int enemyKills, baseScore, boltsCollected, levelsCleared;
  static int specialKills, timeBonus;
  static jlong scoringStart, timerStart, levelTimeMs, PAR_TIME_MS;
  static int totalScore, totalTitaniumBolts, totalKills;
  static int shotsFired, enemyShotHits, boxesHit;
  static bool noDamageBonus, fastKillBonus, allKilledBonus, playerDamaged;
  static int enemiesRemainingPerLevel[12];
  static bool enemiesCountedPerLevel[12];
  static const jbyte BOSS_SHELL_COL[4];
  static const jbyte BOSS_SHELL_ROW[4];
  static const jbyte KEYPAD_CHARS[10][9];
  static const int KEYPAD_CHARS_LEN[10];

  // --- instance state ------------------------------------------------------
  jbyte b = 0;               // mode: 0 game, 19 menu, 20 splash
  bool c = false;            // menu music started
  bool d = false;            // N_() boot init done
  bool e = false;            // draw overlay once
  jbyte z = 0;
  jbyte D = 114;             // special-screen state (114 = none)
  String E;                  // name-entry buffer (capacity 14)
  static const int E_CAPACITY = 14;
  jlong lastMultitapTime = 0;
  int lastMultitapKey = -1;
  int multitapCycle = 0;
  String nameEntryWarning;
  // Bare letters (no trailing underscore): these are data, and would
  // otherwise collide with the F_()/G_() gameplay stubs below.
  jint F[2] = {652482873, 766492548};
  jint G[2][8] = {};
  Image* digitStripImage = nullptr;
  LevelMap* N = nullptr;
  JRandom O;
  jshort P = -1;             // active grid
  jbyte Q = 1;               // level number
  jbyte R = 0;               // spawn column
  jbyte S = 0;               // spawn row
  jshort T = 0, U = 0;       // spawn camera x/y
  jshort V = 0;
  Enemy* enemies[5] = {};
  Player* player = nullptr;
  Projectile* playerShots[10] = {};
  Projectile* enemyShots[10] = {};
  jlong ac = 0;               // splash timer
  jbyte ad = 0;              // splash index (0 sony, 1 hhg, 2 logo)
  Image* splashImage = nullptr;
  bool af = false;           // splash needs draw
  jbyte sprKind = 0, sprAnimState = 0, sprFrameOffset = 0, sprOrientation = 0;
  jint sprAnimFrame = 0;
  jint boltCount = 0;
  jint storeAmmoNeeded[8] = {};
  jshort zipX1[4] = {}, zipY1[4] = {}, zipX2[4] = {}, zipY2[4] = {}, zipDx[4] = {};
  jint zipDy[4] = {}, zipBaseY[4] = {};
  jshort titaniumBoltX[20] = {}, titaniumBoltY[20] = {};
  jbyte titaniumBoltType[20] = {};
  jbyte bossShellState[4] = {}, bossShellTimer[4] = {};
  jint bossHp[5] = {};
  jbyte bossAim = 0, bossFireTick = 0, bossFirePhase = 0;
  jbyte exitTileX = 0, exitTileY = 0;
  jint area1BoltsTaken = 0, area2BoltsTaken = 0;
  jint pickupX[6] = {}, pickupY[6] = {};
  jshort pickupVx[6] = {}, pickupVy[6] = {};
  jbyte pickupType[6] = {};
  jshort platformX[4] = {}, platformY[4] = {}, platformDx[4] = {}, platformDy[4] = {};
  jbyte platformState[4] = {};
  jint levelElapsedMs = 0;
  jlong lastTickMs = 0;
  jbyte bg = 0;              // menu cursor row
  jbyte cr = 0;              // previous menu selection
  jint bh = 0, bi = 0, bj = 0;
  bool weaponOverlayOpen = false;
  String messageText;
  int messagePortrait = 0, messagePortraitFrame = 0, messageScroll = -1;
  int messageChar = -1, messageStringId = 0;
  jbyte messageTimer = 0;
  jint collectiblesTaken1 = 0, collectiblesTaken2 = 0;
  jint levelStateMask = 0, introSeenMask = 0;
  bool by = false, bz = false;
  std::vector<String>* cs = nullptr;   // name-entry info screen
  std::vector<String>* ct = nullptr;
  int cu = 0, cv = 0;
  int splashHoldoff = 0;
  jint ca = 0;
  bool isHidden = false;
  jlong hiddenSince = 0;
  jlong nowMs = 0;
  bool repaintEachFrame = false;
  bool bossVulnerable = false;
  bool scoringActive = false;
  std::vector<std::vector<jshort>> PORTRAIT_TABLE;
  bool repaintRequested = false;
  void repaint() { repaintRequested = true; }

  explicit Game(ratchetandclank* midlet);

  // --- lifecycle / loop (ported) ------------------------------------------
  void c_();                 // start (was: setCurrent + thread)
  void run_();                // one serial step: tick + repaint request
  void paint(Graphics* g);
  void keyPressed(int keyCode);
  void keyReleased(int keyCode);
  void a_();                 // hideNotify
  void b_();                 // showNotify
  static void a_(int ms);    // Thread.sleep

  // --- ported boot/menu surface -------------------------------------------
  void t_();                 // random init + build pools + MIDlet-Spec-Code
  void o_();                 // splash setup
  void p_();                 // game-over/results screen setup
  void q_();                 // hide->pause menu
  void r_();                 // -> pause menu
  void s_();                 // level results setup
  void k_();                 // scoring init
  int l_();                  // enemies alive
  void f_(int level);        // record remaining enemies
  static void m_();          // clear per-level counters
  static void a_(jbyte phase);  // scoring state machine
  int n_();                  // levels cleared count
  static void a_(std::vector<String>* src);  // append into q
  static void a_(std::vector<String>* dst, std::vector<String>* src);  // append src into dst
  static void a_(const String& path);  // /o menu loader
  void a_(Graphics* g);      // splash draw
  void b_(Graphics* g);      // menu draw
  int b_(Graphics* g, int slot, int y);  // digit strip draw
  void f_(int rawKey, int navKey);  // menu input
  static void c_(Graphics* g, int left, int right);  // softkey bar
  static void a_(Graphics* g, std::vector<String>* text, bool centered);
  void u_();                 // platform mover
  void v_();                 // the tick
  void a_(const String& name, int slot);  // name -> digit strip
  static int g_(int v);
  void w_();                 // camera
  static Image* b_(const String& name);  // "/<name>.png"
  static void x_();          // load all sprites
  void y_();                 // free all sprites + reload menu
  void z_();                 // build pools
  void h_(int level);        // enter level (results variant)
  void A_();                 // new-game state reset
  void i_(int level);        // enter level (start variant)
  int B_();                  // titanium bolts remaining
  void g_(int area, int idx);  // take titanium bolt
  void d_();                 // respawn at checkpoint
  void a_(jbyte* out);       // save serialize
  void b_(jbyte* in);        // save deserialize
  void j_();                 // level-intro check (scoring)
  void e_(int stringId, int portrait);  // show message overlay
  void L_();                 // advance/close message overlay
  void M_();                 // per-tick: charge + splash advance
  void N_();                 // boot init (font, rms, sounds, strings, /o)
  int O_();                  // store: total ammo price
  int a_(Graphics* g, const String& s, int y, bool selected);
  int b_(Graphics* g, const String& s, int y, bool selected);
  int a_(Graphics* g, jbyte slot, int y, bool selected);
  int a_(Graphics* g, int id, int value, int y, bool selected);
  int a_(Graphics* g, const String& s, int y);  // title draw
  static String o_(int ms);  // mm:ss:cc format
  String i_(int id, int value);
  static String a_(const String& fmt, const std::vector<String>& args);

  // --- gameplay internals (milestone 3.2 stubs) ---------------------------
  void h_(int rawKey, int navKey);  // gameplay input
  void e_(int idx);                 // player-shot tick
  void k_(int idx);                 // enemy-shot tick
  void F_();
  void G_();
  void I_();                        // boss tick
  void J_();                        // boss setup
  void K_();                        // enemy-vs-player tick
  void n_(int idx);                 // enemy AI tick
  void i_();                        // fire-button release (charge cleanup)
  void c_(Graphics* g);             // overlay/level-transition draw
  void d_(Graphics* g);
  void f_(Graphics* g);
  void g_(Graphics* g);
  void e_(Graphics* g);
  void h_(Graphics* g);
  void i_(Graphics* g);             // boss render
  void j_(Graphics* g);             // message overlay render
  int a_(Graphics* g, int scroll);  // message scroll render
  void a_(Graphics* g, int a, int b);
  void a_(Graphics* g, int a, int b, int c);
  static int a_(Graphics* g, const String& s, int x, int y, int anchor);
  bool c_(int idx);
  bool a_(int a, int b);
  short e_();
  short f_();
  bool g_();
  bool d_(int v);
  bool D_();
  bool E_();
  int h_();
  int b_(int a, int b);
  void c_(int x, int y);            // boss-shell hit at a pixel position


};
