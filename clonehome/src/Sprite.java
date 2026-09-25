import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

// $VF: renamed from: f
public final class Sprite {
   // $VF: renamed from: i javax.microedition.lcdui.Image
   private Image sheet;
   // $VF: renamed from: a short
   public short offsetX;
   // $VF: renamed from: b short
   public short offsetY;
   // $VF: renamed from: c short
   public short width;
   // $VF: renamed from: d short
   public short height;
   // $VF: renamed from: e short
   public short charCode;
   public short f;
   // $VF: renamed from: g short
   public short advance;
   // $VF: renamed from: h short
   public short metric;
   // $VF: renamed from: j short
   private short srcX;
   // $VF: renamed from: k short
   private short srcY;

   public Sprite(Image var1) {
      this.sheet = var1;
   }

   // $VF: renamed from: a (short[]) void
   public final void load(short[] var1) {
      this.offsetX = var1[0];
      this.offsetY = var1[1];
      this.srcX = var1[2];
      this.srcY = var1[3];
      this.width = var1[4];
      this.height = var1[5];
      this.charCode = var1[6];
      this.f = var1[7];
      this.advance = var1[8];
      this.metric = var1[9];
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics, int, int, int) void
   public final void draw(Graphics var1, int var2, int var3, int var4) {
      var4 &= 3;
      var2 += (var4 & 2) == 0 ? this.offsetX : -(this.offsetX + this.width);
      var3 += (var4 & 1) == 0 ? this.offsetY : -(this.offsetY + this.height);
      int var5 = this.width;
      int var6 = this.height;
      short var7 = this.srcX;
      short var8 = this.srcY;
      if (var7 < 0) {
         var5 += var7;
         var2 += var7;
         var7 = 0;
      }

      if (var8 < 0) {
         var6 += var8;
         var3 += var8;
         var8 = 0;
      }

      int var9;
      if ((var9 = var7 + var5 - this.sheet.getWidth()) > 0) {
         var5 -= var9;
      }

      if ((var9 = var8 + var6 - this.sheet.getHeight()) > 0) {
         var6 -= var9;
      }

      if (var5 > 0 && var6 > 0) {
         var1.drawRegion(this.sheet, var7, var8, var5, var6, var4, var2, var3, 20);
      }
   }
}
