package com.controller;

import com.task.TextToSpeechTask;
import com.task.TranslateTask;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class TranslateController implements Initializable {
    @FXML
    private Button fromSpeakButton;

    @FXML
    private Button toSpeakButton;

    @FXML
    private Button copyToClipboardButton;

    @FXML
    private FontAwesomeIconView fromSpeakIcon;

    @FXML
    private FontAwesomeIconView toSpeakIcon;

    @FXML
    private TextArea fromField;

    @FXML
    private TextArea toField;

    @FXML
    private Label labelFromLanguage;

    @FXML
    private Label labelToLanguage;

    @FXML
    private Button switchButton;

    @FXML
    private Label limitLabel;

    @FXML
    private HBox notificationHBox;

    @FXML
    private Label notificationLabel;

    TextToSpeechTask speechTask;

    TranslateTask translateTask;

    boolean enToVi = true;
    private String fromLanguage = "en";
    private String toLanguage = "vi";
    private String currentString;
    private static final int CHARACTER_LIMIT = 5000;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        notificationHBox.setVisible(false);
        PauseTransition visiblePause = new PauseTransition(
                javafx.util.Duration.seconds(2)
        );
        visiblePause.setOnFinished(
                event -> notificationHBox.setVisible(false)
        );

        limitLabel.setText("0/" + CHARACTER_LIMIT);

        fromFieldListener(visiblePause);
        fromFieldTextFormatter(visiblePause);

        fromSpeakButton.setOnAction(event -> onFromSpeak());
        toSpeakButton.setOnAction(event -> onToSpeak());
        copyToClipboardButton.setOnAction(event -> onCopyToClipboard());
        switchButton.setOnAction(event -> switchLanguage());
    }

    private void fromFieldListener(PauseTransition visiblePause) {
        fromField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (translateTask != null) {
                translateTask.cancel();
            }
            currentString = newValue;
            translateTask = new TranslateTask(currentString, fromLanguage, toLanguage);
            translateTask.setOnSucceeded(event -> {
                String result = translateTask.getValue();
                toField.setText(result);
            });
            translateTask.setOnFailed(event -> {
                notificationLabel.setText("No internet connection");
                notificationHBox.setVisible(true);
                if (visiblePause.getStatus() == javafx.animation.Animation.Status.RUNNING) {
                    visiblePause.playFromStart();
                } else {
                    visiblePause.play();
                }
                toField.setText("");
            });
            new Thread(translateTask).start();

            if (newValue == null || newValue.length() > 100) {
                fromField.setStyle("-fx-font-size: 1.2em;");
                toField.setStyle("-fx-font-size: 1.2em;");
            } else {
                fromField.setStyle("-fx-font-size: 1.5em;");
                toField.setStyle("-fx-font-size: 1.5em;");
            }

            limitLabel.setText(newValue == null ? "0" : newValue.length() + "/" + CHARACTER_LIMIT);
        });
    }

    private void fromFieldTextFormatter(PauseTransition visiblePause) {
        fromField.setTextFormatter(new TextFormatter<Character>(change -> {
            if (change.getControlNewText().length() <= CHARACTER_LIMIT) {
                return change;
            }
            notificationLabel.setText("You can only translate 5000 character at a time");
            notificationHBox.setVisible(true);
            if (visiblePause.getStatus() == javafx.animation.Animation.Status.RUNNING) {
                visiblePause.playFromStart();
            } else {
                visiblePause.play();
            }
            return null;
        }));
    }

    private void onFromSpeak() {
        try {
            speechTask = new TextToSpeechTask(fromField.getText(), fromLanguage);
            new Thread(speechTask).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void onToSpeak() {
        try {
            speechTask = new TextToSpeechTask(toField.getText(), toLanguage);
            new Thread(speechTask).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void onCopyToClipboard() {
        String text = toField.getText();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(text);
        clipboard.setContent(clipboardContent);
    }

    public void switchLanguage() {
        if(enToVi) {
            labelFromLanguage.setText("Tiếng Việt");
            labelToLanguage.setText("Tiếng Anh");
            fromLanguage = "vi";
            toLanguage = "en";
        } else {
            labelFromLanguage.setText("Tiếng Anh");
            labelToLanguage.setText("Tiếng Việt");
            fromLanguage = "en";
            toLanguage = "vi";
        }
        fromField.setText(toField.getText());
        enToVi = !enToVi;
    }
}