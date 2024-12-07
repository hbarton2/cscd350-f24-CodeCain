import codecain.BackendCode.Model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the SaveManager class, covering saving and loading UML diagrams,
 * validating file extensions, and ensuring data integrity.
 */
public class SaveManagerTests {

    private final String testPath = "testUml.json";
    private Map<String, UMLClassInfo> testClassMap;

    /**
     * Sets up the test environment before each test.
     * Initializes a set of UMLClassInfo objects and relationships.
     */
    @BeforeEach
    void setUp() {
        UMLClassInfo classInfo = new UMLClassInfo("Dog");
        UMLClassInfo classInfo2 = new UMLClassInfo("Cat");
        UMLClassInfo classInfo3 = new UMLClassInfo("Wolf");
        UMLClassInfo classInfo4 = new UMLClassInfo("Tiger");
        UMLClassInfo classInfo5 = new UMLClassInfo("Shark");
        UMLClassInfo classInfo6 = new UMLClassInfo("Whale");
        UMLClassInfo classInfo7 = new UMLClassInfo("Eagle");
        UMLClassInfo classInfo8 = new UMLClassInfo("Pigeon");

        testClassMap = new HashMap<>();
        testClassMap.put("Dog", classInfo);
        testClassMap.put("Cat", classInfo2);
        testClassMap.put("Wolf", classInfo3);
        testClassMap.put("Tiger", classInfo4);
        testClassMap.put("Shark", classInfo5);
        testClassMap.put("Whale", classInfo6);
        testClassMap.put("Eagle", classInfo7);
        testClassMap.put("Pigeon", classInfo8);
        UMLClass.classMap = new HashMap<>(testClassMap);

        Relationship.relationshipList.clear();
        Relationship.addRelationship("Dog", "Cat", RelationshipType.AGGREGATION);
        Relationship.addRelationship("Wolf", "Tiger", RelationshipType.COMPOSITION);
        Relationship.addRelationship("Shark", "Whale", RelationshipType.REALIZATION);
        Relationship.addRelationship("Eagle", "Pigeon", RelationshipType.GENERALIZATION);
    }

    /**
     * Cleans up the test environment after each test.
     * Deletes the test JSON file created during the test execution.
     */
    @AfterEach
    void tearDown() {
        File file = new File(testPath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Verifies that saving UML data to a valid JSON file works as expected.
     *
     * @throws IOException if an error occurs during file operations.
     */
    @Test
    void testSaveToJSON_Valid() throws IOException {
        SaveManager.saveToJSON(testPath);
        File file = new File(testPath);
        assertTrue(file.exists(), "JSON file should exist");
    }

    /**
     * Tests saving UML data when both class map and relationship list are empty.
     * Ensures that an empty JSON structure is created.
     *
     * @throws IOException if an error occurs during file operations.
     */
    @Test
    void testSaveToJSON_Invalid() throws IOException {
        UMLClass.classMap.clear();
        Relationship.relationshipList.clear();

        SaveManager.saveToJSON(testPath);
        File file = new File(testPath);
        assertTrue(file.exists(), "JSON file should exist");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> uml = mapper.readValue(file, Map.class);
        assertTrue(((Map<?, ?>) uml.get("classes")).isEmpty());
        assertTrue(((List<?>) uml.get("relationships")).isEmpty());
    }

    /**
     * Verifies that all relationship types are saved correctly in the JSON file.
     *
     * @throws IOException if an error occurs during file operations.
     */
    @Test
    void testSaveTOJsonWithRelationshipTypes() throws IOException {
        SaveManager.saveToJSON(testPath);

        File file = new File(testPath);
        assertTrue(file.exists(), "JSON file should exist");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> umlData = mapper.readValue(file, Map.class);

        List<Map<String, Object>> relationshipsData = (List<Map<String, Object>>) umlData.get("relationships");
        assertNotNull(relationshipsData, "Relationships data should not be null");
        assertEquals(4, relationshipsData.size(), "Relationships data should contain four relationships");

        Set<String> type = new HashSet<>();
        for (Map<String, Object> relationship : relationshipsData) {
            Map<String, Object> typeData = (Map<String, Object>) relationship.get("type");
            type.add((String) typeData.get("name"));
        }

        assertTrue(type.contains("AGGREGATION"));
        assertTrue(type.contains("COMPOSITION"));
        assertTrue(type.contains("REALIZATION"));
        assertTrue(type.contains("GENERALIZATION"));
    }

    /**
     * Verifies that loading a valid JSON file restores UML data correctly.
     *
     * @throws IOException if an error occurs during file operations.
     */
    @Test
    void testLoadFromJSON_Valid() throws IOException {
        SaveManager.saveToJSON(testPath);
        UMLClass.classMap.clear();
        Relationship.relationshipList.clear();

        SaveManager.loadFromJSON(testPath);

        assertEquals(8, UMLClass.classMap.size(), "Class Map size should be 8");
        assertEquals(4, Relationship.relationshipList.size(), "Relationship List size should be 4");
    }

    /**
     * Ensures that attempting to load a nonexistent JSON file throws an exception.
     */
    @Test
    void testLoadFromJSON_Invalid() {
        assertThrows(IOException.class, () -> SaveManager.loadFromJSON(testPath));
    }

    /**
     * Verifies that saving to a file with an invalid extension throws an exception.
     */
    @Test
    void testSaveToJSON_InvalidExtension() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                SaveManager.saveToJSON("testUML.txt"), "Should throw Exception for invalid file");
        assertEquals("Invalid file type. Only .json files are allowed.", exception.getMessage());
    }

    /**
     * Verifies that loading from a file with an invalid extension throws an exception.
     */
    @Test
    void testSaveToJSON_InvalidExtension2() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                SaveManager.loadFromJSON("testUML.txt"), "Should throw Exception for invalid file");
        assertEquals("Invalid file type. Only .json files are allowed.", exception.getMessage());
    }

    /**
     * Tests saving custom UML class data and relationships to a JSON file.
     *
     * @throws IOException if an error occurs during file operations.
     */
    @Test
    void testSaveToJSON_CustomData() throws IOException {
        Map<String, UMLClassInfo> classMap = new HashMap<>();
        classMap.put("Dog", new UMLClassInfo("Dog"));
        classMap.put("Cat", new UMLClassInfo("Cat"));

        Relationship.relationshipList.clear();
        Relationship.addRelationship("Dog", "Cat", RelationshipType.AGGREGATION);

        SaveManager.saveToJSON(testPath, classMap, Relationship.relationshipList);
        File file = new File(testPath);
        assertTrue(file.exists(), "JSON file should exist");
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> uml = mapper.readValue(file, Map.class);
        assertEquals(2, ((Map<?, ?>) uml.get("classes")).size(), "Class Map size should be 2");
        assertEquals(1, ((List<?>) uml.get("relationships")).size(), "Relationship List size should be 1");
    }
}
