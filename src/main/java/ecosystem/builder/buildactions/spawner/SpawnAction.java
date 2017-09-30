package ecosystem.builder.buildactions.spawner;

import core.Entity;
import core.SeriList;
import core.SeriMap;
import ecosystem.Ecosystem;
import ecosystem.builder.buildactions.BuildAction;

public class SpawnAction extends BuildAction {
    public SeriMap<Class<? extends Entity>, SpawnParams> mClasses = new SeriMap<>();
    public SeriList<Class<? extends Entity>> mOrderedKeys = new SeriList<>();

    public SpawnAction spawnClass(Class<? extends Entity> entityClass, SpawnParams spawnParams){
        mClasses.put(entityClass, spawnParams);
        mOrderedKeys.add (entityClass);
        return this;
    }

    @Override
    public void execute(Ecosystem ecosystem) throws Exception {
        for (Class<? extends Entity> key : mOrderedKeys){
            SpawnParams spawnParams = mClasses.get(key);
            SeriList<Entity> entities = new SeriList<>();

            // spawn them
            int count = spawnParams.getCount();
            for (int i = 0; i < count; ++i)
                entities.add ((Entity)create(key));

            // post spawn actions
            spawnParams.executePostSpawn(entities);

            // link them
            for (Entity entity : entities) {
                ecosystem.linkEntity(entity);
            }
        }
    }
}
