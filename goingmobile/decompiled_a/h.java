public class h {
   public byte R;
   public byte S;
   public int T;
   public short U;
   public short V;
   public byte W;
   public byte X;
   public int Y;
   public int Z;
   public byte aa;
   public short ab;
   public short ac;
   public byte ad;
   public byte ae;
   public byte af;
   public boolean ag;
   public byte ah;

   public final byte p() {
      return (byte)((this.T >> 8) / 22);
   }

   public final void a(byte var1) {
      this.ad = var1;
      this.ab = this.ac = 0;
   }

   public final void q() {
      if (this.Y < 0) {
         this.Y += 3584;
         this.S--;
         if (this.S < 0) {
            this.S = 0;
            this.Y = 0;
            return;
         }
      } else if (this.Y > 3584) {
         this.Y -= 3584;
         this.S++;
      }
   }
}
