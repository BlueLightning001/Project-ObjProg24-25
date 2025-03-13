package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import be.ugent.objprog.minionwars.tiles.TileGroup;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.util.Locale;

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
    private Pane gamePane;
    private double zoomFactor = 1.0;
    private double dragStartX, dragStartY;
    private ButtonBar menuButtonBar;
    private Button centerBoardButton;


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
        menuButtonBar = new ButtonBar();
        gameTileGroup = new TileGroup(tileModel); //TODO
        gamePane = new Pane();
        menuContainer.getChildren().addAll(menuTitleLabel,menuTable,menuButtonBar);
        menuButtonBar.setPrefSize(menuContainer.getPrefWidth(),50);
        menuButtonBar.getButtons().addAll(endTurnButton,centerBoardButton);
        menuTitleLabel.setPrefSize(menuContainer.getPrefWidth(), 50);

        gamePane.getChildren().add(gameTileGroup);
        this.root.getChildren().addAll(menuContainer, gamePane);
        gamePane.setPrefSize(500,500);
        gamePane.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 10");
        setupZoomAndDrag(gamePane, gameTileGroup);
        Rectangle clip = new Rectangle(500, 500); // Match the gamePane size
        gamePane.setClip(clip);
        this.container.getChildren().add(root);



    }

    private void setupZoomAndDrag(Pane pane, TileGroup contentGroup) {

        pane.setOnScroll((ScrollEvent event) -> {
            double zoomScale = (event.getDeltaY() > 0) ? 1.1 : 0.9; // Zoom in/out
            double newZoomFactor = zoomFactor * zoomScale;

            // prevent zooming out too much or zooming in too much
            if (newZoomFactor < 0.5 || newZoomFactor > 3.0) return;

            zoomFactor = newZoomFactor;
            contentGroup.setScaleX(zoomFactor);
            contentGroup.setScaleY(zoomFactor);

            event.consume();
        });

        pane.setOnMousePressed((MouseEvent event) -> {
            dragStartX = event.getSceneX();
            dragStartY = event.getSceneY();
        });

        pane.setOnMouseDragged((MouseEvent event) -> {
            double offsetX = event.getSceneX() - dragStartX;
            double offsetY = event.getSceneY() - dragStartY;

            contentGroup.setTranslateX(contentGroup.getTranslateX() + offsetX);
            contentGroup.setTranslateY(contentGroup.getTranslateY() + offsetY);

            dragStartX = event.getSceneX();
            dragStartY = event.getSceneY();
        });
    }


    public Region getView() {
        return container;
    }
}
