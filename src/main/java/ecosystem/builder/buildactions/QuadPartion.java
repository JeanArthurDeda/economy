package ecosystem.builder.buildactions;

import core.Entity;
import core.SeriList;
import ecosystem.Ecosystem;
import ecosystem.entities.core.partition.QuadPartitionedEntities;

public class QuadPartion extends BuildAction {
    public SeriList<Class<? extends Entity>> mClasses;
    public QuadPartitionedEntities mPartitioner;

    public QuadPartion (Class<? extends Entity>[] classes, QuadPartitionedEntities partioner){
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
