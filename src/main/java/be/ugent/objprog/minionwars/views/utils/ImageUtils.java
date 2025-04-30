package be.ugent.objprog.minionwars.views.utils;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Utility class for image operations.
 */
public class ImageUtils {
    
    /**
     * Applies a color overlay to an image using a Canvas and returns the modified image.
     * 
     * @param baseImage The original image to apply the overlay to
     * @param overlayColor The color to use for the overlay
     * @param opacity The opacity of the overlay (0.0 to 1.0)
     * @return A new image with the color overlay applied
     */
    public static Image applyColorOverlay(Image baseImage, Color overlayColor, double opacity) {
        int width = (int) baseImage.getWidth();
        int height = (int) baseImage.getHeight();

        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw base image
        gc.drawImage(baseImage, 0, 0, width, height);

        // Apply overlay
        gc.setFill(new Color(overlayColor.getRed(), overlayColor.getGreen(), overlayColor.getBlue(), opacity));
        gc.fillRect(0, 0, width, height);

        WritableImage blendedImage = new WritableImage(width, height);
        canvas.snapshot(null, blendedImage);
        return blendedImage;
    }
    
    /**
     * Applies a color overlay to an image with default opacity of 0.3.
     * 
     * @param baseImage The original image to apply the overlay to
     * @param overlayColor The color to use for the overlay
     * @return A new image with the color overlay applied
     */
    public static Image applyColorOverlay(Image baseImage, Color overlayColor) {
        return applyColorOverlay(baseImage, overlayColor, 0.3);
    }

}