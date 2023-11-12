package com.task;

import com.dictionary.TextToSpeech;
import javafx.concurrent.Task;
import javafx.scene.media.MediaPlayer;

public class TextToSpeechTask extends Task<Void> {
    private String text;
    private String lang;

    public TextToSpeechTask(String text, String lang) {
        this.text = text;
        this.lang = lang;
    }

    @Override
    protected Void call() throws Exception {
        TextToSpeech.play(text, lang);
        return null;
    }
}
