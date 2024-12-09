---

# Code Cain UML Editor

## Overview

Code Cain UML (Unified Modeling Language) Editor is a Java 21 application for creating and managing UML diagrams. It features a JavaFX GUI and an interactive CLI, supporting essential UML diagramming functionalities with undo/redo and file operations. It includes a syncronized GUI to CLI transition and vise versa through the interative GUI that allows the choice between the two on startup.

## Features

- **JavaFX GUI** for intuitive diagram editing.

- **CLI** with auto-complete commands.

- **Undo/Redo** support (CLI Only).

- **File Operations**: Save and load diagrams as JSON.

- **UML Management**:

  - Add, edit, and delete classes, fields, methods, and relationships.

  

## Usage

### Prerequisites

- Java 21 (Download: [Java Downloads](https://www.oracle.com/java/technologies/downloads/#java21))


### Run Instructions

  
#### Precompiled JAR File

For Windows users who can use the already compiled JAR file:

1. **Windows**:

   - Run the JAR file with the following command:

```bash
java -jar umlEditor.jar
```

   - Or double-click `umlEditor.jar` to launch the application.

  

#### Build Your Own JAR (Mac/Linux)

For Mac and Linux users who need to build their own JAR file:

  

1. **Prerequisites**:

   - Java 21 (Download: [Java Downloads](https://www.oracle.com/java/technologies/downloads/#java21)).

     - **Verify Installation**: Run the following command to ensure Java is installed:
```bash
java --version
```

   - Install Maven ([Install Maven](https://maven.apache.org/download.cgi)).
   - Choose the appropriate binary distribution for your system:
     - For Linux/Mac: Download the **Binary tar.gz archive**.
     - For Windows: Download the **Binary zip archive**.

  **Extract the Archive**:
   - Linux/Mac:
```bash
tar -xvzf apache-maven-3.9.9-bin.tar.gz
```
   - Windows:
     - Use a tool like WinRAR or 7-Zip to extract the `apache-maven-3.9.9-bin.zip` file.

   **Set Environment Variables**:
   - Add Maven's `bin` directory to your `PATH` environment variable:
     - **Linux/Mac**: Add the following to your `.bashrc`, `.zshrc`, or `.bash_profile`:
```bash
export PATH=/path/to/apache-maven-3.9.9/bin:$PATH
```

       Replace `/path/to/apache-maven-3.9.9` with the path to the extracted directory.
       Then, reload the shell configuration:
```bash
source ~/.bashrc
```

     - **Windows**:
       1. Open "System Properties" > "Environment Variables".
       2. Add Maven's `bin` directory to the `Path` variable.

   **Verify Installation**:
   Run the following command to confirm Maven is installed:
```bash
mvn -version
```
   You should see the Maven version and Java version in the output.  

2. **Clone the Repository**:

```bash
git clone https://github.com/hbarton2/cscd350-f24-CodeCain.git
cd codecain-uml-editor
```

  

3. **Build the JAR**:

   Run the Maven package command in the project directory:

```bash
mvn clean package
```

  

4. **Locate the JAR**:

   - After building, the JAR file will be in the `target` directory:

```bash
target/umlEditor.jar
```

  

5. **Run the Application**:

   Execute the JAR file:
```bash
java -jar target/umlEditor.jar
```

---
  

## Project Structure

- **`src/main/java/codecain`**: Core application code.

  - **BackendCode**: UML data and state management (e.g., `UMLClass.java`, `StateManager.java`).

  - **CommandLineInterface**: CLI logic and display (e.g., `CommandManager.java`, `CLI.java`).

  - **GraphicalUserInterface**: JavaFX-based GUI components (e.g., `GraphicalInterfaceJavaFX.java`, `ClassNode.java`).

  - **Main.java**: Application entry point.

- **`src/test/java/codecain`**: Unit tests.

- **`src/main/resources`**: FXML files and GUI assets.

  

## Future Enhancements

- Improved UI/UX.

- GUI Undo/Redo

  

## Contact

For questions or feedback, contact the maintainers:

- Aaron Williams-Breth, Sergei Uss, Colby Crutcher, Riley Rudolfo, Ben Foster, Aaron Oehler.

---