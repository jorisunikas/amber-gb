package com.gbemu.core;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class CPU {
    private final Registers reg;
    private final MMU mmu;
    private int cycles;
    private final Runnable[] opcodes;
    private boolean halted;
    private boolean ime;

    public CPU(MMU mmu) {
        this(mmu, new Registers());
    }

    public CPU(MMU mmu, Registers reg) {
        this.mmu = mmu;
        this.reg = reg;
        opcodes = new Runnable[256];

        cycles = 0;
        halted = false;
        ime = false;
        initOpcodes();
    }

    public int step() {
        if (halted)
            return 4;

        int opcode = fetch();
        Runnable handler = opcodes[opcode];
        if (handler == null)
            throw new IllegalStateException(
                    "Unrecognised opcode: 0x" + Integer.toHexString(opcode).toUpperCase());

        handler.run();
        return cycles;
    }

    private int fetch() {
        int opcode = mmu.readByte(reg.getPC());
        reg.incPC();
        return opcode;
    }

    private void initOpcodes() {
        opcodes[0x00] = () -> cycles = 4;
        opcodes[0x01] = this::ld_bc_u16;
        opcodes[0x02] = this::ld_bcptr_a;
        opcodes[0x03] = this::inc_bc;
        opcodes[0x04] = this::inc_b;
        opcodes[0x05] = this::dec_b;
        opcodes[0x06] = this::ld_b_u8;
        opcodes[0x07] = this::rlca;
        opcodes[0x08] = this::ld_u16ptr_sp;
        opcodes[0x09] = this::add_bc;
        opcodes[0x0A] = this::ld_a_bcptr;
        opcodes[0x0B] = this::dec_bc;
        opcodes[0x0C] = this::inc_c;
        opcodes[0x0D] = this::dec_c;
        opcodes[0x0E] = this::ld_c_u8;
        opcodes[0x0F] = this::rrca;

        opcodes[0x10] = this::stop;
        opcodes[0x11] = this::ld_de_u16;
        opcodes[0x12] = this::ld_deptr_a;
        opcodes[0x13] = this::inc_de;
        opcodes[0x14] = this::inc_d;
        opcodes[0x15] = this::dec_d;
        opcodes[0x16] = this::ld_d_u8;
        opcodes[0x17] = this::rla;
        opcodes[0x18] = this::jr_s8;
        opcodes[0x19] = this::add_de;
        opcodes[0x1A] = this::ld_a_deptr;
        opcodes[0x1B] = this::dec_de;
        opcodes[0x1C] = this::inc_e;
        opcodes[0x1D] = this::dec_e;
        opcodes[0x1E] = this::ld_e_u8;
        opcodes[0x1F] = this::rra;

        opcodes[0x20] = this::jr_nz_s8;
        opcodes[0x21] = this::ld_hl_u16;
        opcodes[0x22] = this::ld_hlptr_a_inc;
        opcodes[0x23] = this::inc_hl;
        opcodes[0x24] = this::inc_h;
        opcodes[0x25] = this::dec_h;
        opcodes[0x26] = this::ld_h_u8;
        opcodes[0x27] = this::daa;
        opcodes[0x28] = this::jr_z_s8;
        opcodes[0x29] = this::add_hl;
        opcodes[0x2A] = this::ld_a_hlptr_inc;
        opcodes[0x2B] = this::dec_hl;
        opcodes[0x2C] = this::inc_l;
        opcodes[0x2D] = this::dec_l;
        opcodes[0x2E] = this::ld_l_u8;
        opcodes[0x2F] = this::cpl;

        opcodes[0x30] = this::jr_nc_s8;
        opcodes[0x31] = this::ld_sp_u16;
        opcodes[0x32] = this::ld_hlptr_a_dec;
        opcodes[0x33] = this::inc_sp;
        opcodes[0x34] = this::inc_hlptr;
        opcodes[0x35] = this::dec_hlptr;
        opcodes[0x36] = this::ld_hlptr_u8;
        opcodes[0x37] = this::scf;
        opcodes[0x38] = this::jr_c_s8;
        opcodes[0x39] = this::add_sp;
        opcodes[0x3A] = this::ld_a_hlptr_dec;
        opcodes[0x3B] = this::dec_sp;
        opcodes[0x3C] = this::inc_a;
        opcodes[0x3D] = this::dec_a;
        opcodes[0x3E] = this::ld_a_u8;
        opcodes[0x3F] = this::ccf;

        opcodes[0x40] = this::ld_b_b;
        opcodes[0x41] = this::ld_b_c;
        opcodes[0x42] = this::ld_b_d;
        opcodes[0x43] = this::ld_b_e;
        opcodes[0x44] = this::ld_b_h;
        opcodes[0x45] = this::ld_b_l;
        opcodes[0x46] = this::ld_b_hlptr;
        opcodes[0x47] = this::ld_b_a;
        opcodes[0x48] = this::ld_c_b;
        opcodes[0x49] = this::ld_c_c;
        opcodes[0x4A] = this::ld_c_d;
        opcodes[0x4B] = this::ld_c_e;
        opcodes[0x4C] = this::ld_c_h;
        opcodes[0x4D] = this::ld_c_l;
        opcodes[0x4E] = this::ld_c_hlptr;
        opcodes[0x4F] = this::ld_c_a;

        opcodes[0x50] = this::ld_d_b;
        opcodes[0x51] = this::ld_d_c;
        opcodes[0x52] = this::ld_d_d;
        opcodes[0x53] = this::ld_d_e;
        opcodes[0x54] = this::ld_d_h;
        opcodes[0x55] = this::ld_d_l;
        opcodes[0x56] = this::ld_d_hlptr;
        opcodes[0x57] = this::ld_d_a;
        opcodes[0x58] = this::ld_e_b;
        opcodes[0x59] = this::ld_e_c;
        opcodes[0x5A] = this::ld_e_d;
        opcodes[0x5B] = this::ld_e_e;
        opcodes[0x5C] = this::ld_e_h;
        opcodes[0x5D] = this::ld_e_l;
        opcodes[0x5E] = this::ld_e_hlptr;
        opcodes[0x5F] = this::ld_e_a;

        opcodes[0x60] = this::ld_h_b;
        opcodes[0x61] = this::ld_h_c;
        opcodes[0x62] = this::ld_h_d;
        opcodes[0x63] = this::ld_h_e;
        opcodes[0x64] = this::ld_h_h;
        opcodes[0x65] = this::ld_h_l;
        opcodes[0x66] = this::ld_h_hlptr;
        opcodes[0x67] = this::ld_h_a;
        opcodes[0x68] = this::ld_l_b;
        opcodes[0x69] = this::ld_l_c;
        opcodes[0x6A] = this::ld_l_d;
        opcodes[0x6B] = this::ld_l_e;
        opcodes[0x6C] = this::ld_l_h;
        opcodes[0x6D] = this::ld_l_l;
        opcodes[0x6E] = this::ld_l_hlptr;
        opcodes[0x6F] = this::ld_l_a;

        opcodes[0x70] = this::ld_hlptr_b;
        opcodes[0x71] = this::ld_hlptr_c;
        opcodes[0x72] = this::ld_hlptr_d;
        opcodes[0x73] = this::ld_hlptr_e;
        opcodes[0x74] = this::ld_hlptr_h;
        opcodes[0x75] = this::ld_hlptr_l;
        opcodes[0x76] = this::halt;
        opcodes[0x77] = this::ld_hlptr_a;
        opcodes[0x78] = this::ld_a_b;
        opcodes[0x79] = this::ld_a_c;
        opcodes[0x7A] = this::ld_a_d;
        opcodes[0x7B] = this::ld_a_e;
        opcodes[0x7C] = this::ld_a_h;
        opcodes[0x7D] = this::ld_a_l;
        opcodes[0x7E] = this::ld_a_hlptr;
        opcodes[0x7F] = this::ld_a_a;

        opcodes[0x80] = this::add_b;
        opcodes[0x81] = this::add_c;
        opcodes[0x82] = this::add_d;
        opcodes[0x83] = this::add_e;
        opcodes[0x84] = this::add_h;
        opcodes[0x85] = this::add_l;
        opcodes[0x86] = this::add_hlptr;
        opcodes[0x87] = this::add_a;
        opcodes[0x88] = this::adc_b;
        opcodes[0x89] = this::adc_c;
        opcodes[0x8A] = this::adc_d;
        opcodes[0x8B] = this::adc_e;
        opcodes[0x8C] = this::adc_h;
        opcodes[0x8D] = this::adc_l;
        opcodes[0x8E] = this::adc_hlptr;
        opcodes[0x8F] = this::adc_a;

        opcodes[0x90] = this::sub_b;
        opcodes[0x91] = this::sub_c;
        opcodes[0x92] = this::sub_d;
        opcodes[0x93] = this::sub_e;
        opcodes[0x94] = this::sub_h;
        opcodes[0x95] = this::sub_l;
        opcodes[0x96] = this::sub_hlptr;
        opcodes[0x97] = this::sub_a;
        opcodes[0x98] = this::sbc_b;
        opcodes[0x99] = this::sbc_c;
        opcodes[0x9A] = this::sbc_d;
        opcodes[0x9B] = this::sbc_e;
        opcodes[0x9C] = this::sbc_h;
        opcodes[0x9D] = this::sbc_l;
        opcodes[0x9E] = this::sbc_hlptr;
        opcodes[0x9F] = this::sbc_a;

        opcodes[0xA0] = this::and_b;
        opcodes[0xA1] = this::and_c;
        opcodes[0xA2] = this::and_d;
        opcodes[0xA3] = this::and_e;
        opcodes[0xA4] = this::and_h;
        opcodes[0xA5] = this::and_l;
        opcodes[0xA6] = this::and_hlptr;
        opcodes[0xA7] = this::and_a;
        opcodes[0xA8] = this::xor_b;
        opcodes[0xA9] = this::xor_c;
        opcodes[0xAA] = this::xor_d;
        opcodes[0xAB] = this::xor_e;
        opcodes[0xAC] = this::xor_h;
        opcodes[0xAD] = this::xor_l;
        opcodes[0xAE] = this::xor_hlptr;
        opcodes[0xAF] = this::xor_a;

        opcodes[0xB0] = this::or_b;
        opcodes[0xB1] = this::or_c;
        opcodes[0xB2] = this::or_d;
        opcodes[0xB3] = this::or_e;
        opcodes[0xB4] = this::or_h;
        opcodes[0xB5] = this::or_l;
        opcodes[0xB6] = this::or_hlptr;
        opcodes[0xB7] = this::or_a;
        opcodes[0xB8] = this::cp_b;
        opcodes[0xB9] = this::cp_c;
        opcodes[0xBA] = this::cp_d;
        opcodes[0xBB] = this::cp_e;
        opcodes[0xBC] = this::cp_h;
        opcodes[0xBD] = this::cp_l;
        opcodes[0xBE] = this::cp_hlptr;
        opcodes[0xBF] = this::cp_a;

        opcodes[0xC0] = this::ret_nz;
        opcodes[0xC1] = this::pop_bc;
        opcodes[0xC2] = this::jp_nz_u16;
        opcodes[0xC3] = this::jp_u16;
        opcodes[0xC5] = this::push_bc;
        opcodes[0xC6] = this::add_u8;
        opcodes[0xC7] = this::rst_0;
        opcodes[0xC8] = this::ret_z;
        opcodes[0xC9] = this::ret;
        opcodes[0xCA] = this::jp_z_u16;
        opcodes[0xCB] = this::handle_cb;
        opcodes[0xCC] = this::call_z_u16;
        opcodes[0xCD] = this::call_u16;
        // CE
        opcodes[0xCF] = this::rst_1;

        opcodes[0xD0] = this::ret_nc;
        opcodes[0xD1] = this::pop_de;
        opcodes[0xD2] = this::jp_nc_u16;
        opcodes[0xD3] = () -> {
            throw new IllegalStateException("Illegal opcode: 0xD3");
        };
        // D4
        opcodes[0xD5] = this::push_de;
        opcodes[0xD6] = this::sub_u8;
        opcodes[0xD7] = this::rst_2;
        opcodes[0xD8] = this::ret_c;
        // D9
        opcodes[0xDA] = this::jp_c_u16;
        opcodes[0xDB] = () -> {
            throw new IllegalStateException("Illegal opcode: 0xDB");
        };
        opcodes[0xDC] = this::call_c_u16;
        opcodes[0xDD] = () -> {
            throw new IllegalStateException("Illegal opcode: 0xDD");
        };
        // DE
        opcodes[0xDF] = this::rst_3;

        opcodes[0xE0] = this::ldh_u8ptr_a;
        opcodes[0xE1] = this::pop_hl;
        // E2
        opcodes[0xE3] = () -> {
            throw new IllegalStateException("Illegal opcode: 0xE3");
        };
        opcodes[0xE4] = () -> {
            throw new IllegalStateException("Illegal opcode: 0xE4");
        };
        opcodes[0xE5] = this::push_hl;
        opcodes[0xE6] = this::and_u8;
        opcodes[0xE7] = this::rst_4;
        // E8
        // E9
        opcodes[0xEA] = this::ld_u16ptr_a;
        opcodes[0xEB] = () -> {
            throw new IllegalStateException("Illegal opcode: 0xEB");
        };
        opcodes[0xEC] = () -> {
            throw new IllegalStateException("Illegal opcode: 0xEC");
        };
        opcodes[0xED] = () -> {
            throw new IllegalStateException("Illegal opcode: 0xED");
        };
        // ED
        opcodes[0xEE] = this::xor_u8;
        opcodes[0xEF] = this::rst_5;

        opcodes[0xF0] = this::ldh_a_u8ptr;
        opcodes[0xF1] = this::pop_af;
        // F2
        opcodes[0xF3] = this::di;
        opcodes[0xF4] = () -> {
            throw new IllegalStateException("Illegal opcode: 0xF4");
        };
        opcodes[0xF5] = this::push_af;
        opcodes[0xF6] = this::or_u8;
        opcodes[0xF7] = this::rst_6;
        opcodes[0xFA] = this::ld_a_u16ptr;
        opcodes[0xFB] = this::ei;
        opcodes[0xFC] = () -> {
            throw new IllegalStateException("Illegal opcode: 0xFC");
        };
        opcodes[0xFD] = () -> {
            throw new IllegalStateException("Illegal opcode: 0xFD");
        };
        opcodes[0xFE] = this::cp_u8;
        opcodes[0xFF] = this::rst_7;
    }

    /* HELPER FUNCTIONS */

    private int get_u16() {
        int low = fetch();
        int high = fetch();
        return (high << 8) | low;
    }

    private void ld_r_hlptr_helper(IntConsumer setter) {
        setter.accept(mmu.readByte(reg.getHL()));
        cycles = 8;
    }

    private void ld_rrptr_r_helper(IntSupplier getRR, IntSupplier getR) {
        mmu.writeByte(getRR.getAsInt(), getR.getAsInt());
        cycles = 8;
    }

    private void push_rr_helper(IntSupplier getter) {
        reg.decSP();
        mmu.writeByte(reg.getSP(), getter.getAsInt() >> 8);
        reg.decSP();
        mmu.writeByte(reg.getSP(), getter.getAsInt() & 0xFF);
        cycles = 16;
    }

    private void pop_rr_helper(IntConsumer setter) {
        int value = mmu.readByte(reg.getSP());
        reg.incSP();
        value += (mmu.readByte(reg.getSP()) << 8);
        reg.incSP();
        setter.accept(value);
        cycles = 12;
    }

    /* OPCODES */

    /* MISC */

    private void scf() {
        reg.setFlagC(true);
        reg.setFlagN(false);
        reg.setFlagH(false);
        cycles = 4;
    }

    private void ccf() {
        reg.setFlagC(!reg.isFlagC());
        reg.setFlagN(false);
        reg.setFlagH(false);
        cycles = 4;
    }

    private void cpl() {
        reg.setA((~reg.getA()) & 0xFF);
        reg.setFlagH(true);
        reg.setFlagN(true);
        cycles = 4;
    }

    private void daa() {
        boolean addition = !reg.isFlagN();
        int value = reg.getA();
        int correction = 0;
        if (addition) {
            if ((value & 0xF) >= 0xA || reg.isFlagH())
                correction |= 0x06;
            if ((value & 0xFF) > 0x99 || reg.isFlagC())
                correction |= 0x60;
            value += correction;
        } else {
            if (reg.isFlagH())
                correction |= 0x06;
            if (reg.isFlagC())
                correction |= 0x60;
            value -= correction;
        }
        reg.setFlagH(false);
        reg.setFlagC(correction >= 0x60);
        reg.setFlagZ((value & 0xFF) == 0);
        reg.setA(value);
        cycles = 4;
    }

    private void ld_u16ptr_sp() {
        int adress = get_u16();
        mmu.writeByte(adress, reg.getSP() & 0xFF);
        mmu.writeByte(adress + 1, (reg.getSP() >> 8) & 0xFF);
        cycles = 20;
    }

    private void rlca() {
        rlc_helper(0x07);
        reg.setFlagZ(false);
        cycles = 4;
    }

    private void rla() {
        rl_helper(0x17);
        reg.setFlagZ(false);
        cycles = 4;
    }

    private void rrca() {
        rrc_helper(0x0F);
        reg.setFlagZ(false);
        cycles = 4;
    }

    private void rra() {
        rr_helper(0x1F);
        reg.setFlagZ(false);
        cycles = 4;
    }

    /* CB */

    /** CB opcode format: | group (2 bits) | operation (3 bits) | reg (3 bits) | */
    private void handle_cb() {
        int opcode = fetch();
        int operation = (opcode >> 3) & 0x7;
        int group = (opcode >> 6) & 0x3;
        switch (group) {
            case 0x0 -> {
                switch (operation) {
                    case 0 -> rlc_helper(opcode);
                    case 1 -> rrc_helper(opcode);
                    case 2 -> rl_helper(opcode);
                    case 3 -> rr_helper(opcode);
                    case 4 -> sla_helper(opcode);
                    case 5 -> sra_helper(opcode);
                    case 6 -> swap_helper(opcode);
                    case 7 -> srl_helper(opcode);
                    default -> throw new IllegalStateException();
                }
            }
            case 0x1 -> bit_helper(opcode);
            case 0x2 -> res_helper(opcode);
            case 0x3 -> set_helper(opcode);
        }
    }

    private void swap_helper(int opcode) {
        reg.setF(0);

        IntSupplier getter = get_cb_r_getter(opcode);
        IntConsumer setter = get_cb_r_setter(opcode);

        int value = getter.getAsInt();
        value = (((value << 4) & 0xF0) | ((value >> 4) & 0x0F));
        setter.accept(value);

        reg.setFlagZ(value == 0);
        cycles = (opcode & 0x7) == 0x6 ? 16 : 8;
    }

    private void srl_helper(int opcode) {
        reg.setFlagH(false);
        reg.setFlagN(false);

        IntSupplier getter = get_cb_r_getter(opcode);
        IntConsumer setter = get_cb_r_setter(opcode);

        int value = getter.getAsInt();
        int bit0 = value & 0x01;
        value = (value >> 1) & 0xFF;

        setter.accept(value);
        reg.setFlagZ(value == 0);
        reg.setFlagC(bit0 == 1);
        cycles = (opcode & 0x7) == 0x6 ? 16 : 8;
    }

    private void sra_helper(int opcode) {
        reg.setFlagH(false);
        reg.setFlagN(false);

        IntSupplier getter = get_cb_r_getter(opcode);
        IntConsumer setter = get_cb_r_setter(opcode);

        int value = getter.getAsInt();
        int bit0 = value & 0x01;
        int bit7 = (value >> 7) & 0x1;
        value = ((value >> 1) | (bit7 << 7)) & 0xFF;

        setter.accept(value);
        reg.setFlagZ(value == 0);
        reg.setFlagC(bit0 == 1);
        cycles = (opcode & 0x7) == 0x6 ? 16 : 8;
    }

    private void sla_helper(int opcode) {
        reg.setFlagH(false);
        reg.setFlagN(false);

        IntSupplier getter = get_cb_r_getter(opcode);
        IntConsumer setter = get_cb_r_setter(opcode);

        int value = getter.getAsInt();
        int bit7 = (value & 0x80) >> 7;
        value = (value << 1) & 0xFF;

        setter.accept(value);
        reg.setFlagZ(value == 0);
        reg.setFlagC(bit7 == 1);
        cycles = (opcode & 0x7) == 0x6 ? 16 : 8;

    }

    private void rr_helper(int opcode) {
        reg.setFlagH(false);
        reg.setFlagN(false);

        IntSupplier getter = get_cb_r_getter(opcode);
        IntConsumer setter = get_cb_r_setter(opcode);

        int value = getter.getAsInt();
        int oldCarry = reg.isFlagC() ? 1 : 0;
        int bit0 = value & 0x01;
        value = ((value >> 1) | (oldCarry << 7)) & 0xFF;

        setter.accept(value);
        reg.setFlagZ(value == 0);
        reg.setFlagC(bit0 == 1);
        cycles = (opcode & 0x7) == 0x6 ? 16 : 8;
    }

    private void rl_helper(int opcode) {
        reg.setFlagH(false);
        reg.setFlagN(false);

        IntSupplier getter = get_cb_r_getter(opcode);
        IntConsumer setter = get_cb_r_setter(opcode);

        int value = getter.getAsInt();
        int oldCarry = reg.isFlagC() ? 1 : 0;
        int bit7 = (value & 0x80) >> 7;
        value = ((value << 1) | oldCarry) & 0xFF;

        setter.accept(value);
        reg.setFlagZ(value == 0);
        reg.setFlagC(bit7 == 1);
        cycles = (opcode & 0x7) == 0x6 ? 16 : 8;
    }

    private void rrc_helper(int opcode) {
        reg.setFlagH(false);
        reg.setFlagN(false);

        IntSupplier getter = get_cb_r_getter(opcode);
        IntConsumer setter = get_cb_r_setter(opcode);

        int value = getter.getAsInt();
        int bit0 = value & 0x01;
        value = ((value >> 1) | bit0 << 7) & 0xFF;

        setter.accept(value);
        reg.setFlagZ(value == 0);
        reg.setFlagC(bit0 == 1);
        cycles = (opcode & 0x7) == 0x6 ? 16 : 8;
    }

    private void rlc_helper(int opcode) {
        reg.setFlagH(false);
        reg.setFlagN(false);

        IntSupplier getter = get_cb_r_getter(opcode);
        IntConsumer setter = get_cb_r_setter(opcode);

        int value = getter.getAsInt();
        int bit7 = (value & 0x80) >> 7;
        value = ((value << 1) | bit7) & 0xFF;

        setter.accept(value);
        reg.setFlagZ(value == 0);
        reg.setFlagC(bit7 == 1);
        cycles = (opcode & 0x7) == 0x6 ? 16 : 8;
    }

    private void bit_helper(int opcode) {
        IntSupplier getter = get_cb_r_getter(opcode);
        int bits = (opcode >> 3) & 0x7;
        reg.setFlagH(true);
        reg.setFlagN(false);
        reg.setFlagZ((getter.getAsInt() & (1 << bits)) == 0);
        cycles = (opcode & 0x7) == 0x6 ? 12 : 8;
    }

    private void res_helper(int opcode) {
        IntConsumer setter = get_cb_r_setter(opcode);
        IntSupplier getter = get_cb_r_getter(opcode);
        int bits = (opcode >> 3) & 0x7;
        setter.accept(getter.getAsInt() & ~(1 << bits));
        cycles = (opcode & 0x7) == 0x6 ? 16 : 8;
    }

    private void set_helper(int opcode) {
        IntConsumer setter = get_cb_r_setter(opcode);
        IntSupplier getter = get_cb_r_getter(opcode);
        int bits = (opcode >> 3) & 0x7;
        setter.accept(getter.getAsInt() | (1 << bits));
        cycles = (opcode & 0x7) == 0x6 ? 16 : 8;
    }

    private IntConsumer get_cb_r_setter(int opcode) {
        int value = opcode & 0x7;
        return switch (value) {
            case 0 -> reg::setB;
            case 1 -> reg::setC;
            case 2 -> reg::setD;
            case 3 -> reg::setE;
            case 4 -> reg::setH;
            case 5 -> reg::setL;
            case 6 -> (v) -> mmu.writeByte(reg.getHL(), v);
            case 7 -> reg::setA;
            default -> throw new IllegalStateException();
        };
    }

    private IntSupplier get_cb_r_getter(int opcode) {
        int value = opcode & 0x7;
        return switch (value) {
            case 0 -> reg::getB;
            case 1 -> reg::getC;
            case 2 -> reg::getD;
            case 3 -> reg::getE;
            case 4 -> reg::getH;
            case 5 -> reg::getL;
            case 6 -> () -> mmu.readByte(reg.getHL());
            case 7 -> reg::getA;
            default -> throw new IllegalStateException();
        };
    }

    /* DEC */

    // @formatter:off
    private void dec_bc() { reg.decBC(); cycles = 8; }
    private void dec_de() { reg.decDE(); cycles = 8; }
    private void dec_hl() { reg.decHL(); cycles = 8; }
    private void dec_sp() { reg.decSP(); cycles = 8; }
    // @formatter:on

    // @formatter:off
    private void dec_b() { dec_r_helper(reg::decB, reg::getB); }
    private void dec_c() { dec_r_helper(reg::decC, reg::getC); }
    private void dec_d() { dec_r_helper(reg::decD, reg::getD); }
    private void dec_e() { dec_r_helper(reg::decE, reg::getE); }
    private void dec_h() { dec_r_helper(reg::decH, reg::getH); }
    private void dec_l() { dec_r_helper(reg::decL, reg::getL); }
    private void dec_a() { dec_r_helper(reg::decA, reg::getA); }
    // @formatter:on

    private void dec_r_helper(Runnable decReg, IntSupplier getter) {
        int value = getter.getAsInt();
        reg.setFlagH((value & 0xF) == 0x0);
        reg.setFlagN(true);
        decReg.run();
        reg.setFlagZ(getter.getAsInt() == 0);
        cycles = 4;
    }

    private void dec_hlptr() {
        int value = mmu.readByte(reg.getHL());
        reg.setFlagH((value & 0xF) == 0x0);
        reg.setFlagN(true);
        mmu.writeByte(reg.getHL(), value - 1);
        reg.setFlagZ(mmu.readByte(reg.getHL()) == 0);
        cycles = 12;
    }

    /* INC */

    // @formatter:off
    private void inc_bc() { reg.incBC(); cycles = 8; }
    private void inc_de() { reg.incDE(); cycles = 8; }
    private void inc_hl() { reg.incHL(); cycles = 8; }
    private void inc_sp() { reg.incSP(); cycles = 8; }
    // @formatter:on

    // @formatter:off
    private void inc_b() { inc_r_helper(reg::incB, reg::getB); }
    private void inc_c() { inc_r_helper(reg::incC, reg::getC); }
    private void inc_d() { inc_r_helper(reg::incD, reg::getD); }
    private void inc_e() { inc_r_helper(reg::incE, reg::getE); }
    private void inc_h() { inc_r_helper(reg::incH, reg::getH); }
    private void inc_l() { inc_r_helper(reg::incL, reg::getL); }
    private void inc_a() { inc_r_helper(reg::incA, reg::getA); }
    // @formatter:on

    private void inc_r_helper(Runnable incReg, IntSupplier getter) {
        int value = getter.getAsInt();
        reg.setFlagH((value & 0xF) == 0xF);
        reg.setFlagN(false);
        incReg.run();
        reg.setFlagZ(getter.getAsInt() == 0);
        cycles = 4;
    }

    private void inc_hlptr() {
        int value = mmu.readByte(reg.getHL());
        reg.setFlagH((value & 0xF) == 0xF);
        reg.setFlagN(false);
        mmu.writeByte(reg.getHL(), value + 1);
        reg.setFlagZ(mmu.readByte(reg.getHL()) == 0);
        cycles = 12;
    }

    /* SBC */

    // @formatter:off
    private void sbc_b() { sbc_r_helper(reg::getB); }
    private void sbc_c() { sbc_r_helper(reg::getC); }
    private void sbc_d() { sbc_r_helper(reg::getD); }
    private void sbc_e() { sbc_r_helper(reg::getE); }
    private void sbc_h() { sbc_r_helper(reg::getH); }
    private void sbc_l() { sbc_r_helper(reg::getL); }
    private void sbc_a() { sbc_r_helper(reg::getA); }
    // @formatter:on

    private void sbc_r_helper(IntSupplier getter) {
        int value = getter.getAsInt();
        int valueFlag = reg.isFlagC() ? 1 : 0;
        int result = reg.getA() - (value + valueFlag);
        reg.setFlagH(((value & 0xF) + valueFlag) > (reg.getA() & 0xF));
        reg.setFlagN(true);
        reg.setFlagZ((result & 0xFF) == 0);
        reg.setFlagC(reg.getA() < (value + valueFlag));
        reg.setA(result);
        cycles = 4;
    }

    private void sbc_hlptr() {
        sbc_r_helper(() -> mmu.readByte(reg.getHL()));
        cycles = 8;
    }

    /* SUB */

    // @formatter:off
    private void sub_b() { sub_r_helper(reg::getB); }
    private void sub_c() { sub_r_helper(reg::getC); }
    private void sub_d() { sub_r_helper(reg::getD); }
    private void sub_e() { sub_r_helper(reg::getE); }
    private void sub_h() { sub_r_helper(reg::getH); }
    private void sub_l() { sub_r_helper(reg::getL); }
    private void sub_a() { sub_r_helper(reg::getA); }
    // @formatter:on

    private void sub_r_helper(IntSupplier getter) {
        int value = getter.getAsInt();
        reg.setFlagN(true);
        reg.setFlagC(reg.getA() < value);
        reg.setFlagH((reg.getA() & 0xF) < (value & 0xF));
        reg.setA(reg.getA() - value);
        reg.setFlagZ(reg.getA() == 0);
        cycles = 4;
    }

    private void sub_hlptr() {
        int value = mmu.readByte(reg.getHL());
        reg.setFlagN(true);
        reg.setFlagC(reg.getA() < value);
        reg.setFlagH((reg.getA() & 0xF) < (value & 0xF));
        reg.setA(reg.getA() - value);
        reg.setFlagZ(reg.getA() == 0);
        cycles = 8;
    }

    private void sub_u8() {
        int value = fetch();
        reg.setFlagN(true);
        reg.setFlagC(reg.getA() < value);
        reg.setFlagH((reg.getA() & 0xF) < (value & 0xF));
        reg.setA(reg.getA() - value);
        reg.setFlagZ(reg.getA() == 0);
        cycles = 8;
    }

    /* ADC */

    // @formatter:off
    private void adc_b() { adc_r_helper(reg::getB); }
    private void adc_c() { adc_r_helper(reg::getC); }
    private void adc_d() { adc_r_helper(reg::getD); }
    private void adc_e() { adc_r_helper(reg::getE); }
    private void adc_h() { adc_r_helper(reg::getH); }
    private void adc_l() { adc_r_helper(reg::getL); }
    private void adc_a() { adc_r_helper(reg::getA); }
    // @formatter:on

    private void adc_r_helper(IntSupplier getter) {
        int value = getter.getAsInt();
        int carryValue = reg.isFlagC() ? 1 : 0;
        int result = reg.getA() + value + carryValue;
        reg.setFlagN(false);
        reg.setFlagH(((reg.getA() & 0xF) + (value & 0xF) + carryValue) > 0xF);
        reg.setFlagC(result > 0xFF);
        reg.setFlagZ((result & 0xFF) == 0);
        reg.setA(result);
        cycles = 4;
    }

    private void adc_hlptr() {
        adc_r_helper(() -> (mmu.readByte(reg.getHL())));
        cycles = 8;
    }

    /* ADD */

    // @formatter:off
    private void add_b() { add_r_helper(reg::getB); }
    private void add_c() { add_r_helper(reg::getC); }
    private void add_d() { add_r_helper(reg::getD); }
    private void add_e() { add_r_helper(reg::getE); }
    private void add_h() { add_r_helper(reg::getH); }
    private void add_l() { add_r_helper(reg::getL); }
    private void add_a() { add_r_helper(reg::getA); }
    // @formatter:on

    private void add_r_helper(IntSupplier getter) {
        int value = getter.getAsInt();
        reg.setFlagN(false);
        reg.setFlagH(((reg.getA() & 0xF) + (value & 0xF)) > 0xF);
        reg.setFlagC((reg.getA() + value) > 0xFF);
        reg.setA(reg.getA() + value);
        reg.setFlagZ(reg.getA() == 0);
        cycles = 4;
    }

    private void add_hlptr() {
        int value = mmu.readByte(reg.getHL());
        reg.setFlagN(false);
        reg.setFlagH(((reg.getA() & 0xF) + (value & 0xF)) > 0xF);
        reg.setFlagC((reg.getA() + value) > 0xFF);
        reg.setA(reg.getA() + value);
        reg.setFlagZ(reg.getA() == 0);
        cycles = 8;
    }

    private void add_u8() {
        int value = fetch();
        reg.setFlagN(false);
        reg.setFlagH(((reg.getA() & 0xF) + (value & 0xF)) > 0xF);
        reg.setFlagC((reg.getA() + value) > 0xFF);
        reg.setA(reg.getA() + value);
        reg.setFlagZ(reg.getA() == 0);
        cycles = 8;
    }

    /* ADD RR */

    // @formatter:off
    private void add_bc() { add_rr_helper(reg::getBC);}
    private void add_de() { add_rr_helper(reg::getDE);}
    private void add_hl() { add_rr_helper(reg::getHL);}
    private void add_sp() { add_rr_helper(reg::getSP);}
    // @formatter:on

    private void add_rr_helper(IntSupplier getter) {
        int value = getter.getAsInt();
        int result = value + reg.getHL();
        reg.setFlagN(false);
        reg.setFlagC(result > 0xFFFF);
        reg.setFlagH(((value & 0xFFF) + (reg.getHL() & 0xFFF)) > 0xFFF);
        reg.setHL(result);
        cycles = 8;
    }

    /* CP */

    // @formatter:off
    private void cp_b() { cp_r_helper(reg::getB); }
    private void cp_c() { cp_r_helper(reg::getC); }
    private void cp_d() { cp_r_helper(reg::getD); }
    private void cp_e() { cp_r_helper(reg::getE); }
    private void cp_l() { cp_r_helper(reg::getL); }
    private void cp_h() { cp_r_helper(reg::getH); }
    private void cp_a() { cp_r_helper(reg::getA); }
    // @formatter:on

    private void cp_hlptr() {
        int value = mmu.readByte(reg.getHL());
        reg.setFlagZ(value == reg.getA());
        reg.setFlagN(true);
        reg.setFlagH((reg.getA() & 0xF) < (value & 0xF));
        reg.setFlagC(reg.getA() < value);
        cycles = 8;
    }

    private void cp_r_helper(IntSupplier getter) {
        // A - R
        int value = getter.getAsInt();
        reg.setFlagZ(value == reg.getA());
        reg.setFlagN(true);
        reg.setFlagH((reg.getA() & 0xF) < (value & 0xF));
        reg.setFlagC(reg.getA() < value);
        cycles = 4;
    }

    private void cp_u8() {
        int value = fetch();
        reg.setFlagZ(value == reg.getA());
        reg.setFlagN(true);
        reg.setFlagH((reg.getA() & 0xF) < (value & 0xF));
        reg.setFlagC(reg.getA() < value);
        cycles = 8;
    }

    /* AND */

    // @formatter:off
    private void and_b() {and_r_helper(reg::getB); }
    private void and_c() {and_r_helper(reg::getC); }
    private void and_d() {and_r_helper(reg::getD); }
    private void and_e() {and_r_helper(reg::getE); }
    private void and_h() {and_r_helper(reg::getH); }
    private void and_l() {and_r_helper(reg::getL); }
    private void and_a() {and_r_helper(reg::getA); }
    // @formatter:on

    private void and_hlptr() {
        reg.setA((mmu.readByte(reg.getHL()) & 0xFF) & reg.getA());
        reg.setFlagZ(reg.getA() == 0);
        reg.setFlagH(true);
        cycles = 8;
    }

    private void and_r_helper(IntSupplier getter) {
        reg.setA(getter.getAsInt() & reg.getA());
        reg.setF(0x00);
        reg.setFlagZ(reg.getA() == 0);
        reg.setFlagH(true);
        cycles = 4;
    }

    private void and_u8() {
        reg.setA(fetch() & reg.getA());
        reg.setF(0x00);
        reg.setFlagZ(reg.getA() == 0);
        reg.setFlagH(true);
        cycles = 8;
    }
    /* OR */

    // @formatter:off
    private void or_b() { or_r_helper(reg::getB); }
    private void or_c() { or_r_helper(reg::getC); }
    private void or_d() { or_r_helper(reg::getD); }
    private void or_e() { or_r_helper(reg::getE); }
    private void or_h() { or_r_helper(reg::getH); }
    private void or_l() { or_r_helper(reg::getL); }
    private void or_a() { or_r_helper(reg::getA); }
    // @formatter:on

    private void or_hlptr() {
        reg.setA((mmu.readByte(reg.getHL()) & 0xFF) | reg.getA());
        reg.setFlagZ(reg.getA() == 0);
        cycles = 8;
    }

    private void or_r_helper(IntSupplier getter) {
        reg.setA(getter.getAsInt() | reg.getA());
        reg.setF(0x00);
        reg.setFlagZ(reg.getA() == 0);
        cycles = 4;
    }

    private void or_u8() {
        reg.setA(fetch() | reg.getA());
        reg.setF(0x00);
        reg.setFlagZ(reg.getA() == 0);
        cycles = 8;
    }

    /* XOR */

    // @formatter:off
    private void xor_b() { xor_r_helper(reg::getB); }
    private void xor_c() { xor_r_helper(reg::getC); }
    private void xor_d() { xor_r_helper(reg::getD); }
    private void xor_e() { xor_r_helper(reg::getE); }
    private void xor_h() { xor_r_helper(reg::getH); }
    private void xor_l() { xor_r_helper(reg::getL); }
    private void xor_a() { xor_r_helper(reg::getA); }
    // @formatter:on

    private void xor_hlptr() {
        reg.setA((mmu.readByte(reg.getHL()) & 0xFF) ^ reg.getA());
        reg.setFlagZ(reg.getA() == 0);
        cycles = 8;
    }

    private void xor_r_helper(IntSupplier getter) {
        reg.setA(getter.getAsInt() ^ reg.getA());
        reg.setF(0x00);
        reg.setFlagZ(reg.getA() == 0);
        cycles = 4;
    }

    private void xor_u8() {
        reg.setA(fetch() ^ reg.getA());
        reg.setF(0x00);
        reg.setFlagZ(reg.getA() == 0);
        cycles = 8;
    }

    /* PUSH */

    // @formatter:off
    private void push_bc() { push_rr_helper(reg::getBC); }
    private void push_de() { push_rr_helper(reg::getDE); }
    private void push_hl() { push_rr_helper(reg::getHL); }
    private void push_af() { push_rr_helper(reg::getAF); }
    // @formatter:on

    /* POP */

    // @formatter:off
    private void pop_bc() { pop_rr_helper(reg::setBC); }
    private void pop_de() { pop_rr_helper(reg::setDE); }
    private void pop_hl() { pop_rr_helper(reg::setHL); }
    private void pop_af() { pop_rr_helper(reg::setAF); }
    // @formatter:on

    /* LD rr */

    // @formatter:off
    private void ld_bc_u16() { ld_rr_u16_helper(reg::setBC); }
    private void ld_de_u16() { ld_rr_u16_helper(reg::setDE); }
    private void ld_hl_u16() { ld_rr_u16_helper(reg::setHL); }
    private void ld_sp_u16() { ld_rr_u16_helper(reg::setSP); }
    // @formatter:on

    private void ld_rr_u16_helper(IntConsumer setter) {
        setter.accept(get_u16());
        cycles = 12;
    }

    /* LDH */

    private void ldh_u8ptr_a() {
        mmu.writeByte((fetch() & 0xFF) | 0xFF00, reg.getA());
        cycles = 12;
    }

    private void ldh_a_u8ptr() {
        reg.setA(mmu.readByte((fetch() & 0xFF) | 0xFF00));
        cycles = 12;
    }

    /* LD */

    // @formatter:off
    private void ld_a_bcptr() { ld_r_rrptr(reg::setA, reg::getBC); }
    private void ld_a_deptr() { ld_r_rrptr(reg::setA, reg::getDE); }
    // @formatter:on

    private void ld_r_rrptr(IntConsumer setter, IntSupplier getter) {
        setter.accept(mmu.readByte(getter.getAsInt()));
        cycles = 8;
    }

    // @formatter:off
    private void ld_a_hlptr_inc() { ld_a_hlptr(); reg.incHL(); }
    private void ld_a_hlptr_dec() { ld_a_hlptr(); reg.decHL(); }
    // @formatter:on

    private void ld_hlptr_a_inc() {
        ld_rrptr_r_helper(reg::getHL, reg::getA);
        reg.incHL();
    }

    private void ld_hlptr_a_dec() {
        ld_rrptr_r_helper(reg::getHL, reg::getA);
        reg.decHL();
    }

    // @formatter:off
    private void ld_bcptr_a() { ld_rrptr_r_helper(reg::getBC, reg::getA); }
    private void ld_deptr_a() { ld_rrptr_r_helper(reg::getDE, reg::getA); }
    // @formatter:on

    private void ld_hlptr_u8() {
        mmu.writeByte(reg.getHL(), fetch());
        cycles = 12;
    }

    private void ld_u16ptr_a() {
        mmu.writeByte(get_u16(), reg.getA());
        cycles = 16;
    }

    private void ld_a_u16ptr() {
        reg.setA(mmu.readByte(get_u16()));
        cycles = 16;
    }

    // @formatter:off
    private void stop() {fetch(); halted = true; cycles = 4;}
    private void halt() { halted = true; cycles = 4; }
    private void di() { ime = false; cycles = 4; }
    private void ei() { ime = true; cycles = 4; }
    // @formatter:on

    /* LD (HL), r */

    // @formatter:off
    private void ld_hlptr_b() { ld_hlptr_r_helper(reg::getB); }
    private void ld_hlptr_c() { ld_hlptr_r_helper(reg::getC); }
    private void ld_hlptr_d() { ld_hlptr_r_helper(reg::getD); }
    private void ld_hlptr_e() { ld_hlptr_r_helper(reg::getE); }
    private void ld_hlptr_h() { ld_hlptr_r_helper(reg::getH); }
    private void ld_hlptr_l() { ld_hlptr_r_helper(reg::getL); }
    private void ld_hlptr_a() { ld_hlptr_r_helper(reg::getA); }
    // @formatter:on

    private void ld_hlptr_r_helper(IntSupplier getter) {
        mmu.writeByte(reg.getHL(), getter.getAsInt());
        cycles = 8;
    }

    /* LD A, r */

    // @formatter:off
    private void ld_a_b() { ld_r_r_helper(reg::setA, reg::getB); }
    private void ld_a_c() { ld_r_r_helper(reg::setA, reg::getC); }
    private void ld_a_d() { ld_r_r_helper(reg::setA, reg::getD); }
    private void ld_a_e() { ld_r_r_helper(reg::setA, reg::getE); }
    private void ld_a_h() { ld_r_r_helper(reg::setA, reg::getH); }
    private void ld_a_l() { ld_r_r_helper(reg::setA, reg::getL); }
    private void ld_a_a() { ld_r_r_helper(reg::setA, reg::getA); }
    private void ld_a_hlptr() { ld_r_hlptr_helper(reg::setA); }
    // @formatter:on

    /* LD H, r */

    // @formatter:off
    private void ld_h_b() { ld_r_r_helper(reg::setH, reg::getB); }
    private void ld_h_c() { ld_r_r_helper(reg::setH, reg::getC); }
    private void ld_h_d() { ld_r_r_helper(reg::setH, reg::getD); }
    private void ld_h_e() { ld_r_r_helper(reg::setH, reg::getE); }
    private void ld_h_h() { ld_r_r_helper(reg::setH, reg::getH); }
    private void ld_h_l() { ld_r_r_helper(reg::setH, reg::getL); }
    private void ld_h_a() { ld_r_r_helper(reg::setH, reg::getA); }
    private void ld_h_hlptr() { ld_r_hlptr_helper(reg::setH); }

    /* LD L, r */

    private void ld_l_b() { ld_r_r_helper(reg::setL, reg::getB); }
    private void ld_l_c() { ld_r_r_helper(reg::setL, reg::getC); }
    private void ld_l_d() { ld_r_r_helper(reg::setL, reg::getD); }
    private void ld_l_e() { ld_r_r_helper(reg::setL, reg::getE); }
    private void ld_l_h() { ld_r_r_helper(reg::setL, reg::getH); }
    private void ld_l_l() { ld_r_r_helper(reg::setL, reg::getL); }
    private void ld_l_a() { ld_r_r_helper(reg::setL, reg::getA); }
    private void ld_l_hlptr() { ld_r_hlptr_helper(reg::setL); }

    /* LD E, r */

    private void ld_e_b() { ld_r_r_helper(reg::setE, reg::getB); }
    private void ld_e_c() { ld_r_r_helper(reg::setE, reg::getC); }
    private void ld_e_d() { ld_r_r_helper(reg::setE, reg::getD); }
    private void ld_e_e() { ld_r_r_helper(reg::setE, reg::getE); }
    private void ld_e_h() { ld_r_r_helper(reg::setE, reg::getH); }
    private void ld_e_l() { ld_r_r_helper(reg::setE, reg::getL); }
    private void ld_e_a() { ld_r_r_helper(reg::setE, reg::getA); }
    private void ld_e_hlptr() { ld_r_hlptr_helper(reg::setE); }

    /* LD D, r */

    private void ld_d_b() { ld_r_r_helper(reg::setD, reg::getB); }
    private void ld_d_c() { ld_r_r_helper(reg::setD, reg::getC); }
    private void ld_d_d() { ld_r_r_helper(reg::setD, reg::getD); }
    private void ld_d_e() { ld_r_r_helper(reg::setD, reg::getE); }
    private void ld_d_h() { ld_r_r_helper(reg::setD, reg::getH); }
    private void ld_d_l() { ld_r_r_helper(reg::setD, reg::getL); }
    private void ld_d_a() { ld_r_r_helper(reg::setD, reg::getA); }
    private void ld_d_hlptr() { ld_r_hlptr_helper(reg::setD); }

    /* LD C, r */

    private void ld_c_b() { ld_r_r_helper(reg::setC, reg::getB); }
    private void ld_c_c() { ld_r_r_helper(reg::setC, reg::getC); }
    private void ld_c_d() { ld_r_r_helper(reg::setC, reg::getD); }
    private void ld_c_e() { ld_r_r_helper(reg::setC, reg::getE); }
    private void ld_c_h() { ld_r_r_helper(reg::setC, reg::getH); }
    private void ld_c_l() { ld_r_r_helper(reg::setC, reg::getL); }
    private void ld_c_a() { ld_r_r_helper(reg::setC, reg::getA); }
    private void ld_c_hlptr() { ld_r_hlptr_helper(reg::setC); }

    /* LD B, r */

    private void ld_b_b() { ld_r_r_helper(reg::setB, reg::getB); }
    private void ld_b_c() { ld_r_r_helper(reg::setB, reg::getC); }
    private void ld_b_d() { ld_r_r_helper(reg::setB, reg::getD); }
    private void ld_b_e() { ld_r_r_helper(reg::setB, reg::getE); }
    private void ld_b_h() { ld_r_r_helper(reg::setB, reg::getH); }
    private void ld_b_l() { ld_r_r_helper(reg::setB, reg::getL); }
    private void ld_b_a() { ld_r_r_helper(reg::setB, reg::getA); }
    private void ld_b_hlptr() { ld_r_hlptr_helper(reg::setB); }

    private void ld_r_r_helper(IntConsumer setter, IntSupplier getter) {
        setter.accept(getter.getAsInt());
        cycles = 4;
    }

    /* LD r, u8 */

    private void ld_b_u8() { ld_r_u8_helper(reg::setB); }
    private void ld_c_u8() { ld_r_u8_helper(reg::setC); }
    private void ld_d_u8() { ld_r_u8_helper(reg::setD); }
    private void ld_e_u8() { ld_r_u8_helper(reg::setE); }
    private void ld_h_u8() { ld_r_u8_helper(reg::setH); }
    private void ld_l_u8() { ld_r_u8_helper(reg::setL); }
    private void ld_a_u8() { ld_r_u8_helper(reg::setA); }
    // @formatter:on

    private void ld_r_u8_helper(IntConsumer setter) {
        setter.accept(fetch());
        cycles = 8;
    }

    /* RST */

    // @formatter:off
    private void rst_0() { rst_helper(0x00); }
    private void rst_1() { rst_helper(0x08); }
    private void rst_2() { rst_helper(0x10); }
    private void rst_3() { rst_helper(0x18); }
    private void rst_4() { rst_helper(0x20); }
    private void rst_5() { rst_helper(0x28); }
    private void rst_6() { rst_helper(0x30); }
    private void rst_7() { rst_helper(0x38); }
    // @formatter:on

    private void rst_helper(int value) {
        reg.decSP();
        mmu.writeByte(reg.getSP(), (reg.getPC() >> 8) & 0xFF);
        reg.decSP();
        mmu.writeByte(reg.getSP(), (reg.getPC() & 0xFF));
        reg.setPC(value);
        cycles = 16;
    }

    /* CALL */

    private void call_u16() {
        int address = get_u16();

        reg.decSP();
        mmu.writeByte(reg.getSP(), (reg.getPC() >> 8) & 0xFF);
        reg.decSP();
        mmu.writeByte(reg.getSP(), (reg.getPC() & 0xFF));

        reg.setPC(address);
        cycles = 24;

    }

    private void call_z_u16() {
        if (reg.isFlagZ()) {
            call_u16();
            return;
        }
        fetch();
        fetch();
        cycles = 12;
        return;
    }

    private void call_c_u16() {
        if (reg.isFlagC()) {
            call_u16();
            return;
        }
        fetch();
        fetch();
        cycles = 12;
        return;
    }

    /* RET */

    // @formatter:off
    private void ret_c() { ret_flag_helper(reg.isFlagC()); }
    private void ret_z()  { ret_flag_helper(reg.isFlagZ()); }
    private void ret_nc() { ret_flag_helper(!reg.isFlagC()); }
    private void ret_nz() { ret_flag_helper(!reg.isFlagZ()); }
    // @formatter:on 

    private void ret_flag_helper(boolean flag) {
        if (flag) {
            ret();
            return;
        }
        cycles = 8;
    }

    private void ret() {
        int low = mmu.readByte(reg.getSP());
        reg.incSP();
        int high = mmu.readByte(reg.getSP());
        reg.incSP();

        reg.setPC(high << 8 | low);
        cycles = 16;
    }

    /* JR s8 */

    // @formatter:off
    private void jr_c_s8() { jump_flag_s8_helper(reg.isFlagC()); }
    private void jr_z_s8() { jump_flag_s8_helper(reg.isFlagZ()); }
    private void jr_nc_s8() { jump_flag_s8_helper(!reg.isFlagC()); }
    private void jr_nz_s8() { jump_flag_s8_helper(!reg.isFlagZ()); }
    // @formatter:on

    private void jump_flag_s8_helper(boolean flag) {
        if (flag) {
            jr_s8();
            return;
        }
        fetch();
        cycles = 8;
    }

    private void jr_s8() {
        int offset = (byte) fetch();
        reg.setPC(reg.getPC() + offset);
        cycles = 12;
    }

    /* JP u16 */

    // @formatter:off
    private void jp_c_u16() { jump_flag_u16_helper(reg.isFlagC()); } 
    private void jp_z_u16() { jump_flag_u16_helper(reg.isFlagZ()); }
    private void jp_nc_u16() { jump_flag_u16_helper(!reg.isFlagC()); }
    private void jp_nz_u16() { jump_flag_u16_helper(!reg.isFlagZ()); }
    // @formatter:on

    private void jump_flag_u16_helper(boolean flag) {
        if (flag) {
            jp_u16();
            return;
        }
        fetch();
        fetch();
        cycles = 12;
    }

    private void jp_u16() {
        reg.setPC(get_u16());
        cycles = 16;
    }
}
