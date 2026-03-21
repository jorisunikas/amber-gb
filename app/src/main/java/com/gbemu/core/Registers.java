package com.gbemu.core;

public class Registers {
    private int a, b, c, d, e, h, l;
    private int sp, pc; // stack pointer, program counter
    private boolean flagZ, // zero flag
            flagN, // subtraction flag
            flagH, // half carry flag
            flagC; // carry flag

    public Registers() {
        this.a = 0;
        this.b = 0;
        this.c = 0;
        this.d = 0;
        this.e = 0;
        this.h = 0;
        this.l = 0;
        this.sp = 0;
        this.pc = 0;
        this.flagZ = false;
        this.flagN = false;
        this.flagH = false;
        this.flagC = false;
    }

    public int getA() {
        return a;
    }

    public void setA(int value) {
        a = value & 0xFF;
    }

    public int getB() {
        return b;
    }

    public void setB(int value) {
        b = value & 0xFF;
    }

    public int getC() {
        return c;
    }

    public void setC(int value) {
        c = value & 0xFF;
    }

    public int getD() {
        return d;
    }

    public void setD(int value) {
        d = value & 0xFF;
    }

    public int getE() {
        return e;
    }

    public void setE(int value) {
        e = value & 0xFF;
    }

    public int getH() {
        return h;
    }

    public void setH(int value) {
        h = value & 0xFF;
    }

    public int getL() {
        return l;
    }

    public void setL(int value) {
        l = value & 0xFF;
    }

    public boolean isFlagZ() {
        return flagZ;
    }

    public void setFlagZ(boolean value) {
        flagZ = value;
    }

    public boolean isFlagN() {
        return flagN;
    }

    public void setFlagN(boolean value) {
        flagN = value;
    }

    public boolean isFlagH() {
        return flagH;
    }

    public void setFlagH(boolean value) {
        flagH = value;
    }

    public boolean isFlagC() {
        return flagC;
    }

    public void setFlagC(boolean value) {
        flagC = value;
    }

    public int getF() {
        return ((flagZ ? 0x80 : 0) | (flagN ? 0x40 : 0) | (flagH ? 0x20 : 0) | (flagC ? 0x10 : 0));
    }

    public void setF(int value) {
        flagZ = (0x80 & value) != 0;
        flagN = (0x40 & value) != 0;
        flagH = (0x20 & value) != 0;
        flagC = (0x10 & value) != 0;
    }

    public int getAF() {
        return (a << 8) | getF();
    }

    public void setAF(int value) {
        // right shift ir tada AND
        a = (value >> 8) & 0xFF;
        setF(value & 0xFF);
    }

    public int getBC() {
        return (b << 8) | c;
    }

    public void setBC(int value) {
        b = (value >> 8) & 0xFF;
        c = value & 0xFF;
    }

    public int getDE() {
        return (d << 8) | e;
    }

    public void setDE(int value) {
        d = (value >> 8) & 0xFF;
        e = value & 0xFF;
    }

    public int getHL() {
        return (h << 8) | l;
    }

    public void setHL(int value) {
        h = (value >> 8) & 0xFF;
        l = value & 0xFF;
    }

    public int getSP() {
        return sp;
    }

    public void setSP(int value) {
        sp = value & 0xFFFF;
    }

    public int getPC() {
        return pc;
    }

    public void setPC(int value) {
        pc = value & 0xFFFF;
    }

    public void incPC() {
        ++pc;
    }

    public void decPC() {
        --pc;
    }

    public void incSP() {
        ++sp;
    }

    public void decSP() {
        --sp;
    }
}
