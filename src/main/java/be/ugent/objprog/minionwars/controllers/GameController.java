package be.ugent.objprog.minionwars.controllers;

import be.ugent.objprog.minionwars.JDOMReader;
import be.ugent.objprog.minionwars.ZoomableScrollPane;
import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.MinionModel;
import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.PowerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import be.ugent.objprog.minionwars.powers.Power;
import be.ugent.objprog.minionwars.views.HexTile;
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.views.GameView;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class GameController {
    private final Locale locale;
    private final MinionModel minionModel;
    private GameView view;
    private PlayerModel playerModel;
    private final TileModel tileModel;
    private Stage stage;
    private final PowerModel powerModel;
    private final ResourceBundle bundle;

    public GameController(Stage stage,PlayerModel playerModel, Locale locale,JDOMReader reader) {
        this.stage = stage;
        this.playerModel = playerModel;
        this.minionModel = new MinionModel(reader);
        this.tileModel = new TileModel(reader,locale);
        this.powerModel = new PowerModel(reader,locale);
        this.locale = locale;
        this.view = new GameView(minionModel,playerModel,tileModel,powerModel,locale);
        this.bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);

        view.resetGameGroupPosition();

        stage.setOnCloseRequest(event -> {
            view.getGameTileGroupPane().shutdown();// Releases resources from other threads
        });

        // TODO game logic

        // PART 1
        setUpListenersPart1();
    }

    private void setUpListenersPart1() {
        view.getMinionsTableView().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Unselect the tile when selecting a minion
            if (newValue != null) {
                view.getGameTileGroupPane().setSelectedHexTile(null);  // Deselect any selected tile
            }
        });

        view.getEndTurnButton().setOnAction(event -> {
            this.playerModel.nextPlayer();
            view.getMinionsTableView().getSelectionModel().clearSelection();
            view.getGameTileGroupPane().getHexTiles().stream()
                    .filter(hexTile -> hexTile.getTile().getHomebase() == this.playerModel.getCurrentPlayer().getHomeBaseID())
                    .forEach(hexTile -> {
                        Color playerColor = this.playerModel.getPlayerColor(this.playerModel.getCurrentPlayer()); // Get the player's color
                        hexTile.highlight(playerColor); // Highlight tile
                    });
            view.getGameTileGroupPane().setSelectedHexTile(null);
        });

        view.getGameTileGroupPane().setOnMouseClicked(event -> {
            Object eventSource = event.getTarget();
            if (eventSource instanceof HexTile hexTile && event.getButton() == MouseButton.PRIMARY) {
                Tile tile = hexTile.getTile();
                System.out.println("CLICKED: " + tile);
                Minion selectedMinion = view.getMinionsTableView().getSelectionModel().getSelectedItem();
                Player currentPlayer = this.playerModel.getCurrentPlayer();

                // Player wants to place a minion
                if (selectedMinion != null && tile.isHomeBase() && tile.getHomebase() == currentPlayer.getHomeBaseID()
                        && !tile.isOccupied() && tile.isTraversable()) {
                    // Create a new instance of the minion
                    Minion newMinion = selectedMinion.clone();
                    newMinion.setOwner(currentPlayer);

                    // Deduct money and place minion
                    currentPlayer.removeMoney(newMinion.getCost());
                    System.out.println("PLAYER MONEY: " + currentPlayer.getMoney());
                    currentPlayer.addMinion(newMinion);
                    tile.setOccupant(newMinion);

                } else if (tile.isOccupied() && tile.getOccupant().getOwner().equals(currentPlayer)) {
                    // Select the tile
                    System.out.println("SELECTED: " + hexTile.getTile());
                    view.getGameTileGroupPane().setSelectedHexTile(hexTile);
                }

                view.getMinionsTableView().getSelectionModel().clearSelection();
            }
        });

        // Logic for deleting minion
        getView().setOnKeyPressed(event -> {
            Object eventSource = event.getTarget();
            System.out.println(eventSource);
            if (event.getCode() == KeyCode.R) {
                view.resetGameGroupPosition();
            }
            if (eventSource instanceof ZoomableScrollPane pane && event.getCode() == KeyCode.DELETE) {
                HexTile selectedHexTile = view.getGameTileGroupPane().getSelectedHexTile();
                Tile tileToDelete = null;
                if (selectedHexTile != null) {
                    tileToDelete = selectedHexTile.getTile();
                    Player currentPlayer = playerModel.getCurrentPlayer();
                    Minion occupant = tileToDelete.getOccupant();
                    if (tileToDelete.isOccupied() && occupant.getOwner().equals(currentPlayer)) {
                        tileToDelete.setOccupant(null);
                        view.getGameTileGroupPane().setSelectedHexTile(null);
                        currentPlayer.removeMinion(occupant);
                        currentPlayer.addMoney(occupant.getCost());

                    }
                }

            }
        });
        playerModel.turnCounterProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue.intValue() == 2 ){
              startNextPhase();
           }
        });
    }

    private void startNextPhase() {
        System.out.println("STARTING NEXT PHASE");

        // Remove old selection logic
        view.getView().setOnKeyPressed(null);

        view.changeGamePhase();
        setUpListenersPart2();
        stage.setMinWidth(650);
        stage.setMinHeight(400);

    }

    private void setUpListenersPart2() {
        view.getGameTileGroupPane().setOnMouseClicked(event -> {
            Object eventSource = event.getTarget();
            if (eventSource instanceof HexTile hexTile && event.getButton() == MouseButton.PRIMARY) {
                Tile tile = hexTile.getTile();
                System.out.println("CLICKED: " + tile);
                Player currentPlayer = this.playerModel.getCurrentPlayer();

               if (tile.isOccupied() && tile.getOccupant().getOwner().equals(currentPlayer)) {
                    // Select the tile
                    System.out.println("SELECTED: " + hexTile.getTile());
                    view.getGameTileGroupPane().setSelectedHexTile(hexTile);
                }

            }
        });

        view.getActionsTabPane().getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == null ) {
                clearHighlights();
                return;
            }

            HexTile hexTile = view.getGameTileGroupPane().getSelectedHexTile();
            Color specialColor = Color.BLUE;
            Color attackColor = Color.RED;
            Color moveColor = Color.GREEN;

            if (newTab.getText().equals(bundle.getString("actions.special"))) {
                ListView<Power> powerListView = (ListView<Power>) newTab.getContent();
                Power power = powerListView.getSelectionModel().getSelectedItem();
                if (power != null) {
                    highLightRadius(hexTile,power.getRadius(),specialColor);
                }
            } else if (newTab.getText().equals(bundle.getString("actions.attack"))) {
                Minion occupant = hexTile.getTile().getOccupant();
                if (occupant != null) {
                    int minRange = occupant.getRange().getFirst();
                    int maxRange = occupant.getRange().getLast();
                    highlightRange(hexTile,minRange,maxRange,attackColor);
                }
            } else if (newTab.getText().equals(bundle.getString("actions.move"))) {
                Minion occupant = hexTile.getTile().getOccupant();
                if (occupant != null) {
                    int movement = occupant.getMovement();
                    highLightRadius(hexTile,movement,moveColor);
                }
            }
        });

    }

    private void clearHighlights() {
        view.getGameTileGroupPane().getHexTiles().forEach(HexTile::clearHighlight);
    }

    private void highLightRadius(HexTile hexTile,int radius,Color color) {
        highLightRadius(hexTile.getTile(),radius, color);

    }
    private void highLightRadius(Tile tile,int radius,Color color) {
        highlightRange(tile,0,radius,color);
    }
    private void highlightRange(HexTile hexTile,int minRange,int maxRange,Color color) {
        highlightRange(hexTile.getTile(),minRange,maxRange,color);
    }
    private void highlightRange(Tile tile,int minRange,int maxRange,Color color) {
        List<Tile> tilesInRadius = tileModel.getTilesInRadius(tile,minRange,maxRange);
        for (Tile tileInRadius : tilesInRadius) {
            if (tile != tileInRadius) {
                HexTile hexTile = view.getHexTile(tile);
                System.out.println("HIGHLIGHTING: "+ tile);
                hexTile.highlight(color);
            }
        }
    }
    public void endGame(){
        //TODO launch new game
        view.getGameTileGroupPane().shutdown();
        stage.close();

    }
    public Region getView() {
        return this.view.getView();
    }
}
