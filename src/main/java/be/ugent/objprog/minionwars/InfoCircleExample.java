package be.ugent.objprog.minionwars;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class InfoCircleExample extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create a small circle for the info icon
        Circle circle = new Circle(10, Color.LIGHTBLUE);
        circle.setStroke(Color.DARKBLUE);

        // Add an "i" text inside the circle
        Text infoText = new Text("i");
        infoText.setFont(Font.font(12));
        infoText.setFill(Color.WHITE);
        infoText.setMouseTransparent(true);

        // Stack them together
        StackPane infoIcon = new StackPane(circle, infoText);

        // Create a tooltip
        Tooltip tooltip = new Tooltip("This is an informational tooltip.");
        Tooltip.install(circle, tooltip);
        tooltip.setShowDelay(new Duration(100));
        tooltip.setHideOnEscape(true);
        tooltip.setAutoHide(true);
        tooltip.setHideDelay(new Duration(1000));

        Scene scene = new Scene(infoIcon, 200, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Info Circle Tooltip Example");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
