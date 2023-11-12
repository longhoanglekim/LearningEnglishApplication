package com.controller;

import com.task.TextToSpeechTask;
import com.task.TranslateTask;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.*;


public class TranslateController implements Initializable {
    @FXML
    private Button fromSpeakButton;

    @FXML
    private Button toSpeakButton;

    @FXML
    private FontAwesomeIconView fromSpeakIcon;

    @FXML
    private FontAwesomeIconView toSpeakIcon;

    @FXML
    private TextArea fromField;

    @FXML
    private TextArea toField;

    @FXML
    public ImageView imageFrom;

    @FXML
    public ImageView imageTo;

    @FXML
    private Label labelFromLanguage;

    @FXML
    private Label labelToLanguage;

    @FXML
    private Button switchButton;

    @FXML
    private Label limitLabel;

    TextToSpeechTask speechTask;

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

        imageFrom = new ImageView(imageEn);
        imageTo = new ImageView(imageVi);

        limitLabel.setText("0/" + CHARACTER_LIMIT);

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
                    toField.setText("");
                });
                new Thread(translateTask).start();
            } else {
                fromField.setText(oldValue);
            }
            if (newValue.length() > 100) {
                fromField.setStyle("-fx-font-size: 1.2em;");
                toField.setStyle("-fx-font-size: 1.2em;");
            } else {
                fromField.setStyle("-fx-font-size: 1.5em;");
                toField.setStyle("-fx-font-size: 1.5em;");
            }
            limitLabel.setText(newValue.length() + "/" + CHARACTER_LIMIT);
        });
        fromSpeakButton.setOnAction(event -> {
            try {
                //TextToSpeech.play(fromField.getText(), enToVi ? "en" : "vi");
                speechTask = new TextToSpeechTask(fromField.getText(), enToVi ? "en" : "vi");
                new Thread(speechTask).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        toSpeakButton.setOnAction(event -> {
            try {
                //TextToSpeech.play(toField.getText(), enToVi ? "vi" : "en");
                speechTask = new TextToSpeechTask(toField.getText(), enToVi ? "vi" : "en");
                new Thread(speechTask).start();
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