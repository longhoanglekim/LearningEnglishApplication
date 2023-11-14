package com.controller;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.*;

public class GameController implements Initializable {
    private static final String HANGMAN_PATH = "src/main/resources/data/hangman.txt";
    private static final String soundCorrect = "src/main/resources/com/sound/correctAnswer.mp3";
    private static final String soundDrawing = "src/main/resources/com/sound/drawing.mp3";

    @FXML
    GridPane gridPaneConsonants;
    File file;
    private int currentWrongTime = 0;

    public String answer;
    List<Label> listLabel;
    List<String> answerList;
    @FXML
    public HBox Hbox;

    @FXML
    public ImageView image1;
    @FXML
    public ImageView image2;
    @FXML
    public ImageView image3;
    @FXML
    public ImageView image4;
    @FXML
    public ImageView image5;
    @FXML
    public ImageView image6;
    @FXML
    public ImageView image7;
    @FXML
    public ImageView image8;
    @FXML
    public ImageView image9;
    @FXML
    public ImageView image10;

    List<ImageView> listImageview;


    public void updateListLabel(String s, int index) {
        listLabel.get(index).setText(s);
    }
    public void updateHbox() {
        Hbox.getChildren().clear();
        for (Label l: listLabel) {
            Hbox.getChildren().add(l);
        }
        Hbox.setLayoutY(520);
        System.out.println(Hbox.getLayoutX() + " " + Hbox.getLayoutY());
        //Hbox.setLayoutY();
    }

    public List<String> readFile(String Path, File file) throws FileNotFoundException {
        List<String> res = new ArrayList<>();
        file = new File(Path);
        Scanner sc = new Scanner(new FileReader(file));
        while (sc.hasNextLine()) {
            res.add(sc.next());
        }
        return res;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listLabel = new ArrayList<>();
        try {
            answerList = readFile(HANGMAN_PATH, file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int randomIndex = new Random().nextInt(answerList.size());
        answer = answerList.get(randomIndex);

        for (int i = 0; i < answer.length(); i++) {
            Label tmp = new Label("__");
            listLabel.add(tmp);
        }
        updateHbox();
        initListImage();
        setOpacity();
    }


    /**
     * Handles the event when a button is clicked.
     *
     * @param event The event triggered when the button is clicked.
     */
    public void addClickEvent(ActionEvent event) {
        // Lấy đối tượng gửi sự kiện (button được nhấn)
        Button clickedButton = (Button) event.getSource();

        System.out.println(clickedButton.getId() + " được nhấn");

        // Thêm mã xử lý tùy ý khi button được nhấn
        // ...
        // Tạo đối tượng FadeTransition
        FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(1000), clickedButton);

        // Thiết lập giá trị từ alpha (độ trong suốt) 1 (hiện tại) đến 0 (mất đi)
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);

        // Xử lý sự kiện khi hoàn thành hiệu ứng
        fadeOutTransition.setOnFinished(e -> {
            // Thêm mã xử lý tùy ý khi button được nhấn
            // ...
            handleClickEvent(clickedButton);
            clickedButton.setVisible(false);
        });

        // Bắt đầu hiệu ứng
        fadeOutTransition.play();

    }

    /**
     * Handles logic when a button is clicked in the context of a word guessing game.
     *
     * @param button The clicked button.
     */
    public void handleClickEvent(Button button) {
        String buttonID = button.getId();
        buttonID = buttonID.toLowerCase().substring(6,7);
        char tmp = buttonID.charAt(0);
        boolean find = false;
        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) == tmp) {
                listLabel.get(i).setText(buttonID.toUpperCase());
                find = true;
            }
        }
        if (!find) {
            // Draw hangman
            currentWrongTime++;
            if (currentWrongTime < 10) {
                updateImage();
            }

        } else {
            playSoundCorrectAnswer();
        }
    }

    public void initListImage() {
        listImageview = new ArrayList<>();
        listImageview.add(image1);
        listImageview.add(image2);
        listImageview.add(image3);
        listImageview.add(image4);
        listImageview.add(image5);
        listImageview.add(image6);
        listImageview.add(image7);
        listImageview.add(image8);
        listImageview.add(image9);
        listImageview.add(image10);
    }

    public void setOpacity() {
        for(int i = 0; i < 10; i++) {
            listImageview.get(i).setOpacity(0.0);
        }
    }

    public void updateImage() {
        playSoundDrawing();
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), listImageview.get(currentWrongTime));
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
        //if(currentWrongTime > 9) currentWrongTime = 0;
    }
    public void playSoundCorrectAnswer() {
        File file = new File(soundCorrect);
        AudioClip audioClip = new AudioClip(file.toURI().toString());
        audioClip.play();
    }
    public void playSoundDrawing() {
        File file = new File(soundDrawing);
        AudioClip audioClip = new AudioClip(file.toURI().toString());
        audioClip.play();
    }
    
}