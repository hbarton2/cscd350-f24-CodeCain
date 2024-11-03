package codecain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The SaveManager class handles saving and loading UML diagrams in JSON format.
 * It uses the Jackson library to serialize and deserialize UML data structures.
 */
public class SaveManager {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    /**
     * Saves the current UML diagram, including classes and relationships, into a JSON file.
     *
     * @param filePath The path to save the JSON file.
     * @throws IOException If an I/O error occurs while saving the file.
     */
    public static void saveToJSON(String filePath) throws IOException {
        try {
            Map<String, Object> umlData = new HashMap<>();
            umlData.put("classes", UMLClass.classMap);
            umlData.put("relationships", Relationship.relationshipList);

            objectMapper.writeValue(new File(filePath), umlData);
            System.out.println("UML diagram saved successfully to JSON at " + filePath);
        } catch (IOException e) {
            String errorCatch = "Error saving UML diagram to JSON: " + e.getMessage();
            System.err.println(errorCatch);
            throw new IOException(errorCatch);
        }
    }

    /**
     * Loads the UML diagram from a JSON file and restores its state.
     *
     * @param filePath The path of the JSON file to load.
     * @throws IOException If an I/O error occurs while loading the file.
     */
    public static void loadFromJSON(String filePath) throws IOException {
        try {
            Map<String, Object> umlData = objectMapper.readValue(new File(filePath), Map.class);

            Map<String, UMLClassInfo> classes = objectMapper.convertValue(
                    umlData.get("classes"), new TypeReference<Map<String, UMLClassInfo>>() {}
            );
            UMLClass.classMap = classes;

            List<Relationship> relationships = objectMapper.convertValue(
                    umlData.get("relationships"), new TypeReference<List<Relationship>>() {}
            );
            Relationship.relationshipList = new ArrayList<>(relationships);

            System.out.println("UML diagram loaded successfully from JSON.");
        } catch (IOException e) {
            String errorCatch = "Error loading UML diagram from JSON: " + e.getMessage();
            System.err.println(errorCatch);
            throw new IOException(errorCatch);
        } catch (ClassCastException e) {
            String errorCatch = "Error casting data from JSON to the expected structure: " + e.getMessage();
            System.err.println(errorCatch);
            throw new IOException(errorCatch);
        }
    }
}
