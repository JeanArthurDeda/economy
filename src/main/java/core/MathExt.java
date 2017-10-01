package core;

public final class MathExt  {
    public static double clamp (double x, double min, double max){
        if (x > max)
            return max;
        if (x < min)
            return min;
        return x;
    }
}
