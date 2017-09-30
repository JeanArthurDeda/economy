package ecosystem.builder.buildactions.spawner;

import core.Entity;
import core.SeriList;
import ecosystem.builder.buildactions.spawner.postspawnactions.PostSpawnAction;
import core.seri.Seri;

public class SpawnParams implements Seri {
    public int mCount;
    public SeriList<PostSpawnAction> mPostSpawnActions = new SeriList<>();

    public SpawnParams(int count){
        mCount = count;
    }

    public SpawnParams executePostSpawn(PostSpawnAction postSpawnAction){
        mPostSpawnActions.add (postSpawnAction);
        return this;
    }

    public int getCount (){
        return mCount;
    }

    public void executePostSpawn(SeriList<Entity> entities) throws Exception {
        for (PostSpawnAction postSpawnAction : mPostSpawnActions) {
            if (!postSpawnAction.isCached())
                postSpawnAction.cache();
            postSpawnAction.execute(entities);
        }
    }
}
