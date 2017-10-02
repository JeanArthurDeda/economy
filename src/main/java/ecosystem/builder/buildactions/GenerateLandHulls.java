package ecosystem.builder.buildactions;

import core.Entity;
import core.SeriList;
import core.location.Hull;
import core.location.Location;
import core.location.Plane;
import ecosystem.Ecosystem;
import ecosystem.entities.core.partition.PartitionQuad;
import ecosystem.entities.core.partition.QuadPartitionedEntities;
import ecosystem.entities.core.partition.SpiralIterator;
import ecosystem.entities.valuable.sourced.Land;

public class GenerateLandHulls extends BuildAction {

    @Override
    public void execute(Ecosystem ecosystem) throws Exception {
        QuadPartitionedEntities partioner = ecosystem.getPartitioner(Land.class);
        SeriList<PartitionQuad> quads = partioner.getQuads();

        // ========================
        // Spiral iterator Receiver
        // ========================
        class LandHullConstructor extends SpiralIterator.Receiver{
            // inputs
            protected Land mLand;
            // processing
            protected Hull mHull;
            protected Location mMiddle = new Location();
            protected Location mNor = new Location();
            protected Plane mPlane = new Plane();
            protected int mLandQuadX;
            protected int mLandQuadY;
            // finishing
            protected boolean mSpiralSquareDirty;

            public void setLand (Land land, int quadX, int quadY){
                mLandQuadX = quadX;
                mLandQuadY = quadY;
                mLand = land;
            }

            @Override
            public void onStart(int startX, int startY) {
                mHull = new Hull().setFromWorldBound();
                mLand.setHull(mHull);
            }

            @Override
            public boolean onStartSquare(int x, int y, int startX, int startY) {
                if (!mSpiralSquareDirty)
                    return false;
                mSpiralSquareDirty = false;
                return true;
            }

            @Override
            public boolean onValidValue(int x, int y, int startX, int startY) {
                PartitionQuad quad = quads.get(y * partioner.getNumQuads() + x);

                // trivial rejection based on population
                SeriList<Entity> entities = quad.getEntities().get(Land.class);
                if (null == entities){
                    return true;
                }

                // trivial rejection based on bound
                Location l1 = mLand.getLocation();
                if (x != mLandQuadX && y != mLandQuadY) {
                    quad.getBound().setClosest(mMiddle, l1);
                    mNor.set(mMiddle).sub(l1).normalize();
                    mMiddle.add(l1).scale(0.5);
                    mPlane.set(mMiddle, mNor);
                    if (Hull.CheckResult.INTERSECT != mHull.checkWithPlane(mPlane)) {
                        return true;
                    }
                }

                for (Entity entity : entities) {
                    if (mLand == entity)
                        continue;
                    Location l2 = entity.getLocation();

                    // generate the voronoi plane
                    mMiddle.set (l1).add(l2).scale(0.5);
                    mNor.set(l2).sub(l1).normalize();
                    mPlane.set (mMiddle, mNor);

                    if (Hull.CheckResult.INTERSECT == mHull.checkWithPlane(mPlane)){
                        mHull.carve(mPlane);
                        mSpiralSquareDirty = true;
                    }
                }
                return true;
            }
        }
        LandHullConstructor hullConstructor = new LandHullConstructor();
        SpiralIterator spiralIterator = new SpiralIterator();

        SeriList<Entity> entities = ecosystem.getEntities(Land.class);
        for (Entity entity : entities) {
            int x = partioner.getQuadX(entity.getLocation());
            int y = partioner.getQuadY(entity.getLocation());
            hullConstructor.setLand((Land)entity, x, y);
            spiralIterator.iterate(x, y, partioner.getNumQuads(), partioner.getNumQuads(), hullConstructor);
        }
    }
}
