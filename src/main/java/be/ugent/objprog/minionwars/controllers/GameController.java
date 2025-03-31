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
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.views.GameView;
import be.ugent.objprog.minionwars.views.HexTile;
import be.ugent.objprog.minionwars.views.VictoryPane;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class GameController {
    private final Locale locale;
    private final MinionModel minionModel;
    private final TileModel tileModel;
    private final PowerModel powerModel;
    private final ResourceBundle bundle;
    private GameView view;
    private PlayerModel playerModel;
    private Stage stage;
    private EventHandler<MouseEvent> specialMouseClickedHandler;
    private EventHandler<MouseEvent> specialMouseMovedHandler;
    private JDOMReader jdomReader;
    private ListChangeListener<Minion> player1WinListener;
    private ListChangeListener<Minion> player2WinListener;
    private final ChangeListener<Number> turnCounterListener = (observable, oldValue, newValue) -> {
        if (newValue.intValue() == 2) {
            startNextPhase();
        }
    };

    public GameController(Stage stage, PlayerModel playerModel, Locale locale, JDOMReader reader) {
        this.stage = stage;
        this.playerModel = playerModel;
        this.minionModel = new MinionModel(reader);
        this.tileModel = new TileModel(reader, locale);
        this.powerModel = new PowerModel(reader, locale);
        this.jdomReader = reader;
        // Load the players powers
        this.playerModel.getPlayer1().setAvailablePowers(FXCollections.observableArrayList(powerModel.getPowerList()));
        this.playerModel.getPlayer2().setAvailablePowers(FXCollections.observableArrayList(powerModel.getPowerList()));

        this.locale = locale;
        this.view = new GameView(minionModel, playerModel, tileModel, powerModel, locale);
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
                if (selectedMinion != null && tile.isHomeBase() && tile.getHomebase() == currentPlayer.getHomeBaseID()
                        && !tile.isOccupied() && tile.isTraversable()) {
                    // Create a new instance of the minion
                    Minion newMinion = selectedMinion.clone();
                    newMinion.setOwner(currentPlayer);

                    // Deduct money and place minion
                    currentPlayer.removeMoney(newMinion.getCost());
                    currentPlayer.addMinion(newMinion);
                    tile.setOccupant(newMinion);

                } else if (tile.isOccupied() && tile.getOccupant().getOwner().equals(currentPlayer)) {
                    // Select the tile
                    view.getGameTileGroupPane().setSelectedHexTile(hexTile);
                }

                view.getMinionsTableView().getSelectionModel().clearSelection();
            }
        });

        // Logic for deleting minion
        getView().setOnKeyPressed(event -> {
            Object eventSource = event.getTarget();
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
        // Starts phase 2 after 2 turns passed
        playerModel.turnCounterProperty().addListener(turnCounterListener);
    }

    public Region getView() {
        return this.view.getView();
    }

    private void startNextPhase() {
        System.out.println("STARTING NEXT PHASE");

        // Remove old selection logic
        view.getView().setOnKeyPressed(null);

        //Clear homebase highlights
        Platform.runLater(() -> {
            view.getGameTileGroupPane().getHexTiles().forEach(hexTile -> {
                clearHighlights();
            });
        });
        // Prevent listener duplication on replay
        playerModel.turnCounterProperty().removeListener(turnCounterListener);

        view.changeGamePhase();
        setUpListenersPart2();
        stage.setMinWidth(650);
        stage.setMinHeight(400);

    }

    private void clearHighlights() {
        view.getGameTileGroupPane().getHexTiles().forEach(HexTile::clearHighlight);
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
                System.out.println("GAME ENDING DETECTED1: " + player1WinListener) ;
                endGame(player1);
            }
        };
        player2WinListener = change -> {
            // Every time player2's minions change, check if they are empty.
            Player player2 = playerModel.getPlayer2();
            if (player2 != null && player2.getMinions().isEmpty()) {
                System.out.println("GAME ENDING DETECTED2: " + player2WinListener) ;
                endGame(player2);
            }
        };

        // Attach the listener to player's minions list
        System.out.println("Adding player1WinListener: " + player1WinListener);
        playerModel.getPlayer1().getMinions().addListener(player1WinListener);
        System.out.println("Adding player2WinListener: " + player2WinListener);
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
        tileModel.selectedTileProperty().addListener((observable) -> {
            updateActionUI(view.getActionsTabPane().getSelectionModel().getSelectedItem());
        });
        view.getActionsTabPane().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateActionUI(newValue);
        });


        view.getEndTurnButton().setOnAction(event -> {
            playerModel.nextPlayer();
        });
    }

    private void updateActionUI(Tab selectedTab) {
        clearHighlights();


        Player currentPlayer = this.playerModel.getCurrentPlayer();
        Tile selectedTile = tileModel.getSelectedTile();
        HexTile hexTile = view.getHexTile(selectedTile);
        Color attackColor = Color.RED;
        Color moveColor = Color.GREEN;

        Button restButton = view.getRestButton();
        Button endTurnButton = view.getPart2MenuContainer().getEndTurnButton();
        Button stayButton = view.getActionsTabPane().getStayButton();


        restButton.disableProperty().unbind();
        endTurnButton.disableProperty().unbind();
        endTurnButton.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> currentPlayer.getMinions().stream().anyMatch(Minion::hasActions),
                        currentPlayer.getMinions()
                )
        );



        // Remove previous event handlers before adding new ones
        if (specialMouseClickedHandler != null) {
            view.getGameTileGroupPane().removeEventFilter(MouseEvent.MOUSE_CLICKED, specialMouseClickedHandler);
        }
        if (specialMouseMovedHandler != null) {
            view.getGameTileGroupPane().removeEventFilter(MouseEvent.MOUSE_MOVED, specialMouseMovedHandler);
        }
        Tab attackTab = view.getActionsTabPane().getAttackTab();
        Tab moveTab = view.getActionsTabPane().getMoveTab();

        attackTab.disableProperty().unbind();
        moveTab.disableProperty().unbind();

        if (selectedTile != null && selectedTile.isOccupied()) {
            Minion occupant = selectedTile.getOccupant();
            restButton.disableProperty().bind(
                    Bindings.createBooleanBinding(
                            () -> occupant.hasAttacked() || occupant.hasMoved(),
                            occupant.attackedProperty(),
                            occupant.movedProperty()
                    )
            );

            restButton.setOnAction(event -> {
                occupant.rest();

                updateActionUI(view.getActionsTabPane().getSelectionModel().getSelectedItem());
            });

            attackTab.disableProperty().bind(occupant.attackedProperty());
            moveTab.disableProperty().bind(occupant.movedProperty());
        } else {
            attackTab.setDisable(true);
            moveTab.setDisable(true);
        }

        if (selectedTab == null) return;

        String tabText = selectedTab.getText();

        if (tabText.equals(bundle.getString("actions.special"))) {
            // Clear selection to avoid auto-triggering when switching tabs
            view.getActionsTabPane().getPowerListView().getSelectionModel().clearSelection();

            specialMouseMovedHandler = event -> {

                if (view.getActionsTabPane() == null || !view.getActionsTabPane().getSelectionModel().getSelectedItem().getText().equals(bundle.getString("actions.special"))) return;

                clearHighlights();
                // Get the currently selected power
                Power selectedPower = powerModel.getSelectedPower();
                if (selectedPower == null) {
                    clearHighlights();
                    return;
                }

                // Get the tile under the mouse
                HexTile tileUnderMouse = view.getGameTileGroupPane().getHexTileAt(event.getSceneX(), event.getSceneY());
                if (tileUnderMouse != null) {
                    highLightRadius(tileUnderMouse, selectedPower.getRadius(), Color.BLUE);
                }
            };

            specialMouseClickedHandler = event -> {
                Power selectedPower = powerModel.getSelectedPower();
                HexTile clickedTile = view.getGameTileGroupPane().getHexTileAt(event.getSceneX(), event.getSceneY());
                if (clickedTile != null && selectedPower != null && currentPlayer.getAvailablePowerUses() > 0) {
                    currentPlayer.usePower(selectedPower);
                    selectedPower.apply(clickedTile,currentPlayer);

                    // Clear selection so the power is not used again automatically
                    view.getActionsTabPane().getPowerListView().getSelectionModel().clearSelection();
                    clearHighlights();

                    invalidateAndUpdateSelectedMinion();
                }
            };

            // Add the new event filters
            view.getGameTileGroupPane().addEventFilter(MouseEvent.MOUSE_MOVED, specialMouseMovedHandler);
            view.getGameTileGroupPane().addEventFilter(MouseEvent.MOUSE_CLICKED, specialMouseClickedHandler);

        } else if (tabText.equals(bundle.getString("actions.attack"))) {
            if (hexTile != null && hexTile.getTile().isOccupied()) {
                Minion occupant = hexTile.getTile().getOccupant();
                ToggleButton attackButton = view.getActionsTabPane().getAttackButton();
                ToggleButton specialAttackButton = view.getActionsTabPane().getSpecialAttackButton();
                ToggleGroup attackToggleGroup = attackButton.getToggleGroup();

                attackButton.setSelected(true);

                // Unbind existing properties
                attackButton.disableProperty().unbind();
                specialAttackButton.disableProperty().unbind();

                if (occupant != null && !occupant.hasAttacked()) {
                    Button healButton = view.getActionsTabPane().getHealButton();
                    Button skipButton = view.getActionsTabPane().getSkipButton();

                    healButton.disableProperty().unbind();
                    healButton.disableProperty().bind(occupant.healChargesProperty().lessThanOrEqualTo(0));
                    healButton.setOnAction(event -> {
                        occupant.useHealCharge();
                        occupant.heal(Minion.HEAL_CHARGE_VALUE);

                        invalidateAndUpdateSelectedMinion();
                        updateActionUI(null);
                    });

                    skipButton.setOnAction(event -> {
                        occupant.setAttacked(true);
                        updateActionUI(null);
                    });


                }


                if (occupant != null && hexTile.getTile().isAbleToAttack() && !occupant.hasAttacked()) {
                    int minRange = occupant.getRange().getFirst();
                    int maxRange = occupant.getRange().getLast();

                    // Get all attackable tiles
                    List<Tile> attackableTiles = highlightRange(hexTile, minRange, maxRange, attackColor);
                    clearMinionHighlights(occupant.getOwner(), null);


                    // Attack on click
                    specialMouseClickedHandler = event -> {
                        HexTile clickedHexTile = view.getGameTileGroupPane().getHexTileAt(event.getSceneX(), event.getSceneY());
                        if (clickedHexTile == null || clickedHexTile.getTile() == null) return;

                        Minion target = clickedHexTile.getTile().getOccupant();

                        // Ensure the tile contains a minion and is within attack range
                        if (target != null
                                && target.getOwner() != occupant.getOwner()  // Ensure it's an enemy
                                && attackableTiles.contains(clickedHexTile.getTile())) {  // Check if it's within range

                            if (specialAttackButton.isSelected() && occupant.hasSpecialAttack()) {
                                occupant.specialAttack(target);  // Execute special attack
                            } else {
                                occupant.attack(target);  // Execute normal attack
                            }

                            clearHighlights();
                            invalidateAndUpdateSelectedMinion();
                        }
                    };

                    // Attach the event handler
                    view.getGameTileGroupPane().addEventFilter(MouseEvent.MOUSE_CLICKED, specialMouseClickedHandler);
                }
            }
        }
        else if (tabText.equals(bundle.getString("actions.move"))) {
            if (hexTile != null && hexTile.getTile().isOccupied() && !hexTile.getTile().getOccupant().hasMoved() ) {
                Minion occupant = hexTile.getTile().getOccupant();
                if (occupant != null) {
                    int movement = occupant.getMovement();
                    List<Tile> reachableTiles = tileModel.getReachableTiles(hexTile.getTile(), movement);

                    stayButton.setOnAction(event -> {
                        occupant.setMoved(true);
                        clearHighlights();
                        updateActionUI(null);
                    });

                    reachableTiles.forEach(tile -> {
                        view.getHexTile(tile).highlight(moveColor);
                    });

                    clearMinionHighlights(playerModel.getPlayer1(), playerModel.getPlayer2());

                    // Remove any previous event filter before adding a new one
                    if (specialMouseClickedHandler != null) {
                        view.getGameTileGroupPane().removeEventFilter(MouseEvent.MOUSE_CLICKED, specialMouseClickedHandler);
                    }

                    // Define the event filter
                    specialMouseClickedHandler = event -> {
                        HexTile clickedTile = view.getGameTileGroupPane().getHexTileAt(event.getSceneX(), event.getSceneY());
                        if (clickedTile != null && !clickedTile.getTile().isOccupied() && reachableTiles.contains(clickedTile.getTile()) && !occupant.hasMoved()) {


                            occupant.moveTo(clickedTile.getTile()); // Move minion to new tile

                            clearHighlights(); // Remove highlights after moving
                            setSelected(clickedTile);

                            event.consume(); // Prevent other handlers from processing the event
                        }
                    };

                    // Add the event filter
                    view.getGameTileGroupPane().addEventFilter(MouseEvent.MOUSE_CLICKED, specialMouseClickedHandler);
                }
            }

        }
    }
    private void invalidateAndUpdateSelectedMinion() {
        // Assuming you have a tile model that tracks the selected tile
        Tile selectedTile = tileModel.getSelectedTile();

        if (selectedTile != null && selectedTile.isOccupied()) {
            Minion selectedMinion = selectedTile.getOccupant();

            // invalidate the selected minion in the view
            tileModel.setSelectedTile(null);
            tileModel.setSelectedTile(selectedTile);  // Re-select the minion to trigger a UI update
        }
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


    private void highLightRadius(HexTile hexTile, int radius, Color color) {
        if (hexTile != null) {
            highLightRadius(hexTile.getTile(), radius, color);
        }

    }

    private List<Tile> highlightRange(HexTile hexTile, int minRange, int maxRange, Color color) {
        return highlightRange(hexTile.getTile(), minRange, maxRange, color);
    }

    private void highLightRadius(Tile tile, int radius, Color color) {
        highlightRange(tile, 0, radius, color);
    }
    /**
     * @param tile Center of the range
     * @return Optional List of all affected tiles
     */
    private List<Tile> highlightRange(Tile tile, int minRange, int maxRange, Color color) {
        List<Tile> tilesInRadius = tileModel.getTilesInRadius(tile, minRange, maxRange);
        for (Tile tileInRadius : tilesInRadius) {

            HexTile hexTile = view.getHexTile(tileInRadius);
            hexTile.highlight(color);

        }
        return tilesInRadius;
    }
    private void setSelected(HexTile hexTile){
        if (hexTile != null) {
            tileModel.setSelectedTile(hexTile.getTile());
        } else {
            tileModel.setSelectedTile(null);
        }
    }
    public void endGame(Player winner) {
        view.getGameTileGroupPane().shutdown(); // Close active background threads



        // Prevent duplication of game
        playerModel.getPlayer1().getMinions().removeListener(player1WinListener);
        playerModel.getPlayer2().getMinions().removeListener(player2WinListener);


        boolean fullscreen = stage.isFullScreen();
        VictoryPane victoryScreen = new VictoryPane(winner,playerModel,powerModel,jdomReader,stage,locale);
        Scene scene = new Scene(victoryScreen,stage.getWidth(),stage.getHeight());
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
