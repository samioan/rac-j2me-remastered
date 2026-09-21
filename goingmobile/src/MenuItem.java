// Hand-written, faithfully renamed from decompiled/e.java (class e).
// One row of a menu page, parsed from the /o menu-definition table by
// Game's menu loader and consumed by the menu renderer/input handler.
//
// type:   0 plain label, 1-3 selectable list rows, 4 text-screen pages
//         (96 help / 97 about / 98 credits), 5 value row, 6 conditional
//         row, 8 credits scroller, 9 spacer
// action: menu page to open when selected (-1 = none, -104 language,
//         -105 name entry, -105/-112/-113 special items, 125 load,
//         126 quit, 127 new game) -- also softkey ids in some contexts
// stringId: index into ratchetandclank.strings (m*.txt)
// enabled: whether the row can currently be selected
public final class MenuItem {
   public byte type;
   public byte action;
   public byte stringId;
   public boolean enabled;

   public MenuItem(byte var1, byte var2, byte var3) {
      this.type = var1;
      this.action = var2;
      this.stringId = var3;
      this.enabled = true;
   }
}
