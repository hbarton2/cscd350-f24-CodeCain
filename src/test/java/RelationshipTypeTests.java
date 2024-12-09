import codecain.BackendCode.Model.Relationship;
import codecain.BackendCode.Model.RelationshipType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.Assert.*;

/**
 * Test class for {@link RelationshipType}.
 * Provides unit tests for verifying the behavior of methods in the {@link RelationshipType} class.
 */
public class RelationshipTypeTests {

    /**
     * Tests the {@code toString()} method of {@link RelationshipType}.
     * Ensures that the string representation matches the expected values for each type.
     */
    @Test
    public void testToString(){
        Assertions.assertEquals("Generalization", RelationshipType.GENERALIZATION.toString());
        Assertions.assertEquals("Composition", RelationshipType.COMPOSITION.toString());
        Assertions.assertEquals("Realization", RelationshipType.REALIZATION.toString());
        Assertions.assertEquals("Aggregation", RelationshipType.AGGREGATION.toString());
    }

    /**
     * Tests the {@code fromString()} method with valid input strings.
     * Verifies that the method returns the correct {@link RelationshipType} for valid inputs.
     */
    @Test
    public void testFromValidString(){
        Assertions.assertEquals(RelationshipType.GENERALIZATION, RelationshipType.fromString("Generalization"));
        Assertions.assertEquals(RelationshipType.COMPOSITION, RelationshipType.fromString("Composition"));
        Assertions.assertEquals(RelationshipType.REALIZATION, RelationshipType.fromString("Realization"));
        Assertions.assertEquals(RelationshipType.AGGREGATION, RelationshipType.fromString("Aggregation"));
    }

    /**
     * Tests the {@code fromString()} method with invalid input strings.
     * Verifies that the method returns {@code null} for invalid, empty, or null inputs.
     */
    @Test
    public void testFromInvalidString(){
        Assertions.assertNull(RelationshipType.fromString("Invalid"), "Expected null for invalid input");
        Assertions.assertNull(RelationshipType.fromString(""), "Expected null for empty input");
        Assertions.assertNull(RelationshipType.fromString(null), "Expected null for null input");
    }

    /**
     * Tests the {@code typeExists()} method with valid input strings.
     * Verifies that the method correctly identifies the existence of valid types.
     */
    @Test
    public void testTypeValidExists(){
        Assertions.assertTrue(RelationshipType.typeExists("Generalization"));
        Assertions.assertTrue(RelationshipType.typeExists("Composition"));
        Assertions.assertTrue(RelationshipType.typeExists("Realization"));
        Assertions.assertTrue(RelationshipType.typeExists("Aggregation"));
    }

    /**
     * Tests the {@code typeExists()} method with invalid input strings.
     * Verifies that the method correctly identifies invalid, empty, or null types as non-existent.
     */
    @Test
    public void testTypeInvalidExists(){
        Assertions.assertFalse(RelationshipType.typeExists("Invalid"));
        Assertions.assertFalse(RelationshipType.typeExists(""));
        Assertions.assertFalse(RelationshipType.typeExists(null));
    }

    /**
     * Tests the {@code fromNode()} method with valid JSON nodes.
     * Verifies that the method correctly parses the relationship type from valid JSON input.
     *
     * @throws Exception if an error occurs during JSON parsing
     */
    @Test
    public void testFromValidNode() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"name\": \"GENERALIZATION\"}");
        Assertions.assertEquals(RelationshipType.GENERALIZATION, RelationshipType.fromNode(node));

        JsonNode node2 = mapper.readTree("{\"name\": \"composition\"}");
        Assertions.assertEquals(RelationshipType.COMPOSITION, RelationshipType.fromNode(node2));
    }

    /**
     * Tests the {@code fromNode()} method with invalid JSON nodes.
     * Verifies that the method throws an {@link IllegalArgumentException} for invalid JSON input.
     *
     * @throws Exception if an error occurs during JSON parsing
     */
    @Test
    public void testFromInvalidNode() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{}");
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> RelationshipType.fromNode(node));

        Assertions.assertEquals("Invalid input: JSON node must have a 'name' property.", exception.getMessage());
        JsonNode node2 = mapper.readTree("{\"name\": \"invalid\"}");
        Exception exception2 = Assertions.assertThrows(IllegalArgumentException.class, () -> RelationshipType.fromNode(node2));
        Assertions.assertTrue(exception2.getMessage().contains("Invalid relationship type"));
    }

    /**
     * Tests the {@code getName()} method of {@link RelationshipType}.
     * Verifies that the method returns the correct name for each relationship type.
     */
    @Test
    public void testGetName(){
        Assertions.assertEquals("GENERALIZATION", RelationshipType.GENERALIZATION.getName());
        Assertions.assertEquals("COMPOSITION", RelationshipType.COMPOSITION.getName());
        Assertions.assertEquals("REALIZATION", RelationshipType.REALIZATION.getName());
        Assertions.assertEquals("AGGREGATION", RelationshipType.AGGREGATION.getName());
    }

    /**
     * Tests the {@code getArrowString()} method of {@link RelationshipType}.
     * Verifies that the method returns the correct arrow representation for each relationship type.
     */
    @Test
    public void testGetArrowString(){
        Assertions.assertEquals(" -----|> ", RelationshipType.GENERALIZATION.getArrowString());
        Assertions.assertEquals(" <*>---- ", RelationshipType.COMPOSITION.getArrowString());
        Assertions.assertEquals(" - - -|> ", RelationshipType.REALIZATION.getArrowString());
        Assertions.assertEquals(" <>----- ", RelationshipType.AGGREGATION.getArrowString());
    }
}
