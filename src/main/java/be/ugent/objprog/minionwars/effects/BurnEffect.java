package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;
import javafx.scene.image.Image;

import java.util.Objects;

public class BurnEffect extends MinionEffect {

    public BurnEffect(int duration,int value) {
        super(duration,value);
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/effects/burn.png")));
    }

    @Override
    public void applyEffect(Minion minion) {
        System.out.println("Burn effect on " + minion.getName() + ", value: " + value);
    }
}
