package core.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class IntensityMap {
    public byte [] mData;
    public int mWidth;
    public int mHeight;

    public void load (File file) throws IOException {
        init(ImageIO.read(file));
    }

    public void load (URL url) throws IOException {
        init(ImageIO.read(url));
    }

    public int getWidth (){
        return mWidth;
    }

    public int getHeight (){
        return mHeight;
    }

    public byte[] getData (){
        return mData;
    }

    public int get (int x, int y){
        return (int)mData[y * mWidth + x] - Byte.MIN_VALUE;
    }

    protected void init (BufferedImage image){
        mWidth = image.getWidth();
        mHeight = image.getHeight();
        mData = new byte[mWidth * mHeight];
        for (int y = 0; y < mHeight; y ++)
            for (int x = 0; x < mWidth; x ++){
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = (rgb) & 0xff;
                int a = (r + g + b) / 3 + Byte.MIN_VALUE;
                mData[y * mWidth + x] = (byte)a;
            }
    }
}
