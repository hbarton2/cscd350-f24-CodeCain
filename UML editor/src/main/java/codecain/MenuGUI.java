package codecain;

import codecain.CommandLineInterface.CLI;
import codecain.GraphicalUserInterface.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuGUI extends JFrame {
    private CLI cli; // Declare CLI without instantiation

    public MenuGUI() {
        setTitle("UML Manager");
        setSize(1300, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to Code Cain UML Editor", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(welcomeLabel, BorderLayout.NORTH);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // CLI Button
        JButton cliButton = new JButton("CLI");
        cliButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Instantiate CLI when the button is clicked
                if (cli == null) {
                    cli = new CLI(); // Create CLI instance
                }
                cli.setVisible(true); // Show the CLI interface
                MenuGUI.this.setVisible(false);
            }
        });
        buttonPanel.add(cliButton);

        // GUI Button
        JButton guiButton = new JButton("Graphical Interface");
        guiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the graphical interface
                GUI gui = new GUI();
                gui.setVisible(true);
                MenuGUI.this.setVisible(false);
            }
        });
        buttonPanel.add(guiButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuGUI mainMenu = new MenuGUI();
            mainMenu.setVisible(true);
        });
    }
}
