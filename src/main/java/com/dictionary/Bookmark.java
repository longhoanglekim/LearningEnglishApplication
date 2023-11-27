package com.dictionary;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bookmark {
    private static final String BOOKMARK_PATH = "src/main/resources/data/txt/bookmark.txt";
    private final List<String> bookmarkList = new ArrayList<>();

    public Bookmark() {
        try {
            loadBookmark();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load bookmark from file.
     * @throws FileNotFoundException
     */
    public void loadBookmark() throws FileNotFoundException {
        File file = new File(BOOKMARK_PATH);
        if (!file.exists()) {
            System.err.println("Bookmark file not found");
            return;
        }
        Scanner sc = new Scanner(new FileReader(file));
        while (sc.hasNextLine()) {
            bookmarkList.add(sc.nextLine());
        }
    }

    /**
     * Save bookmark to file.
     * @throws IOException Save file error.
     */
    public void saveBookmark() throws IOException {
        File file = new File(BOOKMARK_PATH);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        for (String s : bookmarkList) {
            fileWriter.write(s + "\n");
        }
        fileWriter.close();
    }

    /**
     * Get the list of bookmark.
     * @return list of bookmark.
     */
    public List<String> getList() {
        return bookmarkList;
    }

    /**
     * Search for a prefix in bookmark.
     * @param word Word to search for.
     * @return ArrayList of words that contain the prefix.
     */
    public List<String> search(String word) {
        List<String> result = new ArrayList<>();
        for (String s : bookmarkList) {
            if (s.contains(word)) {
                result.add(s);
            }
        }
        return result;
    }

    /**
     * Add a word to bookmark.
     * @param target Word to add.
     */
    public void add(String target) {
        bookmarkList.add(target);
    }

    /**
     * Remove a word from bookmark.
     * @param target Word to remove.
     */
    public void remove(String target) {
        bookmarkList.remove(target);
    }

    /**
     * Clear bookmark.
     */
    public void clear() {
        bookmarkList.clear();
    }
}
