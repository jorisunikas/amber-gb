package com.gbemu.core.graphics;

import com.gbemu.core.memory.MMU;

public class PPU {
    private MMU mmu;

    private int currentDots;
    private int currentScanline;
    private State currentState;

    private enum State {
        OAM_SCAN, DRAW, H_BLANK, V_BLANK
    }

    public PPU(MMU mmu) {
        this.mmu = mmu;

        currentDots = 0;
        currentScanline = 0;
        currentState = State.OAM_SCAN;
    }

    public void step(int cycles) {
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
                currentState = State.H_BLANK;
            }
        } else {
            currentState = State.V_BLANK;
        }

        setMODE();
        setLY();
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

        mmu.writeByte(0xFF41, (mmu.readByte(0xFF41) & 0xFC) | value);
    }
}
