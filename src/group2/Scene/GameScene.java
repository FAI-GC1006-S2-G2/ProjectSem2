package group2.Scene;

import group2.Config;
import group2.Geometric.Rect;
import group2.Geometric.Vector2D;
import group2.Map.TileMap;
import group2.Model.*;
import group2.Model.Character;
import group2.SoundManager;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by BTC on 2015/12/17.
 */
public class GameScene extends Scene {
    public Group root;
    public Canvas canvas;
    AnimationTimer mainLoopManager;
    private Random rd = new Random();

    String getCoin;
    String playBackground;
    int debugInterval = 0;
    int fps;
    long lastUpdateTime = 0;
    long lastUpdateBird = 0;
    public boolean gameRunning = true;

    // Variables
    Player player;
    TileMap map;
    List<Coin> coins;
    List<Bird> birds;
    private int coinGet = 0;
    private int level = 10;

    public GameScene() {
        super(new Group());
        newGame(level);
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

    private void newGame(int level) {
        SoundManager.stopBackGroundMusic();
        playBackground = "sounds/background.mp3";
        getCoin = "sounds/coin5.wav";
        SoundManager.playBackGroundMusic(playBackground);
        map = new TileMap(this, level);
        player = map.player;
        coins = map.coins;
        birds = new LinkedList<>();
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
        if (!gameRunning) {
            return;
        }
        checkNewLevel();

        double dt = (currentTime - lastUpdateTime) / Config.NANOSECONDPERSEC;
        if (dt > 0.03) dt = 0.03;
        lastUpdateTime = currentTime;

        // logic code come here
        player.update(dt);
        checkCollision();
        moveMapCenterPlayer();
        checkGoldRemain();
        for (Coin coin : coins) {
            coin.update(dt);
        }
        createAndUpdateBird(currentTime, dt);
        if (checkCollisionWithBird()) {
            looseGame();
        }
        removeBird();

        // for debug purpose
        if (debugInterval >= 30) {
            debugInterval = 0;
            this.fps = (int) (1 / dt);
        }
        debugInterval++;
    }
    private void removeBird(){
        for (int i = 0; i < birds.size(); i ++){
            Vector2D position = birds.get(i).getPosition();
            if (position.y < 0 || position.y > TileMap.mapHeight * TileMap.tileHeight){
                birds.remove(i);
            }
            if (position.x < 0 || position.x > TileMap.mapWidth * TileMap.tileWidth){
                birds.remove(i);
            }
        }
    }

    private boolean checkCollisionWithBird() {
        Rect player = new Rect(this.player.getPosition().x + 3, this.player.getPosition().y, this.player.getSize().width - 6, this.player.getSize().height);
        for (Bird bird : birds) {
            if (player.intersects(bird.getRect())) {
                return true;
            }
        }
        return false;
    }

    int i = 1;

    private void createAndUpdateBird(double currentTime, double dt) {
        if (birds.size() > 40){
            for (int i = 0; i < birds.size(); i++) {
                birds.get(i).update(dt);
            }
            return;
        }
        double elapsedTime = (currentTime - lastUpdateBird) / Config.NANOSECONDPERSEC;
        if (elapsedTime > Config.BirdProperties.TimeGenerate) {
            lastUpdateBird = (long) currentTime;
            createBird();
        }
        for (int i = 0; i < birds.size(); i++) {
            birds.get(i).update(dt);
        }
    }

    private void createBird() {
        int i = rd.nextInt(4);
        int col;
        int row;
        switch (i) {
            case 0:
                Bird birdDown = new Bird("sprites/Bird/bird00.png", Enemy.EnemyState.MOVE_DOWN);
                col = rd.nextInt(TileMap.mapWidth - 2) + 1;
                birdDown.setPosition(new Vector2D(TileMap.tileWidth * col, 0));
                birds.add(birdDown);
                break;
            case 1:
                Bird birdLeft = new Bird("sprites/Bird/bird00.png", Enemy.EnemyState.MOVE_LEFT);
                row = rd.nextInt(TileMap.mapHeight - 2) + 1;
                birdLeft.setPosition(new Vector2D(TileMap.mapWidth * TileMap.tileWidth,row * TileMap.tileHeight));
                birds.add(birdLeft);
                break;
            case 2:
                Bird birdUp = new Bird("sprites/Bird/bird00.png", Enemy.EnemyState.MOVE_UP);
                col = rd.nextInt(TileMap.mapWidth - 2) + 1;
                birdUp.setPosition(new Vector2D(col * TileMap.tileWidth,TileMap.mapHeight * TileMap.tileHeight));
                birds.add(birdUp);
                break;
            case 3:
                Bird birdRight = new Bird("sprites/Bird/bird00.png", Enemy.EnemyState.MOVE_RIGHT);
                row = rd.nextInt(TileMap.mapHeight - 2) + 1;
                birdRight.setPosition(new Vector2D(0,row * TileMap.tileHeight));
                birds.add(birdRight);
        }
//        Bird bird = new Bird("sprites/Bird/bird00.png", Enemy.EnemyState.MOVE_RIGHT);
//        bird.setPosition(new Vector2D(0, i++ * TileMap.tileHeight));
//        if (i == TileMap.mapHeight - 1) i = 1;
//        birds.add(bird);
    }

    public void backToMainMenu() {
        mainLoopManager.stop();
        SoundManager.stopBackGroundMusic();
    }

    private void checkNewLevel() {
        if (coinGet > 50) {
            if (level == 13) {
                gameRunning = false;
                wonGame();
                return;
            }
            coinGet = 0;
            level++;
            newGame(level);
        }
    }

    public void wonGame() {
        SoundManager.stopBackGroundMusic();
        SoundManager.playSound("sounds/victory.mp3");
        // play won so
        gameRunning = false;
        Text text = new Text(Config.WindowProperties.WINDOW_WIDTH / 2 - 100, Config.WindowProperties.WINDOW_HEIGHT / 2, "YOU WON!!!");
        text.setFont(Font.font("Chalkduster", FontWeight.BOLD, 50));
        text.setFill(Color.AZURE);
        root.getChildren().add(text);
    }

    private void looseGame() {
        SoundManager.stopBackGroundMusic();
        SoundManager.playSound("sounds/victory.mp3");
        gameRunning = false;
        Text text = new Text(Config.WindowProperties.WINDOW_WIDTH / 2 - 100, Config.WindowProperties.WINDOW_HEIGHT / 2, "YOU LOSE!!!");
        text.setFont(Font.font("Chalkduster", FontWeight.BOLD, 50));
        text.setFill(Color.AZURE);
        root.getChildren().add(text);
    }

    private void checkGoldRemain() {
        Rect player = new Rect(this.player.getPosition().x + 3, this.player.getPosition().y, this.player.getSize().width - 6, this.player.getSize().height);
        int row = (int) this.player.getPosition().y / TileMap.tileHeight;
        int col = (int) this.player.getPosition().x / TileMap.tileWidth;

        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        for (int i = 0; i < coins.size(); i++) {
            if (row < TileMap.mapHeight - 1 && col < TileMap.mapWidth - 1) {
                if (coins.get(i).getIndex() == (row + 1) * TileMap.mapWidth + col + 1) {
                    i4 = i;
                    continue;
                }
            }
            if (row < TileMap.mapHeight - 1) {
                if (coins.get(i).getIndex() == (row + 1) * TileMap.mapWidth + col) {
                    i3 = i;
                    continue;
                }
            }
            if (col < TileMap.mapWidth - 1) {
                if (coins.get(i).getIndex() == row * TileMap.mapWidth + col + 1) {
                    i2 = i;
                    continue;
                }
            }
            if (coins.get(i).getIndex() == row * TileMap.mapWidth + col) {
                i1 = i;
            }
        }
        removeCoins(player, i4);
        removeCoins(player, i3);
        removeCoins(player, i2);
        removeCoins(player, i1);
//        Coin coin = coins.get(index);
    }

    private void removeCoins(Rect player, int i) {
        if (i == 0) return;
        if (player.intersects(coins.get(i).getRect())) {
            coins.remove(i);
            coinGet++;
            SoundManager.playSound(getCoin);
        }
    }

    private void moveMapCenterPlayer() {
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
        for (Coin coin : coins) {
            coin.subPosition = new Vector2D(x, y);
        }
        if (birds.size() > 0)
            for (Bird bird : birds) {
                bird.subPosition = new Vector2D(x, y);
            }
    }

    private void checkCollision() {
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
                if (y % TileMap.tileHeight > Config.MapProperties.maxResolve && data[left2] == 0 && data[left1] != 0) {
                    player.setPosition(new Vector2D(x, (row + 1) * TileMap.tileHeight));
                    return;
                }
                if (y % TileMap.tileHeight < Config.MapProperties.minResolve && data[left1] == 0 && data[left2] != 0) {
                    player.setPosition(new Vector2D(x, row * TileMap.tileHeight));
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
                if (x + this.player.getSize().width > (TileMap.mapWidth - 1) * TileMap.tileWidth) {
                    player.setPosition(new Vector2D((col - 1) * TileMap.tileWidth, y));
                    return;
                }
                int right1 = TileMap.getIndexOfData(row, col);
                int right2 = TileMap.getIndexOfData(row + 1, col);
                // resolve
                if (y % TileMap.tileHeight > Config.MapProperties.maxResolve && data[right2] == 0 && data[right1] != 0) {
                    player.setPosition(new Vector2D(x, (row + 1) * TileMap.tileHeight));
                    return;
                }
                if (y % TileMap.tileHeight < Config.MapProperties.minResolve && data[right1] == 0 && data[right2] != 0) {
                    player.setPosition(new Vector2D(x, row * TileMap.tileHeight));
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
                if (y + this.player.getSize().height > (TileMap.mapHeight - 1) * TileMap.tileHeight) {
                    player.setPosition(new Vector2D(x, (row - 1) * TileMap.tileHeight));
                    return;
                }
                int down1 = TileMap.getIndexOfData(row, col);
                int down2 = TileMap.getIndexOfData(row, col + 1);
                // resolve
                if (x % TileMap.tileWidth > Config.MapProperties.maxResolve && data[down1] != 0 && data[down2] == 0) {
                    player.setPosition(new Vector2D((col + 1) * TileMap.tileWidth, y));
                    return;
                }
                if (x % TileMap.tileWidth < Config.MapProperties.minResolve && data[down1] == 0 && data[down2] != 0) {
                    player.setPosition(new Vector2D(col * TileMap.tileWidth, y));
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
                if (x % TileMap.tileWidth > Config.MapProperties.maxResolve && data[up1] != 0 && data[up2] == 0) {
                    player.setPosition(new Vector2D((col + 1) * TileMap.tileWidth, y));
                    return;
                }
                if (x % TileMap.tileWidth < Config.MapProperties.minResolve && data[up1] == 0 && data[up2] != 0) {
                    player.setPosition(new Vector2D(col * TileMap.tileWidth, y));
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

    }

    private void render(GraphicsContext gc) {
        // clear canvas
        gc.clearRect(0, 0, Config.WindowProperties.WINDOW_WIDTH, Config.WindowProperties.WINDOW_HEIGHT);

        // our code will come here
        map.render(gc);
        player.render(gc);

        for (Coin coin : coins) {
            coin.render(gc);
        }
        for (Bird bird : birds) {
            bird.render(gc);
        }


        // for debug purpose
        gc.setStroke(Color.RED);
        gc.strokeText("FPS: " + String.valueOf(this.fps), this.getWidth() - 80, this.getHeight() - 30);
        gc.strokeText(String.valueOf(this.player.getPosition().x), 80, this.getHeight() - 30);
        gc.strokeText(String.valueOf(this.player.getPosition().y), 80, this.getHeight() - 15);
        gc.strokeText(String.valueOf(this.coinGet), 80, this.getHeight() - 45);
    }

}
