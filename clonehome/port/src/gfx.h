// 240x320 software framebuffer with the slice of javax.microedition.lcdui.Graphics
// the game uses. Sprite drawing follows Sprite.draw (clonehome/src/Sprite.java):
// trans bit 2 mirrors horizontally, bit 1 vertically, and the offset flips with it.
#pragma once
#include <cstdint>
#include <vector>

#include "jrt.h"

namespace ch {

constexpr int kScreenW = 240, kScreenH = 320;

struct Image {  // 0xAARRGGBB
  int w = 0, h = 0;
  std::vector<uint32_t> px;
  int getWidth() const { return w; }
  int getHeight() const { return h; }
};

struct Font {
  int getHeight() const { return 12; }
  int getBaselinePosition() const { return 10; }
};

class Surface {
 public:
  int w = kScreenW, h = kScreenH;
  std::vector<uint32_t> px = std::vector<uint32_t>(kScreenW * kScreenH, 0xFF000000);

  // Resizes the buffer (logical canvas width changes with the resolution mode); contents become black.
  void resize(int nw, int nh) {
    w = nw;
    h = nh;
    px.assign((size_t)nw * (size_t)nh, 0xFF000000u);
    tx_ = ty_ = 0;
    resetClip();
  }

  // ---- Graphics
  void setColor(int rgb) { color_ = 0xFF000000u | (uint32_t)rgb; }
  void setColor(int r, int g, int b) { color_ = 0xFF000000u | (uint32_t)((r & 255) << 16 | (g & 255) << 8 | (b & 255)); }
  void setClip(int x, int y, int cw, int ch);
  void resetClip() { setClip(0, 0, w, h); }
  void translate(int dx, int dy) { tx_ += dx; ty_ += dy; }
  void resetTransform() { tx_ = ty_ = 0; }
  int getClipX() const { return cx_ - tx_; }
  int getClipY() const { return cy_ - ty_; }
  int getClipWidth() const { return cx2_ - cx_; }
  int getClipHeight() const { return cy2_ - cy_; }
  void fillRect(int x, int y, int rw, int rh) { fillRectArgb(x, y, rw, rh, color_); }
  void drawRect(int x, int y, int rw, int rh);
  void drawLine(int x1, int y1, int x2, int y2);
  void fillArc(int x, int y, int rw, int rh, int startAngle, int arcAngle);
  void fillRoundRect(int x, int y, int rw, int rh, int, int) { fillRect(x, y, rw, rh); }
  void drawImage(const Image* img, int x, int y, int anchor);
  void drawRegion(const Image* img, int sx, int sy, int rw, int rh, int trans, int dx, int dy, int anchor);
  Font* getFont();
  void setFont(Font*) {}
  void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, uint32_t argb);

  // ---- helpers
  void fillRectArgb(int x, int y, int rw, int rh, uint32_t argb);
  void plot(int x, int y, uint32_t argb);
  // Alpha-blended copy with a MIDP-style mirror in trans (0..3).
  void blit(const Image& src, int sx, int sy, int rw, int rh, int trans, int dx, int dy);

 private:
  uint32_t color_ = 0xFF000000;
  int cx_ = 0, cy_ = 0, cx2_ = kScreenW, cy2_ = kScreenH, tx_ = 0, ty_ = 0;
};

bool writeBmp(const Surface& s, const char* path);

}  // namespace ch
