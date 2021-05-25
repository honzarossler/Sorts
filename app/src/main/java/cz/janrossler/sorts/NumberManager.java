package cz.janrossler.sorts;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class NumberManager {
    private SharedPreferences smallData;
    private SharedPreferences bigData;
    private Context context;

    public NumberManager(@NonNull Context context){
        this.context = context;
        smallData = context.getSharedPreferences("sorting-sessions", Activity.MODE_PRIVATE);
        bigData = context.getSharedPreferences("sorting-lists", Activity.MODE_PRIVATE);
    }

    public NumberManager createSession(String session, boolean bruteNew){
        if(!smallData.contains(session) || bruteNew){
            smallData.edit().putString(session, "").apply();
        }
        return this;
    }

    public void removeSession(String session){
        if(smallData.contains(session)) {
            smallData.edit().remove(session).apply();
        }

        if(bigData.contains(session + "unsorted")) {
            bigData.edit().remove(session + "unsorted").apply();
        }

        if(bigData.contains(session + "sorted")) {
            bigData.edit().remove(session + "sorted").apply();
        }
    }

    public boolean isSessionEmpty(String session){
        if(smallData.contains(session))
            return smallData.getString(session, "").length() < 1;
        return false;
    }

    public void createList(String session, int length, int min, int max){
        JSONArray numbers = new JSONArray();
        for(int i = 0; i < length; i++){
            int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
            numbers.put(randomNum);
        }

        bigData.edit().putString(session + "unsorted", numbers.toString()).apply();

        if(smallData.contains(session)){
            try{
                JSONObject _session = getSession(session);
                _session.put("length", length);
                smallData.edit().putString(session, _session.toString()).apply();
            }catch (Exception e){
                e.fillInStackTrace();
            }
        }
    }

    public void saveSortingToSession(String _session, String algorithm, int lastTime, JSONArray sorted){
        if(smallData.contains(_session)){
            try{
                JSONObject session = getSession(_session);
                session.put("algorithm", algorithm);
                session.put("time", lastTime);

                smallData.edit().putString(_session, session.toString()).apply();
                bigData.edit().putString(_session + "sorted", sorted.toString()).apply();
            }catch (Exception e){
                e.fillInStackTrace();
            }
        }
    }

    public JSONObject getSession(String _session){
        JSONObject session = new JSONObject();
        try {
            if (smallData.contains(_session)) {
                session = new JSONObject(smallData.getString(_session, "{}"));
                session.put("session", _session);
            }
        }catch (Exception ignored) {}
        return session;
    }

    public List<Integer> getUnsortedSessionList(String session){
        List<Integer> numbers = new ArrayList<>();
        if(bigData.contains(session + "unsorted")){
            try{
                JSONArray unsorted = new JSONArray(bigData.getString(session + "unsorted", "[]"));
                for(int i = 0; i < unsorted.length(); i++){
                    if(unsorted.get(i) instanceof Integer){
                        numbers.add(unsorted.getInt(i));
                    }
                }
            }catch (Exception e){
                e.fillInStackTrace();
            }
        }
        return numbers;
    }

    public List<Integer> getSortedSessionList(String session){
        List<Integer> numbers = new ArrayList<>();
        if(bigData.contains(session + "sorted")){
            try{
                JSONArray unsorted = new JSONArray(bigData.getString(session + "sorted", "[]"));
                for(int i = 0; i < unsorted.length(); i++){
                    if(unsorted.get(i) instanceof Integer){
                        numbers.add(unsorted.getInt(i));
                    }
                }
            }catch (Exception e){
                e.fillInStackTrace();
            }
        }
        return numbers;
    }

    public JSONArray getAllSessions(){
        JSONArray sessions = new JSONArray();

        try{
            Map<String,?> keys = smallData.getAll();

            for(Map.Entry<String,?> entry : keys.entrySet()){
                JSONObject session = new JSONObject(smallData.getString(entry.getKey(), "{}"));
                session.put("session", entry.getKey());

                sessions.put(session);
            }
        }catch (Exception e){
            e.fillInStackTrace();
        }

        return sessions;
    }
}
