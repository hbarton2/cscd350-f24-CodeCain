package codecain.GraphicalUserInterface;

import codecain.BackendCode.UMLClass;
import codecain.BackendCode.UMLClassInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.util.stream.Collectors;

public class ClassBox extends JPanel {
    private final JTextArea detailsArea;
    private final String className;

    public ClassBox(String className, JPanel canvas) {
        this.className = className;

        setLayout(new BorderLayout());
        setBounds(50, 50, 200, 150);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.LIGHT_GRAY);

        JLabel classNameLabel = new JLabel(className, SwingConstants.CENTER);
        add(classNameLabel, BorderLayout.NORTH);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        add(detailsArea, BorderLayout.CENTER);

        canvas.setLayout(null);
        canvas.add(this);
        canvas.repaint();

        enableDragging(canvas);

        updateDetails();
    }

    private void enableDragging(JPanel canvas) {
        final Point initialClick = new Point();

        // Mouse press listener to track initial click point
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                initialClick.setLocation(e.getPoint());
            }
        });

        // Mouse drag listener to update position
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                int xMove = e.getX() - initialClick.x;
                int yMove = e.getY() - initialClick.y;

                Point location = getLocation();
                int newX = location.x + xMove;
                int newY = location.y + yMove;

                // Ensure box stays within canvas bounds
                int canvasWidth = canvas.getWidth();
                int canvasHeight = canvas.getHeight();
                int boxWidth = getWidth();
                int boxHeight = getHeight();

                newX = Math.max(0, Math.min(newX, canvasWidth - boxWidth));
                newY = Math.max(0, Math.min(newY, canvasHeight - boxHeight));

                setLocation(newX, newY);
                canvas.repaint();
            }
        });
    }

    private void updateDetails() {
        UMLClassInfo umlClassInfo = UMLClass.classMap.get(className);
        if (umlClassInfo == null) {
            detailsArea.setText("Class information not available.");
            return;
        }

        StringBuilder detailsText = new StringBuilder("Fields:\n");
        umlClassInfo.getFields().forEach(field ->
                detailsText.append(field.getFieldName()).append(": ").append(field.getFieldType()).append("\n"));

        detailsText.append("\nMethods:\n");
        umlClassInfo.getMethods().forEach(method -> {
            detailsText.append(method.getMethodName()).append("(");
            detailsText.append(method.getParameters().stream()
                    .map(param -> param.getParameterType() + " " + param.getParameterName())
                    .collect(Collectors.joining(", ")));
            detailsText.append(")\n");
        });

        detailsArea.setText(detailsText.toString());
    }
}
