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

    @Test
    void test_rlca() {
        int[] input = { 0x00, 0x7F, 0x85 };
        int[] inputF = { 0x00, 0xFF, 0x00 };
        int[] expected = { 0x00, 0xFE, 0x0B };
        boolean[] expectedCarryFlag = { false, false, true };

        for (int i = 0; i < input.length; i++) {
            reg.setPC(0x0000);
            reg.setA(input[i]);
            reg.setF(inputF[i]);
            mmu.writeByte(0x0000, 0x07);
            assertThat(cpu.step()).isEqualTo(4);
            assertThat(reg.getA()).isEqualTo(expected[i]);
            assertThat(reg.isFlagZ()).isFalse();
            assertThat(reg.isFlagN()).isFalse();
            assertThat(reg.isFlagH()).isFalse();
            assertThat(reg.isFlagC()).isEqualTo(expectedCarryFlag[i]);
        }
    }

    @Test
    void test_rla(){
        int[] input = { 0x00, 0x7F, 0x7F };
        int[] expected = { 0x00, 0xFE, 0xFF };
        int[] inputF = { 0x00, 0xFF, 0x00 };
        boolean[] inputCarryFlag = { false, false, true };
        boolean[] expectedCarryFlag = { false, false, false };

        for (int i = 0; i < input.length; i++) {
            reg.setPC(0x0000);
            reg.setF(inputF[i]);
            reg.setFlagC(inputCarryFlag[i]);
            reg.setA(input[i]);
            mmu.writeByte(0x0000, 0x17);
            assertThat(cpu.step()).isEqualTo(4);
            assertThat(reg.getA()).isEqualTo(expected[i]);
            assertThat(reg.isFlagZ()).isFalse();
            assertThat(reg.isFlagN()).isFalse();
            assertThat(reg.isFlagH()).isFalse();
            assertThat(reg.isFlagC()).isEqualTo(expectedCarryFlag[i]);
        }
    }

    @Test
    void test_rrca(){
        int[] input = { 0x00, 0xFE, 0xFF };
        int[] expected = { 0x00, 0x7F, 0xFF };
        int[] inputF = {0x00, 0xFF, 0x00};
        boolean[] expectedCarryFlag = { false, false, true };

        for (int i = 0; i < input.length; i++) {
            reg.setPC(0x0000);
            reg.setA(input[i]);
            reg.setF(inputF[i]);
            mmu.writeByte(0x0000, 0x0F);
            assertThat(cpu.step()).isEqualTo(4);
            assertThat(reg.getA()).isEqualTo(expected[i]);
            assertThat(reg.isFlagZ()).isFalse();
            assertThat(reg.isFlagN()).isFalse();
            assertThat(reg.isFlagH()).isFalse();
            assertThat(reg.isFlagC()).isEqualTo(expectedCarryFlag[i]);
        }
    }

    @Test
    void test_rra() {
        int[] input = { 0x00, 0xFE, 0xFF };
        int[] expected = { 0x00, 0x7F, 0x7F };
        int[] inputF = {0x00, 0xFF, 0x00};
        boolean[] inputCarryFlag = { false, false, false };
        boolean[] expectedCarryFlag = { false, false, true };

        for (int i = 0; i < input.length; i++) {
            reg.setPC(0x0000);
            reg.setF(inputF[i]);
            reg.setFlagC(inputCarryFlag[i]);
            reg.setA(input[i]);
            mmu.writeByte(0x0000, 0x1F);
            assertThat(cpu.step()).isEqualTo(4);
            assertThat(reg.getA()).isEqualTo(expected[i]);
            assertThat(reg.isFlagZ()).isFalse();
            assertThat(reg.isFlagN()).isFalse();
            assertThat(reg.isFlagH()).isFalse();
            assertThat(reg.isFlagC()).isEqualTo(expectedCarryFlag[i]);
        }
    }

    @Test

    void test_stop(){
        mmu.writeByte(0x0000, 0x10);
        mmu.writeByte(0x0001, 0x00);
        mmu.writeByte(0xFFFC, 0x50);
        mmu.writeByte(0xFFFD, 0x01);
        assertThat(cpu.step()).isEqualTo(4);
        int pc = reg.getPC();
        assertThat(cpu.step()).isEqualTo(4);
        assertThat(pc).isEqualTo(reg.getPC());
    }
}
