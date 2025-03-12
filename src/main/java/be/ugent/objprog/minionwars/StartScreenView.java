package be.ugent.objprog.minionwars;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;

import java.util.Locale;
import java.util.ResourceBundle;

public class StartScreenView {
    private final ResourceBundle bundle;
    private final PlayerModel model;
    private final StackPane container;
    private final VBox centerContainer;
    private final HBox player1Box;
    private final TextField player1TextField;
    private final Label player1Label;
    private final HBox player2Box;
    private final TextField player2TextField;
    private final Label player2Label;
    private final HBox moneyBox;
    private final TextField moneyTextField;
    private final Label moneyLabel;
    private final Button testButton = new Button("Test");

    public StartScreenView(PlayerModel model, Locale locale) {

        this.bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);
        this.model = model;

        ObservableList<Player> players = model.getPlayers();

        this.container = new StackPane();
        this.centerContainer = new VBox();
        this.player1Box = new HBox();
        this.player1TextField = new TextField();
        this.player1Label = new Label(bundle.getString("startScreen.player1Label"));
        this.player1TextField.setPromptText(bundle.getString("startScreen.player1Prompt"));
        this.player1Box.getChildren().addAll( this.player1Label ,this.player1TextField);
        this.player2Box = new HBox();
        this.player2TextField = new TextField();
        this.player2Label = new Label(bundle.getString("startScreen.player2Label"));
        this.player2TextField.setPromptText(bundle.getString("startScreen.player2Prompt"));
        this.player2Box.getChildren().addAll(this.player2Label, this.player2TextField);
        this.moneyBox = new HBox();
        this.moneyTextField = new TextField();
        this.moneyLabel = new Label(bundle.getString("startScreen.moneyLabel"));
        this.moneyTextField.setPromptText(bundle.getString("startScreen.moneyPrompt"));
        this.moneyBox.getChildren().addAll(this.moneyLabel, this.moneyTextField);

        this.centerContainer.getChildren().addAll(this.player1Box, this.player2Box, this.moneyBox);

        Group scalingGroup = new Group(centerContainer);
        this.container.getChildren().add(scalingGroup);

        this.container.setAlignment(Pos.CENTER);

        this.centerContainer.setAlignment(Pos.CENTER);
        this.centerContainer.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(this.centerContainer, Priority.ALWAYS);

        this.player1Box.setAlignment(Pos.CENTER);
        this.player2Box.setAlignment(Pos.CENTER);
        this.moneyBox.setAlignment(Pos.CENTER);

        this.player1Box.setMaxWidth(Double.MAX_VALUE);
        this.player2Box.setMaxWidth(Double.MAX_VALUE);
        this.moneyBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(this.player1Box, Priority.ALWAYS);
        HBox.setHgrow(this.player2Box, Priority.ALWAYS);
        HBox.setHgrow(this.moneyBox, Priority.ALWAYS);

        // Background
        Image backgroundImage = new Image("be/ugent/objprog/minionwars/images/splash-end.jpg");

        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        100, 100,  // Scale to 100% of parent width and height
                        true, true, // Scale width and height proportionally
                        true, true // Ensure the background covers the entire container
                )
        );
        container.setBackground(new Background(bgImage));

        // Resizing
        Scale scale = new Scale(1, 1);
        centerContainer.getTransforms().add(scale);

        // Bind scaling to window size (prevent downward shift)
        scale.xProperty().bind(Bindings.createDoubleBinding(
                () -> Math.min(container.getWidth() / 500, container.getHeight() / 500),
                container.widthProperty(), container.heightProperty()
        ));
        scale.yProperty().bind(scale.xProperty()); // Keep aspect ratio

        this.container.setAlignment(Pos.CENTER);
        this.centerContainer.setAlignment(Pos.CENTER);

    }
    public StackPane getContainer() {
        return this.container;
    }
}
