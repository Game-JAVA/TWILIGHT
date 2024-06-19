import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


// Atributos
public class Player extends Rectangle {
    private String name;
    private int points;
    private int timeSurvived;
    private double velocityY;
    private double gravity = 0.5;
    private double jumpStrength = -10;

    public Player(String name, double width, double height, Color color) {
        super(width, height, color);
        this.name = name;
        this.points = 0;
        this.timeSurvived = 0;
        this.velocityY = 0;

        // Setando a posicão inicial
        this.setX(360); // posição X
        this.setY(500); // posição Y
    }

    // Método para pular
    public void jump() {
        this.velocityY = jumpStrength;
    }

    // Método para atualizar a velocidade (cair/efeito de gravidade)
    public void updateVelocity() {
        this.velocityY += gravity;
    }

    // Método para mover na horizontal
    public void move(double x) {
        this.setX(this.getX() + x);

        // Checando limites da tela
        if (this.getX() < 0) {
            this.setX(0);
        } else if (this.getX() > 800 - this.getWidth()) { // Presumindo tela de 800
            this.setX(800 - this.getWidth());
        }
    }

    // Método p/ atualizar a posição do player
    public void updatePosition() {
        this.updateVelocity();
        this.setY(this.getY() + this.velocityY);

        //Verifica se o player saiu da tela (caiu)
        if (this.getY() > 600) { // Altura tela em 600
            gameOver();
        }
    }

    // Método para o game over
    public void gameOver() {
        System.out.println("Game Over! " + name + " scored " + points + " points and survived for " + timeSurvived + " seconds.");

        //Lógica adi. de game over
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getTimeSurvived() {
        return timeSurvived;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }
}
