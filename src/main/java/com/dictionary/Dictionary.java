package com.dictionary;

import java.util.ArrayList;

public class Dictionary {
    static ArrayList<Word> words = new ArrayList<>();

    public static int size() {
        return words.size();
    }
}