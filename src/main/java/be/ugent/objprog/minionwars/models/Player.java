package be.ugent.objprog.minionwars.models;

import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.powers.Power;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty money;
    private final ObservableList<Minion> minions = FXCollections.observableArrayList();
    private final ObservableList<Power> availablePowers = FXCollections.observableArrayList();
    private static final int MAX_POWER_USAGE = 2;
    public void reduceAvailablePowerUsages() {
        availablePowerUses.set(availablePowerUses.get() - 1);
    }

    public int getAvailablePowerUses() {
        return availablePowerUses.get();
    }
    public void usePower(Power power) {
        if (availablePowerUses.get() > 0 && availablePowers.contains(power)) {
            reduceAvailablePowerUsages();

            List<Power> updatedPowers = new ArrayList<>(availablePowers);
            updatedPowers.remove(power);


            availablePowers.setAll(updatedPowers);
        }
    }

    public SimpleIntegerProperty availablePowerUsesProperty() {
        return availablePowerUses;
    }

    private final SimpleIntegerProperty availablePowerUses = new SimpleIntegerProperty(MAX_POWER_USAGE);
    private int id;

    public Player(String name, int id) {
        this.name = new SimpleStringProperty(name);
        this.money = new SimpleIntegerProperty(0);
        this.id = id;
    }

    public void addAllMinions(ObservableList<Minion> minions) {
        this.minions.addAll(minions);
    }

    public void addMinion(Minion minion) {
        this.minions.add(minion);
    }

    public void addMoney(int amount) {
        this.money.set(money.get() + amount);
    }

    public ObservableList<Power> getAvailablePowers() {
        return availablePowers;
    }

    public int getHomeBaseID() {
        return this.id;
    }

    public ObservableList<Minion> getMinions() {
        return minions;
    }

    public void setMinions(ObservableList<Minion> minions) {
        this.minions.clear();
        this.minions.addAll(minions);
    }

    public IntegerProperty moneyProperty() {
        return this.money;
    }

    public SimpleStringProperty nameProperty() {
        return this.name;
    }

    public void removeMinion(Minion minion) {
        minion.setOwner(null);
        this.minions.remove(minion);
    }

    public void removeMoney(int amount) {
        if (amount > this.money.get()) {
            throw new IllegalArgumentException("Player doesn't have enough money");
        }
        money.set(money.get() - amount);
    }

    public String toString() {
        return "Name: " + getName() + ", Money: " + getMoney();
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getMoney() {
        return money.get();
    }

    public void setMoney(int money) {
        this.money.set(money);
    }

    public void setAvailablePowers(ObservableList<Power> availablePowers) {
        this.availablePowers.clear();
        this.availablePowers.addAll(availablePowers);
    }
    public void addAllPowers(ObservableList<Power> powers) {
        this.availablePowers.addAll(powers);
    }
}
