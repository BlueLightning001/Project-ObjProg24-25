package be.ugent.objprog.minionwars.tiles;

import be.ugent.objprog.minionwars.ZoomableScrollPane;
import be.ugent.objprog.minionwars.models.TileModel;
import javafx.animation.PauseTransition;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class TileGroupPane extends Pane {
    private static final double BASE_R = 20; // Base hex radius
    private static final double BASE_N = Math.sqrt(BASE_R * BASE_R * 0.75);
    private double tileScaleFactor = 1.0;
    private final Tile[][] tileGrid;
    private ZoomableScrollPane boundPane; // Reference to gamePane
    private PauseTransition resizeDelay;

    public TileGroupPane(TileModel tileModel) {
        this.tileGrid = tileModel.getTileGrid();

        // Delay resizing (prevents laggy updates)
        resizeDelay = new PauseTransition(Duration.millis(200));
        resizeDelay.setOnFinished(e -> adjustTileSize());

    }

    // Method to bind the ZoomableScrollPane after TileGroupPane is initialized
    public void bindPane(ZoomableScrollPane gamePane) {
        this.boundPane = gamePane;

        // Listen for gamePane size changes
        boundPane.widthProperty().addListener((obs, oldVal, newVal) -> resizeDelay.playFromStart());
        boundPane.heightProperty().addListener((obs, oldVal, newVal) -> resizeDelay.playFromStart());

        // Trigger an initial size adjustment
        updateTiles();
        adjustTileSize();
    }

    private void adjustTileSize() {
        if (boundPane == null) return; // Check if the boundPane is set

        double paneWidth = boundPane.getWidth();
        double paneHeight = boundPane.getHeight();
        double gridWidth = tileGrid.length * BASE_N * 2;
        double gridHeight = tileGrid[0].length * BASE_R * 1.5;

        // Calculate uniform scale
        double scaleX = paneWidth / gridWidth;
        double scaleY = paneHeight / gridHeight;
        tileScaleFactor = Math.min(scaleX, scaleY); // Maintain aspect ratio

        updateTiles();
    }

    private void updateTiles() {
        getChildren().clear();
        double r = BASE_R * tileScaleFactor;
        double n = Math.sqrt(r * r * 0.75);
        double tileWidth = 2 * n;
        double tileHeight = 2 * r;

        // Center grid within the Pane
        double totalWidth = tileGrid.length * tileWidth;
        double totalHeight = tileGrid[0].length * tileHeight * 0.75;
        double xOffset = (boundPane.getWidth() - totalWidth) / 2;
        double yOffset = (boundPane.getHeight() - totalHeight) / 2;

        for (int i = 0; i < tileGrid.length; i++) {
            for (int j = 0; j < tileGrid[i].length; j++) {
                Tile tile = tileGrid[i][j];
                double xCoord = i * tileWidth + (j % 2) * n + xOffset;
                double yCoord = j * tileHeight * 0.75 + yOffset;
                HexTile hexTile = new HexTile(xCoord, yCoord, tile, tileScaleFactor);
                getChildren().add(hexTile);
            }
        }
    }
}
