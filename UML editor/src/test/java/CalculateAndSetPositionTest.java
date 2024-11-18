import javafx.application.Platform;
import codecain.GraphicalUserInterface.ClassNode;
import codecain.BackendCode.*;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class CalculateAndSetPositionTest {

    @BeforeClass
    public static void initToolkit() {
        // Initialize JavaFX runtime for testing
        Platform.startup(() -> {});
    }

    /**
     * Helper method: Simulates calculateAndSetPosition logic.
     */
    private void calculateAndSetPosition(ClassNode classNode, UMLClassInfo classInfo, Pane container) {
        double x = 0, y = 0;
        double padding = 20; 
        double nodeWidth = classNode.getPrefWidth() > 0 ? classNode.getPrefWidth() : 200; // Default width
        double nodeHeight = classNode.getPrefHeight() > 0 ? classNode.getPrefHeight() : 300; // Default height
        double containerWidth = container.getWidth() > 0 ? container.getWidth() : 800; // Default container width

        boolean positionFound = false;
        while (!positionFound) {
            positionFound = true; 
            for (Node node : container.getChildren()) {
                if (node instanceof ClassNode) {
                    double otherX = node.getLayoutX();
                    double otherY = node.getLayoutY();
                    if (Math.abs(x - otherX) < nodeWidth + padding && Math.abs(y - otherY) < nodeHeight + padding) {
                        positionFound = false;
                        x += nodeWidth + padding; 
                        if (x + nodeWidth > containerWidth) {
                            x = 0; 
                            y += nodeHeight + padding; 
                        }
                        break;
                    }
                }
            }
        }

        classNode.setLayoutX(x);
        classNode.setLayoutY(y);

        classInfo.setX((int) x);
        classInfo.setY((int) y);
    }

    @Test
    public void testFirstNodePosition() {
        Pane container = new Pane();
        UMLClassInfo classInfo = new UMLClassInfo("TestClass");
        ClassNode classNode = new ClassNode(classInfo);

        calculateAndSetPosition(classNode, classInfo, container);

        assertEquals(0, classInfo.getX());
        assertEquals(0, classInfo.getY());
        assertEquals(0, classNode.getLayoutX(), 0.01);
        assertEquals(0, classNode.getLayoutY(), 0.01);
    }

    @Test
    public void testNodeNextToAnother() {
        Pane container = new Pane();
        UMLClassInfo firstClassInfo = new UMLClassInfo("FirstClass");
        ClassNode firstNode = new ClassNode(firstClassInfo);
        firstNode.setPrefWidth(200);
        firstNode.setPrefHeight(300);
        firstNode.setLayoutX(0);
        firstNode.setLayoutY(0);

        UMLClassInfo secondClassInfo = new UMLClassInfo("SecondClass");
        ClassNode secondNode = new ClassNode(secondClassInfo);
        secondNode.setPrefWidth(200);
        secondNode.setPrefHeight(300);

        container.getChildren().add(firstNode);

        calculateAndSetPosition(secondNode, secondClassInfo, container);

        assertEquals(220, secondClassInfo.getX());
        assertEquals(0, secondClassInfo.getY());
        assertEquals(220, secondNode.getLayoutX(), 0.01);
        assertEquals(0, secondNode.getLayoutY(), 0.01);
    }

    @Test
    public void testPreventOverlap() {
        Pane container = new Pane();

        UMLClassInfo existingClassInfo = new UMLClassInfo("ExistingClass");
        ClassNode existingNode = new ClassNode(existingClassInfo);
        existingNode.setPrefWidth(200);
        existingNode.setPrefHeight(300);
        existingNode.setLayoutX(0);
        existingNode.setLayoutY(0);
        container.getChildren().add(existingNode);

        UMLClassInfo newClassInfo = new UMLClassInfo("NewClass");
        ClassNode newNode = new ClassNode(newClassInfo);
        newNode.setPrefWidth(200);
        newNode.setPrefHeight(300);

        calculateAndSetPosition(newNode, newClassInfo, container);

        assertEquals(220, newClassInfo.getX());
        assertEquals(0, newClassInfo.getY());
    }    
}
