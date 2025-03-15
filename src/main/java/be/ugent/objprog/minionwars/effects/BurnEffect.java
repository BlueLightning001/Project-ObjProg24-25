package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;

public class BurnEffect extends MinionEffect {
    public BurnEffect(int value) {
        super(value);
    }

    @Override
    public void applyEffect(Minion minion) {
        System.out.println("Burn effect on " + minion.getName() + ", value: " + value);
    }
}
