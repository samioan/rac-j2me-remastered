import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class f {
   private Image i;
   public short a;
   public short b;
   public short c;
   public short d;
   public short e;
   public short f;
   public short g;
   public short h;
   private short j;
   private short k;

   public f(Image var1) {
      this.i = var1;
   }

   public final void a(short[] var1) {
      this.a = var1[0];
      this.b = var1[1];
      this.j = var1[2];
      this.k = var1[3];
      this.c = var1[4];
      this.d = var1[5];
      this.e = var1[6];
      this.f = var1[7];
      this.g = var1[8];
      this.h = var1[9];
   }

   public final void a(Graphics var1, int var2, int var3, int var4) {
      var4 &= 3;
      var2 += (var4 & 2) == 0 ? this.a : -(this.a + this.c);
      var3 += (var4 & 1) == 0 ? this.b : -(this.b + this.d);
      int var5 = this.c;
      int var6 = this.d;
      short var7 = this.j;
      short var8 = this.k;
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
      if ((var9 = var7 + var5 - this.i.getWidth()) > 0) {
         var5 -= var9;
      }

      if ((var9 = var8 + var6 - this.i.getHeight()) > 0) {
         var6 -= var9;
      }

      if (var5 > 0 && var6 > 0) {
         var1.drawRegion(this.i, var7, var8, var5, var6, var4, var2, var3, 20);
      }
   }
}
