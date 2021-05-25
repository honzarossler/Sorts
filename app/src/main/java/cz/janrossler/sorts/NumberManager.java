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
    private SharedPreferences data;
    private Context context;

    public NumberManager(@NonNull Context context){
        this.context = context;
        data = context.getSharedPreferences("sorting-sessions", Activity.MODE_PRIVATE);
    }

    public NumberManager createSession(String session, boolean bruteNew){
        if(!data.contains(session) || bruteNew){
            data.edit().putString(session, "").apply();
        }
        return this;
    }

    public void removeSession(String session){
        if(data.contains(session))
            data.edit().remove(session).apply();
    }

    public boolean isSessionEmpty(String session){
        if(data.contains(session))
            return data.getString(session, "").length() < 1;
        return false;
    }

    public void createList(String session, int length, int min, int max){
        if(isSessionEmpty(session)){
            JSONObject list = new JSONObject();
            try {
                JSONArray numbers = new JSONArray();
                for(int i = 0; i < length; i++){
                    int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
                    numbers.put(randomNum);
                }

                list.put("length", length);
                list.put("min", min);
                list.put("max", max);
                list.put("unsorted", numbers);

                data.edit().putString(session, list.toString()).apply();
            }catch (Exception e){
                e.fillInStackTrace();
            }
        }
    }

    public void saveSortingToSession(String _session, String algorithm, int lastTime, JSONArray sorted){
        if(data.contains(_session)){
            try{
                JSONObject session = getSession(_session);
                session.put("algorithm", algorithm);
                session.put("sorted", sorted);
                session.put("time", lastTime);

                data.edit().putString(_session, session.toString()).apply();
            }catch (Exception e){
                e.fillInStackTrace();
            }
        }
    }

    public JSONObject getSession(String _session){
        JSONObject session = new JSONObject();
        try {
            if (data.contains(_session)) {
                session = new JSONObject(data.getString(_session, "{}"));
                session.put("session", _session);
            }
        }catch (Exception ignored) {}
        return session;
    }

    public List<Integer> getUnsortedSessionList(String session){
        List<Integer> numbers = new ArrayList<>();
        try{
            if(!isSessionEmpty(session)){
                JSONObject list = new JSONObject(data.getString(session, "{}"));

                if(list.has("unsorted")){
                    JSONArray unsorted = list.getJSONArray("unsorted");
                    for(int i = 0; i < unsorted.length(); i++){
                        if(unsorted.get(i) instanceof Integer){
                            numbers.add(unsorted.getInt(i));
                        }
                    }
                }
            }
        }catch (Exception e){
            e.fillInStackTrace();
        }
        return numbers;
    }

    public List<Integer> getSortedSessionList(String session){
        List<Integer> numbers = new ArrayList<>();
        try{
            if(!isSessionEmpty(session)){
                JSONObject list = new JSONObject(data.getString(session, "{}"));

                if(list.has("sorted")){
                    JSONArray sorted = list.getJSONArray("sorted");
                    for(int i = 0; i < sorted.length(); i++){
                        if(sorted.get(i) instanceof Integer){
                            numbers.add(sorted.getInt(i));
                        }
                    }
                }
            }
        }catch (Exception e){
            e.fillInStackTrace();
        }
        return numbers;
    }

    public JSONArray getAllSessions(){
        JSONArray sessions = new JSONArray();

        try{
            Map<String,?> keys = data.getAll();

            for(Map.Entry<String,?> entry : keys.entrySet()){
                JSONObject session = new JSONObject(data.getString(entry.getKey(), "{}"));
                session.put("session", entry.getKey());

                if(session.has("sorted"))
                    session.remove("sorted");
                if(session.has("unsorted"))
                    session.remove("unsorted");

                sessions.put(session);
            }
        }catch (Exception e){
            e.fillInStackTrace();
        }

        return sessions;
    }
}
