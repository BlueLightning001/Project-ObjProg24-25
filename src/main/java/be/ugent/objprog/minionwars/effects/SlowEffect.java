package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;

public class SlowEffect extends MinionEffect {
    public SlowEffect(int value) {
        super(value);
    }

    @Override
    public void applyEffect(Minion minion) {
        System.out.println("Slow effect on " + minion.getName() + ", value: " + value );
    }
}
