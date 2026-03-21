package com.gbemu.core;

public class CPU {
    private final Registers registers;
    private final MMU mmu;

    public CPU(MMU mmu) {
        registers = new Registers();
        this.mmu = mmu;
    }

    public int step() {
        int opcode = fetch();
        int cycles = 0;
        switch (opcode) {
            case 0x00 -> cycles = 4; // NOP
            default -> System.out.println("Unimplemented opcode: 0x" +
                    Integer.toHexString(opcode));

        }
        return cycles;
    }

    private int fetch() {
        int opcode = mmu.readByte(registers.pc);
        registers.incPC();
        return opcode;
    }
}
