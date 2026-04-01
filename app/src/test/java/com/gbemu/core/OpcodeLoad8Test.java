package com.gbemu.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.IntConsumer;

import org.junit.jupiter.api.Test;

public class OpcodeLoad8Test extends OpcodeTestBase {

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
    void test_ld_hlptr_u8() {
        reg.setHL(0x3000);
        mmu.writeByte(0x0000, 0x36);
        mmu.writeByte(0x0001, 0x42);
        assertThat(cpu.step()).isEqualTo(12);
        assertThat(mmu.readByte(0x3000)).isEqualTo(0x42);
    }

    @Test
    void test_ld_bcptr_a() {
        reg.setBC(0x3000);
        reg.setA(0x42);
        mmu.writeByte(0x0000, 0x02);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(mmu.readByte(0x3000)).isEqualTo(0x42);
    }

    @Test
    void test_ld_deptr_a() {
        reg.setDE(0x3000);
        reg.setA(0x42);
        mmu.writeByte(0x0000, 0x12);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(mmu.readByte(0x3000)).isEqualTo(0x42);
    }

    @Test
    void test_ld_hlptr_a_inc() {
        reg.setHL(0x8000);
        reg.setA(0x42);
        mmu.writeByte(0x0000, 0x22);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(mmu.readByte(0x8000)).isEqualTo(0x42);
        assertThat(reg.getHL()).isEqualTo(0x8001);
    }

    @Test
    void test_ld_hlptr_a_dec() {
        reg.setHL(0x8000);
        reg.setA(0x42);
        mmu.writeByte(0x0000, 0x32);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(mmu.readByte(0x8000)).isEqualTo(0x42);
        assertThat(reg.getHL()).isEqualTo(0x7FFF);
    }

    @Test
    void test_ld_a_hlptr_inc() {
        reg.setHL(0x8000);
        mmu.writeByte(0x8000, 0x42);
        mmu.writeByte(0x0000, 0x2A);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0x42);
        assertThat(reg.getHL()).isEqualTo(0x8001);
    }

    @Test
    void test_ld_a_hlptr_dec() {
        reg.setHL(0x8000);
        mmu.writeByte(0x8000, 0x42);
        mmu.writeByte(0x0000, 0x3A);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0x42);
        assertThat(reg.getHL()).isEqualTo(0x7FFF);
    }

    @Test
    void test_ldh_u8ptr_a() {
        reg.setA(0x42);
        mmu.writeByte(0x0000, 0xE0);
        mmu.writeByte(0x0001, 0x50);
        assertThat(cpu.step()).isEqualTo(12);
        assertThat(mmu.readByte(0xFF50)).isEqualTo(0x42);
    }

    @Test
    void test_ldh_a_u8ptr() {
        mmu.writeByte(0xFF50, 0x42);
        mmu.writeByte(0x0000, 0xF0);
        mmu.writeByte(0x0001, 0x50);
        assertThat(cpu.step()).isEqualTo(12);
        assertThat(reg.getA()).isEqualTo(0x42);
    }

    @Test
    void load_a_rrptr() {
        int[] opcodes = { 0x0A, 0x1A };
        IntConsumer[] setters = { reg::setBC, reg::setDE };
        for (int i = 0; i < opcodes.length; i++) {
            reg.setPC(0x0000);
            setters[i].accept(0x0100);
            mmu.writeByte(0x0100, 0x12);
            mmu.writeByte(0x0000, opcodes[i]);
            assertThat(cpu.step()).isEqualTo(8);
            assertThat(reg.getA()).isEqualTo(0x12);
        }
    }
}
