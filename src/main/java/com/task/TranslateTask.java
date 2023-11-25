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
    protected String call() throws IOException {
        if (text == null || text.isEmpty()) {
            return null;
        }
        String result;
        try {
            result = Translator.translate(langFrom, langTo, text);
        } catch (IOException e) {
            throw new IOException("Error while translating");
        }
        if (isCancelled()) {
            return null;
        }
        return result;
    }
}
