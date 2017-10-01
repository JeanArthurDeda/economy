package core.location;

public class LocationStack {
    protected Location[] mPool;
    protected int mIndex;

    private LocationStack() {
    }

    public LocationStack(int size){
        mPool = new Location[size];
        for (int i = 0; i < mPool.length; ++i)
            mPool[i] = new Location();
        mIndex = 0;
    }

    public Location get (){
        return mPool[mIndex ++];
    }

    public void reset (){
        mIndex = 0;
    }
}
