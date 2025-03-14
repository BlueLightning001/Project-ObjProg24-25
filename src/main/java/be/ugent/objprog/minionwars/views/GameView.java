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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.util.Locale;
import java.util.Objects;

import javafx.scene.layout.*;
public class GameView {
    private StackPane container;
    private Locale locale;
    private PlayerModel playerModel;
    private HBox root;
    private VBox menuContainer;
    private Label menuTitleLabel;
    private TableView<Minion> menuTable;
    private Button endTurnButton;
    private TileGroupPane gameTileGroup;
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

        // Set up reset button action
        centerBoardButton.setOnAction(event -> resetGameGroupPosition());

        menuButtonBar = new ButtonBar();


        gameTileGroup = new TileGroupPane(tileModel);

        // Necessary since ZoomableScrollPane requires the content first
        gamePane = new ZoomableScrollPane(gameTileGroup);
        gameTileGroup.bindPane(gamePane);

        // Allow scrolling
        gamePane.setPannable(true);

        menuContainer.getChildren().addAll(menuTitleLabel, menuTable, menuButtonBar);
        menuButtonBar.setPrefSize(menuContainer.getPrefWidth(), 50);
        menuButtonBar.getButtons().addAll(endTurnButton, centerBoardButton);
        menuTitleLabel.setPrefSize(menuContainer.getPrefWidth(), 50);

        gamePane.setMinSize(400, 400); // Minimum size for the ZoomableScrollPane
        gamePane.setStyle("-fx-background-color: black");

        // Bind gameTileGroup to gamePane size
        gameTileGroup.prefWidthProperty().bind(gamePane.widthProperty());
        gameTileGroup.prefHeightProperty().bind(gamePane.heightProperty());

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
        root.setStyle("-fx-border-color: blue; -fx-border-style: solid; -fx-border-width: 10");
        root.setSpacing(20);

        this.container.setMinSize(800, 480);
        this.container.setPrefSize(800, 480);
        this.container.getChildren().add(root);

        this.container.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.R) {
                resetGameGroupPosition();
            }
        });
    }

    public void resetGameGroupPosition() {
        gameTileGroup.setTranslateX(0);
        gameTileGroup.setTranslateY(0);
        gameTileGroup.setScaleX(1.0);
        gameTileGroup.setScaleY(1.0);
    }

    public Region getView() {
        return container;
    }
}
