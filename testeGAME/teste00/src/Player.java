import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends Rectangle {
    private double velocityY;
    private final double gravity = 0.5;
    private final double jumpStrength = -17;
    private boolean onPlataform;
    private int plataformsJumped;

    public Player(double width, double height, Color color) {
        super(width, height, color);
        this.velocityY = jumpStrength; // Inicia pulando automaticamente
        this.setX(375); // Posição inicial X (centro horizontal da tela)
        this.setY(200); // Posição inicial Y (na base da tela)
        this.onPlataform = false;
        this.plataformsJumped = 0;
    }

    public void jump() {
        this.velocityY = jumpStrength;
    }

    public void update() {
        if (!onPlataform) {
            this.velocityY += gravity;
        }
        this.setY(this.getY() + this.velocityY);
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public boolean isOnPlataform() {
        return onPlataform;
    }

    public void setOnPlataform(boolean onPlataform) {
        this.onPlataform = onPlataform;
    }

    public int getPlataformsJumped() {
        return plataformsJumped;
    }

    public void incrementPlataformsJumped() {
        this.plataformsJumped++;
    }
}
