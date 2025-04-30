package be.ugent.objprog.minionwars.controllers;

import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.views.GameView;
import be.ugent.objprog.minionwars.views.HexTile;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for tile highlighting and selection logic.
 */
public class TileController {
    private final TileModel tileModel;
    private final GameView view;
    private final PlayerModel playerModel;

    public TileController(TileModel tileModel, GameView view, PlayerModel playerModel) {
        this.tileModel = tileModel;
        this.view = view;
        this.playerModel = playerModel;
    }

    /**
     * Sets the selected tile.
     *
     * @param hexTile The tile to select
     */
    public void setSelected(HexTile hexTile) {
        if (hexTile != null) {
            tileModel.setSelectedTile(hexTile.getTile());
        } else {
            tileModel.setSelectedTile(null);
        }
    }

    /**
     * Clears all highlights from the game board.
     */
    public void clearHighlights() {
        view.getGameTileGroupPane().getHexTiles().forEach(HexTile::clearHighlight);
    }

    /**
     * Clears highlights from minions belonging to the specified players.
     *
     * @param clearFromPlayer1 First player whose minions' highlights should be cleared
     * @param clearFromPlayer2 Second player whose minions' highlights should be cleared
     */
    public void clearMinionHighlights(Player clearFromPlayer1, Player clearFromPlayer2) {
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

    /**
     * Determines the highlight color based on whether the action is offensive and if there are valid targets.
     *
     * @param offensive Whether the action is offensive
     * @param tilesInRadius The tiles within the radius of the action
     * @return The color to use for highlighting
     */
    public Color getHighlightColor(boolean offensive, List<Tile> tilesInRadius) {
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

    /**
     * Highlights tiles within a specified range with a given color.
     *
     * @param tile Center of the range
     * @param minRange Minimum range
     * @param maxRange Maximum range
     * @param color Color to use for highlighting
     * @return List of tiles within the specified range
     */
    public List<Tile> highlightRange(Tile tile, int minRange, int maxRange, Color color) {
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

    /**
     * Highlights tiles within a specified range from a HexTile with a given color.
     *
     * @param hexTile Center of the range
     * @param minRange Minimum range
     * @param maxRange Maximum range
     * @param color Color to use for highlighting
     * @return List of tiles within the specified range
     */
    public List<Tile> highlightRange(HexTile hexTile, int minRange, int maxRange, Color color) {
        return highlightRange(hexTile.getTile(), minRange, maxRange, color);
    }

    /**
     * Highlights tiles within a specified range with a color determined by whether the action is offensive.
     *
     * @param tile Center of the range
     * @param minRange Minimum range
     * @param maxRange Maximum range
     * @param offensive Whether the action is offensive
     * @return List of tiles within the specified range
     */
    public List<Tile> highlightRange(Tile tile, int minRange, int maxRange, boolean offensive) {
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
}