package com.controller;

import com.dictionary.Dictionary;
import com.dictionary.Local;
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

import static com.ui.Model.dictionary;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.*;

public class GameController implements Initializable {
    private static final String HANGMAN_PATH = "src/main/resources/data/hangman.txt";

    @FXML
    GridPane gridPaneConsonants;
    File file;
    private String currentGuess;
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
        clickedButton.setVisible(false);
        // Thêm mã xử lý tùy ý khi button được nhấn
        // ...
        handleClickEvent(clickedButton);
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
            if (Character.compare(answer.charAt(i), tmp) == 0) {
                listLabel.get(i).setText(buttonID.toUpperCase());
            } else {
                // Draw hangman
            }
        }
    }
}