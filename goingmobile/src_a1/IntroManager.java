import java.io.IOException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;

// Mechanically renamed from decompiled_a1/g.java (class g) via a
// scripted find/replace (see the commit that introduced this file) --
// class name, cross-references to already-confirmed members of other
// classes (Game/ratchetandclank/Projectile/CanvasShell), and a handful
// of IntroManager's own unambiguous fields (splashImages/display/
// midlet/recentKeyHistory/CHEAT_CODE_KEYS) are renamed. Everything else
// -- the menu/splash/unlock-code state machine itself (fields b/c/d/e/
// f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/A/B/D/E/F/G/H/I/J/M and most
// of the per-screen render/input-handler methods) -- is deliberately
// LEFT OBFUSCATED, same treatment as the legacy build's own `Game`
// class (CLASS_MAP.md: "the obfuscator reused every single letter... so
// no single-letter member... can be renamed by any safe textual rule").
// At 58KB with dozens of densely-interacting single-letter fields
// across a 1754-line state machine, attempting a full member-level
// rename here risked repeating Player.java's u/v/t mix-up at a much
// larger scale for a smaller marginal payoff (the class's *shape* is
// already well understood from this pass -- see CLASS_MAP.md's
// "IntroManager" section for the write-up). Revisit if/when it's
// actually load-bearing for the port to have every field named.
//
// What this class does, confirmed by reading the whole file: it's the
// MIDlet's pre-game controller (`ratchetandclank.introManager`) --
// splash sequence (3 logo images, `splashImages`), a step-by-step boot
// sequence that constructs `Game` over ~39 ticks (field `M`, calling
// `Game.a(int)` once per tick -- an initialization step function, not
// yet identified further), the ENTIRE menu system (main menu, language
// select, sound toggle, new-game slot picker, delete-save picker, a
// shared save-slot confirm screen, credits/help/about, and the trial's
// unlock-code `Form`/`TextField` UI, ~20 screens selected by field `e`),
// a decorative animated background of real `Projectile` instances
// during the menu (method `a(Projectile)`, a simplified duplicate of
// Projectile.update()), and a Konami-style 4-key cheat sequence
// (`recordKeyForCheat`/`checkCheatCode`/`CHEAT_CODE_KEYS`, unlocking
// `Game.r` -- almost certainly the same "invincibility cheat" flag the
// legacy build's `Game.i`/`Game.j` implement via a keypad code, per
// CLASS_MAP.md's legacy section). Notably, **this means the legacy
// build's menu system (which lived inside `Game`/`f`) moved entirely
// into this separate class in a1** -- a1's `Game` is presumably
// gameplay-only, unlike the legacy build's.
public final class IntroManager {
   public static final int[] CHEAT_CODE_KEYS = new int[]{49, 51, 49, 51};
   public static int b;
   public boolean c = false;
   private Image[] splashImages;
   public static byte d;
   private static byte A;
   private static byte B;
   private Display display;
   public byte e;
   private byte D;
   private long E;
   public byte f;
   public int[] g;
   private long F;
   private byte G;
   private byte H;
   public boolean h;
   private byte I;
   private byte J;
   private int[] recentKeyHistory = new int[]{52, 54, 52, 54};
   private ratchetandclank midlet;
   public String i;
   public boolean j = true;
   public Form k;
   public TextField l;
   public String m = "";
   public long n;
   public boolean o;
   public boolean p = false;
   public boolean q = false;
   public boolean r = false;
   public int s = 0;
   public long t = 0L;
   public int u = 0;
   private int M = 4;
   public int[] v = new int[]{652482873, 766492548};
   public int[][] w = new int[2][8];
   public static int x = 0;
   public static int y = 0;

   public final byte a() {
      return B;
   }

   public final int b() {
      return A - ((176 / A + 1) * A - 176) / 2;
   }

   public IntroManager(ratchetandclank var1) {
      this.midlet = var1;
      this.c = true;
      this.midlet.loadStrings();
      A = Game.F;
      B = Game.G;
      this.display = Display.getDisplay(var1);
      this.e = 0;
      this.D = 0;
      this.f = 3;
      this.g = new int[8];
      this.F = System.currentTimeMillis();
      this.G = 0;
      this.H = 0;
      String var2;
      if ((var2 = this.midlet.getAppProperty("Unlock-Code")) != null) {
         this.j = var2.equals("true");
      }

      try {
         this.splashImages = new Image[3];
         this.splashImages[0] = Image.createImage("/intro_publisher.png");
         this.splashImages[1] = Image.createImage("/intro_handheld.png");
         this.splashImages[2] = Image.createImage("/intro_ratchet.png");
      } catch (IOException var4) {
      }
   }

   public final void c() {
      this.f = 3;
      this.D = 0;
      this.G = 0;
      this.midlet.game.aQ = 3;

      try {
         Game.aF = null;
         System.gc();
         Game.e(20);
         Game.aF = Image.createImage(Game.aR[3]);
      } catch (IOException var2) {
      }

      System.currentTimeMillis();
      this.a((byte)1);
   }

   public final void a(boolean var1) {
      System.gc();
      Game.e(20);
      this.o = true;
      this.r = true;
   }

   public final void d() {
      if (this.r) {
         this.p = true;
         this.midlet.stopSoundHardOnLevelStart();
      } else {
         this.f();
      }
   }

   public final void e() {
      if (this.q) {
         this.q = false;
      } else {
         this.g();
      }

      this.p = false;
   }

   public final void f() {
      this.midlet.stopSoundHardOnLevelStart();
      this.f = (byte)(this.f | 1);
      this.p = true;
      this.t = 0L;
      Game.aF = null;
      Game.aG = null;
      Game.aH = null;
      Game.aC = null;
      Game.aM = null;
      Game.aJ = null;
      System.gc();
   }

   public final void g() {
      if (this.p) {
         if (this.e > 0) {
            this.s = 5;
         }

         this.f = (byte)(this.f | 1);
         this.p = false;
         this.u = 4;

         try {
            if (Game.aG == null) {
               Game.aG = new Image[5];
               Game.aG[0] = Image.createImage("/en_disdro.png");
               Game.aG[1] = Image.createImage("/en_micbot.png");
               Game.aG[2] = Image.createImage("/en_patbot.png");
               Game.aG[3] = Image.createImage("/en_turret.png");
               Game.aG[4] = Image.createImage("/en_boar.png");
            }

            if (Game.aH == null) {
               Game.aH = Image.createImage("/ratcht.png");
            }

            if (Game.aF == null) {
               try {
                  this.midlet.game.aQ = 3;
                  Game.aF = Image.createImage(Game.aR[3]);
               } catch (IOException var2) {
               }
            }

            if (Game.aC == null) {
               Game.aC = Image.createImage("/doors.png");
            }

            if (Game.aM == null) {
               Game.aM = Image.createImage("/explod.png");
            }

            if (Game.aJ == null) {
               Game.aJ = Image.createImage("/box.png");
            }
         } catch (Exception var3) {
         }

         this.t = System.currentTimeMillis();
      }
   }

   public final void h() {
      if (this.M <= 42) {
         if (this.midlet.game == null) {
            this.E = System.currentTimeMillis();
            this.midlet.game = new Game(this.midlet);
         }

         if (this.u > 0) {
            this.u = 0;
            this.M--;
         }

         try {
            this.midlet.game.a(this.M);
            this.M++;
         } catch (Exception var2) {
         }

         if (this.M <= 42) {
            return;
         }

         this.f = 3;
         System.currentTimeMillis();
      }

      if (this.p) {
         Game.e(100);
      } else if (this.u > 0 && --this.u > 0) {
         Game.e(10);
      } else {
         if (System.currentTimeMillis() - this.F > 33L) {
            this.F = System.currentTimeMillis();
            if (this.s > 0 && --this.s == 0 && ratchetandclank.language != -1 && !this.midlet.playMenuLoopSound(false)) {
               this.s = 2;
            }

            this.i();
         } else if ((this.f & 1) > 0) {
            this.f = (byte)(this.f & -2);
         }

         if (this.o) {
            this.o = false;
         }
      }
   }

   public final void a(String var1, int var2) {
      String var3 = var1.toUpperCase();
      int var5 = 0;
      int var7 = var3.length();

      for (int var8 = 0; var8 < var7; var8++) {
         char var6 = var3.charAt(var8);
         var5 += var6 * (var8 + 1);
      }

      int var4 = var5 * this.v[var2];

      for (int var12 = 0; var12 < 8; var12++) {
         int var9 = var12 * 4;
         int var11 = 15 << var9;
         int var10 = (var4 & var11) >> var9;
         this.w[var2][var12] = this.a(var10);
      }
   }

   public final int a(int var1) {
      if (var1 >= 0 && var1 <= 3) {
         return 0;
      } else if (var1 >= 4 && var1 <= 7) {
         return 1;
      } else {
         return var1 >= 8 && var1 <= 11 ? 2 : 3;
      }
   }

   public final void a(byte var1) {
      this.f = 3;
      if (var1 == 1) {
         this.midlet.game.dX = -1;
         this.midlet.game.aj.ao = true;
         d = 90;
         this.midlet.game.aj.L = 1;
         this.midlet.game.aj.a((byte)1);
         this.midlet.game.aj.am = 0;
         this.midlet.game.aj.aa = 6;
         this.midlet.game.aj.ab = -30720;
         this.midlet.game.aj.ac = 0;
         this.midlet.game.aj.ad = 1536;
         this.midlet.game.aj.E = 0;
         this.midlet.game.aj.ag = -2048;
         this.midlet.game.ai[0].Z = 1;
         this.midlet.game.ai[0].v = 0;
         this.midlet.game.ai[0].ao = true;
         this.midlet.game.ai[0].u = false;
         this.midlet.game.ai[0].a((byte)1);
         this.midlet.game.ai[0].am = 0;
         this.midlet.game.ai[0].aa = 6;
         this.midlet.game.ai[0].ab = -10752;
         this.midlet.game.ai[0].ac = 0;
         this.midlet.game.ai[0].ad = 1536;
         this.midlet.game.ai[0].ai = 1;
         this.midlet.game.ai[0].ah = 1;
         this.midlet.game.ai[0].ap = 0;
         this.midlet.game.ai[0].ag = -2048;
         this.midlet.game.ai[1].Z = 1;
         this.midlet.game.ai[1].v = 0;
         this.midlet.game.ai[1].ao = false;
         this.midlet.game.ai[1].u = false;
         this.midlet.game.ai[1].a((byte)1);
         this.midlet.game.ai[1].am = 0;
         this.midlet.game.ai[1].aa = 6;
         this.midlet.game.ai[1].ab = 9 * A << 8;
         this.midlet.game.ai[1].ac = 0;
         this.midlet.game.ai[1].ad = 0;
         this.midlet.game.ai[1].ai = 1;
         this.midlet.game.ai[1].ah = 1;
         this.midlet.game.ai[1].ap = 0;
         this.midlet.game.ai[1].ag = -2048;
         this.midlet.game.ai[2].Z = 1;
         this.midlet.game.ai[2].v = 0;
         this.midlet.game.ai[2].ao = false;
         this.midlet.game.ai[2].u = false;
         this.midlet.game.ai[2].a((byte)1);
         this.midlet.game.ai[2].am = 0;
         this.midlet.game.ai[2].aa = 6;
         this.midlet.game.ai[2].ab = 10 * A << 8;
         this.midlet.game.ai[2].ac = 0;
         this.midlet.game.ai[2].ad = 0;
         this.midlet.game.ai[2].ai = 1;
         this.midlet.game.ai[2].ah = 1;
         this.midlet.game.ai[2].ap = 0;
         this.midlet.game.ai[2].ag = -2048;
         this.midlet.game.ai[3].Z = 1;
         this.midlet.game.ai[3].v = 0;
         this.midlet.game.ai[3].ao = false;
         this.midlet.game.ai[3].u = false;
         this.midlet.game.ai[3].a((byte)1);
         this.midlet.game.ai[3].am = 0;
         this.midlet.game.ai[3].aa = 6;
         this.midlet.game.ai[3].ab = 11 * A << 8;
         this.midlet.game.ai[3].ac = 0;
         this.midlet.game.ai[3].ad = 0;
         this.midlet.game.ai[3].ai = 1;
         this.midlet.game.ai[3].ah = 1;
         this.midlet.game.ai[3].ap = 0;
         this.midlet.game.ai[3].ag = -2048;
         this.s = 3;
      }

      if (var1 == 5) {
         for (int var2 = 0; var2 < Game.ba; var2++) {
            this.midlet.game.ai[var2].Z = -1;
         }

         this.midlet.game.aj.ao = true;
         this.midlet.game.aj.L = 0;
         this.midlet.game.aj.a((byte)8);
         this.midlet.game.aj.am = 1;
         this.midlet.game.aj.ah = 1;
         this.midlet.game.aj.ab = 22528;
         this.midlet.game.aj.ac = 0;
         this.midlet.game.aj.ad = 0;
         this.midlet.game.aj.E = 0;
         this.midlet.game.aj.aa = 2;
         this.midlet.game.aj.ag = -1280;
         this.midlet.game.aj.O = -1;
         this.I = 0;
      }

      if (var1 == 15) {
         this.n = 0L;
         this.k = new Form("");
         this.k.addCommand(new Command(ratchetandclank.strings[39], 4, 1));
         this.k.addCommand(new Command(ratchetandclank.strings[8], 3, 1));
         this.k.setCommandListener(this.midlet.canvas);
         this.l = new TextField(ratchetandclank.strings[289], this.m, 15, 0);
         this.k.append(this.l);
         this.r = true;
         this.display.setCurrent(this.k);
         this.n = System.currentTimeMillis();
      }

      if (var1 == 17) {
         this.a(this.m, 0);
         this.a(this.m, 1);
      }

      this.e = var1;
      this.G = 0;
      if (var1 == 20) {
         if (ratchetandclank.language == -1) {
            this.G = 0;
         } else {
            this.G = (byte)ratchetandclank.language;
         }
      }

      if (var1 == 4 || var1 == 6 || var1 == 11) {
         this.midlet.refreshSaveSlotSummaries();
      }
   }

   public final void a(Command var1) {
      if (this.n != 0L) {
         if (System.currentTimeMillis() - this.n >= 1000L) {
            if (var1.getCommandType() == 4) {
               String var2;
               int var3;
               if ((var3 = (var2 = this.l.getString().trim()).length()) < 4) {
                  this.a(ratchetandclank.strings[292], ratchetandclank.strings[293], 1500);
                  return;
               }

               if (var3 >= 16) {
                  this.a(ratchetandclank.strings[292], ratchetandclank.strings[294], 1500);
                  return;
               }

               this.m = var2;
               this.a((byte)17);
            } else {
               this.a((byte)1);
            }

            this.k = null;
            this.l = null;
            System.gc();
            this.q = true;
            this.midlet.canvas.a = -1;
            this.display.setCurrent(this.midlet.canvas);
         }
      }
   }

   public final void a(String var1, String var2, int var3) {
      Alert var4;
      (var4 = new Alert(var1, var2, null, AlertType.ERROR)).setTimeout(var3);
      this.display.setCurrent(var4);
   }

   public final void a(Graphics var1, byte var2) {
      var1.setClip(0, 0, 176, 220);
      var1.setColor(0);
      var1.fillRect(0, 0, 176, 220);
      int var3;
      if (176 % Game.aF.getWidth() == 0) {
         var3 = 0;
      } else {
         var3 = -(((176 / Game.aF.getWidth() + 1) * Game.aF.getWidth() - 176) / 2);
      }

      this.midlet.game.a(var1, var3, 0, Game.aF.getWidth(), Game.aF.getHeight() / 16);
      var1.drawImage(Game.aF, var3, 0, 0);
      this.midlet.game.a(var1, var3, Game.aF.getHeight() / 16, Game.aF.getWidth(), Game.aF.getHeight() / 16);
      var1.drawImage(Game.aF, var3, Game.aF.getHeight() / 16, 0);
      this.midlet.game.a(var1, 176 - var3 - Game.aF.getWidth(), 0, Game.aF.getWidth(), Game.aF.getHeight() / 16);
      e.b.drawImage(Game.aF, 176 - var3 - Game.aF.getWidth(), 0, 20, 8192);
      this.midlet.game.a(var1, 176 - var3 - Game.aF.getWidth(), Game.aF.getHeight() / 16, Game.aF.getWidth(), Game.aF.getHeight() / 16);
      e.b.drawImage(Game.aF, 176 - var3 - Game.aF.getWidth(), Game.aF.getHeight() / 16, 20, 8192);
      this.midlet.game.a(var1, var3, 2 * Game.aF.getHeight() / 16, Game.aF.getWidth(), Game.aF.getHeight() / 16);
      var1.drawImage(Game.aF, var3, Game.aF.getHeight() / 16, 0);
      this.midlet.game.a(var1, 176 - var3 - Game.aF.getWidth(), 2 * Game.aF.getHeight() / 16, Game.aF.getWidth(), Game.aF.getHeight() / 16);
      e.b.drawImage(Game.aF, 176 - var3 - Game.aF.getWidth(), Game.aF.getHeight() / 16, 20, 8192);
      int var5 = Game.aF.getHeight() / 16;

      for (int var4 = 3 * var5; var4 < 220 - Game.aF.getHeight() / 16; var4 += var5) {
         if (var4 + var5 > 220 - Game.aF.getHeight() / 16) {
            var5 = 220 - Game.aF.getHeight() / 16 - var4;
         }

         this.midlet.game.a(var1, var3, var4, Game.aF.getWidth(), var5);
         var1.drawImage(Game.aF, var3, var4 - 2 * Game.aF.getHeight() / 16, 0);
         this.midlet.game.a(var1, 176 - var3 - Game.aF.getWidth(), var4, Game.aF.getWidth(), var5);
         e.b.drawImage(Game.aF, 176 - var3 - Game.aF.getWidth(), var4 - 2 * Game.aF.getHeight() / 16, 0, 8192);
      }

      this.midlet.game.a(var1, var3, 220 - Game.aF.getHeight() / 16, Game.aF.getWidth(), Game.aF.getHeight() / 16);
      var1.drawImage(Game.aF, var3, 220 - Game.aF.getHeight() / 16 - 3 * Game.aF.getHeight() / 16, 0);
      this.midlet.game.a(var1, 176 - var3 - Game.aF.getWidth(), 220 - Game.aF.getHeight() / 16, Game.aF.getWidth(), Game.aF.getHeight() / 16);
      e.b.drawImage(Game.aF, 176 - var3 - Game.aF.getWidth(), 220 - 4 * Game.aF.getHeight() / 16, 20, 8192);

      for (int var6 = var3 + Game.aF.getWidth(); var6 < 176 - var3 - Game.aF.getWidth(); var6 += Game.aF.getWidth()) {
         this.midlet.game.a(var1, var6, 220 - Game.aF.getHeight() / 16, Game.aF.getWidth(), Game.aF.getHeight() / 16);
         var1.drawImage(Game.aF, var6, 220 - Game.aF.getHeight() / 16 - 4 * Game.aF.getHeight() / 16, 0);
      }

      var1.setClip(0, 0, 176, 220);
   }

   public final void b(int var1) {
      if (this.u <= 0) {
         if (this.t <= 0L || System.currentTimeMillis() - this.t >= 1000L) {
            int var2 = 0;
            if (var1 == 50) {
               var2 = -1;
            } else if (var1 == 56) {
               var2 = -2;
            } else if (var1 == 52) {
               var2 = -3;
            } else if (var1 == 54) {
               var2 = -4;
            } else if (var1 == 53) {
               var2 = -5;
            } else {
               var2 = var1;
            }

            switch (this.e) {
               case 0:
                  if (this.midlet.game != null) {
                     this.E = 0L;
                     this.f = (byte)(this.f | 1);
                     return;
                  }
                  break;
               case 1:
                  this.c(var1, var2);
                  return;
               case 2:
                  this.e(var1, var2);
                  return;
               case 3:
               case 10:
               case 12:
               case 13:
               case 14:
               case 18:
               case 19:
               default:
                  break;
               case 4:
                  this.d(var1, var2);
                  return;
               case 5:
                  this.h(var1, var2);
                  return;
               case 6:
                  this.k(var1, var2);
                  return;
               case 7:
                  this.j(var1, var2);
                  return;
               case 8:
                  this.g(var1, var2);
                  return;
               case 9:
                  this.i(var1, var2);
                  return;
               case 11:
                  this.l(var1, var2);
                  return;
               case 15:
                  return;
               case 16:
                  this.b(var1, var2);
                  return;
               case 17:
                  this.a(var1, var2);
                  return;
               case 20:
                  this.f(var1, var2);
            }
         }
      }
   }

   private void c(int var1, int var2) {
      if (var2 == -1 || var1 == 50) {
         this.G = this.a(this.G, (byte)0, (byte)(this.j ? 6 : 5));
         this.f = (byte)(this.f | 3);
      } else if (var2 == -2 || var1 == 56) {
         this.G = this.b(this.G, (byte)(this.j ? 6 : 5), (byte)0);
         this.f = (byte)(this.f | 3);
      } else if (var1 == -8) {
         this.f = (byte)(this.f | 3);
         this.a((byte)9);
      } else {
         if (var1 == 53 || var2 == -5 || var1 == -6) {
            if (this.G == 0) {
               this.a((byte)11);
               return;
            }

            if (this.G == 1) {
               this.a((byte)4);
               return;
            }

            if (this.G == 2 && this.j) {
               if (this.midlet.isGameWon()) {
                  this.a((byte)15);
                  return;
               }

               this.a((byte)16);
               return;
            }

            if (this.G == 2 && !this.j || this.G == 3 && this.j) {
               this.a((byte)2);
               return;
            }

            if (this.G == 3 && !this.j || this.G == 4 && this.j) {
               this.a((byte)5);
               return;
            }

            if (this.G == 4 && !this.j || this.G == 5 && this.j) {
               for (int var3 = 0; var3 < 4; var3++) {
                  this.recentKeyHistory[var3] = 52;
               }

               this.a((byte)8);
               return;
            }

            if (this.G == 5 && !this.j || this.G == 6 && this.j) {
               this.a((byte)9);
            }
         }
      }
   }

   private void d(int var1, int var2) {
      if (var2 == -1 || var1 == 50) {
         this.G = this.a(this.G, (byte)0, (byte)2);
         this.f = (byte)(this.f | 3);
      } else if (var2 == -2 || var1 == 56) {
         this.G = this.b(this.G, (byte)2, (byte)0);
         this.f = (byte)(this.f | 3);
      } else if (var1 != 53 && var2 != -5 && var1 != -6) {
         if (var1 == -7 || var1 == -8) {
            this.a((byte)1);
         }
      } else if (this.midlet.saveSlotFlags[this.G] != 0) {
         this.midlet.writeSaveSlot(this.G);
      }
   }

   private void e(int var1, int var2) {
      if (var1 == -7 || var1 == -8) {
         this.a((byte)1);
         this.midlet.writeSoundAndLanguageSettings((byte)(this.midlet.soundEnabled ? 1 : 0));
      } else if (var2 == -1 || var1 == 50) {
         this.G = this.a(this.G, (byte)0, (byte)(this.c ? 2 : 1));
         this.f = (byte)(this.f | 3);
      } else if (var2 != -2 && var1 != 56) {
         if (var1 == 53 || var2 == -5 || var1 == -6) {
            if (this.G == 0) {
               if (this.midlet.soundEnabled) {
                  this.s = 0;
                  this.midlet.soundEnabled = false;
                  this.midlet.stopSoundHardOnLevelStart();
               } else {
                  this.s = 15;
                  this.midlet.soundEnabled = true;
               }

               this.f = (byte)(this.f | 3);
               return;
            }

            if (this.G == 1) {
               this.a((byte)6);
               return;
            }

            if (this.G == 2 && this.c) {
               this.a((byte)20);
            }
         }
      } else {
         this.G = this.b(this.G, (byte)(this.c ? 2 : 1), (byte)0);
         this.f = (byte)(this.f | 3);
      }
   }

   private void f(int var1, int var2) {
      if (var1 != -7 && var1 != -8) {
         if (var2 == -1 || var1 == 50) {
            this.G = this.a(this.G, (byte)0, (byte)4);
            this.f = (byte)(this.f | 3);
            return;
         }

         if (var2 == -2 || var1 == 56) {
            this.G = this.b(this.G, (byte)4, (byte)0);
            this.f = (byte)(this.f | 3);
            return;
         }

         if ((var1 == 53 || var2 == -5 || var1 == -6) && this.G != ratchetandclank.language) {
            boolean var3 = ratchetandclank.language == -1;
            ratchetandclank.language = this.G;
            this.midlet.writeSoundAndLanguageSettings((byte)(this.midlet.soundEnabled ? 1 : 0));
            System.gc();
            Game.e(20);
            this.midlet.loadStrings();
            this.f = (byte)(this.f | 3);
            if (var3) {
               this.a((byte)1);
            }
         }
      } else if (ratchetandclank.language != -1) {
         this.a((byte)2);
         return;
      }
   }

   private void g(int var1, int var2) {
      if (var1 != 52 && var2 != -3 && var1 != -7) {
         if (var1 != 54 && var2 != -4 && var1 != -6) {
            if (var1 == 53 || var2 == -5) {
               this.a((byte)1);
            }
         } else if (this.G < 4) {
            this.G++;
            this.f = (byte)(this.f | 3);
         } else {
            this.a((byte)1);
         }
      } else if (this.G > 0) {
         this.G--;
         this.f = (byte)(this.f | 3);
      } else {
         this.a((byte)1);
      }

      if (!this.midlet.game.r) {
         this.recordKeyForCheat(var1);
         this.midlet.game.r = this.checkCheatCode();
      }
   }

   public final void a(int var1, int var2) {
      if (var1 != -7 && var1 != -8) {
         if (var1 == -6) {
            this.a((byte)1);
         }
      } else {
         this.a((byte)15);
      }
   }

   public final void b(int var1, int var2) {
      if (var1 == -7 || var1 == -8) {
         this.a((byte)1);
      }
   }

   private void h(int var1, int var2) {
      if (var1 != 52 && var2 != -3 && var1 != -7) {
         if (var1 != 54 && var2 != -4 && var1 != -6) {
            if (var1 == 53 || var2 == -5) {
               this.a((byte)1);
            }
         } else if (this.G >= 13) {
            this.a((byte)1);
         } else {
            this.G++;
            if (this.G > 3 && this.G < 11) {
               this.midlet.game.aj.L++;
            } else if (this.G == 3) {
               this.midlet.game.aj.L = 0;
               this.midlet.game.aj.a((byte)8);
               this.midlet.game.aj.am = 1;
            } else if (this.G == 11) {
               this.midlet.game.aj.L = 8;
            }

            for (int var4 = 9; var4 >= 0; var4--) {
               this.midlet.game.ak[var4].a();
            }

            this.J = 0;
            this.I = 0;
            this.f = (byte)(this.f | 3);
         }
      } else if (this.G <= 0) {
         this.a((byte)1);
      } else {
         this.G--;
         if (this.G > 3 && this.G < 11) {
            this.midlet.game.aj.L--;
         } else if (this.G == 3) {
            this.midlet.game.aj.L = 0;
            this.midlet.game.aj.a((byte)8);
            this.midlet.game.aj.am = 1;
         } else if (this.G == 11) {
            this.midlet.game.aj.L = 8;
         }

         for (int var3 = 9; var3 >= 0; var3--) {
            this.midlet.game.ak[var3].a();
         }

         this.J = 0;
         this.I = 0;
         this.f = (byte)(this.f | 3);
      }
   }

   private void i(int var1, int var2) {
      if (var1 == -7 || var1 == -8) {
         this.a((byte)1);
      } else if (var2 == -2 || var1 == 56) {
         this.G = this.a(this.G, (byte)0, (byte)1);
         this.f = (byte)(this.f | 3);
      } else if (var2 != -1 && var1 != 50) {
         if (var1 == 53 || var2 == -5 || var1 == -6) {
            if (this.G == 0) {
               this.a((byte)1);
               return;
            }

            if (this.G == 1) {
               this.midlet.stopSoundHardOnLevelStart();
               this.midlet.notifyDestroyed();
            }
         }
      } else {
         this.G = this.b(this.G, (byte)1, (byte)0);
         this.f = (byte)(this.f | 3);
      }
   }

   private void j(int var1, int var2) {
      if (var1 != -7 && var1 != -8) {
         if (var2 == -1 || var1 == 50) {
            this.G = this.a(this.G, (byte)0, (byte)1);
            this.f = (byte)(this.f | 3);
         } else if (var2 != -2 && var1 != 56) {
            if (var1 == 53 || var2 == -5 || var1 == -6) {
               if (this.G == 0) {
                  if (this.h) {
                     this.a((byte)11);
                     return;
                  }

                  this.a((byte)6);
                  return;
               }

               if (this.G == 1) {
                  if (this.h) {
                     this.midlet.game.ac = this.H;
                     this.H = -1;
                     this.midlet.startNewGame(0);
                     return;
                  }

                  this.midlet.clearSaveSlot(this.H);
                  this.H = -1;
                  this.midlet.refreshSaveSlotSummaries();
                  this.f = (byte)(this.f | 3);
                  this.a((byte)6);
               }
            }
         } else {
            this.G = this.b(this.G, (byte)1, (byte)0);
            this.f = (byte)(this.f | 3);
         }
      } else if (this.h) {
         this.a((byte)11);
      } else {
         this.a((byte)6);
      }
   }

   private void k(int var1, int var2) {
      if (var1 == -7 || var1 == -8) {
         this.a((byte)2);
      } else if (var2 == -1 || var1 == 50) {
         this.G = this.a(this.G, (byte)0, (byte)2);
         this.f = (byte)(this.f | 3);
      } else if (var2 != -2 && var1 != 56) {
         if (var1 == 53 || var2 == -5 || var1 == -6) {
            if (this.midlet.saveSlotFlags[this.G] == 0) {
               return;
            }

            this.H = this.G;
            this.h = false;
            this.a((byte)7);
         }
      } else {
         this.G = this.b(this.G, (byte)2, (byte)0);
         this.f = (byte)(this.f | 3);
      }
   }

   private void l(int var1, int var2) {
      if (var1 == -7 || var1 == -8) {
         this.a((byte)1);
      } else if (var2 == -1 || var1 == 50) {
         this.G = this.a(this.G, (byte)0, (byte)2);
         this.f = (byte)(this.f | 3);
      } else if (var2 != -2 && var1 != 56) {
         if (var1 == 53 || var2 == -5 || var1 == -6) {
            if (this.midlet.saveSlotFlags[this.G] == 0) {
               this.midlet.game.ac = this.G;
               this.midlet.startNewGame(0);
               return;
            }

            this.H = this.G;
            this.h = true;
            this.a((byte)7);
         }
      } else {
         this.G = this.b(this.G, (byte)2, (byte)0);
         this.f = (byte)(this.f | 3);
      }
   }

   public final void a(Graphics var1) {
      if (this.u <= 0) {
         if (this.e == 0 || Game.aG != null && Game.aH != null && Game.aF != null) {
            if (this.e != 0) {
               var1.setClip(0, 0, 176, 220);
            } else {
               var1.setClip(0, 0, 176, 220);
            }

            if (this.o) {
               var1.setColor(0);
               var1.fillRect(0, 0, 176, 220);
            } else {
               switch (this.e) {
                  case 0:
                     this.b(var1);
                     return;
                  case 1:
                     this.d(var1);
                     return;
                  case 2:
                     this.h(var1);
                     return;
                  case 4:
                     this.e(var1);
                     return;
                  case 5:
                     this.a(var1, (int)this.G);
                     return;
                  case 6:
                     this.k(var1);
                     return;
                  case 7:
                     this.l(var1);
                     return;
                  case 8:
                     this.b(var1, this.G);
                     return;
                  case 9:
                     this.j(var1);
                     return;
                  case 10:
                     var1.setClip(0, 0, 176, 220);
                     var1.setColor(0);
                     var1.fillRect(0, 0, 176, 220);
                     return;
                  case 11:
                     this.c(var1);
                     return;
                  case 15:
                     return;
                  case 16:
                     this.g(var1);
                     return;
                  case 17:
                     this.f(var1);
                     return;
                  case 20:
                     this.i(var1);
                  case 3:
                  case 12:
                  case 13:
                  case 14:
                  case 18:
                  case 19:
               }
            }
         }
      }
   }

   private void i() {
      switch (this.e) {
         case 0:
            this.j();
            return;
         case 1:
            if (d <= 100) {
               d++;
               return;
            }

            this.midlet.game.aj.n();
            this.midlet.game.ai[0].i();
            this.midlet.game.ai[1].i();
            this.midlet.game.ai[2].i();
            this.midlet.game.ai[3].i();
            this.f = (byte)(this.f | 1);
            this.midlet.game.aj.ab = this.midlet.game.aj.ab + this.midlet.game.aj.ad;
            this.midlet.game.ai[0].ab = this.midlet.game.ai[0].ab + this.midlet.game.ai[0].ad;
            this.midlet.game.ai[1].ab = this.midlet.game.ai[1].ab + this.midlet.game.ai[1].ad;
            this.midlet.game.ai[2].ab = this.midlet.game.ai[2].ab + this.midlet.game.ai[2].ad;
            this.midlet.game.ai[3].ab = this.midlet.game.ai[3].ab + this.midlet.game.ai[3].ad;
            if (this.midlet.game.aj.ab >> 8 > 7 * A && this.midlet.game.aj.ao) {
               this.midlet.game.aj.ao = false;
               this.midlet.game.aj.ad = (short)(this.midlet.game.aj.ad * -1);
               this.midlet.game.ai[0].ao = false;
               this.midlet.game.ai[0].ad = (short)(this.midlet.game.ai[0].ad * -1);
               this.midlet.game.ai[0].ab = 12 * A << 8;
               this.midlet.game.ai[1].ad = -1536;
               this.midlet.game.ai[2].ad = -1536;
               this.midlet.game.ai[3].ad = -1536;
               return;
            }

            if (this.midlet.game.ai[0].ab >> 8 < 0 && !this.midlet.game.ai[0].ao) {
               d = 0;
               this.midlet.game.aj.ao = true;
               this.midlet.game.aj.ad = (short)(this.midlet.game.aj.ad * -1);
               this.midlet.game.aj.ab = -30720;
               this.midlet.game.ai[0].ad = (short)(this.midlet.game.ai[0].ad * -1);
               this.midlet.game.ai[0].ao = true;
               this.midlet.game.ai[0].ab = -10752;
               this.midlet.game.ai[1].ab = 9 * A << 8;
               this.midlet.game.ai[1].ad = 0;
               this.midlet.game.ai[2].ab = 10 * A << 8;
               this.midlet.game.ai[2].ad = 0;
               this.midlet.game.ai[3].ab = 11 * A << 8;
               this.midlet.game.ai[3].ad = 0;
               return;
            }
            break;
         case 5:
            this.f = (byte)(this.f | 1);
            this.midlet.game.aj.n();
            if (this.G == 3) {
               if (this.I > 70) {
                  this.midlet.game.aj.a((byte)0);
                  this.midlet.game.aj.am = 0;
                  this.I = 0;
               } else if (this.I == 1 || this.I == 10 || this.I == 20) {
                  this.midlet.game.aj.a((byte)8);
                  this.midlet.game.aj.am = 1;
               }

               this.I++;
               return;
            }

            if (this.J < 3) {
               if (this.G > 3 && this.G < 11 && this.I > 10) {
                  this.midlet.game.aj.p();
                  this.I = 0;
                  this.J++;

                  for (int var1 = 0; var1 < 8; var1++) {
                     if (this.midlet.game.aj.M[var1] == 0) {
                        this.midlet.game.aj.M[var1] = 30;
                     }
                  }
               }
            } else if (this.I > 50) {
               this.J = 0;
               this.I = 0;
            } else if (this.I == 1) {
               this.midlet.game.aj.a((byte)0);
               this.midlet.game.aj.am = 0;
            }

            this.I++;

            for (int var2 = 9; var2 >= 0; var2--) {
               this.a(this.midlet.game.ak[var2]);
            }
      }
   }

   private void j() {
      if (this.E != 0L && this.E + 2000L >= System.currentTimeMillis()) {
         if (this.splashImages[this.D] == null) {
            switch (this.D) {
               case 0:
                  try {
                     if (this.splashImages[1] == null) {
                        this.splashImages[1] = Image.createImage("/intro_handheld.png");
                     }

                     return;
                  } catch (Exception var2) {
                     return;
                  }
               case 1:
                  try {
                     if (this.splashImages[2] == null) {
                        this.splashImages[2] = Image.createImage("/intro_ratchet.png");
                     }

                     return;
                  } catch (Exception var3) {
                  }
            }
         }
      } else {
         if (this.D == 0) {
            this.D = 1;
            this.f = (byte)(this.f | 1);
            this.E = System.currentTimeMillis();
            return;
         }

         if (this.D == 1) {
            this.D = 2;
            this.f = (byte)(this.f | 1);
            this.E = System.currentTimeMillis();
            return;
         }

         if (this.D == 2) {
            this.f = (byte)(this.f | 1);
            this.E = System.currentTimeMillis();
            this.splashImages[2] = null;
            this.splashImages[1] = null;
            this.splashImages[0] = null;
            System.gc();
            Thread.yield();
            if (ratchetandclank.language >= 0) {
               this.a((byte)1);
               return;
            }

            this.a((byte)20);
            return;
         }
      }
   }

   public final void a(Command var1, Displayable var2) {
      if (var2 == this.k) {
         this.a(var1);
      }
   }

   public final void b(Command var1, Displayable var2) {
      if (var2 == this.k) {
         this.a(var1);
      } else if (var1.getCommandType() == 4 && x >= 0) {
         this.b(-6);
      } else {
         if (var1.getCommandType() == 2 && y >= 0) {
            this.b(-7);
         }
      }
   }

   public final void a(Graphics var1, int var2, int var3, Object var4) {
      var1.setClip(0, 0, 176, 220);
      ratchetandclank.currentFont = ratchetandclank.largeFont;
      if (var2 >= 0) {
         String var5 = ratchetandclank.strings[var2];
         int var6 = ratchetandclank.largeFont.a;
         int var7 = 220 - var6;
         ratchetandclank.largeFont.a(var5);
         var1.setColor(6580223);
         var1.setColor(14474495);
         ratchetandclank.currentFont.a(var1, var5, 1, var7 + 1, 20);
      }

      if (var3 >= 0) {
         String var9 = ratchetandclank.strings[var3];
         int var10 = ratchetandclank.largeFont.a;
         int var11 = 220 - var10;
         int var8 = ratchetandclank.largeFont.a(var9);
         var1.setColor(6580223);
         var1.setColor(14474495);
         ratchetandclank.currentFont.a(var1, var9, 176 - var8 - 1, var11 + 1, 20);
      }
   }

   private int a(Graphics var1, String var2, int var3, int var4, int var5, boolean var6, int var7) {
      ratchetandclank.currentFont = var6 ? ratchetandclank.smallFontAlias : ratchetandclank.smallFont;
      if (var6) {
         this.midlet.introManager.i = var2;
      }

      var1.setColor(var7);
      return this.midlet.game.a(var1, var2, var3, var4, 17, Game.ap.getWidth() - 10);
   }

   public final int a(Graphics var1, String var2, int var3, int var4, int var5, boolean var6) {
      return this.a(var1, var2, var3, var4, var5, var6, var6 ? 1175023 : 14474495);
   }

   public final int b(Graphics var1, String var2, int var3, int var4, int var5, boolean var6) {
      return this.a(var1, var2, var3, var4, var5, var6, var6 ? 16777215 : 14474495);
   }

   public final int a(Graphics var1, byte var2, int var3, int var4, int var5, boolean var6) {
      int var7 = 0;
      if (this.midlet.saveSlotFlags[var2] == 0) {
         var7 = this.b(var1, var2 + 1 + ratchetandclank.strings[31], var3, var4, 17, var6);
      } else {
         Object[] var8 = new Object[]{new Integer(var2 + 1), new String(this.c(this.midlet.saveSlotTimes[var2]))};
         String var9 = this.midlet.game.a(ratchetandclank.strings[32], var8);
         var7 = this.a(var1, var9, var3, var4, var5, var6);
      }

      return var7;
   }

   public final int a(Graphics var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      boolean var8 = false;
      Object[] var9 = new Object[]{new Integer(var3)};
      String var10 = this.midlet.game.a(ratchetandclank.strings[var2], var9);
      return this.a(var1, var10, var4, var5, var6, var7);
   }

   public final int a(Graphics var1, String var2) {
      ratchetandclank.currentFont = ratchetandclank.largeFont;
      var1.setColor(14474495);
      return this.midlet.game.a(var1, var2, 88, 5, 17, 176 - 2 * this.b() - 10);
   }

   public final void a(Graphics var1, int var2, int var3) {
      ratchetandclank.currentFont = ratchetandclank.smallFontAlias;
      var1.setClip(0, 219, 1, 1);
      int var4 = this.a(var1, this.i, 0, 0, 17, true, 1175023);
      int var5 = (176 - Game.ap.getWidth()) / 2;
      int var6 = Game.ap.getHeight();
      var1.setClip(var5, var3 - 3, Game.ap.getWidth(), var6 / 3);
      var1.drawImage(Game.ap, var5, var3 - 3, 20);
      int var7 = var3 + var6 / 3 - 3;

      for (int var8 = var6 / 3; var7 < var3 + var4 - var6 / 3 + 3; var7 += var8) {
         if (var7 + var8 > var3 + var4 - var6 / 3 + 3) {
            var8 -= var7 + var8 - (var3 + var4 - var6 / 3 + 3);
         }

         var1.setClip(var5, var7, Game.ap.getWidth(), var8);
         var1.drawImage(Game.ap, var5, var7 - var6 / 3, 20);
      }

      var1.setClip(var5, var3 + var4 - var6 / 3 + 3, Game.ap.getWidth(), var6 / 3);
      var1.drawImage(Game.ap, var5, var3 + var4 - var6 / 3 + 3 - 2 * var6 / 3 - 1, 20);
      var1.setClip(0, 0, 176, 220);
      this.a(var1, this.i, var2, var3 - 1, 17, false, 1175023);
   }

   public final void b(Graphics var1, int var2, int var3) {
      Object[] var4 = new Object[]{new Integer(var2), new Integer(var3)};
      String var5 = this.midlet.game.a(ratchetandclank.strings[36], var4);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      this.a(var1, var5, 88, 220 - ratchetandclank.currentFont.a + 1, 17, false);
   }

   public final String c(int var1) {
      int var2 = var1 / 1000;
      int var4;
      int var3 = (var4 = var1 / 60000) / 60;
      var1 = var4 % 60;
      var2 %= 60;
      return var3 + (var1 < 10 ? ":0" : ":") + var1 + (var2 < 10 ? ":0" : ":") + var2;
   }

   private byte a(byte var1, byte var2, byte var3) {
      return var1 > var2 ? --var1 : var3;
   }

   private byte b(byte var1, byte var2, byte var3) {
      return var1 < var2 ? ++var1 : var3;
   }

   public final void b(Graphics var1) {
      boolean var2 = false;
      boolean var3 = false;
      if (this.D == 0) {
         this.f = (byte)(this.f | 1);
         if ((this.f & 1) != 0) {
            try {
               if (this.splashImages[0] == null) {
                  this.splashImages[0] = Image.createImage("/intro_publisher.png");
                  Game.e(30);
               }
            } catch (Exception var5) {
            }

            var1.setColor(0);
            var1.fillRect(0, 0, 176, 220);
            var1.drawImage(this.splashImages[this.D], 88, 220 - this.splashImages[this.D].getHeight() >> 1, 17);
            this.splashImages[this.D] = null;
            System.gc();
            Game.e(20);
         }

         var1.setClip(0, 110, 176, 110);
         var1.setColor(13369344);
         var1.fillRect(39, 206, b * 100 / 95, 6);
         var1.setColor(16777215);
         var1.drawRect(38, 206, 102, 6);
      } else if (this.splashImages[this.D] != null) {
         int var4 = 0;
         if (this.D == 1) {
            var4 = 16777215;
         }

         var1.setClip(0, 0, 176, 220);
         var1.setColor(var4);
         var1.fillRect(0, 0, 176, 220);
         var1.setClip(88 - (this.splashImages[this.D].getWidth() >> 1), 110 - (this.splashImages[this.D].getHeight() >> 1), this.splashImages[this.D].getWidth(), this.splashImages[this.D].getHeight());
         var1.drawImage(this.splashImages[this.D], 88 - (this.splashImages[this.D].getWidth() >> 1), 110 - (this.splashImages[this.D].getHeight() >> 1), 20);
         this.splashImages[0] = null;
         this.splashImages[this.D] = null;
      }
   }

   public final void d(int var1) {
      b = (this.M - 4) * 100 / 39;
      System.gc();
      Game.e(10);

      try {
         Thread.yield();
      } catch (Exception var3) {
      }
   }

   private void d(Graphics var1) {
      int var2 = 0;
      this.a(var1, (byte)0);
      this.midlet.game.aj.a(var1, this.midlet.game.aj.ah, 0, 11);
      this.midlet.game.ai[0].a(var1, 0, this.midlet.game.ai[0].ah, 0, 11);
      this.midlet.game.ai[1].a(var1, 1, this.midlet.game.ai[1].ah, 0, 11);
      this.midlet.game.ai[2].a(var1, 1, this.midlet.game.ai[1].ah, 0, 11);
      this.midlet.game.ai[3].a(var1, 1, this.midlet.game.ai[1].ah, 0, 11);
      var1.setClip(0, 0, 176, 220);
      var1.drawImage(Game.as, 88, 4, 17);
      var2 = 4 + Game.as.getHeight() + 4;
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      int var3 = 0;
      var3++;
      int var5;
      this.g[0] = var5 = Game.ap.getHeight() / 2 + var2;
      var3++;
      this.g[1] = var2 = Game.ap.getHeight() / 2 + this.a(var1, ratchetandclank.strings[1], 88, var5, 0, this.G == 0);
      var3++;
      int var7;
      this.g[2] = var7 = Game.ap.getHeight() / 2 + this.a(var1, ratchetandclank.strings[2], 88, var2, 0, this.G == 1);
      if (this.j) {
         var3++;
         this.g[3] = var7 = Game.ap.getHeight() / 2 + this.a(var1, ratchetandclank.strings[286], 88, var7, 0, this.G == 2);
      }

      this.g[var3++] = var2 = Game.ap.getHeight() / 2 + this.a(var1, ratchetandclank.strings[3], 88, var7, 0, this.G == 2 && !this.j || this.G == 3 && this.j);
      int var9;
      this.g[var3++] = var9 = Game.ap.getHeight() / 2 + this.a(var1, ratchetandclank.strings[4], 88, var2, 0, this.G == 3 && !this.j || this.G == 4 && this.j);
      this.g[var3] = var2 = Game.ap.getHeight() / 2 + this.a(var1, ratchetandclank.strings[5], 88, var9, 0, this.G == 4 && !this.j || this.G == 5 && this.j);
      this.a(var1, ratchetandclank.strings[6], 88, var2, 0, this.G == 5 && !this.j || this.G == 6 && this.j);
      this.a(var1, 0 + A + (A >> 1), this.g[this.G]);
      this.a(var1, 7, -1, this);
   }

   private void e(Graphics var1) {
      this.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.midlet.game.e(var1);
      this.a(var1, ratchetandclank.strings[2]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      int var2;
      this.g[0] = var2 = Game.ap.getHeight() / 2 + 100;
      int var4;
      this.g[1] = var4 = Game.ap.getHeight() / 2 + this.a(var1, (byte)0, 0 + A + (A >> 1), var2, 0, this.G == 0);
      this.g[2] = var2 = Game.ap.getHeight() / 2 + this.a(var1, (byte)1, 0 + A + (A >> 1), var4, 0, this.G == 1);
      this.a(var1, (byte)2, 0 + A + (A >> 1), var2, 0, this.G == 2);
      this.a(var1, 7, 8, this);
      this.a(var1, 0 + A + (A >> 1), this.g[this.G]);
   }

   private void b(Graphics var1, int var2) {
      int var3 = 0;
      this.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      var3 = this.a(var1, ratchetandclank.strings[5]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      var3 += ratchetandclank.smallFont.lineHeight;
      if (var2 == 0) {
         this.midlet.game.a(var1, ratchetandclank.strings[284], 88, var3, 17, 176 - 2 * this.b());
      } else if (var2 == 1) {
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[86], 88, var3, 17, 176 - 2 * this.b());
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[87], 176, var3, 17, 176 - 2 * this.b()) + ratchetandclank.currentFont.a * 2;
         this.midlet.game.a(var1, ratchetandclank.strings[88], 88, var3, 17, 176 - 2 * this.b());
      } else if (var2 == 2) {
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[89], 88, var3, 17, 176 - 2 * this.b());
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[90], 88, var3, 17, 176 - 2 * this.b()) + ratchetandclank.currentFont.a;
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[91], 88, var3, 17, 176 - 2 * this.b());
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[92], 88, var3, 17, 176 - 2 * this.b()) + ratchetandclank.currentFont.a;
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[93], 88, var3, 17, 176 - 2 * this.b());
         this.midlet.game.a(var1, ratchetandclank.strings[94], 88, var3, 17, 176 - 2 * this.b());
      } else if (var2 == 3) {
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[95], 88, var3, 17, 176 - 2 * this.b());
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[96], 88, var3, 17, 176 - 2 * this.b()) + ratchetandclank.currentFont.a;
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[97], 88, var3, 17, 176 - 2 * this.b());
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[98], 88, var3, 17, 176 - 2 * this.b()) + ratchetandclank.currentFont.a;
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[99], 88, var3, 17, 176 - 2 * this.b());
         this.midlet.game.a(var1, ratchetandclank.strings[100], 88, var3, 17, 176 - 2 * this.b());
      } else if (var2 == 4) {
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[101], 88, var3, 17, 176 - 2 * this.b());
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[102], 176, var3, 17, 176 - 2 * this.b());
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[103], 88, var3, 17, 176 - 2 * this.b()) + ratchetandclank.currentFont.a * 2;
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[84], 88, var3, 17, 176 - 2 * this.b());
         String var4;
         int var5;
         String var6;
         if ((var5 = (var4 = ratchetandclank.strings[85]).indexOf("??")) > 0 && (var6 = this.midlet.getAppProperty("MIDlet-Version")) != null) {
            var4 = var4.substring(0, var5) + var6;
         }

         this.midlet.game.a(var1, var4, 88, var3, 17, 176 - 2 * this.b());
      }

      this.b(var1, var2 + 1, 5);
      this.a(var1, 9, 8, this);
   }

   private int c(Graphics var1, int var2, int var3) {
      var3 = this.midlet.game.a(var1, ratchetandclank.strings[var2 == 0 ? 312 : 313], -1, var3, 17, this.midlet.game.g());
      byte var4 = 33;

      for (int var5 = 0; var5 < 8; var4 += 14) {
         var1.setClip(var4, var3, 12, 12);
         var1.drawImage(Game.am, var4 - this.w[var2][var5] * 12, var3, 20);
         var5++;
      }

      var1.setClip(0, 0, 176, 220);
      return var3 + 14;
   }

   private void f(Graphics var1) {
      this.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      int var2 = this.a(var1, ratchetandclank.strings[286]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      var2 += ratchetandclank.smallFont.lineHeight / 2;
      var2 = this.a(var1, ratchetandclank.strings[290], -1, var2, 17, false);
      var2 = this.a(var1, this.m, -1, var2, 17, false) + ratchetandclank.currentFont.a / 2;
      ratchetandclank.currentFont = ratchetandclank.largeFont;
      var1.setColor(14474495);
      var2 = this.c(var1, 1, var2) + ratchetandclank.currentFont.a / 2;
      var2 = this.c(var1, 0, var2) + ratchetandclank.currentFont.a / 2;
      this.a(var1, ratchetandclank.strings[288], -1, var2, 17, false);
      this.a(var1, 39, 8, this);
   }

   private void g(Graphics var1) {
      this.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      int var2 = this.a(var1, ratchetandclank.strings[286]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      var2 += 5;
      var2 = this.midlet.game.a(var1, ratchetandclank.strings[287], this.b(), var2, 17, 176 - 2 * this.b());
      var2 += 5;
      this.midlet.game.a(var1, ratchetandclank.strings[288], this.b(), var2, 17, 176 - 2 * this.b());
      this.a(var1, -1, 8, this);
   }

   private void h(Graphics var1) {
      this.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.midlet.game.e(var1);
      this.a(var1, ratchetandclank.strings[27]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      int var2;
      this.g[0] = var2 = Game.ap.getHeight() / 2 + 100;
      int var3;
      this.g[1] = var3 = Game.ap.getHeight() / 2 + this.a(var1, this.midlet.soundEnabled ? ratchetandclank.strings[28] : ratchetandclank.strings[29], 0 + A + (A >> 1), var2, 0, this.G == 0);
      var2 = Game.ap.getHeight() / 2 + this.a(var1, ratchetandclank.strings[33], 0 + A + (A >> 1), var3, 0, this.G == 1);
      this.g[2] = var2;
      this.a(var1, ratchetandclank.strings[314], 88, var2, 17, this.G == 2);
      this.a(var1, 7, 8, this);
      this.a(var1, 0 + A + (A >> 1), this.g[this.G]);
   }

   private void i(Graphics var1) {
      this.a(var1, (byte)0);
      String var2 = ratchetandclank.language == -1 ? ratchetandclank.strings[7] + " " + ratchetandclank.strings[314] : ratchetandclank.strings[314];
      int var3 = this.a(var1, var2);
      var3 += 28;
      this.g[0] = var3;
      int var5;
      this.g[1] = var5 = Game.ap.getHeight() / 2 + this.a(var1, ratchetandclank.strings[315], 0, var3, 0, this.G == 0);
      this.g[2] = var3 = Game.ap.getHeight() / 2 + this.a(var1, ratchetandclank.strings[316], 0, var5, 0, this.G == 1);
      int var7;
      this.g[3] = var7 = Game.ap.getHeight() / 2 + this.a(var1, ratchetandclank.strings[317], 0, var3, 0, this.G == 2);
      this.g[4] = var3 = Game.ap.getHeight() / 2 + this.a(var1, ratchetandclank.strings[318], 0, var7, 0, this.G == 3);
      Game.ap.getHeight();
      this.a(var1, ratchetandclank.strings[319], 0, var3, 0, this.G == 4);
      this.a(var1, 7, ratchetandclank.language == -1 ? -1 : 8, this);
      this.a(var1, 0, this.g[this.G]);
   }

   public final void a(Graphics var1, int var2) {
      int var3 = 0;
      int var4 = 88 - Game.aq.getWidth() / 2 + 10;
      int var5 = 88 + Game.aq.getWidth() / 2 - 10;
      int var6 = Game.aJ.getWidth();
      int var7 = Game.aJ.getHeight() >> 3;
      this.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.midlet.game.e(var1);
      var3 = this.a(var1, ratchetandclank.strings[4]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      if (var2 < 3) {
         this.a(var1, ratchetandclank.strings[76], 0 + (A >> 1), var3, 0, false);
      } else if (var2 < 11) {
         this.a(var1, ratchetandclank.strings[262], 88, var3, 17, false);
      } else {
         this.a(var1, ratchetandclank.strings[271], 88, var3, 17, false);
      }

      var3 = this.midlet.game.k() - 3;
      if (var2 == 0) {
         var3 += ratchetandclank.currentFont.a;
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[77], var4, var3, 0, var5);
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[78], var4, var3, 0, var5);
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[79], var4, var3, 0, var5);
         this.midlet.game.a(var1, ratchetandclank.strings[80], var4, var3, 0, var5);
      } else if (var2 == 1) {
         var3 += ratchetandclank.currentFont.a;
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[258], var4, var3, 0, var5);
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[259], var4, var3, 0, var5);
         this.midlet.game.a(var1, ratchetandclank.strings[81], var4, var3, 0, var5);
      } else if (var2 == 2) {
         var3 += ratchetandclank.currentFont.a;
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[82], var4, var3, 0, var5);
         var3 = this.midlet.game.a(var1, ratchetandclank.strings[83], var4, var3, 0, var5);
         this.midlet.game.a(var1, ratchetandclank.strings[260], var4, var3, 0, var5);
      } else if (var2 == 3) {
         var3 += ratchetandclank.currentFont.a;
         var3 = this.a(var1, ratchetandclank.strings[48], 0 + (A >> 1), var3, 0, false) + ratchetandclank.currentFont.a;
         this.midlet.game.a(var1, ratchetandclank.strings[263], var4, var3, 0, var5);
      } else if (var2 == 4) {
         var3 += ratchetandclank.currentFont.a;
         var3 = this.a(var1, ratchetandclank.strings[49], 0 + (A >> 1), var3, 0, false) + ratchetandclank.currentFont.a;
         this.midlet.game.a(var1, ratchetandclank.strings[264], var4, var3, 0, var5);
      } else if (var2 == 5) {
         var3 += ratchetandclank.currentFont.a;
         var3 = this.a(var1, ratchetandclank.strings[50], 0 + (A >> 1), var3, 0, false) + ratchetandclank.currentFont.a;
         this.midlet.game.a(var1, ratchetandclank.strings[265], var4, var3, 0, var5);
      } else if (var2 == 6) {
         var3 += ratchetandclank.currentFont.a;
         var3 = this.a(var1, ratchetandclank.strings[51], 0 + (A >> 1), var3, 0, false) + ratchetandclank.currentFont.a;
         this.midlet.game.a(var1, ratchetandclank.strings[266], var4, var3, 0, var5);
      } else if (var2 == 7) {
         var3 += ratchetandclank.currentFont.a;
         var3 = this.a(var1, ratchetandclank.strings[52], 0 + (A >> 1), var3, 0, false) + ratchetandclank.currentFont.a;
         this.midlet.game.a(var1, ratchetandclank.strings[267], var4, var3, 0, var5);
      } else if (var2 == 8) {
         var3 += ratchetandclank.currentFont.a;
         var3 = this.a(var1, ratchetandclank.strings[53], 0 + (A >> 1), var3, 0, false) + ratchetandclank.currentFont.a;
         this.midlet.game.a(var1, ratchetandclank.strings[268], var4, var3, 0, var5);
      } else if (var2 == 9) {
         var3 += ratchetandclank.currentFont.a;
         var3 = this.a(var1, ratchetandclank.strings[54], 0 + (A >> 1), var3, 0, false) + ratchetandclank.currentFont.a;
         this.midlet.game.a(var1, ratchetandclank.strings[269], var4, var3, 0, var5);
      } else if (var2 == 10) {
         var3 += ratchetandclank.currentFont.a;
         var3 = this.a(var1, ratchetandclank.strings[55], 0 + (A >> 1), var3, 0, false) + ratchetandclank.currentFont.a;
         this.midlet.game.a(var1, ratchetandclank.strings[270], var4, var3, 0, var5);
      } else if (var2 == 11) {
         var1.setClip(176 - var6 >> 1, 2 * B - (B >> 1) - 2, var6, var7);
         var1.drawImage(Game.aJ, 176 - var6 >> 1, 2 * B - (B >> 1) - 2, 0);
         var1.setClip(0, 0, 176, 220);
         var3 += ratchetandclank.currentFont.a;
         var3 = this.a(var1, ratchetandclank.strings[272], 0 + (A >> 1), var3, 0, false) + ratchetandclank.currentFont.a;
         this.midlet.game.a(var1, ratchetandclank.strings[275], var4, var3, 0, var5);
      } else if (var2 == 12) {
         var1.setClip(176 - var6 >> 1, 2 * B - (B >> 1) - 2, var6, var7);
         var1.drawImage(Game.aJ, 176 - var6 >> 1, 2 * B - (B >> 1) - var7 - 2, 0);
         var1.setClip(0, 0, 176, 220);
         var3 += ratchetandclank.currentFont.a;
         var3 = this.a(var1, ratchetandclank.strings[273], 0 + (A >> 1), var3, 0, false) + ratchetandclank.currentFont.a;
         this.midlet.game.a(var1, ratchetandclank.strings[276], var4, var3, 0, var5);
      } else if (var2 == 13) {
         var1.setClip(176 - var6 >> 1, 2 * B - (B >> 1) - 2, var6, var7);
         var1.drawImage(Game.aJ, 176 - var6 >> 1, 2 * B - (B >> 1) - 2 * var7 - 2, 0);
         var1.setClip(0, 0, 176, 220);
         var3 += ratchetandclank.currentFont.a;
         var3 = this.a(var1, ratchetandclank.strings[274], 0 + (A >> 1), var3, 0, false) + ratchetandclank.currentFont.a;
         this.midlet.game.a(var1, ratchetandclank.strings[277], var4, var3, 0, var5);
      }

      this.b(var1, var2 + 1, 14);
      this.a(var1, 9, 8, this);
      if (var2 > 2 && var2 < 11) {
         this.midlet.game.aj.a(var1, this.midlet.game.aj.ah, 0, -4);
         if (var2 > 3) {
            for (int var8 = 9; var8 >= 0; var8--) {
               this.midlet.game.ak[var8].a(var1);
            }
         }
      }
   }

   private void j(Graphics var1) {
      this.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.midlet.game.e(var1);
      this.a(var1, ratchetandclank.strings[35]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      int var2;
      this.g[0] = var2 = Game.ap.getHeight() / 2 + 100;
      int var4;
      this.g[1] = var4 = Game.ap.getHeight() / 2 + this.a(var1, ratchetandclank.strings[11], 0 + A + (A >> 1), var2, 0, this.G == 0);
      this.a(var1, ratchetandclank.strings[10], 0 + A + (A >> 1), var4, 0, this.G == 1);
      this.a(var1, 7, 8, this);
      this.a(var1, 0 + A + (A >> 1), this.g[this.G]);
   }

   private void k(Graphics var1) {
      this.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.midlet.game.e(var1);
      this.a(var1, ratchetandclank.strings[33]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      int var2;
      this.g[0] = var2 = Game.ap.getHeight() / 2 + 100;
      int var4;
      this.g[1] = var4 = Game.ap.getHeight() / 2 + this.a(var1, (byte)0, 0 + A + (A >> 1), var2, 0, this.G == 0);
      this.g[2] = var2 = Game.ap.getHeight() / 2 + this.a(var1, (byte)1, 0 + A + (A >> 1), var4, 0, this.G == 1);
      this.a(var1, (byte)2, 0 + A + (A >> 1), var2, 0, this.G == 2);
      this.a(var1, 7, 8, this);
      this.a(var1, 0 + A + (A >> 1), this.g[this.G]);
   }

   private void l(Graphics var1) {
      this.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.midlet.game.e(var1);
      ratchetandclank.currentFont = ratchetandclank.largeFont;
      var1.setColor(14474495);
      this.a(var1, ratchetandclank.strings[34]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      int var2;
      this.g[0] = var2 = Game.ap.getHeight() / 2 + 100;
      int var4;
      this.g[1] = var4 = Game.ap.getHeight() / 2 + this.a(var1, ratchetandclank.strings[11], 0 + A + (A >> 1), var2, 0, this.G == 0);
      this.a(var1, ratchetandclank.strings[10], 0 + A + (A >> 1), var4, 0, this.G == 1);
      this.a(var1, 7, 8, this);
      this.a(var1, 0 + A + (A >> 1), this.g[this.G]);
   }

   public final void c(Graphics var1) {
      this.a(var1, (byte)0);
      var1.setClip(0, 0, 176, 220);
      this.midlet.game.e(var1);
      this.a(var1, ratchetandclank.strings[283]);
      ratchetandclank.currentFont = ratchetandclank.smallFont;
      int var2;
      this.g[0] = var2 = Game.ap.getHeight() / 2 + 100;
      int var3;
      this.g[1] = var3 = Game.ap.getHeight() / 2 + this.a(var1, (byte)0, 0 + A + (A >> 1), var2, 0, this.G == 0);
      this.g[2] = var2 = Game.ap.getHeight() / 2 + this.a(var1, (byte)1, 0 + A + (A >> 1), var3, 0, this.G == 1);
      this.a(var1, (byte)2, 0 + A + (A >> 1), var2, 0, this.G == 2);
      this.midlet.introManager.a(var1, 7, 8, this);
      this.midlet.introManager.a(var1, A >> 1, this.g[this.G]);
   }

   // Decorative background-projectile tick for the menu screens
   // (called from i()'s screen-5 branch, 10 slots at a time) -- a
   // simplified duplicate of Projectile.update()'s own logic (only an
   // x>176 offscreen check, no y-height check except types 3-5), reusing
   // Projectile's confirmed field/method names directly since this
   // operates on real Game.ak[]/al[] Projectile instances.
   public final void a(Projectile var1) {
      if (var1.type != -1) {
         if (var1.type >= 6 && var1.type <= 8) {
            var1.posX = var1.posX + var1.vx;
            int var7 = var1.posX >> 8;
            var1.posY = var1.posY + var1.vy;
            if (var7 > 176) {
               var1.reset();
               return;
            }
         } else if (var1.type >= 0 && var1.type <= 2) {
            var1.posX = var1.posX + var1.vx;
            int var6 = var1.posX >> 8;
            var1.posY = var1.posY + var1.vy;
            if (var6 > 176) {
               var1.reset();
               return;
            }
         } else {
            if (var1.type >= 3 && var1.type <= 5) {
               var1.posX = var1.posX + var1.vx;
               int var5 = var1.posX >> 8;
               int var9;
               if ((var9 = var1.posY >> 8) > 220 || var5 > 176) {
                  var1.reset();
               }

               var1.posY = var1.posY - var1.vy;
               var1.vy -= 256;
               return;
            }

            if (var1.type >= 9 && var1.type <= 11) {
               if (var1.age++ == 2) {
                  var1.reset();
                  return;
               }
            } else if (var1.type >= 12 && var1.type <= 14) {
               var1.homingSeek(1);
               int var4 = var1.posX >> 8;
               if (var4 > 176) {
                  var1.reset();
                  return;
               }
            } else if (var1.type >= 15 && var1.type <= 17) {
               if (var1.age++ == 3) {
                  var1.reset();
                  return;
               }
            } else if (var1.type >= 18 && var1.type <= 20) {
               var1.homingSeek(4);
               int var2 = var1.posX >> 8;
               if (var2 > 176) {
                  var1.reset();
               }
            }
         }
      }
   }

   // Konami-style cheat-code check: recordKeyForCheat(int) shifts each
   // keypress into recentKeyHistory (a 4-entry ring buffer), and
   // checkCheatCode() compares it against CHEAT_CODE_KEYS. Called from
   // g(int,int) after any keypress on... whichever screen `g(int,int)`
   // handles (not independently identified -- see CLASS_MAP.md).
   private void recordKeyForCheat(int var1) {
      this.recentKeyHistory[0] = this.recentKeyHistory[1];
      this.recentKeyHistory[1] = this.recentKeyHistory[2];
      this.recentKeyHistory[2] = this.recentKeyHistory[3];
      this.recentKeyHistory[3] = var1;
   }

   private boolean checkCheatCode() {
      for (int var1 = 0; var1 < 4; var1++) {
         if (this.recentKeyHistory[var1] != CHEAT_CODE_KEYS[var1]) {
            System.err.println("wrong key");
            return false;
         }
      }

      System.err.println("keyUnlocked");
      return true;
   }
}
