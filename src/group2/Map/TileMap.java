package group2.Map;

import group2.Config;
import group2.Geometric.Vector2D;
import group2.Model.GameObject;
import group2.Model.Player;
import group2.Scene.GameScene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import xmlwise.XmlElement;
import xmlwise.XmlParseException;
import xmlwise.Xmlwise;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Author: Gác Xanh (phamanh)
 * Date: 23/12/2015
 * Class: OOP2
 * Project: ProjectGame
 */
public class TileMap extends GameObject {
    public static int mapWidth;
    public static int mapHeight;
    public static int tileWidth;
    public static int tileHeight;
    public static Image background;
    private static Image tileSet;
    private static int numberTilePerRow;
    private static int tileSpacing = 0;
    private static int tileMargin = 0;
    public static int mapBorder = 4;

    public static int getIndexOfData(int row, int col){
        return row * mapWidth + col;
    }

    public static int getMapWidth() {
        return mapWidth;
    }

    public static int getMapHeight() {
        return mapHeight;
    }

    public LinkedList<Layer> layersList = new LinkedList<>();

    private void loadMapFromXML(String fileName) {
        XmlElement root = null;
        try {
            root = Xmlwise.loadXml("levels/" + fileName);
            setPropertyFromXML(root);
        } catch (XmlParseException | IOException e) {
            e.printStackTrace();
        }
        // data
        LinkedList<XmlElement> layerNode = root.get("layer");
        for (XmlElement layer : layerNode) {
            Layer layer1 = new Layer(layer);
            layersList.add(layer1);
        }
    }

    private void setPropertyFromXML(XmlElement root) {
        XmlElement tilesetNode = root.getFirst();
        XmlElement imageSourceNode = tilesetNode.getFirst();
        XmlElement imageLayerNode = root.get(1);
        String imageURL = imageSourceNode.getAttribute("source");
        String backgroundURL = imageLayerNode.getFirst().getAttribute("source");

        this.tileWidth = Integer.valueOf(root.getAttribute("tilewidth"));
        this.tileHeight = Integer.valueOf(root.getAttribute("tileheight"));
        this.mapWidth = Integer.valueOf(root.getAttribute("width"));
        this.mapHeight = Integer.valueOf(root.getAttribute("height"));
        if (tilesetNode.containsAttribute("margin")) {
            this.tileMargin = Integer.valueOf(tilesetNode.getAttribute("margin"));
        }
        if (tilesetNode.containsAttribute("spacing")) {
            this.tileSpacing = Integer.valueOf(tilesetNode.getAttribute("spacing"));
        }
        this.numberTilePerRow = (Integer.valueOf(imageSourceNode.getAttribute("width")) + tileSpacing - tileMargin) / tileWidth;
        try {
            this.tileSet = new Image(new FileInputStream("levels/" + imageURL));
            this.background = new Image(new FileInputStream("levels/" + backgroundURL));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public TileMap(GameScene scene, int level) {
        loadMapFromXML("level" + level + ".tmx");
    }

    public void render(GraphicsContext gc) {
        if (layersList.size() <= 0 || mapHeight * mapWidth <= 0) return;
        gc.drawImage(this.background, -this.position.x, -this.position.y, this.background.getWidth(), this.background.getHeight());
        int size = mapWidth * mapHeight;
        for (int i = 0; i < layersList.size(); i++) {

            int[] data = layersList.get(i).getData();

            for (int j = 0; j < size; j++) {
                int gid = data[j];
                if (gid == 0) continue;
                gid--;

                int row = j / mapWidth;
                int col = j % mapWidth;

                double dy = - this.position.y + row * tileWidth;
                double dx = - this.position.x + col * tileHeight;

                int tileRow = gid / numberTilePerRow;
                int tileCol = gid % numberTilePerRow;

                int sx = tileMargin + tileCol * (tileWidth + tileSpacing);
                int sy = tileMargin + tileRow * (tileWidth + tileSpacing);

                gc.drawImage(tileSet, sx, sy, tileWidth, tileHeight, dx, dy, tileWidth, tileHeight);

            }
        }
    }

}
