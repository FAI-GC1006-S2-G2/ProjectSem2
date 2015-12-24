package group2.Map;

import javafx.scene.image.Image;
import xmlwise.XmlElement;

/**
 * Author: Gác Xanh (phamanh)
 * Date: 23/12/2015
 * Class: OOP2
 * Project: ProjectSem2
 */
public class Layer {
    private int[] data;
    private XmlElement xmlElement;

    public Layer(XmlElement xmlElement) {
        this.xmlElement = xmlElement;
        setData();
    }

    private void setData(){
        XmlElement dataNode = xmlElement.get("data").getFirst();
        String[] dataValue = dataNode.getValue().trim().split(",");
        this.data = new int[TileMap.getMapWidth() * TileMap.getMapHeight()];
        for (int i = 0; i < data.length; i++) {
            String result = dataValue[i].trim();
            try {
                this.data[i] = Integer.parseInt(result);
            } catch (NumberFormatException e) {
                this.data[i] = 0;
            }
        }
    }

    public int[] getData() {
        return data;
    }

}
