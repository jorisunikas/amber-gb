package com.gbemu.core.opcodes;

import com.gbemu.core.cpu.CPU;
import com.gbemu.core.cpu.Registers;
import com.gbemu.core.memory.MMU;

import org.junit.jupiter.api.BeforeEach;

public abstract class OpcodeTestBase {
    protected CPU cpu;
    protected MMU mmu;
    protected Registers reg;

    @BeforeEach
    void setup() {
        mmu = new MMU();
        reg = new Registers();
        cpu = new CPU(mmu, reg);
        reg.setPC(0x0000);
    }
}
