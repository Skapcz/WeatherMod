package com.github.WeatherMod.common.noise;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.Color.gray;

public class NoiseViewer {
    private final JFrame frame;
    private final JLabel label;
    private final int scale;
    private final int width;
    private final int height;

    private final BufferedImage image;

    public NoiseViewer(int width, int height, int scale) {
        this.width = width;
        this.height = height;
        this.scale = scale;

        // vytvoření okna
        frame = new JFrame("Noise");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        label = new JLabel(new ImageIcon(image.getScaledInstance(width * scale, height * scale, Image.SCALE_FAST)));

        frame.add(label);
        frame.pack();
        frame.setVisible(true);
    }

    // metoda pro překreslení obrázku
    public void update(float[][] data) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float value = data[x][y];
                value = Math.max(0, Math.min(1, value)); // omezit
                int gray = (int) (value * 255);
                int color = new Color(gray, gray, gray).getRGB();
                image.setRGB(x, y, color);
            }
        }
        // aktualizuje label
        label.setIcon(new ImageIcon(image.getScaledInstance(width * scale, height * scale, Image.SCALE_FAST)));
        label.repaint();
    }
}
