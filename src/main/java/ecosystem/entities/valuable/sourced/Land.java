package ecosystem.entities.valuable.sourced;


import core.Entity;
import ecosystem.entities.categories.ValuableEntity;
import ecosystem.entities.core.SeriEntities;

import java.util.List;

public class Land extends ValuableEntity{
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
