package ecosystem.entities.valuable.sourced;


import core.Entity;
import ecosystem.entities.categories.Valuable;
import ecosystem.entities.core.SeriEntities;

public class Land extends Valuable {
    public SeriEntities mEntities = new SeriEntities();

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
