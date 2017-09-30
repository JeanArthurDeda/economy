package ecosystem.builder.buildactions.spawner.postspawnactions;

import core.Entity;
import core.SeriList;
import core.seri.Seri;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public abstract class PostSpawnAction implements Seri {
    public boolean mIsCached;

    public void cache () throws Exception {
        mIsCached = true;
    }

    public boolean isCached (){
        return mIsCached;
    }

    public abstract void execute(SeriList<Entity> entities) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;
}
