package be.ugent.objprog.minionwars.views;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.Locale;
import java.util.ResourceBundle;

public class InfoTooltip extends StackPane {

    public InfoTooltip(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);
        // Create the info icon label
        Label infoIcon = new Label("i");
        infoIcon.setStyle("-fx-font-size: 14px; -fx-text-fill: white; " +
                "-fx-background-color: #2196F3; -fx-alignment: center; " +
                "-fx-min-width: 20px; -fx-min-height: 20px; " +
                "-fx-max-width: 20px; -fx-max-height: 20px; " +
                "-fx-background-radius: 10px;");
        // Create a tooltip
        Tooltip tooltip = new Tooltip(bundle.getString("infoTooltip.tooltip"));
        tooltip.setShowDelay(Duration.ZERO); // Show immediately
        tooltip.setHideDelay(Duration.seconds(2)); // Hide after 2s

        Tooltip.install(infoIcon, tooltip);

        getChildren().add(infoIcon);
    }
}
