// Stand-ins for the MIDP / Nokia classes the translated game code touches.
#pragma once
#include <string>

#include "jrt.h"
#include "gfx.h"

namespace ch {

struct Platform {
  static inline std::string dataDir = ".";
  static inline std::string saveDir = "saves";
};

class Engine;

class MIDlet {
 public:
  virtual ~MIDlet() = default;
  String getAppProperty(const String&) { return String(nullptr); }
  void notifyDestroyed();
  virtual void startApp() {}
  virtual void pauseApp() {}
  virtual void destroyApp(bool) {}
};

class Display {
 public:
  static Display* getDisplay(MIDlet*) {
    static Display d;
    return &d;
  }
  void setCurrent(Engine*) {}
};

// The port's own options shown in the game's Settings screens (Resolution, Fullscreen, Scaling, Speed).
// Rows 0..3; implemented in main.cpp next to the window code.
// Appends a line to %LOCALAPPDATA%\rac-ch-port\audio.log (audio failures and --sound-test results).
void audioLog(const std::string& line);

struct Port {
  static String label(int row);
  static void change(int row, int dir);
};

// Nokia UI API: triangles with an ARGB colour.
class DirectGraphics {
 public:
  Surface* g = nullptr;
  void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int argb) {
    g->fillTriangle(x1, y1, x2, y2, x3, y3, (uint32_t)argb);
  }
  void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int argb) {
    uint32_t c = (uint32_t)argb;
    int saved = 0;
    (void)saved;
    g->setColor((int)(c & 0xFFFFFF));
    g->drawLine(x1, y1, x2, y2);
    g->drawLine(x2, y2, x3, y3);
    g->drawLine(x3, y3, x1, y1);
  }
};

struct DirectUtils {
  static DirectGraphics* getDirectGraphics(Surface* g) {
    static DirectGraphics d;
    d.g = g;
    return &d;
  }
};

// javax.microedition.media stand-ins (audio backend arrives with the sound milestone).
struct ByteArrayInputStream {
  Arr<int8_t> data;
  explicit ByteArrayInputStream(const Arr<int8_t>& d) : data(d) {}
};

class Player {
 public:
  void realize() {}
  void prefetch() {}
  void setMediaTime(int64_t) {}
  void setLoopCount(int n) { loops_ = n; }
  void start();
  void stop();
  void close();
  int getState();
  static void pumpLoops();  // restarts a looping MIDI that has run out; call once per frame  // 300 prefetched, 400 started (a finished clip drops back to 300)
  Arr<int8_t> data;
  String mime;

 private:
  bool isMidi() const { return mime.equals(String(u"audio/midi")); }
  int loops_ = 1, state_ = 300;
  int64_t endTime_ = 0;  // wav: when playback finishes; 0 = never (looping)
  int mciId_ = 0;
  bool mciOpen_ = false;
};

struct Manager {
  static Player* createPlayer(ByteArrayInputStream* in, const String& mime) {
    Player* p = new Player();
    p->data = in->data;
    p->mime = mime;
    return p;
  }
};

}  // namespace ch
