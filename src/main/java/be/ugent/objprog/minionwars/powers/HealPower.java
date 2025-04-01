package be.ugent.objprog.minionwars.powers;

import be.ugent.objprog.minionwars.effects.MinionEffect;
import javafx.scene.image.Image;

import java.util.Objects;

public class HealPower extends Power {
    public HealPower(String name,int radius, int value, MinionEffect effect) {
        super(name,radius, value, effect);
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/powers/healing.png")));
        offensive = false;
    }
}
