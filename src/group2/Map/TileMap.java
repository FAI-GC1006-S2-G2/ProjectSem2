package group2.Map;

import group2.Geometric.Vector2D;
import group2.Model.Coin;
import group2.Model.GameObject;
import group2.Scene.GameScene;
import group2.Model.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import xmlwise.XmlElement;
import xmlwise.XmlParseException;
import xmlwise.Xmlwise;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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
    public Player player;
    private Vector2D exitPoint;
    public List<Coin> coins;

    public static int getIndexOfData(int row, int col) {
        return row * mapWidth + col;
    }

    static int getMapWidth() {
        return mapWidth;
    }

    static int getMapHeight() {
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
        assert root != null;
        LinkedList<XmlElement> layerNode = root.get("layer");
        for (XmlElement layer : layerNode) {
            Layer layer1 = new Layer(layer);
            layersList.add(layer1);
        }
        // get coin
        coins = new LinkedList<>();
        int[] data = layersList.get(0).getData();
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0) {
                int row = i / mapWidth;
                int col = i % mapWidth;
                if (col == 0 || col >= mapWidth - 1 || row == 0 || row >= mapHeight - 1) {
                    continue;
                }
                Coin coin = new Coin();
                coin.setPosition(new Vector2D(col * tileWidth, row * tileHeight));
                coin.setIndex(i);
                coins.add(coin);
            }
        }
    }

    private void setPropertyFromXML(XmlElement root) {
        XmlElement tilesetNode = root.getFirst();
        XmlElement imageSourceNode = tilesetNode.getFirst();
        XmlElement imageLayerNode = root.get(1);
        String imageURL = imageSourceNode.getAttribute("source");
        String backgroundURL = imageLayerNode.getFirst().getAttribute("source");

        tileWidth = Integer.valueOf(root.getAttribute("tilewidth"));
        tileHeight = Integer.valueOf(root.getAttribute("tileheight"));
        mapWidth = Integer.valueOf(root.getAttribute("width"));
        mapHeight = Integer.valueOf(root.getAttribute("height"));
        if (tilesetNode.containsAttribute("margin")) {
            tileMargin = Integer.valueOf(tilesetNode.getAttribute("margin"));
        }
        if (tilesetNode.containsAttribute("spacing")) {
            tileSpacing = Integer.valueOf(tilesetNode.getAttribute("spacing"));
        }
        numberTilePerRow = (Integer.valueOf(imageSourceNode.getAttribute("width")) + tileSpacing - tileMargin) / tileWidth;
        try {
            tileSet = new Image(new FileInputStream("levels/" + imageURL));
            background = new Image(new FileInputStream("levels/" + backgroundURL));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Get object
        LinkedList<XmlElement> objectGroups = root.get("objectgroup");
        objectGroups.stream().filter(objectGroup -> objectGroup.getAttribute("name").equals("objects")).forEach(objectGroup -> {
            loadCharacterFromXmlElement(objectGroup, "player");
            loadCharacterFromXmlElement(objectGroup, "exit");
        });

    }

    private void loadCharacterFromXmlElement(List<XmlElement> list, String name) {
        for (XmlElement object : list) {
            if (object.getAttribute("name").equals(name)) {
                String type = object.getAttribute("type");
                GameObject gameObject = null;
                switch (type) {
                    case "player":
                        gameObject = new Player("sprites/Player00.png");
                        this.player = (Player) gameObject;
                        break;
                    case "exit":
                        double exitX = Double.valueOf(object.getAttribute("x").trim());
                        double exitY = Double.valueOf(object.getAttribute("y").trim());
                        this.exitPoint = new Vector2D(exitX, exitY);
                        return;
                    default:
                }
                double posX = Double.valueOf(object.getAttribute("x").trim());
                double posY = Double.valueOf(object.getAttribute("y").trim());
                assert gameObject != null;
                gameObject.setPosition(new Vector2D(posX, posY));
            }
        }
    }

    public TileMap(int level) {
        loadMapFromXML("level" + level + ".tmx");
    }

    public void render(GraphicsContext gc) {
        if (layersList.size() <= 0 || mapHeight * mapWidth <= 0) return;
        gc.drawImage(background, -this.position.x, -this.position.y, background.getWidth(), background.getHeight());
        int size = mapWidth * mapHeight;
        for (Layer aLayersList : layersList) {

            int[] data = aLayersList.getData();

            for (int j = 0; j < size; j++) {
                int gid = data[j];
                if (gid == 0) continue;
                gid--;

                int row = j / mapWidth;
                int col = j % mapWidth;

                double dy = -this.position.y + row * tileWidth;
                double dx = -this.position.x + col * tileHeight;

                int tileRow = gid / numberTilePerRow;
                int tileCol = gid % numberTilePerRow;

                int sx = tileMargin + tileCol * (tileWidth + tileSpacing);
                int sy = tileMargin + tileRow * (tileWidth + tileSpacing);

                gc.drawImage(tileSet, sx, sy, tileWidth, tileHeight, dx, dy, tileWidth, tileHeight);
            }
        }
    }

}
