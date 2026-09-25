import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;

// $VF: renamed from: c
public final class Enemy {
   // $VF: renamed from: a int
   public int type;
   // $VF: renamed from: b int
   public int animSetId;
   // $VF: renamed from: c int
   public int row;
   // $VF: renamed from: d int
   public int worldX;
   // $VF: renamed from: e int
   public int worldY;
   // $VF: renamed from: f int
   public int velY;
   // $VF: renamed from: g int
   public int velX;
   // $VF: renamed from: h int
   public int col;
   public int i;
   public int j;
   // $VF: renamed from: k int
   public int drawFlags;
   // $VF: renamed from: l int
   public int health;
   // $VF: renamed from: m int
   public int slot;
   // $VF: renamed from: n int
   public int state;
   // $VF: renamed from: o int
   public int animPhase;
   // $VF: renamed from: p int
   public int hurtTimer;
   // $VF: renamed from: q boolean
   public boolean facingRight;
   // $VF: renamed from: r int
   public int mount;
   // $VF: renamed from: s int
   public int tileBelow;
   // $VF: renamed from: t byte[]
   public static byte[] bodyOffsetX;
   // $VF: renamed from: u byte[]
   public static byte[] bodyOffsetY;
   // $VF: renamed from: v byte[]
   public static byte[] bodyWidth;
   // $VF: renamed from: w byte[]
   public static byte[] bodyHeight;
   // $VF: renamed from: x byte[]
   public static byte[] attackOffsetX;
   // $VF: renamed from: y byte[]
   public static byte[] attackOffsetY;
   // $VF: renamed from: z byte[]
   public static byte[] attackWidth;
   // $VF: renamed from: A byte[]
   public static byte[] attackHeight;
   public boolean B = false;
   // $VF: renamed from: C byte[]
   public static byte[] maxHealth;
   // $VF: renamed from: D byte[]
   public static byte[] attackDamage;
   // $VF: renamed from: E byte[]
   public static byte[] boltDrop;
   // $VF: renamed from: F byte[]
   public static byte[] burstCount;
   // $VF: renamed from: G int[]
   public static final int[] animSequenceMap = new int[]{
      0,
      1,
      2,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      1,
      2,
      3,
      4,
      0,
      0,
      0,
      0,
      0,
      0,
      1,
      2,
      3,
      4,
      4,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      1,
      1,
      1,
      1,
      1,
      1,
      1,
      1,
      1,
      1,
      0,
      2,
      0,
      1,
      0,
      0,
      0,
      0,
      0,
      0,
      2,
      5,
      0,
      3,
      1,
      4,
      7,
      6,
      8,
      9,
      0,
      0,
      0,
      1,
      0,
      0,
      0,
      0,
      0,
      0
   };
   // $VF: renamed from: H short[]
   public static short[] moveSpeed = new short[]{1024, 1344, 704, 0, 160, 704, 704, 704};
   // $VF: renamed from: I byte[]
   public static byte[] attackCooldown;
   // $VF: renamed from: J byte[]
   public static byte[] drawOffsets;
   // $VF: renamed from: K boolean
   public boolean vertical;
   // $VF: renamed from: L int
   public int spawnCode;
   // $VF: renamed from: M b
   public static Game game;
   // $VF: renamed from: N int
   public int knockback;
   // $VF: renamed from: O boolean
   public boolean blockedByWall;
   // $VF: renamed from: P boolean
   public boolean dropAhead;
   // $VF: renamed from: Q boolean
   public boolean noFloorAhead;
   // $VF: renamed from: R boolean
   public boolean noFloorBeyond;
   // $VF: renamed from: S boolean
   public boolean playerLeftNear;
   // $VF: renamed from: T boolean
   public boolean playerRightNear;
   // $VF: renamed from: U int
   public int tileHere;
   public int V = 0;
   // $VF: renamed from: W int
   public int attackTimer = 0;
   // $VF: renamed from: X int
   public int attackCountdown;
   // $VF: renamed from: Y int
   public int spawnIndex;
   // $VF: renamed from: Z int
   public int engaged;
   public int aa;
   // $VF: renamed from: ab int
   public int homeX;
   // $VF: renamed from: ac boolean
   public boolean animPlaying;
   // $VF: renamed from: ad boolean
   public boolean meleeHit;
   // $VF: renamed from: ae boolean
   public boolean leaping = false;

   public Enemy(Game var1, int var2) {
      game = var1;
      this.type = -1;
      this.mount = 0;
      this.aa = 0;
      this.facingRight = true;
      this.vertical = false;
      this.slot = var2;
      this.blockedByWall = false;
      this.dropAhead = false;
      this.noFloorAhead = false;
      this.attackCountdown = 0;
   }

   // $VF: renamed from: a () void
   public static void loadStats() {
      drawOffsets = new byte[320];
      bodyOffsetX = new byte[8];
      bodyOffsetY = new byte[8];
      bodyWidth = new byte[8];
      bodyHeight = new byte[8];
      attackOffsetX = new byte[8];
      attackOffsetY = new byte[8];
      attackWidth = new byte[8];
      attackHeight = new byte[8];
      loadHitboxes(2049);
   }

   // $VF: renamed from: b () int
   public final int feetY() {
      return (this.worldY >> 8) + 38 - Game.spriteBaseHeight - -12;
   }

   // $VF: renamed from: c () int
   public final int pixelX() {
      return this.worldX >> 8;
   }

   // $VF: renamed from: f () int
   private int rightColumn() {
      return ((this.worldX >> 8) + 12) / 57;
   }

   // $VF: renamed from: g () int
   private int leftColumn() {
      return ((this.worldX >> 8) - 12) / 57;
   }

   // $VF: renamed from: a (boolean) int
   private int floorLimit(boolean var1) {
      int var4 = Game.crateW;
      boolean var5 = false;
      if (var1) {
         this.tileHere = Game.getRawTile(this.col, this.row);
      }

      this.tileBelow = Game.getRawTile(this.col, this.row + 1);

      int var3;
      for (var3 = this.row + 1; var3 < 18; var3++) {
         if ((Game.solidColumnMask[this.col] & 1 << var3) > 0) {
            var3 *= 38;
            var5 = true;
            break;
         }
      }

      if (!var5) {
         var3 = 684;
      }

      int var2 = this.worldX >> 8;

      for (int var6 = 0; var6 < 50; var6++) {
         if (Game.crateType[var6] != -1
            && Game.crateX[var6] <= var2 + 8
            && Game.crateX[var6] + var4 >= var2 - 8
            && Game.crateY[var6] < var3
            && Game.crateY[var6] > this.row * 38) {
            var3 = Game.crateY[var6];
         }
      }

      return var3;
   }

   // $VF: renamed from: h () boolean
   private boolean touchesCrate() {
      int var1 = Game.crateW;
      int var2 = Game.crateH;
      int var3 = this.worldX >> 8;
      int var4 = this.feetY();

      for (int var5 = 49; var5 >= 0; var5--) {
         if (Game.crateType[var5] >= 0
            && Math.abs(Game.crateX[var5] - var3) <= 57
            && Math.abs(Game.crateY[var5] - var4) <= 38
            && Game.rectsOverlap(Game.crateX[var5], Game.crateY[var5], var1, var2, var3 - 8, var4, 16, 37)) {
            return true;
         }
      }

      return false;
   }

   // $VF: renamed from: a (c, int, int) void
   public static void playAnim(Enemy var0, int var1, int var2) {
      game.startAnim(var0.slot, var0.animSetId, animSequenceMap[var1 * 10 + var2]);
   }

   // $VF: renamed from: i () void
   private void stepPhysics() {
      this.col = this.pixelX() / 57;
      int var10000 = this.floorLimit(true);
      int var1 = 0;
      var1 = var10000 / 38;
      this.noFloorAhead = false;
      this.noFloorBeyond = false;
      this.blockedByWall = false;
      this.dropAhead = false;
      if (this.row + 1 < var1 && this.type != 3 || this.state == 2) {
         this.velY += 256;
         this.worldY = this.worldY + this.velY;
         this.row = (this.worldY >> 8) / 38;
         if (this.row + 1 == var1 && this.velY > -1824) {
            this.state = 1;
            this.velX = 0;
            if (this.type == 2 || this.type == 0 || this.type == 1 && this.state != 2) {
               playAnim(this, this.type, 1);
            }

            this.velY = 0;
            this.worldY = this.row * 38 << 8;
         }
      }

      if (this.row - 1 >= 0 && !game.isPassable(this.col, this.row) && this.velY <= 0) {
         this.velY = -this.velY;
      }

      if (this.type != 6
         && this.type != 7
         && this.type != 5
         && Game.rectsOverlap(
            this.pixelX() - bodyOffsetX[this.type],
            this.feetY() + bodyOffsetY[this.type],
            bodyWidth[this.type],
            bodyHeight[this.type],
            Game.playerPixelX() - Game.hitboxX,
            Game.playerPixelY() + -12 + Game.hitboxY,
            Game.hitboxW,
            Game.hitboxH
         )) {
         this.velX = 0;
         if (this.velY >= 0 && this.type != 3) {
            this.velY = -(this.velY >> 1);
            if (Game.playerPixelX() < this.pixelX()) {
               this.velX = 510;
               this.facingRight = false;
            } else {
               this.velX = -510;
               this.facingRight = true;
            }
         } else {
            this.velY = -this.velY;
         }
      }

      if (this.velX != 0) {
         int var2;
         if (this.facingRight) {
            this.i = this.rightColumn();
            if ((var2 = this.leftColumn()) == this.col) {
               var2--;
            }
         } else {
            this.i = this.leftColumn();
            if ((var2 = this.rightColumn()) == this.col) {
               var2++;
            }
         }

         this.j = this.row;
         int var3 = this.worldX;
         int var4 = this.col;
         int var5;
         if ((var5 = (this.worldX >> 8) % 57) < 14 && !this.facingRight) {
            this.col--;
         } else if (var5 > 43 && this.facingRight) {
            this.col++;
         }

         this.worldX = this.worldX + (this.facingRight ? 7296 : -7296);
         var10000 = this.floorLimit(false);
         int var6 = 0;
         var6 = var10000 / 38;
         this.dropAhead = var6 > var1;
         if (var6 != 27 && this.knockback != 0 && (this.dropAhead || this.noFloorAhead || this.noFloorBeyond)) {
            this.velX = 0;
         }

         this.col = var4;
         this.worldX = var3;
         if (this.i > 0 && this.i < 27 && game.isPassable(this.i, this.j)) {
            if (!game.isPassable(this.col, this.j)) {
               if (this.pixelX() % 57 < 28) {
                  this.worldX -= 3648;
               } else {
                  this.worldX += 3648;
               }
            }

            this.worldX = this.worldX + this.velX;
            if (this.touchesCrate()) {
               this.worldX = this.worldX - (this.velX << 1);
               this.blockedByWall = true;
            }
         } else {
            this.noFloorAhead = true;
         }

         if (!game.isPassable(var2, this.j)) {
            this.noFloorBeyond = true;
         }
      }
   }

   // $VF: renamed from: d () void
   public final void update() {
      if (this.state != 5 && this.health > 0) {
         if (this.state == 4 && this.knockback == 0 && this.type != 6 && this.type != 7 && this.type != 5) {
            if (++this.hurtTimer > 10) {
               this.velX = 0;
               this.animPhase = 2;
               this.state = 0;
            }
         } else if (this.state != 7 && this.state != 6) {
            if (Math.abs(Game.playerPixelX() - this.pixelX()) <= 399 && Math.abs(Game.playerPixelY() - this.feetY()) <= 342) {
               int var1 = Game.playerPixelX() - this.pixelX();
               int var2 = Game.playerPixelY() + 38 - Game.spriteBaseHeight - this.feetY();
               this.playerRightNear = var1 < 171
                  && game.isPassable(this.col - 1, this.row - 1)
                  && game.isPassable(this.col - 2, this.row - 1)
                  && var1 > 52
                  && Math.abs(var2) < 76
                  && (this.aa == 0 || this.type == 3);
               this.playerLeftNear = var1 < -52
                  && game.isPassable(this.col + 1, this.row - 1)
                  && game.isPassable(this.col + 2, this.row - 1)
                  && var1 > -171
                  && Math.abs(var2) < 76
                  && (this.aa == 0 || this.type == 3);
               if (this.type != 3 || Math.abs(Game.playerPixelX() - this.pixelX()) <= 199 && Math.abs(Game.playerPixelY() - this.feetY()) <= 190) {
                  this.stepPhysics();
                  if (this.tileBelow >= 26 && this.tileBelow <= 31 || this.tileHere >= 26 && this.tileHere <= 31) {
                     this.health--;
                     this.velY = -304;
                     if (this.health <= 0) {
                        this.state = 0;
                        game.spawnEnemyProjectile(this.worldX, this.feetY() + 19 << 8, 21, this.facingRight, false, 0, this.spawnCode);
                        this.animPhase = 2;
                        this.type = -1;
                        return;
                     }
                  }

                  if (this.knockback != 0 && this.type != 3 && this.type != 0) {
                     this.velX = this.knockback << 8;
                     this.velY = 0;
                     this.state = 4;
                     if (this.type != 4) {
                        playAnim(this, this.type, 4);
                     }

                     if (this.knockback > 0) {
                        this.knockback--;
                        this.facingRight = true;
                     } else {
                        this.knockback++;
                        this.facingRight = false;
                     }

                     if (this.noFloorAhead) {
                        this.velX = 0;
                     }
                  } else {
                     this.meleeHit = false;
                     if (this.type == 0) {
                        if (this.blockedByWall) {
                           this.facingRight = !this.facingRight;
                           this.aa = 10;
                        } else if (this.noFloorAhead) {
                           if (this.tileHere == -60) {
                              this.velY = -510;
                              playAnim(this, this.type, 2);
                              this.state = 2;
                              return;
                           }

                           this.facingRight = !this.facingRight;
                           this.aa = 10;
                        } else if (this.dropAhead) {
                           if (this.tileHere == -61 && Math.abs(var1) > 28) {
                              this.velY = moveSpeed[this.type];
                              this.velX = moveSpeed[this.type] << 2;
                              if (!this.facingRight) {
                                 this.velX *= -1;
                              }

                              playAnim(this, this.type, 2);
                              this.state = 2;
                              return;
                           }

                           if (this.tileHere != -60) {
                              this.facingRight = !this.facingRight;
                           }
                        }

                        if (this.state != 2) {
                           this.leaping = false;
                        }

                        if (this.state != 2 && Math.abs(var1) <= 57 && var2 < 0 && var2 >= -114 && this.spawnCode != 3) {
                           this.velX = 0;
                           this.leaping = true;
                           this.state = 2;
                           playAnim(this, this.type, 2);
                           this.velY = -2432;
                        } else if (this.playerRightNear && var1 < 456) {
                           if (this.state != 1) {
                              playAnim(this, this.type, 1);
                              this.state = 1;
                           }

                           if (this.spawnCode == 5) {
                              this.velX = moveSpeed[this.type] << 2;
                           } else {
                              this.velX = moveSpeed[this.type] << 1;
                           }

                           this.velY = 1216;
                           this.facingRight = true;
                           this.leaping = false;
                        } else if (this.playerLeftNear && -var1 <= 456) {
                           if (this.state != 1) {
                              playAnim(this, this.type, 1);
                              this.state = 1;
                           }

                           if (this.spawnCode == 5) {
                              this.velX = -(moveSpeed[this.type] << 2);
                           } else {
                              this.velX = -(moveSpeed[this.type] << 1);
                           }

                           this.velY = 1216;
                           this.facingRight = false;
                           this.leaping = false;
                        }

                        if ((
                              Math.abs(Game.playerPixelX() - this.pixelX()) > 52
                                 || Math.abs(Game.playerPixelY() + 38 - Game.spriteBaseHeight - this.feetY()) > 19
                           )
                           && (var2 > -19 || var2 <= -76 || Math.abs(var1) > 28)) {
                           if (!this.leaping) {
                              this.leaping = false;
                              int var5 = this.velX;
                              this.walk();
                              if ((var5 > 0 || this.velX <= 0) && (var5 < 0 || this.velX >= 0)) {
                                 this.velX = var5;
                              }
                           } else {
                              this.velX = 0;
                           }
                        } else {
                           this.velX = 0;
                           game.spawnEnemyProjectile(this.worldX, this.feetY() + 19 << 8, 21, this.facingRight, false, 0, this.spawnCode);
                           this.type = -1;
                           int var3 = Game.sectionId;
                           if (game.inArena) {
                              var3 = 0;
                           }

                           int var4 = this.spawnIndex + var3 * 10 >> 5;
                           game.enemyBits[var4] = game.enemyBits[var4] & ~(1 << this.spawnIndex + var3 * 10 - (var4 << 5));
                        }

                        if (this.aa != 0) {
                           this.aa--;
                           return;
                        }
                     } else if (this.type == 1) {
                        if (var2 < 0 && var2 > -76 && Math.abs(var1) > 114 && this.spawnCode == 2) {
                           this.velY = -2432;
                           this.velX = -(moveSpeed[this.type] << 1);
                           playAnim(this, this.type, 2);
                           this.state = 2;
                           this.homeX = 20;
                        } else if (this.blockedByWall) {
                           this.facingRight = !this.facingRight;
                           if (this.state == 2) {
                              this.velX = -this.velX;
                           }
                        } else if (this.noFloorAhead) {
                           if (this.tileHere == -60 && Math.abs(var1) > 28 && this.spawnCode == 2) {
                              this.velY = -2432;
                              this.velX = moveSpeed[this.type] << 1;
                              playAnim(this, this.type, 2);
                              this.state = 2;
                              return;
                           }

                           if (this.state == 2) {
                              this.velX >>= 1;
                           }

                           this.facingRight = !this.facingRight;
                        } else if (this.dropAhead) {
                           if (this.tileHere == -61 && Math.abs(var1) > 28 && this.spawnCode == 2) {
                              this.velY = -3328;
                              this.velX = moveSpeed[this.type] << 2;
                              if (!this.facingRight) {
                                 this.velX *= -1;
                              }

                              playAnim(this, this.type, 2);
                              this.state = 2;
                              return;
                           }

                           if (this.tileHere != -60) {
                              this.facingRight = !this.facingRight;
                           }
                        }

                        if ((
                              this.state == 2
                                 || !this.playerRightNear
                                 || this.spawnCode == 0
                                 || var1 <= 104 && !this.dropAhead
                                 || this.noFloorBeyond
                                 || this.touchesCrate()
                           )
                           && (var1 <= 52 || var2 >= 19 || var2 <= -76 || !this.touchesCrate())) {
                           if (this.state != 2
                                 && this.playerLeftNear
                                 && this.spawnCode != 0
                                 && (var1 < -104 || this.dropAhead)
                                 && !this.noFloorBeyond
                                 && !this.touchesCrate()
                              || var1 < -52 && var2 < 19 && var2 > -76 && this.touchesCrate()) {
                              if (this.state != 2) {
                                 playAnim(this, this.type, 2);
                                 this.state = 2;
                              }

                              this.velX = -moveSpeed[this.type] << 2;
                              this.velY = -608;
                              this.facingRight = false;
                           }
                        } else {
                           if (this.state != 2) {
                              playAnim(this, this.type, 2);
                              this.state = 2;
                           }

                           this.velX = moveSpeed[this.type] << 2;
                           this.velY = -608;
                           this.facingRight = true;
                        }

                        if (this.homeX > 0) {
                           this.homeX--;
                           return;
                        }

                        if (this.state == 2) {
                           return;
                        }

                        if (Math.abs(var1) > 52 || Math.abs(var2) > 52 || Math.abs(var2) >= 38) {
                           if (this.state == 2 && !this.noFloorAhead) {
                              this.facingRight = !this.facingRight;
                              return;
                           }

                           this.walk();
                           return;
                        }

                        this.velX = 0;
                        if (Game.playerAnim == 10) {
                           return;
                        }

                        this.facingRight = Game.playerPixelX() - this.pixelX() > 0;
                        if (--this.attackCountdown < 0) {
                           this.attackCountdown = attackCooldown[this.type];
                           if (this.state != 2 && this.state != 3) {
                              playAnim(this, this.type, 3);
                              this.state = 3;
                              this.animPhase = 1;
                           }

                           if (Game.invulnFrames > 0) {
                              return;
                           }

                           if (Game.rectsOverlap(
                              Game.playerPixelX() - Game.hitboxX,
                              Game.playerPixelY() + Game.hitboxY + -12,
                              Game.hitboxW,
                              Game.hitboxH,
                              this.pixelX() + (this.facingRight ? attackOffsetX[this.type] : -attackOffsetX[this.type] - attackWidth[this.type]),
                              this.feetY() + attackOffsetY[this.type],
                              attackWidth[this.type],
                              attackHeight[this.type]
                           )) {
                              if (!Game.invincible) {
                                 Game.health = Game.health - attackDamage[this.spawnCode];
                              }

                              game.boltMultiplier = 1;
                              Game.hurtTimer = 0;
                              if (Game.airState == 2) {
                                 Game.airState = 1;
                              }

                              Game.hudDirty = true;
                           }
                        }

                        if (this.state == 2) {
                           return;
                        }
                     } else if (this.type == 2) {
                        if (this.state == 0) {
                           this.attackTimer = this.attackTimer + Game.deltaTime;
                           if (this.attackTimer >= 1100) {
                              this.animPhase = 2;
                              this.leaping = false;
                              this.attackTimer = 0;
                              this.facingRight = !this.facingRight;
                           }
                        }

                        if ((this.blockedByWall || this.noFloorAhead || this.dropAhead)
                           && this.state != 0
                           && this.state != 3
                           && this.animPhase != 2
                           && this.state != 2) {
                           playAnim(this, this.type, 0);
                           this.state = 0;
                           this.velX = 0;
                           this.animPhase = 1;
                        }

                        Math.abs(this.homeX - this.worldX);
                        this.V++;
                        if (this.playerRightNear
                           && this.state != 3
                           && var1 < 104
                           && Math.abs(var2) < 38
                           && !this.noFloorBeyond
                           && !this.dropAhead
                           && !this.noFloorAhead
                           && this.V > 15) {
                           if (this.state != 2 && Game.getRawTile((this.worldX >> 8) / 57 + 1, this.row + 1) != 27) {
                              playAnim(this, this.type, 2);
                              this.state = 2;
                              this.animPhase = 1;
                              this.velX = -moveSpeed[this.type] << 1;
                              this.velY = -2432;
                              this.facingRight = false;
                              this.V = 0;
                              this.leaping = true;
                              return;
                           }
                        } else if (this.playerLeftNear
                           && this.state != 3
                           && var1 > -104
                           && Math.abs(var2) < 38
                           && !this.noFloorBeyond
                           && !this.dropAhead
                           && !this.noFloorAhead
                           && this.V > 15) {
                           if (this.state != 2 && Game.getRawTile((this.worldX >> 8) / 57 + 1, this.row + 1) != 27) {
                              playAnim(this, this.type, 2);
                              this.state = 2;
                              this.animPhase = 1;
                              this.velX = moveSpeed[this.type] << 1;
                              this.velY = -2432;
                              this.facingRight = true;
                              this.V = 0;
                              this.leaping = true;
                              return;
                           }
                        } else if (this.engaged == 0) {
                           if (Math.abs(var2) <= 38 && Math.abs(var1) < 171 && this.state != 2) {
                              this.facingRight = var1 > 0;
                              this.velX = 0;
                              this.velY = 0;
                              this.engaged = 1;
                              this.aa = 0;
                              this.attackCountdown = 0;
                              this.leaping = false;
                              return;
                           }

                           if (this.state != 0 && this.state != 2 || this.animPhase == 2 && this.state == 0) {
                              this.leaping = false;
                              this.walk();
                              return;
                           }
                        } else if (this.engaged == 1 && this.state != 2 && this.state != 4) {
                           if (this.playerRightNear && !this.facingRight) {
                              this.facingRight = true;
                           } else if (this.playerLeftNear && this.facingRight) {
                              this.facingRight = false;
                           }

                           if (this.aa < burstCount[this.spawnCode] && this.engaged != 0) {
                              this.attack();
                              return;
                           }

                           if (this.animPhase == 2) {
                              this.engaged = 0;
                              return;
                           }

                           if (this.state != 0 && this.state != 2 && !this.animPlaying) {
                              this.animPhase = 1;
                              playAnim(this, this.type, 0);
                              this.state = 0;
                              this.velX = 0;
                              return;
                           }
                        }
                     } else if (this.type == 3) {
                        this.attackTimer = this.attackTimer + Game.deltaTime;
                        if (this.engaged == 0) {
                           if (this.aa >= burstCount[this.spawnCode]
                              || this.attackTimer <= 100 && this.spawnCode != 15 && this.spawnCode != 18 && this.spawnCode != 21) {
                              this.aa = 0;
                              this.engaged = 1;
                           } else {
                              this.attack();
                           }
                        }

                        if (this.engaged == 1 && this.aa++ > 10) {
                           switch (this.mount) {
                              case 0:
                                 if (-var2 > (var1 > 0 ? var1 : -var1)) {
                                    this.vertical = true;
                                    this.engaged = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 if (this.playerRightNear) {
                                    this.facingRight = true;
                                    this.vertical = false;
                                    this.engaged = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 if (this.playerLeftNear) {
                                    this.facingRight = false;
                                    this.vertical = false;
                                    this.engaged = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 this.engaged = 1;
                                 return;
                              case 1:
                                 if (var2 > (var1 > 0 ? var1 : -var1)) {
                                    this.vertical = true;
                                    this.engaged = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 if (this.playerRightNear) {
                                    this.facingRight = true;
                                    this.vertical = false;
                                    this.engaged = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 if (this.playerLeftNear) {
                                    this.facingRight = false;
                                    this.vertical = false;
                                    this.engaged = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 this.engaged = 1;
                                 return;
                              case 2:
                                 if (-var2 > (var1 > 0 ? var1 : -var1)) {
                                    this.facingRight = true;
                                    this.vertical = false;
                                    this.engaged = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 if (var2 > (var1 > 0 ? var1 : -var1)) {
                                    this.vertical = false;
                                    this.facingRight = false;
                                    this.engaged = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 if (this.playerRightNear) {
                                    this.vertical = true;
                                    this.engaged = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 this.engaged = 1;
                              default:
                                 return;
                              case 3:
                                 if (-var2 > (var1 > 0 ? var1 : -var1)) {
                                    this.facingRight = true;
                                    this.vertical = false;
                                    this.engaged = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 if (var2 > (var1 > 0 ? var1 : -var1)) {
                                    this.vertical = false;
                                    this.facingRight = false;
                                    this.engaged = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 if (this.playerLeftNear) {
                                    this.vertical = true;
                                    this.engaged = 0;
                                    this.aa = 0;
                                    return;
                                 }

                                 this.engaged = 1;
                                 return;
                           }
                        }
                     } else {
                        if (this.type == 4) {
                           if (this.blockedByWall) {
                              this.facingRight = !this.facingRight;
                           } else if (this.noFloorAhead) {
                              this.facingRight = !this.facingRight;
                           } else if (this.dropAhead) {
                              this.facingRight = !this.facingRight;
                           }

                           this.walk();
                           return;
                        }

                        if (this.type == 6 || this.type == 7 || this.type == 5) {
                           if (Game.worldId == 4) {
                              playAnim(this, this.type, 0);
                              this.facingRight = false;
                              return;
                           }

                           if (!Game.O) {
                              if (-var2 > 171 && var1 > 0 && this.facingRight) {
                                 this.facingRight = false;
                              } else if (-var2 > 171 && var1 < 0 && !this.facingRight) {
                                 this.facingRight = true;
                              } else if (var1 > 28 && !Game.O) {
                                 this.facingRight = true;
                                 this.velX = moveSpeed[2];
                                 if (this.state != 1 && this.type == 6 && this.animPlaying) {
                                    if (Game.L) {
                                       playAnim(this, this.type, 1);
                                    } else {
                                       playAnim(this, this.type, 3);
                                    }

                                    this.state = 1;
                                 }
                              } else if (var1 < -28 && !Game.O) {
                                 this.facingRight = false;
                                 this.velX = -moveSpeed[2];
                                 if (this.state != 1 && this.type == 6 && this.animPlaying) {
                                    if (Game.L) {
                                       playAnim(this, this.type, 1);
                                    } else {
                                       playAnim(this, this.type, 3);
                                    }

                                    this.state = 1;
                                 }
                              }
                           } else {
                              this.velX = 0;
                              if (this.type != 7) {
                                 this.state = 0;
                              }

                              if (this.type == 6) {
                                 if (!Game.L) {
                                    playAnim(this, this.type, 2);
                                 } else {
                                    playAnim(this, this.type, 0);
                                 }
                              }
                           }

                           if (this.type == 7) {
                              if (!this.animPlaying) {
                                 Game.O = false;
                                 playAnim(this, this.type, 0);
                                 this.state = 0;
                              }

                              if (-var2 < 38 && var2 < 0 || var2 > 0 && var2 < 38) {
                                 if ((this.facingRight && var1 <= 71 && var1 >= 0 || !this.facingRight && var1 >= -71 && var1 < 0) && this.state != 3) {
                                    this.B = false;
                                    this.attackTimer = 0;
                                    playAnim(this, this.type, 3);
                                    this.state = 3;
                                    Game.O = true;
                                    this.attackTimer = 0;
                                 }
                              } else if (this.state != 0 && this.animPlaying) {
                                 this.state = 0;
                                 playAnim(this, this.type, 0);
                                 Game.O = false;
                                 this.attackTimer = 0;
                              }

                              if (this.state == 3) {
                                 this.attackTimer = this.attackTimer + Game.deltaTime;
                                 if (this.attackTimer >= 700) {
                                    if (!this.B
                                       && Game.rectsOverlap(
                                          Game.playerPixelX() - Game.hitboxX,
                                          Game.playerPixelY() + Game.hitboxY + -12,
                                          Game.hitboxW,
                                          Game.hitboxH,
                                          this.pixelX() + (this.facingRight ? attackOffsetX[this.type] : -attackOffsetX[this.type] - attackWidth[this.type]),
                                          this.feetY() + attackOffsetY[this.type],
                                          attackWidth[this.type],
                                          attackHeight[this.type]
                                       )) {
                                       Game.health = Game.health - attackDamage[this.spawnCode];
                                       Game.hudDirty = true;
                                       if (Game.playerAnim != 10) {
                                          game.setPlayerAnim(9);
                                          Game.hurtTimer = 0;
                                       }

                                       this.attackTimer = 0;
                                       this.B = true;
                                       return;
                                    }

                                    if (this.attackTimer >= 1400) {
                                       this.B = false;
                                       return;
                                    }
                                 }
                              }
                           } else if (this.type == 5) {
                              this.attackTimer = this.attackTimer + Game.deltaTime;
                              if (-var2 < 76) {
                                 if (this.facingRight && var1 > 57 && var1 < 228 || !this.facingRight && var1 < -57 && var1 > -228) {
                                    if (this.attackTimer > 1000) {
                                       playAnim(this, this.type, 3);
                                       this.attackTimer = 0;
                                       this.state = 3;
                                       game.spawnEnemyProjectile(
                                          this.worldX + (this.facingRight ? 7296 : -7296),
                                          this.feetY() + (Game.spriteBaseHeight >> 1) - 38 << 8,
                                          4,
                                          this.facingRight,
                                          this.vertical,
                                          this.mount,
                                          this.spawnCode
                                       );
                                       return;
                                    }
                                 } else if (this.state != 0 && this.animPlaying) {
                                    this.state = 0;
                                    if (Game.O) {
                                       playAnim(this, this.type, 1);
                                       return;
                                    }

                                    playAnim(this, this.type, 0);
                                    return;
                                 }
                              } else if (this.state != 0 && this.animPlaying) {
                                 this.state = 0;
                                 playAnim(this, this.type, 0);
                                 return;
                              }
                           } else if (this.type == 6) {
                              if (!Game.L
                                 && Game.rectsOverlap(
                                    Game.playerPixelX() - Game.hitboxX,
                                    Game.playerPixelY() + Game.hitboxY + -12,
                                    Game.hitboxW,
                                    Game.hitboxH,
                                    this.pixelX() + (this.facingRight ? bodyOffsetX[this.type] - bodyWidth[this.type] : -bodyOffsetX[this.type]),
                                    this.feetY() + bodyOffsetY[this.type],
                                    bodyWidth[this.type],
                                    bodyHeight[this.type]
                                 )) {
                                 Game.health -= 4;
                                 Game.hudDirty = true;
                                 return;
                              }

                              if (!Game.L) {
                                 if (this.attackTimer > 800) {
                                    if (-var2 > (var1 > 0 ? var1 : -var1)) {
                                       playAnim(this, this.type, 7);
                                       game.spawnEnemyProjectile(
                                          this.worldX + (this.facingRight ? -6384 : 6384),
                                          this.feetY() - (Game.spriteBaseHeight >> 2) << 8,
                                          2,
                                          this.facingRight,
                                          true,
                                          this.mount,
                                          this.spawnCode
                                       );
                                       this.attackTimer = 0;
                                       return;
                                    }

                                    if (this.playerRightNear && this.facingRight) {
                                       game.spawnEnemyProjectile(
                                          this.worldX + (this.facingRight ? 7296 : -7296),
                                          this.feetY() + (Game.spriteBaseHeight >> 1) << 8,
                                          2,
                                          this.facingRight,
                                          false,
                                          this.mount,
                                          this.spawnCode
                                       );
                                       playAnim(this, this.type, 6);
                                       this.attackTimer = 0;
                                       return;
                                    }

                                    if (this.playerLeftNear && !this.facingRight) {
                                       game.spawnEnemyProjectile(
                                          this.worldX + (this.facingRight ? 7296 : -7296),
                                          this.feetY() + (Game.spriteBaseHeight >> 1) << 8,
                                          2,
                                          this.facingRight,
                                          false,
                                          this.mount,
                                          this.spawnCode
                                       );
                                       playAnim(this, this.type, 6);
                                       this.attackTimer = 0;
                                       return;
                                    }
                                 } else {
                                    this.attackTimer = this.attackTimer + Game.deltaTime;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            } else {
               this.velX = this.velY = 0;
            }
         }
      } else {
         if (this.type == 7) {
            Game.L = false;
            Game.O = false;
            game.spawnEnemyProjectile(
               this.worldX + (this.facingRight ? 3648 : -3648), (this.feetY() << 8) + 9728, 30, this.facingRight, false, 0, this.spawnCode
            );
         } else if (this.type == 6) {
            Game.N = false;
            game.spawnEnemyProjectile(this.worldX, this.feetY() + 19 << 8, 30, this.facingRight, false, 0, this.spawnCode);
            game.messageHasPortrait = true;
            Game.messageCount = Game.cJ[17];
            game.showMessage(Game.cK[17]);
         } else if (this.type == 5) {
            Game.M = false;
            game.spawnEnemyProjectile(
               this.worldX + (this.facingRight ? -7296 : 7296), (this.feetY() << 8) - 4864, 30, this.facingRight, false, 0, this.spawnCode
            );
            game.spawnEnemyProjectile(
               this.worldX + (this.facingRight ? -14592 : 14592), (this.feetY() << 8) - 38, 30, this.facingRight, false, 0, this.spawnCode
            );
            game.spawnEnemyProjectile(
               this.worldX + (this.facingRight ? -3648 : 3648), (this.feetY() << 8) + 9728, 30, this.facingRight, false, 0, this.spawnCode
            );
         }

         this.type = -1;
      }
   }

   // $VF: renamed from: e () void
   public final void updateAnim() {
      if (this.animPhase != 2) {
         if (this.type == -1) {
            game.spawnEnemyProjectile(this.worldX, this.feetY() + 19 << 8, 30, this.facingRight, false, 0, this.spawnCode);
         } else if (this.type == 1 || this.type == 2 || this.type == 0 || this.type == 6 || this.type == 7 || this.type == 5 || this.type == 4) {
            this.animPlaying = game.stepAnim(this.slot, Game.deltaTime);
         }
      }
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics, int, int, int) void
   public final void draw(Graphics var1, int var2, int var3, int var4) {
      Game.cameraX = var3;
      Game.cameraY = var4;
      int var5 = this.pixelX() + Game.cameraX - 22;
      int var6 = this.feetY() + Game.cameraY;
      if (var5 >= -44 && var5 <= 240 && var6 >= -Game.spriteBaseHeight && var6 <= 320) {
         Sprite[] var7 = Game.enemySprites[this.type];
         int var8 = 0;
         if ((var2 & 1) > 0) {
            if (this.type != 3) {
               byte var9 = 0;
               if (!this.facingRight) {
                  var9 = 2;
                  var5 += 57;
               }

               if (this.type == 1 || this.type == 2 || this.type == 0 || this.type == 6 || this.type == 7 || this.type == 5 || this.type == 4) {
                  game.drawAnim(var1, Game.enemySprites[this.type], this.slot, var5, var6, var9);
                  return;
               }
            } else {
               if (this.vertical) {
                  byte var16 = drawOffsets[(this.type * 8 + this.state) * 5];
                  var8 = var16 + 2;
               }

               byte var10 = 0;
               byte var11 = 0;
               int var12;
               byte var17;
               switch (this.mount) {
                  case 1:
                     var17 = 0;
                     var12 = this.facingRight ? 1 : 3;
                     break;
                  case 2:
                     var17 = 6;
                     var12 = this.facingRight ? 2 : 3;
                     break;
                  case 3:
                     var17 = 6;
                     var12 = this.facingRight ? 0 : 1;
                     break;
                  default:
                     var17 = 0;
                     var12 = this.facingRight ? 0 : 2;
               }

               boolean var13 = (var12 & 2) > 0;
               boolean var14 = (var12 & 1) > 0;
               if (var13) {
                  var5 += 57;
               }

               if (var14) {
                  var6 += 50;
               }

               if ((var8 & 1) > 0) {
                  if (var17 == 0) {
                     if (!this.vertical) {
                        var10 = (byte)(this.facingRight ? -1 : 1);
                     } else {
                        var11 = (byte)(var14 ? -1 : 1);
                     }
                  } else if (this.vertical) {
                     var10 = (byte)(var13 ? -1 : 1);
                  } else {
                     var11 = (byte)(this.facingRight ? 1 : -1);
                  }
               }

               int var15 = this.mount == 1 ? 4 : 0;
               var7[var17].draw(var1, var5, var6 + var15, var12);
               var7[var17 + 1].draw(var1, var5 + var10, var6 + var11 + var15, var12);
               var7[var17 + 2 + var8].draw(var1, var5 + var10, var6 + var11 + var15, var12);
            }
         }
      }
   }

   // $VF: renamed from: j () void
   private void walk() {
      if (this.state != 1) {
         if (this.type == 1 || this.type == 2 || this.type == 0 || this.type == 4) {
            playAnim(this, this.type, 1);
            this.state = 1;
         }

         this.state = 1;
      }

      this.animPhase = 0;
      this.aa = 0;
      this.velX = this.facingRight ? moveSpeed[this.type] : -moveSpeed[this.type];
   }

   // $VF: renamed from: k () void
   private void attack() {
      if (--this.attackCountdown < 0) {
         if (this.type == 2) {
            if (this.state != 3 && this.state != 2) {
               playAnim(this, this.type, 3);
               this.state = 3;
            }

            this.animPhase = 1;
         } else {
            this.state = 3;
         }

         int var1 = 0;
         int var2 = this.worldX;
         switch (this.type) {
            case 2:
               if (!this.vertical) {
                  var1 -= 2;
               }
               break;
            case 3:
               switch (this.mount) {
                  case 0:
                     if (!this.vertical) {
                        var1 += 2;
                        var2 += (this.facingRight ? 12 : -12) << 8;
                     } else {
                        var2 += (this.facingRight ? -10 : 10) << 8;
                        var1 = -28;
                     }
                     break;
                  case 1:
                     if (!this.vertical) {
                        var1 = -12;
                        var2 += (this.facingRight ? 12 : -12) << 8;
                     } else {
                        var2 += (this.facingRight ? -10 : 6) << 8;
                        var1 += 28;
                     }
                     break;
                  case 2:
                     if (!this.vertical) {
                        var1 = 0 + ((this.facingRight ? -12 : 12) << 1);
                        var2 -= 1792;
                     } else {
                        var1 = 0 - (this.facingRight ? 12 : 0);
                        var2 += 5120;
                     }
                     break;
                  case 3:
                     if (!this.vertical) {
                        var1 = 0 + ((this.facingRight ? -12 : 12) << 1);
                     } else {
                        var1 = 0 - (this.facingRight ? 12 : 0);
                        var2 -= 5120;
                     }
               }
         }

         int var3 = this.feetY() + (Game.spriteBaseHeight >> 1) + var1 << 8;
         byte var4 = 0;
         if (this.type == 2) {
            var4 = (byte)(this.facingRight ? 7168 : -7168);
         }

         if (this.spawnCode == 6 || this.spawnCode == 9 || this.spawnCode == 12 || this.spawnCode == 15 || this.spawnCode == 18 || this.spawnCode == 21) {
            game.spawnEnemyProjectile(var2 + var4, var3, 0, this.facingRight, this.vertical, this.mount, this.spawnCode);
         } else if (this.spawnCode == 7 || this.spawnCode == 10 || this.spawnCode == 13 || this.spawnCode == 16 || this.spawnCode == 19 || this.spawnCode == 22
            )
          {
            game.spawnEnemyProjectile(var2 + var4, var3, 6, this.facingRight, this.vertical, this.mount, this.spawnCode);
         } else if (this.spawnCode == 7 || this.spawnCode == 10 || this.spawnCode == 13) {
            game.spawnEnemyProjectile(var2 + var4, var3, 18, this.facingRight, this.vertical, this.mount, this.spawnCode);
         } else if (this.spawnCode == 17 || this.spawnCode == 20 || this.spawnCode == 23) {
            game.spawnEnemyProjectile(var2, var3, 3, this.facingRight, this.vertical, this.mount, this.spawnCode);
         }

         this.attackCountdown = attackCooldown[this.type];
         this.aa++;
      } else {
         if (this.type == 3) {
            this.attackTimer = 0;
         }

         this.animPhase = 2;
      }
   }

   // $VF: renamed from: a (java.io.DataInputStream, byte[], int, boolean) void
   private static void readScaled(DataInputStream var0, byte[] var1, int var2, boolean var3) {
      int var4;
      if (var3) {
         var4 = 44;
      } else {
         var4 = Game.spriteBaseHeight;
      }

      for (int var5 = 0; var5 < var2; var5++) {
         try {
            var1[var5] = var0.readByte();
         } catch (IOException var6) {
         }

         var1[var5] = (byte)(var1[var5] * var4 / 44);
      }
   }

   // $VF: renamed from: a (int) void
   private static void loadHitboxes(int var0) {
      try {
         DataInputStream var1;
         readScaled(var1 = game.openResource(var0), bodyOffsetX, 8, true);
         readScaled(var1, bodyOffsetY, 8, false);
         readScaled(var1, bodyWidth, 8, true);
         readScaled(var1, bodyHeight, 8, false);
         readScaled(var1, attackOffsetX, 8, true);
         readScaled(var1, attackOffsetY, 8, false);
         readScaled(var1, attackWidth, 8, true);
         readScaled(var1, attackHeight, 8, false);
         var1.close();
      } catch (IOException var2) {
      }
   }
}
