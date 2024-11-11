package codecain.GraphicalUserInterface;

import codecain.BackendCode.Relationship;
import codecain.BackendCode.UMLClass;
import codecain.BackendCode.UMLClassInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * The {@code ClassBox} class represents a draggable, resizable box on a canvas
 * that visually displays a UML class's details, such as its name, fields, and methods.
 * It supports updating details dynamically and interacting with the user through drag-and-drop functionality.
 */
public class ClassBox extends JPanel {

    /** The text area displaying the details (fields and methods) of the UML class. */
    private final JTextArea detailsArea;

    /** The name of the UML class represented by this {@code ClassBox}. */
    private final String className;

    /**
     * hashmap with each relationship attached to the classBox
     */
    public HashMap<Relationship, Integer> attachedRelationshipIndices;

    /**
     * Constructs a {@code ClassBox} with the given class name and adds it to the specified canvas.
     * Initializes the layout, appearance, and drag-and-drop functionality.
     *
     * @param className the name of the UML class this box represents.
     * @param canvas    the {@code JPanel} canvas to which this box is added.
     */
    public ClassBox(String className, JPanel canvas) {
        attachedRelationshipIndices = new HashMap<>();

        this.className = className;

        setLayout(new BorderLayout());
        setBounds(50, 50, 200, 150); // Default size and position
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.LIGHT_GRAY);

        // Add the class name label to the top
        JLabel classNameLabel = new JLabel(className, SwingConstants.CENTER);
        add(classNameLabel, BorderLayout.NORTH);

        // Add the details area (fields and methods) to the center
        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        add(detailsArea, BorderLayout.CENTER);

        // Configure canvas layout and add this component
        canvas.setLayout(null);
        canvas.add(this);
        canvas.repaint();

        // Enable dragging functionality
        enableDragging(canvas);

        // Update the details of the class
        updateDetails();
    }

    /**
     * Enables drag-and-drop functionality for the {@code ClassBox}.
     * Ensures the box stays within the bounds of the canvas during movement.
     *
     * @param canvas the {@code JPanel} canvas containing this box.
     */
    private void enableDragging(JPanel canvas) {
        final Point initialClick = new Point();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                initialClick.setLocation(e.getPoint());
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                int xMove = e.getX() - initialClick.x;
                int yMove = e.getY() - initialClick.y;

                Point location = getLocation();
                int newX = location.x + xMove;
                int newY = location.y + yMove;

                int canvasWidth = canvas.getWidth();
                int canvasHeight = canvas.getHeight();
                int boxWidth = getWidth();
                int boxHeight = getHeight();
                
                newX = Math.max(0, Math.min(newX, canvasWidth - boxWidth));
                newY = Math.max(0, Math.min(newY, canvasHeight - boxHeight));

                setLocation(newX, newY);

                UMLClassInfo umlClassInfo = UMLClass.classMap.get(className);
                if (umlClassInfo != null) {
                    umlClassInfo.setX(newX);
                    umlClassInfo.setY(newY);
                }

                canvas.repaint();
            }
        });
    }

    /**
     * Updates the details (fields and methods) displayed in the {@code ClassBox}.
     * Retrieves information about the UML class from the backend and refreshes the display.
     * If the class information is unavailable, a default message is shown.
     */
    void updateDetails() {
        UMLClassInfo umlClassInfo = UMLClass.classMap.get(className);
        if (umlClassInfo == null) {
            detailsArea.setText("Class information not available.");
            return;
        }
        StringBuilder detailsText = new StringBuilder("Fields:\n");
        umlClassInfo.getFields().forEach(field ->
                detailsText.append(field.getFieldName()).append(": ").append(field.getFieldType()).append("\n"));
        detailsText.append("\nMethods:\n");
        umlClassInfo.getMethods().forEach(method -> {
            detailsText.append(method.getMethodName()).append("(");
            detailsText.append(method.getParameters().stream()
                    .map(param -> param.getParameterType() + " " + param.getParameterName())
                    .collect(Collectors.joining(", ")));
            detailsText.append(")\n");
        });
        detailsArea.setText(detailsText.toString());
    }

    /**
     * adds a relationship arrow position to the classBox
     * @param r the relationship that is being added
     */
    public void addRelationshipPoint(Relationship r){
        attachedRelationshipIndices.put(r, getLowestIndex());
        System.out.println("relationship added to class " + this.className);
    }

    /**
     * removes the relationship point from the map
     * @param r the relationship to remove
     */
    public void removeRelationshipPoint(Relationship r){
        attachedRelationshipIndices.remove(r);
        System.out.println("relationship removed from class " + this.className);
    }

    /**
     * gets the lowest available value in the hashmap
     * @return Integer
     */
    private Integer getLowestIndex(){
        int i = 0;
        for (; attachedRelationshipIndices.containsValue(i); i++);
        System.out.println(i);
        return i;
    }


}

