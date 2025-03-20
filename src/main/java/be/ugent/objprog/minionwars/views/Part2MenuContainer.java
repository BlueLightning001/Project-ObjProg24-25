package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.models.MinionModel;
import be.ugent.objprog.minionwars.models.PlayerModel;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.Objects;

public class Part2MenuContainer extends HBox {
    private MinionModel minionModel;
    private PlayerModel playerModel;
    private TileGroupPane tileGroupPane;
    private HBox currentPlayerHBox;
    private Label currentPlayerLabel;
    private Label currentPlayerMinionsUsedLabel;


    public Part2MenuContainer(PlayerModel playerModel, MinionModel minionModel, TileGroupPane tileGroupPane) {
        this.playerModel = playerModel;
        this.minionModel = minionModel;
        this.tileGroupPane = tileGroupPane;

        // Create and configure the player information elements.
        currentPlayerLabel = new Label("Current Player");
        currentPlayerMinionsUsedLabel = new Label("currentPlayerCoins");
        currentPlayerHBox = new HBox();
        currentPlayerHBox.getChildren().addAll(currentPlayerLabel, currentPlayerMinionsUsedLabel);

        double fontScale = 0.1;

        // Set up MinionsIcon
        ImageView MinionsIcon = new ImageView(new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/minions-0073FF.png")
        )));
        currentPlayerMinionsUsedLabel.setGraphic(MinionsIcon);
        MinionsIcon.setFitHeight(10);
        MinionsIcon.setFitWidth(10);
        MinionsIcon.fitHeightProperty().bind(currentPlayerHBox.heightProperty().multiply(0.3));
        MinionsIcon.fitWidthProperty().bind(MinionsIcon.fitHeightProperty());

        // Bind the text properties for player name and money.
        currentPlayerLabel.textProperty().bind(playerModel.currentPlayerProperty().get().nameProperty());
        currentPlayerMinionsUsedLabel.textProperty().bind(playerModel.currentPlayerProperty().get().moneyProperty().asString());
        playerModel.currentPlayerProperty().addListener((obs, oldPlayer, newPlayer) -> {
            if (newPlayer != null) {
                currentPlayerLabel.textProperty().bind(newPlayer.nameProperty());
                currentPlayerLabel.textProperty().bind(newPlayer.moneyProperty().asString());
            }
        });




        // Set alignments.
        currentPlayerLabel.setAlignment(Pos.CENTER);
        currentPlayerMinionsUsedLabel.setAlignment(Pos.CENTER_LEFT);

        // Let the labels take up available space.
        HBox.setHgrow(currentPlayerLabel, Priority.ALWAYS);
        HBox.setHgrow(currentPlayerMinionsUsedLabel, Priority.ALWAYS);
        currentPlayerLabel.prefWidthProperty().bind(currentPlayerHBox.widthProperty().multiply(0.7));
        currentPlayerMinionsUsedLabel.prefWidthProperty().bind(currentPlayerHBox.widthProperty().multiply(0.3));
        currentPlayerLabel.prefHeightProperty().bind(currentPlayerHBox.heightProperty());
        currentPlayerMinionsUsedLabel.prefHeightProperty().bind(currentPlayerHBox.heightProperty());
        currentPlayerHBox.setMaxWidth(Double.MAX_VALUE);
        currentPlayerHBox.setAlignment(Pos.CENTER_RIGHT);
        currentPlayerHBox.setSpacing(20);

        // Adjust font sizes based on the width of the HBox.
        currentPlayerHBox.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            Platform.runLater(() -> {
                double newFontSize = newWidth.doubleValue() * fontScale;
                currentPlayerLabel.setStyle("-fx-font-size: " + newFontSize + "px;");
                currentPlayerMinionsUsedLabel.setStyle("-fx-font-size: " + (newFontSize * 0.8) + "px;");
            });
        });

        // Line below current player box
        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setStyle("-fx-border-color: black; -fx-border-width: 2");

        // Minion Display Box
        //TODO
    }
}
