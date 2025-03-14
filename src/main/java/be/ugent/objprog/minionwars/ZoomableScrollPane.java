package be.ugent.objprog.minionwars;


import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

// Credit to
/*
/   Hári, D. H. (2017, June 1). JavaFx 8 - Scaling / zooming ScrollPane relative to mouse position.
    Stack Overflow. Retrieved March 14, 2025, from https://stackoverflow.com/a/44314455
 */
public class ZoomableScrollPane extends ScrollPane {
    private double scaleValue = 0.7;
    private double zoomIntensity = 0.02;
    private Node target;
    private Node zoomNode;
    private final double minScale = 0.5;
    private final double maxScale = 3.0;

    public ZoomableScrollPane(Node target) {
        super();
        this.target = target;
        this.zoomNode = new Group(target);
        setContent(outerNode(zoomNode));

        setPannable(false);
        zoomNode.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                setPannable(true);  // Enable panning
            }
        });
        zoomNode.setOnMouseReleased(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                setPannable(false);  // Disable panning after release
            }
        });
        this.getStylesheets().add("/be/ugent/objprog/minionwars/css/scrollpane.css");


        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setFitToHeight(true); //center
        setFitToWidth(true); //center

        updateScale();
    }

    private Node outerNode(Node node) {
        Node outerNode = centeredNode(node);
        outerNode.setOnScroll(e -> {
            e.consume();
            onScroll(e.getTextDeltaY(), new Point2D(e.getX(), e.getY()));
        });
        return outerNode;
    }

    private Node centeredNode(Node node) {
        VBox vBox = new VBox(node);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private void updateScale() {
        target.setScaleX(scaleValue);
        target.setScaleY(scaleValue);
    }
    private void onScroll(double wheelDelta, Point2D mousePoint) {
        double zoomFactor = Math.exp(wheelDelta * zoomIntensity);
        double newScale = scaleValue * zoomFactor;

        // Apply zoom limits
        if (newScale < minScale) {
            newScale = minScale;
        } else if (newScale > maxScale) {
            newScale = maxScale;
        }

        double actualZoomFactor = newScale / scaleValue;
        scaleValue = newScale;
        updateScale();
        this.layout();

        Bounds innerBounds = zoomNode.getLayoutBounds();
        Bounds viewportBounds = getViewportBounds();

        double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
        double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());


        Point2D posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(mousePoint));


        Point2D adjustment = target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(actualZoomFactor - 1));


        Bounds updatedInnerBounds = zoomNode.getBoundsInLocal();
        this.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
        this.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
    }

}
