package codecain.GUIArrows;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class AggregationArrow extends GUIArrow{
    @Override
    protected GUIArrow drawArrow() {
        getPoints().clear();

        getPoints().addAll(
                0.0, 0.0,
                100.0, 0.0
        );

        getPoints().addAll(
                90.0, -5.0,
                110.0, 0.0,
                90.0, 5.0,
                110.0, 0.0
        );

        setStroke(Color.BLACK);
        setFill(Color.WHITE);
        setStrokeWidth(2);
        return this;
    }

}
