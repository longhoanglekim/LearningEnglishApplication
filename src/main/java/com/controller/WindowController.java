package com.controller;

import com.ui.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.ToggleSwitch;

import java.net.URL;
import java.util.ResourceBundle;

public class WindowController implements Initializable {
    @FXML
    public BorderPane rootWindow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getView().getSelectedMenuItem().addListener((observableValue, oldValue, newValue) -> {
            switch (newValue) {
                case "History":
                    rootWindow.setCenter(Model.getInstance().getView().getHistoryPane());
                    break;
                case "Translate":
                    rootWindow.setCenter(Model.getInstance().getView().getTranslatePane());
                    break;
                case "Game":
                    rootWindow.setCenter(Model.getInstance().getView().getGamePane());
                    break;
                default:
                    rootWindow.setCenter(Model.getInstance().getView().getDictionaryPane());
                    break;
            }
        });
    }

}
