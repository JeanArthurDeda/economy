package ecosystem.builder.buildactions;

import core.Entity;
import core.location.Hull;
import core.location.Location;
import core.SeriList;
import core.location.LocationStack;
import core.seri.Seri;
import ecosystem.Ecosystem;
import ecosystem.entities.core.SeriEntities;
import ecosystem.entities.core.partition.PartitionQuad;
import ecosystem.entities.core.partition.QuadPartitionedEntities;
import ecosystem.entities.core.partition.SpiralIterator;
import ecosystem.entities.valuable.sourced.Land;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Dumper extends BuildAction {
    public static class Params implements Seri {
        public Class<? extends Entity> mClasa;
        public int mColor;

        public Params (Class<? extends Entity> clasa, int color){
            mClasa = clasa;
            mColor = color;
        }

        public int getColor (){
            return mColor;
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
        BufferedImage image = new BufferedImage(mWidth, mHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D render = image.createGraphics();

        QuadPartitionedEntities partioner = ecosystem.getPartitioner(Land.class);
        partioner.render( render, mWidth, mHeight );

        for (Params params : mParams) {
            Class<? extends Entity> clasa = params.getClasa ();
            SeriList<Entity> entities = ecosystem.getEntities(clasa);
            int color = params.getColor();
            if (clasa == Land.class) {
                drawHulls(render, entities, color);
            }
            else
                drawPoints(render, entities, color);
        }
        ImageIO.write(image, "jpg", new File (mUrl));
    }

    protected void drawPoints(Graphics2D render, SeriList<Entity> entities, int color) {
        render.setColor (new Color((color >> 16) & 0xff, (color >> 8) & 0xff, color & 0xff));
        render.setBackground(null);
        for (Entity entity : entities) {
            Location location = entity.getLocation();
            double x = (location.getX() * 0.5 + 0.5) * mWidth;
            double y = (location.getY() * 0.5 + 0.5) * mHeight;
            render.fill(new Rectangle2D.Double(x - 0.5, y - 0.5, 0.5, 0.5));
        }
    }

    protected void drawHulls(Graphics2D render, SeriList<Entity> entities, int color) {
        render.setColor (new Color((color >> 16) & 0xff, (color >> 8) & 0xff, color & 0xff));
        render.setBackground(null);
        for (Entity entity : entities) {
            Land land = (Land)entity;
            Hull hull = land.getHull();
            hull.render(render, mWidth, mHeight, 0);
        }
    }

    @Override
    public String toString() {
        return super.toString() + " " + mUrl + " " + mWidth + "x" + mHeight;
    }
}
