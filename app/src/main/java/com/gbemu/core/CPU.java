package com.gbemu.core;

public class CPU {
    private final Registers registers;
    private final MMU mmu;
    private int cycles;
    private final Runnable[] opcodes;

    public CPU(MMU mmu) {
        this.mmu = mmu;

        registers = new Registers();
        opcodes = new Runnable[256];

        cycles = 0;
        initOpcodes();
    }

    public int step() {
        int opcode = fetch();
        int cycles = 0;
        Runnable handler = opcodes[opcode];
        if (handler == null)
            System.out.println("Unrecognised opcode: 0x" + Integer.toHexString(opcode));

        handler.run();
        return cycles;
    }

    private int fetch() {
        int opcode = mmu.readByte(registers.pc);
        registers.incPC();
        return opcode;
    }

    private void initOpcodes() {
        opcodes[0x00] = () -> cycles = 4;
        opcodes[0x18] = () -> {
            jr_s8();
            cycles = 12;
        };
        opcodes[0xC3] = () -> {
            jp_u16();
            cycles = 16;
        };
    }

    // u(nsigned) vs s(igned)

    private void jp_u16() {
        int high = fetch();
        int low = fetch();
        registers.setSP(high << 8 | low);
    }

    private void jr_s8() {
        int offset = (byte) fetch();
        registers.setSP(registers.getSP() + offset);
    }
}
