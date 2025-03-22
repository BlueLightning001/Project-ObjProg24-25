package be.ugent.objprog.minionwars.powers;

import be.ugent.objprog.minionwars.effects.BlindnessEffect;
import be.ugent.objprog.minionwars.effects.BurnEffect;
import be.ugent.objprog.minionwars.effects.EffectFactory;
import be.ugent.objprog.minionwars.effects.HealEffect;
import be.ugent.objprog.minionwars.effects.MinionEffect;
import be.ugent.objprog.minionwars.effects.ParalysisEffect;
import be.ugent.objprog.minionwars.effects.PoisonEffect;
import be.ugent.objprog.minionwars.effects.RageEffect;
import be.ugent.objprog.minionwars.effects.SlowEffect;

import java.util.Map;

public class PowerFactory {
    private final Map<String, PowerFactory.PowerFactoryFunction> powerFactories = Map.of(
           "fireball", FireballPower::new,
            "lightning", LightningPower::new,
            "heal",HealPower::new
    );
    public Power createPower(String powerType, int radius, int value, MinionEffect effect) {
        PowerFactory.PowerFactoryFunction factoryFunction = powerFactories.get(powerType);

        if (factoryFunction == null) {
            throw new IllegalArgumentException("Unknown power type: " + powerType);
        }

        System.out.println("Creating power: " + powerType + ", Radius: " + radius + ", Value: " + value);
        return factoryFunction.create(radius, value,effect);
    }

    @FunctionalInterface
    public interface PowerFactoryFunction {
        Power create(int radius, int value, MinionEffect effect);
    }
}
