package be.ugent.objprog.minionwars.tiles;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.image.Image;

//For displaying the tiles
public class HexTile extends Polygon {
    private static final double r = 20;
    private static final double n = Math.sqrt(r * r * 0.75);
    private static final double TILE_HEIGHT = 2 * r;
    private static final double TILE_WIDTH = 2 * n;
    private double startX, startY;
    private ObjectProperty<Tile> tile;

    public HexTile(double x, double y, Tile tile) {
        this.tile = new SimpleObjectProperty<>(tile);
        getPoints().addAll(
                x, y,
                x, y + r,
                x + n, y + r * 1.5,
                x + TILE_WIDTH, y + r,
                x + TILE_WIDTH, y,
                x + n, y - r * 0.5
        );

        // Load image
        System.out.println(tile);
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
                System.out.println("Pressed: " + tile.toString());
            }
        });

    }
    @Override
    public String toString() {
        return tile.toString();
    }
}

