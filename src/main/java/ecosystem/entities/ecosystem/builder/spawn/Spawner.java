package ecosystem.entities.ecosystem.builder.spawn;

import core.Entity;
import core.SeriList;
import core.SeriMap;
import core.seri.Seri;

public class Spawner implements Seri {
    public SeriMap<Class<? extends Entity>, SpawnParam> mClasses = new SeriMap<>();
    public SeriList<Class<? extends Entity>> mOrderedKeys = new SeriList<>();

    public Spawner spawn (Class<? extends Entity> entityClass, SpawnParam spawnParam){
        mClasses.put(entityClass, spawnParam);
        mOrderedKeys.add (entityClass);
        return this;
    }

    public SeriList<Entity> spawn () throws InstantiationException, IllegalAccessException {
        SeriList<Entity> entities = new SeriList<>();

        for (Class<? extends Entity> key : mOrderedKeys){
            SpawnParam spawnParam = mClasses.get(key);
            SeriList<Entity> localEntities = new SeriList<>();

            // spawn them
            int num = spawnParam.getCount();
            for (int i = 0; i < num; ++i)
                localEntities.add ((Entity)create(key));

            // apply the fields setters
            spawnParam.apply(localEntities);

            entities.addAll(localEntities);
        }

        return entities;
    }

}
