package com.thread;

import javafx.concurrent.Task;

public class translateTask extends Task<String> {
    private String text;
    private String langFrom;
    private String langTo;

    public translateTask(String text, String langFrom, String langTo) {
        this.text = text;
        this.langFrom = langFrom;
        this.langTo = langTo;
    }

    @Override
    protected String call() throws Exception {

        return null;
    }
}
