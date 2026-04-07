package com.gbemu.core.graphics;

/**
 * Return value for sprite pixel queries.
 * raw pixel - 2 bit color index (0 - transparent, 1-3 colored)
 * encoded pixel - RGB colored from palette, valid only if rawPixel != 0
 */
public record SpritePixel(int rawPixel, int encodedPixel) {
}
