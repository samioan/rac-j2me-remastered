// midp.h -- MIDP-2.0 / CLDC / Nokia-UI shim for the Going Mobile PC port.
//
// The port transcribes the decompiled Java as closely as C++ allows; this
// header provides the javax.microedition / com.nokia.mid.ui API surface the
// game actually uses (the complete call list was extracted from src/ in
// phase 3):
//   Graphics: setColor/fillRect/drawRect/drawLine/fillRoundRect/drawImage/
//             setClip/clipRect/translate
//   Nokia DirectGraphics: drawImage(img,x,y,anchor,manipulation),
//             fillTriangle(x1,y1,x2,y2,x3,y3,argb)
//   Image: PNG resource loading (decoded via GDI+ on Windows)
//   RMS: RecordStore (file-backed)
//   media: MIDI playback (MCI)
// plus the small java.lang helpers the decompile relies on (Java-style
// string ops, Math, System.currentTimeMillis).
//
// Strings hold Latin-1 bytes: the original loads text resources with raw
// byte reads cast to char ((char)in.read()), so one byte == one char, and
// the font indexes chars < 128 as c-32 and >= 160 as c-64.
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

// Nokia FullCanvas key codes (the values keyPressed() receives).
enum KeyCode {
  KEY_UP = -1, KEY_DOWN = -2, KEY_LEFT = -3, KEY_RIGHT = -4, KEY_FIRE = -5,
  KEY_SOFT_LEFT = -6, KEY_SOFT_RIGHT = -7, KEY_SEND = -10
};

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
  void drawLine(int x1, int y1, int x2, int y2);
  void fillRoundRect(int x, int y, int w, int h, int arcWidth, int arcHeight);
  void drawImage(Image* img, int x, int y, int anchor);
  void setClip(int x, int y, int w, int h);
  void clipRect(int x, int y, int w, int h);
  void translate(int dx, int dy);
  int getTranslateX() const { return transX_; }
  int getTranslateY() const { return transY_; }

  // Nokia DirectGraphics entry points.
  void drawImageManip(Image* img, int x, int y, int anchor, int manipulation);
  void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int argb);

 private:
  Image* target_ = nullptr;
  int color_ = 0;
  int transX_ = 0, transY_ = 0;
  int clipX_ = 0, clipY_ = 0, clipW_ = -1, clipH_ = -1;  // untranslated, image space
};


// ---- rms (file-backed RecordStore) -------------------------------------
//
// The original stores to the MIDlet record store "RANDCSm" (3 x 220-byte
// save slots + record 4 = 3-byte settings). The port keeps the exact record
// layout in a single file under %LOCALAPPDATA%/rac-gm-port/.
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
// getResourceAsStream("/n1") -> reads <dataDir>/n1. The data dir is the
// canonical build's extracted/ jar contents (gitignored, regenerated with
// tools/extract_jar.py); located by walking up from the current directory.
bool   setDataDir(const String& dir);
String getDataDir();
bool   resourceExists(const String& path);
String resourcePath(const String& path);  // absolute path for "/name"
ByteArray loadResource(const String& path);  // empty if missing

// ---- media (MMAPI subset: the 7 .mid cues, one at a time) ---------------
//
// Mirrors src/SoundPlayer.java: queue(id, loop) records a request (first
// wins until finished/stopped); update() advances the pending dance on the
// main loop instead of a background thread. MIDI plays through MCI.
class SoundPlayer {
 public:
  static const int PORT = 0, KILLEN = 1, BUBBLE = 2, DEATH = 3,
                   BOX = 4, SHOOT = 5, MENU = 6;

  void loadAll();                 // caches the 7 cue file paths
  void queue(int id, int loop);
  void stop();
  void update();                  // call once per frame
  bool threadAlive = true;        // the original nulls `thread` to end run()

 private:
  void stopPlayer();
  String soundFiles_[7];
  int  pendingSound_ = -1;
  int  pendingLoop_ = -1;
  bool playing_ = false;          // MCI player exists
  bool endOfMedia_ = false;      // MCI signalled completion
  int  playingLoop_ = 1;
};

// ---- platform ----------------------------------------------------------
// 128x128 canvas backbuffer + presentation, input event delivery, and a
// screenshot dumper (F12) used for headless verification.
namespace platform {
extern Image canvas;  // the FullCanvas framebuffer
bool initWindow();    // returns false on failure
// pumpEvents() drains the Win32 message queue and forwards key events (Nokia
// FullCanvas key codes, see KeyCode above) to the callbacks registered with
// setKeyCallback -- main.cpp wires those to Game::keyPressed/keyReleased so
// this header doesn't need to know about Game.
void setKeyCallback(void (*onKeyDown)(int), void (*onKeyUp)(int));
void pumpEvents();
void present();       // stretch-blit canvas to the window
void requestQuit();
bool quitRequested();
void screenshot(const char* tag);  // writes screenshot_<tag>.bmp next to cwd
}  // namespace platform
