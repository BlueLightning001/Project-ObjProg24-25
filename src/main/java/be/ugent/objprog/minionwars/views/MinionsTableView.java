package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.effects.MinionEffect;
import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.MinionModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Objects;

public class MinionsTableView extends TableView<Minion> {
    public MinionsTableView(MinionModel model) {
        super();
        setEditable(false);
        setTableMenuButtonVisible(false);
        System.out.println(model.getMinions());
        setItems(model.getMinions());

        TableColumn<Minion, ImageView> minionIconCol = getMinionImageViewTableColumn();


        TableColumn<Minion, String> nameCol = new TableColumn<>();
        nameCol.setCellValueFactory(cell -> {
            return new SimpleStringProperty(cell.getValue().getName());
        });
        nameCol.setCellFactory(column -> new TableCell<Minion, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.isBlank()) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(item);

                    // Dynamically adjust font size
                    ChangeListener<Number> resizeListener = (obs, oldSize, newSize) -> updateFontSize();

                    widthProperty().addListener(resizeListener);
                    tableRowProperty().addListener((obs, oldRow, newRow) -> {
                        if (newRow != null) {
                            newRow.heightProperty().addListener(resizeListener);
                        }
                    });

                    updateFontSize();
                    setAlignment(Pos.CENTER);
                    setWrapText(true);

                }
            }


            private void updateFontSize() {
                TableRow<Minion> row = getTableRow();
                if (row != null) {
                    double fontSize = Math.min(getWidth() * 0.12, row.getHeight() * 0.5); // Scale factor
                    setFont(Font.font("Monotype Corsiva", FontWeight.EXTRA_BOLD, fontSize));
                }
            }
        });


        TableColumn<Minion, GridPane> statsCol = new TableColumn<>();
        // (returns empty property, since we don't use it directly)
        statsCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(null));


        statsCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(GridPane gridPane, boolean empty) {
                super.updateItem(gridPane, empty);
                double iconScale = 0.2;
                double fontScale = 0.15;
                Minion minion = getTableRow() != null ? getTableRow().getItem() : null;

                if (empty || minion == null) {
                    setGraphic(null);
                } else {
                    // Price label
                    Label priceLabel = new Label();
                    priceLabel.textProperty().bind(Bindings.convert(minion.costProperty()));
                    ImageView priceImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/coin-FFB900.png"))));
                    priceLabel.setGraphic(priceImageView);

                    // Scaling
                    priceImageView.setPreserveRatio(true);
                    priceImageView.fitHeightProperty().bind(getTableColumn().widthProperty().multiply(iconScale));
                    priceImageView.fitWidthProperty().bind(priceImageView.fitHeightProperty());
                    priceLabel.styleProperty().bind(
                            Bindings.format("-fx-font-size: %.2fpx;", getTableColumn().widthProperty().multiply(fontScale))
                    );

                    // Attack label
                    Label attackLabel = new Label();
                    attackLabel.textProperty().bind(Bindings.convert(minion.attackProperty()));
                    ImageView attackImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/attack-D60000.png"))));
                    attackLabel.setGraphic(attackImageView);


                    attackImageView.setPreserveRatio(true);
                    attackImageView.fitHeightProperty().bind(getTableColumn().widthProperty().multiply(iconScale));
                    attackImageView.fitWidthProperty().bind(attackImageView.fitHeightProperty());
                    attackLabel.styleProperty().bind(
                            Bindings.format("-fx-font-size: %.2fpx;", getTableColumn().widthProperty().multiply(fontScale))
                    );

                    // Defense label
                    Label defenseLabel = new Label();
                    defenseLabel.textProperty().bind(Bindings.convert(minion.defenceProperty()));
                    ImageView defenseImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/health-D60000.png"))));
                    defenseLabel.setGraphic(defenseImageView);


                    defenseImageView.setPreserveRatio(true);
                    defenseImageView.fitHeightProperty().bind(getTableColumn().widthProperty().multiply(iconScale));
                    defenseImageView.fitWidthProperty().bind(defenseImageView.fitHeightProperty());
                    defenseLabel.styleProperty().bind(
                            Bindings.format("-fx-font-size: %.2fpx;", getTableColumn().widthProperty().multiply(fontScale))
                    );
                    // Range label
                    Label rangeLabel = new Label();
                    rangeLabel.textProperty().bind(Bindings.format("%d-%d", minion.getRange()[0], minion.getRange()[1]  ));
                    ImageView rangeImageView  = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/range-119533.png"))));
                    rangeLabel.setGraphic(rangeImageView);

                    rangeImageView.setPreserveRatio(true);
                    rangeImageView.fitHeightProperty().bind(getTableColumn().widthProperty().multiply(iconScale));
                    rangeImageView.fitWidthProperty().bind(rangeImageView.fitHeightProperty());
                    rangeLabel.styleProperty().bind(
                            Bindings.format("-fx-font-size: %.2fpx;", getTableColumn().widthProperty().multiply(fontScale))
                    );

                    // Effect symbol
                    Label effectLabel = new Label();
                    MinionEffect minionEffect = minion.getEffect();
                    ImageView effectImageView;
                    if (minionEffect != null) {
                        effectImageView = new ImageView(minionEffect.getImage());
                    } else {
                        effectImageView = new ImageView();
                    }

                    effectLabel.setGraphic(effectImageView);
                    effectImageView.setPreserveRatio(true);
                    effectImageView.fitHeightProperty().bind(getTableColumn().widthProperty().multiply(iconScale));
                    effectImageView.fitWidthProperty().bind(effectImageView.fitHeightProperty());

                    GridPane statsGrid = new GridPane(5,5);
                    statsGrid.setAlignment(Pos.CENTER_LEFT);
                    statsGrid.add(priceLabel, 0, 0);
                    statsGrid.add(attackLabel, 0, 1);
                    statsGrid.add(defenseLabel, 1, 0);
                    statsGrid.add(rangeLabel, 1, 1);
                    if (effectImageView.getImage() != null) {
                        statsGrid.add(effectLabel, 0, 2);
                    }

                    setGraphic(statsGrid);
                    setAlignment(Pos.CENTER_LEFT);
                    setPadding(new Insets(5));
                }
            }
        });

        getColumns().setAll(minionIconCol, nameCol, statsCol);
        // Adjust the column widths to fit the content
        minionIconCol.prefWidthProperty().bind(widthProperty().multiply(0.2));
        nameCol.prefWidthProperty().bind(widthProperty().multiply(0.5));
        statsCol.prefWidthProperty().bind(widthProperty().multiply(0.3));

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

                    Circle circle = new Circle();
                    circle.setFill(new ImagePattern(imageView.getImage()));
                    circle.radiusProperty().bind(getTableColumn().prefWidthProperty().multiply(0.35));

                    circle.setCenterY(getTableRow().getHeight() / 2);
                    circle.setCenterX(getTableColumn().getWidth() / 2);
                    setGraphic(circle);
                    setAlignment(Pos.TOP_CENTER);

                }
            }
        });
        return minionIconCol;
    }


}
