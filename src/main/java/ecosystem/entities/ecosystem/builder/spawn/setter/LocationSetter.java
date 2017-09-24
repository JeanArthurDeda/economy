package ecosystem.entities.ecosystem.builder.spawn.setter;

import core.Entity;
import core.SeriList;
import core.distribution.location.IndexLocationDist;
import core.seri.Seri;


public class LocationSetter implements Seri {
    public IndexLocationDist mIndexLocationDist;

    public LocationSetter(IndexLocationDist indexScalarDist) {
        mIndexLocationDist = indexScalarDist;
    }

    public void apply(SeriList<Entity> entities) throws IllegalAccessException {
        // set the values
        int i = 0;
        int size = entities.size();
        for (Entity entity : entities){
            double indexRatio = (double)(i++) / (double)(size - 1);
            entity.setLocation(mIndexLocationDist.get(indexRatio));
        }
    }
}
