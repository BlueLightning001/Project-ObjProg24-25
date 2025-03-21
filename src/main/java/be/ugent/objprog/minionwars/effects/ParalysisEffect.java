package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;
import javafx.scene.image.Image;

import java.util.Objects;

public class ParalysisEffect extends MinionEffect {

    public ParalysisEffect(int duration,int value) {
        super(duration,value);
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/effects/paralysis.png")));
    }

    @Override
    public void applyEffect(Minion m) {
        System.out.println("Paralysis " + m.getName() + ", value: " + value);
    }
}
