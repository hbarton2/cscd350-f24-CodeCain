package codecain.GraphicalUserInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClassBox extends JPanel {
    public ClassBox(String className, JPanel canvas) {
        setLayout(new BorderLayout());
        setBounds(50, 50, 200, 150);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.LIGHT_GRAY);

        JLabel classNameLabel = new JLabel(className, SwingConstants.CENTER);
        add(classNameLabel, BorderLayout.NORTH);

        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        add(detailsArea, BorderLayout.CENTER);

        addDragFunctionality(this, canvas);
    }

    private void addDragFunctionality(JPanel classBox, JPanel canvas) {
        final Point initialClick = new Point();
        classBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick.setLocation(e.getPoint());
            }
        });

        classBox.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int xMove = e.getX() - initialClick.x;
                int yMove = e.getY() - initialClick.y;

                Point location = classBox.getLocation();
                int newX = Math.max(0, Math.min(location.x + xMove, canvas.getWidth() - classBox.getWidth()));
                int newY = Math.max(0, Math.min(location.y + yMove, canvas.getHeight() - classBox.getHeight()));

                classBox.setLocation(newX, newY);
                canvas.repaint();
            }
        });
    }
}
