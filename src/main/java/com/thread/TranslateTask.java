package com.thread;

import com.dictionary.Translator;
import javafx.concurrent.Task;

import java.io.IOException;
import java.net.ConnectException;

public class TranslateTask extends Task<String> {
    private String text;
    private String langFrom;
    private String langTo;

    public TranslateTask(String text, String langFrom, String langTo) {
        this.text = text;
        this.langFrom = langFrom;
        this.langTo = langTo;
    }

    @Override
    protected String call() throws Exception {
        String result = null;
        try {
            result = Translator.translate(langFrom, langTo, text);
        } catch (IOException e) {
            return null;
        }
        if (isCancelled()) {
            return null;
        }
        return result;
    }
}
