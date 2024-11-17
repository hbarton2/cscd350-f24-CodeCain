package codecain.CommandLineInterface;

import codecain.BackendCode.*;
import codecain.BackendCode.UndoRedo.StateManager;
import javafx.scene.control.TextArea;
import java.util.ArrayList;
import java.util.List;

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
        if (tokens.length < 4) {
            return "Usage: add relationship <class1> <class2>";
        }
        if (Relationship.relationshipExists(tokens[2], tokens[3])) {
            return "Relationship between '" + tokens[2] + "' and '" + tokens[3] + "' already exists.";
        }
        boolean added = Relationship.addRelationship(tokens[2], tokens[3]);
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
            return "Field '" + fieldName + "' already exists in class '" + className + "'.";
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
        stateManager.saveState();
        String errorMessage = checkClassExists(tokens[2]);
        if (errorMessage != null) {
            return errorMessage;
        }
        UMLMethods methods = new UMLMethods();
        methods.addMethod(tokens[2], tokens[3], List.of(new UMLParameterInfo(tokens[4], tokens[5])));
        return DisplayHelper.methodAdded(tokens[3], tokens[2]);
    }

    /**
     * Adds a parameter to a specified method of a class.
     *
     * @param tokens command parts specifying class, method, parameter type, and name
     * @return message confirming or denying the addition of the parameter
     */
    private String handleAddParameter(String[] tokens) {
        stateManager.saveState();
        String errorMessage = checkClassExists(tokens[2]);
        if (errorMessage != null) {
            return errorMessage;
        }
        UMLMethods paramMethods = new UMLMethods();
        paramMethods.addParameter(tokens[2], tokens[3], tokens[4], tokens[5]);
        return DisplayHelper.parameterAdded(tokens[5], tokens[3], tokens[2]);
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
        String errorMessage = checkClassExists(tokens[2]);
        if (errorMessage != null) {
            return errorMessage;
        }

        UMLFields fields = new UMLFields();
        fields.removeField(tokens[2], tokens[3]);
        return DisplayHelper.fieldRemoved(tokens[3], tokens[2]);
    }

    /**
     * Deletes a method from a specified UML class.
     *
     * @param tokens command parts specifying class and method name
     * @return message confirming or denying the deletion of the method
     */
    private String handleDeleteMethod(String[] tokens) {
        stateManager.saveState();
        String errorMessage = checkClassExists(tokens[2]);
        if (errorMessage != null) {
            return errorMessage;
        }

        UMLMethods methods = new UMLMethods();
        methods.removeMethod(tokens[2], tokens[3]);
        return DisplayHelper.methodRemoved(tokens[3], tokens[2]);
    }

    /**
     * Deletes a parameter from a specified method of a class.
     *
     * @param tokens command parts specifying class, method, and parameter name
     * @return message confirming or denying the deletion of the parameter
     */
    private String handleDeleteParameter(String[] tokens) {
        stateManager.saveState();
        String errorMessage = checkClassExists(tokens[2]);
        if (errorMessage != null) {
            return errorMessage;
        }

        UMLMethods paramMethods = new UMLMethods();
        paramMethods.removeParameter(tokens[2], tokens[3], tokens[4]);
        return DisplayHelper.parameterRemoved(tokens[4], tokens[3], tokens[2]);
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
        stateManager.saveState();
        String errorMessage = checkClassExists(tokens[2]);
        if (errorMessage != null) {
            return errorMessage;
        }

        UMLMethods methods = new UMLMethods();
        methods.renameMethod(tokens[2], tokens[3], tokens[4]);
        return DisplayHelper.methodRenamed(tokens[3], tokens[4], tokens[2]);
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

        String errorMessage = checkClassExists(tokens[2]);
        if (errorMessage != null) {
            return errorMessage;
        }

        UMLMethods methods = new UMLMethods();
        methods.changeSingleParameter(tokens[2], tokens[3], tokens[4], tokens[5], tokens[6]);
        return DisplayHelper.parameterRenamed(tokens[4], tokens[6], tokens[3], tokens[2]);
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

        UMLMethods methods = new UMLMethods();
        methods.changeAllParameters(className, methodName, newParameters);
        return DisplayHelper.allParametersChanged(methodName, className);
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
            default -> "Invalid command. Use 'list classes' or 'list relationships'.";
        };
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

}
