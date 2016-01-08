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

//    private void checkCollision(double dt) {
//        double x = this.getPosition().x;
//        double y = this.getPosition().y;
//        int data[] = map.layersList.get(0).getData();
//
//
//        switch (characterDirection) {
//            case CharacterDirection.LEFT: {
//                int row = (int) y / TileMap.tileHeight;
//                int col = (int) x / TileMap.tileWidth;
//                if (x < TileMap.tileWidth) {
//                    this.setPosition(new Vector2D((col + 1) * TileMap.tileWidth, y));
//                    return;
//                }
//                int left1 = TileMap.getIndexOfData(row, col);
//                int left2 = TileMap.getIndexOfData(row + 1, col);
//                // check left collision
//                if (y % TileMap.tileHeight == 0 && data[left1] == 0) {
//                    return;
//                }
//                if (data[left1] == 0 && data[left2] == 0) {
//                    return;
//                }
//                this.setPosition(new Vector2D((col + 1) * TileMap.tileWidth, y));
//            }
//            break;
//            case CharacterDirection.RIGHT: {
//                int row = (int) y / TileMap.tileHeight;
//                int col = (int) (x + size.width) / TileMap.tileWidth;
////                if ((x + size.width) >)
//                int right1 = TileMap.getIndexOfData(row, col);
//                int right2 = TileMap.getIndexOfData(row + 1, col);
//                // check right collision
//                if (data[right1] == 0 && data[right2] == 0) {
//                    return;
//                } else if (y % TileMap.tileHeight == 0 && data[right1] == 0) {
//                    return;
//                }
//                this.setPosition(new Vector2D((col - 1) * TileMap.tileWidth, y));
//            }
//            break;
//            case CharacterDirection.DOWN: {
//                int row = (int) (y + size.height) / TileMap.tileHeight;
//                int col = (int) x / TileMap.tileWidth;
//                int down1 = TileMap.getIndexOfData(row, col);
//                int down2 = TileMap.getIndexOfData(row, col + 1);
//                // check down collision
//                if (data[down1] == 0 && data[down2] == 0) {
//                    return;
//                } else if (x % TileMap.tileHeight == 0 && data[down1] == 0) {
//                    return;
//                }
//                this.setPosition(new Vector2D(x, (row - 1) * TileMap.tileHeight));
//            }
//            break;
//            case CharacterDirection.UP: {
//                int row = (int) y / TileMap.tileHeight;
//                int col = (int) x / TileMap.tileWidth;
//                int up1 = TileMap.getIndexOfData(row, col);
//                int up2 = TileMap.getIndexOfData(row, col + 1);
//                // check up collision
//                if (data[up1] == 0 && data[up2] == 0) {
//                    return;
//                }
//                if (x % TileMap.tileHeight == 0 && data[up1] == 0) {
//                    return;
//                }
//                this.setPosition(new Vector2D(x, (row + 1) * TileMap.tileHeight));
//            }
//        }
//        // check right collision
//
//    }
    // getter & setters

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
