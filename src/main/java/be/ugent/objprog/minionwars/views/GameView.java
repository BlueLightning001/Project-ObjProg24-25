package be.ugent.objprog.minionwars.views;

import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class GameView {
    private StackPane container;
    private HBox root;
    public TableView menuTable;

    public GameView() {
        container = new StackPane();
        root = new HBox();

    }
    public Region getView() {
        return container;
    }
}
