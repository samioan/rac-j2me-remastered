// midp.h -- MIDP-2.0 / CLDC / Nokia-UI shim for the a1 port (milestone
// 3.1a). Re-based from ../src/midp.h (milestone 3.1's shim) onto a1: same
// javax.microedition/com.nokia.mid.ui surface (Graphics/Image/RecordStore/
// media are unchanged -- generic MIDP, not screen- or build-specific), plus
// two things a1 actually needs that the legacy build didn't:
//   - Command/CommandListener: a1's CanvasShell is a plain
//     javax.microedition.lcdui.Canvas + CommandListener (soft-key menu
//     commands), not Nokia's FullCanvas -- see ROADMAP.md's "The target".
//     Minimal placeholders for now; filled in once IntroManager's real
//     menu-screen construction (Form/List/TextField, the unlock-code UI)
//     is transcribed.
//   - SoundPlayer: a1 has 6 cues (not 7) and mixes .wav in with .mid
//     (src_a1/SoundPlayer.java's soundFormats table), vs. the legacy
//     build's all-.mid set.
// Screen is 176x220 (platform::canvas below), not 128x128.
//
// Strings hold Latin-1 bytes, same convention as ../src/midp.h.
#pragma once

#include <cstdint>
#include <string>
#include <vector>

typedef int8_t   jbyte;
typedef int16_t  jshort;
typedef int32_t  jint;
typedef int64_t  jlong;

typedef std::string String;       // Java String / StringBuffer (Latin-1 bytes)
typedef std::vector<std::byte> ByteArray;

// ---- java.lang-ish helpers ---------------------------------------------
int    length(const String& s);                 // s.size()
char   charAt(const String& s, int i);          // (char)(uint8_t)s[i]
String substring(const String& s, int b, int e);
String trim(const String& s);
String toUpperCase(const String& s);
bool   equals(const String& a, const String& b);
String valueOf(int v);
int    indexOf(const String& s, char c);

class Math {
public:
  static int abs(int v) { return v < 0 ? -v : v; }
  static int min(int a, int b) { return a < b ? a : b; }
  static int max(int a, int b) { return a > b ? a : b; }
};

// System.currentTimeMillis() / gc() / arraycopy / Thread.sleep
jlong currentTimeMillis();
inline void gc() {}
void arraycopy(const void* src, int srcPos, void* dst, int dstPos, int len);
void sleepMs(int ms);

// ---- lcdui --------------------------------------------------------------

// Anchor constants (MIDP values, as used by the game: 3, 17, 20, 64 ...)
enum Anchor {
  HCENTER = 1, VCENTER = 2, LEFT = 4, RIGHT = 8,
  TOP = 16, BOTTOM = 32, BASELINE = 64
};

// Nokia DirectGraphics manipulation constants.
enum Manip {
  MANIP_NONE = 0, MANIP_ROT_90 = 1, MANIP_ROT_180 = 2, MANIP_ROT_270 = 3,
  MANIP_MIRROR = 4, MANIP_MIRROR_ROT_90 = 5, MANIP_MIRROR_ROT_180 = 6,
  MANIP_MIRROR_ROT_270 = 7
};

// Key codes CanvasShell.keyPressed/keyReleased receive. a1's CanvasShell
// checks for '*' via the standard MIDP KEY_STAR value (42, see
// src_a1/CanvasShell.java's keyPressed); the game/movement codes below
// keep the legacy build's Nokia FullCanvas values (-1..-7) as a documented
// ASSUMPTION, not yet independently confirmed for a1 -- Nokia S60/UIQ
// devices are known to deliver the same negative codes to a plain
// Canvas.keyPressed as to FullCanvas (it's a platform-level key mapping,
// not a FullCanvas-specific feature), but this hasn't been checked against
// IntroManager's own keyPressed body yet (its menu-input handler isn't
// transcribed). Revisit once that lands.
enum KeyCode {
  KEY_UP = -1, KEY_DOWN = -2, KEY_LEFT = -3, KEY_RIGHT = -4, KEY_FIRE = -5,
  KEY_SOFT_LEFT = -6, KEY_SOFT_RIGHT = -7, KEY_SEND = -10, KEY_STAR = 42
};

// ---- lcdui Command/CommandListener --------------------------------------
//
// New vs. the legacy FullCanvas build: a1's CanvasShell is a plain
// javax.microedition.lcdui.Canvas implements CommandListener, so
// IntroManager builds real MIDP soft-key commands (see
// src_a1/CanvasShell.java's commandAction). Minimal placeholders -- no
// menu-screen construction is transcribed yet, so nothing constructs a
// real Command/Displayable in this milestone.
enum CommandType {
  CMD_SCREEN = 1, CMD_BACK = 2, CMD_CANCEL = 3, CMD_OK = 4, CMD_HELP = 5,
  CMD_STOP = 6, CMD_EXIT = 7, CMD_START = 8, CMD_ITEM = 9
};

struct Command {
  String label;
  int type = CMD_SCREEN;
  int priority = 0;
  Command() {}
  Command(const String& l, int t, int p) : label(l), type(t), priority(p) {}
};

// Stands in for javax.microedition.lcdui.Displayable (the unlock-code UI's
// Form/TextField, per ROADMAP.md -- not exercised by the shipped a1.jar,
// which carries no Unlock-Code manifest attribute).
struct Displayable {};

struct Image {
  int w = 0, h = 0;
  std::vector<uint32_t> px;  // 0xAARRGGBB

  bool isNull() const { return w == 0 || h == 0; }
  int getWidth() const { return w; }
  int getHeight() const { return h; }

  static Image* createImage(const String& path);  // "/name.png"
  static Image* createImage(int width, int height);
};

class Graphics {
 public:
  Graphics() {}
  explicit Graphics(Image* target) : target_(target) {}

  void setTarget(Image* target) { target_ = target; }

  void setColor(int rgb) { color_ = rgb & 0xFFFFFF; }
  void fillRect(int x, int y, int w, int h);
  void drawRect(int x, int y, int w, int h);
  void fillArc(int x, int y, int w, int h, int startAngle, int arcAngle);  // full ellipse only
  void drawLine(int x1, int y1, int x2, int y2);
  void fillRoundRect(int x, int y, int w, int h, int arcWidth, int arcHeight);
  void drawImage(Image* img, int x, int y, int anchor);
  void setClip(int x, int y, int w, int h);
  void clipRect(int x, int y, int w, int h);
  void translate(int dx, int dy);
  int getTranslateX() const { return transX_; }
  int getTranslateY() const { return transY_; }

  // Nokia DirectGraphics entry points -- folded directly into Graphics
  // rather than a separate DirectGraphics class (see canvasshell.h's
  // directGraphics field).
  void drawImageManip(Image* img, int x, int y, int anchor, int manipulation);
  void fillPolygon(const int* xs, const int* ys, int n, int argb);  // convex, triangle fan
  void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int argb);
  void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int argb);

 private:
  Image* target_ = nullptr;
  int color_ = 0;
  int transX_ = 0, transY_ = 0;
  int clipX_ = 0, clipY_ = 0, clipW_ = -1, clipH_ = -1;  // untranslated, image space
};


// ---- rms (file-backed RecordStore) -------------------------------------
//
// Same "RANDCSm" store name and 4-record layout as the legacy build, but
// a1's game-slot records are 214 bytes (not 220) -- confirmed by
// src_a1/ratchetandclank.java's verifySaveStore()/rebuildSaveStore(). Kept
// in a separate file under %LOCALAPPDATA%/rac-gm-port-a1/ so a1 saves
// never collide with the legacy build's.
class RecordStore {
 public:
  static RecordStore* openRecordStore(const char* name, bool createIfNecessary);
  static void deleteRecordStore(const char* name);
  ~RecordStore();

  int getNumRecords();
  void addRecord(const jbyte* data, int offset, int length);
  void setRecord(int id, const jbyte* data, int offset, int length);
  int getRecord(int id, jbyte* buffer, int offset);  // returns size
  int getRecordSize(int id);
  void closeRecordStore();

 private:
  RecordStore() {}
  void load();
  void save();
  String path_;
  std::vector<std::vector<jbyte>> records_;  // record id = index + 1
  bool dirty_ = false;
};

// ---- resources ---------------------------------------------------------
//
// getResourceAsStream("/level0.bin") -> reads <dataDir>/level0.bin. The
// data dir is a1's unpacked jar contents (tools/extract_jar.py's output on
// roms/RAC-GoingMobile-a1.jar, i.e. goingmobile/extracted_a1/, gitignored).
bool   setDataDir(const String& dir);
String getDataDir();
bool   resourceExists(const String& path);
String resourcePath(const String& path);  // absolute path for "/name"
ByteArray loadResource(const String& path);  // empty if missing

// ---- media (MMAPI subset) -------------------------------------------------
//
// Mirrors src_a1/SoundPlayer.java: 6 cues (DEATH/BUBBLE/SHOOT/MSOUND/BOX/
// MENU, not the legacy build's 7), and only MENU is MIDI here -- the rest
// are .wav (soundFormats[i] != 0 in the Java, per BUILD_COMPARISON.md's
// ".wav sound variants" note). queue(id, loop) records a request (first
// wins until finished/stopped); update() advances the pending dance on the
// main loop instead of a background thread, same simplification as the
// legacy build's shim. Playback goes through MCI (waveaudio for .wav,
// sequencer for .mid).
class SoundPlayer {
 public:
  static const int DEATH = 0, BUBBLE = 1, SHOOT = 2, MSOUND = 3, BOX = 4, MENU = 5;

  void loadAll();                 // caches the 6 cue file paths + formats
  void haltPlayer();               // hard-stop; public, called directly by ratchetandclank
  void queue(int id);              // queue(id, 1)
  void queue(int id, int loop);
  bool playMenuLoop(bool loop);    // forces MENU to loop; always returns true
  void stop();
  void update();                  // call once per frame

 private:
  String soundFiles_[6];
  bool   isWav_[6] = {};
  std::vector<char> wavData_[6];  // preloaded so one-shot cues don't hit the disk mid-frame
  int  pendingSound_ = -1;
  int  pendingLoop_ = -1;
  bool playing_ = false;          // MCI player exists
  bool endOfMedia_ = false;      // MCI signalled completion
  int  playingLoop_ = 1;
  int  playingId_ = -1;           // cue currently open in MCI
};

// ---- platform ----------------------------------------------------------
// 176x220 canvas backbuffer + presentation, input event delivery, and a
// screenshot dumper (F12) used for headless verification. Same interface
// as the legacy build's platform namespace (../src/midp.h) -- only the
// canvas size and key mapping differ, both in midp.cpp.
namespace platform {
extern Image canvas;
bool initWindow();    // returns false on failure
void setKeyCallback(void (*onKeyDown)(int), void (*onKeyUp)(int));
void pumpEvents();
void present();       // stretch-blit canvas to the window
void requestQuit();
bool quitRequested();
void screenshot(const char* tag);  // writes screenshot_<tag>.bmp next to cwd
}  // namespace platform
