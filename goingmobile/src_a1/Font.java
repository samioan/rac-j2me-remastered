// Hand-written, faithfully renamed from decompiled_a1/a.java (class a).
// a1's bitmap font -- unlike the legacy build (where the font is embedded
// directly in ratchetandclank, see ../src/ratchetandclank.java's font
// methods in CLASS_MAP.md), a1 factors it out into its own class and
// instantiates it twice (confirmed at decompiled_a1/h.java:714,716):
// `new Font("/f2.v", 10, 1)` and `new Font("/f3.v", 13, 1)`, stored as
// `ratchetandclank.h`/`ratchetandclank.f` -- same `fillRect`-glyph format
// as the legacy build's `f2.v` (byte-identical file, per
// BUILD_COMPARISON.md), plus a second, taller (13px vs 10px line height)
// font from the new `f3.v` asset.
//
// glyphTable[192][]: index 0 is the glyph's advance width, the rest are
// packed fillRect commands (x/y/w/h, 4 bits each, in a short). charToGlyphIndex
// maps char codes 32-127 to table indices 0-95 and 160-255 to 96-191 (0
// otherwise, i.e. no glyph) -- same two-range mapping bug/behavior noted
// for the legacy build's font in PORT_ROADMAP.md's bug #3 writeup.
//
// draw*'s `var7` (anchor) argument is a standard MIDP
// `javax.microedition.lcdui.Graphics` anchor value (TOP/BOTTOM/VCENTER/
// LEFT/HCENTER/RIGHT/BASELINE): `& 114` (BASELINE|BOTTOM|TOP|VCENTER)
// selects the vertical case (only VCENTER/BOTTOM are handled; TOP/
// BASELINE/0 fall through unadjusted), `& 13` (RIGHT|LEFT|HCENTER)
// selects the horizontal case (HCENTER falls through into the normal
// left-to-right draw after computing a centering offset; RIGHT draws
// right-to-left; anything else logs the mystery string
// "@!#%!%$#@%!%!%!%!$#% " + anchor and draws nothing -- kept verbatim,
// looks like a debug leftover).
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;

public final class Font {
   private short[][] glyphTable = new short[192][];
   public int lineHeight;
   private int spacing;

   public Font(String var1, int var2, int var3) throws IOException {
      InputStream var4 = System.out.getClass().getResourceAsStream(var1);

      for (int var6 = 0; var6 < this.glyphTable.length; var6++) {
         int var5 = var4.read();
         this.glyphTable[var6] = new short[(var5 & 15) + 1];
         this.glyphTable[var6][0] = (short)((var5 & 240) >> 4);

         for (int var7 = 0; var7 < (var5 & 15); var7++) {
            this.glyphTable[var6][var7 + 1] = (short)(var4.read() << 8 | var4.read());
         }

         this.lineHeight = var2;
         this.spacing = var3;
      }

      var4.close();
   }

   private int charToGlyphIndex(int var1) {
      if (var1 < 32) {
         return 0;
      } else if (var1 < 128) {
         return var1 - 32;
      } else if (var1 < 160) {
         return 0;
      } else {
         return var1 < 256 ? var1 - 64 : 0;
      }
   }

   public final void drawText(Graphics var1, String var2, int var3, int var4, int var5) {
      this.drawTextRange(var1, var2, 0, -1, var3, var4, var5);
   }

   private void drawTextRange(Graphics var1, String var2, int var3, int var4, int var5, int var6, int var7) {
      if (var4 < 0) {
         var4 = var2.length() - var3;
      }

      switch (var7 & 114) {
         case 2:
            var6 -= this.lineHeight / 2;
            break;
         case 32:
            var6 -= this.lineHeight;
      }

      int var8 = 0;
      switch (var7 & 13) {
         case 1:
            for (int var16 = var3; var16 < var3 + var4; var16++) {
               var8 += this.glyphTable[this.charToGlyphIndex((int)var2.charAt(var16))][0] + this.spacing;
            }

            var5 -= (var8 - this.spacing) / 2;
         case 0:
         case 4:
            for (int var17 = var3; var17 < var3 + var4; var17++) {
               int var15 = this.charToGlyphIndex((int)var2.charAt(var17));

               for (int var18 = this.glyphTable[var15].length - 1; var18 >= 1; var18--) {
                  short var14 = this.glyphTable[var15][var18];
                  var1.fillRect(var5 + (var14 >> 12 & 15), var6 + (var14 >> 8 & 15), var14 >> 4 & 15, var14 & 15);
               }

               var5 += this.glyphTable[var15][0] + this.spacing;
            }
            break;
         case 2:
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            System.out.println("@!#%!%$#@%!%!%!%!$#% " + var7);
            break;
         case 8:
            var5 += this.spacing;

            for (int var10 = var3 + var4 - 1; var10 >= var3; var10--) {
               int var9 = this.charToGlyphIndex((int)var2.charAt(var10));
               var5 -= this.glyphTable[var9][0] + this.spacing;

               for (int var11 = this.glyphTable[var9].length - 1; var11 >= 1; var11--) {
                  short var13 = this.glyphTable[var9][var11];
                  var1.fillRect(var5 + (var13 >> 12 & 15), var6 + (var13 >> 8 & 15), var13 >> 4 & 15, var13 & 15);
               }
            }
      }
   }

   public final void drawTextRange(Graphics var1, char[] var2, int var3, int var4, int var5, int var6, int var7) {
      if (var4 < 0) {
         var4 = var2.length - var3;
      }

      switch (var7 & 114) {
         case 2:
            var6 -= this.lineHeight / 2;
            break;
         case 32:
            var6 -= this.lineHeight;
      }

      int var8 = 0;
      switch (var7 & 13) {
         case 1:
            for (int var16 = var3; var16 < var3 + var4; var16++) {
               var8 += this.glyphTable[this.charToGlyphIndex((int)var2[var16])][0] + this.spacing;
            }

            var5 -= (var8 - this.spacing) / 2;
         case 0:
         case 4:
            for (int var17 = var3; var17 < var3 + var4; var17++) {
               int var15 = this.charToGlyphIndex((int)var2[var17]);

               for (int var18 = this.glyphTable[var15].length - 1; var18 >= 1; var18--) {
                  short var14 = this.glyphTable[var15][var18];
                  var1.fillRect(var5 + (var14 >> 12 & 15), var6 + (var14 >> 8 & 15), var14 >> 4 & 15, var14 & 15);
               }

               var5 += this.glyphTable[var15][0] + this.spacing;
            }
            break;
         case 2:
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            System.out.println("@!#%!%$#@%!%!%!%!$#% " + var7);
            break;
         case 8:
            var5 += this.spacing;

            for (int var10 = var3 + var4 - 1; var10 >= var3; var10--) {
               int var9 = this.charToGlyphIndex((int)var2[var10]);
               var5 -= this.glyphTable[var9][0] + this.spacing;

               for (int var11 = this.glyphTable[var9].length - 1; var11 >= 1; var11--) {
                  short var13 = this.glyphTable[var9][var11];
                  var1.fillRect(var5 + (var13 >> 12 & 15), var6 + (var13 >> 8 & 15), var13 >> 4 & 15, var13 & 15);
               }
            }
      }
   }

   public final int textWidth(String var1) {
      return this.textWidth(var1, 0, -1);
   }

   public final int textWidth(String var1, int var2, int var3) {
      if (var3 < 0) {
         var3 = var1.length() - var2;
      }

      int var4 = 0;

      for (int var5 = var2; var5 < var2 + var3; var5++) {
         var4 += this.glyphTable[this.charToGlyphIndex((int)var1.charAt(var5))][0] + this.spacing;
      }

      return var4 - this.spacing;
   }

   public final int charWidth(char var1) {
      return this.glyphTable[this.charToGlyphIndex((int)var1)][0] + this.spacing;
   }
}
