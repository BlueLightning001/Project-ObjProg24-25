package be.ugent.objprog.minionwars.tiles;

public class VoidTile extends Tile {
    private final static String IMAGE_PATH = "/be/ugent/objprog/minionwars/images/tiles/void.png";

    public VoidTile(int x, int y, int homebase) {
        super(x, y, 0, IMAGE_PATH, false, 1, false, false);
    }

}
