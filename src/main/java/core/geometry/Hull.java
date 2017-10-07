package core.geometry;

import core.seri.SeriGraphPool;
import core.seri.SeriGraph;
import core.seri.wrapers.SeriList;
import core.seri.Seri;

import java.awt.*;
import java.awt.geom.Line2D;

// convex hull
public class Hull implements Seri, SeriGraph {
    public enum CheckResult {
        INSIDE,
        OUTSIDE,
        INTERSECT
    }

    public SeriList<Location> mLocations = new SeriList<>();
    public SeriList<Plane> mPlanes = new SeriList<>();
    public SeriList<Hull> mNeighbors = new SeriList<>();
    public Bound mBound = new Bound();

    public Hull (){
        SeriGraphPool.getInstance().add(this);
    }

    public Hull setFromWorldBound(){
        mLocations.clear();
        mLocations.add(new Location(-1.0, -1.0));
        mLocations.add(new Location(1.0, -1.0));
        mLocations.add(new Location(1.0, 1.0));
        mLocations.add(new Location(-1.0, 1.0));
        invalidateInternalData();
        return this;
    }

    public SeriList<Location> getLocations () {
        return mLocations;
    }

    public SeriList<Plane> getPlanes() {
        return mPlanes;
    }

    public SeriList<Hull> getNeighbors (){
        return mNeighbors;
    }

    public Bound getBound() {
        return mBound;
    }

    public CheckResult checkWithPlane(Plane plane){
        double min = Double.MAX_VALUE;
        double max = - Double.MAX_VALUE;
        for (Location location : mLocations) {
            double d = plane.dist(location);
            if (d > max)
                max = d;
            if (d < min)
                min = d;
        }
        if (min * max < 0.0)
            return CheckResult.INTERSECT;
        if (min <= 0.0)
            return CheckResult.INSIDE;
        return CheckResult.OUTSIDE;
    }

    // keeps only the inside, should be invoked only if checkWithPlane returned INTERSECT
    public Hull carve (Plane plane, Hull hull){
        SeriList<Location> locations = new SeriList<>();
        SeriList<Plane> planes = new SeriList<>();
        SeriList<Hull> neighbors = new SeriList<>();
        mBound.reset();

        // iterate over all the edges
        boolean exited = false;
        int size = mLocations.size();
        Location l1 = mLocations.get(0);
        mBound.add (l1);
        double d1 = plane.dist(l1);
        Location l2;
        double d2;
        for (int i = 1; i <= size; ++i){
            l2 = mLocations.get(i % size);
            mBound.add (l2);
            d2 = plane.dist(l2);
            Plane p = mPlanes.get(i - 1);
            Hull n = mNeighbors.get(i - 1);

            double max = Math.max(d1, d2);

            // intersect
            if (d1 * d2 < 0.0){
                // does it exists
                if (d1 < 0.0) {
                    locations.add(l1);
                    planes.add (p);
                    neighbors.add (n);
                    exited = true;
                } else {
                    if (exited) {
                        planes.add(new Plane(plane));
                        neighbors.add (hull);
                    }
                    planes.add (p);
                    neighbors.add (n);
                }
                Location inter = new Location();
                double ratio = Math.abs(d1) / (Math.abs(d1) + Math.abs(d2));
                inter.set (l2).sub(l1).scale(ratio).add(l1);
                mBound.add (inter);
                locations.add (inter);
            } else if (max <= 0.0){
                locations.add (l1);
                planes.add (p);
                neighbors.add (n);
                if (0.0 == d2) {
                    locations.add(l2);
                    planes.add (new Plane(plane));
                    neighbors.add (hull);
                }
            }

            l1 = l2;
            d1 = d2;
        }
        if (locations.size() > planes.size()) {
            planes.add(new Plane(plane));
            neighbors.add (hull);
        }
        mLocations = locations;
        mPlanes = planes;
        mNeighbors = neighbors;

        return this;
    }

    public Hull expand (double value){
        Location nor = new Location();

        int size = mLocations.size();
        for (int i = 0; i < size; i++) {
            nor.set(mPlanes.get(i).getNor())
                    .add(mPlanes.get((i + size - 1)%size).getNor())
                    .normalize().scale(value);
            mLocations.get(i).add (nor);
        }
        invalidateInternalData();

        return this;
    }

    public Hull render (Graphics2D render, int width, int height, int normalSize){
        int size = mLocations.size();
        for (int i = 0; i < size; i++) {
            Plane plane = mPlanes.get(i);
            Location a = mLocations.get(i);
            Location b = mLocations.get((i + 1) % size);
            // draw line
            render.draw(new Line2D.Double(  (a.getX() * 0.5 + 0.5) * width, (a.getY() * 0.5 + 0.5) * height,
                    (b.getX() * 0.5 + 0.5) * width, (b.getY() * 0.5 + 0.5) * height));
            // draw plane normal
            if (normalSize > 0) {
                Location center = plane.getLocation();
                double sx = (center.getX() * 0.5 + 0.5) * width;
                double sy = (center.getY() * 0.5 + 0.5) * height;
                double ex = sx + plane.getNor().getX () * normalSize;
                double ey = sy + plane.getNor().getY () * normalSize;
                render.draw(new Line2D.Double(sx, sy, ex, ey));
            }

        }
        return this;
    }

    protected void invalidateInternalData(){
        mPlanes.clear();
        mBound.reset();
        mNeighbors.clear();
        Location l1 = mLocations.get (0);
        Location l2;
        int size = mLocations.size();
        for (int i = 1; i <= size; ++i){
            l2 = mLocations.get (i % size);
            mPlanes.add (new Plane().setFromEdge(l1, l2));
            mNeighbors.add(null);
            mBound.add (l1);
            l1 = l2;
        }
    }
}
