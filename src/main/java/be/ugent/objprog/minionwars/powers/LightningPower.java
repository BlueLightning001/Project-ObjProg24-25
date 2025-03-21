package be.ugent.objprog.minionwars.powers;

import be.ugent.objprog.minionwars.views.HexTile;

public class LightningPower extends Power{
    public LightningPower(int radius, int value) {
        super(radius,value  );
    }

    @Override
    public void apply(HexTile center) {
        System.out.println("Lightning power called");
    }
}
