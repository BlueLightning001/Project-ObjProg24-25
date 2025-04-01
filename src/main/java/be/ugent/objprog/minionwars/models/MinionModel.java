package be.ugent.objprog.minionwars.models;

import be.ugent.objprog.minionwars.JDOMReader;
import be.ugent.objprog.minionwars.minions.Minion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class MinionModel {
    private final ObservableList<Minion> minions;

    public MinionModel(JDOMReader reader) {
        minions = FXCollections.observableArrayList();
        setMinions(reader.getMinions());
    }

    public void addMinion(Minion minion) {
        minions.add(minion);
    }

    public ObservableList<Minion> getMinions() {
        return minions;
    }

    public void setMinions(List<Minion> minions) {
        this.minions.setAll(minions);
    }

    public void removeMinion(Minion minion) {
        minions.remove(minion);
    }

}
