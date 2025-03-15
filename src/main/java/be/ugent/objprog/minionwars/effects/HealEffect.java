package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;
import javafx.scene.image.Image;

import java.util.Objects;

public class HealEffect extends MinionEffect {

    public HealEffect(int value) {
        super(value);
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/effects/heal.png")));
    }

    @Override
    public void applyEffect(Minion m) {
        System.out.println("Healing " + m.getName() + ", value: " + value);
    }
}
