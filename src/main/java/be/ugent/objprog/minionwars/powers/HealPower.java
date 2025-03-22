package be.ugent.objprog.minionwars.powers;

import be.ugent.objprog.minionwars.effects.MinionEffect;
import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.views.HexTile;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Objects;

public class HealPower extends Power {
    public HealPower(int radius, int value, MinionEffect effect) {
        super(radius,value, effect);
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/powers/healing.png")));
        offensive = false;
    }

    @Override
    public void apply(HexTile center) {
        List<Tile> affectedTiles = center.getTilesInRadius(radius);
        for (Tile tile : affectedTiles) {
            Minion minion = tile.getOccupant();
            if (minion != null) {
                System.out.println("Healing " + minion);
                minion.heal(value);
                minion.applyEffectLogic(effect);
            }
        }
    }
}
