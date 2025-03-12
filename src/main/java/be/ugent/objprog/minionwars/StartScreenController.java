package be.ugent.objprog.minionwars;

import java.util.Locale;

public class StartScreenController {
    private final StartScreenView view;
    private final PlayerModel model;
    public StartScreenController(Locale locale) {

        //TODO resource bundle

        //TODO model and view
        this.model = new PlayerModel();
        this.view = new StartScreenView(model,locale);
    }
    public StartScreenView getView() {
        return this.view;
    }
}
