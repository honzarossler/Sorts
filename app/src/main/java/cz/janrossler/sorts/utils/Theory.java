package cz.janrossler.sorts.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Theory {
    private static final String TITLE_TEXT = "title.text";

    private final HashMap<String,Object> head = new HashMap<>();
    private final HashMap<String, JSONArray> body = new HashMap<>();
    private final HashMap<String, String> source = new HashMap<>();
    private boolean showSource = false;

    public Theory(Context context, String theory){
        TheoryReader reader = new TheoryReader(context);
        JSONObject data = reader.getTheoryData(theory);

        try{
            if(data.has("head")){
                JSONObject oHead = data.getJSONObject("head");

                if(oHead.has("title")){
                    JSONObject oTitle = oHead.getJSONObject("title");
                    if(oTitle.has("text")){
                        head.put(TITLE_TEXT, oTitle.getString("text"));
                    }
                }
            }
            if(data.has("body")){
                JSONObject oBody = data.getJSONObject("body");
                Iterator<String> keys = oBody.keys();
                while (keys.hasNext()){
                    String key = keys.next();
                    body.put(key, oBody.getJSONArray(key));
                }
            }

            if(data.has("source")){
                showSource = true;
                JSONObject sour = new JSONObject(data.getString("source"));
                Iterator<String> iter = sour.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    source.put(key, sour.getString(key));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getTitleText(){
        return Objects.requireNonNull(head.getOrDefault(TITLE_TEXT, "")).toString();
    }

    public JSONArray getDefaultContent(){
        return body.getOrDefault("_", new JSONArray());
    }

    public JSONArray getLocalizedContent(){
        return body.getOrDefault(Locale.getDefault().toString(), getDefaultContent());
    }

    public JSONArray getTranslatedContent(String lang){
        return body.getOrDefault(lang, getLocalizedContent());
    }

    public boolean sourceEnabled(){
        return showSource;
    }

    public String getDefaultSource(){
        return source.getOrDefault("_", "");
    }

    public String getLocalizedSource(){
        return source.getOrDefault(Locale.getDefault().toString(), getDefaultSource());
    }

    public String getTranslatedSource(String lang){
        return source.getOrDefault(lang, getLocalizedSource());
    }

    public String[] getAllSupportedLanguages(){
        JSONArray array = new JSONArray();

        for (Map.Entry<String, JSONArray> stringJSONArrayEntry : body.entrySet()) {
            array.put(stringJSONArrayEntry.getKey());
        }

        String[] langs = new String[array.length()];
        try {
            for(int i = 0; i < array.length(); i++)
                langs[i] = array.getString(i);
        }catch (Exception e){
            e.printStackTrace();
        }

        return langs;
    }
}
