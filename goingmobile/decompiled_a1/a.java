import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;

public final class a {
   private short[][] b = new short[192][];
   public int a;
   private int c;

   public a(String var1, int var2, int var3) throws IOException {
      InputStream var4 = System.out.getClass().getResourceAsStream(var1);

      for (int var6 = 0; var6 < this.b.length; var6++) {
         int var5 = var4.read();
         this.b[var6] = new short[(var5 & 15) + 1];
         this.b[var6][0] = (short)((var5 & 240) >> 4);

         for (int var7 = 0; var7 < (var5 & 15); var7++) {
            this.b[var6][var7 + 1] = (short)(var4.read() << 8 | var4.read());
         }

         this.a = var2;
         this.c = var3;
      }

      var4.close();
   }

   private int a(int var1) {
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

   public final void a(Graphics var1, String var2, int var3, int var4, int var5) {
      this.a(var1, var2, 0, -1, var3, var4, var5);
   }

   private void a(Graphics var1, String var2, int var3, int var4, int var5, int var6, int var7) {
      if (var4 < 0) {
         var4 = var2.length() - var3;
      }

      switch (var7 & 114) {
         case 2:
            var6 -= this.a / 2;
            break;
         case 32:
            var6 -= this.a;
      }

      int var8 = 0;
      switch (var7 & 13) {
         case 1:
            for (int var16 = var3; var16 < var3 + var4; var16++) {
               var8 += this.b[this.a((int)var2.charAt(var16))][0] + this.c;
            }

            var5 -= (var8 - this.c) / 2;
         case 0:
         case 4:
            for (int var17 = var3; var17 < var3 + var4; var17++) {
               int var15 = this.a((int)var2.charAt(var17));

               for (int var18 = this.b[var15].length - 1; var18 >= 1; var18--) {
                  short var14 = this.b[var15][var18];
                  var1.fillRect(var5 + (var14 >> 12 & 15), var6 + (var14 >> 8 & 15), var14 >> 4 & 15, var14 & 15);
               }

               var5 += this.b[var15][0] + this.c;
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
            var5 += this.c;

            for (int var10 = var3 + var4 - 1; var10 >= var3; var10--) {
               int var9 = this.a((int)var2.charAt(var10));
               var5 -= this.b[var9][0] + this.c;

               for (int var11 = this.b[var9].length - 1; var11 >= 1; var11--) {
                  short var13 = this.b[var9][var11];
                  var1.fillRect(var5 + (var13 >> 12 & 15), var6 + (var13 >> 8 & 15), var13 >> 4 & 15, var13 & 15);
               }
            }
      }
   }

   public final void a(Graphics var1, char[] var2, int var3, int var4, int var5, int var6, int var7) {
      if (var4 < 0) {
         var4 = var2.length - var3;
      }

      switch (var7 & 114) {
         case 2:
            var6 -= this.a / 2;
            break;
         case 32:
            var6 -= this.a;
      }

      int var8 = 0;
      switch (var7 & 13) {
         case 1:
            for (int var16 = var3; var16 < var3 + var4; var16++) {
               var8 += this.b[this.a((int)var2[var16])][0] + this.c;
            }

            var5 -= (var8 - this.c) / 2;
         case 0:
         case 4:
            for (int var17 = var3; var17 < var3 + var4; var17++) {
               int var15 = this.a((int)var2[var17]);

               for (int var18 = this.b[var15].length - 1; var18 >= 1; var18--) {
                  short var14 = this.b[var15][var18];
                  var1.fillRect(var5 + (var14 >> 12 & 15), var6 + (var14 >> 8 & 15), var14 >> 4 & 15, var14 & 15);
               }

               var5 += this.b[var15][0] + this.c;
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
            var5 += this.c;

            for (int var10 = var3 + var4 - 1; var10 >= var3; var10--) {
               int var9 = this.a((int)var2[var10]);
               var5 -= this.b[var9][0] + this.c;

               for (int var11 = this.b[var9].length - 1; var11 >= 1; var11--) {
                  short var13 = this.b[var9][var11];
                  var1.fillRect(var5 + (var13 >> 12 & 15), var6 + (var13 >> 8 & 15), var13 >> 4 & 15, var13 & 15);
               }
            }
      }
   }

   public final int a(String var1) {
      return this.a(var1, 0, -1);
   }

   public final int a(String var1, int var2, int var3) {
      if (var3 < 0) {
         var3 = var1.length() - var2;
      }

      int var4 = 0;

      for (int var5 = var2; var5 < var2 + var3; var5++) {
         var4 += this.b[this.a((int)var1.charAt(var5))][0] + this.c;
      }

      return var4 - this.c;
   }

   public final int a(char var1) {
      return this.b[this.a((int)var1)][0] + this.c;
   }
}
