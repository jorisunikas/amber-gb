package com.gbemu.core;

import com.gbemu.display.Display;
import com.gbemu.core.timer.Timer;

public class Console {
    private Display display;
    private CPU cpu;
    private MMU mmu;
    private Timer timer;

    public Console() {
        this.display = new Display();
        this.mmu = new MMU();
        this.cpu = new CPU(this.mmu);
        this.timer = new Timer(this.mmu);
    }

    public Display getDisplay() {
        return this.display;
    }

    public void run(){
        while (true) {
            int cycles = cpu.step();
            timer.step(cycles);
        }
    }
}
