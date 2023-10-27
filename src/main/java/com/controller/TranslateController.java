package com.controller;

import com.ui.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.dictionary.Translator.translateEnToVi;
import static com.dictionary.Translator.translateViToEn;


public class TranslateController implements Initializable {
    @FXML
    public TextField fromField;
    @FXML
    public TextField toField;
    @FXML
    public ImageView imageFromLanguage;
    @FXML
    public ImageView imageToLanguage;
    @FXML
    public Label labelFromLanguage;
    @FXML
    public Label labelToLanguage;

    Image imageVi = new Image(getClass().getResourceAsStream("/com/icon/Vietnam.png"));
    Image imageEn = new Image(getClass().getResourceAsStream("/com/icon/UK.png"));

    boolean EnToVi = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void onTranslateClicked() {
        try {
            if (EnToVi) {
                toField.setText(translateEnToVi(fromField.getText()));
            } else {
                toField.setText(translateViToEn(fromField.getText()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onSwichClicked() {
        if(EnToVi) {
            labelFromLanguage.setText("Vietnamese");
            labelToLanguage.setText("English");
            imageFromLanguage.setImage(imageVi);
            imageToLanguage.setImage(imageEn);
            EnToVi = false;
        } else {
            labelFromLanguage.setText("English");
            labelToLanguage.setText("Vietnamese");
            imageFromLanguage.setImage(imageEn);
            imageToLanguage.setImage(imageVi);
            EnToVi = true;
        }
    }

}
