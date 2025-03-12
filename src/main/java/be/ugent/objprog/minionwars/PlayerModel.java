package be.ugent.objprog.minionwars;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PlayerModel {
    private final SimpleObjectProperty<Player> player1 = new SimpleObjectProperty<>(new Player(null));
    private final SimpleObjectProperty<Player> player2 = new SimpleObjectProperty<>(new Player(null));
    private final List<ObjectProperty<Player>> players = List.of(player1, player2);
    private final SimpleIntegerProperty startBudget = new SimpleIntegerProperty(MIN_START_BUDGET);

    private static final int MIN_START_BUDGET = 10;
    private static final int MAX_START_BUDGET = 50;

    public int getMaxStartBudget() { return MAX_START_BUDGET; }
    public int getMinStartBudget() { return MIN_START_BUDGET; }
    public int getStartBudget() { return startBudget.get(); }
    public void setStartBudget(int budget) { startBudget.set(budget); }
    public SimpleIntegerProperty startBudgetProperty() { return startBudget; }

    public ObjectProperty<Player> player1Property() { return player1; }
    public ObjectProperty<Player> player2Property() { return player2; }

    public boolean isValidName(String name, Player excludedPlayer) {
        if (name == null || name.isBlank()) return false;
        return players.stream()
                .map(ObjectProperty::get)
                .filter(p -> p != null && p.getName() != null)
                .noneMatch(p -> p.getName().equals(name) && !p.getName().equals(excludedPlayer.getName()));
    }


    public boolean areValidPlayers() {
        System.out.println(players);
        return players.stream()
                .map(ObjectProperty::get)
                .allMatch(p -> p != null && isValidName(p.getName(), p));
    }

    public boolean isValidStartBudget() {
        int budget = startBudget.get();
        return budget >= MIN_START_BUDGET && budget <= MAX_START_BUDGET;
    }

    @Override
    public String toString() {
        return players + " Start budget: " + getStartBudget();
    }
}
