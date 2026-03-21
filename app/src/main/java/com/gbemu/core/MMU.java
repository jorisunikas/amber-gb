package com.gbemu.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class MMU {
    private final int[] memory;
    private Consumer<Integer> serialCallback;

    public MMU() {
        memory = new int[0x10000];
        serialCallback = null;
    }

    public int readByte(int adress) {
        return memory[adress & 0xFFFF];
    }

    public void writeByte(int adress, int value) {
        adress = adress & 0xFFFF;
        value = value & 0xFF;

        checkSerialCallback(adress, value);

        memory[adress] = value;
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

    public void setSerialCallback(Consumer<Integer> callback) {
        this.serialCallback = callback;
    }

    private void checkSerialCallback(int adress, int value) {
        if (adress == 0xFF02 && value == 0x81 && serialCallback != null)
            serialCallback.accept(memory[0xFF01]);

    }
}
