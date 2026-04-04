package com.gbemu.core.timer;

import com.gbemu.core.memory.MMU;

public class Timer {
    private final MMU mmu;
    private int divCycles, timaCycles;

    public Timer(MMU mmu) {
        this.mmu = mmu;
        divCycles = 0;
        timaCycles = 0;
    }

    public void step(int cycles) {
        /* INC DIV */
        divCycles += cycles;
        if (divCycles >= 256) {
            divCycles -= 256;
            mmu.writeByte(0xFF04, mmu.readByte(0xFF04) + 1);
        }

        /* check if TIMA is enabled */
        int tac = mmu.readByte(0xFF07);
        if ((tac & 0x04) == 0) {
            return;
        }

        timaCycles += cycles;
        int threshold = switch (tac & 0x03) {
            case 0 -> 1024;
            case 1 -> 16;
            case 2 -> 64;
            case 3 -> 256;
            default -> 1024;
        };

        /* INC TIMA */
        while (timaCycles >= threshold) {
            timaCycles -= threshold;
            int timaValue = mmu.readByte(0xFF05) + 1;

            /* upon TIMA overflow: reset to TMA, request an interupt */
            if (timaValue > 0xFF) {
                timaValue = mmu.readByte(0xFF06);
                mmu.writeByte(0xFF0F, mmu.readByte(0xFF0F) | 0x04);
            }
            mmu.writeByte(0xFF05, timaValue);
        }
    }
}
