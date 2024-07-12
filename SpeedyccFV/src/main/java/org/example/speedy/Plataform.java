package org.example.speedy;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// Classe que representa uma plataforma no jogo
public class Plataform extends ImageView {

    // Construtor da plataforma
    public Plataform(double x, double y, String imagePath) {
        try {
            Image image = new Image(imagePath); // Carrega a imagem da plataforma
            this.setImage(image); // Define a imagem da plataforma
        } catch (NullPointerException e) {
            System.out.println("Image not found: " + imagePath); // Mensagem de erro se a imagem não for encontrada
        }
        this.setX(x); // Define a posição X da plataforma
        this.setY(y); // Define a posição Y da plataforma
        this.setFitWidth(69); // Ajusta a largura conforme necessário
        this.setFitHeight(20); // Ajusta a altura conforme necessário
    }
}
