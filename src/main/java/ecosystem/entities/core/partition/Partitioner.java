package ecosystem.entities.core.partition;

import core.Entity;
import core.seri.wrapers.SeriList;
import core.geometry.Bound;
import core.geometry.Location;
import core.seri.Seri;
import ecosystem.entities.core.SeriEntities;

import java.awt.*;

public class Partitioner implements Seri{
    public SeriList<PartitionQuad> mQuads;
    public double mQuadSize;
    public int mNumQuads;

    public Partitioner(int numQuads){
        mNumQuads = numQuads;
        mQuadSize = Location.WORLD_EDGE_SIZE / (double)numQuads;
        mQuads = new SeriList<>();
        for (int y = 0; y < mNumQuads; ++y)
            for (int x = 0; x < mNumQuads; ++x){
                Location min = new Location((double)x * mQuadSize - 1.0 , (double)y * mQuadSize - 1.0);
                Location max = new Location(min).add(mQuadSize, mQuadSize);
                mQuads.add (new PartitionQuad(new Bound(min, max)));
            }
    }

    public void add (Entity entity){
        int index = getQuadIndex(entity.getLocation());
        PartitionQuad quad = mQuads.get(index);
        SeriEntities entities = quad.getEntities();
        entities.add(entity);
    }

    public int getNumQuads() {
        return mNumQuads;
    }

    public double getQuadSize(){
        return mQuadSize;
    }

    public int getQuadX(Location location) {
        return (int)((location.getX () + 1.0) / mQuadSize);
    }

    public int getQuadX(double locationX) {
        return (int)((locationX + 1.0) / mQuadSize);
    }

    public int getQuadY(Location location) {
        return (int)((location.getY () + 1.0) / mQuadSize);
    }

    public int getQuadY(double locationY) {
        return (int)((locationY + 1.0) / mQuadSize);
    }

    public int getQuadIndex(Location location){
        int quadX = getQuadX(location);
        int quadY = getQuadY(location);
        int index = quadY * mNumQuads + quadX;
        return index;
    }

    public int getQuadIndex(double locationX, double locationY){
        int quadX = getQuadX(locationX);
        int quadY = getQuadX(locationY);
        int index = quadY * mNumQuads + quadX;
        return index;
    }

    public SeriList<PartitionQuad> getQuads () {
        return mQuads;
    }

    public Partitioner render (Graphics2D render, int width, int height, Integer quadColor, Integer emptyQuadColor, Integer quadIndexesColor){

        // draw full quads
        if (quadColor != null || emptyQuadColor != null) {
            Color quadCol = quadColor != null ? new Color(quadColor, true) : null;
            Color emptyQuadCol = emptyQuadColor != null ? new Color(emptyQuadColor, true) : null;
            for (PartitionQuad quad : mQuads) {
                Color color = quadCol;
                if (0 == quad.getEntities().getClasses().size())
                    color = emptyQuadCol;
                if (null == color)
                    continue;
                render.setColor (color);
                Bound bound = quad.getBound();
                bound.render(render, width, height);
            }
        }

        // draw quad indexes
        if (quadIndexesColor != null) {
            render.setColor(new Color(quadIndexesColor, true));
            for (int i = 0; i < mNumQuads; ++i)
                for (int j = 0; j < mNumQuads; ++j) {
                    PartitionQuad quad = mQuads.get(i * mNumQuads + j);
                    if (0 == quad.getEntities().getClasses().size() && null == emptyQuadColor)
                        continue;
                    Bound bound = quad.getBound();
                    double x = 0.5 * (bound.getMax().getX() + bound.getMin().getX());
                    double y = 0.5 * (bound.getMax().getY() + bound.getMin().getY());
                    x = width * (x * 0.5 + 0.5);
                    y = width * (y * 0.5 + 0.5);
                    render.drawString(j + "," + i, (float) x, (float) y);
                }
        }

        return this;
    }
}
