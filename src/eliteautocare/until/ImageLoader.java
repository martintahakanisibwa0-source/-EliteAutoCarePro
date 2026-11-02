package eliteautocare.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageLoader {
    // Uses an absolute path so NetBeans can never lose the images
    private static final String IMAGE_DIR = "C:\\Users\\Admin\\Documents\\NetBeansProjects\\EliteAutoCarePro\\images\\";

    public static ImageIcon load(String fileName, int width, int height) {
        try {
            File file = new File(IMAGE_DIR + fileName);
            if (!file.exists()) {
                System.err.println("⚠ Image not found: " + file.getAbsolutePath());
                return new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
            }
            ImageIcon original = new ImageIcon(file.getAbsolutePath());
            if (width <= 0 || height <= 0) return original;
            Image scaled = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            System.err.println("⚠ Failed to load " + fileName + ": " + e.getMessage());
            return new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
        }
    }

    public static Image loadRaw(String fileName) {
        try {
            File file = new File(IMAGE_DIR + fileName);
            if (!file.exists()) {
                System.err.println("⚠ Image not found: " + file.getAbsolutePath());
                return null;
            }
            return new ImageIcon(file.getAbsolutePath()).getImage();
        } catch (Exception e) { return null; }
    }
}