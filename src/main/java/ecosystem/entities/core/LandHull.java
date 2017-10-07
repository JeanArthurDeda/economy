package ecosystem.entities.core;

import core.geometry.Hull;
import ecosystem.entities.valuable.sourced.Land;

public class LandHull extends Hull {
    public Land mLand;

    public LandHull (Land land){
        mLand = land;
    }

    public Land getLand (){
        return mLand;
    }
}
