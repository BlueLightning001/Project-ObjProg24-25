package be.ugent.objprog.minionwars.tiles;

public class WaterTile extends Tile{
    private final static String IMAGE_PATH = "/be/ugent/objprog/minionwars/images/tiles/water.png";

    public WaterTile(int x, int y, int homebase) {
        super(x, y,homebase ,IMAGE_PATH, false, 1, false, false);
    }

}
