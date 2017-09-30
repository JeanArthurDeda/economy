package ecosystem.builder.buildactions.spawner.postspawnactions;

import core.Entity;
import core.SeriList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RandomScalarDistribution extends PostSpawnAction {
    public String mSetFunName;
    public Double mTotalValue;

    public RandomScalarDistribution(String setFuncName, double totalValue){
        mSetFunName = setFuncName;
        mTotalValue = totalValue;
    }

    @Override
    public void execute(SeriList<Entity> entities) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        // =====
        // count
        // =====
        int count = 0;
        for (Entity entity : entities) {
            Method set = entity.getClass().getMethod(mSetFunName, double.class);
            if (set != null) {
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
        for (int i = 0; i < values.length; i++) {
            total += values[i] = Math.random();
        }
        double scale = mTotalValue / total;

        // ===
        // set
        // ===
        count = 0;
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            Method set = entity.getClass().getMethod(mSetFunName, double.class);
            if (set != null) {
                set.invoke(entity, values[count++] * scale);
            }
        }
    }
}
