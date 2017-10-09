package ecosystem.builder.buildactions;

import core.seri.Seri;
import core.seri.SeriGraph;
import core.seri.SeriGraphPool;
import ecosystem.Ecosystem;

public abstract class BuildAction implements Seri, SeriGraph {
    public BuildAction (){
        SeriGraphPool.getInstance().add (this);
    }

    public abstract void execute(Ecosystem ecosystem) throws Exception;

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
