package codecain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SaveManager {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Saves the current UML diagram, including classes, fields, and methods, into a JSON file.
     *
     * @param filePath The path to save the JSON file.
     */
    public static void saveToJSON(String filePath) {
        try {
            // Create a map to hold all components of the UML diagram
            Map<String, Object> umlData = new HashMap<>();

            // Save all UML classes, fields, and methods
            umlData.put("classes", Class.classMap);
            umlData.put("fields", Fields.classFields);
            umlData.put("methods", Methods.classMethods);

            // Write the UML diagram data to the specified JSON file
            objectMapper.writeValue(new File(filePath), umlData);
            System.out.println("UML diagram saved successfully to JSON at " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving UML diagram to JSON: " + e.getMessage());
        }
    }

    /**
     * Loads the UML diagram from a JSON file and restores its state.
     *
     * @param filePath The path of the JSON file to load.
     */
    public static void loadFromJSON(String filePath) {
        try {
            // Read the UML data from the JSON file
            Map<String, Object> umlData = objectMapper.readValue(new File(filePath), Map.class);

            // Restore the classes, fields, and methods
            Class.classMap = (Map<String, Class>) umlData.get("classes");
            Fields.classFields = (Map<String, Map<String, String>>) umlData.get("fields");
            Methods.classMethods = (Map<String, Map<String, java.util.List<String>>>) umlData.get("methods");

            System.out.println("UML diagram loaded successfully from JSON.");
        } catch (IOException e) {
            System.err.println("Error loading UML diagram from JSON: " + e.getMessage());
        } catch (ClassCastException e) {
            System.err.println("Error casting data from JSON to the expected structure: " + e.getMessage());
        }
    }
}