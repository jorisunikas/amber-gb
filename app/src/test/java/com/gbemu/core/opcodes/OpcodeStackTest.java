package com.gbemu.core.opcodes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OpcodeStackTest extends OpcodeTestBase {

    @Test
    void test_push_bc(){
        reg.setBC(0x1234);
        mmu.writeByte(0x0000, 0xC5);
        assertThat(cpu.step()).isEqualTo(16);
        assertThat(mmu.readByte(0xFFFD)).isEqualTo(0x12);
        assertThat(mmu.readByte(0xFFFC)).isEqualTo(0x34);
        assertThat(reg.getSP()).isEqualTo(0xFFFC);
    }

    @Test
    void test_push_de(){
        reg.setDE(0x1234);
        mmu.writeByte(0x0000, 0xD5);
        assertThat(cpu.step()).isEqualTo(16);
        assertThat(mmu.readByte(0xFFFD)).isEqualTo(0x12);
        assertThat(mmu.readByte(0xFFFC)).isEqualTo(0x34);
        assertThat(reg.getSP()).isEqualTo(0xFFFC);
    }

    @Test
    void test_push_hl(){
        reg.setHL(0x1234);
        mmu.writeByte(0x0000, 0xE5);
        assertThat(cpu.step()).isEqualTo(16);
        assertThat(mmu.readByte(0xFFFD)).isEqualTo(0x12);
        assertThat(mmu.readByte(0xFFFC)).isEqualTo(0x34);
        assertThat(reg.getSP()).isEqualTo(0xFFFC);
    }

    @Test
    void test_push_af(){
        // regF has lower 4 bits set to 0
        reg.setAF(0x1230);
        mmu.writeByte(0x0000, 0xF5);
        assertThat(cpu.step()).isEqualTo(16);
        assertThat(mmu.readByte(0xFFFD)).isEqualTo(0x12);
        assertThat(mmu.readByte(0xFFFC)).isEqualTo(0x30);
        assertThat(reg.getSP()).isEqualTo(0xFFFC);
    }

    @Test
    void test_pop_bc(){
        reg.setSP(0xFFFC);
        mmu.writeByte(0x0000, 0xC1);
        mmu.writeByte(0xFFFC, 0x34);
        mmu.writeByte(0xFFFD, 0x12);
        assertThat(cpu.step()).isEqualTo(12);
        assertThat(reg.getBC()).isEqualTo(0x1234);
        assertThat(reg.getSP()).isEqualTo(0xFFFE);
    }

    @Test
    void test_pop_de(){
        reg.setSP(0xFFFC);
        mmu.writeByte(0x0000, 0xD1);
        mmu.writeByte(0xFFFC, 0x34);
        mmu.writeByte(0xFFFD, 0x12);
        assertThat(cpu.step()).isEqualTo(12);
        assertThat(reg.getDE()).isEqualTo(0x1234);
        assertThat(reg.getSP()).isEqualTo(0xFFFE);
    }

    @Test
    void test_pop_hl(){
        reg.setSP(0xFFFC);
        mmu.writeByte(0x0000, 0xE1);
        mmu.writeByte(0xFFFC, 0x34);
        mmu.writeByte(0xFFFD, 0x12);
        assertThat(cpu.step()).isEqualTo(12);
        assertThat(reg.getHL()).isEqualTo(0x1234);
        assertThat(reg.getSP()).isEqualTo(0xFFFE);
    }

    @Test
    void test_pop_af(){
        reg.setSP(0xFFFC);
        mmu.writeByte(0x0000, 0xF1);
        mmu.writeByte(0xFFFC, 0x34);
        mmu.writeByte(0xFFFD, 0x12);
        assertThat(cpu.step()).isEqualTo(12);
        assertThat(reg.getAF()).isEqualTo(0x1230);
        assertThat(reg.getSP()).isEqualTo(0xFFFE);
    }
}
