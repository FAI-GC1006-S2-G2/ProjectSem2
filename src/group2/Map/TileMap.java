package group2.Map;

import group2.Model.GameObject;
import group2.Scene.GameScene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import xmlwise.XmlElement;
import xmlwise.XmlParseException;
import xmlwise.Xmlwise;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by BTC on 2015/12/18.
 */
public class TileMap extends GameObject {

    public int mapWidth;
    public int mapHeight;
    public int tileWidth;
    public int tileHeight;
    public Image tileSet;
    public int[] data;

    private void loadMapFromXML(String fileName) {
        try {
            XmlElement root = Xmlwise.loadXml("levels/" + fileName);
            XmlElement tilesetNode = root.getFirst();
            XmlElement imageSourceNode = tilesetNode.getFirst();
            String imageURL = imageSourceNode.getAttribute("source");

            this.tileWidth = Integer.valueOf(root.getAttribute("tilewidth"));
            this.tileHeight = Integer.valueOf(root.getAttribute("tileheight"));
            this.mapWidth = Integer.valueOf(root.getAttribute("width"));
            this.mapHeight = Integer.valueOf(root.getAttribute("height"));
            this.tileSet = new Image(new FileInputStream("levels/" + imageURL));

            // data
            LinkedList<XmlElement> layerNode = root.get("layer");
            XmlElement dataNode = layerNode.getFirst().get("data").getFirst();
            String[] dataValue = dataNode.getValue().trim().split(",");
            this.data = new int[mapWidth * mapHeight];
            for (int i = 0; i < data.length; i++) {
                String result = dataValue[i].trim();
                try {
                    this.data[i] = Integer.parseInt(result);
                } catch (NumberFormatException e) {
                    this.data[i] = 0;
                }
            }
        } catch (XmlParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public TileMap(GameScene scene, int level) {
//      this.scene = scene;
//      this.currentLevel = level;
        loadMapFromXML("level" + level + ".tmx");

    }

    public void render(GraphicsContext gc) {
        int size = mapWidth * mapHeight;
        for (int i = 0; i < size; i++) {
            int gid = data[i];
            if (gid == 0) continue;
            gid--;
            int row = i / mapWidth;
            int col = i % mapHeight;
            double dy = this.position.y + row * tileWidth;
            double dx = this.position.x + col * tileHeight;

            int tileRow = gid / 8;
            int tileCol = gid % 8;
            int sx = 1 + tileCol * 33;
            int sy = 1 + tileRow * 33;

            gc.drawImage(tileSet, sx, sy, 32, 32, dx, dy, tileWidth, tileHeight);

        }


    }

}
