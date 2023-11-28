package com.controller;

import com.ui.Model;
import com.ui.Preload;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.util.ResourceBundle;

public class PreloadController implements Initializable {
    @FXML
    private Label progressLabel;
    @FXML
    private ProgressBar progressBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

}
