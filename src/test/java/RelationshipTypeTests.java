import codecain.BackendCode.Model.Relationship;
import codecain.BackendCode.Model.RelationshipType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.Assert.*;

public class RelationshipTypeTests {

    @Test
    public void testToString(){
        Assertions.assertEquals("Generalization", RelationshipType.GENERALIZATION.toString());
        Assertions.assertEquals("Composition", RelationshipType.COMPOSITION.toString());
        Assertions.assertEquals("Realization", RelationshipType.REALIZATION.toString());
        Assertions.assertEquals("Aggregation", RelationshipType.AGGREGATION.toString());
    }
    @Test
    public void testFromValidString(){
        Assertions.assertEquals(RelationshipType.GENERALIZATION, RelationshipType.fromString("Generalization"));
        Assertions.assertEquals(RelationshipType.COMPOSITION, RelationshipType.fromString("Composition"));
        Assertions.assertEquals(RelationshipType.REALIZATION, RelationshipType.fromString("Realization"));
        Assertions.assertEquals(RelationshipType.AGGREGATION, RelationshipType.fromString("Aggregation"));
    }
    @Test
    public void testFromInvalidString(){
        Assertions.assertNull(RelationshipType.fromString("Invalid"), "Expected null for invalid input");
        Assertions.assertNull(RelationshipType.fromString(""), "Expected null for empty input");
        Assertions.assertNull(RelationshipType.fromString(null), "Expected null for null input");
    }
    @Test
    public void testTypeValidExists(){
        Assertions.assertTrue(RelationshipType.typeExists("Generalization"));
        Assertions.assertTrue(RelationshipType.typeExists("Composition"));
        Assertions.assertTrue(RelationshipType.typeExists("Realization"));
        Assertions.assertTrue(RelationshipType.typeExists("Aggregation"));
    }
    @Test
    public void testTypeInvalidExists(){
        Assertions.assertFalse(RelationshipType.typeExists("Invalid"));
        Assertions.assertFalse(RelationshipType.typeExists(""));
        Assertions.assertFalse(RelationshipType.typeExists(null));
    }

}
