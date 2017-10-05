import ecosystem.Ecosystem;
import ecosystem.builder.EcosystemBuilder;
import ecosystem.builder.buildactions.Dumper;
import ecosystem.builder.buildactions.GenerateLandHulls;
import ecosystem.builder.buildactions.spawner.SpawnAction;
import ecosystem.builder.buildactions.spawner.SpawnParams;
import ecosystem.builder.buildactions.spawner.postspawnactions.DensityMapLocationDistribution;
import ecosystem.entities.valuable.sourced.Land;


public class MainBuilder {
    public static void main (String[] args){
        try {
            EcosystemBuilder builder = new EcosystemBuilder(10);
            builder
                .execute(new SpawnAction()
                    .spawnClass(Land.class, new SpawnParams(1000)
                            .executePostSpawn(new DensityMapLocationDistribution("c:\\dev\\economy\\assets\\land.jpg"))))
                    .execute(new GenerateLandHulls())
                    .execute(new Dumper("c:\\dev\\economy\\debugTree\\ecosystem.jpg", 2048, 2048)
                    .dump(new Dumper.Params(Land.class, 0xffffff, 0xffffff, null, 0xffffff, null, null, 10)));

            Ecosystem ecosystem = builder.build();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
