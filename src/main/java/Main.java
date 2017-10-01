import ecosystem.builder.buildactions.spawner.postspawnactions.DensityMapLocationDistribution;
import ecosystem.Ecosystem;
import ecosystem.builder.EcosystemBuilder;
import ecosystem.builder.buildactions.Dumper;
import ecosystem.builder.buildactions.spawner.SpawnAction;
import ecosystem.builder.buildactions.spawner.SpawnParams;
import ecosystem.entities.valuable.sourced.Land;


public class Main {

    public static void main (String[] args){
        long time = System.currentTimeMillis();
        try {
            EcosystemBuilder builder;
            builder = new EcosystemBuilder(10)
                    .execute(new SpawnAction()
                            .spawnClass(Land.class, new SpawnParams(10000)
                                .executePostSpawn(new DensityMapLocationDistribution("c:\\dev\\economy\\assets\\land.jpg"))
                            )
                    )
                    .execute(new Dumper("c:\\dev\\economy\\debugTree\\ecosystem.jpg", 2048, 2048)
                        .dump(new Dumper.Params(Land.class, Dumper.Type.VORONOI, 0xff0000))
                        .dump(new Dumper.Params(Land.class, Dumper.Type.POINT, 0xffffff))
                    );

            Ecosystem ecosystem = builder.build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        time = System.currentTimeMillis() - time;
        System.out.print(time);
    }
}
