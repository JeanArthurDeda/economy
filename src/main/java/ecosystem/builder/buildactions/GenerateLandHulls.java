package ecosystem.builder.buildactions;

import core.Entity;
import core.SeriList;
import core.location.Hull;
import core.location.Location;
import core.location.Plane;
import ecosystem.Ecosystem;
import ecosystem.entities.valuable.sourced.Land;

public class GenerateLandHulls extends BuildAction {

    @Override
    public void execute(Ecosystem ecosystem) throws Exception {
        Location middle = new Location();
        Location nor = new Location();
        Plane plane = new Plane();
        SeriList<Entity> entities = ecosystem.getEntities(Land.class);

        // brute force
        for (Entity e : entities) {
            Land land = (Land)e;
            Location l1 = e.getLocation();
            Hull hull = new Hull().setWorldBound();

            for (Entity entity : entities) {
                if (e == entity)
                    continue;
                Location l2 = entity.getLocation();

                middle.set (l1).add(l2).scale(0.5);
                nor.set(l2).sub(l1).normalize();

                plane.set(middle, nor);

                if (Hull.CheckResult.INTERSECT == hull.checkWithPlane(plane)){
                    hull.carve(plane);
                }
            }

            land.setHull(hull);
        }
    }
}
