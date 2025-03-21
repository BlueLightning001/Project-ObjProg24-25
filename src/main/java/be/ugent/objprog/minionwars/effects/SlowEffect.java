package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;
import javafx.scene.image.Image;

import java.util.Objects;

public class SlowEffect extends MinionEffect {
    public SlowEffect(int duration,  int value) {
        super(duration,value);
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/effects/slow.png")));
    }

    @Override
    public void applyEffect(Minion minion) {
        System.out.println("Slow effect on " + minion.getName() + ", value: " + value );
    }
}
