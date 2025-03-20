package be.ugent.objprog.minionwars.views;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.Locale;

public class ActionsPane extends TabPane {
    private Locale locale;
    public ActionsPane(Locale locale) {
        super();
        this.locale = locale;

        Tab moveTab = new Tab("Move");
        moveTab.setClosable(false);
        //TODO


        Tab attackTab = new Tab("Attack");
        attackTab.setClosable(false);
        //TODO

        Tab specialTab = new Tab("Special");
        specialTab.setClosable(false);
        //TODO
    }
}
