package core;

import core.seri.Seri;

public class Location implements Seri {
    public static final double WORLD_EDGE_SIZE = 2.0f;

    public double mX;
    public double mY;

    public Location(){
    }

    public Location(double x, double y) {
        mX = x;
        mY = y;
    }

    public Location setWorldRatio (double xRatio, double yRatio){
        mX = xRatio * 2.0 - 1.0;
        mY = yRatio * 2.0 - 1.0;
        return this;
    }

    public Location jitter (double sizeX, double sizeY){
        mX += (Math.random() - 0.5) * sizeX;
        mY += (Math.random() - 0.5) * sizeY;
        return this;
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
