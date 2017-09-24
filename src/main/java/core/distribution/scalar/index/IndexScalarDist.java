package core.distribution.scalar.index;

import core.seri.Seri;

// Distributes a scalar based on a index ratio

public abstract class IndexScalarDist implements Seri {
    public abstract double get (double indexRatio);

    public void consolidate () {

    }
}
