package be.ugent.objprog.minionwars.powers;

import be.ugent.objprog.minionwars.views.HexTile;

public class HealPower extends Power {
    public HealPower(int radius,int value) {
        super(radius,value);
    }

    @Override
    public void apply(HexTile center) {
        System.out.println("Healing " + center.getTile());
    }
}
