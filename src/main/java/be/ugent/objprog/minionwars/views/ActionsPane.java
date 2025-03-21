package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.powers.Power;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Locale;

public class ActionsPane extends TabPane {
    private Locale locale;
    private double font = 15;
    private double error = 50; //%
    private Button moveButton;
    private Button attackButton;
    private Button specialAttackButton;
    private Button healButton;
    public ActionsPane(Locale locale) {
        super();
        this.locale = locale;


        //// Moving
        Tab moveTab = getMoveTab();

        //// Attacking
        Tab attackTab = getAttackTab();

        //// Special Moves
        Tab specialTab = new Tab("Special");
        TableView<Power> powerTableView = new TableView<>();

        //TODO

        setTabDragPolicy(TabDragPolicy.FIXED);
        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        setVisible(false);
        getTabs().addAll(moveTab,attackTab,specialTab);
    }

    private Tab getAttackTab() {
        Tab attackTab = new Tab("Attack");
        StackPane attackPane = new StackPane();
        VBox.setVgrow(attackPane, Priority.ALWAYS);
        HBox.setHgrow(attackPane, Priority.ALWAYS);
        attackPane.setStyle("-fx-border-color: Orange; -fx-border-width: 2");

        VBox attackContent = new VBox();
        attackContent.setStyle("-fx-border-color: BLUE; -fx-border-width: 10");
        attackContent.setAlignment(Pos.CENTER);

        Label attackLabel = new Label("Attack Label");
        styleNode(attackLabel,attackPane,0.7,0.4);

        attackButton = new Button("Normal attack");
        styleNode(attackButton,attackPane,0.5,0.1);

        specialAttackButton = new Button("Special attack");
        styleNode(specialAttackButton,attackPane,0.5,0.1);

        Label orLabel = new Label("Or Label");
        styleNode(orLabel,attackPane,0.7,0.1);

        healButton = new Button("Heal");
        styleNode(healButton,attackPane,0.5,0.1);


        attackContent.getChildren().addAll(attackLabel,attackButton,specialAttackButton, orLabel, healButton);
        attackPane.getChildren().add(attackContent);
        attackTab.setContent(attackPane);
        return attackTab;
    }

    private void styleNode(Label toBeStyled, StackPane container, double width, double height) {
        toBeStyled.setWrapText(true);
        toBeStyled.setAlignment(Pos.CENTER);
        toBeStyled.prefWidthProperty().bind(container.widthProperty().multiply(width));
        toBeStyled.prefHeightProperty().bind(container.heightProperty().multiply(height));
        toBeStyled.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            Platform.runLater(() -> {
                double fontSize = newWidth.doubleValue() * 0.1;
                toBeStyled.setStyle("-fx-font-size: " + fontSize + "px;");
            });
        });
    }
    private void styleNode(Button toBeStyled, StackPane container, double width, double height) {
        toBeStyled.setAlignment(Pos.CENTER);
        toBeStyled.prefWidthProperty().bind(container.widthProperty().multiply(width));
        toBeStyled.prefHeightProperty().bind(container.heightProperty().multiply(height));
        toBeStyled.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            Platform.runLater(() -> {
                double fontSize = newWidth.doubleValue() * 0.1;
                toBeStyled.setStyle("-fx-font-size: " + fontSize + "px;");
            });
        });

    }

    private Tab getMoveTab() {
        Tab moveTab = new Tab("Move");
        StackPane movePane = new StackPane();
        VBox.setVgrow(movePane, Priority.ALWAYS);
        HBox.setHgrow(movePane, Priority.ALWAYS);
        movePane.setStyle("-fx-border-color: Yellow");

        VBox moveContent = new VBox();
        moveContent.setStyle("-fx-border-color: BLUE; -fx-border-width: 10");
        moveContent.setAlignment(Pos.CENTER);

        Label moveLabel = new Label("Move Label");
        styleNode(moveLabel,movePane,0.7,0.4);

        moveButton = new Button("Stand still");
        styleNode(moveButton,movePane,0.5,0.1);


        moveContent.getChildren().addAll(moveLabel, moveButton);
        movePane.getChildren().add(moveContent);
        moveTab.setContent(movePane);
        return moveTab;
    }

    public void updateSelected(HexTile hexTile) {
        if (hexTile != null) {
            if (hexTile.getTile().isOccupied()){
                setVisible(true);
            } else {
                System.out.println("TILE NOT OCCUPIED");
                setVisible(false);
            }
        } else {
            setVisible(false);
        }
    }
}
