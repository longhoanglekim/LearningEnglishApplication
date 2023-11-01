package com.dictionary;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
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
            //System.out.println("Duplicate word: " + word.getTarget());
        } else {
            tmp.isEndOfWord = true;
            tmp.word = word;
        }
    }

    private static void dfs(Trie root, ArrayList<String> result) {
        if (root.isEndOfWord) {
            result.add(root.word.getTarget());
        }
        for (Character x : root.map.keySet()) {
            dfs(root.map.get(x), result);
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
            dfs(tmp.map.get(x), result);
        }
        return result;
    }

    public static ArrayList<String> getAllWordsTarget(Trie root) {
        ArrayList<String> result = new ArrayList<>();
        dfs(root, result);
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
}
