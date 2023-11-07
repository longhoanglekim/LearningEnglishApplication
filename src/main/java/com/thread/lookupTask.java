package com.thread;

import com.dictionary.Word;
import javafx.concurrent.Task;

import static com.ui.View.dictionary;

public class lookupTask extends Task<Word> {
    private String word;

    public lookupTask(String word) {
        this.word = word;
    }

    @Override
    protected Word call() throws Exception {
        Word result = dictionary.lookup(word);
        if (isCancelled()) {
            return null;
        }
        return result;
    }
}
