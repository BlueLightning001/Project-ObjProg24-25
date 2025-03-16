package be.ugent.objprog.minionwars.models;

import be.ugent.objprog.minionwars.minions.Minion;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Player {
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty money;
    private final ObservableList<Minion> minions = FXCollections.observableArrayList();
    private int id;
    public Player(String name, int id) {
        this.name = new SimpleStringProperty(name);
        this.money =new SimpleIntegerProperty(0);
        this.id = id;
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
    public void addMinion(Minion minion) {
        this.minions.add(minion);
    }
    public void removeMinion(Minion minion) {
        this.minions.remove(minion);
    }
    public void addAllMinions(ObservableList<Minion> minions) {
        this.minions.addAll(minions);
    }
    public SimpleStringProperty nameProperty() {
        return this.name;
    }
    public String getName() {
        return name.get();
    }
    public void setName(String name) {
       this.name.set(name);
    }

    public IntegerProperty moneyProperty() {
        return this.money;
    }
    public int getMoney() {
        return money.get();
    }
    public void removeMoney(int amount) {
        if (amount > this.money.get()) {
            throw new IllegalArgumentException("Player doesn't have enough money");
        }
        money.set(money.get() - amount);
    }
    public void addMoney(int amount) {
        this.money.set(money.get() + amount);
    }
    public void setMoney(int money) {
        this.money.set(money);
    }

    public String toString(){
        return "Name: " + getName() + ", Money: " + getMoney();
    }

}
