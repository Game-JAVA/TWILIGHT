package org.example.speedy;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// Classe que representa o jogador controlado pelo usuário
public class Player extends ImageView {
    private double velocityY; // Velocidade vertical do jogador
    private double velocityX; // Velocidade horizontal do jogador
    public double accelerationX; // Aceleração horizontal do jogador
    private final double gravity = 0.6; // Gravidade aplicada ao jogador
    private final double jumpStrength = -17.7; // Força do pulo do jogador
    private final double friction = 0.8; // Atrito aplicado ao jogador
    private boolean onPlataform; // Verifica se o jogador está em uma plataforma
    private int plataformsJumped; // Contador de plataformas puladas
    private Rectangle collisionBox; // Caixa de colisão
    private SoundEffect jumpSound; // Referência para o som de pulo

    // Construtor do jogador
    public Player(String imagePath, SoundEffect jumpSound) {
        Image image = new Image(imagePath); // Carrega a imagem do jogador
        this.setImage(image); // Define a imagem do jogador
        this.setFitWidth(50); // Ajusta a largura conforme necessário
        this.setFitHeight(50); // Ajusta a altura conforme necessário
        this.velocityY = 0; // Inicia com velocidade vertical zero
        this.velocityX = 0; // Inicia com velocidade horizontal zero
        this.accelerationX = 0; // Inicia com aceleração horizontal zero
        this.setX(375); // Define a posição inicial X (centro horizontal da tela)
        this.setY(220); // Define a posição inicial Y (base da tela)
        this.onPlataform = false; // Inicia fora de uma plataforma
        this.plataformsJumped = 0; // Inicia com zero plataformas puladas
        this.jumpSound = jumpSound; // Inicializa o jumpSound recebido como parâmetro


        // Cria uma caixa de colisão invisível para o jogador
        double collisionBoxWidth = this.getFitWidth();
        double collisionBoxHeight = this.getFitHeight() / 3; // Altura para capturar a base da imagem
        collisionBox = new Rectangle(collisionBoxWidth, collisionBoxHeight);
        collisionBox.setFill(Color.TRANSPARENT); // Torna a caixa de colisão invisível
        updateCollisionBox(); // Atualiza a posição da caixa de colisão
    }

    // Método para fazer o jogador pular
    public void jump() {
        this.velocityY = jumpStrength; // Define a velocidade vertical para o pulo
        this.onPlataform = false; // O jogador não está mais em uma plataforma após pular
        jumpSound.play(); // Reproduz o som de pulo
    }

    // Método para atualizar a posição do jogador
    public void update() {
        if (!onPlataform) {
            this.velocityY += gravity; // Aplica a gravidade se não estiver em uma plataforma
        }
        this.velocityX += accelerationX; // Aplica a aceleração horizontal
        this.velocityX *= friction; // Aplica o atrito para suavizar o movimento horizontal

        this.setX(this.getX() + this.velocityX); // Atualiza a posição horizontal do jogador
        this.setY(this.getY() + this.velocityY); // Atualiza a posição vertical do jogador
        updateCollisionBox(); // Atualiza a posição da caixa de colisão
    }

    // Método para atualizar a posição da caixa de colisão
    public void updateCollisionBox() {
        collisionBox.setX(this.getX()); // Define a posição X da caixa de colisão
        collisionBox.setY(this.getY() + (this.getFitHeight() - collisionBox.getHeight())); // Define a posição Y da caixa de colisão
    }

    // Métodos para mover o jogador para a esquerda e direita
    public void moveLeft() {
        this.accelerationX = -1.5; // Define a aceleração horizontal para a esquerda
    }

    public void moveRight() {
        this.accelerationX = 1.5; // Define a aceleração horizontal para a direita
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

    public Rectangle getCollisionBox() {
        return collisionBox;
    }
}
