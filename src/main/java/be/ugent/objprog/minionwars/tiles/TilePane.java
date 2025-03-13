package be.ugent.objprog.minionwars.tiles;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;

import java.util.List;

public class TilePane extends Pane {
    private static final double r = 20; // Hexagon outer radius
    private static final double n = Math.sqrt(r * r * 0.75); // Hexagon width offset
    private static final double TILE_HEIGHT = 2 * r;
    private static final double TILE_WIDTH = 2 * n;

    private final List<Tile> tiles;
    private Tile[][] tileGrid;

    public TilePane(List<Tile> tiles) {
        this.tiles = tiles;
        int maxX = 0;
        int maxY = 0;

        for (Tile tile : tiles) {
            // Update grid size
            maxX = Math.max(maxX, tile.getXCoord());
            maxY = Math.max(maxY, tile.getYCoord());

            // visual tile representation
            double xCoord = tile.getXCoord() * TILE_WIDTH + (tile.getYCoord() % 2) * n;
            double yCoord = tile.getYCoord() * TILE_HEIGHT * 0.75;
            Polygon hexTile = new HexTile(xCoord, yCoord, tile);
            getChildren().add(hexTile);
        }


        tileGrid = new Tile[maxX + 1][maxY + 1];

        // Fill tileGrid
        for (Tile tile : tiles) {
            tileGrid[tile.getXCoord()][tile.getYCoord()] = tile;
        }

        // Fill missing tiles with VoidTiles
        fillEmptyTiles();
    }

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

    public List<Tile> getTiles() {
        return tiles;
    }

    public Tile[][] get2DTileArray() {
        return tileGrid;
    }
}
