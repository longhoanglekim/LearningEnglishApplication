package com.controller;

import com.dictionary.TextToSpeech;
import com.thread.TranslateTask;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.*;


public class TranslateController implements Initializable {
    @FXML
    private Button fromSpeakButton;

    @FXML
    private Button toSpeakButton;

    @FXML
    private TextArea fromField;

    @FXML
    private TextArea toField;
    /*    @FXML
        public ImageView imageFromLanguage;
        @FXML
        public ImageView imageToLanguage;*/
    @FXML
    private Label labelFromLanguage;

    @FXML
    private Label labelToLanguage;

    @FXML
    private Button switchButton;

    @FXML
    private Label limitLabel;

    TranslateTask translateTask;
    Image imageVi;
    Image imageEn;
    boolean enToVi = true;
    private String currentString;
    private static final int CHARACTER_LIMIT = 5000;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageVi = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/icon/Vietnam.png")));
        imageEn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/icon/UK.png")));
        fromField.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));
        toField.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));
        labelFromLanguage.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));;
        labelToLanguage.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));

        fromField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() <= CHARACTER_LIMIT) {
                if (translateTask != null) {
                    translateTask.cancel();
                }
                currentString = newValue;
                translateTask = new TranslateTask(currentString, enToVi ? "en" : "vi", enToVi ? "vi" : "en");
                translateTask.setOnSucceeded(event -> {
                    System.out.println("Translate success");
                    String result = translateTask.getValue();
                    toField.setText(result);
                });
                translateTask.setOnFailed(event -> {
                    toField.setText("No internet connection");
                });
                new Thread(translateTask).start();
            } else {
                fromField.setText(oldValue);
            }
            limitLabel.setText(newValue.length() + "/" + CHARACTER_LIMIT);
        });
        fromSpeakButton.setOnAction(event -> {
            try {
                TextToSpeech.play(fromField.getText(), enToVi ? "en" : "vi");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        toSpeakButton.setOnAction(event -> {
            try {
                TextToSpeech.play(toField.getText(), enToVi ? "vi" : "en");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        switchButton.setOnAction(event -> switchLanguage());
    }

    public void switchLanguage() {
        if(enToVi) {
            labelFromLanguage.setText("Tiếng Việt");
            labelToLanguage.setText("Tiếng Anh");
            enToVi = false;
        } else {
            labelFromLanguage.setText("Tiếng Anh");
            labelToLanguage.setText("Tiếng Việt");
            enToVi = true;
        }
        fromField.setText(toField.getText());
    }
}