package be.ugent.objprog.minionwars.tiles;

import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.models.PlayerModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.image.Image;

//For displaying the tiles
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class HexTile extends Polygon {
    private static final double BASE_R = 20;
    private static final double BASE_N = Math.sqrt(BASE_R * BASE_R * 0.75);

    private double startX, startY;
    private double r, n, tileWidth, tileHeight;
    private double x, y;
    private final ObjectProperty<Tile> tile;
    private final ObjectProperty<Player> currentPlayer; // Track the active player
    private Rectangle overlay; // Homebase overlay effect

    public HexTile(double x, double y, Tile tile, PlayerModel playerModel, double scaleFactor) {
        this.tile = new SimpleObjectProperty<>(tile);
        this.currentPlayer = playerModel.currentPlayerProperty();
        setScaleFactor(scaleFactor); // Ensure proper scaling



        // Bind the fill property to change based on tile state
        fillProperty().bind(Bindings.createObjectBinding(this::computeFill, this.tile, this.currentPlayer));

    }

    public Rectangle getOverlay() {
        return overlay;
    }
    public Tile getTile() {
        return tile.get();
    }

    public ObjectProperty<Tile> tileProperty() {
        return tile;
    }

    public void setScaleFactor(double scaleFactor) {
        this.r = BASE_R * scaleFactor;
        this.n = Math.sqrt(this.r * this.r * 0.75);
        this.tileWidth = 2 * this.n;
        this.tileHeight = 2 * this.r;

        updateShape();
    }

    // Ensure shape updates with scale
    private void updateShape() {
        getPoints().setAll(
                x, y,
                x, y + r,
                x + n, y + r * 1.5,
                x + tileWidth, y + r,
                x + tileWidth, y,
                x + n, y - r * 0.5
        );
    }


    private Paint computeFill() {
        if (tile.get().isOccupied()) {
            Minion minion = tile.get().getOccupant();
            if (minion != null && minion.getOwner().equals(currentPlayer.get())) {
                return new ImagePattern(minion.getMinionIcon());
            }
        }
        return new ImagePattern(new Image(getClass().getResource(tile.get().getImagePath()).toExternalForm()));
    }
}

