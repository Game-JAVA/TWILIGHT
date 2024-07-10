import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Plataform extends Rectangle {
    public Plataform(double x, double y, double width, double height, Color color) {
        super(width, height, color);
        this.setX(x);
        this.setY(y);
    }
}
