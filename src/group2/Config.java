package group2;

import group2.Geometric.Vector2D;

/**
 * Created by BTC on 2015/12/17.
 */
public class Config {

   public final static double NANOSECONDPERSEC = 1000000000.0;

   public static class WindowProperties {
      public final static int WINDOW_WIDTH = 480;
      public final static int WINDOW_HEIGHT = 480;
   }

   public static class PlayerProperties {
      public final static double JumpForce = 400;
      public final static double JumpCutOff = 200;
      public final static double MaxMoveSpeed = 500;
      public final static double WalkingAccelerate = 1500;

      public final static int Width = 32;
      public final static int Height = 32;
   }

}
