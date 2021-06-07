package cz.janrossler.sorts.utils;

import android.content.Context;

import org.json.JSONObject;

import java.util.Locale;

public class Language {
    private final JSONObject languages;
    private static Language instance;

    private Language(Context context){
        languages = Utilities.loadLanguagesFromAsset(context);
    }

    public static Language getInstance(Context context){
        if(instance == null){
            instance = new Language(context);
        }
        return instance;
    }

    public String getLanguage(String code){
        Locale l = Locale.getDefault();
        String lang = code;

        try {
            JSONObject data;
            String entry = "_";
            if (languages.has(l.toString())){
                entry = l.toString();
            }

            data = languages.getJSONObject(entry);
            if(data.has(code))
                lang = data.getString(code);
        }catch (Exception e){
            e.printStackTrace();
        }

        return lang;
    }
}
