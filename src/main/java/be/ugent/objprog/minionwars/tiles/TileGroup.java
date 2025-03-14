package be.ugent.objprog.minionwars.tiles;

import be.ugent.objprog.minionwars.models.TileModel;
import javafx.animation.PauseTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class TileGroup extends Group {
    private static final double BASE_R = 20;
    private static final double BASE_N = Math.sqrt(BASE_R * BASE_R * 0.75);
    private double tileScaleFactor = 1.0;
    private final Tile[][] tileGrid;
    private Pane boundPane; // Reference to gamePane
    private PauseTransition resizeDelay; // Timer for delaying resize

    public TileGroup(TileModel tileModel, Pane gamePane) {
        this.tileGrid = tileModel.getTileGrid();
        this.boundPane = gamePane;

        // Create resize delay to prevent constant resizing (200ms after last resize event)
        resizeDelay = new PauseTransition(Duration.millis(200));
        resizeDelay.setOnFinished(e -> adjustTileSize()); // Only adjust after delay


        // Listen for pane size changes, but only update after delay (Resource heavy)
        boundPane.widthProperty().addListener((obs, oldVal, newVal) -> resizeDelay.playFromStart());
        boundPane.heightProperty().addListener((obs, oldVal, newVal) -> resizeDelay.playFromStart());

        updateTiles();
    }

    private void adjustTileSize() {
        // Calculate scaling based on gamePane size
        double paneWidth = boundPane.getWidth();
        double paneHeight = boundPane.getHeight();
        double gridWidth = tileGrid.length * BASE_N * 2;
        double gridHeight = tileGrid[0].length * BASE_R * 2;

        // Determine scale factor based on pane size
        double scaleX = paneWidth / gridWidth;
        double scaleY = paneHeight / gridHeight;
        tileScaleFactor = Math.min(scaleX, scaleY); // Keep uniform scaling

        updateTiles();
    }

    public void setTileScaleFactor(double scaleFactor) {
        this.tileScaleFactor = scaleFactor;
        updateTiles();
    }

    private void updateTiles() {
        getChildren().clear();
        double r = BASE_R * tileScaleFactor;
        double n = Math.sqrt(r * r * 0.75);
        double tileWidth = 2 * n;
        double tileHeight = 2 * r;

        int xStartOffset = 40;
        int yStartOffset = 40;

        for (int i = 0; i < tileGrid.length; i++) {
            for (int j = 0; j < tileGrid[i].length; j++) {
                Tile tile = tileGrid[i][j];
                double xCoord = i * tileWidth + (j % 2) * n + xStartOffset;
                double yCoord = j * tileHeight * 0.75 + yStartOffset;
                HexTile hexTile = new HexTile(xCoord, yCoord, tile, tileScaleFactor);
                getChildren().add(hexTile);
            }
        }
    }
}

