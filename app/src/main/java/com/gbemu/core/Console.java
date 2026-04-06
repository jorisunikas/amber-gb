package com.gbemu.core;

import com.gbemu.display.Display;
import com.gbemu.core.timer.Timer;

import java.nio.file.Path;

import com.gbemu.core.cpu.CPU;
import com.gbemu.core.graphics.PPU;
import com.gbemu.core.memory.MMU;

public class Console {
    private Display display;
    private CPU cpu;
    private MMU mmu;
    private Timer timer;
    private PPU ppu;

    private static final long FRAME_DURATION_NS = 16_742_706L;

    public Console() {
        this.display = new Display();
        this.mmu = new MMU();
        this.cpu = new CPU(this.mmu);
        this.timer = new Timer(this.mmu);
        this.ppu = new PPU(mmu);
        mmu.loadROM(Path.of("src/main/resources/dmg-acid2.gb"));
    }

    public Display getDisplay() {
        return this.display;
    }

    public void run() {
        long nextFrameTime = System.nanoTime();

        while (true) {
            int cyclesThisFrame = 0;
            while (cyclesThisFrame < 70224) {
                int cycles = cpu.step();
                timer.step(cycles);
                ppu.step(cycles);
                cyclesThisFrame += cycles;
            }

            display.render(ppu.getFramebuffer());

            nextFrameTime += FRAME_DURATION_NS;
            long sleepNs = nextFrameTime - System.nanoTime();
            if (sleepNs > 0) {
                try {
                    Thread.sleep(sleepNs / 1_000_000, (int) (sleepNs % 1_000_000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
