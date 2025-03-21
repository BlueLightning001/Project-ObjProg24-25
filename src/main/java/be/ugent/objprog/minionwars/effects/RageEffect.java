package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;
import javafx.scene.image.Image;

import java.util.Objects;

public class RageEffect extends MinionEffect {
    public RageEffect(int duration,int value) {
        super(duration,value);
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/effects/rage.png")));
    }

    @Override
    public void applyEffect(Minion minion) {
        System.out.println("Rage effect on " + minion.getName() + ", value: " + value);
    }
}
