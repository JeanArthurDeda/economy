package all_deprecated;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Deprecated
public class IntensityMapDeprecated {
    public float [] mData;
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

    public float[] getData (){
        return mData;
    }

    public float get (int x, int y){
        return mData[y * mWidth + x];
    }

    protected void init (BufferedImage image){
        mWidth = image.getWidth();
        mHeight = image.getHeight();
        mData = new float[mWidth * mHeight];
        for (int y = 0; y < mHeight; y ++)
            for (int x = 0; x < mWidth; x ++){
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = (rgb) & 0xff;
                float a = (float)(r + g + b) / 765.0f;
                mData[y * mWidth + x] = a;
            }
    }
}
