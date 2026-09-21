public class i {
   public byte Z;
   public byte aa;
   public int ab;
   public short ac;
   public short ad;
   public byte ae;
   public byte af;
   public int ag;
   public int ah;
   public byte ai;
   public short aj;
   public short ak;
   public byte al;
   public byte am;
   public byte an;
   public boolean ao;
   public byte ap;

   public final byte u() {
      return (byte)((this.ab >> 8) / h.F);
   }

   public final void a(byte var1) {
      this.al = var1;
      this.aj = this.ak = 0;
   }

   public final void v() {
      if (this.ag < 0) {
         this.ag = this.ag + (h.G << 8);
         this.aa--;
         if (this.aa < 0) {
            this.aa = 0;
            this.ag = 0;
            return;
         }
      } else if (this.ag > h.G << 8) {
         this.ag = this.ag - (h.G << 8);
         this.aa++;
      }
   }
}
