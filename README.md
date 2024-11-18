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

```bash
java -jar cscd350-f24-CodeCain.jar
```
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

- **`src/main/java/codecain`**: Contains the main application code
  - **'BackendCode'**: Contains the backend code for the UML Editor
    - **`UndoRedo`**: Contains classes for managing undo/redo operations
      - **`Caretaker`**: Manages the history of commands for undo/redo
      - **`Memento`**: Represents the state of the UML diagram at a given point
      - **`StateManager`**: Manages the state of the UML diagram and performs undo/redo operations
    - **`Relationship.java`**: Represents a relationship between UML classes
    - **`SaveManager.java`**: Manages saving and loading UML diagrams as JSON files
    - **`UMLClass.java`**: Represents a UML class with fields, methods, and relationships
    - **`UMLClassInfo.java`**: Represents the information of a UML class
    - **`UMLFieldInfo`**: Represents the information of a UML field
    - **`UMLFields`**:  Represents the fields of a UML class
    - **`UMLMethodInfo`**: Represents the information of a UML method
    - **`UMLMethods`**: Represents the methods of a UML class
    - **`UmlParameterInfo`**: Represents a parameter in a UML class
  - **'CommandLineInterface'**: Contains the code for the command line interface
    - **`CLI.java`**: Manages the command-line interface for the UML Editor
    - **`CLIController.java`**: Handles user input and command execution
    - **`CLIView.java`**: Displays the command-line interface for the UML Editor
    - **`CommandManger.java`**: Manages the command-line interface for the UML Editor
    - **`DisplayHelper.java`**: Provides helper methods for displaying information in the CLI
    - **`FileOperations.java`**: Handles file operations for the UML Editor
  - **'GraphicalUserInterface.java'**: Handles the execution of commands for the UML Editor
    - **`Main.java`**: Launches the UML Editor application
    - **`MenuGUI.java`**: Manages the menu bar for the UML Editor CLI and GUI
- **`src/test/java/uml`**: Contains JUnit tests for the UML Editor
- **`src/main/resources`**: Contains JavaFX FXML files for the GUI