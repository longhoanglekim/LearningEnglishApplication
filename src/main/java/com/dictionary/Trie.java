package com.dictionary;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Trie {
    boolean isEndOfWord;
    Map<Character, Trie> map;
    Word word;

    Trie() {
        isEndOfWord = false;
        map = new TreeMap<>();
        word = null;
    }

    /**
     * Insert a word to the trie. If the word already exists, append the definition.
     * @param root Root of the trie.
     * @param word Word to insert.
     * @see Trie#remove(Trie, String)
     */
    public static void insert(Trie root, Word word) {
        Trie tmp = root;
        String target = word.getTarget();
        //System.out.println(target);
        for (int i = 0; i < target.length(); i++) {
            char x = target.charAt(i);

            if (!tmp.map.containsKey(x)) {
                tmp.map.put(x, new Trie());
            }
            tmp = tmp.map.get(x);
        }

        if (tmp.isEndOfWord) {
            String tmpExplain = tmp.word.getExplain();
            tmp.word.setExplain(tmpExplain + "\n" + word.getExplain());
        } else {
            tmp.isEndOfWord = true;
            tmp.word = word;

        }
    }

    /**
     * Remove a word from the trie.
     * @param root Root of the trie.
     * @param target Word to remove.
     * @see Trie#insert(Trie, Word)
     */
    public static void remove(Trie root, String target) throws Exception {
        Trie tmp = root;
        for (int i = 0; i < target.length(); i++) {
            char x = target.charAt(i);

            if (!tmp.map.containsKey(x)) {
                return;
            }
            tmp = tmp.map.get(x);
        }
        tmp.isEndOfWord = false;
        tmp.word = null;
    }

    private static void dfsTarget(Trie root, ArrayList<String> result) {
        if (root.isEndOfWord) {
            result.add(root.word.getTarget());
        }
        for (Character x : root.map.keySet()) {
            dfsTarget(root.map.get(x), result);
        }
    }


    private static void dfsWord(Trie root, ArrayList<Word> res) {
        if (root.isEndOfWord) {
            res.add(root.word);
        }
        for (Character x : root.map.keySet()) {
            dfsWord(root.map.get(x), res);
        }
    }

    /**
     * Search for a pre'suffix' word in the trie.
     * @param root Root of the trie.
     * @param target Word to search for.
     * @return ArrayList of words that start with the given string.
     * @see Trie#getAllWords(Trie)
     * @see Trie#dfsTarget(Trie, ArrayList)
     * @see Trie#lookup(Trie, String)
     */
    public static ArrayList<String> search(Trie root, String target) {
        ArrayList<String> result = new ArrayList<>();
        Trie tmp = root;
        for (int i = 0; i < target.length(); i++) {
            char x = target.charAt(i);

            if (!tmp.map.containsKey(x)) {
                return null;
            }
            tmp = tmp.map.get(x);
        }
        if (tmp.isEndOfWord) result.add(tmp.word.getTarget());
        for (Character x : tmp.map.keySet()) {
            dfsTarget(tmp.map.get(x), result);
        }
        return result;
    }

    /**
     * Get all words's target in the trie.
     * @param root Root of the trie.
     * @return ArrayList of all words in the trie.
     * @see Trie#getAllWords(Trie)
     * @see Trie#dfsTarget(Trie, ArrayList)
     */
    public static ArrayList<String> getAllWordsTarget(Trie root) {
        ArrayList<String> result = new ArrayList<>();
        dfsTarget(root, result);
        return result;
    }

    /**
     * Get all words in the trie.
     * @param root Root of the trie.
     * @return ArrayList of all words in the trie.
     * @see Trie#getAllWordsTarget(Trie)
     * @see Trie#dfsWord(Trie, ArrayList)
     */
    public static ArrayList<Word> getAllWords(Trie root) {
        ArrayList<Word> result = new ArrayList<>();
        dfsWord(root, result);
        return result;
    }

    /**
     * Look up a word in the trie.
     * @param root Root of the trie.
     * @param target Word to look up.
     * @return Definition of the word.
     * @see Trie#search(Trie, String)
     */
    public static Word lookup(Trie root, String target) {
        if (target == null || target.isEmpty()) return null;
        Trie tmp = root;
        for (int i = 0; i < target.length(); i++) {
            char x = target.charAt(i);

            if (!tmp.map.containsKey(x)) {
                return null;
            }
            tmp = tmp.map.get(x);
        }

       if (tmp.isEndOfWord) return tmp.word;
       return null;
    }

    /**
     * Get size of the dictionary by dfs all the trie.
     * @param root Root of the trie.
     * @return Size of the trie.
     * @see Trie#dfsTarget(Trie, ArrayList)
     */
    public static int getSize(Trie root) {
        ArrayList<String> result = new ArrayList<>();
        dfsTarget(root, result);
        return result.size();
    }
}
