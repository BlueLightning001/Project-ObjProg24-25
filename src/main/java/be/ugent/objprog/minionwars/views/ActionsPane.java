package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.PowerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import be.ugent.objprog.minionwars.powers.Power;
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.views.cells.PowerListCell;
import be.ugent.objprog.minionwars.views.utils.UIStyleUtils;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class ActionsPane extends TabPane {
    private final ResourceBundle bundle;
    private final ListView<Power> powerListView;
    private final Tab moveTab;
    private final Tab attackTab;
    private final Tab specialTab;
    private Button skipButton;
    private Button stayButton;
    private ToggleButton attackButton;
    private ToggleButton specialAttackButton;
    private Button healButton;
    private ToggleGroup attackToggleGroup;

    public ActionsPane(TileModel tileModel, PlayerModel playerModel, PowerModel powerModel, Locale locale) {
        super();
        bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);

        //// Moving
        moveTab = makeMoveTab();

        //// Attacking
        attackTab = makeAttackTab();

        //// Special Moves
        specialTab = new Tab(bundle.getString("actions.special"));
        powerListView = new ListView<>();

        // Bind the ListView to the current player's powers
        playerModel.currentPlayerProperty().addListener((obs, oldPlayer, newPlayer) -> {
            powerListView.itemsProperty().unbind();
            powerModel.selectedPowerProperty().unbind();
            if (newPlayer != null) {
                // Properly bind power list
                powerListView.itemsProperty().bind(
                        Bindings.createObjectBinding(newPlayer::getAvailablePowers, newPlayer.availablePowerUsesProperty())
                );
                powerModel.selectedPowerProperty().bind(powerListView.getSelectionModel().selectedItemProperty());
            } else {
                powerListView.setItems(FXCollections.observableArrayList()); // Clear if no player
            }
        });
        //Force initial update (player changes before this object is constructed)
        Player currentPlayer = playerModel.getCurrentPlayer();
        if (currentPlayer != null) {
            powerListView.itemsProperty().bind(
                    Bindings.createObjectBinding(currentPlayer::getAvailablePowers, currentPlayer.availablePowerUsesProperty())
            );
            powerModel.selectedPowerProperty().bind(powerListView.getSelectionModel().selectedItemProperty());
        }


        powerListView.setCellFactory(listView -> new PowerListCell(playerModel, locale));
        // Update UI when selected tile changes
        tileModel.selectedTileProperty().addListener((obs, oldTile, newTile) -> updateAttackOptions(newTile));


        specialTab.setContent(powerListView);

        setTabDragPolicy(TabDragPolicy.FIXED);
        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        getTabs().setAll(specialTab);
    }

    private Tab makeMoveTab() {
        Tab moveTab = new Tab(this.bundle.getString("actions.move"));
        StackPane movePane = new StackPane();
        VBox.setVgrow(movePane, Priority.ALWAYS);
        HBox.setHgrow(movePane, Priority.ALWAYS);

        VBox moveContent = new VBox();
        moveContent.setAlignment(Pos.CENTER);

        Label moveLabel = new Label(bundle.getString("actions.move.moveLabel"));
        moveLabel.setWrapText(true);
        styleNode(moveLabel, movePane, 0.7, 0.4);

        stayButton = new Button(bundle.getString("actions.move.stayButton"));
        styleNode(stayButton, movePane, 0.5, 0.1);


        moveContent.getChildren().addAll(moveLabel, stayButton);
        movePane.getChildren().add(moveContent);
        moveTab.setContent(movePane);
        return moveTab;
    }

    private Tab makeAttackTab() {
        Tab attackTab = new Tab(this.bundle.getString("actions.attack"));
        StackPane attackPane = new StackPane();
        VBox.setVgrow(attackPane, Priority.ALWAYS);
        HBox.setHgrow(attackPane, Priority.ALWAYS);

        VBox attackContent = new VBox();
        attackContent.setAlignment(Pos.CENTER);

        Label attackLabel = new Label(bundle.getString("actions.attack.attackLabel"));
        styleNode(attackLabel, attackPane, 0.7, 0.4);

        attackLabel.setWrapText(true);

        // Create a ToggleGroup for the attack buttons
        attackToggleGroup = new ToggleGroup();

        attackButton = new ToggleButton("Normal attack");
        styleNode(attackButton, attackPane, 0.5, 0.1);
        attackButton.setToggleGroup(attackToggleGroup);

        specialAttackButton = new ToggleButton("Special attack");
        specialAttackButton.setMinHeight(70);
        styleNode(specialAttackButton, attackPane, 0.5, 0.2);
        autoResizeText(specialAttackButton, 0.2);
        specialAttackButton.setToggleGroup(attackToggleGroup);

        Label orLabel = new Label(bundle.getString("actions.or"));
        styleNode(orLabel, attackPane, 0.7, 0.1);

        healButton = new Button(MessageFormat.format(bundle.getString("actions.attack.heal"), Minion.HEAL_CHARGE_VALUE));
        healButton.setContentDisplay(ContentDisplay.RIGHT);
        ImageView healButtonImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/heal-D60000.png"))));
        healButton.setGraphic(healButtonImage);
        healButtonImage.fitHeightProperty().bind(healButton.heightProperty().multiply(0.3));
        healButtonImage.setPreserveRatio(true);
        styleNode(healButton, attackPane, 0.5, 0.1);

        skipButton = new Button(bundle.getString("actions.attack.skip"));
        styleNode(skipButton, attackPane, 0.5, 0.1);

        Label orLabel2 = new Label(bundle.getString("actions.or"));
        styleNode(orLabel2, attackPane, 0.7, 0.1);

        attackContent.getChildren().addAll(
                attackLabel, attackButton, specialAttackButton,
                orLabel, healButton, orLabel2, skipButton
        );
        attackPane.getChildren().add(attackContent);
        attackTab.setContent(attackPane);

        return attackTab;
    }


    private void updateAttackOptions(Tile selectedTile) {
        if (selectedTile != null && selectedTile.isOccupied()) {
            Minion occupant = selectedTile.getOccupant();
            boolean hasSpecialAttack = occupant.hasSpecialAttack();

            // Enable/disable buttons based on minion's abilities
            specialAttackButton.disableProperty().unbind();
            specialAttackButton.setVisible(hasSpecialAttack);
            if (hasSpecialAttack) {
                ImageView effectImageView = new ImageView(occupant.getEffect().getImage());
                specialAttackButton.setGraphic(effectImageView);
                effectImageView.fitHeightProperty().bind(specialAttackButton.heightProperty().multiply(0.3));
                effectImageView.setPreserveRatio(true);
                specialAttackButton.setContentDisplay(ContentDisplay.RIGHT);
                specialAttackButton.setText(bundle.getString("actions.attack.specialAttack") + "\n" + MessageFormat.format(bundle.getString("power.effect"), occupant.getEffect().getName()));

                specialAttackButton.setDisable(!occupant.specialReady());
                specialAttackButton.disableProperty().bind(occupant.recoveryChargesProperty().greaterThanOrEqualTo(occupant.getBaseRecoveryCharges()).not());
            }

            attackButton.setText(bundle.getString("actions.attack.normalAttack"));

            // Always default to normal attack when selecting a new minion
            attackToggleGroup.selectToggle(attackButton);
        }
    }

    private void styleNode(Labeled toBeStyled, StackPane container, double width, double height) {
        UIStyleUtils.styleNode(toBeStyled, container, width, height);
    }

    private void autoResizeText(Labeled label, double scaleFactor) {
        UIStyleUtils.autoResizeText(label, scaleFactor);
    }

    /**
     * Removes all listeners to prevent memory leaks.
     * Should be called when this pane is no longer needed.
     */
    public void cleanup() {
        // Use UIStyleUtils to clean up all listeners
        UIStyleUtils.cleanup();
    }

    public ToggleButton getAttackButton() {
        return attackButton;
    }

    public Tab getAttackTab() {
        return attackTab;
    }

    public Button getHealButton() {
        return healButton;
    }

    public Tab getMoveTab() {
        return moveTab;
    }

    public ListView<Power> getPowerListView() {
        return powerListView;
    }

    public Button getSkipButton() {
        return skipButton;
    }

    public ToggleButton getSpecialAttackButton() {
        return specialAttackButton;
    }

    public Tab getSpecialTab() {
        return specialTab;
    }

    public Button getStayButton() {
        return stayButton;
    }

    public void updateSelected(HexTile hexTile) {
        if (hexTile != null) {
            if (hexTile.getTile().isOccupied()) {
                getTabs().setAll(moveTab, attackTab, specialTab);
            } else {
                getTabs().setAll(specialTab);
            }
        } else {
            getTabs().setAll(specialTab);
        }
    }
}
