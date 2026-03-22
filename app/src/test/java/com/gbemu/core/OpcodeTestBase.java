package com.gbemu.core;

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
    }
}
