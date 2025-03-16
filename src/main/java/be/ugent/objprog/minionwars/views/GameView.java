package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.ZoomableScrollPane;
import be.ugent.objprog.minionwars.models.MinionModel;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import be.ugent.objprog.minionwars.tiles.HexTile;
import be.ugent.objprog.minionwars.tiles.TileGroupPane;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameView {
    private final TileGroupPane gameTileGroupPane;
    private StackPane container;
    private Locale locale;
    private PlayerModel playerModel;
    private HBox root;
    private VBox menuContainer;
    private Label currentPlayerLabel;
    private Label currentPlayerCoinsLabel;
    private HBox currentPlayerHBox;
    private MinionsTableView menuTable;
    private Button endTurnButton;
    private ZoomableScrollPane gamePane;
    private ButtonBar menuButtonBar;
    private Button centerBoardButton;
    private ResourceBundle bundle;
    private MinionModel minionModel;

    public GameView(MinionModel minionModel, PlayerModel playerModel, TileModel tileModel, Locale locale) {
        this.minionModel = minionModel;
        this.playerModel = playerModel;
        this.locale = locale;
        bundle = ResourceBundle.getBundle("/be/ugent/objprog/minionwars/lang/messages", locale);

        container = new StackPane();
        root = new HBox();
        menuContainer = new VBox();
        currentPlayerLabel = new Label("Current Player");
        currentPlayerCoinsLabel = new Label("currentPlayerCoins");
        currentPlayerHBox = new HBox();
        currentPlayerHBox.getChildren().addAll(currentPlayerLabel, currentPlayerCoinsLabel);

        playerModel.turnCounterProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue.intValue() >= 2) {
               changeGamePhase();
           }
        });

        double fontScale = 0.1;

        ImageView coinIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/coin-FFB900.png"))));
        currentPlayerCoinsLabel.setGraphic(coinIcon);
        coinIcon.setFitHeight(10);
        coinIcon.setFitWidth(10);

        // Listeners for player changes
        currentPlayerLabel.textProperty().bind(playerModel.currentPlayerProperty().get().nameProperty());
        currentPlayerCoinsLabel.textProperty().bind(playerModel.currentPlayerProperty().get().moneyProperty().asString());
        playerModel.currentPlayerProperty().addListener((obs, oldPlayer, newPlayer) -> {
            if (newPlayer != null) {
                currentPlayerLabel.textProperty().bind(newPlayer.nameProperty());
                currentPlayerCoinsLabel.textProperty().bind(newPlayer.moneyProperty().asString());
            }
        });

        currentPlayerLabel.setAlignment(Pos.CENTER);
        currentPlayerCoinsLabel.setAlignment(Pos.CENTER_LEFT);

        currentPlayerLabel.setStyle("-fx-border-color: blue; -fx-border-width: 5");
        currentPlayerCoinsLabel.setStyle("-fx-border-color: green; -fx-border-width: 5");



        // Make the labels take up all the available space
        HBox.setHgrow(currentPlayerLabel, Priority.ALWAYS);
        HBox.setHgrow(currentPlayerCoinsLabel, Priority.ALWAYS);
        currentPlayerLabel.prefWidthProperty().bind(currentPlayerHBox.widthProperty().multiply(0.7));
        currentPlayerCoinsLabel.prefWidthProperty().bind(currentPlayerHBox.widthProperty().multiply(0.3));
        currentPlayerLabel.prefHeightProperty().bind(currentPlayerHBox.heightProperty());
        currentPlayerCoinsLabel.prefHeightProperty().bind(currentPlayerHBox.heightProperty());
        //currentPlayerLabel.styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", currentPlayerHBox.widthProperty().multiply(fontScale)));
        //currentPlayerCoinsLabel.styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", currentPlayerHBox.widthProperty().multiply(fontScale)));
        coinIcon.fitHeightProperty().bind(currentPlayerHBox.heightProperty().multiply(0.3));
        coinIcon.fitWidthProperty().bind(coinIcon.fitHeightProperty());
        currentPlayerHBox.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            // Only update font size after layout is settled
            Platform.runLater(() -> {
                double newFontSize = newWidth.doubleValue() * fontScale;
                currentPlayerLabel.setStyle("-fx-font-size: " + newFontSize + "px;");
                currentPlayerCoinsLabel.setStyle("-fx-font-size: " + (newFontSize * 0.8) + "px;");
            });
        });


        menuTable = new MinionsTableView(playerModel,minionModel, locale);

        endTurnButton = new Button(bundle.getString("gameScreen.endTurnButton"));
        centerBoardButton = new Button(bundle.getString("gameScreen.centerBoard"));


        centerBoardButton.setOnAction(event -> resetGameGroupPosition());

        menuButtonBar = new ButtonBar();
        gameTileGroupPane = new TileGroupPane(tileModel,playerModel);


        gamePane = new ZoomableScrollPane(gameTileGroupPane);
        // gameTileGroupPane doesn't function without a Pane, but this is a workaround since
        // they cannot both reference each-other from constructor
        gameTileGroupPane.bindPane(gamePane);
        gamePane.getStylesheets().add("/be/ugent/objprog/minionwars/css/scrollpane.css");


        menuContainer.getChildren().addAll(currentPlayerHBox, menuTable, menuButtonBar);
        menuButtonBar.setPrefSize(menuContainer.getPrefWidth(), 50);
        menuButtonBar.getButtons().addAll(endTurnButton, centerBoardButton);
        ButtonBar.setButtonData(endTurnButton, ButtonBar.ButtonData.LEFT);
        ButtonBar.setButtonData(centerBoardButton, ButtonBar.ButtonData.RIGHT);


        centerBoardButton.prefHeightProperty().bind(menuButtonBar.heightProperty().multiply(0.3));
        endTurnButton.prefHeightProperty().bind(menuButtonBar.heightProperty().multiply(0.3));

        endTurnButton.styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", endTurnButton.widthProperty().multiply(fontScale)));
        centerBoardButton.styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", centerBoardButton.widthProperty().multiply(fontScale)));
        menuButtonBar.setPrefWidth(Region.USE_COMPUTED_SIZE);
        menuButtonBar.setMaxWidth(Double.MAX_VALUE);
        menuButtonBar.buttonMinWidthProperty().bind(menuButtonBar.widthProperty().multiply(0.3));


        // Bind gameTileGroupPane to gamePane size
        gameTileGroupPane.prefWidthProperty().bind(gamePane.widthProperty());
        gameTileGroupPane.prefHeightProperty().bind(gamePane.heightProperty());

        // Ensure menuContainer resizes properly
        menuContainer.prefWidthProperty().bind(root.widthProperty().multiply(0.30));
        menuContainer.prefHeightProperty().bind(root.heightProperty());
        currentPlayerHBox.setMaxWidth(Double.MAX_VALUE);
        currentPlayerHBox.setAlignment(Pos.CENTER_RIGHT);
        currentPlayerHBox.setSpacing(20);
        menuTable.setMaxWidth(Double.MAX_VALUE);
        menuTable.setPrefHeight(Region.USE_COMPUTED_SIZE);
        VBox.setVgrow(menuTable, Priority.ALWAYS); // Make it take remaining space
        VBox.setVgrow(menuButtonBar, Priority.ALWAYS);
        VBox.setVgrow(currentPlayerHBox, Priority.ALWAYS);

        // Height ratios
        menuTable.prefHeightProperty().bind(root.heightProperty().multiply(0.8));
        menuButtonBar.prefHeightProperty().bind(root.heightProperty().multiply(0.1));
        currentPlayerHBox.prefHeightProperty().bind(root.heightProperty().multiply(0.1));


        // Bind gamePane size
        gamePane.prefWidthProperty().bind(root.widthProperty().multiply(0.70));
        gamePane.prefHeightProperty().bind(root.heightProperty());

        // Bind root size to container size
        root.prefWidthProperty().bind(container.widthProperty());
        root.prefHeightProperty().bind(container.heightProperty());

        root.setPadding(new Insets(20));
        root.getChildren().addAll(menuContainer, gamePane);
        root.setSpacing(20);



        this.container.setPrefSize(800, 480);

        this.container.getChildren().add(root);

        this.container.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.R) {
                resetGameGroupPosition();
            }
        });
    }

    public void changeGamePhase() { //TODO !!
        getGameTileGroupPane().getHexTiles().forEach( HexTile::endStartPhase);
        menuContainer.getChildren().setAll(endTurnButton);
    }

    public MinionsTableView getMinionsTableView() {
        return menuTable;
    }

    public void resetGameGroupPosition() {
        gameTileGroupPane.setTranslateX(0);
        gameTileGroupPane.setTranslateY(0);
        gameTileGroupPane.setScaleX(1.0);
        gameTileGroupPane.setScaleY(1.0);
        ZoomableScrollPane boundPane = gameTileGroupPane.getBoundPane();
        if (boundPane != null) {
            boundPane.resetScale();
        }
    }

    public Button getEndTurnButton() {
        return endTurnButton;
    }

    public TileGroupPane getGameTileGroupPane() {
        return gameTileGroupPane;
    }

    public Region getView() {
        return container;
    }
}
