package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.models.PlayerModel;
import be.ugent.objprog.minionwars.tiles.Tile;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.image.Image;

//For displaying the tiles


public class HexTile extends Polygon {
    private static final double BASE_R = 20;
    private static final double BASE_N = Math.sqrt(BASE_R * BASE_R * 0.75);

    private double r, n, tileWidth, tileHeight;
    private final ObjectProperty<Tile> tile;
    private final ObjectProperty<Player> currentPlayer;
    private final Image baseImage;
    private boolean startPhase = true;
    private final PlayerModel playerModel;
    private Color highlightColor = Color.TRANSPARENT;
    private SimpleBooleanProperty selected;
    private final static int DEFAULT_STROKE = 1;
    private final static Color DEFAULT_STROKE_COLOR = Color.BLACK;
    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public HexTile(double x, double y, Tile tile, PlayerModel playerModel, double scaleFactor) {
        this.tile = new SimpleObjectProperty<>(tile);
        this.playerModel = playerModel;
        this.currentPlayer = playerModel.currentPlayerProperty();
        this.baseImage = new Image(getClass().getResource(this.tile.get().getImagePath()).toExternalForm());
        this.selected = new SimpleBooleanProperty(false);
        setScaleFactor(scaleFactor);
        setStrokeWidth(DEFAULT_STROKE);
        setStroke(Color.BLACK);

        setupListeners();
        updateTileAppearance();
    }

    /** Highlights the tile with the given color. */
    public void highlight(Color color) {
        this.highlightColor = color;
        updateTileAppearance();
    }

    /** Clears any highlight effect. */
    public void clearHighlight() {
        this.highlightColor = Color.TRANSPARENT;
        updateTileAppearance();
    }

    public void setSelected(boolean b) {
        this.selected.set(b);
    }

    private void setupListeners() {
        tile.addListener((obs, oldTile, newTile) -> updateTileAppearance());
        playerModel.currentPlayerProperty().addListener((obs, oldPlayer, newPlayer) -> updateTileAppearance());
        tile.get().occupantProperty().addListener((obs, oldOccupant, newOccupant) -> updateTileAppearance());
        this.selected.addListener((obs, oldSelected, newSelected) -> {
            if (newSelected) {
                this.setStroke(Color.CYAN); // Highlight border
                System.out.println("Selected: " + tile.get());

            } else {
                this.setStroke(DEFAULT_STROKE_COLOR);
                System.out.println("Unselected: " + tile.get());
            }

        });
    }

    /** Updates tile appearance using a Canvas to apply color overlays and highlighting. */
    private void updateTileAppearance() {
        Image finalImage = baseImage;
            //TODO
        // Only current player can see the minions in startphasze
        if (startPhase && tile.get().isHomeBase()) {
            Player homePlayer = playerModel.getPlayers().get(tile.get().getHomebase() - 1).get();
            if (homePlayer.equals(currentPlayer.get())) {
                Color homebaseColor = playerModel.getPlayerColor(homePlayer);
                finalImage = applyColorOverlay(baseImage, homebaseColor);
                if (tile.get().isOccupied()) {
                    finalImage = applyColorOverlay(tile.get().getOccupant().getMinionIcon(), homebaseColor);
                }
            }
            // All tiles now visible for everyone, highlight own tiles in cyan,
            // and enemies tiles in red borders
        } else if (!startPhase){

        } else {
            highlightColor = Color.TRANSPARENT;
        }

        if (highlightColor != Color.TRANSPARENT) {
            finalImage = applyColorOverlay(finalImage, highlightColor);
        }
        setFill(new ImagePattern(finalImage));
    }

    public void endStartPhase() {
        startPhase = false;
        updateTileAppearance();
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

    /** Applies a color overlay using a Canvas and returns the modified image. */
    private Image applyColorOverlay(Image baseImage, Color overlayColor) {
        int width = (int) baseImage.getWidth();
        int height = (int) baseImage.getHeight();

        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw base image
        gc.drawImage(baseImage, 0, 0, width, height);

        // Apply overlay
        gc.setFill(new Color(overlayColor.getRed(), overlayColor.getGreen(), overlayColor.getBlue(), 0.3));
        gc.fillRect(0, 0, width, height);

        WritableImage blendedImage = new WritableImage(width, height);
        canvas.snapshot(null, blendedImage);
        return blendedImage;
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


