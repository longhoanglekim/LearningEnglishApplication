package com.dictionary;

import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.*;
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
                String[] wap = line.split("/");
                String target = wap[0].substring(1);    //Remove the '@' char.
                String pronounce;
                StringBuilder explain = new StringBuilder();
                Pattern pattern = Pattern.compile("[+*-=!\\w" + VN_CHAR + "].*");
                while (sc.hasNext(pattern)) {
                    String line_explain = sc.nextLine();
                    if (sc.hasNext("[+*-=!].*")) {
                        explain.append(line_explain + "\n");
                    } else {
                        explain.append(line_explain);
                    }
                }
                sc.skip("\\R?");        //Skip the empty line.
                if (wap.length == 1) {
                    pronounce = "";
                } else {
                    pronounce = '/' + wap[1] + '/';
                }
                Word newWord = new Word(target, pronounce, explain.toString());
                //words.put(newWord.getTarget(), newWord);
                words.insert(words, newWord);
            }
        } catch (Exception e) {
            throw new Exception("Cannot read dictionary file.");
        }
    }

    public List<String> getAllWordsTarget() {
        return null;
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
    public Word lookUp(String word) {
        return Trie.lookup(words, word);
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
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        while (selectedDirectory == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while exporting dictionary.");
            alert.setContentText("Please choose a directory to export.");
            alert.showAndWait();
        }

        selectedDirectory = new File(selectedDirectory.getAbsolutePath() + "/dictionary.txt");
        ArrayList<Word> list = new ArrayList<>();

        Collections.sort(list, new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                return o1.getTarget().compareTo(o2.getTarget());
            }
        });
        try {
            if (selectedDirectory.createNewFile()) {
                FileWriter fileWriter = new FileWriter(selectedDirectory);
                for (Word word : list) {
                    fileWriter.write(word.toString());
                    fileWriter.write("\n");
                }
                fileWriter.close();
                System.out.println("Exported dictionary to " + selectedDirectory.getAbsolutePath());
            } else {

            }
        } catch (IOException e) {
            System.out.println("Error while exporting dictionary.");
            e.printStackTrace();
        }
    }
}
