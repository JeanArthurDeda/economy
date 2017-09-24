package core;

import core.seri.Seri;

public class Location implements Seri {
    public static final double WORLD_EDGE_SIZE = 2.0f;

    public double mX;
    public double mY;

    public Location(double x, double y) {
        mX = x;
        mY = y;
    }

    public double distTo(Location other){
        double x = mX - other.mX;
        double y = mY - other.mY;
        return x * x + y * y;
    }

    public double getX () {
        return mX;
    }

    public double getY () {
        return mY;
    }
}
