package codecain.CommandLineInterface.Model;

import codecain.BackendCode.Model.SaveManager;

/**
 * Handles saving and loading operations for UML diagrams.
 */
public class FileOperations {

    /**
     * Saves the UML diagram to a specified file in JSON format.
     *
     * @param fileName the name of the file to save the diagram to
     * @return a confirmation message if the diagram is saved successfully, or an error message if the save fails
     */
    public String saveDiagram(String fileName) {
        if (fileName.isEmpty()) {
            return "Usage: save <filename>";
        }
        try {
            SaveManager.saveToJSON(fileName + ".json");
            return "UML diagram saved to " + fileName + ".json";
        } catch (Exception e) {
            return "Error saving UML diagram: " + e.getMessage();
        }
    }

    /**
     * Loads a UML diagram from a specified file in JSON format.
     *
     * @param fileName the name of the file to load the diagram from
     * @return a confirmation message if the diagram is loaded successfully, or an error message if the load fails
     */
    public String loadDiagram(String fileName) {
        if (fileName.isEmpty()) {
            return "Usage: load <filename>";
        }
        try {
            SaveManager.loadFromJSON(fileName);
            return "UML diagram loaded from " + fileName;
        } catch (Exception e) {
            return "Error loading UML diagram: " + e.getMessage();
        }
    }
}
