package com.gbemu.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class OpcodeTest {
    private CPU cpu;
    private MMU mmu;
    private Registers reg;

    @BeforeEach
    void setup() {
        mmu = new MMU();
        reg = new Registers();
        cpu = new CPU(mmu, reg);
    }

    @Test
    void test_nop() {
        mmu.writeByte(0x0000, 0x00);
        assertThat(cpu.step()).as("nop cycles = 4").isEqualTo(4);
    }

    @Test 
    void test_jp_u16() {
        mmu.writeByte(0x0000, 0xC3);
        mmu.writeByte(0x0001, 0x50);
        mmu.writeByte(0x0002, 0x01);
        assertThat(cpu.step()).as("jp u16 cycles = 16").isEqualTo(16);
        assertThat(reg.getPC()).isEqualTo(0x0150);
    }

    @Test
    void test_jr_s8() {
        mmu.writeByte(0x0000, 0x18);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("jr s8 cycles = 12").isEqualTo(12);
        assertThat(reg.getPC()).isEqualTo(0x0022); // 0x20 + 0x02 (after reading)
    }

    @Test
    void test_jr_nz_s8_jumps() {
        reg.flagZ = false;
        mmu.writeByte(0x0000, 0x18);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("jr nz s8 cycles = 12").isEqualTo(12);
        assertThat(reg.getPC()).isEqualTo(0x0022); // 0x20 + 0x02 (after reading)
    }

    @Test
    void test_jr_nz_s8_doesnt() {
        reg.flagZ = true;
        mmu.writeByte(0x0000, 0x18);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("jr nz s8 cycles = 8").isEqualTo(8);
        assertThat(reg.getPC()).isEqualTo(0x0002); // 0x20 + 0x02 (after reading)
    }
}
