package com.dictionary;

import java.util.ArrayList;
import java.util.Scanner;

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

    public void dictionaryAdvanced() {
        //perform ui
        while (true) {

            System.out.println("--------------Welcome to My Application---------------------");
            System.out.println("[0] Exit");
            System.out.println("[1] Add");
            System.out.println("[2] Remove");
            System.out.println("[3] Display");
            System.out.println("[4] Lookup");
            System.out.println("[5] Search");
            System.out.println("[6] Gane");
            System.out.println("[7] Import from file");
            System.out.println("[8] Export to file");
            System.out.println("Your action: ");

            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            switch (choice) {
                case 0:
                    System.out.println("Tam biet ban!");
                    break;
                case 1:
                    DictionaryManagement.insertFromCommandline();
                    break;
                case 2:
                    DictionaryManagement.deleteWord();
                    break;
                case 3:
                    showAllWords();
                    break;
                case 4:
                    System.out.println("Nhap tu ban muon tra cuu:");
                    String lookup_word = sc.nextLine();
                    DictionaryManagement.dictionaryLookup(lookup_word);
                    break;
                case 5:
                    System.out.println("Nhap tu ban muon tim:");
                    String search_word = sc.nextLine();
                    dictionarySearcher(search_word);
                    break;
                case 6:
                    ///chua co game
                    break;
                case 7:
                    DictionaryManagement.insertFromFile();
                    break;
                case 8:
                    DictionaryManagement.dictionaryExportToFile();
                    break;
                default:
                    System.out.println("Action not supported.");
            }
        }
    }
}
