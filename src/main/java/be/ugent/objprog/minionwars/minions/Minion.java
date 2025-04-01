package be.ugent.objprog.minionwars.minions;

import be.ugent.objprog.minionwars.effects.EffectFactory;
import be.ugent.objprog.minionwars.effects.MinionEffect;
import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.tiles.Tile;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
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
    public final static int HEAL_CHARGE_VALUE = 2;
    private final SimpleStringProperty type;
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty cost;
    private final SimpleIntegerProperty movement;
    private final SimpleIntegerProperty attack;
    private final SimpleIntegerProperty defence;
    private final int baseDefence;
    private final int baseAttack;
    private final int baseMovement;
    private final int baseRecoveryCharges = 2;
    private final BooleanBinding hasActionsProperty;
    private final SimpleIntegerProperty recoveryCharges = new SimpleIntegerProperty(baseRecoveryCharges);
    private final SimpleIntegerProperty healCharges = new SimpleIntegerProperty(2);
    private final Integer[] baseRange;
    private final SimpleBooleanProperty moved = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty attacked = new SimpleBooleanProperty(false);
    private final ObservableList<Integer> range;
    private final Image minionIcon;
    private final ObservableList<MinionEffect> statusAilments = FXCollections.observableArrayList();
    private MinionEffect effect;
    private Player owner = null;
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

        this.hasActionsProperty = Bindings.createBooleanBinding(
                () -> !(moved.get() && attacked.get()),  // Should be false when both are true
                moved,
                attacked
        );


    }

    public void activateStatusAilments() {
        for (MinionEffect effect : statusAilments) {
            if (effect.getDuration() > 0) {
                effect.applyEffect(this);
            }

        }
        reduceAilmentValues();
    }

    public void reduceAilmentValues() {
        for (MinionEffect minionEffect : new ArrayList<>(statusAilments)) {
            minionEffect.reduceDuration();
            if (minionEffect.getDuration() < 0) {
                removeStatusAilment(minionEffect);
            }
        }
    }

    public void removeStatusAilment(MinionEffect effect) {
        statusAilments.remove(effect);
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

    public Minion copy() {
        return new Minion(this.type.get(), this.name.get(), this.cost.get(), this.movement.get(),
                List.of(this.range.getFirst(), this.range.getLast()).toArray(new Integer[2]),
                this.attack.get(), this.defence.get(), this.effect,
                this.minionIcon);

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

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getBaseDefence() {
        return baseDefence;
    }

    public int getBaseMovement() {
        return baseMovement;
    }

    public Integer[] getBaseRange() {
        return baseRange;
    }

    public double getBaseRecoveryCharges() {
        return baseRecoveryCharges;
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

    public int getHealCharges() {
        return healCharges.get();
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

    public int getRecoveryCharges() {
        return recoveryCharges.get();
    }

    public void setRecoveryCharges(int recoveryCharges) {
        this.recoveryCharges.set(recoveryCharges);
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

    public BooleanBinding hasActionsProperty() {
        return hasActionsProperty;
    }

    public boolean hasAttacked() {
        return attacked.get();
    }

    public boolean hasMoved() {
        return moved.get();
    }

    public boolean hasSpecialAttack() {
        return this.effect != null;
    }

    public void heal(int value) {
        this.defence.set(this.defence.get() + value);
        if (this.defence.get() > baseDefence) {
            this.defence.set(baseDefence);
        }
    }

    public SimpleIntegerProperty healChargesProperty() {
        return healCharges;
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

    public void refillActions() {
        setMoved(false);
        setAttacked(false);
    }

    public void setAttacked(boolean attacked) {
        this.attacked.set(attacked);
    }

    public void rest() {
        if (recoveryCharges.get() < baseRecoveryCharges) {
            recoveryCharges.set(recoveryCharges.get() + 1);
        }
        setAttacked(true);
        setMoved(true);
    }

    public void restoreStats() {
        this.setMovement(this.baseMovement);
        this.setAttack(this.baseAttack);
        this.range.setAll(this.baseRange);
    }

    public void specialAttack(Minion target) {
        attack(target);
        if (effect != null) {
            EffectFactory effectFactory = new EffectFactory();

            MinionEffect effectClone = effectFactory.createEffect(effect.getClass().getSimpleName().toLowerCase().replace("effect", ""),
                    effect.getName(),
                    effect.getDuration(), effect.getValue());
            if (effect.isOffensive()) {
                target.addStatusAilment(effectClone);
            } else {
                this.addStatusAilment(effectClone);
            }
        }
        recoveryCharges.set(0);
    }

    public void attack(Minion target) {
        int attackPower = this.attack.get();
        target.decreaseDefence(attackPower);
        setAttacked(true);
    }

    public void addStatusAilment(MinionEffect effect) {
        if (effect != null) {
            statusAilments.removeIf(e -> e.getClass().equals(effect.getClass()) && e.getValue() <= effect.getValue()); // "Refreshes" the statusAilment if it is higher
            statusAilments.add(effect);
        }
    }

    public void decreaseDefence(int value) {
        if (this.defence.get() <= value) { //Character dies
            markDead();
        }
        this.defence.set(this.defence.get() - value);
    }

    public void markDead() {
        this.getOccupiedTile().setOccupant(null);
        owner.removeMinion(this);

    }

    public Tile getOccupiedTile() {
        return occupiedTile;
    }

    public void setOccupiedTile(Tile occupiedTile) {
        this.occupiedTile = occupiedTile;
    }

    public boolean specialReady() {
        return recoveryChargesProperty().get() >= baseRecoveryCharges;
    }

    public SimpleIntegerProperty recoveryChargesProperty() {
        return recoveryCharges;
    }

    @Override
    public String toString() {
        return type.get().toUpperCase() + ":  NAME: " + getName() + ", HEALTH: " + getDefence() + ", MOVEMENT: " + getMovement() + ",RANGE: " + getRange() + ", OWNER: " + owner +
                "\n StatusAilments: " + statusAilments;
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

    public void useHealCharge() {
        healCharges.set(healCharges.get() - 1);
        setAttacked(true);
    }


}
