package com.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Hangman {
    private static final String HANGMAN_PATH = "src/main/resources/data/hangman.txt";
    private static final int MAX_WRONG_GUESS = 10;
    private static int wrongGuess = 0;
    private static String answer;
    private static List<Character> guessList;
    private static List<String> candidateWords;

    public Hangman() {}

    public void initialize() {
        guessList = new ArrayList<>();
        candidateWords = new ArrayList<>();
        readFile();
        randomWord();
    }

    private void readFile() {
        try {
            File file = new File(HANGMAN_PATH);
            Scanner sc = new Scanner(new FileReader(file));
            while (sc.hasNextLine()) {
                candidateWords.add(sc.next());
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found - Hangman");
        }
    }

    public boolean takeGuess(Character guess) {
        if (guessList.contains(guess)) {
            guessList.remove(guess);
            return true;
        }
        wrongGuess++;
        return false;
    }

    public String randomWord() {
        int index = (int) (Math.random() * candidateWords.size());
        wrongGuess = 0;
        answer = candidateWords.get(index);
        guessList.clear();
        for (int i = 0; i < answer.length(); i++) {
            if (!guessList.contains(answer.charAt(i))) {
                guessList.add(answer.charAt(i));
            }
        }
        return answer;
    }

    public boolean isGameOver() {
        return wrongGuess >= MAX_WRONG_GUESS;
    }

    public boolean isWin() {
        return wrongGuess < MAX_WRONG_GUESS && guessList.isEmpty();
    }

    public int getWrongGuess() {
        return wrongGuess;
    }

    public void setWrongGuess(int wrongGuess) {
        this.wrongGuess = wrongGuess;
    }

    public String getWord() {
        return answer;
    }
}
