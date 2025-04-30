package be.ugent.objprog.minionwars.views.utils;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Labeled;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for styling UI components.
 */
public class UIStyleUtils {
    // Map to store listeners for cleanup
    private static final Map<Labeled, List<ChangeListener<Number>>> labelListeners = new HashMap<>();

    /**
     * Styles a labeled node with responsive font sizing based on container dimensions.
     *
     * @param toBeStyled The labeled node to style
     * @param container The container that will determine the size
     * @param width The width multiplier (relative to container width)
     * @param height The height multiplier (relative to container height)
     */
    public static void styleNode(Labeled toBeStyled, StackPane container, double width, double height) {
        toBeStyled.setAlignment(Pos.CENTER);
        toBeStyled.prefWidthProperty().bind(container.widthProperty().multiply(width));
        toBeStyled.prefHeightProperty().bind(container.heightProperty().multiply(height));

        ChangeListener<Number> widthListener = (obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                double fontSize = toBeStyled.getWidth() * 0.1;
                toBeStyled.setStyle("-fx-font-size: " + fontSize + "px;");
            });
        };

        toBeStyled.widthProperty().addListener(widthListener);

        // Store listener for later cleanup
        labelListeners.computeIfAbsent(toBeStyled, k -> new ArrayList<>()).add(widthListener);
    }

    /**
     * Automatically resizes text based on the component's dimensions.
     *
     * @param label The label to resize
     * @param scaleFactor The scale factor to apply to the font size
     */
    public static void autoResizeText(Labeled label, double scaleFactor) {
        ChangeListener<Number> resizeListener = (obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                double fontSize = Math.min(label.getWidth(), label.getHeight()) * scaleFactor;
                label.setStyle("-fx-font-size: " + fontSize + "px;");
            });
        };

        // Listen for both width and height changes
        label.widthProperty().addListener(resizeListener);
        label.heightProperty().addListener(resizeListener);

        // Store listeners for later cleanup
        labelListeners.computeIfAbsent(label, k -> new ArrayList<>()).add(resizeListener);
    }

    /**
     * Removes all listeners to prevent memory leaks.
     * Should be called when components are no longer needed.
     */
    public static void cleanup() {
        // Remove all stored listeners
        labelListeners.forEach((label, listeners) -> {
            for (ChangeListener<Number> listener : listeners) {
                label.widthProperty().removeListener(listener);
                label.heightProperty().removeListener(listener);
            }
        });
        labelListeners.clear();
    }

    /**
     * Removes listeners for a specific labeled component.
     *
     * @param label The labeled component to clean up
     */
    public static void cleanup(Labeled label) {
        List<ChangeListener<Number>> listeners = labelListeners.get(label);
        if (listeners != null) {
            for (ChangeListener<Number> listener : listeners) {
                label.widthProperty().removeListener(listener);
                label.heightProperty().removeListener(listener);
            }
            labelListeners.remove(label);
        }
    }
}