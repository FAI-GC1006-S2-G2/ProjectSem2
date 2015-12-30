package group2.Model;

import group2.Geometric.Size;
import group2.Geometric.Vector2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by BTC on 2015/12/17.
 */
public class Sprite {

    protected Vector2D position = Vector2D.zero;
    protected Size size;
    protected boolean flipX;

    protected Scene scene;
    protected Image image;

    protected String getClassName() {
        return this.getClass().getSimpleName();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setTexture(Image newImage) {
        this.image = newImage;
        this.size = new Size(newImage.getWidth(), newImage.getHeight());
    }

    public Sprite(String imageNamed) {

        try {
            this.image = new Image(new FileInputStream(imageNamed));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.position = Vector2D.zero;
        this.size = new Size(image.getWidth(), image.getHeight());
    }

    public Sprite() {
    }

    public void update(double dt) {
    }

    ;

    public void render(GraphicsContext gc) {
        gc.drawImage(this.image,
                position.x - size.width / 2, position.y - size.height / 2,
                size.width, size.height);
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}

