package com.gbemu.core.memory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class MMU {
    private final int[] memory;
    private Consumer<Integer> serialCallback;
    private StringBuilder serialOutput;

    public MMU() {
        memory = new int[0x10000];
        serialOutput = new StringBuilder();
        serialCallback = null;
    }

    public int readByte(int address) {
        return memory[address & 0xFFFF];
    }

    public void writeByte(int address, int value) {
        address = address & 0xFFFF;
        value = value & 0xFF;

        checkSerialCallback(address, value);

        /* reset DIV register */
        if(address == 0xFF04){
            memory[address] = 0;
            return;
        }

        memory[address] = value;
    }

    public void loadROM(Path path) {
        byte[] rom = null;
        try {
            rom = Files.readAllBytes(path);
        } catch (Exception e) {
            System.out.println(e.toString());
            return;
        }
        for (int i = 0; i < Math.min(rom.length, 0x8000); i++) {
            memory[i] = rom[i] & 0xFF;
        }
    }

    public void setSerialCallback(Consumer<Integer> callback) {
        this.serialCallback = callback;
    }

    private void checkSerialCallback(int address, int value) {
        if (address == 0xFF02 && value == 0x81) {
            serialOutput.append((char) memory[0xFF01]);
            if (serialCallback != null) {
                serialCallback.accept(memory[0xFF01]);
            }
        }
    }

    public String getSerialOutput() {
        return serialOutput.toString();
    }
}
