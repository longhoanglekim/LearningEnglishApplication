package com.dictionary;

import java.util.ArrayList;
import java.util.List;

public abstract class Dictionary {
    protected List<String> bookmarkList = new ArrayList<>();
    protected List<String> historyList = new ArrayList<>();


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
    public abstract Word lookUp(String word);

    /**
     * Add a word to the dictionary.
     * @param word Word to add.
     */
    public abstract void addWord(Word word);

    /**
     * Remove a word from the dictionary.
     * @param word Word to remove.
     */
    public abstract void removeWord(Word word);

    /**
     * Edit a word in the dictionary.
     * @param word Word to edit.
     */
    public abstract void editWord(Word word);

    /**
     * Export the dictionary to a file/database.
     */
    public abstract void export();

    /**
     *
     * @return list of bookmark.
     */
    public List<String> getBookmarkList() {
        return bookmarkList;
    }

    public List<String> searchBookmark(String word) {
        List<String> result = new ArrayList<>();
        for (String s : bookmarkList) {
            if (s.contains(word)) {
                result.add(s);
            }
        }
        return result;
    }

    public void addBookmarkWord(String target) {
        bookmarkList.add(target);
    }

    public void removeBookmarkWord(String word) {
        bookmarkList.remove(word);
    }

    public List<String> getHistoryList() {
        return historyList;
    }

    public List<String> searchHistory(String word) {
        List<String> result = new ArrayList<>();
        for (String s : historyList) {
            if (s.contains(word)) {
                result.add(s);
            }
        }
        return result;
    }

    public void addHistoryWord(String target) {
        if (historyList.contains(target)) {
            historyList.remove(target);
        }
        historyList.add(0, target);
    }

    public void removeHistoryWord(String word) {
        historyList.remove(word);
    }

    public void clearHistory() {
        historyList.clear();
    }

}
