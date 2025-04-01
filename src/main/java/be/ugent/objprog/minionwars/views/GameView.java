package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.models.MinionModel;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.PowerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import be.ugent.objprog.minionwars.tiles.Tile;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.util.Locale;
import java.util.ResourceBundle;

public class GameView {
    private final TileGroupPane gameTileGroupPane;
    private final StackPane container;
    private final Locale locale;
    private final PlayerModel playerModel;
    private final TileModel tileModel;
    private final HBox root;
    private final MinionsTableView menuTable;
    private final ZoomableScrollPane gamePane;
    private final MinionModel minionModel;
    private final PowerModel powerModel;
    private Part2MenuContainer part2MenuContainer;
    private Button endTurnButton;
    private Button centerBoardButton;

    public GameView(MinionModel minionModel, PlayerModel playerModel, TileModel tileModel, PowerModel powerModel, Locale locale) {
        this.minionModel = minionModel;
        this.playerModel = playerModel;
        this.tileModel = tileModel;
        this.powerModel = powerModel;
        this.locale = locale;
        ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);

        container = new StackPane();
        root = new HBox();

        gameTileGroupPane = new TileGroupPane(tileModel, playerModel);
        gamePane = new ZoomableScrollPane(gameTileGroupPane);
        // gameTileGroupPane doesn't function without a Pane, but this is a workaround since
        // they cannot both reference each-other from constructor
        gameTileGroupPane.bindPane(gamePane);
        gamePane.getStylesheets().add("/be/ugent/objprog/minionwars/css/scrollpane.css");

        // Bind gameTileGroupPane to gamePane size
        gameTileGroupPane.prefWidthProperty().bind(gamePane.widthProperty());
        gameTileGroupPane.prefHeightProperty().bind(gamePane.heightProperty());

        //// PART 1
        Part1MenuContainer part1MenuContainer = new Part1MenuContainer(playerModel, minionModel, locale);
        centerBoardButton = part1MenuContainer.getCenterBoardButton();
        endTurnButton = part1MenuContainer.getEndTurnButton();
        menuTable = part1MenuContainer.getMinionsTableView();

        // Reset board position
        centerBoardButton.setOnAction(event -> resetGameGroupPosition());

        // End turn only when player has at least one minion
        rebindEndTurnButtonPart1();
        this.playerModel.currentPlayerProperty().addListener((observable) -> {
            rebindEndTurnButtonPart1();
        });

        // Ensure part1MenuContainer resizes properly
        part1MenuContainer.prefWidthProperty().bind(root.widthProperty().multiply(0.30));
        part1MenuContainer.prefHeightProperty().bind(root.heightProperty());


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

    private void rebindEndTurnButtonPart1() {
        endTurnButton.disableProperty().unbind();
        endTurnButton.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> this.playerModel.getCurrentPlayer().getMinions().isEmpty(),
                        this.playerModel.getCurrentPlayer().getMinions()
                )
        );
    }

    public void changeGamePhase() {
        getGameTileGroupPane().getHexTiles().forEach(HexTile::endStartPhase);
        this.root.getChildren().clear();
        part2MenuContainer = new Part2MenuContainer(playerModel, minionModel, tileModel, powerModel, gameTileGroupPane, locale);
        this.root.getChildren().addAll(part2MenuContainer, gamePane);

        part2MenuContainer.prefWidthProperty().bind(root.widthProperty().multiply(0.30));
        part2MenuContainer.prefHeightProperty().bind(root.heightProperty());

        gamePane.prefWidthProperty().bind(root.widthProperty().multiply(0.70));
        gamePane.prefHeightProperty().bind(root.heightProperty());

        centerBoardButton = part2MenuContainer.getCenterBoardButton();
        centerBoardButton.setOnAction(event -> resetGameGroupPosition());

        endTurnButton = part2MenuContainer.getEndTurnButton();

    }

    public TileGroupPane getGameTileGroupPane() {
        return gameTileGroupPane;
    }

    public ActionsPane getActionsTabPane() {
        return part2MenuContainer.getActionsPane();
    }

    public Button getEndTurnButton() {
        return endTurnButton;
    }

    public HexTile getHexTile(Tile tile) {
        if (tile != null) {
            return getHexTile(tile.getXCoord(), tile.getYCoord());
        }
        return null;
    }

    public HexTile getHexTile(int x, int y) {
        HexTile[][] grid = gameTileGroupPane.getHexTileGrid();
        if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length) {
            return null;
        }
        return grid[x][y];
    }

    public MinionsTableView getMinionsTableView() {
        return menuTable;
    }

    public Part2MenuContainer getPart2MenuContainer() {
        return part2MenuContainer;
    }

    public Button getRestButton() {
        if (part2MenuContainer == null) {
            return null;
        }
        return part2MenuContainer.getRestButton();
    }

    public Region getView() {
        return container;
    }

}
