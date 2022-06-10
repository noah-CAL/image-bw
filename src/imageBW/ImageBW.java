package imageBW;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;


public class ImageBW {
    public static void main(String[] args) {
        File file = getUserJPG();
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageUtils.colorAndDisplay(image, ImageUtils.IDENTITY);
        ImageUtils.colorAndDisplay(image, ImageUtils.BLACK_AND_WHITE);
    }

    /** Utility method to prompt the user for input via a visual file selection window.
     * Returns a JPG or JPEG FILE object. */
    private static File getUserJPG() {
        File file = null;
        System.out.println("Please select an image.");
        while (file == null || !isValidImageFile(file)) {
            if (file != null) {
                System.out.println("Select a proper image file!");
            }
            JFrame window = new JFrame("File Selection Window");
            JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(window);
            file = fc.getSelectedFile();
        }
        return file;
    }

    /** Returns TRUE if FILE is not a directory and has a file extension of JPG or JPEG. */
    private static boolean isValidImageFile(File file) {
        String name = file.getName();
        if (!file.exists() || !file.isFile() || !name.contains(".")) {
            return false;
        }
        HashSet<String> allowedExtensions = new HashSet<>();
        {
            allowedExtensions.add("jpg");
            allowedExtensions.add("jpeg");
        }
        String ext = getFileExtension(name);
        return allowedExtensions.contains(ext);
    }

    /** Returns a String with the contents following the rightmost "." in the name of FILE.
     * Returns the full file name if there is no "." (no extension). */
    private static String getFileExtension(String filename) {
        char[] chars = filename.toCharArray();
        String result = "";
        for (int i = chars.length - 1; i >= 0; i -= 1) {
            String next = String.valueOf(chars[i]);
            if (next.equals(".")) return result;
            result = "%s%s".formatted(next, result);
        }
        return result;
    }
}
