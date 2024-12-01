package codecain.BackendCode.Model;

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
        Map<String, Object> umlData = new HashMap<>();
        umlData.put("classes", UMLClass.classMap);
        umlData.put("relationships", Relationship.relationshipList);

        objectMapper.writeValue(new File(filePath), umlData);
        System.out.println("UML diagram saved successfully to JSON at " + filePath);
    }

    public static void loadFromJSON(String filePath) throws IOException {
        validateJSONFile(filePath);
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
    }

    public static void saveToJSON(String filePath, Map<String, UMLClassInfo> classMap, List<Relationship> relationshipList) throws IOException {
        validateJSONFile(filePath);
        Map<String, Object> umlData = new HashMap<>();
        umlData.put("classes", classMap);
        umlData.put("relationships", relationshipList);

        objectMapper.writeValue(new File(filePath), umlData);
        System.out.println("UML diagram saved successfully to JSON at " + filePath);
    }


    /**
     * Validates that the file has a .json extension.
     *
     * @param filePath The path of the file to validate.
     * @throws IllegalArgumentException If the file does not have a .json extension.
     */
    private static void validateJSONFile(String filePath) {
        if (!filePath.endsWith(".json")) {
            throw new IllegalArgumentException("Invalid file type. Only .json files are allowed.");
        }
    }
}
