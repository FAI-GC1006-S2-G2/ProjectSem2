package group2.Model;

import group2.Config;
import group2.Geometric.Rect;
import group2.Geometric.Vector2D;
import group2.Geometric.Vector2DHelper;
import javafx.scene.canvas.GraphicsContext;

/**
 * Author: Gác Xanh (phamanh)
 * Date: 11/01/2016
 * Class: OOP2
 * Project: ProjectSem2
 */
public class Bird extends Enemy {

    public Bird(String imageNamed, EnemyState enemyState) {
        super(imageNamed);
        this.isMoving = true;
        this.enemyState = enemyState;
        animation = frameDictionary.get(this.enemyState);
        animation.repeat = isMoving;
        setupVelocity(enemyState);
    }

    @Override
    public void loadAnimations() {
        frameDictionary.put(EnemyState.MOVE_DOWN, this.loadAnimations("flyDownAnim", true));
        frameDictionary.put(EnemyState.MOVE_UP, this.loadAnimations("flyUpAnim", true));
        frameDictionary.put(EnemyState.MOVE_RIGHT, this.loadAnimations("flyRightAnim", true));
        frameDictionary.put(EnemyState.MOVE_LEFT, this.loadAnimations("flyLeftAnim", true));
    }

    private void setupVelocity(EnemyState enemyState) {
        switch (enemyState) {
            case MOVE_RIGHT:
                this.velocity = new Vector2D(Config.BirdProperties.MaxMoveSpeed, 0);
                break;
            case MOVE_DOWN:
                this.velocity = new Vector2D(0, Config.BirdProperties.MaxMoveSpeed);
                break;
            case MOVE_LEFT:
                this.velocity = new Vector2D(-Config.BirdProperties.MaxMoveSpeed, 0);
                break;
            case MOVE_UP:
                this.velocity = new Vector2D(0, -Config.BirdProperties.MaxMoveSpeed);
        }
    }

    @Override
    public void update(double dt) {
        Vector2D velocityStep = Vector2DHelper.MutilByScalar(this.velocity, dt);
        this.position = Vector2DHelper.AddVector(this.position, velocityStep);
        super.update(dt);
    }

    @Override
    protected void updateAnimation(double dt) {
        this.timeElapsedSinceStartAnimation += dt;
        double elapsedTime = this.timeElapsedSinceStartAnimation;

        this.setTexture(animation.getFrame(elapsedTime));
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(this.image, -subPosition.x + this.position.x,
                -subPosition.y + this.position.y,
                this.image.getWidth(), this.image.getHeight());
    }

    public Rect getRect() {
        return new Rect(this.getPosition().x-1, this.getPosition().y-1, this.image.getWidth()-2, this.image.getHeight()-2);
    }
}
