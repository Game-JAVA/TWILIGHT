package org.example.speedy;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
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
import java.util.Objects;
import java.util.Random;

public class ScreenController {
    @FXML
    private Label gameTittle;
    @FXML
    private Button playButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button settingsButton;
    @FXML
    private AnchorPane background;
    @FXML
    private AnchorPane screen2;
    @FXML
    private AnchorPane gameOverScreen;
    @FXML
    private ImageView cat;
    private Scene scene;
    private Stage stage;
    private FXMLLoader fxmlLoader;
    private Player player;
    private List<Plataform> plataforms;
    private List<MovingObstacle> movingObstacles;
    private Pane gamePane;
    private final int windowWidth = 800;
    private final int windowHeight = 600;
    private double lastPlataformY = 500;
    private int score = 0;
    private Text scoreText;
    private ImageView backgroundImageView;
    private boolean isPaused = false;
    private boolean isGameOver = false;
    private AnimationTimer timer;
    private Scene gameScene;
    private Scene pauseScene;



    public void setStage(Stage stage) {
        this.stage = stage;
    }

    //método da tela de carregamento do jogo que aparece quando play é pressionado
    public void switchToLoad(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Game.class.getResource("GameLoad.fxml"));
        Pane root = fxmlLoader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, windowWidth, windowHeight);
        String css = Objects.requireNonNull(this.getClass().getResource("stylesheet.css")).toExternalForm();
        scene.getStylesheets().add(css);
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("catrun.gif"))));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(100); // Ajusta o tamanho conforme necessário
        imageView.setFitHeight(75); // Ajusta o tamanho conforme necessário
        // Ancorar o ImageView ao canto direito da tela
        AnchorPane.setBottomAnchor(imageView, 10.0);
        AnchorPane.setRightAnchor(imageView, 10.0);
        root.getChildren().add(imageView);
        stage.setTitle("Speddy CoCat");
        stage.setScene(scene);
        stage.show();

        // Configura a transição para a próxima cena após 4 segundos
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(e -> {
            try {
                gameStart(); // Muda para a próxima cena
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        delay.play();
    }

    //método responsavel por conter a lógica do jogo
    public void gameStart() throws IOException {
        stage.setTitle("Speedy CoCat");
        gamePane = new Pane();

        gameScene = new Scene(gamePane, windowWidth, windowHeight); // Inicialize corretamente a gameScene
        gameScene.setFill(Color.rgb(0, 183, 240));

        // Adiciona a imagem de fundo
        backgroundImageView = new ImageView(new Image("backgroundgame.png"));
        backgroundImageView.setFitWidth(windowWidth);
        backgroundImageView.setFitHeight(windowHeight);
        gamePane.getChildren().add(backgroundImageView);

        player = new Player("sprite gato2.png");
        gamePane.getChildren().add(player);
        gamePane.getChildren().add(player.getCollisionBox());

        plataforms = new ArrayList<>();
        movingObstacles = new ArrayList<>();
        createInitialPlataforms();

        scoreText = new Text(10, 20, "Pontuação: 0");
        scoreText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        gamePane.getChildren().add(scoreText);

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

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                player.update();
                updateMovingObstacles();
                checkCollisions();
                scrollScreen();

                //condição que valida que o jogador caiu fora do mapa
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

        stage.setScene(gameScene);
        stage.show();
    }



    private void createInitialPlataforms() {
        for (int i = 0; i < 15; i++) {
            double x = new Random().nextDouble() * (windowWidth - 100);
            double y = lastPlataformY - 50;
            Plataform plataform = new Plataform(x, y, "spritegrama.png");
            plataforms.add(plataform);
            gamePane.getChildren().add(plataform);
            lastPlataformY = y;
        }
    }

    private void updateMovingObstacles() {
        for (MovingObstacle movingObstacle : movingObstacles) {
            movingObstacle.update();
        }
    }

    private void createPlataform() {
        double x = new Random().nextDouble() * (windowWidth - 100);
        double y = lastPlataformY - 100;
        Plataform plataform = new Plataform(x, y, "sprite nuvem2.png");
        plataforms.add(plataform);
        gamePane.getChildren().add(plataform);
        lastPlataformY = y;
    }

    public void checkCollisions() {
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
                        score += 1;
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

                    if (player.getY() < windowHeight) {
                        player.incrementPlataformsJumped();
                        player.jump();
                        score += 1;
                        scoreText.setText("Pontuação: " + score);
                    }
                }
            }
        }
    }

    public void scrollScreen() {
        if (player.getY() < windowHeight / 2) {
            double dy = windowHeight / 2 - player.getY();
            player.setY(windowHeight / 2);
            player.updateCollisionBox();

            backgroundImageView.setY(backgroundImageView.getY() + dy);

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

    private void createMovingObstacle() {
        double x = new Random().nextDouble() * (windowWidth - 100);
        double y = lastPlataformY - 100;
        MovingObstacle movingObstacle = new MovingObstacle(x, y, "spriteAviao.png");
        movingObstacles.add(movingObstacle);
        gamePane.getChildren().add(movingObstacle);
        lastPlataformY = y;
    }

    //Método que valida que o jogador perdeu
    private void gameOver() throws IOException {
        if (!isGameOver) {
            isGameOver = true;
            System.out.println("Game Over");
            switchToGameOver(); // Chama a função para mudar de cena
        }
    }

    //Método que vai para o menu quando switchToGameOver é chamado
    public void switchToScene1() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameMenu.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), windowWidth, windowHeight);
            String css = this.getClass().getResource("stylesheet.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Método pause
    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            timer.stop(); // Pausa o jogo
            switchToPause(); // Alterna para a tela de pausa
            System.out.println("Game Paused");
        } else {
            timer.start(); // Retoma o jogo
            System.out.println("Game Resumed");
        }
    }

    //Método da tela de pause
    public void switchToPause() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Pause.fxml"));
            pauseScene = new Scene(fxmlLoader.load(), windowWidth, windowHeight);
            String css = this.getClass().getResource("stylesheet.css").toExternalForm();
            pauseScene.getStylesheets().add(css);
            stage.setScene(pauseScene);
            stage.show();

            pauseScene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    switchToGame();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Método que salva o estado do jogo para exibir após o pause
    public void switchToGame() {
        stage.setScene(gameScene);
        stage.show();
    }

    //método que exibe a tela de game over e após quatro segundos vai para o menu
    public void switchToGameOver() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Game.class.getResource("GameOver.fxml"));
        Pane root = fxmlLoader.load();
        Scene scene = new Scene(root, windowWidth, windowHeight);
        String css = Objects.requireNonNull(this.getClass().getResource("stylesheet.css")).toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("SpeedyCoCat");
        stage.setScene(scene);
        stage.show();

        // Configura a transição para a próxima cena após 4 segundos
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(e -> {
            switchToScene1(); // Muda para a próxima cena
        });
        delay.play();
    }
}