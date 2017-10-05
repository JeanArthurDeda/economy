package core;

import java.util.Random;

public final class MathExt  {
    protected static Random mGenerator = new Random(0);

    public static double random (){
        return mGenerator.nextDouble();
    }
    public static double clamp (double value, double min, double max){
        if (value > max)
            return max;
        if (value < min)
            return min;
        return value;
    }
    public static int clamp (int value, int min, int max){
        if (value > max)
            return max;
        if (value < min)
            return min;
        return value;
    }
}
