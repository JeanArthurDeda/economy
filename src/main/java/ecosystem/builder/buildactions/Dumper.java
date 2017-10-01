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
        QuadPartitionedEntities partioner = ecosystem.getPartitioner(clasa);

        LocationStack stack = new LocationStack(3);
        double texelSize = 0.5 * Math.sqrt (8.0) / Math.sqrt (mWidth * mWidth + mHeight * mHeight);

        for (int y = 0; y < mHeight; ++y)
            for (int x  = 0; x < mWidth; ++x){
                double locationX = ((double)x / (double)mWidth) * 2.0 - 1.0;
                double locationY = ((double)y / (double)mHeight) * 2.0 - 1.0;

                double min1 = Double.MAX_VALUE;
                Location loc1 = null;
                double min2 = Double.MAX_VALUE;
                Location loc2 = null;

                SeriList<PartitionQuad> quads = partioner.getQuads();
                int quadX = partioner.getQuadX(locationX);
                int quadY = partioner.getQuadY(locationY);
                SpiralIterator spiralIterator = new SpiralIterator(quadX, quadY, partioner.getNumQuads(), partioner.getNumQuads());

                for (int index = spiralIterator.get(); index != -1; index = spiralIterator.get()){
                    PartitionQuad quad = quads.get(index);
                    double distToQuad = quad.getBound().dist(locationX, locationY);
                    if (min2 < distToQuad)
                        continue;

                    SeriEntities entities = quad.getEntities();
                    for (int i = 0; i < entities.size(); i++) {
                        Entity entity = entities.get(i);
                        Location location = entity.getLocation();
                        double d = location.dist(locationX, locationY);
                        if (d < min1) {
                            if (min1 < min2){
                                min2 = min1;
                                loc2 = loc1;
                            }
                            min1 = d;
                            loc1 = entity.getLocation();
                        } else if (d < min2){
                            min2 = d;
                            loc2 = entity.getLocation();
                        }
                    }
                }

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
            render.fill(new Rectangle2D.Double(x - 1.0, y - 1.0, 2.0, 2.0));
        }
    }
}
