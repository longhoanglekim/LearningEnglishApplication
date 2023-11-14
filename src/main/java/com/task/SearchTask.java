package com.task;

import javafx.concurrent.Task;
import java.util.List;

import static com.ui.Model.dictionary;

public class SearchTask extends Task<List<String>> {
    private final String word;

    public SearchTask(String word) {
        this.word = word;
    }

    @Override
    protected List<String> call() {
        List<String> result = dictionary.search(word);
        if (isCancelled()) {
            return null;
        }
        System.out.println(result.size());
        return result;
    }
}
