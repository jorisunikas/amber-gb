package com.gbemu.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class OpcodeJumpTest extends OpcodeTestBase {

    @Test
    void test_call_u16() {
        reg.setSP(0x3000);
        mmu.writeByte(0x0000, 0xCD);
        mmu.writeByte(0x0001, 0x00);
        mmu.writeByte(0x0002, 0x10);
        assertThat(cpu.step()).as("call u16 cycles = 24").isEqualTo(24);
        assertThat(reg.getPC()).isEqualTo(0x1000);
        assertThat(reg.getSP()).isEqualTo(0x2FFE);
    }

    @Test
    void test_ret() {
        reg.setSP(0xFFFC);
        mmu.writeByte(0xFFFC, 0x50);
        mmu.writeByte(0xFFFD, 0x01);
        mmu.writeByte(0x0000, 0xC9);
        assertThat(cpu.step()).as("ret cycles = 16").isEqualTo(16);
        assertThat(reg.getPC()).isEqualTo(0x0150);
        assertThat(reg.getSP()).isEqualTo(0xFFFE);
    }

    @Test
    void test_ret_nz_takesJump() {
        reg.setFlagZ(false);
        reg.setSP(0xFFFC);
        mmu.writeByte(0xFFFC, 0x50);
        mmu.writeByte(0xFFFD, 0x01);
        mmu.writeByte(0x0000, 0xC0);
        assertThat(cpu.step()).as("ret nz cycles = 16").isEqualTo(16);
        assertThat(reg.getPC()).isEqualTo(0x0150);
        assertThat(reg.getSP()).isEqualTo(0xFFFE);
    }

    @Test
    void test_ret_nz_fallsThrough() {
        reg.setFlagZ(true);
        reg.setSP(0xFFFC);
        mmu.writeByte(0xFFFC, 0x50);
        mmu.writeByte(0xFFFD, 0x01);
        mmu.writeByte(0x0000, 0xC0);
        assertThat(cpu.step()).as("ret nz cycles = 8").isEqualTo(8);
        assertThat(reg.getPC()).isEqualTo(0x0001);
        assertThat(reg.getSP()).isEqualTo(0xFFFC);
    }

    @Test
    void test_ret_z_takesJump() {
        reg.setFlagZ(true);
        reg.setSP(0xFFFC);
        mmu.writeByte(0xFFFC, 0x50);
        mmu.writeByte(0xFFFD, 0x01);
        mmu.writeByte(0x0000, 0xC8);
        assertThat(cpu.step()).as("ret z cycles = 16").isEqualTo(16);
        assertThat(reg.getPC()).isEqualTo(0x0150);
        assertThat(reg.getSP()).isEqualTo(0xFFFE);
    }

    @Test
    void test_ret_z_fallsThrough() {
        reg.setFlagZ(false);
        reg.setSP(0xFFFC);
        mmu.writeByte(0xFFFC, 0x50);
        mmu.writeByte(0xFFFD, 0x01);
        mmu.writeByte(0x0000, 0xC8);
        assertThat(cpu.step()).as("ret z cycles = 8").isEqualTo(8);
        assertThat(reg.getPC()).isEqualTo(0x0001);
        assertThat(reg.getSP()).isEqualTo(0xFFFC);
    }

    @Test
    void test_ret_nc_takesJump() {
        reg.setFlagC(false);
        reg.setSP(0xFFFC);
        mmu.writeByte(0xFFFC, 0x50);
        mmu.writeByte(0xFFFD, 0x01);
        mmu.writeByte(0x0000, 0xD0);
        assertThat(cpu.step()).as("ret nc cycles = 16").isEqualTo(16);
        assertThat(reg.getPC()).isEqualTo(0x0150);
        assertThat(reg.getSP()).isEqualTo(0xFFFE);
    }

    @Test
    void test_ret_nc_fallsThrough() {
        reg.setFlagC(true);
        reg.setSP(0xFFFC);
        mmu.writeByte(0xFFFC, 0x50);
        mmu.writeByte(0xFFFD, 0x01);
        mmu.writeByte(0x0000, 0xD0);
        assertThat(cpu.step()).as("ret nc cycles = 8").isEqualTo(8);
        assertThat(reg.getPC()).isEqualTo(0x0001);
        assertThat(reg.getSP()).isEqualTo(0xFFFC);
    }

    @Test
    void test_ret_c_takesJump() {
        reg.setFlagC(true);
        reg.setSP(0xFFFC);
        mmu.writeByte(0xFFFC, 0x50);
        mmu.writeByte(0xFFFD, 0x01);
        mmu.writeByte(0x0000, 0xD8);
        assertThat(cpu.step()).as("ret c cycles = 16").isEqualTo(16);
        assertThat(reg.getPC()).isEqualTo(0x0150);
        assertThat(reg.getSP()).isEqualTo(0xFFFE);
    }

    @Test
    void test_ret_c_fallsThrough() {
        reg.setFlagC(false);
        reg.setSP(0xFFFC);
        mmu.writeByte(0xFFFC, 0x50);
        mmu.writeByte(0xFFFD, 0x01);
        mmu.writeByte(0x0000, 0xD8);
        assertThat(cpu.step()).as("ret c cycles = 8").isEqualTo(8);
        assertThat(reg.getPC()).isEqualTo(0x0001);
        assertThat(reg.getSP()).isEqualTo(0xFFFC);
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
        mmu.writeByte(0x0001, 0xFB);
        assertThat(cpu.step()).as("jr s8 cycles = 12").isEqualTo(12);
        assertThat(reg.getPC()).isEqualTo(0xFFFD);
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
