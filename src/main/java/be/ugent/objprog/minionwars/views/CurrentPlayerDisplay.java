package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.models.PlayerModel;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CurrentPlayerDisplay extends HBox {
    private final PlayerModel playerModel;
    private final Label currentPlayerLabel;
    private final Label currentPlayerMinionsUsedLabel;
    private final Map<Player, List<Runnable>> currentListenerMap = new HashMap<>();
    private Player currentBoundPlayer = null;
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
                    long usedMinions = currentPlayer.getMinions().stream()
                            .filter(minion -> !minion.hasActions())
                            .count();

                    int totalMinions = currentPlayer.getMinions().size();
                    return usedMinions + "/" + totalMinions;
                }, playerModel.getCurrentPlayer().getMinions())
        );

        // Handle player change.
        playerModel.currentPlayerProperty().addListener((obs, oldPlayer, newPlayer) -> {
            if (newPlayer != null) {
                currentPlayerLabel.textProperty().unbind();
                currentPlayerMinionsUsedLabel.textProperty().unbind();

                currentPlayerLabel.textProperty().bind(newPlayer.nameProperty());
                bindMinionCount(newPlayer); // Add listener to update when minion actions change
            }
        });

        // Initial binding
        if (playerModel.getCurrentPlayer() != null) {
            bindMinionCount(playerModel.getCurrentPlayer());
        }

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
        widthProperty().addListener((obs) -> {
            Platform.runLater(() -> {
                double newFontSize = getWidth() * fontScale;
                currentPlayerLabel.setStyle("-fx-font-size: " + newFontSize + "px;");
                currentPlayerMinionsUsedLabel.setStyle("-fx-font-size: " + (newFontSize * 0.8) + "px;");
            });
        });
    }

    // Method to bind minion count label and listen for updates
    private void bindMinionCount(Player player) {
        currentPlayerMinionsUsedLabel.textProperty().unbind();

        // Clean up listeners for the previous player
        if (currentListenerMap.containsKey(currentBoundPlayer)) {
            currentListenerMap.get(currentBoundPlayer).forEach(Runnable::run);
        }

        List<Runnable> detachers = new ArrayList<>();
        InvalidationListener updateListener = obs -> updateMinionActionCount(player);

        // Add listeners to each minion's attacked and moved properties
        for (Minion minion : player.getMinions()) {
            minion.attackedProperty().addListener(updateListener);
            minion.movedProperty().addListener(updateListener);
            detachers.add(() -> minion.attackedProperty().removeListener(updateListener));
            detachers.add(() -> minion.movedProperty().removeListener(updateListener));
        }

        // Also react to changes in the minion list (e.g. new minions added)
        ListChangeListener<Minion> minionListChangeListener = change -> bindMinionCount(player);
        player.getMinions().addListener(minionListChangeListener);
        detachers.add(() -> player.getMinions().removeListener(minionListChangeListener));

        currentBoundPlayer = player;
        currentListenerMap.put(player, detachers);

        updateMinionActionCount(player);
    }


    private void updateMinionActionCount(Player player) {
        Platform.runLater(() -> {
            currentPlayerMinionsUsedLabel.setText(getMinionActionText(player));
        });
    }



    // Produces the used/total text for the label
    private String getMinionActionText(Player player) {
        long usedMinions = player.getMinions().stream()
                .filter(minion -> !minion.hasActions())
                .count();
        int totalMinions = player.getMinions().size();
        return usedMinions + "/" + totalMinions;
    }
}

