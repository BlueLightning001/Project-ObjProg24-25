package be.ugent.objprog.minionwars.models;

import be.ugent.objprog.minionwars.JDOMReader;
import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.minions.MinionTypeImage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.InputStream;
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

    public void enableDespicableMode() {
        for (Minion minion : minions) {
            MinionTypeImage minionImage = MinionTypeImage.valueOf(minion.getType().toUpperCase().replace("-", "_"));

            // Try despicable mode image
            String despicablePath = minionImage.getImagePath(true);
            InputStream stream = getClass().getResourceAsStream(despicablePath);
            if (stream != null) {
                try (stream) {
                    Image image = new Image(stream);
                    minion.setMinionIcon(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("❌ Despicable image not found: " + despicablePath);
            }

            // If not set, default will show
        }

    }
}
