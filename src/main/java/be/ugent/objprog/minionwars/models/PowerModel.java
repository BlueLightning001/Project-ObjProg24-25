package be.ugent.objprog.minionwars.models;

import be.ugent.objprog.minionwars.JDOMReader;
import be.ugent.objprog.minionwars.effects.EffectFactory;
import be.ugent.objprog.minionwars.effects.MinionEffect;
import be.ugent.objprog.minionwars.powers.Power;
import be.ugent.objprog.minionwars.powers.PowerFactory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PowerModel {
    private final ObservableList<Power> powers;
    private final PowerFactory powerFactory = new PowerFactory();
    private final EffectFactory effectFactory = new EffectFactory();
    private final SimpleObjectProperty<Power> selectedPower = new SimpleObjectProperty<>();

    public PowerModel(JDOMReader reader, Locale locale) {
        this.powers = FXCollections.observableArrayList(reader.getPowers());
    }

    public ObservableList<Power> getBasePowers() {
        return powers;
    }

    public List<Power> getPowerList() {
        return powers.stream()
                .map(p -> {
                    MinionEffect newEffect = null;
                    if (p.getEffect() != null) {
                        newEffect = effectFactory.createEffect(
                                p.getEffect().getClass().getSimpleName().replace("Effect", "").toLowerCase(),
                                p.getEffect().getDuration(),
                                p.getEffect().getValue()
                        );
                    }
                    return powerFactory.createPower(
                            p.getClass().getSimpleName().replace("Power", "").toLowerCase(),
                            p.getRadius(),
                            p.getValue(),
                            newEffect
                    );
                })
                .collect(Collectors.toList());
    }

    public Power getSelectedPower() {
        return selectedPower.get();
    }

    public void setSelectedPower(Power selectedPower) {
        this.selectedPower.set(selectedPower);
    }

    public SimpleObjectProperty<Power> selectedPowerProperty() {
        return selectedPower;
    }


}
