package com.dictionary;

import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Database extends Dictionary {
    private static Connection databaseLink;
    private static int numberOfWords;
    private static String databaseName = "dictionarybasic";
    private static String table = "en_vi";
    Trie words = new Trie();

    /**
     * Get database connection from a database.
     */
    public void getDatabaseConnection() throws Exception {
        String databaseUser = "root";
        String databasePassword = "lyhongduc123";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            Logger.getLogger(Database.class.getName()).warning("SQLException: " + e.getMessage());
            throw new Exception("Cannot connect to database.");
        }
    }

    @Override
    public boolean initialize() {
        try {
            getDatabaseConnection();
            List<String> allWords = getAllWordsTarget();
            for (String word : allWords) {
                Word newWord = new Word(word, "", "");
                Trie.insert(words, newWord);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
        return true;
    }

    public List<String> getAllWordsTarget() {
        ArrayList<String> result = new ArrayList<>();
        String query = "SELECT word FROM " + table + " ORDER BY word asc";
        try {
            Statement statement = databaseLink.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);
            while (queryOutput.next()) {
                String target = queryOutput.getString(1);
                result.add(target);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\nWhile getting all word's target");
        }
        numberOfWords = result.size();
        return result;
    }

    /**
     * Search for a pre'suffix' word in the dictionary.
     *
     * @param word Word to search for.
     * @return ArrayList of words that start with the given string.
     */
    @Override
    public List<String> search(String word) {
        ArrayList<String> result = Trie.search(words, word);
        return result;
    }

    /**
     * Look up a word definition in the dictionary.
     * @param word Word to look up.
     * @return Definition of the word / null if not found.
     */
    @Override
    public Word lookup(String word) {
        String query = "SELECT * FROM " + table + " WHERE word LIKE \"" + word + "\"";
        try {
            Statement statement = databaseLink.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);
            if (queryOutput.next()) {
                String target = queryOutput.getString(2);
                String pronounce = queryOutput.getString(3);
                String explain = queryOutput.getString(4);
                historyList.add(ACTION.DLOOKUP + target);
                return new Word(target, pronounce, explain);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\nWhile looking up word: " + word);
        }
        return null;
    }

    /**
     * Add a word to the database.
     * @param word Word to add.
     */
    @Override
    public void addWord(Word word) {
        if (word == null) return;
        if (lookup(word.getTarget()) != null) return;

        String query = "INSERT INTO " + table + " VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = databaseLink.prepareStatement(query);
            preparedStatement.setInt(1, ++numberOfWords);
            preparedStatement.setString(2, word.getTarget());
            preparedStatement.setString(3, word.getPronounce());
            preparedStatement.setString(4, word.getExplain());
            preparedStatement.executeUpdate();
            historyList.add(ACTION.DADD + word.getTarget());
            Trie.insert(words, word);
            System.out.println("Added word: " + word.getTarget());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Remove a word from the dictionary.
     *
     * @param word Word to remove.
     */
    @Override
    public void removeWord(String word) {
        String query = "DELETE FROM " + table + " WHERE word LIKE \"" + word + "\"";
        try {
            PreparedStatement preparedStatement = databaseLink.prepareStatement(query);
            preparedStatement.executeUpdate();
            historyList.add(ACTION.DREMOVE + word);
            Trie.remove(words, word);
            System.out.println("Deleted word: " + word);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Edit a word in the dictionary.
     *
     * @param word Word to edit.
     */
    @Override
    public void editWord(Word word) {
        String query = "UPDATE " + table + " SET word = ?, pronounce = ?, definition = ? WHERE word = ?";
        try {
            PreparedStatement preparedStatement = databaseLink.prepareStatement(query);
            preparedStatement.setString(1, word.getTarget());
            preparedStatement.setString(2, word.getPronounce());
            preparedStatement.setString(3, word.getExplain());
            preparedStatement.setString(4, word.getTarget());
            preparedStatement.executeUpdate();
            historyList.add(ACTION.DEDIT + word.getTarget());
            System.out.println("Edited word: " + word.getTarget());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Export the dictionary to a file/database.
     */
    @Override
    public void export(boolean defaultPath) {
        File selectedDirectory;
        if (!defaultPath) {
            FileChooser directoryChooser = new FileChooser();
            directoryChooser.setTitle("Export dictionary");
            directoryChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Document files", "*.txt"));
            directoryChooser.setInitialFileName("Beta_dictionary.txt");
            directoryChooser.setInitialDirectory(new File("./src/main/resources/data/"));

            selectedDirectory = directoryChooser.showSaveDialog(null);
            if (selectedDirectory == null) return;
        } else {
            selectedDirectory = new File("./src/main/resources/data/Beta_dictionary.txt");
        }

        List<Word> words = new ArrayList<>();
        String query = "SELECT * FROM " + table + " ORDER BY word asc";
        try {
            Statement statement = databaseLink.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);
            while (queryOutput.next()) {
                String target = queryOutput.getString("word");
                String pronounce = queryOutput.getString("pronounce");
                String explain = queryOutput.getString("detail");
                Word word = new Word(target, pronounce, explain);
                words.add(word);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\nCaused by: " + e.getCause()
                    + "\nWhile exporting dictionary");
        }

        try (FileWriter fileWriter = new FileWriter(selectedDirectory)) {
            for (Word word : words) {
                fileWriter.write(word.toString());
                fileWriter.write("\n\n");
            }
            System.out.println("Exported dictionary to: " + selectedDirectory.getAbsolutePath());
        } catch (IOException e) {
            Logger.getLogger("Can not export dictionary. Caused by: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        super.close();
        try {
            databaseLink.close();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage() + "\nWhile closing database connection");
        }
    }
}
