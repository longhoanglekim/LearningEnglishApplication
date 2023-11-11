package com.controller;

import com.dictionary.TextToSpeech;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.dictionary.Translator.translateEnToVi;
import static com.dictionary.Translator.translateViToEn;


public class TranslateController implements Initializable {
    @FXML
    public Button speakButton;
    @FXML
    public TextArea fromField;
    @FXML
    public TextArea toField;
    /*    @FXML
        public ImageView imageFromLanguage;
        @FXML
        public ImageView imageToLanguage;*/
    @FXML
    public Label labelFromLanguage;

    @FXML
    public Label labelToLanguage;

    @FXML
    public Button switchButton;
    Image imageVi;
    Image imageEn;
    boolean enToVi = true;
    private String currentString;
    private static int LIMIT = 2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageVi = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/icon/Vietnam.png")));
        imageEn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/icon/UK.png")));
        fromField.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));
        toField.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));
        labelFromLanguage.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));;
        labelToLanguage.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));

        fromField.textProperty().addListener((observableValue, oldVal, newVal) -> {
            try {
                if (newVal == null || newVal.isEmpty()) {
                    toField.setText("");
                    return;
                }
                if (newVal.equals(oldVal)) return;
                if (enToVi) {
                    currentString = translateEnToVi(newVal);
                } else {
                    currentString = translateViToEn(newVal);
                }
                toField.setText(currentString);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        speakButton.setOnAction(event -> {
            try {
                if (enToVi) {
                    TextToSpeech.play(fromField.getText());
                } else {
                    TextToSpeech.play(fromField.getText());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        switchButton.setOnAction(event -> switchLanguage());
    }

    public void switchLanguage() {
        String temp = fromField.getText();
        fromField.setText(toField.getText());
        toField.setText(temp);
        if(enToVi) {
            labelFromLanguage.setText("Vietnamese");
            labelToLanguage.setText("English");
            enToVi = false;
            GridPane.setConstraints(speakButton, 0, 2);
        } else {
            labelFromLanguage.setText("English");
            labelToLanguage.setText("Vietnamese");
            enToVi = true;
            GridPane.setConstraints(speakButton, 2, 2);
        }
    }
}