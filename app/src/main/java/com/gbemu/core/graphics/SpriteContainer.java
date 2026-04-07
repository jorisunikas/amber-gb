package com.gbemu.core.graphics;

import java.util.Arrays;

import com.gbemu.core.memory.MMU;

public class SpriteContainer {
    private final int SPRITE_SIZE = 10;
    private Sprite[] sprites;
    private int length;
    private int y;
    private int LDLCValue;
    private MMU mmu;

    public SpriteContainer(MMU mmu) {
        this.mmu = mmu;

        sprites = new Sprite[SPRITE_SIZE];
        length = 0;
        y = 0;
    }

    /**
     * Updates y position inside the container.
     * This method should be called at the end of OAM SCAN.
     *
     * @param y current scanline
     */
    public void updateScanline(int y) {
        this.y = y;
        this.LDLCValue = mmu.readByte(0xFF40);
        readSprites();
    }

    /**
     * Returns RGB encoded pixel of the highest priority sprite at location (x,
     * scanline).
     * Returns 0 if sprites are disabled, no sprite exists at this location or the
     * pixel is transparent.
     * 
     * @param x               screen X coordinate (0-159)
     * @param hasHighPriority true = sprite is drawn on top of background
     * @return RGB color value, 0 if no pixel exists here or sprites are disabled
     */
    public int getEncodedPixel(int x, boolean hasHighPriority) {
        if (!areObjectsEnabled())
            return 0;
        Sprite s = getSprite(x, hasHighPriority);
        if (s == null)
            return 0;
        return getPixel(s, x).encodedPixel();
    }

    /**
     * Checks if sprites are enabled and if a sprite exists at current location X.
     * Returns false if sprites are disabled, no sprite exists or pixel is
     * transparent.
     *
     * @param x               screen X coordinate (0-159)
     * @param hasHighPriority true = sprite is drawn on top of background
     * @return true = a non transparent sprite exists at this location
     */
    public boolean isSprite(int x, boolean hasHighPriority) {
        if (!areObjectsEnabled())
            return false;
        Sprite s = getSprite(x, hasHighPriority);
        if (s == null)
            return false;
        return getPixel(s, x).rawPixel() != 0;
    }

    /* PRIVATE */

    private Sprite getSprite(int x, boolean hasHighPriority) {
        int minX = 1000;
        Sprite result = null;
        for (Sprite s : sprites) {
            if (s == null)
                break;
            if ((s.x - 8) <= x && s.x > x && s.x < minX && s.hasPriority() == hasHighPriority) {
                result = s;
                minX = s.x;
            }
        }
        return result;
    }

    private void readSprites() {
        Arrays.fill(sprites, null);
        int height = getLDLCBit(2) ? 16 : 8;
        length = 0;
        for (int i = 0; i < 40 && length < 10; i++) {
            int address = 0xFE00 + i * 4;
            int spriteY = mmu.readByte(address) - 16;
            if (y >= spriteY && y < spriteY + height) {
                sprites[length++] = (new Sprite(
                        mmu.readByte(address),
                        mmu.readByte(address + 1),
                        mmu.readByte(address + 2),
                        mmu.readByte(address + 3)));
            }
        }
    }

    private SpritePixel getPixel(Sprite s, int x) {
        int spriteRow = y - (s.y - 16);
        if (s.isFlipY())
            spriteRow = (getLDLCBit(2) ? 16 : 8) - spriteRow - 1;
        int address = 0x8000
                + 16 * (getLDLCBit(2) ? (spriteRow < 8 ? s.tileIndex & 0xFE : s.tileIndex | 0x01) : s.tileIndex);

        int pixelYInTile = spriteRow % 8;
        int pixelXInTile = (x - (s.x - 8)) % 8;
        if (s.isFlipX())
            pixelXInTile = 7 - pixelXInTile;

        int lowerByte = mmu.readByte(address + 2 * pixelYInTile);
        int higherByte = mmu.readByte(address + 2 * pixelYInTile + 1);

        int hBit = (higherByte >> (7 - pixelXInTile)) & 0x01;
        int lBit = (lowerByte >> (7 - pixelXInTile)) & 0x01;

        int rawPixel = (hBit << 1) | lBit;
        int encodedPixel = encodePixel(rawPixel, s.getPalette());
        return new SpritePixel(rawPixel, encodedPixel);
    }

    private boolean getLDLCBit(int bit) {
        return ((LDLCValue >> bit) & 0x1) == 1;
    }

    private boolean areObjectsEnabled() {
        return getLDLCBit(1);
    }

    private int encodePixel(int bitValue, int paletteOffset) {
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
}
