package ecosystem.entities.ecosystem.builder;

import core.Entity;
import core.SeriList;
import core.seri.Seri;
import ecosystem.entities.ecosystem.Ecosystem;
import ecosystem.entities.ecosystem.builder.spawn.Spawner;

public class EcosystemBuilder implements Seri {
    public Spawner mSpawner = new Spawner();

    public EcosystemBuilder setSpawner(Spawner spawner){
        mSpawner = spawner;
        return this;
    }

    public Ecosystem build () throws IllegalAccessException, InstantiationException {
        Ecosystem ecosystem = new Ecosystem();

        SeriList<Entity> entities = mSpawner.spawn();
        for (Entity entity : entities){
            ecosystem.linkEntity(entity);
        }

        return ecosystem;
    }
}
