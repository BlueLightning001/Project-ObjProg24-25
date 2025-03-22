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
    protected final Image offensiveImage = new Image(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/attack-D60000.png"));
    protected final Image healthImage = new Image(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/health-D60000.png"));
    protected boolean offensive;
    public Power(int radius, int value, MinionEffect effect) {
        this.radius = radius;
        this.value = value;
        this.effect = effect;
        this.offensive = false;
    }

    //TODO ALL IMPLEMENTATIONS
    public abstract void apply(HexTile center);

    public MinionEffect getEffect() {
        return effect;
    }

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

    public boolean hasEffect() {
        return effect != null;
    }
    public Image getValueImage() {
        if (offensive) {
            return offensiveImage;
        } else {
            return healthImage;
        }
    }
}
