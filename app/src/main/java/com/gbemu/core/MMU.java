package com.gbemu.core;

import java.nio.file.Files;
import java.nio.file.Path;

public class MMU {
    private final int[] memory;

    public MMU() {
        memory = new int[0x10000];
    }

    public int readByte(int adress) {
        return memory[adress & 0xFFFF];
    }

    public void writeByte(int adress, int value) {
        memory[adress & 0xFFFF] = (value & 0xFF);
    }

    public void loadROM(String path) {
        byte[] rom = null;
        try {
             rom = Files.readAllBytes(Path.of(path));
        } catch (Exception e) {
            System.out.println(e.toString());
            return;
        }
        for (int i = 0; i < Math.min(rom.length, 0x8000); i++) {
            memory[i] = rom[i];
        }
    }
}
