package ecosystem.builder.buildactions.spawner.postspawnactions;

import core.Entity;
import core.MathExt;
import core.geometry.Location;
import core.seri.wrapers.SeriList;
import ecosystem.builder.buildactions.spawner.PostSpawnAction;

public class RandomLocationDistribution extends PostSpawnAction {
    @Override
    public void execute(SeriList<Entity> entities) {
        entities.forEach(entity -> {
            double x = MathExt.random();
            double y = MathExt.random();
            entity.setLocation(new Location().setWorldRatio(x, y));
        });
    }
}
