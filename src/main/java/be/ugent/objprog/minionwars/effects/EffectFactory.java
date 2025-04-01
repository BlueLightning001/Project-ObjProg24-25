package be.ugent.objprog.minionwars.effects;

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

        return factoryFunction.create(duration, value);
    }


    @FunctionalInterface
    public interface EffectFactoryFunction {
        MinionEffect create(int duration, int value);
    }

}
