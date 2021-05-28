package cz.janrossler.sorts.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;

public class TheoryReader {
    private Context context;
    private AssetManager am;

    public TheoryReader(@NonNull Context context) {
        this.context = context;
        am = context.getAssets();
    }

    public boolean isTheoryAvailable(String path, String theoryFile){
        try {
            return Arrays.asList(am.list(path)).contains(theoryFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public JSONArray getAllTheories(){
        JSONArray theories = new JSONArray();

        try{
            JSONArray arr = Utilities.getSortAlgorithms(context);

            for(int i = 0; i < arr.length(); i++){
                JSONObject thr = arr.getJSONObject(i);
                if(thr.has("theory") && isTheoryAvailable("", thr.getString("theory"))){
                    JSONObject theory = new JSONObject();
                    theory.put("name", thr.getString("name"));
                    theory.put("theory", thr.getString("theory"));

                    theories.put(theory);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return theories;
    }

    public JSONObject getTheoryData(String theoryFile){
        JSONObject theory = new JSONObject();

        try{
            String rootFolder = "";

            String json;
            try {
                InputStream is = am.open(theoryFile);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                ex.printStackTrace();
                json = "{}";
            }

            theory = new JSONObject(json);

            if(theory.has("head")){
                JSONObject head = theory.getJSONObject("head");
                if(head.has("folder"))
                    rootFolder = head.getString("folder");
            }

            if(theory.has("body")){
                JSONObject body = theory.getJSONObject("body");

                Iterator<String> iter = body.keys();
                while(iter.hasNext()){
                    String key = iter.next();
                    JSONArray newPart = new JSONArray();
                    JSONArray part = body.getJSONArray(key);

                    for(int i = 0; i < part.length(); i++){
                        JSONObject obj = part.getJSONObject(i);
                        if(obj.has("text")){
                            String text = getTheoryText(obj.getString("text").replace("%folder%", rootFolder));
                            obj.put("text", text);
                        }

                        if(obj.has("image")){
                            String text = obj.getString("image").replace("%folder%", rootFolder);
                            obj.put("image", text);
                        }

                        newPart.put(obj);
                    }

                    body.put(key, newPart);
                }
                theory.put("body", body);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return theory;
    }

    private String getTheoryText(String path){
        String text;
        try {
            InputStream is = am.open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text = new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception e) {
            Log.w("TheoryReader", "assetExists failed: "+e.toString());
            text = "";
        }
        return text;
    }
}
