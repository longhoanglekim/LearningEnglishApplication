package com.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

/**
 * Controller for changing scene. Doesn't work yet. Will be changed later.
 * Currently, not in used.
 */
public class SceneController {
    //private int currentScene = 0;
    @FXML
    private Label welcomeText;

    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    protected void onSearchButtonClick() {
    }


    @FXML
    protected void onInputTextChanged() {
        //currentScene = 1;
        welcomeText.setText("Add a new word");
    }
}
