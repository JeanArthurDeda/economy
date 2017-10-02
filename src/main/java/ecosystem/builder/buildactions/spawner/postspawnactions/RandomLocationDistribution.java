package ecosystem.builder.buildactions.spawner.postspawnactions;

import core.Entity;
import core.Rand;
import core.location.Location;
import core.SeriList;
import ecosystem.builder.buildactions.spawner.PostSpawnAction;

public class RandomLocationDistribution extends PostSpawnAction {
    @Override
    public void execute(SeriList<Entity> entities) {
        entities.forEach(entity -> {
            double x = Rand.get();
            double y = Rand.get();
            entity.setLocation(new Location().setWorldRatio(x, y));
        });
    }
}
