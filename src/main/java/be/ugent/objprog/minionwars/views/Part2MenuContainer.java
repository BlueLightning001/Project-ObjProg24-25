package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.effects.MinionEffect;
import be.ugent.objprog.minionwars.effects.PoisonEffect;
import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.MinionModel;
import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Scale;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class Part2MenuContainer extends VBox {
    private final Locale locale;
    private MinionModel minionModel;
    private PlayerModel playerModel;
    private TileGroupPane tileGroupPane;
    private TileModel tileModel;
    private HBox currentPlayerHBox;
    private Label currentPlayerLabel;
    private Label currentPlayerMinionsUsedLabel;
    private ResourceBundle bundle;
    private GridPane minionDisplay = new GridPane();
    private Circle minionsIcon = new Circle();
    private Label minionsLabel = new Label();
    private Label tileNameLabel = new Label();
    private GridPane statsDisplay = new GridPane();
    private Label attackStatLabel = new Label();
    private Label defenseStatLabel = new Label();
    private Label ailmentsStatLabel = new Label();

    public Part2MenuContainer(PlayerModel playerModel, MinionModel minionModel,TileModel tileModel, TileGroupPane tileGroupPane, Locale locale) {
        this.playerModel = playerModel;
        this.minionModel = minionModel;
        this.tileGroupPane = tileGroupPane;
        this.tileModel = tileModel;
        this.locale = locale;
        // Load the resource bundle.
        this.bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);


        // Create and configure the player information elements.
        currentPlayerLabel = new Label("Current Player");
        currentPlayerMinionsUsedLabel = new Label("currentPlayerCoins");
        currentPlayerHBox = new HBox();
        currentPlayerHBox.getChildren().addAll(currentPlayerLabel, currentPlayerMinionsUsedLabel);

        double fontScale = 0.1;

        // Set up MinionsIcon
        ImageView MinionsIcon = new ImageView(new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/minions-0073FF.png")
        )));
        currentPlayerMinionsUsedLabel.setGraphic(MinionsIcon);
        MinionsIcon.setFitHeight(10);
        MinionsIcon.setFitWidth(10);
        MinionsIcon.fitHeightProperty().bind(currentPlayerHBox.heightProperty().multiply(0.3));
        MinionsIcon.fitWidthProperty().bind(MinionsIcon.fitHeightProperty());

        // Bind the text properties for player name and money.
        currentPlayerLabel.textProperty().bind(playerModel.currentPlayerProperty().get().nameProperty());
        currentPlayerMinionsUsedLabel.textProperty().bind(
                Bindings.createStringBinding(() -> {
                    Player currentPlayer = playerModel.getCurrentPlayer();
                    long activeMinions = currentPlayer.getMinions().stream()
                            .filter(Minion::hasActions)
                            .count();
                    int totalMinions = currentPlayer.getMinions().size();
                    return activeMinions + "/" + totalMinions;
                }, playerModel.getCurrentPlayer().getMinions())
        );
        playerModel.currentPlayerProperty().addListener((obs, oldPlayer, newPlayer) -> {
            if (newPlayer != null) {
                currentPlayerLabel.textProperty().unbind();
                currentPlayerMinionsUsedLabel.textProperty().unbind();

                currentPlayerLabel.textProperty().bind(newPlayer.nameProperty());
                currentPlayerMinionsUsedLabel.textProperty().bind(
                        Bindings.createStringBinding(() -> {
                            long activeMinions = newPlayer.getMinions().stream()
                                    .filter(Minion::hasActions)
                                    .count();
                            int totalMinions = newPlayer.getMinions().size();
                            return activeMinions + "/" + totalMinions;
                        }, newPlayer.getMinions())
                );

            }
        });

        // Set alignments.
        currentPlayerLabel.setAlignment(Pos.CENTER);
        currentPlayerMinionsUsedLabel.setAlignment(Pos.CENTER_LEFT);

        // Let the labels take up available space.
        HBox.setHgrow(currentPlayerLabel, Priority.ALWAYS);
        HBox.setHgrow(currentPlayerMinionsUsedLabel, Priority.ALWAYS);
        currentPlayerLabel.prefWidthProperty().bind(currentPlayerHBox.widthProperty().multiply(0.7));
        currentPlayerMinionsUsedLabel.prefWidthProperty().bind(currentPlayerHBox.widthProperty().multiply(0.3));
        currentPlayerLabel.prefHeightProperty().bind(currentPlayerHBox.heightProperty());
        currentPlayerMinionsUsedLabel.prefHeightProperty().bind(currentPlayerHBox.heightProperty());
        currentPlayerHBox.setMaxWidth(Double.MAX_VALUE);
        currentPlayerHBox.setAlignment(Pos.CENTER_RIGHT);
        currentPlayerHBox.setSpacing(20);

        // Adjust font sizes based on the width of the HBox.
        currentPlayerHBox.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            Platform.runLater(() -> {
                double newFontSize = newWidth.doubleValue() * fontScale;
                currentPlayerLabel.setStyle("-fx-font-size: " + newFontSize + "px;");
                currentPlayerMinionsUsedLabel.setStyle("-fx-font-size: " + (newFontSize * 0.8) + "px;");
            });
        });

        // Line below current player box
        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setStyle("-fx-background-color: black;");

        //// Minion Display Box
        minionDisplay.prefWidthProperty().bind(widthProperty());

        minionsIcon.setRadius(30);
        minionsIcon.setFill(Color.TRANSPARENT);

        // Add everything together
        minionDisplay.add(minionsIcon, 0, 0);
        GridPane.setRowSpan(minionsIcon, 2);
        minionsIcon.setCenterY(minionDisplay.getHeight() / 2);
        minionsIcon.setCenterX(minionDisplay.getWidth() * 0.15 / 2);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(20);
        minionDisplay.add(statsDisplay, 2, 0);
        GridPane.setRowSpan(statsDisplay, 2);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(25);


        // Add labels
        statsDisplay.add(attackStatLabel, 0, 0);
        attackStatLabel.setAlignment(Pos.CENTER);
        attackStatLabel.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(attackStatLabel, HPos.CENTER);
        GridPane.setValignment(attackStatLabel, VPos.CENTER);

        statsDisplay.add(defenseStatLabel, 1, 0);
        defenseStatLabel.setAlignment(Pos.CENTER);
        defenseStatLabel.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(defenseStatLabel, HPos.CENTER);
        GridPane.setValignment(defenseStatLabel, VPos.CENTER);

        statsDisplay.add(ailmentsStatLabel, 0, 1);
        ailmentsStatLabel.setAlignment(Pos.CENTER);
        ailmentsStatLabel.setTextAlignment(TextAlignment.CENTER);
        GridPane.setColumnSpan(ailmentsStatLabel, 2);
        GridPane.setHalignment(ailmentsStatLabel, HPos.CENTER);
        GridPane.setValignment(ailmentsStatLabel, VPos.CENTER);

        minionDisplay.add(minionsLabel, 1, 0);
        minionsLabel.setAlignment(Pos.CENTER);
        minionsLabel.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(minionsLabel, HPos.CENTER);
        GridPane.setValignment(minionsLabel, VPos.CENTER);

        minionDisplay.add(tileNameLabel, 1, 1);
        tileNameLabel.setAlignment(Pos.CENTER);
        tileNameLabel.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(tileNameLabel, HPos.CENTER);
        GridPane.setValignment(tileNameLabel, VPos.CENTER);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(55);

        attackStatLabel.setStyle("-fx-font-weight: bolder; -fx-font-size: 18");
        defenseStatLabel.setStyle("-fx-font-weight: bolder; -fx-font-size: 18");
        ailmentsStatLabel.setStyle("-fx-font-weight: bolder; -fx-font-size: 18");
        minionsLabel.setStyle("-fx-font-weight: bolder; -fx-font-size: 18");
        tileNameLabel.setStyle("-fx-font-weight: bolder; -fx-font-size: 18");


        statsDisplay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setHgrow(statsDisplay, Priority.ALWAYS);
        GridPane.setVgrow(statsDisplay, Priority.ALWAYS);
        ColumnConstraints statCol1 = new ColumnConstraints();
        statCol1.setPercentWidth(50);
        ColumnConstraints statCol2 = new ColumnConstraints();
        statCol2.setPercentWidth(50);
        statsDisplay.getColumnConstraints().addAll(statCol1, statCol2);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(50); // 50% of available height
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(50);
        minionDisplay.getRowConstraints().setAll(row1, row2);


        RowConstraints statsRow1 = new RowConstraints();
        statsRow1.setPercentHeight(50);
        RowConstraints statsRow2 = new RowConstraints();
        statsRow2.setPercentHeight(50);
        statsDisplay.getRowConstraints().setAll(statsRow1, statsRow2);

        minionDisplay.getColumnConstraints().addAll(col1, col2, col3);

        System.out.println("MINION CHILD: " + minionDisplay.getChildren());
        System.out.println("STATS CHILD: " + statsDisplay.getChildren());
        tileGroupPane.selectedHexTileProperty().addListener((obs, oldTile, newTile) -> {
            setSelected(newTile);
        });

        //DEBUG //TODO
        setStyle("-fx-border-color: green; -fx-border-width: 2");
        currentPlayerHBox.setStyle("-fx-border-color: red; -fx-border-width: 2");


        getNodeByRowColumnIndex(1, 0, statsDisplay).setStyle("-fx-border-width: 2 0 0 0; -fx-border-color: #050505;");
        getChildren().addAll(currentPlayerHBox,separator,minionDisplay);

        // Set vertical grow priority for contained elements.
//        VBox.setVgrow(currentPlayerHBox, Priority.ALWAYS);
//        VBox.setVgrow(menuTable, Priority.ALWAYS);
//        VBox.setVgrow(menuButtonBar, Priority.ALWAYS);

        // Height ratios
//        menuTable.prefHeightProperty().bind(this.heightProperty().multiply(0.8));
        minionDisplay.prefHeightProperty().bind(this.heightProperty().multiply(0.1));
        currentPlayerHBox.prefHeightProperty().bind(this.heightProperty().multiply(0.1));

    }
    public void setSelected(HexTile hexTile){
        if (hexTile != null) {
            Minion minion = hexTile.getTile().getOccupant();
            if (minion == null) {
                clearLabels();
                minionDisplay.setVisible(false);
            } else {
                minion.addStatusAilment(new PoisonEffect(3));
                minionDisplay.setVisible(true);
                // Set the minion icon for the minionsIcon using an ImagePattern
                ImagePattern pattern = new ImagePattern(minion.getMinionIcon());
                minionsIcon.setFill(pattern);
                minionsLabel.setText(minion.getName());

                // Attack stat setup
                attackStatLabel.setText("" + minion.getAttack());
                ImageView attackImageView = new ImageView(new Image(getClass().getResourceAsStream(
                        "/be/ugent/objprog/minionwars/images/icons/attack-D60000.png")));
                attackImageView.setPreserveRatio(true);
                // Bind attack image's height to a fraction of the attack label's height
                attackImageView.fitHeightProperty().bind(attackStatLabel.heightProperty().multiply(0.8));
                attackStatLabel.setGraphic(attackImageView);

                // Defense stat setup
                defenseStatLabel.setText("" + minion.getDefence());
                ImageView defenseImageView = new ImageView(new Image(getClass().getResourceAsStream(
                        "/be/ugent/objprog/minionwars/images/icons/health-D60000.png")));
                defenseImageView.setPreserveRatio(true);
                defenseImageView.fitHeightProperty().bind(defenseStatLabel.heightProperty().multiply(0.8));
                defenseStatLabel.setGraphic(defenseImageView);

                // Effects setup
                if (minion.getStatusAilments().isEmpty()) {
                    ailmentsStatLabel.setText("");
                    ailmentsStatLabel.setGraphic(null);
                } else {
                    // Create an HBox to hold all effect icons
                    HBox effectsBox = new HBox(5);
                    effectsBox.setAlignment(Pos.CENTER);
                    for (MinionEffect effect : minion.getStatusAilments()) {
                        ImageView effectImageView = new ImageView(effect.getImage());
                        effectImageView.setPreserveRatio(true);
                        effectImageView.fitHeightProperty().bind(ailmentsStatLabel.heightProperty().multiply(0.8));
                        effectsBox.getChildren().add(effectImageView);
                    }
                    ailmentsStatLabel.setText("");
                    ailmentsStatLabel.setGraphic(effectsBox);
                }
            }
            tileNameLabel.setText(MessageFormat.format(bundle.getString("menuPart2.tileNameText"),tileModel.getTileName(hexTile.getTile().getClass())));
        } else {
            clearLabels();
            minionDisplay.setVisible(false);
        }

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
    }

    public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) != null && javafx.scene.layout.GridPane.getColumnIndex(node) != null
                    && GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
    }

}
