package be.ugent.objprog.minionwars.views;

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
    private Pane gamePane;
    private double zoomFactor = 1.0;
    private double dragStartX, dragStartY;
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
        endTurnButton = new Button("START"); //TODO
        centerBoardButton = new Button("CENTER_BOARD");
        centerBoardButton.setOnAction(event -> {
            resetGameGroupPosition();
        });

        menuButtonBar = new ButtonBar();

        gamePane = new Pane();
        gameTileGroup = new TileGroupPane(tileModel,gamePane); //TODO
        menuContainer.getChildren().addAll(menuTitleLabel, menuTable, menuButtonBar);
        menuButtonBar.setPrefSize(menuContainer.getPrefWidth(), 50);
        menuButtonBar.getButtons().addAll(endTurnButton, centerBoardButton);
        menuTitleLabel.setPrefSize(menuContainer.getPrefWidth(), 50);

        gamePane.getChildren().add(gameTileGroup);


        gamePane.setMinSize(400, 400);
        gamePane.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 5");

        gameTileGroup.prefWidthProperty().bind(gamePane.widthProperty());
        gameTileGroup.prefHeightProperty().bind(gamePane.heightProperty());
        gameTileGroup.setStyle("-fx-border-color: red; -fx-border-style: solid; -fx-border-width: 2"); // Debug border


        // Ensure menuContainer resizes properly
        menuContainer.prefWidthProperty().bind(root.widthProperty().multiply(0.25)); // 25% of root width
        menuContainer.prefHeightProperty().bind(root.heightProperty());
        menuTitleLabel.setMaxWidth(Double.MAX_VALUE);
        menuTable.setMaxWidth(Double.MAX_VALUE);
        menuTable.setPrefHeight(Region.USE_COMPUTED_SIZE);
        VBox.setVgrow(menuTable, Priority.ALWAYS); // Make it take remaining space
        VBox.setVgrow(menuButtonBar, Priority.NEVER);


        // Bind the width of gamePane to root width
        gamePane.prefWidthProperty().bind(root.widthProperty().multiply(0.75)); // 75% of root width
        gamePane.prefHeightProperty().bind(root.heightProperty());

        // DEBUG: Check root size binding
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

        setupZoomAndDrag(gamePane, gameTileGroup);
    }

    public void resetGameGroupPosition() {
        gameTileGroup.setTranslateX(0);
        gameTileGroup.setTranslateY(0);
        gameTileGroup.setScaleX(1.0);
        gameTileGroup.setScaleY(1.0);
        zoomFactor = 1.0; // Reset stored zoom factor
    }

    private void setupZoomAndDrag(Pane pane, TileGroupPane contentGroup) {
        // Prevent board from leaving bounds
        Rectangle rect = new Rectangle(pane.getWidth(), pane.getHeight());
        pane.setClip(rect);
        rect.heightProperty().bind(pane.heightProperty());
        rect.widthProperty().bind(pane.widthProperty());

        pane.setOnScroll((ScrollEvent event) -> {
            double zoomScale = (event.getDeltaY() > 0) ? 1.1 : 0.9; // Zoom in/out
            double newZoomFactor = zoomFactor * zoomScale;

            // Prevent zooming out too much or zooming in too much
            if (newZoomFactor < 0.5 || newZoomFactor > 3.0) return;

            zoomFactor = newZoomFactor;
            contentGroup.setScaleX(zoomFactor);
            contentGroup.setScaleY(zoomFactor);

            event.consume();
        });

        pane.setOnMousePressed((MouseEvent event) -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                dragStartX = event.getSceneX();
                dragStartY = event.getSceneY();
                event.consume();
            }
        });

        pane.setOnMouseDragged((MouseEvent event) -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                double offsetX = event.getSceneX() - dragStartX;
                double offsetY = event.getSceneY() - dragStartY;

                contentGroup.setTranslateX(contentGroup.getTranslateX() + offsetX);
                contentGroup.setTranslateY(contentGroup.getTranslateY() + offsetY);

                dragStartX = event.getSceneX();
                dragStartY = event.getSceneY();
                event.consume();
            }
        });
    }

    public Region getView() {
        return container;
    }
}
