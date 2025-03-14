package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.ZoomableScrollPane;
import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import be.ugent.objprog.minionwars.tiles.TileGroupPane;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Locale;

public class GameView {
    private final TileGroupPane gameTileGroupPane;
    private StackPane container;
    private Locale locale;
    private PlayerModel playerModel;
    private HBox root;
    private VBox menuContainer;
    private Label menuTitleLabel;
    private TableView<Minion> menuTable;
    private Button endTurnButton;
    private ZoomableScrollPane gamePane;
    private ButtonBar menuButtonBar;
    private Button centerBoardButton;
    private double borderWidth = 5.0;
    public GameView(PlayerModel playerModel, TileModel tileModel, Locale locale) {
        this.playerModel = playerModel;
        this.locale = locale;
        container = new StackPane();
        root = new HBox();
        menuContainer = new VBox();
        menuTitleLabel = new Label();
        menuTable = new TableView<>();
        endTurnButton = new Button("START"); // TODO
        centerBoardButton = new Button("CENTER_BOARD");


        centerBoardButton.setOnAction(event -> resetGameGroupPosition());

        menuButtonBar = new ButtonBar();


        gameTileGroupPane = new TileGroupPane(tileModel);


        gamePane = new ZoomableScrollPane(gameTileGroupPane);
        // gameTileGroupPane doesn't function without a Pane, but this is a workaround since
        // they cannot both reference each-other from constructor
        gameTileGroupPane.bindPane(gamePane);

        menuContainer.getChildren().addAll(menuTitleLabel, menuTable, menuButtonBar);
        menuButtonBar.setPrefSize(menuContainer.getPrefWidth(), 50);
        menuButtonBar.getButtons().addAll(endTurnButton, centerBoardButton);
        menuTitleLabel.setPrefSize(menuContainer.getPrefWidth(), 50);


        // Bind gameTileGroupPane to gamePane size
        gameTileGroupPane.prefWidthProperty().bind(gamePane.widthProperty());
        gameTileGroupPane.prefHeightProperty().bind(gamePane.heightProperty());

        // Ensure menuContainer resizes properly
        menuContainer.prefWidthProperty().bind(root.widthProperty().multiply(0.25)); // 25% of root width
        menuContainer.prefHeightProperty().bind(root.heightProperty());
        menuTitleLabel.setMaxWidth(Double.MAX_VALUE);
        menuTable.setMaxWidth(Double.MAX_VALUE);
        menuTable.setPrefHeight(Region.USE_COMPUTED_SIZE);
        VBox.setVgrow(menuTable, Priority.ALWAYS); // Make it take remaining space
        VBox.setVgrow(menuButtonBar, Priority.NEVER);

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
