package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.models.MinionModel;
import be.ugent.objprog.minionwars.models.PlayerModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class Part1MenuContainer extends VBox {
    private Label currentPlayerLabel;
    private Label currentPlayerCoinsLabel;
    private HBox currentPlayerHBox;
    private MinionsTableView menuTable;
    private Button endTurnButton;
    private Button centerBoardButton;
    private ButtonBar menuButtonBar;
    private ResourceBundle bundle;

    public Part1MenuContainer(PlayerModel playerModel, MinionModel minionModel, Locale locale) {
        // Load the resource bundle.
        this.bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);

        // Create and configure the player information elements.
        currentPlayerLabel = new Label("Current Player");
        currentPlayerCoinsLabel = new Label("currentPlayerCoins");
        currentPlayerHBox = new HBox();
        currentPlayerHBox.getChildren().addAll(currentPlayerLabel, currentPlayerCoinsLabel);

        double fontScale = 0.1;

        // Set up coin icon on the coins label.
        ImageView coinIcon = new ImageView(new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/coin-FFB900.png")
        )));
        currentPlayerCoinsLabel.setGraphic(coinIcon);
        coinIcon.setFitHeight(10);
        coinIcon.setFitWidth(10);
        coinIcon.fitHeightProperty().bind(currentPlayerHBox.heightProperty().multiply(0.3));
        coinIcon.fitWidthProperty().bind(coinIcon.fitHeightProperty());

        // Bind the text properties for player name and money.
        currentPlayerLabel.textProperty().bind(playerModel.currentPlayerProperty().get().nameProperty());
        currentPlayerCoinsLabel.textProperty().bind(playerModel.currentPlayerProperty().get().moneyProperty().asString());
        playerModel.currentPlayerProperty().addListener((obs, oldPlayer, newPlayer) -> {
            if (newPlayer != null) {
                currentPlayerLabel.textProperty().bind(newPlayer.nameProperty());
                currentPlayerCoinsLabel.textProperty().bind(newPlayer.moneyProperty().asString());
            }
        });




        // Set alignments.
        currentPlayerLabel.setAlignment(Pos.CENTER);
        currentPlayerCoinsLabel.setAlignment(Pos.CENTER_LEFT);

        // Let the labels take up available space.
        HBox.setHgrow(currentPlayerLabel, Priority.ALWAYS);
        HBox.setHgrow(currentPlayerCoinsLabel, Priority.ALWAYS);
        currentPlayerLabel.prefWidthProperty().bind(currentPlayerHBox.widthProperty().multiply(0.7));
        currentPlayerCoinsLabel.prefWidthProperty().bind(currentPlayerHBox.widthProperty().multiply(0.3));
        currentPlayerLabel.prefHeightProperty().bind(currentPlayerHBox.heightProperty());
        currentPlayerCoinsLabel.prefHeightProperty().bind(currentPlayerHBox.heightProperty());
        currentPlayerHBox.setMaxWidth(Double.MAX_VALUE);
        currentPlayerHBox.setAlignment(Pos.CENTER_RIGHT);
        currentPlayerHBox.setSpacing(20);

        // Adjust font sizes based on the width of the HBox.
        currentPlayerHBox.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            Platform.runLater(() -> {
                double newFontSize = newWidth.doubleValue() * fontScale;
                currentPlayerLabel.setStyle("-fx-font-size: " + newFontSize + "px;");
                currentPlayerCoinsLabel.setStyle("-fx-font-size: " + (newFontSize * 0.8) + "px;");
            });
        });

        // Create the MinionsTableView.
        menuTable = new MinionsTableView(playerModel, minionModel, locale);
        menuTable.setMaxWidth(Double.MAX_VALUE);
        menuTable.setPrefHeight(Region.USE_COMPUTED_SIZE);

        // Create the buttons.
        endTurnButton = new Button(bundle.getString("gameScreen.endTurnButton"));
        centerBoardButton = new Button(bundle.getString("gameScreen.centerBoard"));

        // Create the button bar and add the buttons.
        menuButtonBar = new ButtonBar();
        menuButtonBar.setPrefSize(this.getPrefWidth(), 50);
        menuButtonBar.getButtons().addAll(endTurnButton, centerBoardButton);
        ButtonBar.setButtonData(endTurnButton, ButtonBar.ButtonData.LEFT);
        ButtonBar.setButtonData(centerBoardButton, ButtonBar.ButtonData.RIGHT);

        // Bind button heights and font sizes.
        endTurnButton.prefHeightProperty().bind(menuButtonBar.heightProperty().multiply(0.3));
        centerBoardButton.prefHeightProperty().bind(menuButtonBar.heightProperty().multiply(0.3));
        endTurnButton.styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", endTurnButton.widthProperty().multiply(fontScale)));
        centerBoardButton.styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", centerBoardButton.widthProperty().multiply(fontScale)));
        menuButtonBar.setPrefWidth(Region.USE_COMPUTED_SIZE);
        menuButtonBar.setMaxWidth(Double.MAX_VALUE);
        menuButtonBar.buttonMinWidthProperty().bind(menuButtonBar.widthProperty().multiply(0.3));

        // separator
        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setStyle("-fx-background-color: black;");


        getChildren().addAll(currentPlayerHBox, separator, menuTable, menuButtonBar);

        // Set vertical grow priority for contained elements.
        VBox.setVgrow(currentPlayerHBox, Priority.ALWAYS);
        VBox.setVgrow(menuTable, Priority.ALWAYS);
        VBox.setVgrow(menuButtonBar, Priority.ALWAYS);

        // Height ratios
        menuTable.prefHeightProperty().bind(this.heightProperty().multiply(0.8));
        menuButtonBar.prefHeightProperty().bind(this.heightProperty().multiply(0.1));
        currentPlayerHBox.prefHeightProperty().bind(this.heightProperty().multiply(0.1));
    }

    public Button getEndTurnButton() {
        return endTurnButton;
    }

    public Button getCenterBoardButton() {
        return centerBoardButton;
    }

    public MinionsTableView getMinionsTableView() {
        return menuTable;
    }
}
