import codecain.BackendCode.Model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SaveManagerTests {
    private final String testPath = "testUml.json";
    private Map<String, UMLClassInfo> testClassMap;
    private List<Relationship> testRelationships;

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
        Relationship.addRelationship("Dog","Cat", RelationshipType.AGGREGATION);
        Relationship.addRelationship("Wolf","Tiger", RelationshipType.COMPOSITION);
        Relationship.addRelationship("Shark","Whale", RelationshipType.REALIZATION);
        Relationship.addRelationship("Eagle","Pigeon", RelationshipType.GENERALIZATION);

        Relationship.relationshipList = new ArrayList<>(Relationship.relationshipList);
    }
    @AfterEach
    void tearDown() {
        File file = new File(testPath);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testSaveToJSON_Valid() throws IOException {
        SaveManager.saveToJSON(testPath);
        File file = new File(testPath);
        assertTrue(file.exists(), "JSON file should exist");
    }
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
    @Test
    void testSaveTOJsonWithRelationshipTypes() throws IOException {
        SaveManager.saveToJSON(testPath);

        File file = new File(testPath);
        assertTrue(file.exists(), "JSON file should exist");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> umlData = mapper.readValue(file, Map.class);

        List<Map<String, Object>> relationshipsData = (List<Map<String, Object>>) umlData.get("relationships");
        assertNotNull(relationshipsData, "Relationships data should not be null");
        assertEquals(4, relationshipsData.size(), "Relationships data should contain four relationship");

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



}
