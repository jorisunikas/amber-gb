# Amber GB

Amber GB is a WIP Game Boy (DMG) emulator written in Java 21.

## Current Status

Working towards the first milestone: passing [Blargg's test ROMs](https://gbdev.gg8.se/wiki/articles/Test_ROMs).

| Metric     | Count     |
|------------|-----------|
| Opcodes    | 154 / 512 |
| Unit tests | 269       |

Unit tests are written manually for each opcode. This is because basic CPU functionality is required to boot the ROMs.

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
> At this point, running the emulator only opens a Swing window with an orange background.

### Building a JAR

The project can be compiled into a standalone JAR using the [Shadow](https://plugins.gradle.org/plugin/com.gradleup.shadow) plugin:

```bash
./gradlew shadowJar
```

The output `amber-gb.jar` will be located in app/build/libs directory.

## Features

- CPU: 154 of 512 opcodes implemented, each covered by unit tests.
- MMU: 64KB memory map, ROM loading from disk, Serial port callback for Blargg's test output.
- Display: early-stage Swing window.
## Resources

- [Pan Docs](https://gbdev.io/pandocs/)
- [The Ultimate Game Boy talk](https://www.youtube.com/watch?v=HyzD8pNlpwI)
- [Game Boy opcodes](https://meganesu.github.io/generate-gb-opcodes/)
