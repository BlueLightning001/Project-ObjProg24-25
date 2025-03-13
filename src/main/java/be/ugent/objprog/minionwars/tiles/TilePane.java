package be.ugent.objprog.minionwars.tiles;

import be.ugent.objprog.minionwars.models.TileModel;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;

import java.util.List;

public class TilePane extends Pane {
    private static final double r = 20; // Hexagon outer radius
    private static final double n = Math.sqrt(r * r * 0.75); // Hexagon width offset
    private static final double TILE_HEIGHT = 2 * r;
    private static final double TILE_WIDTH = 2 * n;


    private final Tile[][] tileGrid;

    public TilePane(TileModel tileModel, List<Tile> initialTiles) {

        this.tileGrid = tileModel.getTileGrid();

        // Loop through the tiles and create visual representations
        for (Tile tile : initialTiles) {
            double xCoord = tile.getXCoord() * TILE_WIDTH + (tile.getYCoord() % 2) * n;
            double yCoord = tile.getYCoord() * TILE_HEIGHT * 0.75;
            Polygon hexTile = new HexTile(xCoord, yCoord, tile);
            getChildren().add(hexTile);
        }
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public Tile[][] get2DTileArray() {
        return tileGrid;
    }
}
