package com.commandline;

import java.util.*;


public class DictionaryCommandline {
    /**
     * Show all words in dictionary.
     * May be used for testing. May be removed later.
     */
    public void showAllWords() {
        System.out.println("No\t| English\t| Vietnamese");
        for (int i = 0; i < Dictionary.size(); i++) {
            Word word = Dictionary.words.get(i);
            String tabSpace = "\t\t";
            if (word.getWord_target().length() > 5) tabSpace = "\t";
            System.out.println(i + 1 + "\t| " + word.getWord_target() + tabSpace + "| " + word.getWord_explain());
        }
    }

    /**
     * Load and show all words in dictionary.
     * May be used for testing. May be removed later.
     */
    public void dictionaryBasic() {
        DictionaryManagement.insertFromCommandline();
        showAllWords();
    }

    /**
     * Search for words that start with a given string.
     * @param word String to search for.
     * @return List of words that start with the given string.
     */
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

    /**
     * Guess words game.
     * @return true if user wants to continue, false otherwise.
     */
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
        while (iterator.hasNext()) {
            Word element = iterator.next();
            System.out.println(element.getWord_explain() + " la: ");

            boolean isCorrect = showChoiceGame(element.getWord_target());
            if (!isCorrect) {
                System.out.print("Sai co ket thuc?, y/n ");
                if(sc.nextLine().equals("y")) {
                    break;
                }
                count++;
            }

        }
        System.out.println("ban dung: " + (count + 5) + " lan de hoan thanh");
        System.out.println("Choi tiep?, y/n");
        return sc.nextLine().equals("y");
    }

    /**
     * Perform dictionary advanced.
     * May be used for testing. May be removed later.
     */
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
            System.out.print("Your action: ");

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
                    System.out.print("Nhap tu ban muon tra cuu: ");
                    sc.nextLine();
                    String lookup_word = sc.nextLine();
                    Word word = DictionaryManagement.dictionaryLookup(lookup_word);
                    if (word == null) {
                        System.out.println("Từ không tồn tại!");
                    } else {
                        System.out.print("Từ bạn muốn tra cứu: ");
                        System.out.println();
                    }
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

    private boolean showChoiceGame(String answer) {
        Hashtable<Integer, String> hashtable = new Hashtable<>();
        int choice = new Random().nextInt(4);
        for (int i = 0; i < 4; i++) {
            if (i == choice) {
                hashtable.put(i + 1, answer);
            } else {
                int random = new Random().nextInt(Dictionary.words.size());
                while (Dictionary.words.get(random).getWord_target().equals(answer) || hashtable.containsValue(Dictionary.words.get(random).getWord_target())) {
                    random = new Random().nextInt(Dictionary.words.size());
                }
                hashtable.put(i + 1, Dictionary.words.get(random).getWord_target());
            }
        }
        for (int i = 0; i < 4; i++) {
            System.out.println((i + 1) + ". " + hashtable.get(i + 1));
        }
        System.out.print("Chon dap an [1/2/3/4]: ");
        int input = new Scanner(System.in).nextInt();
        if (input == choice + 1) {
            System.out.println("Dung");
            return true;
        } else {
            System.out.println("Sai vcl");
            return false;
        }
    }

}
