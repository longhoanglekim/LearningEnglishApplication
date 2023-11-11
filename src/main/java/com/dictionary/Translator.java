package com.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Translator {


    /**
     * Translate text from vi to en.
     * @param text vi to translate.
     * @return the translation text in en.
     * @throws IOException .
     */
   /* public static String translateViToEn(String text) throws IOException {
        return splitJson(translate("vi", "en", text));
    }*/

    /**
     * Translate text from en to vi.
     * @param text en to translate.
     * @return the translation text in vi.
     * @throws IOException dfs.
     */
    /*public static String translateEnToVi(String text) throws IOException {
        return splitJson(translate("en", "vi", text));
    }*/


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
    public static String translate(String langFrom, String langTo, String text)
            throws IOException {
        String urlStr =
                "https://translate.googleapis.com/translate_a/single?client=gtx&sl="
                        + langFrom
                        + "&tl="
                        + langTo
                        + "&dt=t&q="
                        + URLEncoder.encode(text, StandardCharsets.UTF_8);
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
        return splitJson(response.toString());
    }

    public static String splitJson(String json) {

        JsonElement jsonElement = JsonParser.parseString(json);
        String res = "";
        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();

            // Lấy phần tử đầu tiên trong mảng JSON ban đầu
            if (!jsonArray.isEmpty()) {
                JsonArray innerArray1 = jsonArray.get(0).getAsJsonArray();

                // Lấy phần tử đầu tiên trong mảng JSON thứ 2
                while (!innerArray1.isEmpty()) {
                    JsonArray innerArray2 = innerArray1.get(0).getAsJsonArray();

                    // Lấy translate word ở index 0 của mảng JSON;
                    if (!innerArray2.isEmpty()) {
                        res += innerArray2.get(0).getAsString();
                    }
                    innerArray1.remove(0);
                }
            }
        }
        return res;
    }
}
