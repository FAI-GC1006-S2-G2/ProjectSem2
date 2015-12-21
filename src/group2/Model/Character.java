package group2.Model;

import group2.Geometric.Rect;
import group2.Geometric.Vector2D;
import javafx.scene.image.Image;
import xmlwise.Plist;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by BTC on 2015/12/17.
 */

public abstract class Character extends GameObject {

   protected Map<CharacterState, AnimatedImage> frameDictionary;
   protected double timeElapsedSinceStartAnimation; // second
   protected AnimatedImage animation;
   protected int characterDirection;
   protected CharacterState characterState;
   protected boolean isMoving = false;
   protected boolean onGround = false;
   protected boolean onWall = false;

   public boolean isActive = false;
   public Vector2D desiredPosition;
   public Vector2D velocity = Vector2D.zero;
   public int life;
   public CharacterState getState() {
      return this.characterState;
   }
   public void setOnGround(boolean onGround) {
      this.onGround = onGround;
      if (this.onGround)
         this.velocity = new Vector2D(this.velocity.x, 0);
   }

   public void setOnWall(boolean onWall) {
      this.onWall = onWall;
      if (this.onWall)
         this.velocity = new Vector2D(0, this.velocity.y);
   }

   public boolean getOnWall() { return this.onWall; }
   public void tookHit(Character character) {

   }

   public Rect collisionBoundingBox() {
      return new Rect(desiredPosition.x - this.size.width / 2, desiredPosition.y - this.size.height / 2, this.size.width, this.size.height);
   }

   public void loadAnimations() {}

   public void changeState(CharacterState newState) {
      if (newState == this.characterState) return;
      this.characterState = newState;
      this.timeElapsedSinceStartAnimation = 0;
      animation = frameDictionary.get(this.characterState);
   }

   public Character(String imageNamed) {
      super(imageNamed);
      this.characterState = CharacterState.STANDING;
      frameDictionary = new HashMap<CharacterState, AnimatedImage>();
      this.timeElapsedSinceStartAnimation = 0;
      this.loadAnimations();
   }

   protected void updateState(double dt) {

   }

   @Override
   public void update(double dt) {
      // setup current frame for animation purpose
      updateAnimation(dt);
   }

   protected void updateAnimation(double dt) {
      // this.flipX = this.velocity.x < 0;
      this.timeElapsedSinceStartAnimation += dt;
      double elapsedTime = (double)(this.timeElapsedSinceStartAnimation);
      animation = frameDictionary.get(this.characterState);
      animation.reapeat = isMoving;

      this.setTexture(animation.getFrame(elapsedTime));
   }

   protected AnimatedImage loadAnimations(String className, String animationName, boolean repeat) {
      try {
         Map<String, Object> root = Plist.load("data/" + className + ".plist");
         Map<String, Object> properties = (Map<String, Object>)root.get(animationName);
         String[] imageNames = properties.get("animationFrames").toString().split(",");
         double duration = Double.valueOf(properties.get("delay").toString());

         Image[] images = new Image[imageNames.length];
         for (int i = 0; i < images.length; i++) {
            images[i] = new Image(new FileInputStream("sprites/" + className + imageNames[i] + ".png"));
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

   public Character() {
      // TODO Auto-generated constructor stub
   }

   public enum CharacterState {
      STANDING, MOVE_UP, MOVE_RIGHT, MOVE_DOWN, MOVE_LEFT
   }

   public class CharacterDirection {
      public static final int NONE = 0;
      public static final int UP = 1;
      public static final int RIGHT = 2;
      public static final int DOWN = 3;
      public static final int LEFT = 4;

   }
}
