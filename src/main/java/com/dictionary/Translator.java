package com.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Translator {


    /**
     * Translate text from vi to en.
     * @param text vi to translate.
     * @return the translation text in en.
     * @throws IOException .
     */
    public static String translateViToEn(String text) throws IOException {
        return translate("vi", "en", text);
    }

    /**
     * Translate text from en to vi.
     * @param text en to translate.
     * @return the translation text in vi.
     * @throws IOException dfs.
     */
    public static String translateEnToVi(String text) throws IOException {
        return translate("en", "vi", text);
    }


    /**
     * Translate text from langFrom to langTo.
     *
     * <p><a
     * href="https://stackoverflow.com/questions/8147284/how-to-use-google-translate-api-in-my-java-application">Reference</a>
     *
     * @param langFrom the input language (2 letters (ex: 'en'))
     * @param langTo   the output language (2 letters (ex: 'vi'))
     * @param text     the text to be translated
     * @return the translation text in langTo
     */
    private static String translate(String langFrom, String langTo, String text)
            throws IOException {
        String urlStr =
                "https://script.google.com/macros/s/AKfycby3AOWmhe32TgV9nW-Q0TyGOEyCHQeFiIn7hRgy5m8jHPaXDl2GdToyW_3Ys5MTbK6wjg/exec"
                        + "?q="
                        + URLEncoder.encode(text, StandardCharsets.UTF_8)
                        + "&target="
                        + langTo
                        + "&source="
                        + langFrom;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
