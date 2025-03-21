package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.effects.BlindnessEffect;
import be.ugent.objprog.minionwars.effects.MinionEffect;
import be.ugent.objprog.minionwars.effects.ParalysisEffect;
import be.ugent.objprog.minionwars.effects.PoisonEffect;
import be.ugent.objprog.minionwars.effects.RageEffect;
import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.TileModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class SelectedMinionDisplay extends GridPane {
    private Circle minionsIcon = new Circle();
    private Label minionsLabel = new Label();
    private Label attackStatLabel = new Label();
    private Label defenseStatLabel = new Label();
    private Label ailmentsStatLabel = new Label();
    private Label tileNameLabel = new Label();
    private VBox statusAilmentsContainer;
    private ScrollPane statusAilmentsScroll;
    private final TileModel tileModel;
    private final ResourceBundle bundle;

    public SelectedMinionDisplay(TileModel tileModel, Locale locale) {
        this.tileModel = tileModel;
        this.bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);

        setUpLayout();
        updateSelected(null);


    }

    private void setUpLayout() {
        VBox.setVgrow(this,Priority.SOMETIMES);
        setMinHeight(60);
        setPrefHeight(70);
        setGridLinesVisible(true); //TODO debug
        setMaxHeight(70);
        // Set up minion display
        minionsIcon.setRadius(30);
        minionsIcon.setFill(Color.TRANSPARENT);
        add(minionsIcon, 0, 0);
        GridPane.setRowSpan(minionsIcon, 2);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(20);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(25);

        // Add labels
        setupStatLabel(attackStatLabel);
        setupStatLabel(defenseStatLabel);
        setupStatLabel(ailmentsStatLabel);
        setupStatLabel(minionsLabel);
        setupStatLabel(tileNameLabel);

        add(minionsLabel, 1, 0);
        add(tileNameLabel, 1, 1);

        GridPane statsDisplay = statsDisplay();
        add(statsDisplay, 2, 0);
        GridPane.setRowSpan(statsDisplay, 2);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(55);
        getColumnConstraints().addAll(col1, col2, col3);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(50);
        row1.setMaxHeight(30);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(50);
        row2.setMaxHeight(30);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(0);
        row3.setMaxHeight(50);
        getRowConstraints().setAll(row1, row2, row3);

        // Status ailments display (VBox inside ScrollPane)
        statusAilmentsContainer = new VBox(5);
        statusAilmentsContainer.setAlignment(Pos.TOP_LEFT);

        statusAilmentsScroll = new ScrollPane(statusAilmentsContainer);
        statusAilmentsScroll.setFitToWidth(true);
        statusAilmentsScroll.setPrefHeight(60);
        statusAilmentsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        statusAilmentsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        add(statusAilmentsScroll, 0, 2);
        GridPane.setColumnSpan(statusAilmentsScroll, 3);

        statusAilmentsScroll.managedProperty().bind(statusAilmentsScroll.visibleProperty());
        statusAilmentsScroll.prefHeightProperty().bind(Bindings.when(statusAilmentsScroll.visibleProperty()).then(50).otherwise(0));

        maxHeightProperty().bind(
                Bindings.when(statusAilmentsScroll.visibleProperty())
                        .then(130)  // If visible, height expands
                        .otherwise(70) // If hidden, height shrinks
        );
        minHeightProperty().bind(
                Bindings.when(statusAilmentsScroll.visibleProperty())
                .then(90)  // If visible, height expands
                .otherwise(60) // If hidden, height shrinks
        );
        statusAilmentsScroll.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                row1.setPercentHeight(35);
                row2.setPercentHeight(35);
                row3.setPercentHeight(30);
            } else {
                row1.setPercentHeight(50);
                row2.setPercentHeight(50);
                row3.setPercentHeight(0);
            }
            this.requestLayout();
        });


    }

    private void setupStatLabel(Label label) {
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setStyle("-fx-font-weight: bolder; -fx-font-size: 15");
        GridPane.setHalignment(label, HPos.CENTER);
        GridPane.setValignment(label, VPos.CENTER);
    }

    private GridPane statsDisplay() {
        GridPane statsGrid = new GridPane();

        statsGrid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);

        statsGrid.getColumnConstraints().addAll(col1, col2);

        RowConstraints row1 = new RowConstraints();
        row1.setVgrow(Priority.ALWAYS);

        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);

        statsGrid.getRowConstraints().addAll(row1, row2);

        statsGrid.add(attackStatLabel, 0, 0);
        statsGrid.add(defenseStatLabel, 1, 0);
        statsGrid.add(ailmentsStatLabel, 0, 1);
        GridPane.setColumnSpan(ailmentsStatLabel, 2);

        getNodeByRowColumnIndex(1, 0, statsGrid).setStyle("-fx-border-width: 2 0 0 0; -fx-border-color: #050505;");

        return statsGrid;
    }

    public void updateSelected(HexTile hexTile) {
        if (hexTile != null) {
            Minion minion = hexTile.getTile().getOccupant();
            if (minion == null) {
                clearLabels();
                setVisible(false);
            } else {
                setVisible(true);
                minionsIcon.setFill(new ImagePattern(minion.getMinionIcon()));
                minionsLabel.setText(minion.getName());

                attackStatLabel.setText("" + minion.getAttack());
                attachStatIcon(attackStatLabel, "/be/ugent/objprog/minionwars/images/icons/attack-D60000.png");

                defenseStatLabel.setText(minion.getDefence() + "/" + minion.getBaseDefence());
                attachStatIcon(defenseStatLabel, "/be/ugent/objprog/minionwars/images/icons/health-D60000.png");

                // Clear previous status ailments
                statusAilmentsContainer.getChildren().clear();

                if (minion.getStatusAilments().isEmpty()) {
                    ailmentsStatLabel.setText("");
                    ailmentsStatLabel.setGraphic(null);
                    statusAilmentsScroll.setVisible(false);

                } else {

                    statusAilmentsScroll.setVisible(true);
                    HBox ailmentsLabelBox = new HBox();
                    for (MinionEffect effect : minion.getStatusAilments()) {
                        HBox effectBox = new HBox(5);
                        effectBox.setAlignment(Pos.CENTER_LEFT);

                        ImageView effectImageView = new ImageView(effect.getImage());
                        effectImageView.setPreserveRatio(true);
                        effectImageView.setFitHeight(20);

                        Label effectLabel = new Label(MessageFormat.format(bundle.getString("effect.message"),
                                effect.getName(Locale.getDefault()), effect.getDuration()));
                        effectBox.getChildren().addAll(effectImageView, effectLabel);
                        statusAilmentsContainer.getChildren().add(effectBox);
                    }
                    ailmentsStatLabel.setText("");
                    ailmentsStatLabel.setGraphic(ailmentsLabelBox);

                }
            }
            tileNameLabel.setText(MessageFormat.format(bundle.getString("menuPart2.tileNameText"),
                    tileModel.getTileName(hexTile.getTile().getClass())));
        } else {
            clearLabels();
            setVisible(false);
        }
    }

    private void attachStatIcon(Label label, String path) {
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(path)));
        imageView.setPreserveRatio(true);
        imageView.fitHeightProperty().bind(label.heightProperty().multiply(0.8));
        label.setGraphic(imageView);
    }

    private void clearLabels() {
        minionsIcon.setFill(Color.TRANSPARENT);
        minionsLabel.setText("");
        attackStatLabel.setText("");
        attackStatLabel.setGraphic(null);
        defenseStatLabel.setText("");
        defenseStatLabel.setGraphic(null);
        ailmentsStatLabel.setText("");
        ailmentsStatLabel.setGraphic(null);
        tileNameLabel.setText("");
        statusAilmentsContainer.getChildren().clear();
    }
    public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) != null && javafx.scene.layout.GridPane.getColumnIndex(node) != null
                    && GridPane.getRowIndex(node) == row && javafx.scene.layout.GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
    }
}
