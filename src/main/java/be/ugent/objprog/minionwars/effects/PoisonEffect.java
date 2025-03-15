package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;
import javafx.scene.image.Image;

import java.util.Objects;

public class PoisonEffect extends MinionEffect {
    public PoisonEffect(int value) {
        super(value);
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/effects/poison.png")));
    }

    @Override
    public void applyEffect(Minion minion) {
        System.out.println(minion.getName() + " is poisoned with effect value: " + this.value);


    }
}
