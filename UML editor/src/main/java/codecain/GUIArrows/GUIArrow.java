package codecain.GUIArrows;


import javafx.scene.shape.Polygon;

public abstract class GUIArrow extends Polygon {

    public GUIArrow(){
        drawArrow();
    }

    protected abstract GUIArrow drawArrow();

    public void setRotation(double degrees){
        setRotate(degrees);
    }

    public void setPosition(double x, double y){
        setTranslateX(x);
        setTranslateY(y);
    }

}
