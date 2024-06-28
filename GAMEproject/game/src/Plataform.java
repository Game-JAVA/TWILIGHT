import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Plataform extends ImageView {
    public Plataform(double x, double y, String imagePath) {
        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            this.setImage(image);
        } catch (NullPointerException e) {
            System.out.println("Image not found: " + imagePath);
        }
        this.setX(x);
        this.setY(y);
        this.setFitWidth(69); // Ajuste o tamanho conforme necessário
        this.setFitHeight(20); // Ajuste o tamanho conforme necessário
    }
}
