package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.models.PlayerModel;
import javafx.beans.binding.Bindings;
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

public class StartScreenView  {
    private final ResourceBundle bundle;
    private final PlayerModel model;
    private final StackPane container;
    private final VBox centerContainer;
    private TextField player1TextField;
    private Label player1Label;
    private TextField player2TextField;
    private Label player2Label;
    private TextField moneyTextField;
    private Label moneyLabel;
    private GridPane grid;
    private Button startButton;
    private Label titleLabel;
    private StackPane titleContainer;
    private Label warningLabel;
    public StartScreenView(PlayerModel model, Locale locale) {
        this.model = model;
        this.bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);
        Font labelFont = Font.font("Monotype Corsiva", FontWeight.BOLD, 20);
        DropShadow shadow = createDropShadow();

        this.container = new StackPane();
        this.centerContainer = new VBox();

        setupTitle();
        setupGrid(labelFont, shadow);
        setupStartButton(labelFont);
        setupWarningLabel();
        setupBackground();
        setupResizing();

        this.centerContainer.getChildren().addAll(titleContainer, warningLabel, grid, startButton);
        this.centerContainer.setSpacing(20);
        this.centerContainer.setAlignment(Pos.CENTER);
        this.centerContainer.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(this.centerContainer, Priority.ALWAYS);

        this.container.setAlignment(Pos.CENTER);
        Group scalingGroup = new Group(centerContainer);
        this.container.getChildren().add(scalingGroup);
    }

    // Creates a shadow effect
    private DropShadow createDropShadow() {
        DropShadow shadow = new DropShadow();
        shadow.setBlurType(BlurType.GAUSSIAN);
        shadow.setRadius(10);
        shadow.setColor(Color.BLACK);
        return shadow;
    }

    // Sets up the title container with a custom background
    private void setupTitle() {
        this.titleContainer = new StackPane();
        Image titleBanner = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/other/banner.png")));
        this.titleContainer.setPrefSize(titleBanner.getWidth(), titleBanner.getHeight());
        this.titleContainer.setBackground(new Background(getBackgroundImage(titleBanner)));

        this.titleLabel = new Label(bundle.getString("startScreen.titleLabel"));
        titleLabel.setFont(Font.font("Old English Text MT", 90));
        titleLabel.setStyle("-fx-text-fill: #2D2D2D;");
        titleContainer.getChildren().add(titleLabel);
    }

    // Sets up the grid layout for text fields and labels
    private void setupGrid(Font labelFont, DropShadow shadow) {
        this.grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        this.player1TextField = createTextField("startScreen.player1Prompt");
        this.player1Label = createStyledLabel("startScreen.player1Label", labelFont, shadow);

        this.player2TextField = createTextField("startScreen.player2Prompt");
        this.player2Label = createStyledLabel("startScreen.player2Label", labelFont, shadow);

        this.moneyTextField = createTextField("startScreen.moneyPrompt");
        this.moneyLabel = createStyledLabel("startScreen.moneyLabel", labelFont, shadow);

        grid.add(player1Label, 0, 0);
        grid.add(player1TextField, 1, 0);
        grid.add(player2Label, 0, 1);
        grid.add(player2TextField, 1, 1);
        grid.add(moneyLabel, 0, 2);
        grid.add(moneyTextField, 1, 2);
    }

    private Label createStyledLabel(String key, Font font, DropShadow shadow) {
        Label label = new Label(bundle.getString(key));
        label.setStyle("-fx-text-fill: white;");
        label.setEffect(shadow);
        label.setFont(font);
        return label;
    }

    private TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(bundle.getString(prompt));
        return textField;
    }


    private void setupStartButton(Font labelFont) {
        this.startButton = new Button(bundle.getString("startScreen.startButton"));
        this.startButton.setPrefSize(centerContainer.getPrefWidth(), 50);
        this.startButton.setFont(labelFont);
        this.startButton.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.7); " +
                        "-fx-text-fill: white; " +
                        "-fx-opacity: 0.8; " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 2px;"
        );
    }


    private void setupWarningLabel() {
        this.warningLabel = new Label();
        warningLabel.setStyle("-fx-text-fill: red;");
        warningLabel.setVisible(false);
    }


    private void setupBackground() {
        Image backgroundImage = new Image("be/ugent/objprog/minionwars/images/splash-start.jpg");
        BackgroundImage bgImage = getBackgroundImage(backgroundImage);
        container.setBackground(new Background(bgImage));
    }

    // Configures window resizing
    private void setupResizing() {
        Scale scale = new Scale(1, 1);
        centerContainer.getTransforms().add(scale);

        scale.xProperty().bind(Bindings.createDoubleBinding(
                () -> Math.min(container.getWidth() / 500, container.getHeight() / 500),
                container.widthProperty(), container.heightProperty()
        ));
        scale.yProperty().bind(scale.xProperty());
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

    public void hideWarning() {
        warningLabel.setVisible(false);
    }

    public void showWarning(String message) {
        warningLabel.setVisible(true);
        warningLabel.setText(message);
    }
}
