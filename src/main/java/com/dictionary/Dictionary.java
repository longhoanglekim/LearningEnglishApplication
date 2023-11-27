package com.dictionary;

import java.io.*;
import java.util.List;

public abstract class Dictionary {
    protected int size;
    protected Bookmark bookmarkList = new Bookmark();
    protected History historyList = new History();


    /**
     * Initialize the dictionary.
     */
    public abstract boolean initialize();


    /**
     * Get all words in the dictionary.
     * @return ArrayList of STRING WORD in the dictionary.
     */
    public abstract List<String> getAllWordsTarget();

    /**
     * Search for a pre'suffix' word in the dictionary.
     * @param word Word to search for.
     * @return ArrayList of words that start with the given string.
     */
    public abstract List<String> search(String word);

    /**
     * Look up a word definition in the dictionary.
     * @param word Word to look up.
     * @return Definition of the word.
     */
    public abstract Word lookup(String word);

    /**
     * Add a word to the dictionary.
     * @param word Word to add.
     */
    public abstract void addWord(Word word);

    /**
     * Remove a word from the dictionary.
     * @param word Word to remove.
     */
    public abstract void removeWord(String word);

    /**
     * Edit a word in the dictionary.
     * @param word Word to edit.
     */
    public abstract void editWord(Word word);

    /**
     * Export the dictionary to a file/database.
     */
    public abstract void export(boolean defaultPath);

    /**
     * Get the list of bookmark.
     * @return list of bookmark.
     */
    public Bookmark getBookmarkList() {
        return bookmarkList;
    }

    /**
     * Get the list of history.
     * @return list of history.
     */
    public History getHistoryList() {
        return historyList;
    }

    /**
     * Get the size of the dictionary.
     * @return size of the dictionary.
     */
    public int getSize() {
        return size;
    }

    public void close() {
        try {
            bookmarkList.saveBookmark();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Cannot save bookmark");
        }
        try {
            historyList.saveHistory();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Cannot save history");
        }
    }
}
