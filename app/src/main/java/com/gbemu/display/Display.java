package com.gbemu.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Display extends JPanel {
    private final static int GB_WIDTH = 160;
    private final static int GB_HEIGHT = 144;
    private final static int SCALE = 4;

    private BufferedImage screen;

    public Display() {
        this.screen = new BufferedImage(GB_WIDTH * SCALE, GB_HEIGHT * SCALE, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(GB_WIDTH * SCALE, GB_HEIGHT * SCALE));
        setBackground(Color.ORANGE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(screen, 0, 0, null);
    }

    public void render(int[] frameBuffer) {
        for (int i = 0; i < GB_HEIGHT; i++) {
            for (int j = 0; j < GB_WIDTH; j++) {
                int color = frameBuffer[j + i * GB_WIDTH];
                for (int si = 0; si < SCALE; si++) {
                    for (int sj = 0; sj < SCALE; sj++) {
                        screen.setRGB(j * SCALE + sj, i * SCALE + si, color);
                    }
                }
            }
        }
        repaint();
    }
}
