package be.ugent.objprog.minionwars.tiles;

public class DirtTile extends Tile {
    private final static String IMAGE_PATH = "/be/ugent/objprog/minionwars/images/tiles/dirt.png";
    private final static int TRAVERSAL_COST = 1;

    public DirtTile(int x, int y, int homebase) {
        super(x, y, homebase, IMAGE_PATH, true, TRAVERSAL_COST, true, true);
    }

}
