package tw.someone.sweeper.model;

public class MoveCounter {

    private int mCount = 0;

    public void increment() {
        mCount++;
    }

    public void reset() {
        mCount = 0;
    }

    public int getCount() {
        return mCount;
    }

}
