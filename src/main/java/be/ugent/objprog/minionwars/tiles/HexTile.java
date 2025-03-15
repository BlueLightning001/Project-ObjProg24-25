package be.ugent.objprog.minionwars.tiles;

import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.models.PlayerModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.image.Image;

//For displaying the tiles
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
    private PlayerModel playerModel;
    private boolean startPhase = true;
    public HexTile(double x, double y, Tile tile, PlayerModel playerModel, double scaleFactor) {
        this.tile = new SimpleObjectProperty<>(tile);
        this.playerModel = playerModel;
        this.currentPlayer = playerModel.currentPlayerProperty();
        setScaleFactor(scaleFactor); // Ensure proper scaling


        updateTileAppearance();
        setStrokeWidth(1);
        setStroke(Color.BLACK);

        this.tile.addListener((obs, oldTile, newTile) -> {
            updateTileAppearance();
        });
    }
    public void updateTileAppearance() { //TODO
        Image baseImage = new Image(getClass().getResource(tile.get().getImagePath()).toExternalForm());
        if (startPhase && this.tile.get().isHomeBase()) {
            if ((tile.get().getHomebase() == 1 && currentPlayer.get().equals(playerModel.getPlayer1()) ||
                    tile.get().getHomebase() == 2 && currentPlayer.get().equals(playerModel.getPlayer2())) ) {
                System.out.println("HOMEBASE SHOWN: " + tile.get());
                Color homebaseColor = playerModel.getPlayerColor(playerModel.getPlayers().get(this.tile.get().getHomebase() - 1).get());
                baseImage = applyColorOverlay(baseImage, homebaseColor);
            }
        }
        setFill(new ImagePattern(baseImage));
    }
    public void endStartPhase() {
        startPhase = false;
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
    public Image applyColorOverlay(Image baseImage, Color overlayColor) {
        int width = (int) baseImage.getWidth();
        int height = (int) baseImage.getHeight();

        // Create a Canvas to draw the blended image
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw the original image
        gc.drawImage(baseImage, 0, 0, width, height);

        // Apply the homebase color
        gc.setFill(new Color(overlayColor.getRed(), overlayColor.getGreen(), overlayColor.getBlue(), 0.3)); // 50% transparency
        gc.fillRect(0, 0, width, height);

        // Convert Canvas to an Image
        WritableImage blendedImage = new WritableImage(width, height);
        canvas.snapshot(null, blendedImage);

        return blendedImage;
    }
}

