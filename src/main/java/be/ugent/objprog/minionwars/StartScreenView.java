package be.ugent.objprog.minionwars;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Scale;

import java.util.Locale;
import java.util.Objects;
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
    private final Button startButton;
    private final Label titleLabel;
    private final StackPane titleContainer;

    public StartScreenView(PlayerModel model, Locale locale) {
        Font labelFont = Font.font("Monotype Corsiva", FontWeight.BOLD, 20);
        this.bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);
        this.model = model;

        ObservableList<Player> players = model.getPlayers();

        this.container = new StackPane();
        this.centerContainer = new VBox();

        // Shadow effect
        DropShadow shadow = new DropShadow();

        shadow.setBlurType(BlurType.GAUSSIAN);
        shadow.setRadius(10);
        shadow.setColor(Color.BLACK);

        this.player1TextField = new TextField();
        this.player1Label = new Label(bundle.getString("startScreen.player1Label"));
        this.player1Label.setStyle("-fx-text-fill: white;");
        this.player1Label.setEffect(shadow);
        this.player1Label.setFont(labelFont);
        this.player1TextField.setPromptText(bundle.getString("startScreen.player1Prompt"));

        this.player2TextField = new TextField();
        this.player2Label = new Label(bundle.getString("startScreen.player2Label"));
        this.player2Label.setStyle("-fx-text-fill: white;");
        this.player2Label.setEffect(shadow);
        this.player2Label.setFont(labelFont);
        this.player2TextField.setPromptText(bundle.getString("startScreen.player2Prompt"));

        this.moneyTextField = new TextField();
        this.moneyLabel = new Label(bundle.getString("startScreen.moneyLabel"));
        this.moneyLabel.setStyle("-fx-text-fill: white;");
        this.moneyLabel.setEffect(shadow);
        this.moneyLabel.setFont(labelFont);
        this.moneyTextField.setPromptText(bundle.getString("startScreen.moneyPrompt"));

        this.grid = new GridPane();
        grid.setHgap(10); // Space between Label & TextField
        grid.setVgap(10); // Space between rows
        grid.setAlignment(Pos.CENTER);

        // Add to grid (column, row)
        grid.add(player1Label, 0, 0);
        grid.add(player1TextField, 1, 0);
        grid.add(player2Label, 0, 1);
        grid.add(player2TextField, 1, 1);
        grid.add(moneyLabel, 0, 2);
        grid.add(moneyTextField, 1, 2);

        // Title
        this.titleContainer = new StackPane();
        Image titleBanner = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/other/banner.png")));
        this.titleContainer.setPrefSize(titleBanner.getWidth(), titleBanner.getHeight());
        this.titleContainer.setBackground(new Background(getBackgroundImage(titleBanner)));
        this.titleLabel = new Label(bundle.getString("startScreen.titleLabel"));
        titleLabel.setFont(Font.font("Old English Text MT", 90));
        titleLabel.setStyle("-fx-text-fill: #2D2D2D;");
        titleContainer.getChildren().add(titleLabel);

        // Start button
        this.startButton = new Button(bundle.getString("startScreen.startButton"));
        this.startButton.setPrefSize(centerContainer.getPrefWidth(), 50);
        this.startButton.setFont(labelFont);


        this.centerContainer.getChildren().addAll(titleContainer, grid, startButton); //TODO startbutton
        this.centerContainer.setSpacing(20);

        // Used for resizing the window
        Group scalingGroup = new Group(centerContainer);
        this.container.getChildren().add(scalingGroup);

        this.container.setAlignment(Pos.CENTER);

        this.centerContainer.setAlignment(Pos.CENTER);
        this.centerContainer.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(this.centerContainer, Priority.ALWAYS);


        // Background
        Image backgroundImage = new Image("be/ugent/objprog/minionwars/images/splash-end.jpg");

        BackgroundImage bgImage = getBackgroundImage(backgroundImage);
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

    private static BackgroundImage getBackgroundImage(Image backgroundImage) {
        // Scale to 100% of parent width and height
        // Scale width and height proportionally
        // Ensure the background covers the entire container
        return new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        100, 100,
                        true, true,
                        true, true
                )
        );
    }

    public StackPane getContainer() {
        return this.container;
    }

    public TextField getMoneyTextField() {
        return moneyTextField;
    }

    public TextField getPlayer1TextField() {
        return player1TextField;
    }

    public TextField getPlayer2TextField() {
        return player2TextField;
    }

    public Button getStartButton() {
        return startButton;
    }
}
