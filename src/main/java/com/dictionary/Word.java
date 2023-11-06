package com.dictionary;

import java.util.ArrayList;
import java.util.Scanner;

public class Word {
    private String target;
    private String pronounce;
    private String irregular;
    private String explain;
    private ArrayList<String> definition = new ArrayList<>();

    public Word(String target, String pronounce, String explain) {
        this.target = target;
        this.pronounce = pronounce;
        this.explain = explain;
        //beautifyDefinition();
    }

    private void beautifyDefinition() {
        Scanner sc = new Scanner(explain);
        String s;
        int indexBlock = 1;
        while (sc.hasNextLine()) {
            s = sc.nextLine();
            if (s.charAt(0) == '*') {
                s = s.toUpperCase();
            } else if (s.charAt(0) == '-') {
                s = indexBlock++ + ". " + s.substring(1);
            } else if (s.charAt(0) == '=') {
                String[] tmp = s.split("\\+");
                s = tmp[0] + ":" + tmp[1];
            }
            definition.add(s);
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

    public void setDefinition(ArrayList<String> definition) {
        this.definition = definition;
    }



    public String getIrregular() {
        return irregular;
    }

    public void setIrregular(String irregular) {
        this.irregular = irregular;
    }

    @Override
    public String toString() {
        return "@" + target + pronounce + "\n" + explain;
    }
}
