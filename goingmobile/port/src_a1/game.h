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
  void e_(Graphics* g);                              // approx: tiled backdrop
  int a_(Graphics* g, const String& text, int x, int y, int anchor, int width);  // exact: word-wrap draw, returns new y
  static String a_(const String& fmt, std::initializer_list<String> args);      // exact: %0/%1 substitution
  int k_();  // approx: content-area top y (only game.g() call site is on the
             // unlock-code screen, out of scope -- see intromanager.cpp)

  // --- sprite-render helpers (transcribed from Game.java b/p/a(g,x,y,int)) ---
  void b_(Graphics* g, int x, int y, int w, int h);  // HUD/screen-clamped setClip
  void p_(int idx);                                   // cache sprite lookup for player(bb)/enemy idx
  void c_(Graphics* g, int enemyIndex, int x, int y, int yOffset);  // rotated/flipped enemy draw
  void a_(Graphics* g, int x, int y, int yOffset);    // mirrored player-sprite draw

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
