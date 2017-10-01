package ecosystem.builder;

import core.SeriList;
import core.seri.Seri;
import ecosystem.Ecosystem;
import ecosystem.builder.buildactions.BuildAction;
import ecosystem.entities.core.partition.QuadPartitionedEntities;
import ecosystem.entities.valuable.sourced.Land;

public class EcosystemBuilder implements Seri {
    public int mNumLandQuads;
    public SeriList<BuildAction> mBuildActions = new SeriList<>();

    public EcosystemBuilder(int numLandQuads) {
        mNumLandQuads = numLandQuads;
    }

    public EcosystemBuilder execute(BuildAction buildAction){
        mBuildActions.add (buildAction);
        return this;
    }

    public Ecosystem build () throws Exception {
        Ecosystem ecosystem = new Ecosystem();

        ecosystem.setPartitioner(Land.class, new QuadPartitionedEntities(mNumLandQuads));

        for (BuildAction buildAction : mBuildActions) {
            buildAction.execute(ecosystem);
        }

        return ecosystem;
    }
}
