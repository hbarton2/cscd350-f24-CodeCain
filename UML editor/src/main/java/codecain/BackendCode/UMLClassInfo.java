package codecain.BackendCode;

import java.util.ArrayList;
import java.util.List;

/**

 * The UMLClassInfo class represents the details of a UML class, including its name, fields, methods, and position.
 */
public class UMLClassInfo {

    /**
     * The name of the UML class.
     */
    private String className;

    /**
     * A list of fields (UMLFieldsInfo) in the UML class.
     */
    private List<UMLFieldInfo> fields;

    /**
     * A list of methods (UMLMethodInfo) in the UML class.
     */
    private List<UMLMethodInfo> methods;

    /**
     * The x-coordinate of the class box position.
     */
    private int x;

    /**
     * The y-coordinate of the class box position.
     */
    private int y;

    /**
     * Default constructor for UMLClassInfo. Required for JSON deserialization.
     */
    public UMLClassInfo() {
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

    /**
     * Constructs a UMLClassInfo object with the specified class name.
     * Initializes empty lists for fields and methods and sets default position.
     *
     * @param className the name of the UML class
     */
    public UMLClassInfo(String className) {
        this.className = className;
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.x = 0;
        this.y = 0;
    }

    /**
     * Gets the name of the UML class.
     *
     * @return the name of the class
     */

    public String getClassName() {
        return this.className;
    }

    /**
     * Sets the name of the UML class.
     *
     * @param className the new name of the class
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Gets the list of fields in the UML class.
     *
     * @return a list of UMLFieldsInfo objects representing the fields
     */
    public List<UMLFieldInfo> getFields() {
        return this.fields;
    }

    /**
     * Gets the list of methods in the UML class.
     *
     * @return a list of UMLMethodInfo objects representing the methods
     */
    public List<UMLMethodInfo> getMethods() {
        return this.methods;
    }


     * Gets the x-coordinate of the class box position.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the class box position.
     *
     * @param x the x-coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate of the class box position.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the class box position.
     *
     * @param y the y-coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Retrieves a method by its name from the list of methods in the UML class.
     *
     * @param methodName the name of the method to retrieve
     * @return the UMLMethodInfo object representing the method if found, or null if not found
     */
    public UMLMethodInfo getMethodByName(String methodName) {
        for (UMLMethodInfo method : methods) {
            if (method.getMethodName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}
