package com.gbemu.core.graphics;

public class Sprite {
    public final int y, x, tileIndex, attributes;

    public Sprite(int y, int x, int tileIndex, int attributes) {
        this.y = y;
        this.x = x;
        this.tileIndex = tileIndex;
        this.attributes = attributes;
    }

    /** {return} opposite of bit7 in attributes */
    public boolean hasPriority() {
        return ((attributes >> 7) & 0x01) == 0;
    }

    public boolean isFlipX() {
        return ((attributes >> 5) & 0x01) == 1;
    }

    public boolean isFlipY() {
        return ((attributes >> 6) & 0x01) == 1;
    }

    public int getPalette() {
        return ((attributes >> 4) & 0x01);
    }
}
