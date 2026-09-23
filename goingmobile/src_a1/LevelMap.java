// Hand-written, faithfully renamed from decompiled_a1/d.java (class d).
// a1's tilemap engine -- structurally the legacy build's LevelMap
// (decompiled/c.java) plus a new layer on top: each of the 13 levels
// (`/level0.bin`..`/level12.bin`) is split into up to 12 "rooms" (sub-
// grids, each still the legacy build's 28x18 byte-per-cell shape), with
// a per-level table (LEVEL_ROOM_TABLES) describing how many rooms it
// has, a neighbor/connectivity table between them (subGridNeighbors),
// and a mapping from room id to the room's index in that level's file
// (subGridIndexMap). This is NOT the same thing as `mapData.txt` (a
// world-map asset loaded separately by Game, decompiled_a1/h.java:2648)
// -- LEVEL_ROOM_TABLES is compiled-in data, not loaded from any file.
//
// Only LevelMap's own members are renamed here. Every `this.game.xxx`
// reference is Game's own (still-obfuscated) member -- Game hasn't had
// its phase-1 pass yet (see CLASS_MAP.md); the class-level rename
// (`h`->`Game`) is applied per this file's policy note in CLASS_MAP.md,
// member names are not.
//
// Tile legend (from this method's own branches; not yet cross-checked
// against a tool-verified parser the way the legacy build's was --
// TENTATIVE numbering, revisit once a1 gets its own ASSET_FORMATS.md
// pass): 0-18ish background/solid (rendered from Game.aF, animated by
// tileAnimCounter for tiles 15-18), 19-34 also solid-ish (used by
// computeColumnSolidMasks), 35 player-start-adjacent marker, 36-42
// decoration, 43-46 titanium-bolt boxes (BOLT_TILE_TYPES), 47-55 enemy
// spawns, 56-61 hazards/unhandled (fall through to raw tile id), 62-69
// zip-line-ish (`Game.a(col,row,parity,half)`), 70-76 solid-with-bitmask
// (columnSolidMasks-gated via `Game.bw`), 77-96 infolink-style tiles
// (same range as the legacy build's), 97-98 hyper-shot-style targets,
// 99-101 unhandled/passthrough, 102-113 moving platforms
// (PLATFORM_TILE_TYPES), 114-127 enemies (ENEMY_TILE_TYPES), negative
// byte values (tile ids 128-255 read as negative bytes) -119..-92ish
// are markers/camera/player-start tiles (mirrors the legacy build's
// 0x80+ range).
import javax.microedition.lcdui.Graphics;

public final class LevelMap {
   public static byte currentSubGrid;
   public static byte startSubGrid;
   public Game game;
   public static byte[][][] subGrids;
   public static int[] columnSolidMasks;
   public byte[][] tiles;
   public static byte subGridCount;
   public static byte[][] subGridNeighbors;
   public static byte[] subGridIndexMap;
   public static byte tileWidth;
   public static byte tileHeight;
   public static byte tileAnimCounter;
   public static final byte[] PLATFORM_TILE_TYPES = new byte[]{15, 15, 16, 17, 18, 18, 19, 20, 21, 21, 22, 23};
   public static final byte[] ENEMY_TILE_TYPES = new byte[]{3, 4, 5, 0, 1, 2, 6, 7, 7, 9, 10, 10, 12, 13};
   public static final byte[] BOLT_TILE_TYPES = new byte[]{0, 2, 1, 3};
   // 13 entries (level0..level12); each entry is [roomCount, roomCount*4
   // neighbor bytes, roomCount index-map bytes, startSubGrid] packed flat.
   public static final byte[][] LEVEL_ROOM_TABLES = new byte[][]{
      {1, -1, -1, -1, -1, 0, 2},
      {6, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, 5, 3, -1, -1, -1, 4, 0, 1, 2, 3, 4, 5, 0},
      {4, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, -1, 2, 0, 1, 2, 3, 1},
      {5, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, -1, 3, 4, 0, 1, 2, 3, 2},
      {6, -1, -1, 1, -1, -1, 2, 3, 0, 1, -1, 4, -1, -1, 4, -1, 1, 3, -1, 5, 2, -1, -1, -1, 4, 0, 1, 3, 2, 4, 5, 2},
      {5, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, -1, 3, 2, 1, 0, 3, 4, 2},
      {5, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, -1, 3, 0, 1, 2, 3, 4, 0},
      {3, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, -1, 1, 0, 2, 1, 0},
      {4, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, -1, 2, 0, 1, 2, 3, 1},
      {6, -1, 1, 2, -1, 0, -1, 3, -1, -1, 3, 4, 0, 2, -1, -1, 1, -1, -1, 5, 2, -1, -1, -1, 4, 0, 4, 3, 5, 1, 2, 1},
      {6, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, 5, 3, -1, -1, -1, 4, 0, 1, 2, 3, 5, 4, 2},
      {
            12,
            -1, 1, 8, -1, 0, 2, 9, -1, 1, 3, 10, -1, 2, 4, 11, -1, 3, 5, -1, -1, 4, 6, -1, -1, 5, 7, -1, -1, 6, -1, -1, -1,
            -1, 9, -1, 0, 8, 10, -1, 1, 9, 11, -1, 2, 10, -1, -1, 3,
            0, 1, 2, 3, 0, 4, 1, 2, 5, 3, 4, 5,
            1
      },
      {1, -1, -1, -1, -1, 0, 2}
   };

   public LevelMap(Game var1) {
      this.game = var1;
      tileWidth = Game.tileWidth;
      tileHeight = Game.tileHeight;
      tileAnimCounter = 0;
      subGridNeighbors = new byte[12][4];
      subGridIndexMap = new byte[12];
      subGrids = new byte[12][28][18];
      columnSolidMasks = new int[28];
      this.tiles = new byte[28][18];
   }

   public final short getTile(int var1, int var2) {
      return var1 >= 0 && var1 < 28 && var2 >= 0 && var2 < 18 ? subGrids[subGridIndexMap[this.game.X]][var1][var2] : -1;
   }

   // Parses LEVEL_ROOM_TABLES[level] into subGridCount/subGridNeighbors/
   // subGridIndexMap/startSubGrid; currentSubGrid always resets to 0.
   public final void loadRoomTable(int var1) {
      byte[] var2 = LEVEL_ROOM_TABLES[var1];
      int var5 = 0;
      var5++;
      subGridCount = var2[0];

      for (int var3 = 0; var3 < subGridCount; var3++) {
         for (int var4 = 0; var4 < 4; var4++) {
            subGridNeighbors[var3][var4] = var2[var5++];
         }
      }

      for (int var6 = 0; var6 < subGridCount; var6++) {
         subGridIndexMap[var6] = var2[var5++];
      }

      currentSubGrid = 0;
      startSubGrid = var2[var5];
   }

   // Loads `/level<N>.bin`: subGridCount consecutive 28x18 grids (one per
   // room), each byte offset by -32 like the legacy build's `/n<level>`
   // format (raw byte - 0x20 = tile id).
   public final void loadLevelFile(int var1) {
      this.loadRoomTable(var1);
      this.game.x();

      try {
         java.io.InputStream var2 = this.getClass().getResourceAsStream("/level" + var1 + ".bin");

         for (int var3 = 0; var3 < subGridCount; var3++) {
            for (int var4 = 0; var4 < 28; var4++) {
               for (int var5 = 0; var5 < 18; var5++) {
                  int var6 = var2.read() - 32;
                  subGrids[var3][var4][var5] = (byte)var6;
               }
            }
         }

         var2.close();
      } catch (Exception var7) {
      }
   }

   // Activates room `var1` as the current tiles[] grid: resets a large
   // block of Game's per-room state (enemy pool, projectiles, moving
   // platforms, pickups, zip lines, exit tile, infolink state -- all
   // still-obfuscated Game members, see the file header), then walks
   // every cell of the room translating raw tile ids into render state
   // (tiles[]) and spawning enemies/platforms/pickups/zip-lines/the exit
   // via Game's own (obfuscated) spawn helpers. var2 selects whether the
   // player-start tile (-125) actually repositions the player (true) or
   // is ignored (false, e.g. re-entering a room already in progress).
   public final void enterRoom(int var1, boolean var2) {
      int var3 = 0;
      int var4 = 0;
      boolean var7 = false;
      boolean var8 = false;
      boolean var9 = false;
      this.game.X = (short)var1;
      this.game.d(this.game.Z, var1);

      for (int var10 = 3; var10 >= 0; var10--) {
         this.game.cq[var10] = -1;
      }

      for (int var14 = 49; var14 >= 0; var14--) {
         this.game.bq[var14] = this.game.br[var14] = -1;
         this.game.bo[var14] = 0;
         this.game.bs[var14] = false;
         this.game.bn[var14] = -1;
      }

      for (int var15 = Game.enemyPoolSize - 1; var15 >= 0; var15--) {
         this.game.enemies[var15].kind = -1;
      }

      this.game.bP = -1;
      this.game.bQ = -1;

      for (int var16 = 3; var16 >= 0; var16--) {
         this.game.bc[var16] = -1;
         this.game.be[var16] = -1;
      }

      for (int var17 = 11; var17 >= 0; var17--) {
         this.game.ck[var17] = -1;
      }

      for (int var18 = 2; var18 >= 0; var18--) {
         this.game.bC[var18] = 0;
         this.game.bF[var18] = -1;
      }

      for (int var19 = 9; var19 >= 0; var19--) {
         this.game.enemyProjectiles[var19].type = -1;
      }

      for (int var20 = 9; var20 >= 0; var20--) {
         this.game.playerProjectiles[var20].type = -1;
      }

      this.game.cc = -1;
      this.game.cd = -1;
      this.game.ca = -1;
      this.game.cb = -1;
      this.game.bX = -1;
      this.game.bY = -1;
      this.game.cT = true;
      if (subGridNeighbors[var1][1] != -1) {
         this.game.cT = false;
      }

      for (int var21 = 7; var21 >= 0; var21--) {
         this.game.bj[var21] = -1;
      }

      for (int var22 = 27; var22 >= 0; var22--) {
         for (int var11 = 17; var11 >= 0; var11--) {
            short var5 = this.getTile(var22, var11);
            if (var7) {
               this.tiles[var22][var11 + 1] = (byte)var5;
            } else if (var8) {
               if (var5 % 2 == 0 && var5 < 12) {
                  this.tiles[var22][var11 + 1] = (byte)(var5 + 1);
               } else {
                  this.tiles[var22][var11 + 1] = (byte)var5;
               }
            }

            if (var9) {
               short var6 = var5;
               if (this.tiles[var22][var11 + 2] >= 19) {
                  for (int var23 = 1; var6 >= 47 && var6 <= 55; var23++) {
                     var6 = this.getTile(var22, var11 - var23);
                  }

                  if (var6 % 2 == 0 && var6 < 12) {
                     this.tiles[var22][var11 + 1] = (byte)(var6 + 1);
                  } else {
                     this.tiles[var22][var11 + 1] = (byte)var6;
                  }
               } else if (var6 >= 47 && var6 <= 55) {
                  for (int var12 = 1; var6 >= 47 && var6 <= 55; var12++) {
                     var6 = this.getTile(var22, var11 - var12);
                  }

                  this.tiles[var22][var11 + 1] = (byte)var6;
               }
            }

            var7 = false;
            var8 = false;
            var9 = false;
            if (var5 >= 47 && var5 <= 55) {
               var7 = true;
               var9 = true;
               switch (var5) {
                  case 47:
                     this.game.a(var22, var11, 0, 0, var3);
                     break;
                  case 48:
                     this.game.a(var22, var11, 2, 0, var3);
                     break;
                  case 49:
                     this.game.a(var22, var11, 1, 0, var3);
                     break;
                  case 50:
                     this.game.a(var22, var11, 0, 0, var3++);
                     this.game.a(var22, var11, 0, 1, var3);
                     break;
                  case 51:
                     this.game.a(var22, var11, 0, 0, var3++);
                     this.game.a(var22, var11, 2, 1, var3);
                     break;
                  case 52:
                     this.game.a(var22, var11, 0, 0, var3++);
                     this.game.a(var22, var11, 1, 1, var3);
                     break;
                  case 53:
                     this.game.a(var22, var11, 0, 2, var3);
                     break;
                  case 54:
                     this.game.a(var22, var11, 2, 2, var3);
                     break;
                  case 55:
                     this.game.a(var22, var11, 1, 2, var3);
               }

               var3++;
            } else if (var5 == -97 || var5 == -96 || var5 == -95) {
               this.tiles[var22][var11] = 1;
            } else if (var5 == -98) {
               this.tiles[var22][var11] = 0;
               this.game.a(var22, var11);
            } else if (var5 == 35) {
               if (this.game.f(this.game.Z, this.game.X)) {
                  this.game.bP = (byte)var22;
                  this.game.bQ = (byte)var11;
               }

               var7 = true;
            } else if (var5 >= -118 && var5 <= -99) {
               var8 = true;
            } else if (var5 == -93) {
               if (this.game.h(11)) {
                  this.game.ca = (byte)var22;
                  this.game.cb = (byte)var11;
               }

               var8 = true;
            } else if (var5 == -94) {
               this.game.cc = (byte)var22;
               this.game.cd = (byte)var11;
               var8 = true;
            } else if (var5 == -92) {
               if (this.game.bZ) {
                  this.game.bX = (byte)var22;
                  this.game.bY = (byte)var11;
               }

               var8 = true;
            } else if (var5 == 99) {
               var8 = true;
            } else if (var5 == 100) {
               this.tiles[var22][var11] = this.tiles[var22 + 1][var11];
            } else if (var5 == 56) {
               this.tiles[var22][var11] = 27;
            } else if (var5 == 57) {
               this.tiles[var22][var11] = 22;
            } else if (var5 == 58) {
               this.tiles[var22][var11] = 20;
            } else if (var5 == 59) {
               this.tiles[var22][var11] = 21;
            } else if (var5 == 60) {
               this.tiles[var22][var11] = 22;
            } else if (var5 == 61) {
               this.tiles[var22][var11] = 22;
            } else if (var5 == -125) {
               if (var2) {
                  this.game.ah = (byte)var1;
                  this.game.ad = (byte)var22;
                  this.game.ae = (byte)(var11 - 1);
                  if (var22 < 1) {
                     this.game.af = 0;
                  } else {
                     this.game.af = (short)(-tileWidth * (var22 - 1));
                  }

                  if (var11 < 6) {
                     this.game.ag = 0;
                  } else {
                     this.game.ag = (short)(-tileHeight * (var11 - 5));
                  }
               }

               this.tiles[var22][var11] = 22;
            } else if (var5 == -127) {
               this.tiles[var22][var11] = 0;
            } else if (var5 == -126) {
               this.tiles[var22][var11] = 1;
            } else if (var5 >= 62 && var5 <= 69) {
               var7 = true;
               int var25 = var5 - 62;
               this.game.a(var22, var11, var25 % 2 == 0, var25 >> 1);
            } else if (var5 == 97 || var5 == 98) {
               var7 = true;
            } else if (var5 == -124) {
               var8 = true;
            } else if (var5 >= 43 && var5 <= 46) {
               var7 = true;
               if (this.getTile(var22, var11 + 1) >= 19) {
                  var8 = true;
                  var7 = false;
               }

               this.game.d(var22, var11, BOLT_TILE_TYPES[var5 - 43]);
            } else if (var5 >= 114 && var5 <= 127) {
               var8 = true;
               this.game.a(var22, var11, ENEMY_TILE_TYPES[var5 - 114], var4++);
            } else if (var5 == -128) {
               var8 = true;
               this.game.a(var22, var11, (byte)13, var4++);
            } else if (var5 >= 102 && var5 <= 113) {
               if ((var5 - 102) % 2 == 0) {
                  var7 = true;
               } else {
                  this.tiles[var22][var11] = this.tiles[var22 + 1][var11];
               }

               this.game.a(var22, var11, PLATFORM_TILE_TYPES[var5 - 102], var4++);
            } else if ((var5 < 70 || var5 > 76) && var5 != -119) {
               if (var5 >= 36 && var5 <= 42) {
                  var8 = true;
                  this.game.a((byte)var22, (byte)var11);
               } else if (var5 >= 77 && var5 <= 96) {
                  var8 = true;
               } else if (var5 == 101) {
                  var7 = true;
                  int var24 = var22 * tileWidth + (tileWidth >> 1);
                  int var13 = (var11 + 1) * tileHeight + (tileHeight >> 1);
                  this.game.a(var24 << 8, var13 << 8, 32, true, true, 0, (byte)26);
               } else if (var5 > 34) {
                  var7 = true;
               } else {
                  this.tiles[var22][var11] = (byte)var5;
               }
            } else {
               this.tiles[var22][var11] = 1;
               this.game.a((byte)var22, (byte)var11, var5);
            }
         }
      }

      this.computeColumnSolidMasks();
      this.game.Q();
      if (this.game.cV) {
         this.game.bt[0] = false;
      } else {
         this.game.bt[this.game.X] = false;
      }
   }

   // Bit per solid row (tiles[][] value in [19,34]) per column, same
   // role as the legacy build's columnSolidMasks (there: tiles 4-13).
   public final void computeColumnSolidMasks() {
      for (byte var2 = 27; var2 >= 0; var2--) {
         columnSolidMasks[var2] = 0;

         for (byte var3 = 0; var3 < 18; var3++) {
            short var1;
            if ((var1 = this.tiles[var2][var3]) >= 19 && var1 <= 27 || var1 >= 19 && var1 <= 34) {
               columnSolidMasks[var2] = columnSolidMasks[var2] | 1 << var3;
            }
         }
      }
   }

   public final void render(Graphics var1) {
      int var2 = Game.x;
      int var3 = Game.y;
      byte var4 = (byte)(-var2 / tileWidth);
      byte var5 = (byte)(-var3 / tileHeight);
      byte var6 = var4;
      byte var7 = var5;
      int var8 = var4 * tileWidth + var2;
      int var9 = var5 * tileHeight + var3;
      int var14 = var8;
      int var15 = var9;
      var6 = (byte)(var6 + 176 / tileWidth + 2);
      var7 = (byte)(var7 + 220 / tileHeight + 2);
      if (var6 > 28) {
         var6 = 28;
      }

      if (var7 > 18) {
         var7 = 18;
      }

      if (++tileAnimCounter >= 12) {
         tileAnimCounter = 0;
      }

      try {
         for (byte var10 = var4; var10 < var6; var10++) {
            for (byte var11 = var5; var11 < var7; var11++) {
               if (var14 < 176 && var15 < 220) {
                  short var16;
                  if ((var16 = this.tiles[var10][var11]) <= 34) {
                     int var18 = var15;
                     boolean var19 = false;
                     if (0 < tileHeight) {
                        if (tileAnimCounter >> 1 > 2) {
                           if (var16 == 15 || var16 == 17) {
                              var16++;
                           } else if (var16 == 16 || var16 == 18) {
                              var16--;
                           }
                        }

                        if (this.game.aQ != 0 && var16 == 26) {
                           if (tileAnimCounter < 6) {
                              var16 = 13;
                           } else {
                              var16 = 14;
                           }
                        }

                        this.game.b(var1, var14, var18, tileWidth, tileHeight - 0);
                        var1.drawImage(Game.aF, var14, var18 - var16 * tileHeight - 0, 0);
                     }
                  } else {
                     var1.setColor(0);
                     var1.fillRect(var14, var15, tileWidth, tileHeight);
                  }

                  short var17;
                  if ((var17 = this.getTile(var10, var11)) > -127 && var17 <= -125) {
                     int var24 = Game.aK.getWidth();
                     byte var25 = tileHeight;
                     int var29 = var15;
                     int var30 = var17 - -126;
                     if (tileAnimCounter >> 1 > 2) {
                        var30 += 2;
                     }

                     if (0 < var25) {
                        this.game.b(var1, var14 + (tileWidth - var24 >> 1), var29, var24, var25 - 0);
                        var1.drawImage(Game.aK, var14 + (tileWidth - var24 >> 1), var29 - 0 - var25 * var30, 0);
                     }
                  } else if (var17 == 97 || var17 == 98) {
                     int var28 = var15 + (tileHeight - 19 >> 1);
                     this.game.b(var1, var14 + (tileWidth - 19 >> 1), var28, 19, 19);
                     var1.drawImage(Game.aL, var14 + (tileWidth - 19 >> 1), var28 - 0 - 209, 0);
                  } else if (var17 == 35 && this.game.f(this.game.Z, this.game.X)) {
                     int var27 = var15;
                     this.game.b(var1, var14 + (tileWidth - 19 >> 1), var27, 19, 19);
                     var1.drawImage(Game.aL, var14 + (tileWidth - 19 >> 1), var27 - 0 - 76, 0);
                  } else if (var17 >= 77 && var17 <= 96) {
                     int var12 = Game.aJ.getWidth();
                     int var13 = Game.aJ.getHeight() >> 3;
                     int var26 = var15 + tileHeight - var13;
                     int var20 = this.game.h(var17 - 77) ? 3 * var13 : 4 * var13;
                     if (var17 >= 81 && var17 <= 87 && var17 != 85) {
                        var20 = this.game.h(var17 - 77) ? 7 * var13 : 8 * var13;
                     }

                     if (var17 == 80) {
                        var20 += 2 * var13;
                     }

                     if (0 < var13) {
                        this.game.b(var1, var14 + (tileWidth - var12 >> 1), var26, var12, var13 - 0);
                        var1.drawImage(Game.aJ, var14 + (tileWidth - var12 >> 1), var26 - 0 - var20, 0);
                     }
                  }

                  var15 += tileHeight;
               } else {
                  var15 += tileHeight;
               }
            }

            var15 = var9;
            var14 += tileWidth;
         }
      } catch (Exception var21) {
      }
   }

   // Walkable/non-solid check: false if either the tile at (col,row) or
   // (col,row+1) is a bitmask-gated solid (70-76, gated by Game.bw) or a
   // one-way-platform tile (-119) with Game.bw bit 7 set; otherwise true
   // iff the raw tile id is in [0,19].
   public final boolean isWalkable(int var1, int var2) {
      if (var1 >= 0 && var1 < 28 && var2 >= 0 && var2 < 18) {
         byte var3 = this.tiles[var1][var2];
         short var4 = this.getTile(var1, var2);
         short var5 = this.getTile(var1, var2 + 1);
         int var6 = var4 - 70;
         int var7 = var5 - 70;
         var6 = 1 << var6;
         var7 = 1 << var7;
         if ((var4 < 70 || var4 > 76 || (this.game.bw & var6) == 0) && (var5 < 70 || var5 > 76 || (this.game.bw & var7) == 0)) {
            return (var4 == -119 || var5 == -119) && (this.game.bw & 128) != 0 ? false : var3 >= 0 && var3 <= 19;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
