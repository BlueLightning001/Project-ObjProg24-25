package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;
import javafx.scene.image.Image;

import java.util.Objects;

public class BlindnessEffect extends MinionEffect {

    public BlindnessEffect(int baseDuration, int value) {
        super(baseDuration, value);
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/effects/blindness.png")));
    }

    @Override
    public void applyEffect(Minion minion) {
        System.out.println("Blindness effect on: " + minion);
    }
}
