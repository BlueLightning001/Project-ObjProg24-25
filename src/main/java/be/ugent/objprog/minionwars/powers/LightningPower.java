package be.ugent.objprog.minionwars.powers;

import be.ugent.objprog.minionwars.effects.MinionEffect;
import javafx.scene.image.Image;

import java.util.Objects;

public class LightningPower extends Power {

    public LightningPower(String name,int radius, int value, MinionEffect effect) {
        super(name,radius, value, effect);
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/powers/lightning.png")));
        offensive = true;
    }

}
