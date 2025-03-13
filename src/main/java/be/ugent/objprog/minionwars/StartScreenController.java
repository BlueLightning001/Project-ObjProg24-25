package be.ugent.objprog.minionwars;

import javafx.scene.layout.Region;
import javafx.util.converter.NumberStringConverter;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class StartScreenController {
    private final StartScreenView view;
    private final PlayerModel model;
    private final ResourceBundle bundle;

    public StartScreenController(Locale locale) {
        this.bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);
        this.model = new PlayerModel();
        this.view = new StartScreenView(model, locale);

        setupBindings();
        setupListeners();
    }

    private void setupBindings() {
        view.getPlayer1TextField().textProperty().bindBidirectional(model.player1Property().get().nameProperty());
        view.getPlayer2TextField().textProperty().bindBidirectional(model.player2Property().get().nameProperty());
        view.getMoneyTextField().textProperty().bindBidirectional(model.startBudgetProperty(), new NumberStringConverter());
    }

    private void setupListeners() {
        view.getMoneyTextField().textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                view.getMoneyTextField().setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        view.getStartButton().setOnAction(event -> startGameIfValid());
    }

    private void startGameIfValid() {
        if (model.allPlayersHaveNames()) {
            if (model.playersHaveDistinctNames()) {
                if (model.isValidStartBudget()) {
                    view.hideWarning();
                    System.out.println("STARTED");
                    model.giveStartBudget();
                    startGame();
                } else {
                    view.showWarning(MessageFormat.format(bundle.getString("warning.invalidStartBudget"), model.getMinStartBudget(), model.getMaxStartBudget()));
                }
            }else {
                view.showWarning(bundle.getString("warning.duplicateNames"));
            }
        } else {
            view.showWarning(bundle.getString("warning.missingPlayers"));
        }
    }
    public Region getView() {
        return view.getContainer();
    }
    private void startGame() {
        System.out.println(model);
    }

}
