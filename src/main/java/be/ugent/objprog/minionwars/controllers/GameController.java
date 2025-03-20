package be.ugent.objprog.minionwars.controllers;

import be.ugent.objprog.minionwars.JDOMReader;
import be.ugent.objprog.minionwars.ZoomableScrollPane;
import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.MinionModel;
import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import be.ugent.objprog.minionwars.views.HexTile;
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.views.GameView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Locale;

public class GameController {
    private final Locale locale;
    private final MinionModel minionModel;
    private GameView view;
    private PlayerModel playerModel;
    private TileModel tileModel;
    private Stage stage;

    public GameController(Stage stage,PlayerModel playerModel, Locale locale,JDOMReader reader) {
        this.stage = stage;
        this.playerModel = playerModel;
        this.minionModel = new MinionModel(reader);
        this.tileModel = new TileModel(reader);
        this.locale = locale;
        this.view = new GameView(minionModel,playerModel,tileModel,locale);
        view.resetGameGroupPosition();
        view.getEndTurnButton().setOnAction(event -> {
            playerModel.nextPlayer();
            view.getMinionsTableView().getSelectionModel().clearSelection();
            view.getGameTileGroupPane().getHexTiles().stream()
                    .filter(hexTile -> hexTile.getTile().getHomebase() == playerModel.getCurrentPlayer().getHomeBaseID())
                    .forEach(hexTile -> {
                        Color playerColor = playerModel.getPlayerColor(playerModel.getCurrentPlayer()); // Get the player's color
                        hexTile.highlight(playerColor); // Highlight tile
                    });
           view.getGameTileGroupPane().setSelectedHexTile(null);


        });
        stage.setOnCloseRequest(event -> {
            view.getGameTileGroupPane().shutdown();// Releases resources from other threads
        });

        // TODO game logic

        // PHASE 2
        view.getMinionsTableView().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Unselect the tile when selecting a minion
            if (newValue != null) {
                view.getGameTileGroupPane().setSelectedHexTile(null);  // Deselect any selected tile
            }
        });

        view.getGameTileGroupPane().setOnMouseClicked(event -> {
            Object eventSource = event.getTarget();
            if (eventSource instanceof HexTile hexTile && event.getButton() == MouseButton.PRIMARY) {
                Tile tile = hexTile.getTile();
                System.out.println("CLICKED: " + tile);
                Minion selectedMinion = view.getMinionsTableView().getSelectionModel().getSelectedItem();
                Player currentPlayer = playerModel.getCurrentPlayer();
                // Player wants to place minion
                if ( selectedMinion != null && tile.isHomeBase() && tile.getHomebase() == currentPlayer.getHomeBaseID()
                        && !tile.isOccupied() && tile.isTraversable()) {
                    currentPlayer.removeMoney(selectedMinion.getCost());
                    System.out.println("PLAYER MONEY: " + currentPlayer.getMoney());
                    currentPlayer.addMinion(selectedMinion);
                    selectedMinion.setOwner(currentPlayer);
                    tile.setOccupant(selectedMinion);

                    // Select the tile
                } else if (tile.isOccupied() && tile.getOccupant().getOwner().equals(currentPlayer)) {
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
                        currentPlayer.addMoney(occupant.getCost());

                    }
                }

            }
        });
        playerModel.turnCounterProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue.intValue() == 2 ){
              startnextPhase();
           }
        });
    }

    private void startnextPhase() {
        endGame();
        return;
//        System.out.println("STARTING NEXT PHASE");
//        view.changeGamePhase();
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
