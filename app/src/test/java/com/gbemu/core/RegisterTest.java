package com.gbemu.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RegisterTest {
    private Registers registers;

    @BeforeEach
    void setup() {
        registers = new Registers();
    }

    @Test
    void testGetF() {
        registers.setFlagZ(true);
        registers.setFlagH(true);
        registers.setFlagN(true);
        registers.setFlagC(true);
        assertThat(registers.getF()).as("F register with all flags set").isEqualTo(0xF0);
    }

    @Test
    void testSetF() {
        registers.setF(0xF0);
        assertThat(registers.isFlagZ()).as("Z flag set").isEqualTo(true);
        assertThat(registers.isFlagH()).as("H flag set").isEqualTo(true);
        assertThat(registers.isFlagN()).as("N flag set").isEqualTo(true);
        assertThat(registers.isFlagC()).as("C flag set").isEqualTo(true);
    }

    @Test
    void testGetBC() {
        registers.setB(0x12);
        registers.setC(0x34);
        assertThat(registers.getBC()).as("BC set to 0x1234").isEqualTo(0x1234);
    }

    @Test
    void testSetDE() {
        registers.setDE(0x1234);
        assertThat(registers.getD()).as("D set to 0x12").isEqualTo(0x12);
        assertThat(registers.getE()).as("E set to 0x34").isEqualTo(0x34);
    }

    @Test
    void testGetDE() {
        registers.setD(0x12);
        registers.setE(0x34);
        assertThat(registers.getDE()).as("DE set to 0x1234").isEqualTo(0x1234);
    }

    @Test
    void testSetHL() {
        registers.setHL(0x1234);
        assertThat(registers.getH()).as("H set to 0x12").isEqualTo(0x12);
        assertThat(registers.getL()).as("L set to 0x34").isEqualTo(0x34);
    }

    @Test
    void testGetHL() {
        registers.setH(0x12);
        registers.setL(0x34);
        assertThat(registers.getHL()).as("HL set to 0x1234").isEqualTo(0x1234);
    }

    @Test
    void testGetPC() {
        registers.setPC(0x1234);
        assertThat(registers.getPC()).as("PC set to 0x1234").isEqualTo(0x1234);
    }

    @Test
    void testGetSP() {
        registers.setSP(0x1234);
        assertThat(registers.getSP()).as("SP set to 0x1234").isEqualTo(0x1234);
    }

    @Test
    void testIncPC() {
        registers.setPC(0x0000);
        registers.incPC();
        assertThat(registers.getPC()).as("PC incremented by 1").isEqualTo(0x0001);
    }

    @Test
    void testDecPC() {
        registers.setPC(0x0001);
        registers.decPC();
        assertThat(registers.getPC()).as("PC decremented by 1").isEqualTo(0x0000);
    }

    @Test
    void testIncSP() {
        registers.setSP(0x0000);
        registers.incSP();
        assertThat(registers.getSP()).as("SP incremented by 1").isEqualTo(0x0001);
    }

    @Test
    void testDecSP() {
        registers.setSP(0x0001);
        registers.decSP();
        assertThat(registers.getSP()).as("SP decremented by 1").isEqualTo(0x0000);
    }

    @Test
    void testUnsignedMask() {
        registers.setA(0xFF);
        assertThat(registers.getA()).isEqualTo(255);
    }
}
