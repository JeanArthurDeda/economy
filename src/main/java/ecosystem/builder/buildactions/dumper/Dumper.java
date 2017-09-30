package ecosystem.builder.buildactions.dumper;

import core.Entity;
import core.Location;
import core.SeriList;
import core.SeriMap;
import ecosystem.Ecosystem;
import ecosystem.builder.buildactions.BuildAction;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Dumper extends BuildAction {
    public SeriMap<Class<? extends Entity>, Integer> mClassColors = new SeriMap<>();
    public SeriList<Class<? extends Entity>> mOrderedKeys = new SeriList<>();
    public String mUrl;
    public int mWidth;
    public int mHeight;

    public Dumper (String url, int width, int height){
        mUrl = url;
        mWidth = width;
        mHeight = height;
    }

    public Dumper dump (Class<? extends Entity> clasa, int color){
        mClassColors.put(clasa, color);
        mOrderedKeys.add (clasa);
        return this;
    }

    @Override
    public void execute(Ecosystem ecosystem) throws InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException, InstantiationException {
        BufferedImage image = new BufferedImage(mWidth, mHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D render = image.createGraphics();

        for (Class<? extends Entity> key : mOrderedKeys) {
            int color = mClassColors.get(key);
            render.setColor (new Color((color >> 16) & 0xff, (color >> 8) & 0xff, color & 0xff));
            SeriList<Entity> entities = ecosystem.getEntities(key);
            for (Entity entity : entities) {
                Location location = entity.getLocation();
                double x = (location.getX() * 0.5 + 0.5) * mWidth;
                double y = (location.getY() * 0.5 + 0.5) * mHeight;
                render.fill(new Rectangle2D.Double(x - 1.0, y - 1.0, 2.0, 2.0));
            }
        }

        ImageIO.write(image, "jpg", new File (mUrl));
    }
}
