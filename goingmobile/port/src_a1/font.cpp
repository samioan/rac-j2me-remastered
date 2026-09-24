// font.cpp -- implementation of Font (see font.h), transcribed from
// src_a1/Font.java.
#include "font.h"
#include <cstddef>

Font::Font(const String& path, int lineHeightIn, int spacing) : spacing_(spacing) {
  ByteArray data = loadResource(path);
  size_t pos = 0;
  auto readByte = [&]() -> int {
    return pos < data.size() ? (int)std::to_integer<uint8_t>(data[pos++]) : 0;
  };
  glyphTable_.resize(192);
  for (int i = 0; i < 192; i++) {
    int b = readByte();
    int count = b & 15;
    glyphTable_[(size_t)i].resize((size_t)count + 1);
    glyphTable_[(size_t)i][0] = (jshort)((b & 240) >> 4);
    for (int j = 0; j < count; j++) {
      int hi = readByte();
      int lo = readByte();
      glyphTable_[(size_t)i][(size_t)j + 1] = (jshort)((hi << 8) | lo);
    }
  }
  lineHeight = lineHeightIn;
}

int Font::charToGlyphIndex(int c) const {
  if (c < 32) return 0;
  if (c < 128) return c - 32;
  if (c < 160) return 0;
  return c < 256 ? c - 64 : 0;
}

template <typename CharAt>
void Font::drawTextRangeImpl(Graphics* g, CharAt charAt, int off, int len,
                             int x, int y, int anchor) {
  switch (anchor & 114) {
    case 2: y -= lineHeight / 2; break;
    case 32: y -= lineHeight; break;
    default: break;
  }
  switch (anchor & 13) {
    case 1: {
      int w = 0;
      for (int i = off; i < off + len; i++)
        w += glyphTable_[(size_t)charToGlyphIndex((unsigned char)charAt(i))][0] + spacing_;
      x -= (w - spacing_) / 2;
      [[fallthrough]];
    }
    case 0:
    case 4:
      for (int i = off; i < off + len; i++) {
        int idx = charToGlyphIndex((unsigned char)charAt(i));
        auto& glyph = glyphTable_[(size_t)idx];
        for (int k = (int)glyph.size() - 1; k >= 1; k--) {
          jshort v = glyph[(size_t)k];
          g->fillRect(x + ((v >> 12) & 15), y + ((v >> 8) & 15), (v >> 4) & 15, v & 15);
        }
        x += glyph[0] + spacing_;
      }
      break;
    case 8: {
      x += spacing_;
      for (int i = off + len - 1; i >= off; i--) {
        int idx = charToGlyphIndex((unsigned char)charAt(i));
        auto& glyph = glyphTable_[(size_t)idx];
        x -= glyph[0] + spacing_;
        for (int k = (int)glyph.size() - 1; k >= 1; k--) {
          jshort v = glyph[(size_t)k];
          g->fillRect(x + ((v >> 12) & 15), y + ((v >> 8) & 15), (v >> 4) & 15, v & 15);
        }
      }
      break;
    }
    default:
      break;  // the original logs a debug string here; not worth replicating
  }
}

void Font::drawTextRange(Graphics* g, const String& s, int off, int len,
                         int x, int y, int anchor) {
  if (len < 0) len = (int)s.size() - off;
  drawTextRangeImpl(g, [&](int i) { return charAt(s, i); }, off, len, x, y, anchor);
}

void Font::drawTextRange(Graphics* g, const char* chars, int charsLen, int off,
                         int len, int x, int y, int anchor) {
  if (len < 0) len = charsLen - off;
  drawTextRangeImpl(g, [&](int i) { return chars[i]; }, off, len, x, y, anchor);
}

void Font::drawText(Graphics* g, const String& s, int x, int y, int anchor) {
  drawTextRange(g, s, 0, -1, x, y, anchor);
}

int Font::textWidth(const String& s) { return textWidth(s, 0, -1); }

int Font::textWidth(const String& s, int off, int len) {
  if (len < 0) len = (int)s.size() - off;
  int w = 0;
  for (int i = off; i < off + len; i++)
    w += glyphTable_[(size_t)charToGlyphIndex((unsigned char)charAt(s, i))][0] + spacing_;
  return w - spacing_;
}

int Font::charWidth(char c) {
  return glyphTable_[(size_t)charToGlyphIndex((unsigned char)c)][0] + spacing_;
}
