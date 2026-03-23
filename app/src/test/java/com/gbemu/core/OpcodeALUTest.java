package com.gbemu.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OpcodeALUTest extends OpcodeTestBase {

    /* XOR */

    @Test
    void test_xor_b(){
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
    void test_xor_b_zero(){
        reg.setA(0xAA);
        reg.setB(0xAA);
        mmu.writeByte(0x0000, 0xA8);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_xor_c(){
        reg.setA(0xFF);
        reg.setC(0x0F);
        mmu.writeByte(0x0000, 0xA9);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xF0);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_xor_c_zero(){
        reg.setA(0xAA);
        reg.setC(0xAA);
        mmu.writeByte(0x0000, 0xA9);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_xor_d(){
        reg.setA(0xFF);
        reg.setD(0x0F);
        mmu.writeByte(0x0000, 0xAA);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xF0);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_xor_d_zero(){
        reg.setA(0xAA);
        reg.setD(0xAA);
        mmu.writeByte(0x0000, 0xAA);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_xor_e(){
        reg.setA(0xFF);
        reg.setE(0x0F);
        mmu.writeByte(0x0000, 0xAB);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xF0);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_xor_e_zero(){
        reg.setA(0xAA);
        reg.setE(0xAA);
        mmu.writeByte(0x0000, 0xAB);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_xor_h(){
        reg.setA(0xFF);
        reg.setH(0x0F);
        mmu.writeByte(0x0000, 0xAC);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xF0);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_xor_h_zero(){
        reg.setA(0xAA);
        reg.setH(0xAA);
        mmu.writeByte(0x0000, 0xAC);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_xor_l(){
        reg.setA(0xFF);
        reg.setL(0x0F);
        mmu.writeByte(0x0000, 0xAD);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xF0);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_xor_l_zero(){
        reg.setA(0xAA);
        reg.setL(0xAA);
        mmu.writeByte(0x0000, 0xAD);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_xor_a(){
        reg.setA(0xFF);
        mmu.writeByte(0x0000, 0xAF);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_xor_hlptr(){
        reg.setA(0xFF);
        reg.setHL(0x0200);
        mmu.writeByte(0x0000, 0xAE);
        mmu.writeByte(0x0200, 0x0F);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0xF0);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_xor_hlptr_zero(){
        reg.setA(0xAA);
        reg.setHL(0x0200);
        mmu.writeByte(0x0000, 0xAE);
        mmu.writeByte(0x0200, 0xAA);
        assertThat(cpu.step()).isEqualTo(8);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    /* OR */
}
