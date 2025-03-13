package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import be.ugent.objprog.minionwars.tiles.TilePane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Locale;

public class GameView {
    private StackPane container;
    private Locale locale;
    private PlayerModel playerModel;
    private HBox root;
    private VBox menuContainer;
    private Label menuTitleLabel;
    private TableView<Minion> menuTable;
    private Button endTurnButton;
    private TilePane gameTilePane;


    public GameView(PlayerModel playerModel, TileModel tileModel, Locale locale) {
        this.playerModel = playerModel;
        this.locale = locale;
        container = new StackPane();
        root = new HBox();
        menuContainer = new VBox();
        menuTitleLabel = new Label();
        menuTable = new TableView<>();
        endTurnButton = new Button();
        gameTilePane = new TilePane(tileModel); //TODO
        menuContainer.getChildren().addAll(menuTitleLabel,menuTable,endTurnButton);
        this.root.getChildren().addAll(menuContainer, gameTilePane);
        gameTilePane.setPrefSize(500,500);
        gameTilePane.setStyle("-fx-background-color: red;");
        this.container.getChildren().add(root);



    }
    public Region getView() {
        return container;
    }
}
