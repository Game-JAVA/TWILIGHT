import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class Platform extends Rectangle {
    public Platform(double x, double y, double width, double height) {
        super(width, height, Color.GREEN);
        this.setX(x);
        this.setY(y);
    }
}
