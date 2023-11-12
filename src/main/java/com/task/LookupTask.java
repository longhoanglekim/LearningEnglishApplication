package com.task;

import com.dictionary.Word;
import javafx.concurrent.Task;

import static com.ui.Model.dictionary;

public class LookupTask extends Task<Word> {
    private String word;

    public LookupTask(String word) {
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
