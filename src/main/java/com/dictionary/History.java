package com.dictionary;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class History {
    private static final String HISTORY_PATH = "src/main/resources/data/history.txt";
    private static final int LIMIT = 50;
    private final List<String> historyList = new ArrayList<>();

    /**
     * Constructor.
     */
    public History() {
        try {
            loadHistory();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage() + "\n" + e.getCause());
        }
    }

    /**
     * Load history from file.
     */
    public void loadHistory() throws FileNotFoundException {
        File file = new File(HISTORY_PATH);
        if (!file.exists()) {
            System.err.println("History file not found");
            return;
        }
        Scanner sc = new Scanner(new FileReader(file));
        while (sc.hasNextLine()) {
            historyList.add(sc.nextLine());
        }
        sc.close();
    }

    /**
     * Save history to file.
     */
    public void saveHistory() throws IOException {
        File file = new File(HISTORY_PATH);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        for (String s : historyList) {
            fileWriter.write(s + "\n");
        }
        fileWriter.close();
    }

    /**
     * Search for a prefix in history.
     * @param word Prefix to search for.
     * @return ArrayList of words that contain the prefix.
     */
    public List<String> search(String word) {
        List<String> result = new ArrayList<>();
        for (String s : historyList) {
            if (s.contains(word)) {
                result.add(s);
            }
        }
        return result;
    }

    /**
     * Add a word to history.
     * if the word is already in history, move it to the top.
     * @param target Word to add.
     */
    public void add(String target) {
        if (historyList.size() == LIMIT) {
            historyList.remove(LIMIT - 1);
        }
        historyList.remove(target);
        historyList.add(0, target);
    }

    /**
     * Remove a word from history.
     * @param word Word to remove.
     */
    public void remove(String word) {
        if (word == null || historyList.isEmpty()) {
            return;
        }
        historyList.remove(word);
    }

    /**
     * Clear history.
     */
    public void clear() {
        historyList.clear();
    }

    /**
     * Get the list of history.
     * @return list of history.
     */
    public List<String> getList() {
        return historyList;
    }
}
