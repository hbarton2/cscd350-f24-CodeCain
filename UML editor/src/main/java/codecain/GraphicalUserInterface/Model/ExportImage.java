package codecain.GraphicalUserInterface.Model;

import codecain.GraphicalUserInterface.View.AlertHelper;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Utility class for exporting the content of a JavaFX {@link Pane} to an image file.
 * This is commonly used for saving the graphical representation of UML diagrams to files.
 */
public class ExportImage {

    /**
     * Exports the content of a given {@link Pane} to a PNG image file.
     *
     * <p>The method takes a snapshot of the provided {@link Pane}, creates a {@link WritableImage}
     * to hold the graphical content, and saves the image as a PNG file at the specified file location.</p>
     *
     * @param container The {@link Pane} to be captured as an image. Typically contains graphical elements to export.
     * @param file      The destination {@link File} to save the PNG image. The file should have a ".png" extension.
     *                  If null, the method does not save the image.
     */
    public static void exportImage(Pane container, File file) {
        try {
            // Snapshot tre nodeContainer
            WritableImage image = container.snapshot(null, null);
            if (file != null) {
                // Save the image as a png file
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                System.out.println("UML diagram exported to: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.showAlert(Alert.AlertType.ERROR, null, null, "Failed to export UML diagram as PNG: " + e.getMessage());
        }
    }
}
