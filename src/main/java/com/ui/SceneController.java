package com.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for changing scene. Doesn't work yet. Will be changed later.
 * Currently, not in used.
 */
public class SceneController {
    private int currentScene = 0;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onSearchButtonClick() {
    }


    @FXML
    protected void onInputTextChanged() {
        currentScene = 1;
        welcomeText.setText("Add a new word");
    }
}
