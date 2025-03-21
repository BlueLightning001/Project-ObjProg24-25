package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.models.MinionModel;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

import java.util.Locale;
import java.util.ResourceBundle;

public class Part2MenuContainer extends VBox {
    private final Locale locale;
    private MinionModel minionModel;
    private PlayerModel playerModel;
    private TileGroupPane tileGroupPane;
    private TileModel tileModel;
    private CurrentPlayerDisplay currentPlayerDisplay;
    private ActionsPane actionsPane;

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


        // Current player info
        currentPlayerDisplay = new CurrentPlayerDisplay(playerModel);


        // Line below current player box
        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setStyle("-fx-background-color: black;");

        //// Minion Display Box
        selectedMinionDisplay = new SelectedMinionDisplay(tileModel, locale);



        //// Actions Tabs
        actionsPane =  new ActionsPane(locale);


        // Listeners
        tileGroupPane.selectedHexTileProperty().addListener((obs, oldTile, newTile) -> {
            setSelected(newTile);
        });

        //DEBUG //TODO
        setStyle("-fx-border-color: green; -fx-border-width: 2");
        this.currentPlayerDisplay.setStyle("-fx-border-color: red; -fx-border-width: 2");



        getChildren().addAll(this.currentPlayerDisplay,separator,selectedMinionDisplay,actionsPane);

        // Set vertical grow priority for contained elements.
//        VBox.setVgrow(currentPlayerDisplay, Priority.ALWAYS);
//        VBox.setVgrow(selectedMinionDisplay, Priority.ALWAYS);
//        VBox.setVgrow(menuButtonBar, Priority.ALWAYS);

        // Height ratios
        actionsPane.prefHeightProperty().bind(this.heightProperty().multiply(0.7));
        selectedMinionDisplay.prefHeightProperty().bind(this.heightProperty().multiply(0.1));
        this.currentPlayerDisplay.prefHeightProperty().bind(this.heightProperty().multiply(0.1));

    }
    public void setSelected(HexTile hexTile){
            selectedMinionDisplay.updateSelected(hexTile);
            actionsPane.updateSelected(hexTile);
        }

}



