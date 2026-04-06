package com.gbemu.core.graphics;

import com.gbemu.core.memory.MMU;

public class PPU {
    private MMU mmu;

    private int currentDots;
    private int LDLCValue;
    private int currentScanline;
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
        LDLCValue = mmu.readByte(0xFF40);
        currentState = State.OAM_SCAN;
    }

    public void step(int cycles) {
        LDLCValue = mmu.readByte(0xFF40);

        /* LDLC.7 == 0; PPU turns off */
        if ((LDLCValue & 0x80) == 0) {
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

            if (currentScanline > 153)
                currentScanline = 0;
        }

        if (currentScanline < 144) {
            if (currentDots < 80) {
                currentState = State.OAM_SCAN;
            } else if (currentDots < 252) {
                currentState = State.DRAW;
            } else {
                if (currentState == State.DRAW)
                    drawScanline(LDLCValue);
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

        /* Sets current PPU mode to the lower 2 bits of LDLC register */
        mmu.writeByte(0xFF41, (mmu.readByte(0xFF41) & 0xFC) | value);
    }

    private void drawScanline(int LDLCValue) {
        if ((LDLCValue & 0x01) == 0) {
            drawWhiteBackground();
            return;
        }

        boolean bit4 = getLDLCBit(4);

        /* Calculates base values for indexing Tilemaps and Tiles themselves */
        int tileMapBaseBackground = getLDLCBit(3) ? 0x9C00 : 0x9800;
        int tileMapBaseWindow = getLDLCBit(6) ? 0x9C00 : 0x9800;
        int tileDataBase = bit4 ? 0x8000 : 0x9000;

        /* ... & 0xFF - Accounts for background wrapping */
        int screenY = (getSCY() + currentScanline) & 0xFF;

        for (int i = 0; i < 160; i++) {
            int screenX = (getSCX() + i) & 0xFF;

            boolean windowVisible = i >= (getWX() - 7) && currentScanline >= getWY() && getLDLCBit(5);
            if (windowVisible) {
                int windowX = i - (getWX() - 7);
                int windowY = currentScanline - getWY();
                drawPixel(tileMapBaseWindow, tileDataBase, bit4, i, windowX, windowY);
                continue;
            }

            drawPixel(tileMapBaseBackground, tileDataBase, bit4, i, screenX, screenY);
        }
    }

    private void drawPixel(int tileMapBase, int tileDataBase, boolean bit4, int i, int pixelX, int pixelY) {
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
         * Each row contains 2 bytes. Lower byte contains the lower bit,
         * higher byte contains the higher bit. Bit7 - leftmost pixel;
         * bit0 - rightmost pixel
         */
        int hBit = (higherByte >> (7 - tilePixelX)) & 0x01;
        int lBit = (lowerByte >> (7 - tilePixelX)) & 0x01;

        int color = (hBit << 1) | lBit;
        framebuffer[i + currentScanline * 160] = getRGBColor(color);

    }

    private boolean getLDLCBit(int bit) {
        return ((LDLCValue >> bit) & 0x1) == 1;
    }

    private void drawWhiteBackground() {
        for (int i = 0; i < 160; i++) {
            framebuffer[i + currentScanline * 160] = 0xFFFFFF;
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
