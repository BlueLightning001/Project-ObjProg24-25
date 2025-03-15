package be.ugent.objprog.minionwars.controllers;

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
    private GameView view;
    private PlayerModel playerModel;
    private TileModel tileModel;
    private Stage stage;

    public GameController(Stage stage,PlayerModel playerModel, Locale locale) {
        this.stage = stage;
        this.playerModel = playerModel;
        this.tileModel = new TileModel();
        this.locale = locale;
        this.view = new GameView(playerModel,tileModel,locale);
        view.resetGameGroupPosition();

        view.getEndTurnButton().setOnAction(event -> {
            endGame();
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
