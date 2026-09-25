"""Render-time interpolation for the Clone Home port (applied by java2cpp.py after ch_widescreen).

Game logic runs at a fixed rate (25 Hz by default; the game does one step per frame). To draw faster
than that, every logic tick records the positions of everything that moves (camera, player, enemies,
both projectile pools, moving platforms, pickups, falling crates); a frame drawn a fraction `a` of the
way to the next tick temporarily writes blended positions into those fields, renders, and puts the
real ones back. Logic is untouched, the picture just lags one tick behind. Moves larger than a
threshold (respawns, room changes) and entities whose type changed are not blended.

`Engine.interpAlpha` (0..255, or 256 = no blending) and `Engine.tickFrame` come from the C++ engine
loop. Extra frames also zero `deltaTime`, because the two decorative animations stepped inside the
render code (`stepAnim(maxEnemies, deltaTime)`) must advance once per tick, not once per frame.

Each entry is (file, old, new, expected occurrences).
"""

N = 292  # 4 header values + 96 triples (10 enemies, 10+10 shots, 4 platforms, 12 pickups, 50 crates)

METHODS = """   private static int[] iPrev = new int[292];
   private static int[] iCur = new int[292];
   private static int[] iBlend = new int[292];
   private static boolean iValid = false;

   private boolean collect(int[] var1) {
      if (this.enemies == null || this.playerShots == null || this.enemyShots == null || platformDir == null || pickupX == null || crateType == null || this.crateFallOffset == null) {
         return false;
      }

      var1[0] = cameraX;
      var1[1] = cameraY;
      var1[2] = playerX;
      var1[3] = playerY;
      int var2 = 4;

      for (int var3 = 0; var3 < 10; var3++) {
         var1[var2] = this.enemies[var3].type;
         var1[var2 + 1] = this.enemies[var3].worldX;
         var1[var2 + 2] = this.enemies[var3].worldY;
         var2 += 3;
      }

      for (int var4 = 0; var4 < 10; var4++) {
         var1[var2] = this.playerShots[var4].type;
         var1[var2 + 1] = this.playerShots[var4].worldX;
         var1[var2 + 2] = this.playerShots[var4].worldY;
         var2 += 3;
      }

      for (int var5 = 0; var5 < 10; var5++) {
         var1[var2] = this.enemyShots[var5].type;
         var1[var2 + 1] = this.enemyShots[var5].worldX;
         var1[var2 + 2] = this.enemyShots[var5].worldY;
         var2 += 3;
      }

      for (int var6 = 0; var6 < 4; var6++) {
         var1[var2] = platformDir[var6];
         var1[var2 + 1] = platformOffsetX[var6];
         var1[var2 + 2] = platformOffsetY[var6];
         var2 += 3;
      }

      for (int var7 = 0; var7 < 12; var7++) {
         var1[var2] = pickupKind[var7];
         var1[var2 + 1] = pickupX[var7];
         var1[var2 + 2] = pickupY[var7];
         var2 += 3;
      }

      for (int var8 = 0; var8 < 50; var8++) {
         var1[var2] = crateType[var8];
         var1[var2 + 1] = this.crateFallOffset[var8];
         var1[var2 + 2] = 0;
         var2 += 3;
      }

      return true;
   }

   private void putAll(int[] var1) {
      cameraX = var1[0];
      cameraY = var1[1];
      playerX = var1[2];
      playerY = var1[3];
      this.setScroll(cameraX, cameraY - 21);
      int var2 = 4;

      for (int var3 = 0; var3 < 10; var3++) {
         this.enemies[var3].worldX = var1[var2 + 1];
         this.enemies[var3].worldY = var1[var2 + 2];
         var2 += 3;
      }

      for (int var4 = 0; var4 < 10; var4++) {
         this.playerShots[var4].worldX = var1[var2 + 1];
         this.playerShots[var4].worldY = var1[var2 + 2];
         var2 += 3;
      }

      for (int var5 = 0; var5 < 10; var5++) {
         this.enemyShots[var5].worldX = var1[var2 + 1];
         this.enemyShots[var5].worldY = var1[var2 + 2];
         var2 += 3;
      }

      for (int var6 = 0; var6 < 4; var6++) {
         platformOffsetX[var6] = (short)var1[var2 + 1];
         platformOffsetY[var6] = (short)var1[var2 + 2];
         var2 += 3;
      }

      for (int var7 = 0; var7 < 12; var7++) {
         pickupX[var7] = var1[var2 + 1];
         pickupY[var7] = var1[var2 + 2];
         var2 += 3;
      }

      for (int var8 = 0; var8 < 50; var8++) {
         this.crateFallOffset[var8] = (short)var1[var2 + 1];
         var2 += 3;
      }
   }

   private static int lerp(int var0, int var1, int var2, int var3) {
      int var4 = var1 - var0;
      return var4 > var3 || var4 < -var3 ? var1 : var0 + (var4 * var2 >> 8);
   }

   public final void interpSnapshot() {
      iValid = this.collect(iPrev);
   }

   public final boolean interpApply(int var1) {
      if (!iValid || !this.collect(iCur)) {
         return false;
      }

      iBlend[0] = lerp(iPrev[0], iCur[0], var1, 48);
      iBlend[1] = lerp(iPrev[1], iCur[1], var1, 48);
      iBlend[2] = lerp(iPrev[2], iCur[2], var1, 12288);
      iBlend[3] = lerp(iPrev[3], iCur[3], var1, 12288);
      int var2 = 4;

      for (int var3 = 0; var3 < 96; var3++) {
         int var4 = var3 >= 30 && var3 < 34 || var3 >= 46 ? 48 : 12288;
         iBlend[var2] = iCur[var2];
         if (iPrev[var2] == iCur[var2]) {
            iBlend[var2 + 1] = lerp(iPrev[var2 + 1], iCur[var2 + 1], var1, var4);
            iBlend[var2 + 2] = lerp(iPrev[var2 + 2], iCur[var2 + 2], var1, var4);
         } else {
            iBlend[var2 + 1] = iCur[var2 + 1];
            iBlend[var2 + 2] = iCur[var2 + 2];
         }

         var2 += 3;
      }

      this.putAll(iBlend);
      return true;
   }

   public final void interpRestore() {
      this.putAll(iCur);
   }

"""

PATCHES = [
    # snapshot at the start of every logic tick
    ("Game.java", """   public final void update() {
      deltaTime = super.frameTime;""",
     METHODS + """   public final void update() {
      this.interpSnapshot();
      deltaTime = super.frameTime;""", 1),
    # blended draw for the extra frames of live gameplay (var4 is true for the world states, see ch_widescreen)
    ("Game.java", """      this.renderInner(var1);
      if (var3 != 0) {""",
     """      int var6 = deltaTime;
      boolean var7 = var4 && Engine.interpAlpha < 256;
      if (var7 && !Engine.tickFrame) {
         deltaTime = 0;
      }

      boolean var5 = var7 && this.interpApply(Engine.interpAlpha);
      this.renderInner(var1);
      deltaTime = var6;
      if (var5) {
         this.interpRestore();
      }

      if (var3 != 0) {""", 1),
]


def apply(name, text):
    for fname, old, new, count in PATCHES:
        if fname != name:
            continue
        found = text.count(old)
        if found != count:
            raise SystemExit(f"interp patch matches {found}x (expected {count}) in {fname}: {old[:70]!r}")
        text = text.replace(old, new)
    return text
