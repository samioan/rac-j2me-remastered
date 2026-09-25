// Port of Engine.java (the GameCanvas base class): frame timing, key state,
// record storage, bitmap-font text, the tile layer and the animation player.
// Names and signatures match clonehome/src/Engine.java so the translated Game
// class (gen/) compiles against it unchanged; arithmetic follows the Java.
#pragma once
#include <cstdint>
#include <map>
#include <string>
#include <vector>

#include "assets.h"
#include "gfx.h"
#include "jrt.h"
#include "platform.h"

namespace ch {

using Graphics = Surface;

// Sprite.java: one rectangle of a sheet plus its draw offset.
class Sprite {
 public:
  explicit Sprite(Image* sheet) : sheet(sheet) {}
  void load(const Arr<int16_t>& v) {
    offsetX = v[0]; offsetY = v[1]; srcX = v[2]; srcY = v[3]; width = v[4]; height = v[5];
    charCode = v[6]; f = v[7]; advance = v[8]; metric = v[9];
  }
  void draw(Graphics* g, int x, int y, int trans);
  int16_t offsetX = 0, offsetY = 0, width = 0, height = 0, charCode = 0, f = 0, advance = 0, metric = 0;
  int16_t srcX = 0, srcY = 0;
  Image* sheet;
};

class SoundPlayer;

class Engine {
 public:
  explicit Engine(MIDlet* midlet);
  virtual ~Engine() = default;

  virtual void render(Graphics* g) = 0;
  virtual void update() = 0;
  virtual void onLifecycle(int state);  // 0 start, 1 paused, 2 resumed, 3 destroy, 5 resumed-after

  bool ok() const { return ok_; }
  bool quit = false;
  int targetFps = 25;     // real-time logic steps per second (the game does one step per frame)
  bool realTime = false;  // sleep to hold the minimum frame time (set by the window loop)
  MIDlet* midlet;
  SoundPlayer* soundPlayer = nullptr;
  bool soundEnabled = false;
  Arr<int8_t> resourceTypes;

  // One iteration of Engine.run's loop body. `nowMs` is a monotonic clock;
  // returns milliseconds the caller should sleep before the next call.
  int runFrame(long nowMs, Surface& screen);
  void hideNotify() { pause(); }
  void showNotify() { resume(); }
  void postLifecycle(int s) { if (s == 3) onLifecycle(3); }
  void pause();
  void resume();

  // ---- frame timing
  int frameTime = 10, minFrameTime = 10, maxFrameTime = 100;
  void resetFrameTimingNow();

  // ---- input
  int keysPressed = 0, keysReleased = 0;
  void platformKey(int midpKeyCode, bool down);
  int getWidth() const { return kScreenW; }
  int getHeight() const { return kScreenH; }
  void pollKeys();
  void clearKeys();
  bool isKeyHeld(int8_t idx) const { return (keysHeld_ >> idx & 1) != 0; }
  bool isKeyPressed(int8_t idx) const { return (keysPressed >> idx & 1) != 0; }
  bool anyKeyPressed() const { return keysPressed != 0; }

  // ---- records (RMS replacement: one file per record id)
  static Arr<int8_t> readRecord(int id);
  static bool writeRecord(int id, const Arr<int8_t>& data);
  void syncSettings(bool write);

  // ---- resources
  Arr<Sprite*> loadSprites(int id) { return loadSprites(id, Arr<int8_t>()); }
  Arr<Sprite*> loadSprites(int id, const Arr<int8_t>& swap);
  Image* loadImage(int id) { return loadImage(id, Arr<int8_t>()); }
  Image* loadImage(int id, const Arr<int8_t>& swap);
  void loadAnimSet(int id);
  Arr<int8_t> getResourceBytes(int id);
  DataInputStream* openResource(int id);
  String getString(int i);
  void loadStringTable(int id);
  Assets assets;

  // ---- viewport (Engine.setViewport)
  int canvasW = kScreenW, canvasH = kScreenH, offX = 0, offY = 0;
  bool bordersCleared = false;
  void setViewport(int w, int h);
  void resetClip(Graphics* g) { g->setClip(0, 0, canvasW, canvasH); }
  void requestClear() { clearBorders_ = true; }

  // ---- bitmap font
  void setFont(const Arr<Sprite*>& font);
  int stringWidth(const String& s) { return stringWidth(s, 0, s.length()); }
  int stringWidth(const String& s, int off, int len);
  int charWidth(char16_t c);
  int charsWidth(const Arr<char16_t>& s, int off, int len);
  int fontBaseline();
  int fontHeight();
  void drawString(Graphics* g, const String& s, int x, int y, int anchor) { drawString(g, s, 0, s.length(), x, y, anchor); }
  void drawString(Graphics* g, const String& s, int off, int len, int x, int y, int anchor);
  void drawString(Graphics* g, const Arr<char16_t>& s, int off, int len, int x, int y, int anchor);

  // ---- tile layer
  void initTileMap(int cols, int rows, Image* sheet, int tw, int th);
  void setTileSheet(Image* sheet, int tw, int th);
  void fillTiles(int x, int y, int w, int h, int tile);
  void setViewSize(int w, int h);
  void setTile(int x, int y, int tile) { tileGrid_[y * gridCols_ + x] = (int16_t)tile; }
  int addAnimatedTile(int tile);
  void setAnimatedTile(int handle, int tile) { animatedTiles_[~handle] = tile; }
  void setScroll(int x, int y) { scrollX_ = -x; scrollY_ = -y; }
  void drawTileMap(Graphics* g);

  // ---- animation player (instances are 8 shorts each, see startAnim)
  void startAnim(int inst, int setId, int seq);
  bool stepAnim(int inst, int dt);
  void getAnimBounds(const Arr<Sprite*>& sp, int inst, int x, int y, int trans, const Arr<int>& out);
  void drawAnim(Graphics* g, const Arr<Sprite*>& sp, int inst, int x, int y, int trans);

 private:
  bool ok_ = false;
  long lastFrameStart_ = 0;
  bool resetFrameTiming_ = true, clearBorders_ = true, paused_ = false, started_ = false;
  bool settingsLoaded_ = false, optM_ = false, optN_ = false;
  int keysHeld_ = 0, pendingPressed_ = 0, pendingHeld_ = 0, pendingReleased_ = 0;
  std::map<std::string, Image*> imageCache_;

  Arr<Sprite*> font_;
  Sprite* findGlyph(char16_t c);

  Image* tileSheet_ = nullptr;
  std::vector<int> tileSrcX_, tileSrcY_, animatedTiles_;
  std::vector<int16_t> tileGrid_;
  int tileW_ = 0, tileH_ = 0, gridCols_ = 0, gridRows_ = 0;
  int viewW_ = 0, viewH_ = 0, scrollX_ = 0, scrollY_ = 0;
  void drawTiles(Graphics* g, int mapX, int mapY, int dx, int dy, int w, int h);

  // Packed animation data (Engine fields M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z).
  std::vector<int16_t> inst_;                          // M
  std::vector<int16_t> setId_, setSeq_, setList_;      // N, O, P
  std::vector<int16_t> seqOff_, listOff_, durations_;  // R, T, Z
  std::vector<int8_t> steps_, parts_;                  // V, X
};

}  // namespace ch
