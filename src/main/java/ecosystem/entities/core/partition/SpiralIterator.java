package ecosystem.entities.core.partition;

public class SpiralIterator {
    public static abstract class Receiver {
        public void onStart (int startX, int startY) {}
        public boolean onStartSquare (int x, int y, int startX, int startY) {return true;}
        public boolean onValidValue (int x, int y, int startX, int startY) {return true;}
    }

    protected transient static final int[] mDirs = new int[] {
            1, 0,           // right
            0, 1,           // down
            -1, 0,          // left
            0, -1           // up
    };
    protected transient int mStartX;
    protected transient int mStartY;
    protected transient int mX;
    protected transient int mY;
    protected transient int mWidth;
    protected transient int mHeight;

    protected transient int mEdgeSize;
    protected transient int mEdgeStep;
    protected transient int mSteps;
    protected transient int mDir;
    protected transient Receiver mReceiver;

    public int getWidth (){
        return mWidth;
    }

    public int getHeight (){
        return mHeight;
    }

    public boolean iterate (int x, int y, int width, int height, Receiver receiver){
        mStartX = x;
        mStartY = y;
        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
        mEdgeSize = 1;

        mEdgeSize = 1;
        mEdgeStep = 0;
        mSteps = 0;
        mDir = 0;
        mReceiver = receiver;

        // start
        mReceiver.onStart(mStartX, mStartY);
        if (!mReceiver.onValidValue(mX, mY, mStartX, mStartY))
            return false;
        mSteps ++;

        mX -= 2;
        mY -= 1;
        mEdgeSize += 2;
        // start square
        if (!mReceiver.onStartSquare(mX + 1, mY, mStartX, mStartY))
            return false;


        while (mSteps < mWidth * mHeight){
            if (getNext() && !mReceiver.onStartSquare(mX, mY, mStartX, mStartY))
                return false;
            while (mX < 0 || mX >= mWidth || mY < 0 || mY >= mHeight)
                if (getNext() && !mReceiver.onStartSquare(mX, mY, mStartX, mStartY))
                    return false;
            mSteps ++;

            if (!mReceiver.onValidValue(mX, mY, mStartX, mStartY))
                return false;
        }
        return true;
    }

    protected boolean getNext(){
        boolean startSquare = false;
        // new step on edge
        mEdgeStep ++;
        // end of spiral
        if (mDir == 3 && mEdgeStep == mEdgeSize){
            mDir = 0;
            mEdgeStep = 1;
            mEdgeSize += 2;
            mX -= 2;
            mY -= 2;
            startSquare = true;
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
        return startSquare;
    }
}
