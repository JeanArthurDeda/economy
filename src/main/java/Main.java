import core.TokenStream;
import core.seri.Seri;
import ecosystem.builder.buildactions.spawner.postspawnactions.DensityMapLocationDistribution;
import ecosystem.Ecosystem;
import ecosystem.builder.EcosystemBuilder;
import ecosystem.builder.buildactions.dumper.Dumper;
import ecosystem.builder.buildactions.spawner.SpawnAction;
import ecosystem.builder.buildactions.spawner.SpawnParams;
import ecosystem.entities.valuable.sourced.Land;


public class Main {

    public static void main (String[] args){

        try {
            EcosystemBuilder builder;
            builder = new EcosystemBuilder()
                    .execute(new SpawnAction()
                            .spawnClass(Land.class, new SpawnParams(100)
                                .executePostSpawn(new DensityMapLocationDistribution("c:\\dev\\economy\\assets\\land.jpg"))
                            )
                    )
                    .execute(new Dumper("c:\\dev\\economy\\debugTree\\ecosystem.jpg", 2048, 2048)
                        .dump(Land.class, 0xffffff));

            Ecosystem ecosystem = builder.build();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
