package com.dictionary;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class DictionaryManagement {
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

    public static String dictionaryLookup(String word) {
        for (int i = 0; i < Dictionary.words.size(); i++) {
            String target = Dictionary.words.get(i).getWord_target();
            if (target.equals(word)) {
                return Dictionary.words.get(i).getWord_explain();
            }
        }
        return "Not found";
    }

    public static void addWord() {
        Scanner sc = new Scanner(System.in);

        String target = sc.nextLine();
        String explain = sc.nextLine();

        Word newWord = new Word(target, explain);
        Dictionary.words.add(newWord);
    }


    public static void deleteWord() {
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
}
