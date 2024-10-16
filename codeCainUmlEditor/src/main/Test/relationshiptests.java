import org.junit.Before;
import org.junit.jupiter.api.*;

import static org.junit.Assert.*;

public class relationshiptests {

    /**
     * tests relationships with 1 relationship inside
     */

    @Nested
    class relationshipExistsTestsCase1{

        @BeforeAll
        static void setUp(){


            try {
                Relationship.addRelationship("bob","dave");
                Relationship.addRelationship("tom","dave");
                Relationship.addRelationship("sam","james");
                Relationship.addRelationship("bob","tom");
                Relationship.addRelationship("james","dave");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * tests bob and dave together
         */
        @Test
        void test1(){
            Assertions.assertTrue(Relationship.relationshipExists("bob","dave"));
        }

        /**
         * Tests bob and dave separately
         */
        @Test
        void test2(){
            Assertions.assertTrue(Relationship.relationshipExists("bob"), "bob not found");
            Assertions.assertTrue(Relationship.relationshipExists("dave"), "dave not found");
        }

        /**
         * tests with one class name that shouldnt exist
         * should be false
         */
        @Test
        void test3(){
            Assertions.assertFalse(Relationship.relationshipExists("jim"));
        }

        /**
         * tests with one class that does exist, and one that doesn't
         */
        @Test
        void test4(){
            Assertions.assertFalse(Relationship.relationshipExists("jim", "dave"),
                    "relationship should not exist");
        }
    }


    @Nested
    public class relationshipAddTests{

        /**
         * tests to make sure the add method works
         */
        @Test
        void test1(){
            try {
                Relationship.addRelationship("Jim", "Tom");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Assertions.assertFalse(Relationship.relationshipList.isEmpty(),
                    "relationship list is empty");
        }
    }

    @Nested
    class relationshipRemoveTests{


        /**
         * tests to make sure the remove method works
         */
        @Test
        void test1() throws Exception {
            try {
                Relationship.relationshipList.clear();

                Relationship.addRelationship("Jim", "Tom");
                Relationship.removeRelationship("Jim", "Tom");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Assertions.assertTrue(Relationship.relationshipList.isEmpty(),
                    Relationship.listToString());
        }
    }

    @Nested
    class relationshipRenameTests{

        @BeforeAll
        static void setUp(){
            try {
                Relationship.addRelationship("Jim", "Tom");
                Relationship.addRelationship("Jim", "John");
                Relationship.addRelationship("Sam", "Bob");
                Relationship.addRelationship("Lynn", "George");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        void test1() throws Exception {
            //System.out.println(Relationship.listToString());

            try {
                Relationship.renameClassInRelationships("Jim", "James");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Assertions.assertFalse(Relationship.relationshipExists("Jim"),
                    "Jim should not exist");
            Assertions.assertTrue(Relationship.relationshipExists("James"),
                    "James does not exist");
            Assertions.assertTrue(Relationship.relationshipExists("James","Tom"),
                    "Relationship between James and Tom exists");

            // System.out.println("\n\n" + Relationship.listToString());

        }


    }


}
