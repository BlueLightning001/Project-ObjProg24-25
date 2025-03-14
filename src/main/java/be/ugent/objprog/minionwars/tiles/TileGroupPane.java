package be.ugent.objprog.minionwars.tiles;

import be.ugent.objprog.minionwars.ZoomableScrollPane;
import be.ugent.objprog.minionwars.models.TileModel;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import java.util.ArrayList;
import java.util.List;

public class TileGroupPane extends Pane {
    private static final double BASE_R = 20;
    private static final double BASE_N = Math.sqrt(BASE_R * BASE_R * 0.75);
    private double tileScaleFactor = 1.0;
    private final Tile[][] tileGrid;

    public ZoomableScrollPane getBoundPane() {
        return boundPane;
    }

    private ZoomableScrollPane boundPane;
    private final List<HexTile> hexTiles = new ArrayList<>();

    private final ExecutorService resizeExecutor = Executors.newSingleThreadExecutor();

    public TileGroupPane(TileModel tileModel) {
        this.tileGrid = tileModel.getTileGrid();
        initializeTiles();  // Create the tiles ONCE
    }

    public void bindPane(ZoomableScrollPane gamePane) {
        this.boundPane = gamePane;
        boundPane.widthProperty().addListener((obs, oldVal, newVal) -> adjustTileSizeAsync());
        boundPane.heightProperty().addListener((obs, oldVal, newVal) -> adjustTileSizeAsync());

        adjustTileSizeAsync(); // Initial resize
    }

    private void initializeTiles() {
        for (int i = 0; i < tileGrid.length; i++) {
            for (int j = 0; j < tileGrid[i].length; j++) {
                Tile tile = tileGrid[i][j];
                HexTile hexTile = new HexTile(0, 0, tile, tileScaleFactor);
                hexTiles.add(hexTile);
            }
        }
        Platform.runLater(() -> getChildren().setAll(hexTiles)); // Add to UI
    }

    private void adjustTileSizeAsync() {
        if (boundPane == null) return;

        resizeExecutor.submit(() -> {
            double paneWidth = boundPane.getWidth();
            double paneHeight = boundPane.getHeight();
            double gridWidth = tileGrid.length * BASE_N * 2;
            double gridHeight = tileGrid[0].length * BASE_R * 1.5;

            double scaleX = paneWidth / gridWidth;
            double scaleY = paneHeight / gridHeight;
            double newScaleFactor = Math.min(scaleX, scaleY);

            double r = BASE_R * newScaleFactor;
            double n = Math.sqrt(r * r * 0.75);
            double tileWidth = 2 * n;
            double tileHeight = 2 * r;

            double totalWidth = tileGrid.length * tileWidth;
            double totalHeight = tileGrid[0].length * tileHeight * 0.75;
            double xOffset = (paneWidth - totalWidth) / 2;
            double yOffset = (paneHeight - totalHeight) / 2;

            Platform.runLater(() -> {
                tileScaleFactor = newScaleFactor; // Update scale factor

                for (HexTile hexTile : hexTiles) {
                    Tile tile = hexTile.getTile();
                    int i = tile.getXCoord();  // Assuming Tile has X/Y coordinates
                    int j = tile.getYCoord();
                    double xCoord = i * tileWidth + (j % 2) * n + xOffset;
                    double yCoord = j * tileHeight * 0.75 + yOffset;
                    hexTile.setTranslateX(xCoord);
                    hexTile.setTranslateY(yCoord);
                    hexTile.setScaleX(newScaleFactor);
                    hexTile.setScaleY(newScaleFactor);
                }
            });
        });
    }

    public void shutdown() {
        resizeExecutor.shutdown(); // Call this when closing the game
    }
}

