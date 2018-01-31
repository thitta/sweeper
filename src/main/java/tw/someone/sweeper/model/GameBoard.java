package tw.someone.sweeper.model;

import javafx.util.Pair;

import java.util.*;

public class GameBoard {

    private int mBoardWidth;
    private int mBoardHeight;
    private int mBombCount;
    private Set<Integer> mBombGrids;
    private int[] mAnswers;
    private ArrayList<GridStatus> mGridsStatus;
    private ArrayList<Pair> mLatestResults;

    // Constructor
    public GameBoard(int boardWidth, int boardHeight, int bombCount) {
        this.mBoardHeight = boardHeight;
        this.mBoardWidth = boardWidth;
        this.mBombCount = bombCount;
        this.refreshBoard();
    }

    // Initiate
    private void refreshBoard() {
        mBombGrids = new TreeSet<>();
        mAnswers = new int[mBoardWidth * mBoardHeight];
        mGridsStatus = new ArrayList<>(mBoardWidth * mBoardHeight);
        resetGridsStatus();
        clearLatestResults();
    }

    private void resetGridsStatus() {
        mGridsStatus = new ArrayList<>();
        int gridCount = mBoardWidth * mBoardHeight;
        for (int gridId = 0; gridId < gridCount; gridId++) {
            mGridsStatus.add(gridId, GridStatus.BLANK);
        }
    }

    // set bombs in the first Step
    public void takeFirstStep(int initGridId) {
        setBombGrids(initGridId);
        setAnswers();
    }

    private void setBombGrids(int initGridId) {
        Random r = new Random();
        while (mBombGrids.size() < mBombCount) {
            int newNumber = r.nextInt(mBoardWidth * mBoardHeight - 1);
            if (newNumber != initGridId) mBombGrids.add(newNumber);
        }
    }

    private void setAnswers() {
        int gridCount = mBoardWidth * mBoardHeight;
        for (int i = 0; i < gridCount; i++) {
            mAnswers[i] = examineGrid(i);
        }
    }

    private int examineGrid(int gridId) {
        if (mBombGrids.contains(gridId)) return 9;
        int result = 0;
        for (int toExamineId : getNearbyGrids(gridId)) {
            if (mBombGrids.contains(toExamineId)) result += 1;
        }
        return result;
    }

    //  make move
    public void checkGrid(int gridId) {
        mGridsStatus.set(gridId, GridStatus.CHECKED);
        clearLatestResults();
        if (isBombGrid(gridId)) return;
        addLastResult(gridId);
        checkGridSpread(gridId);
    }

    public ArrayList<Pair> getLatestResults() {
        return mLatestResults;
    }

    private void clearLatestResults() {
        mLatestResults = new ArrayList<>();
    }

    private void addLastResult(int gridId) {
        Pair result = new Pair<>(gridId, getGridAnswer(gridId));
        mLatestResults.add(result);
    }

    private void checkGridSpread(int gridId) {
        if (!isZeroGrid(gridId)) return;
        // get all related zero grids
        Set<Integer> zeroGrids = new TreeSet<>();
        Set<Integer> newZeroGrids = new TreeSet<>();
        zeroGrids.add(gridId);
        while (!zeroGrids.equals(newZeroGrids)) {
            zeroGrids.addAll(newZeroGrids);
            newZeroGrids.addAll(zeroGrids);
            for (int zeroGridId : zeroGrids) {
                newZeroGrids.addAll(getNearbyZeroGrids(zeroGridId));
            }
        }
        // get all nearby grids
        Set<Integer> toCheckGrids = new TreeSet<>();
        for (int zeroGridId : zeroGrids) {
            toCheckGrids.add(zeroGridId);
            toCheckGrids.addAll(getNearbyGrids(zeroGridId));
        }
        // check all
        for (int toCheckGridId : toCheckGrids) {
            mGridsStatus.set(toCheckGridId, GridStatus.CHECKED);
            addLastResult(toCheckGridId);
        }
    }

    private Set<Integer> getNearbyGrids(int gridId) {
        // prepare
        Set<Integer> nearbyGrids = new TreeSet<>();
        Set<Integer> toRemove = new TreeSet<>();
        // add all surrounding grid ID
        nearbyGrids.add(gridId + 1);
        nearbyGrids.add(gridId - 1);
        nearbyGrids.add(gridId + mBoardWidth);
        nearbyGrids.add(gridId + mBoardWidth + 1);
        nearbyGrids.add(gridId + mBoardWidth - 1);
        nearbyGrids.add(gridId - mBoardWidth);
        nearbyGrids.add(gridId - mBoardWidth + 1);
        nearbyGrids.add(gridId - mBoardWidth - 1);
        // check none-necessary grids
        for (int value : nearbyGrids) {
            if (value > mBoardWidth * mBoardHeight - 1 || value < 0) {
                toRemove.add(value);
            } else if (gridId % mBoardWidth == 0) {
                toRemove.add(gridId - 1);
                toRemove.add(gridId + mBoardWidth - 1);
                toRemove.add(gridId - mBoardWidth - 1);
            } else if (gridId % mBoardWidth == mBoardWidth - 1) {
                toRemove.add(gridId + 1);
                toRemove.add(gridId + mBoardWidth + 1);
                toRemove.add(gridId - mBoardWidth + 1);
            }
        }
        // remove and return
        nearbyGrids.removeAll(toRemove);
        return nearbyGrids;
    }

    private Set<Integer> getNearbyZeroGrids(int gridId) {
        Set<Integer> result = new TreeSet<>();
        for (int nearbyGridId : getNearbyGrids(gridId)) {
            if (getGridAnswer(nearbyGridId) == 0) result.add(nearbyGridId);
        }
        return result;
    }

    // Danger Grid
    public GridStatus trySetDangerGrid(int gridId) {
        if (mGridsStatus.get(gridId) == GridStatus.BLANK) mGridsStatus.set(gridId, GridStatus.DANGER);
        else if (mGridsStatus.get(gridId) == GridStatus.DANGER) mGridsStatus.set(gridId, GridStatus.BLANK);
        return mGridsStatus.get(gridId);
    }

    // Status Check
    public boolean isBoardCompleted() {
        int uncheckedGridsCount = (int) mGridsStatus.stream().filter(p -> p.equals(GridStatus.BLANK)).count();
        return uncheckedGridsCount == mBombCount;
    }

    public boolean isBombGrid(int gridId) {
        return mBombGrids.contains(gridId);
    }

    public boolean isDangerGrid(int gridId) {
        return mGridsStatus.get(gridId) == GridStatus.DANGER;
    }

    public boolean isCheckedGrid(int gridId) {
        return mGridsStatus.get(gridId) == GridStatus.CHECKED;
    }

    private boolean isZeroGrid(int gridId) {
        return examineGrid(gridId) == 0;
    }

    public int[] getAnswers() {
        return mAnswers;
    }

    private int getGridAnswer(int gridId) {
        return mAnswers[gridId];
    }

}