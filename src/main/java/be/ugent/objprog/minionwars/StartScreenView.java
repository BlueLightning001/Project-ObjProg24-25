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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;

import java.util.Locale;
import java.util.ResourceBundle;

public class StartScreenView {
    private final ResourceBundle bundle;
    private final PlayerModel model;
    private final StackPane container;
    private final VBox centerContainer;
    private final TextField player1TextField;
    private final Label player1Label;
    private final TextField player2TextField;
    private final Label player2Label;
    private final TextField moneyTextField;
    private final Label moneyLabel;
    private final GridPane grid;
    public StartScreenView(PlayerModel model, Locale locale) {

        this.bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);
        this.model = model;

        ObservableList<Player> players = model.getPlayers();

        this.container = new StackPane();
        this.centerContainer = new VBox();
        this.player1TextField = new TextField();
        this.player1Label = new Label(bundle.getString("startScreen.player1Label"));
        this.player1TextField.setPromptText(bundle.getString("startScreen.player1Prompt"));
        this.player2TextField = new TextField();
        this.player2Label = new Label(bundle.getString("startScreen.player2Label"));
        this.player2TextField.setPromptText(bundle.getString("startScreen.player2Prompt"));
        this.moneyTextField = new TextField();
        this.moneyLabel = new Label(bundle.getString("startScreen.moneyLabel"));
        this.moneyTextField.setPromptText(bundle.getString("startScreen.moneyPrompt"));

        this.grid = new GridPane();
        grid.setHgap(10); // Space between Label & TextField
        grid.setVgap(10); // Space between rows
        grid.setAlignment(Pos.CENTER); // Center it in the parent

        // Add to grid (column, row)
        grid.add(player1Label, 0, 0);
        grid.add(player1TextField, 1, 0);
        grid.add(player2Label, 0, 1);
        grid.add(player2TextField, 1, 1);
        grid.add(moneyLabel, 0, 2);
        grid.add(moneyTextField, 1, 2);

        this.centerContainer.getChildren().add(grid); //TODO startbutton



        Group scalingGroup = new Group(centerContainer);
        this.container.getChildren().add(scalingGroup);

        this.container.setAlignment(Pos.CENTER);

        this.centerContainer.setAlignment(Pos.CENTER);
        this.centerContainer.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(this.centerContainer, Priority.ALWAYS);



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

        // Bind scaling to window size
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
