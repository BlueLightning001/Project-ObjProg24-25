package be.ugent.objprog.minionwars.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.List;
import java.util.Random;

public class PlayerModel {
    private static final int MIN_START_BUDGET = 10;
    private static final int MAX_START_BUDGET = 50;
    private final SimpleObjectProperty<Player> player1;
    private final SimpleObjectProperty<Player> player2;
    private final List<SimpleObjectProperty<Player>> players;
    private final SimpleIntegerProperty startBudget;
    private final SimpleObjectProperty<Player> currentPlayer ;  // random first player that starts
    private final SimpleIntegerProperty turnCounter;

    public PlayerModel() {
        player1 = new SimpleObjectProperty<>(new Player(null));
        player2 = new SimpleObjectProperty<>(new Player(null));
        currentPlayer = new SimpleObjectProperty<>(null);
        turnCounter = new SimpleIntegerProperty(0);
        players = List.of(player1, player2);
        currentPlayer.set(players.get(new Random().nextInt( 2)).get());
        startBudget = new SimpleIntegerProperty(MIN_START_BUDGET);
    }

    public void addMoney(int amount, Player player) {
        player.setMoney(player.getMoney() + amount);
    }

    public boolean allPlayersHaveNames() {
        return player1.get() != null && player2.get() != null &&
                player1.get().getName() != null && player2.get().getName() != null &&
                !player1.get().getName().isBlank() && !player2.get().getName().isBlank();
    }

    public void nextPlayer() {
        if (currentPlayer.get() == player1.get()) {
            currentPlayer.set(player2.get());
        } else {
            currentPlayer.set(player1.get());
        }
        turnCounter.set(turnCounter.get() + 1); // To show how many turns you played after the game ends
    }


    public int getMaxStartBudget() {
        return MAX_START_BUDGET;
    }

    public int getMinStartBudget() {
        return MIN_START_BUDGET;
    }

    public Player getPlayer1() {
        return player1.get();
    }

    public Player getPlayer2() {
        return player2.get();
    }

    public Player getCurrentPlayer() {
        return currentPlayer.get();
    }

    public List<SimpleObjectProperty<Player>> getPlayers() {
        return players;
    }

    public void giveStartBudget() {
        player1.get().setMoney(startBudget.get());
        player2.get().setMoney(startBudget.get());
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

    public SimpleObjectProperty<Player> currentPlayerProperty() {
        return currentPlayer;
    }

    public boolean playersHaveDistinctNames() {
        return player1.get() != null && player2.get() != null &&
                !player1.get().getName().trim().equalsIgnoreCase(player2.get().getName().trim());
    }

    public void removeMoney(int amount, Player player) {
        if (player.getMoney() < amount) { //TODO temporary
            throw new IllegalArgumentException("You don't have enough money to remove the money");
        }
        player.setMoney(player.getMoney() - amount);
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
