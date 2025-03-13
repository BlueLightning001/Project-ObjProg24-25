package be.ugent.objprog.minionwars.tiles;

import be.ugent.objprog.minionwars.models.TileModel;
import javafx.scene.Group;
import javafx.scene.shape.Polygon;

public class TileGroup extends Group {
    private static final double r = 20; // Hexagon outer radius
    private static final double n = Math.sqrt(r * r * 0.75); // Hexagon width offset
    private static final double TILE_HEIGHT = 2 * r;
    private static final double TILE_WIDTH = 2 * n;



    private final Tile[][] tileGrid;

    public TileGroup(TileModel tileModel) {
        this.tileGrid = tileModel.getTileGrid();
        int xStartOffset = 40; // offsets the entire field to the right
        int yStartOffset = 40; // offsets the entire fiels downwards

        // Loop through the tiles and create visual representations
        for (int i = 0; i < tileGrid.length; i++) {
            for (int j = 0; j < tileGrid[i].length; j++) {
                Tile tile = tileGrid[i][j];
                double xCoord = i * TILE_WIDTH + (j % 2) * n + xStartOffset;
                double yCoord = j * TILE_HEIGHT * 0.75 + yStartOffset;
                Polygon hexTile = new HexTile(xCoord, yCoord, tile);
                getChildren().add(hexTile);
            }
        }
    }



    public Tile[][] get2DTileArray() {
        return tileGrid;
    }
}
