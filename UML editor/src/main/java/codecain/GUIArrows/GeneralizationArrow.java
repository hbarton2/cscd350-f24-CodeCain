package codecain.GUIArrows;

public class GeneralizationArrow extends GUIArrow{
    @Override
    protected GUIArrow drawArrow() {
        getPoints().clear();

        double arrowheadWidth = 10.0;
        double arrowheadHeight = 15.0;
        double stemLength = 50.0;

        getPoints().addAll(
                0.0, 0.0,
                -arrowheadWidth / 2, arrowheadHeight,
                0.0, arrowheadHeight / 1.5,
                arrowheadWidth / 2, arrowheadHeight,
                0.0, 0.0,
                0.0, stemLength
        );

        return this;
    }

}
