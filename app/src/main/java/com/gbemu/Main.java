package com.gbemu;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import com.gbemu.core.Console;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Console console = new Console();

            JFrame frame = new JFrame("GameBoy Emulator");
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            frame.add(console.getDisplay());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
