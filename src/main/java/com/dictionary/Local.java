package com.dictionary;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Local extends Dictionary {
    private static final String VN_CHAR = "áàảãạăắằẳẵặâấầẩẫậđéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵđ";
    private static Trie words;
    /**
     * Initialize the dictionary.
     */

    @Override
    public boolean initialize() {
        words = new Trie();
        try {
            loadDictionary();
            size = Trie.getSize(words);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    private void loadDictionary() throws Exception {
        File dict;
        File irregular = null;
        Word newWord;
        try {
            dict = new File("./src/main/resources/data/dictionary.txt");
            irregular = new File("./src/main/resources/data/irregular.txt");
        } catch (Exception e) {
            throw new FileNotFoundException("Dictionary file not found.");
        }
        try {
            Scanner sc = new Scanner(new FileReader(dict));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] wap = line.split("/");
                String target = wap[0].substring(1);    //Remove the '@' char.
                target = target.substring(0, target.length() - 1); //Remove the last space (for some words to group)
                String pronounce;
                StringBuilder explain = new StringBuilder();
                Pattern pattern = Pattern.compile("[+*-=!\\w" + VN_CHAR + "].*");
                while (sc.hasNext(pattern)) {
                    String line_explain = sc.nextLine();
                    if (sc.hasNext("[+*-=!].*")) {
                        explain.append(line_explain).append("\n");
                    } else {
                        explain.append(line_explain);
                    }
                }
                sc.skip("\\R?");        //  Skip the empty line/'\n' character.
                if (wap.length == 1) {
                    pronounce = "";
                } else {
                    pronounce = '/' + wap[1] + '/';
                }
                newWord = new Word(target, pronounce, explain.toString());
                Trie.insert(words, newWord);
            }
            /*Scanner sc_irregular = new Scanner(new FileReader(irregular));
            while (sc_irregular.hasNextLine()) {
                String line = sc_irregular.nextLine();
                String[] wap = line.split("\\s");
                String target = wap[0];
                String irregular_form = wap[1] + " " + wap[2];
                if (Trie.lookup(words, target) != null) {
                    Trie.lookup(words, target).setIrregular(irregular_form);
                }
            }*/
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new Exception("Cannot read dictionary file.");
        }
    }

    public List<String> getAllWordsTarget() {
        return Trie.getAllWordsTarget(words);
    }

    /**
     * Search for a pre'suffix' word in the dictionary.
     *
     * @param word Word to search for.
     * @return ArrayList of words that start with the given string.
     */
    @Override
    public List<String> search(String word) {
        return Trie.search(words, word);
    }

    /**
     * Look up a word definition in the dictionary.
     *
     * @param word Word to look up.
     * @return Definition of the word.
     */
    @Override
    public Word lookup(String word) {
        Word result = Trie.lookup(words, word);
        if (result != null) {
            historyList.add(result.getTarget());
            return result;
        }
        return null;
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
    public void removeWord(String word) {

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
        FileChooser directoryChooser = new FileChooser();
        directoryChooser.setTitle("Export dictionary");
        directoryChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Document files", "*.txt"));
        directoryChooser.setInitialFileName("Beta_dictionary.txt");
        directoryChooser.setInitialDirectory(new File("./src/main/resources/data/"));

        File selectedDirectory = directoryChooser.showSaveDialog(null);
        if (selectedDirectory == null) return;

        try (FileWriter fileWriter = new FileWriter(selectedDirectory)) {
            for (Word word : Trie.getAllWords(words)) {
                fileWriter.write(word.toString());
                fileWriter.write("\n\n");
            }
            System.out.println("Exported dictionary to: " + selectedDirectory.getAbsolutePath());
        } catch (IOException e) {
            Logger.getLogger("Can not export dictionary. Caused by: " + e.getMessage());
        }
    }
}
