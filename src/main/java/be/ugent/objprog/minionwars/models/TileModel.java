package be.ugent.objprog.minionwars.models;

import be.ugent.objprog.minionwars.JDOMReader;
import be.ugent.objprog.minionwars.tiles.DirtTile;
import be.ugent.objprog.minionwars.tiles.ForestTile;
import be.ugent.objprog.minionwars.tiles.MountainTile;
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.tiles.VoidTile;
import be.ugent.objprog.minionwars.tiles.WaterTile;
import be.ugent.objprog.minionwars.views.HexTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class TileModel {
    private final Map<Class<? extends Tile>, String> tileNames = new HashMap<>();
    public List<Tile> getInitialTiles() {
        return initialTiles;
    }
    private ResourceBundle bundle;
    private final List<Tile> initialTiles;
    private Tile[][] tileGrid;

    public TileModel(JDOMReader reader, Locale locale) {
        this.initialTiles = new ArrayList<>();
        initializeTiles(reader);
        initializeTileGrid();

        bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);

        tileNames.put(DirtTile.class, bundle.getString("tiles.dirtTile"));
        tileNames.put(WaterTile.class, bundle.getString("tiles.waterTile"));
        tileNames.put(MountainTile.class, bundle.getString("tiles.mountainTile"));
        tileNames.put(VoidTile.class, bundle.getString("tiles.voidTile"));
        tileNames.put(ForestTile.class, bundle.getString("tiles.forestTile"));
    }
    public String getTileName(Class<? extends Tile> tileClass) {
        return tileNames.getOrDefault(tileClass, bundle.getString("tiles.unknownTile"));
    }
    public void initializeTiles(JDOMReader jdomReader) {
        System.out.println(jdomReader.getTiles());
        initialTiles.addAll(jdomReader.getTiles());

    }
    /*
      Manhattan distance is illogical for hexagonal tiles
      Hugo. (2024, January 14). #6 - Calculating the distance between hexagonal tiles. seaotter.games.
      Retrieved March 23, 2025,
      from https://seaotter.games/blog/calculating-a-distance-between-hexagonal-tiles
     */
    public int hexDistance(Tile a, Tile b) {
        int q1 = a.getXCoord();
        int r1 = a.getYCoord() - (a.getXCoord() - (a.getXCoord() % 2)) / 2;

        int q2 = b.getXCoord();
        int r2 = b.getYCoord() - (b.getXCoord() - (b.getXCoord() % 2)) / 2;

        int s1 = -q1 - r1;
        int s2 = -q2 - r2;

        return (Math.abs(q1 - q2) + Math.abs(r1 - r2) + Math.abs(s1 - s2)) / 2;
    }

    public List<Tile> getTilesInRadius(Tile centerTile, int radius) {
        List<Tile> tilesInRadius = new ArrayList<>();

        int centerX = centerTile.getXCoord();
        int centerY = centerTile.getYCoord();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int newX = centerX + dx;
                int newY = centerY + dy;

                // Bounds check
                if (newX >= 0 && newX < tileGrid.length && newY >= 0 && newY < tileGrid[newX].length) {
                    Tile candidate = tileGrid[newX][newY];

                    // Check if within hex radius
                    if (hexDistance(centerTile, candidate) <= radius) {
                        tilesInRadius.add(candidate);
                    }
                }
            }
        }
        return tilesInRadius;
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
                    tileGrid[i][j] = new VoidTile(i, j,0);
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
    public void removeTile(int x, int y) {
        tileGrid[x][y] = null;
    }
}
