package group2;

import group2.Geometric.Vector2D;

/**
 * Created by BTC on 2015/12/17.
 */
public class Config {

   public final static double NANOSECONDPERSEC = 1000000000.0;

   public static class WindowProperties {
      public final static int WINDOW_WIDTH = 800;
      public final static int WINDOW_HEIGHT = 600;
   }

   public static class PlayerProperties {
      public final static double JumpForce = 400;
      public final static double JumpCutOff = 200;
      public final static double MaxMoveSpeed = 160;
      public final static double WalkingAccelerate = 1000;

      public final static int WIDTH = 32;
      public final static int HEIGHT = 32;
   }

   public static class MapProperties{
      public final static int minResolve = 6;
      public final static int maxResolve = 26;
   }

   public static class CoinProperties{
      public final static int width = 8;
      public final static int height = 13;
      public final static int sizeW = 16;
      public final static int sizeH = 16;
   }

   public static class BirdProperties{
      public final static int maxBird = 40;
      public final static double TimeGenerate = 0.1;
      public final static int MaxMoveSpeed = 130;
   }
}
