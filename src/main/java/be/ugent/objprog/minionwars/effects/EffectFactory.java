package be.ugent.objprog.minionwars.effects;

import javafx.scene.effect.Effect;

import java.util.Map;

import java.util.Map;

public class EffectFactory {
    private final Map<String, EffectFactoryFunction> minionEffectsFactories = Map.of(
            "heal", HealEffect::new  // Store constructor references correctly
    );

    public MinionEffect createEffect(String effectType, int value) {
        EffectFactoryFunction factoryFunction = minionEffectsFactories.get(effectType);

        if (factoryFunction == null) {
            throw new IllegalArgumentException("Unknown effect type: " + effectType);
        }

        System.out.println("Creating effect: " + effectType + ", Value: " + value);
        return factoryFunction.create(value);
    }

    @FunctionalInterface
    public interface EffectFactoryFunction {
        MinionEffect create(int value);
    }
}
