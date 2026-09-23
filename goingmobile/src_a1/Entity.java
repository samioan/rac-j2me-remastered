// Hand-written, faithfully renamed from decompiled_a1/i.java (class i).
// a1's equivalent of the legacy build's Entity (decompiled/h.java, class h,
// see ../src/Entity.java) -- base class for Player (b) and Enemy (f).
// Same position convention as the legacy build: posX is 1/256-pixel fixed
// point, row/posInRow the tile-row + in-row offset (wrapping via
// wrapRow() against h.G, the row height -- a1's tile grid is a different
// size than the legacy build's 22x14, not yet confirmed).
//
// Confirmed by this class's own three methods (column/setAnimState/
// wrapRow, structurally identical to the legacy build's) plus grep
// cross-checks against Player (b.java) and Enemy (f.java) use sites:
// health (initialized to 20, decremented by damage, <=0 triggers the
// death anim), kind (Enemy sets -1 at spawn like the legacy build's
// known bug, then 0..4 as the type index into every per-type table),
// activeFlag (set to 1 at spawn sites in h.java/g.java). animRestart/
// animHold are renamed by pattern match to the legacy build's fields
// (same set-to-0/1 shape at the same call sites) but not independently
// confirmed -- see the open questions below.
public class Entity {
   public byte kind;
   public byte row;
   public int posX;
   public short velY;
   public short velX;
   public byte ae;          // unconfirmed -- transient collision-check scratch? (assigned from column/row helper calls, re-read immediately after)
   public byte af;          // unconfirmed -- see ae
   public int posInRow;
   public int activeFlag;   // set to 1 at spawn (int here, byte in the legacy build)
   public byte health;
   public short animFrame;  // short here, byte in the legacy build
   public short animCounter; // short here, byte in the legacy build
   public byte animState;
   public byte animRestart; // tentative -- pattern match, not independently confirmed
   public byte animHold;    // tentative -- pattern match, not independently confirmed
   public boolean facingRight;
   public byte ap;          // unconfirmed -- zeroed at spawn/reset, tested `== 0` in Player physics

   public final byte column() {
      return (byte)((this.posX >> 8) / Game.tileWidth);
   }

   public final void setAnimState(byte var1) {
      this.animState = var1;
      this.animFrame = this.animCounter = 0;
   }

   public final void wrapRow() {
      if (this.posInRow < 0) {
         this.posInRow += Game.tileHeight << 8;
         this.row--;
         if (this.row < 0) {
            this.row = 0;
            this.posInRow = 0;
            return;
         }
      } else if (this.posInRow > Game.tileHeight << 8) {
         this.posInRow -= Game.tileHeight << 8;
         this.row++;
      }
   }
}
