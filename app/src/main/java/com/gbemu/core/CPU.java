package com.gbemu.core;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.zip.Adler32;

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
        opcodes[0x06] = this::ld_b_u8;
        opcodes[0x0E] = this::ld_c_u8;

        opcodes[0x11] = this::ld_de_u16;
        opcodes[0x12] = this::ld_deptr_a;
        opcodes[0x16] = this::ld_d_u8;
        opcodes[0x18] = this::jr_s8;
        opcodes[0x1E] = this::ld_e_u8;

        opcodes[0x21] = this::ld_hl_u16;
        opcodes[0x20] = this::jr_nz_s8;
        opcodes[0x22] = this::ld_hlptr_a_inc;
        opcodes[0x26] = this::ld_h_u8;
        opcodes[0x28] = this::jr_z_s8;
        opcodes[0x2A] = this::ld_a_hlptr_inc;
        opcodes[0x2E] = this::ld_l_u8;

        opcodes[0x30] = this::jr_nc_s8;
        opcodes[0x31] = this::ld_sp_u16;
        opcodes[0x32] = this::ld_hlptr_a_dec;
        opcodes[0x36] = this::ld_hlptr_u8;
        opcodes[0x38] = this::jr_c_s8;
        opcodes[0x3A] = this::ld_a_hlptr_dec;
        opcodes[0x3E] = this::ld_a_u8;

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

        opcodes[0xA8] = this::xor_b;
        opcodes[0xA9] = this::xor_c;
        opcodes[0xAA] = this::xor_d;
        opcodes[0xAB] = this::xor_e;
        opcodes[0xAC] = this::xor_h;
        opcodes[0xAD] = this::xor_l;
        opcodes[0xAE] = this::xor_hlptr;
        opcodes[0xAF] = this::xor_a;

        opcodes[0xC0] = this::ret_nz;
        opcodes[0xC1] = this::pop_bc;
        opcodes[0xC2] = this::jp_nz_u16;
        opcodes[0xC3] = this::jp_u16;
        opcodes[0xC5] = this::push_bc;
        opcodes[0xC8] = this::ret_z;
        opcodes[0xC9] = this::ret;
        opcodes[0xCA] = this::jp_z_u16;
        opcodes[0xCD] = this::call_u16;

        opcodes[0xD0] = this::ret_nc;
        opcodes[0xD1] = this::pop_de;
        opcodes[0xD2] = this::jp_nc_u16;
        opcodes[0xD5] = this::push_de;
        opcodes[0xD8] = this::ret_c;
        opcodes[0xDA] = this::jp_c_u16;

        opcodes[0xE0] = this::ldh_u8ptr_a;
        opcodes[0xE1] = this::pop_hl;
        opcodes[0xE5] = this::push_hl;
        opcodes[0xEA] = this::ld_u16ptr_a;

        opcodes[0xF0] = this::ldh_a_u8ptr;
        opcodes[0xF1] = this::pop_af;
        opcodes[0xF3] = this::di;
        opcodes[0xF5] = this::push_af;
        opcodes[0xFA] = this::ld_a_u16ptr;
        opcodes[0xFB] = this::ei;
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

    private void ld_hlptr_r_helper(IntSupplier getter) {
        mmu.writeByte(reg.getHL(), getter.getAsInt());
        cycles = 8;
    }

    private void ld_r_r_helper(IntConsumer setter, IntSupplier getter) {
        setter.accept(getter.getAsInt());
        cycles = 4;
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

    private void ld_rrptr_r_helper(IntSupplier getRR, IntSupplier getR) {
        mmu.writeByte(getRR.getAsInt(), getR.getAsInt());
        cycles = 8;
    }

    private void ld_rr_u16_helper(IntConsumer setter) {
        setter.accept(get_u16());
        cycles = 12;
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

    private void xor_r_helper(IntSupplier getter) {
        reg.setA(getter.getAsInt() ^ reg.getA());
        reg.setFlagZ(reg.getA() == 0);
        cycles = 4;
    }

    /* OPCODES */

    /* XOR */

    private void xor_b() {
        xor_r_helper(reg::getB);
    }

    private void xor_c() {
        xor_r_helper(reg::getC);
    }

    private void xor_d() {
        xor_r_helper(reg::getD);
    }

    private void xor_e() {
        xor_r_helper(reg::getE);
    }

    private void xor_h() {
        xor_r_helper(reg::getH);
    }

    private void xor_l() {
        xor_r_helper(reg::getL);
    }

    private void xor_a() {
        xor_r_helper(reg::getA);
    }

    private void xor_hlptr(){
        reg.setA((mmu.readByte(reg.getHL()) & 0xFF) ^ reg.getA());
        reg.setFlagZ(reg.getA() == 0);
        cycles = 8;
    }

    /* PUSH */

    private void push_bc() {
        push_rr_helper(reg::getBC);
    }

    private void push_de() {
        push_rr_helper(reg::getDE);
    }

    private void push_hl() {
        push_rr_helper(reg::getHL);
    }

    private void push_af() {
        push_rr_helper(reg::getAF);
    }

    /* POP */

    private void pop_bc() {
        pop_rr_helper(reg::setBC);
    }

    private void pop_de() {
        pop_rr_helper(reg::setDE);
    }

    private void pop_hl() {
        pop_rr_helper(reg::setHL);
    }

    private void pop_af() {
        pop_rr_helper(reg::setAF);
    }

    private void ld_bc_u16() {
        ld_rr_u16_helper(reg::setBC);
    }

    private void ld_de_u16() {
        ld_rr_u16_helper(reg::setDE);
    }

    private void ld_hl_u16() {
        ld_rr_u16_helper(reg::setHL);
    }

    private void ld_sp_u16() {
        ld_rr_u16_helper(reg::setSP);
    }

    private void ldh_u8ptr_a() {
        mmu.writeByte((fetch() & 0xFF) | 0xFF00, reg.getA());
        cycles = 12;
    }

    private void ldh_a_u8ptr() {
        reg.setA(mmu.readByte((fetch() & 0xFF) | 0xFF00));
        cycles = 12;
    }

    private void ld_a_hlptr_inc() {
        ld_a_hlptr();
        reg.incHL();
    }

    private void ld_a_hlptr_dec() {
        ld_a_hlptr();
        reg.decHL();
    }

    private void ld_hlptr_a_inc() {
        ld_rrptr_r_helper(reg::getHL, reg::getA);
        reg.incHL();
    }

    private void ld_hlptr_a_dec() {
        ld_rrptr_r_helper(reg::getHL, reg::getA);
        reg.decHL();
    }

    private void ld_bcptr_a() {
        ld_rrptr_r_helper(reg::getBC, reg::getA);
    }

    private void ld_deptr_a() {
        ld_rrptr_r_helper(reg::getDE, reg::getA);
    }

    private void ld_hlptr_u8() {
        mmu.writeByte(reg.getHL(), fetch());
        cycles = 12;
    }

    private void ld_u16ptr_a() {
        int address = get_u16();
        mmu.writeByte(address, reg.getA());
        cycles = 16;
    }

    private void ld_a_u16ptr() {
        int address = get_u16();
        reg.setA(mmu.readByte(address));
        cycles = 16;
    }

    // TODO implement fully
    private void halt() {
        cycles = 4;
        halted = true;
    }

    // TODO implement fully
    private void di() {
        ime = false;
        cycles = 4;
    }

    // TODO implement fully
    private void ei() {
        ime = true;
        cycles = 4;
    }

    /* ld (HL), r */

    private void ld_hlptr_b() {
        ld_hlptr_r_helper(reg::getB);
    }

    private void ld_hlptr_c() {
        ld_hlptr_r_helper(reg::getC);
    }

    private void ld_hlptr_d() {
        ld_hlptr_r_helper(reg::getD);
    }

    private void ld_hlptr_e() {
        ld_hlptr_r_helper(reg::getE);
    }

    private void ld_hlptr_h() {
        ld_hlptr_r_helper(reg::getH);
    }

    private void ld_hlptr_l() {
        ld_hlptr_r_helper(reg::getL);
    }

    private void ld_hlptr_a() {
        ld_hlptr_r_helper(reg::getA);
    }

    /* ld A, r */

    private void ld_a_b() {
        ld_r_r_helper(reg::setA, reg::getB);
    }

    private void ld_a_c() {
        ld_r_r_helper(reg::setA, reg::getC);
    }

    private void ld_a_d() {
        ld_r_r_helper(reg::setA, reg::getD);
    }

    private void ld_a_e() {
        ld_r_r_helper(reg::setA, reg::getE);
    }

    private void ld_a_h() {
        ld_r_r_helper(reg::setA, reg::getH);
    }

    private void ld_a_l() {
        ld_r_r_helper(reg::setA, reg::getL);
    }

    private void ld_a_hlptr() {
        ld_r_hlptr_helper(reg::setA);
    }

    private void ld_a_a() {
        ld_r_r_helper(reg::setA, reg::getA);
    }
    /* ld H, r */

    private void ld_h_b() {
        ld_r_r_helper(reg::setH, reg::getB);
    }

    private void ld_h_c() {
        ld_r_r_helper(reg::setH, reg::getC);
    }

    private void ld_h_d() {
        ld_r_r_helper(reg::setH, reg::getD);
    }

    private void ld_h_e() {
        ld_r_r_helper(reg::setH, reg::getE);
    }

    private void ld_h_h() {
        ld_r_r_helper(reg::setH, reg::getH);
    }

    private void ld_h_l() {
        ld_r_r_helper(reg::setH, reg::getL);
    }

    private void ld_h_hlptr() {
        ld_r_hlptr_helper(reg::setH);
    }

    private void ld_h_a() {
        ld_r_r_helper(reg::setH, reg::getA);
    }

    /* ld L, r */

    private void ld_l_b() {
        ld_r_r_helper(reg::setL, reg::getB);
    }

    private void ld_l_c() {
        ld_r_r_helper(reg::setL, reg::getC);
    }

    private void ld_l_d() {
        ld_r_r_helper(reg::setL, reg::getD);
    }

    private void ld_l_e() {
        ld_r_r_helper(reg::setL, reg::getE);
    }

    private void ld_l_h() {
        ld_r_r_helper(reg::setL, reg::getH);
    }

    private void ld_l_l() {
        ld_r_r_helper(reg::setL, reg::getL);
    }

    private void ld_l_hlptr() {
        ld_r_hlptr_helper(reg::setL);
    }

    private void ld_l_a() {
        ld_r_r_helper(reg::setL, reg::getA);
    }

    /* ld E, r */

    private void ld_e_b() {
        ld_r_r_helper(reg::setE, reg::getB);
    }

    private void ld_e_c() {
        ld_r_r_helper(reg::setE, reg::getC);
    }

    private void ld_e_d() {
        ld_r_r_helper(reg::setE, reg::getD);
    }

    private void ld_e_e() {
        ld_r_r_helper(reg::setE, reg::getE);
    }

    private void ld_e_h() {
        ld_r_r_helper(reg::setE, reg::getH);
    }

    private void ld_e_l() {
        ld_r_r_helper(reg::setE, reg::getL);
    }

    private void ld_e_hlptr() {
        ld_r_hlptr_helper(reg::setE);
    }

    private void ld_e_a() {
        ld_r_r_helper(reg::setE, reg::getA);
    }
    /* ld D, r */

    private void ld_d_b() {
        ld_r_r_helper(reg::setD, reg::getB);
    }

    private void ld_d_c() {
        ld_r_r_helper(reg::setD, reg::getC);
    }

    private void ld_d_d() {
        ld_r_r_helper(reg::setD, reg::getD);
    }

    private void ld_d_e() {
        ld_r_r_helper(reg::setD, reg::getE);
    }

    private void ld_d_h() {
        ld_r_r_helper(reg::setD, reg::getH);
    }

    private void ld_d_l() {
        ld_r_r_helper(reg::setD, reg::getL);
    }

    private void ld_d_hlptr() {
        ld_r_hlptr_helper(reg::setD);
    }

    private void ld_d_a() {
        ld_r_r_helper(reg::setD, reg::getA);
    }

    /* ld C, r */

    private void ld_c_b() {
        ld_r_r_helper(reg::setC, reg::getB);
    }

    private void ld_c_c() {
        ld_r_r_helper(reg::setC, reg::getC);
    }

    private void ld_c_d() {
        ld_r_r_helper(reg::setC, reg::getD);
    }

    private void ld_c_e() {
        ld_r_r_helper(reg::setC, reg::getE);
    }

    private void ld_c_h() {
        ld_r_r_helper(reg::setC, reg::getH);
    }

    private void ld_c_l() {
        ld_r_r_helper(reg::setC, reg::getL);
    }

    private void ld_c_hlptr() {
        ld_r_hlptr_helper(reg::setC);
    }

    private void ld_c_a() {
        ld_r_r_helper(reg::setC, reg::getA);
    }

    /* ld B, r */

    private void ld_b_b() {
        ld_r_r_helper(reg::setB, reg::getB);
    }

    private void ld_b_c() {
        ld_r_r_helper(reg::setB, reg::getC);
    }

    private void ld_b_d() {
        ld_r_r_helper(reg::setB, reg::getD);
    }

    private void ld_b_e() {
        ld_r_r_helper(reg::setB, reg::getE);
    }

    private void ld_b_h() {
        ld_r_r_helper(reg::setB, reg::getH);
    }

    private void ld_b_l() {
        ld_r_r_helper(reg::setB, reg::getL);
    }

    private void ld_b_hlptr() {
        ld_r_hlptr_helper(reg::setB);
    }

    private void ld_b_a() {
        ld_r_r_helper(reg::setB, reg::getA);
    }

    private void ld_b_u8() {
        ld_u8_helper(reg::setB);
    }

    private void ld_c_u8() {
        ld_u8_helper(reg::setC);
    }

    private void ld_d_u8() {
        ld_u8_helper(reg::setD);
    }

    private void ld_e_u8() {
        ld_u8_helper(reg::setE);
    }

    private void ld_h_u8() {
        ld_u8_helper(reg::setH);
    }

    private void ld_l_u8() {
        ld_u8_helper(reg::setL);
    }

    private void ld_a_u8() {
        ld_u8_helper(reg::setA);
    }

    private void ld_u8_helper(IntConsumer setter) {
        setter.accept(fetch());
        cycles = 8;
    }

    private void call_u16() {
        int address = get_u16();

        reg.decSP();
        mmu.writeByte(reg.getSP(), (reg.getPC() >> 8) & 0xFF);
        reg.decSP();
        mmu.writeByte(reg.getSP(), (reg.getPC() & 0xFF));

        reg.setPC(address);
        cycles = 24;

    }

    private void ret() {
        int low = mmu.readByte(reg.getSP());
        reg.incSP();
        int high = mmu.readByte(reg.getSP());
        reg.incSP();

        reg.setPC(high << 8 | low);
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
        ret_flag_helper(!reg.isFlagZ());
    }

    private void ret_z() {
        ret_flag_helper(reg.isFlagZ());
    }

    private void ret_nc() {
        ret_flag_helper(!reg.isFlagC());
    }

    private void ret_c() {
        ret_flag_helper(reg.isFlagC());
    }

    private void jp_u16() {
        reg.setPC(get_u16());
        cycles = 16;
    }

    private void jr_s8() {
        int offset = (byte) fetch();
        reg.setPC(reg.getPC() + offset);
        cycles = 12;
    }

    private void jr_nz_s8() {
        jump_flag_s8_helper(!reg.isFlagZ());
    }

    private void jr_z_s8() {
        jump_flag_s8_helper(reg.isFlagZ());
    }

    private void jr_nc_s8() {
        jump_flag_s8_helper(!reg.isFlagC());
    }

    private void jr_c_s8() {
        jump_flag_s8_helper(reg.isFlagC());
    }

    private void jp_nz_u16() {
        jump_flag_u16_helper(!reg.isFlagZ());
    }

    private void jp_z_u16() {
        jump_flag_u16_helper(reg.isFlagZ());
    }

    private void jp_nc_u16() {
        jump_flag_u16_helper(!reg.isFlagC());
    }

    private void jp_c_u16() {
        jump_flag_u16_helper(reg.isFlagC());
    }
}
