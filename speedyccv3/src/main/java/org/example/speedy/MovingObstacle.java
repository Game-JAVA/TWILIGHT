package org.example.speedy;


public class MovingObstacle extends Plataform {
    private double direction = 1;
    private final double speed = 3;

    public MovingObstacle(double x, double y, String imagePath) {
        super(x, y, imagePath);
        this.setX(x);
        this.setY(y);
        this.setFitWidth(70);
        this.setFitHeight(30);
    }

    public void update() {
        this.setX(this.getX() + direction * speed);
        if (this.getX() <= 0 || this.getX() >= 800 - this.getFitWidth()) {
            direction *= -1;
        }
    }
}