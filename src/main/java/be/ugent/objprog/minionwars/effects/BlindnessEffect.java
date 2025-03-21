package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;

public class BlindnessEffect extends MinionEffect {
    public BlindnessEffect(int baseDuration, int value) {
        super(baseDuration, value);
    }

    @Override
    public void applyEffect(Minion minion) {
        System.out.println("Blindness effect on: " + minion);
    }
}
