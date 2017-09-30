package ecosystem.builder.buildactions.spawner.postspawnactions;

import core.Entity;
import core.Location;
import core.SeriList;

public class RandomLocationDistribution extends PostSpawnAction {
    @Override
    public void execute(SeriList<Entity> entities) {
        entities.forEach(entity -> {
            double x = Math.random();
            double y = Math.random();
            entity.setLocation(new Location().setWorldRatio(x, y));
        });
    }
}
