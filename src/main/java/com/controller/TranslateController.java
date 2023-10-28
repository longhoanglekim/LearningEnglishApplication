package com.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.URL;
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

        // Press enter to translate.
        fromField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
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
        });
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
