package be.ugent.objprog.minionwars.minions;

import be.ugent.objprog.minionwars.effects.MinionEffect;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Minion {
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty cost;
    private final SimpleIntegerProperty movement;
    private Integer[] range;
    private final SimpleIntegerProperty attack;
    private final SimpleIntegerProperty defence;
    private MinionEffect effect;

    public Minion(String name, int cost, int movement, Integer[] range, int attack, int defence, MinionEffect effect) {
        this.name = new SimpleStringProperty(name);
        this.cost = new SimpleIntegerProperty(cost);
        this.movement = new SimpleIntegerProperty(movement);
        this.range = range;
        this.attack = new SimpleIntegerProperty(attack);
        this.defence = new SimpleIntegerProperty(defence);
        this.effect = effect;
    }

    public SimpleIntegerProperty attackProperty() {
        return attack;
    }

    public SimpleIntegerProperty costProperty() {
        return cost;
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

    public int getCost() {
        return cost.get();
    }

    public void setCost(int cost) {
        this.cost.set(cost);
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

    public int getMovement() {
        return movement.get();
    }

    public void setMovement(int movement) {
        this.movement.set(movement);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Integer[] getRange() {
        return range;
    }

    public void setRange(Integer[] range) {
        this.range = range;
    }

    public SimpleIntegerProperty movementProperty() {
        return movement;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

}
