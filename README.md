# Code Cain UML Editor Project

## Overview

The Code Cain UML Editor is a Java-based application that provides a graphical interface for designing and managing UML diagrams. It supports creating classes, relationships, and various UML diagram elements while offering undo/redo functionality, file operations (save/load), and a command-line interface (CLI) for advanced users.

## Features

- **Graphical User Interface (GUI)** using JavaFX
- **Command-Line Interface (CLI)** for an FX CLI with auto-complete and intuitive commands
- **Undo/Redo Functionality** to manage diagram edits
- **File Operations**: Save and load diagrams as JSON files
- **UML Element Management**:
  - Create, rename, and delete classes, fields, methods, and relationships
  - Modify UML class elements dynamically



## Usage
Running the Application, make sure you are using Java 23.  
GUI Mode: Launch MainGUI.java.  
CLI Mode: Run CommandManager to enter commands interactively.  
JAR File: Run the UML Editor JAR file


## Launching The Program
To run this program, you will need to have Java 23 installed on your machine. You can download the latest version of Java from the official Oracle website. Once you have Java installed, you can run the program by executing the following command in the terminal:

### Option 1
```bash
java -jar cscd350-f24-CodeCain.jar
```

### Option 2
Simply double click on cscd350-f24-CodeCain.jar

Here is a link to the latest version of Java: https://www.oracle.com/java/technologies/downloads/  
Download SDK 23 for your desired operating system.
# Future Enhancements
Improved UI for a more intuitive design experience.
Export UML diagrams to various formats (e.g., PNG, JPEG).

# Contact
For any questions or feedback, please feel free to reach out to the project maintainers (Those who use have our info):
- Aaron Williams-Breth
- Sergei Uss
- Colby Crutcher
- Riley Rudolfo
- Ben Foster
- Aaron Oehler



## Project Structure

- **`src/main/java/codecain`**: Contains the main application code.

  - **`BackendCode`**: Backend logic for managing UML diagrams.
    - **`Model`**: Represents the data structure and relationships in UML diagrams.
      - **`Relationship.java`**: Represents relationships between UML classes.
      - **`SaveManager.java`**: Handles saving and loading UML diagrams as JSON files.
      - **`UMLClass.java`**: Represents UML classes with their fields, methods, and relationships.
      - **`UMLClassInfo.java`**: Stores information about UML classes.
      - **`UMLFieldInfo.java`**: Models a UML class field.
      - **`UMLFields.java`**: Manages fields within UML classes.
      - **`UMLMethodInfo.java`**: Models a UML method.
      - **`UMLMethods.java`**: Manages methods within UML classes.
      - **`UMLParameterInfo.java`**: Represents a parameter in a UML method.
    - **`UndoRedo`**: Provides undo/redo functionality.
      - **`Caretaker.java`**: Maintains the history of states for undo/redo operations.
      - **`Memento.java`**: Captures and restores the state of the UML diagram.
      - **`StateManager.java`**: Manages undo/redo logic and state transitions.

  - **`CommandLineInterface`**: Provides the command-line interface for managing UML diagrams.
    - **`Controller`**: Handles user inputs and commands.
      - **`CLIController.java`**: Manages command execution for the CLI.
    - **`Model`**: Core classes for CLI operations.
      - **`CommandManager.java`**: Manages command execution and history for CLI operations.
      - **`DisplayHelper.java`**: Provides helper functions to display information in the CLI.
      - **`FileOperations.java`**: Handles saving and loading files through CLI.
    - **`View`**: Visual representation for the CLI.
      - **`CLI.java`**: Entry point for the command-line interface.
      - **`CLIView.java`**: Displays the CLI interface and manages user interactions.

  - **`GraphicalUserInterface`**: Contains the JavaFX-based graphical user interface.
    - **`Controller`**: Manages interactions and events in the GUI.
      - **`Controller.java`**: Centralized controller for the graphical user interface.
    - **`Model`**: Backend logic for GUI-specific operations.
      - **`ClassManager.java`**: Handles operations for UML classes in the GUI.
      - **`FieldManager.java`**: Handles operations for UML fields in the GUI.
      - **`MethodManager.java`**: Handles operations for UML methods in the GUI.
      - **`ParameterManager.java`**: Handles operations for UML method parameters in the GUI.
    - **`View`**: JavaFX components and utilities for the GUI.
      - **`AlertHelper.java`**: Provides utility methods to display alerts and dialogs.
      - **`ClassNode.java`**: Represents a visual UML class node in the diagram.
      - **`DialogUtils.java`**: Utility class for managing dialogs in the GUI.
      - **`GraphicalInterfaceJavaFX.java`**: Handles main graphical interface operations.
      - **`MenuGUI.java`**: Manages the menu bar and associated events in the GUI.
      - **`PositionUtils.java`**: Provides utilities for managing node positioning.

  - **`Main.java`**: Entry point for launching the UML Editor application.

- **`src/test/java/codecain`**: Contains unit tests for validating the application's functionality.
- **`src/main/resources`**: Stores FXML files and other resources for the JavaFX GUI.
