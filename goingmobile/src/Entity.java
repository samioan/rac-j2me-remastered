// Hand-written, faithfully renamed from decompiled/h.java (class h).
// Base class for Player (a) and Enemy (d): fixed-point position (px<<8),
// grid row/column bookkeeping, health and animation state.
//
// Position convention: posX is the horizontal position in 1/256-pixel units
// (the game's fixed point), row is the 14-pixel-tall tile row and posInRow
// the 1/256-pixel offset within it (0..3583, wrapping via wrapRow()).
// column() derives the 22-pixel-wide tile column from posX.
public class Entity {
   // For Enemy (d): enemy type 0..4; for Player: always 0 (anim set index).
   public byte kind;
   public byte row;
   public int posX;
   public short velY;
   public short velX;
   public byte fieldW;     // never read anywhere -- role unconfirmed
   public byte spawnRow;   // copy of row kept at spawn
   public int posInRow;    // 1/256 px within the row (wraps at 14*256)
   public byte activeFlag; // set to 1 at spawn; role otherwise unconfirmed
   public byte health;
   public byte animFrame;
   public byte animCounter;
   public byte animState;
   public byte animRestart;
   public byte animHold;
   public boolean facingRight;
   public byte subState;

   public final byte column() {
      return (byte)((this.posX >> 8) / 22);
   }

   public final void setAnimState(byte var1) {
      this.animState = var1;
      this.animFrame = this.animCounter = 0;
   }

   public final void wrapRow() {
      if (this.posInRow < 0) {
         this.posInRow += 3584;
         this.row--;
         if (this.row < 0) {
            this.row = 0;
            this.posInRow = 0;
            return;
         }
      } else if (this.posInRow > 3584) {
         this.posInRow -= 3584;
         this.row++;
      }
   }
}
