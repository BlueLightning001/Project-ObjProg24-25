package be.ugent.objprog.minionwars;

import be.ugent.objprog.minionwars.controllers.StartScreenController;
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
        String configs = getParameters().getRaw().getFirst();
        System.out.println(configs);
        JDOMReader reader = new JDOMReader(configs);
        if (reader == null) {
            throw new RuntimeException("Reading config file failed");
        } else {
            System.out.println("READER: " + reader);
        }
        StartScreenController controller = new StartScreenController(primaryStage,locale,reader);
        Scene scene = new Scene(controller.getView(), controller.getView().getPrefWidth() , controller.getView().getPrefHeight());
        primaryStage.setTitle("Minion Wars");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/minions/sword.png"))));
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setMinHeight(150);
        primaryStage.setMinWidth(150);
        // Add key event handler for F11
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F11) {

                primaryStage.setFullScreen(!primaryStage.isFullScreen());
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}