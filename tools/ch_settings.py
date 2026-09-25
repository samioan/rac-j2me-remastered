"""Port options in the game's own Settings screens (applied by java2cpp.py, like ch_widescreen.py).

Both settings screens get four extra rows -- Resolution, Fullscreen, Scaling, Speed -- drawn in the
game's menu style, above the existing entries. Left/right (or Select) changes a row. The values live
in C++: `Port.label(row)` returns the row's text and `Port.change(row, dir)` changes it
(see port/src/main.cpp). The screens are:

* the title menu's "Game Settings" (`screen == 2`: Sound, Clear Save), and
* the pause menu's "Game Settings" (`state == 5`: Sound only).

Each entry is (file, old, new, expected occurrences).
"""

PATCHES = [
    # ---- title menu > Settings: render
    ("Game.java", """         case 2:
            this.e(var1, 32);
            this.menuItemY[0] = 176;
            this.menuItemY[1] = 6 + this.a(var1, this.audio.isEnabled() ? this.getString(33) : this.getString(34), 176, menuCursor == 0, 16777215);
            var2 = this.menuItemY[1];
            this.a(var1, this.getString(38), var2, menuCursor == 1, 16777215);""",
     """         case 2:
            this.e(var1, 32);
            var2 = 154;
            this.menuItemY[0] = var2;
            var2 = 6 + this.a(var1, this.audio.isEnabled() ? this.getString(33) : this.getString(34), var2, menuCursor == 0, 16777215);

            for (var3 = 0; var3 < 4; var3++) {
               this.menuItemY[1 + var3] = var2;
               var2 = 6 + this.a(var1, Port.label(var3), var2, menuCursor == 1 + var3, 16777215);
            }

            this.menuItemY[5] = var2;
            this.a(var1, this.getString(38), var2, menuCursor == 5, 16777215);""", 1),
    # ---- title menu > Settings: input
    ("Game.java", """            case 2:
               if (this.b(var1, 0, 1, 1)) {
                  switch (menuCursor) {""",
     """            case 2:
               if ((var1 == 2 || var1 == 5) && menuCursor >= 1 && menuCursor <= 4) {
                  Port.change(menuCursor - 1, var1 == 5 ? 1 : -1);
                  this.es |= 3;
                  return;
               }

               if (this.b(var1, 0, 5, 1)) {
                  switch (menuCursor) {""", 1),
    ("Game.java", """                     case 1:
                        this.setScreen(6);
                     default:
                        return;""",
     """                     case 1:
                     case 2:
                     case 3:
                     case 4:
                        Port.change(menuCursor - 1, 1);
                        this.es |= 3;
                        return;
                     case 5:
                        this.setScreen(6);
                     default:
                        return;""", 1),

    # ---- pause menu > Settings: render
    ("Game.java", """               boolean var85 = false;
               this.e(var1, 32);
               this.menuItemY[0] = 174;
               this.a(var1, this.getString(this.audio.isEnabled() ? 33 : 34), 174, menuCursor == 0, 16777215);""",
     """               boolean var85 = false;
               this.e(var1, 32);
               int var901 = 156;
               this.menuItemY[0] = var901;
               var901 = 6 + this.a(var1, this.getString(this.audio.isEnabled() ? 33 : 34), var901, menuCursor == 0, 16777215);

               for (int var902 = 0; var902 < 4; var902++) {
                  this.menuItemY[1 + var902] = var901;
                  var901 = 6 + this.a(var1, Port.label(var902), var901, menuCursor == 1 + var902, 16777215);
               }
""", 1),
    # ---- pause menu > Settings: input
    ("Game.java", """         } else if (state == 5) {
            if (var1 != 8 && var1 != 27) {
               if (var1 == 29) {
                  menuCursor = 0;
                  state = 4;
                  this.audio.play(4);
                  return;
               }
            } else if (menuCursor == 0) {
               this.audio.setEnabled(!this.audio.isEnabled());
               return;
            }
         } else if (state == 6) {""",
     """         } else if (state == 5) {
            moveCursor(var1, 0, 4);
            if ((var1 == 2 || var1 == 5) && menuCursor >= 1) {
               Port.change(menuCursor - 1, var1 == 5 ? 1 : -1);
               return;
            }

            if (var1 != 8 && var1 != 27) {
               if (var1 == 29) {
                  menuCursor = 0;
                  state = 4;
                  this.audio.play(4);
                  return;
               }
            } else if (menuCursor == 0) {
               this.audio.setEnabled(!this.audio.isEnabled());
               return;
            } else {
               Port.change(menuCursor - 1, 1);
               return;
            }
         } else if (state == 6) {""", 1),
]


def apply(name, text):
    for fname, old, new, count in PATCHES:
        if fname != name:
            continue
        found = text.count(old)
        if found != count:
            raise SystemExit(f"settings patch matches {found}x (expected {count}) in {fname}: {old[:70]!r}")
        text = text.replace(old, new)
    return text
