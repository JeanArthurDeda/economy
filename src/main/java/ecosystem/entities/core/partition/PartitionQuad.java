package ecosystem.entities.core.partition;

import core.Entity;
import core.geometry.Bound;
import core.seri.Seri;
import core.seri.wrapers.SeriList;
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

    public SeriList<Entity> getEntities (Class<? extends Entity> clasa){
        return mEntities.get(clasa);
    }

    public Bound getBound() {
        return mBound;
    }
}
