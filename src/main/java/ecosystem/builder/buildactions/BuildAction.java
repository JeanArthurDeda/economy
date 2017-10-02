package ecosystem.builder.buildactions;

import core.seri.Seri;
import ecosystem.Ecosystem;

public abstract class BuildAction implements Seri {
    public abstract void execute(Ecosystem ecosystem) throws Exception;

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
