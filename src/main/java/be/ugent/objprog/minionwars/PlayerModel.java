package be.ugent.objprog.minionwars;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.Collections;

public class PlayerModel {
    private final ObservableList<Player> players = FXCollections.observableArrayList();

    public PlayerModel() {
    }
    public ObservableList<Player> getPlayers() {
        return players;
    }
    public void setPlayers(ObservableList<Player> players) {
        this.players.clear();
        this.players.addAll(players);
    }
    public void addPlayer(Player player) {
        this.players.add(player);
    }
    public void removePlayer(Player player) {
        this.players.remove(player);
    }


}
