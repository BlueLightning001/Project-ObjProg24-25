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
    private SelectedMinionDisplay selectedMinionDisplay;
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
        selectedMinionDisplay = new SelectedMinionDisplay(tileModel, locale);


        tileGroupPane.selectedHexTileProperty().addListener((obs, oldTile, newTile) -> {
            setSelected(newTile);
        });

        //DEBUG //TODO
        setStyle("-fx-border-color: green; -fx-border-width: 2");
        currentPlayerHBox.setStyle("-fx-border-color: red; -fx-border-width: 2");



        getChildren().addAll(currentPlayerHBox,separator,selectedMinionDisplay);

        // Set vertical grow priority for contained elements.
//        VBox.setVgrow(currentPlayerHBox, Priority.ALWAYS);
//        VBox.setVgrow(selectedMinionDisplay, Priority.ALWAYS);
//        VBox.setVgrow(menuButtonBar, Priority.ALWAYS);

        // Height ratios
//        menuTable.prefHeightProperty().bind(this.heightProperty().multiply(0.8));
        selectedMinionDisplay.prefHeightProperty().bind(this.heightProperty().multiply(0.1));
        currentPlayerHBox.prefHeightProperty().bind(this.heightProperty().multiply(0.1));

    }
    public void setSelected(HexTile hexTile){
            selectedMinionDisplay.updateSelected(hexTile);
        }

}



