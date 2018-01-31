package tw.someone.sweeper.model;

import javafx.scene.control.Button;

public class GridButton extends Button {

    private String[] mGridStyleClass = new String[]{"gridBtnDanger", "gridBtnBomb", "gridBtnFlag"};

    // Constructor
    public GridButton(int gridId) {
        this.setUserData(Integer.toString(gridId));
    }

    // Set Grid Button Value and Style Class
    public void clear() {
        this.removeStyleClass();
        this.setText("");
    }

    public void setValue(int answer) {
        this.clear();
        String btnText = "";
        if (answer == 9) {
            this.setBomb();
        } else if (answer == 0) {
            btnText = "-";
        } else {
            btnText = Integer.toString(answer);
        }
        this.setStyleClassByAnswer(answer);
        this.setText(btnText);
    }

    public void setDanger() {
        this.clear();
        this.getStyleClass().add("gridBtnDanger");
    }

    public void setBomb() {
        this.clear();
        this.getStyleClass().add("gridBtnBomb");
    }

    public void setFlag() {
        this.clear();
        this.getStyleClass().add("gridBtnFlag");
    }

    private void removeStyleClass() {
        for (int i = 0; i < 8; i++) {
            String toRemoveClassName = "text-color-" + Integer.toString(i);
            this.getStyleClass().remove(toRemoveClassName);
        }
        this.getStyleClass().removeAll(mGridStyleClass);
    }

    private void setStyleClassByAnswer(int answer) {
        String styleClassName = "text-color-" + Integer.toString(answer);
        this.getStyleClass().add(styleClassName);
    }

}