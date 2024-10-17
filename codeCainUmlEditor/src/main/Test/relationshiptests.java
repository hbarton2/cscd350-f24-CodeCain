import org.junit.Before;
import org.junit.jupiter.api.*;

import static org.junit.Assert.*;

public class relationshiptests {

    @Nested
    class addRelationshipTests {

        @BeforeAll
        static void setup(){
            Class.addClass("jim");
            Class.addClass("bob");
        }

        @Test
        void test1(){
            try {
                Relationship.addRelationship("jim", "bob");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Assertions.assertTrue(Relationship.relationshipExists("jim"));
            Assertions.assertTrue(Relationship.relationshipExists("jim", "bob"));
        }

        @Test
        void test2(){

                Relationship.addRelationship("jim", "george");

        }
    }


//    /**
//     * tests relationships with 1 relationship inside
//     */
//
//
//    @Nested
//    class relationshipExistsTestsCase1{
//
//        @BeforeAll
//        static void setUp(){
//
//            try {
//                Relationship.addRelationship("bob","dave");
//                Relationship.addRelationship("tom","dave");
//                Relationship.addRelationship("sam","james");
//                Relationship.addRelationship("bob","tom");
//                Relationship.addRelationship("james","dave");
//            } catch (Exception e) {
//                System.out.println("relationships not added");
//                throw new RuntimeException(e);
//            }
//        }
//
//        /**
//         * tests bob and dave together
//         */
//        @Test
//        void test1(){
//            Assertions.assertTrue(Relationship.relationshipExists("bob","dave"));
//        }
//
//        /**
//         * Tests bob and dave separately
//         */
//        @Test
//        void test2(){
//            Assertions.assertTrue(Relationship.relationshipExists("bob"), "bob not found");
//            Assertions.assertTrue(Relationship.relationshipExists("dave"), "dave not found");
//        }
//
//        /**
//         * tests with one class name that shouldnt exist
//         * should be false
//         */
//        @Test
//        void test3(){
//            Assertions.assertFalse(Relationship.relationshipExists("jim"));
//        }
//
//        /**
//         * tests with one class that does exist, and one that doesn't
//         */
//        @Test
//        void test4(){
//            Assertions.assertFalse(Relationship.relationshipExists("jim", "dave"),
//                    "relationship should not exist");
//        }
//    }
//
//
//    @Nested
//    public class relationshipAddTests{
//
//        /**
//         * tests to make sure the add method works
//         */
//        @Test
//        void test1(){
//            try {
//                Relationship.addRelationship("Jim", "Tom");
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//
//            Assertions.assertFalse(Relationship.relationshipList.isEmpty(),
//                    "relationship list is empty");
//        }
//    }
//
//    @Nested
//    class relationshipRemoveTests{
//
//
//        /**
//         * tests to make sure the remove method works
//         */
//        @Test
//        void test1() throws Exception {
//            try {
//                Relationship.relationshipList.clear();
//
//                Relationship.addRelationship("jim", "tom");
//                Relationship.removeRelationship("jim", "tom");
//
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//
//            Assertions.assertTrue(Relationship.relationshipList.isEmpty(),
//                    Relationship.listToString());
//        }
//    }
//
//    @Nested
//    class relationshipRenameTests{
//
//        @BeforeAll
//        static void setUp(){
//            try {
//                Relationship.addRelationship("jim", "tom");
//                Relationship.addRelationship("jim", "john");
//                Relationship.addRelationship("sam", "bob");
//                Relationship.addRelationship("lynn", "george");
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        @Test
//        void test1() throws Exception {
//            //System.out.println(Relationship.listToString());
//
//            try {
//                Relationship.renameClassInRelationships("jim", "james");
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            Assertions.assertFalse(Relationship.relationshipExists("jim"),
//                    "jim should not exist");
//            Assertions.assertTrue(Relationship.relationshipExists("james"),
//                    "james does not exist");
//            Assertions.assertTrue(Relationship.relationshipExists("james","tom"),
//                    "Relationship between James and Tom exists");
//
//            // System.out.println("\n\n" + Relationship.listToString());
//
//        }
//
//
//    }
//
//    @Nested
//    class removeAttachedRelationshipsTests{
//
//        /**
//         * adding a bunch of relationships
//         */
//        @BeforeAll
//        static void setup() throws Exception {
//           Relationship.addRelationship("bob","jim");
//           Relationship.addRelationship("george", "bob");
//           Relationship.addRelationship("jim","george");
//           Relationship.addRelationship("bob", "james");
//           Relationship.addRelationship("bill", "james");
//        }
//
//        /**
//         * tests to see if jim still exists after removing attached relationships
//         */
//        @Test
//        void test1() throws Exception {
//            Assertions.assertTrue(Relationship.relationshipExists("jim"),
//                    "setup failed");
//            Relationship.removeAttachedRelationships("jim");
//
//            String errormsglist = "Relationship list \n=============================" +
//                    Relationship.listToString();
//
//            Assertions.assertFalse(Relationship.relationshipExists("jim"),
//                    "class still has relationships" + errormsglist);
//
//            //System.out.println(Relationship.listToString());
//
//        }
//
//
//        @Test
//        void test2() throws Exception {
//            Relationship.removeAttachedRelationships("bob");
//            Assertions.assertFalse(Relationship.relationshipExists("bob"));
//
//            System.out.println(Relationship.listToString());
//        }
//
//    }

}
