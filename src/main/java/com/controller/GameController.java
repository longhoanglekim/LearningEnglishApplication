package com.controller;

import com.dictionary.Dictionary;
import com.dictionary.Local;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import static com.ui.Model.dictionary;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.*;

public class GameController implements Initializable {
    private static final String HANGMAN_PATH = "src/main/resources/data/hangman.txt";

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

   /* *//**
     * Processes the entered character, updates the UI, and checks if the entered character
     * is present in the answer. Displays a message indicating whether the guess is correct or not.
     *
     * @param enteredChar The character entered by the user.
     *//*
    private void processEnteredChar(String enteredChar) {
        guessChar.setText(enteredChar);
        if (answer.contains(String.valueOf(enteredChar.charAt(0)))) {
            System.err.println("Yes");
            char tmp = enteredChar.charAt(0);
            char[] charArray = currentGuess.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                if (answer.charAt(i) == tmp) {
                    charArray[i] = tmp;
                }
            }
            currentGuess = new String(charArray);
            guessLine.setText(currentGuess);
        } else {
            System.err.println("No");
            System.err.println(++currentWrongTime);
        }
    }*/
}