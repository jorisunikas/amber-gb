package com.gbemu.core;

import com.gbemu.display.Display;

public class Console {
    private Display display;

    public Console() {
        this.display = new Display();
    }

    public Display getDisplay() {
        return this.display;
    }

}
