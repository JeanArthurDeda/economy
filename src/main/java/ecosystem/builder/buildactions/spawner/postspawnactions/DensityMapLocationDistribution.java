package ecosystem.builder.buildactions.spawner.postspawnactions;

import core.Entity;
import core.location.Location;
import core.SeriList;
import core.image.IntensityMap;
import ecosystem.builder.buildactions.spawner.PostSpawnAction;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

public class DensityMapLocationDistribution extends PostSpawnAction {
    public String mUrl;
    protected transient int mWidth;
    protected transient int mHeight;
    protected transient double mOOWidth;
    protected transient double mOOHeight;
    protected transient SeriList<Integer>[] mIndexes = new SeriList[256];

    public DensityMapLocationDistribution(String url){
        mUrl = url.toLowerCase();

    }

    @Override
    public void cache() throws Exception {
        super.cache();

        System.out.println ("Caching " + mUrl);

        IntensityMap map = new IntensityMap();
        if (mUrl.contains("http"))
            map.load(new URL(mUrl));
        else
            map.load(new File(mUrl));
        mWidth = map.getWidth();
        mHeight = map.getHeight();
        mOOWidth = 1.0 / (double)mWidth;
        mOOHeight = 1.0 / (double)mHeight;

        byte[] data = map.getData();
        for (int i = 0; i < data.length; i++) {
            byte t = data[i];
            int v = t - Byte.MIN_VALUE;

            if (null == mIndexes[v])
                mIndexes[v] = new SeriList<>();

            mIndexes[v].add(i);
        }
    }

    @Override
    public void execute(SeriList<Entity> entities) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        System.out.println ("Executing " + mUrl);

        for (Entity entity : entities) {
            Location location = null;
            while (null == location) {
                int chance = (int) (Math.random() * 254.0) + 1;
                location = getLocation(chance);
            }
            entity.setLocation(location);
        }
    }

    protected Location getLocation (int chance){
        // =====
        // count
        // =====
        int count = 0;
        for (int i = chance; i <= 255; ++i)
            if (mIndexes[i] != null)
                count += mIndexes[i].size();
        if (0 == count)
            return null;

        // ======
        // choose
        // ======
        int chosen = (int)(Math.random() * (double)count);
        int index = 0;
        count = 0;
        for (int i = chance; i <= 255; ++i)
            if (mIndexes[i] != null) {
                int size = mIndexes[i].size();
                if (count + size > chosen){
                    chosen -= count;
                    index = mIndexes[i].get(chosen);
                    break;
                }
                count += size;
            }


        int y = index % mWidth;
        int x = index / mHeight;
        double ratioX = (double) y * mOOWidth;
        double ratioY = (double) x * mOOHeight;
        return new Location().setWorldRatio(ratioX, ratioY).jitter(mOOWidth, mOOHeight);
    }

}
