package com.gbemu.core;

public class CPU {
    private final Registers registers;
    private final MMU mmu;
    private int cycles;
    private final Runnable[] opcodes;

    public CPU(MMU mmu) {
        this(mmu, new Registers());
    }

    public CPU(MMU mmu, Registers reg) {
        this.mmu = mmu;

        registers = reg;
        opcodes = new Runnable[256];

        cycles = 0;
        initOpcodes();
    }

    public int step() {
        int opcode = fetch();
        Runnable handler = opcodes[opcode];
        if (handler == null)
            throw new IllegalStateException("Unrecognised opcode: 0x" + Integer.toHexString(opcode));

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
        opcodes[0x18] = this::jr_s8;
        opcodes[0x20] = this::jr_nz_s8;
        opcodes[0x28] = this::jr_z_s8;
        opcodes[0xC3] = this::jp_u16;
    }

    // u(nsigned) vs s(igned)

    private void jp_u16() {
        int low = fetch();
        int high = fetch();
        registers.setPC(high << 8 | low);
        cycles = 16;
    }

    private void jr_s8() {
        int offset = (byte) fetch();
        registers.setPC(registers.getPC() + offset);
        cycles = 12;
    }

    private void jr_nz_s8() {
        if (!registers.flagZ) {
            jr_s8();
            return;
        }
        fetch();
        cycles = 8;
    }

    private void jr_z_s8() {
        if (registers.flagZ) {
            jr_s8();
            return;
        }
        fetch();
        cycles = 8;
    }
}
