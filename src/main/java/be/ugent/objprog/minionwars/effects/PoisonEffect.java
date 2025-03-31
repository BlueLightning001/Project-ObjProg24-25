package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;
import javafx.scene.image.Image;

import java.util.Objects;

public class PoisonEffect extends MinionEffect {
    public PoisonEffect(int duration,int value) {
        super(duration,value);
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/effects/poison.png")));
    }

    @Override
    public void applyEffect(Minion minion) {
        minion.decreaseDefence(value.get());

        System.out.println(minion.getName() + " is poisoned with effect value: " + this.value.get());


    }
}
