package com.gbemu.core;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class OpcodeCBTest extends OpcodeTestBase {

    @Test
    void test_res_logic() {
        int[] opcodes = { 0x80, 0x88, 0x90, 0x98, 0xA0, 0xA8, 0xB0, 0xB8 };
        int[] expected = { 0xFE, 0xFD, 0xFB, 0xF7, 0xEF, 0xDF, 0xBF, 0x7F };

        for (int i = 0; i < opcodes.length; i++) {
            reg.setPC(0x0000);
            reg.setF(0xFF);
            reg.setB(0xFF);
            mmu.writeByte(0x0000, 0xCB);
            mmu.writeByte(0x0001, opcodes[i]);
            assertThat(cpu.step()).isEqualTo(8);
            assertThat(reg.getB()).isEqualTo(expected[i]);
            assertThat(reg.isFlagZ()).isTrue();
            assertThat(reg.isFlagN()).isTrue();
            assertThat(reg.isFlagH()).isTrue();
            assertThat(reg.isFlagC()).isTrue();
        }
    }

    @Test
    void test_res_all() {
        int[] opcodes = { 0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x87 };
        IntSupplier[] getters = { reg::getB, reg::getC, reg::getD, reg::getE, reg::getH, reg::getL, reg::getA };
        IntConsumer[] setters = { reg::setB, reg::setC, reg::setD, reg::setE, reg::setH, reg::setL, reg::setA };

        for (int i = 0; i < opcodes.length; i++) {
            reg.setPC(0x0000);
            setters[i].accept(0xFF);
            reg.setF(0xFF);
            mmu.writeByte(0x0000, 0xCB);
            mmu.writeByte(0x0001, opcodes[i]);
            assertThat(cpu.step()).isEqualTo(8);
            assertThat(getters[i].getAsInt()).isEqualTo(0xFE);
            assertThat(reg.isFlagZ()).isTrue();
            assertThat(reg.isFlagN()).isTrue();
            assertThat(reg.isFlagH()).isTrue();
            assertThat(reg.isFlagC()).isTrue();
        }
    }

    @Test
    void test_res_hlptr() {
        int[] opcodes = { 0x86, 0x8E, 0x96, 0x9E, 0xA6, 0xAE, 0xB6, 0xBE };
        int[] expected = { 0xFE, 0xFD, 0xFB, 0xF7, 0xEF, 0xDF, 0xBF, 0x7F };

        for (int i = 0; i < opcodes.length; i++) {
            reg.setPC(0x0000);
            reg.setF(0xFF);
            reg.setHL(0x0100);
            mmu.writeByte(0x0000, 0xCB);
            mmu.writeByte(0x0001, opcodes[i]);
            mmu.writeByte(0x0100, 0xFF);
            assertThat(cpu.step()).isEqualTo(16);
            assertThat(mmu.readByte(0x0100)).isEqualTo(expected[i]);
            assertThat(reg.isFlagZ()).isTrue();
            assertThat(reg.isFlagN()).isTrue();
            assertThat(reg.isFlagH()).isTrue();
            assertThat(reg.isFlagC()).isTrue();
        }
    }

    @Test
    void test_set_logic() {
        int[] opcodes = { 0xC0, 0xC8, 0xD0, 0xD8, 0xE0, 0xE8, 0xF0, 0xF8 };
        int[] expected = { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80 };

        for (int i = 0; i < opcodes.length; i++) {
            reg.setPC(0x0000);
            reg.setF(0xFF);
            reg.setB(0x00);
            mmu.writeByte(0x0000, 0xCB);
            mmu.writeByte(0x0001, opcodes[i]);
            assertThat(cpu.step()).isEqualTo(8);
            assertThat(reg.getB()).isEqualTo(expected[i]);
            assertThat(reg.isFlagZ()).isTrue();
            assertThat(reg.isFlagN()).isTrue();
            assertThat(reg.isFlagH()).isTrue();
            assertThat(reg.isFlagC()).isTrue();
        }
    }

    @Test
    void test_set_all() {
        int[] opcodes = { 0xC0, 0xC1, 0xC2, 0xC3, 0xC4, 0xC5, 0xC7 };
        IntSupplier[] getters = { reg::getB, reg::getC, reg::getD, reg::getE, reg::getH, reg::getL, reg::getA };
        IntConsumer[] setters = { reg::setB, reg::setC, reg::setD, reg::setE, reg::setH, reg::setL, reg::setA };

        for (int i = 0; i < opcodes.length; i++) {
            reg.setPC(0x0000);
            setters[i].accept(0x00);
            reg.setF(0xFF);
            mmu.writeByte(0x0000, 0xCB);
            mmu.writeByte(0x0001, opcodes[i]);
            assertThat(cpu.step()).isEqualTo(8);
            assertThat(getters[i].getAsInt()).isEqualTo(0x01);
            assertThat(reg.isFlagZ()).isTrue();
            assertThat(reg.isFlagN()).isTrue();
            assertThat(reg.isFlagH()).isTrue();
            assertThat(reg.isFlagC()).isTrue();
        }
    }

    @Test
    void test_set_hlptr() {
        int[] opcodes = { 0xC6, 0xCE, 0xD6, 0xDE, 0xE6, 0xEE, 0xF6, 0xFE };
        int[] expected = { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80 };

        for (int i = 0; i < opcodes.length; i++) {
            reg.setPC(0x0000);
            reg.setF(0xFF);
            reg.setHL(0x0100);
            mmu.writeByte(0x0000, 0xCB);
            mmu.writeByte(0x0001, opcodes[i]);
            mmu.writeByte(0x0100, 0x00);
            assertThat(cpu.step()).isEqualTo(16);
            assertThat(mmu.readByte(0x0100)).isEqualTo(expected[i]);
            assertThat(reg.isFlagZ()).isTrue();
            assertThat(reg.isFlagN()).isTrue();
            assertThat(reg.isFlagH()).isTrue();
            assertThat(reg.isFlagC()).isTrue();
        }
    }

    @Test
    void test_bit_logic(){
        int[] opcodes = { 0x40, 0x48, 0x50, 0x58, 0x60, 0x68, 0x70, 0x78 };

        for (int i = 0; i < opcodes.length; i++) {
            reg.setPC(0x0000);
            reg.setFlagC(true);
            reg.setB(0xFF);
            mmu.writeByte(0x0000, 0xCB);
            mmu.writeByte(0x0001, opcodes[i]);
            assertThat(cpu.step()).isEqualTo(8);
            assertThat(reg.isFlagZ()).isFalse();
            assertThat(reg.isFlagN()).isFalse();
            assertThat(reg.isFlagH()).isTrue();
            assertThat(reg.isFlagC()).isTrue();
        }
    }

    @Test
    void test_bit_logic_zero(){
        int[] opcodes = { 0x40, 0x48, 0x50, 0x58, 0x60, 0x68, 0x70, 0x78 };

        for (int i = 0; i < opcodes.length; i++) {
            reg.setPC(0x0000);
            reg.setFlagC(false);
            reg.setB(0x00);
            mmu.writeByte(0x0000, 0xCB);
            mmu.writeByte(0x0001, opcodes[i]);
            assertThat(cpu.step()).isEqualTo(8);
            assertThat(reg.isFlagZ()).isTrue();
            assertThat(reg.isFlagN()).isFalse();
            assertThat(reg.isFlagH()).isTrue();
            assertThat(reg.isFlagC()).isFalse();
        }
    }

    @Test
    void test_bit_all(){
        int[] opcodes = { 0x40, 0x41, 0x42, 0x43, 0x44, 0x45, 0x47 };
        IntConsumer[] setters = { reg::setB, reg::setC, reg::setD, reg::setE, reg::setH, reg::setL, reg::setA };

        for (int i = 0; i < opcodes.length; i++) {
            reg.setPC(0x0000);
            setters[i].accept(0x00);
            reg.setF(0xFF);
            mmu.writeByte(0x0000, 0xCB);
            mmu.writeByte(0x0001, opcodes[i]);
            assertThat(cpu.step()).isEqualTo(8);
            assertThat(reg.isFlagZ()).isTrue();
            assertThat(reg.isFlagN()).isFalse();
            assertThat(reg.isFlagH()).isTrue();
            assertThat(reg.isFlagC()).isTrue();
        }
    }

    @Test
    void test_bit_hlptr() {
        int[] opcodes = { 0x46, 0x4E, 0x56, 0x5E, 0x66, 0x6E, 0x76, 0x7E };

        for (int i = 0; i < opcodes.length; i++) {
            reg.setPC(0x0000);
            reg.setF(0xFF);
            reg.setHL(0x0100);
            mmu.writeByte(0x0000, 0xCB);
            mmu.writeByte(0x0001, opcodes[i]);
            mmu.writeByte(0x0100, 0x00);
            assertThat(cpu.step()).isEqualTo(12);
            assertThat(reg.isFlagZ()).isTrue();
            assertThat(reg.isFlagN()).isFalse();
            assertThat(reg.isFlagH()).isTrue();
            assertThat(reg.isFlagC()).isTrue();
        }
    }
}
