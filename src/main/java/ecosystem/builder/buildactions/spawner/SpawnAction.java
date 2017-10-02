package ecosystem.builder.buildactions.spawner;

import core.Entity;
import core.SeriList;
import core.SeriMap;
import core.seri.SeriConf;
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
            long time = System.currentTimeMillis();
            int count = spawnParams.getCount();
            for (int i = 0; i < count; ++i)
                entities.add ((Entity)create(key));
            time = System.currentTimeMillis() - time;
            System.out.println(String.format("%10d", time) + SeriConf.INDENT + SeriConf.INDENT + "spawning " + entities.size());

            // post spawn actions
            spawnParams.executePostSpawn(entities);

            // link them
            time = System.currentTimeMillis();
            for (Entity entity : entities) {
                ecosystem.linkEntity(entity);
            }
            time = System.currentTimeMillis() - time;
            System.out.println(String.format("%10d", time) + SeriConf.INDENT + SeriConf.INDENT + "Linking " + entities.size());
        }
    }
}
