package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import be.ugent.objprog.minionwars.tiles.TileGroup;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.util.Locale;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameView {
    private StackPane container;
    private Locale locale;
    private PlayerModel playerModel;
    private HBox root;
    private VBox menuContainer;
    private Label menuTitleLabel;
    private TableView<Minion> menuTable;
    private Button endTurnButton;
    private TileGroup gameTileGroup;
    private Pane gamePane;  // Use Pane instead of Group to handle resizing
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

        gamePane = new Pane();  // Using Pane instead of Group for proper layout resizing
        gameTileGroup = new TileGroup(tileModel,gamePane); //TODO
        menuContainer.getChildren().addAll(menuTitleLabel, menuTable, menuButtonBar);
        menuButtonBar.setPrefSize(menuContainer.getPrefWidth(), 50);
        menuButtonBar.getButtons().addAll(endTurnButton, centerBoardButton);
        menuTitleLabel.setPrefSize(menuContainer.getPrefWidth(), 50);

        gamePane.getChildren().add(gameTileGroup);

        // DEBUG: Set a minimum size for gamePane
        gamePane.setMinSize(400, 400);
        gamePane.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 5");

        // Ensure menuContainer resizes properly
        menuContainer.prefWidthProperty().bind(root.widthProperty().multiply(0.25)); // 25% of root width
        menuContainer.prefHeightProperty().bind(root.heightProperty());
        menuTitleLabel.setMaxWidth(Double.MAX_VALUE);
        menuTable.setMaxWidth(Double.MAX_VALUE);
        menuTable.setPrefHeight(Region.USE_COMPUTED_SIZE); // Allow automatic height
        VBox.setVgrow(menuTable, Priority.ALWAYS); // Make it take remaining space
        VBox.setVgrow(menuButtonBar, Priority.NEVER);


        // Bind the width of gamePane to root width
        gamePane.prefWidthProperty().bind(root.widthProperty().multiply(0.75)); // 75% of root width
        gamePane.prefHeightProperty().bind(root.heightProperty()); // Bind height to root height

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

    private void setupZoomAndDrag(Pane pane, TileGroup contentGroup) {
        // Prevent board from leaving bounds
        Rectangle rect = new Rectangle(pane.getWidth(), pane.getHeight());
        pane.setClip(rect);
        rect.heightProperty().bind(pane.heightProperty());
        rect.widthProperty().bind(pane.widthProperty());

        // DEBUG: Check if content is added and visible
        if (pane.getChildren().isEmpty()) {
            System.out.println("No children added to gamePane!");
        }

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
            dragStartX = event.getSceneX();
            dragStartY = event.getSceneY();
            event.consume();
        });

        pane.setOnMouseDragged((MouseEvent event) -> {
            double offsetX = event.getSceneX() - dragStartX;
            double offsetY = event.getSceneY() - dragStartY;

            contentGroup.setTranslateX(contentGroup.getTranslateX() + offsetX);
            contentGroup.setTranslateY(contentGroup.getTranslateY() + offsetY);

            dragStartX = event.getSceneX();
            dragStartY = event.getSceneY();
            event.consume();
        });
    }

    public Region getView() {
        return container;
    }
}
