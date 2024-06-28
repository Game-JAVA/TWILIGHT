import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Application {
    private Player player; // Instância do jogador
    private List<Plataform> plataforms; // Lista de plataformas
    private List<MovingObstacle> movingObstacles; // Lista de obstáculos móveis
    private Pane gamePane; // Pane do jogo
    private final int windowWidth = 800; // Largura da janela
    private final int windowHeight = 600; // Altura da janela
    private double lastPlataformY = 500; // Posição Y da última plataforma

    public static void main(String[] args) {
        launch(args); // Inicializa o JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Speedy CoCat"); // Título da janela

        gamePane = new Pane();
        Scene scene = new Scene(gamePane, windowWidth, windowHeight);
        player = new Player("sprites/sprite gato2.png"); // Criação do jogador com a sprite
        gamePane.getChildren().add(player); // Adiciona o jogador ao gamePane
        gamePane.getChildren().add(player.getCollisionBox()); // Adiciona a caixa de colisão ao gamePane

        plataforms = new ArrayList<>();
        movingObstacles = new ArrayList<>();
        createInitialPlataforms(); // Cria as plataformas iniciais
        createInitialMovingObstacles(); // Cria os obstáculos móveis iniciais

        // Configura o controle do jogador com teclas
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    player.setX(player.getX() - 50); // Move o jogador para a esquerda
                    player.updateCollisionBox(); // Atualiza a posição da caixa de colisão
                    break;
                case RIGHT:
                    player.setX(player.getX() + 50); // Move o jogador para a direita
                    player.updateCollisionBox(); // Atualiza a posição da caixa de colisão
                    break;
            }
        });

        // Timer para atualizar o jogo continuamente
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                player.update(); // Atualiza a posição do jogador
                updateMovingObstacles(); // Atualiza a posição dos obstáculos móveis
                checkCollisions(); // Verifica colisões
                scrollScreen(); // Move a tela conforme necessário

                // Verifica se o jogador caiu da tela e já pulou em pelo menos 2 plataformas
                if (player.getY() > windowHeight && player.getPlataformsJumped() >= 2) {
                    gameOver(); // Chama a função de Game Over
                    stop(); // Para o timer
                }
            }
        };
        timer.start(); // Inicia o timer

        primaryStage.setScene(scene);
        primaryStage.show(); // Mostra a janela
    }

    // Cria as plataformas iniciais
    private void createInitialPlataforms() {
        for (int i = 0; i < 5; i++) {
            double x = new Random().nextDouble() * (windowWidth - 100);
            double y = lastPlataformY - 100;
            Plataform plataform = new Plataform(x, y, "sprites/sprite nuvem2.png");
            plataforms.add(plataform);
            gamePane.getChildren().add(plataform);
            lastPlataformY = y;
        }
    }

    // Cria os obstáculos móveis iniciais
    private void createInitialMovingObstacles() {
        for (int i = 0; i < 3; i++) {
            double x = new Random().nextDouble() * (windowWidth - 100);
            double y = lastPlataformY - 100;
            MovingObstacle movingObstacle = new MovingObstacle(x, y, "sprites/spriteAviao.png");
            movingObstacles.add(movingObstacle);
            gamePane.getChildren().add(movingObstacle);
            lastPlataformY = y;
        }
    }

    // Atualiza a posição dos obstáculos móveis
    private void updateMovingObstacles() {
        for (MovingObstacle movingObstacle : movingObstacles) {
            movingObstacle.update();
        }
    }

    // Cria uma nova plataforma
    private void createPlataform() {
        double x = new Random().nextDouble() * (windowWidth - 100);
        double y = lastPlataformY - 100;
        Plataform plataform = new Plataform(x, y, "sprites/sprite nuvem2.png");
        plataforms.add(plataform);
        gamePane.getChildren().add(plataform);
        lastPlataformY = y;
    }

    // Verifica colisões entre o jogador, as plataformas e os obstáculos móveis
    private void checkCollisions() {
        player.setOnPlataform(false);
        for (Plataform plataform : plataforms) {
            if (player.getCollisionBox().getBoundsInParent().intersects(plataform.getBoundsInParent())) {
                if (player.getVelocityY() > 0) {
                    player.setY(plataform.getY() - player.getFitHeight());
                    player.setVelocityY(0);
                    player.setOnPlataform(true);
                    if (player.getY() < windowHeight) {
                        player.incrementPlataformsJumped();
                        player.jump();
                    }
                }
            }
        }
        for (MovingObstacle movingObstacle : movingObstacles) {
            if (player.getCollisionBox().getBoundsInParent().intersects(movingObstacle.getBoundsInParent())) {
                if (player.getVelocityY() > 0) {
                    player.setY(movingObstacle.getY() - player.getFitHeight());
                    player.setVelocityY(0);
                    player.setOnPlataform(true);
                    if (player.getY() < windowHeight) {
                        player.incrementPlataformsJumped();
                        player.jump();
                    }
                }
            }
        }
    }

    // Move a tela para cima conforme o jogador sobe
    private void scrollScreen() {
        if (player.getY() < windowHeight / 2) {
            double dy = windowHeight / 2 - player.getY();
            player.setY(windowHeight / 2);
            player.updateCollisionBox(); // Atualiza a posição da caixa de colisão

            for (Plataform plataform : plataforms) {
                plataform.setY(plataform.getY() + dy);
                if (plataform.getY() > windowHeight) {
                    gamePane.getChildren().remove(plataform);
                }
            }
            plataforms.removeIf(plataform -> plataform.getY() > windowHeight);

            for (MovingObstacle movingObstacle : movingObstacles) {
                movingObstacle.setY(movingObstacle.getY() + dy);
                if (movingObstacle.getY() > windowHeight) {
                    gamePane.getChildren().remove(movingObstacle);
                }
            }
            movingObstacles.removeIf(movingObstacle -> movingObstacle.getY() > windowHeight);

            lastPlataformY += dy;
            createPlataform();
            if (new Random().nextBoolean()) {
                createMovingObstacle();
            }
        }
    }

    // Cria um novo obstáculo móvel
    private void createMovingObstacle() {
        double x = new Random().nextDouble() * (windowWidth - 100);
        double y = lastPlataformY - 100;
        MovingObstacle movingObstacle = new MovingObstacle(x, y, "sprites/spriteAviao.png");
        movingObstacles.add(movingObstacle);
        gamePane.getChildren().add(movingObstacle);
        lastPlataformY = y;
    }

    // mensagem de Game Over
    private void gameOver() {
        System.out.println("TESTING ok");
        // lógica de game over aqui
    }
}
