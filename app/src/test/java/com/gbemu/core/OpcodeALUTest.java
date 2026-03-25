package com.gbemu.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.assertj.core.api.Assertions.assertThat;
import java.beans.Transient;

public class OpcodeALUTest extends OpcodeTestBase {

    /* XOR */

    @Test
    void test_xor_b() {
        reg.setF(0xFF);
        reg.setA(0xFF);
        reg.setB(0x0F);
        mmu.writeByte(0x0000, 0xA8);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xF0);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_xor_b_zero() {
        reg.setA(0xAA);
        reg.setB(0xAA);
        mmu.writeByte(0x0000, 0xA8);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_xor_c() {
        reg.setA(0xFF);
        reg.setC(0x0F);
        mmu.writeByte(0x0000, 0xA9);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xF0);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_xor_c_zero() {
        reg.setA(0xAA);
        reg.setC(0xAA);
        mmu.writeByte(0x0000, 0xA9);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_xor_d() {
        reg.setA(0xFF);
        reg.setD(0x0F);
        mmu.writeByte(0x0000, 0xAA);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xF0);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_xor_d_zero() {
        reg.setA(0xAA);
        reg.setD(0xAA);
        mmu.writeByte(0x0000, 0xAA);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_xor_e() {
        reg.setA(0xFF);
        reg.setE(0x0F);
        mmu.writeByte(0x0000, 0xAB);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xF0);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_xor_e_zero() {
        reg.setA(0xAA);
        reg.setE(0xAA);
        mmu.writeByte(0x0000, 0xAB);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_xor_h() {
        reg.setA(0xFF);
        reg.setH(0x0F);
        mmu.writeByte(0x0000, 0xAC);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xF0);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_xor_h_zero() {
        reg.setA(0xAA);
        reg.setH(0xAA);
        mmu.writeByte(0x0000, 0xAC);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_xor_l() {
        reg.setA(0xFF);
        reg.setL(0x0F);
        mmu.writeByte(0x0000, 0xAD);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xF0);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_xor_l_zero() {
        reg.setA(0xAA);
        reg.setL(0xAA);
        mmu.writeByte(0x0000, 0xAD);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_xor_a() {
        reg.setA(0xFF);
        mmu.writeByte(0x0000, 0xAF);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_xor_hlptr() {
        reg.setA(0xFF);
        reg.setHL(0x0200);
        mmu.writeByte(0x0000, 0xAE);
        mmu.writeByte(0x0200, 0x0F);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0xF0);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_xor_hlptr_zero() {
        reg.setA(0xAA);
        reg.setHL(0x0200);
        mmu.writeByte(0x0000, 0xAE);
        mmu.writeByte(0x0200, 0xAA);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    /* OR */
    @Test
    void test_or_b() {
        reg.setA(0xF0);
        reg.setB(0x0F);
        mmu.writeByte(0x0000, 0xB0);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xFF);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_or_b_zero() {
        reg.setA(0x00);
        reg.setB(0x00);
        mmu.writeByte(0x0000, 0xB0);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_or_c() {
        reg.setA(0xF0);
        reg.setC(0x0F);
        mmu.writeByte(0x0000, 0xB1);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xFF);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_or_c_zero() {
        reg.setA(0x00);
        reg.setC(0x00);
        mmu.writeByte(0x0000, 0xB1);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_or_d() {
        reg.setA(0xF0);
        reg.setD(0x0F);
        mmu.writeByte(0x0000, 0xB2);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xFF);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_or_d_zero() {
        reg.setA(0x00);
        reg.setD(0x00);
        mmu.writeByte(0x0000, 0xB2);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_or_e() {
        reg.setA(0xF0);
        reg.setE(0x0F);
        mmu.writeByte(0x0000, 0xB3);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xFF);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_or_e_zero() {
        reg.setA(0x00);
        reg.setE(0x00);
        mmu.writeByte(0x0000, 0xB3);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_or_h() {
        reg.setA(0xF0);
        reg.setH(0x0F);
        mmu.writeByte(0x0000, 0xB4);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xFF);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_or_h_zero() {
        reg.setA(0x00);
        reg.setH(0x00);
        mmu.writeByte(0x0000, 0xB4);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_or_l() {
        reg.setA(0xF0);
        reg.setL(0x0F);
        mmu.writeByte(0x0000, 0xB5);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xFF);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_or_l_zero() {
        reg.setA(0x00);
        reg.setL(0x00);
        mmu.writeByte(0x0000, 0xB5);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_or_a() {
        reg.setA(0xF0);
        mmu.writeByte(0x0000, 0xB7);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xF0);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_or_a_zero() {
        reg.setA(0x00);
        mmu.writeByte(0x0000, 0xB7);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_or_hlptr() {
        reg.setA(0xF0);
        reg.setHL(0x0200);
        mmu.writeByte(0x0000, 0xB6);
        mmu.writeByte(0x0200, 0x0F);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0xFF);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_or_hlptr_zero() {
        reg.setA(0x00);
        reg.setHL(0x0200);
        mmu.writeByte(0x0000, 0xB6);
        mmu.writeByte(0x0200, 0x00);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_and_b() {
        reg.setA(0xF2);
        reg.setB(0x0F);
        mmu.writeByte(0x0000, 0xA0);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x02);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isTrue();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_and_b_zero() {
        reg.setA(0xF0);
        reg.setB(0x0F);
        mmu.writeByte(0x0000, 0xA0);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isTrue();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_and_c() {
        reg.setA(0xF2);
        reg.setC(0x0F);
        mmu.writeByte(0x0000, 0xA1);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x02);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_and_c_zero() {
        reg.setA(0xF0);
        reg.setC(0x0F);
        mmu.writeByte(0x0000, 0xA1);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_and_d() {
        reg.setA(0xF2);
        reg.setD(0x0F);
        mmu.writeByte(0x0000, 0xA2);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x02);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_and_d_zero() {
        reg.setA(0xF0);
        reg.setD(0x0F);
        mmu.writeByte(0x0000, 0xA2);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_and_e() {
        reg.setA(0xF2);
        reg.setE(0x0F);
        mmu.writeByte(0x0000, 0xA3);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x02);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_and_e_zero() {
        reg.setA(0xF0);
        reg.setE(0x0F);
        mmu.writeByte(0x0000, 0xA3);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_and_h() {
        reg.setA(0xF2);
        reg.setH(0x0F);
        mmu.writeByte(0x0000, 0xA4);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x02);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_and_h_zero() {
        reg.setA(0xF0);
        reg.setH(0x0F);
        mmu.writeByte(0x0000, 0xA4);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_and_l() {
        reg.setA(0xF2);
        reg.setL(0x0F);
        mmu.writeByte(0x0000, 0xA5);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x02);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_and_l_zero() {
        reg.setA(0xF0);
        reg.setL(0x0F);
        mmu.writeByte(0x0000, 0xA5);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_and_a() {
        reg.setA(0xF2);
        mmu.writeByte(0x0000, 0xA7);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xF2);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_and_a_zero() {
        reg.setA(0);
        mmu.writeByte(0x0000, 0xA7);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_and_hlptr() {
        reg.setA(0xF2);
        reg.setHL(0x0200);
        mmu.writeByte(0x0000, 0xA6);
        mmu.writeByte(0x0200, 0x0F);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0x02);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagH()).isTrue();
    }

    @Test
    void test_and_hlptr_zero() {
        reg.setA(0x00);
        reg.setHL(0x0200);
        mmu.writeByte(0x0000, 0xA6);
        mmu.writeByte(0x0200, 0x00);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
        assertThat(reg.isFlagH()).isTrue();
    }

    @Test
    void test_cp_b_more() {
        reg.setA(0xAA);
        reg.setB(0xBB);
        mmu.writeByte(0x0000, 0xB8);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isTrue();
        assertThat(reg.isFlagC()).isTrue();
    }

    @Test
    void test_cp_b_eq() {
        reg.setA(0xAA);
        reg.setB(0xAA);
        mmu.writeByte(0x0000, 0xB8);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.isFlagZ()).isTrue();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_cp_b_less() {
        reg.setA(0xAA);
        reg.setB(0x99);
        mmu.writeByte(0x0000, 0xB8);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_cp_c() {
        reg.setA(0xAA);
        reg.setC(0xAA);
        mmu.writeByte(0x0000, 0xB9);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_cp_d() {
        reg.setA(0xAA);
        reg.setD(0xAA);
        mmu.writeByte(0x0000, 0xBA);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_cp_e() {
        reg.setA(0xAA);
        reg.setE(0xAA);
        mmu.writeByte(0x0000, 0xBB);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_cp_h() {
        reg.setA(0xAA);
        reg.setH(0xAA);
        mmu.writeByte(0x0000, 0xBC);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_cp_l() {
        reg.setA(0xAA);
        reg.setL(0xAA);
        mmu.writeByte(0x0000, 0xBD);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_cp_a() {
        reg.setA(0xAA);
        mmu.writeByte(0x0000, 0xBF);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_xor_u8() {
        reg.setA(0xFF);
        reg.setF(0xFF); // see if all flags are reset
        mmu.writeByte(0x0000, 0xEE);
        mmu.writeByte(0x0001, 0x0F);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0xF0);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_xor_u8_zero() {
        reg.setA(0xFF);
        mmu.writeByte(0x0000, 0xEE);
        mmu.writeByte(0x0001, 0xFF);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0x00);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_or_u8() {
        reg.setA(0xF0);
        reg.setF(0xFF); // see if all flags are reset
        mmu.writeByte(0x0000, 0xF6);
        mmu.writeByte(0x0001, 0x0F);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0xFF);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_or_u8_zero() {
        reg.setA(0x00);
        mmu.writeByte(0x0000, 0xF6);
        mmu.writeByte(0x0001, 0x00);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0x00);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_and_u8() {
        reg.setA(0xFF);
        reg.setF(0xFF); // see if all flags are reset
        mmu.writeByte(0x0000, 0xE6);
        mmu.writeByte(0x0001, 0xF0);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0xF0);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isTrue();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_and_u8_zero() {
        reg.setA(0x00);
        mmu.writeByte(0x0000, 0xE6);
        mmu.writeByte(0x0001, 0x00);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0x00);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_cp_u8() {
        reg.setA(0xFE);
        reg.setF(0xFF); // see if all flags are reset
        mmu.writeByte(0x0000, 0xFE);
        mmu.writeByte(0x0001, 0x0F);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isTrue();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_cp_u8_zero() {
        reg.setA(0xFF);
        mmu.writeByte(0x0000, 0xFE);
        mmu.writeByte(0x0001, 0xFF);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.isFlagZ()).isTrue();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_add_b() {
        reg.setA(0x01);
        reg.setB(0x01);
        mmu.writeByte(0x0000, 0x80);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x02);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_add_b_of() {
        reg.setA(0xFA);
        reg.setB(0x0A);
        mmu.writeByte(0x0000, 0x80);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x04);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isTrue();
        assertThat(reg.isFlagC()).isTrue();
    }

    @Test
    void test_add_b_zero() {
        reg.setA(0x00);
        reg.setB(0x00);
        mmu.writeByte(0x0000, 0x80);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x00);
        assertThat(reg.isFlagZ()).isTrue();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_add_c() {
        reg.setA(0x01);
        reg.setC(0x01);
        mmu.writeByte(0x0000, 0x81);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x02);
    }

    @Test
    void test_add_d() {
        reg.setA(0x01);
        reg.setD(0x01);
        mmu.writeByte(0x0000, 0x82);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x02);
    }

    @Test
    void test_add_e() {
        reg.setA(0x01);
        reg.setE(0x01);
        mmu.writeByte(0x0000, 0x83);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x02);
    }

    @Test
    void test_add_h() {
        reg.setA(0x01);
        reg.setH(0x01);
        mmu.writeByte(0x0000, 0x84);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x02);
    }

    @Test
    void test_add_l() {
        reg.setA(0x01);
        reg.setL(0x01);
        mmu.writeByte(0x0000, 0x85);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x02);
    }

    @Test
    void test_add_a() {
        reg.setA(0x01);
        mmu.writeByte(0x0000, 0x87);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x02);
    }

    @Test
    void test_add_hlptr() {
        reg.setA(0x01);
        reg.setHL(0x0100);
        mmu.writeByte(0x0000, 0x86);
        mmu.writeByte(0x0100, 0x01);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0x02);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_add_hlptr_of() {
        reg.setA(0xFA);
        reg.setHL(0x0010);
        mmu.writeByte(0x0000, 0x86);
        mmu.writeByte(0x0010, 0x0A);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0x04);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isTrue();
        assertThat(reg.isFlagC()).isTrue();
    }

    @Test
    void test_add_hlptr_zero() {
        reg.setA(0x00);
        reg.setHL(0x0100);
        mmu.writeByte(0x0000, 0x86);
        mmu.writeByte(0x0100, 0x00);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0x00);
        assertThat(reg.isFlagZ()).isTrue();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_add_u8() {
        reg.setA(0x01);
        mmu.writeByte(0x0000, 0xC6);
        mmu.writeByte(0x0001, 0x01);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0x02);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_add_u8_of() {
        reg.setA(0xFA);
        mmu.writeByte(0x0000, 0xC6);
        mmu.writeByte(0x0001, 0x0A);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0x04);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isTrue();
        assertThat(reg.isFlagC()).isTrue();
    }

    @Test
    void test_add_u8_zero() {
        reg.setA(0x00);
        mmu.writeByte(0x0000, 0xC6);
        mmu.writeByte(0x0001, 0x00);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0x00);
        assertThat(reg.isFlagZ()).isTrue();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_sub_b() {
        reg.setA(0x0F);
        reg.setB(0x01);
        mmu.writeByte(0x0000, 0x90);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x0E);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_sub_b_of_1() {
        reg.setA(0x00);
        reg.setB(0x01);
        mmu.writeByte(0x0000, 0x90);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xFF);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isTrue();
        assertThat(reg.isFlagC()).isTrue();
    }

    @Test
    void test_sub_b_of_2() {
        reg.setA(0x0F);
        reg.setB(0x10);
        mmu.writeByte(0x0000, 0x90);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xFF);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isTrue();
    }

    @Test
    void test_sub_b_zero() {
        reg.setA(0x0F);
        reg.setB(0x0F);
        mmu.writeByte(0x0000, 0x90);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x00);
        assertThat(reg.isFlagZ()).isTrue();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_sub_c() {
        reg.setA(0x0F);
        reg.setC(0x02);
        mmu.writeByte(0x0000, 0x91);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x0D);
    }

    @Test
    void test_sub_d() {
        reg.setA(0x0F);
        reg.setD(0x02);
        mmu.writeByte(0x0000, 0x92);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x0D);
    }

    @Test
    void test_sub_e() {
        reg.setA(0x0F);
        reg.setE(0x02);
        mmu.writeByte(0x0000, 0x93);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x0D);
    }

    @Test
    void test_sub_h() {
        reg.setA(0x0F);
        reg.setH(0x02);
        mmu.writeByte(0x0000, 0x94);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x0D);
    }

    @Test
    void test_sub_l() {
        reg.setA(0x0F);
        reg.setL(0x02);
        mmu.writeByte(0x0000, 0x95);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x0D);
    }

    @Test
    void test_sub_a() {
        reg.setA(0x0F);
        mmu.writeByte(0x0000, 0x97);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x00);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_sub_hlptr() {
        reg.setA(0x0F);
        reg.setHL(0x0100);
        mmu.writeByte(0x0000, 0x96);
        mmu.writeByte(0x0100, 0x01);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0x0E);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_sub_hlptr_of_1() {
        reg.setA(0x00);
        reg.setHL(0x0100);
        mmu.writeByte(0x0000, 0x96);
        mmu.writeByte(0x0100, 0x01);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0xFF);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isTrue();
        assertThat(reg.isFlagC()).isTrue();
    }

    @Test
    void test_sub_hlptr_of_2() {
        reg.setA(0x0F);
        reg.setHL(0x0100);
        mmu.writeByte(0x0000, 0x96);
        mmu.writeByte(0x0100, 0x10);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0xFF);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isTrue();
    }

    @Test
    void test_sub_hlptr_zero() {
        reg.setA(0x0F);
        reg.setHL(0x0100);
        mmu.writeByte(0x0000, 0x96);
        mmu.writeByte(0x0100, 0x0F);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0x00);
        assertThat(reg.isFlagZ()).isTrue();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_sub_u8() {
        reg.setA(0x0F);
        mmu.writeByte(0x0000, 0xD6);
        mmu.writeByte(0x0001, 0x01);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0x0E);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_sub_u8_of_1() {
        reg.setA(0x00);
        mmu.writeByte(0x0000, 0xD6);
        mmu.writeByte(0x0001, 0x01);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0xFF);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isTrue();
        assertThat(reg.isFlagC()).isTrue();
    }

    @Test
    void test_sub_u8_of_2() {
        reg.setA(0x0F);
        mmu.writeByte(0x0000, 0xD6);
        mmu.writeByte(0x0001, 0x10);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0xFF);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isTrue();
    }

    @Test
    void test_sub_u8_zero() {
        reg.setA(0x0F);
        mmu.writeByte(0x0000, 0xD6);
        mmu.writeByte(0x0001, 0x0F);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0x00);
        assertThat(reg.isFlagZ()).isTrue();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_inc_b() {
        reg.setFlagC(true);
        reg.setB(0x01);
        mmu.writeByte(0x0000, 0x04);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getB()).isEqualTo(2);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isTrue();
    }

    @Test
    void test_inc_b_zero() {
        reg.setFlagC(false);
        reg.setB(0xFF);
        mmu.writeByte(0x0000, 0x04);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getB()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isTrue();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_inc_b_h_flag() {
        reg.setB(0x0F);
        mmu.writeByte(0x0000, 0x04);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getB()).isEqualTo(0x10);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isTrue();
    }

    @Test
    void test_inc_c() {
        reg.setC(0x01);
        mmu.writeByte(0x0000, 0x0C);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getC()).isEqualTo(2);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
    }

    @Test
    void test_inc_d() {
        reg.setD(0x01);
        mmu.writeByte(0x0000, 0x14);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getD()).isEqualTo(2);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
    }

    @Test
    void test_inc_e() {
        reg.setE(0x01);
        mmu.writeByte(0x0000, 0x1C);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getE()).isEqualTo(2);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
    }

    @Test
    void test_inc_h() {
        reg.setH(0x01);
        mmu.writeByte(0x0000, 0x24);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getH()).isEqualTo(2);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
    }

    @Test
    void test_inc_l() {
        reg.setL(0x01);
        mmu.writeByte(0x0000, 0x2C);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getL()).isEqualTo(2);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
    }

    @Test
    void test_inc_a() {
        reg.setA(0x01);
        mmu.writeByte(0x0000, 0x3C);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(2);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
    }

    @Test
    void test_inc_hlptr() {
        reg.setFlagC(true);
        reg.setHL(0x0100);
        mmu.writeByte(0x0000, 0x34);
        mmu.writeByte(0x0100, 0x01);
        assertThat(cpu.step()).isEqualTo(12);
        assertThat(mmu.readByte(0x0100)).isEqualTo(0x02);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isTrue();
    }

    @Test
    void test_inc_hlptr_zero() {
        reg.setFlagC(false);
        reg.setHL(0x0100);
        mmu.writeByte(0x0000, 0x34);
        mmu.writeByte(0x0100, 0xFF);
        assertThat(cpu.step()).isEqualTo(12);
        assertThat(mmu.readByte(0x0100)).isEqualTo(0x00);
        assertThat(reg.isFlagZ()).isTrue();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isTrue();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_inc_hlptr_h_flag() {
        reg.setHL(0x0100);
        mmu.writeByte(0x0000, 0x34);
        mmu.writeByte(0x0100, 0x0F);
        assertThat(cpu.step()).isEqualTo(12);
        assertThat(mmu.readByte(0x0100)).isEqualTo(0x10);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isTrue();
    }

    @Test
    void test_dec_b() {
        reg.setFlagC(false);
        reg.setB(0x02);
        mmu.writeByte(0x0000, 0x05);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getB()).isEqualTo(0x01);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_dec_b_zero() {
        reg.setFlagC(true);
        reg.setB(0x01);
        mmu.writeByte(0x0000, 0x05);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getB()).isEqualTo(0x00);
        assertThat(reg.isFlagZ()).isTrue();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isTrue();
    }

    @Test
    void test_dec_b_h_flag() {
        reg.setB(0x00);
        mmu.writeByte(0x0000, 0x05);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getB()).isEqualTo(0xFF);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isTrue();
    }

    @Test
    void test_dec_c() {
        reg.setC(0x02);
        mmu.writeByte(0x0000, 0x0D);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getC()).isEqualTo(0x01);
    }

    @Test
    void test_dec_d() {
        reg.setD(0x02);
        mmu.writeByte(0x0000, 0x15);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getD()).isEqualTo(0x01);
    }

    @Test
    void test_dec_e() {
        reg.setE(0x02);
        mmu.writeByte(0x0000, 0x1D);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getE()).isEqualTo(0x01);
    }

    @Test
    void test_dec_h() {
        reg.setH(0x02);
        mmu.writeByte(0x0000, 0x25);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getH()).isEqualTo(0x01);
    }

    @Test
    void test_dec_l() {
        reg.setL(0x02);
        mmu.writeByte(0x0000, 0x2D);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getL()).isEqualTo(0x01);
    }

    @Test
    void test_dec_a() {
        reg.setA(0x02);
        mmu.writeByte(0x0000, 0x3D);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x01);
    }

    @Test
    void test_dec_hlptr() {
        reg.setFlagC(false);
        reg.setHL(0x0100);
        mmu.writeByte(0x0000, 0x35);
        mmu.writeByte(0x0100, 0x02);
        assertThat(cpu.step()).isEqualTo(12);
        assertThat(mmu.readByte(0x0100)).isEqualTo(0x01);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_dec_hlptr_zero() {
        reg.setFlagC(true);
        reg.setHL(0x0100);
        mmu.writeByte(0x0000, 0x35);
        mmu.writeByte(0x0100, 0x01);
        assertThat(cpu.step()).isEqualTo(12);
        assertThat(mmu.readByte(0x0100)).isEqualTo(0x00);
        assertThat(reg.isFlagZ()).isTrue();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isTrue();
    }

    @Test
    void test_dec_hlptr_h_flag() {
        reg.setHL(0x0100);
        mmu.writeByte(0x0000, 0x35);
        mmu.writeByte(0x0100, 0x00);
        assertThat(cpu.step()).isEqualTo(12);
        assertThat(mmu.readByte(0x0100)).isEqualTo(0xFF);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isTrue();
    }

    @Test
    void test_inc_bc() {
        reg.setF(0xFF);
        reg.setBC(0x0000);
        mmu.writeByte(0x0000, 0x03);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getBC()).isEqualTo(0x0001);
        assertThat(reg.getB()).isEqualTo(0x00);
        assertThat(reg.getC()).isEqualTo(0x01);
        assertThat(reg.isFlagZ()).isTrue();
        assertThat(reg.isFlagN()).isTrue();
        assertThat(reg.isFlagH()).isTrue();
        assertThat(reg.isFlagC()).isTrue();
    }

    @Test
    void test_inc_bc_wrap() {
        reg.setF(0x00);
        reg.setBC(0xFFFF);
        mmu.writeByte(0x0000, 0x03);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getBC()).isEqualTo(0x0000);
        assertThat(reg.getB()).isEqualTo(0x00);
        assertThat(reg.getC()).isEqualTo(0x00);
        assertThat(reg.isFlagZ()).isFalse();
        assertThat(reg.isFlagN()).isFalse();
        assertThat(reg.isFlagH()).isFalse();
        assertThat(reg.isFlagC()).isFalse();
    }

    @Test
    void test_inc_de(){
        reg.setDE(0x0101);
        mmu.writeByte(0x0000, 0x13);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getDE()).isEqualTo(0x0102);
        assertThat(reg.getD()).isEqualTo(0x01);
        assertThat(reg.getE()).isEqualTo(0x02);
    }

    @Test
    void test_inc_hl(){
        reg.setHL(0x0101);
        mmu.writeByte(0x0000, 0x23);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getHL()).isEqualTo(0x0102);
        assertThat(reg.getH()).isEqualTo(0x01);
        assertThat(reg.getL()).isEqualTo(0x02);
    }

    @Test
    void test_inc_sp(){
        reg.setSP(0x0101);
        mmu.writeByte(0x0000, 0x33);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getSP()).isEqualTo(0x0102);
    }
}
