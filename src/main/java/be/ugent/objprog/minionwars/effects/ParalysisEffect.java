package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;
import javafx.scene.image.Image;

import java.util.Objects;

public class ParalysisEffect extends MinionEffect {

    public ParalysisEffect(String name, int duration, int value) {
        super(name, duration, value);
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/effects/paralysis.png")));
    }

    @Override
    public void applyEffect(Minion m) {
        // Blocks all minion actions
        m.setAttacked(true);
        m.setMoved(true);
    }
}
