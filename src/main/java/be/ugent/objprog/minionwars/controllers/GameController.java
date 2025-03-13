package be.ugent.objprog.minionwars.controllers;

import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.views.GameView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.util.Locale;

public class GameController {
    private final Locale locale;
    private GameView view;
    private PlayerModel playerModel;

    public GameController(PlayerModel playerModel,Locale locale) {
        this.playerModel = playerModel;
        this.locale = locale;
        this.view = new GameView(playerModel,locale);


    }
    public Region getView() {
        return this.view.getView();
    }
}
