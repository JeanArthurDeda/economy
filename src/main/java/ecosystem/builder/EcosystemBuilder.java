package ecosystem.builder;

import core.SeriList;
import core.seri.Seri;
import core.seri.SeriConf;
import ecosystem.Ecosystem;
import ecosystem.builder.buildactions.BuildAction;
import ecosystem.entities.core.partition.QuadPartitionedEntities;
import ecosystem.entities.valuable.sourced.Land;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

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

        long time = System.currentTimeMillis();
        for (BuildAction buildAction : mBuildActions) {
            long localTime = System.currentTimeMillis();
            buildAction.execute(ecosystem);
            localTime = System.currentTimeMillis() - localTime;
            System.out.println(String.format("%10d", localTime) + SeriConf.INDENT + buildAction.toString());
        }
        time = System.currentTimeMillis() - time;
        System.out.println(String.format("%10d", time) + " total of " + getClass().getSimpleName());

        return ecosystem;
    }
}
