package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;

public class HealEffect extends MinionEffect {
    public HealEffect(int value) {
        super(value);
    }

    @Override
    public void applyEffect(Minion m) {
        System.out.println("Healing " + m.getName() + ", value: " + value);
    }
}
