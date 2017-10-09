package ecosystem.builder.buildactions.spawner.postspawnactions;

import core.Entity;
import core.geometry.Location;
import core.seri.wrapers.SeriList;
import ecosystem.builder.buildactions.spawner.PostSpawnAction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GraphScalarDistribution extends PostSpawnAction {
    public String mSetMethodName;
    protected double mTotalValue;
    public SeriList<Location> mPoints = new SeriList<>();

    public GraphScalarDistribution(String setMethodName, double totalValue){
        mSetMethodName = setMethodName;
        mTotalValue = totalValue;
        mIsCached = true;
    }

    public GraphScalarDistribution(String setMethodName){
        mSetMethodName = setMethodName;
        mTotalValue = 0.0;
        mIsCached = true;
    }

    public GraphScalarDistribution graphPoint(double x, double y){
        mPoints.add (new Location(x, y));
        return this;
    }
    @Override
    public void execute(SeriList<Entity> entities) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // =====
        // count
        // =====
        int count = 0;
        for (Entity entity : entities) {
            Method setMethod = entity.getClass().getMethod(mSetMethodName, double.class);
            if (setMethod != null) {
                count++;
            }
        }
        if (0 == count)
            return;

        // ========
        // generate
        // ========
        double[] values = new double[count];
        double total = 0.0;

        int numPoints = mPoints.size();
        Location last = mPoints.get(numPoints - 1);
        Location first = mPoints.get (0);
        int pointIndex = 0;

        for (int i = 0; i < count; ++i){
            double ratio = (double)i / (double)(count - 1);
            double y;
            // check tail
            if (ratio <= mPoints.get(0).getX()) {
                y = first.getY();
            } else if (ratio >= last.getX()) {
                y = last.getY();
            } else {
                while (pointIndex < numPoints - 1 && ratio > mPoints.get(pointIndex).getX())
                    pointIndex ++;

                Location prev = mPoints.get(pointIndex - 1);
                Location next = mPoints.get(pointIndex);

                double r = (ratio - prev.getX()) / (next.getX() - prev.getX());
                y = prev.getY() + r * (next.getY() - prev.getY());
            }
            total += values[i] = y;
        }
        double scale = mTotalValue != 0.0 ? mTotalValue / total : 1.0;

        // ===
        // set
        // ===
        count = 0;
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            Method setMethod = entity.getClass().getMethod(mSetMethodName, double.class);
            if (setMethod != null) {
                setMethod.invoke(entity, values[count++] * scale);
            }
        }
    }
}
