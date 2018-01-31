package tw.someone.sweeper.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {

    private Game mGame;
    private final int DEFAULT_BOARD_WIDTH = 8;
    private final int DEFAULT_BOARD_HEIGHT = 8;
    private final int DEFAULT_BOMB_COUNT = 10;

    @Before
    public void setUp() {
        mGame = new Game(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, DEFAULT_BOMB_COUNT);
    }

    @Test
    public void stepIsCount() {
        mGame.makeMove(0);
        assertEquals(1, mGame.getMoveCounter().getCount());
    }

    @Test
    public void stepCanOnlyBeCountOnce() {
        mGame.makeMove(0);
        mGame.makeMove(0);
        assertEquals(1, mGame.getMoveCounter().getCount());
    }

    @Test
    public void dangerGridCantBeCheck() {
        mGame.getGameBoard().trySetDangerGrid(0);
        mGame.makeMove(0);
        assertEquals(0, mGame.getMoveCounter().getCount());
        assertEquals(false, mGame.getGameBoard().isCheckedGrid(0));
    }

    @Test
    public void dangerGridCanBeRelease() {
        mGame.getGameBoard().trySetDangerGrid(0);
        mGame.getGameBoard().trySetDangerGrid(0);
        mGame.makeMove(0);
        assertEquals(1, mGame.getMoveCounter().getCount());
        assertEquals(true, mGame.getGameBoard().isCheckedGrid(0));
    }

    @Test
    public void firstStepCantBeBomb() {
        for (int i = 0; i < 100; i++) {
            mGame = new Game(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, DEFAULT_BOMB_COUNT);
            mGame.makeMove(0);
            assertEquals(GameStatus.PROCEED, mGame.getGameStatus());
        }
    }

    @Test
    public void gameCanBeReset() {
        mGame.makeMove(0);
        mGame = new Game(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, DEFAULT_BOMB_COUNT);
        assertEquals(0, mGame.getMoveCounter().getCount());
        assertEquals(false, mGame.getGameBoard().isCheckedGrid(0));
    }

}