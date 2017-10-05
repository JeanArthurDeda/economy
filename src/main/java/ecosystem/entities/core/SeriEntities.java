package ecosystem.entities.core;

import core.seri.wrapers.SeriList;
import core.Entity;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class SeriEntities extends SeriList<Entity> {
    protected HashMap<Class<? extends Entity>, SeriList<Entity>> mClassEntities = new HashMap<>();

    @Override
    public boolean add(Entity entity) {
        add2ClassList(entity);
        return super.add(entity);
    }

    @Override
    public boolean addAll(Collection<? extends Entity> entities) {
        for (Entity entity : entities)
            add2ClassList(entity);
        return super.addAll(entities);
    }

    @Override
    public boolean remove(Object entity) {
        if (!rem2ClassList(entity))
            return false;
        return super.remove(entity);
    }

    protected void add2ClassList(Entity entity) {
        // class partition
        SeriList<Entity> classList = mClassEntities.get(entity.getClass());
        if (classList != null)
            classList.add(entity);
        else{
            classList = new SeriList<>();
            classList.add(entity);
            mClassEntities.put(entity.getClass(), classList);
        }
    }

    protected boolean rem2ClassList(Object entity) {
        // class partition
        List<Entity> classList = mClassEntities.get(entity.getClass());
        if (classList == null)
            return true;
        classList.remove(entity);
        return false;
    }

    @Override
    public void clear() {
        mClassEntities.clear();
        super.clear();
    }

    public SeriList<Entity> get (Class<?> entityClass){
        return mClassEntities.get(entityClass);
    }

    public Set<Class<? extends Entity>> getClasses (){
        return mClassEntities.keySet();
    }
}
