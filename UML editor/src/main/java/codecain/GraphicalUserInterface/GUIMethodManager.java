package codecain.GraphicalUserInterface;

import codecain.BackendCode.UMLMethods;
import codecain.BackendCode.UMLParameterInfo;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code GUIMethodManager} class handles the management of methods within UML classes.
 * It provides methods to add, delete, and rename methods, as well as manage parameters
 * within methods. The class updates both the backend and the graphical representation of
 * the class diagram as necessary.
 */
public class GUIMethodManager {

    /** The manager responsible for handling operations related to classes. */
    private final GUIClassManager classManager;

    /**
     * Constructs a {@code GUIMethodManager} with the specified {@code GUIClassManager}.
     *
     * @param classManager the manager responsible for class operations.
     */
    public GUIMethodManager(GUIClassManager classManager) {
        this.classManager = classManager;
    }

    /**
     * Adds a new method to a specified class.
     * Prompts the user for the class name, method name, and parameters. Updates the backend and
     * the graphical representation if the operation succeeds. Displays a confirmation or error message.
     */
    public void addMethod() {
        String className = JOptionPane.showInputDialog("Enter the class name to add a method:");
        String methodName = JOptionPane.showInputDialog("Enter the method name:");
        String parametersInput = JOptionPane.showInputDialog("Enter parameters (comma-separated, format: type name):");

        List<UMLParameterInfo> parameters = parseParameters(parametersInput);
        if (parameters == null) return;

        UMLMethods methodManager = new UMLMethods();
        methodManager.addMethod(className, methodName, parameters);

        ClassBox classBox = (ClassBox) classManager.getClassPanels().get(className);
        if (classBox != null) {
            classBox.updateDetails();
        }

        JOptionPane.showMessageDialog(null, "Method '" + methodName + "' added to class '" + className + "'.");
    }

    /**
     * Deletes a method from a specified class.
     * Prompts the user for the class name and method name. Updates the backend and the graphical
     * representation if the operation succeeds. Displays a confirmation or error message.
     */
    public void deleteMethod() {
        String className = JOptionPane.showInputDialog("Enter the class name to delete a method:");
        String methodName = JOptionPane.showInputDialog("Enter the method name:");

        if (className == null || methodName == null) {
            JOptionPane.showMessageDialog(null, "Operation canceled.");
            return;
        }

        UMLMethods methodManager = new UMLMethods();
        methodManager.removeMethod(className, methodName);

        ClassBox classBox = (ClassBox) classManager.getClassPanels().get(className);
        if (classBox != null) {
            classBox.updateDetails();
        }

        JOptionPane.showMessageDialog(null, "Method '" + methodName + "' deleted from class '" + className + "'.");
    }

    /**
     * Renames a method within a specified class.
     * Prompts the user for the class name, the current method name, and the new method name. Updates
     * the backend and the graphical representation if the operation succeeds. Displays a confirmation
     * or error message.
     */
    public void renameMethod() {
        String className = JOptionPane.showInputDialog("Enter the class name to rename a method:");
        String oldMethodName = JOptionPane.showInputDialog("Enter the current method name:");
        String newMethodName = JOptionPane.showInputDialog("Enter the new method name:");

        if (className == null || oldMethodName == null || newMethodName == null) {
            JOptionPane.showMessageDialog(null, "Operation canceled.");
            return;
        }

        UMLMethods methodManager = new UMLMethods();
        methodManager.renameMethod(className, oldMethodName, newMethodName);

        ClassBox classBox = (ClassBox) classManager.getClassPanels().get(className);
        if (classBox != null) {
            classBox.updateDetails();
        }

        JOptionPane.showMessageDialog(null, "Method '" + oldMethodName + "' renamed to '" + newMethodName + "' in class '" + className + "'.");
    }

    /**
     * Adds a parameter to a specified method in a class.
     * Prompts the user for the class name, method name, parameter type, and parameter name. Updates
     * the backend and the graphical representation if the operation succeeds. Displays a confirmation
     * or error message.
     */
    public void addParameter() {
        String className = JOptionPane.showInputDialog("Enter the class name:");
        String methodName = JOptionPane.showInputDialog("Enter the method name:");
        String parameterType = JOptionPane.showInputDialog("Enter the parameter type:");
        String parameterName = JOptionPane.showInputDialog("Enter the parameter name:");

        if (className == null || methodName == null || parameterType == null || parameterName == null) {
            JOptionPane.showMessageDialog(null, "Operation canceled.");
            return;
        }

        UMLMethods methodManager = new UMLMethods();
        methodManager.addParameter(className, methodName, parameterType, parameterName);

        ClassBox classBox = (ClassBox) classManager.getClassPanels().get(className);
        if (classBox != null) {
            classBox.updateDetails();
        }

        JOptionPane.showMessageDialog(null, "Parameter '" + parameterName + "' added to method '" + methodName + "' in class '" + className + "'.");
    }

    /**
     * Deletes a parameter from a specified method in a class.
     * Prompts the user for the class name, method name, and parameter name. Updates the backend and
     * the graphical representation if the operation succeeds. Displays a confirmation or error message.
     */
    public void deleteParameter() {
        String className = JOptionPane.showInputDialog("Enter the class name:");
        String methodName = JOptionPane.showInputDialog("Enter the method name:");
        String parameterName = JOptionPane.showInputDialog("Enter the parameter name to delete:");

        if (className == null || methodName == null || parameterName == null) {
            JOptionPane.showMessageDialog(null, "Operation canceled.");
            return;
        }

        UMLMethods methodManager = new UMLMethods();
        methodManager.removeParameter(className, methodName, parameterName);

        ClassBox classBox = (ClassBox) classManager.getClassPanels().get(className);
        if (classBox != null) {
            classBox.updateDetails();
        }

        JOptionPane.showMessageDialog(null, "Parameter '" + parameterName + "' removed from method '" + methodName + "' in class '" + className + "'.");
    }

    /**
     * Renames a parameter in a specified method of a class.
     * Prompts the user for the class name, method name, old parameter name, new parameter type,
     * and new parameter name. Updates the backend and the graphical representation if the operation
     * succeeds. Displays a confirmation or error message.
     */
    public void renameParameter() {
        String className = JOptionPane.showInputDialog("Enter the class name:");
        String methodName = JOptionPane.showInputDialog("Enter the method name:");
        String oldParameterName = JOptionPane.showInputDialog("Enter the current parameter name:");
        String newParameterType = JOptionPane.showInputDialog("Enter the new parameter type:");
        String newParameterName = JOptionPane.showInputDialog("Enter the new parameter name:");

        if (className == null || methodName == null || oldParameterName == null || newParameterType == null || newParameterName == null) {
            JOptionPane.showMessageDialog(null, "Operation canceled.");
            return;
        }

        UMLMethods methodManager = new UMLMethods();
        methodManager.changeSingleParameter(className, methodName, oldParameterName, newParameterType, newParameterName);

        ClassBox classBox = (ClassBox) classManager.getClassPanels().get(className);
        if (classBox != null) {
            classBox.updateDetails();
        }

        JOptionPane.showMessageDialog(null, "Parameter '" + oldParameterName + "' renamed to '" + newParameterName + "' in method '" + methodName + "' of class '" + className + "'.");
    }

    /**
     * Parses the parameter input string into a list of {@code UMLParameterInfo}.
     *
     * @param parametersInput the input string of parameters (comma-separated, format: type name).
     * @return a list of {@code UMLParameterInfo} objects or {@code null} if the input format is invalid.
     */
    private List<UMLParameterInfo> parseParameters(String parametersInput) {
        List<UMLParameterInfo> parameters = new ArrayList<>();
        if (parametersInput != null && !parametersInput.trim().isEmpty()) {
            for (String param : parametersInput.split(",")) {
                String[] typeAndName = param.trim().split("\\s+");
                if (typeAndName.length == 2) {
                    parameters.add(new UMLParameterInfo(typeAndName[0], typeAndName[1]));
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid parameter format. Use 'type name' for each parameter.");
                    return null;
                }
            }
        }
        return parameters;
    }
}
