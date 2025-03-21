package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.ZoomableScrollPane;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import be.ugent.objprog.minionwars.tiles.Tile;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TileGroupPane extends Pane {
    private static final double BASE_R = 20;
    private static final double BASE_N = Math.sqrt(BASE_R * BASE_R * 0.75);
    private final Tile[][] tileGridModel;
    private final PlayerModel playerModel;
    private final List<HexTile> hexTiles = new ArrayList<>();
    private final HexTile[][] hexTileGrid;
    private final ExecutorService resizeExecutor = Executors.newSingleThreadExecutor();
    private double tileScaleFactor = 1.0;
    private SimpleObjectProperty<HexTile> selectedHexTile;
    private ZoomableScrollPane boundPane;
    private TileModel tileModel;

    public TileGroupPane(TileModel tileModel, PlayerModel playerModel) {
        this.tileModel = tileModel;
        this.tileGridModel = tileModel.getTileGrid();
        this.playerModel = playerModel;
        this.selectedHexTile = new SimpleObjectProperty<>(null);
        hexTileGrid = new HexTile[tileGridModel.length][tileGridModel[0].length];
        initializeTiles();  // Create the tiles once

        // Update tiles to match selection
        selectedHexTileProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.setSelected(false);
            }
            if (newValue != null) {
                newValue.setSelected(true);
            }
        });


    }



    private void initializeTiles() {
        for (int i = 0; i < tileGridModel.length; i++) {
            for (int j = 0; j < tileGridModel[i].length; j++) {
                Tile tile = tileGridModel[i][j];
                HexTile hexTile = new HexTile(0, 0, tile, playerModel, tileScaleFactor, tileModel);
                hexTiles.add(hexTile);
                hexTileGrid[i][j] = hexTile;
            }
        }
        Platform.runLater(() -> getChildren().setAll(hexTiles)); // Add to UI
    }

    public SimpleObjectProperty<HexTile> selectedHexTileProperty() {
        return selectedHexTile;
    }

    public void bindPane(ZoomableScrollPane gamePane) {
        this.boundPane = gamePane;
        boundPane.widthProperty().addListener((obs, oldVal, newVal) -> adjustTileSizeAsync());
        boundPane.heightProperty().addListener((obs, oldVal, newVal) -> adjustTileSizeAsync());

        adjustTileSizeAsync(); // Initial resize
    }

    private void adjustTileSizeAsync() {
        if (boundPane == null) return;

        resizeExecutor.submit(() -> {
            double paneWidth = boundPane.getWidth();
            double paneHeight = boundPane.getHeight();
            double gridWidth = tileGridModel.length * BASE_N * 2;
            double gridHeight = tileGridModel[0].length * BASE_R * 1.5;

            double scaleX = paneWidth / gridWidth;
            double scaleY = paneHeight / gridHeight;
            double newScaleFactor = Math.min(scaleX, scaleY);

            double r = BASE_R * newScaleFactor;
            double n = Math.sqrt(r * r * 0.75);
            double tileWidth = 2 * n;
            double tileHeight = 2 * r;

            double totalWidth = tileGridModel.length * tileWidth;
            double totalHeight = tileGridModel[0].length * tileHeight * 0.75;
            double xOffset = (paneWidth - totalWidth) / 2;
            double yOffset = (paneHeight - totalHeight) / 2;

            // VERY LAGGY !!
            Platform.runLater(() -> {
                tileScaleFactor = newScaleFactor;

                for (HexTile hexTile : hexTiles) {
                    Tile tile = hexTile.getTile();
                    int i = tile.getXCoord();
                    int j = tile.getYCoord();
                    double xCoord = i * tileWidth + (j % 2) * n + xOffset;
                    double yCoord = j * tileHeight * 0.75 + yOffset;
                    double spacing = 3;

                    double newX = xCoord + spacing * i;
                    double newY = yCoord + spacing * j;

                    hexTile.setTranslateX(newX);
                    hexTile.setTranslateY(newY);
                    hexTile.setScaleX(newScaleFactor);
                    hexTile.setScaleY(newScaleFactor);
                }
            });
        });
    }

    public ZoomableScrollPane getBoundPane() {
        return boundPane;
    }

    public List<HexTile> getHexTiles() {
        return hexTiles;
    }

    public HexTile getSelectedHexTile() {
        return selectedHexTile.get();
    }

    public void setSelectedHexTile(HexTile selectedHexTile) {
        if (selectedHexTile == null || selectedHexTile.equals(this.selectedHexTile.get())){
            this.selectedHexTile.set(null);
        } else {
            if (this.selectedHexTile.get() != null) {
                this.selectedHexTile.get().setSelected(false);
            }
            this.selectedHexTile.set(selectedHexTile);
            this.selectedHexTile.get().setSelected(true);
        }

    }

    public void shutdown() {
        resizeExecutor.shutdown(); // Call this when closing the game
    }
}

