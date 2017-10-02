package core.location;

import core.MathExt;
import core.seri.Seri;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Bound implements Seri {
    public Location mMin = new Location(Double.MAX_VALUE, Double.MAX_VALUE);
    public Location mMax = new Location(-Double.MAX_VALUE, -Double.MAX_VALUE);

    public Bound (){
    }

    public Bound (Location min, Location max){
        mMax = max;
        mMin = min;
    }

    public Bound reset (){
        mMin.set(Double.MAX_VALUE, Double.MAX_VALUE);
        mMax.set(-Double.MAX_VALUE, -Double.MAX_VALUE);
        return this;
    }

    public Location getMin() {
        return mMin;
    }

    public Location getMax() {
        return mMax;
    }

    public Bound add (Location location){
        mMax.setX (Math.max (location.getX(), mMax.getX()));
        mMax.setY (Math.max (location.getY(), mMax.getY()));
        mMin.setX (Math.min (location.getX(), mMin.getX()));
        mMin.setY (Math.min (location.getY(), mMin.getY()));
        return this;
    }

    public double dist (double locationX, double locationY){
        double x = MathExt.clamp(locationX, mMin.getX(), mMax.getX()) - locationX;
        double y = MathExt.clamp(locationY, mMin.getY(), mMax.getY()) - locationY;

        return Math.sqrt (x * x + y * y);
    }

    public double dist (Location location){
        return dist (location.getX(), location.getY());
    }

    public Bound setClosest (Location set, Location reference){
        set.setX (MathExt.clamp(reference.getX(), mMin.getX(), mMax.getX()));
        set.setY (MathExt.clamp(reference.getY(), mMin.getY(), mMax.getY()));
        return this;
    }

    public double distSq (double locationX, double locationY){
        double x = MathExt.clamp(locationX, mMin.getX(), mMax.getX()) - locationX;
        double y = MathExt.clamp(locationY, mMin.getY(), mMax.getY()) - locationY;

        return x * x + y * y;
    }

    public double distSq (Location location){
        return distSq (location.getX(), location.getY());
    }

    public boolean contains (Location location){
        if (location.getX() > mMax.getX() ||
            location.getX() < mMin.getX() ||
            location.getY() > mMax.getY() ||
            location.getY() < mMin.getY())
            return false;
        return true;
    }

    public Bound render (Graphics2D render, int width, int height){
        render.draw(new Rectangle2D.Double( (mMin.getX() * 0.5 + 0.5) * width, (mMin.getY() * 0.5 + 0.5) * height,
                                            (mMax.getX() * 0.5 + 0.5) * width, (mMax.getY() * 0.5 + 0.5) * height));
        return this;
    }

}
