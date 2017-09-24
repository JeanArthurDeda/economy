import core.distribution.location.IndexLocationRandom;
import core.distribution.scalar.index.IndexScalarGraph;
import ecosystem.entities.ecosystem.Ecosystem;
import ecosystem.entities.ecosystem.builder.EcosystemBuilder;
import ecosystem.entities.ecosystem.builder.spawn.SpawnParam;
import ecosystem.entities.ecosystem.builder.spawn.Spawner;
import ecosystem.entities.valuable.sourced.NaturalResource;
import ecosystem.entities.valuable.sourced.Food;
import ecosystem.entities.valuable.sourced.Land;

public class Main {
    public static void main (String[] args){
        try {
            EcosystemBuilder builder = new EcosystemBuilder()
                    .setSpawner(new Spawner()
                            .spawn(Land.class, new SpawnParam(1)
                                .setLocation(new IndexLocationRandom()))
                            .spawn(NaturalResource.class, new SpawnParam(4)
                                .setLocation(new IndexLocationRandom())
                                .setScalar("mNumUnits", 4.0, new IndexScalarGraph()
                                    .add(0.9, 0.1)
                                    .add(1.0, 1.0)))
                            .spawn(Food.class, new SpawnParam(4)
                                .setLocation(new IndexLocationRandom())
                                .setScalar("mNumUnits", 4.0, new IndexScalarGraph()
                                    .add(0.9, 0.1)
                                    .add(1.0, 1.0)))
                    );

            Ecosystem ecosystem = builder.build();
            StringBuilder stream = new StringBuilder();
            builder.serialize("", ecosystem.getEntitiesPool(), stream);
            System.out.println (stream.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
}
