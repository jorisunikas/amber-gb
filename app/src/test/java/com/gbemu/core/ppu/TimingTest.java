package com.gbemu.core.ppu;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gbemu.core.graphics.PPU;
import com.gbemu.core.memory.MMU;

public class TimingTest {
    private MMU mmu;
    private PPU ppu;

    @BeforeEach
    void setup() {
        this.mmu = new MMU();
        this.ppu = new PPU(mmu);
    }

    @Test
    void testTransitions() {
        ppu.step(79); 
        assertThat(getSTAT() & 0x03).isEqualTo(2);

        ppu.step(1); 
        assertThat(getSTAT() & 0x03).isEqualTo(3);

        ppu.step(171);
        assertThat(getSTAT() & 0x03).isEqualTo(3);

        ppu.step(1); 
        assertThat(getSTAT() & 0x03).isEqualTo(0);

        ppu.step(203);
        assertThat(getSTAT() & 0x03).isEqualTo(0);
        assertThat(getLY()).isEqualTo(0);

        ppu.step(1);
        assertThat(getSTAT() & 0x03).isEqualTo(2);
        assertThat(getLY()).isEqualTo(1);
    }

    @Test
    void testVBLANK() {
        ppu.step(0);
        assertThat(getSTAT() & 0x03).isEqualTo(2);

        ppu.step(456 * 143);
        assertThat(getLY()).isEqualTo(143);
        assertThat(getSTAT() & 0x03).isEqualTo(2);
        assertThat(mmu.readByte(0xFF0F) & 0x01).isEqualTo(0);

        ppu.step(456);
        assertThat(getLY()).isEqualTo(144);
        assertThat(getSTAT() & 0x03).isEqualTo(1);
        assertThat(mmu.readByte(0xFF0F) & 0x01).isEqualTo(1);

        ppu.step(456 * 10);
        assertThat(getSTAT() & 0x03).isEqualTo(2);
        assertThat(getLY()).isEqualTo(0);
    }

    private int getSTAT() {
        return mmu.readByte(0xFF41);
    }

    private int getLY() {
        return mmu.readByte(0xFF44);
    }
}
