package codecain;

import javax.swing.*;
import java.awt.*;


/*

    Change the canvas JPanel to a RelationshipPanel, and add canvas.repaint into the addRelationship method
    in order to get this to work.
    Still need to figure out how to get the arrows to not overlap. Will probably add some data structure in
    the Relationship class for that or something

*/
public class RelationshipCanvas extends JPanel {


    /**
     * Graphical interface
     */
    private final GraphicalInterface gui;

    /**
     * the spacing of the arrows
     */
    private final int arrowSpacing = 32;

    /**
     * creates a dashed line
     */
    private final Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
            0, new float[]{9}, 0);

    /**
     * creates a solid line
     */
    private final Stroke solid = new BasicStroke(2);


    public RelationshipCanvas(GraphicalInterface gui){
        this.gui = gui;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        updateLines(g2d);
    }

    private void updateLines(Graphics2D g){
        for (Relationship r : Relationship.relationshipList){
            String[] names = r.getClassNamesAsArray();

            ClassPanel panel1 =  gui.classPanels.get(names[0]);
            ClassPanel panel2 = gui.classPanels.get(names[1]);

            Point c1Location = panel1.getLocation();
            Point c2Location = panel2.getLocation();

            int x1 = c1Location.x + getXposition(panel1,r);
            int y1 = c1Location.y + panel1.getHeight() +24;
            int x2 = c2Location.x + getXposition(panel2,r);
            int y2 = c2Location.y + panel2.getHeight() +24;

            drawUMLLine(r.getType(),x1,y1,x2,y2,g);
            drawArrow(r.getType(),x1,y1,g);

        }
    }

    /**
     *
     */
    private int getXposition(ClassPanel panel1, Relationship r){
        int relationshipCount = panel1.attachedRelationshipIndices.size();
        int index = panel1.attachedRelationshipIndices.get(r);
        return ( panel1.getWidth() / (2 + relationshipCount - 1) ) * (index+1);

    }


    /**
     *
     */
    private void drawUMLLine(RelationshipType type, int x1, int y1, int x2, int y2, Graphics g){
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setStroke(solid);
        g2d.drawLine(x1,y1,x1,y1-24);
        g2d.drawLine(x2,y2,x2,y2-24);
        if (type.equals(RelationshipType.REALIZATION)){
            g2d.setStroke(dashed);
        }
        else{
            g2d.setStroke(solid);
        }
        g2d.drawLine(x1,y1,x2,y2);
        g2d.dispose();
    }


    private void drawArrow(RelationshipType type, int x, int y, Graphics2D g){
        int[] diamondx = {x   , x-6 , x, x+6 };
        int[] diamondy = {y-24, y-12, y, y-12};

        switch(type){
            case COMPOSITION -> {
                g.drawPolygon(diamondx,diamondy,4);
                g.setColor(Color.BLACK);
                g.fillPolygon(diamondx,diamondy,4);
            }
            case AGGREGATION -> {
                g.drawPolygon(diamondx,diamondy,4);
                g.setColor(Color.WHITE);
                g.fillPolygon(diamondx,diamondy,4);
                g.setColor(Color.BLACK);
            }
            case GENERALIZATION -> {
                g.drawLine(x,y-24,x-12,y-12);
                g.drawLine(x,y-24,x+12,y-12);
                g.drawLine(x,y,x,y-24);
            }
            case REALIZATION -> {
                int[] trianglex = {x , x-12, x+12};
                int[] triangley = {y-24, y, y};

                g.setColor(Color.BLACK);
                g.drawPolygon(trianglex,triangley,3);
                g.setColor(Color.WHITE);
                g.fillPolygon(trianglex,triangley,3);
                g.setColor(Color.BLACK);
            }
        }
    }
}

//
//package codecain;
//
//import javax.swing.*;
//        import java.awt.*;
//        import java.util.HashMap;
//
//
///*
//
//    Change the canvas JPanel to a RelationshipPanel, and add canvas.repaint into the addRelationship method
//    in order to get this to work.
//    Still need to figure out how to get the arrows to not overlap. Will probably add some data structure in
//    the Relationship class for that or something
//
//*/
//public class RelationshipCanvas extends JPanel {
//
//
//    private class RelationshipInfo{
//        HashMap<Relationship,Integer> attachedRelationships;
//        public RelationshipInfo(){
//            attachedRelationships = new HashMap<>();
//        }
//
//
//        public void addRelationshipPoint(Relationship r){
//            attachedRelationships.put(r, getLowestValue());
//            System.out.println("relationship added to class ");
//        }
//
//
//        public void removeRelationshipPoint(Relationship r){
//            attachedRelationships.remove(r);
//            System.out.println("relationship removed");
//        }
//
//
//        private Integer getLowestValue(){
//            int i = 0;
//            for (; attachedRelationships.containsValue(i); i++);
//            return i;
//        }
//
//    }

//
//    /**
//     * Graphical interface
//     */
//    private final GraphicalInterface gui;
//    private final int arrowSpacing = 32;
//
//    /**
//     * creates a dashed line
//     */
//    private final Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
//            0, new float[]{9}, 0);
//
//    /**
//     * creates a solid line
//     */
//    private final Stroke solid = new BasicStroke(2);
//
//    /**
//     * hashMap that contains all class names with their attached relationship info
//     */
//    private HashMap<String, RelationshipInfo> classes;
//
//
//    public RelationshipCanvas(GraphicalInterface gui){
//        this.gui = gui;
//    }
//
//    @Override
//    protected void paintComponent(Graphics g){
//        super.paintComponent(g);
//        Graphics2D g2d = (Graphics2D)g;
//        updateLines(g2d);
//    }
//
//    private void updateLines(Graphics2D g){
//        for (Relationship r : Relationship.relationshipList){
//            String[] names = r.getClassNamesAsArray();
//
//            ClassPanel panel1 =  gui.classPanels.get(names[0]);
//            ClassPanel panel2 = gui.classPanels.get(names[1]);
//
//            Point c1Location = panel1.getLocation();
//            Point c2Location = panel2.getLocation();
//
//            //int x1 = c1Location.x +panel1.getWidth()/2 + (arrowSpacing*panel1.attachedRelationships.get(r));
//            int x1 = c1Location.x +panel1.getWidth()/2 + (arrowSpacing*panel1.attachedRelationships.get(r));
//            int y1 = c1Location.y +panel1.getHeight() +24;
//            //int x2 = c2Location.x + panel2.getWidth()/2 + (arrowSpacing*panel2.attachedRelationships.get(r));
//            int x2 = c2Location.x + panel2.getWidth()/2 + (arrowSpacing*panel2.attachedRelationships.get(r));
//            int y2 = c2Location.y + panel2.getHeight() +24;
//
//            drawUMLLine(r.getType(),x1,y1,x2,y2,g);
//            drawArrow(r.getType(),x1,y1,g);
//
//        }
//    }
//
//    /**
//     *
//     * @param r the relationship to add
//     */
//    public void addRelationshipInfo(Relationship r){
//        String[] names = r.getClassNamesAsArray();
//        for (int i = 0; i < 2; i++) {
//            if (!classes.containsValue(names[i])) {
//                RelationshipInfo newClassInfo = new RelationshipInfo();
//                classes.put(names[i], newClassInfo);
//            }
//            classes.get(names[i]).attachedRelationships.put(r, classes.get(names[i]).getLowestValue());
//        }
//    }
//
//    private void drawUMLLine(RelationshipType type, int x1, int y1, int x2, int y2, Graphics g){
//        Graphics2D g2d = (Graphics2D) g.create();
//
//        g2d.setStroke(solid);
//        g2d.drawLine(x1,y1,x1,y1-24);
//        g2d.drawLine(x2,y2,x2,y2-24);
//        if (type.equals(RelationshipType.REALIZATION)){
//            g2d.setStroke(dashed);
//        }
//        else{
//            g2d.setStroke(solid);
//        }
//        g2d.drawLine(x1,y1,x2,y2);
//        g2d.dispose();
//    }
//
//    private void drawArrow(RelationshipType type, int x, int y, Graphics2D g){
//        int[] diamondx = {x   , x-6 , x, x+6 };
//        int[] diamondy = {y-24, y-12, y, y-12};
//
//        switch(type){
//            case COMPOSITION -> {
//                g.drawPolygon(diamondx,diamondy,4);
//                g.setColor(Color.BLACK);
//                g.fillPolygon(diamondx,diamondy,4);
//            }
//            case AGGREGATION -> {
//                g.drawPolygon(diamondx,diamondy,4);
//                g.setColor(Color.WHITE);
//                g.fillPolygon(diamondx,diamondy,4);
//                g.setColor(Color.BLACK);
//            }
//            case GENERALIZATION -> {
//                g.drawLine(x,y-24,x-12,y-12);
//                g.drawLine(x,y-24,x+12,y-12);
//                g.drawLine(x,y,x,y-24);
//            }
//            case REALIZATION -> {
//                int[] trianglex = {x , x-12, x+12};
//                int[] triangley = {y-24, y, y};
//
//                g.setColor(Color.BLACK);
//                g.drawPolygon(trianglex,triangley,3);
//                g.setColor(Color.WHITE);
//                g.fillPolygon(trianglex,triangley,3);
//                g.setColor(Color.BLACK);
//            }
//        }
//    }
//}

