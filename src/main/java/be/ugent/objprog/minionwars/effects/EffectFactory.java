package be.ugent.objprog.minionwars.effects;

import javafx.scene.effect.Effect;

import java.util.Map;

import java.util.Map;

public class EffectFactory {
    private final Map<String, EffectFactoryFunction> minionEffectsFactories = Map.of(
            "heal", HealEffect::new,
            "poison", PoisonEffect::new,
            "slow", SlowEffect::new,
            "paralysis", ParalysisEffect::new,
            "burn", BurnEffect::new,
            "rage", RageEffect::new,
            "blindness", BlindnessEffect::new
    );

    public MinionEffect createEffect(String effectType, int duration, int value) {
        EffectFactoryFunction factoryFunction = minionEffectsFactories.get(effectType);

        if (factoryFunction == null) {
            throw new IllegalArgumentException("Unknown effect type: " + effectType);
        }

        System.out.println("Creating effect: " + effectType + ", Duration: " + duration + ", Value: " + value);
        return factoryFunction.create(duration, value);
    }


    @FunctionalInterface
    public interface EffectFactoryFunction {
        MinionEffect create(int duration, int value);
    }

}
