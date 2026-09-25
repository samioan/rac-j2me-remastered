"""Widescreen changes for the Clone Home port, applied by java2cpp.py to the renamed Java
(clonehome/src stays a faithful decompile).

The phone canvas is 240x320. Gameplay draws the world across the whole canvas width, so a
wider window shows more of the level: `Game.viewW` is the logical width (240 = original).
Everything laid out in 240-px design coordinates (menus, the HUD bar, dialogue text) is drawn
centred: menus by translating the whole frame, the HUD and message box by translating just
those blocks. Culling and camera limits that hard-coded 240 use viewW.

Each entry is (file, old, new, expected occurrences).
"""

# States drawn over the persistent world image (the screen is not cleared entering them). 22, the weapon
# wheel, is laid out in 240-px design coordinates, so it is translated like a menu but keeps the world behind it.
WIDE_STATES = "state == 0 || state == 16 || state == 17 || state == 24 || state == 22"

PATCHES = [
    # ---- state: the width, the centring offset, and the render wrapper
    ("Game.java", "   public static int centerX;\n",
     """   public static int centerX;
   public static int viewW = 240;
   private static int lastViewW = 240;
   private static boolean lastWide = true;

   private static int uiX() {
      return viewW - 240 >> 1;
   }
""", 1),
    ("Game.java", """   public final void render(Graphics var1) {
      if (this.gA && state == 4) {""",
     """   public final void render(Graphics var1) {
      boolean var2 = %s;
      if (var2 != lastWide || viewW != lastViewW) {
         lastWide = var2;
         lastViewW = viewW;
         this.setViewSize(viewW, 299);
         var1.setClip(0, 0, 4096, 320);
         var1.setColor(0);
         var1.fillRect(0, 0, 4096, 320);
         this.requestClear();
         hudDirty = true;
      }

      boolean var4 = var2 && state != 22;
      int var3 = var4 ? 0 : viewW - 240 >> 1;
      centerX = var4 ? viewW >> 1 : 120;
      this.setViewport(var4 ? viewW : 240, 320);
      if (var3 != 0) {
         var1.translate(var3, 0);
      }

      this.renderInner(var1);
      if (var3 != 0) {
         var1.translate(-var3, 0);
      }
   }

   private void renderInner(Graphics var1) {
      if (this.gA && state == 4) {""" % WIDE_STATES, 1),

    # ---- world drawing
    # drawTileLayer draws its per-tile objects (swingshot anchors, hints, checkpoints, doors' caps) for a fixed
    # 6 tile columns from the left edge: enough for 240 px, but objects further right vanished when widened.
    ("Game.java", "      var6 += 6;\n", "      var6 += viewW / 57 + 2;\n", 1),
    ("Game.java", "var1.setClip(0, 21, 240, 299);", "var1.setClip(0, 21, viewW, 299);", 4),
    ("Game.java", "var1.fillRect(0, 299, 240, 21);", "var1.fillRect(0, 299, viewW, 21);", 1),
    ("Game.java", "this.setViewSize(240, 299);", "this.setViewSize(viewW, 299);", 1),
    ("Game.java", "var1.setClip(240 - var92 >> 1, 21, var92, var18);", "var1.setClip(viewW - var92 >> 1, 21, var92, var18);", 1),
    ("Game.java", "var1.drawImage(weaponIconSheet, 240 - var92 >> 1, 21 - 7 * var18, 0);",
     "var1.drawImage(weaponIconSheet, viewW - var92 >> 1, 21 - 7 * var18, 0);", 1),

    # ---- HUD bar: 240-wide design drawn centred on a black strip
    ("Game.java", """                  if (hudDirty) {
                     this.setFont(fontMain);
                     this.resetClip(var1);
                     var1.setColor(0);
                     var1.fillRect(0, 0, 240, 21);""",
     """                  if (hudDirty) {
                     var1.translate(uiX(), 0);
                     this.setFont(fontMain);
                     this.resetClip(var1);
                     var1.setClip(-uiX(), 0, viewW, 21);
                     var1.setColor(0);
                     var1.fillRect(-uiX(), 0, viewW, 21);""", 1),
    ("Game.java", """                     hudDirty = false;
                  }

                  if (ac) {""",
     """                     var1.translate(-uiX(), 0);
                     hudDirty = false;
                  }

                  if (ac) {""", 1),

    # ---- dialogue box
    ("Game.java", """                  if (this.messageActive) {
                     this.drawMessageBox(var1);
                     messageNext = this.drawMessageText(var1, messageCursor);
                     hudDirty = true;
                  }""",
     """                  if (this.messageActive) {
                     var1.translate(uiX(), 0);
                     this.drawMessageBox(var1);
                     messageNext = this.drawMessageText(var1, messageCursor);
                     var1.translate(-uiX(), 0);
                     hudDirty = true;
                  }""", 1),
    ("Game.java", "var1.fillRect(0, var2, 240, var3);",
     "var1.setClip(-uiX(), var2, viewW, var3);\n      var1.fillRect(-uiX(), var2, viewW, var3);", 1),
    ("Game.java", "this.drawAnim(var1, playerSprites, bt + 1, var6 + 142, var7, 2);",
     "this.drawAnim(var1, playerSprites, bt + 1, var6 + 142 - uiX(), var7, 2);", 1),

    # ---- camera: keep the player where the 240-px design put him relative to the centre
    ("Game.java", "var1 = -(var4 - 57);", "var1 = -(var4 - 57 - uiX());", 1),
    ("Game.java", "var1 = -(var4 + 57 - 240);", "var1 = -(var4 + 57 - 240 - uiX());", 1),
    ("Game.java", """      if (cameraX < -1356) {
         cameraX = -1356;""", """      if (cameraX < viewW - 1596) {
         cameraX = viewW - 1596;""", 1),
    ("Game.java", "cameraMinX = -1356;", "cameraMinX = viewW - 1596;", 1),
    ("Game.java", "cameraX = cameraMinX;", "cameraX = viewW - 1596;", 1),

    # ---- off-screen culling / activity ranges
    ("Game.java", "crateX[var9] + cameraX <= 240", "crateX[var9] + cameraX <= viewW", 1),
    ("Game.java", "crateX[var10] + cameraX <= 240", "crateX[var10] + cameraX <= viewW", 1),
    ("Game.java", "if (var6 <= 240 && var6 + var2 >= 0", "if (var6 <= viewW && var6 + var2 >= 0", 1),
    ("Game.java", "if (var18 <= 240 && var18 + var28 >= 0", "if (var18 <= viewW && var18 + var28 >= 0", 1),
    ("Game.java", "if ((var19 <= 240 || var12 <= 240)", "if ((var19 <= viewW || var12 <= viewW)", 1),
    ("Game.java", "var20 <= 240 && var25 + var32 >= 0", "var20 <= viewW && var25 + var32 >= 0", 1),
    ("Game.java", "var21 <= 240 && var26 + var32 >= 0", "var21 <= viewW && var26 + var32 >= 0", 1),
    ("Game.java", "if (var22 <= 240 && var22 + 57 >= 0", "if (var22 <= viewW && var22 + 57 >= 0", 1),
    ("Game.java", "var3 - var6 + cameraX <= 240", "var3 - var6 + cameraX <= viewW", 1),
    ("Game.java", "if (var6 > 240) {", "if (var6 > viewW) {", 1),
    ("Game.java", "if (var5 > 240) {", "if (var5 > viewW) {", 1),
    ("Game.java", "|| var4 > 240) {", "|| var4 > viewW) {", 1),
    ("Game.java", "(var3 = var0.worldX >> 8) > 240", "(var3 = var0.worldX >> 8) > viewW", 1),
    ("Game.java", "(var1 = var0.worldX >> 8) > 240", "(var1 = var0.worldX >> 8) > viewW", 1),
    ("Enemy.java", "var5 <= 240 && var6 >= -Game.spriteBaseHeight", "var5 <= Game.viewW && var6 >= -Game.spriteBaseHeight", 1),
    ("Projectile.java", "-var4 + 240 + 57", "-var4 + Game.viewW + 57", 4),
]


def apply(name, text):
    for fname, old, new, count in PATCHES:
        if fname != name:
            continue
        found = text.count(old)
        if found != count:
            raise SystemExit(f"widescreen patch matches {found}x (expected {count}) in {fname}: {old[:70]!r}")
        text = text.replace(old, new)
    return text
