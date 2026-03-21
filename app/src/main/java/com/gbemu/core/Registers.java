package com.gbemu.core;

public class Registers {
    public int a, b, c, d, e, h, l;
    public int sp, pc; // stack pointer, program counter
    public boolean flagZ, // zero flag
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

    public int getF() {
        return ((flagZ ? 0x80 : 0) | (flagN ? 0x40 : 0) | (flagH ? 0x20 : 0) | (flagC ? 0x10 : 0));
    }

    public void setF(int value){
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

    public int getBC(){
        return (b << 8) | c;
    }
    
    public void setBC(int value){
        b = (value >> 8) & 0xFF;
        c = value & 0xFF;
    }

    public int getDE(){
        return (d << 8) | e;
    }
    
    public void setDE(int value){
        d = (value >> 8) & 0xFF;
        e = value & 0xFF;
    }

    public int getHL(){
        return (h << 8) | l;
    }
    
    public void setHL(int value){
        h = (value >> 8) & 0xFF;
        l = value & 0xFF;
    }
}
