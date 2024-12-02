package codecain.CommandLineInterface.Model;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import codecain.CommandLineInterface.Controller.CLIController;
import codecain.GraphicalUserInterface.View.AlertHelper;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

public class CLIExportImage {
     /**
     * Exports the content of a given Pane to a PNG image file.
     * Calculates the bounds of all visible nodes within the Pane, creates a snapshot of the
     * graphical content, and saves the image as a PNG file at the specified file location.
     *
     * @param container The Pane whose graphical content is to be captured. Typically contains nodes such as UML diagrams.
     * @param file The destination File to save the PNG image. Must have a ".png" extension.
     *             If null, the method does not save the image.
     */
    public static void exportImage(Pane container, File file) {
        try {
            Bounds bounds = getNodeBounds(container);
            Rectangle2D viewport = boundsToRectangle2D(bounds);
            SnapshotParameters snapshotParameters = createSnapshotParameters(viewport);
            WritableImage image = new WritableImage(
                (int) bounds.getWidth(),
                (int) bounds.getHeight()
            );
            container.snapshot(snapshotParameters, image);

            if (file != null) {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                System.out.println("UML diagram exported to: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.showAlert(Alert.AlertType.ERROR, null, null, "Failed to export UML diagram as PNG: " + e.getMessage());
        }
    }
    

    /**
     * Calculates the combined bounds of all child nodes within a given Pane.
     * Iterates over all child nodes of the container and computes
     * the smallest bounding box that encompasses all nodes.
     *
     * @param container The Pane whose child nodes' bounds are to be calculated.
     * @return A Bounds object representing the combined bounds of all child nodes.
     */
    private static Bounds getNodeBounds(Pane container) {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
    
        for (Node node : container.getChildren()) {
            Bounds nodeBounds = node.getBoundsInParent();
            minX = Math.min(minX, nodeBounds.getMinX());
            minY = Math.min(minY, nodeBounds.getMinY());
            maxX = Math.max(maxX, nodeBounds.getMaxX());
            maxY = Math.max(maxY, nodeBounds.getMaxY());
        }
    
        return new javafx.geometry.BoundingBox(minX, minY, maxX - minX, maxY - minY);
    }

    /**
     * Converts a Bounds object into a Rectangle2D object.
     * Extracts the minimum x and y coordinates, width, and height
     * from the Bounds object and constructs a Rectangle2D.
     *
     * @param bounds The Bounds object to be converted.
     * @return A Rectangle2D object representing the same region as the Bounds.
     */
    private static Rectangle2D boundsToRectangle2D(Bounds bounds) {
        return new Rectangle2D(
            bounds.getMinX(),
            bounds.getMinY(),
            bounds.getWidth(),
            bounds.getHeight()
        );
    }

    /**
     * Creates a SnapshotParameters object with a specified viewport.
     * The viewport defines the region of the Pane to be captured during the snapshot operation.
     *
     * @param viewport A Rectangle2D representing the region to capture.
     * @return A configured SnapshotParameters object with the specified viewport.
     */
    private static SnapshotParameters createSnapshotParameters(Rectangle2D viewport) {
        SnapshotParameters params = new SnapshotParameters();
        params.setViewport(viewport);
        return params;
    }
    private String handleExport(String[] tokens) {
    if (tokens.length < 2) {
        return "Usage: export <filename>.png";
    }

    String fileName = tokens[1];
    if (!fileName.endsWith(".png")) {
        fileName += ".png";
    }

    File file = new File(fileName);
    if (CLIController.nodeContainer == null) {
        return "Error: UML diagram container is not set.";
    }

    CLIExportImage.exportImage(CLIController.nodeContainer, file);
    return "UML diagram exported to: " + file.getAbsolutePath();
}

}
