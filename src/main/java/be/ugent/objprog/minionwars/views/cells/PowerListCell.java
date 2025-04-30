package be.ugent.objprog.minionwars.views.cells;

import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.powers.Power;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Custom ListCell for displaying Power items in a ListView.
 */
public class PowerListCell extends ListCell<Power> {
    private final ResourceBundle bundle;
    private final PlayerModel playerModel;
    
    // UI components
    private final Label nameLabel = new Label();
    private final Label descriptionLabel = new Label();
    
    // Detail labels for each value
    private final Label valueLabel = new Label();
    private final Label radiusLabel = new Label();
    private final Label durationLabel = new Label();
    private final Label effectLabel = new Label();
    
    // Icon views for each detail
    private final ImageView powerIcon = new ImageView();
    private final ImageView valueIcon = new ImageView();
    private final ImageView radiusIcon = new ImageView();
    private final ImageView durationIcon = new ImageView();
    private final ImageView effectIcon = new ImageView();
    
    private final Image radiusImage;
    private final Image durationImage;
    
    private final Separator separator = new Separator();
    private final VBox infoBox = new VBox(nameLabel, descriptionLabel);
    private final GridPane detailsGrid = new GridPane();
    private final HBox cellContainer = new HBox(powerIcon, infoBox, separator, detailsGrid);
    
    private final Locale locale;
    

    public PowerListCell(PlayerModel playerModel, Locale locale)  {
        this.playerModel = playerModel;
        this.locale = locale;
        this.bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);
        
        // Load images
        this.radiusImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/range-119533.png")));
        this.durationImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/duration-0073FF.png")));
        
        setupUI();
    }
    
    private void setupUI() {
        separator.setOrientation(Orientation.VERTICAL);
        
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20");
        descriptionLabel.setStyle("-fx-font-size: 16;");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(200);
        descriptionLabel.setPrefWidth(200);
        descriptionLabel.setMinWidth(100);
        HBox.setHgrow(descriptionLabel, Priority.NEVER);
        
        powerIcon.setFitWidth(70);
        powerIcon.setFitHeight(70);
        powerIcon.setPreserveRatio(true);
        
        valueIcon.setFitWidth(20);
        valueIcon.setFitHeight(20);
        radiusIcon.setFitWidth(20);
        radiusIcon.setFitHeight(20);
        durationIcon.setFitWidth(20);
        durationIcon.setFitHeight(20);
        effectIcon.setFitWidth(20);
        effectIcon.setFitHeight(20);
        
        valueLabel.setStyle("-fx-font-size: 14;");
        radiusLabel.setStyle("-fx-font-size: 14;");
        durationLabel.setStyle("-fx-font-size: 14;");
        effectLabel.setStyle("-fx-font-size: 14;");
        
        // Set up the detailsGrid with two rows and two columns
        detailsGrid.setHgap(5);
        detailsGrid.setVgap(5);
        detailsGrid.add(valueLabel, 0, 0);
        detailsGrid.add(radiusLabel, 1, 0);
        detailsGrid.add(durationLabel, 0, 1);
        detailsGrid.add(effectLabel, 1, 1);
        
        // Set alignment and spacing for containers
        cellContainer.setSpacing(10);
        infoBox.setSpacing(2);
        detailsGrid.setAlignment(Pos.CENTER_LEFT);
        separator.setPrefWidth(5);
        
        detailsGrid.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(detailsGrid, Priority.ALWAYS);
    }
    
    @Override
    protected void updateItem(Power power, boolean empty) {
        super.updateItem(power, empty);
        
        if (empty || power == null) {
            setText(null);
            setGraphic(null);
        } else {
            nameLabel.setText(power.getName());
            descriptionLabel.setText(MessageFormat.format(bundle.getString("power.effect"), power.getDescription(locale)));
            powerIcon.setImage(power.getImage());
            
            // Update value details
            if (power.getValue() > 0) {
                valueLabel.setText("" + power.getValue());
                valueIcon.setImage(power.getValueImage());
                valueIcon.setVisible(true);
            } else {
                valueLabel.setText("");
                valueIcon.setVisible(false);
            }
            valueLabel.setGraphic(valueIcon);
            
            // Update radius details
            radiusLabel.setText("" + power.getRadius());
            radiusIcon.setImage(radiusImage);
            radiusLabel.setGraphic(radiusIcon);
            
            // Update effect details
            if (power.hasEffect()) {
                durationLabel.setText("" + power.getEffect().getDuration());
                durationIcon.setImage(durationImage);
                durationIcon.setVisible(true);
                
                effectLabel.setText("" + power.getEffect().getValue());
                effectIcon.setImage(power.getEffect().getImage());
                effectIcon.setVisible(true);
            } else {
                durationLabel.setText("");
                durationIcon.setVisible(false);
                
                effectLabel.setText("");
                effectIcon.setVisible(false);
            }
            durationLabel.setGraphic(durationIcon);
            effectLabel.setGraphic(effectIcon);
            
            setGraphic(cellContainer);
            
            setDisable(playerModel.getCurrentPlayer().getAvailablePowerUses() <= 0);
        }
    }
}