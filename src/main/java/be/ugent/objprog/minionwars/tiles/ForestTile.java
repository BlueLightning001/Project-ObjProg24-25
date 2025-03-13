package be.ugent.objprog.minionwars.tiles;

public class ForestTile extends Tile{
    private static final String IMAGE_PATH = "be/ugent/objprog/minionwars/images/tiles/forest.png";
    private static final int TRAVERSAL_COST = 2;

    public ForestTile(int x, int y) {
        super(x, y, IMAGE_PATH, true, TRAVERSAL_COST, true, true);
    }
}
