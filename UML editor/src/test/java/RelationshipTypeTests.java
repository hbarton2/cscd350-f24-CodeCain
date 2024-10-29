import codecain.Relationship;
import codecain.RelationshipType;
import codecain.UMLClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;
//import static org.junit.jupiter.api.Assertions.*;

public class RelationshipTypeTests {

    /**
     * adds a bunch of classes to test this out
     *
     */
    @BeforeAll
    static void setUp(){
        UMLClass.addClass("Jim");
        UMLClass.addClass("John");
        UMLClass.addClass("Lee");
        UMLClass.addClass("Bill");
        UMLClass.addClass("Bob");
    }


    /**
     * tests that the relationship exists
     */
    @Test
    void testAddRelationship1(){
        System.out.println();
        Relationship.addRelationship("Jim", "John", RelationshipType.GENERALIZATION);
        assertTrue("generalization relationship should exist between John and Jim",
                Relationship.relationshipExists("Jim", "John", RelationshipType.GENERALIZATION));
        assertFalse("relationship is not aggregation",
                Relationship.relationshipExists("Jim", "John", RelationshipType.AGGREGATION));
        System.out.println();
    }

    /**
     * tests to make sure the removed relationship doesn't still exist
     */
    @Test
    void testRemoveRelationship1(){
        System.out.println("Test Remove Relationship");
        Relationship.addRelationship("Jim", "John", RelationshipType.GENERALIZATION);
        Relationship.removeRelationship("Jim", "John");
        assertFalse(Relationship.relationshipExists("Jim","John", RelationshipType.GENERALIZATION));
        System.out.println();

    }

    /**
     * tests to make sure the source of the relationship is correct
     */
    @Test
    void testRelationshipSource(){
        System.out.println("Test Relationship Source");
        Relationship.addRelationship("Jim","John", RelationshipType.GENERALIZATION);
        assertEquals("Jim",Relationship.relationshipList.getFirst().getSource());
        System.out.println();

    }



}
