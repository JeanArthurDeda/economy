package ecosystem.builder.buildactions.spawner;

import core.Entity;
import core.seri.wrapers.SeriList;
import core.seri.wrapers.SeriMap;
import core.performance.TimedTask;
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
            TimedTask.start("Spawn " + count + " " + key.getSimpleName());
            for (int i = 0; i < count; ++i)
                entities.add ((Entity)create(key));
            TimedTask.finish();

            // post spawn actions
            spawnParams.executePostSpawn(entities);

            // link them
            TimedTask.start("Link " + count + " " + key.getSimpleName());
            for (Entity entity : entities) {
                ecosystem.link(entity);
            }
            TimedTask.finish();
        }
    }
}
