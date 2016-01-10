package group2.Model;

import group2.Config;
import group2.Geometric.Rect;
import group2.Geometric.Vector2D;
import group2.Map.TileMap;
import javafx.scene.canvas.GraphicsContext;

/**
 * Author: Gác Xanh (phamanh)
 * Date: 10/01/2016
 * Class: OOP2
 * Project: ProjectSem2
 */
public class Coin extends GameObject {
    public Coin(String imageNamed) {
        super(imageNamed);
    }

    private boolean active = true;
    private int index;
    public Vector2D subPosition = Vector2D.zero;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void render(GraphicsContext gc) {
        double posX = position.x;
        double posY = position.y;

        gc.drawImage(this.image, -subPosition.x + this.position.x + Config.CoinProperties.width,
                -subPosition.y + this.position.y + Config.CoinProperties.height,
                Config.CoinProperties.sizeW, Config.CoinProperties.sizeH);
    }

    public Rect getRect() {
        return new Rect(this.position.x + Config.CoinProperties.width, this.position.y + Config.CoinProperties.height,
                Config.CoinProperties.sizeW, Config.CoinProperties.sizeH);
    }
}
