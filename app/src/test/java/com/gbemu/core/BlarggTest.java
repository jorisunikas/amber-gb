package com.gbemu.core;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

public class BlarggTest {
    private static final int MAX_STEPS = 50_000_000;
    private static final Path ROM_DIR = Path.of("src/test/resources");

    private String runRom(String filename) throws IOException {
        Path path = ROM_DIR.resolve(filename);

        Assumptions.assumeTrue(
                java.nio.file.Files.exists(path),
                "ROM not found, skipping: " + filename);

        MMU mmu = new MMU();
        CPU cpu = new CPU(mmu);
        mmu.loadROM(path);

        for (int i = 0; i < MAX_STEPS; i++) {
            cpu.step();
            String out = mmu.getSerialOutput();
            if (out.contains("Passed") || out.contains("Failed"))
                break;

        }
        return mmu.getSerialOutput();
    }

    @Test
    void test_01_special() throws IOException {
        assertThat(runRom("01-special.gb")).contains("Passed");
    }
}
