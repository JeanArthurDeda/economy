package ecosystem.entities.core.partition;

public class SpiralIterator {
    protected transient static final int[] mDirs = new int[] {
            1, 0,           // right
            0, 1,           // down
            -1, 0,          // left
            0, -1           // up
    };
    protected int mX;
    protected int mY;
    protected int mWidth;
    protected int mHeight;
    protected int mEdgeSize;
    protected int mEdgeStep;
    protected int mSteps;
    protected int mDir;

    public SpiralIterator(int x, int y, int width, int height) {
        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
        mEdgeSize = 1;
    }

    public int getWidth (){
        return mWidth;
    }

    public int getHeight (){
        return mHeight;
    }

    public int get (){
        // start
        if (0 == mSteps){
            int index = mY * mWidth + mX;

            // Compute next
            mDir = 0;
            mEdgeStep = 0;
            mEdgeSize += 2;
            mSteps = 1;
            mX -= 2;
            mY -= 1;
            return index;
        }
        // end
        if (mSteps == mWidth * mHeight)
            return -1;

        getNext();
        while (mX < 0 || mX >= mWidth || mY < 0 || mY >= mHeight)
            getNext();
        mSteps ++;
        return mY * mWidth + mX;
    }

    protected void getNext(){
        // new step on edge
        mEdgeStep ++;
        // end of spiral
        if (mDir == 3 && mEdgeStep == mEdgeSize){
            mDir = 0;
            mEdgeStep = 1;
            mEdgeSize += 2;
            mX -= 2;
            mY -= 2;
        }
        // end of edge
        if (mEdgeStep == mEdgeSize + 1){
            mEdgeStep = 2;
            mDir ++;
        }
        int addX = mDirs[mDir * 2];
        int addY = mDirs[mDir * 2 + 1];
        mX += addX;
        mY += addY;
    }
}
