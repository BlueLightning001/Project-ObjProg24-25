package be.ugent.objprog.minionwars.minions;

import be.ugent.objprog.minionwars.effects.MinionEffect;
import be.ugent.objprog.minionwars.models.Player;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;

import java.util.List;


public class Minion {
    private final SimpleStringProperty type;
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty cost;
    private final SimpleIntegerProperty movement;
    private final SimpleIntegerProperty attack;
    private final SimpleIntegerProperty defence;
    private final int baseDefence;
    private final int baseAttack;
    private final int baseMovement;
    private final Integer[] baseRange;
    private ObservableList<Integer> range;
    private MinionEffect effect;
    private Image minionIcon;
    private Player owner = null;  //TODO REMOVE UNNECESSARY PROPERTIES AND REPLACE THEM WITH NORMAL VALUES
    private ObservableList<Effect> statusAilments = FXCollections.observableArrayList();
    private boolean moved = false;
    private boolean attacked = false;

    public Minion(String type, String name, int cost, int movement, Integer[] range, int attack, int defence, MinionEffect effect, Image minionIcon) {
        this.type = new SimpleStringProperty(type);
        this.name = new SimpleStringProperty(name);
        this.cost = new SimpleIntegerProperty(cost);
        this.movement = new SimpleIntegerProperty(movement);
        this.range = new SimpleListProperty<>(FXCollections.observableArrayList(range));
        this.baseRange = range;
        this.baseMovement = movement;
        this.attack = new SimpleIntegerProperty(attack);
        this.defence = new SimpleIntegerProperty(defence);
        this.baseDefence = defence;
        this.baseAttack = attack;
        this.effect = effect;
        this.minionIcon = minionIcon;
    }

    public void addDefence(int value) {
        this.defence.set(this.defence.get() + value);
        if (this.defence.get() > baseDefence) {
            this.defence.set(baseDefence);
        }
    }
    public void resetActions(){
        moved = false;
        attacked = false;
    }
    public boolean hasActions(){
        return moved || attacked;
    }
    public void addStatusAilment(Effect effect) {
        statusAilments.add(effect);
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

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public ObservableList<Effect> getStatusAilments() {
        return statusAilments;
    }

    public String getType() {
        return type.get();
    }

    public boolean isAttacked() {
        return attacked;
    }

    public void setAttacked(boolean attacked) {
        this.attacked = attacked;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public SimpleIntegerProperty movementProperty() {
        return movement;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void removeStatusAilment(Effect effect) {
        statusAilments.remove(effect);
    }

    @Override
    public String toString() {
        return type.get().toUpperCase() + ":  NAME: " + getName() + ", COST: " + getCost() + ", MOVEMENT: " + getMovement() + ",RANGE: " + getRange() + ", IMAGE: " + minionIcon;
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

    public List<Integer> getRange() {
        return range.stream().toList();
    }

    public void setRange(Integer[] range) {
        this.range.setAll(range);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }
}
