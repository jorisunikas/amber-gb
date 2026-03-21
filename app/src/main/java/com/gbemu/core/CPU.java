package com.gbemu.core;

public class CPU {
    private final Registers registers;
    private final MMU mmu;
    private int cycles;
    private final Runnable[] opcodes;
    private boolean halted;

    public CPU(MMU mmu) {
        this(mmu, new Registers());
    }

    public CPU(MMU mmu, Registers reg) {
        this.mmu = mmu;

        registers = reg;
        opcodes = new Runnable[256];

        cycles = 0;
        halted = false;
        initOpcodes();
    }

    public int step() {
        if (halted)
            return 4;

        int opcode = fetch();
        Runnable handler = opcodes[opcode];
        if (handler == null)
            throw new IllegalStateException(
                    "Unrecognised opcode: 0x" + Integer.toHexString(opcode));

        handler.run();
        return cycles;
    }

    private int fetch() {
        int opcode = mmu.readByte(registers.getPC());
        registers.incPC();
        return opcode;
    }

    private void initOpcodes() {
        opcodes[0x00] = () -> cycles = 4;
        opcodes[0x18] = this::jr_s8;
        opcodes[0x20] = this::jr_nz_s8;
        opcodes[0x28] = this::jr_z_s8;
        opcodes[0x30] = this::jr_nc_s8;
        opcodes[0x38] = this::jr_c_s8;
        opcodes[0x76] = this::halt;
        opcodes[0xC0] = this::ret_nz;
        opcodes[0xC2] = this::jp_nz_u16;
        opcodes[0xC8] = this::ret_z;
        opcodes[0xC9] = this::ret;
        opcodes[0xCA] = this::jp_z_u16;
        opcodes[0xCD] = this::call_u16;
        opcodes[0xD0] = this::ret_nc;
        opcodes[0xD2] = this::jp_nc_u16;
        opcodes[0xD8] = this::ret_c;
        opcodes[0xDA] = this::jp_c_u16;
        opcodes[0xC3] = this::jp_u16;
    }

    // u(nsigned) vs s(igned)

    //TODO implement fully
    private void halt() {
        cycles = 4;
        halted = true;
    }

    private void call_u16() {
        int low = fetch();
        int high = fetch();
        int address = high << 8 | low;

        registers.decSP();
        mmu.writeByte(registers.getSP(), (registers.getPC() >> 8) & 0xFF);
        registers.decSP();
        mmu.writeByte(registers.getSP(), (registers.getPC() & 0xFF));

        registers.setPC(address);
        cycles = 24;

    }

    private void ret() {
        int low = mmu.readByte(registers.getSP());
        registers.incSP();
        int high = mmu.readByte(registers.getSP());
        registers.incSP();

        registers.setPC(high << 8 | low);
        cycles = 16;
    }

    private void ret_flag_helper(boolean flag) {
        if (flag) {
            ret();
            return;
        }
        cycles = 8;
    }

    private void ret_nz() {
        ret_flag_helper(!registers.isFlagZ());
    }

    private void ret_z() {
        ret_flag_helper(registers.isFlagZ());
    }

    private void ret_nc() {
        ret_flag_helper(!registers.isFlagC());
    }

    private void ret_c() {
        ret_flag_helper(registers.isFlagC());
    }

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

    private void jump_flag_s8_helper(boolean flag) {
        if (flag) {
            jr_s8();
            return;
        }
        fetch();
        cycles = 8;

    }

    private void jump_flag_u16_helper(boolean flag) {
        if (flag) {
            jp_u16();
            return;
        }
        fetch();
        fetch();
        cycles = 12;
    }

    private void jr_nz_s8() {
        jump_flag_s8_helper(!registers.isFlagZ());
    }

    private void jr_z_s8() {
        jump_flag_s8_helper(registers.isFlagZ());
    }

    private void jr_nc_s8() {
        jump_flag_s8_helper(!registers.isFlagC());
    }

    private void jr_c_s8() {
        jump_flag_s8_helper(registers.isFlagC());
    }

    private void jp_nz_u16() {
        jump_flag_u16_helper(!registers.isFlagZ());
    }

    private void jp_z_u16() {
        jump_flag_u16_helper(registers.isFlagZ());
    }

    private void jp_nc_u16() {
        jump_flag_u16_helper(!registers.isFlagC());
    }

    private void jp_c_u16() {
        jump_flag_u16_helper(registers.isFlagC());
    }
}
