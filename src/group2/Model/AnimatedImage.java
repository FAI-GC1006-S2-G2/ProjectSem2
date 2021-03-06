package group2.Model;

import javafx.scene.image.Image;

/**
 * Created by BTC on 2015/12/17.
 */

public class AnimatedImage {
   public Image[] frames;
   public double duration;
   public boolean repeat = false;

   public AnimatedImage(Image[] frames, double duration, boolean repeat) {
      this.frames = frames;
      this.duration = duration;
      this.repeat = repeat;
   }

   public AnimatedImage(Image[] frames, double duration) {
      this.frames = frames;
      this.duration = duration;
      this.repeat = true;
   }

   public Image getFrame(double elapsedTime) {
      if (frames.length == 1) return frames[0];
      if (elapsedTime > frames.length * duration && !repeat)
         return frames[frames.length - 1];

      double elapsed = (elapsedTime % (frames.length * duration));
      int index = (int)( elapsed / duration);
      return frames[index];
   }

   public boolean animationCompleted(double elapsedTime) {
      return elapsedTime > frames.length * duration;
   }

}

