package ecosystem.entities.core.partition;

import core.Entity;
import core.SeriList;
import core.location.Bound;
import core.location.Location;
import core.seri.Seri;
import ecosystem.entities.core.SeriEntities;

import java.awt.*;

public class QuadPartitionedEntities implements Seri{
    public SeriList<PartitionQuad> mQuads;
    public double mQuadSize;
    public int mNumQuads;

    public QuadPartitionedEntities(int numQuads){
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

    public QuadPartitionedEntities render (Graphics2D render, int width, int height){
        render.setColor(new Color(192, 192, 192));
        for (PartitionQuad quad : mQuads) {
            if (0 == quad.getEntities().getClasses().size())
                continue;
            quad.getBound().render(render, width, height);
        }
        return this;
    }
}
