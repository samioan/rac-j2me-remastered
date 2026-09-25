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
  int getState() const { return state_; }
  Arr<int8_t> data;
  String mime;

 private:
  int loops_ = 1, state_ = 300;
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
