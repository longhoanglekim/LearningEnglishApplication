package com.dictionary;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class TextToSpeech {
    static MediaPlayer mediaPlayer;
    private static Media getMedia(String target, String lang) throws IOException {
        String targetModified = target.replaceAll(" ", "%20");
        targetModified = targetModified.replaceAll("\n", "%20");
        String urlStr =
                "https://translate.google.com/translate_tts?ie=UTF-8&q="
                + targetModified
                + "&tl=" + lang +"&client=tw-ob";
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        return new Media(urlStr);
    }

    public static void play(String text, String lang) throws IOException {
        Media media = getMedia(text, lang);
        stop();
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}