package be.ugent.objprog.minionwars.powers;

import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.views.HexTile;

public abstract class Power {
    protected final String name;
    protected final int radius;

    public Power(String name, int radius) {
        this.name = name;
        this.radius = radius;
    }

    public String getName() { return name; }

    public int getRadius() { return radius; }

    public abstract void apply(HexTile center);
}
