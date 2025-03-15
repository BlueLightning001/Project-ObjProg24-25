package be.ugent.objprog.minionwars.minions;

import be.ugent.objprog.minionwars.effects.EffectVisitor;
import be.ugent.objprog.minionwars.effects.MinionEffect;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

import java.util.Arrays;


public class Minion {
    protected final SimpleStringProperty type;
    protected final SimpleStringProperty name;
    protected final SimpleIntegerProperty cost;
    protected final SimpleIntegerProperty movement;
    protected final SimpleIntegerProperty attack;
    protected final SimpleIntegerProperty defence;
    protected Integer[] range;
    protected MinionEffect effect;
    protected Image minionIcon;
    public Minion(String type, String name, int cost, int movement, Integer[] range, int attack, int defence, MinionEffect effect, Image minionIcon) {
        this.type = new SimpleStringProperty(type);
        this.name = new SimpleStringProperty(name);
        this.cost = new SimpleIntegerProperty(cost);
        this.movement = new SimpleIntegerProperty(movement);
        this.range = range;
        this.attack = new SimpleIntegerProperty(attack);
        this.defence = new SimpleIntegerProperty(defence);
        this.effect = effect;
        this.minionIcon = minionIcon;
    }

    public void applyEffectLogic(MinionEffect effect) {
        effect.applyEffect(this);
    }

    public SimpleIntegerProperty attackProperty() {
        return attack;
    }

    public SimpleIntegerProperty costProperty() {
        return cost;
    }

    public void decreaseDefence(int value) {
        if (this.defence.get() < value) {
            throw new IllegalArgumentException("Defence too low " + defence.get());
        }
        this.defence.set(this.defence.get() - value);
    }

    public SimpleIntegerProperty defenceProperty() {
        return defence;
    }

    public int getAttack() {
        return attack.get();
    }

    public void setAttack(int attack) {
        this.attack.set(attack);
    }

    public int getDefence() {
        return defence.get();
    }

    public void setDefence(int defence) {
        this.defence.set(defence);
    }

    public MinionEffect getEffect() {
        return effect;
    }

    public void setEffect(MinionEffect effect) {
        this.effect = effect;
    }

    public Image getMinionIcon() {
        return minionIcon;
    }

    public String getType() {
        return type.get();
    }

    public SimpleIntegerProperty movementProperty() {
        return movement;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    @Override
    public String toString() {
        return type.get().toUpperCase() + ":  NAME: " + getName() + ", COST: " + getCost() + ", MOVEMENT: " + getMovement() + ",RANGE: " + Arrays.toString(getRange()) + ", IMAGE: " + minionIcon;
    }

    public String getName() {
        return name.get();
    }

    public int getCost() {
        return cost.get();
    }

    public void setCost(int cost) {
        this.cost.set(cost);
    }

    public int getMovement() {
        return movement.get();
    }

    public void setMovement(int movement) {
        this.movement.set(movement);
    }

    public Integer[] getRange() {
        return range;
    }

    public void setRange(Integer[] range) {
        this.range = range;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }
}
