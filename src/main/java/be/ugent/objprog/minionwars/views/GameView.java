package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.ZoomableScrollPane;
import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import be.ugent.objprog.minionwars.tiles.TileGroupPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

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
    private TableView<Minion> menuTable;
    private Button endTurnButton;
    private ZoomableScrollPane gamePane;
    private ButtonBar menuButtonBar;
    private Button centerBoardButton;
    private double borderWidth = 5.0;
    ResourceBundle bundle;
    public GameView(PlayerModel playerModel, TileModel tileModel, Locale locale) {
        this.playerModel = playerModel;
        this.locale = locale;
        bundle = ResourceBundle.getBundle("/be/ugent/objprog/minionwars/lang/messages", locale);

        container = new StackPane();
        root = new HBox();
        menuContainer = new VBox();
        currentPlayerLabel = new Label("Current Player");
        currentPlayerCoinsLabel = new Label("TEST");
        currentPlayerHBox = new HBox();
        currentPlayerHBox.getChildren().addAll(currentPlayerLabel, currentPlayerCoinsLabel);
        currentPlayerCoinsLabel.setStyle("-fx-border-color: green; -fx-border-width: 10px;");
        ImageView coinIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/coin-FFB900.png"))));
        currentPlayerCoinsLabel.setGraphic(coinIcon);
        coinIcon.setFitHeight(10);
        coinIcon.setFitWidth(10);


        menuTable = new TableView<>();
        endTurnButton = new Button(bundle.getString("gameScreen.endTurnButton")); // TODO
        centerBoardButton = new Button(bundle.getString("gameScreen.centerBoard"));


        centerBoardButton.setOnAction(event -> resetGameGroupPosition());

        menuButtonBar = new ButtonBar();
        gameTileGroupPane = new TileGroupPane(tileModel);


        gamePane = new ZoomableScrollPane(gameTileGroupPane);
        // gameTileGroupPane doesn't function without a Pane, but this is a workaround since
        // they cannot both reference each-other from constructor
        gameTileGroupPane.bindPane(gamePane);

        menuContainer.getChildren().addAll(currentPlayerHBox, menuTable, menuButtonBar);
        menuButtonBar.setPrefSize(menuContainer.getPrefWidth(), 50);
        menuButtonBar.getButtons().addAll(endTurnButton, centerBoardButton);
        ButtonBar.setButtonData(endTurnButton, ButtonBar.ButtonData.LEFT);
        ButtonBar.setButtonData(centerBoardButton, ButtonBar.ButtonData.RIGHT);

        currentPlayerLabel.setPrefSize(menuContainer.getPrefWidth(), 50);


        // Bind gameTileGroupPane to gamePane size
        gameTileGroupPane.prefWidthProperty().bind(gamePane.widthProperty());
        gameTileGroupPane.prefHeightProperty().bind(gamePane.heightProperty());

        // Ensure menuContainer resizes properly
        menuContainer.prefWidthProperty().bind(root.widthProperty().multiply(0.25)); // 25% of root width
        menuContainer.prefHeightProperty().bind(root.heightProperty());
        currentPlayerHBox.setMaxWidth(Double.MAX_VALUE);
        currentPlayerHBox.setAlignment(Pos.CENTER_RIGHT);
        currentPlayerHBox.setSpacing(20);
        currentPlayerHBox.setStyle("-fx-border-color: orange; -fx-border-width: 10px;");
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
        gamePane.prefWidthProperty().bind(root.widthProperty().multiply(0.75)); // 75% of root width
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

    public TileGroupPane getGameTileGroupPane() {
        return gameTileGroupPane;
    }

    public Region getView() {
        return container;
    }
}
