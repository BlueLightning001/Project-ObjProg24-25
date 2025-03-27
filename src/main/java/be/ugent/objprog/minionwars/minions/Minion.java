package be.ugent.objprog.minionwars.minions;

import be.ugent.objprog.minionwars.effects.MinionEffect;
import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.tiles.Tile;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.util.ArrayList;
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
    private final SimpleBooleanProperty moved = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty attacked = new SimpleBooleanProperty(false);
    private ObservableList<Integer> range;
    private MinionEffect effect;
    private Image minionIcon;
    private Player owner = null;  //TODO REMOVE UNNECESSARY PROPERTIES AND REPLACE THEM WITH NORMAL VALUES
    private ObservableList<MinionEffect> statusAilments = FXCollections.observableArrayList();
    private Tile occupiedTile = null;

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

    public void activateStatusAilments() {
        for (MinionEffect effect : statusAilments) {
            System.out.println("ACTIVATING STATUS AILMENT: " + effect.getClass().getSimpleName());
            effect.applyEffect(this);
        }
    }

    public void addStatusAilment(MinionEffect effect) {
        if (effect != null) {
            statusAilments.removeIf(e -> e.getClass().equals(effect.getClass()) && e.getValue() <= effect.getValue()); // "Refreshes" the statusAilment if it is higher
            statusAilments.add(effect);
            System.out.println(statusAilments);
        }
    }

    public void applyEffectLogic(MinionEffect effect) {
        if (effect != null) {
            effect.applyEffect(this);
        }
    }

    public SimpleIntegerProperty attackProperty() {
        return attack;
    }

    public SimpleBooleanProperty attackedProperty() {
        return attacked;
    }

    @Override
    public Minion clone() {
        try {
            return new Minion(this.type.get(), this.name.get(), this.cost.get(), this.movement.get(),
                    List.of(this.range.getFirst(), this.range.getLast()).toArray(new Integer[2]),
                    this.attack.get(), this.defence.get(), this.effect,
                    this.minionIcon);
        } catch (Exception e) {
            throw new AssertionError("Cloning failed", e); // Should never happen
        }
    }

    public SimpleIntegerProperty costProperty() {
        return cost;
    }

    public void decreaseDefence(int value) {
        System.out.println("CURRENT DEFENSE: " + defence.get());
        System.out.println("VALUE: " + value);
        if (this.defence.get() <= value) { //Character dies
            markDead();
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

    public int getBaseDefence() {
        return baseDefence;
    }

    public int getCost() {
        return cost.get();
    }

    public void setCost(int cost) {
        this.cost.set(cost);
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

    public Tile getOccupiedTile() {
        return occupiedTile;
    }

    public void setOccupiedTile(Tile occupiedTile) {
        this.occupiedTile = occupiedTile;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public ObservableList<MinionEffect> getStatusAilments() {
        return statusAilments;
    }

    public String getType() {
        return type.get();
    }

    public boolean hasActions() {
        return !moved.get() || !attacked.get();
    }

    public boolean hasAttacked() {
        return attacked.get();
    }
    public void attack(Minion target) {
        int attackPower = this.attack.get();
        target.decreaseDefence(attackPower);
        setAttacked(true);
    }
    public void markDead(){
        this.getOccupiedTile().setOccupant(null);
        owner.removeMinion(this);

    }
    public boolean hasMoved() {
        return moved.get();
    }

    public void heal(int value) {
        this.defence.set(this.defence.get() + value);
        if (this.defence.get() > baseDefence) {
            this.defence.set(baseDefence);
        }
    }

    public void moveTo(Tile newTile) {
        setMoved(true);

        Tile oldTile = this.occupiedTile;


        oldTile.setOccupant(null);
        newTile.setOccupant(this);

    }

    public void setMoved(boolean moved) {
        this.moved.set(moved);
    }

    public SimpleBooleanProperty movedProperty() {
        return moved;
    }

    public SimpleIntegerProperty movementProperty() {
        return movement;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void reduceAilmentValue() {
        for (MinionEffect minionEffect : new ArrayList<>(statusAilments)) {
            minionEffect.reduceDuration();
            if (minionEffect.getDuration() <= 0) {
                removeStatusAilment(minionEffect);
            }
        }
    }

    public void removeStatusAilment(MinionEffect effect) {
        statusAilments.remove(effect);
    }

    public void refillActions() {
        setMoved(false);
        setAttacked(false);
    }

    public void setAttacked(boolean attacked) {
        this.attacked.set(attacked);
    }

    @Override
    public String toString() {
        return type.get().toUpperCase() + ":  NAME: " + getName() + ", HEALTH: " + getDefence() + ", MOVEMENT: " + getMovement() + ",RANGE: " + getRange() + ", OWNER: " + owner +
                "\n StatusAilments: " + statusAilments.toString();
    }

    public String getName() {
        return name.get();
    }

    public int getDefence() {
        return defence.get();
    }

    public void setDefence(int defence) {
        this.defence.set(defence);
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
