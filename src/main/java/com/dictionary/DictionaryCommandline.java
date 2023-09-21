package com.dictionary;

import java.util.*;

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

    public boolean guessWords() {
        Scanner sc = new Scanner(System.in);
        Set<Word> set = new HashSet<>();
        int count = 0;
        //random
        Random random = new Random();
        while(set.size() < 5) {
            int n = random.nextInt(Dictionary.size());
            set.add(Dictionary.words.get(n));
        }
        Iterator<Word> iterator = set.iterator();
        boolean shouldEnd = false;
        while (iterator.hasNext()) {
            Word element = iterator.next();
            System.out.print(element.getWord_explain() + " la: ");
            String guess = sc.nextLine();
            while (!guess.equals(element.getWord_target())) {
                count++;
                System.out.print("Sai co ket thuc?, y/n ");
                if(sc.nextLine().equals("y")) {
                    shouldEnd = true;
                    break;
                }
                System.out.print("Doan lai la: ");
                guess = sc.nextLine();
            }
            System.out.println("good!");
            if(shouldEnd) break;
        }
        System.out.println("ban dung: " + (count + 5) + " lan de hoan thanh");
        System.out.println("Choi tiep?, y/n");
        if(sc.nextLine().equals("y")) return true;
        return false;
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
            System.out.println("[6] Game");
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
                    sc.nextLine();
                    String lookup_word = sc.nextLine();
                    System.out.print("Tu ban muon tra cuu la: ");
                    System.out.println(DictionaryManagement.dictionaryLookup(lookup_word));
                    break;
                case 5:
                    System.out.println("Nhap tu ban muon tim:");
                    sc.nextLine();
                    String search_word = sc.nextLine();
                    ArrayList<Word> tmp = dictionarySearcher(search_word);
                    for (int i = 0; i < tmp.size(); i++) {
                        System.out.println(tmp.get(i).getWord_explain());
                    }
                    break;
                case 6:
                    while (guessWords());
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
