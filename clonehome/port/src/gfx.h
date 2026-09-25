// 240x320 software framebuffer. Sprite drawing follows Sprite.draw
// (clonehome/src/Sprite.java): trans bit 2 mirrors horizontally, bit 1
// vertically, and the sprite's offset flips with it.
#pragma once
#include <cstdint>
#include <vector>

#include "assets.h"

namespace ch {

constexpr int kScreenW = 240, kScreenH = 320;

class Surface {
 public:
  int w = kScreenW, h = kScreenH;
  std::vector<uint32_t> px = std::vector<uint32_t>(kScreenW * kScreenH, 0xFF000000);

  // Clip rectangle, as Graphics.setClip.
  void setClip(int x, int y, int cw, int ch);
  void resetClip() { setClip(0, 0, w, h); }
  void fillRect(int x, int y, int rw, int rh, uint32_t argb);
  // Copies a region of src (alpha-blended) with a MIDP-style mirror in trans (0..3).
  void drawRegion(const Image& src, int sx, int sy, int rw, int rh, int trans, int dx, int dy);
  void drawSprite(const Image& sheet, const SpriteRect& s, int x, int y, int trans);

 private:
  int cx_ = 0, cy_ = 0, cx2_ = kScreenW, cy2_ = kScreenH;
};

bool writeBmp(const Surface& s, const char* path);

}  // namespace ch
