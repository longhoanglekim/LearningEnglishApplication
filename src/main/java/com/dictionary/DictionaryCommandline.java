package com.dictionary;

import java.util.ArrayList;

public class DictionaryCommandline {
    public void showAllWords() {
        System.out.println("No\t| English\t| Vietnamese");
        for (int i = 0; i < Dictionary.size(); i++) {
            Word word = Dictionary.words.get(i);
            String tabSpace = "\t\t";
            if (word.getWord_target().length() > 5) tabSpace = "\t";
            System.out.println(i + 1 + "\t| " + word.getWord_target() + tabSpace + "| " + word.getWord_explain());
        }
    }

    public void dictionaryBasic() {
        DictionaryManagement.insertFromCommandline();
        showAllWords();
    }

    public ArrayList<Word> dictionarySearcher(String word) {
        ArrayList<Word> result = new ArrayList<>();
        for (int i = 0; i < Dictionary.size(); i++) {
            String target = Dictionary.words.get(i).getWord_target();
            if (target.startsWith(word)) {
                result.add(Dictionary.words.get(i));
            }
        }
        return result;
    }
}
