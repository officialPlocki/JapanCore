package org.japanbuild.japancore.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.okhttp.*;
import org.japanbuild.japancore.JapanCore;
import org.japanbuild.japancore.util.ez.Row;
import org.japanbuild.japancore.util.ez.Table;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public record AzureTranslate(String key) {

    public AzureTranslate(String key) {
        this.key = key;
        JapanCore.getDatabase().addTable(new Table("translations")
                .idColumn()
                .column("language", String.class)
                .column("translationKey", String.class)
                .column("translation", String.class));
    }

    public String getTranslation(JapanPlayer player, String key) {
        Lang lang = player.getLanguage();
        Row row = JapanCore.getDatabase().selectSingleRow("SELECT translation FROM translations WHERE translationKey = ? AND language = ?", key, lang);
        if (row == null) {
            return "LanguageKey doesn't exist.";
        }
        return row.get("translation");
    }

    public String getTranslation(Lang lang, String key, String... replacements) {
        Row row = JapanCore.getDatabase().selectSingleRow("SELECT translation FROM translations WHERE translationKey = ? AND language = ?", key, lang);
        if (row == null) {
            return "LanguageKey doesn't exist.";
        }
        String s = row.get("translation");
        for(String replacement : replacements) {
            s = s.replaceFirst("%r", replacement);
        }
        return s;
    }

    public void registerTranslation(String key, String germanTranslation) {
        try {
            if(getTranslation(Lang.DE, key).equalsIgnoreCase("LanguageKey doesn't exist.")) {
                HashMap<Lang, String> translations = getAzureTranslations(germanTranslation);
                translations.forEach((lang, translation) -> {
                    JapanCore.getDatabase().insert("translations", new Row()
                            .with("language", lang.name())
                            .with("translationKey", key)
                            .with("translation", translation));
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String translateSingleTime(String message, Lang lang, String... replacements) {
        try {
            String string = getAzureTranslation(lang, message);
            for(String replacement : replacements) {
                string = string.replaceFirst("%r", replacement);
            }
            return string;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "An error occurred";
    }

    /**
     * It takes a string and a language code as input, and returns the translated string
     *
     * @param lang The language to translate to.
     * @param text The text to translate.
     * @return The translated text.
     */
    private String getAzureTranslation(Lang lang, String text) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.cognitive.microsofttranslator.com")
                .addPathSegment("/translate")
                .addQueryParameter("api-version", "3.0")
                .addQueryParameter("to", lang.name())
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "[{\"Text\": \"" + text + "\"}]");
        Request request = new Request.Builder().url(url).post(body)
                .addHeader("Ocp-Apim-Subscription-Key", key)
                .addHeader("Ocp-Apim-Subscription-Region", "germanywestcentral")
                .addHeader("Content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        String s = prettify(replaceLast(response.body().string().replaceFirst("\\[", "")));
        JSONArray array = new JSONObject(s).getJSONArray("translations");
        return array.getJSONObject(0).getString("text");
    }

    /**
     * It takes a string and returns a map of languages to translations
     *
     * @param text The text to translate.
     * @return A HashMap with the translations of the text.
     */
    private HashMap<Lang, String> getAzureTranslations(String text) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.cognitive.microsofttranslator.com")
                .addPathSegment("/translate")
                .addQueryParameter("api-version", "3.0")
                .addQueryParameter("to", "ja")
                .addQueryParameter("to", "en")
                .addQueryParameter("to", "de")
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "[{\"Text\": \"" + text + "\"}]");
        Request request = new Request.Builder().url(url).post(body)
                .addHeader("Ocp-Apim-Subscription-Key", key)
                .addHeader("Ocp-Apim-Subscription-Region", "germanywestcentral")
                .addHeader("Content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        String s = prettify(replaceLast(response.body().string().replaceFirst("\\[", "")));
        JSONArray array = new JSONObject(s).getJSONArray("translations");
        HashMap<Lang, String> map = new HashMap<>();
        map.put(Lang.JA, array.getJSONObject(0).getString("text"));
        map.put(Lang.EN, array.getJSONObject(1).getString("text"));
        map.put(Lang.DE, array.getJSONObject(2).getString("text"));
        return map;
    }

    /**
     * Replace the last occurence of a pattern in a string
     *
     * @param text The text to be processed.
     * @return The text with the last square bracket removed.
     */
    private String replaceLast(String text) {
        return text.replaceFirst("(?s)" + "\\]" + "(?!.*?" + "\\]" + ")", "");
    }

    /**
     * It takes a string of JSON text and returns a string of prettified JSON text
     *
     * @param json_text The JSON string to prettify.
     * @return The prettified JSON string.
     */
    private String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(json_text);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }

}