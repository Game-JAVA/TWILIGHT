public class MovingObstacle extends Plataform {
    private double direction = 1; // Direção do movimento
    private final double speed = 2; // Velocidade do movimento

    public MovingObstacle(double x, double y, String imagePath) {
        super(x, y, imagePath);
        this.setX(x);
        this.setY(y);
        this.setFitWidth(70); // Ajuste o tamanho conforme necessário
        this.setFitHeight(30); // Ajuste o tamanho conforme necessário
    }

    // Atualiza a posição do obstáculo móvel
    public void update() {
        this.setX(this.getX() + direction * speed);
        if (this.getX() <= 0 || this.getX() >= 800 - this.getFitWidth()) {
            direction *= -1; // Inverte a direção ao atingir as bordas
        }
    }
}