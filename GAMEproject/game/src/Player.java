import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends ImageView {
    private double velocityY; // Velocidade vertical do jogador
    private double velocityX; // Velocidade horizontal do jogador
    public double accelerationX; // Aceleração horizontal do jogador
    private final double gravity = 0.5; // Gravidade aplicada ao jogador
    private final double jumpStrength = -16.6; // Força do pulo do jogador
    private final double friction = 0.8; // Atrito aplicado ao jogador
    private boolean onPlataform; // Verifica se o jogador está em uma plataforma
    private int plataformsJumped; // Contador de plataformas puladas
    private Rectangle collisionBox; // Caixa de colisão

    public Player(String imagePath) {
        Image image = new Image(imagePath);
        this.setImage(image);
        this.setFitWidth(50); // Ajuste o tamanho conforme necessário
        this.setFitHeight(50); // Ajuste o tamanho conforme necessário
        this.velocityY = 0; // Inicia com velocidade zero
        this.velocityX = 0; // Velocidade inicial horizontal
        this.accelerationX = 0; // Aceleração inicial horizontal
        this.setX(375); // Posição inicial X (centro horizontal da tela)
        this.setY(220); // Posição inicial Y (na base da tela)
        this.onPlataform = false;
        this.plataformsJumped = 0;

        // Cria a caixa de colisão invisível
        double collisionBoxWidth = this.getFitWidth();
        double collisionBoxHeight = this.getFitHeight() / 3; // altura para capturar a base da imagem
        collisionBox = new Rectangle(collisionBoxWidth, collisionBoxHeight);
        collisionBox.setFill(Color.TRANSPARENT); // Torna a caixa de colisão invisível
        updateCollisionBox(); // Inicializa a posição da caixa de colisão
    }

    // Função para o jogador pular
    public void jump() {
        this.velocityY = jumpStrength;
        this.onPlataform = false; // O jogador não está mais em uma plataforma após pular
    }

    // Atualiza a posição do jogador
    public void update() {
        if (!onPlataform) {
            this.velocityY += gravity; // Aplica gravidade se não estiver em uma plataforma
        }
        this.velocityX += accelerationX; // Aplica aceleração horizontal
        this.velocityX *= friction; // Aplica o atrito para suavizar o movimento horizontal

        this.setX(this.getX() + this.velocityX);
        this.setY(this.getY() + this.velocityY);
        this.updateCollisionBox(); // Atualiza a posição da caixa de colisão
    }

    // Atualiza a posição da caixa de colisão
    public void updateCollisionBox() {
        collisionBox.setX(this.getX());
        collisionBox.setY(this.getY() + (this.getFitHeight() - collisionBox.getHeight()));
    }

    // Métodos para definir a aceleração horizontal
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
