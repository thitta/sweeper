package tw.someone.sweeper.controller;

import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import tw.someone.sweeper.model.*;

public class Home {

    private final int DEFAULT_BOARD_WIDTH = 8;
    private final int DEFAULT_BOARD_HEIGHT = 8;
    private final int DEFAULT_BOMB_COUNT = 10;
    private UI mUI;
    private Game mGame;

    @FXML
    private Button uiMainButton;
    @FXML
    private GridPane uiBoardContainer;

    // Constructor
    public Home() {
        mGame = new Game(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, DEFAULT_BOMB_COUNT);
    }

    // Initialize
    @FXML
    protected void initialize() {
        // register event listener of grid button
        GridButton[] gridButtons = new GridButton[DEFAULT_BOARD_WIDTH * DEFAULT_BOARD_HEIGHT];
        for (int row = 0; row < DEFAULT_BOARD_HEIGHT; row++) {
            for (int column = 0; column < DEFAULT_BOARD_WIDTH; column++) {
                int gridId = row * DEFAULT_BOARD_WIDTH + column;
                Button btn = new GridButton(gridId);
                gridButtons[gridId] = (GridButton) btn;
                btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        int gridId = Integer.parseInt(((Button) event.getSource()).getUserData().toString());
                        if (event.getButton() == MouseButton.SECONDARY) setDangerGrid(gridId);
                        if (event.getButton() == MouseButton.PRIMARY) makeMove(gridId);
                    }
                });
            }
        }
        mUI = new UI(uiMainButton, uiBoardContainer, gridButtons, DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT);
    }

    public void restartGame() {
        mGame = new Game(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, DEFAULT_BOMB_COUNT);
        mUI.clearAllGrids();
        mUI.gameStart();
    }

    // Left Click on Grid
    private void makeMove(int gridId) {
        mGame.makeMove(gridId);
        for (Pair result : mGame.getGameBoard().getLatestResults()) {
            mUI.setGridValue((int) result.getKey(), (int) result.getValue());
        }
        // check game fail or win
        if (mGame.getGameStatus() == GameStatus.FAIL) {
            mUI.showAllGrids(mGame.getGameBoard().getAnswers(), false);
            mUI.gameFail();
        } else if (mGame.getGameStatus() == GameStatus.WIN) {
            mUI.showAllGrids(mGame.getGameBoard().getAnswers(), true);
            mUI.gameSucceed();
        }
    }

    // Right Click on Grid
    private void setDangerGrid(int gridId) {
        if (mGame.getGameStatus() != GameStatus.PROCEED) return;
        GridStatus result = mGame.getGameBoard().trySetDangerGrid(gridId);
        if (result == GridStatus.DANGER) mUI.setGridDanger(gridId);
        if (result == GridStatus.BLANK) mUI.setGridBlank(gridId);
    }

}
