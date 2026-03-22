package com.gbemu.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class OpcodeLoad16Test extends OpcodeTestBase {

    @Test
    void test_ld_bc_u16() {
        mmu.writeByte(0x0000, 0x01);
        mmu.writeByte(0x0001, 0x12);
        mmu.writeByte(0x0002, 0x34);
        assertThat(cpu.step()).isEqualTo(12);
        assertThat(reg.getBC()).isEqualTo(0x3412);
    }

    @Test
    void test_ld_de_u16() {
        mmu.writeByte(0x0000, 0x11);
        mmu.writeByte(0x0001, 0x12);
        mmu.writeByte(0x0002, 0x34);
        assertThat(cpu.step()).isEqualTo(12);
        assertThat(reg.getDE()).isEqualTo(0x3412);
    }

    @Test
    void test_ld_hl_u16() {
        mmu.writeByte(0x0000, 0x21);
        mmu.writeByte(0x0001, 0x12);
        mmu.writeByte(0x0002, 0x34);
        assertThat(cpu.step()).isEqualTo(12);
        assertThat(reg.getHL()).isEqualTo(0x3412);
    }

    @Test
    void test_ld_sp_u16() {
        mmu.writeByte(0x0000, 0x31);
        mmu.writeByte(0x0001, 0x12);
        mmu.writeByte(0x0002, 0x34);
        assertThat(cpu.step()).isEqualTo(12);
        assertThat(reg.getSP()).isEqualTo(0x3412);
    }

    @Test
    void test_ld_a_u16ptr() {
        mmu.writeByte(0x0000, 0xFA);
        mmu.writeByte(0x0001, 0x00);
        mmu.writeByte(0x0002, 0x30);
        mmu.writeByte(0x3000, 0x42);
        assertThat(cpu.step()).isEqualTo(16);
        assertThat(reg.getA()).isEqualTo(0x42);
    }

    @Test
    void test_ld_u16ptr_a() {
        reg.setA(0x42);
        mmu.writeByte(0x0000, 0xEA);
        mmu.writeByte(0x0001, 0x00);
        mmu.writeByte(0x0002, 0x30);
        assertThat(cpu.step()).isEqualTo(16);
        assertThat(mmu.readByte(0x3000)).isEqualTo(0x42);
    }
}
