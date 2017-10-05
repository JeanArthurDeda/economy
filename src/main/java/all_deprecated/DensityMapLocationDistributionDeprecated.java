package all_deprecated;

import core.Entity;
import core.MathExt;
import core.geometry.Location;
import core.seri.wrapers.SeriList;
import ecosystem.builder.buildactions.spawner.PostSpawnAction;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

@Deprecated
public class DensityMapLocationDistributionDeprecated extends PostSpawnAction {
    public static int mNumRands = 0;
    public String mUrl;
    public String mDebugDumpPath;
    public int mQuadSizeLog2;

    protected transient IntensityMapTreeDeprecated mIntensityMapTree;
    protected transient IntensityMapTreeDeprecated.Index[] mIndexes;

    public DensityMapLocationDistributionDeprecated(String url, int quadSizeLog2){
        mUrl = url.toLowerCase();
        mQuadSizeLog2 = quadSizeLog2;
    }

    public DensityMapLocationDistributionDeprecated setDebugDumpPath (String debugDumpPath){
        mDebugDumpPath = debugDumpPath;
        return this;
    }

    @Override
    public void cache() throws Exception {
        super.cache();

        mIntensityMapTree = new IntensityMapTreeDeprecated(mQuadSizeLog2, mDebugDumpPath);
        if (mUrl.contains("http"))
            mIntensityMapTree.load(new URL(mUrl));
        else
            mIntensityMapTree.load(new File(mUrl));
            mIntensityMapTree.load(new File(mUrl));

        mIndexes = mIntensityMapTree.createTraversalIndexes();
    }

    @Override
    public void execute(SeriList<Entity> entities) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        IntensityMapTreeDeprecated.Leaf root = mIntensityMapTree.getRoot();
        int leafSize = mIntensityMapTree.getRootSize();

        for (Entity entity : entities) {
            float chance = (float)MathExt.random();
            mNumRands ++;
            while (chance < root.min || chance > root.max){
                chance = (float)MathExt.random();
                mNumRands ++;
            }

            entity.setLocation(getLocation(0, 0, leafSize >> mIntensityMapTree.getQuadSizeLog2(), root.leaves, chance));
        }
    }

    protected Location getLocation (int x, int y, int leafSize, IntensityMapTreeDeprecated.Leaf[] leaves, float chance){
        int numIndexes = mIntensityMapTree.computeLeavesIndexesContaining(leaves, 0.5f, mIndexes);

        // decide which path to take
        int chosen = (int)(MathExt.random() * (double)numIndexes);
        mNumRands ++;
        IntensityMapTreeDeprecated.Index index = mIndexes[chosen];
        IntensityMapTreeDeprecated.Leaf leaf = leaves[index.index];

        // is it a node or a leaf
        if (null == leaf.leaves){
            numIndexes = mIntensityMapTree.computeTexelsIndexesContaining(x + index.x * leafSize, y + index.y * leafSize, chance, mIndexes);
            chosen = (int)(MathExt.random() * (double)numIndexes);
            mNumRands ++;
            index = mIndexes[chosen];
            double ooW = 1.0 / (double)mIntensityMapTree.getWidth();
            double ooH = 1.0 / (double)mIntensityMapTree.getHeight();
            double ratioX = (double) index.x * ooW;
            double ratioY = (double) index.y * ooH;
            return new Location().setWorldRatio(ratioX, ratioY).jitter(ooW, ooH);
        } else {
            return getLocation(x + index.x * leafSize, y + index.y * leafSize, leafSize >> 1, leaf.leaves, chance);
        }
    }
}
