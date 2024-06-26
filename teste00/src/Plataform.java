import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Plataform extends ImageView {
    public Plataform(double x, double y, String imagePath) {
        Image image = new Image(imagePath);
        this.setImage(image);
        this.setX(x);
        this.setY(y);
        this.setFitWidth(80); // Ajuste o tamanho conforme necessário
        this.setFitHeight(20); // Ajuste o tamanho conforme necessário
    }
}
