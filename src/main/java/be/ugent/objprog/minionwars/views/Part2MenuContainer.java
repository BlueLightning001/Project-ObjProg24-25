package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.models.MinionModel;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.PowerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Separator;
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
        actionsPane = new ActionsPane(playerModel, powerModel, locale);

        //// Buttons
        ButtonBar menuButtonBar = new ButtonBar();
        restButton = new Button("Rest");
        endTurnButton = new Button("End Turn");
        centerBoardButton = new Button("Center Board");
        menuButtonBar.getButtons().addAll(restButton, endTurnButton, centerBoardButton);
        menuButtonBar.setStyle("-fx-border-color: orange; -fx-border-width: 5");
        ButtonBar.setButtonData(endTurnButton, ButtonBar.ButtonData.LEFT);
        ButtonBar.setButtonData(centerBoardButton, ButtonBar.ButtonData.RIGHT);
        ButtonBar.setButtonData(restButton, ButtonBar.ButtonData.BIG_GAP);

        // Listeners
        tileGroupPane.selectedHexTileProperty().addListener((obs, oldTile, newTile) -> {
            setSelected(newTile);
        });

        //DEBUG //TODO
        setStyle("-fx-border-color: green; -fx-border-width: 2");
        this.currentPlayerDisplay.setStyle("-fx-border-color: red; -fx-border-width: 2");


        getChildren().addAll(this.currentPlayerDisplay, separator, selectedMinionDisplay, actionsPane, menuButtonBar);


        // Height ratios
        menuButtonBar.prefHeightProperty().bind(this.heightProperty().multiply(0.1));
        actionsPane.prefHeightProperty().bind(this.heightProperty().multiply(0.7));
        selectedMinionDisplay.prefHeightProperty().bind(this.heightProperty().multiply(0.1));
        this.currentPlayerDisplay.prefHeightProperty().bind(this.heightProperty().multiply(0.1));


    }

    public void setSelected(HexTile hexTile) {
        selectedMinionDisplay.updateSelected(hexTile);
        actionsPane.updateSelected(hexTile);
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



