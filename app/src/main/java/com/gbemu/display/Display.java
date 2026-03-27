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
        g.drawImage(screen, getWidth(), getHeight(), null);
    }

    public void setPixel(int x, int y, int color) {
        screen.setRGB(x, y, color);
    }

    public void refresh() {
        repaint();
    }

}
