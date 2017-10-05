package ecosystem.builder;

import core.seri.wrapers.SeriList;
import core.performance.TimedTask;
import core.seri.Seri;
import ecosystem.Ecosystem;
import ecosystem.builder.buildactions.BuildAction;
import ecosystem.entities.core.partition.Partitioner;
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

        ecosystem.setPartitioner(Land.class, new Partitioner(mNumLandQuads));

        TimedTask.start(getClass().getSimpleName() + ".build");

        for (BuildAction buildAction : mBuildActions) {
            TimedTask.start(buildAction.toString());

            buildAction.execute(ecosystem);

            TimedTask.finish();
        }

        TimedTask.finish();

        return ecosystem;
    }
}
