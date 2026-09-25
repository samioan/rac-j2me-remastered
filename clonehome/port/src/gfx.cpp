#define _CRT_SECURE_NO_WARNINGS
#include "gfx.h"

#include <algorithm>
#include <cstdio>

namespace ch {

void Surface::setClip(int x, int y, int cw, int chh) {
  cx_ = std::max(0, x);
  cy_ = std::max(0, y);
  cx2_ = std::min(w, x + cw);
  cy2_ = std::min(h, y + chh);
}

void Surface::fillRect(int x, int y, int rw, int rh, uint32_t argb) {
  int x0 = std::max(x, cx_), y0 = std::max(y, cy_);
  int x1 = std::min(x + rw, cx2_), y1 = std::min(y + rh, cy2_);
  for (int yy = y0; yy < y1; yy++)
    for (int xx = x0; xx < x1; xx++) px[yy * w + xx] = argb;
}

void Surface::drawRegion(const Image& src, int sx, int sy, int rw, int rh, int trans, int dx, int dy) {
  bool mx = (trans & 2) != 0, my = (trans & 1) != 0;
  for (int j = 0; j < rh; j++) {
    int y = dy + j;
    if (y < cy_ || y >= cy2_) continue;
    int syy = sy + (my ? rh - 1 - j : j);
    if (syy < 0 || syy >= src.h) continue;
    for (int i = 0; i < rw; i++) {
      int x = dx + i;
      if (x < cx_ || x >= cx2_) continue;
      int sxx = sx + (mx ? rw - 1 - i : i);
      if (sxx < 0 || sxx >= src.w) continue;
      uint32_t p = src.px[syy * src.w + sxx];
      uint32_t a = p >> 24;
      if (a == 0) continue;
      uint32_t& d = px[y * w + x];
      if (a == 255) {
        d = p;
      } else {
        uint32_t r = ((p >> 16 & 255) * a + (d >> 16 & 255) * (255 - a)) / 255;
        uint32_t g = ((p >> 8 & 255) * a + (d >> 8 & 255) * (255 - a)) / 255;
        uint32_t b = ((p & 255) * a + (d & 255) * (255 - a)) / 255;
        d = 0xFF000000u | r << 16 | g << 8 | b;
      }
    }
  }
}

void Surface::drawSprite(const Image& sheet, const SpriteRect& s, int x, int y, int trans) {
  trans &= 3;
  x += (trans & 2) == 0 ? s.offX : -(s.offX + s.w);
  y += (trans & 1) == 0 ? s.offY : -(s.offY + s.h);
  int rw = s.w, rh = s.h, sx = s.srcX, sy = s.srcY;
  if (sx < 0) { rw += sx; x += sx; sx = 0; }
  if (sy < 0) { rh += sy; y += sy; sy = 0; }
  if (sx + rw > sheet.w) rw = sheet.w - sx;
  if (sy + rh > sheet.h) rh = sheet.h - sy;
  if (rw > 0 && rh > 0) drawRegion(sheet, sx, sy, rw, rh, trans, x, y);
}

bool writeBmp(const Surface& s, const char* path) {
  FILE* f = std::fopen(path, "wb");
  if (!f) return false;
  uint32_t size = 54 + s.w * s.h * 4;
  uint8_t hdr[54] = {'B', 'M'};
  auto put = [&](int off, uint32_t v) { for (int i = 0; i < 4; i++) hdr[off + i] = (uint8_t)(v >> (8 * i)); };
  put(2, size); put(10, 54); put(14, 40); put(18, (uint32_t)s.w); put(22, (uint32_t)-s.h);
  hdr[26] = 1; hdr[28] = 32;
  std::fwrite(hdr, 1, 54, f);
  std::fwrite(s.px.data(), 4, s.px.size(), f);
  std::fclose(f);
  return true;
}

}  // namespace ch
