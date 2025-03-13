package be.ugent.objprog.minionwars.tiles;

import java.util.Map;

public class TileFactory {
    private final Map<String, TileFactoryFunction> tileFactories = Map.of(
            "dirt", DirtTile::new,
            "forest", ForestTile::new,
            "mountains", MountainTile::new,
            "water", WaterTile::new,
            "void", VoidTile::new
    );
    public Tile createTile(String type, int x, int y, int homebase) {
        System.out.println("Creating tile " + type + " at " + x + ", " + y + ", hb: " + homebase );
        return tileFactories.get(type).create(x, y,homebase);
    }
    public interface TileFactoryFunction {
        Tile create(int x, int y,int homebase);
    }
}


