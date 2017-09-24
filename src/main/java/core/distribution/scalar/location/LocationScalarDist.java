package core.distribution.scalar.location;


import core.Location;

// distributes a scalar based on location
public abstract class LocationScalarDist {
    public abstract double get (Location location);
}
