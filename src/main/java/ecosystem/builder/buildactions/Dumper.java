package ecosystem.builder.buildactions;

import core.Entity;
import core.location.Location;
import core.SeriList;
import core.location.LocationStack;
import core.seri.Seri;
import ecosystem.Ecosystem;
import ecosystem.entities.core.SeriEntities;
import ecosystem.entities.core.partition.PartitionQuad;
import ecosystem.entities.core.partition.QuadPartitionedEntities;
import ecosystem.entities.core.partition.SpiralIterator;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Dumper extends BuildAction {
    public enum Type {
        POINT,
        VORONOI
    }

    public static class Params implements Seri {
        public Class<? extends Entity> mClasa;
        public int mColor;
        public Type mType;

        public Params (Class<? extends Entity> clasa, Type type, int color){
            mClasa = clasa;
            mType = type;
            mColor = color;
        }

        public int getColor (){
            return mColor;
        }

        public Type getType (){
            return mType;
        }

        public Class<? extends Entity> getClasa() {
            return mClasa;
        }
    }

    public SeriList<Params> mParams = new SeriList<>();
    public String mUrl;
    public int mWidth;
    public int mHeight;

    public Dumper (String url, int width, int height){
        mUrl = url;
        mWidth = width;
        mHeight = height;
    }

    public Dumper dump (Params params){
        mParams.add(params);
        return this;
    }

    @Override
    public void execute(Ecosystem ecosystem) throws InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException, InstantiationException {
        System.out.println ("Executing " + mUrl);
        BufferedImage image = new BufferedImage(mWidth, mHeight, BufferedImage.TYPE_INT_RGB);

        for (Params params : mParams) {
            Class<? extends Entity> clasa = params.getClasa ();
            Type type = params.getType();
            int color = params.getColor();
            switch (type) {
                case POINT:
                    drawPoints(image, ecosystem, clasa, color);
                    break;
                case VORONOI:
                    drawVoronoi(image, ecosystem, clasa, color);
                    break;
            }
        }
        ImageIO.write(image, "jpg", new File (mUrl));
    }

    private void drawVoronoi(BufferedImage image, Ecosystem ecosystem, Class<? extends Entity> clasa, int color) {
        long time = System.currentTimeMillis();
        LocationStack stack = new LocationStack(3);
        double texelSize = 0.5 * Location.WORLD_DIAGONAL_SIZE / Math.sqrt (mWidth * mWidth + mHeight * mHeight);

        // ===================================
        // create the spiral iterator receiver
        // ===================================
        QuadPartitionedEntities partioner = ecosystem.getPartitioner(clasa);
        SeriList<PartitionQuad> quads = partioner.getQuads();
        class VoronoiMinGatherer extends SpiralIterator.Receiver{
            protected double mLocationX;
            protected double mLocationY;

            protected double mMin1 = Double.MAX_VALUE;
            protected Location mLoc1 = null;
            protected double mMin2 = Double.MAX_VALUE;
            protected Location mLoc2 = null;
            protected boolean mQuadProcessed;

            public void setLocation (double locationX, double locationY){
                mLocationX = locationX;
                mLocationY = locationY;
            }

            public Location getLoc1() {
                return mLoc1;
            }

            public Location getLoc2() {
                return mLoc2;
            }

            @Override
            public void onStart(int startX, int startY) {
                mMin1 = Double.MAX_VALUE;
                mLoc1 = null;
                mMin2 = Double.MAX_VALUE;
                mLoc2 = null;
                mQuadProcessed = false;
            }

            @Override
            public boolean onStartSquare(int x, int y, int startX, int startY) {
                if (!mQuadProcessed)
                    return false;
                mQuadProcessed = false;
                return true;
            }

            @Override
            public boolean onValidValue(int x, int y, int startX, int startY) {
                int index = partioner.getNumQuads() * y + x;
                PartitionQuad quad = quads.get(index);
                double distToQuad = quad.getBound().dist(mLocationX, mLocationY);
                if (mMin2 < distToQuad)
                    return true;
                mQuadProcessed = true;

                SeriEntities entities = quad.getEntities();
                for (Entity entity : entities) {
                    Location location = entity.getLocation();
                    double d = location.dist(mLocationX, mLocationY);
                    if (d < mMin1) {
                        if (mMin1 < mMin2){
                            mMin2 = mMin1;
                            mLoc2 = mLoc1;
                        }
                        mMin1 = d;
                        mLoc1 = entity.getLocation();
                    } else if (d < mMin2){
                        mMin2 = d;
                        mLoc2 = entity.getLocation();
                    }
                }
                return true;
            }
        }
        SpiralIterator spiralIterator = new SpiralIterator();
        VoronoiMinGatherer voronoiMinGatherer = new VoronoiMinGatherer();

        // =======
        // Voronoi
        // =======
        for (int y = 0; y < mHeight; ++y)
            for (int x  = 0; x < mWidth; ++x){
                double locationX = ((double)x / (double)mWidth) * 2.0 - 1.0;
                double locationY = ((double)y / (double)mHeight) * 2.0 - 1.0;
                int quadX = partioner.getQuadX(locationX);
                int quadY = partioner.getQuadY(locationY);


                voronoiMinGatherer.setLocation(locationX, locationY);
                spiralIterator.iterate(quadX, quadY, partioner.getNumQuads(), partioner.getNumQuads(), voronoiMinGatherer);

                Location loc1 = voronoiMinGatherer.getLoc1();
                Location loc2 = voronoiMinGatherer.getLoc2();

                // compute the middle point between the voronoi cells
                stack.reset();
                Location middle = loc1.add(loc2, stack).mul(0.5);

                // compute the direction of the voronoi cells
                Location dir = loc2.sub(loc1, stack).normalize();

                // compute the vector to point
                Location pointOnLine = stack.get().set(locationX, locationY).sub(loc1);

                // compute the point on line
                double dot = dir.dot(pointOnLine);
                pointOnLine.set(loc1).add(dir.mul(dot));

                double d = pointOnLine.dist(middle);
                if (d < texelSize)
                    image.setRGB(x, y, color);
            }
        time = System.currentTimeMillis() - time;
        System.out.println(String.format("DrawVoronoi %d", time));
    }

    protected void drawPoints(BufferedImage image, Ecosystem ecosystem, Class<? extends Entity> clasa, int color) {
        SeriList<Entity> entities = ecosystem.getEntities(clasa);
        Graphics2D render = image.createGraphics();
        render.setColor (new Color((color >> 16) & 0xff, (color >> 8) & 0xff, color & 0xff));
        render.setBackground(null);
        for (Entity entity : entities) {
            Location location = entity.getLocation();
            double x = (location.getX() * 0.5 + 0.5) * mWidth;
            double y = (location.getY() * 0.5 + 0.5) * mHeight;
            render.fill(new Rectangle2D.Double(x - 0.5, y - 0.5, 0.5, 0.5));
        }
    }
}
