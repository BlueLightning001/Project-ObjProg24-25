package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.models.PlayerModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Pos;

import java.util.Objects;

public class CurrentPlayerDisplay extends HBox {
    private final Label currentPlayerLabel;
    private final Label currentPlayerMinionsUsedLabel;
    private final PlayerModel playerModel;
    private final double fontScale = 0.1;

    public CurrentPlayerDisplay(PlayerModel playerModel) {
        this.playerModel = playerModel;

        currentPlayerLabel = new Label("Current Player");
        currentPlayerMinionsUsedLabel = new Label("currentPlayerCoins");

        getChildren().addAll(currentPlayerLabel, currentPlayerMinionsUsedLabel);

        // Set up MinionsIcon
        ImageView MinionsIcon = new ImageView(new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/minions-0073FF.png")
        )));
        currentPlayerMinionsUsedLabel.setGraphic(MinionsIcon);
        MinionsIcon.setFitHeight(10);
        MinionsIcon.setFitWidth(10);
        MinionsIcon.fitHeightProperty().bind(heightProperty().multiply(0.3));
        MinionsIcon.fitWidthProperty().bind(MinionsIcon.fitHeightProperty());

        // Bind the text properties for player name and minions.
        currentPlayerLabel.textProperty().bind(playerModel.currentPlayerProperty().get().nameProperty());
        currentPlayerMinionsUsedLabel.textProperty().bind(
                Bindings.createStringBinding(() -> {
                    Player currentPlayer = playerModel.getCurrentPlayer();
                    long activeMinions = currentPlayer.getMinions().stream()
                            .filter(Minion::hasActions)
                            .count();
                    int totalMinions = currentPlayer.getMinions().size();
                    return activeMinions + "/" + totalMinions;
                }, playerModel.getCurrentPlayer().getMinions())
        );

        // Handle player change.
        playerModel.currentPlayerProperty().addListener((obs, oldPlayer, newPlayer) -> {
            if (newPlayer != null) {
                currentPlayerLabel.textProperty().unbind();
                currentPlayerMinionsUsedLabel.textProperty().unbind();

                currentPlayerLabel.textProperty().bind(newPlayer.nameProperty());
                currentPlayerMinionsUsedLabel.textProperty().bind(
                        Bindings.createStringBinding(() -> {
                            long activeMinions = newPlayer.getMinions().stream()
                                    .filter(Minion::hasActions)
                                    .count();
                            int totalMinions = newPlayer.getMinions().size();
                            return activeMinions + "/" + totalMinions;
                        }, newPlayer.getMinions())
                );
            }
        });

        // Set alignments.
        currentPlayerLabel.setAlignment(Pos.CENTER);
        currentPlayerMinionsUsedLabel.setAlignment(Pos.CENTER_LEFT);

        // Let the labels take up available space.
        HBox.setHgrow(currentPlayerLabel, Priority.ALWAYS);
        HBox.setHgrow(currentPlayerMinionsUsedLabel, Priority.ALWAYS);
        currentPlayerLabel.prefWidthProperty().bind(widthProperty().multiply(0.7));
        currentPlayerMinionsUsedLabel.prefWidthProperty().bind(widthProperty().multiply(0.3));
        currentPlayerLabel.prefHeightProperty().bind(heightProperty());
        currentPlayerMinionsUsedLabel.prefHeightProperty().bind(heightProperty());
        setMaxWidth(Double.MAX_VALUE);
        setAlignment(Pos.CENTER_RIGHT);
        setSpacing(20);

        // Adjust font sizes based on the width of the HBox.
        widthProperty().addListener((obs, oldWidth, newWidth) -> {
            Platform.runLater(() -> {
                double newFontSize = newWidth.doubleValue() * fontScale;
                currentPlayerLabel.setStyle("-fx-font-size: " + newFontSize + "px;");
                currentPlayerMinionsUsedLabel.setStyle("-fx-font-size: " + (newFontSize * 0.8) + "px;");
            });
        });
    }
}

