import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends Rectangle {
    private double velocityY; // Velocidade vertical do jogador
    private final double gravity = 0.5; // Gravidade aplicada ao jogador
    private final double jumpStrength = -17; // Força do pulo do jogador
    private boolean onPlataform; // Verifica se o jogador está em uma plataforma
    private int plataformsJumped; // Contador de plataformas puladas

    public Player(double width, double height, Color color) {
        super(width, height, color);
        this.velocityY = jumpStrength; // Inicia pulando automaticamente
        this.setX(375); // Posição inicial X (centro horizontal da tela)
        this.setY(200); // Posição inicial Y (na base da tela)*
        this.onPlataform = false;
        this.plataformsJumped = 0;
    }

    // Função para o jogador pular
    public void jump() {
        this.velocityY = jumpStrength;
    }

    // Atualiza a posição do jogador
    public void update() {
        if (!onPlataform) {
            this.velocityY += gravity;
        }
        this.setY(this.getY() + this.velocityY);
    }

    // Getters e setters
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
