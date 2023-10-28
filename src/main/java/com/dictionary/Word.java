package com.dictionary;

import java.util.ArrayList;

public class Word {
    private String target;
    private String pronounce;
    private String explain;
    private ArrayList<String> definition = new ArrayList<>();

    public Word(String target, String pronounce, String explain) {
        this.target = target;
        this.pronounce = pronounce;
        this.explain = explain;
        beautifyDefinition();
    }

    private void beautifyDefinition() {
        String[] temp = explain.split("<br/>");
        for (String s : temp) {
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
}
