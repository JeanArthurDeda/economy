package ecosystem;

import core.Entity;
import core.seri.SeriGraphPool;
import core.seri.SeriConf;
import core.seri.TokenStream;
import core.seri.wrapers.SeriMap;
import core.seri.wrapers.SeriList;
import core.geometry.Location;
import core.seri.Seri;

import ecosystem.entities.core.SeriEntities;
import ecosystem.entities.core.partition.Partitioner;
import ecosystem.entities.transactional.Bank;
import ecosystem.entities.transactional.Banks.NationalBank;
import ecosystem.entities.transactional.Cell;
import ecosystem.entities.transactional.Company;
import ecosystem.entities.transactional.Government;
import ecosystem.entities.valuable.service.Education;
import ecosystem.entities.valuable.service.Health;
import ecosystem.entities.valuable.service.Information;
import ecosystem.entities.valuable.service.Legal;
import ecosystem.entities.valuable.service.Protection;
import ecosystem.entities.valuable.service.Transportation;
import ecosystem.entities.valuable.sourced.Consumable;
import ecosystem.entities.valuable.sourced.Drinks;
import ecosystem.entities.valuable.sourced.Electricity;
import ecosystem.entities.valuable.sourced.Electronics;
import ecosystem.entities.valuable.sourced.Entertainment;
import ecosystem.entities.valuable.sourced.Food;
import ecosystem.entities.valuable.sourced.Furniture;
import ecosystem.entities.valuable.sourced.Gas;
import ecosystem.entities.valuable.sourced.Immobile;
import ecosystem.entities.valuable.sourced.Land;
import ecosystem.entities.valuable.sourced.NaturalResource;
import ecosystem.entities.valuable.sourced.EVechicle;



public class Ecosystem implements Seri {
    public SeriMap<Class<? extends Entity>, Integer> mEntitiesClassCount = new SeriMap<>();

    public SeriEntities mEntities = new SeriEntities();
    public SeriMap<Class<? extends Entity>, Partitioner> mQuadPartitioners = new SeriMap<>();

    public Ecosystem() {
        // generate the list of entity extended classes
        genEntitiesClassCount();
    }

    public SeriEntities getEntities() {
        return mEntities;
    }

    public SeriList<Entity> getEntities(Class<? extends Entity> entityClass) {
        return mEntities.get(entityClass);
    }

    public void setPartitioner(Class<? extends Entity> clasa, Partitioner partitioner){
        mQuadPartitioners.put (clasa, partitioner);
    }

    public Partitioner getPartitioner (Class<? extends Entity> clasa){
        return mQuadPartitioners.get(clasa);
    }

    // ===============
    // Entity spawning
    // ===============
    public void linkEntity (Entity entity){
        Class<? extends Entity> clasa = entity.getClass();
        Partitioner partioner = mQuadPartitioners.get(clasa);

        if (partioner != null)
            partioner.add(entity);
        mEntities.add(entity);

        // land link
        if (clasa != Land.class){
            SeriList<Entity> lands = getEntities(Land.class);
            double minDist = Location.WORLD_DIAGONAL_SIZE;
            for (Entity landEntity : lands){
                Land land = (Land)landEntity;
                double dist = entity.getLocation().dist(land.getLocation());
                if (dist < minDist){
                    entity.setLand(land);
                    minDist = dist;
                }
            }
            Land land = entity.getLand();
            land.addEntity(entity);
        }

        // generate name
        int count = getEntitiesClassCount().get (clasa);
        String name = clasa.getSimpleName() + "_" + count;
        entity.setName(name);
        getEntitiesClassCount().put (clasa, count + 1);
    }

    public void unlinkEntity (Entity entity){
        mEntities.remove(entity);

        // land unlink
        if (entity.getClass() != Land.class) {
            Land land = entity.getLand();
            land.removeEntity(entity);
        }
    }

    // ===========================================
    // Generate a list with all the loaded classes
    // ===========================================
    public void genEntitiesClassCount(){
        mEntitiesClassCount.put(Bank.class, 0);
        mEntitiesClassCount.put(NationalBank.class, 0);
        mEntitiesClassCount.put(Cell.class, 0);
        mEntitiesClassCount.put(Company.class, 0);
        mEntitiesClassCount.put(Government.class, 0);
        mEntitiesClassCount.put(Education.class, 0);
        mEntitiesClassCount.put(Health.class, 0);
        mEntitiesClassCount.put(Information.class, 0);
        mEntitiesClassCount.put(Legal.class, 0);
        mEntitiesClassCount.put(Protection.class, 0);
        mEntitiesClassCount.put(Transportation.class, 0);
        mEntitiesClassCount.put(Consumable.class, 0);
        mEntitiesClassCount.put(Drinks.class, 0);
        mEntitiesClassCount.put(Electricity.class, 0);
        mEntitiesClassCount.put(Electronics.class, 0);
        mEntitiesClassCount.put(Entertainment.class, 0);
        mEntitiesClassCount.put(Food.class, 0);
        mEntitiesClassCount.put(Gas.class, 0);
        mEntitiesClassCount.put(Furniture.class, 0);
        mEntitiesClassCount.put(Immobile.class, 0);
        mEntitiesClassCount.put(Land.class, 0);
        mEntitiesClassCount.put(NaturalResource.class, 0);
        mEntitiesClassCount.put(EVechicle.class, 0);
    }

    public SeriMap<Class<? extends Entity>, Integer> getEntitiesClassCount() {
        return mEntitiesClassCount;
    }

    // ====================================================
    // Serialization & Deserialization with respect to pool
    // ====================================================
    public void save(StringBuilder stream) throws IllegalAccessException {
        SeriGraphPool.getInstance().serialize("", stream);
        stream.append(SeriConf.NEWLINE);
        serialize("", stream);
    }

    public void load (TokenStream stream) throws Exception {
        SeriGraphPool.getInstance().deserialize(stream);
        deserialize(stream);
    }

    @Override
    public void deserialize(TokenStream stream) throws Exception {

    }
}
