package be.ugent.objprog.minionwars;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Player {
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty money;
    public Player(String name) {
        this.name = new SimpleStringProperty(name);
        this.money =new SimpleIntegerProperty(0);
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
    public void setMoney(int money) {
        this.money.set(money);
    }

    public String toString(){
        return "Name: " + getName() + ", Money: " + getMoney();
    }

}
