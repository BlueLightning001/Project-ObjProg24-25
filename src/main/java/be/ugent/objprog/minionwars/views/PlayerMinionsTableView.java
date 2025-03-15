package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.minions.MinionTypeImage;
import be.ugent.objprog.minionwars.models.MinionModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;

import java.util.Objects;

public class PlayerMinionsTableView extends TableView<Minion> {
    public PlayerMinionsTableView(MinionModel model) {
        super();
        setEditable(false);
        setTableMenuButtonVisible(false);



        TableColumn<Minion, ImageView> minionIconCol = new TableColumn<>();
        minionIconCol.setCellValueFactory(cell -> {
            MinionTypeImage minionType;
            try {
                minionType = MinionTypeImage.valueOf(cell.getValue().getName().toUpperCase());
            } catch (IllegalArgumentException | NullPointerException e) {
                return new SimpleObjectProperty<>(null); // no image found
            }

            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(minionType.getImagePath())));

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(30); // Adjust as needed
            imageView.setFitHeight(30);

            return new SimpleObjectProperty<>(imageView);
        });

        TableColumn<Minion, String> nameCol = new TableColumn<>();
        nameCol.setCellValueFactory(cell ->{
            return new SimpleStringProperty(cell.getValue().getName());
        });

        TableColumn<Minion, VBox> statsCol = new TableColumn<>();
        // Cell value factory (returns empty property, since we don't use it directly)
        statsCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(null));



        // Cell factory to create the VBox dynamically
        statsCol.setCellFactory(column -> new TableCell<Minion, VBox>() {
            @Override
            protected void updateItem(VBox vbox, boolean empty) {
                super.updateItem(vbox, empty);

                Minion minion = getTableRow() != null ? getTableRow().getItem() : null;

                if (empty || minion == null) {
                    setGraphic(null);
                } else {
                    Label priceLabel = new Label();
                    priceLabel.textProperty().bind(Bindings.format("Cost: %.0f", minion.costProperty()));

                    Label attackLabel = new Label();
                    attackLabel.textProperty().bind(Bindings.format("Attack: %d", minion.attackProperty()));

                    Label defenseLabel = new Label();
                    defenseLabel.textProperty().bind(Bindings.format("Defense: %d", minion.defenceProperty()));

                    VBox statsVBox = new VBox(5, priceLabel, attackLabel, defenseLabel);
                    statsVBox.setAlignment(Pos.CENTER_LEFT);

                    setGraphic(statsVBox);
                }
            }
        });
    }


}
