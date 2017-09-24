package core.distribution.scalar.index;

public class IndexScalarRandom extends IndexScalarDist {

    @Override
    public double get(double indexRatio) {
        return Math.random();
    }
}
