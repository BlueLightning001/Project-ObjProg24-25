package be.ugent.objprog.minionwars.controllers;

import be.ugent.objprog.minionwars.JDOMReader;
import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.MinionModel;
import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import be.ugent.objprog.minionwars.tiles.HexTile;
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.views.GameView;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

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
            view.getGameTileGroupPane().getSelectedHexTile().setSelected(false);

        });
        stage.setOnCloseRequest(event -> {
            view.getGameTileGroupPane().shutdown();// Releases resources from other threads
        });
        view.getGameTileGroupPane().setOnMouseClicked(event -> {
            Object eventSource = event.getTarget();
            if (eventSource instanceof HexTile hexTile && event.getButton() == MouseButton.PRIMARY) {
                Tile tile = hexTile.getTile();

                System.out.println("CLICKED ON TILE: " + tile);
                //TODO handle game logic
                Minion selectedMinion = view.getMinionsTableView().getSelectionModel().getSelectedItem();
                Player currentPlayer = playerModel.getCurrentPlayer();
                // Player wants to place minion
                if ( selectedMinion != null && tile.isHomeBase() && tile.getHomebase() == currentPlayer.getHomeBaseID() && !tile.isOccupied()) {
                    currentPlayer.removeMoney(selectedMinion.getCost());
                    System.out.println("PLAYER MONEY: " + currentPlayer.getMoney());
                    currentPlayer.addMinion(selectedMinion);
                    selectedMinion.setOwner(currentPlayer);
                    tile.setOccupant(selectedMinion);
                    view.getMinionsTableView().getSelectionModel().clearSelection();
                } else if (tile.isOccupied() && tile.getOccupant().getOwner().equals(currentPlayer)) {
                    view.getGameTileGroupPane().setSelectedHexTile(hexTile);
                }

            }
        });
        view.getGameTileGroupPane().setOnKeyPressed(event -> {
            Object eventSource = event.getTarget();
            if (eventSource instanceof HexTile hexTile && event.getCode() == KeyCode.DELETE ) {
                Tile tile = hexTile.getTile();
                Player currentPlayer = playerModel.getCurrentPlayer();
                Minion occupant = tile.getOccupant();
                if (tile.isOccupied() && occupant.getOwner().equals(currentPlayer)) {
                    tile.setOccupant(null);
                    currentPlayer.addMoney(occupant.getCost());

                }
            }
        });
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
