package be.ugent.objprog.minionwars.controllers;

import be.ugent.objprog.minionwars.JDOMReader;
import be.ugent.objprog.minionwars.models.MinionModel;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import be.ugent.objprog.minionwars.views.GameView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Locale;

public class GameController {
    private final Locale locale;
    private final MinionModel minionModel;
    private GameView view;
    private PlayerModel playerModel;
    private TileModel tileModel;
    private Stage stage;

    public GameController(Stage stage,PlayerModel playerModel, Locale locale,JDOMReader reader) {
        this.stage = stage;
        this.playerModel = playerModel;
        this.minionModel = new MinionModel(reader);
        this.tileModel = new TileModel(reader);
        this.locale = locale;
        this.view = new GameView(minionModel,playerModel,tileModel,locale);
        view.resetGameGroupPosition();

        view.getEndTurnButton().setOnAction(event -> {
            playerModel.nextPlayer();
        });
        stage.setOnCloseRequest(event -> {
            view.getGameTileGroupPane().shutdown();// Releases resources from other threads
        });
    }
    public void endGame(){
        //TODO launch new game
        view.getGameTileGroupPane().shutdown();
        stage.close();

    }
    public Region getView() {
        return this.view.getView();
    }
}
