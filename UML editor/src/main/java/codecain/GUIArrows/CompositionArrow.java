package codecain.GUIArrows;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class CompositionArrow extends GUIArrow{
    @Override
    protected GUIArrow drawArrow() {
        getPoints().clear();

        getPoints().addAll(
                0.0,0.0,
                -12.0,6.0,
                0.0,24.0,
                12.0,6.0
        );

        setStroke(Color.BLACK);
        setFill(Color.BLACK);
        setStrokeWidth(2);
        return this;
    }

}
