package be.ugent.objprog.minionwars.powers;

import be.ugent.objprog.minionwars.effects.MinionEffect;
import be.ugent.objprog.minionwars.effects.ParalysisEffect;
import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.views.HexTile;
import javafx.scene.image.Image;

import java.util.List;


public abstract class Power {
    protected final int radius;
    protected final int value;
    protected final MinionEffect effect;
    protected Image image = null;
    public Power(int radius, int value, MinionEffect effect) {
        this.radius = radius;
        this.value = value;
        this.effect = effect;
    }

    public int getValue() {
        return value;
    }
    public int getRadius() { return radius; }

    //TODO ALL IMPLEMENTATIONS
    public abstract void apply(HexTile center);
}
