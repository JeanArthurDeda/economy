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
import ecosystem.entities.core.partition.PartitionQuad;
import ecosystem.entities.core.partition.Partitioner;
import ecosystem.entities.core.partition.SpiralIterator;
import ecosystem.entities.valuable.sourced.Land;


public class Ecosystem implements Seri {
    public SeriMap<Class<? extends Entity>, Integer> mClassCount = new SeriMap<>();
    public SeriEntities mEntities = new SeriEntities();
    public SeriMap<Class<? extends Entity>, Partitioner> mPartitioners = new SeriMap<>();


    public SeriEntities getEntities() {
        return mEntities;
    }

    public SeriList<Entity> getEntities(Class<? extends Entity> entityClass) {
        return mEntities.get(entityClass);
    }

    public void setPartitioner(Class<? extends Entity> clasa, Partitioner partitioner){
        mPartitioners.put (clasa, partitioner);
    }

    public Partitioner getPartitioner (Class<? extends Entity> clasa){
        return mPartitioners.get(clasa);
    }

    // ===============
    // Entity spawning
    // ===============
    public void link(Entity entity){
        Class<? extends Entity> clasa = entity.getClass();

        Partitioner partitioner = mPartitioners.get(clasa);
        if (partitioner != null)
            partitioner.add(entity);
        mEntities.add(entity);

        // land link
        if (clasa != Land.class){
            linkToLand(entity);
        }

        // generate name
        int count = getClassCount(clasa);
        String name = clasa.getSimpleName() + "_" + count;
        entity.setName(name);
    }

    public void unlink (Entity entity){
        mEntities.remove(entity);

        // land unlink
        if (entity.getClass() != Land.class) {
            Land land = entity.getLand();
            land.removeEntity(entity);
        }
    }

    public int getClassCount(Class<? extends Entity> clasa){
        Integer c = mClassCount.get(clasa);
        int count = c == null ? 1 : c + 1;
        mClassCount.put(clasa, count);
        return count;
    }

    protected void linkToLand (Entity entity){
        Partitioner partitioner = getPartitioner(Land.class);
        int quadX = partitioner.getQuadX(entity.getLocation());
        int quadY = partitioner.getQuadY(entity.getLocation());

        new SpiralIterator().iterate(quadX, quadY, partitioner.getNumQuads(), partitioner.getNumQuads(), new SpiralIterator.Receiver() {
            @Override
            public boolean onValidValue(int x, int y, int startX, int startY) {
                PartitionQuad quad = partitioner.getQuad (y, x);
                SeriList<Entity> entities = quad.getEntities(Land.class);
                if (null == entities)
                    return true;
                Land entityLand = null;
                double minDist = Location.WORLD_DIAGONAL_SIZE;
                for (Entity e : entities) {
                    Land land = (Land)e;
                    double dist = land.getLocation().dist(entity.getLocation());
                    if (dist < minDist){
                        minDist = dist;
                        entityLand = land;
                    }
                }
                entity.setLand(entityLand);
                return false;
            }
        });

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
}
