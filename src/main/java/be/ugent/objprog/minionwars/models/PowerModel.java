package be.ugent.objprog.minionwars.models;

import be.ugent.objprog.minionwars.JDOMReader;
import be.ugent.objprog.minionwars.powers.Power;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Locale;

public class PowerModel {
    private final ObservableList<Power> powers ;

    public PowerModel(JDOMReader reader, Locale locale) {
        this.powers =FXCollections.observableArrayList(reader.getPowers());
    }
    public ObservableList<Power> getPowers() {
        return powers;
    }

}
