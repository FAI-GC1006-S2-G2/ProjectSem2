package group2.Scene;

import group2.Config;
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

   public GameScene() {
      super(new Group());
      setupGameLoop();
   }

   private void setupGameLoop() {
      this.root = (Group) super.getRoot();
      this.canvas = new Canvas(group2.Config.WindowProperties.WINDOW_WIDTH, group2.Config.WindowProperties.WINDOW_HEIGHT);
      root.getChildren().add(canvas);

      ArrayList<String> input = new ArrayList<String>();
      this.setOnKeyPressed(
            new EventHandler<KeyEvent>()
            {
               public void handle(KeyEvent e)
               {
                  String code = e.getCode().toString();

                  // only add once... prevent duplicates
                  if ( !input.contains(code) )
                     input.add( code );
               }
            });

      this.setOnKeyReleased(
            new EventHandler<KeyEvent>()
            {
               public void handle(KeyEvent e)
               {
                  String code = e.getCode().toString();
                  input.remove( code );
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

   public void handleEvents(List<String> input) {
//      if (input.contains("LEFT"))
////         this.player.shouldMoveLeft = true;
//      else if (input.contains("RIGHT"))
//         this.player.shouldMoveRight = true;
//      else {
//         this.player.shouldMoveLeft = false;
//         this.player.shouldMoveRight = false;
//      }
//
//      if (input.contains("SPACE"))
//         this.player.shouldJump = true;
//      else {
//         this.player.shouldJump = false;
//      }
   }

   public void update(long currentTime) {

      double dt = (currentTime - lastUpdateTime) / Config.NANOSECONDPERSEC;
      if (dt > 0.03) dt = 0.03;
      lastUpdateTime = currentTime;


      // logic code come here


      // for debug purpose
      if (debugInterval >= 30) {
         debugInterval = 0;
         this.fps = (int)(1 / dt);
      }
      debugInterval++;
   }

   public void render(GraphicsContext gc) {
      // clear canvas
      gc.clearRect(0, 0, Config.WindowProperties.WINDOW_WIDTH, Config.WindowProperties.WINDOW_HEIGHT);

      // our code will come here

      // for debug purpose
      gc.setStroke(Color.AQUA);
      gc.strokeText("FPS: " + String.valueOf(this.fps), this.getWidth() - 80, this.getHeight() - 30);
   }

}
