package codecain.GraphicalUserInterface;

import codecain.BackendCode.UMLMethods;
import codecain.BackendCode.UMLParameterInfo;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GUIMethodManager {
    public void addMethod() {
        String className = JOptionPane.showInputDialog("Enter the class name to add a method:");
        String methodName = JOptionPane.showInputDialog("Enter the method name:");
        String parametersInput = JOptionPane.showInputDialog("Enter parameters (comma-separated, format: type name):");

        List<UMLParameterInfo> parameters = parseParameters(parametersInput);
        if (parameters == null) return;

        UMLMethods methodManager = new UMLMethods();
        methodManager.addMethod(className, methodName, parameters);

        JOptionPane.showMessageDialog(null, "Method '" + methodName + "' added to class '" + className + "'.");
    }

    public void deleteMethod() {
        String className = JOptionPane.showInputDialog("Enter the class name to delete a method:");
        String methodName = JOptionPane.showInputDialog("Enter the method name:");

        if (className == null || methodName == null) {
            JOptionPane.showMessageDialog(null, "Operation canceled.");
            return;
        }

        UMLMethods methodManager = new UMLMethods();
        methodManager.removeMethod(className, methodName);

        JOptionPane.showMessageDialog(null, "Method '" + methodName + "' deleted from class '" + className + "'.");
    }

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

        JOptionPane.showMessageDialog(null, "Method '" + oldMethodName + "' renamed to '" + newMethodName + "' in class '" + className + "'.");
    }

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

        JOptionPane.showMessageDialog(null, "Parameter '" + parameterName + "' added to method '" + methodName + "' in class '" + className + "'.");
    }

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

        JOptionPane.showMessageDialog(null, "Parameter '" + parameterName + "' removed from method '" + methodName + "' in class '" + className + "'.");
    }

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

        JOptionPane.showMessageDialog(null, "Parameter '" + oldParameterName + "' renamed to '" + newParameterName + "' in method '" + methodName + "' of class '" + className + "'.");
    }

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
