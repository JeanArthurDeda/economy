package ecosystem.builder.buildactions;

import core.Entity;
import core.seri.wrapers.SeriList;
import core.performance.TimedTask;
import core.geometry.Location;
import core.geometry.Plane;
import ecosystem.Ecosystem;
import ecosystem.entities.core.LandHull;
import ecosystem.entities.core.partition.PartitionQuad;
import ecosystem.entities.core.partition.Partitioner;
import ecosystem.entities.core.partition.SpiralIterator;
import ecosystem.entities.valuable.sourced.Land;

public class GenerateLandHulls extends BuildAction {

    @Override
    public void execute(Ecosystem ecosystem) throws Exception {
        SeriList<Entity> entities = ecosystem.getEntities(Land.class);
        Partitioner partioner = ecosystem.getPartitioner(Land.class);
        SeriList<PartitionQuad> quads = partioner.getQuads();

        // generate all the entities hulls
        for (Entity entity : entities) {
            Land land = (Land)entity;
            land.setHull((LandHull) new LandHull(land).setFromWorldBound());
        }

        // ========================
        // Spiral iterator Receiver
        // ========================
        class LandHullConstructor extends SpiralIterator.Receiver{
            // inputs
            protected Land mLand;
            // processing
            protected Location mMiddle = new Location();
            protected Location mNor = new Location();
            protected Plane mPlane = new Plane();
            // finishing
            protected boolean mDirty;

            public void setLand (Land land){
                mLand = land;
            }

            @Override
            public boolean onStartSquare(int x, int y, int startX, int startY) {
                if (!mDirty)
                    return false;
                mDirty = false;
                return true;
            }

            @Override
            public boolean onValidValue(int x, int y, int startX, int startY) {
                PartitionQuad quad = quads.get(y * partioner.getNumQuads() + x);

                // trivial rejection based on population
                SeriList<Entity> entities = quad.getEntities().get(Land.class);
                if (null == entities){
                    mDirty = true;
                    return true;
                }

                LandHull hull = mLand.getHull();
                // trivial rejection based on quad
                Location l1 = mLand.getLocation();
                if (x != startX || y != startY) {
                    double extent = 0;
                    SeriList<Location> locations = hull.getLocations();
                    for (Location location : locations) {
                        extent = Math.max (extent, location.dist(mLand.getLocation()));
                    }
                    double distToQuad = quad.getBound().dist(mLand.getLocation());

                    if (distToQuad * 0.5 > extent)
                        return true;
                    mDirty = true;
                } else {
                    mDirty = true;
                    if (entities.size() == 1){
                        return true;
                    }
                }

                for (Entity entity : entities) {
                    Land land = (Land)entity;
                    if (mLand == land)
                        continue;
                    Location l2 = entity.getLocation();

                    // generate the voronoi plane
                    mMiddle.set (l1).add(l2).scale(0.5);
                    mNor.set(l2).sub(l1).normalize();
                    mPlane.set (mMiddle, mNor);

                    if (LandHull.CheckResult.INTERSECT == hull.checkWithPlane(mPlane)){
                        hull.carve(mPlane, land.getHull());
                    }
                }
                return true;
            }
        }
        LandHullConstructor hullConstructor = new LandHullConstructor();
        SpiralIterator spiralIterator = new SpiralIterator();

        int size = entities.size();
        for (int i = 0; i < size; i++) {
            double ratio = (double)i / (double)size;
            TimedTask.update(ratio);
            Entity entity = entities.get(i);
            int x = partioner.getQuadX(entity.getLocation());
            int y = partioner.getQuadY(entity.getLocation());
            hullConstructor.setLand((Land)entity);
            spiralIterator.iterate(x, y, partioner.getNumQuads(), partioner.getNumQuads(), hullConstructor);
        }

    }
}
