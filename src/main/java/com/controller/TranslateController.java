package com.controller;

import com.ui.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

import static com.dictionary.Translator.translateEnToVi;


public class TranslateController implements Initializable {
    @FXML
    public TextField fromField;
    @FXML
    public TextField toField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void onTranslateClicked() {
        try {
            toField.setText(translateEnToVi(fromField.getText()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
