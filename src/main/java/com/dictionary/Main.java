package com.dictionary;

public class Main {
    public static void main(String[] args) {
        DictionaryCommandline dic = new DictionaryCommandline();
        DictionaryManagement.insertFromFile();
        dic.dictionaryBasic();
    }
}
