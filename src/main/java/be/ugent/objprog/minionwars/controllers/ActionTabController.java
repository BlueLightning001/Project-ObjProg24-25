package be.ugent.objprog.minionwars.controllers;

import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.PowerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import be.ugent.objprog.minionwars.powers.Power;
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.views.GameView;
import be.ugent.objprog.minionwars.views.HexTile;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Controller responsible for handling tab-specific actions (Attack, Move, Special).
 */
public class ActionTabController {
    private final TileModel tileModel;
    private final GameView view;
    private final PlayerModel playerModel;
    private final PowerModel powerModel;
    private final TileController tileController;

    private EventHandler<MouseEvent> specialMouseClickedHandler;
    private EventHandler<MouseEvent> specialMouseMovedHandler;
    private Tab lastTab;

    public ActionTabController(TileModel tileModel, GameView view, PlayerModel playerModel, PowerModel powerModel, TileController tileController) {
        this.tileModel = tileModel;
        this.view = view;
        this.playerModel = playerModel;
        this.powerModel = powerModel;
        this.tileController = tileController;
    }

    /**
     * Updates the UI based on the selected tab.
     *
     * @param selectedTab The currently selected tab
     */
    public void updateActionUI(Tab selectedTab) {
        tileController.clearHighlights();
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

    /**
     * Checks if the given tab is the Attack tab.
     *
     * @param tab The tab to check
     * @return True if the tab is the Attack tab, false otherwise
     */
    public boolean isAttackTab(Tab tab) {
        return tab.equals(view.getActionsTabPane().getAttackTab());
    }

    /**
     * Checks if the given tab is the Move tab.
     *
     * @param tab The tab to check
     * @return True if the tab is the Move tab, false otherwise
     */
    public boolean isMoveTab(Tab tab) {
        return tab.equals(view.getActionsTabPane().getMoveTab());
    }

    /**
     * Checks if the given tab is the Special tab.
     *
     * @param tab The tab to check
     * @return True if the tab is the Special tab, false otherwise
     */
    public boolean isSpecialTab(Tab tab) {
        return tab.equals(view.getActionsTabPane().getSpecialTab());
    }

    /**
     * Clears previous event handlers to prevent duplication.
     */
    public void clearPreviousEventHandlers() {
        if (specialMouseClickedHandler != null) {
            view.getGameTileGroupPane().removeEventFilter(MouseEvent.MOUSE_CLICKED, specialMouseClickedHandler);
        }
        if (specialMouseMovedHandler != null) {
            view.getGameTileGroupPane().removeEventFilter(MouseEvent.MOUSE_MOVED, specialMouseMovedHandler);
        }
    }

    /**
     * Sets up the rest and turn buttons.
     */
    private void setupRestAndTurnButtons() {
        Button restButton = view.getRestButton();
        Button endTurnButton = view.getPart2MenuContainer().getEndTurnButton();

        restButton.disableProperty().unbind();
        endTurnButton.disableProperty().unbind();

        endTurnButton.disableProperty().bind(Bindings.createBooleanBinding(
            () -> playerModel.getCurrentPlayer().getMinions().stream().anyMatch(Minion::hasActions), 
            playerModel.getCurrentPlayer().getMinions()
        ));
    }

    /**
     * Updates the bindings for the selected tile.
     */
    private void updateSelectedTileBindings() {
        Tile selectedTile = tileModel.getSelectedTile();
        if (selectedTile != null && selectedTile.isOccupied()) {
            Minion occupant = selectedTile.getOccupant();
            Button restButton = view.getRestButton();
            Tab attackTab = view.getActionsTabPane().getAttackTab();
            Tab moveTab = view.getActionsTabPane().getMoveTab();

            restButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> occupant.hasAttacked() || occupant.hasMoved(), 
                occupant.attackedProperty(), occupant.movedProperty()
            ));

            restButton.setOnAction(event -> {
                occupant.rest();
                updateActionUI(lastTab);
            });

            attackTab.disableProperty().bind(occupant.attackedProperty());
            moveTab.disableProperty().bind(occupant.movedProperty());
        }
    }

    /**
     * Sets up handlers for the Attack tab.
     */
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
        healButton.disableProperty().bind(
            occupant.healChargesProperty().lessThanOrEqualTo(0)
            .or(occupant.defenceProperty().greaterThanOrEqualTo(occupant.getBaseDefence()))
        );

        healButton.setOnAction(event -> {
            occupant.useHealCharge();
            occupant.heal(Minion.HEAL_CHARGE_VALUE);
            invalidateAndUpdateSelectedMinion();
            updateActionUI(lastTab);
        });

        skipButton.setOnAction(event -> {
            occupant.setAttacked(true);
            updateActionUI(lastTab);
        });

        int minRange = occupant.getRange().getFirst();
        int maxRange = occupant.getRange().getLast();
        List<Tile> attackableTiles = tileController.highlightRange(hexTile, minRange, maxRange, Color.RED);
        tileController.clearMinionHighlights(occupant.getOwner(), null);

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

                tileController.clearHighlights();
                invalidateAndUpdateSelectedMinion();
            }
        };

        view.getGameTileGroupPane().addEventFilter(MouseEvent.MOUSE_CLICKED, specialMouseClickedHandler);
    }

    /**
     * Sets up handlers for the Move tab.
     */
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
                    tileController.clearHighlights();
                    updateActionUI(lastTab);
                });

                reachableTiles.forEach(tile -> view.getHexTile(tile).highlight(Color.GREEN));
                tileController.clearMinionHighlights(playerModel.getPlayer1(), playerModel.getPlayer2());

                // Remove old handler
                if (specialMouseClickedHandler != null) {
                    view.getGameTileGroupPane().removeEventFilter(MouseEvent.MOUSE_CLICKED, specialMouseClickedHandler);
                }
                // New event handler for moving
                specialMouseClickedHandler = event -> {
                    HexTile clickedTile = view.getGameTileGroupPane().getHexTileAt(event.getSceneX(), event.getSceneY());
                    if (clickedTile != null && !clickedTile.getTile().isOccupied() && reachableTiles.contains(clickedTile.getTile()) && !occupant.hasMoved()) { // Only when tile is empty and reachable

                        occupant.moveTo(clickedTile.getTile());
                        tileController.clearHighlights();
                        tileController.setSelected(clickedTile);
                        event.consume();
                    }
                };

                view.getGameTileGroupPane().addEventFilter(MouseEvent.MOUSE_CLICKED, specialMouseClickedHandler);
            });
        });

        thread.setDaemon(true); // Does not prevent JVM exit
        thread.start();
    }

    /**
     * Sets up handlers for the Special tab.
     */
    private void setupSpecialTabHandlers() {
        view.getActionsTabPane().getPowerListView().getSelectionModel().clearSelection();

        // Highlights an area around the mouse to show the range of a power
        specialMouseMovedHandler = event -> {
            if (!isSpecialTab(view.getActionsTabPane().getSelectionModel().getSelectedItem())) return;
            tileController.clearHighlights();
            Power selectedPower = powerModel.getSelectedPower();
            if (selectedPower == null) return;

            HexTile tileUnderMouse = view.getGameTileGroupPane().getHexTileAt(event.getSceneX(), event.getSceneY());
            if (tileUnderMouse != null) {
                tileController.highlightRange(tileUnderMouse.getTile(), 0, selectedPower.getRadius(), selectedPower.isOffensive());
            }
        };

        // Logic for using power
        specialMouseClickedHandler = event -> {
            Power selectedPower = powerModel.getSelectedPower();
            HexTile clickedTile = view.getGameTileGroupPane().getHexTileAt(event.getSceneX(), event.getSceneY());

            if (clickedTile != null && selectedPower != null && playerModel.getCurrentPlayer().getAvailablePowerUses() > 0) {
                playerModel.getCurrentPlayer().usePower(selectedPower);
                selectedPower.apply(clickedTile, playerModel.getCurrentPlayer());
                view.getActionsTabPane().getPowerListView().getSelectionModel().clearSelection();
                tileController.clearHighlights();
                invalidateAndUpdateSelectedMinion();
            }
        };

        view.getGameTileGroupPane().addEventFilter(MouseEvent.MOUSE_MOVED, specialMouseMovedHandler);
        view.getGameTileGroupPane().addEventFilter(MouseEvent.MOUSE_CLICKED, specialMouseClickedHandler);
    }

    /**
     * Invalidates and updates the selected minion to trigger a UI update.
     */
    public void invalidateAndUpdateSelectedMinion() {
        Tile selectedTile = tileModel.getSelectedTile();

        if (selectedTile != null && selectedTile.isOccupied()) {
            selectedTile.getOccupant();

            // invalidate the selected minion in the view
            tileModel.setSelectedTile(null);
            tileModel.setSelectedTile(selectedTile);  // Re-select the minion to trigger a UI update
        }
    }

    /**
     * Gets the last selected tab.
     *
     * @return The last selected tab
     */
    public Tab getLastTab() {
        return lastTab;
    }

    /**
     * Sets the last selected tab.
     *
     * @param lastTab The tab to set as the last selected tab
     */
    public void setLastTab(Tab lastTab) {
        this.lastTab = lastTab;
    }
}
