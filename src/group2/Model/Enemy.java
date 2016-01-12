package group2.Model;

import group2.Geometric.Vector2D;
import javafx.scene.image.Image;
import xmlwise.Plist;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Gác Xanh (phamanh)
 * Date: 11/01/2016
 * Class: OOP2
 * Project: ProjectSem2
 */
public abstract class Enemy extends GameObject {

    protected Map<EnemyState, AnimatedImage> frameDictionary;
    protected double timeElapsedSinceStartAnimation; // second
    protected AnimatedImage animation;
    protected int enemyDirection;
    protected EnemyState enemyState;
    protected boolean isMoving = false;
    public Vector2D velocity = Vector2D.zero;
    public Vector2D subPosition = Vector2D.zero;

    public abstract void loadAnimations();

    public void changeState(EnemyState newState) {
        if (newState == this.enemyState) return;
        this.enemyState = newState;
        this.timeElapsedSinceStartAnimation = 0;
        animation = frameDictionary.get(this.enemyState);
    }

    public Enemy(String imageNamed) {
        super(imageNamed);
        frameDictionary = new HashMap<EnemyState, AnimatedImage>();
        this.timeElapsedSinceStartAnimation = 0;
        this.loadAnimations();
    }

    protected void updateState(double dt) {

    }

    @Override
    public void update(double dt) {
        // setup current frame for animation purpose
        this.updateAnimation(dt);
    }

    protected abstract void updateAnimation(double dt);

    protected AnimatedImage loadAnimations(String className, String animationName, boolean repeat) {
        try {
            Map<String, Object> root = Plist.load("data/" + className + ".plist");
            Map<String, Object> properties = (Map<String, Object>) root.get(animationName);
            String[] imageNames = properties.get("animationFrames").toString().split(",");
            double duration = Double.valueOf(properties.get("delay").toString());

            Image[] images = new Image[imageNames.length];
            for (int i = 0; i < images.length; i++) {
                images[i] = new Image(new FileInputStream("sprites/" + className + "/" + className + imageNames[i] + ".png"));
            }
            AnimatedImage result = new AnimatedImage(images, duration, repeat);
            return result;

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    protected AnimatedImage loadAnimations(String animationName, boolean repeat) {
        return loadAnimations(this.getClassName(), animationName, repeat);
    }

    public enum EnemyState {
        STANDING, MOVE_UP, MOVE_RIGHT, MOVE_DOWN, MOVE_LEFT
    }

    public class EnemyDirection {
        public static final int NONE = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
        public static final int LEFT = 4;
    }

}
