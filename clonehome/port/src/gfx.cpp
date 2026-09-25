#define _CRT_SECURE_NO_WARNINGS
#include "gfx.h"

#include <algorithm>
#include <cstdio>
#include <cstdlib>

namespace ch {

void Surface::setClip(int x, int y, int cw, int chh) {
  x += tx_;
  y += ty_;
  cx_ = std::max(0, x);
  cy_ = std::max(0, y);
  cx2_ = std::min(w, x + cw);
  cy2_ = std::min(h, y + chh);
}

void Surface::plot(int x, int y, uint32_t argb) {
  x += tx_;
  y += ty_;
  if (x < cx_ || x >= cx2_ || y < cy_ || y >= cy2_) return;
  uint32_t a = argb >> 24;
  uint32_t& d = px[(size_t)y * w + x];
  if (a == 255) {
    d = argb;
  } else if (a) {
    uint32_t r = ((argb >> 16 & 255) * a + (d >> 16 & 255) * (255 - a)) / 255;
    uint32_t g = ((argb >> 8 & 255) * a + (d >> 8 & 255) * (255 - a)) / 255;
    uint32_t b = ((argb & 255) * a + (d & 255) * (255 - a)) / 255;
    d = 0xFF000000u | r << 16 | g << 8 | b;
  }
}

void Surface::fillRectArgb(int x, int y, int rw, int rh, uint32_t argb) {
  x += tx_;
  y += ty_;
  int x0 = std::max(x, cx_), y0 = std::max(y, cy_);
  int x1 = std::min(x + rw, cx2_), y1 = std::min(y + rh, cy2_);
  for (int yy = y0; yy < y1; yy++)
    for (int xx = x0; xx < x1; xx++) px[(size_t)yy * w + xx] = argb;
}

void Surface::drawRect(int x, int y, int rw, int rh) {
  if (rw < 0 || rh < 0) return;
  fillRect(x, y, rw + 1, 1);
  fillRect(x, y + rh, rw + 1, 1);
  fillRect(x, y, 1, rh + 1);
  fillRect(x + rw, y, 1, rh + 1);
}

void Surface::drawLine(int x1, int y1, int x2, int y2) {
  int dx = std::abs(x2 - x1), dy = -std::abs(y2 - y1);
  int sx = x1 < x2 ? 1 : -1, sy = y1 < y2 ? 1 : -1, err = dx + dy;
  for (;;) {
    plot(x1, y1, color_);
    if (x1 == x2 && y1 == y2) break;
    int e2 = 2 * err;
    if (e2 >= dy) { err += dy; x1 += sx; }
    if (e2 <= dx) { err += dx; y1 += sy; }
  }
}

void Surface::fillArc(int x, int y, int rw, int rh, int, int) {  // full ellipses only
  if (rw <= 0 || rh <= 0) return;
  double a = rw / 2.0, b = rh / 2.0, cx = x + a, cy = y + b;
  for (int yy = y; yy < y + rh; yy++)
    for (int xx = x; xx < x + rw; xx++) {
      double u = (xx + 0.5 - cx) / a, v = (yy + 0.5 - cy) / b;
      if (u * u + v * v <= 1.0) plot(xx, yy, color_);
    }
}

void Surface::fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, uint32_t argb) {
  int minX = std::min({x1, x2, x3}), maxX = std::max({x1, x2, x3});
  int minY = std::min({y1, y2, y3}), maxY = std::max({y1, y2, y3});
  auto edge = [](int ax, int ay, int bx, int by, int px, int py) { return (bx - ax) * (py - ay) - (by - ay) * (px - ax); };
  int area = edge(x1, y1, x2, y2, x3, y3);
  if (!area) return;
  for (int yy = minY; yy <= maxY; yy++)
    for (int xx = minX; xx <= maxX; xx++) {
      int w0 = edge(x2, y2, x3, y3, xx, yy), w1 = edge(x3, y3, x1, y1, xx, yy), w2 = edge(x1, y1, x2, y2, xx, yy);
      if (area > 0 ? (w0 >= 0 && w1 >= 0 && w2 >= 0) : (w0 <= 0 && w1 <= 0 && w2 <= 0)) plot(xx, yy, argb);
    }
}

Font* Surface::getFont() {
  static Font f;
  return &f;
}

void Surface::blit(const Image& src, int sx, int sy, int rw, int rh, int trans, int dx, int dy) {
  bool mx = (trans & 2) != 0, my = (trans & 1) != 0;
  dx += tx_;
  dy += ty_;
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
      uint32_t p = src.px[(size_t)syy * src.w + sxx];
      uint32_t a = p >> 24;
      if (a == 0) continue;
      uint32_t& d = px[(size_t)y * w + x];
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

// MIDP anchors: HCENTER 1, VCENTER 2, LEFT 4, RIGHT 8, TOP 16, BOTTOM 32.
static void anchorPos(int anchor, int rw, int rh, int& x, int& y) {
  if (anchor & 1) x -= rw >> 1;
  else if (anchor & 8) x -= rw;
  if (anchor & 2) y -= rh >> 1;
  else if (anchor & 32) y -= rh;
}

void Surface::drawImage(const Image* img, int x, int y, int anchor) {
  if (!img) return;
  anchorPos(anchor, img->w, img->h, x, y);
  blit(*img, 0, 0, img->w, img->h, 0, x, y);
}

void Surface::drawRegion(const Image* img, int sx, int sy, int rw, int rh, int trans, int dx, int dy, int anchor) {
  if (!img) return;
  anchorPos(anchor, rw, rh, dx, dy);
  blit(*img, sx, sy, rw, rh, trans, dx, dy);
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
