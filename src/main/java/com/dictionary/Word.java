package com.dictionary;

public class Word {
    private String target = "";
    private String pronounce = "";
    private String explain = "";

    public Word(String target, String pronounce, String explain) {
        this.target = target;
        this.pronounce = pronounce;
        this.explain = explain;
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
}
