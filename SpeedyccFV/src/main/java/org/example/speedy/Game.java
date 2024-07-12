package org.example.speedy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Classe principal que inicializa o jogo extendendo a classe Application do JavaFX
public class Game extends Application {

    // Método start() é chamado quando o aplicativo é lançado
    @Override
    public void start(Stage stage) throws IOException {
        // Carrega o arquivo FXML que define o menu principal do jogo
        FXMLLoader fxmlLoader = new FXMLLoader(Game.class.getResource("GameMenu.fxml"));
        // Cria uma cena com base no conteúdo do arquivo FXML carregado
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        // Adiciona um arquivo CSS para estilizar a cena
        String css = "stylesheet.css";
        scene.getStylesheets().add(css);
        // Define o título da janela do jogo
        stage.setTitle("SpeedyCoCat");
        // Define a cena a ser exibida na janela do jogo
        stage.setScene(scene);
        // Mostra a janela do jogo
        stage.show();
    }

    // Método main() é o ponto de entrada do aplicativo Java
    public static void main(String[] args) {
        // Lança o aplicativo JavaFX
        launch();
    }
}
