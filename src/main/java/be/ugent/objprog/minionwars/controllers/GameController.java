package be.ugent.objprog.minionwars.controllers;

import be.ugent.objprog.minionwars.JDOMReader;
import be.ugent.objprog.minionwars.models.MinionModel;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.PowerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import be.ugent.objprog.minionwars.views.GameView;
import javafx.collections.FXCollections;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.util.Locale;

public class GameController {
    private final Locale locale;
    private final TileModel tileModel;
    private final PowerModel powerModel;
    private final GameView view;
    private final PlayerModel playerModel;
    private final Stage stage;
    private final JDOMReader jdomReader;

    // Controller classes for different responsibilities
    private TileController tileController;
    private ActionTabController actionTabController;
    private GameStateController gameStateController;

    public GameController(Stage stage, PlayerModel playerModel, Locale locale, JDOMReader reader) {
        this.stage = stage;
        this.playerModel = playerModel;
        MinionModel minionModel = new MinionModel(reader);
        this.tileModel = new TileModel(reader, locale);
        this.powerModel = new PowerModel(reader, locale);
        this.jdomReader = reader;

        // Load the players powers
        this.playerModel.getPlayer1().setAvailablePowers(FXCollections.observableArrayList(powerModel.getPowerList()));
        this.playerModel.getPlayer2().setAvailablePowers(FXCollections.observableArrayList(powerModel.getPowerList()));

        this.locale = locale;
        this.view = new GameView(minionModel, playerModel, tileModel, powerModel, locale);

        view.resetGameGroupPosition();

        // Enables despicable mode
        if (playerModel.isDespicable()) {
            minionModel.enableDespicableMode();
        }

        stage.setOnCloseRequest(event -> {
            view.getGameTileGroupPane().shutdown();// Releases resources from other threads
        });

        // Initialize controller classes
        this.tileController = new TileController(tileModel, view, playerModel);
        this.actionTabController = new ActionTabController(tileModel, view, playerModel, powerModel, tileController);
        this.gameStateController = new GameStateController(playerModel, view, stage, locale, tileController, powerModel, reader, tileModel, actionTabController);

        // PART 1
        gameStateController.setUpListenersPart1();
    }

    public Region getView() {
        return this.view.getView();
    }
}