package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class MinionEffect {
    protected final int baseDuration;
    protected final String name;
    protected Image image = null;
    protected SimpleIntegerProperty value = new SimpleIntegerProperty();
    protected SimpleIntegerProperty duration = new SimpleIntegerProperty();
    protected boolean offensive = true;

    public MinionEffect(String name,int baseDuration, int value) {
        this.name = name;
        this.baseDuration = baseDuration;
        this.duration.set(baseDuration);
        this.value.set(value);
    }

    public abstract void applyEffect(Minion minion);

    public SimpleIntegerProperty durationProperty() {
        return duration;
    }

    public int getBaseDuration() {
        return baseDuration;
    }

    public int getDuration() {
        return duration.get();
    }

    public String getName() {
        return this.name;
    }

    public void setDuration(int duration) {
        this.duration.set(duration);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getDescription(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);
        return bundle.getString("effect." + this.getClass().getSimpleName());
    }

    public int getValue() {
        return value.get();
    }

    public void setValue(int value) {
        this.value.set(value);
    }

    public boolean isOffensive() {
        return offensive;
    }

    public void reduceDuration() {
        this.duration.set(this.duration.get() - 1);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [baseDuration=" + baseDuration + ", duration=" + duration + ", value=" + value + "]";
    }

    public SimpleIntegerProperty valueProperty() {
        return value;
    }
}


