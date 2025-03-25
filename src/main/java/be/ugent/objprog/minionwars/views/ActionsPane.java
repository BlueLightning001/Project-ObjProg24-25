package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.models.PowerModel;
import be.ugent.objprog.minionwars.powers.Power;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class ActionsPane extends TabPane {
    private final PowerModel powerModel;
    private final ResourceBundle bundle;
    private Locale locale;
    private double font = 15;
    private double error = 50; //%
    private Button moveButton;
    private Button attackButton;
    private Button specialAttackButton;
    private Button healButton;
    private final Tab moveTab;
    private final Tab attackTab;
    private final Tab specialTab;
    public ActionsPane(PowerModel powerModel, Locale locale) {
        super();
        this.locale = locale;
        this.powerModel = powerModel;
        bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);

        //// Moving
        moveTab = getMoveTab();

        //// Attacking
        attackTab = getAttackTab();

        //// Special Moves
        specialTab = new Tab(bundle.getString("actions.special"));
        ListView<Power> powerListView = new ListView<>(this.powerModel.getPowers());
        powerListView.setCellFactory(listView -> new ListCell<>() {
            private final Label nameLabel = new Label();
            private final Label descriptionLabel = new Label();

            // Detail labels for each value
            private final Label valueLabel = new Label();
            private final Label radiusLabel = new Label();
            private final Label durationLabel = new Label();
            private final Label effectLabel = new Label();

            // Icon views for each detail
            private final ImageView powerIcon = new ImageView();
            private final ImageView valueIcon = new ImageView();
            private final ImageView radiusIcon = new ImageView();
            private final ImageView durationIcon = new ImageView();
            private final ImageView effectIcon = new ImageView();

            private final Image radiusImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/range-119533.png")));
            private final Image durationImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/duration-0073FF.png")));

            private final Separator separator = new Separator();
            private final VBox infoBox = new VBox(nameLabel, descriptionLabel);
            private final GridPane detailsGrid = new GridPane();
            private final HBox cellContainer = new HBox(powerIcon, infoBox, separator, detailsGrid);

            {

                separator.setOrientation(Orientation.VERTICAL);


                nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20");
                descriptionLabel.setStyle("-fx-font-size: 16;");
                descriptionLabel.setWrapText(true);
                descriptionLabel.setMaxWidth(200);
                descriptionLabel.setPrefWidth(200);
                descriptionLabel.setMinWidth(100);
                HBox.setHgrow(descriptionLabel, Priority.NEVER);


                powerIcon.setFitWidth(70);
                powerIcon.setFitHeight(70);
                powerIcon.setPreserveRatio(true);

                valueIcon.setFitWidth(20);
                valueIcon.setFitHeight(20);
                radiusIcon.setFitWidth(20);
                radiusIcon.setFitHeight(20);
                durationIcon.setFitWidth(20);
                durationIcon.setFitHeight(20);
                effectIcon.setFitWidth(20);
                effectIcon.setFitHeight(20);


                valueLabel.setStyle("-fx-font-size: 14;");
                radiusLabel.setStyle("-fx-font-size: 14;");
                durationLabel.setStyle("-fx-font-size: 14;");
                effectLabel.setStyle("-fx-font-size: 14;");

                // Set up the detailsGrid with two rows and two columns
                detailsGrid.setHgap(5);
                detailsGrid.setVgap(5);
                detailsGrid.add(valueLabel, 0, 0);
                detailsGrid.add(radiusLabel, 1, 0);
                detailsGrid.add(durationLabel, 0, 1);
                detailsGrid.add(effectLabel, 1, 1);



                // Set alignment and spacing for containers
                cellContainer.setSpacing(10);
                infoBox.setSpacing(2);
                detailsGrid.setAlignment(Pos.CENTER_LEFT);
                separator.setPrefWidth(5);

                detailsGrid.setMaxWidth(Double.MAX_VALUE);
                GridPane.setHgrow(detailsGrid, Priority.ALWAYS);

                detailsGrid.prefWidthProperty().bind(listView.widthProperty().multiply(0.1));
                infoBox.prefWidthProperty().bind(listView.widthProperty().multiply(0.5));
            }

            @Override
            protected void updateItem(Power power, boolean empty) {
                super.updateItem(power, empty);

                if (empty || power == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    nameLabel.setText(power.getName(locale));
                    descriptionLabel.setText(power.getDescription(locale));
                    powerIcon.setImage(power.getImage());

                    // Update value details
                    if (power.getValue() > 0) {
                        valueLabel.setText("" + power.getValue());
                        valueIcon.setImage(power.getValueImage());
                        valueIcon.setVisible(true);
                    } else {
                        valueLabel.setText("");
                        valueIcon.setVisible(false);
                    }
                    valueLabel.setGraphic(valueIcon);

                    // Update radius details
                    radiusLabel.setText("" + power.getRadius());
                    radiusIcon.setImage(radiusImage);
                    radiusLabel.setGraphic(radiusIcon);

                    // Update effect details
                    if (power.hasEffect()) {
                        durationLabel.setText("" + power.getEffect().getDuration());
                        durationIcon.setImage(durationImage);
                        durationIcon.setVisible(true);

                        effectLabel.setText("" + power.getEffect().getValue());
                        effectIcon.setImage(power.getEffect().getImage());
                        effectIcon.setVisible(true);
                    } else {
                        durationLabel.setText("");
                        durationIcon.setVisible(false);

                        effectLabel.setText("");
                        effectIcon.setVisible(false);
                    }
                    durationLabel.setGraphic(durationIcon);
                    effectLabel.setGraphic(effectIcon);



                    setGraphic(cellContainer);
                }
            }
        });




        specialTab.setContent(powerListView);
        //TODO

        setTabDragPolicy(TabDragPolicy.FIXED);
        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        getTabs().setAll(specialTab);
        setStyle("-fx-border-color: blue; -fx-border-width: 5;");
    }

    private Tab getAttackTab() {
        Tab attackTab = new Tab(this.bundle.getString("actions.attack"));
        StackPane attackPane = new StackPane();
        VBox.setVgrow(attackPane, Priority.ALWAYS);
        HBox.setHgrow(attackPane, Priority.ALWAYS);


        VBox attackContent = new VBox();

        attackContent.setAlignment(Pos.CENTER);

        Label attackLabel = new Label("Attack Label");
        styleNode(attackLabel,attackPane,0.7,0.4);

        attackButton = new Button("Normal attack");
        styleNode(attackButton,attackPane,0.5,0.1);

        specialAttackButton = new Button("Special attack");
        styleNode(specialAttackButton,attackPane,0.5,0.1);

        Label orLabel = new Label("Or Label");
        styleNode(orLabel,attackPane,0.7,0.1);

        healButton = new Button("Heal");
        styleNode(healButton,attackPane,0.5,0.1);


        attackContent.getChildren().addAll(attackLabel,attackButton,specialAttackButton, orLabel, healButton);
        attackPane.getChildren().add(attackContent);
        attackTab.setContent(attackPane);
        return attackTab;
    }

    private void styleNode(Label toBeStyled, StackPane container, double width, double height) {
        toBeStyled.setWrapText(true);
        toBeStyled.setAlignment(Pos.CENTER);
        toBeStyled.prefWidthProperty().bind(container.widthProperty().multiply(width));
        toBeStyled.prefHeightProperty().bind(container.heightProperty().multiply(height));
        toBeStyled.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            Platform.runLater(() -> {
                double fontSize = newWidth.doubleValue() * 0.1;
                toBeStyled.setStyle("-fx-font-size: " + fontSize + "px;");
            });
        });
    }
    private void styleNode(Button toBeStyled, StackPane container, double width, double height) {
        toBeStyled.setAlignment(Pos.CENTER);
        toBeStyled.prefWidthProperty().bind(container.widthProperty().multiply(width));
        toBeStyled.prefHeightProperty().bind(container.heightProperty().multiply(height));
        toBeStyled.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            Platform.runLater(() -> {
                double fontSize = newWidth.doubleValue() * 0.1;
                toBeStyled.setStyle("-fx-font-size: " + fontSize + "px;");
            });
        });

    }

    private Tab getMoveTab() {
        Tab moveTab = new Tab(this.bundle.getString("actions.move"));
        StackPane movePane = new StackPane();
        VBox.setVgrow(movePane, Priority.ALWAYS);
        HBox.setHgrow(movePane, Priority.ALWAYS);

        VBox moveContent = new VBox();
        moveContent.setAlignment(Pos.CENTER);

        Label moveLabel = new Label("Move Label");
        styleNode(moveLabel,movePane,0.7,0.4);

        moveButton = new Button("Stand still");
        styleNode(moveButton,movePane,0.5,0.1);


        moveContent.getChildren().addAll(moveLabel, moveButton);
        movePane.getChildren().add(moveContent);
        moveTab.setContent(movePane);
        return moveTab;
    }

    public void updateSelected(HexTile hexTile) {
        if (hexTile != null) {
            if (hexTile.getTile().isOccupied()){
               getTabs().setAll(moveTab,attackTab,specialTab);
            } else {
                getTabs().setAll(specialTab);
            }
        } else {
            getTabs().setAll(specialTab);
        }
    }
}
