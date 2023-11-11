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
    private int currentWrongTime = 0;

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
        char[] arr = new char[answer.length()];
        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) >= 'a' && answer.charAt(i) <= 'z') {
                arr[i] = '_';
            } else {
                arr[i] = answer.charAt(i);
            }
        }
        currentGuess = new String(arr);
        guessLine.setText(currentGuess);
        guessChar.setOnKeyPressed(event -> {
            if (currentWrongTime < 5) {
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
            } else {
                // Nếu currentWrongTime vượt quá 5, bạn có thể thực hiện các hành động khác ở đây hoặc không thực hiện gì cả.
                System.out.println("Game over! You've reached the maximum number of wrong guesses.");
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
    }
}