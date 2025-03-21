package be.ugent.objprog.minionwars.powers;

import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.views.HexTile;
// TODO MAYBE ONE CLASS WITH OPTIONAL EFFECT
public abstract class Power {
    protected final int radius;
    protected final int value;

    public Power(int radius, int value) {
        this.radius = radius;
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public int getRadius() { return radius; }

    public abstract void apply(HexTile center);
}
