package com.dictionary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Database extends Dictionary {
    private Connection databaseLink;

    /**
     * Get database connection from a database.
     * @return database link.
     */
    public Connection getDatabaseConnection() throws Exception {
        String databaseName = "dictionary";
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
        return databaseLink;
    }

    /**
     * Close database connection.
     */
    public void closeDatabaseConnection() {
        try {
            databaseLink.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean initialize() {
        try {
            getDatabaseConnection();
            boolean isInitialized = true;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<String> getAllWordsTarget() {
        ArrayList<String> result = new ArrayList<>();
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
        ArrayList<String> result = new ArrayList<>();
        String query = "SELECT word FROM tbl_edict WHERE word regexp'^" + word + ".*' ORDER BY word asc";
        try {
            Statement statement = databaseLink.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);
            while (queryOutput.next()) {
                String target = queryOutput.getString(1);
                result.add(target);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Look up a word definition in the dictionary.
     * @param word Word to look up.
     * @return Definition of the word / null if not found.
     */
    @Override
    public Word lookup(String word) {
        String query = "SELECT * FROM tbl_edict WHERE word = '" + word + "'";
        try {
            Statement statement = databaseLink.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);
            while (queryOutput.next()) {
                String target = queryOutput.getString("word");
                String pronounce = queryOutput.getString("pronounce");
                String explain = queryOutput.getString("detail");
                return new Word(target, pronounce, explain);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        if (lookup(word.getTarget()) == null) return;

        String query = "INSERT INTO tbl_edict(word, pronounce, detail) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = databaseLink.prepareStatement(query);
            preparedStatement.setString(1, word.getTarget());
            preparedStatement.setString(2, word.getPronounce());
            preparedStatement.setString(3, word.getExplain());
            preparedStatement.executeUpdate();
            System.out.println("Added word: " + word.getTarget());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Remove a word from the dictionary.
     *
     * @param word Word to remove.
     */
    @Override
    public void removeWord(String word) {
        String query = "DELETE FROM tbl_edict WHERE word = '" + word + "'";
        try {
            PreparedStatement preparedStatement = databaseLink.prepareStatement(query);
            preparedStatement.executeUpdate();
            System.out.println("Deleted word: " + word);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
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
        String query = "SELECT * FROM tbl_edict";

    }

}
