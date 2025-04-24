package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.JDOMReader;
import be.ugent.objprog.minionwars.controllers.GameController;
import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.PowerModel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class VictoryPane extends StackPane {
    private static final Image BACKGROUND = new Image("/be/ugent/objprog/minionwars/images/splash-end.jpg");
    private final PlayerModel playerModel;
    private final PowerModel powerModel;
    private final JDOMReader reader;


    public VictoryPane(Player winner, PlayerModel playerModel, PowerModel powerModel, JDOMReader reader, Stage stage, Locale locale) {
        this.playerModel = playerModel;
        this.powerModel = powerModel;
        this.reader = reader;
        ResourceBundle bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);

        setBackground(new Background(new BackgroundImage(
                BACKGROUND,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        100, 100, true, true, true, true
                )
        )));
        setAlignment(Pos.CENTER);


        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.prefWidthProperty().bind(widthProperty().multiply(0.5)); // 50% of window width
        content.prefHeightProperty().bind(heightProperty().multiply(0.5)); // 50% of window height


        Label winnerLabel = new Label(MessageFormat.format(bundle.getString("victoryScreen.winnerLabel"), winner.getName(), playerModel.getTurnCounter()));
        winnerLabel.setStyle("-fx-text-fill: white;");
        winnerLabel.setWrapText(true);

        DropShadow shadow = new DropShadow();
        shadow.setBlurType(BlurType.GAUSSIAN);
        shadow.setRadius(10);
        shadow.setColor(Color.BLACK);
        winnerLabel.setEffect(shadow);

        Button exitGameButton = new Button(bundle.getString("victoryScreen.exitGameButton"));
        Button restartButton = new Button(bundle.getString("victoryScreen.restartButton"));


        String buttonStyle = "-fx-background-color: rgba(0, 0, 0, 0.7); " +
                "-fx-text-fill: white; " +
                "-fx-opacity: 0.8; " +
                "-fx-border-color: white; " +
                "-fx-border-width: 2px;";
        exitGameButton.setStyle(buttonStyle);
        restartButton.setStyle(buttonStyle);

        content.getChildren().addAll(winnerLabel, restartButton, exitGameButton);
        getChildren().add(content);


        widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double fontSize = newWidth.doubleValue() * 0.05; // Scale font with width
            winnerLabel.setFont(Font.font("Monotype Corsiva", FontWeight.BOLD, fontSize));
            restartButton.setStyle(buttonStyle + "-fx-font-size: " + (fontSize * 0.6) + "px;");
            exitGameButton.setStyle(buttonStyle + "-fx-font-size: " + (fontSize * 0.6) + "px;");
        });


        exitGameButton.setOnAction(event -> stage.close());
        restartButton.setOnAction(event -> restartGame(stage, locale));

    }

    private void restartGame(Stage oldStage, Locale locale) {
        resetModels(); // Reset game state

        // Store previous stage settings
        double width = oldStage.getWidth();
        double height = oldStage.getHeight();
        boolean isFullscreen = oldStage.isFullScreen();

        // Close the old stage
        oldStage.close();

        // Create a new stage
        Stage newStage = new Stage();
        newStage.setTitle(oldStage.getTitle());
        newStage.getIcons().addAll(oldStage.getIcons());
        newStage.setWidth(width);
        newStage.setHeight(height);
        newStage.setFullScreen(isFullscreen);

        try {
            GameController gameController = new GameController(newStage, playerModel, locale, new JDOMReader(reader.getFilename()),playerModel.isDespicable());
            Scene scene = new Scene(gameController.getView(), width, height);
            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.F11) {
                    newStage.setFullScreen(!newStage.isFullScreen());
                }
            });
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            newStage.close();
            throw new RuntimeException(e);
        }
    }


    private void resetModels() {
        playerModel.reset(powerModel);
    }
}
