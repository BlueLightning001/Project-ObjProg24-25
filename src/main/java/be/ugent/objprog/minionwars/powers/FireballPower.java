package be.ugent.objprog.minionwars.powers;

import be.ugent.objprog.minionwars.effects.MinionEffect;
import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.views.HexTile;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Objects;

public class FireballPower extends Power {
    public FireballPower(int radius, int value, MinionEffect effect) {
        super(radius, value, effect);
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/powers/fireball.png")));
        offensive = true;
    }

    //TODO A LOT
    @Override
    public void apply(HexTile center, Player caster) {
        System.out.println("Fireball power called");
        List<Tile> affectedTiles = center.getTilesInRadius(radius);
        for (Tile tile : affectedTiles) {
            Minion minion = tile.getOccupant();
            if (minion != null && !minion.getOwner().equals(caster)) {
                System.out.println("Damaging " + minion);
                minion.decreaseDefence(value);
                minion.applyEffectLogic(effect);
            }
        }
    }


}
