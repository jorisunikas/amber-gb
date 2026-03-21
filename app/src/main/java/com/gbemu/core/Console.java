package com.gbemu.core;

import com.gbemu.display.Display;

public class Console {
    private Display display;
    private CPU cpu;
    private MMU mmu;

    public Console() {
        this.display = new Display();
        this.mmu = new MMU();
        this.cpu = new CPU(this.mmu);
    }

    public Display getDisplay() {
        return this.display;
    }

    public void run(){
        while (true) {
            int cycles = cpu.step();
        }
    }
}
