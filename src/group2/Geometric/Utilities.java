package group2.Geometric;

/**
 * Created by BTC on 2015/12/17.
 */
public class Utilities {
   public static double clamp(double input, double min, double max) {
      if (input < min) input = min;
      if (input > max) input = max;
      return input;
   }
}

