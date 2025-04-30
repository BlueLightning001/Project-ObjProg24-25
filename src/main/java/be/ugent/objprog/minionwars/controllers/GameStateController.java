package be.ugent.objprog.minionwars.controllers;

import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.PowerModel;
import be.ugent.objprog.minionwars.views.GameView;
import be.ugent.objprog.minionwars.views.VictoryPane;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.Locale;

/**
 * Controller responsible for managing game state, including phase transitions and win conditions.
 */
public class GameStateController {
    private final PlayerModel playerModel;
    private final GameView view;
    private final Stage stage;
    private final Locale locale;
    private final TileController tileController;
    private final PowerModel powerModel;
    private final be.ugent.objprog.minionwars.models.TileModel tileModel;
    private final ActionTabController actionTabController;

    private ListChangeListener<be.ugent.objprog.minionwars.minions.Minion> player1WinListener;
    private ListChangeListener<be.ugent.objprog.minionwars.minions.Minion> player2WinListener;
    private ChangeListener<Number> turnCounterListener;

    private be.ugent.objprog.minionwars.JDOMReader jdomReader;

    public GameStateController(PlayerModel playerModel, GameView view, Stage stage, Locale locale, 
                              TileController tileController, PowerModel powerModel, be.ugent.objprog.minionwars.JDOMReader jdomReader,
                              be.ugent.objprog.minionwars.models.TileModel tileModel, ActionTabController actionTabController) {
        this.playerModel = playerModel;
        this.view = view;
        this.stage = stage;
        this.locale = locale;
        this.tileController = tileController;
        this.powerModel = powerModel;
        this.jdomReader = jdomReader;
        this.tileModel = tileModel;
        this.actionTabController = actionTabController;
    }

    /**
     * Sets up listeners for phase 1 of the game.
     */
    public void setUpListenersPart1() {
        // Set up center board button
        view.getCenterBoardButton().setOnAction(event -> view.resetGameGroupPosition());

        // Set up minion selection listener
        view.getMinionsTableView().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Unselect the tile when selecting a minion
            if (newValue != null) {
                view.getGameTileGroupPane().setSelectedHexTile(null);
            }
        });

        // Set up end turn button
        view.getEndTurnButton().setOnAction(event -> {
            this.playerModel.nextPlayer();
            view.getMinionsTableView().getSelectionModel().clearSelection();
            view.getGameTileGroupPane().getHexTiles().forEach(hexTile -> {
                if (!hexTile.getHighlightColor().equals(javafx.scene.paint.Color.TRANSPARENT)) {
                    hexTile.clearHighlight();
                }
            });
            view.getGameTileGroupPane().setSelectedHexTile(null);
        });

        // Set up tile click listener for placing minions
        view.getGameTileGroupPane().setOnMouseClicked(event -> {
            Object eventSource = event.getTarget();
            if (eventSource instanceof be.ugent.objprog.minionwars.views.HexTile hexTile && event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                be.ugent.objprog.minionwars.tiles.Tile tile = hexTile.getTile();
                be.ugent.objprog.minionwars.minions.Minion selectedMinion = view.getMinionsTableView().getSelectionModel().getSelectedItem();
                Player currentPlayer = this.playerModel.getCurrentPlayer();

                // Player wants to place a minion
                // Only on traversable home bases with same id
                if (selectedMinion != null && tile.isHomeBase() && tile.getHomebase() == currentPlayer.getHomeBaseID() && !tile.isOccupied() && tile.isTraversable()) {
                    // Create a new instance of the minion
                    be.ugent.objprog.minionwars.minions.Minion newMinion = selectedMinion.copy();
                    newMinion.setOwner(currentPlayer);

                    // Deduct money and place minion
                    currentPlayer.removeMoney(newMinion.getCost());
                    currentPlayer.addMinion(newMinion);
                    tile.setOccupant(newMinion);

                } else // Player wants to select a placed minion
                    if (tile.isOccupied() && tile.getOccupant().getOwner().equals(currentPlayer)) {
                        // Select the tile
                        tileModel.setSelectedTile(tile);
                    }

                view.getMinionsTableView().getSelectionModel().clearSelection();
            }
        });

        // Set up key press listener for deleting minions and resetting game position
        view.getView().setOnKeyPressed(event -> {
            Object eventSource = event.getTarget();
            if (event.getCode() == javafx.scene.input.KeyCode.R) {
                view.resetGameGroupPosition();
            }
            // Logic for deleting minion
            if (eventSource instanceof be.ugent.objprog.minionwars.views.ZoomableScrollPane && event.getCode() == javafx.scene.input.KeyCode.DELETE) {
                be.ugent.objprog.minionwars.tiles.Tile selectedTile = tileModel.getSelectedTile();
                if (selectedTile != null) {
                    Player currentPlayer = playerModel.getCurrentPlayer();
                    be.ugent.objprog.minionwars.minions.Minion occupant = selectedTile.getOccupant();
                    if (selectedTile.isOccupied() && occupant.getOwner().equals(currentPlayer)) {
                        selectedTile.setOccupant(null); // Remove minion from field
                        tileModel.setSelectedTile(null); // Unselect selected tile
                        currentPlayer.removeMinion(occupant); // Remove minion from player
                        currentPlayer.addMoney(occupant.getCost()); // Refund minion cost
                    }
                }
            }
        });

        // Logic for transitioning to phase 2 after a certain number of turns
        turnCounterListener = (observable, oldValue, newValue) -> {
            if (newValue.intValue() == playerModel.getPlayers().size()) {
                startNextPhase();
            }
        };

        // Starts phase 2 after 2 turns passed
        playerModel.turnCounterProperty().addListener(turnCounterListener);
    }

    /**
     * Sets up listeners for phase 2 of the game, including win condition listeners.
     */
    public void setUpListenersPart2() {
        // Set up center board button
        view.getCenterBoardButton().setOnAction(event -> view.resetGameGroupPosition());

        // Handles selecting tiles
        view.getGameTileGroupPane().setOnMouseClicked(event -> {
            Object eventSource = event.getTarget();
            if (eventSource instanceof be.ugent.objprog.minionwars.views.HexTile hexTile && event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                be.ugent.objprog.minionwars.tiles.Tile tile = hexTile.getTile();
                Player currentPlayer = this.playerModel.getCurrentPlayer();

                if (tile.isOccupied() && tile.getOccupant().getOwner().equals(currentPlayer)) {
                    // Select the tile
                    tileController.setSelected(hexTile);
                }
            }
        });

        // Listeners for ending game
        player1WinListener = change -> {
            // Every time player1's minions change, check if they are empty.
            Player player1 = playerModel.getPlayer1();
            if (player1 != null && player1.getMinions().isEmpty()) {
                endGame(playerModel.getPlayer2());
            }
        };

        player2WinListener = change -> {
            // Every time player2's minions change, check if they are empty.
            Player player2 = playerModel.getPlayer2();
            if (player2 != null && player2.getMinions().isEmpty()) {
                endGame(playerModel.getPlayer1());
            }
        };

        // Attach the listener to player's minions list using the property
        playerModel.getPlayer1().minionsProperty().addListener(player1WinListener);
        playerModel.getPlayer2().minionsProperty().addListener(player2WinListener);

        // Sets powerListview bindings and ensures selection is cleared
        playerModel.currentPlayerProperty().addListener((obs, oldPlayer, newPlayer) -> {
            javafx.scene.control.ListView<be.ugent.objprog.minionwars.powers.Power> powerListView = view.getPart2MenuContainer().getActionsPane().getPowerListView();
            if (newPlayer != null) {
                powerListView.itemsProperty().bind(newPlayer.availablePowersProperty());

                // Force update: clear power selection when the turn changes
                javafx.application.Platform.runLater(() -> {
                    powerListView.getSelectionModel().clearSelection();
                    tileController.setSelected(null);
                    actionTabController.invalidateAndUpdateSelectedMinion();
                });
            } else {
                powerListView.setItems(javafx.collections.FXCollections.observableArrayList()); // Clear if no player
            }
        });

        // Refresh UI when selected tile changes
        tileModel.selectedTileProperty().addListener((observable) -> {
            javafx.scene.control.Tab selectedTab = view.getActionsTabPane().getSelectionModel().getSelectedItem();
            if (selectedTab != null) {
                actionTabController.updateActionUI(selectedTab);
            }
        });

        // Set up tab selection listener
        view.getActionsTabPane().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            actionTabController.updateActionUI(newValue);
        });

        // Set up end turn button
        view.getEndTurnButton().setOnAction(event -> playerModel.nextPlayer());
    }

    /**
     * Transitions the game to the next phase.
     */
    public void startNextPhase() {
        // Remove old selection logic
        view.getView().setOnKeyPressed(null);

        // Clear homebase highlights
        Platform.runLater(() -> view.getGameTileGroupPane().getHexTiles().forEach(hexTile -> tileController.clearHighlights()));

        // Prevent listener duplication on replay
        playerModel.turnCounterProperty().removeListener(turnCounterListener);

        view.changeGamePhase();
        setUpListenersPart2();
        stage.setMinWidth(650);
        stage.setMinHeight(600);
    }

    /**
     * Ends the game and displays the victory screen.
     *
     * @param winner The player who won the game
     */
    public void endGame(Player winner) {
        view.getGameTileGroupPane().shutdown(); // Close active background threads

        // Prevent duplication of game
        playerModel.getPlayer1().minionsProperty().removeListener(player1WinListener);
        playerModel.getPlayer2().minionsProperty().removeListener(player2WinListener);

        boolean fullscreen = stage.isFullScreen();
        VictoryPane victoryScreen = new VictoryPane(winner, playerModel, powerModel, jdomReader, stage, locale);
        Scene scene = new Scene(victoryScreen, stage.getWidth(), stage.getHeight());
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F11) {
                stage.setFullScreen(!stage.isFullScreen());
            }
        });

        stage.close();
        stage.setScene(scene);
        stage.setFullScreen(fullscreen);
        stage.setFullScreenExitHint("");
        stage.show();
    }

    /**
     * Gets the player1 win listener.
     *
     * @return The player1 win listener
     */
    public ListChangeListener<be.ugent.objprog.minionwars.minions.Minion> getPlayer1WinListener() {
        return player1WinListener;
    }

    /**
     * Gets the player2 win listener.
     *
     * @return The player2 win listener
     */
    public ListChangeListener<be.ugent.objprog.minionwars.minions.Minion> getPlayer2WinListener() {
        return player2WinListener;
    }

    /**
     * Gets the turn counter listener.
     *
     * @return The turn counter listener
     */
    public ChangeListener<Number> getTurnCounterListener() {
        return turnCounterListener;
    }
}
