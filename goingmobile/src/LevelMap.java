import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class LevelMap {
   public static final byte[] a = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
   public static final byte[] b = new byte[]{0, 1, 2, 3, 4, 0, 1, 2, 3, 4, 0, 1, 2};
   public Game game;
   public static byte[][][] levelGrids;
   public static int[] columnSolidMasks;
   public byte[][] tiles;
   public static byte gridCount;
   public static final byte[] PLATFORM_TILE_TYPES = new byte[]{15, 15, 16, 17, 18, 18, 19, 20, 21, 21, 22, 23};
   public static final byte[] ENEMY_TILE_TYPES = new byte[]{3, 4, 5, 0, 1, 2, 6, 7, 7, 9, 10, 10, 12, 13};
   public static final byte[] BOLT_TILE_TYPES = new byte[]{0, 2, 1, 3};

   public LevelMap(Game var1) {
      this.game = var1;
      levelGrids = new byte[12][28][18];
      columnSolidMasks = new int[28];
      this.tiles = new byte[28][18];
   }

   public final short a(int var1, int var2) {
      if (var1 >= 0 && var1 < 28 && var2 >= 0 && var2 < 18) {
         boolean var3 = false;
         byte[][][] var10000;
         byte var10001;
         if (this.game.Q == 11) {
            var10000 = levelGrids;
            var10001 = b[this.game.P];
         } else {
            var10000 = levelGrids;
            var10001 = 0;
         }

         return var10000[var10001][var1][var2];
      } else {
         return -1;
      }
   }

   public final void a(int var1) {
      gridCount = 1;
      if (this.game.Q == 11) {
         gridCount = 12;
      }

      try {
         InputStream var2 = this.getClass().getResourceAsStream("/n" + var1);

         for (int var3 = 0; var3 < gridCount; var3++) {
            for (int var4 = 0; var4 < 28; var4++) {
               for (int var5 = 0; var5 < 18; var5++) {
                  int var6 = var2.read() - 32;
                  levelGrids[var3][var4][var5] = (byte)var6;
               }
            }
         }
      } catch (IOException var7) {
      }
   }

   public final void a(int var1, boolean var2) {
      int var3 = 0;
      int var4 = 0;
      boolean var7 = false;
      boolean var8 = false;
      boolean var9 = false;
      this.game.P = (short)var1;

      for (int var10 = 3; var10 >= 0; var10--) {
         this.game.platformState[var10] = -1;
      }

      for (int var13 = 19; var13 >= 0; var13--) {
         this.game.titaniumBoltType[var13] = -1;
      }

      for (int var14 = Game.MAX_ENEMIES - 1; var14 >= 0; var14--) {
         this.game.enemies[var14].kind = -1;
      }

      this.game.exitTileX = -1;
      this.game.exitTileY = -1;

      for (int var15 = 3; var15 >= 0; var15--) {
         this.game.zipX1[var15] = -1;
         this.game.zipX2[var15] = -1;
      }

      for (int var16 = 5; var16 >= 0; var16--) {
         this.game.pickupType[var16] = -1;
      }

      for (int var17 = 9; var17 >= 0; var17--) {
         this.game.enemyShots[var17].type = -1;
      }

      for (int var18 = 9; var18 >= 0; var18--) {
         this.game.playerShots[var18].type = -1;
      }

      this.game.bz = true;

      for (int var19 = 27; var19 >= 0; var19--) {
         for (int var11 = 17; var11 >= 0; var11--) {
            short var5;
            label192: {
               byte[] var10000;
               int var10001;
               int var10002;
               label191: {
                  var5 = this.a(var19, var11);
                  if (!var7) {
                     if (!var8) {
                        break label192;
                     }

                     if (var5 % 2 == 0 && var5 < 12) {
                        var10000 = this.tiles[var19];
                        var10001 = var11 + 1;
                        var10002 = var5 + 1;
                        break label191;
                     }
                  }

                  var10000 = this.tiles[var19];
                  var10001 = var11 + 1;
                  var10002 = var5;
               }

               var10000[var10001] = (byte)var10002;
            }

            label199:
            if (var9) {
               byte[] var23;
               int var28;
               int var33;
               label222: {
                  short var6 = var5;
                  LevelMap var22;
                  if (this.tiles[var19][var11 + 2] >= 4) {
                     for (int var12 = 1; var6 >= 47 && var6 <= 55; var12++) {
                        var6 = this.a(var19, var11 - var12);
                     }

                     if (var6 % 2 == 0 && var6 < 12) {
                        var23 = this.tiles[var19];
                        var28 = var11 + 1;
                        var33 = var6 + 1;
                        break label222;
                     }

                     var22 = this;
                  } else {
                     if (var6 < 47 || var6 > 55) {
                        break label199;
                     }

                     for (int var20 = 1; var6 >= 47 && var6 <= 55; var20++) {
                        var6 = this.a(var19, var11 - var20);
                     }

                     var22 = this;
                  }

                  var23 = var22.tiles[var19];
                  var28 = var11 + 1;
                  var33 = var6;
               }

               var23[var28] = (byte)var33;
            }

            var7 = false;
            var8 = false;
            var9 = false;
            if (var5 >= 47 && var5 <= 55) {
               label305: {
                  Game var27;
                  int var32;
                  int var35;
                  byte var37;
                  byte var40;
                  label304: {
                     var7 = true;
                     var9 = true;
                     switch (var5) {
                        case 47:
                           var27 = this.game;
                           var32 = var19;
                           var35 = var11;
                           var37 = 0;
                           var40 = 0;
                           break label304;
                        case 48:
                           var27 = this.game;
                           var32 = var19;
                           var35 = var11;
                           var37 = 2;
                           var40 = 0;
                           break label304;
                        case 49:
                           var27 = this.game;
                           var32 = var19;
                           var35 = var11;
                           var37 = 1;
                           var40 = 0;
                           break label304;
                        case 50:
                           this.game.a(var19, var11, 0, 0, var3++);
                           var27 = this.game;
                           var32 = var19;
                           var35 = var11;
                           var37 = 0;
                           var40 = 1;
                           break label304;
                        case 51:
                           this.game.a(var19, var11, 0, 0, var3++);
                           var27 = this.game;
                           var32 = var19;
                           var35 = var11;
                           var37 = 2;
                           var40 = 1;
                           break label304;
                        case 52:
                           this.game.a(var19, var11, 0, 0, var3++);
                           var27 = this.game;
                           var32 = var19;
                           var35 = var11;
                           var37 = 1;
                           var40 = 1;
                           break label304;
                        case 53:
                           var27 = this.game;
                           var32 = var19;
                           var35 = var11;
                           var37 = 0;
                           break;
                        case 54:
                           var27 = this.game;
                           var32 = var19;
                           var35 = var11;
                           var37 = 2;
                           break;
                        case 55:
                           var27 = this.game;
                           var32 = var19;
                           var35 = var11;
                           var37 = 1;
                           break;
                        default:
                           break label305;
                     }

                     var40 = 2;
                  }

                  var27.a(var32, var35, var37, var40, var3);
               }

               var3++;
            } else {
               byte[] var24;
               int var29;
               byte var34;
               if (var5 != -97 && var5 != -96 && var5 != -95) {
                  if (var5 == 35) {
                     if (this.game.a(this.game.Q, this.game.P)) {
                        this.game.exitTileX = (byte)var19;
                        this.game.exitTileY = (byte)var11;
                     }

                     var7 = true;
                     continue;
                  }

                  if (var5 >= -118 && var5 <= -99) {
                     var8 = true;
                     continue;
                  }

                  if (var5 == -93) {
                     var8 = true;
                     continue;
                  }

                  if (var5 == -94) {
                     var8 = true;
                     continue;
                  }

                  if (var5 == -92) {
                     var8 = true;
                     continue;
                  }

                  if (var5 == 99) {
                     var8 = true;
                     continue;
                  }

                  if (var5 == 100) {
                     var24 = this.tiles[var19];
                     var29 = var11;
                     var34 = this.tiles[var19 + 1][var11];
                  } else if (var5 == 56) {
                     var24 = this.tiles[var19];
                     var29 = var11;
                     var34 = 9;
                  } else if (var5 == 57) {
                     var24 = this.tiles[var19];
                     var29 = var11;
                     var34 = 7;
                  } else if (var5 == 58) {
                     var24 = this.tiles[var19];
                     var29 = var11;
                     var34 = 5;
                  } else if (var5 == 59) {
                     var24 = this.tiles[var19];
                     var29 = var11;
                     var34 = 6;
                  } else if (var5 == 60) {
                     var24 = this.tiles[var19];
                     var29 = var11;
                     var34 = 7;
                  } else if (var5 == 61) {
                     var24 = this.tiles[var19];
                     var29 = var11;
                     var34 = 7;
                  } else if (var5 == -125) {
                     if (var2) {
                        this.game.V = (byte)var1;
                        this.game.R = (byte)var19;
                        this.game.S = (byte)(var11 - 1);
                        Game var25;
                        short var30;
                        if (var19 < 1) {
                           var25 = this.game;
                           var30 = 0;
                        } else {
                           var25 = this.game;
                           var30 = (short)(-22 * (var19 - 1));
                        }

                        var25.T = var30;
                        Game var26;
                        if (var11 < 6) {
                           var26 = this.game;
                           var30 = 0;
                        } else {
                           var26 = this.game;
                           var30 = (short)(-14 * (var11 - 5));
                        }

                        var26.U = var30;
                     }

                     var24 = this.tiles[var19];
                     var29 = var11;
                     var34 = 7;
                  } else if (var5 == -122) {
                     var24 = this.tiles[var19];
                     var29 = var11;
                     var34 = 7;
                  } else if (var5 == -127) {
                     var24 = this.tiles[var19];
                     var29 = var11;
                     var34 = 0;
                  } else if (var5 == -126) {
                     var24 = this.tiles[var19];
                     var29 = var11;
                     var34 = 1;
                  } else {
                     if (var5 >= 62 && var5 <= 69) {
                        var7 = true;
                        int var21 = var5 - 62;
                        this.game.a(var19, var11, var21 % 2 == 0, var21 >> 1);
                        continue;
                     }

                     if (var5 == 97 || var5 == 98) {
                        var7 = true;
                        continue;
                     }

                     if (var5 == -124) {
                        var8 = true;
                        continue;
                     }

                     if (var5 >= 43 && var5 <= 46) {
                        var7 = true;
                        if (this.a(var19, var11 + 1) >= 4) {
                           var8 = true;
                           var7 = false;
                        }

                        this.game.c(var19, var11, BOLT_TILE_TYPES[var5 - 43]);
                        continue;
                     }

                     if (var5 >= 114 && var5 <= 127) {
                        var8 = true;
                        this.game.a(var19, var11, ENEMY_TILE_TYPES[var5 - 114], var4++);
                        continue;
                     }

                     if (var5 == -128) {
                        var8 = true;
                        this.game.a(var19, var11, (byte)13, var4++);
                        continue;
                     }

                     if (var5 >= 102 && var5 <= 113) {
                        if ((var5 - 102) % 2 == 0) {
                           var7 = true;
                        } else {
                           this.tiles[var19][var11] = this.tiles[var19 + 1][var11];
                        }

                        this.game.a(var19, var11, PLATFORM_TILE_TYPES[var5 - 102], var4++);
                        continue;
                     }

                     if ((var5 < 70 || var5 > 76) && var5 != -119) {
                        if (var5 >= 36 && var5 <= 42) {
                           var8 = true;
                           continue;
                        }

                        if (var5 >= 77 && var5 <= 96) {
                           var8 = true;
                           continue;
                        }

                        if (var5 == 101) {
                           var7 = true;
                           continue;
                        }

                        if (var5 > 13) {
                           var7 = true;
                           continue;
                        }

                        var24 = this.tiles[var19];
                        var29 = var11;
                        var34 = (byte)var5;
                     } else {
                        var24 = this.tiles[var19];
                        var29 = var11;
                        var34 = 1;
                     }
                  }
               } else {
                  var24 = this.tiles[var19];
                  var29 = var11;
                  var34 = 1;
               }

               var24[var29] = var34;
            }
         }
      }

      this.a();
   }

   private void a() {
      byte var10000 = 27;

      while (true) {
         byte var2 = var10000;
         if (var10000 < 0) {
            return;
         }

         columnSolidMasks[var2] = 0;
         var10000 = 0;

         while (true) {
            byte var3 = var10000;
            if (var10000 >= 18) {
               var10000 = (byte)(var2 - 1);
               break;
            }

            short var1;
            if ((var1 = this.tiles[var2][var3]) >= 4 && var1 <= 9 || var1 >= 4 && var1 <= 13) {
               columnSolidMasks[var2] = columnSolidMasks[var2] | 1 << var3;
            }

            var10000 = (byte)(var3 + 1);
         }
      }
   }

   public final void a(Graphics var1) {
      int var2 = Game.r;
      int var3 = Game.s;
      byte var4 = (byte)(-var2 / 22);
      byte var5 = (byte)(-var3 / 14);
      byte var6 = var4;
      byte var7 = var5;
      int var8 = var4 * 22 + var2;
      int var9 = var5 * 14 + var3;
      int var14 = var8;
      int var15 = var9;
      var6 = (byte)(var6 + 7);
      var7 = (byte)(var7 + 11);
      if (var6 > 28) {
         var6 = 28;
      }

      if (var7 > 18) {
         var7 = 18;
      }

      byte var10000 = var4;

      while (true) {
         byte var10 = var10000;
         if (var10000 >= var6) {
            return;
         }

         var10000 = var5;

         while (true) {
            byte var11 = var10000;
            if (var10000 >= var7) {
               var15 = var9;
               var14 += 22;
               var10000 = (byte)(var10 + 1);
               break;
            }

            short var16;
            if ((var16 = this.tiles[var10][var11]) <= 13) {
               int var18 = var15;
               int var19 = 0;
               if (var15 < 20) {
                  var18 = 20;
                  var19 = 20 - var15;
               }

               if (var19 < 14 && var19 >= 0) {
                  var1.setClip(var14, var18, 22, 14 - var19);
                  var1.drawImage(Game.tileSetImage, var14, var18 - var16 * 14 - var19, 0);
               }
            }

            label137: {
               short var17;
               Graphics var32;
               Image var10001;
               int var10002;
               int var10003;
               int var10004;
               if (((var17 = this.a(var10, var11)) < -127 || var17 > -125) && var17 != -122) {
                  if (var17 == 97 || var17 == 98) {
                     int var26 = var15 + 3;
                     int var30 = 0;
                     if (var26 < 20) {
                        var30 = 20 - var26;
                        var26 = 20;
                     }

                     if (var30 >= 8 || var30 < 0) {
                        break label137;
                     }

                     var1.setClip(var14 + 7, var26, 8, 8 - var30);
                     var32 = var1;
                     var10001 = Game.smallSpriteImage;
                     var10002 = var14 + 7;
                     var10003 = var26 - var30;
                     var10004 = 56;
                  } else if (var17 == 35 && this.game.a(this.game.Q, this.game.P)) {
                     int var29 = 0;
                     int var25 = var15;
                     if (var15 < 20) {
                        var29 = 20 - var25;
                        var25 = 20;
                     }

                     if (var29 >= 8 || var29 < 0) {
                        break label137;
                     }

                     var1.setClip(var14 + 7, var25, 8, 8 - var29);
                     var32 = var1;
                     var10001 = Game.smallSpriteImage;
                     var10002 = var14 + 7;
                     var10003 = var25 - var29;
                     var10004 = 32;
                  } else {
                     if (var17 < 77 || var17 > 96) {
                        break label137;
                     }

                     int var28 = 0;
                     int var24;
                     if ((var24 = var15 + 14 - 16) < 20) {
                        var28 = 20 - var24;
                        var24 = 20;
                     }

                     if (var28 >= 16 || var28 < 0) {
                        break label137;
                     }

                     var1.setClip(var14 + 3, var24, 16, 16 - var28);
                     var32 = var1;
                     var10001 = Game.titaniumBoltImage;
                     var10002 = var14 + 3;
                     var10003 = var24 - var28;
                     var10004 = 48;
                  }
               } else {
                  if (var17 == -122) {
                     var17 = -125;
                  }

                  int var27 = 0;
                  byte var13 = 14;
                  int var23 = var15;
                  int var20;
                  if ((var20 = var17 - -127) == 2) {
                     var13 = 6;
                  }

                  if (var23 < 20) {
                     var27 = 20 - var23;
                     var23 = 20;
                  }

                  if (var27 >= var13 || var27 < 0) {
                     break label137;
                  }

                  var1.setClip(var14 + 0, var23, 22, var13 - var27);
                  var32 = var1;
                  var10001 = Game.actorImage;
                  var10002 = var14 + 0;
                  var10003 = var23 - var27;
                  var10004 = 14 * var20;
               }

               var32.drawImage(var10001, var10002, var10003 - var10004, 0);
            }

            var15 += 14;
            var10000 = (byte)(var11 + 1);
         }
      }
   }

   public final boolean b(int var1, int var2) {
      byte var3;
      return var1 >= 0 && var1 < 28 && var2 >= 0 && var2 < 18 ? (var3 = this.tiles[var1][var2]) >= 0 && var3 <= 4 : false;
   }

   static {
      byte[] var10000 = new byte[]{0, 2, 0, 1, 0, 0, 1, 1, 2, 2, 0, 2, 0};
   }
}
