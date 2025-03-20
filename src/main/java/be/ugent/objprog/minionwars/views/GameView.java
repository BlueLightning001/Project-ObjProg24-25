package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.ZoomableScrollPane;
import be.ugent.objprog.minionwars.models.MinionModel;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.util.Locale;
import java.util.ResourceBundle;

public class GameView {
    private final TileGroupPane gameTileGroupPane;
    private StackPane container;
    private Locale locale;
    private PlayerModel playerModel;
    private TileModel tileModel;
    private HBox root;
    private Part1MenuContainer part1MenuContainer;
    private Part2MenuContainer part2MenuContainer;
    private MinionsTableView menuTable;
    private Button endTurnButton;
    private ZoomableScrollPane gamePane;
    private Button centerBoardButton;
    private ResourceBundle bundle;
    private MinionModel minionModel;
    public GameView(MinionModel minionModel, PlayerModel playerModel, TileModel tileModel, Locale locale) {
        this.minionModel = minionModel;
        this.playerModel = playerModel;
        this.tileModel = tileModel;
        this.locale = locale;
        bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);

        container = new StackPane();
        root = new HBox();
        part1MenuContainer = new Part1MenuContainer(playerModel, minionModel, locale);
        centerBoardButton = part1MenuContainer.getCenterBoardButton();
        endTurnButton = part1MenuContainer.getEndTurnButton();
        menuTable = part1MenuContainer.getMinionsTableView();

        // Reset board position
        centerBoardButton.setOnAction(event -> resetGameGroupPosition());

        gameTileGroupPane = new TileGroupPane(tileModel, playerModel);
        gamePane = new ZoomableScrollPane(gameTileGroupPane);
        // gameTileGroupPane doesn't function without a Pane, but this is a workaround since
        // they cannot both reference each-other from constructor
        gameTileGroupPane.bindPane(gamePane);
        gamePane.getStylesheets().add("/be/ugent/objprog/minionwars/css/scrollpane.css");

        // Bind gameTileGroupPane to gamePane size
        gameTileGroupPane.prefWidthProperty().bind(gamePane.widthProperty());
        gameTileGroupPane.prefHeightProperty().bind(gamePane.heightProperty());

        // Ensure part1MenuContainer resizes properly
        part1MenuContainer.prefWidthProperty().bind(root.widthProperty().multiply(0.30));
        part1MenuContainer.prefHeightProperty().bind(root.heightProperty());
        ;


        // Bind gamePane size
        gamePane.prefWidthProperty().bind(root.widthProperty().multiply(0.70));
        gamePane.prefHeightProperty().bind(root.heightProperty());

        // Bind root size to container size
        root.prefWidthProperty().bind(container.widthProperty());
        root.prefHeightProperty().bind(container.heightProperty());

        root.setPadding(new Insets(20));
        root.getChildren().addAll(part1MenuContainer, gamePane);
        root.setSpacing(20);


        this.container.setPrefSize(800, 480);

        this.container.getChildren().add(root);
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

    public void changeGamePhase() { //TODO !!
        getGameTileGroupPane().getHexTiles().forEach(HexTile::endStartPhase);
        this.root.getChildren().clear();
        part2MenuContainer = new Part2MenuContainer(playerModel, minionModel, tileModel, gameTileGroupPane, locale);
        this.root.getChildren().addAll(part2MenuContainer, gamePane);
    }

    public TileGroupPane getGameTileGroupPane() {
        return gameTileGroupPane;
    }

    public Button getEndTurnButton() {
        return endTurnButton;
    }

    public MinionsTableView getMinionsTableView() {
        return menuTable;
    }

    public Part2MenuContainer getPart2MenuContainer() {
        return part2MenuContainer;
    }

    public Region getView() {
        return container;
    }

}
