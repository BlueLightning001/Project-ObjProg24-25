package be.ugent.objprog.minionwars.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.List;

public class PlayerModel {
    private static final int MIN_START_BUDGET = 10;
    private static final int MAX_START_BUDGET = 50;
    private final SimpleObjectProperty<Player> player1 = new SimpleObjectProperty<>(new Player(null));
    private final SimpleObjectProperty<Player> player2 = new SimpleObjectProperty<>(new Player(null));
    private final List<ObjectProperty<Player>> players = List.of(player1, player2);
    private final SimpleIntegerProperty startBudget = new SimpleIntegerProperty(MIN_START_BUDGET);

    public boolean allPlayersHaveNames() {
        return player1.get() != null && player2.get() != null &&
                player1.get().getName() != null && player2.get().getName() != null &&
                !player1.get().getName().isBlank() && !player2.get().getName().isBlank();
    }

    public int getMaxStartBudget() {
        return MAX_START_BUDGET;
    }

    public int getMinStartBudget() {
        return MIN_START_BUDGET;
    }

    public void giveStartBudget() {
        player1.get().setMoney(startBudget.get());
        player2.get().setMoney(startBudget.get());
    }
    public void addMoney(int amount, Player player) {
        player.setMoney(player.getMoney() + amount);
    }
    public void removeMoney(int amount, Player player) {
        if (player.getMoney() < amount) { //TODO temporary
            throw new IllegalArgumentException("You don't have enough money to remove the money");
        }
        player.setMoney(player.getMoney() - amount);
    }
    public boolean isValidStartBudget() {
        int budget = startBudget.get();
        return budget >= MIN_START_BUDGET && budget <= MAX_START_BUDGET;
    }

    public ObjectProperty<Player> player1Property() {
        return player1;
    }

    public ObjectProperty<Player> player2Property() {
        return player2;
    }

    public boolean playersHaveDistinctNames() {
        return player1.get() != null && player2.get() != null &&
                !player1.get().getName().trim().equalsIgnoreCase(player2.get().getName().trim());
    }

    public SimpleIntegerProperty startBudgetProperty() {
        return startBudget;
    }

    @Override
    public String toString() {
        return players + " Start budget: " + getStartBudget();
    }

    public int getStartBudget() {
        return startBudget.get();
    }

    public void setStartBudget(int budget) {
        startBudget.set(budget);
    }
}
