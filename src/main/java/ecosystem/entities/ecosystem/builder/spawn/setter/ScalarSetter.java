package ecosystem.entities.ecosystem.builder.spawn.setter;

import core.Entity;
import core.SeriList;
import core.distribution.scalar.index.IndexScalarConstant;
import core.distribution.scalar.index.IndexScalarDist;
import core.seri.Seri;

import java.lang.reflect.Field;

public class ScalarSetter implements Seri {
    public Double mValue;
    public IndexScalarDist mIndexScalarDist = new IndexScalarConstant();

    public ScalarSetter(double value, IndexScalarDist indexScalarDist) {
        mValue = value;
        mIndexScalarDist = indexScalarDist;
    }

    public void apply(String fieldName, SeriList<Entity> entities) throws IllegalAccessException {
        // count the entities
        int count = 0;
        for (Entity entity : entities){
            Field[] fields = entity.getClass().getFields();
            Field field = getField(fieldName, fields);
            Class<?> fieldClass = field.getType();
            if (field == null || fieldClass != double.class)
                continue;
            count ++;
        }
        if (0 == count)
            return;

        // generate the values of each entity
        double[] values = new double[count];
        double total = 0.0;
        for (int i = 0; i < count; i ++){
            double x = (double)i / (double)(count - 1);
            values[i] = mIndexScalarDist.get(x);
            total += values[i];
        }
        double scale = mValue / total;

        // set the values
        int i = 0;
        for (Entity entity : entities){
            Field[] fields = entity.getClass().getFields();
            Field field = getField(fieldName, fields);
            if (field == null || field.getType() != double.class)
                continue;
            field.setDouble(entity, values[i++] * scale);
        }
    }
}
