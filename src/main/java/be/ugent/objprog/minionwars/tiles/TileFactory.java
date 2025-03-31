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
        TileFactoryFunction factoryFunction = tileFactories.get(type);
        if (factoryFunction == null) {
            throw new IllegalArgumentException("Unknown tile type: " + type);
        }
         return factoryFunction.create(x, y,homebase);
    }
    @FunctionalInterface
    public interface TileFactoryFunction {
        Tile create(int x, int y,int homebase);
    }
}


