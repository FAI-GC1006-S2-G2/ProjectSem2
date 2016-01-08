package group2.Scene;

import group2.Config;
import group2.Geometric.Vector2D;
import group2.Map.TileMap;
import group2.Model.Character;
import group2.Model.Player;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BTC on 2015/12/17.
 */
public class GameScene extends Scene {
    public Group root;
    public Canvas canvas;
    AnimationTimer mainLoopManager;

    int debugInterval = 0;
    int fps;
    long lastUpdateTime = 0;

    // Variables
    Player player;
    TileMap map;

    public GameScene() {
        super(new Group());
        newGame();
        setupGameLoop();
    }

    private void setupGameLoop() {
        this.root = (Group) super.getRoot();
        this.canvas = new Canvas(group2.Config.WindowProperties.WINDOW_WIDTH, group2.Config.WindowProperties.WINDOW_HEIGHT);
        root.getChildren().add(canvas);

        ArrayList<String> input = new ArrayList<String>();
        this.setOnKeyPressed(
                new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent e) {
                        String code = e.getCode().toString();
                        // only add once... prevent duplicates
                        if (!input.contains(code)) {
                            input.add(code);
                        }
                    }
                });

        this.setOnKeyReleased(
                new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent e) {
                        String code = e.getCode().toString();
                        input.remove(code);
                    }
                });

        mainLoopManager = new AnimationTimer() {

            @Override
            public void handle(long currentTime) {
                handleEvents(input);
                update(currentTime);
                render(canvas.getGraphicsContext2D());
            }
        };
        mainLoopManager.start();
    }

    private void newGame() {
        map = new TileMap(this, 10);
        player = new Player("sprites/Player00.png");
        player.setPosition(new Vector2D(200, 200));
        player.map = map;
    }

    private void handleEvents(List<String> input) {
        if (input.contains("LEFT")) {
            this.player.moveLeft();
        } else if (input.contains("RIGHT"))
            this.player.moveRight();
        else if (input.contains("UP")) {
            this.player.moveUp();
        } else if (input.contains("DOWN")) {
            this.player.moveDown();
        } else {
            this.player.stopMove();
        }
    }

    private void update(long currentTime) {

        double dt = (currentTime - lastUpdateTime) / Config.NANOSECONDPERSEC;
        if (dt > 0.03) dt = 0.03;
        lastUpdateTime = currentTime;

        // logic code come here
        player.update(dt);
        moveMapCenterPlayer(dt);
        checkCollision(dt);

        // for debug purpose
        if (debugInterval >= 30) {
            debugInterval = 0;
            this.fps = (int) (1 / dt);
        }
        debugInterval++;
    }

    private void moveMapCenterPlayer(double dt) {
        double x = player.getPosition().x - (Config.WindowProperties.WINDOW_WIDTH - Config.PlayerProperties.WIDTH) / 2;
        double y = player.getPosition().y - (Config.WindowProperties.WINDOW_HEIGHT - Config.PlayerProperties.HEIGHT) / 2;
        if (x <= 0) {
            x = 0;
        } else if (player.getPosition().x > (TileMap.background.getWidth() - (Config.WindowProperties.WINDOW_WIDTH + Config.PlayerProperties.WIDTH) / 2)) {
            x = TileMap.background.getWidth() - Config.WindowProperties.WINDOW_WIDTH;
        }
        if (y <= 0) {
            y = 0;
        } else if (player.getPosition().y > (TileMap.background.getHeight() - (Config.WindowProperties.WINDOW_HEIGHT + Config.PlayerProperties.HEIGHT) / 2)) {
            y = TileMap.background.getHeight() - Config.WindowProperties.WINDOW_HEIGHT;
        }
        map.setPosition(new Vector2D(x, y));
    }

    private void checkCollision(double dt) {
        double x = player.getPosition().x;
        double y = player.getPosition().y;
        int data[] = map.layersList.get(0).getData();


        switch (player.getCharacterDirection()) {
            case Character.CharacterDirection.LEFT: {
                int row = (int) y / TileMap.tileHeight;
                int col = (int) x / TileMap.tileWidth;
                if (x < TileMap.tileWidth) {
                    player.setPosition(new Vector2D((col + 1) * TileMap.tileWidth, y));
                    return;
                }
                int left1 = TileMap.getIndexOfData(row, col);
                int left2 = TileMap.getIndexOfData(row + 1, col);
                // resolve
                if (y % TileMap.tileHeight > 28 && data[left2] == 0 && data[left1] != 0) {
                    player.setPosition(new Vector2D(x, (row + 1) * TileMap.tileHeight));
                    return;
                }
                // check left collision
                if (y % TileMap.tileHeight == 0 && data[left1] == 0) {
                    return;
                }
                if (data[left1] == 0 && data[left2] == 0) {
                    return;
                }
                player.setPosition(new Vector2D((col + 1) * TileMap.tileWidth, y));
            }
            break;
            case Character.CharacterDirection.RIGHT: {
                int row = (int) y / TileMap.tileHeight;
                int col = (int) (x + player.getSize().width) / TileMap.tileWidth;
                if (x + this.player.getSize().width > (TileMap.mapWidth - 1) * TileMap.tileWidth){
                    player.setPosition(new Vector2D((col -1) * TileMap.tileWidth,y));
                    return;
                }
                int right1 = TileMap.getIndexOfData(row, col);
                int right2 = TileMap.getIndexOfData(row + 1, col);
                // resolve
                if (y % TileMap.tileHeight > 28 && data[right2] == 0 && data[right1] != 0) {
                    player.setPosition(new Vector2D(x, (row + 1) * TileMap.tileHeight));
                    return;
                }
                // check right collision
                if (data[right1] == 0 && data[right2] == 0) {
                    return;
                } else if (y % TileMap.tileHeight == 0 && data[right1] == 0) {
                    return;
                }
                player.setPosition(new Vector2D((col - 1) * TileMap.tileWidth, y));
            }
            break;
            case Character.CharacterDirection.DOWN: {
                int row = (int) (y + player.getSize().height) / TileMap.tileHeight;
                int col = (int) x / TileMap.tileWidth;
                if (y + this.player.getSize().height > (TileMap.mapHeight - 1) * TileMap.tileHeight){
                    player.setPosition(new Vector2D(x,(row - 1) * TileMap.tileHeight));
                    return;
                }
                int down1 = TileMap.getIndexOfData(row, col);
                int down2 = TileMap.getIndexOfData(row, col + 1);
                // resolve
                if (x % TileMap.tileWidth > 28 && data[down1] != 0 && data[down2] == 0){
                    player.setPosition(new Vector2D((col + 1) * TileMap.tileWidth,y));
                    return;
                }
                // check down collision
                if (data[down1] == 0 && data[down2] == 0) {
                    return;
                } else if (x % TileMap.tileHeight == 0 && data[down1] == 0) {
                    return;
                }
                player.setPosition(new Vector2D(x, (row - 1) * TileMap.tileHeight));
            }
            break;
            case Character.CharacterDirection.UP: {
                int row = (int) y / TileMap.tileHeight;
                int col = (int) x / TileMap.tileWidth;
                if (y < TileMap.tileHeight) {
                    player.setPosition(new Vector2D(x, (row + 1) * TileMap.tileHeight));
                    return;
                }
                int up1 = TileMap.getIndexOfData(row, col);
                int up2 = TileMap.getIndexOfData(row, col + 1);
                // resolve
                if (y % TileMap.tileWidth > 28 && data[up1] != 0 && data[up2] == 0){
                    player.setPosition(new Vector2D((col + 1) * TileMap.tileWidth, y));
                    return;
                }
                // check up collision
                if (data[up1] == 0 && data[up2] == 0) {
                    return;
                }
                if (x % TileMap.tileHeight == 0 && data[up1] == 0) {
                    return;
                }
                player.setPosition(new Vector2D(x, (row + 1) * TileMap.tileHeight));
            }
        }
        // check right collision

    }

    private void render(GraphicsContext gc) {
        // clear canvas
        gc.clearRect(0, 0, Config.WindowProperties.WINDOW_WIDTH, Config.WindowProperties.WINDOW_HEIGHT);

        // our code will come here
        map.render(gc);
        player.render(gc);

        // for debug purpose
        gc.setStroke(Color.RED);
        gc.strokeText("FPS: " + String.valueOf(this.fps), this.getWidth() - 80, this.getHeight() - 30);
        gc.strokeText(String.valueOf(this.player.getPosition().x % player.getSize().width), 80, this.getHeight() - 30);
        gc.strokeText(String.valueOf(this.player.getPosition().y % player.getSize().height), 80, this.getHeight() - 15);
    }

}
