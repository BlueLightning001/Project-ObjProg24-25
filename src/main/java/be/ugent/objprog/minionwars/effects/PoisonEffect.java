package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;

public class PoisonEffect extends MinionEffect {
    public PoisonEffect(int value) {
        super(value);
    }

    @Override
    public void applyEffect(Minion minion) {
        System.out.println(minion.getName() + " is poisoned with effect value: " + this.value);


    }
}
