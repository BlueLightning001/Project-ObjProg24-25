package be.ugent.objprog.minionwars;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FontPreviewApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // List of fonts that JavaFX can use
        String[] fontNames = Font.getFamilies().toArray(new String[0]);

        ListView<String> fontListView = new ListView<>();
        fontListView.getItems().addAll(fontNames);

        // Create a VBox for the layout
        VBox vbox = new VBox(fontListView);

        // Add a Text object to preview selected font
        Text previewText = new Text("Sample Text");
        previewText.setFont(new Font(fontNames[0], 20));  // Preview the first font

        fontListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            previewText.setFont(new Font(newValue, 20));  // Update preview on font selection
        });

        vbox.getChildren().add(previewText);

        Scene scene = new Scene(vbox, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Font Preview");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
