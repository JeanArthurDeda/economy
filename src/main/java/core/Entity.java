package core;


import core.geometry.Location;
import core.seri.Pool;
import core.seri.Seri;
import core.seri.SeriPool;
import ecosystem.entities.valuable.sourced.Land;


abstract public class Entity implements Seri, SeriPool {
    public Location mLocation;
    public Land mLand;
    public String mName;

    public Entity (){
        Pool.getInstance().add(this);
    }

    public Land getLand (){
        return mLand;
    }

    public void setLand (Land land){
        mLand = land;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation (Location location) {
        mLocation = location;
    }

    public void setName (String name){
        mName = name;
    }

    public String getName() {
        return mName;
    }
}
