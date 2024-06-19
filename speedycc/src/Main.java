import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Speedy CoCat Test");

        AnchorPane root = new AnchorPane();

        // Cria o jogador (gato) como um retângulo
        Player player = new Player("Cat", 50, 50, Color.BLUE);
        root.getChildren().add(player);

        // Cria uma plataforma
        Platform platform = new Platform(150, 300, 200, 20);
        root.getChildren().add(platform);

        // Configura a cena
        Scene scene = new Scene(root, 800, 600);

        // Faz a leitura das teclas pressionadas para mover o jogador
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    player.move(-5); // Move para a esquerda
                    break;
                case RIGHT:
                    player.move(5); // Move para a direita
                    break;
                case SPACE:
                    player.jump(); // Pula
                    break;
            }
        });

        // Atualiza a posição do jogador periodicamente
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                player.updatePosition();
                checkCollision(player, platform);
            }
        };
        timer.start();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void checkCollision(Player player, Platform platform) {
        if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
            // verica se o player saiu da plataforma
            if (player.getVelocityY() > 0) {
                player.setY(platform.getY() - player.getHeight());
                player.setVelocityY(0);
            }
        }
    }
}
