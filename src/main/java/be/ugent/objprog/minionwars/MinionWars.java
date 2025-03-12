package be.ugent.objprog.minionwars;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

public class MinionWars extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Locale locale = Locale.getDefault();

        StartScreenController controller = new StartScreenController(locale);

        Scene scene = new Scene(controller.getView().getContainer(), 500, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}