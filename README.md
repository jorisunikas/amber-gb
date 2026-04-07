# Amber GB

Amber GB is a WIP Game Boy (DMG) emulator written in Java 21.

## Current Status

Working towards implementing the PPU (Pixel Processing Unit).

## Features

- CPU: fully implemented instruction set (512 opcodes), functioning interrupt system.
- PPU: functioning graphics system, able to draw image to display correctly.
- MMU: 64KB memory map, ROM loading from disk.
- Timer: fully implemented 4 registers, interrupt request upon overflow, integrated with CPU's interrupt system.
- Display: early-stage Swing window.

## Testing

The PPU produces an accurate image for [dmg-acid2.gb](https://github.com/mattcurrie/dmg-acid2) test.

All 11 individual [Blargg's CPU instruction](https://gbdev.gg8.se/wiki/articles/Test_ROMs) test ROMs pass.

| Test                    | Result |
|-------------------------|--------|
| dmg-acid2.gb            | Pass   |
| 01-special              | Pass   |
| 02-interrupts           | Pass   |
| 03-op sp,hl             | Pass   |
| 04-op r,imm             | Pass   |
| 05-op rp                | Pass   |
| 06-ld r,r               | Pass   |
| 07-jr,jp,call,ret,rst   | Pass   |
| 08-misc instrs          | Pass   |
| 09-op r,r               | Pass   |
| 10-bit ops              | Pass   |
| 11-op a,(hl)            | Pass   |


## Getting Started

**Requirements**: Java 21

The emulator can be built with [Gradle](https://gradle.org/):

```bash
./gradlew run
```

Or, on Windows:

```cmd
gradlew.bat run
```

> [!NOTE]
> At this point, user-friendly ROM loading is not yet implemented, ROM's location must be hard coded in the Main.java class.

### Building a JAR

The project can be compiled into a standalone JAR using the [Shadow](https://plugins.gradle.org/plugin/com.gradleup.shadow) plugin:

```bash
./gradlew shadowJar
```

The output `amber-gb.jar` will be located in app/build/libs directory.

## Resources

- [Pan Docs](https://gbdev.io/pandocs/)
- [The Ultimate Game Boy talk](https://www.youtube.com/watch?v=HyzD8pNlpwI)
- [Game Boy opcodes](https://meganesu.github.io/generate-gb-opcodes/)
- [Game Boy's PPU test](https://github.com/mattcurrie/dmg-acid2?tab=readme-ov-file)
