package codecain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuGUI extends JFrame {
    private CLI cli; // Declare CLI without instantiation

    public MenuGUI() {
        setTitle("UML Manager");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to Code Cain UML Editor", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(welcomeLabel, BorderLayout.NORTH);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // GUI Button
        JButton guiButton = new JButton("Graphical Interface");
        guiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the graphical interface
                GraphicalInterface gui = new GraphicalInterface();
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
