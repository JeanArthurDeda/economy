package ecosystem.entities.valuable.sourced;


import core.Entity;
import core.location.Hull;
import ecosystem.entities.categories.Valuable;
import ecosystem.entities.core.SeriEntities;

public class Land extends Valuable {
    public Hull mHull;
    public SeriEntities mEntities = new SeriEntities();

    public Land setHull (Hull hull){
        mHull = hull;
        return this;
    }

    public Hull getHull() {
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
