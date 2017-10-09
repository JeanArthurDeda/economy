import ecosystem.Ecosystem;
import ecosystem.builder.EcosystemBuilder;
import ecosystem.builder.buildactions.Dumper;
import ecosystem.builder.buildactions.GenerateLandHulls;
import ecosystem.builder.buildactions.spawner.SpawnAction;
import ecosystem.builder.buildactions.spawner.SpawnParams;
import ecosystem.builder.buildactions.spawner.postspawnactions.DensityMapLocationDistribution;
import ecosystem.builder.buildactions.spawner.postspawnactions.GraphScalarDistribution;
import ecosystem.entities.valuable.sourced.Immobile;
import ecosystem.entities.valuable.sourced.Land;


public class MainBuilder {
    public static void main (String[] args){
        try {
            DensityMapLocationDistribution distributeLocationsOnMap = new DensityMapLocationDistribution("c:\\dev\\economy\\assets\\land.jpg");

            EcosystemBuilder builder = new EcosystemBuilder(10);
            builder
                .execute(new SpawnAction()
                    .spawnClass(Land.class, new SpawnParams(100)
                            .executePostSpawn(distributeLocationsOnMap)
                    )
                    .spawnClass(Immobile.class, new SpawnParams(1000)
                            .executePostSpawn(distributeLocationsOnMap)
                            .executePostSpawn(new GraphScalarDistribution("setNumUnits")
                                    .graphPoint(0.0, 30)
                                    .graphPoint(0.9, 140)
                                    .graphPoint(1.0, 500)
                            )
                    )
//                    .spawnClass(HouseHold.class, new SpawnParams(4000)
//                            .executePostSpawn(distributeLocationsOnMap)
//                    )
                )
                .execute(new GenerateLandHulls())
                .execute(new Dumper("c:\\dev\\economy\\debugTree\\ecosystem.jpg", 2048, 2048)
                    .dump(new Dumper.Params(Immobile.class, 0xffff00ff).pointSize("getNumUnits", 2.0 / 30.0, 0.0))
                    .dump(new Dumper.Params(Land.class, 0xffffffff).hulls(0xffffffff))
//                    .dump(new Dumper.Params(HouseHold.class, 0xff00ff00, null, null, null, null, null, 10))
                );

            Ecosystem ecosystem = builder.build();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
