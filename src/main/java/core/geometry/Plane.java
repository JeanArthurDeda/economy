package core.geometry;

import core.seri.Seri;

public class Plane implements Seri {
    public Location mLocation;
    public Location mNor;

    public Plane (){

    }

    public Plane(Location location, Location nor) {
        mLocation = location;
        mNor = nor;
    }

    public Plane set (Location location, Location nor){
        mLocation = location;
        mNor = nor;
        return this;
    }

    public Plane setFromEdge (Location l1, Location l2){
        mNor = new Location(l2).sub(l1);
        mLocation = new Location(mNor).scale(0.5).add(l1);
        mNor.normalize().rotate90();
        return this;
    }

    public Location getLocation() {
        return mLocation;
    }

    public Location getNor() {
        return mNor;
    }

    public double dist (double locationX, double locationY){
        return mNor.dot(locationX - mLocation.getX(), locationY - mLocation.getY());
    }

    public double dist (Location location){
        return mNor.dot(location.getX() - mLocation.getX(), location.getY() - mLocation.getY());
    }
}
