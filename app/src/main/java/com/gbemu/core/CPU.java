package com.gbemu.core;

public class CPU {
    private final Registers registers;
    private final MMU mmu;

    public CPU(MMU mmu) {
        registers = new Registers();
        this.mmu = mmu;
    }

    public void step() {
        int opcode = fetch();
        excecute(opcode);
    }

    private int fetch() {
        int opcode = mmu.readByte(registers.pc);
        registers.incPC();
        return opcode;
    }

    private void excecute(int opcode) {
        switch (opcode) {
        }
    }
}
