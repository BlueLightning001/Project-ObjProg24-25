package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.models.MinionModel;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.PowerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import javafx.beans.binding.Bindings;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Locale;
import java.util.ResourceBundle;

public class Part2MenuContainer extends VBox {
    private final Locale locale;
    private final MinionModel minionModel;
    private final PlayerModel playerModel;
    private final TileGroupPane tileGroupPane;
    private final TileModel tileModel;
    private final PowerModel powerModel;
    private final Button restButton;
    private final Button endTurnButton;
    private final Button centerBoardButton;
    private CurrentPlayerDisplay currentPlayerDisplay;
    private ActionsPane actionsPane;
    private ResourceBundle bundle;
    private SelectedMinionDisplay selectedMinionDisplay;

    public Part2MenuContainer(PlayerModel playerModel, MinionModel minionModel, TileModel tileModel, PowerModel powerModel, TileGroupPane tileGroupPane, Locale locale) {
        this.playerModel = playerModel;
        this.minionModel = minionModel;
        this.tileGroupPane = tileGroupPane;
        this.tileModel = tileModel;
        this.powerModel = powerModel;
        this.locale = locale;

        // Load the resource bundle.
        this.bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);


        // Current player info
        currentPlayerDisplay = new CurrentPlayerDisplay(playerModel);


        // Line below current player box
        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setStyle("-fx-background-color: black;");

        //// Minion Display Box
        selectedMinionDisplay = new SelectedMinionDisplay(tileModel, locale);


        //// Actions Tabs
        actionsPane = new ActionsPane(tileModel,playerModel, powerModel, locale);
        actionsPane.setVisible(false);

        //// Buttons
        HBox menuButtons = new HBox();
        menuButtons.setSpacing(10);
        menuButtons.setAlignment(Pos.CENTER);

        restButton = new Button(bundle.getString("part2Menu.restButton"));
        restButton.setVisible(false);
        endTurnButton = new Button(bundle.getString("gameScreen.endTurnButton"));
        endTurnButton.setDisable(true);
        centerBoardButton = new Button(bundle.getString("gameScreen.centerBoard"));

        menuButtons.getChildren().addAll( endTurnButton,restButton, centerBoardButton);

        endTurnButton.setWrapText(true);
        centerBoardButton.setWrapText(true);
        restButton.setWrapText(true);

        // Bind button sizes to the HBox
        restButton.prefWidthProperty().bind(menuButtons.widthProperty().divide(3));
        endTurnButton.prefWidthProperty().bind(menuButtons.widthProperty().divide(3));
        centerBoardButton.prefWidthProperty().bind(menuButtons.widthProperty().divide(3));

        restButton.prefHeightProperty().bind(menuButtons.heightProperty().multiply(0.7));
        endTurnButton.prefHeightProperty().bind(menuButtons.heightProperty().multiply(0.7));
        centerBoardButton.prefHeightProperty().bind(menuButtons.heightProperty().multiply(0.7));

        // Scale font size based on button height
        double fontScaleFactor = 0.25;
        restButton.styleProperty().bind(Bindings.concat("-fx-font-size: ", restButton.heightProperty().multiply(fontScaleFactor).asString(), ";"));
        endTurnButton.styleProperty().bind(Bindings.concat("-fx-font-size: ", endTurnButton.heightProperty().multiply(fontScaleFactor).asString(), ";"));
        centerBoardButton.styleProperty().bind(Bindings.concat("-fx-font-size: ", centerBoardButton.heightProperty().multiply(fontScaleFactor).asString(), ";"));



        // Listeners
        tileModel.selectedTileProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setSelected(tileGroupPane.getHexTileGrid()[newValue.getXCoord()][newValue.getYCoord()]);
            } else {
                setSelected(null);
            }
        });


        getChildren().addAll(this.currentPlayerDisplay, separator, selectedMinionDisplay, actionsPane, menuButtons);


        // Height ratios
        menuButtons.prefHeightProperty().bind(this.heightProperty().multiply(0.1));
        actionsPane.prefHeightProperty().bind(this.heightProperty().multiply(0.7));
        selectedMinionDisplay.prefHeightProperty().bind(this.heightProperty().multiply(0.1));
        this.currentPlayerDisplay.prefHeightProperty().bind(this.heightProperty().multiply(0.1));


    }

    public void setSelected(HexTile hexTile) {

        selectedMinionDisplay.updateSelected(hexTile);
        actionsPane.updateSelected(hexTile);

        boolean hasMinion = hexTile != null && hexTile.getTile().isOccupied();
        restButton.setVisible(hasMinion);
        actionsPane.setVisible(hasMinion);
    }

    public ActionsPane getActionsPane() {
        return actionsPane;
    }

    public Button getCenterBoardButton() {
        return centerBoardButton;
    }

    public Button getEndTurnButton() {
        return endTurnButton;
    }

    public Button getRestButton() {
        return restButton;
    }

    public SelectedMinionDisplay getSelectedMinionDisplay() {
        return selectedMinionDisplay;
    }

}



