package core.distribution.location;

import core.Location;

public class IndexLocationRandom extends IndexLocationDist {
    @Override
    public Location get(double indexRatio) {

        double sign = Math.random() < 0.5 ? -1.0 : 1.0;
        double x = Math.random() * sign;
        sign = Math.random() < 0.5 ? -1.0 : 1.0;
        double y = Math.random() * sign;

        return new Location(x, y);
    }
}
