package be.ugent.objprog.minionwars.tiles;

public class MountainTile extends Tile {
    private static final String IMAGE_PATH = "/be/ugent/objprog/minionwars/images/tiles/mountains.png";
    private static final int TRAVERSAL_COST = 1;

    public MountainTile(int x, int y, int homebase) {
    super(x, y,homebase ,IMAGE_PATH, true, TRAVERSAL_COST, false, true);
    }

}
