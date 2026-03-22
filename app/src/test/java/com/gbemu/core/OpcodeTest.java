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
        reg.setFlagZ(false);
        reg.setSP(0xFFFC);
        mmu.writeByte(0xFFFC, 0x50);
        mmu.writeByte(0xFFFD, 0x01);
        mmu.writeByte(0x0000, 0xD8);
        assertThat(cpu.step()).as("ret c cycles = 8").isEqualTo(8);
        assertThat(reg.getPC()).isEqualTo(0x0001);
        assertThat(reg.getSP()).isEqualTo(0xFFFC);
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

    @Test
    void test_ld_b_u8() {
        mmu.writeByte(0x0000, 0x06);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("ld b, u8 = 8").isEqualTo(8);
        assertThat(reg.getB()).isEqualTo(0x20);
    }

    @Test
    void test_ld_c_u8() {
        mmu.writeByte(0x0000, 0x0E);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("ld c, u8 = 8").isEqualTo(8);
        assertThat(reg.getC()).isEqualTo(0x20);
    }

    @Test
    void test_ld_d_u8() {
        mmu.writeByte(0x0000, 0x16);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("ld d, u8 = 8").isEqualTo(8);
        assertThat(reg.getD()).isEqualTo(0x20);
    }

    @Test
    void test_ld_e_u8() {
        mmu.writeByte(0x0000, 0x1E);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("ld e, u8 = 8").isEqualTo(8);
        assertThat(reg.getE()).isEqualTo(0x20);
    }

    @Test
    void test_ld_h_u8() {
        mmu.writeByte(0x0000, 0x26);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("ld h, u8 = 8").isEqualTo(8);
        assertThat(reg.getH()).isEqualTo(0x20);
    }

    @Test
    void test_ld_l_u8() {
        mmu.writeByte(0x0000, 0x2E);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("ld l, u8 = 8").isEqualTo(8);
        assertThat(reg.getL()).isEqualTo(0x20);
    }

    @Test
    void test_ld_a_u8() {
        mmu.writeByte(0x0000, 0x3E);
        mmu.writeByte(0x0001, 0x20);
        assertThat(cpu.step()).as("ld a, u8 = 8").isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0x20);
    }

    // LD B, r
    @Test
    void test_ld_b_b() {
        reg.setB(0x42);
        mmu.writeByte(0x0000, 0x40);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getB()).isEqualTo(0x42);
    }

    @Test
    void test_ld_b_c() {
        reg.setC(0x42);
        mmu.writeByte(0x0000, 0x41);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getB()).isEqualTo(0x42);
    }

    @Test
    void test_ld_b_d() {
        reg.setD(0x42);
        mmu.writeByte(0x0000, 0x42);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getB()).isEqualTo(0x42);
    }

    @Test
    void test_ld_b_e() {
        reg.setE(0x42);
        mmu.writeByte(0x0000, 0x43);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getB()).isEqualTo(0x42);
    }

    @Test
    void test_ld_b_h() {
        reg.setH(0x42);
        mmu.writeByte(0x0000, 0x44);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getB()).isEqualTo(0x42);
    }

    @Test
    void test_ld_b_l() {
        reg.setL(0x42);
        mmu.writeByte(0x0000, 0x45);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getB()).isEqualTo(0x42);
    }

    @Test
    void test_ld_b_a() {
        reg.setA(0x42);
        mmu.writeByte(0x0000, 0x47);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getB()).isEqualTo(0x42);
    }

    // LD C, r
    @Test
    void test_ld_c_b() {
        reg.setB(0x42);
        mmu.writeByte(0x0000, 0x48);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getC()).isEqualTo(0x42);
    }

    @Test
    void test_ld_c_c() {
        reg.setC(0x42);
        mmu.writeByte(0x0000, 0x49);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getC()).isEqualTo(0x42);
    }

    @Test
    void test_ld_c_d() {
        reg.setD(0x42);
        mmu.writeByte(0x0000, 0x4A);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getC()).isEqualTo(0x42);
    }

    @Test
    void test_ld_c_e() {
        reg.setE(0x42);
        mmu.writeByte(0x0000, 0x4B);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getC()).isEqualTo(0x42);
    }

    @Test
    void test_ld_c_h() {
        reg.setH(0x42);
        mmu.writeByte(0x0000, 0x4C);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getC()).isEqualTo(0x42);
    }

    @Test
    void test_ld_c_l() {
        reg.setL(0x42);
        mmu.writeByte(0x0000, 0x4D);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getC()).isEqualTo(0x42);
    }

    @Test
    void test_ld_c_a() {
        reg.setA(0x42);
        mmu.writeByte(0x0000, 0x4F);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getC()).isEqualTo(0x42);
    }

    // LD D, r
    @Test
    void test_ld_d_b() {
        reg.setB(0x42);
        mmu.writeByte(0x0000, 0x50);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getD()).isEqualTo(0x42);
    }

    @Test
    void test_ld_d_c() {
        reg.setC(0x42);
        mmu.writeByte(0x0000, 0x51);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getD()).isEqualTo(0x42);
    }

    @Test
    void test_ld_d_d() {
        reg.setD(0x42);
        mmu.writeByte(0x0000, 0x52);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getD()).isEqualTo(0x42);
    }

    @Test
    void test_ld_d_e() {
        reg.setE(0x42);
        mmu.writeByte(0x0000, 0x53);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getD()).isEqualTo(0x42);
    }

    @Test
    void test_ld_d_h() {
        reg.setH(0x42);
        mmu.writeByte(0x0000, 0x54);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getD()).isEqualTo(0x42);
    }

    @Test
    void test_ld_d_l() {
        reg.setL(0x42);
        mmu.writeByte(0x0000, 0x55);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getD()).isEqualTo(0x42);
    }

    @Test
    void test_ld_d_a() {
        reg.setA(0x42);
        mmu.writeByte(0x0000, 0x57);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getD()).isEqualTo(0x42);
    }

    // LD E, r
    @Test
    void test_ld_e_b() {
        reg.setB(0x42);
        mmu.writeByte(0x0000, 0x58);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getE()).isEqualTo(0x42);
    }

    @Test
    void test_ld_e_c() {
        reg.setC(0x42);
        mmu.writeByte(0x0000, 0x59);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getE()).isEqualTo(0x42);
    }

    @Test
    void test_ld_e_d() {
        reg.setD(0x42);
        mmu.writeByte(0x0000, 0x5A);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getE()).isEqualTo(0x42);
    }

    @Test
    void test_ld_e_e() {
        reg.setE(0x42);
        mmu.writeByte(0x0000, 0x5B);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getE()).isEqualTo(0x42);
    }

    @Test
    void test_ld_e_h() {
        reg.setH(0x42);
        mmu.writeByte(0x0000, 0x5C);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getE()).isEqualTo(0x42);
    }

    @Test
    void test_ld_e_l() {
        reg.setL(0x42);
        mmu.writeByte(0x0000, 0x5D);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getE()).isEqualTo(0x42);
    }

    @Test
    void test_ld_e_a() {
        reg.setA(0x42);
        mmu.writeByte(0x0000, 0x5F);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getE()).isEqualTo(0x42);
    }

    // LD H, r
    @Test
    void test_ld_h_b() {
        reg.setB(0x42);
        mmu.writeByte(0x0000, 0x60);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getH()).isEqualTo(0x42);
    }

    @Test
    void test_ld_h_c() {
        reg.setC(0x42);
        mmu.writeByte(0x0000, 0x61);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getH()).isEqualTo(0x42);
    }

    @Test
    void test_ld_h_d() {
        reg.setD(0x42);
        mmu.writeByte(0x0000, 0x62);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getH()).isEqualTo(0x42);
    }

    @Test
    void test_ld_h_e() {
        reg.setE(0x42);
        mmu.writeByte(0x0000, 0x63);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getH()).isEqualTo(0x42);
    }

    @Test
    void test_ld_h_h() {
        reg.setH(0x42);
        mmu.writeByte(0x0000, 0x64);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getH()).isEqualTo(0x42);
    }

    @Test
    void test_ld_h_l() {
        reg.setL(0x42);
        mmu.writeByte(0x0000, 0x65);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getH()).isEqualTo(0x42);
    }

    @Test
    void test_ld_h_a() {
        reg.setA(0x42);
        mmu.writeByte(0x0000, 0x67);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getH()).isEqualTo(0x42);
    }

    // LD L, r
    @Test
    void test_ld_l_b() {
        reg.setB(0x42);
        mmu.writeByte(0x0000, 0x68);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getL()).isEqualTo(0x42);
    }

    @Test
    void test_ld_l_c() {
        reg.setC(0x42);
        mmu.writeByte(0x0000, 0x69);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getL()).isEqualTo(0x42);
    }

    @Test
    void test_ld_l_d() {
        reg.setD(0x42);
        mmu.writeByte(0x0000, 0x6A);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getL()).isEqualTo(0x42);
    }

    @Test
    void test_ld_l_e() {
        reg.setE(0x42);
        mmu.writeByte(0x0000, 0x6B);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getL()).isEqualTo(0x42);
    }

    @Test
    void test_ld_l_h() {
        reg.setH(0x42);
        mmu.writeByte(0x0000, 0x6C);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getL()).isEqualTo(0x42);
    }

    @Test
    void test_ld_l_l() {
        reg.setL(0x42);
        mmu.writeByte(0x0000, 0x6D);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getL()).isEqualTo(0x42);
    }

    @Test
    void test_ld_l_a() {
        reg.setA(0x42);
        mmu.writeByte(0x0000, 0x6F);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getL()).isEqualTo(0x42);
    }

    // LD A, r
    @Test
    void test_ld_a_b() {
        reg.setB(0x42);
        mmu.writeByte(0x0000, 0x78);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x42);
    }

    @Test
    void test_ld_a_c() {
        reg.setC(0x42);
        mmu.writeByte(0x0000, 0x79);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x42);
    }

    @Test
    void test_ld_a_d() {
        reg.setD(0x42);
        mmu.writeByte(0x0000, 0x7A);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x42);
    }

    @Test
    void test_ld_a_e() {
        reg.setE(0x42);
        mmu.writeByte(0x0000, 0x7B);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x42);
    }

    @Test
    void test_ld_a_h() {
        reg.setH(0x42);
        mmu.writeByte(0x0000, 0x7C);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x42);
    }

    @Test
    void test_ld_a_l() {
        reg.setL(0x42);
        mmu.writeByte(0x0000, 0x7D);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x42);
    }

    @Test
    void test_ld_a_a() {
        reg.setA(0x42);
        mmu.writeByte(0x0000, 0x7F);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x42);
    }

    // LD r, (HL) - read from memory at HL into register
    @Test
    void test_ld_b_hlptr() {
        reg.setHL(0x8000);
        mmu.writeByte(0x8000, 0x42);
        mmu.writeByte(0x0000, 0x46);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getB()).isEqualTo(0x42);
    }

    @Test
    void test_ld_c_hlptr() {
        reg.setHL(0x8000);
        mmu.writeByte(0x8000, 0x42);
        mmu.writeByte(0x0000, 0x4E);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getC()).isEqualTo(0x42);
    }

    @Test
    void test_ld_d_hlptr() {
        reg.setHL(0x8000);
        mmu.writeByte(0x8000, 0x42);
        mmu.writeByte(0x0000, 0x56);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getD()).isEqualTo(0x42);
    }

    @Test
    void test_ld_e_hlptr() {
        reg.setHL(0x8000);
        mmu.writeByte(0x8000, 0x42);
        mmu.writeByte(0x0000, 0x5E);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getE()).isEqualTo(0x42);
    }

    @Test
    void test_ld_h_hlptr() {
        reg.setHL(0x8000);
        mmu.writeByte(0x8000, 0x42);
        mmu.writeByte(0x0000, 0x66);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getH()).isEqualTo(0x42);
    }

    @Test
    void test_ld_l_hlptr() {
        reg.setHL(0x8000);
        mmu.writeByte(0x8000, 0x42);
        mmu.writeByte(0x0000, 0x6E);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getL()).isEqualTo(0x42);
    }

    @Test
    void test_ld_a_hlptr() {
        reg.setHL(0x8000);
        mmu.writeByte(0x8000, 0x42);
        mmu.writeByte(0x0000, 0x7E);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0x42);
    }

    // LD (HL), r - write register into memory at HL
    @Test
    void test_ld_hlptr_b() {
        reg.setHL(0x8000);
        reg.setB(0x42);
        mmu.writeByte(0x0000, 0x70);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(mmu.readByte(0x8000)).isEqualTo(0x42);
    }

    @Test
    void test_ld_hlptr_c() {
        reg.setHL(0x8000);
        reg.setC(0x42);
        mmu.writeByte(0x0000, 0x71);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(mmu.readByte(0x8000)).isEqualTo(0x42);
    }

    @Test
    void test_ld_hlptr_d() {
        reg.setHL(0x8000);
        reg.setD(0x42);
        mmu.writeByte(0x0000, 0x72);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(mmu.readByte(0x8000)).isEqualTo(0x42);
    }

    @Test
    void test_ld_hlptr_e() {
        reg.setHL(0x8000);
        reg.setE(0x42);
        mmu.writeByte(0x0000, 0x73);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(mmu.readByte(0x8000)).isEqualTo(0x42);
    }

    @Test
    void test_ld_hlptr_h() {
        reg.setHL(0x3000);
        mmu.writeByte(0x0000, 0x74);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(mmu.readByte(0x3000)).isEqualTo(0x30);
    }

    @Test
    void test_ld_hlptr_l() {
        reg.setHL(0x3030);
        mmu.writeByte(0x0000, 0x75);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(mmu.readByte(0x3030)).isEqualTo(0x30);
    }

    @Test
    void test_ld_hlptr_a() {
        reg.setHL(0x8000);
        reg.setA(0x42);
        mmu.writeByte(0x0000, 0x77);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(mmu.readByte(0x8000)).isEqualTo(0x42);
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
    void test_ld_u16ptr_a(){
        reg.setA(0x42);
        mmu.writeByte(0x0000, 0xEA);
        mmu.writeByte(0x0001, 0x00);
        mmu.writeByte(0x0002, 0x30);
        assertThat(cpu.step()).isEqualTo(16);
        assertThat(mmu.readByte(0x3000)).isEqualTo(0x42);
    }

    @Test 
    void test_ld_hlptr_u8(){
        reg.setHL(0x3000);
        mmu.writeByte(0x0000, 0x36);
        mmu.writeByte(0x0001, 0x42);
        assertThat(cpu.step()).isEqualTo(12);
        assertThat(mmu.readByte(0x3000)).isEqualTo(0x42);
    }

    @Test
    void test_ld_bcptr_a(){
        reg.setBC(0x3000);
        reg.setA(0x42);
        mmu.writeByte(0x0000, 0x02);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(mmu.readByte(0x3000)).isEqualTo(0x42);
    }

    @Test
    void test_ld_deptr_a(){
        reg.setDE(0x3000);
        reg.setA(0x42);
        mmu.writeByte(0x0000, 0x12);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(mmu.readByte(0x3000)).isEqualTo(0x42);
    }
}
