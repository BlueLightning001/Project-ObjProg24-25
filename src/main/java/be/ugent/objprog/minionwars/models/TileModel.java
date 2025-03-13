package be.ugent.objprog.minionwars.models;

import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.tiles.VoidTile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class TileModel {
    private final List<Tile> initialTiles;
    private Tile[][] tileGrid;

    public TileModel() {
        this.initialTiles = new ArrayList<>();
        initializeTiles();
        initializeTileGrid();

    }

    public void initializeTiles() {
        //TODO

    }

    // Initialize the tile grid based on the tiles list
    private void initializeTileGrid() {
        // Find the grid size
        int maxX = 0;
        int maxY = 0;
        for (Tile tile : initialTiles) {
            maxX = Math.max(maxX, tile.getXCoord());
            maxY = Math.max(maxY, tile.getYCoord());
        }

        // Create the tileGrid with size based on the max coordinates
        tileGrid = new Tile[maxX + 1][maxY + 1];

        // Fill the tile grid with actual tiles
        for (Tile tile : initialTiles) {
            tileGrid[tile.getXCoord()][tile.getYCoord()] = tile;
        }

        // Fill missing tiles with VoidTiles
        fillEmptyTiles();
    }

    // Fill the empty spaces in the grid with VoidTiles
    private void fillEmptyTiles() {
        for (int i = 0; i < tileGrid.length; i++) {
            if (tileGrid[i] == null) {
                tileGrid[i] = new Tile[tileGrid[0].length]; // Ensure row exists
            }
            for (int j = 0; j < tileGrid[i].length; j++) {
                if (tileGrid[i][j] == null) {
                    tileGrid[i][j] = new VoidTile(i, j);
                }
            }
        }
    }

    public Tile getTile(int x, int y) {
        return tileGrid[x][y];
    }

    public Tile[][] getTileGrid() {
        return tileGrid;
    }

    // Methods to modify the tiles
    public void setTile(Tile tile) {
        tileGrid[tile.getXCoord()][tile.getYCoord()] = tile;
    }

    public void removeTile(Tile tile) {
        tileGrid[tile.getXCoord()][tile.getYCoord()] = null;
    }
}
