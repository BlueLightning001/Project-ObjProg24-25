package be.ugent.objprog.minionwars.controllers;

import be.ugent.objprog.minionwars.JDOMReader;
import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.MinionModel;
import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.PowerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import be.ugent.objprog.minionwars.powers.Power;
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.views.GameView;
import be.ugent.objprog.minionwars.views.HexTile;
import be.ugent.objprog.minionwars.views.VictoryPane;
import be.ugent.objprog.minionwars.views.ZoomableScrollPane;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class GameController {
    private final Locale locale;
    private final TileModel tileModel;
    private final PowerModel powerModel;
    private final GameView view;
    private final PlayerModel playerModel;
    private final Stage stage;
    private final JDOMReader jdomReader;
    private EventHandler<MouseEvent> specialMouseClickedHandler;
    private EventHandler<MouseEvent> specialMouseMovedHandler;
    private ListChangeListener<Minion> player1WinListener;
    private ListChangeListener<Minion> player2WinListener;
    private Tab lastTab;
    private ChangeListener<Number> turnCounterListener;

    public GameController(Stage stage, PlayerModel playerModel, Locale locale, JDOMReader reader) {
        this.stage = stage;
        this.playerModel = playerModel;
        MinionModel minionModel = new MinionModel(reader);
        this.tileModel = new TileModel(reader, locale);
        this.powerModel = new PowerModel(reader, locale);
        this.jdomReader = reader;

        // Load the players powers
        this.playerModel.getPlayer1().setAvailablePowers(FXCollections.observableArrayList(powerModel.getPowerList()));
        this.playerModel.getPlayer2().setAvailablePowers(FXCollections.observableArrayList(powerModel.getPowerList()));

        this.locale = locale;
        this.view = new GameView(minionModel, playerModel, tileModel, powerModel, locale);

        view.resetGameGroupPosition();

        // Enables despicable mode
        if (playerModel.isDespicable()) {
            minionModel.enableDespicableMode();
        }

        stage.setOnCloseRequest(event -> {
            view.getGameTileGroupPane().shutdown();// Releases resources from other threads
        });

        // PART 1
        setUpListenersPart1();
    }

    // Helper method to undo highlights on tiles for certain situations
    private void clearMinionHighlights(Player clearFromPlayer1, Player clearFromPlayer2) {
        List<Minion> allMinions = new ArrayList<>();


        if (clearFromPlayer1 != null) {
            allMinions.addAll(clearFromPlayer1.getMinions());
        }

        if (clearFromPlayer2 != null) {
            allMinions.addAll(clearFromPlayer2.getMinions());
        }

        for (Minion minion : allMinions) {
            Tile tile = minion.getOccupiedTile();
            if (tile != null) {
                int x = tile.getXCoord();
                int y = tile.getYCoord();
                HexTile minionHexTile = view.getGameTileGroupPane().getHexTileGrid()[x][y];
                minionHexTile.clearHighlight();
            }
        }
    }



    private Color getHighlightColor(boolean offensive, List<Tile> tilesInRadius) {
        boolean conditionMet = false;

        for (Tile tileInRadius : tilesInRadius) {
            Minion minion = tileInRadius.getOccupant();
            if (minion != null) {
                boolean isOwnedByCurrentPlayer = minion.getOwner().equals(playerModel.getCurrentPlayer());
                if ((!offensive && isOwnedByCurrentPlayer) || (offensive && !isOwnedByCurrentPlayer)) {
                    conditionMet = true;
                    break;
                }
            }
        }

        return conditionMet ? Color.BLUE : Color.RED;
    }

    public Region getView() {
        return this.view.getView();
    }

    /**
     * @param tile Center of the range
     * @return Optional List of all affected tiles
     */
    private List<Tile> highlightRange(Tile tile, int minRange, int maxRange, Color color) {
        List<Tile> tilesInRadius = tileModel.getTilesInRadius(tile, minRange, maxRange);
        for (Tile tileInRadius : tilesInRadius) {
            HexTile hexTile = view.getHexTile(tileInRadius);
            if (hexTile != null) {
                if (!color.equals(hexTile.getHighlightColor())) {
                    hexTile.highlight(color);
                }
            }
        }

        return tilesInRadius;
    }

    private List<Tile> highlightRange(HexTile hexTile, int minRange, int maxRange, Color color) {
        return highlightRange(hexTile.getTile(), minRange, maxRange, color);
    }

    private List<Tile> highlightRange(Tile tile, int minRange, int maxRange, boolean offensive) {
        List<Tile> tilesInRadius = tileModel.getTilesInRadius(tile, minRange, maxRange);
        Color highlightColor = getHighlightColor(offensive, tilesInRadius);

        for (Tile tileInRadius : tilesInRadius) {
            HexTile hexTile = view.getHexTile(tileInRadius);
            if (hexTile != null) {
                if (!highlightColor.equals(hexTile.getHighlightColor())) { //Improves performance by not re-highlighting tiles
                    hexTile.highlight(highlightColor);
                }
            }
        }


        return tilesInRadius;
    }

    private void invalidateAndUpdateSelectedMinion() {
        Tile selectedTile = tileModel.getSelectedTile();

        if (selectedTile != null && selectedTile.isOccupied()) {
            selectedTile.getOccupant();

            // invalidate the selected minion in the view
            tileModel.setSelectedTile(null);
            tileModel.setSelectedTile(selectedTile);  // Re-select the minion to trigger a UI update
        }
    }

    private boolean isAttackTab(Tab tab) {
        return tab.equals(view.getActionsTabPane().getAttackTab());
    }

    private boolean isMoveTab(Tab tab) {
        return tab.equals(view.getActionsTabPane().getMoveTab());
    }

    private boolean isSpecialTab(Tab tab) {
        return tab.equals(view.getActionsTabPane().getSpecialTab());
    }

    private void setSelected(HexTile hexTile) {
        if (hexTile != null) {
            tileModel.setSelectedTile(hexTile.getTile());
        } else {
            tileModel.setSelectedTile(null);
        }
    }

    private void setUpListenersPart1() {
        view.getMinionsTableView().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Unselect the tile when selecting a minion
            if (newValue != null) {
                view.getGameTileGroupPane().setSelectedHexTile(null);
            }
        });

        view.getEndTurnButton().setOnAction(event -> {
            this.playerModel.nextPlayer();
            view.getMinionsTableView().getSelectionModel().clearSelection();
            view.getGameTileGroupPane().getHexTiles().forEach(hexTile -> {
                if (!hexTile.getHighlightColor().equals(Color.TRANSPARENT)) {
                    hexTile.clearHighlight();
                }
            });
            view.getGameTileGroupPane().setSelectedHexTile(null);
        });

        view.getGameTileGroupPane().setOnMouseClicked(event -> {
            Object eventSource = event.getTarget();
            if (eventSource instanceof HexTile hexTile && event.getButton() == MouseButton.PRIMARY) {
                Tile tile = hexTile.getTile();
                Minion selectedMinion = view.getMinionsTableView().getSelectionModel().getSelectedItem();
                Player currentPlayer = this.playerModel.getCurrentPlayer();

                // Player wants to place a minion
                // Only on traversable home bases with same id
                if (selectedMinion != null && tile.isHomeBase() && tile.getHomebase() == currentPlayer.getHomeBaseID() && !tile.isOccupied() && tile.isTraversable()) {
                    // Create a new instance of the minion
                    Minion newMinion = selectedMinion.copy();
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


        getView().setOnKeyPressed(event -> {
            Object eventSource = event.getTarget();
            if (event.getCode() == KeyCode.R) {
                view.resetGameGroupPosition();
            }
            // Logic for deleting minion
            if (eventSource instanceof ZoomableScrollPane && event.getCode() == KeyCode.DELETE) {
                Tile selectedTile = tileModel.getSelectedTile();
                if (selectedTile != null) {
                    Player currentPlayer = playerModel.getCurrentPlayer();
                    Minion occupant = selectedTile.getOccupant();
                    if (selectedTile.isOccupied() && occupant.getOwner().equals(currentPlayer)) {
                        selectedTile.setOccupant(null); // Remove minion from field
                        tileModel.setSelectedTile(null); // Unselect selected tile
                        currentPlayer.removeMinion(occupant); // Remove minion from player
                        currentPlayer.addMoney(occupant.getCost()); // Refund minion cost

                    }
                }

            }
        });
        turnCounterListener = (observable, oldValue, newValue) -> {
            if (newValue.intValue() == playerModel.getPlayers().size()) {
                startNextPhase();
            }
        };

        // Starts phase 2 after 2 turns passed
        playerModel.turnCounterProperty().addListener(turnCounterListener);
    }

    private void setUpListenersPart2() {
        // Handles selecting tiles
        view.getGameTileGroupPane().setOnMouseClicked(event -> {
            Object eventSource = event.getTarget();
            if (eventSource instanceof HexTile hexTile && event.getButton() == MouseButton.PRIMARY) {
                Tile tile = hexTile.getTile();
                Player currentPlayer = this.playerModel.getCurrentPlayer();

                if (tile.isOccupied() && tile.getOccupant().getOwner().equals(currentPlayer)) {
                    // Select the tile
                    setSelected(hexTile);
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

        // Attach the listener to player's minions list
        playerModel.getPlayer1().getMinions().addListener(player1WinListener);
        playerModel.getPlayer2().getMinions().addListener(player2WinListener);


        // sets powerListview bindings and ensures selection is cleared
        playerModel.currentPlayerProperty().addListener((obs, oldPlayer, newPlayer) -> {
            ListView<Power> powerListView = view.getPart2MenuContainer().getActionsPane().getPowerListView();
            if (newPlayer != null) {
                powerListView.itemsProperty().bind(Bindings.createObjectBinding(newPlayer::getAvailablePowers));

                // Force update: clear power selection when the turn changes
                Platform.runLater(() -> {
                    powerListView.getSelectionModel().clearSelection();
                    setSelected(null);
                    invalidateAndUpdateSelectedMinion();
                });
            } else {
                powerListView.setItems(FXCollections.observableArrayList()); // Clear if no player
            }
        });

        // Refresh ui when
        tileModel.selectedTileProperty().addListener((observable) -> updateActionUI(view.getActionsTabPane().getSelectionModel().getSelectedItem()));
        view.getActionsTabPane().getSelectionModel().selectedItemProperty().addListener(this::changed);


        view.getEndTurnButton().setOnAction(event -> playerModel.nextPlayer());
    }

    private void startNextPhase() {

        // Remove old selection logic
        view.getView().setOnKeyPressed(null);

        //Clear homebase highlights
        Platform.runLater(() -> view.getGameTileGroupPane().getHexTiles().forEach(hexTile -> clearHighlights()));
        // Prevent listener duplication on replay
        playerModel.turnCounterProperty().removeListener(turnCounterListener);

        view.changeGamePhase();
        setUpListenersPart2();
        stage.setMinWidth(650);
        stage.setMinHeight(600);

    }

    private void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
        updateActionUI(newValue);
    }

    // Logic for all actions
    private void updateActionUI(Tab selectedTab) {
        clearHighlights();
        clearPreviousEventHandlers();
        setupRestAndTurnButtons();
        updateSelectedTileBindings();

        if (selectedTab == null) return;

        lastTab = selectedTab;

        if (isSpecialTab(selectedTab)) {
            setupSpecialTabHandlers();
        } else if (isAttackTab(selectedTab)) {
            setupAttackTabHandlers();
        } else if (isMoveTab(selectedTab)) {
            setupMoveTabHandlers();
        }
    }

    private void clearHighlights() {
        view.getGameTileGroupPane().getHexTiles().forEach(HexTile::clearHighlight);
    }


    private void clearPreviousEventHandlers() {
        if (specialMouseClickedHandler != null) {
            view.getGameTileGroupPane().removeEventFilter(MouseEvent.MOUSE_CLICKED, specialMouseClickedHandler);
        }
        if (specialMouseMovedHandler != null) {
            view.getGameTileGroupPane().removeEventFilter(MouseEvent.MOUSE_MOVED, specialMouseMovedHandler);
        }
    }

    private void setupRestAndTurnButtons() {
        Player currentPlayer = playerModel.getCurrentPlayer();
        Button restButton = view.getRestButton();
        Button endTurnButton = view.getPart2MenuContainer().getEndTurnButton();

        restButton.disableProperty().unbind();
        endTurnButton.disableProperty().unbind();

        endTurnButton.disableProperty().bind(Bindings.createBooleanBinding(() -> currentPlayer.getMinions().stream().anyMatch(Minion::hasActions), currentPlayer.getMinions()));
    }

    private void updateSelectedTileBindings() {
        Tile selectedTile = tileModel.getSelectedTile();
        if (selectedTile != null && selectedTile.isOccupied()) {
            Minion occupant = selectedTile.getOccupant();
            Button restButton = view.getRestButton();
            Tab attackTab = view.getActionsTabPane().getAttackTab();
            Tab moveTab = view.getActionsTabPane().getMoveTab();

            restButton.disableProperty().bind(Bindings.createBooleanBinding(() -> occupant.hasAttacked() || occupant.hasMoved(), occupant.attackedProperty(), occupant.movedProperty()));

            restButton.setOnAction(event -> {
                occupant.rest();
                changed(null, null, lastTab);
            });

            attackTab.disableProperty().bind(occupant.attackedProperty());
            moveTab.disableProperty().bind(occupant.movedProperty());
        }
    }

    private void setupAttackTabHandlers() {
        Tile selectedTile = tileModel.getSelectedTile();
        if (selectedTile == null || !selectedTile.isOccupied()) return;

        HexTile hexTile = view.getHexTile(selectedTile);
        if (hexTile == null) return;

        Minion occupant = selectedTile.getOccupant();
        if (occupant == null || occupant.hasAttacked()) return;

        ToggleButton attackButton = view.getActionsTabPane().getAttackButton();
        ToggleButton specialAttackButton = view.getActionsTabPane().getSpecialAttackButton();
        attackButton.setSelected(true); // Default to normal attack

        attackButton.disableProperty().unbind();
        specialAttackButton.disableProperty().unbind();

        Button healButton = view.getActionsTabPane().getHealButton();
        Button skipButton = view.getActionsTabPane().getSkipButton();

        healButton.disableProperty().unbind();
        healButton.disableProperty().bind(occupant.healChargesProperty().lessThanOrEqualTo(0).or(occupant.defenceProperty().greaterThanOrEqualTo(occupant.getBaseDefence())));

        healButton.setOnAction(event -> {
            occupant.useHealCharge();
            occupant.heal(Minion.HEAL_CHARGE_VALUE);
            invalidateAndUpdateSelectedMinion();
            changed(null, null, lastTab);
        });

        skipButton.setOnAction(event -> {
            occupant.setAttacked(true);
            changed(null, null, lastTab);
        });

        int minRange = occupant.getRange().getFirst();
        int maxRange = occupant.getRange().getLast();
        List<Tile> attackableTiles = highlightRange(hexTile, minRange, maxRange, Color.RED);
        clearMinionHighlights(occupant.getOwner(), null);

        specialMouseClickedHandler = event -> {
            HexTile clickedHexTile = view.getGameTileGroupPane().getHexTileAt(event.getSceneX(), event.getSceneY());
            if (clickedHexTile == null || clickedHexTile.getTile() == null) return;

            Minion target = clickedHexTile.getTile().getOccupant();
            if (target != null && target.getOwner() != occupant.getOwner() // Tile contains enemy
                    && attackableTiles.contains(clickedHexTile.getTile()) // Tile is in reach
                    && clickedHexTile.getTile().isAbleToBeAttacked()) {   // Tile is attackable

                if (specialAttackButton.isSelected() && occupant.hasSpecialAttack()) {
                    occupant.specialAttack(target);
                } else {
                    occupant.attack(target);
                }

                clearHighlights();
                invalidateAndUpdateSelectedMinion();
            }
        };

        view.getGameTileGroupPane().addEventFilter(MouseEvent.MOUSE_CLICKED, specialMouseClickedHandler);
    }

    private void setupMoveTabHandlers() {
        Tile selectedTile = tileModel.getSelectedTile();
        if (selectedTile == null || !selectedTile.isOccupied()) return;

        HexTile hexTile = view.getHexTile(selectedTile);
        if (hexTile == null) return;

        Minion occupant = selectedTile.getOccupant();
        if (occupant == null || occupant.hasMoved()) return;

        int movement = occupant.getMovement();

        Thread thread = new Thread(() -> {
            List<Tile> reachableTiles = tileModel.getReachableTiles(hexTile.getTile(), movement);

            Platform.runLater(() -> {
                Button stayButton = view.getActionsTabPane().getStayButton();
                stayButton.setOnAction(event -> {
                    occupant.setMoved(true);
                    clearHighlights();
                    changed(null, null, lastTab);
                });

                reachableTiles.forEach(tile -> view.getHexTile(tile).highlight(Color.GREEN));
                clearMinionHighlights(playerModel.getPlayer1(), playerModel.getPlayer2());

                // Remove old handler
                if (specialMouseClickedHandler != null) {
                    view.getGameTileGroupPane().removeEventFilter(MouseEvent.MOUSE_CLICKED, specialMouseClickedHandler);
                }
                // New event handler for moving
                specialMouseClickedHandler = event -> {
                    HexTile clickedTile = view.getGameTileGroupPane().getHexTileAt(event.getSceneX(), event.getSceneY());
                    if (clickedTile != null && !clickedTile.getTile().isOccupied() && reachableTiles.contains(clickedTile.getTile()) && !occupant.hasMoved()) { // Only when tile is empty and reachable

                        occupant.moveTo(clickedTile.getTile());
                        clearHighlights();
                        setSelected(clickedTile);
                        event.consume();
                    }
                };

                view.getGameTileGroupPane().addEventFilter(MouseEvent.MOUSE_CLICKED, specialMouseClickedHandler);
            });
        });

        thread.setDaemon(true); // Does not prevent JVM exit
        thread.start();
    }


    private void setupSpecialTabHandlers() {
        view.getActionsTabPane().getPowerListView().getSelectionModel().clearSelection();

        // Highlights an area around the mouse to show the range of a power
        specialMouseMovedHandler = event -> {
            if (!isSpecialTab(view.getActionsTabPane().getSelectionModel().getSelectedItem())) return;
            clearHighlights();
            Power selectedPower = powerModel.getSelectedPower();
            if (selectedPower == null) return;

            HexTile tileUnderMouse = view.getGameTileGroupPane().getHexTileAt(event.getSceneX(), event.getSceneY());
            if (tileUnderMouse != null) {
                highlightRange(tileUnderMouse.getTile(), 0, selectedPower.getRadius(), selectedPower.isOffensive());
            }
        };

        // Logic for using power
        specialMouseClickedHandler = event -> {
            Power selectedPower = powerModel.getSelectedPower();
            HexTile clickedTile = view.getGameTileGroupPane().getHexTileAt(event.getSceneX(), event.getSceneY());
            Player currentPlayer = playerModel.getCurrentPlayer();

            if (clickedTile != null && selectedPower != null && currentPlayer.getAvailablePowerUses() > 0) {
                currentPlayer.usePower(selectedPower);
                selectedPower.apply(clickedTile, currentPlayer);
                view.getActionsTabPane().getPowerListView().getSelectionModel().clearSelection();
                clearHighlights();
                invalidateAndUpdateSelectedMinion();
            }
        };

        view.getGameTileGroupPane().addEventFilter(MouseEvent.MOUSE_MOVED, specialMouseMovedHandler);
        view.getGameTileGroupPane().addEventFilter(MouseEvent.MOUSE_CLICKED, specialMouseClickedHandler);
    }


    public void endGame(Player winner) {
        view.getGameTileGroupPane().shutdown(); // Close active background threads


        // Prevent duplication of game
        playerModel.getPlayer1().getMinions().removeListener(player1WinListener);
        playerModel.getPlayer2().getMinions().removeListener(player2WinListener);


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


}
