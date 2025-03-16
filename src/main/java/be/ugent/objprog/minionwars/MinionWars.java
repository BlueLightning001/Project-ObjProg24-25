package be.ugent.objprog.minionwars;

import be.ugent.objprog.minionwars.controllers.StartScreenController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;


import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;

public class MinionWars extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Locale.setDefault(new Locale("nl", "BE"));

        Locale locale = Locale.getDefault();

        List<String> configs = getParameters().getRaw();
        if (configs.isEmpty() || configs.getFirst().isBlank()) {
            System.err.println("Required game config is missing.");
            Platform.exit(); // Close the application
            return;
        }
        System.out.println(configs);

        JDOMReader reader;
        try {
            reader = new JDOMReader(configs.getFirst());
        } catch (IOException e) {
            System.err.println("Error reading config file: " + e.getMessage());
            Platform.exit(); // Close the application
            return;
        }

        System.out.println("READER: " + reader);
        StartScreenController controller = new StartScreenController(primaryStage, locale, reader);
        Scene scene = new Scene(controller.getView(), controller.getView().getPrefWidth(), controller.getView().getPrefHeight());
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
