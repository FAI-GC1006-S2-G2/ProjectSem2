package group2.Model;

import group2.Config;
import group2.Geometric.Size;
import group2.Geometric.Vector2D;
import group2.Map.TileMap;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

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

    public void render(GraphicsContext gc) {
        double x = position.x;
        double y = position.y;
        if (position.x > (Config.WindowProperties.WINDOW_WIDTH - Config.PlayerProperties.WIDTH) / 2 &&
                position.x <= (TileMap.background.getWidth() - (Config.WindowProperties.WINDOW_WIDTH + Config.PlayerProperties.WIDTH) / 2)) {
            x = (Config.WindowProperties.WINDOW_WIDTH - size.width) / 2;
        } else if (position.x > (TileMap.background.getWidth() - (Config.WindowProperties.WINDOW_WIDTH + Config.PlayerProperties.WIDTH) / 2)) {
            x = position.x - (TileMap.background.getWidth() - Config.WindowProperties.WINDOW_WIDTH);
        }
        if (position.y > (Config.WindowProperties.WINDOW_HEIGHT - Config.PlayerProperties.HEIGHT) / 2 &&
                position.y <= (TileMap.background.getHeight() - (Config.WindowProperties.WINDOW_HEIGHT + Config.PlayerProperties.HEIGHT) / 2)) {
            y = (Config.WindowProperties.WINDOW_HEIGHT - Config.PlayerProperties.HEIGHT) / 2;
        } else if (position.y > (TileMap.background.getHeight() - (Config.WindowProperties.WINDOW_HEIGHT + Config.PlayerProperties.HEIGHT) / 2)) {
            y = position.y - (TileMap.background.getHeight() - Config.WindowProperties.WINDOW_HEIGHT);
        }
//        gc.setStroke(Color.BLACK);
//        gc.fillRect(x,y,size.width,size.height);
        gc.drawImage(this.image, x, y, size.width, size.height);
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

