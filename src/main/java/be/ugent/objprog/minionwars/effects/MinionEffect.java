package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;

public abstract class MinionEffect {
    protected int value;

    public MinionEffect(int value) {
        this.value = value;
    }
    //TODO implement each effect
    public abstract void applyEffect(Minion minion);

    public int getValue() {
        return value;
    }
}

