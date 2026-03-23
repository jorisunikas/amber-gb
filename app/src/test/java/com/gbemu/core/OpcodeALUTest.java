package com.gbemu.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.assertj.core.api.Assertions.assertThat;

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
    void test_or_hlptr(){
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
    void test_and_b(){
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
    void test_and_b_zero(){
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
    void test_and_c(){
        reg.setA(0xF2);
        reg.setC(0x0F);
        mmu.writeByte(0x0000, 0xA1);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x02);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_and_c_zero(){
        reg.setA(0xF0);
        reg.setC(0x0F);
        mmu.writeByte(0x0000, 0xA1);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_and_d(){
        reg.setA(0xF2);
        reg.setD(0x0F);
        mmu.writeByte(0x0000, 0xA2);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x02);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_and_d_zero(){
        reg.setA(0xF0);
        reg.setD(0x0F);
        mmu.writeByte(0x0000, 0xA2);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_and_e(){
        reg.setA(0xF2);
        reg.setE(0x0F);
        mmu.writeByte(0x0000, 0xA3);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x02);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_and_e_zero(){
        reg.setA(0xF0);
        reg.setE(0x0F);
        mmu.writeByte(0x0000, 0xA3);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_and_h(){
        reg.setA(0xF2);
        reg.setH(0x0F);
        mmu.writeByte(0x0000, 0xA4);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x02);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_and_h_zero(){
        reg.setA(0xF0);
        reg.setH(0x0F);
        mmu.writeByte(0x0000, 0xA4);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_and_l(){
        reg.setA(0xF2);
        reg.setL(0x0F);
        mmu.writeByte(0x0000, 0xA5);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0x02);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_and_l_zero(){
        reg.setA(0xF0);
        reg.setL(0x0F);
        mmu.writeByte(0x0000, 0xA5);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_and_a(){
        reg.setA(0xF2);
        mmu.writeByte(0x0000, 0xA7);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0xF2);
        assertThat(reg.isFlagZ()).isFalse();
    }

    @Test
    void test_and_a_zero(){
        reg.setA(0);
        mmu.writeByte(0x0000, 0xA7);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.getA()).isEqualTo(0);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_and_hlptr(){
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
    void test_cp_b_more(){
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
    void test_cp_b_eq(){
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
    void test_cp_b_less(){
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
    void test_cp_c(){
        reg.setA(0xAA);
        reg.setC(0xAA);
        mmu.writeByte(0x0000, 0xB9);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_cp_d(){
        reg.setA(0xAA);
        reg.setD(0xAA);
        mmu.writeByte(0x0000, 0xBA);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_cp_e(){
        reg.setA(0xAA);
        reg.setE(0xAA);
        mmu.writeByte(0x0000, 0xBB);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_cp_h(){
        reg.setA(0xAA);
        reg.setH(0xAA);
        mmu.writeByte(0x0000, 0xBC);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_cp_l(){
        reg.setA(0xAA);
        reg.setL(0xAA);
        mmu.writeByte(0x0000, 0xBD);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.isFlagZ()).isTrue();
    }

    @Test
    void test_cp_a(){
        reg.setA(0xAA);
        mmu.writeByte(0x0000, 0xBF);
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(reg.isFlagZ()).isTrue();
    }
}
