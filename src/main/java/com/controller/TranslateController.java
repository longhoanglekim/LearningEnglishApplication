package com.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.dictionary.Translator.translateEnToVi;
import static com.dictionary.Translator.translateViToEn;


public class TranslateController implements Initializable {
    @FXML
    public TextArea fromField;
    @FXML
    public TextArea toField;
    @FXML
    public ImageView imageFromLanguage;
    @FXML
    public ImageView imageToLanguage;
    @FXML
    public Label labelFromLanguage;
    @FXML
    public Label labelToLanguage;
    @FXML
    public ListView<String> historyTranslate;
    Image imageVi, imageEn;
    boolean EnToVi = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageVi = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/icon/Vietnam.png")));
        imageEn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/icon/UK.png")));
        historyTranslate.setVisible(false);
    }

    public void onTranslateClicked() {
        try {
            String toTranslate;
            String fromTranslate = fromField.getText();
            if (EnToVi) {
                toTranslate = translateEnToVi(fromTranslate);
            } else {
                toTranslate = translateViToEn(fromTranslate);
            }
            toField.setText(toTranslate);
            historyTranslate.getItems().addAll(fromTranslate);
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
    public void onHistoryClicked() {
        historyTranslate.setVisible(true);
    }

}
