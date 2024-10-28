package codecain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphicalInterface extends JFrame {
    private JPanel canvas;
    private JPanel controlsPanel;
    private JPanel classesPanel;

    public GraphicalInterface() {
        setTitle("Class Diagram Editor");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        controlsPanel = new JPanel();
        controlsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");

        controlsPanel.add(saveButton);
        controlsPanel.add(loadButton);

        add(controlsPanel, BorderLayout.NORTH);

        canvas = new JPanel();
        canvas.setBackground(Color.WHITE);
        add(canvas, BorderLayout.CENTER);

        classesPanel = new JPanel();
        classesPanel.setLayout(new BoxLayout(classesPanel, BoxLayout.Y_AXIS));

        JButton addClassButton = new JButton("Add Class");
        JButton deleteClassButton = new JButton("Remove Class");

        classesPanel.add(addClassButton);
        classesPanel.add(deleteClassButton);

        add(classesPanel, BorderLayout.EAST);

        addClassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addClass();
            }
        });

        deleteClassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteClass();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDiagram();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDiagram();
            }
        });
    }

    private void addClass() {
        String className = JOptionPane.showInputDialog(this, "Enter the name of the class to add:");
        if (className != null && !className.trim().isEmpty()) {
            UMLClass.addClass(className);
            JOptionPane.showMessageDialog(this, "Class '" + className + "' added.");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid class name. Class not added.");
        }
    }

    private void deleteClass() {
        String className = JOptionPane.showInputDialog(this, "Enter the name of the class to delete:");
        if (className != null && !className.trim().isEmpty()) {
            UMLClass.removeClass(className);
            JOptionPane.showMessageDialog(this, "Class '" + className + "' deleted.");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid class name. Class not deleted.");
        }
    }

    private void saveDiagram() {
        String fileName = JOptionPane.showInputDialog(this, "Enter file name to save:");
        if (fileName != null && !fileName.trim().isEmpty()) {
            SaveManager.saveToJSON(fileName + ".json");
            JOptionPane.showMessageDialog(this, "Diagram saved as " + fileName + ".json");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid file name. Diagram not saved.");
        }
    }

    private void loadDiagram() {
        JOptionPane.showMessageDialog(this, "Load button clicked.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphicalInterface window = new GraphicalInterface();
            window.setVisible(true);
        });
    }
}
