package com.gbemu.core;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import com.gbemu.core.cpu.CPU;
import com.gbemu.core.memory.MMU;
import com.gbemu.core.timer.Timer;

public class BlarggTest {
    private static final long MAX_STEPS = 500_000_000;
    private static final Path ROM_DIR = Path.of("src/test/resources");

    private String runRom(String filename) throws IOException {
        Path path = ROM_DIR.resolve(filename);

        Assumptions.assumeTrue(
                java.nio.file.Files.exists(path),
                "ROM not found, skipping: " + filename);

        MMU mmu = new MMU();
        Timer timer = new Timer(mmu);
        CPU cpu = new CPU(mmu);
        mmu.loadROM(path);

        for (int i = 0; i < MAX_STEPS; i++) {
            int cycles = cpu.step();
            timer.step(cycles);
            String out = mmu.getSerialOutput();
            if (out.contains("Passed") || out.contains("Failed"))
                break;
            if (i % 10_000_000 == 0)
                System.out.print(mmu.getSerialOutput());

        }
        return mmu.getSerialOutput();
    }

    @Test
    void test_01() throws IOException {
        assertThat(runRom("01-special.gb")).contains("Passed");
    }

    @Test
    void test_02() throws IOException {
        assertThat(runRom("02-interrupts.gb")).contains("Passed");
    }

    @Test
    void test_03() throws IOException {
        assertThat(runRom("03-op-sp-hl.gb")).contains("Passed");
    }

    @Test
    void test_04() throws IOException {
        assertThat(runRom("04-op-r-imm.gb")).contains("Passed");
    }

    @Test
    void test_05() throws IOException {
        assertThat(runRom("05-op-rb.gb")).contains("Passed");
    }

    @Test
    void test_06() throws IOException {
        assertThat(runRom("06-ld-r-r.gb")).contains("Passed");
    }

    @Test
    void test_07() throws IOException {
        assertThat(runRom("07-jr-jp-call-ret-rst.gb")).contains("Passed");
    }

    @Test
    void test_08() throws IOException {
        assertThat(runRom("08-misc.gb")).contains("Passed");
    }

    @Test
    void test_09() throws IOException {
        assertThat(runRom("09-op-r-r.gb")).contains("Passed");
    }

    @Test
    void test_10() throws IOException {
        assertThat(runRom("10-bit-ops.gb")).contains("Passed");
    }

    @Test
    void test_11() throws IOException {
        assertThat(runRom("11-op-a-hl.gb")).contains("Passed");
    }
}
