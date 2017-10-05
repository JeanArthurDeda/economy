package ecosystem.builder.buildactions;

import core.Entity;
import core.seri.wrapers.SeriList;
import ecosystem.Ecosystem;
import ecosystem.entities.core.partition.Partitioner;

public class QuadPartion extends BuildAction {
    public SeriList<Class<? extends Entity>> mClasses;
    public Partitioner mPartitioner;

    public QuadPartion (Class<? extends Entity>[] classes, Partitioner partioner){
        mClasses = new SeriList<>(classes);
        mPartitioner = partioner;
    }

    @Override
    public void execute(Ecosystem ecosystem) throws Exception {
        for (Class<? extends Entity> clasa : mClasses) {
            ecosystem.setPartitioner(clasa, mPartitioner);
        }
    }
}
