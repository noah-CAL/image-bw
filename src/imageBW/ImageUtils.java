package imageBW;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {
    public static void main(String[] args) {
        colorAndDisplay(1023, 511, BLACK_AND_WHITE);
        colorAndDisplay(1023, 511, BW_GRID);
        colorAndDisplay(1023, 511, BASIC_SIERPINSKI);
        colorAndDisplay(1023, 511, SIERPINSKI_NOISE_A);
        colorAndDisplay(1023, 511, SIERPINSKI_NOISE_B);
        colorAndDisplay(1023, 511, COLOR_CURVES);
    }

    /** Destructively recolors every pixel in IMG with a COLORER function. */
    private static void colorImage(BufferedImage img, PixelColorer colorer) {
        for (int i = 0; i < img.getWidth(); i += 1) {
            for (int j = 0; j < img.getHeight(); j += 1) {
                img.setRGB(i, j, colorer.recolor(img, i, j));
            }
        }
    }

    /** Creates a blank image with a default coloring and then displays the result of the COLORER recolering. */
    public static void colorAndDisplay(int width, int height, PixelColorer colorer) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        colorImage(img, ((img1, x, y) -> x + y));
        colorAndDisplay(img, colorer);
    }

    /** Destructively recolors every pixel in IMG with a COLORER function and then displays the resulting image. */
    public static void colorAndDisplay(BufferedImage img, PixelColorer colorer) {
        colorImage(img, colorer);
        JFrame frame = new JFrame("Image");
        ImageIcon icon = new ImageIcon(img);
        frame.add(new JLabel(icon));
        frame.pack();
        frame.setVisible(true);
    }

    public static interface PixelColorer {
        /** Returns an RGB value describing the recoloring of the pixel. */
        public int recolor(BufferedImage img, int x, int y);
    }

    static PixelColorer BLACK_AND_WHITE = ((img, x, y) -> {
        int RGB = img.getRGB(x, y);
        float[] HSB = Color.RGBtoHSB(
                (RGB >> 16) & 0xFF,
                (RGB >> 8) & 0xFF,
                RGB & 0xFF,
                null
        );
        float hue = HSB[0],
                sat = HSB[1],
                bright = HSB[2];
        return Color.HSBtoRGB(0, 0, bright);
    });
    static PixelColorer BW_GRID = ((img, x, y) -> {
        return ((x ^ y) << 16) + ((x ^ y) << 8) + (x ^ y);
    });

    static PixelColorer BASIC_SIERPINSKI = ((img, x, y) -> {
        return ((x | y) << 16) + ((x | y) << 8) + (x | y);
    });

    static PixelColorer SIERPINSKI_NOISE_A = ((img, x, y) -> {
        return ((x | y) << 16) + ((x | y) << 8) + (x * y);
    });

    static PixelColorer SIERPINSKI_NOISE_B = ((img, x, y) -> {
        return ((x | y) << 16) + ((x | y) << 8) + ((x * y) | y);
    });

    static PixelColorer COLOR_CURVES = ((img, x, y) -> {
        return (y << 16) + (x << 8) + (x * y);
    });

    static PixelColorer IDENTITY = ((img, x, y) -> img.getRGB(x, y));
}
