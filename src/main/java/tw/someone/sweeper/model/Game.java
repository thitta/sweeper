package tw.someone.sweeper.model;

public class Game {

    private GameBoard mGameBoard;
    private MoveCounter mMoveCounter;
    private GameStatus mGameStatus;

    // constructor
    public Game(int boardWidth, int boardHeight, int bombCount) {
        mGameBoard = new GameBoard(boardWidth, boardHeight, bombCount);
        mMoveCounter = new MoveCounter();
        mGameStatus = GameStatus.PROCEED;
    }

    // make move
    public void makeMove(int gridId) {
        // Special Case
        if (mGameStatus != GameStatus.PROCEED || mGameBoard.isDangerGrid(gridId) || mGameBoard.isCheckedGrid(gridId))
            return;
        if (mMoveCounter.getCount() == 0) mGameBoard.takeFirstStep(gridId);
        // General Case
        mMoveCounter.increment();
        mGameBoard.checkGrid(gridId);
        // Check if game win or fail
        if (mGameBoard.isBombGrid(gridId)) setGameStatus(GameStatus.FAIL);
        else if (mGameBoard.isBoardCompleted()) setGameStatus(GameStatus.WIN);
    }

    // getter and setter
    public GameBoard getGameBoard() {
        return mGameBoard;
    }

    public MoveCounter getMoveCounter() {
        return mMoveCounter;
    }

    public GameStatus getGameStatus() {
        return mGameStatus;
    }

    private void setGameStatus(GameStatus gamestatus) {
        mGameStatus = gamestatus;
    }

}
