package org.example.speedy;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends ImageView {
    private double velocityY; // Velocidade vertical do jogador
    private final double gravity = 0.5; // Gravidade aplicada ao jogador
    private final double jumpStrength = -17; // Força do pulo do jogador
    private boolean onPlataform; // Verifica se o jogador está em uma plataforma
    private int plataformsJumped; // Contador de plataformas puladas
    private Rectangle collisionBox; // Caixa de colisão

    public Player(String imagePath) {
        Image image = new Image(imagePath);
        this.setImage(image);
        this.setFitWidth(50); // Ajuste o tamanho conforme necessário
        this.setFitHeight(50); // Ajuste o tamanho conforme necessário
        this.velocityY = jumpStrength; // Inicia pulando automaticamente
        this.setX(375); // Posição inicial X (centro horizontal da tela)
        this.setY(200); // Posição inicial Y (na base da tela)
        this.onPlataform = false;
        this.plataformsJumped = 0;

        // Cria a caixa de colisão invisível
        double collisionBoxWidth = this.getFitWidth();
        double collisionBoxHeight = this.getFitHeight() / 3; // Ajuste a altura para capturar a base da imagem
        collisionBox = new Rectangle(collisionBoxWidth, collisionBoxHeight);
        collisionBox.setFill(Color.TRANSPARENT); // Torna a caixa de colisão invisível
        updateCollisionBox(); // Inicializa a posição da caixa de colisão
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
        this.updateCollisionBox(); // Atualiza a posição da caixa de colisão
    }

    // Atualiza a posição da caixa de colisão
    public void updateCollisionBox() {
        collisionBox.setX(this.getX());
        collisionBox.setY(this.getY() + (this.getFitHeight() - collisionBox.getHeight()));
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
