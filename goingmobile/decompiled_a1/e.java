import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

public final class e extends Canvas implements Runnable, CommandListener {
   private Thread d;
   private ratchetandclank e;
   public byte a = -1;
   public static DirectGraphics b;
   public boolean c;

   public e(ratchetandclank var1) {
      this.e = var1;
   }

   public final void hideNotify() {
      try {
         if (this.a == 0) {
            this.e.b();
            this.a = 1;
            if (this.e.i) {
               if (this.e.c != null) {
                  this.e.c.p();
               }
            } else if (this.e.b != null) {
               this.e.b.d();
            }
         }
      } catch (Exception var2) {
      }
   }

   public final void showNotify() {
      try {
         if (this.a == -1) {
            this.a = 0;
            this.a();
         } else {
            this.hideNotify();
         }
      } catch (Exception var2) {
      }
   }

   public final void a() {
      if (this.e.i) {
         if (this.e.c != null) {
            this.e.c.q();
            return;
         }
      } else if (this.e.b != null) {
         this.e.b.e();
      }
   }

   public final void paint(Graphics var1) {
      try {
         if (this.a == 1) {
            var1.setClip(0, 0, 176, 220);
            var1.setColor(0);
            var1.fillRect(0, 0, 176, 220);
            var1.setColor(16777215);
            ratchetandclank.f.a(var1, "Press * Key...", 88, 110, 17);
         } else if (this.a == 2) {
            var1.setClip(0, 0, 176, 220);
            var1.setColor(0);
            var1.fillRect(0, 0, 176, 220);
         } else if (this.a == 0) {
            b = DirectUtils.getDirectGraphics(var1);
            if (this.e.i) {
               if (this.e.c != null) {
                  this.e.c.w(var1);
               }
            } else if (this.e.b != null) {
               this.e.b.a(var1);
            }

            b = null;
         }
      } catch (Exception var3) {
      }

      this.c = false;
   }

   public final void b() {
      this.d = new Thread(this);
      this.d.start();
   }

   public final void run() {
      while (true) {
         try {
            if (this.a == 0) {
               if (this.e.i) {
                  if (this.e.c != null) {
                     this.e.c.r();
                  }
               } else if (this.e.b != null) {
                  this.e.b.h();
               }

               if (!this.isShown()) {
                  this.hideNotify();
               }
            } else if (this.a == 2) {
               this.a();
               this.a = 0;
            }

            this.c = true;
            this.repaint();

            while (this.c) {
               Thread.sleep(10L);
            }

            Thread.sleep(33L);
         } catch (Exception var2) {
         }
      }
   }

   public final void keyPressed(int var1) {
      try {
         if (this.a == 1) {
            if (var1 == 42) {
               this.a = 2;
               this.repaint();
            }
         } else if (this.a == 0) {
            if (this.e.i) {
               if (this.e.c != null) {
                  this.e.c.b(var1);
               }
            } else if (this.e.b != null) {
               this.e.b.b(var1);
            }
         }
      } catch (Exception var3) {
      }
   }

   public final void keyReleased(int var1) {
      try {
         if (this.a == 0 && this.e.i && this.e.c != null) {
            this.e.c.c(var1);
         }
      } catch (Exception var3) {
      }
   }

   public final void commandAction(Command var1, Displayable var2) {
      try {
         if (this.a == 0) {
            if (!this.e.i && this.e.b != null) {
               this.e.b.b(var1, var2);
            }
         } else if (this.a == 1 && !this.e.i && this.e.b != null) {
            this.e.b.a(var1, var2);
         }
      } catch (Exception var4) {
      }
   }
}
