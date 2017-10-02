package core;

import java.util.Random;

public class Rand {
    protected static Random mGenerator = new Random(0);

    public static double get (){
        return mGenerator.nextDouble();
    }
}
