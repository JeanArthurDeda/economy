package ecosystem.builder;

import core.SeriList;
import core.seri.Seri;
import ecosystem.Ecosystem;
import ecosystem.builder.buildactions.BuildAction;

public class EcosystemBuilder implements Seri {
    public SeriList<BuildAction> mBuildActions = new SeriList<>();

    public EcosystemBuilder execute(BuildAction buildAction){
        mBuildActions.add (buildAction);
        return this;
    }

    public Ecosystem build () throws Exception {
        Ecosystem ecosystem = new Ecosystem();

        for (BuildAction buildAction : mBuildActions) {
            buildAction.execute(ecosystem);
        }

        return ecosystem;
    }
}
