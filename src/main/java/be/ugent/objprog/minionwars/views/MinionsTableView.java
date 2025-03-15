package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.InfoCircleExample;
import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.minions.MinionTypeImage;
import be.ugent.objprog.minionwars.models.MinionModel;
import javafx.beans.binding.Bindings;
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
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class MinionsTableView extends TableView<Minion> {
    public MinionsTableView(MinionModel model) {
        super();
        setEditable(false);
        setTableMenuButtonVisible(false);
        System.out.println(model.getMinions());
        setItems(model.getMinions());

        TableColumn<Minion, ImageView> minionIconCol = getMinionImageViewTableColumn();


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
                    priceLabel.textProperty().bind(Bindings.format("Cost: %d", minion.costProperty()));

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

        getColumns().setAll(minionIconCol,nameCol,statsCol);
        // Adjust the column widths to fit the content
        minionIconCol.prefWidthProperty().bind(widthProperty().multiply(0.2));
        nameCol.prefWidthProperty().bind(widthProperty().multiply(0.6));
        statsCol.prefWidthProperty().bind(widthProperty().multiply(0.2));

        getStylesheets().add(MinionsTableView.class.getResource("/be/ugent/objprog/minionwars/css/tableview.css").toExternalForm());
        getStyleClass().add("noheader");
    }

    private static TableColumn<Minion, ImageView> getMinionImageViewTableColumn() {
        TableColumn<Minion, ImageView> minionIconCol = new TableColumn<>();
        minionIconCol.setCellValueFactory(cell -> {

            Image image = cell.getValue().getMinionIcon();
            ImageView imageView = new ImageView(image);
            return new SimpleObjectProperty<>(imageView);
        });
        minionIconCol.setCellFactory(column -> new TableCell<Minion, ImageView>() {
            @Override
            protected void updateItem(ImageView imageView, boolean empty) {
                super.updateItem(imageView, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null || imageView == null) {
                    setGraphic(null);
                } else {
                    // Creating the circle for the clip
                    Circle circle = new Circle();
                    circle.setFill(new ImagePattern(imageView.getImage()));
                    circle.radiusProperty().bind(getTableColumn().prefWidthProperty().multiply(0.4));

                    circle.setCenterY(getTableRow().getHeight() / 2);
                    circle.setCenterX(getTableColumn().getWidth() / 2);
                    setGraphic(circle);
                }
            }
        });
        return minionIconCol;
    }


}
