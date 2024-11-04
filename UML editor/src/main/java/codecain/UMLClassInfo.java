package codecain;

import java.util.ArrayList;
import java.util.List;

/**
 * The UMLClassInfo class represents the details of a UML class, including its name, fields, and methods.
 * It provides methods to retrieve and manage the fields and methods of the class.
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
     * Default constructor for UMLClassInfo. Required for JSON deserialization.
     * Initializes an instance of UMLClassInfo without any parameters.
     */
    public UMLClassInfo() {
    }
    /**
     * Constructs a UMLClassInfo object with the specified class name.
     * Initializes empty lists for fields and methods.
     *
     * @param className the name of the UML class
     */
    public UMLClassInfo(String  className) {
        this.className = className;
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
    }


    /**
     * Gets the name of the UML class.
     *
     * @return the name of the class
     */
    public Object getClassName() {
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

    /**
     * Retrieves a method by its name from the list of methods in the UML class.
     *
     * @param methodName the name of the method to retrieve
     * @return the UMLMethodInfo object representing the method if found, or null if not found
     */
    public UMLMethodInfo getMethodByName(Object methodName) {
        for (UMLMethodInfo method : methods) {
            if (method.getMethodName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

}
