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
    
}
