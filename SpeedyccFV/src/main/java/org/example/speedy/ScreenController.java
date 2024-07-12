package org.example.speedy;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;

// Classe que controla as telas e a lógica do jogo
public class ScreenController {
    @FXML
    private Label gameTittle; // Título do jogo
    @FXML
    private Button playButton; // Botão de jogar
    @FXML
    private Button exitButton; // Botão de sair
    @FXML
    private Button settingsButton; // Botão de configurações
    @FXML
    private AnchorPane background; // Pano de fundo
    @FXML
    private AnchorPane screen2; // Segunda tela
    @FXML
    private AnchorPane gameOverScreen; // Tela de Game Over
    @FXML
    private ImageView cat; // Imagem do gato (personagem principal)
    private Scene scene; // Cena atual
    private Stage stage; // Estágio principal do jogo
    private FXMLLoader fxmlLoader; // Loader para carregar FXML
    private Player player; // Instância do jogador
    private List<Plataform> plataforms; // Lista de plataformas no jogo
    private List<MovingObstacle> movingObstacles; // Lista de obstáculos móveis
    private Pane gamePane; // Painel do jogo
    private final int windowWidth = 800; // Largura da janela
    private final int windowHeight = 600; // Altura da janela
    private double lastPlataformY = 500; // Última posição Y de uma plataforma
    private int score = 0; // Pontuação do jogador
    private Text scoreText; // Texto da pontuação
    private ImageView backgroundImageView; // Imagem de fundo do jogo
    private boolean isPaused = false; // Flag de pausa do jogo
    private boolean isGameOver = false; // Flag de game over
    private AnimationTimer timer; // Timer para atualizações do jogo
    private Scene gameScene; // Cena do jogo
    private Scene pauseScene; // Cena de pausa
    private SoundEffect backgroundMusic; // Efeito sonoro de fundo
    private SoundEffect jumpSound; // Efeito sonoro de pulo
    private SoundEffect gameOverSound; // Efeito sonoro de game over

    // Define o estágio principal do jogo
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Transição para a tela de carregamento inicial
    public void switchToLoad(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Game.class.getResource("GameLoad.fxml"));
        Pane root = fxmlLoader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, windowWidth, windowHeight);
        String css = "stylesheet.css";
        scene.getStylesheets().add(css);
        ImageView imageView = new ImageView(new Image("catrun.gif"));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(100);
        imageView.setFitHeight(75);
        AnchorPane.setBottomAnchor(imageView, 10.0);
        AnchorPane.setRightAnchor(imageView, 10.0);
        root.getChildren().add(imageView);
        stage.setTitle("Speddy CoCat");
        stage.setScene(scene);
        stage.show();

        // Transição para a tela de jogo após 4 segundos
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(e -> {
            try {
                gameStart();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        delay.play();
    }

    // Inicializa o jogo
    public void gameStart() throws IOException {
        stage.setTitle("Speedy CoCat");
        gamePane = new Pane();
        gameScene = new Scene(gamePane, windowWidth, windowHeight);
        gamePane.setBackground(new Background(new BackgroundFill(Color.rgb(0, 183, 240), CornerRadii.EMPTY, Insets.EMPTY)));

        backgroundImageView = new ImageView(new Image("backgroungame.png"));
        backgroundImageView.setFitWidth(windowWidth);
        backgroundImageView.setFitHeight(windowHeight);
        gamePane.getChildren().add(backgroundImageView);

        // Inicializa os efeitos sonoros
        backgroundMusic = new SoundEffect("backgroundmusic2.wav", true);
        jumpSound = new SoundEffect("jumpsound2 (1).wav", false);
        gameOverSound = new SoundEffect("gameoversound.wav", false);
        backgroundMusic.play();

        // Inicializa o jogador
        player = new Player("sprite gato2.png", jumpSound);
        gamePane.getChildren().add(player);
        gamePane.getChildren().add(player.getCollisionBox());

        plataforms = new ArrayList<>();
        movingObstacles = new ArrayList<>();
        createInitialPlataforms();

        // Define a posição inicial do jogador sobre a plataforma inicial
        Plataform initialPlataform = plataforms.get(0);
        player.setX(initialPlataform.getX() + (initialPlataform.getFitWidth() - player.getFitWidth()) / 2);
        player.setY(initialPlataform.getY() - player.getFitHeight());
        player.updateCollisionBox();

        scoreText = new Text(10, 20, "Pontuação: 0");
        scoreText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        gamePane.getChildren().add(scoreText);

        // Define ações do teclado durante o jogo
        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                togglePause();
            }
            if (!isPaused && !isGameOver) {
                switch (event.getCode()) {
                    case LEFT:
                        player.moveLeft();
                        break;
                    case RIGHT:
                        player.moveRight();
                        break;
                }
            }
        });

        gameScene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT:
                case RIGHT:
                    player.accelerationX = 0;
                    break;
            }
        });

        // Timer para atualização do jogo
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                player.update();
                updateMovingObstacles();
                checkCollisions();
                scrollScreen();

                // Verifica se o jogador caiu abaixo da tela
                if (player.getY() > windowHeight && player.getPlataformsJumped() >= 0) {
                    try {
                        gameOver();
                        stop();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        timer.start();

        // Mostra a cena do jogo
        stage.setScene(gameScene);
        stage.show();
    }

    // Cria as plataformas iniciais do jogo
    private void createInitialPlataforms() {
        // Adiciona a plataforma inicial na parte inferior da tela
        Plataform initialPlataform = new Plataform((windowWidth - 100) / 2, windowHeight - 20, "spritegrama.png");
        plataforms.add(initialPlataform);
        gamePane.getChildren().add(initialPlataform);

        for (int i = 0; i < 17; i++) {
            double x = new Random().nextDouble() * (windowWidth - 100);
            double y = lastPlataformY - 50;
            Plataform plataform = new Plataform(x, y, "spritegrama.png");
            plataforms.add(plataform);
            gamePane.getChildren().add(plataform);
            lastPlataformY = y;
        }
    }

    // Atualiza os obstáculos móveis
    private void updateMovingObstacles() {
        for (MovingObstacle movingObstacle : movingObstacles) {
            movingObstacle.update();
        }
    }

    // Cria uma nova plataforma
    private void createPlataform() {
        double x = new Random().nextDouble() * (windowWidth - 100);
        double y = lastPlataformY - 100;
        Plataform plataform = new Plataform(x, y, "sprite nuvem2.png");
        plataforms.add(plataform);
        gamePane.getChildren().add(plataform);
        lastPlataformY = y;
    }

    // Verifica as colisões do jogador com plataformas e obstáculos
    public void checkCollisions() {
        player.setOnPlataform(false);
        for (Plataform plataform : plataforms) {
            if (player.getCollisionBox().getBoundsInParent().intersects(plataform.getBoundsInParent())) {
                jumpSound.play();
                if (player.getVelocityY() > 0) {
                    player.setY(plataform.getY() - player.getFitHeight());
                    player.setVelocityY(0);
                    player.setOnPlataform(true);

                    if (player.getY() < windowHeight) {
                        player.incrementPlataformsJumped();
                        player.jump(); // Chama o método jump() do jogador
                        player.jump();
                        score += 1;
                        scoreText.setText("Pontuação: " + score);
                    }
                }
            }
        }
        for (MovingObstacle movingObstacle : movingObstacles) {
            if (player.getCollisionBox().getBoundsInParent().intersects(movingObstacle.getBoundsInParent())) {
                jumpSound.play();
                if (player.getVelocityY() > 0) {
                    player.setY(movingObstacle.getY() - player.getFitHeight());
                    player.setVelocityY(0);
                    player.setOnPlataform(true);

                    if (player.getY() < windowHeight) {
                        player.incrementPlataformsJumped();
                        player.jump(); // Chama o método jump() do jogador
                        player.jump();
                        score += 1;
                        scoreText.setText("Pontuação: " + score);
                    }
                }
            }
        }
    }

    // Desloca a tela conforme o jogador sobe
    public void scrollScreen() {
        if (player.getY() < windowHeight / 2) {
            double dy = windowHeight / 2 - player.getY();
            player.setY(windowHeight / 2);
            player.updateCollisionBox();

            backgroundImageView.setY(backgroundImageView.getY() + dy);

            // Move as plataformas
            for (Plataform plataform : plataforms) {
                plataform.setY(plataform.getY() + dy);
                if (plataform.getY() > windowHeight) {
                    gamePane.getChildren().remove(plataform);
                }
            }
            plataforms.removeIf(plataform -> plataform.getY() > windowHeight);

            // Move os obstáculos móveis
            for (MovingObstacle movingObstacle : movingObstacles) {
                movingObstacle.setY(movingObstacle.getY() + dy);
                if (movingObstacle.getY() > windowHeight) {
                    gamePane.getChildren().remove(movingObstacle);
                }
            }
            movingObstacles.removeIf(movingObstacle -> movingObstacle.getY() > windowHeight);

            // Cria novas plataformas e obstáculos
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
        MovingObstacle movingObstacle = new MovingObstacle(x, y, "spriteAviao.png");
        movingObstacles.add(movingObstacle);
        gamePane.getChildren().add(movingObstacle);
        lastPlataformY = y;
    }

    // Trata o fim do jogo
    private void gameOver() throws IOException {
        if (!isGameOver) {
            isGameOver = true;
            System.out.println("Game Over");
            gameOverSound.play();
            switchToGameOver();
        }
    }

    // Transição para a cena inicial do jogo
    public void switchToScene1() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameMenu.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), windowWidth, windowHeight);
            String css = "stylesheet.css";
            scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Alterna a pausa do jogo
    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
            switchToPause();
            System.out.println("Game Paused");
        } else {
            timer.start();
            System.out.println("Game Resumed");
        }
    }

    // Transição para a tela de pausa
    public void switchToPause() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Pause.fxml"));
            pauseScene = new Scene(fxmlLoader.load(), windowWidth, windowHeight);
            String css = this.getClass().getResource("stylesheet.css").toExternalForm();
            pauseScene.getStylesheets().add(css);
            stage.setScene(pauseScene);
            stage.show();

            // Define ação do teclado para retomar o jogo
            pauseScene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    switchToGame();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Transição de volta para a tela de jogo após pausa
    public void switchToGame() {
        stage.setScene(gameScene);
        stage.show();
    }

    // Transição para a tela de Game Over
    public void switchToGameOver() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Game.class.getResource("GameOver.fxml"));
        Pane root = fxmlLoader.load();
        Scene scene = new Scene(root, windowWidth, windowHeight);
        String css = "stylesheet.css";
        scene.getStylesheets().add(css);
        stage.setTitle("SpeedyCoCat");
        stage.setScene(scene);
        stage.show();

        // Transição para a cena inicial após 4 segundos
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(e -> {
            switchToScene1();
        });
        delay.play();
    }
}
