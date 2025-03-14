package be.ugent.objprog.minionwars.tiles;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.image.Image;

//For displaying the tiles
public class HexTile extends Polygon {
    private static final double BASE_R = 20;
    private static final double BASE_N = Math.sqrt(BASE_R * BASE_R * 0.75);
    private  double startX;
    private  double startY;
    private double r, n, tileWidth, tileHeight;
    private ObjectProperty<Tile> tile;

    public HexTile(double x, double y, Tile tile, double scaleFactor) {
        this.tile = new SimpleObjectProperty<>(tile);
        setScaleFactor(scaleFactor);
        updateShape(x, y);

        // Load image
        Image image = new Image(getClass().getResource(tile.getImagePath()).toExternalForm());
        setFill(new ImagePattern(image));

        setStrokeWidth(1);
        setStroke(Color.BLACK);
        setOnMousePressed(event -> {
            startX = event.getScreenX();
            startY = event.getScreenY();
        });

        setOnMouseReleased(event -> {
            double endX = event.getScreenX();
            double endY = event.getScreenY();

            double dragDistance = Math.hypot(endX - startX, endY - startY);

            if (dragDistance < 5) { // Only register a click if the movement is small
                handleTileClick(tile);
            }
        });

    }

    private void handleTileClick(Tile tile) {
        System.out.println("PRESSED: "+tile);
    }

    public void setScaleFactor(double scaleFactor) {
        this.r = BASE_R * scaleFactor;
        this.n = Math.sqrt(this.r * this.r * 0.75);
        this.tileWidth = 2 * this.n;
        this.tileHeight = 2 * this.r;
    }

    private void updateShape(double x, double y) {
        getPoints().clear();
        getPoints().addAll(
                x, y,
                x, y + r,
                x + n, y + r * 1.5,
                x + tileWidth, y + r,
                x + tileWidth, y,
                x + n, y - r * 0.5
        );
    }
}
