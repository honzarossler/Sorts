package cz.janrossler.sorts.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class Theory {
    private static final String TITLE_TEXT = "title.text";

    private final HashMap<String,String> head = new HashMap<>();
    private final HashMap<String, JSONArray> body = new HashMap<>();

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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getTitleText(){
        return head.getOrDefault(TITLE_TEXT, "");
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
