package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;

public class RageEffect extends MinionEffect {
    public RageEffect(int value) {
        super(value);
    }

    @Override
    public void applyEffect(Minion minion) {
        System.out.println("Rage effect on " + minion.getName() + ", value: " + value);
    }
}
