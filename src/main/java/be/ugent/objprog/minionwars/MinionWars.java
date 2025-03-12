package be.ugent.objprog.minionwars;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class MinionWars extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Locale locale = Locale.getDefault();

        StartScreenController controller = new StartScreenController(locale);
        //Application.setUserAgentStylesheet(getClass().getResource("/be/ugent/objprog/minionwars/css/AtlantaFX-2.0.1-themes/nord-light.css").toExternalForm());
        Scene scene = new Scene(controller.getView().getContainer(), 500, 500);
        primaryStage.setTitle("Minion Wars");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/minions/sword.png"))));
        primaryStage.setScene(scene);
        // Add key event handler for F11
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F11) {
                // Toggle fullscreen mode
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}