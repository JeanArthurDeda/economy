package core.distribution.scalar.index;


import core.SeriList;
import core.seri.Seri;

public class IndexScalarGraph extends IndexScalarDist {
    public SeriList<Point2d> mPoints = new SeriList<>();

    public IndexScalarGraph add (double x, double y){
        mPoints.add (new Point2d(x, y));
        return this;
    }

    @Override
    public double get(double indexRatio) {
        // check tail
        Point2d first = mPoints.get (0);
        if (indexRatio <= mPoints.get(0).x)
            return first.y;
        int size = mPoints.size();
        Point2d last = mPoints.get(size - 1);
        if (indexRatio >= last.x)
            return last.y;

        int index = 0;
        while (index < size - 1 && indexRatio > mPoints.get(index).x)
            index ++;

        Point2d prev = mPoints.get(index - 1);
        Point2d next = mPoints.get(index);

        double ratio = (indexRatio - prev.x) / (next.x - prev.x);
        return prev.y + ratio * (next.y - prev.y);
    }

    public class Point2d implements Seri {
        public double x;
        public double y;

        public Point2d(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
