package be.ugent.objprog.minionwars.models;

import be.ugent.objprog.minionwars.JDOMReader;
import be.ugent.objprog.minionwars.tiles.DirtTile;
import be.ugent.objprog.minionwars.tiles.ForestTile;
import be.ugent.objprog.minionwars.tiles.MountainTile;
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.tiles.VoidTile;
import be.ugent.objprog.minionwars.tiles.WaterTile;

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

      Sources that helped with the calculations:
      Hugo. (2024, January 14). #6 - Calculating the distance between hexagonal tiles. seaotter.games.
      Retrieved March 23, 2025,
      from https://seaotter.games/blog/calculating-a-distance-between-hexagonal-tiles

      Red Blob Games. (2013, March). Hexagonal grids. redblobgames.com. Retrieved March 25, 2025,
      from https://www.redblobgames.com/grids/hexagons/
     */
    public int cubeDistance(Tile a, Tile b) {
        int[] aCoords = oddRToAxial(a);
        int[] bCoords = oddRToAxial(b);
        int[] aCubeCoords = axialToCube(aCoords);
        int[] bCubeCoords = axialToCube(bCoords);
        System.out.println("Distance between: (" + a.getXCoord() + ", " + a.getYCoord() + ") and (" + b.getXCoord() + ", " + b.getYCoord() + ")" );
        int distance = - 1;
        if (aCubeCoords != null && bCubeCoords != null) {
            distance = Math.max(Math.max(Math.abs(aCubeCoords[0] - bCubeCoords[0]),
                            Math.abs(aCubeCoords[1] - bCubeCoords[1]))
                    , Math.abs(aCubeCoords[2] - bCubeCoords[2]));
        }
        System.out.println("IS: " + distance);
        return distance;

    }


    private int[] axialToCube(int[] axial){
        if(axial.length == 2){
            int q = axial[0];
            int r = axial[1];
            int s = - q - r;
            return new int[]{q,r,s};
        }
        return null;
    }
    private int[] cubeToAxial(int[] cube){
        if(cube.length == 3){
            int q = cube[0];
            int r = cube[1];
            int s = cube[2];
            return new int[]{q,r};
        }
        return null;
    }
    private int[] oddRToAxial(Tile tile){
        int q = tile.getXCoord() - (tile.getYCoord() - (tile.getYCoord()&1)) / 2;
        int r = tile.getYCoord();
        return new int[]{q, r};
    }
    private int[] axialToOddR(int[] axial){
        int q = axial[0];
        int r = axial[1];
        int col = q + (r - (r&1)) / 2;
        int row = q + (r&1);
        return new int[]{col, row};
    }


    public List<Tile> getTilesInRadius(Tile centerTile, int minRange, int maxRange) {
        List<Tile> tilesInRadius = new ArrayList<>();

        int centerX = centerTile.getXCoord();
        int centerY = centerTile.getYCoord();

        for (int dx = -maxRange; dx <= maxRange; dx++) {
            for (int dy = -maxRange; dy <= maxRange; dy++) {
                int newX = centerX + dx;
                int newY = centerY + dy;

                // Bounds check
                if (newX >= 0 && newX < tileGrid.length && newY >= 0 && newY < tileGrid[newX].length) {
                    Tile candidate = tileGrid[newX][newY];

                    // Calculate hex distance
                    int distance = cubeDistance(centerTile, candidate);

                    // Ensure it's within range bounds
                    if (distance >= minRange && distance <= maxRange) {
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
