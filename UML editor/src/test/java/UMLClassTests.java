import static org.junit.jupiter.api.Assertions.*;

import codecain.UMLClass;
import codecain.UMLClassInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import codecain.Relationship;
import org.junit.jupiter.api.AfterEach;
import java.util.ArrayList;
public class UMLClassTests {
    @BeforeEach
    public void setUp(){
        UMLClass.classMap.clear();
    }
    @AfterEach
    public void tearDown(){
        UMLClass.classMap.clear();
    }

    @Test
    public void testAddClass(){
        UMLClass.addClass("DOG");
        assertTrue(UMLClass.classMap.containsKey("DOG"));
    }
    @Test
    public void testAddDuplicatedClass(){
        UMLClass.addClass("DOG");
        UMLClass.addClass("DOG");
        assertEquals(1, UMLClass.classMap.size(),"Should prompt you cant add a class that already exists");
    }

    @Test
    public void testRemoveClass(){
        UMLClass.addClass("DOG");
        UMLClass.removeClass("DOG");
        assertFalse(UMLClass.classMap.containsKey("DOG"));
    }
    @Test
    public void testRemoveNonExistingClass(){
        UMLClass.removeClass("DOG");
        assertFalse(UMLClass.classMap.containsKey("DOG"), "Should prompt you cannot delete a class that isn't there");
    }
    @Test
    public void testRenamingClass(){
        UMLClass.addClass("DOG");
        UMLClass.renameClass("DOG", "CAT");
        assertFalse(UMLClass.classMap.containsKey("DOG"));
        assertTrue(UMLClass.classMap.containsKey("CAT"));
    }
    @Test
    public void testRenamingThatAlreadyExists(){
        UMLClass.addClass("DOG");
        UMLClass.addClass("CAT");
        UMLClass.renameClass("DOG", "CAT");
        //These should both be true because the renaming should fail
        assertTrue(UMLClass.classMap.containsKey("DOG"));
        assertTrue(UMLClass.classMap.containsKey("CAT"));
    }
    @Test
    public void testRenamingNonExistingClass(){
        UMLClass.renameClass("DOG", "CAT");
        assertFalse(UMLClass.classMap.containsKey("DOG"));
        assertFalse(UMLClass.classMap.containsKey("CAT"));
    }

}
