package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;
import javafx.scene.image.Image;

import java.util.Objects;

public class BlindnessEffect extends MinionEffect {

    public BlindnessEffect(String name, int baseDuration, int value) {
        super(name, baseDuration, value);
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/effects/blindness.png")));
    }

    @Override
    public void applyEffect(Minion minion) {
        //Minimum range?
        Integer[] newRange = new Integer[2];

        int newMinRange = minion.getRange().getFirst();
        int newMaxRange = minion.getRange().getLast() - value.get();

        // Max range is now lower than minimum range, minion cannot attack since he is completely blind
        if (newMaxRange < newMinRange) {
            newMinRange = 0;
            newMaxRange = 0;
        }
        newRange[0] = newMinRange;
        newRange[1] = newMaxRange;

        minion.setRange(newRange);
    }
}
