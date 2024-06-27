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
    private ImageView cat;
    private Scene scene;
    private Stage stage;
    private FXMLLoader fxmlLoader;

    private Player player; // Instância do jogador
    private List<Plataform> plataforms; // Lista de plataformas
    private Pane gamePane; // Pane do jogo
    private final int windowWidth = 800; // Largura da janela
    private final int windowHeight = 600; // Altura da janela
    private double lastPlataformY = 500; // Posição Y da última plataforma
    private boolean isPaused = false;
    private boolean isGameOver = false;
    private AnimationTimer timer;
    private Scene gameScene;
    private Scene pauseScene;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void switchToScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameMenu.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), windowWidth, windowHeight);
            String css = this.getClass().getResource("stylesheet.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        stage.setTitle("Hello!");
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

    public void gameStart() throws IOException {
        stage.setTitle("Speedy CoCat");

        gamePane = new Pane();
        gameScene = new Scene(gamePane, windowWidth, windowHeight);
        player = new Player("sprite gato2.png"); // Criação do jogador com a sprite
        gamePane.getChildren().add(player); // Adiciona o jogador ao gamePane
        gamePane.getChildren().add(player.getCollisionBox()); // Adiciona a caixa de colisão ao gamePane

        plataforms = new ArrayList<>();
        createInitialPlataforms();

        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                togglePause();
            }
            if (!isPaused && !isGameOver) {
                switch (event.getCode()) {
                    case LEFT:
                        player.setX(player.getX() - 50);
                        break;
                    case RIGHT:
                        player.setX(player.getX() + 50);
                        break;
                }
            }
        });

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused && !isGameOver) {
                    player.update();
                    checkCollisions();
                    scrollScreen();
                    if (player.getY() > windowHeight && player.getPlataformsJumped() >= 0) {
                        gameOver();
                    }
                }
            }
        };
        timer.start();

        stage.setScene(gameScene);
        stage.show();
    }

    private void createInitialPlataforms() {
        for (int i = 0; i < 5; i++) {
            double x = new Random().nextDouble() * (windowWidth - 100);
            double y = lastPlataformY - 100;
            Plataform plataform = new Plataform(x, y, "sprite nuvem2.png");
            plataforms.add(plataform);
            gamePane.getChildren().add(plataform);
            lastPlataformY = y;
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

    private void checkCollisions() {
        player.setOnPlataform(false);
        for (Plataform plataform : plataforms) {
            if (player.getBoundsInParent().intersects(plataform.getBoundsInParent())) {
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
    }

    private void scrollScreen() {
        if (player.getY() < windowHeight / 2) {
            double dy = windowHeight / 2 - player.getY();
            player.setY(windowHeight / 2);

            for (Plataform plataform : plataforms) {
                plataform.setY(plataform.getY() + dy);
                if (plataform.getY() > windowHeight) {
                    gamePane.getChildren().remove(plataform);
                }
            }
            plataforms.removeIf(plataform -> plataform.getY() > windowHeight);

            lastPlataformY += dy;
            createPlataform();
        }
    }

    private void gameOver() {
        if (!isGameOver) {
            isGameOver = true;
            System.out.println("Game Over");
            switchToScene1(); // Chama a função para mudar de cena
        }
    }

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

    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            switchToPause();
            System.out.println("Game Paused");
        } else {
            switchToGame();
            System.out.println("Game Resumed");
        }
    }

    public void switchToPause() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Pause.fxml"));
            pauseScene = new Scene(fxmlLoader.load(), 600, 400);
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

    public void switchToGame() {
        stage.setScene(gameScene);
        stage.show();
    }
}