package com.dictionary;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Manage dictionary.
 */
public class DictionaryManagement {

    /**
     * Insert words from command line to dictionary.
     */
    public static void insertFromCommandline() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Number of words to insert: ");

        int n = sc.nextInt();
        sc.nextLine(); // Remove enter

        for (int i = 0; i < n; i++) {
            String target = sc.nextLine();
            String explain = sc.nextLine();
            Word newWord = new Word(target, explain);
            Dictionary.words.add(newWord);
        }
    }

    /**
     * Insert words from file to dictionary.
     */
    public static void insertFromFile() {
        try {
            File file = new File("src/main/java/com/dictionary/dictionaries.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                String[] word = data.split(" ", 2);
                Word newWord = new Word(word[0], word[1]);
                Dictionary.words.add(newWord);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred. No such file or dictionary.");
            e.printStackTrace();
        }
    }

    /**
     * Look up a word in dictionary.
     * @param word Word to look up.
     * @return Word's explain if found, "Not found" otherwise.
     * @deprecated Use {@link DictionaryCommandline#dictionarySearcher(String)} instead.
     */
    public static String dictionaryLookup(String word) {
        for (int i = 0; i < Dictionary.words.size(); i++) {
            String target = Dictionary.words.get(i).getWord_target();
            if (target.equals(word)) {
                return Dictionary.words.get(i).getWord_explain();
            }
        }
        return "Not found";
    }

    /**
     * Add a word to dictionary.
     */
    public static void addWord() {
        Scanner sc = new Scanner(System.in);

        String target = sc.nextLine();
        String explain = sc.nextLine();

        Word newWord = new Word(target, explain);
        Dictionary.words.add(newWord);
    }

    /**
     * Delete a word from dictionary.
     */
    public static void deleteWord() {
        System.out.println("Nhap tu muon xoa:");
        Scanner sc = new Scanner(System.in);
        String target = sc.nextLine();
        for (int i = 0; i < Dictionary.size(); i++) {
            String word = Dictionary.words.get(i).getWord_explain();
            if (word.equals(target)) {
                Dictionary.words.remove(i);
                break;
            }
        }
    }

    /**
     * Export current local dictionary to txt file.
     */
    public static void dictionaryExportToFile() {
        File new_file = new File("src/main/java/com/dictionary/demo.txt");
        try {
            FileWriter writer = new FileWriter(new_file);
            for (Word i : Dictionary.words) {
                writer.write(i.getWord_target() + " " + i.getWord_explain());
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
