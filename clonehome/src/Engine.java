import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;

// $VF: renamed from: a
public abstract class Engine extends GameCanvas implements Runnable {
   // $VF: renamed from: k long
   private long lastFrameStart;
   // $VF: renamed from: a javax.microedition.midlet.MIDlet
   public MIDlet midlet;
   // $VF: renamed from: b e
   public SoundPlayer soundPlayer;
   private boolean l;
   // $VF: renamed from: c boolean
   public boolean soundEnabled;
   private boolean m;
   private boolean n;
   // $VF: renamed from: o int
   private int loadedBank = -1;
   // $VF: renamed from: p int[]
   private int[] resourceLengths;
   // $VF: renamed from: q byte[]
   private byte[] bankData;
   // $VF: renamed from: d byte[]
   public byte[] resourceTypes;
   // $VF: renamed from: r int
   private int pngPayloadOffset;
   // $VF: renamed from: s byte[]
   private byte[] stringBytes;
   // $VF: renamed from: t short[]
   private short[] stringOffsets;
   // $VF: renamed from: u f[]
   private Sprite[] font;
   // $VF: renamed from: v char[]
   private char[] charBuffer;
   // $VF: renamed from: w javax.microedition.lcdui.Font
   private Font systemFont = Font.getFont(0, 0, 0);
   // $VF: renamed from: x javax.microedition.lcdui.Graphics
   private Graphics tileGraphics;
   // $VF: renamed from: y int
   private int viewWidthPx;
   // $VF: renamed from: z int
   private int viewHeightPx;
   // $VF: renamed from: A int
   private int scrollX;
   // $VF: renamed from: B int
   private int scrollY;
   // $VF: renamed from: C javax.microedition.lcdui.Image
   private Image tileSheet;
   // $VF: renamed from: D int[]
   private int[] tileSrcX;
   // $VF: renamed from: E int[]
   private int[] tileSrcY;
   // $VF: renamed from: F short[]
   private short[] tileGrid;
   // $VF: renamed from: G int[]
   private int[] animatedTiles;
   // $VF: renamed from: H int
   private int tileWidth;
   // $VF: renamed from: I int
   private int tileHeight;
   // $VF: renamed from: J int
   private int gridCols;
   // $VF: renamed from: K int
   private int gridRows;
   // $VF: renamed from: L boolean
   private boolean tileDrawDisabled;
   private short[] M;
   private short[] N;
   private short[] O;
   private short[] P;
   private int Q;
   private short[] R;
   private int S;
   private short[] T;
   private int U;
   private byte[] V;
   private int W;
   private byte[] X;
   private int Y;
   private short[] Z;
   private int aa;
   // $VF: renamed from: ab byte[]
   private static final byte[] keyCodeTable = new byte[32];
   // $VF: renamed from: ac int
   private volatile int keysHeld;
   // $VF: renamed from: e int
   public volatile int keysPressed;
   // $VF: renamed from: f int
   public volatile int keysReleased;
   // $VF: renamed from: ad int
   private volatile int pendingPressed;
   // $VF: renamed from: ae int
   private volatile int pendingHeld;
   // $VF: renamed from: af int
   private volatile int pendingReleased;
   // $VF: renamed from: g int
   public int frameTime = 10;
   // $VF: renamed from: h int
   public int minFrameTime = 10;
   // $VF: renamed from: i int
   public int maxFrameTime = 100;
   // $VF: renamed from: ag boolean
   private boolean resetFrameTiming;
   // $VF: renamed from: ah int
   private int canvasWidth;
   // $VF: renamed from: ai int
   private int canvasHeight;
   // $VF: renamed from: aj int
   private int canvasOffsetX;
   // $VF: renamed from: ak int
   private int canvasOffsetY;
   // $VF: renamed from: j boolean
   public boolean bordersCleared;
   // $VF: renamed from: al boolean
   private boolean clearBorders;
   // $VF: renamed from: am java.lang.Thread
   private Thread gameThread;
   // $VF: renamed from: an boolean
   private volatile boolean threadRunning;
   // $VF: renamed from: ao boolean
   private volatile boolean shuttingDown;
   // $VF: renamed from: ap long
   private volatile long lifecycleTime;
   // $VF: renamed from: aq int
   private volatile int pendingLifecycle = -1;
   // $VF: renamed from: ar int
   private int lifecycleState = 2;

   static {
      System.currentTimeMillis();
      keyCodeTable[1] = -1;
      keyCodeTable[2] = -3;
      keyCodeTable[3] = 35;
      keyCodeTable[4] = -8;
      keyCodeTable[5] = -4;
      keyCodeTable[6] = -2;
      keyCodeTable[8] = -5;
      keyCodeTable[10] = 42;
      keyCodeTable[13] = -36;
      keyCodeTable[14] = -37;
      keyCodeTable[15] = -11;

      for (int var0 = 0; var0 < 10; var0++) {
         keyCodeTable[16 + var0] = (byte)(48 + var0);
      }

      keyCodeTable[27] = -6;
      keyCodeTable[29] = -7;
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics) void
   public abstract void render(Graphics var1);

   // $VF: renamed from: a () void
   public abstract void update();

   // $VF: renamed from: a (int) byte[]
   public static byte[] readRecord(int var0) {
      RecordStore var2 = null;

      byte[] var1;
      try {
         int var3 = (var2 = RecordStore.openRecordStore(String.valueOf(var0), false)).getNumRecords();
         int var4 = 0;

         for (int var5 = 1; var5 <= var3; var5++) {
            var4 += var2.getRecordSize(var5);
         }

         var1 = new byte[var4];
         var4 = 0;

         for (int var9 = 1; var9 <= var3; var9++) {
            var4 += var2.getRecord(var9, var1, var4);
         }
      } catch (Throwable var7) {
         var1 = (byte[])null;
      }

      if (var2 != null) {
         try {
            var2.closeRecordStore();
         } catch (Throwable var6) {
         }
      }

      return var1;
   }

   // $VF: renamed from: a (int, byte[]) boolean
   public static boolean writeRecord(int var0, byte[] var1) {
      String var2 = String.valueOf(var0);
      RecordStore var3 = null;
      boolean var4 = true;

      try {
         String[] var5;
         if ((var5 = RecordStore.listRecordStores()) != null) {
            for (int var6 = 0; var6 < var5.length; var6++) {
               if (var2.equals(var5[var6])) {
                  RecordStore.deleteRecordStore(var2);
                  break;
               }
            }
         }

         if (var1 != null) {
            var3 = RecordStore.openRecordStore(var2, true);
            int var12 = (var1.length + 786432 - 1) / 786432;
            int var7 = 0;

            for (int var8 = 1; var8 <= var12; var8++) {
               int var9;
               if ((var9 = var1.length - var7) > 786432) {
                  var9 = 786432;
               }

               var3.addRecord(var1, var7, var9);
               var7 += var9;
            }
         }
      } catch (Throwable var11) {
         var4 = false;
      }

      if (var3 != null) {
         try {
            var3.closeRecordStore();
         } catch (Throwable var10) {
         }
      }

      return var4;
   }

   // $VF: renamed from: a (boolean) void
   public final synchronized void syncSettings(boolean var1) {
      if (var1 || !this.l) {
         if (var1) {
            byte[] var3;
            (var3 = new byte[3])[0] = (byte)(this.soundEnabled ? 1 : 0);
            var3[1] = (byte)(this.m ? 1 : 0);
            var3[2] = (byte)(this.n ? 1 : 0);
            writeRecord(-9, var3);
            return;
         }

         this.l = true;
         byte[] var2;
         if ((var2 = readRecord(-9)) == null || var2.length != 3) {
            var2 = new byte[]{-1, -1, -1};
            writeRecord(-9, var2);
         }

         this.soundEnabled = var2[0] != 0;
         this.m = var2[1] != 0;
         this.n = var2[2] != 0;
      }
   }

   // $VF: renamed from: b (int) f[]
   public final Sprite[] loadSprites(int var1) {
      return this.loadSprites(var1, null);
   }

   // $VF: renamed from: b (int, byte[]) f[]
   public final Sprite[] loadSprites(int var1, byte[] var2) {
      try {
         DataInputStream var6;
         int var3 = (var6 = this.openResource(var1)).readChar();
         var6.readByte();
         int var4 = 0;
         byte[] var7 = new byte[var6.readShort() - 4];
         int var5 = var6.readChar();
         char var8 = var6.readChar();
         var6.readFully(var7);
         boolean[] var9 = new boolean[16];

         for (int var18 = 0; var18 < 16; var18++) {
            var9[var18] = (var5 >> var18 & 1) == 0;
         }

         var5 = 5;
         var4 = 0;
         int[] var10 = new int[10];

         for (int var11 = 0; var11 < 3; var11++) {
            for (int var12 = 0; var12 < (var11 == 0 ? 2 : 4); var12++) {
               var10[var4++] = var9[var11] ? (var9[var5++] ? 2 : 1) : 0;
            }
         }

         Image var24 = this.loadImage(var3, var2);
         short[] var25 = new short[10];
         Sprite[] var13 = new Sprite[var8];

         for (int var14 = 0; var14 < var8; var14++) {
            var13[var14] = new Sprite(var24);
            var5 = 0;

            for (int var15 = 0; var15 < var25.length; var15++) {
               var4 = 0;
               if ((var3 = var10[var15] - 1) >= 0) {
                  var5 += var14 << var3;
                  if (var3 == 0) {
                     var4 = var7[var5];
                     if (var15 > 1 && var15 != 6 && var15 != 7) {
                        var4 &= 255;
                     }
                  } else {
                     var4 = (var7[var5] & 255) << 8 | var7[var5 + 1] & 255;
                  }

                  var5 += var8 - var14 << var3;
               }

               var25[var15] = (short)var4;
            }

            var13[var14].load(var25);
         }

         return var13;
      } catch (Exception var16) {
         return null;
      }
   }

   // $VF: renamed from: c (int) javax.microedition.lcdui.Image
   public final Image loadImage(int var1) {
      return this.loadImage(var1, null);
   }

   // $VF: renamed from: c (int, byte[]) javax.microedition.lcdui.Image
   public final Image loadImage(int var1, byte[] var2) {
      Image var3 = null;
      this.selectBank(var1);
      var1 &= 1023;
      int var4 = this.resourceOffset(var1);
      this.swapPngBytes(this.bankData, var4, var2);
      var3 = Image.createImage(this.bankData, var4, this.resourceLengths[var1]);
      this.swapPngBytes(this.bankData, var4, var2);
      return var3;
   }

   // $VF: renamed from: a (byte[], int, byte[]) void
   private void swapPngBytes(byte[] var1, int var2, byte[] var3) {
      if (var3 != null) {
         DataInputStream var4 = new DataInputStream(new ByteArrayInputStream(var1, var2, var1.length - var2));

         try {
            this.findPngDataChunk(var4);
            int var5 = 0;
            var2 += this.pngPayloadOffset;

            while (var5 < var3.length) {
               byte var6 = var1[var2];
               var1[var2++] = var3[var5];
               var3[var5++] = var6;
            }

            return;
         } catch (IOException var7) {
         }
      }
   }

   // $VF: renamed from: b (java.io.DataInputStream) int
   private int findPngDataChunk(DataInputStream var1) throws IOException {
      var1.skip(8L);
      this.pngPayloadOffset = 8;

      while (true) {
         int var2 = var1.readInt();
         int var3 = var1.readInt();
         this.pngPayloadOffset += 8;
         if (1347179589 == var3) {
            return var2;
         }

         if (1229278788 == var3) {
            return -1;
         }

         var1.skip(var2 + 4);
         this.pngPayloadOffset += var2 + 4;
      }
   }

   // $VF: renamed from: d (int) byte[]
   public final byte[] getResourceBytes(int var1) {
      this.selectBank(var1);
      var1 &= 1023;
      byte[] var2 = new byte[this.resourceLengths[var1]];
      System.arraycopy(this.bankData, this.resourceOffset(var1), var2, 0, var2.length);
      return var2;
   }

   // $VF: renamed from: e (int) java.io.DataInputStream
   public final DataInputStream openResource(int var1) {
      this.selectBank(var1);
      var1 &= 1023;
      return new DataInputStream(new ByteArrayInputStream(this.bankData, this.resourceOffset(var1), this.resourceLengths[var1]));
   }

   // $VF: renamed from: l (int) int
   private int resourceOffset(int var1) {
      int var2 = 0;

      while (--var1 >= 0) {
         var2 += this.resourceLengths[var1];
      }

      return var2;
   }

   // $VF: renamed from: m (int) void
   private void selectBank(int var1) {
      if ((var1 = var1 >> 10) != 0 && var1 != this.loadedBank) {
         if (var1 > 0) {
            try {
               DataInputStream var2 = new DataInputStream(this.getClass().getResourceAsStream("/RP" + var1));
               this.parseBank(var2);
               var2.close();
            } catch (Exception var3) {
            }
         } else {
            this.parseBank(null);
         }

         this.loadedBank = var1;
      }
   }

   // $VF: renamed from: a (java.io.DataInputStream) boolean
   public final boolean parseBank(DataInputStream var1) {
      this.resourceLengths = null;
      this.resourceTypes = null;
      this.bankData = null;
      if (var1 == null) {
         this.loadedBank = -1;
         return false;
      }

      this.loadedBank = 0;

      try {
         int var4 = var1.read();
         int var2 = 1 + (var4 & 1) << 1;
         char var9 = var1.readChar();
         byte[] var5 = new byte[var2 * var9];
         var1.readFully(var5);
         this.resourceLengths = new int[var9];
         int var3 = 0;

         for (int var6 = 0; var6 < var9; var6++) {
            for (int var7 = var2 - 1; var7 >= 0; var7--) {
               this.resourceLengths[var6] = this.resourceLengths[var6] | (255 & var5[var3++]) << (var7 << 3);
            }
         }

         this.resourceTypes = new byte[var9];
         var1.readFully(this.resourceTypes);
         this.bankData = new byte[var1.readInt()];
         var1.readFully(this.bankData);
         return true;
      } catch (Exception var8) {
         return false;
      }
   }

   // $VF: renamed from: f (int) java.lang.String
   public final String getString(int var1) {
      short var2 = this.stringOffsets[var1];

      try {
         return new String(this.stringBytes, var2, this.stringOffsets[var1 + 1] - var2, "UTF-8");
      } catch (Exception var3) {
         return null;
      }
   }

   // $VF: renamed from: g (int) void
   public final void loadStringTable(int var1) {
      if (var1 == -1) {
         this.stringBytes = null;
         this.stringOffsets = null;
      } else {
         try {
            DataInputStream var2 = this.openResource(var1);
            this.stringOffsets = new short[var2.readShort() + 1];
            int var3 = this.resourceLengths[var1 & 1023];
            this.stringBytes = new byte[var3 - (this.stringOffsets.length << 1)];
            short var4 = 0;

            int var5;
            for (var5 = 0; var5 < this.stringOffsets.length - 1; var5++) {
               this.stringOffsets[var5] = var4;
               short var7;
               if ((var7 = var2.readShort()) > 0) {
                  var2.readFully(this.stringBytes, var4, var7);
               }

               var4 += var7;
            }

            this.stringOffsets[var5] = var4;
         } catch (Exception var6) {
         }
      }
   }

   // $VF: renamed from: a (f[]) void
   public final void setFont(Sprite[] var1) {
      this.font = var1;
      if (this.font != null && var1[3].metric == 255) {
         var1[3].metric = 0;
         short var2 = 0;

         for (int var3 = 0; var3 < var1.length; var3++) {
            Sprite var4 = var1[var3];
            var2 = (short)(var2 + (var4.offsetX << 8) + (var4.offsetY & 255));
            var4.offsetX = var4.charCode;
            var4.offsetY = var4.f;
            var4.charCode = var2;
         }
      }
   }

   // $VF: renamed from: n (int) void
   private void ensureCharBuffer(int var1) {
      if (this.charBuffer == null || this.charBuffer.length < var1) {
         this.charBuffer = new char[var1];
      }
   }

   // $VF: renamed from: a (java.lang.String) int
   public final int stringWidth(String var1) {
      return this.stringWidth(var1, 0, var1.length());
   }

   // $VF: renamed from: a (java.lang.String, int, int) int
   public final int stringWidth(String var1, int var2, int var3) {
      this.ensureCharBuffer(var3);

      for (int var4 = 0; var4 < var3; var4++) {
         this.charBuffer[var4] = var1.charAt(var2 + var4);
      }

      return this.charsWidth(this.charBuffer, 0, var3);
   }

   // $VF: renamed from: a (char) int
   public final int charWidth(char var1) {
      this.ensureCharBuffer(1);
      this.charBuffer[0] = var1;
      return this.charsWidth(this.charBuffer, 0, 1);
   }

   // $VF: renamed from: a (char[], int, int) int
   public final int charsWidth(char[] var1, int var2, int var3) {
      if (this.font != null) {
         var3 += var2;
         short var4 = 0;

         while (var2 < var3) {
            Sprite var5 = this.findGlyph(var1[var2]);
            var4 += var5 != null ? var5.advance : this.font[2].metric;
            var2++;
         }

         return var4;
      } else {
         return var3 == 0 ? 0 : this.systemFont.charsWidth(var1, var2, var3);
      }
   }

   // $VF: renamed from: b (char) f
   private Sprite findGlyph(char var1) {
      int var2 = 0;
      int var3 = this.font.length - 1;

      while (var2 <= var3) {
         int var4 = var2 + var3 >> 1;
         char var5;
         if ((var5 = (char)this.font[var4].charCode) < var1) {
            var2 = var4 + 1;
         } else {
            if (var5 <= var1) {
               return this.font[var4];
            }

            var3 = var4 - 1;
         }
      }

      return null;
   }

   // $VF: renamed from: b () int
   public final int fontBaseline() {
      return this.font != null ? this.font[1].metric : this.systemFont.getBaselinePosition();
   }

   // $VF: renamed from: c () int
   public final int fontHeight() {
      return this.font != null ? this.font[0].metric : this.systemFont.getHeight();
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics, java.lang.String, int, int, int) void
   public final void drawString(Graphics var1, String var2, int var3, int var4, int var5) {
      this.drawString(var1, var2, 0, var2.length(), var3, var4, var5);
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics, java.lang.String, int, int, int, int, int) void
   public final void drawString(Graphics var1, String var2, int var3, int var4, int var5, int var6, int var7) {
      this.ensureCharBuffer(var4);

      for (int var8 = 0; var8 < var4; var8++) {
         this.charBuffer[var8] = var2.charAt(var3 + var8);
      }

      this.drawString(var1, this.charBuffer, 0, var4, var5, var6, var7);
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics, char[], int, int, int, int, int) void
   public final void drawString(Graphics var1, char[] var2, int var3, int var4, int var5, int var6, int var7) {
      if ((var7 & 64) != 0) {
         var6 -= this.fontBaseline();
      } else if ((var7 & 32) != 0) {
         var6 -= this.fontHeight();
      }

      if ((var7 & 9) != 0) {
         var5 -= this.charsWidth(var2, var3, var4) >> (var7 & 1);
      }

      if (this.font != null) {
         for (int var9 = var4 + var3; var3 < var9; var3++) {
            Sprite var8;
            if ((var8 = this.findGlyph(var2[var3])) != null) {
               var8.draw(var1, var5, var6, 0);
               var5 += var8.advance;
            } else {
               var5 += this.font[2].metric;
            }
         }
      } else {
         if (var4 != 0) {
            if (var1.getFont() != this.systemFont) {
               var1.setFont(this.systemFont);
            }

            var1.drawChars(var2, var3, var4, var5, var6, 20);
         }
      }
   }

   // $VF: renamed from: b (int, int, javax.microedition.lcdui.Image, int, int) void
   private void initTileMapImpl(int var1, int var2, Image var3, int var4, int var5) {
      this.scrollX = 0;
      this.scrollY = 0;
      this.gridCols = var1;
      this.gridRows = var2;
      this.tileGrid = new short[var2 * var1];
      this.animatedTiles = new int[0];
      this.tileSrcX = null;
      this.setTileSheet(var3, var4, var5);
   }

   // $VF: renamed from: b (javax.microedition.lcdui.Image, int, int) boolean
   private boolean setTileSheetImpl(Image var1, int var2, int var3) {
      this.tileWidth = var2;
      this.tileHeight = var3;
      var2 = var1.getWidth();
      var3 = var1.getHeight();
      int var4 = this.tileSrcX == null ? 0 : this.tileSrcX.length;
      int var5 = var2 / this.tileWidth * (var3 / this.tileHeight);
      if (var4 > var5) {
         this.fillTiles(0, 0, this.gridCols, this.gridRows, 0);
         this.animatedTiles = new int[0];
      }

      var2 -= this.tileWidth;
      var3 -= this.tileHeight;
      this.tileSheet = var1;
      this.tileSrcX = new int[var5];
      this.tileSrcY = new int[var5];
      int var6 = 0;

      for (int var7 = 0; var7 <= var3; var7 += this.tileHeight) {
         for (int var8 = 0; var8 <= var2; var8 += this.tileWidth) {
            this.tileSrcX[var6] = var8;
            this.tileSrcY[var6] = var7;
            var6++;
         }
      }

      return var4 > var5;
   }

   // $VF: renamed from: f (int, int) boolean
   private boolean setViewSizeImpl(int var1, int var2) {
      this.viewWidthPx = Math.max(0, var1);
      this.viewHeightPx = Math.max(0, var2);
      if (var1 >= 0 && var2 >= 0) {
         return false;
      }

      this.tileSheet = null;
      this.tileSrcX = null;
      this.tileSrcY = null;
      this.tileGrid = null;
      this.animatedTiles = null;
      return true;
   }

   // $VF: renamed from: a (int, int) void
   public final void setScroll(int var1, int var2) {
      this.scrollX = -var1;
      this.scrollY = -var2;
   }

   // $VF: renamed from: a (int, int, int, int, int, int) void
   private void drawTilesIfEnabled(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (!this.tileDrawDisabled) {
         this.drawTiles(var1, var2, var3, var4, var5, var6);
      }
   }

   // $VF: renamed from: o (int) int
   private int resolveTile(int var1) {
      return var1 < 0 ? this.animatedTiles[~var1] : var1;
   }

   // $VF: renamed from: b (int, int, int, int, int, int) void
   private void drawTiles(int var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = var1 % this.tileWidth;
      int var8 = var2 % this.tileHeight;
      int var9 = var3;
      int var10 = var1 / this.tileWidth;
      int var11 = var2 / this.tileHeight;
      int var12;
      int var13 = var12 = var10 + var11 * this.gridCols;
      int var14 = var6;
      int var15;
      if (var7 != 0) {
         if ((var15 = this.tileWidth - var7) > var5) {
            var15 = var5;
         }
      } else {
         var15 = 0;
      }

      if (var8 != 0) {
         int var16 = var5;
         int var17;
         if ((var17 = this.tileHeight - var8) > var6) {
            var17 = var6;
         }

         if (var7 != 0) {
            int var18 = this.tileGrid[var12];
            if ((var18 = this.resolveTile(var18)) > 0) {
               this.tileGraphics.drawRegion(this.tileSheet, this.tileSrcX[--var18] + var7, this.tileSrcY[var18] + var8, var15, var17, 0, var3, var4, 20);
            }

            var12++;
            var16 -= var15;
            var3 += var15;
         }

         while (var16 >= this.tileWidth) {
            int var41 = this.tileGrid[var12];
            if ((var41 = this.resolveTile(var41)) > 0) {
               this.tileGraphics.drawRegion(this.tileSheet, this.tileSrcX[--var41], this.tileSrcY[var41] + var8, this.tileWidth, var17, 0, var3, var4, 20);
            }

            var12++;
            var16 -= this.tileWidth;
            var3 += this.tileWidth;
         }

         if (var16 > 0) {
            int var44 = this.tileGrid[var12];
            if ((var44 = this.resolveTile(var44)) > 0) {
               this.tileGraphics.drawRegion(this.tileSheet, this.tileSrcX[--var44], this.tileSrcY[var44] + var8, var16, var17, 0, var3, var4, 20);
            }
         }

         var14 -= var17;
         var12 = var13 += this.gridCols;
         var3 = var9;
         var4 += var17;
      }

      while (var14 >= this.tileHeight) {
         int var19 = var5;
         if (var7 != 0) {
            int var21 = this.tileGrid[var12];
            if ((var21 = this.resolveTile(var21)) > 0) {
               this.tileGraphics.drawRegion(this.tileSheet, this.tileSrcX[--var21] + var7, this.tileSrcY[var21], var15, this.tileHeight, 0, var3, var4, 20);
            }

            var12++;
            var19 -= var15;
            var3 += var15;
         }

         while (var19 >= this.tileWidth) {
            int var24 = this.tileGrid[var12];
            if ((var24 = this.resolveTile(var24)) > 0) {
               this.tileGraphics.drawRegion(this.tileSheet, this.tileSrcX[--var24], this.tileSrcY[var24], this.tileWidth, this.tileHeight, 0, var3, var4, 20);
            }

            var12++;
            var19 -= this.tileWidth;
            var3 += this.tileWidth;
         }

         if (var19 > 0) {
            int var27 = this.tileGrid[var12];
            if ((var27 = this.resolveTile(var27)) > 0) {
               this.tileGraphics.drawRegion(this.tileSheet, this.tileSrcX[--var27], this.tileSrcY[var27], var19, this.tileHeight, 0, var3, var4, 20);
            }
         }

         var12 = var13 += this.gridCols;
         var3 = var9;
         var4 += this.tileHeight;
         var14 -= this.tileHeight;
      }

      if (var14 > 0) {
         int var20 = var5;
         if (var7 != 0) {
            int var30 = this.tileGrid[var12];
            if ((var30 = this.resolveTile(var30)) > 0) {
               this.tileGraphics.drawRegion(this.tileSheet, this.tileSrcX[--var30] + var7, this.tileSrcY[var30], var15, var14, 0, var3, var4, 20);
            }

            var12++;
            var20 -= var15;
            var3 += var15;
         }

         while (var20 >= this.tileWidth) {
            int var33 = this.tileGrid[var12];
            if ((var33 = this.resolveTile(var33)) > 0) {
               this.tileGraphics.drawRegion(this.tileSheet, this.tileSrcX[--var33], this.tileSrcY[var33], this.tileWidth, var14, 0, var3, var4, 20);
            }

            var12++;
            var20 -= this.tileWidth;
            var3 += this.tileWidth;
         }

         if (var20 > 0) {
            int var36 = this.tileGrid[var12];
            if ((var36 = this.resolveTile(var36)) > 0) {
               this.tileGraphics.drawRegion(this.tileSheet, this.tileSrcX[--var36], this.tileSrcY[var36], var20, var14, 0, var3, var4, 20);
            }
         }
      }
   }

   // $VF: renamed from: a (int, int, javax.microedition.lcdui.Image, int, int) void
   public final void initTileMap(int var1, int var2, Image var3, int var4, int var5) {
      this.initTileMapImpl(var1, var2, var3, var4, var5);
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Image, int, int) void
   public final void setTileSheet(Image var1, int var2, int var3) {
      this.setTileSheetImpl(var1, var2, var3);
   }

   // $VF: renamed from: a (int, int, int, int, int) void
   public final void fillTiles(int var1, int var2, int var3, int var4, int var5) {
      int var6 = var2 * this.gridCols + var1;

      for (int var7 = var2; var7 < var2 + var4; var6 += this.gridCols) {
         int var8 = var6;

         for (int var9 = var1; var9 < var1 + var3; var8++) {
            this.tileGrid[var8] = (short)var5;
            var9++;
         }

         var7++;
      }
   }

   // $VF: renamed from: b (int, int) void
   public final void setViewSize(int var1, int var2) {
      this.setViewSizeImpl(var1, var2);
   }

   // $VF: renamed from: a (int, int, int) void
   public final void setTile(int var1, int var2, int var3) {
      this.tileGrid[var2 * this.gridCols + var1] = (short)var3;
   }

   // $VF: renamed from: h (int) int
   public final int addAnimatedTile(int var1) {
      int[] var2 = this.animatedTiles;
      this.animatedTiles = new int[var2.length + 1];
      System.arraycopy(var2, 0, this.animatedTiles, 0, var2.length);
      this.animatedTiles[var2.length] = var1;
      return -this.animatedTiles.length;
   }

   // $VF: renamed from: c (int, int) void
   public final void setAnimatedTile(int var1, int var2) {
      this.animatedTiles[~var1] = var2;
   }

   // $VF: renamed from: b (javax.microedition.lcdui.Graphics) void
   public final void drawTileMap(Graphics var1) {
      int var2;
      if (this.scrollX < 0) {
         var2 = this.scrollX;
      } else if ((var2 = this.scrollX + this.viewWidthPx - this.gridCols * this.tileWidth) < 0) {
         var2 = 0;
      }

      int var3;
      if (this.scrollY < 0) {
         var3 = this.scrollY;
      } else if ((var3 = this.scrollY + this.viewHeightPx - this.gridRows * this.tileHeight) < 0) {
         var3 = 0;
      }

      if (var2 != 0 || var3 != 0) {
         this.scrollX -= var2;
         this.scrollY -= var3;
         var1.translate(-var2, -var3);
      }

      int var5 = this.scrollX;
      int var6 = this.scrollY;
      int var7 = 0;
      int var8 = 0;
      int var9 = this.viewWidthPx;
      int var10 = this.viewHeightPx;
      int var4;
      if ((var4 = var1.getClipX()) > 0) {
         var5 += var4;
         var9 -= var4;
         var7 = var4;
      }

      if ((var4 = var7 + var9 - (var4 + var1.getClipWidth())) > 0) {
         var9 -= var4;
      }

      if ((var4 = var1.getClipY()) > 0) {
         var6 += var4;
         var10 -= var4;
         var8 = var4;
      }

      if ((var4 = var8 + var10 - (var4 + var1.getClipHeight())) > 0) {
         var10 -= var4;
      }

      if (var9 > 0 && var10 > 0) {
         this.tileGraphics = var1;
         this.drawTilesIfEnabled(var5, var6, var7, var8, var9, var10);
         this.tileGraphics = null;
      }

      if (var2 != 0 || var3 != 0) {
         this.scrollX += var2;
         this.scrollY += var3;
         var1.translate(var2, var3);
      }
   }

   // $VF: renamed from: i (int) void
   public final void loadAnimSet(int var1) {
      try {
         DataInputStream var2 = this.openResource(var1);
         this.N = growArray(this.N, this.Q + 1);
         this.O = growArray(this.O, this.Q + 1);
         this.P = growArray(this.P, this.Q + 1);
         this.N[this.Q] = (short)var1;
         this.O[this.Q] = (short)this.S;
         this.P[this.Q] = (short)this.U;
         this.Q++;
         int var3 = var2.readChar() & 32767;
         char var4 = var2.readChar();
         this.V = growArray(this.V, this.W + var2.readChar());
         this.X = growArray(this.X, this.Y + var2.readChar());
         this.R = growArray(this.R, this.S + var3);
         this.T = growArray(this.T, this.U + var4);
         var2.readChar();
         int var5 = 0;
         int var6 = 0;
         byte[][] var7 = new byte[11][];

         for (int var8 = 0; var8 < 11; var8++) {
            boolean var9 = var8 != 8;
            int var10 = var3;
            if (var8 > 1) {
               var10 = var5;
            }

            if (var8 > 6) {
               var10 = var4;
            }

            if (var8 > 7) {
               var10 = var6;
            }

            byte[] var11 = new byte[var10];
            int var12 = 0;

            for (int var13 = 0; var13 < var10; var13++) {
               short var14;
               if ((var14 = var2.readByte()) == -128 && var9) {
                  var14 = var2.readShort();
               }

               int var15 = var12 + var14;
               if (var9) {
                  var12 = var15;
               }

               if (var8 == 1) {
                  var5 += var15;
               }

               if (var8 == 7) {
                  var6 += var15;
               }

               if (var8 == 2) {
                  int var16 = 0;

                  while (var16 < this.aa && this.Z[var16] != var15) {
                     var16++;
                  }

                  if (var16 == this.aa) {
                     this.Z = growArray(this.Z, this.aa + 1);
                     this.Z[this.aa++] = (short)var15;
                  }

                  var15 = var16;
               }

               var11[var13] = (byte)var15;
            }

            var7[var8] = var11;
         }

         var5 = 0;
         var6 = 0;

         for (int var20 = 0; var20 < var3; var20++) {
            this.R[this.S++] = (short)this.W;
            int var22 = var7[1][var20] & 255;
            this.V[this.W++] = var7[0][var20];

            for (this.V[this.W++] = (byte)var22; var22-- > 0; var5++) {
               for (int var24 = 2; var24 < 7; var24++) {
                  this.V[this.W++] = var7[var24][var5];
               }
            }
         }

         for (int var21 = 0; var21 < var4; var21++) {
            this.T[this.U++] = (short)this.Y;
            int var23 = var7[7][var21] & 255;

            for (this.X[this.Y++] = (byte)var23; var23-- > 0; var6++) {
               for (int var25 = 8; var25 < 11; var25++) {
                  this.X[this.Y++] = var7[var25][var6];
               }
            }
         }
      } catch (Exception var17) {
      }
   }

   // $VF: renamed from: b (int, int, int) void
   public final void startAnim(int var1, int var2, int var3) {
      short var4 = 0;

      while (var4 < this.Q && var2 != this.N[var4]) {
         var4++;
      }

      int var5 = this.O[var4] + var3;
      int var6 = var1 * 8;
      this.M = growArray(this.M, var6 + 8);
      short var7 = this.R[var5];
      this.M[var6 + 0] = 0;
      this.M[var6 + 1] = 0;
      this.M[var6 + 2] = this.V[var7 + 0];
      this.M[var6 + 3] = 0;
      this.M[var6 + 4] = 0;
      this.M[var6 + 5] = (short)(this.V[var7 + 2 + 4] & 0xFF);
      this.M[var6 + 6] = var7;
      this.M[var6 + 7] = var4;
   }

   // $VF: renamed from: d (int, int) boolean
   public final boolean stepAnim(int var1, int var2) {
      int var3 = var1 * 8;
      int var4;
      if ((var4 = this.M[var3 + 2]) == 0) {
         return false;
      }

      int var5 = (this.M[var3 + 0] & '\uffff') + var2;
      int var6 = this.M[var3 + 1];
      short var7 = this.M[var3 + 6];
      int var8 = this.V[var7 + 1] & 255;
      int var9 = var7 + 2;
      int var10 = var6;
      int var12 = 0;

      int var11;
      while (var5 > (var11 = this.Z[this.V[var9 + var6 * 5 + 0]] & '\uffff')) {
         var5 -= var11;
         if (++var6 == var8) {
            if (var4 > 0) {
               if (--var4 == 0) {
                  var6--;
                  var5 = var11;
                  break;
               }
            }

            var6 = 0;
         }

         int var13;
         if ((var13 = this.V[var9 + var6 * 5 + 3] & 63) >= var12) {
            var12 = var13;
            var10 = var6;
         }
      }

      int var22 = this.V[var9 + var10 * 5 + 4] & 255;
      var9 += var6 * 5;
      int var14 = this.V[var9 + 1];
      int var15 = this.V[var9 + 2];
      if (this.V[var9 + 3] >> 6 > 0) {
         var9 -= 5;
         byte var17 = 0;
         byte var18 = 0;
         if (var6 != 0) {
            var17 = this.V[var9 + 1];
            var18 = this.V[var9 + 2];
         }

         int var19 = (var5 << 12) / Math.max(1, var11);
         var14 = var17 + ((var14 - var17) * var19 >> 12);
         var15 = var18 + ((var15 - var18) * var19 >> 12);
      }

      this.M[var3 + 0] = (short)var5;
      this.M[var3 + 1] = (short)var6;
      this.M[var3 + 2] = (short)var4;
      this.M[var3 + 3] = (short)var14;
      this.M[var3 + 4] = (short)var15;
      this.M[var3 + 5] = (short)var22;
      return var4 != 0;
   }

   // $VF: renamed from: a (f[], int, int, int, int, int[]) void
   public final void getAnimBounds(Sprite[] var1, int var2, int var3, int var4, int var5, int[] var6) {
      int var7 = Integer.MAX_VALUE;
      int var8 = Integer.MAX_VALUE;
      int var9 = Integer.MIN_VALUE;
      int var10 = Integer.MIN_VALUE;
      int var11 = this.T[this.P[this.M[var2 * 8 + 7]] + this.M[var2 * 8 + 5]];
      int var12 = this.X[var11++] & 255;

      while (var12-- > 0) {
         int var13 = this.X[var11++] & 255;
         byte var14 = this.X[var11++];
         byte var15 = this.X[var11++];
         Sprite var16;
         int var17 = (var16 = var1[var13]).offsetX + var14;
         if ((var5 & 2) != 0) {
            var17 = -var17 - var16.width;
         }

         int var18 = var16.offsetY + var15;
         if ((var5 & 1) != 0) {
            var18 = -var18 - var16.height;
         }

         var7 = Math.min(var7, var17);
         var8 = Math.min(var8, var18);
         var9 = Math.max(var9, var17 + var16.width);
         var10 = Math.max(var10, var18 + var16.height);
      }

      var6[0] = var3 + var7;
      var6[1] = var4 + var8;
      var6[2] = var9 - var7;
      var6[3] = var10 - var8;
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics, f[], int, int, int, int) void
   public final void drawAnim(Graphics var1, Sprite[] var2, int var3, int var4, int var5, int var6) {
      int var7 = this.T[this.P[this.M[var3 * 8 + 7]] + this.M[var3 * 8 + 5]];
      int var8 = this.X[var7++] & 255;

      while (var8-- > 0) {
         int var9 = this.X[var7++] & 255;
         byte var10 = this.X[var7++];
         byte var11 = this.X[var7++];
         Sprite var12;
         Sprite var16 = var12 = var2[var9];
         var16.offsetX = (short)(var16.offsetX + var10);
         var12.offsetY = (short)(var12.offsetY + var11);
         var12.draw(var1, var4, var5, var6);
         var12.offsetX = (short)(var12.offsetX - var10);
         var12.offsetY = (short)(var12.offsetY - var11);
      }
   }

   // $VF: renamed from: a (short[], int) short[]
   private static short[] growArray(short[] var0, int var1) {
      if (var0 == null) {
         return new short[(var1 << 1) + 16];
      }

      if (var0.length >= var1) {
         return var0;
      }

      short[] var3 = new short[(var1 * 9 >> 3) + 8];
      System.arraycopy(var0, 0, var3, 0, var0.length);
      return var3;
   }

   // $VF: renamed from: a (byte[], int) byte[]
   private static byte[] growArray(byte[] var0, int var1) {
      if (var0 == null) {
         return new byte[(var1 << 1) + 16];
      }

      if (var0.length >= var1) {
         return var0;
      }

      byte[] var3 = new byte[var1 * 9 >> 3];
      System.arraycopy(var0, 0, var3, 0, var0.length);
      return var3;
   }

   // $VF: renamed from: a (byte) boolean
   public final boolean isKeyHeld(byte var1) {
      int var2 = 1 << var1;
      return (this.keysHeld & var2) != 0;
   }

   // $VF: renamed from: b (byte) boolean
   public final boolean isKeyPressed(byte var1) {
      int var2 = 1 << var1;
      return (this.keysPressed & var2) != 0;
   }

   // $VF: renamed from: d () boolean
   public final boolean anyKeyPressed() {
      return this.keysPressed != 0;
   }

   // $VF: renamed from: e () void
   public final synchronized void clearKeys() {
      this.pendingPressed = 0;
      this.pendingHeld = 0;
      this.pendingReleased = this.pendingReleased | this.keysHeld;
      this.keysPressed = 0;
      this.keysHeld = 0;
      this.keysReleased = 0;
   }

   // $VF: renamed from: f () void
   public final synchronized void pollKeys() {
      this.keysPressed = this.pendingPressed;
      this.pendingPressed = 0;
      this.pendingReleased = this.pendingReleased & this.pendingHeld;
      this.pendingHeld = this.pendingHeld & ~this.pendingReleased;
      this.keysHeld = this.keysPressed | this.pendingHeld;
      this.keysReleased = this.pendingReleased;
      this.pendingReleased = 0;
   }

   // $VF: renamed from: a (int, boolean) void
   private void handleKey(int var1, boolean var2) {
      if (var1 != 0) {
         synchronized (this) {
            for (int var4 = 1; var4 < keyCodeTable.length; var4++) {
               if (keyCodeTable[var4] == var1) {
                  int var5 = 1 << var4;
                  if (var2) {
                     this.pendingPressed |= var5;
                     this.pendingHeld |= var5;
                     this.pendingReleased &= ~var5;
                  } else {
                     this.pendingReleased |= var5;
                  }
               }
            }
         }
      }
   }

   public void keyPressed(int var1) {
      this.handleKey(var1, true);
      super.keyPressed(var1);
   }

   public void keyReleased(int var1) {
      this.handleKey(var1, false);
      super.keyReleased(var1);
   }

   public void keyRepeated(int var1) {
      super.keyRepeated(var1);
   }

   // $VF: renamed from: g () void
   public final void resetFrameTimingNow() {
      this.frameTime = this.minFrameTime;
      this.resetFrameTiming = true;
   }

   // $VF: renamed from: d (javax.microedition.lcdui.Graphics) void
   private void paintFrame(Graphics var1) {
      if (var1 != null) {
         this.bordersCleared = this.clearBorders;
         this.clearBorders = false;
         if (240 != this.canvasWidth || 320 != this.canvasHeight) {
            if (this.bordersCleared) {
               int var3 = 320 - this.canvasHeight + 1 >>> 1;
               var1.setColor(-16777216);
               if (var3 > 0) {
                  if (this.canvasOffsetY > 0) {
                     var1.fillRect(0, 0, 240, this.canvasOffsetY);
                  }

                  var1.fillRect(0, 320 - var3, 240, var3);
               }

               if ((var3 = 240 - this.canvasWidth + 1 >>> 1) > 0) {
                  if (this.canvasOffsetX > 0) {
                     var1.fillRect(0, this.canvasOffsetY, this.canvasOffsetX, this.canvasHeight);
                  }

                  var1.fillRect(240 - var3, this.canvasOffsetY, var3, this.canvasHeight);
               }
            }

            var1.translate(this.canvasOffsetX, this.canvasOffsetY);
         }

         this.resetClip(var1);
         this.render(var1);
      }
   }

   // $VF: renamed from: e (int, int) void
   public final void setViewport(int var1, int var2) {
      this.clearBorders = true;
      if (var1 <= 0) {
         var1 = 240;
      }

      if (var2 <= 0) {
         var2 = 320;
      }

      this.canvasWidth = var1;
      this.canvasHeight = var2;
      this.canvasOffsetX = 240 - var1;
      this.canvasOffsetY = 320 - var2;
      if (this.canvasOffsetX < 0) {
         this.canvasOffsetX++;
      }

      if (this.canvasOffsetY < 0) {
         this.canvasOffsetY++;
      }

      this.canvasOffsetX >>= 1;
      this.canvasOffsetY >>= 1;
   }

   // $VF: renamed from: c (javax.microedition.lcdui.Graphics) void
   public final void resetClip(Graphics var1) {
      var1.setClip(0, 0, this.canvasWidth, this.canvasHeight);
   }

   // $VF: renamed from: h () void
   public final void requestClear() {
      this.clearBorders = true;
   }

   // $VF: renamed from: j (int) void
   public void onLifecycle(int var1) {
      if (var1 == 3) {
         if (this.soundPlayer != null) {
            this.soundPlayer.close();
         }

         this.shuttingDown = true;
         this.midlet.notifyDestroyed();
      }

      if (var1 == 0 || var1 == 1 || var1 == 2) {
         this.clearBorders = true;
         this.resetFrameTiming = true;
         this.clearKeys();
      }
   }

   public final void hideNotify() {
      this.postLifecycle(1);
   }

   public final void showNotify() {
      this.postLifecycle(2);
      if (this.gameThread == null) {
         this.gameThread = new Thread(this);
         this.gameThread.start();
      }
   }

   // $VF: renamed from: k (int) void
   public final synchronized void postLifecycle(int var1) {
      if (var1 == 3) {
         this.onLifecycle(var1);
      } else if (this.threadRunning && (var1 == 1 || var1 == 2)) {
         synchronized (this.gameThread) {
            this.pendingLifecycle = var1;
            this.lifecycleTime = System.currentTimeMillis();
         }
      }
   }

   // $VF: renamed from: i () void
   private void processLifecycle() {
      while (this.pendingLifecycle != -1) {
         if (this.lifecycleState != 1) {
            if (this.soundPlayer != null) {
               this.soundPlayer.stop();
            }

            this.onLifecycle(1);
            this.lifecycleState = 1;
         }

         synchronized (this.gameThread) {
            if (this.pendingLifecycle == 1) {
               this.pendingLifecycle = -1;
            }
         }

         while (this.pendingLifecycle == 2 && this.isShown()) {
            if ((int)(System.currentTimeMillis() - this.lifecycleTime) < 750) {
               try {
                  Thread.sleep(250L);
                  Thread.yield();
               } catch (Exception var3) {
               }
            } else {
               synchronized (this.gameThread) {
                  if (this.pendingLifecycle != 2) {
                     continue;
                  }

                  this.pendingLifecycle = -1;
               }

               if (this.soundPlayer != null) {
                  this.soundPlayer.stop();
               }

               this.onLifecycle(2);
               this.lifecycleState = 2;
               this.onLifecycle(5);
            }
         }
      }
   }

   public final void run() {
      this.threadRunning = true;
      this.onLifecycle(0);

      for (; !this.shuttingDown; Thread.yield()) {
         this.processLifecycle();
         if (this.isShown()) {
            long var1 = System.currentTimeMillis();
            if (this.resetFrameTiming) {
               this.resetFrameTiming = false;
               this.frameTime = this.minFrameTime;
            } else {
               int var3;
               if ((var3 = (int)(var1 - this.lastFrameStart)) > this.maxFrameTime || var3 < 0) {
                  var3 = this.maxFrameTime;
               }

               this.frameTime = var3;
               if (this.frameTime < this.minFrameTime) {
                  int var4;
                  if ((var4 = this.minFrameTime - this.frameTime) < 10) {
                     var4 = 10;
                  }

                  this.frameTime = this.minFrameTime;

                  try {
                     Thread.sleep(var4);
                  } catch (Exception var6) {
                  }
               }
            }

            this.lastFrameStart = var1;
            this.update();
            if (this.soundPlayer != null) {
               this.soundPlayer.run();
            }

            if (this.shuttingDown) {
               return;
            }

            if (this.isShown()) {
               this.paintFrame(this.getGraphics());
               this.flushGraphics();
            }
         } else {
            try {
               Thread.sleep(100L);
            } catch (Exception var5) {
            }

            this.resetFrameTiming = true;
         }
      }
   }

   public Engine(MIDlet var1) {
      super(false);
      this.setFullScreenMode(true);
      this.midlet = var1;
      this.setViewport(240, 320);
   }
}
