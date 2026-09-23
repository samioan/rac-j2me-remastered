// Hand-written, faithfully renamed from decompiled_a1/e.java (class e).
// a1's canvas shell: unlike the legacy build (where Game itself extends
// com.nokia.mid.ui.FullCanvas, see ../src/Game.java), a1 splits this out
// into its own plain javax.microedition.lcdui.Canvas + CommandListener,
// delegating every real callback to whichever of the MIDlet's two
// controller objects is active: ratchetandclank.introManager (type
// IntroManager -- splash + unlock-code UI, class-level rename only,
// members still obfuscated) while midlet.gameStarted is false,
// ratchetandclank.game (type Game, same class-only-renamed status) once
// it's true.
//
// canvasState: -1 = never shown yet; 0 = active (ticking + delegating
// every frame); 1 = paused, showing "Press * Key..." (entered whenever
// showNotify() fires while already shown once -- used as a resume gate,
// not just first-launch: MIDP re-shows the canvas after backgrounding);
// 2 = transitional -- set by keyPressed's '*' handler while paused, the
// tick loop sees it, calls activate() once, then drops back to 0.
import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

public final class CanvasShell extends Canvas implements Runnable, CommandListener {
   private Thread thread;
   private ratchetandclank midlet;
   public byte canvasState = -1;
   public static DirectGraphics directGraphics;
   public boolean repaintPending;

   public CanvasShell(ratchetandclank var1) {
      this.midlet = var1;
   }

   public final void hideNotify() {
      try {
         if (this.canvasState == 0) {
            this.midlet.stopSoundHard();
            this.canvasState = 1;
            if (this.midlet.gameStarted) {
               if (this.midlet.game != null) {
                  this.midlet.game.p();
               }
            } else if (this.midlet.introManager != null) {
               this.midlet.introManager.d();
            }
         }
      } catch (Exception var2) {
      }
   }

   public final void showNotify() {
      try {
         if (this.canvasState == -1) {
            this.canvasState = 0;
            this.activate();
         } else {
            this.hideNotify();
         }
      } catch (Exception var2) {
      }
   }

   public final void activate() {
      if (this.midlet.gameStarted) {
         if (this.midlet.game != null) {
            this.midlet.game.q();
            return;
         }
      } else if (this.midlet.introManager != null) {
         this.midlet.introManager.e();
      }
   }

   public final void paint(Graphics var1) {
      try {
         if (this.canvasState == 1) {
            var1.setClip(0, 0, 176, 220);
            var1.setColor(0);
            var1.fillRect(0, 0, 176, 220);
            var1.setColor(16777215);
            ratchetandclank.largeFont.drawText(var1, "Press * Key...", 88, 110, 17);
         } else if (this.canvasState == 2) {
            var1.setClip(0, 0, 176, 220);
            var1.setColor(0);
            var1.fillRect(0, 0, 176, 220);
         } else if (this.canvasState == 0) {
            directGraphics = DirectUtils.getDirectGraphics(var1);
            if (this.midlet.gameStarted) {
               if (this.midlet.game != null) {
                  this.midlet.game.w(var1);
               }
            } else if (this.midlet.introManager != null) {
               this.midlet.introManager.a(var1);
            }

            directGraphics = null;
         }
      } catch (Exception var3) {
      }

      this.repaintPending = false;
   }

   public final void startLoop() {
      this.thread = new Thread(this);
      this.thread.start();
   }

   public final void run() {
      while (true) {
         try {
            if (this.canvasState == 0) {
               if (this.midlet.gameStarted) {
                  if (this.midlet.game != null) {
                     this.midlet.game.r();
                  }
               } else if (this.midlet.introManager != null) {
                  this.midlet.introManager.h();
               }

               if (!this.isShown()) {
                  this.hideNotify();
               }
            } else if (this.canvasState == 2) {
               this.activate();
               this.canvasState = 0;
            }

            this.repaintPending = true;
            this.repaint();

            while (this.repaintPending) {
               Thread.sleep(10L);
            }

            Thread.sleep(33L);
         } catch (Exception var2) {
         }
      }
   }

   public final void keyPressed(int var1) {
      try {
         if (this.canvasState == 1) {
            if (var1 == 42) {
               this.canvasState = 2;
               this.repaint();
            }
         } else if (this.canvasState == 0) {
            if (this.midlet.gameStarted) {
               if (this.midlet.game != null) {
                  this.midlet.game.b(var1);
               }
            } else if (this.midlet.introManager != null) {
               this.midlet.introManager.b(var1);
            }
         }
      } catch (Exception var3) {
      }
   }

   public final void keyReleased(int var1) {
      try {
         if (this.canvasState == 0 && this.midlet.gameStarted && this.midlet.game != null) {
            this.midlet.game.c(var1);
         }
      } catch (Exception var3) {
      }
   }

   public final void commandAction(Command var1, Displayable var2) {
      try {
         if (this.canvasState == 0) {
            if (!this.midlet.gameStarted && this.midlet.introManager != null) {
               this.midlet.introManager.b(var1, var2);
            }
         } else if (this.canvasState == 1 && !this.midlet.gameStarted && this.midlet.introManager != null) {
            this.midlet.introManager.a(var1, var2);
         }
      } catch (Exception var4) {
      }
   }
}
