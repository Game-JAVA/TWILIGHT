import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
    private int score = 0; // Atributo de pontuação
    private Text scoreText; // Txt pontuação
    private SoundEffect backgroundSound = new SoundEffect("sounds/backgroundmusic2.wav", true);
    private SoundEffect jumpSound = new SoundEffect("sounds/jumpsound2 (1).wav", false);
    private SoundEffect gameoverSound = new SoundEffect("sounds/gameoversound.wav", false);
    private ImageView backgroundImageView = new ImageView("sprites/backgroungame.png"); //imagem de fundo

    public static void main(String[] args) {
        launch(args); // Inicializa o JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Speedy CoCat"); // Título da janela

        // Objeto som
        backgroundSound.play();

        gamePane = new Pane();
        Scene scene = new Scene(gamePane, windowWidth, windowHeight);
        scene.setFill(Color.rgb(0,183,240));

        // Adiciona a imagem de fundo
        Image backgroundImage = new Image("sprites/backgroungame.png");
        backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(windowWidth);
        backgroundImageView.setFitHeight(windowHeight);
        gamePane.getChildren().add(backgroundImageView); // Adiciona a imagem de fundo ao gamePane

        player = new Player("sprites/sprite gato2.png"); // Criação do jogador com a sprite
        gamePane.getChildren().add(player); // Adiciona o jogador ao gamePane
        gamePane.getChildren().add(player.getCollisionBox()); // Adiciona a caixa de colisão ao gamePane

        plataforms = new ArrayList<>();
        movingObstacles = new ArrayList<>();
        createInitialPlataforms(); // Cria as plataformas iniciais
//        createInitialMovingObstacles(); // Cria os obstáculos móveis iniciais

        // Inicializa o txt de pontuação
        scoreText = new Text(10, 20, "Pontuação: 0");
        scoreText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        gamePane.getChildren().add(scoreText);

        // Configura o controle do jogador com teclas
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    player.moveLeft(); // Aplica aceleração para a esquerda
                    break;
                case RIGHT:
                    player.moveRight(); // Aplica aceleração para a direita
                    break;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT:
                case RIGHT:
                    player.accelerationX = 0; // Para a aceleração quando a tecla é solta
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
        for (int i = 0; i < 12; i++) {
            double x = new Random().nextDouble() * (windowWidth - 100);
            double y = lastPlataformY - 50;
            Plataform plataform = new Plataform(x, y, "sprites/spritegrama.png");
            plataforms.add(plataform);
            gamePane.getChildren().add(plataform);
            lastPlataformY = y;
        }
    }

    // Cria os obstáculos móveis iniciais


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

    // Verifica colisões entre o jogador, plataformas e obstáculos móveis
    private void checkCollisions() {
        player.setOnPlataform(false);
        for (Plataform plataform : plataforms) {
            if (player.getCollisionBox().getBoundsInParent().intersects(plataform.getBoundsInParent())) {
                if (player.getVelocityY() > 0) {
                    player.setY(plataform.getY() - player.getFitHeight());
                    player.setVelocityY(0);
                    player.setOnPlataform(true);
                    jumpSound.play();

                    if (player.getY() < windowHeight) {
                        player.incrementPlataformsJumped();
                        player.jump();

                        // Incrementa a pontuação e atualiza
                        score += 1; //pontos por plataforma
                        scoreText.setText("Pontuação: " + score);
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
                    jumpSound.play();

                    if (player.getY() < windowHeight) {
                        player.incrementPlataformsJumped();
                        player.jump();

                        // Incrementa a pontuação e atualiza
                        score += 1; //pontos por obstáculo móvel
                        scoreText.setText("Pontuação: " + score);
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

            backgroundImageView.setY(backgroundImageView.getY() + dy); // Atualiza a posição da imagem de fundo

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

    //lógica de fim de jogo
    private void gameOver() {
        gameoverSound.play();
        Text gameOverText = new Text(windowWidth / 2 - 50, windowHeight / 2, "Game Over");
        gameOverText.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");
        gamePane.getChildren().add(gameOverText);
    }
}
