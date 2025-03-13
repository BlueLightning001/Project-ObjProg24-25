package be.ugent.objprog.minionwars.tiles;

import be.ugent.objprog.minionwars.models.TileModel;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;

import java.util.List;
import java.util.Map;

public class TilePane extends Pane {
    private static final double r = 20; // Hexagon outer radius
    private static final double n = Math.sqrt(r * r * 0.75); // Hexagon width offset
    private static final double TILE_HEIGHT = 2 * r;
    private static final double TILE_WIDTH = 2 * n;



    private final Tile[][] tileGrid;

    public TilePane(TileModel tileModel) {
        List<Tile> initialTiles = tileModel.getInitialTiles();
        this.tileGrid = tileModel.getTileGrid();

        // Loop through the tiles and create visual representations
        for (int i = 0; i < tileGrid.length; i++) {
            for (int j = 0; j < tileGrid[i].length; j++) {
                double xCoord = j * TILE_WIDTH + (i % 2) * n;
                double yCoord = j * TILE_HEIGHT * 0.75;
                Polygon hexTile = new HexTile(xCoord, yCoord, tileGrid[i][j]);
                getChildren().add(hexTile);
            }
        }
    }



    public Tile[][] get2DTileArray() {
        return tileGrid;
    }
}
