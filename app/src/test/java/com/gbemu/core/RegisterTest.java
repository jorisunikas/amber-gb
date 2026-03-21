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
        registers.flagZ = true;
        registers.flagH = true;
        registers.flagN = true;
        registers.flagC = true;
        assertThat(registers.getF()).as("F register with all flags set").isEqualTo(0xF0);
    }

    @Test
    void testSetF() {
        registers.setF(0xF0);
        assertThat(registers.flagZ).as("F register with all flags set").isEqualTo(true);
        assertThat(registers.flagH).as("F register with all flags set").isEqualTo(true);
        assertThat(registers.flagN).as("F register with all flags set").isEqualTo(true);
        assertThat(registers.flagC).as("F register with all flags set").isEqualTo(true);
    }

    @Test
    void testGetBC() {
        registers.b = 0x12;
        registers.c = 0x34;
        assertThat(registers.getBC()).as("bc set to 0x1234").isEqualTo(0x1234);
    }

    @Test
    void testSetDE() {
        registers.setDE(0x1234);
        assertThat(registers.d).as("d set to 0x12").isEqualTo(0x12);
        assertThat(registers.e).as("e set to 0x34").isEqualTo(0x34);
    }

    @Test
    void testGetDE() {
        registers.d = 0x12;
        registers.e = 0x34;
        assertThat(registers.getDE()).as("de set to 0x1234").isEqualTo(0x1234);
    }

    @Test
    void testSetHL() {
        registers.setHL(0x1234);
        assertThat(registers.h).as("h set to 0x12").isEqualTo(0x12);
        assertThat(registers.l).as("l set to 0x34").isEqualTo(0x34);
    }

    @Test
    void testGetHL() {
        registers.h = 0x12;
        registers.l = 0x34;
        assertThat(registers.getHL()).as("hl set to 0x1234").isEqualTo(0x1234);
    }

    @Test
    void testUnsignedMask() {
        registers.a = 0xFF;
        assertThat(registers.a).isEqualTo(255);
    }
}
