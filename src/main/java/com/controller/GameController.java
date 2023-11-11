package com.controller;

import com.dictionary.Dictionary;
import com.dictionary.Local;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import static com.ui.Model.dictionary;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.*;

public class GameController implements Initializable {
    private static final String BOOKMARK_PATH = "src/main/resources/data/bookmark.txt";
    @FXML
    TextArea guessChar;
    @FXML
    TextArea guessLine;
    private String currentGuess;
    private int currentWrongTime;
    private boolean isCharacterEntered = false;
    private String answer;
    private List<String> answerList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File file = new File(BOOKMARK_PATH);
        if (!file.exists()) {
            System.err.println("Bookmark file not found");
            return;
        }
        Scanner sc;
        try {
            sc = new Scanner(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (sc.hasNextLine()) {
            answerList.add(sc.nextLine());
        }
        int randomIndex = new Random().nextInt(answerList.size());
        answer = answerList.get(randomIndex);
        String tmp = new String(new char[answer.length()]).replace('\0', '_');
        guessLine.setText(tmp);
        guessChar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
                processEnteredChar(guessChar.getText());

                guessChar.clear();
            } else {
                String typedCharacter = event.getText();
                if (typedCharacter.length() == 1) {
                    // If there is already a character, clear it before appending the new one
                    if (!guessChar.getText().isEmpty()) {
                        guessChar.clear();
                    }

                } else {
                    System.out.println("Please enter a valid letter.");
                }
            }
        });
    }

    /**
     * Processes the entered character, updates the UI, and checks if the entered character
     * is present in the answer. Displays a message indicating whether the guess is correct or not.
     *
     * @param enteredChar The character entered by the user.
     */
    private void processEnteredChar(String enteredChar) {
        guessChar.setText(enteredChar);
        if (answer.contains(String.valueOf(enteredChar.charAt(0)))) {
            System.err.println("Yes");
        } else {
            System.err.println("No");
        }
        isCharacterEntered = false;
    }
}