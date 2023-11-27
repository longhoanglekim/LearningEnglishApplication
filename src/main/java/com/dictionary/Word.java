package com.dictionary;

import java.util.ArrayList;
import java.util.Scanner;

public class Word {
    private String target;
    private String pronounce;
    private String explain;
    private ArrayList<String> definition = new ArrayList<>();

    /**
     * Constructor.
     * @param target Word to add.
     * @param pronounce Pronunciation of the word.
     * @param explain Definition of the word.
     */
    public Word(String target, String pronounce, String explain) {
        this.target = target;
        this.pronounce = pronounce;
        this.explain = explain;
        beautifyWord();
    }

    /**
     * Modify the definition of the word to make it more readable when rendering.
     * Define '-' as a block/meaning.
     * Define '=' as a example in 2 language.
     * Define '!' as a example of use.
     * Define '*' as a part of speech.
     * Define '|' as none type special.
     */
    private void beautifyWord() {
        try {
            Scanner sc = new Scanner(explain);
            String s;
            int indexBlock = 1;
            while (sc.hasNextLine()) {
                s = sc.nextLine();
                char firstChar = s.charAt(0);
                if (firstChar == '-') {
                    s = "-" + indexBlock++ + ". " + s.substring(1);
                } else if (firstChar == '=') {
                    String[] tmp = s.split("\\+");
                    s = "=٠ " + tmp[0].substring(1);
                    s += (tmp.length > 1) ? "\n " + tmp[1] : "";
                } else if (firstChar == '!') {
                    s = "!- " + s.substring(1);
                } else if (firstChar != '*') {
                    s = "|" + s;
                }
                definition.add(s);
            }
        } catch (NullPointerException e) {
            definition.add("");
        }
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getPronounce() {
        return pronounce;
    }

    public void setPronounce(String pronounce) {
        this.pronounce = pronounce;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public ArrayList<String> getDefinition() {
        return definition;
    }

    @Override
    public String toString() {
        return "@" + target + " " + pronounce + "\n" + explain;
    }
}
