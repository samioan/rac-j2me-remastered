// font.h -- port of goingmobile/src_a1/Font.java (class a). a1's standalone
// bitmap font, factored out of the MIDlet (unlike the legacy build, where
// it lives directly on ratchetandclank) and instantiated twice --
// "/f2.v" at line height 10 (byte-identical to the legacy build's font
// asset) and "/f3.v" at line height 13 -- stored on ratchetandclank as
// smallFont/largeFont (see midlet.h).
//
// glyphTable[192][]: index 0 is the glyph's advance width, the rest are
// packed fillRect commands (x/y/w/h, 4 bits each, in a short).
// charToGlyphIndex maps 32-127 -> 0-95 and 160-255 -> 96-191 (0 otherwise).
#pragma once
#include "midp.h"

class Font {
 public:
  Font(const String& path, int lineHeight, int spacing);

  void drawText(Graphics* g, const String& s, int x, int y, int anchor);
  void drawTextRange(Graphics* g, const char* chars, int charsLen, int off,
                      int len, int x, int y, int anchor);
  int textWidth(const String& s);
  int textWidth(const String& s, int off, int len);
  int charWidth(char c);

  int lineHeight = 0;

 private:
  void drawTextRange(Graphics* g, const String& s, int off, int len, int x,
                      int y, int anchor);
  template <typename CharAt>
  void drawTextRangeImpl(Graphics* g, CharAt charAt, int off, int len, int x,
                         int y, int anchor);
  int charToGlyphIndex(int c) const;

  std::vector<std::vector<jshort>> glyphTable_;  // [192][]
  int spacing_ = 0;
};
