package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.models.TileModel;
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.tiles.VoidTile;
import be.ugent.objprog.minionwars.views.utils.ImageUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

import java.util.List;
import java.util.Objects;


public class HexTile extends Polygon {
    private static final double BASE_R = 20;
    private final static int DEFAULT_STROKE = 1;
    private final static Color DEFAULT_STROKE_COLOR = Color.BLACK;
    private final ObjectProperty<Tile> tile;
    private final ObjectProperty<Player> currentPlayer;
    private final Image baseImage;
    private final PlayerModel playerModel;
    private final ObjectProperty<Color> highlightColor = new SimpleObjectProperty<>(Color.TRANSPARENT);
    private final TileModel tileModel;
    private final SimpleBooleanProperty selected;
    private double r;
    private double n;
    private double tileWidth;
    private boolean startPhase = true;

    public HexTile(Tile tile, PlayerModel playerModel, double scaleFactor, TileModel tileModel) {
        this.tileModel = tileModel;
        this.tile = new SimpleObjectProperty<>(tile);
        this.playerModel = playerModel;
        this.currentPlayer = playerModel.currentPlayerProperty();
        this.baseImage = new Image(Objects.requireNonNull(getClass().getResource(this.tile.get().getImagePath())).toExternalForm());
        this.selected = new SimpleBooleanProperty(false);
        setScaleFactor(scaleFactor);
        setStrokeWidth(DEFAULT_STROKE);
        setStroke(Color.BLACK);

        setupListeners();
        updateTileAppearance();
    }

    public void clearHighlight() {
        setHighlightColor(Color.TRANSPARENT);
    }

    public void endStartPhase() {
        startPhase = false;
        updateTileAppearance();
    }

    // Updates tile appearance using a Canvas to apply color overlays and highlighting.
    private void updateTileAppearance() {
        setStroke(Color.BLACK);
        Image finalImage = baseImage;

        // During start phase, show home base for current player
        if (startPhase && tile.get().isHomeBase()) {
            Player homePlayer = playerModel.getPlayers().get(tile.get().getHomebase() - 1).get();
            if (homePlayer.equals(currentPlayer.get())) {
                Color homebaseColor = playerModel.getPlayerColor(homePlayer);
                finalImage = applyColorOverlay(baseImage, homebaseColor);
                if (tile.get().isOccupied()) {
                    finalImage = applyColorOverlay(tile.get().getOccupant().getMinionIcon(), homebaseColor);
                }
            }
        } else if (!startPhase && tile.get().isOccupied()) {
            finalImage = tile.get().getOccupant().getMinionIcon();

            // Set border color for enemy tiles
            Player homePlayer = playerModel.getCurrentPlayer();
            if (!homePlayer.equals(tile.get().getOccupant().getOwner())) {
                setStroke(Color.RED);
            }
        }

        // Apply highlight overlay
        if (!highlightColor.get().equals(Color.TRANSPARENT)) {
            finalImage = applyColorOverlay(finalImage, highlightColor.get());
        }
        setFill(new ImagePattern(finalImage));
    }

    /**
     * Applies a color overlay using ImageUtils and returns the modified image.
     */
    private Image applyColorOverlay(Image baseImage, Color overlayColor) {
        return ImageUtils.applyColorOverlay(baseImage, overlayColor);
    }

    public Color getHighlightColor() {
        return highlightColor.get();
    }

    public void setHighlightColor(Color color) {
        if (!tile.get().getClass().equals(VoidTile.class)) {
            highlightColor.set(color);
        }
    }

    public List<Tile> getTilesInRadius(int radius) {
        return tileModel.getTilesInRadius(this.getTile(), 0, radius);
    }

    public Tile getTile() {
        return tile.get();
    }

    public List<Tile> getTilesInRange(int minRange, int maxRange) {
        return tileModel.getTilesInRadius(this.getTile(), minRange, maxRange);
    }

    public void highlight(Color color) {
        setHighlightColor(color);
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean b) {
        this.selected.set(b);
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public void setScaleFactor(double scaleFactor) {
        this.r = BASE_R * scaleFactor;
        this.n = Math.sqrt(this.r * this.r * 0.75);
        this.tileWidth = 2 * this.n;
        updateShape();
    }

    private void setupListeners() {
        tile.addListener((obs) -> updateTileAppearance());
        playerModel.currentPlayerProperty().addListener((obs) -> updateTileAppearance());
        tile.get().occupantProperty().addListener((obs) -> updateTileAppearance());
        this.selected.addListener((obs, oldSelected, newSelected) -> {
            if (newSelected) {
                this.setStroke(Color.CYAN); // Highlight border

            } else {
                if (startPhase || !this.getTile().isOccupied() || (this.getTile().isOccupied() && playerModel.getCurrentPlayer().equals(this.getTile().getOccupant().getOwner()))) {
                    this.setStroke(DEFAULT_STROKE_COLOR);
                } else if (this.getTile().isOccupied()) {
                    this.setStroke(Color.RED);
                }
            }

        });
        highlightColor.addListener((obs) -> {
            updateTileAppearance();
        });

        // Add hover effect
        setOnMouseEntered(event -> {
            if (!selected.get() && !this.tile.get().getClass().equals(VoidTile.class)) { // Only change if not selected
                this.setStroke(Color.YELLOW); // Temporary highlight on hover
            }
        });

        setOnMouseExited(event -> {
            if (!selected.get()) { // Restore original border if not selected
                updateTileAppearance();
            }
        });
    }


    public ObjectProperty<Tile> tileProperty() {
        return tile;
    }

    private void updateShape() {
        getPoints().setAll(
                0.0, 0.0,
                0.0, r,
                n, r * 1.5,
                tileWidth, r,
                tileWidth, 0.0,
                n, -r * 0.5
        );
    }
}
