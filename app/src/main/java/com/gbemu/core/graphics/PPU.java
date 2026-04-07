package com.gbemu.core.graphics;

import com.gbemu.core.memory.MMU;

public class PPU {
    private MMU mmu;

    private int currentDots;
    private int LDLCValue;
    private int currentScanline;
    private int windowLineCounter;
    private Sprite[] currentSprites;
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
        LDLCValue = mmu.readByte(0xFF40);
        currentSprites = new Sprite[10];
        currentState = State.OAM_SCAN;
        sprites = new SpriteContainer(mmu);
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
                    currentSprites = readSprites();
                }
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

        boolean hasWindowInc = false;
        boolean bit4 = getLDLCBit(4);
        boolean isObjectsEnabled = getLDLCBit(1);

        /* Calculates base values for indexing Tilemaps and Tiles themselves */
        int tileMapBaseBackground = getLDLCBit(3) ? 0x9C00 : 0x9800;
        int tileMapBaseWindow = getLDLCBit(6) ? 0x9C00 : 0x9800;
        int tileDataBase = bit4 ? 0x8000 : 0x9000;

        /* ... & 0xFF - Accounts for background wrapping */
        int screenY = (getSCY() + currentScanline) & 0xFF;

        for (int i = 0; i < 160; i++) {
            int screenX = (getSCX() + i) & 0xFF;
            boolean isWindowVisible = i >= (getWX() - 7) && currentScanline >= getWY() && getLDLCBit(5);

            if (sprites.isSprite(i, true) && isObjectsEnabled) {
                    framebuffer[i + currentScanline * 160] = sprites.getEncodedPixel(i, true);
                    continue;
            }
            if (isWindowVisible) {
                int windowX = i - (getWX() - 7);
                /* Window cannot rely on scanline count, it needs it's own counter */
                int windowY = windowLineCounter;
                framebuffer[i + currentScanline * 160] = getRGBColor(
                        getPixelValue(tileMapBaseWindow, tileDataBase, bit4, i, windowX, windowY));
                hasWindowInc = true;
                continue;
            }

            int pixelValue = getPixelValue(tileMapBaseBackground, tileDataBase, bit4, i, screenX, screenY);
            framebuffer[i + currentScanline * 160] = getRGBColor(pixelValue);
            if (sprites.isSprite(i, false) && isObjectsEnabled && pixelValue == 0) {
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

    private int getSpritePixelValue(Sprite s, int pixel) {
        int spriteRow = currentScanline - (s.y - 16);
        if (s.isFlipY())
            spriteRow = (getLDLCBit(2) ? 16 : 8) - spriteRow - 1;
        int address = 0x8000
                + 16 * (getLDLCBit(2) ? (spriteRow < 8 ? s.tileIndex & 0xFE : s.tileIndex | 0x01) : s.tileIndex);

        int pixelYInTile = spriteRow % 8;
        int pixelXInTile = (pixel - (s.x - 8)) % 8;
        if (s.isFlipX())
            pixelXInTile = 7 - pixelXInTile;

        int lowerByte = mmu.readByte(address + 2 * pixelYInTile);
        int higherByte = mmu.readByte(address + 2 * pixelYInTile + 1);

        int hBit = (higherByte >> (7 - pixelXInTile)) & 0x01;
        int lBit = (lowerByte >> (7 - pixelXInTile)) & 0x01;

        int color = (hBit << 1) | lBit;
        return color;
    }

    private boolean isExistingSpriteHigh(int screenX, int screenY) {
        int value = indexOfExistingSprite(screenX, screenY);
        if (value == -1)
            return false;
        return currentSprites[value].hasPriority();
    }

    private boolean isExistingSpriteLow(int screenX, int screenY) {
        int value = indexOfExistingSprite(screenX, screenY);
        if (value == -1)
            return false;
        return !currentSprites[value].hasPriority();
    }

    private int indexOfExistingSprite(int screenX, int screenY) {
        int lowestX = 1000;
        int index = -1;
        for (int i = 0; i < currentSprites.length; i++) {
            Sprite s = currentSprites[i];
            if (s == null)
                break;
            if ((s.x - 8) <= screenX && s.x > screenX && s.x < lowestX) {
                index = i;
                lowestX = s.x;
            }
        }
        return index;
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

    private Sprite[] readSprites() {
        Sprite[] sprites = new Sprite[10];
        int height = getLDLCBit(2) ? 16 : 8;
        int count = 0;
        for (int i = 0; i < 40 && count < 10; i++) {
            int address = 0xFE00 + i * 4;
            int spriteY = mmu.readByte(address) - 16;
            if (currentScanline >= spriteY && currentScanline < spriteY + height) {
                sprites[count++] = (new Sprite(
                        mmu.readByte(address),
                        mmu.readByte(address + 1),
                        mmu.readByte(address + 2),
                        mmu.readByte(address + 3)));
            }
        }
        return sprites;
    }

    private boolean getLDLCBit(int bit) {
        return ((LDLCValue >> bit) & 0x1) == 1;
    }

    private void drawWhiteBackground() {
        for (int i = 0; i < 160; i++) {
            framebuffer[i + currentScanline * 160] = getRGBColor(0);
        }
    }

    private int getRGBColorPallete(int bitValue, int paletteOffset) {
        int palette = mmu.readByte(0xFF48 + paletteOffset);
        int shade = (palette >> 2 * bitValue) & 0x03;
        return switch (shade) {
            case 0 -> 0xFFFFFF;
            case 1 -> 0xAAAAAA;
            case 2 -> 0x555555;
            case 3 -> 0x000000;
            default -> 0xFFFFFF;
        };
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
