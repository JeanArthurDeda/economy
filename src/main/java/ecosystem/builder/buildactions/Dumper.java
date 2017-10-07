package ecosystem.builder.buildactions;

import core.Entity;
import core.geometry.Hull;
import core.geometry.Location;
import core.seri.wrapers.SeriList;
import core.seri.Seri;
import ecosystem.Ecosystem;
import ecosystem.entities.core.partition.Partitioner;
import ecosystem.entities.valuable.sourced.Land;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Dumper extends BuildAction {
    public static class Params implements Seri {
        public Class<? extends Entity> mClasa;
        public Integer mColorPoints;
        public Integer mColorHulls;
        public Integer mColorNames;
        public Integer mColorQuads;
        public Integer mColorEmptyQuads;
        public Integer mColorQuadIndexes;
        public int mFontSize;

        public Params (Class<? extends Entity> clasa, Integer points, Integer hulls, Integer names, Integer quads, Integer emptyQuads, Integer quadIndexes, int fontSize){
            mClasa = clasa;
            mColorPoints = points;
            mColorHulls = hulls;
            mColorNames = names;
            mColorQuads = quads;
            mColorEmptyQuads = emptyQuads;
            mColorQuadIndexes = quadIndexes;
            mFontSize = fontSize;
        }

        public Integer getColorEmptyQuads() {
            return mColorEmptyQuads;
        }

        public Integer getColorHulls() {
            return mColorHulls;
        }

        public Integer getColorNames() {
            return mColorNames;
        }

        public Integer getColorPoints() {
            return mColorPoints;
        }

        public Integer getColorQuads() {
            return mColorQuads;
        }

        public Integer getColorQuadIndexes() {
            return mColorQuadIndexes;
        }

        public int getFontSize() {
            return mFontSize;
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


        for (Params params : mParams) {
            render.setFont( new Font("SansSerif", Font.PLAIN, params.getFontSize()));

            Class<? extends Entity> clasa = params.getClasa ();
            SeriList<Entity> entities = ecosystem.getEntities(clasa);

            // draw hulls
            if (clasa == Land.class && params.getColorHulls() != null) {
                drawHulls(render, entities, params.getColorHulls());
            }

            // draw points
            if (params.getColorPoints() != null)
                drawPoints(render, entities, params.getColorPoints());

            // draw partitioner
            Partitioner partioner = ecosystem.getPartitioner(clasa);
            if (partioner != null)
                partioner.render( render, mWidth, mHeight, params.getColorQuads(), params.getColorEmptyQuads(), params.getColorQuadIndexes());

            // draw names
            if (params.getColorNames() != null)
                drawNames(render, entities, params.getColorNames(), 4, params.getFontSize() / 2);

        }
        ImageIO.write(image, "jpg", new File (mUrl));
    }

    protected void drawPoints(Graphics2D render, SeriList<Entity> entities, Integer color) {
        render.setColor (new Color(color, true));
        for (Entity entity : entities) {
            Location location = entity.getLocation();
            double x = (location.getX() * 0.5 + 0.5) * mWidth;
            double y = (location.getY() * 0.5 + 0.5) * mHeight;
            render.fill(new Rectangle2D.Double(x - 1, y - 1, 2, 2));
        }
    }

    protected void drawHulls(Graphics2D render, SeriList<Entity> entities, Integer color) {
        render.setColor (new Color(color, true));
        for (Entity entity : entities) {
            Land land = (Land)entity;
            Hull hull = land.getHull();
            hull.render(render, mWidth, mHeight, 0);
        }
    }

    protected void drawNames (Graphics2D render, SeriList<Entity> entities, Integer color, int offsetX, int offsetY){
        render.setColor (new Color(color, true));
        for (Entity entity : entities) {
            Location location = entity.getLocation();
            double x = (location.getX() * 0.5 + 0.5) * mWidth + offsetX;
            double y = (location.getY() * 0.5 + 0.5) * mHeight + offsetY;
            render.drawString(entity.getName (), (float)x, (float)y);

        }
    }

    @Override
    public String toString() {
        return super.toString() + " " + mUrl + " " + mWidth + "x" + mHeight;
    }
}
