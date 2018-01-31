package tw.someone.sweeper.model;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class UI {

    private String[] mMainBtnStyles = new String[]{"mainBtnFail", "mainBtnNormal", "mainBtnSucceed"};
    private int mBoardHeight;
    private int mBoardWidth;

    private GridPane mBoardContainer;
    private Button mMainButton;
    private GridButton[] mGridButtons;


    // Constructor
    public UI(Button mainBtn, GridPane boardContainer, GridButton[] gridButtons, int boardWidth, int boardHeight) {
        this.mMainButton = mainBtn;
        this.mBoardContainer = boardContainer;
        this.mGridButtons = gridButtons;
        this.mBoardWidth = boardWidth;
        this.mBoardHeight = boardHeight;
        this.addAllGridButtons();
    }

    private void addAllGridButtons() {
        for (int i = 0; i < mBoardWidth * mBoardHeight; i++) {
            GridButton btn = mGridButtons[i];
            btn.getStyleClass().add("gridBtn");
            GridPane.setRowIndex(btn, i / mBoardWidth);
            GridPane.setColumnIndex(btn, i % mBoardWidth);
            mBoardContainer.getChildren().add(btn);
        }
    }

    // Grid UI
    public void setGridValue(int gridId, int value) {
        mGridButtons[gridId].setValue(value);
    }

    public void setGridDanger(int gridId) {
        mGridButtons[gridId].setDanger();
    }

    public void setGridBlank(int gridId) {
        mGridButtons[gridId].clear();
    }

    // All Grids UI
    public void showAllGrids(int[] answers, boolean isWin) {
        for (int i = 0; i < answers.length; i++) {
            int answer = answers[i];
            if (answer == 9) {
                if (isWin) mGridButtons[i].setFlag();
                else mGridButtons[i].setBomb();
            } else {
                mGridButtons[i].setValue(answer);
            }
        }
    }

    public void clearAllGrids() {
        for (GridButton btn : mGridButtons) btn.clear();
    }

    // Home UI
    public void gameStart() {
        clearMainButton();
        mMainButton.getStyleClass().add("mainBtnNormal");
    }

    public void gameSucceed() {
        clearMainButton();
        mMainButton.getStyleClass().add("mainBtnSucceed");
    }

    public void gameFail() {
        clearMainButton();
        mMainButton.getStyleClass().add("mainBtnFail");
    }

    private void clearMainButton() {
        mMainButton.getStyleClass().removeAll(mMainBtnStyles);
    }

}
