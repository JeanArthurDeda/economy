package ecosystem.entities.core.partition;

import core.geometry.Bound;
import core.seri.Seri;
import ecosystem.entities.core.SeriEntities;

public class PartitionQuad implements Seri {
    public SeriEntities mEntities;
    public Bound mBound;

    public PartitionQuad (Bound bound){
        mBound = bound;
        mEntities = new SeriEntities();
    }

    public SeriEntities getEntities (){
        return mEntities;
    }

    public Bound getBound() {
        return mBound;
    }
}
