import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Obstacle extends Rectangle {
    private double speed;

    public Obstacle(double x, double y, double width, double height, double speed, Color color) {
        super(width, height, color);
        this.setX(x);
        this.setY(y);
        this.speed = speed;
    }

    public void move() {
        this.setX(this.getX() + speed);

        // Inverter a direção se atingir os limites da tela
        if (this.getX() <= 0 || this.getX() >= 800 - this.getWidth()) {
            speed = -speed;
        }
    }
}