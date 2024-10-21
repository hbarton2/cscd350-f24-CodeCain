/*import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class SaveManager {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Method to save UML diagram to a JSON file
    public static void saveToJSON(String filePath, Object umlObject) {
        try {
            objectMapper.writeValue(new File(filePath), umlObject);
            System.out.println("UML diagram saved successfully to JSON at " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving UML diagram to JSON: " + e.getMessage());
        }
    }

    // Method to load UML diagram from a JSON file
    public static <T> T loadFromJSON(String filePath, Class<T> umlClass) {
        try {
            return objectMapper.readValue(new File(filePath), umlClass);
        } catch (IOException e) {
            System.err.println("Error loading UML diagram from JSON: " + e.getMessage());
            return null;
        }
    }
}
*/