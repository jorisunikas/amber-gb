package com.gbemu.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class OpcodeMiscTest extends OpcodeTestBase {

    @Test
    void test_nop() {
        mmu.writeByte(0x0000, 0x00);
        assertThat(cpu.step()).as("nop cycles = 4").isEqualTo(4);
    }

    @Test
    void test_halt() {
        mmu.writeByte(0x0000, 0x76);
        mmu.writeByte(0x0001, 0xD8);
        mmu.writeByte(0xFFFC, 0x50);
        mmu.writeByte(0xFFFD, 0x01);
        assertThat(cpu.step()).as("halt cycles = 4").isEqualTo(4);
        int pc = reg.getPC();
        assertThat(cpu.step()).as("halt cycles = 4, no further excecution occurs").isEqualTo(4);
        assertThat(pc).as("pc does not increment").isEqualTo(reg.getPC());
    }
}
