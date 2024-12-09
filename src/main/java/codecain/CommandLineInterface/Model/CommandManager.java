package codecain.CommandLineInterface.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import codecain.BackendCode.Model.Relationship;
import codecain.BackendCode.Model.RelationshipType;
import codecain.BackendCode.Model.UMLClass;
import codecain.BackendCode.Model.UMLClassInfo;
import codecain.BackendCode.Model.UMLFields;
import codecain.BackendCode.Model.UMLMethodInfo;
import codecain.BackendCode.Model.UMLMethods;
import codecain.BackendCode.Model.UMLParameterInfo;
import codecain.BackendCode.UndoRedo.StateManager;
import codecain.CommandLineInterface.View.CLIView;
import codecain.GraphicalUserInterface.Controller.Controller;
import codecain.GraphicalUserInterface.Model.ExportImage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Manages and executes user commands for UML diagram operations.
 */
public class CommandManager {

    private TextArea commandOutput;
    private FileOperations fileOperations;
    private final StateManager stateManager; // Centralized StateManager for undo/redo

    /**
     * Initializes a new instance of the CommandManager with the specified JTextArea for command output.
     *
     * @param commandOutput JTextArea to display command outputs
     */
    public CommandManager(TextArea commandOutput) {
        this.commandOutput = commandOutput;
        this.fileOperations = new FileOperations();
        this.stateManager = new StateManager();    }

    /**
     * Appends text to the command output TextArea.
     *
     * @param text the text to append
     */
    private void appendToOutput(String text) {
        commandOutput.appendText(text);
    }

    /**
     * Parses and executes a given command by identifying its type and performing the corresponding operation.
     *
     * @param command the command to parse and execute
     */
    public void parseAndExecute(String command) {
        String[] tokens = command.split(" ");
        if (tokens.length == 0 || tokens[0].isEmpty()) {
            appendToOutput("No command entered.\n");
            return;
        }

        String commandName = tokens[0].toLowerCase();
        String result = "";
        switch (commandName) {
            case "help" -> {
                result = DisplayHelper.showHelp();
                int helpStartPosition = commandOutput.getLength();
                commandOutput.appendText(">> " + command + "\n" + result + "\n");
                commandOutput.positionCaret(helpStartPosition);
                return;
            }
            case "add", "delete", "rename", "list" -> result = handleCommand(tokens);
            case "save" -> result = fileOperations.saveDiagram(getFileName(tokens));
            case "load" -> result = fileOperations.loadDiagram(getFileName(tokens));
            case "undo" -> undo();
            case "redo" -> redo();
            case "export" -> {
                if (tokens.length < 2) {
                    appendToOutput("Usage: export <file-path>\n");
                    return;
                }
                openGUIAndExport(tokens[1]);
            }
            case "exit" -> {
                System.exit(0);
                return;
            }
            default -> result = "Unknown command. Type 'help' to see available commands.";
        }

        if (!result.isEmpty()) {
            appendToOutput( "\n" + result + "\n");
        }
    }

    /**
     * Opens the GUI through the Controller and exports the UML diagram as an image.
     *
     * @param exportFilePath The file path to save the UML diagram image.
     */
    public void openGUIAndExport(String exportFilePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/codecain/GraphicalInterface.fxml"));
            Pane root = loader.load();
            Controller controller = loader.getController();
    
            Stage guiStage = new Stage();
            guiStage.setTitle("UML Diagram Editor");
            guiStage.setScene(new Scene(root));
            guiStage.setOpacity(0);
            guiStage.show();
    
            Pane nodeContainer = controller.getNodeContainer();
            nodeContainer = controller.populateGUIFromClassMap();
    
        if (exportFilePath != null) {
            if (!exportFilePath.toLowerCase().endsWith(".png")) {
                exportFilePath += ".png";
            }

            File exportFile = new File(exportFilePath);
            ExportImage.exportImage(nodeContainer, exportFile);
            appendToOutput("UML diagram successfully exported to: " + exportFilePath + "\n");
        } else {
            appendToOutput("Export file path is null. Cannot export UML diagram.\n");
        }
        guiStage.close();
    } catch (Exception e) {
        appendToOutput("Failed to open GUI and export UML diagram: " + e.getMessage() + "\n");
        e.printStackTrace();
    }
}
    

    /**
     * Undo the last action.
     */
    private void undo() {
        if (stateManager.undo()) {
            appendToOutput("Undo successful.\n");
        } else {
            appendToOutput("No actions to undo.\n");
        }
    }

    /**
     * Redo the last undone action.
     */
    private void redo() {
        if (stateManager.redo()) {
            appendToOutput("Redo successful.\n");
        } else {
            appendToOutput("No actions to redo.\n");
        }
    }




    /**
     * Handles commands related to specific operations like add, delete, rename, and list by delegating
     * them to their respective handler methods.
     *
     * @param tokens the split parts of the command
     * @return result message of the command execution
     */
    private String handleCommand(String[] tokens) {
        if (tokens.length < 2) {
            return DisplayHelper.insufficientParameters();
        }

        return switch (tokens[0].toLowerCase()) {
            case "add" -> handleAdd(tokens);
            case "delete" -> handleDelete(tokens);
            case "rename" -> handleRename(tokens);
            case "list" -> handleList(tokens);
            default -> DisplayHelper.unknownAction(tokens[0]);
        };
    }

    /**
     * Handles the addition of various UML components, such as classes, relationships, fields, etc.
     *
     * @param tokens command parts including type and parameters
     * @return message indicating success or failure of the add operation
     */
    private String handleAdd(String[] tokens) {
        if (tokens.length < 3) {
            return "Usage: add <type> <name> [additional parameters]. Use 'help' for available commands.";
        }

        String type = tokens[1].toLowerCase();
        return switch (type) {
            case "class" -> handleAddClass(tokens[2]);
            case "relationship" -> handleAddRelationship(tokens);
            case "field" -> handleAddField(tokens);
            case "method" -> handleAddMethod(tokens);
            case "parameter" -> handleAddParameter(tokens);
            default -> DisplayHelper.unknownAddType(type);
        };
    }

    /**
     * Adds a new UML class with the specified name.
     *
     * @param className name of the class to add
     * @return message confirming the addition of the class or notifying if it already exists
     */
    private String handleAddClass(String className) {
        stateManager.saveState();
        if (UMLClass.exists(className)) {
            return DisplayHelper.classAlreadyExists(className);
        }
        UMLClass.addClass(className);
        return DisplayHelper.classAdded(className);
    }



    /**
     * Adds a relationship between two classes if it does not already exist.
     *
     * @param tokens command parts specifying the classes involved in the relationship
     * @return message confirming or denying the addition of the relationship
     */
    private String handleAddRelationship(String[] tokens) {
        stateManager.saveState();
        if (tokens.length < 5) {
            return "Usage: add relationship <class1> <class2>";
        }
        if (Relationship.relationshipExists(tokens[2], tokens[3])) {
            return "Relationship between '" + tokens[2] + "' and '" + tokens[3] + "' already exists.";
        }
        boolean added = Relationship.addRelationship(tokens[2], tokens[3],
                RelationshipType.fromString(tokens[4]));
        if (added) {
            return DisplayHelper.relationshipAdded(tokens[2], tokens[3]);
        } else {
            return "Failed to add relationship. Ensure both classes exist and the relationship is valid.";
        }
    }

    /**
     * Adds a field to a specified UML class if it does not already exist.
     *
     * @param tokens command parts specifying class, field type, and field name
     * @return message confirming or denying the addition of the field
     */
    private String handleAddField(String[] tokens) {
        stateManager.saveState();
        if (tokens.length < 5) {
            return "Usage: add field <className> <fieldType> <fieldName>";
        }

        String className = tokens[2];
        String fieldType = tokens[3];
        String fieldName = tokens[4];

        String errorMessage = checkClassExists(className);
        if (errorMessage != null) {
            return errorMessage;
        }

        UMLFields fields = new UMLFields();

        if (fields.doesFieldExist(getClassInfo(className), fieldName)) {
            return "Error: Field '" + fieldName + "' already exists in class '" + className + "'.";
        }

        fields.addField(className, fieldType, fieldName);
        return DisplayHelper.fieldAdded(fieldName, fieldType, className);
    }


    /**
     * Adds a method to a specified UML class.
     *
     * @param tokens command parts specifying class, method name, and parameters
     * @return message confirming or denying the addition of the method
     */
    private String handleAddMethod(String[] tokens) {
        if (tokens.length < 4) {
            return "Usage: add method <className> <methodName> <parameterType1 parameterName1, parameterType2 parameterName2, ...>. Type 'help' for more info.";
        }
    
        String className = tokens[2];
        String methodName = tokens[3];
    
        // Validate if the class exists
        String errorMessage = checkClassExists(className);
        if (errorMessage != null) {
            return errorMessage;
        }
    
        List<UMLParameterInfo> parameters = new ArrayList<>();
    
        // Reassemble parameters string from tokens after the method name
        if (tokens.length > 4) {
            StringBuilder parametersBuilder = new StringBuilder();
            for (int i = 4; i < tokens.length; i++) {
                parametersBuilder.append(tokens[i]).append(" ");
            }
    
            String parametersString = parametersBuilder.toString().trim();
            String[] paramTokens = parametersString.split(",");
    
            for (String paramToken : paramTokens) {
                paramToken = paramToken.trim(); // Trim spaces around the pair
                String[] typeAndName = paramToken.split("\\s+"); // Split by space
    
                if (typeAndName.length != 2) {
                    return "Invalid parameter format. Each parameter must be in the format <parameterType> <parameterName>, separated by commas.";
                }
    
                String parameterType = typeAndName[0].trim();
                String parameterName = typeAndName[1].trim();
    
                parameters.add(new UMLParameterInfo(parameterType, parameterName));
            }
        }
    
        UMLMethods methods = new UMLMethods();
        methods.addMethod(className, methodName, parameters);
    
        return DisplayHelper.methodAdded(methodName, className);
    }
    
    /**
     * Adds a parameter to a specified method of a class.
     *
     * @param tokens command parts specifying class, method, parameter type, and name
     * @return message confirming or denying the addition of the parameter
     */
    private String handleAddParameter(String[] tokens) {
        if (tokens.length < 5) {
            return "Usage: add parameter <className> <methodName> <parameterType> <parameterName>";
        }
    
        String className = tokens[2];
        String methodName = tokens[3];
        String parameterType = tokens[4];
        String parameterName = tokens[5];
    
        // Check if the class exists
        String errorMessage = checkClassExists(className);
        if (errorMessage != null) {
            return errorMessage;
        }
    
        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) {
            return "Class '" + className + "' not found.";
        }
    
        // Find methods with the given name
        var matchingMethods = classInfo.getMethods().stream()
                .filter(method -> method.getMethodName().equals(methodName))
                .toList();
    
        if (matchingMethods.isEmpty()) {
            return "No method named '" + methodName + "' found in class '" + className + "'.";
        } else if (matchingMethods.size() == 1) {
            // If there's only one method, add the parameter directly
            matchingMethods.get(0).addParameter(new UMLParameterInfo(parameterType, parameterName));
            return "Parameter '" + parameterName + "' added to method '" + methodName + "' in class '" + className + "'.";
        } else {
            // If there are multiple methods, prompt the user to choose
            StringBuilder prompt = new StringBuilder("Multiple methods named '" + methodName + "' found. Please choose which one to modify:\n");
            for (int i = 0; i < matchingMethods.size(); i++) {
                prompt.append((i + 1)).append(": ").append(matchingMethods.get(i)).append("\n");
            }
    
            CLIView.promptForInput(prompt.toString() + "Enter the number:", userInput -> {
                try {
                    int choice = Integer.parseInt(userInput);
                    if (choice > 0 && choice <= matchingMethods.size()) {
                        matchingMethods.get(choice - 1).addParameter(new UMLParameterInfo(parameterType, parameterName));
                        CLIView.getCommandOutput().appendText("Parameter '" + parameterName + "' added to method '" + methodName + "' (option " + choice + ") in class '" + className + "'.\n");
                    } else {
                        CLIView.getCommandOutput().appendText("Invalid choice. No parameter added.\n");
                    }
                } catch (NumberFormatException e) {
                    CLIView.getCommandOutput().appendText("Invalid input. Please enter a valid number.\n");
                }
            });
    
            return ""; // Empty string to prevent immediate output
        }
    }
    

    /**
     * Handles deletion of various UML components, such as classes, relationships, fields, etc.
     *
     * @param tokens command parts including type and parameters
     * @return message indicating success or failure of the delete operation
     */
    private String handleDelete(String[] tokens) {
        if (tokens.length < 3) {
            return "Usage: delete <type> <name>. Use 'help' for available commands.";
        }

        String type = tokens[1].toLowerCase();
        return switch (type) {
            case "class" -> handleDeleteClass(tokens[2]);
            case "relationship" -> handleDeleteRelationship(tokens);
            case "field" -> handleDeleteField(tokens);
            case "method" -> handleDeleteMethod(tokens);
            case "parameter" -> handleDeleteParameter(tokens);
            default -> DisplayHelper.unknownDeleteType(type);
        };
    }

    /**
     * Deletes a UML class and removes any relationships associated with it.
     *
     * @param className name of the class to delete
     * @return message confirming the deletion of the class
     */
    private String handleDeleteClass(String className) {
        stateManager.saveState();

        if (className == null || className.isBlank()) {
            return "Error: The class name provided is invalid.";
        }

        if (!UMLClass.exists(className)) {
            return "Error: Class '" + className + "' does not exist.";
        }

        UMLClass.removeClass(className);
        Relationship.removeAttachedRelationships(className);
        return DisplayHelper.classRemoved(className);
    }
    /**
     * Deletes a relationship between two classes.
     *
     * @param tokens command parts specifying the classes involved in the relationship
     * @return message confirming or denying the deletion of the relationship
     */
    private String handleDeleteRelationship(String[] tokens) {
        stateManager.saveState();
        if (tokens.length < 4) {
            return "Usage: delete relationship <class1> <class2>";
        }
        boolean removed = Relationship.removeRelationship(tokens[2], tokens[3]);
        if (removed) {
            return DisplayHelper.relationshipRemoved(tokens[2], tokens[3]);
        } else {
            return "Failed to remove relationship. Ensure the relationship exists.";
        }
    }

    /**
     * Deletes a field from a specified UML class.
     *
     * @param tokens command parts specifying class and field name
     * @return message confirming or denying the deletion of the field
     */
    private String handleDeleteField(String[] tokens) {
        stateManager.saveState();

        if (tokens.length < 4) {
            return "Usage: delete field <className> <fieldName>";
        }

        String className = tokens[2];
        String fieldName = tokens[3];

        String errorMessage = checkClassExists(className);
        if (errorMessage != null) {
            return errorMessage;
        }

        UMLFields fields = new UMLFields();

        if (!fields.doesFieldExist(getClassInfo(className), fieldName)) {
            return "Error: Field '" + fieldName + "' does not exist in class '" + className + "'.";
        }

        fields.removeField(className, fieldName);
        return DisplayHelper.fieldRemoved(fieldName, className);
    }

    /**
     * Deletes a method from a specified UML class.
     *
     * @param tokens command parts specifying class and method name
     * @return message confirming or denying the deletion of the method
     */
    private String handleDeleteParameter(String[] tokens) {
        if (tokens.length < 6) {
            return "Usage: delete parameter <className> <methodName> <parameterType> <parameterName>";
        }
    
        String className = tokens[2];
        String methodName = tokens[3];
        String parameterType = tokens[4];
        String parameterName = tokens[5];
    
        UMLParameterInfo parameterInfo = new UMLParameterInfo(parameterType, parameterName);

        String errorMessage = checkClassExists(className);
        if (errorMessage != null) {
            return errorMessage;
        }
    
        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) {
            return "Class '" + className + "' not found.";
        }
            var matchingMethods = classInfo.getMethods().stream()
                .filter(method -> method.getMethodName().equals(methodName))
                .toList();
    
        if (matchingMethods.isEmpty()) {
            return "No method named '" + methodName + "' found in class '" + className + "'.";
        } else if (matchingMethods.size() == 1) {
            if (matchingMethods.get(0).getParameters().contains(parameterInfo)) {
                matchingMethods.get(0).getParameters().remove(parameterInfo);
                return "Parameter '" + parameterInfo + "' removed from method '" + methodName + "' in class '" + className + "'.";
            } else {
                return "Parameter '" + parameterInfo + "' not found in method '" + methodName + "' of class '" + className + "'.";
            }
        } else {
            StringBuilder prompt = new StringBuilder("Multiple methods named '" + methodName + "' found. Please choose which one to modify:\n");
            for (int i = 0; i < matchingMethods.size(); i++) {
                prompt.append((i + 1)).append(": ").append(matchingMethods.get(i)).append("\n");
            }
            CLIView.promptForInput(prompt.toString() + "Enter the number:", userInput -> {
                try {
                    int choice = Integer.parseInt(userInput);
                    if (choice > 0 && choice <= matchingMethods.size()) {
                        var selectedMethod = matchingMethods.get(choice - 1);
                        if (selectedMethod.getParameters().contains(parameterInfo)) {
                            selectedMethod.getParameters().remove(parameterInfo);
                            CLIView.getCommandOutput().appendText("Parameter '" + parameterInfo + "' removed from method '" + methodName + "' (option " + choice + ") in class '" + className + "'.\n");
                        } else {
                            CLIView.getCommandOutput().appendText("Parameter '" + parameterInfo + "' not found in selected method.\n");
                        }
                    } else {
                        CLIView.getCommandOutput().appendText("Invalid choice. No parameter removed.\n");
                    }
                } catch (NumberFormatException e) {
                    CLIView.getCommandOutput().appendText("Invalid input. Please enter a valid number.\n");
                }
            });
    
            return "";
        }
    }
    

    /**
     * Renames various UML components, such as classes, fields, methods, etc.
     *
     * @param tokens command parts including type and names
     * @return message indicating success or failure of the rename operation
     */
    private String handleRename(String[] tokens) {
        if (tokens.length < 4) {
            return "Usage: rename <type> <oldName> <newName>. Use 'help' for available commands.";
        }

        String type = tokens[1].toLowerCase();
        return switch (type) {
            case "class" -> handleRenameClass(tokens[2], tokens[3]);
            case "field" -> handleRenameField(tokens);
            case "method" -> handleRenameMethod(tokens);
            case "parameter" -> handleRenameParameter(tokens);
            case "all_parameters" -> handleChangeAllParameters(tokens);
            default -> DisplayHelper.unknownAction(type);
        };
    }

    /**
     * Renames a UML class.
     *
     * @param oldName current name of the class
     * @param newName new name for the class
     * @return message confirming the renaming of the class
     */
    private String handleRenameClass(String oldName, String newName) {
        stateManager.saveState();

        if (oldName == null || oldName.isBlank()) {
            return "Error: The old class name provided is invalid.";
        }

        if (newName == null || newName.isBlank()) {
            return "Error: The new class name provided is invalid.";
        }

        if (!UMLClass.exists(oldName)) {
            return "Error: Class '" + oldName + "' does not exist.";
        }
        if (UMLClass.exists(newName)) {
            return "Error: Class '" + newName + "' already exists.";
        }

        UMLClass.renameClass(oldName, newName);
        return DisplayHelper.classRenamed(oldName, newName);
    }

    /**
     * Renames a field in a specified UML class.
     *
     * @param tokens command parts specifying class, field name, and new field details
     * @return message confirming the renaming of the field, or an error message if the class does not exist
     */
    private String handleRenameField(String[] tokens) {
        stateManager.saveState();
        String errorMessage = checkClassExists(tokens[2]);
        if (errorMessage != null) {
            return errorMessage;
        }

        UMLFields fields = new UMLFields();
        fields.renameField(tokens[2], tokens[3], tokens[4], tokens[5]);
        return DisplayHelper.fieldRenamed(tokens[3], tokens[4], tokens[2]);
    }

    /**
     * Renames a method in a specified UML class.
     *
     * @param tokens command parts specifying class, method name, and new method name
     * @return message confirming the renaming of the method, or an error message if the class does not exist
     */
    private String handleRenameMethod(String[] tokens) {
        if (tokens.length < 5) {
            return "Usage: rename method <className> <currentMethodName> <newMethodName>";
        }

        stateManager.saveState();

        String className = tokens[2];
        String currentMethodName = tokens[3];
        String newMethodName = tokens[4];

        String errorMessage = checkClassExists(className);
        if (errorMessage != null) {
            return errorMessage;
        }

        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) {
            return "Class '" + className + "' not found.";
        }

        var matchingMethods = classInfo.getMethods().stream()
                .filter(method -> method.getMethodName().equals(currentMethodName))
                .toList();

        if (matchingMethods.isEmpty()) {
            return "No method named '" + currentMethodName + "' found in class '" + className + "'.";
        } else if (matchingMethods.size() == 1) {
            matchingMethods.get(0).setMethodName(newMethodName);
            return "Method '" + currentMethodName + "' renamed to '" + newMethodName + "' in class '" + className + "'.";
        } else {
            StringBuilder prompt = new StringBuilder("Multiple methods named '" + currentMethodName + "' found. Please choose which one to rename:\n");
            for (int i = 0; i < matchingMethods.size(); i++) {
                prompt.append((i + 1)).append(": ").append(matchingMethods.get(i)).append("\n");
            }

            CLIView.promptForInput(prompt.toString() + "Enter the number:", userInput -> {
                try {
                    int choice = Integer.parseInt(userInput);
                    if (choice > 0 && choice <= matchingMethods.size()) {
                        matchingMethods.get(choice - 1).setMethodName(newMethodName);
                        CLIView.getCommandOutput().appendText("Method '" + currentMethodName + "' renamed to '" + newMethodName + "' (option " + choice + ") in class '" + className + "'.\n");
                    } else {
                        CLIView.getCommandOutput().appendText("Invalid choice. No method renamed.\n");
                    }
                } catch (NumberFormatException e) {
                    CLIView.getCommandOutput().appendText("Invalid input. Please enter a valid number.\n");
                }
            });

            return ""; // Empty string to prevent immediate output
        }
    }


    /**
     * Renames a parameter in a specified method of a UML class.
     *
     * @param tokens command parts specifying class, method, old parameter name, new parameter type, and new parameter name
     * @return message confirming the renaming of the parameter, or an error message if the class does not exist
     */
    private String handleRenameParameter(String[] tokens) {
        stateManager.saveState();
        if (tokens.length < 6) {
            return "Usage: rename parameter <className> <methodName> <oldParameterName> <newParameterType> <newParameterName>";
        }

        String className = tokens[2];
        String methodName = tokens[3];
        String oldParameterName = tokens[4];
        String newParameterType = tokens[5];
        String newParameterName = tokens[6];

        String errorMessage = checkClassExists(className);
        if (errorMessage != null) {
            return errorMessage;
        }

        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) {
            return "Class '" + className + "' not found.";
        }

        var matchingMethods = classInfo.getMethods().stream()
                .filter(method -> method.getMethodName().equals(methodName))
                .toList();

        if (matchingMethods.isEmpty()) {
            return "No method named '" + methodName + "' found in class '" + className + "'.";
        } else if (matchingMethods.size() == 1) {
            UMLMethodInfo selectedMethod = matchingMethods.get(0);
            if (renameParameter(selectedMethod, oldParameterName, newParameterType, newParameterName)) {
                return "Parameter '" + oldParameterName + "' renamed to '" + newParameterType + " " + newParameterName + "' in method '" + methodName + "' of class '" + className + "'.";
            } else {
                return "Parameter '" + oldParameterName + "' not found in method '" + methodName + "' of class '" + className + "'.";
            }
        } else {
            StringBuilder prompt = new StringBuilder("Multiple methods named '" + methodName + "' found in class '" + className + "'. Please choose which one to modify:\n");
            for (int i = 0; i < matchingMethods.size(); i++) {
                prompt.append((i + 1)).append(": ").append(matchingMethods.get(i)).append("\n");
            }

            CLIView.promptForInput(prompt.toString() + "Enter the number:", userInput -> {
                try {
                    int choice = Integer.parseInt(userInput);
                    if (choice > 0 && choice <= matchingMethods.size()) {
                        UMLMethodInfo selectedMethod = matchingMethods.get(choice - 1);
                        if (renameParameter(selectedMethod, oldParameterName, newParameterType, newParameterName)) {
                            CLIView.getCommandOutput().appendText("Parameter '" + oldParameterName + "' renamed to '" + newParameterType + " " + newParameterName + "' in method '" + methodName + "' (option " + choice + ") in class '" + className + "'.\n");
                        } else {
                            CLIView.getCommandOutput().appendText("Parameter '" + oldParameterName + "' not found in selected method.\n");
                        }
                    } else {
                        CLIView.getCommandOutput().appendText("Invalid choice. No parameter renamed.\n");
                    }
                } catch (NumberFormatException e) {
                    CLIView.getCommandOutput().appendText("Invalid input. Please enter a valid number.\n");
                }
            });

            return "";
        }
    }

    /**
     * Helper method to rename a parameter in a method.
     *
     * @param method           The UMLMethodInfo object
     * @param oldParameterName The current name of the parameter
     * @param newParameterType The new type for the parameter
     * @param newParameterName The new name for the parameter
     * @return true if the parameter was found and updated, false otherwise
     */
    private boolean renameParameter(UMLMethodInfo method, String oldParameterName, String newParameterType, String newParameterName) {
        for (UMLParameterInfo param : method.getParameters()) {
            if (param.getParameterName().equals(oldParameterName)) {
                param.setParameterType(newParameterType);
                param.setParameterName(newParameterName);
                return true;
            }
        }
        return false;
    }



    /**
     * Changes all parameters for a specified method in a UML class.
     *
     * @param tokens command parts specifying class, method, and new parameter details
     * @return message confirming the parameter changes, or an error message if the class does not exist
     */
    private String handleChangeAllParameters(String[] tokens) {
        stateManager.saveState();
        if (tokens.length < 5) {
            return "Usage: change parameters <className> <methodName> <parameterType1> <parameterName1> ...";
        }
        String className = tokens[2];
        String methodName = tokens[3];
        List<UMLParameterInfo> newParameters = parseParameters(tokens, 4);

        String errorMessage = checkClassExists(className);
        if (errorMessage != null) {
            return errorMessage;
        }

        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) {
            return "Class '" + className + "' not found.";
        }

        var matchingMethods = classInfo.getMethods().stream()
                .filter(method -> method.getMethodName().equals(methodName))
                .toList();

        if (matchingMethods.isEmpty()) {
            return "No method named '" + methodName + "' found in class '" + className + "'.";
        } else if (matchingMethods.size() == 1) {
            UMLMethodInfo selectedMethod = matchingMethods.get(0);
            selectedMethod.getParameters().clear();
            selectedMethod.getParameters().addAll(newParameters);
            return "All parameters changed for method '" + methodName + "' in class '" + className + "'.";
        } else {
            StringBuilder prompt = new StringBuilder("Multiple methods named '" + methodName + "' found in class '" + className + "'. Please choose which one to modify:\n");
            for (int i = 0; i < matchingMethods.size(); i++) {
                prompt.append((i + 1)).append(": ").append(matchingMethods.get(i)).append("\n");
            }
            CLIView.promptForInput(prompt.toString() + "Enter the number:", userInput -> {
                try {
                    int choice = Integer.parseInt(userInput);
                    if (choice > 0 && choice <= matchingMethods.size()) {
                        UMLMethodInfo selectedMethod = matchingMethods.get(choice - 1);
                        selectedMethod.getParameters().clear();
                        selectedMethod.getParameters().addAll(newParameters);
                        CLIView.getCommandOutput().appendText("All parameters changed for method '" + methodName + "' (option " + choice + ") in class '" + className + "'.\n");
                    } else {
                        CLIView.getCommandOutput().appendText("Invalid choice. No parameters changed.\n");
                    }
                } catch (NumberFormatException e) {
                    CLIView.getCommandOutput().appendText("Invalid input. Please enter a valid number.\n");
                }
            });

            return "";
        }
    }


    /**
     * Lists UML classes or relationships based on the provided tokens.
     *
     * @param tokens command parts specifying list type
     * @return message with the list of classes or relationships
     */
    private String handleList(String[] tokens) {
        if (tokens.length < 2) {
            return "Invalid command. Use 'list classes' or 'list relationships'.";
        }

        return switch (tokens[1].toLowerCase()) {
            case "classes" -> UMLClass.listAllClassesInfo();
            case "relationships" -> {
                String relationships = Relationship.listToString();
                yield relationships.isEmpty() ? "No relationships available." : "Relationships:\n" + relationships;
            }
            case "details" -> handleListDetails(tokens);
            default -> "Invalid command. Use 'list classes', 'list relationships', or 'list details'.";
        };
    }
    private String handleListDetails(String[] tokens) {
        if (tokens.length < 3) {
            return "Usage: list details <className>";
        }
        String className = tokens[2];
        return UMLClass.getClassDetails(className);
    }

    /**
     * Retrieves the filename from the command tokens, if provided.
     *
     * @param tokens command parts that may contain a filename
     * @return the specified filename or an empty string if not provided
     */
    private String getFileName(String[] tokens) {
        if (tokens.length > 1) {
            return tokens[1];
        } else {
            return "";
        }
    }

    /**
     * Checks if a class with the specified name exists.
     *
     * @param className name of the class to check
     * @return null if the class exists; otherwise, an error message
     */
    private String checkClassExists(String className) {
        if (!UMLClass.exists(className)) {
            return "Class '" + className + "' does not exist.";
        }
        return null;
    }

    /**
     * Parses command tokens to retrieve parameter types and names.
     *
     * @param tokens command parts containing parameter details
     * @param startIndex index from where parameters start in the tokens array
     * @return list of UMLParameterInfo objects for each parameter
     */
    private List<UMLParameterInfo> parseParameters(String[] tokens, int startIndex) {
        List<UMLParameterInfo> parameters = new ArrayList<>();
        for (int i = startIndex; i < tokens.length; i += 2) {
            if (i + 1 < tokens.length) {
                parameters.add(new UMLParameterInfo(tokens[i], tokens[i + 1]));
            }
        }
        return parameters;
    }

    /**
     * Helper method to get UMLClassInfo for a class.
     *
     * @param className the name of the class
     * @return the UMLClassInfo object, or null if class does not exist
     */
    private UMLClassInfo getClassInfo(String className) {
        UMLClassInfo classInfo = UMLClass.classMap.get(className);
        if (classInfo == null) {
            System.out.println("Class '" + className + "' does not exist.");
        }
        return classInfo;
    }
    private String handleDeleteMethod(String[] tokens) {
        if (tokens.length < 3) {
            return "Usage: delete method <className> <methodName>";
        }

        String className = tokens[2];
        String methodName = tokens[3];

        String errorMessage = checkClassExists(className);
        if (errorMessage != null) {
            return errorMessage;
        }

        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) {
            return "Class '" + className + "' not found.";
        }

        var matchingMethods = classInfo.getMethods().stream()
                .filter(method -> method.getMethodName().equals(methodName))
                .toList();

        if (matchingMethods.isEmpty()) {
            return "No method named '" + methodName + "' found in class '" + className + "'.";
        } else if (matchingMethods.size() == 1) {
            classInfo.getMethods().remove(matchingMethods.get(0));
            return "Method '" + methodName + "' removed from class '" + className + "'.";
        } else {
            StringBuilder prompt = new StringBuilder("Multiple methods named '" + methodName + "' found. Please choose which one to delete:\n");
            for (int i = 0; i < matchingMethods.size(); i++) {
                prompt.append((i + 1)).append(": ").append(matchingMethods.get(i)).append("\n");
            }

            CLIView.promptForInput(prompt.toString() + "Enter the number:", userInput -> {
                try {
                    int choice = Integer.parseInt(userInput);
                    if (choice > 0 && choice <= matchingMethods.size()) {
                        classInfo.getMethods().remove(matchingMethods.get(choice - 1));
                        CLIView.getCommandOutput().appendText("Method '" + methodName + "' (option " + choice + ") removed from class '" + className + "'.\n");
                    } else {
                        CLIView.getCommandOutput().appendText("Invalid choice. No method removed.\n");
                    }
                } catch (NumberFormatException e) {
                    CLIView.getCommandOutput().appendText("Invalid input. Please enter a valid number.\n");
                }
            });

            return ""; // Empty string to prevent immediate output
        }
    }
}
