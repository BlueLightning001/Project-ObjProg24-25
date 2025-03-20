package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class MinionEffect {
    protected Image image = null;
    protected SimpleIntegerProperty value = new SimpleIntegerProperty();

    public MinionEffect(int value) {
        this.value.set(value);
    }
    public String getName(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);
        return bundle.getString("effect." + this.getClass().getSimpleName());
    }
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    //TODO implement each effect
    public abstract void applyEffect(Minion minion);

    public int getValue() {
        return value.get();
    }
    public SimpleIntegerProperty valueProperty() {
        return value;
    }
}

