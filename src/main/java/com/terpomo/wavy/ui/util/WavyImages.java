package com.terpomo.wavy.ui.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class WavyImages {

    private static WavyImages instance;
    public final BufferedImage SWEEP_IMAGE;
    public final BufferedImage CLOSE_IMAGE;

    public static WavyImages getInstance() {
        if (instance == null)
            instance = new WavyImages();
        return instance;
    }

    private WavyImages() {
        try {
            this.CLOSE_IMAGE = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("img/close_12.png"));
            this.SWEEP_IMAGE = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("img/sweep_12.png"));
        } catch (IOException e) {
            throw new RuntimeException("Impossible to read image.");
        }
    }
}
