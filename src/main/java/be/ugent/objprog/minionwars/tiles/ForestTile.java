package be.ugent.objprog.minionwars.tiles;

public class ForestTile extends Tile {
    private static final String IMAGE_PATH = "/be/ugent/objprog/minionwars/images/tiles/forest.png";
    private static final int TRAVERSAL_COST = 2;

    public ForestTile(int x, int y, int homebase) {
        super(x, y, homebase, IMAGE_PATH, true, TRAVERSAL_COST, true, true);
    }

}
