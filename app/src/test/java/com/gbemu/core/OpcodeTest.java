package com.gbemu.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void test_jr_s8_positiveOffset() {
        mmu.writeByte(0x0000, 0x18);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("jr s8 cycles = 12").isEqualTo(12);
        assertThat(reg.getPC()).isEqualTo(0x0022);
    }

    @Test
    void test_jr_s8_negativeOffset() {
        mmu.writeByte(0x0000, 0x18);
        mmu.writeByte(0x0001, 0xFB); // -5 as signed byte
        assertThat(cpu.step()).as("jr s8 cycles = 12").isEqualTo(12);
        assertThat(reg.getPC()).isEqualTo(0xFFFD); // 0x0002 + (-5)
    }

    @Test
    void test_jr_nz_s8_takesJump() {
        reg.setFlagZ(false);
        mmu.writeByte(0x0000, 0x20);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("jr nz s8 cycles = 12").isEqualTo(12);
        assertThat(reg.getPC()).isEqualTo(0x0022);
    }

    @Test
    void test_jr_nz_s8_fallsThrough() {
        reg.setFlagZ(true);
        mmu.writeByte(0x0000, 0x20);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("jr nz s8 cycles = 8").isEqualTo(8);
        assertThat(reg.getPC()).isEqualTo(0x0002);
    }

    @Test
    void test_jr_z_s8_takesJump() {
        reg.setFlagZ(true);
        mmu.writeByte(0x0000, 0x28);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("jr z s8 cycles = 12").isEqualTo(12);
        assertThat(reg.getPC()).isEqualTo(0x0022);
    }

    @Test
    void test_jr_z_s8_fallsThrough() {
        reg.setFlagZ(false);
        mmu.writeByte(0x0000, 0x28);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("jr z s8 cycles = 8").isEqualTo(8);
        assertThat(reg.getPC()).isEqualTo(0x0002);
    }

    @Test
    void test_jr_nc_s8_takesJump() {
        reg.setFlagC(false);
        mmu.writeByte(0x0000, 0x30);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("jr nc s8 cycles = 12").isEqualTo(12);
        assertThat(reg.getPC()).isEqualTo(0x0022);
    }

    @Test
    void test_jr_nc_s8_fallsThrough() {
        reg.setFlagC(true);
        mmu.writeByte(0x0000, 0x30);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("jr nc s8 cycles = 8").isEqualTo(8);
        assertThat(reg.getPC()).isEqualTo(0x0002);
    }

    @Test
    void test_jr_c_s8_takesJump() {
        reg.setFlagC(true);
        mmu.writeByte(0x0000, 0x38);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("jr c s8 cycles = 12").isEqualTo(12);
        assertThat(reg.getPC()).isEqualTo(0x0022);
    }

    @Test
    void test_jr_c_s8_fallsThrough() {
        reg.setFlagC(false);
        mmu.writeByte(0x0000, 0x38);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("jr c s8 cycles = 8").isEqualTo(8);
        assertThat(reg.getPC()).isEqualTo(0x0002);
    }

    @Test
    void test_jp_nz_u16_takesJump() {
        reg.setFlagZ(false);
        mmu.writeByte(0x0000, 0xC2);
        mmu.writeByte(0x0001, 0x50);
        mmu.writeByte(0x0002, 0x01);
        assertThat(cpu.step()).as("jp nz u16 cycles = 16").isEqualTo(16);
        assertThat(reg.getPC()).isEqualTo(0x0150);
    }

    @Test
    void test_jp_nz_u16_fallsThrough() {
        reg.setFlagZ(true);
        mmu.writeByte(0x0000, 0xC2);
        mmu.writeByte(0x0001, 0x50);
        mmu.writeByte(0x0002, 0x01);
        assertThat(cpu.step()).as("jr nz u16 cycles = 12").isEqualTo(12);
        assertThat(reg.getPC()).isEqualTo(0x0003);
    }

    @Test
    void test_jp_z_u16_takesJump() {
        reg.setFlagZ(true);
        mmu.writeByte(0x0000, 0xCA);
        mmu.writeByte(0x0001, 0x50);
        mmu.writeByte(0x0002, 0x01);
        assertThat(cpu.step()).as("jp z u16 cycles = 16").isEqualTo(16);
        assertThat(reg.getPC()).isEqualTo(0x0150);
    }

    @Test
    void test_jp_z_u16_fallsThrough() {
        reg.setFlagZ(false);
        mmu.writeByte(0x0000, 0xCA);
        mmu.writeByte(0x0001, 0x50);
        mmu.writeByte(0x0002, 0x01);
        assertThat(cpu.step()).as("jr z u16 cycles = 12").isEqualTo(12);
        assertThat(reg.getPC()).isEqualTo(0x0003);
    }

    @Test
    void test_jp_nc_u16_takesJump() {
        reg.setFlagC(false);
        mmu.writeByte(0x0000, 0xD2);
        mmu.writeByte(0x0001, 0x50);
        mmu.writeByte(0x0002, 0x01);
        assertThat(cpu.step()).as("jp nc u16 cycles = 16").isEqualTo(16);
        assertThat(reg.getPC()).isEqualTo(0x0150);
    }

    @Test
    void test_jp_nc_u16_fallsThrough() {
        reg.setFlagC(true);
        mmu.writeByte(0x0000, 0xD2);
        mmu.writeByte(0x0001, 0x50);
        mmu.writeByte(0x0002, 0x01);
        assertThat(cpu.step()).as("jr nc u16 cycles = 12").isEqualTo(12);
        assertThat(reg.getPC()).isEqualTo(0x0003);
    }

    @Test
    void test_jp_c_u16_takesJump() {
        reg.setFlagC(true);
        mmu.writeByte(0x0000, 0xDA);
        mmu.writeByte(0x0001, 0x50);
        mmu.writeByte(0x0002, 0x01);
        assertThat(cpu.step()).as("jp c u16 cycles = 16").isEqualTo(16);
        assertThat(reg.getPC()).isEqualTo(0x0150);
    }

    @Test
    void test_jp_c_u16_fallsThrough() {
        reg.setFlagC(false);
        mmu.writeByte(0x0000, 0xDA);
        mmu.writeByte(0x0001, 0x50);
        mmu.writeByte(0x0002, 0x01);
        assertThat(cpu.step()).as("jr c u16 cycles = 12").isEqualTo(12);
        assertThat(reg.getPC()).isEqualTo(0x0003);
    }
}
