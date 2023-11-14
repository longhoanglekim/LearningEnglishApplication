package com.task;

import com.dictionary.Translator;
import javafx.concurrent.Task;

import java.io.IOException;

public class TranslateTask extends Task<String> {
    private final String text;
    private final String langFrom;
    private final String langTo;

    public TranslateTask(String text, String langFrom, String langTo) {
        this.text = text;
        this.langFrom = langFrom;
        this.langTo = langTo;
    }

    @Override
    protected String call() {
        String result;
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
