package be.ugent.objprog.minionwars.powers;

import be.ugent.objprog.minionwars.effects.MinionEffect;
import be.ugent.objprog.minionwars.views.HexTile;
import javafx.scene.image.Image;

import java.util.Locale;
import java.util.ResourceBundle;


public abstract class Power {
    protected final int radius;
    protected final int value;
    protected final MinionEffect effect;
    protected Image image = null;

    public Power(int radius, int value, MinionEffect effect) {
        this.radius = radius;
        this.value = value;
        this.effect = effect;
    }

    //TODO ALL IMPLEMENTATIONS
    public abstract void apply(HexTile center);

    public Image getImage() {
        return image;
    }
    public String getName(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);
        return bundle.getString("power." + this.getClass().getSimpleName());
    }
    public String getDescription(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);
        return bundle.getString("power.description." + this.getClass().getSimpleName());
    }
    public int getRadius() {
        return radius;
    }

    public int getValue() {
        return value;
    }
}
