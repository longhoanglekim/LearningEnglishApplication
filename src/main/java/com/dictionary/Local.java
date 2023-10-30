package com.dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Local extends Dictionary {
    private HashMap<String, Word> words;
    /**
     * Initialize the dictionary.
     */
    @Override
    public boolean initialize() {
        words = new HashMap<>();
        try {
            loadDictionary();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            //Logger.getLogger(Local.class.getName()).warning(e.getMessage());
            return false;
        }
        return true;
    }

    private void loadDictionary() throws Exception {
        File file = null;
        try {
            file = new File("./src/main/resources/data/dictionary.txt");
        } catch (Exception e) {
            throw new FileNotFoundException("Dictionary file not found.");
        }

        try {
            Scanner sc = new Scanner(new FileReader(file));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.startsWith("@")) {
                    line = line.substring(1); // remove the char '@'.
                }
                String[] wap = line.split("/");
                //System.out.println(wap[0] + " " + wap[1]);
                StringBuilder explain = new StringBuilder();
                while (sc.hasNextLine()) {
                    String line_explain = sc.nextLine();
                    if (line_explain.equals("")) {
                        break;
                    }
                    explain.append(line_explain + "\n");
                }
                if (wap.length == 1) {
                    wap = new String[2];
                    wap[0] = line;
                    wap[1] = "";
                } else {
                    wap[1] = "/" + wap[1] + "/";
                }
                Word newWord = new Word(wap[0], wap[1], explain.toString());
                words.put(newWord.getTarget(), newWord);
            }
        } catch (Exception e) {
            throw new Exception("Cannot read dictionary file.");
        }
    }

    /**
     * Get all words in the dictionary.
     *
     * @return ArrayList of all words in the dictionary.
     */
    @Override
    public List<Word> getAllWords() {
        return words.values().stream().toList();
    }

    public List<String> getAllWordsTarget() {
        return words.keySet().stream().toList();
    }

    /**
     * Search for a pre'suffix' word in the dictionary.
     *
     * @param word Word to search for.
     * @return ArrayList of words that start with the given string.
     */
    @Override
    public List<Word> search(String word) {
        return null;
    }

    /**
     * Look up a word definition in the dictionary.
     *
     * @param word Word to look up.
     * @return Definition of the word.
     */
    @Override
    public Word lookUp(String word) {
        return words.get(word);
    }

    /**
     * Add a word to the dictionary.
     *
     * @param word Word to add.
     */
    @Override
    public void addWord(Word word) {

    }

    /**
     * Remove a word from the dictionary.
     *
     * @param word Word to remove.
     */
    @Override
    public void removeWord(Word word) {

    }

    /**
     * Edit a word in the dictionary.
     *
     * @param word Word to edit.
     */
    @Override
    public void editWord(Word word) {

    }

    /**
     * Export the dictionary to a file/database.
     */
    @Override
    public void export() {

    }
}
