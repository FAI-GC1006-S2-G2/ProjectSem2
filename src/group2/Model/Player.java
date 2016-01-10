package group2.Model;

import group2.Config;
import group2.Geometric.Rect;
import group2.Geometric.Vector2D;
import group2.Geometric.Vector2DHelper;
import group2.Map.TileMap;

/**
 * Created by BTC on 2015/12/17.
 */
public class Player extends Character {

//   @Override
//   public void setTexture(Image newImage) {
//      super.setTexture(newImage);
//   }

    @Override
    public Rect collisionBoundingBox() {
        Rect bounding = new Rect(this.desiredPosition.x - Config.PlayerProperties.WIDTH / 2,
                this.desiredPosition.y - Config.PlayerProperties.HEIGHT / 2,
                Config.PlayerProperties.WIDTH,
                Config.PlayerProperties.HEIGHT);
        return new Rect(bounding.x, bounding.y, bounding.width, bounding.height);
    }

    @Override
    public void loadAnimations() {
        frameDictionary.put(CharacterState.MOVE_UP, this.loadAnimations("moveUpAnim", true));
        frameDictionary.put(CharacterState.MOVE_RIGHT, this.loadAnimations("moveRightAnim", true));
        frameDictionary.put(CharacterState.MOVE_DOWN, this.loadAnimations("moveDownAnim", true));
        frameDictionary.put(CharacterState.MOVE_LEFT, this.loadAnimations("moveLeftAnim", true));
        frameDictionary.put(CharacterState.STANDING, this.loadAnimations("standingAnim", true));
    }

    @Override
    public void changeState(CharacterState newState) {
        if (newState == characterState) return;
        super.changeState(newState);
    }

    @Override
    protected void updateState(double dt) {
        CharacterState newState = this.characterState;

        Vector2D joyForce = Vector2D.zero;
        Vector2D joyForceStep;

        this.isMoving = true;
        switch (characterDirection) {
            case CharacterDirection.UP:
                this.velocity.x = 0;
                joyForce = new Vector2D(0, -Config.PlayerProperties.WalkingAccelerate);
                joyForceStep = Vector2DHelper.MutilByScalar(joyForce, dt);
                this.velocity = Vector2DHelper.AddVector(this.velocity, joyForceStep);
                if (this.velocity.y < -Config.PlayerProperties.MaxMoveSpeed)
                    this.velocity.y = -Config.PlayerProperties.MaxMoveSpeed;
                newState = CharacterState.MOVE_UP;
                break;
            case CharacterDirection.RIGHT:
                this.velocity.y = 0;
                joyForce = new Vector2D(Config.PlayerProperties.WalkingAccelerate, 0);
                joyForceStep = Vector2DHelper.MutilByScalar(joyForce, dt);
                this.velocity = Vector2DHelper.AddVector(this.velocity, joyForceStep);
                if (this.velocity.x > Config.PlayerProperties.MaxMoveSpeed)
                    this.velocity.x = Config.PlayerProperties.MaxMoveSpeed;
                newState = CharacterState.MOVE_RIGHT;
                break;
            case CharacterDirection.DOWN:
                this.velocity.x = 0;
                joyForce = new Vector2D(0, Config.PlayerProperties.WalkingAccelerate);
                joyForceStep = Vector2DHelper.MutilByScalar(joyForce, dt);
                this.velocity = Vector2DHelper.AddVector(this.velocity, joyForceStep);
                if (this.velocity.y > Config.PlayerProperties.MaxMoveSpeed)
                    this.velocity.y = Config.PlayerProperties.MaxMoveSpeed;
                newState = CharacterState.MOVE_DOWN;
                break;
            case CharacterDirection.LEFT:
                this.velocity.y = 0;
                joyForce = new Vector2D(-Config.PlayerProperties.WalkingAccelerate, 0);
                joyForceStep = Vector2DHelper.MutilByScalar(joyForce, dt);
                this.velocity = Vector2DHelper.AddVector(this.velocity, joyForceStep);
                if (this.velocity.x < -Config.PlayerProperties.MaxMoveSpeed)
                    this.velocity.x = -Config.PlayerProperties.MaxMoveSpeed;
                newState = CharacterState.MOVE_LEFT;
                break;
            case CharacterDirection.NONE:
                isMoving = false;
                this.velocity = Vector2D.zero;
        }
//        Vector2D joyForceStep = Vector2DHelper.MutilByScalar(joyForce, dt);
//        this.velocity = Vector2DHelper.AddVector(this.velocity, joyForceStep);

        this.changeState(newState);

    }

    public Player(String imageNamed) {
        super(imageNamed);
        characterState = CharacterState.STANDING;
    }

    int invulnerableTickCount = 0;
    int engameTickCount = 0;

    @Override
    public void update(double dt) {
        updateState(dt);
//        this.velocity = Vector2DHelper.clamped(this.velocity, Config.PlayerProperties.MaxMoveSpeed, Config.PlayerProperties.MaxMoveSpeed);
//        this.velocity = new Vector2D(this.velocity.x * 0.85, this.velocity.y * 0.85);
        Vector2D velocityStep = Vector2DHelper.MutilByScalar(this.velocity, dt);
        this.position = Vector2DHelper.AddVector(this.position, velocityStep);
        // setup current frame for class
        super.update(dt);
    }

    public TileMap map;

    public void moveLeft() {
        characterDirection = CharacterDirection.LEFT;
    }

    public void moveRight() {
        characterDirection = CharacterDirection.RIGHT;
    }

    public void moveUp() {
        characterDirection = CharacterDirection.UP;
    }

    public void moveDown() {
        characterDirection = CharacterDirection.DOWN;
    }

    public void stopMove() {
        characterDirection = CharacterDirection.NONE;
    }
}
