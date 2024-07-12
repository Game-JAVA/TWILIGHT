package org.example.speedy;

// Classe que representa um obstáculo móvel no jogo, derivado de Plataform
public class MovingObstacle extends Plataform {
    private double direction = 1; // Direção do movimento do obstáculo (1 para direita, -1 para esquerda)
    private final double speed = 5; // Velocidade do obstáculo

    // Construtor do obstáculo móvel
    public MovingObstacle(double x, double y, String imagePath) {
        super(x, y, imagePath); // Chama o construtor da superclasse Plataform
        this.setX(x); // Define a posição X inicial do obstáculo
        this.setY(y); // Define a posição Y inicial do obstáculo
        this.setFitWidth(70); // Ajusta a largura conforme necessário
        this.setFitHeight(30); // Ajusta a altura conforme necessário
    }

    // Método para atualizar a posição do obstáculo móvel
    public void update() {
        this.setX(this.getX() + direction * speed); // Atualiza a posição X com base na direção e velocidade

        // Verifica se o obstáculo atingiu as bordas da tela e inverte a direção
        if (this.getX() <= 0 || this.getX() >= 800 - this.getFitWidth()) {
            direction *= -1;
        }
    }
}
