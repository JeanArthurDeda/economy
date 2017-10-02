package ecosystem.builder.buildactions.spawner;

import core.Entity;
import core.SeriList;
import core.seri.Seri;
import core.seri.SeriConf;

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
            if (!postSpawnAction.isCached()) {
                long time = System.currentTimeMillis();
                postSpawnAction.cache();
                time = System.currentTimeMillis() - time;
                System.out.println(String.format("%10d", time) + SeriConf.INDENT + SeriConf.INDENT + "Cache " + postSpawnAction.toString());
            }
            long time = System.currentTimeMillis();
            postSpawnAction.execute(entities);
            time = System.currentTimeMillis() - time;
            System.out.println(String.format("%10d", time) + SeriConf.INDENT + SeriConf.INDENT + postSpawnAction.toString());
        }
    }
}
