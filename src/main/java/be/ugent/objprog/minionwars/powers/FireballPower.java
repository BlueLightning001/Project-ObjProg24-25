package be.ugent.objprog.minionwars.powers;

import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.views.HexTile;

import java.util.List;

public class FireballPower extends Power {
    public FireballPower(int radius,int value) {
        super(radius, value);
    }

    @Override
    public void apply(HexTile center) {
        List<Tile> affectedTiles = center.getTilesInRadius(radius);
        for (Tile tile : affectedTiles) {
            Minion minion = tile.getOccupant();
            if (minion != null) {
                System.out.println("DAmaging " + minion);
                minion.decreaseDefence(value);
            }
        }
    }
}
