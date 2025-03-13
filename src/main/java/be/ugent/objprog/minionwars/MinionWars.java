package be.ugent.objprog.minionwars;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class MinionWars extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Locale locale = Locale.getDefault();

        StartScreenController controller = new StartScreenController(locale);
        Scene scene = new Scene(controller.getView(), controller.getView().getPrefWidth() , controller.getView().getPrefHeight());
        primaryStage.setTitle("Minion Wars");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/minions/sword.png"))));
        primaryStage.setScene(scene);
        // Add key event handler for F11
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F11) {

                primaryStage.setFullScreen(!primaryStage.isFullScreen());
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}