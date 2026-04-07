package com.gbemu.core.graphics;

import com.gbemu.core.memory.MMU;

public class PPU {
    private MMU mmu;

    private int currentDots;
    private int LCDCValue;
    private int currentScanline;
    private int windowLineCounter;
    private SpriteContainer sprites;
    private State currentState;
    private final int[] framebuffer;

    private enum State {
        OAM_SCAN, DRAW, H_BLANK, V_BLANK
    }

    public PPU(MMU mmu) {
        this.mmu = mmu;

        framebuffer = new int[160 * 144];
        currentDots = 0;
        currentScanline = 0;
        windowLineCounter = 0;
        LCDCValue = mmu.readByte(0xFF40);
        currentState = State.OAM_SCAN;
        sprites = new SpriteContainer(mmu);
    }

    public void step(int cycles) {
        LCDCValue = mmu.readByte(0xFF40);

        /* LDLC.7 == 0; PPU turns off */
        if ((LCDCValue & 0x80) == 0) {
            setPPUOff();
            return;
        }

        currentDots += cycles;

        while (currentDots >= 456) {
            currentDots -= 456;
            ++currentScanline;

            if (currentScanline == 144) {
                mmu.writeByte(0xFF0F, (mmu.readByte(0xFF0F) | 0x01));
            }

            if (currentScanline > 153) {
                currentScanline = 0;
                windowLineCounter = 0;
            }

            checkLYC();
        }

        if (currentScanline < 144) {
            if (currentDots < 80) {
                currentState = State.OAM_SCAN;
            } else if (currentDots < 252) {
                if (currentState == State.OAM_SCAN) {
                    sprites.updateScanline(currentScanline);
                }
                currentState = State.DRAW;
            } else {
                if (currentState == State.DRAW)
                    drawScanline(LCDCValue);
                currentState = State.H_BLANK;
            }
        } else {
            currentState = State.V_BLANK;
        }

        setMODE();
        setLY();
    }

    public int[] getFramebuffer() {
        return framebuffer;
    }

    public boolean isVBlank() {
        return (currentScanline == 144) && currentState == State.V_BLANK;
    }

    private void setLY() {
        mmu.writeByte(0xFF44, currentScanline);
    }

    private void setMODE() {
        int value = switch (currentState) {
            case OAM_SCAN -> 2;
            case DRAW -> 3;
            case H_BLANK -> 0;
            case V_BLANK -> 1;
        };

        /* Sets current PPU mode to the lower 2 bits of LCD Status register */
        mmu.writeByte(0xFF41, (mmu.readByte(0xFF41) & 0xFC) | value);
    }

    /**
     * Renders a single scanline to the framebuffer.
     * Rendering order (front to back):
     * 1. High priority sprites (bit7=0)
     * 2. Window
     * 3. Background
     * 4. Low priority sprites (bit7=1) only visible if Window or Background
     * pixel index=0
     *
     * @param current LCDC (0xFF40) value
     */
    private void drawScanline(int LCDCValue) {
        if ((LCDCValue & 0x01) == 0) {
            drawWhiteBackground();
            return;
        }

        boolean hasWindowInc = false;
        boolean bit4 = getLCDCBit(4);

        int tileMapBaseBackground = getLCDCBit(3) ? 0x9C00 : 0x9800;
        int tileMapBaseWindow = getLCDCBit(6) ? 0x9C00 : 0x9800;
        int tileDataBase = bit4 ? 0x8000 : 0x9000;

        /* Scrolled background Y - & 0xFF accounts for wrapping */
        int screenY = (getSCY() + currentScanline) & 0xFF;

        for (int i = 0; i < 160; i++) {

            /* Scrolled background X - & 0xFF accounts for wrapping */
            int screenX = (getSCX() + i) & 0xFF;

            /* Window starts at X = 0, when WX = 7 due to hardware */
            boolean isWindowVisible = i >= (getWX() - 7)
                    && currentScanline >= getWY()
                    && getLCDCBit(5);

            /* 1. High priority sprites */
            if (sprites.isSprite(i, true)) {
                framebuffer[i + currentScanline * 160] = sprites.getEncodedPixel(i, true);
                continue;
            }

            /* 2. Window layer */
            if (isWindowVisible) {
                int windowX = i - (getWX() - 7);
                int pixelValue = getPixelValue(tileMapBaseWindow, tileDataBase, bit4, i, windowX, windowLineCounter);
                framebuffer[i + currentScanline * 160] = getRGBColor(pixelValue);

                /* 4. Low priority sprites, window color index = 0 */
                if (sprites.isSprite(i, false) && pixelValue == 0) {
                    framebuffer[i + currentScanline * 160] = sprites.getEncodedPixel(i, false);
                }

                hasWindowInc = true;
                continue;
            }

            /* 3. Background layer */
            int pixelValue = getPixelValue(tileMapBaseBackground, tileDataBase, bit4, i, screenX, screenY);
            framebuffer[i + currentScanline * 160] = getRGBColor(pixelValue);

            /* 4. Low priority sprites, background color index = 0 */
            if (sprites.isSprite(i, false) && pixelValue == 0) {
                framebuffer[i + currentScanline * 160] = sprites.getEncodedPixel(i, false);
            }
        }

        if (hasWindowInc)
            windowLineCounter++;
    }

    private int getPixelValue(int tileMapBase, int tileDataBase, boolean bit4, int i, int pixelX, int pixelY) {
        int tileX = pixelX / 8;
        int tileY = pixelY / 8;
        int tilePixelX = pixelX % 8;
        int tilePixelY = pixelY % 8;

        /* Indexes Tile Map which contains a reference to a specific tile in VRAM */
        int tileMapAdress = tileMapBase + (tileY * 32) + tileX;
        int tileDataIndex = mmu.readByte(tileMapAdress);

        /*
         * Accounts for different indexing types:
         * bit4 = 1: unsigned offset from 0x8000
         * bit4 = 0: signed offset from 0x9000
         */
        int tileDataAdress = tileDataBase + 16 *
                (bit4 ? tileDataIndex : ((byte) tileDataIndex));

        /* Extracts bytes which contain the specific pixel data */
        int lowerByte = mmu.readByte(tileDataAdress + 2 * tilePixelY);
        int higherByte = mmu.readByte(tileDataAdress + 2 * tilePixelY + 1);

        /*
         * Each rowInSprite contains 2 bytes. Lower byte contains the lower bit,
         * higher byte contains the higher bit. Bit7 - leftmost pixel;
         * bit0 - rightmost pixel
         */
        int hBit = (higherByte >> (7 - tilePixelX)) & 0x01;
        int lBit = (lowerByte >> (7 - tilePixelX)) & 0x01;

        int color = (hBit << 1) | lBit;
        return color;

    }

    /**
     * if LY == LYC, then set STAT.2 = true;
     * if STAT.2 == true and STAT.6 == true;
     * request LCD interrupt
     */
    private void checkLYC() {
        int stat = mmu.readByte(0xFF41);
        if (mmu.readByte(0xFF45) == currentScanline) {
            stat |= 0x04;
            if ((stat & 0x40) != 0) {
                mmu.writeByte(0xFF0F, mmu.readByte(0xFF0F) | 0x02);
            }
        } else {
            stat &= ~(0x04);
        }
        mmu.writeByte(0xFF41, stat);
    }

    private boolean getLCDCBit(int bit) {
        return ((LCDCValue >> bit) & 0x1) == 1;
    }

    private void drawWhiteBackground() {
        for (int i = 0; i < 160; i++) {
            framebuffer[i + currentScanline * 160] = getRGBColor(0);
        }
    }

    private int getRGBColor(int bitValue) {
        int palette = getBGP();
        int shade = (palette >> 2 * bitValue) & 0x03;
        return switch (shade) {
            case 0 -> 0xFFFFFF;
            case 1 -> 0xAAAAAA;
            case 2 -> 0x555555;
            case 3 -> 0x000000;
            default -> 0xFFFFFF;
        };
    }

    private void setPPUOff() {
        currentState = State.H_BLANK;
        currentScanline = 0;
        currentDots = 0;
        setLY();
        setMODE();
    }

    private int getBGP() {
        return mmu.readByte(0xFF47);
    }

    private int getSCY() {
        return mmu.readByte(0xFF42);
    }

    private int getSCX() {
        return mmu.readByte(0xFF43);
    }

    private int getWX() {
        return mmu.readByte(0xFF4B);
    }

    private int getWY() {
        return mmu.readByte(0xFF4A);
    }
}
