package ecosystem.entities.ecosystem.builder.spawn;

import core.Entity;
import core.SeriList;
import core.SeriMap;
import core.distribution.location.IndexLocationDist;
import core.distribution.location.IndexLocationRandom;
import core.distribution.scalar.index.IndexScalarDist;
import core.seri.Seri;
import ecosystem.entities.ecosystem.builder.spawn.setter.LocationSetter;
import ecosystem.entities.ecosystem.builder.spawn.setter.ScalarSetter;

import java.util.Set;

public class SpawnParam implements Seri {
    public int mCount;
    public SeriMap<String, ScalarSetter> mScalarSetter = new SeriMap<>();
    public LocationSetter mLocationSetter;

    public SpawnParam (int count){
        mCount = count;
    }

    public SpawnParam setScalar(String fieldName, double totalValue, IndexScalarDist indexScalarDist){
        mScalarSetter.put(fieldName, new ScalarSetter(totalValue, indexScalarDist));
        return this;
    }

    public SpawnParam setLocation(IndexLocationDist indexLocationDist){
        mLocationSetter = new LocationSetter(indexLocationDist);
        return this;
    }

    public int getCount (){
        return mCount;
    }

    public void apply (SeriList<Entity> entities) throws IllegalAccessException {
        // set locations
        mLocationSetter.apply(entities);

        // set scalars
        Set<String> fieldNames = mScalarSetter.keySet();
        for (String fieldName : fieldNames){
            ScalarSetter scalarSetter = mScalarSetter.get(fieldName);
            scalarSetter.apply(fieldName, entities);
        }
    }
}
