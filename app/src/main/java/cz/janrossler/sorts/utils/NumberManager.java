package cz.janrossler.sorts.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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
        if(smallData.contains(session))
            smallData.edit().remove(session).apply();

        if(bigData.contains(session + "unsorted"))
            bigData.edit().remove(session + "unsorted").apply();

        if(bigData.contains(session + "sorted"))
            bigData.edit().remove(session + "sorted").apply();
    }

    public void createList(String session, int length, int min, int max){
        JSONArray numbers = new JSONArray();
        for(int i = 0; i < length; i++){
            int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
            numbers.put(randomNum);
        }

        bigData.edit().putString(session + "unsorted", numbers.toString()).apply();

        try{
            JSONObject _session = getSession(session);
            _session.put("length", length);
            _session.put("min", min);
            _session.put("max", max);
            _session.put("editable", false);
            smallData.edit().putString(session, _session.toString()).apply();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void pushList(String session, @NonNull List<Integer> list){
        int min = list.size() > 0 ? list.get(0) : 0;
        int max = 0;

        for(int i = 0; i < list.size(); i++){
            if(list.get(i) > max) max = list.get(i);
            if(list.get(i) < min) min = list.get(i);
        }

        bigData.edit().putString(session + "unsorted", Arrays.toString(list.toArray())).apply();

        try{
            JSONObject _session = getSession(session);
            _session.put("length", list.size());
            _session.put("min", min);
            _session.put("max", max);
            _session.put("editable", true);
            smallData.edit().putString(session, _session.toString()).apply();
        }catch (Exception e){
            e.printStackTrace();
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
                e.printStackTrace();
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
                for(int i = 0; i < unsorted.length(); i++)
                    if (unsorted.get(i) instanceof Integer)
                        numbers.add(unsorted.getInt(i));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return numbers;
    }

    public List<Integer> getSortedSessionList(String session){
        List<Integer> numbers = new ArrayList<>();
        if(bigData.contains(session + "sorted")){
            try{
                JSONArray unsorted = new JSONArray(bigData.getString(session + "sorted", "[]"));
                for(int i = 0; i < unsorted.length(); i++)
                    if (unsorted.get(i) instanceof Integer)
                        numbers.add(unsorted.getInt(i));
            }catch (Exception e){
                e.printStackTrace();
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
            e.printStackTrace();
        }

        return sessions;
    }
}
