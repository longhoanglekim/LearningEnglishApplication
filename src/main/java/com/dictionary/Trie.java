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

    public static void remove(Trie root, String target) {
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

    public static ArrayList<String> getAllWordsTarget(Trie root) {
        ArrayList<String> result = new ArrayList<>();
        dfsTarget(root, result);
        return result;
    }

    public static ArrayList<Word> getAllWords(Trie root) {
        ArrayList<Word> result = new ArrayList<>();
        dfsWord(root, result);
        return result;
    }

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

    public static int getSize(Trie root) {
        ArrayList<String> result = new ArrayList<>();
        dfsTarget(root, result);
        return result.size();
    }
}
