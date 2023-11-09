package com.thread;

import javafx.concurrent.Task;
import java.util.List;

import static com.ui.Model.dictionary;

public class searchTask extends Task<List<String>> {
    private String word;

    public searchTask(String word) {
        this.word = word;
    }

    @Override
    protected List<String> call() throws Exception {
        List<String> result = dictionary.search(word);
        if (isCancelled()) {
            return null;
        }
        System.out.println(result.size());
        return result;
    }
}
