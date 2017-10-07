package ecosystem.entities.valuable.sourced;


import core.Entity;
import core.geometry.Hull;
import ecosystem.entities.categories.Valuable;
import ecosystem.entities.core.LandHull;
import ecosystem.entities.core.SeriEntities;

public class Land extends Valuable {
    public LandHull mHull;
    public SeriEntities mEntities = new SeriEntities();

    public Land setHull (LandHull hull){
        mHull = hull;
        return this;
    }

    public LandHull getHull() {
        return mHull;
    }

    public void addEntity(Entity entity){
        mEntities.add(entity);
    }

    public void removeEntity(Entity entity){
        mEntities.remove(entity);
    }

    public SeriEntities getEntities () {
        return mEntities;
    }
}
