package com.controller;

import com.dictionary.Dictionary;
import com.dictionary.Local;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import static com.ui.Model.dictionary;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.*;

public class GameController implements Initializable {
    private static final String HANGMAN_PATH = "src/main/resources/data/hangman.txt";

    @FXML
    Pane hangmanDraw;
    private double posX = 40;
    private double posY = 200;

    @FXML
    GridPane gridPaneConsonants;
    File file;
    private int currentWrongTime = 0;

    public String answer;
    List<Label> listLabel;
    List<String> answerList;
    @FXML
    public HBox Hbox;
    public void updateListLabel(String s, int index) {
        listLabel.get(index).setText(s);
    }
    public void updateHbox() {
        Hbox.getChildren().clear();
        for (Label l: listLabel) {
            Hbox.getChildren().add(l);
        }
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
        //System.out.println(buttonID);
        for (int i = 0; i < answer.length(); i++) {
            char tmp = buttonID.charAt(0);
            boolean find = false;
            for (int j = i + 1; j < answer.length(); j++) {
                if (answer.charAt(i) == tmp) {
                    listLabel.get(i).setText(buttonID.toUpperCase());
                    find = true;
                }
            }
            if (!find) {
                // Draw hangman
                currentWrongTime++;
                draw();
                break;
            }
        }
    }

    /**
     * method to draw hangman when getting a wrong guess.
     */
    public void draw() {

        if (currentWrongTime == 1) {
            // Draw vertical
            Line lineV = new Line(posX, posY, posX, posY);
            hangmanDraw.getChildren().add(lineV);
            // Tạo Timeline với hiệu ứng xuất hiện từng đoạn trong 3 giây
            Duration durationV = Duration.seconds(2);
            Timeline timelineV = new Timeline();

                // Tạo các KeyFrame và KeyValue
            for (double t = 0; t <= 1.0; t += 0.01) {
                KeyValue keyValue = new KeyValue(lineV.endYProperty(), posY - t * 150);
                KeyFrame keyFrame = new KeyFrame(durationV.multiply(t), keyValue);
                timelineV.getKeyFrames().add(keyFrame);
            }


            // Chạy Timeline và thiết lập sự kiện hoàn thành
            timelineV.setOnFinished(event -> {
                // Draw horizontal
                Line lineH = new Line(posX, posY - 150, posX, posY - 150);
                hangmanDraw.getChildren().add(lineH);

                // Tạo Timeline với hiệu ứng xuất hiện từng đoạn trong 3 giây
                Duration durationH = Duration.seconds(2);
                Timeline timelineH = new Timeline();

                // Tạo các KeyFrame và KeyValue
                for (double t = 0; t <= 1.0; t += 0.01) {
                    KeyValue keyValue = new KeyValue(lineH.endXProperty(), posX + t * 100);
                    KeyFrame keyFrame = new KeyFrame(durationH.multiply(t), keyValue);
                    timelineH.getKeyFrames().add(keyFrame);
                }

                // Chạy Timeline ngang
                timelineH.play();
            });

            // Chạy Timeline dọc
            timelineV.play();

        } else if (currentWrongTime == 2) {
            Line lineV2 = new Line(posX, posY, posX, posY);
            hangmanDraw.getChildren().add(lineV2);
            // Tạo Timeline với hiệu ứng xuất hiện từng đoạn trong 3 giây
            Duration durationV2 = Duration.seconds(2);
            Timeline timelineV2 = new Timeline();

            // Tạo các KeyFrame và KeyValue
            for (double t = 0; t <= 1.0; t += 0.01) {
                // Thay đổi cách tính toán posY để đảm bảo tính nhất quán
                double updatedPosY = posY - 150 + t * 50;
                KeyValue keyValue = new KeyValue(lineV2.endYProperty(), updatedPosY);
                KeyFrame keyFrame = new KeyFrame(durationV2.multiply(t), keyValue);
                timelineV2.getKeyFrames().add(keyFrame);
            }

            // Chạy Timeline dọc
            timelineV2.play();
            System.out.println(2);
        }
    }
}