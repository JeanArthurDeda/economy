package all_deprecated;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Deprecated
public class IntensityMapTreeDeprecated extends IntensityMapDeprecated {
    protected final String mDebugDumpPath;
    protected final int mQuadSize;
    protected final int mQuadSizeLog2;
    protected final int mQuadSizeMask;
    protected int mNumLevels;
    protected Leaf mRoot;

    public class Leaf{
        public float min;
        public float max;
        public Leaf[] leaves;

        public Leaf() {
            min = Float.MAX_VALUE;
            max = -Float.MAX_VALUE;
        }
    }

    public class Index {
        public int x, y;
        public int index;
    }

    public IntensityMapTreeDeprecated(int quadSizeLog2, String debugDumpPath) {
        mQuadSizeLog2 = quadSizeLog2;
        mQuadSize = 1 << quadSizeLog2;
        mQuadSizeMask = (1 << quadSizeLog2) - 1;
        mDebugDumpPath = debugDumpPath;
    }

    @Override
    public void load (File file) throws IOException {
        super.load(file);
        createLeaves();
    }

    @Override
    public void load (URL url) throws IOException {
        super.load(url);
        createLeaves();
    }

    public Leaf getRoot() {
        return mRoot;
    }

    public int getRootSize (){
        return 1 << (mQuadSizeLog2 * mNumLevels);
    }

    public int getNumLevels (){
        return mNumLevels;
    }

    public int getQuadSize (){
        return mQuadSize;
    }

    public int getQuadSizeLog2 () {
        return mQuadSizeLog2;
    }

    public int getQuadSizeMask () {
        return mQuadSizeMask;
    }

    // =====================
    // Helpers for traversal
    // =====================
    public int computeLeavesIndexesContaining(Leaf[] leaves, float value, Index[] outIndexes){
        int index = 0;
        int count = 0;
        for (int y = 0; y < mQuadSize; ++y)
            for (int x = 0; x < mQuadSize; ++x, ++index){
                Leaf l = leaves[index];
                if (null == l)
                    continue;
                if (value > l.max)
                    continue;
                outIndexes[count].x = x;
                outIndexes[count].y = y;
                outIndexes[count].index = index;
                count ++;
            }
        return count;
    }

    public int computeTexelsIndexesContaining(int texelX, int texelY, float value, Index[] outIndexes){
        int width = getWidth();
        int height = getHeight();
        int count = 0;
        for (int y = texelY; y < texelY + mQuadSize; ++y) {
            if (texelY >= height)
                continue;
            for (int x = texelX; x < texelX + mQuadSize; ++x) {
                if (texelX >= width)
                    continue;
                float max = get(x, y);
                if (value > max)
                    continue;
                outIndexes[count].x = x;
                outIndexes[count].y = y;
                outIndexes[count].index = x * width + y;
                count ++;
            }
        }
        return count;
    }

    public Index[] createTraversalIndexes() {
        Index[] indexes = new Index[1 << (mQuadSizeLog2 * 2)];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = new Index();
        }
        return indexes;
    }

    // ========
    // Internal
    // ========

    protected void createLeaves() {
        // generate the texels leaves
        int imageWidth = getWidth();
        int imageHeight = getHeight();
        int width = (imageWidth + mQuadSizeMask) >> mQuadSizeLog2;
        int height = (imageHeight + mQuadSizeMask) >> mQuadSizeLog2;
        Leaf[] leaves = createLeaves(width * height);

        for (int y = 0; y < height; y ++)
            for (int x = 0; x < width; x++){
                Leaf leaf = leaves[y * width + x];
                int leafImageX = x << mQuadSizeLog2;
                int leafImageY = y << mQuadSizeLog2;

                for (int i = 0; i < mQuadSize; i ++) {
                    int sampleY = leafImageY + i;
                    if (sampleY >= imageHeight)
                        continue;
                    for (int j = 0; j < mQuadSize; j++) {
                        int sampleX = leafImageX + j;
                        if (sampleX >= imageWidth)
                            continue;

                        float v = get(leafImageX + j, leafImageY + i);
                        leaf.max = Math.max(v, leaf.max);
                        leaf.min = Math.min(v, leaf.min);
                    }
                }
            }
        mNumLevels = 1;
        if (mDebugDumpPath != null)
            dump (leaves, mNumLevels, width, height, new File (mDebugDumpPath, String.format("min%02d.jpg", mNumLevels)), new File (mDebugDumpPath, String.format("max%02d.jpg", mNumLevels)));

        Leaf[] keepLeaves = leaves;
        leaves = createLeaves(keepLeaves, width, height);
        while (leaves != null) {
            width = (width + mQuadSizeMask) >> mQuadSizeLog2;
            height = (height + mQuadSizeMask) >> mQuadSizeLog2;
            mNumLevels ++;
            keepLeaves = leaves;
            if (mDebugDumpPath != null)
                dump (leaves, mNumLevels, width, height, new File (mDebugDumpPath, String.format("min%02d.jpg", mNumLevels)), new File (mDebugDumpPath, String.format("max%02d.jpg", mNumLevels)));
            leaves = createLeaves(keepLeaves, width, height);
        }

        mRoot = keepLeaves[0];
    }

    protected Leaf[] createLeaves(Leaf[] srcLeaves, int srcWidth, int srcHeight){
        if (1 == srcWidth && 1 == srcHeight)
            return null;

        int width = (srcWidth + mQuadSizeMask) >> mQuadSizeLog2;
        int height = (srcHeight + mQuadSizeMask) >> mQuadSizeLog2;

        Leaf[] leaves = createLeaves(width * height);

        for (int y = 0; y < height; y ++)
            for (int x = 0; x < width; x++){
                Leaf leaf = leaves[y * width + x];
                int srcX = x << mQuadSizeLog2;
                int srcY = y << mQuadSizeLog2;

                leaf.leaves = new Leaf[1 << (mQuadSizeLog2 * 2)];
                for (int i = 0; i < mQuadSize; i ++) {
                    int sampleY = srcY + i;
                    if (sampleY >= srcHeight)
                        continue;
                    for (int j = 0; j < mQuadSize; j++) {
                        int sampleX = srcX + j;
                        if (sampleX >= srcWidth)
                            continue;

                        Leaf src = srcLeaves[sampleY * srcWidth + sampleX];
                        leaf.leaves[(i << mQuadSizeLog2) + j] = src;
                        leaf.max = Math.max(src.max, leaf.max);
                        leaf.min = Math.min(src.min, leaf.min);
                    }
                }
            }

        return leaves;
    }

    protected Leaf[] createLeaves (int numLeaves){
        Leaf[] leaves = new Leaf[numLeaves];
        for (int i = 0; i < leaves.length; i++) {
            leaves[i] = new Leaf();
        }
        return leaves;
    }

    protected void dump (Leaf[] srcLeaves, int level, int srcWidth, int srcHeight, File minFile, File maxFile){
        int width = getWidth();
        int height = getHeight();
        int quadSize = 1 << (level * mQuadSizeLog2);
        BufferedImage maxImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D maxRender = maxImage.createGraphics();
        BufferedImage minImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D minRender = minImage.createGraphics();

        for (int y = 0; y < srcHeight; ++y)
            for (int x = 0; x < srcWidth; ++x){
                Leaf leaf = srcLeaves[y * srcWidth + x];
                if (null == leaf)
                    continue;
                int maxColor = (int)(leaf.max * 255.0f);
                maxColor = (maxColor << 16) | (maxColor << 8) | (maxColor);
                int minColor = (int)(leaf.min * 255.0f);
                minColor = (minColor << 16) | (minColor << 8) | (minColor);
                Rectangle2D.Float rectangle = new Rectangle2D.Float(x * quadSize, y * quadSize, quadSize, quadSize);
                maxRender.setPaint(new Color(maxColor));
                maxRender.fill (rectangle);
                minRender.setPaint(new Color(minColor));
                minRender.fill (rectangle);
                if (quadSize > 2){
                    maxRender.setPaint(new Color(0));
                    maxRender.draw (rectangle);
                    minRender.setPaint(new Color(0xffffff));
                    minRender.draw (rectangle);
                }
            }
        try {
            ImageIO.write (maxImage, "jpg", maxFile);
            ImageIO.write (minImage, "jpg", minFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
