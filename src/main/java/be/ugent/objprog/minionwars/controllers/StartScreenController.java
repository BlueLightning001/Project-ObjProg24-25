package be.ugent.objprog.minionwars.controllers;

import be.ugent.objprog.minionwars.JDOMReader;
import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.views.StartScreenView;
import javafx.scene.Scene;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class StartScreenController {
    private final StartScreenView view;
    private final PlayerModel model;
    private final ResourceBundle bundle;
    private final Stage stage;
    private final Double prefStageWidth = null;
    private final Double prefStageHeight = null;
    private final Locale locale;
    private final JDOMReader reader;

    public StartScreenController(Stage stage, Locale locale, JDOMReader reader) {
        this.reader = reader;
        this.bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);
        this.model = new PlayerModel();
        this.view = new StartScreenView(model, locale);
        this.stage = stage;
        this.locale = locale;

        setupBindings();
        setupListeners();
    }

    private void setupBindings() {
        view.getPlayer1TextField().textProperty().bindBidirectional(model.player1Property().get().nameProperty(), new TrimStringConverter());
        view.getPlayer2TextField().textProperty().bindBidirectional(model.player2Property().get().nameProperty(), new TrimStringConverter());
        view.getMoneyTextField().textProperty().bindBidirectional(model.startBudgetProperty(), new NumberStringConverter());
    }

    private void setupListeners() {
        // Only allows numbers moneyTextField
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) { // Allow only digits
                return change;
            }
            return null; // Reject the change
        };

        TextFormatter<Number> textFormatter = new TextFormatter<>(new NumberStringConverter(), model.getMinStartBudget(), filter);
        view.getMoneyTextField().setTextFormatter(textFormatter);
        // Makes it easier to navigate through the menu using only keyboard
        view.getPlayer1TextField().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                view.getPlayer2TextField().requestFocus();
            }
        });
        view.getPlayer2TextField().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                view.getMoneyTextField().requestFocus();
            }
        });
        view.getMoneyTextField().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                startGameIfValid();
            }
        });

        view.getStartButton().setOnAction(event -> startGameIfValid());
    }

    private void startGameIfValid() {
        if (!model.allPlayersHaveNames()) {
            view.showWarning(bundle.getString("warning.missingPlayers"));
            return;
        }

        if (!model.playersHaveDistinctNames()) {
            view.showWarning(bundle.getString("warning.duplicateNames"));
            return;
        }

        if (!model.isValidStartBudget()) {
            view.showWarning(MessageFormat.format(bundle.getString("warning.invalidStartBudget"), model.getMinStartBudget(), model.getMaxStartBudget()));
            return;
        }

        // If all checks pass, start the game
        view.hideWarning();
        model.giveStartBudget();
        startGame();
    }

    private void startGame() {
        boolean fullscreen = stage.isFullScreen();

        GameController gameController = new GameController(stage, model, locale, reader, model.isDespicable());
        Scene scene = new Scene(gameController.getView(), getView().getWidth(), getView().getHeight());

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F11) {
                stage.setFullScreen(!stage.isFullScreen());
            }
        });
        stage.setMinHeight(400);
        stage.setMinWidth(410);
        stage.setScene(scene);
        stage.centerOnScreen();
        if (fullscreen) {
            stage.setFullScreenExitHint(""); // Hide the hint
            stage.setFullScreen(true);
        }

        stage.show();
        stage.setFullScreenExitHint(null); // Restore default hint
    }

    public Region getView() {
        return view.getContainer();
    }

    private static class TrimStringConverter extends StringConverter<String> {

        /**
         * Converts the string provided into an object defined by the specific converter.
         * Format of the string and type of the resulting object is defined by the specific converter.
         *
         * @param string the {@code String} to convert
         * @return an object representation of the string passed in.
         */
        @Override
        public String fromString(String string) {
            return string != null ? string.trim() : "";
        }

        @Override
        public String toString(String object) {
            return object != null ? object.trim() : "";
        }
    }

}
