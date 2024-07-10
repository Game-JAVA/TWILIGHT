import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Application {
    private Player player;
    private List<Plataform> plataforms;
    private Pane gamePane;
    private final int windowWidth = 800;
    private final int windowHeight = 600;
    private double lastPlataformY = 500;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Speedy CoCat");

        gamePane = new Pane();
        Scene scene = new Scene(gamePane, windowWidth, windowHeight);
        player = new Player(50, 50, Color.BLUE);
        gamePane.getChildren().add(player);

        plataforms = new ArrayList<>();
        createInitialPlataforms();

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    player.setX(player.getX() - 50);
                    break;
                case RIGHT:
                    player.setX(player.getX() + 50);
                    break;
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                player.update();
                checkCollisions();
                scrollScreen();
                if (player.getY() > windowHeight && player.getPlataformsJumped() >= 2) {
                    gameOver();
                    stop();
                }
            }
        };
        timer.start();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createInitialPlataforms() {
        for (int i = 0; i < 5; i++) {
            double x = new Random().nextDouble() * (windowWidth - 100);
            double y = lastPlataformY - 100;
            Plataform plataform = new Plataform(x, y, 100, 20, Color.GREEN);
            plataforms.add(plataform);
            gamePane.getChildren().add(plataform);
            lastPlataformY = y;
        }
    }

    private void createPlataform() {
        double x = new Random().nextDouble() * (windowWidth - 100);
        double y = lastPlataformY - 100;
        Plataform plataform = new Plataform(x, y, 100, 20, Color.GREEN);
        plataforms.add(plataform);
        gamePane.getChildren().add(plataform);
        lastPlataformY = y;
    }

    private void checkCollisions() {
        player.setOnPlataform(false);
        for (Plataform plataform : plataforms) {
            if (player.getBoundsInParent().intersects(plataform.getBoundsInParent())) {
                if (player.getVelocityY() > 0) {
                    player.setY(plataform.getY() - player.getHeight());
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
        System.out.println("Game Over");
        // Adicione lógica de game over aqui
    }
}
