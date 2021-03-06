package ecosystem.builder.buildactions.spawner;

import core.Entity;
import core.seri.SeriGraph;
import core.seri.SeriGraphPool;
import core.seri.wrapers.SeriList;
import core.seri.Seri;

import java.lang.reflect.InvocationTargetException;

public abstract class PostSpawnAction implements Seri, SeriGraph {
    protected transient boolean mIsCached;

    public PostSpawnAction(){
        SeriGraphPool.getInstance().add (this);
    }

    public void cache () throws Exception {
        mIsCached = true;
    }

    public boolean isCached (){
        return mIsCached;
    }

    public abstract void execute(SeriList<Entity> entities) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
