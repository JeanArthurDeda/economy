package ecosystem.builder.buildactions;

import core.Entity;
import core.geometry.Hull;
import core.geometry.Location;
import core.seri.wrapers.SeriList;
import core.seri.Seri;
import ecosystem.Ecosystem;
import ecosystem.entities.core.partition.Partitioner;
import ecosystem.entities.valuable.sourced.Land;
import javafx.scene.shape.Circle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        public String mGetSizeMethodName;
        public double mSizeScale;
        public double mSizeBias;

        public Params (Class<? extends Entity> clasa, Integer pointsColor){
            mClasa = clasa;
            mColorPoints = pointsColor;
        }

        public Params hulls (Integer hullsColor){
            mColorHulls = hullsColor;
            return this;
        }

        public Params fontSize (int size){
            mFontSize = size;
            return this;
        }

        public Params names (Integer names){
            mColorNames = names;
            return this;
        }

        public Params partitioner (Integer quadsColor, Integer emptyQuadsColor, Integer quadIndexesColor){
            mColorQuads = quadsColor;
            mColorEmptyQuads = emptyQuadsColor;
            mColorQuadIndexes = quadIndexesColor;
            return this;
        }

        public Params pointSize (String getSizeMethodName, double scale, double bias){
            mGetSizeMethodName = getSizeMethodName;
            mSizeScale = scale;
            mSizeBias = bias;
            return this;
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

        public String getSizeMethodName() {
            return mGetSizeMethodName;
        }

        public double getSizeScale (){
            return mSizeScale;
        }

        public double getSizeBias(){
            return mSizeBias;
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
            if (params.getColorPoints() != null) {
                Method getMethod = null;
                String getMethodName = params.getSizeMethodName();
                if (getMethodName != null)
                    getMethod = clasa.getMethod(getMethodName);
                drawPoints(render, entities, params.getColorPoints(), getMethod, params.getSizeScale(), params.getSizeBias());
            }

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

    protected void drawPoints(Graphics2D render, SeriList<Entity> entities, Integer color, Method getSizeMethod, double sizeScale, double sizeBias) throws InvocationTargetException, IllegalAccessException {
        render.setColor (new Color(color, true));
        for (Entity entity : entities) {
            Location location = entity.getLocation();
            double size = 3.0;
            if (getSizeMethod != null){
                size = (double) getSizeMethod.invoke(entity) * sizeScale + sizeBias;
            }
            double x = (location.getX() * 0.5 + 0.5) * mWidth;
            double y = (location.getY() * 0.5 + 0.5) * mHeight;
            render.fill(new Ellipse2D.Double(x, y, size, size));
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
